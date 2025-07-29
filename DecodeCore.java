/*     */ package de.maxhenkel.voicechat.concentus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DecodeCore
/*     */ {
/*     */   static void silk_decode_core(SilkChannelDecoder psDec, SilkDecoderControl psDecCtrl, short[] xq, int xq_ptr, short[] pulses) {
/*  50 */     int NLSF_interpolation_flag, lag = 0;
/*     */     
/*  52 */     short[] B_Q14 = psDecCtrl.LTPCoef_Q14;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     Inlines.OpusAssert((psDec.prev_gain_Q16 != 0));
/*     */     
/*  67 */     short[] sLTP = new short[psDec.ltp_mem_length];
/*  68 */     int[] sLTP_Q15 = new int[psDec.ltp_mem_length + psDec.frame_length];
/*  69 */     int[] res_Q14 = new int[psDec.subfr_length];
/*  70 */     int[] sLPC_Q14 = new int[psDec.subfr_length + 16];
/*     */     
/*  72 */     int offset_Q10 = SilkTables.silk_Quantization_Offsets_Q10[psDec.indices.signalType >> 1][psDec.indices.quantOffsetType];
/*     */     
/*  74 */     if (psDec.indices.NLSFInterpCoef_Q2 < 4) {
/*  75 */       NLSF_interpolation_flag = 1;
/*     */     } else {
/*  77 */       NLSF_interpolation_flag = 0;
/*     */     } 
/*     */ 
/*     */     
/*  81 */     int rand_seed = psDec.indices.Seed; int i;
/*  82 */     for (i = 0; i < psDec.frame_length; i++) {
/*  83 */       rand_seed = Inlines.silk_RAND(rand_seed);
/*  84 */       psDec.exc_Q14[i] = Inlines.silk_LSHIFT(pulses[i], 14);
/*  85 */       if (psDec.exc_Q14[i] > 0) {
/*  86 */         psDec.exc_Q14[i] = psDec.exc_Q14[i] - 1280;
/*     */       }
/*  88 */       else if (psDec.exc_Q14[i] < 0) {
/*  89 */         psDec.exc_Q14[i] = psDec.exc_Q14[i] + 1280;
/*     */       } 
/*     */       
/*  92 */       psDec.exc_Q14[i] = psDec.exc_Q14[i] + (offset_Q10 << 4);
/*  93 */       if (rand_seed < 0) {
/*  94 */         psDec.exc_Q14[i] = -psDec.exc_Q14[i];
/*     */       }
/*     */       
/*  97 */       rand_seed = Inlines.silk_ADD32_ovflw(rand_seed, pulses[i]);
/*     */     } 
/*     */ 
/*     */     
/* 101 */     System.arraycopy(psDec.sLPC_Q14_buf, 0, sLPC_Q14, 0, 16);
/*     */     
/* 103 */     int pexc_Q14 = 0;
/* 104 */     int pxq = xq_ptr;
/* 105 */     int sLTP_buf_idx = psDec.ltp_mem_length;
/*     */     
/* 107 */     for (int k = 0; k < psDec.nb_subfr; k++) {
/* 108 */       int gain_adj_Q16, pres_Q14[] = res_Q14;
/* 109 */       int pres_Q14_ptr = 0;
/* 110 */       short[] A_Q12 = psDecCtrl.PredCoef_Q12[k >> 1];
/* 111 */       int B_Q14_ptr = k * 5;
/* 112 */       int signalType = psDec.indices.signalType;
/*     */       
/* 114 */       int Gain_Q10 = Inlines.silk_RSHIFT(psDecCtrl.Gains_Q16[k], 6);
/* 115 */       int inv_gain_Q31 = Inlines.silk_INVERSE32_varQ(psDecCtrl.Gains_Q16[k], 47);
/*     */ 
/*     */       
/* 118 */       if (psDecCtrl.Gains_Q16[k] != psDec.prev_gain_Q16) {
/* 119 */         gain_adj_Q16 = Inlines.silk_DIV32_varQ(psDec.prev_gain_Q16, psDecCtrl.Gains_Q16[k], 16);
/*     */ 
/*     */         
/* 122 */         for (i = 0; i < 16; i++) {
/* 123 */           sLPC_Q14[i] = Inlines.silk_SMULWW(gain_adj_Q16, sLPC_Q14[i]);
/*     */         }
/*     */       } else {
/* 126 */         gain_adj_Q16 = 65536;
/*     */       } 
/*     */ 
/*     */       
/* 130 */       Inlines.OpusAssert((inv_gain_Q31 != 0));
/* 131 */       psDec.prev_gain_Q16 = psDecCtrl.Gains_Q16[k];
/*     */ 
/*     */       
/* 134 */       if (psDec.lossCnt != 0 && psDec.prevSignalType == 2 && psDec.indices.signalType != 2 && k < 2) {
/*     */ 
/*     */         
/* 137 */         Arrays.MemSetWithOffset(B_Q14, (short)0, B_Q14_ptr, 5);
/* 138 */         B_Q14[B_Q14_ptr + 2] = 4096;
/*     */         
/* 140 */         signalType = 2;
/* 141 */         psDecCtrl.pitchL[k] = psDec.lagPrev;
/*     */       } 
/*     */       
/* 144 */       if (signalType == 2) {
/*     */         
/* 146 */         lag = psDecCtrl.pitchL[k];
/*     */ 
/*     */         
/* 149 */         if (k == 0 || (k == 2 && NLSF_interpolation_flag != 0)) {
/*     */           
/* 151 */           int start_idx = psDec.ltp_mem_length - lag - psDec.LPC_order - 2;
/* 152 */           Inlines.OpusAssert((start_idx > 0));
/*     */           
/* 154 */           if (k == 2) {
/* 155 */             System.arraycopy(xq, xq_ptr, psDec.outBuf, psDec.ltp_mem_length, 2 * psDec.subfr_length);
/*     */           }
/*     */           
/* 158 */           Filters.silk_LPC_analysis_filter(sLTP, start_idx, psDec.outBuf, start_idx + k * psDec.subfr_length, A_Q12, 0, psDec.ltp_mem_length - start_idx, psDec.LPC_order);
/*     */ 
/*     */ 
/*     */           
/* 162 */           if (k == 0)
/*     */           {
/* 164 */             inv_gain_Q31 = Inlines.silk_LSHIFT(Inlines.silk_SMULWB(inv_gain_Q31, psDecCtrl.LTP_scale_Q14), 2);
/*     */           }
/* 166 */           for (i = 0; i < lag + 2; i++) {
/* 167 */             sLTP_Q15[sLTP_buf_idx - i - 1] = Inlines.silk_SMULWB(inv_gain_Q31, sLTP[psDec.ltp_mem_length - i - 1]);
/*     */           }
/* 169 */         } else if (gain_adj_Q16 != 65536) {
/* 170 */           for (i = 0; i < lag + 2; i++) {
/* 171 */             sLTP_Q15[sLTP_buf_idx - i - 1] = Inlines.silk_SMULWW(gain_adj_Q16, sLTP_Q15[sLTP_buf_idx - i - 1]);
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 177 */       if (signalType == 2) {
/*     */         
/* 179 */         int pred_lag_ptr = sLTP_buf_idx - lag + 2;
/* 180 */         for (i = 0; i < psDec.subfr_length; i++) {
/*     */ 
/*     */           
/* 183 */           int LTP_pred_Q13 = 2;
/* 184 */           LTP_pred_Q13 = Inlines.silk_SMLAWB(LTP_pred_Q13, sLTP_Q15[pred_lag_ptr], B_Q14[B_Q14_ptr]);
/* 185 */           LTP_pred_Q13 = Inlines.silk_SMLAWB(LTP_pred_Q13, sLTP_Q15[pred_lag_ptr - 1], B_Q14[B_Q14_ptr + 1]);
/* 186 */           LTP_pred_Q13 = Inlines.silk_SMLAWB(LTP_pred_Q13, sLTP_Q15[pred_lag_ptr - 2], B_Q14[B_Q14_ptr + 2]);
/* 187 */           LTP_pred_Q13 = Inlines.silk_SMLAWB(LTP_pred_Q13, sLTP_Q15[pred_lag_ptr - 3], B_Q14[B_Q14_ptr + 3]);
/* 188 */           LTP_pred_Q13 = Inlines.silk_SMLAWB(LTP_pred_Q13, sLTP_Q15[pred_lag_ptr - 4], B_Q14[B_Q14_ptr + 4]);
/* 189 */           pred_lag_ptr++;
/*     */ 
/*     */           
/* 192 */           pres_Q14[pres_Q14_ptr + i] = Inlines.silk_ADD_LSHIFT32(psDec.exc_Q14[pexc_Q14 + i], LTP_pred_Q13, 1);
/*     */ 
/*     */           
/* 195 */           sLTP_Q15[sLTP_buf_idx] = Inlines.silk_LSHIFT(pres_Q14[pres_Q14_ptr + i], 1);
/* 196 */           sLTP_buf_idx++;
/*     */         } 
/*     */       } else {
/* 199 */         pres_Q14 = psDec.exc_Q14;
/* 200 */         pres_Q14_ptr = pexc_Q14;
/*     */       } 
/*     */       
/* 203 */       for (i = 0; i < psDec.subfr_length; i++) {
/*     */         
/* 205 */         Inlines.OpusAssert((psDec.LPC_order == 10 || psDec.LPC_order == 16));
/*     */         
/* 207 */         int LPC_pred_Q10 = Inlines.silk_RSHIFT(psDec.LPC_order, 1);
/* 208 */         LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 1], A_Q12[0]);
/* 209 */         LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 2], A_Q12[1]);
/* 210 */         LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 3], A_Q12[2]);
/* 211 */         LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 4], A_Q12[3]);
/* 212 */         LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 5], A_Q12[4]);
/* 213 */         LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 6], A_Q12[5]);
/* 214 */         LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 7], A_Q12[6]);
/* 215 */         LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 8], A_Q12[7]);
/* 216 */         LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 9], A_Q12[8]);
/* 217 */         LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 10], A_Q12[9]);
/* 218 */         if (psDec.LPC_order == 16) {
/* 219 */           LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 11], A_Q12[10]);
/* 220 */           LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 12], A_Q12[11]);
/* 221 */           LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 13], A_Q12[12]);
/* 222 */           LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 14], A_Q12[13]);
/* 223 */           LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 15], A_Q12[14]);
/* 224 */           LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLPC_Q14[16 + i - 16], A_Q12[15]);
/*     */         } 
/*     */ 
/*     */         
/* 228 */         sLPC_Q14[16 + i] = Inlines.silk_ADD_LSHIFT32(pres_Q14[pres_Q14_ptr + i], LPC_pred_Q10, 4);
/*     */ 
/*     */         
/* 231 */         xq[pxq + i] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(Inlines.silk_SMULWW(sLPC_Q14[16 + i], Gain_Q10), 8));
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 237 */       System.arraycopy(sLPC_Q14, psDec.subfr_length, sLPC_Q14, 0, 16);
/* 238 */       pexc_Q14 += psDec.subfr_length;
/* 239 */       pxq += psDec.subfr_length;
/*     */     } 
/*     */ 
/*     */     
/* 243 */     System.arraycopy(sLPC_Q14, 0, psDec.sLPC_Q14_buf, 0, 16);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\DecodeCore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */