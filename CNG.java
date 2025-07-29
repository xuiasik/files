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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CNG
/*     */ {
/*     */   static void silk_CNG_exc(int[] exc_Q10, int exc_Q10_ptr, int[] exc_buf_Q14, int Gain_Q16, int length, BoxedValueInt rand_seed) {
/*  57 */     int exc_mask = 255;
/*     */     
/*  59 */     while (exc_mask > length) {
/*  60 */       exc_mask = Inlines.silk_RSHIFT(exc_mask, 1);
/*     */     }
/*     */     
/*  63 */     int seed = rand_seed.Val;
/*  64 */     for (int i = exc_Q10_ptr; i < exc_Q10_ptr + length; i++) {
/*  65 */       seed = Inlines.silk_RAND(seed);
/*  66 */       int idx = Inlines.silk_RSHIFT(seed, 24) & exc_mask;
/*  67 */       Inlines.OpusAssert((idx >= 0));
/*  68 */       Inlines.OpusAssert((idx <= 255));
/*  69 */       exc_Q10[i] = (short)Inlines.silk_SAT16(Inlines.silk_SMULWW(exc_buf_Q14[idx], Gain_Q16 >> 4));
/*     */     } 
/*     */     
/*  72 */     rand_seed.Val = seed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_CNG_Reset(SilkChannelDecoder psDec) {
/*  82 */     int NLSF_step_Q15 = Inlines.silk_DIV32_16(32767, (short)(psDec.LPC_order + 1));
/*  83 */     int NLSF_acc_Q15 = 0;
/*  84 */     for (int i = 0; i < psDec.LPC_order; i++) {
/*  85 */       NLSF_acc_Q15 += NLSF_step_Q15;
/*  86 */       psDec.sCNG.CNG_smth_NLSF_Q15[i] = (short)NLSF_acc_Q15;
/*     */     } 
/*  88 */     psDec.sCNG.CNG_smth_Gain_Q16 = 0;
/*  89 */     psDec.sCNG.rand_seed = 3176576;
/*     */   }
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
/*     */   static void silk_CNG(SilkChannelDecoder psDec, SilkDecoderControl psDecCtrl, short[] frame, int frame_ptr, int length) {
/* 107 */     short[] A_Q12 = new short[psDec.LPC_order];
/* 108 */     CNGState psCNG = psDec.sCNG;
/*     */     
/* 110 */     if (psDec.fs_kHz != psCNG.fs_kHz) {
/*     */       
/* 112 */       silk_CNG_Reset(psDec);
/*     */       
/* 114 */       psCNG.fs_kHz = psDec.fs_kHz;
/*     */     } 
/*     */     
/* 117 */     if (psDec.lossCnt == 0 && psDec.prevSignalType == 0) {
/*     */       int i;
/*     */ 
/*     */       
/* 121 */       for (i = 0; i < psDec.LPC_order; i++) {
/* 122 */         psCNG.CNG_smth_NLSF_Q15[i] = (short)(psCNG.CNG_smth_NLSF_Q15[i] + (short)Inlines.silk_SMULWB(psDec.prevNLSF_Q15[i] - psCNG.CNG_smth_NLSF_Q15[i], 16348));
/*     */       }
/*     */ 
/*     */       
/* 126 */       int max_Gain_Q16 = 0;
/* 127 */       int subfr = 0;
/* 128 */       for (i = 0; i < psDec.nb_subfr; i++) {
/* 129 */         if (psDecCtrl.Gains_Q16[i] > max_Gain_Q16) {
/* 130 */           max_Gain_Q16 = psDecCtrl.Gains_Q16[i];
/* 131 */           subfr = i;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 136 */       Arrays.MemMove(psCNG.CNG_exc_buf_Q14, 0, psDec.subfr_length, (psDec.nb_subfr - 1) * psDec.subfr_length);
/*     */ 
/*     */       
/* 139 */       for (i = 0; i < psDec.nb_subfr; i++) {
/* 140 */         psCNG.CNG_smth_Gain_Q16 += Inlines.silk_SMULWB(psDecCtrl.Gains_Q16[i] - psCNG.CNG_smth_Gain_Q16, 4634);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 145 */     if (psDec.lossCnt != 0) {
/* 146 */       int[] CNG_sig_Q10 = new int[length + 16];
/*     */ 
/*     */       
/* 149 */       int gain_Q16 = Inlines.silk_SMULWW(psDec.sPLC.randScale_Q14, psDec.sPLC.prevGain_Q16[1]);
/* 150 */       if (gain_Q16 >= 2097152 || psCNG.CNG_smth_Gain_Q16 > 8388608) {
/* 151 */         gain_Q16 = Inlines.silk_SMULTT(gain_Q16, gain_Q16);
/* 152 */         gain_Q16 = Inlines.silk_SUB_LSHIFT32(Inlines.silk_SMULTT(psCNG.CNG_smth_Gain_Q16, psCNG.CNG_smth_Gain_Q16), gain_Q16, 5);
/* 153 */         gain_Q16 = Inlines.silk_LSHIFT32(Inlines.silk_SQRT_APPROX(gain_Q16), 16);
/*     */       } else {
/* 155 */         gain_Q16 = Inlines.silk_SMULWW(gain_Q16, gain_Q16);
/* 156 */         gain_Q16 = Inlines.silk_SUB_LSHIFT32(Inlines.silk_SMULWW(psCNG.CNG_smth_Gain_Q16, psCNG.CNG_smth_Gain_Q16), gain_Q16, 5);
/* 157 */         gain_Q16 = Inlines.silk_LSHIFT32(Inlines.silk_SQRT_APPROX(gain_Q16), 8);
/*     */       } 
/*     */       
/* 160 */       BoxedValueInt boxed_rand_seed = new BoxedValueInt(psCNG.rand_seed);
/* 161 */       silk_CNG_exc(CNG_sig_Q10, 16, psCNG.CNG_exc_buf_Q14, gain_Q16, length, boxed_rand_seed);
/* 162 */       psCNG.rand_seed = boxed_rand_seed.Val;
/*     */ 
/*     */       
/* 165 */       NLSF.silk_NLSF2A(A_Q12, psCNG.CNG_smth_NLSF_Q15, psDec.LPC_order);
/*     */ 
/*     */       
/* 168 */       System.arraycopy(psCNG.CNG_synth_state, 0, CNG_sig_Q10, 0, 16);
/*     */       
/* 170 */       for (int i = 0; i < length; i++) {
/* 171 */         int lpci = 16 + i;
/* 172 */         Inlines.OpusAssert((psDec.LPC_order == 10 || psDec.LPC_order == 16));
/*     */         
/* 174 */         int sum_Q6 = Inlines.silk_RSHIFT(psDec.LPC_order, 1);
/* 175 */         sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 1], A_Q12[0]);
/* 176 */         sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 2], A_Q12[1]);
/* 177 */         sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 3], A_Q12[2]);
/* 178 */         sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 4], A_Q12[3]);
/* 179 */         sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 5], A_Q12[4]);
/* 180 */         sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 6], A_Q12[5]);
/* 181 */         sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 7], A_Q12[6]);
/* 182 */         sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 8], A_Q12[7]);
/* 183 */         sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 9], A_Q12[8]);
/* 184 */         sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 10], A_Q12[9]);
/*     */         
/* 186 */         if (psDec.LPC_order == 16) {
/* 187 */           sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 11], A_Q12[10]);
/* 188 */           sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 12], A_Q12[11]);
/* 189 */           sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 13], A_Q12[12]);
/* 190 */           sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 14], A_Q12[13]);
/* 191 */           sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 15], A_Q12[14]);
/* 192 */           sum_Q6 = Inlines.silk_SMLAWB(sum_Q6, CNG_sig_Q10[lpci - 16], A_Q12[15]);
/*     */         } 
/*     */ 
/*     */         
/* 196 */         CNG_sig_Q10[lpci] = Inlines.silk_ADD_LSHIFT(CNG_sig_Q10[lpci], sum_Q6, 4);
/*     */         
/* 198 */         frame[frame_ptr + i] = Inlines.silk_ADD_SAT16(frame[frame_ptr + i], (short)Inlines.silk_RSHIFT_ROUND(CNG_sig_Q10[lpci], 10));
/*     */       } 
/*     */       
/* 201 */       System.arraycopy(CNG_sig_Q10, length, psCNG.CNG_synth_state, 0, 16);
/*     */     } else {
/* 203 */       Arrays.MemSet(psCNG.CNG_synth_state, 0, psDec.LPC_order);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\CNG.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */