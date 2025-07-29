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
/*     */ class NoiseShapeAnalysis
/*     */ {
/*     */   static int warped_gain(int[] coefs_Q24, int lambda_Q16, int order) {
/*  48 */     lambda_Q16 = -lambda_Q16;
/*  49 */     int gain_Q24 = coefs_Q24[order - 1];
/*  50 */     for (int i = order - 2; i >= 0; i--) {
/*  51 */       gain_Q24 = Inlines.silk_SMLAWB(coefs_Q24[i], gain_Q24, lambda_Q16);
/*     */     }
/*  53 */     gain_Q24 = Inlines.silk_SMLAWB(16777216, gain_Q24, -lambda_Q16);
/*  54 */     return Inlines.silk_INVERSE32_varQ(gain_Q24, 40);
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
/*     */   static void limit_warped_coefs(int[] coefs_syn_Q24, int[] coefs_ana_Q24, int lambda_Q16, int limit_Q24, int order) {
/*  66 */     int ind = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     lambda_Q16 = -lambda_Q16; int i;
/*  72 */     for (i = order - 1; i > 0; i--) {
/*  73 */       coefs_syn_Q24[i - 1] = Inlines.silk_SMLAWB(coefs_syn_Q24[i - 1], coefs_syn_Q24[i], lambda_Q16);
/*  74 */       coefs_ana_Q24[i - 1] = Inlines.silk_SMLAWB(coefs_ana_Q24[i - 1], coefs_ana_Q24[i], lambda_Q16);
/*     */     } 
/*  76 */     lambda_Q16 = -lambda_Q16;
/*  77 */     int nom_Q16 = Inlines.silk_SMLAWB(65536, -lambda_Q16, lambda_Q16);
/*  78 */     int den_Q24 = Inlines.silk_SMLAWB(16777216, coefs_syn_Q24[0], lambda_Q16);
/*  79 */     int gain_syn_Q16 = Inlines.silk_DIV32_varQ(nom_Q16, den_Q24, 24);
/*  80 */     den_Q24 = Inlines.silk_SMLAWB(16777216, coefs_ana_Q24[0], lambda_Q16);
/*  81 */     int gain_ana_Q16 = Inlines.silk_DIV32_varQ(nom_Q16, den_Q24, 24);
/*  82 */     for (i = 0; i < order; i++) {
/*  83 */       coefs_syn_Q24[i] = Inlines.silk_SMULWW(gain_syn_Q16, coefs_syn_Q24[i]);
/*  84 */       coefs_ana_Q24[i] = Inlines.silk_SMULWW(gain_ana_Q16, coefs_ana_Q24[i]);
/*     */     } 
/*     */     
/*  87 */     for (int iter = 0; iter < 10; iter++) {
/*     */       
/*  89 */       int maxabs_Q24 = -1;
/*  90 */       for (i = 0; i < order; i++) {
/*  91 */         int tmp = Inlines.silk_max(Inlines.silk_abs_int32(coefs_syn_Q24[i]), Inlines.silk_abs_int32(coefs_ana_Q24[i]));
/*  92 */         if (tmp > maxabs_Q24) {
/*  93 */           maxabs_Q24 = tmp;
/*  94 */           ind = i;
/*     */         } 
/*     */       } 
/*  97 */       if (maxabs_Q24 <= limit_Q24) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 103 */       for (i = 1; i < order; i++) {
/* 104 */         coefs_syn_Q24[i - 1] = Inlines.silk_SMLAWB(coefs_syn_Q24[i - 1], coefs_syn_Q24[i], lambda_Q16);
/* 105 */         coefs_ana_Q24[i - 1] = Inlines.silk_SMLAWB(coefs_ana_Q24[i - 1], coefs_ana_Q24[i], lambda_Q16);
/*     */       } 
/* 107 */       gain_syn_Q16 = Inlines.silk_INVERSE32_varQ(gain_syn_Q16, 32);
/* 108 */       gain_ana_Q16 = Inlines.silk_INVERSE32_varQ(gain_ana_Q16, 32);
/* 109 */       for (i = 0; i < order; i++) {
/* 110 */         coefs_syn_Q24[i] = Inlines.silk_SMULWW(gain_syn_Q16, coefs_syn_Q24[i]);
/* 111 */         coefs_ana_Q24[i] = Inlines.silk_SMULWW(gain_ana_Q16, coefs_ana_Q24[i]);
/*     */       } 
/*     */ 
/*     */       
/* 115 */       int chirp_Q16 = 64881 - Inlines.silk_DIV32_varQ(
/* 116 */           Inlines.silk_SMULWB(maxabs_Q24 - limit_Q24, Inlines.silk_SMLABB(819, 102, iter)), 
/* 117 */           Inlines.silk_MUL(maxabs_Q24, ind + 1), 22);
/* 118 */       BWExpander.silk_bwexpander_32(coefs_syn_Q24, order, chirp_Q16);
/* 119 */       BWExpander.silk_bwexpander_32(coefs_ana_Q24, order, chirp_Q16);
/*     */ 
/*     */       
/* 122 */       lambda_Q16 = -lambda_Q16;
/* 123 */       for (i = order - 1; i > 0; i--) {
/* 124 */         coefs_syn_Q24[i - 1] = Inlines.silk_SMLAWB(coefs_syn_Q24[i - 1], coefs_syn_Q24[i], lambda_Q16);
/* 125 */         coefs_ana_Q24[i - 1] = Inlines.silk_SMLAWB(coefs_ana_Q24[i - 1], coefs_ana_Q24[i], lambda_Q16);
/*     */       } 
/* 127 */       lambda_Q16 = -lambda_Q16;
/* 128 */       nom_Q16 = Inlines.silk_SMLAWB(65536, -lambda_Q16, lambda_Q16);
/* 129 */       den_Q24 = Inlines.silk_SMLAWB(16777216, coefs_syn_Q24[0], lambda_Q16);
/* 130 */       gain_syn_Q16 = Inlines.silk_DIV32_varQ(nom_Q16, den_Q24, 24);
/* 131 */       den_Q24 = Inlines.silk_SMLAWB(16777216, coefs_ana_Q24[0], lambda_Q16);
/* 132 */       gain_ana_Q16 = Inlines.silk_DIV32_varQ(nom_Q16, den_Q24, 24);
/* 133 */       for (i = 0; i < order; i++) {
/* 134 */         coefs_syn_Q24[i] = Inlines.silk_SMULWW(gain_syn_Q16, coefs_syn_Q24[i]);
/* 135 */         coefs_ana_Q24[i] = Inlines.silk_SMULWW(gain_ana_Q16, coefs_ana_Q24[i]);
/*     */       } 
/*     */     } 
/* 138 */     Inlines.OpusAssert(false);
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
/*     */   static void silk_noise_shape_analysis(SilkChannelEncoder psEnc, SilkEncoderControl psEncCtrl, short[] pitch_res, int pitch_res_ptr, short[] x, int x_ptr) {
/*     */     int warping_Q16, HarmShapeGain_Q16, Tilt_Q16;
/* 156 */     SilkShapeState psShapeSt = psEnc.sShape;
/* 157 */     int scale = 0;
/*     */ 
/*     */ 
/*     */     
/* 161 */     int[] auto_corr = new int[17];
/* 162 */     int[] refl_coef_Q16 = new int[16];
/* 163 */     int[] AR1_Q24 = new int[16];
/* 164 */     int[] AR2_Q24 = new int[16];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     int x_ptr2 = x_ptr - psEnc.la_shape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     int SNR_adj_dB_Q7 = psEnc.SNR_dB_Q7;
/*     */ 
/*     */     
/* 182 */     psEncCtrl.input_quality_Q14 = Inlines.silk_RSHIFT(psEnc.input_quality_bands_Q15[0] + psEnc.input_quality_bands_Q15[1], 2);
/*     */ 
/*     */ 
/*     */     
/* 186 */     psEncCtrl.coding_quality_Q14 = Inlines.silk_RSHIFT(Sigmoid.silk_sigm_Q15(Inlines.silk_RSHIFT_ROUND(SNR_adj_dB_Q7 - 2560, 4)), 1);
/*     */ 
/*     */ 
/*     */     
/* 190 */     if (psEnc.useCBR == 0) {
/* 191 */       int b_Q8 = 256 - psEnc.speech_activity_Q8;
/* 192 */       b_Q8 = Inlines.silk_SMULWB(Inlines.silk_LSHIFT(b_Q8, 8), b_Q8);
/* 193 */       SNR_adj_dB_Q7 = Inlines.silk_SMLAWB(SNR_adj_dB_Q7, 
/* 194 */           Inlines.silk_SMULBB(-8, b_Q8), 
/* 195 */           Inlines.silk_SMULWB(16384 + psEncCtrl.input_quality_Q14, psEncCtrl.coding_quality_Q14));
/*     */     } 
/*     */ 
/*     */     
/* 199 */     if (psEnc.indices.signalType == 2) {
/*     */       
/* 201 */       SNR_adj_dB_Q7 = Inlines.silk_SMLAWB(SNR_adj_dB_Q7, 512, psEnc.LTPCorr_Q15);
/*     */     } else {
/*     */       
/* 204 */       SNR_adj_dB_Q7 = Inlines.silk_SMLAWB(SNR_adj_dB_Q7, 
/* 205 */           Inlines.silk_SMLAWB(3072, -104858, psEnc.SNR_dB_Q7), 16384 - psEncCtrl.input_quality_Q14);
/*     */     } 
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
/* 217 */     if (psEnc.indices.signalType == 2) {
/*     */       
/* 219 */       psEnc.indices.quantOffsetType = 0;
/* 220 */       psEncCtrl.sparseness_Q8 = 0;
/*     */     } else {
/*     */       
/* 223 */       int nSamples = Inlines.silk_LSHIFT(psEnc.fs_kHz, 1);
/* 224 */       int energy_variation_Q7 = 0;
/* 225 */       int log_energy_prev_Q7 = 0;
/* 226 */       int pitch_res_ptr2 = pitch_res_ptr;
/* 227 */       BoxedValueInt boxed_nrg = new BoxedValueInt(0);
/* 228 */       BoxedValueInt boxed_scale = new BoxedValueInt(0);
/* 229 */       for (int i = 0; i < Inlines.silk_SMULBB(5, psEnc.nb_subfr) / 2; i++) {
/* 230 */         SumSqrShift.silk_sum_sqr_shift(boxed_nrg, boxed_scale, pitch_res, pitch_res_ptr2, nSamples);
/* 231 */         int nrg = boxed_nrg.Val;
/* 232 */         scale = boxed_scale.Val;
/* 233 */         nrg += Inlines.silk_RSHIFT(nSamples, scale);
/*     */ 
/*     */         
/* 236 */         int log_energy_Q7 = Inlines.silk_lin2log(nrg);
/* 237 */         if (i > 0) {
/* 238 */           energy_variation_Q7 += Inlines.silk_abs(log_energy_Q7 - log_energy_prev_Q7);
/*     */         }
/* 240 */         log_energy_prev_Q7 = log_energy_Q7;
/* 241 */         pitch_res_ptr2 += nSamples;
/*     */       } 
/*     */       
/* 244 */       psEncCtrl.sparseness_Q8 = Inlines.silk_RSHIFT(Sigmoid.silk_sigm_Q15(Inlines.silk_SMULWB(energy_variation_Q7 - 640, 6554)), 7);
/*     */ 
/*     */ 
/*     */       
/* 248 */       if (psEncCtrl.sparseness_Q8 > 192) {
/* 249 */         psEnc.indices.quantOffsetType = 0;
/*     */       } else {
/* 251 */         psEnc.indices.quantOffsetType = 1;
/*     */       } 
/*     */ 
/*     */       
/* 255 */       SNR_adj_dB_Q7 = Inlines.silk_SMLAWB(SNR_adj_dB_Q7, 65536, psEncCtrl.sparseness_Q8 - 128);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 266 */     int strength_Q16 = Inlines.silk_SMULWB(psEncCtrl.predGain_Q16, 66);
/* 267 */     int BWExp2_Q16 = Inlines.silk_DIV32_varQ(62259, 
/* 268 */         Inlines.silk_SMLAWW(65536, strength_Q16, strength_Q16), 16), BWExp1_Q16 = BWExp2_Q16;
/* 269 */     int delta_Q16 = Inlines.silk_SMULWB(65536 - Inlines.silk_SMULBB(3, psEncCtrl.coding_quality_Q14), 655);
/*     */     
/* 271 */     BWExp1_Q16 = Inlines.silk_SUB32(BWExp1_Q16, delta_Q16);
/* 272 */     BWExp2_Q16 = Inlines.silk_ADD32(BWExp2_Q16, delta_Q16);
/*     */     
/* 274 */     BWExp1_Q16 = Inlines.silk_DIV32_16(Inlines.silk_LSHIFT(BWExp1_Q16, 14), Inlines.silk_RSHIFT(BWExp2_Q16, 2));
/*     */     
/* 276 */     if (psEnc.warping_Q16 > 0) {
/*     */       
/* 278 */       warping_Q16 = Inlines.silk_SMLAWB(psEnc.warping_Q16, psEncCtrl.coding_quality_Q14, 2621);
/*     */     } else {
/* 280 */       warping_Q16 = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     short[] x_windowed = new short[psEnc.shapeWinLength]; int k;
/* 291 */     for (k = 0; k < psEnc.nb_subfr; k++) {
/*     */ 
/*     */       
/* 294 */       int flat_part = psEnc.fs_kHz * 3;
/* 295 */       int slope_part = Inlines.silk_RSHIFT(psEnc.shapeWinLength - flat_part, 1);
/*     */       
/* 297 */       ApplySineWindow.silk_apply_sine_window(x_windowed, 0, x, x_ptr2, 1, slope_part);
/* 298 */       int shift = slope_part;
/* 299 */       System.arraycopy(x, x_ptr2 + shift, x_windowed, shift, flat_part);
/* 300 */       shift += flat_part;
/* 301 */       ApplySineWindow.silk_apply_sine_window(x_windowed, shift, x, x_ptr2 + shift, 2, slope_part);
/*     */ 
/*     */       
/* 304 */       x_ptr2 += psEnc.subfr_length;
/* 305 */       BoxedValueInt scale_boxed = new BoxedValueInt(scale);
/* 306 */       if (psEnc.warping_Q16 > 0) {
/*     */         
/* 308 */         Autocorrelation.silk_warped_autocorrelation(auto_corr, scale_boxed, x_windowed, warping_Q16, psEnc.shapeWinLength, psEnc.shapingLPCOrder);
/*     */       } else {
/*     */         
/* 311 */         Autocorrelation.silk_autocorr(auto_corr, scale_boxed, x_windowed, psEnc.shapeWinLength, psEnc.shapingLPCOrder + 1);
/*     */       } 
/* 313 */       scale = scale_boxed.Val;
/*     */ 
/*     */       
/* 316 */       auto_corr[0] = Inlines.silk_ADD32(auto_corr[0], Inlines.silk_max_32(Inlines.silk_SMULWB(Inlines.silk_RSHIFT(auto_corr[0], 4), 52), 1));
/*     */ 
/*     */ 
/*     */       
/* 320 */       int nrg = Schur.silk_schur64(refl_coef_Q16, auto_corr, psEnc.shapingLPCOrder);
/* 321 */       Inlines.OpusAssert((nrg >= 0));
/*     */ 
/*     */       
/* 324 */       K2A.silk_k2a_Q16(AR2_Q24, refl_coef_Q16, psEnc.shapingLPCOrder);
/*     */       
/* 326 */       int Qnrg = -scale;
/*     */       
/* 328 */       Inlines.OpusAssert((Qnrg >= -12));
/* 329 */       Inlines.OpusAssert((Qnrg <= 30));
/*     */ 
/*     */       
/* 332 */       if ((Qnrg & 0x1) != 0) {
/* 333 */         Qnrg--;
/* 334 */         nrg >>= 1;
/*     */       } 
/*     */       
/* 337 */       int tmp32 = Inlines.silk_SQRT_APPROX(nrg);
/* 338 */       Qnrg >>= 1;
/*     */ 
/*     */       
/* 341 */       psEncCtrl.Gains_Q16[k] = Inlines.silk_LSHIFT_SAT32(tmp32, 16 - Qnrg);
/*     */       
/* 343 */       if (psEnc.warping_Q16 > 0) {
/*     */         
/* 345 */         int j = warped_gain(AR2_Q24, warping_Q16, psEnc.shapingLPCOrder);
/* 346 */         Inlines.OpusAssert((psEncCtrl.Gains_Q16[k] >= 0));
/* 347 */         if (Inlines.silk_SMULWW(Inlines.silk_RSHIFT_ROUND(psEncCtrl.Gains_Q16[k], 1), j) >= 1073741823) {
/* 348 */           psEncCtrl.Gains_Q16[k] = Integer.MAX_VALUE;
/*     */         } else {
/* 350 */           psEncCtrl.Gains_Q16[k] = Inlines.silk_SMULWW(psEncCtrl.Gains_Q16[k], j);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 355 */       BWExpander.silk_bwexpander_32(AR2_Q24, psEnc.shapingLPCOrder, BWExp2_Q16);
/*     */ 
/*     */       
/* 358 */       System.arraycopy(AR2_Q24, 0, AR1_Q24, 0, psEnc.shapingLPCOrder);
/*     */ 
/*     */       
/* 361 */       Inlines.OpusAssert((BWExp1_Q16 <= 65536));
/* 362 */       BWExpander.silk_bwexpander_32(AR1_Q24, psEnc.shapingLPCOrder, BWExp1_Q16);
/*     */ 
/*     */       
/* 365 */       int pre_nrg_Q30 = LPCInversePredGain.silk_LPC_inverse_pred_gain_Q24(AR2_Q24, psEnc.shapingLPCOrder);
/* 366 */       nrg = LPCInversePredGain.silk_LPC_inverse_pred_gain_Q24(AR1_Q24, psEnc.shapingLPCOrder);
/*     */ 
/*     */       
/* 369 */       pre_nrg_Q30 = Inlines.silk_LSHIFT32(Inlines.silk_SMULWB(pre_nrg_Q30, 22938), 1);
/* 370 */       psEncCtrl.GainsPre_Q14[k] = 4915 + Inlines.silk_DIV32_varQ(pre_nrg_Q30, nrg, 14);
/*     */ 
/*     */       
/* 373 */       limit_warped_coefs(AR2_Q24, AR1_Q24, warping_Q16, 67092088, psEnc.shapingLPCOrder);
/*     */ 
/*     */       
/* 376 */       for (int i = 0; i < psEnc.shapingLPCOrder; i++) {
/* 377 */         psEncCtrl.AR1_Q13[k * 16 + i] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(AR1_Q24[i], 11));
/* 378 */         psEncCtrl.AR2_Q13[k * 16 + i] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(AR2_Q24[i], 11));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 390 */     int gain_mult_Q16 = Inlines.silk_log2lin(-Inlines.silk_SMLAWB(-2048, SNR_adj_dB_Q7, 10486));
/* 391 */     int gain_add_Q16 = Inlines.silk_log2lin(Inlines.silk_SMLAWB(2048, 256, 10486));
/* 392 */     Inlines.OpusAssert((gain_mult_Q16 > 0));
/* 393 */     for (k = 0; k < psEnc.nb_subfr; k++) {
/* 394 */       psEncCtrl.Gains_Q16[k] = Inlines.silk_SMULWW(psEncCtrl.Gains_Q16[k], gain_mult_Q16);
/* 395 */       Inlines.OpusAssert((psEncCtrl.Gains_Q16[k] >= 0));
/* 396 */       psEncCtrl.Gains_Q16[k] = Inlines.silk_ADD_POS_SAT32(psEncCtrl.Gains_Q16[k], gain_add_Q16);
/*     */     } 
/*     */     
/* 399 */     gain_mult_Q16 = 65536 + Inlines.silk_RSHIFT_ROUND(Inlines.silk_MLA(3355443, psEncCtrl.coding_quality_Q14, 410), 10);
/*     */     
/* 401 */     for (k = 0; k < psEnc.nb_subfr; k++) {
/* 402 */       psEncCtrl.GainsPre_Q14[k] = Inlines.silk_SMULWB(gain_mult_Q16, psEncCtrl.GainsPre_Q14[k]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 413 */     strength_Q16 = Inlines.silk_MUL(64, Inlines.silk_SMLAWB(4096, 4096, psEnc.input_quality_bands_Q15[0] - 32768));
/*     */     
/* 415 */     strength_Q16 = Inlines.silk_RSHIFT(Inlines.silk_MUL(strength_Q16, psEnc.speech_activity_Q8), 8);
/* 416 */     if (psEnc.indices.signalType == 2) {
/*     */ 
/*     */       
/* 419 */       int fs_kHz_inv = Inlines.silk_DIV32_16(3277, psEnc.fs_kHz);
/* 420 */       for (k = 0; k < psEnc.nb_subfr; k++) {
/* 421 */         int b_Q14 = fs_kHz_inv + Inlines.silk_DIV32_16(49152, psEncCtrl.pitchL[k]);
/*     */         
/* 423 */         psEncCtrl.LF_shp_Q14[k] = Inlines.silk_LSHIFT(16384 - b_Q14 - Inlines.silk_SMULWB(strength_Q16, b_Q14), 16);
/* 424 */         psEncCtrl.LF_shp_Q14[k] = psEncCtrl.LF_shp_Q14[k] | b_Q14 - 16384 & 0xFFFF;
/*     */       } 
/* 426 */       Inlines.OpusAssert(true);
/*     */       
/* 428 */       Tilt_Q16 = -16384 - Inlines.silk_SMULWB(49152, 
/* 429 */           Inlines.silk_SMULWB(5872026, psEnc.speech_activity_Q8));
/*     */     } else {
/* 431 */       int b_Q14 = Inlines.silk_DIV32_16(21299, psEnc.fs_kHz);
/*     */ 
/*     */       
/* 434 */       psEncCtrl.LF_shp_Q14[0] = Inlines.silk_LSHIFT(16384 - b_Q14 - 
/* 435 */           Inlines.silk_SMULWB(strength_Q16, Inlines.silk_SMULWB(39322, b_Q14)), 16);
/* 436 */       psEncCtrl.LF_shp_Q14[0] = psEncCtrl.LF_shp_Q14[0] | b_Q14 - 16384 & 0xFFFF;
/* 437 */       for (k = 1; k < psEnc.nb_subfr; k++) {
/* 438 */         psEncCtrl.LF_shp_Q14[k] = psEncCtrl.LF_shp_Q14[0];
/*     */       }
/* 440 */       Tilt_Q16 = -16384;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 451 */     int HarmBoost_Q16 = Inlines.silk_SMULWB(Inlines.silk_SMULWB(131072 - Inlines.silk_LSHIFT(psEncCtrl.coding_quality_Q14, 3), psEnc.LTPCorr_Q15), 6554);
/*     */ 
/*     */ 
/*     */     
/* 455 */     HarmBoost_Q16 = Inlines.silk_SMLAWB(HarmBoost_Q16, 65536 - 
/* 456 */         Inlines.silk_LSHIFT(psEncCtrl.input_quality_Q14, 2), 6554);
/*     */     
/* 458 */     if (psEnc.indices.signalType == 2) {
/*     */       
/* 460 */       HarmShapeGain_Q16 = Inlines.silk_SMLAWB(19661, 65536 - 
/* 461 */           Inlines.silk_SMULWB(262144 - Inlines.silk_LSHIFT(psEncCtrl.coding_quality_Q14, 4), psEncCtrl.input_quality_Q14), 13107);
/*     */ 
/*     */ 
/*     */       
/* 465 */       HarmShapeGain_Q16 = Inlines.silk_SMULWB(Inlines.silk_LSHIFT(HarmShapeGain_Q16, 1), 
/* 466 */           Inlines.silk_SQRT_APPROX(Inlines.silk_LSHIFT(psEnc.LTPCorr_Q15, 15)));
/*     */     } else {
/* 468 */       HarmShapeGain_Q16 = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 478 */     for (k = 0; k < 4; k++) {
/* 479 */       psShapeSt
/* 480 */         .HarmBoost_smth_Q16 = Inlines.silk_SMLAWB(psShapeSt.HarmBoost_smth_Q16, HarmBoost_Q16 - psShapeSt.HarmBoost_smth_Q16, 26214);
/* 481 */       psShapeSt
/* 482 */         .HarmShapeGain_smth_Q16 = Inlines.silk_SMLAWB(psShapeSt.HarmShapeGain_smth_Q16, HarmShapeGain_Q16 - psShapeSt.HarmShapeGain_smth_Q16, 26214);
/* 483 */       psShapeSt
/* 484 */         .Tilt_smth_Q16 = Inlines.silk_SMLAWB(psShapeSt.Tilt_smth_Q16, Tilt_Q16 - psShapeSt.Tilt_smth_Q16, 26214);
/*     */       
/* 486 */       psEncCtrl.HarmBoost_Q14[k] = Inlines.silk_RSHIFT_ROUND(psShapeSt.HarmBoost_smth_Q16, 2);
/* 487 */       psEncCtrl.HarmShapeGain_Q14[k] = Inlines.silk_RSHIFT_ROUND(psShapeSt.HarmShapeGain_smth_Q16, 2);
/* 488 */       psEncCtrl.Tilt_Q14[k] = Inlines.silk_RSHIFT_ROUND(psShapeSt.Tilt_smth_Q16, 2);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\NoiseShapeAnalysis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */