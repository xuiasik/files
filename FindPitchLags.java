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
/*     */ class FindPitchLags
/*     */ {
/*     */   static void silk_find_pitch_lags(SilkChannelEncoder psEnc, SilkEncoderControl psEncCtrl, short[] res, short[] x, int x_ptr) {
/*  49 */     int[] auto_corr = new int[17];
/*  50 */     short[] rc_Q15 = new short[16];
/*  51 */     int[] A_Q24 = new int[16];
/*  52 */     short[] A_Q12 = new short[16];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     int buf_len = psEnc.la_pitch + psEnc.frame_length + psEnc.ltp_mem_length;
/*     */ 
/*     */     
/*  64 */     Inlines.OpusAssert((buf_len >= psEnc.pitch_LPC_win_length));
/*     */     
/*  66 */     int x_buf = x_ptr - psEnc.ltp_mem_length;
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
/*  77 */     short[] Wsig = new short[psEnc.pitch_LPC_win_length];
/*     */ 
/*     */     
/*  80 */     int x_buf_ptr = x_buf + buf_len - psEnc.pitch_LPC_win_length;
/*  81 */     int Wsig_ptr = 0;
/*  82 */     ApplySineWindow.silk_apply_sine_window(Wsig, Wsig_ptr, x, x_buf_ptr, 1, psEnc.la_pitch);
/*     */ 
/*     */     
/*  85 */     Wsig_ptr += psEnc.la_pitch;
/*  86 */     x_buf_ptr += psEnc.la_pitch;
/*  87 */     System.arraycopy(x, x_buf_ptr, Wsig, Wsig_ptr, psEnc.pitch_LPC_win_length - Inlines.silk_LSHIFT(psEnc.la_pitch, 1));
/*     */ 
/*     */     
/*  90 */     Wsig_ptr += psEnc.pitch_LPC_win_length - Inlines.silk_LSHIFT(psEnc.la_pitch, 1);
/*  91 */     x_buf_ptr += psEnc.pitch_LPC_win_length - Inlines.silk_LSHIFT(psEnc.la_pitch, 1);
/*  92 */     ApplySineWindow.silk_apply_sine_window(Wsig, Wsig_ptr, x, x_buf_ptr, 2, psEnc.la_pitch);
/*     */ 
/*     */     
/*  95 */     BoxedValueInt boxed_scale = new BoxedValueInt(0);
/*  96 */     Autocorrelation.silk_autocorr(auto_corr, boxed_scale, Wsig, psEnc.pitch_LPC_win_length, psEnc.pitchEstimationLPCOrder + 1);
/*  97 */     int scale = boxed_scale.Val;
/*     */ 
/*     */     
/* 100 */     auto_corr[0] = Inlines.silk_SMLAWB(auto_corr[0], auto_corr[0], 66) + 1;
/*     */ 
/*     */     
/* 103 */     int res_nrg = Schur.silk_schur(rc_Q15, auto_corr, psEnc.pitchEstimationLPCOrder);
/*     */ 
/*     */     
/* 106 */     psEncCtrl.predGain_Q16 = Inlines.silk_DIV32_varQ(auto_corr[0], Inlines.silk_max_int(res_nrg, 1), 16);
/*     */ 
/*     */     
/* 109 */     K2A.silk_k2a(A_Q24, rc_Q15, psEnc.pitchEstimationLPCOrder);
/*     */ 
/*     */     
/* 112 */     for (int i = 0; i < psEnc.pitchEstimationLPCOrder; i++) {
/* 113 */       A_Q12[i] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT(A_Q24[i], 12));
/*     */     }
/*     */ 
/*     */     
/* 117 */     BWExpander.silk_bwexpander(A_Q12, psEnc.pitchEstimationLPCOrder, 64881);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     Filters.silk_LPC_analysis_filter(res, 0, x, x_buf, A_Q12, 0, buf_len, psEnc.pitchEstimationLPCOrder);
/*     */     
/* 128 */     if (psEnc.indices.signalType != 0 && psEnc.first_frame_after_reset == 0) {
/*     */       
/* 130 */       int thrhld_Q13 = 4915;
/* 131 */       thrhld_Q13 = Inlines.silk_SMLABB(thrhld_Q13, -32, psEnc.pitchEstimationLPCOrder);
/* 132 */       thrhld_Q13 = Inlines.silk_SMLAWB(thrhld_Q13, -209714, psEnc.speech_activity_Q8);
/* 133 */       thrhld_Q13 = Inlines.silk_SMLABB(thrhld_Q13, -1228, Inlines.silk_RSHIFT(psEnc.prevSignalType, 1));
/* 134 */       thrhld_Q13 = Inlines.silk_SMLAWB(thrhld_Q13, -1637, psEnc.input_tilt_Q15);
/* 135 */       thrhld_Q13 = Inlines.silk_SAT16(thrhld_Q13);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 144 */       BoxedValueShort boxed_lagIndex = new BoxedValueShort(psEnc.indices.lagIndex);
/* 145 */       BoxedValueByte boxed_contourIndex = new BoxedValueByte(psEnc.indices.contourIndex);
/* 146 */       BoxedValueInt boxed_LTPcorr = new BoxedValueInt(psEnc.LTPCorr_Q15);
/* 147 */       if (PitchAnalysisCore.silk_pitch_analysis_core(res, psEncCtrl.pitchL, boxed_lagIndex, boxed_contourIndex, boxed_LTPcorr, psEnc.prevLag, psEnc.pitchEstimationThreshold_Q16, thrhld_Q13, psEnc.fs_kHz, psEnc.pitchEstimationComplexity, psEnc.nb_subfr) == 0) {
/*     */ 
/*     */         
/* 150 */         psEnc.indices.signalType = 2;
/*     */       } else {
/* 152 */         psEnc.indices.signalType = 1;
/*     */       } 
/*     */       
/* 155 */       psEnc.indices.lagIndex = boxed_lagIndex.Val;
/* 156 */       psEnc.indices.contourIndex = boxed_contourIndex.Val;
/* 157 */       psEnc.LTPCorr_Q15 = boxed_LTPcorr.Val;
/*     */     } else {
/* 159 */       Arrays.MemSet(psEncCtrl.pitchL, 0, 4);
/* 160 */       psEnc.indices.lagIndex = 0;
/* 161 */       psEnc.indices.contourIndex = 0;
/* 162 */       psEnc.LTPCorr_Q15 = 0;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\FindPitchLags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */