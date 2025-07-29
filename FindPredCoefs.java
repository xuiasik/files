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
/*     */ class FindPredCoefs
/*     */ {
/*     */   static void silk_find_pred_coefs(SilkChannelEncoder psEnc, SilkEncoderControl psEncCtrl, short[] res_pitch, short[] x, int x_ptr, int condCoding) {
/*  45 */     int minInvGain_Q30, invGains_Q16[] = new int[4];
/*  46 */     int[] local_gains = new int[4];
/*  47 */     int[] Wght_Q15 = new int[4];
/*  48 */     short[] NLSF_Q15 = new short[16];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     int[] LTP_corrs_rshift = new int[4];
/*     */ 
/*     */     
/*  56 */     int min_gain_Q16 = 33554431; int i;
/*  57 */     for (i = 0; i < psEnc.nb_subfr; i++) {
/*  58 */       min_gain_Q16 = Inlines.silk_min(min_gain_Q16, psEncCtrl.Gains_Q16[i]);
/*     */     }
/*  60 */     for (i = 0; i < psEnc.nb_subfr; i++) {
/*     */       
/*  62 */       Inlines.OpusAssert((psEncCtrl.Gains_Q16[i] > 0));
/*     */       
/*  64 */       invGains_Q16[i] = Inlines.silk_DIV32_varQ(min_gain_Q16, psEncCtrl.Gains_Q16[i], 14);
/*     */ 
/*     */       
/*  67 */       invGains_Q16[i] = Inlines.silk_max(invGains_Q16[i], 363);
/*     */ 
/*     */       
/*  70 */       Inlines.OpusAssert((invGains_Q16[i] == Inlines.silk_SAT16(invGains_Q16[i])));
/*  71 */       int tmp = Inlines.silk_SMULWB(invGains_Q16[i], invGains_Q16[i]);
/*  72 */       Wght_Q15[i] = Inlines.silk_RSHIFT(tmp, 1);
/*     */ 
/*     */       
/*  75 */       local_gains[i] = Inlines.silk_DIV32(65536, invGains_Q16[i]);
/*     */     } 
/*     */     
/*  78 */     short[] LPC_in_pre = new short[psEnc.nb_subfr * psEnc.predictLPCOrder + psEnc.frame_length];
/*  79 */     if (psEnc.indices.signalType == 2) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  89 */       Inlines.OpusAssert((psEnc.ltp_mem_length - psEnc.predictLPCOrder >= psEncCtrl.pitchL[0] + 2));
/*     */       
/*  91 */       int[] WLTP = new int[psEnc.nb_subfr * 5 * 5];
/*     */ 
/*     */       
/*  94 */       BoxedValueInt boxed_codgain = new BoxedValueInt(psEncCtrl.LTPredCodGain_Q7);
/*  95 */       FindLTP.silk_find_LTP(psEncCtrl.LTPCoef_Q14, WLTP, boxed_codgain, res_pitch, psEncCtrl.pitchL, Wght_Q15, psEnc.subfr_length, psEnc.nb_subfr, psEnc.ltp_mem_length, LTP_corrs_rshift);
/*     */ 
/*     */       
/*  98 */       psEncCtrl.LTPredCodGain_Q7 = boxed_codgain.Val;
/*     */ 
/*     */       
/* 101 */       BoxedValueByte boxed_periodicity = new BoxedValueByte(psEnc.indices.PERIndex);
/* 102 */       BoxedValueInt boxed_gain = new BoxedValueInt(psEnc.sum_log_gain_Q7);
/* 103 */       QuantizeLTPGains.silk_quant_LTP_gains(psEncCtrl.LTPCoef_Q14, psEnc.indices.LTPIndex, boxed_periodicity, boxed_gain, WLTP, psEnc.mu_LTP_Q9, psEnc.LTPQuantLowComplexity, psEnc.nb_subfr);
/*     */ 
/*     */       
/* 106 */       psEnc.indices.PERIndex = boxed_periodicity.Val;
/* 107 */       psEnc.sum_log_gain_Q7 = boxed_gain.Val;
/*     */ 
/*     */       
/* 110 */       LTPScaleControl.silk_LTP_scale_ctrl(psEnc, psEncCtrl, condCoding);
/*     */ 
/*     */       
/* 113 */       LTPAnalysisFilter.silk_LTP_analysis_filter(LPC_in_pre, x, x_ptr - psEnc.predictLPCOrder, psEncCtrl.LTPCoef_Q14, psEncCtrl.pitchL, invGains_Q16, psEnc.subfr_length, psEnc.nb_subfr, psEnc.predictLPCOrder);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 125 */       int x_ptr2 = x_ptr - psEnc.predictLPCOrder;
/* 126 */       int x_pre_ptr = 0;
/* 127 */       for (i = 0; i < psEnc.nb_subfr; i++) {
/* 128 */         Inlines.silk_scale_copy_vector16(LPC_in_pre, x_pre_ptr, x, x_ptr2, invGains_Q16[i], psEnc.subfr_length + psEnc.predictLPCOrder);
/*     */         
/* 130 */         x_pre_ptr += psEnc.subfr_length + psEnc.predictLPCOrder;
/* 131 */         x_ptr2 += psEnc.subfr_length;
/*     */       } 
/*     */       
/* 134 */       Arrays.MemSet(psEncCtrl.LTPCoef_Q14, (short)0, psEnc.nb_subfr * 5);
/* 135 */       psEncCtrl.LTPredCodGain_Q7 = 0;
/* 136 */       psEnc.sum_log_gain_Q7 = 0;
/*     */     } 
/*     */ 
/*     */     
/* 140 */     if (psEnc.first_frame_after_reset != 0) {
/* 141 */       minInvGain_Q30 = 10737418;
/*     */     } else {
/* 143 */       minInvGain_Q30 = Inlines.silk_log2lin(Inlines.silk_SMLAWB(2048, psEncCtrl.LTPredCodGain_Q7, 21845));
/*     */       
/* 145 */       minInvGain_Q30 = Inlines.silk_DIV32_varQ(minInvGain_Q30, 
/* 146 */           Inlines.silk_SMULWW(10000, 
/* 147 */             Inlines.silk_SMLAWB(65536, 196608, psEncCtrl.coding_quality_Q14)), 14);
/*     */     } 
/*     */ 
/*     */     
/* 151 */     FindLPC.silk_find_LPC(psEnc, NLSF_Q15, LPC_in_pre, minInvGain_Q30);
/*     */ 
/*     */     
/* 154 */     NLSF.silk_process_NLSFs(psEnc, psEncCtrl.PredCoef_Q12, NLSF_Q15, psEnc.prev_NLSFq_Q15);
/*     */ 
/*     */     
/* 157 */     ResidualEnergy.silk_residual_energy(psEncCtrl.ResNrg, psEncCtrl.ResNrgQ, LPC_in_pre, psEncCtrl.PredCoef_Q12, local_gains, psEnc.subfr_length, psEnc.nb_subfr, psEnc.predictLPCOrder);
/*     */ 
/*     */ 
/*     */     
/* 161 */     System.arraycopy(NLSF_Q15, 0, psEnc.prev_NLSFq_Q15, 0, 16);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\FindPredCoefs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */