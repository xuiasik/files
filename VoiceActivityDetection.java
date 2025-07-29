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
/*     */ class VoiceActivityDetection
/*     */ {
/*  42 */   private static final int[] tiltWeights = new int[] { 30000, 6000, -12000, -12000 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int silk_VAD_Init(SilkVADState psSilk_VAD) {
/*  50 */     int ret = 0;
/*     */ 
/*     */     
/*  53 */     psSilk_VAD.Reset();
/*     */     
/*     */     int b;
/*     */     
/*  57 */     for (b = 0; b < 4; b++) {
/*  58 */       psSilk_VAD.NoiseLevelBias[b] = Inlines.silk_max_32(Inlines.silk_DIV32_16(50, (short)(b + 1)), 1);
/*     */     }
/*     */ 
/*     */     
/*  62 */     for (b = 0; b < 4; b++) {
/*  63 */       psSilk_VAD.NL[b] = Inlines.silk_MUL(100, psSilk_VAD.NoiseLevelBias[b]);
/*  64 */       psSilk_VAD.inv_NL[b] = Inlines.silk_DIV32(2147483647, psSilk_VAD.NL[b]);
/*     */     } 
/*     */     
/*  67 */     psSilk_VAD.counter = 15;
/*     */ 
/*     */     
/*  70 */     for (b = 0; b < 4; b++) {
/*  71 */       psSilk_VAD.NrgRatioSmth_Q8[b] = 25600;
/*     */     }
/*     */ 
/*     */     
/*  75 */     return ret;
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
/*     */   static int silk_VAD_GetSA_Q8(SilkChannelEncoder psEncC, short[] pIn, int pIn_ptr) {
/*  92 */     int sumSquared = 0;
/*     */ 
/*     */     
/*  95 */     int[] Xnrg = new int[4];
/*  96 */     int[] NrgToNoiseRatio_Q8 = new int[4];
/*     */     
/*  98 */     int[] X_offset = new int[4];
/*  99 */     int ret = 0;
/* 100 */     SilkVADState psSilk_VAD = psEncC.sVAD;
/*     */ 
/*     */     
/* 103 */     Inlines.OpusAssert(true);
/* 104 */     Inlines.OpusAssert((320 >= psEncC.frame_length));
/* 105 */     Inlines.OpusAssert((psEncC.frame_length <= 512));
/* 106 */     Inlines.OpusAssert((psEncC.frame_length == 8 * Inlines.silk_RSHIFT(psEncC.frame_length, 3)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     int decimated_framelength1 = Inlines.silk_RSHIFT(psEncC.frame_length, 1);
/* 116 */     int decimated_framelength2 = Inlines.silk_RSHIFT(psEncC.frame_length, 2);
/* 117 */     int decimated_framelength = Inlines.silk_RSHIFT(psEncC.frame_length, 3);
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
/* 128 */     X_offset[0] = 0;
/* 129 */     X_offset[1] = decimated_framelength + decimated_framelength2;
/* 130 */     X_offset[2] = X_offset[1] + decimated_framelength;
/* 131 */     X_offset[3] = X_offset[2] + decimated_framelength2;
/* 132 */     short[] X = new short[X_offset[3] + decimated_framelength1];
/*     */ 
/*     */     
/* 135 */     Filters.silk_ana_filt_bank_1(pIn, pIn_ptr, psSilk_VAD.AnaState, X, X, X_offset[3], psEncC.frame_length);
/*     */ 
/*     */ 
/*     */     
/* 139 */     Filters.silk_ana_filt_bank_1(X, 0, psSilk_VAD.AnaState1, X, X, X_offset[2], decimated_framelength1);
/*     */ 
/*     */ 
/*     */     
/* 143 */     Filters.silk_ana_filt_bank_1(X, 0, psSilk_VAD.AnaState2, X, X, X_offset[1], decimated_framelength2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     X[decimated_framelength - 1] = (short)Inlines.silk_RSHIFT(X[decimated_framelength - 1], 1);
/* 154 */     short HPstateTmp = X[decimated_framelength - 1];
/*     */     int i;
/* 156 */     for (i = decimated_framelength - 1; i > 0; i--) {
/* 157 */       X[i - 1] = (short)Inlines.silk_RSHIFT(X[i - 1], 1);
/* 158 */       X[i] = (short)(X[i] - X[i - 1]);
/*     */     } 
/*     */     
/* 161 */     X[0] = (short)(X[0] - psSilk_VAD.HPstate);
/* 162 */     psSilk_VAD.HPstate = HPstateTmp;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int b;
/*     */ 
/*     */ 
/*     */     
/* 171 */     for (b = 0; b < 4; b++) {
/*     */       
/* 173 */       decimated_framelength = Inlines.silk_RSHIFT(psEncC.frame_length, Inlines.silk_min_int(4 - b, 3));
/*     */ 
/*     */       
/* 176 */       int dec_subframe_length = Inlines.silk_RSHIFT(decimated_framelength, 2);
/* 177 */       int dec_subframe_offset = 0;
/*     */ 
/*     */ 
/*     */       
/* 181 */       Xnrg[b] = psSilk_VAD.XnrgSubfr[b];
/* 182 */       for (int s = 0; s < 4; s++) {
/* 183 */         sumSquared = 0;
/*     */         
/* 185 */         for (i = 0; i < dec_subframe_length; i++) {
/*     */ 
/*     */           
/* 188 */           int x_tmp = Inlines.silk_RSHIFT(X[X_offset[b] + i + dec_subframe_offset], 3);
/*     */           
/* 190 */           sumSquared = Inlines.silk_SMLABB(sumSquared, x_tmp, x_tmp);
/*     */           
/* 192 */           Inlines.OpusAssert((sumSquared >= 0));
/*     */         } 
/*     */ 
/*     */         
/* 196 */         if (s < 3) {
/* 197 */           Xnrg[b] = Inlines.silk_ADD_POS_SAT32(Xnrg[b], sumSquared);
/*     */         } else {
/*     */           
/* 200 */           Xnrg[b] = Inlines.silk_ADD_POS_SAT32(Xnrg[b], Inlines.silk_RSHIFT(sumSquared, 1));
/*     */         } 
/*     */         
/* 203 */         dec_subframe_offset += dec_subframe_length;
/*     */       } 
/*     */       
/* 206 */       psSilk_VAD.XnrgSubfr[b] = sumSquared;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     silk_VAD_GetNoiseLevels(Xnrg, psSilk_VAD);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     sumSquared = 0;
/* 226 */     int input_tilt = 0;
/* 227 */     for (b = 0; b < 4; b++) {
/* 228 */       int j = Xnrg[b] - psSilk_VAD.NL[b];
/* 229 */       if (j > 0) {
/*     */         
/* 231 */         if ((Xnrg[b] & 0xFF800000) == 0) {
/* 232 */           NrgToNoiseRatio_Q8[b] = Inlines.silk_DIV32(Inlines.silk_LSHIFT(Xnrg[b], 8), psSilk_VAD.NL[b] + 1);
/*     */         } else {
/* 234 */           NrgToNoiseRatio_Q8[b] = Inlines.silk_DIV32(Xnrg[b], Inlines.silk_RSHIFT(psSilk_VAD.NL[b], 8) + 1);
/*     */         } 
/*     */ 
/*     */         
/* 238 */         int SNR_Q7 = Inlines.silk_lin2log(NrgToNoiseRatio_Q8[b]) - 1024;
/*     */ 
/*     */         
/* 241 */         sumSquared = Inlines.silk_SMLABB(sumSquared, SNR_Q7, SNR_Q7);
/*     */ 
/*     */ 
/*     */         
/* 245 */         if (j < 1048576)
/*     */         {
/* 247 */           SNR_Q7 = Inlines.silk_SMULWB(Inlines.silk_LSHIFT(Inlines.silk_SQRT_APPROX(j), 6), SNR_Q7);
/*     */         }
/* 249 */         input_tilt = Inlines.silk_SMLAWB(input_tilt, tiltWeights[b], SNR_Q7);
/*     */       } else {
/* 251 */         NrgToNoiseRatio_Q8[b] = 256;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 256 */     sumSquared = Inlines.silk_DIV32_16(sumSquared, 4);
/*     */ 
/*     */ 
/*     */     
/* 260 */     int pSNR_dB_Q7 = (short)(3 * Inlines.silk_SQRT_APPROX(sumSquared));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     int SA_Q15 = Sigmoid.silk_sigm_Q15(Inlines.silk_SMULWB(45000, pSNR_dB_Q7) - 128);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 279 */     psEncC.input_tilt_Q15 = Inlines.silk_LSHIFT(Sigmoid.silk_sigm_Q15(input_tilt) - 16384, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     int speech_nrg = 0;
/* 289 */     for (b = 0; b < 4; b++)
/*     */     {
/* 291 */       speech_nrg += (b + 1) * Inlines.silk_RSHIFT(Xnrg[b] - psSilk_VAD.NL[b], 4);
/*     */     }
/*     */ 
/*     */     
/* 295 */     if (speech_nrg <= 0) {
/* 296 */       SA_Q15 = Inlines.silk_RSHIFT(SA_Q15, 1);
/* 297 */     } else if (speech_nrg < 32768) {
/* 298 */       if (psEncC.frame_length == 10 * psEncC.fs_kHz) {
/* 299 */         speech_nrg = Inlines.silk_LSHIFT_SAT32(speech_nrg, 16);
/*     */       } else {
/* 301 */         speech_nrg = Inlines.silk_LSHIFT_SAT32(speech_nrg, 15);
/*     */       } 
/*     */ 
/*     */       
/* 305 */       speech_nrg = Inlines.silk_SQRT_APPROX(speech_nrg);
/* 306 */       SA_Q15 = Inlines.silk_SMULWB(32768 + speech_nrg, SA_Q15);
/*     */     } 
/*     */ 
/*     */     
/* 310 */     psEncC.speech_activity_Q8 = Inlines.silk_min_int(Inlines.silk_RSHIFT(SA_Q15, 7), 255);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 320 */     int smooth_coef_Q16 = Inlines.silk_SMULWB(4096, Inlines.silk_SMULWB(SA_Q15, SA_Q15));
/*     */     
/* 322 */     if (psEncC.frame_length == 10 * psEncC.fs_kHz) {
/* 323 */       smooth_coef_Q16 >>= 1;
/*     */     }
/*     */     
/* 326 */     for (b = 0; b < 4; b++) {
/*     */       
/* 328 */       psSilk_VAD.NrgRatioSmth_Q8[b] = Inlines.silk_SMLAWB(psSilk_VAD.NrgRatioSmth_Q8[b], NrgToNoiseRatio_Q8[b] - psSilk_VAD.NrgRatioSmth_Q8[b], smooth_coef_Q16);
/*     */ 
/*     */       
/* 331 */       int SNR_Q7 = 3 * (Inlines.silk_lin2log(psSilk_VAD.NrgRatioSmth_Q8[b]) - 1024);
/*     */       
/* 333 */       psEncC.input_quality_bands_Q15[b] = Sigmoid.silk_sigm_Q15(Inlines.silk_RSHIFT(SNR_Q7 - 2048, 4));
/*     */     } 
/*     */     
/* 336 */     return ret;
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
/*     */   static void silk_VAD_GetNoiseLevels(int[] pX, SilkVADState psSilk_VAD) {
/*     */     int min_coef;
/* 352 */     if (psSilk_VAD.counter < 1000) {
/*     */       
/* 354 */       min_coef = Inlines.silk_DIV32_16(32767, (short)(Inlines.silk_RSHIFT(psSilk_VAD.counter, 4) + 1));
/*     */     } else {
/* 356 */       min_coef = 0;
/*     */     } 
/*     */     
/* 359 */     for (int k = 0; k < 4; k++) {
/*     */       
/* 361 */       int nl = psSilk_VAD.NL[k];
/* 362 */       Inlines.OpusAssert((nl >= 0));
/*     */ 
/*     */       
/* 365 */       int nrg = Inlines.silk_ADD_POS_SAT32(pX[k], psSilk_VAD.NoiseLevelBias[k]);
/* 366 */       Inlines.OpusAssert((nrg > 0));
/*     */ 
/*     */       
/* 369 */       int inv_nrg = Inlines.silk_DIV32(2147483647, nrg);
/* 370 */       Inlines.OpusAssert((inv_nrg >= 0));
/*     */ 
/*     */       
/* 373 */       if (nrg > Inlines.silk_LSHIFT(nl, 3)) {
/* 374 */         coef = 128;
/* 375 */       } else if (nrg < nl) {
/* 376 */         coef = 1024;
/*     */       } else {
/* 378 */         coef = Inlines.silk_SMULWB(Inlines.silk_SMULWW(inv_nrg, nl), 2048);
/*     */       } 
/*     */ 
/*     */       
/* 382 */       int coef = Inlines.silk_max_int(coef, min_coef);
/*     */ 
/*     */       
/* 385 */       psSilk_VAD.inv_NL[k] = Inlines.silk_SMLAWB(psSilk_VAD.inv_NL[k], inv_nrg - psSilk_VAD.inv_NL[k], coef);
/* 386 */       Inlines.OpusAssert((psSilk_VAD.inv_NL[k] >= 0));
/*     */ 
/*     */       
/* 389 */       nl = Inlines.silk_DIV32(2147483647, psSilk_VAD.inv_NL[k]);
/* 390 */       Inlines.OpusAssert((nl >= 0));
/*     */ 
/*     */       
/* 393 */       nl = Inlines.silk_min(nl, 16777215);
/*     */ 
/*     */       
/* 396 */       psSilk_VAD.NL[k] = nl;
/*     */     } 
/*     */ 
/*     */     
/* 400 */     psSilk_VAD.counter++;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\VoiceActivityDetection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */