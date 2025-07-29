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
/*     */ class PitchAnalysisCore
/*     */ {
/*     */   private static final int SCRATCH_SIZE = 22;
/*     */   private static final int SF_LENGTH_4KHZ = 20;
/*     */   private static final int SF_LENGTH_8KHZ = 40;
/*     */   private static final int MIN_LAG_4KHZ = 8;
/*     */   private static final int MIN_LAG_8KHZ = 16;
/*     */   private static final int MAX_LAG_4KHZ = 72;
/*     */   private static final int MAX_LAG_8KHZ = 143;
/*     */   private static final int CSTRIDE_4KHZ = 65;
/*     */   private static final int CSTRIDE_8KHZ = 132;
/*     */   private static final int D_COMP_MIN = 13;
/*     */   private static final int D_COMP_MAX = 147;
/*     */   private static final int D_COMP_STRIDE = 134;
/*     */   
/*     */   static class silk_pe_stage3_vals
/*     */   {
/*  53 */     public final int[] Values = new int[5];
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int silk_pitch_analysis_core(short[] frame, int[] pitch_out, BoxedValueShort lagIndex, BoxedValueByte contourIndex, BoxedValueInt LTPCorr_Q15, int prevLag, int search_thres1_Q16, int search_thres2_Q13, int Fs_kHz, int complexity, int nb_subfr) {
/*     */     int nb_cbk_search, prevLag_log2_Q7;
/*     */     byte[][] Lag_CB_ptr;
/*  78 */     int[] filt_state = new int[6];
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
/*  89 */     int[] d_srch = new int[24];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     int[] CC = new int[11];
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
/* 107 */     Inlines.OpusAssert((Fs_kHz == 8 || Fs_kHz == 12 || Fs_kHz == 16));
/*     */ 
/*     */     
/* 110 */     Inlines.OpusAssert((complexity >= 0));
/* 111 */     Inlines.OpusAssert((complexity <= 2));
/*     */     
/* 113 */     Inlines.OpusAssert((search_thres1_Q16 >= 0 && search_thres1_Q16 <= 65536));
/* 114 */     Inlines.OpusAssert((search_thres2_Q13 >= 0 && search_thres2_Q13 <= 8192));
/*     */ 
/*     */     
/* 117 */     int frame_length = (20 + nb_subfr * 5) * Fs_kHz;
/* 118 */     int frame_length_4kHz = (20 + nb_subfr * 5) * 4;
/* 119 */     int frame_length_8kHz = (20 + nb_subfr * 5) * 8;
/* 120 */     int sf_length = 5 * Fs_kHz;
/* 121 */     int min_lag = 2 * Fs_kHz;
/* 122 */     int max_lag = 18 * Fs_kHz - 1;
/*     */ 
/*     */     
/* 125 */     short[] frame_8kHz = new short[frame_length_8kHz];
/* 126 */     if (Fs_kHz == 16) {
/* 127 */       Arrays.MemSet(filt_state, 0, 2);
/* 128 */       Resampler.silk_resampler_down2(filt_state, frame_8kHz, frame, frame_length);
/* 129 */     } else if (Fs_kHz == 12) {
/* 130 */       Arrays.MemSet(filt_state, 0, 6);
/* 131 */       Resampler.silk_resampler_down2_3(filt_state, frame_8kHz, frame, frame_length);
/*     */     } else {
/* 133 */       Inlines.OpusAssert((Fs_kHz == 8));
/* 134 */       System.arraycopy(frame, 0, frame_8kHz, 0, frame_length_8kHz);
/*     */     } 
/*     */ 
/*     */     
/* 138 */     Arrays.MemSet(filt_state, 0, 2);
/*     */     
/* 140 */     short[] frame_4kHz = new short[frame_length_4kHz];
/* 141 */     Resampler.silk_resampler_down2(filt_state, frame_4kHz, frame_8kHz, frame_length_8kHz);
/*     */     
/*     */     int i;
/* 144 */     for (i = frame_length_4kHz - 1; i > 0; i--) {
/* 145 */       frame_4kHz[i] = Inlines.silk_ADD_SAT16(frame_4kHz[i], frame_4kHz[i - 1]);
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
/* 156 */     BoxedValueInt boxed_energy = new BoxedValueInt(0);
/* 157 */     BoxedValueInt boxed_shift = new BoxedValueInt(0);
/* 158 */     SumSqrShift.silk_sum_sqr_shift(boxed_energy, boxed_shift, frame_4kHz, frame_length_4kHz);
/* 159 */     int energy = boxed_energy.Val;
/* 160 */     int shift = boxed_shift.Val;
/*     */     
/* 162 */     if (shift > 0) {
/* 163 */       shift = Inlines.silk_RSHIFT(shift, 1);
/* 164 */       for (i = 0; i < frame_length_4kHz; i++) {
/* 165 */         frame_4kHz[i] = Inlines.silk_RSHIFT16(frame_4kHz[i], shift);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     short[] C = new short[nb_subfr * 132];
/* 175 */     int[] xcorr32 = new int[65];
/* 176 */     Arrays.MemSet(C, (short)0, (nb_subfr >> 1) * 65);
/* 177 */     short[] target = frame_4kHz;
/* 178 */     int target_ptr = Inlines.silk_LSHIFT(20, 2); int k;
/* 179 */     for (k = 0; k < nb_subfr >> 1; k++) {
/* 180 */       short[] basis = target;
/* 181 */       int basis_ptr = target_ptr - 8;
/*     */       
/* 183 */       CeltPitchXCorr.pitch_xcorr(target, target_ptr, target, target_ptr - 72, xcorr32, 40, 65);
/*     */ 
/*     */       
/* 186 */       int cross_corr = xcorr32[64];
/* 187 */       int normalizer = Inlines.silk_inner_prod_self(target, target_ptr, 40);
/* 188 */       normalizer = Inlines.silk_ADD32(normalizer, Inlines.silk_inner_prod_self(basis, basis_ptr, 40));
/* 189 */       normalizer = Inlines.silk_ADD32(normalizer, Inlines.silk_SMULBB(40, 4000));
/*     */       
/* 191 */       Inlines.MatrixSet(C, k, 0, 65, 
/* 192 */           (short)Inlines.silk_DIV32_varQ(cross_corr, normalizer, 14));
/*     */ 
/*     */ 
/*     */       
/* 196 */       for (int d = 9; d <= 72; d++) {
/* 197 */         basis_ptr--;
/*     */         
/* 199 */         cross_corr = xcorr32[72 - d];
/*     */ 
/*     */         
/* 202 */         normalizer = Inlines.silk_ADD32(normalizer, 
/* 203 */             Inlines.silk_SMULBB(basis[basis_ptr], basis[basis_ptr]) - 
/* 204 */             Inlines.silk_SMULBB(basis[basis_ptr + 40], basis[basis_ptr + 40]));
/*     */         
/* 206 */         Inlines.MatrixSet(C, k, d - 8, 65, 
/* 207 */             (short)Inlines.silk_DIV32_varQ(cross_corr, normalizer, 14));
/*     */       } 
/*     */ 
/*     */       
/* 211 */       target_ptr += 40;
/*     */     } 
/*     */ 
/*     */     
/* 215 */     if (nb_subfr == 4) {
/* 216 */       for (i = 72; i >= 8; i--)
/*     */       {
/* 218 */         int sum = Inlines.MatrixGet(C, 0, i - 8, 65) + Inlines.MatrixGet(C, 1, i - 8, 65);
/*     */         
/* 220 */         sum = Inlines.silk_SMLAWB(sum, sum, Inlines.silk_LSHIFT(-i, 4));
/*     */         
/* 222 */         C[i - 8] = (short)sum;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 227 */       for (i = 72; i >= 8; i--) {
/* 228 */         int sum = Inlines.silk_LSHIFT(C[i - 8], 1);
/*     */         
/* 230 */         sum = Inlines.silk_SMLAWB(sum, sum, Inlines.silk_LSHIFT(-i, 4));
/*     */         
/* 232 */         C[i - 8] = (short)sum;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 238 */     int length_d_srch = Inlines.silk_ADD_LSHIFT32(4, complexity, 1);
/* 239 */     Inlines.OpusAssert((3 * length_d_srch <= 24));
/* 240 */     Sort.silk_insertion_sort_decreasing_int16(C, d_srch, 65, length_d_srch);
/*     */ 
/*     */     
/* 243 */     int Cmax = C[0];
/*     */     
/* 245 */     if (Cmax < 3277) {
/* 246 */       Arrays.MemSet(pitch_out, 0, nb_subfr);
/* 247 */       LTPCorr_Q15.Val = 0;
/* 248 */       lagIndex.Val = 0;
/* 249 */       contourIndex.Val = 0;
/*     */       
/* 251 */       return 1;
/*     */     } 
/*     */     
/* 254 */     int threshold = Inlines.silk_SMULWB(search_thres1_Q16, Cmax);
/* 255 */     for (i = 0; i < length_d_srch; i++) {
/*     */       
/* 257 */       if (C[i] > threshold) {
/* 258 */         d_srch[i] = Inlines.silk_LSHIFT(d_srch[i] + 8, 1);
/*     */       } else {
/* 260 */         length_d_srch = i;
/*     */         break;
/*     */       } 
/*     */     } 
/* 264 */     Inlines.OpusAssert((length_d_srch > 0));
/*     */     
/* 266 */     short[] d_comp = new short[134];
/* 267 */     for (i = 13; i < 147; i++) {
/* 268 */       d_comp[i - 13] = 0;
/*     */     }
/* 270 */     for (i = 0; i < length_d_srch; i++) {
/* 271 */       d_comp[d_srch[i] - 13] = 1;
/*     */     }
/*     */ 
/*     */     
/* 275 */     for (i = 146; i >= 16; i--) {
/* 276 */       d_comp[i - 13] = (short)(d_comp[i - 13] + (short)(d_comp[i - 1 - 13] + d_comp[i - 2 - 13]));
/*     */     }
/*     */     
/* 279 */     length_d_srch = 0;
/* 280 */     for (i = 16; i < 144; i++) {
/* 281 */       if (d_comp[i + 1 - 13] > 0) {
/* 282 */         d_srch[length_d_srch] = i;
/* 283 */         length_d_srch++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 288 */     for (i = 146; i >= 16; i--) {
/* 289 */       d_comp[i - 13] = (short)(d_comp[i - 13] + (short)(d_comp[i - 1 - 13] + d_comp[i - 2 - 13] + d_comp[i - 3 - 13]));
/*     */     }
/*     */     
/* 292 */     int length_d_comp = 0;
/* 293 */     for (i = 16; i < 147; i++) {
/* 294 */       if (d_comp[i - 13] > 0) {
/* 295 */         d_comp[length_d_comp] = (short)(i - 2);
/* 296 */         length_d_comp++;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     boxed_energy.Val = 0;
/* 313 */     boxed_shift.Val = 0;
/* 314 */     SumSqrShift.silk_sum_sqr_shift(boxed_energy, boxed_shift, frame_8kHz, frame_length_8kHz);
/* 315 */     energy = boxed_energy.Val;
/* 316 */     shift = boxed_shift.Val;
/*     */     
/* 318 */     if (shift > 0) {
/* 319 */       shift = Inlines.silk_RSHIFT(shift, 1);
/* 320 */       for (i = 0; i < frame_length_8kHz; i++) {
/* 321 */         frame_8kHz[i] = Inlines.silk_RSHIFT16(frame_8kHz[i], shift);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     Arrays.MemSet(C, (short)0, nb_subfr * 132);
/*     */     
/* 333 */     target = frame_8kHz;
/* 334 */     target_ptr = 160;
/* 335 */     for (k = 0; k < nb_subfr; k++) {
/*     */       
/* 337 */       int energy_target = Inlines.silk_ADD32(Inlines.silk_inner_prod(target, target_ptr, target, target_ptr, 40), 1);
/* 338 */       for (int j = 0; j < length_d_comp; j++) {
/* 339 */         int d = d_comp[j];
/* 340 */         short[] basis = target;
/* 341 */         int basis_ptr = target_ptr - d;
/*     */         
/* 343 */         int cross_corr = Inlines.silk_inner_prod(target, target_ptr, basis, basis_ptr, 40);
/* 344 */         if (cross_corr > 0) {
/* 345 */           int energy_basis = Inlines.silk_inner_prod_self(basis, basis_ptr, 40);
/* 346 */           Inlines.MatrixSet(C, k, d - 14, 132, 
/* 347 */               (short)Inlines.silk_DIV32_varQ(cross_corr, 
/* 348 */                 Inlines.silk_ADD32(energy_target, energy_basis), 14));
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 353 */           Inlines.MatrixSet(C, k, d - 14, 132, (short)0);
/*     */         } 
/*     */       } 
/* 356 */       target_ptr += 40;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 361 */     int CCmax = Integer.MIN_VALUE;
/* 362 */     int CCmax_b = Integer.MIN_VALUE;
/*     */     
/* 364 */     int CBimax = 0;
/*     */     
/* 366 */     int lag = -1;
/*     */ 
/*     */     
/* 369 */     if (prevLag > 0) {
/* 370 */       if (Fs_kHz == 12) {
/* 371 */         prevLag = Inlines.silk_DIV32_16(Inlines.silk_LSHIFT(prevLag, 1), 3);
/* 372 */       } else if (Fs_kHz == 16) {
/* 373 */         prevLag = Inlines.silk_RSHIFT(prevLag, 1);
/*     */       } 
/* 375 */       prevLag_log2_Q7 = Inlines.silk_lin2log(prevLag);
/*     */     } else {
/* 377 */       prevLag_log2_Q7 = 0;
/*     */     } 
/* 379 */     Inlines.OpusAssert((search_thres2_Q13 == Inlines.silk_SAT16(search_thres2_Q13)));
/*     */     
/* 381 */     if (nb_subfr == 4) {
/* 382 */       Lag_CB_ptr = SilkTables.silk_CB_lags_stage2;
/* 383 */       if (Fs_kHz == 8 && complexity > 0) {
/*     */         
/* 385 */         nb_cbk_search = 11;
/*     */       } else {
/* 387 */         nb_cbk_search = 3;
/*     */       } 
/*     */     } else {
/* 390 */       Lag_CB_ptr = SilkTables.silk_CB_lags_stage2_10_ms;
/* 391 */       nb_cbk_search = 3;
/*     */     } 
/*     */     
/* 394 */     for (k = 0; k < length_d_srch; k++) {
/* 395 */       int d = d_srch[k];
/* 396 */       for (int j = 0; j < nb_cbk_search; j++) {
/* 397 */         CC[j] = 0;
/* 398 */         for (i = 0; i < nb_subfr; i++) {
/*     */ 
/*     */           
/* 401 */           int d_subfr = d + Lag_CB_ptr[i][j];
/* 402 */           CC[j] = CC[j] + 
/* 403 */             Inlines.MatrixGet(C, i, d_subfr - 14, 132);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 409 */       int CCmax_new = Integer.MIN_VALUE;
/* 410 */       int CBimax_new = 0;
/* 411 */       for (i = 0; i < nb_cbk_search; i++) {
/* 412 */         if (CC[i] > CCmax_new) {
/* 413 */           CCmax_new = CC[i];
/* 414 */           CBimax_new = i;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 419 */       int lag_log2_Q7 = Inlines.silk_lin2log(d);
/*     */       
/* 421 */       Inlines.OpusAssert((lag_log2_Q7 == Inlines.silk_SAT16(lag_log2_Q7)));
/* 422 */       Inlines.OpusAssert((nb_subfr * 1638 == Inlines.silk_SAT16(nb_subfr * 1638)));
/* 423 */       int CCmax_new_b = CCmax_new - Inlines.silk_RSHIFT(Inlines.silk_SMULBB(nb_subfr * 1638, lag_log2_Q7), 7);
/*     */ 
/*     */ 
/*     */       
/* 427 */       Inlines.OpusAssert((nb_subfr * 1638 == Inlines.silk_SAT16(nb_subfr * 1638)));
/* 428 */       if (prevLag > 0) {
/* 429 */         int delta_lag_log2_sqr_Q7 = lag_log2_Q7 - prevLag_log2_Q7;
/* 430 */         Inlines.OpusAssert((delta_lag_log2_sqr_Q7 == Inlines.silk_SAT16(delta_lag_log2_sqr_Q7)));
/* 431 */         delta_lag_log2_sqr_Q7 = Inlines.silk_RSHIFT(Inlines.silk_SMULBB(delta_lag_log2_sqr_Q7, delta_lag_log2_sqr_Q7), 7);
/* 432 */         int prev_lag_bias_Q13 = Inlines.silk_RSHIFT(Inlines.silk_SMULBB(nb_subfr * 1638, LTPCorr_Q15.Val), 15);
/*     */         
/* 434 */         prev_lag_bias_Q13 = Inlines.silk_DIV32(Inlines.silk_MUL(prev_lag_bias_Q13, delta_lag_log2_sqr_Q7), delta_lag_log2_sqr_Q7 + 64);
/* 435 */         CCmax_new_b -= prev_lag_bias_Q13;
/*     */       } 
/*     */ 
/*     */       
/* 439 */       if (CCmax_new_b > CCmax_b && CCmax_new > 
/* 440 */         Inlines.silk_SMULBB(nb_subfr, search_thres2_Q13) && SilkTables.silk_CB_lags_stage2[0][CBimax_new] <= 16) {
/*     */         
/* 442 */         CCmax_b = CCmax_new_b;
/* 443 */         CCmax = CCmax_new;
/* 444 */         lag = d;
/* 445 */         CBimax = CBimax_new;
/*     */       } 
/*     */     } 
/*     */     
/* 449 */     if (lag == -1) {
/*     */       
/* 451 */       Arrays.MemSet(pitch_out, 0, nb_subfr);
/* 452 */       LTPCorr_Q15.Val = 0;
/* 453 */       lagIndex.Val = 0;
/* 454 */       contourIndex.Val = 0;
/*     */       
/* 456 */       return 1;
/*     */     } 
/*     */ 
/*     */     
/* 460 */     LTPCorr_Q15.Val = Inlines.silk_LSHIFT(Inlines.silk_DIV32_16(CCmax, nb_subfr), 2);
/* 461 */     Inlines.OpusAssert((LTPCorr_Q15.Val >= 0));
/*     */     
/* 463 */     if (Fs_kHz > 8) {
/*     */       short[] input_frame_ptr;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 473 */       boxed_energy.Val = 0;
/* 474 */       boxed_shift.Val = 0;
/* 475 */       SumSqrShift.silk_sum_sqr_shift(boxed_energy, boxed_shift, frame, frame_length);
/* 476 */       energy = boxed_energy.Val;
/* 477 */       shift = boxed_shift.Val;
/*     */       
/* 479 */       if (shift > 0) {
/* 480 */         short[] scratch_mem = new short[frame_length];
/*     */         
/* 482 */         shift = Inlines.silk_RSHIFT(shift, 1);
/* 483 */         for (i = 0; i < frame_length; i++) {
/* 484 */           scratch_mem[i] = Inlines.silk_RSHIFT16(frame[i], shift);
/*     */         }
/* 486 */         input_frame_ptr = scratch_mem;
/*     */       } else {
/* 488 */         input_frame_ptr = frame;
/*     */       } 
/*     */ 
/*     */       
/* 492 */       int CBimax_old = CBimax;
/*     */       
/* 494 */       Inlines.OpusAssert((lag == Inlines.silk_SAT16(lag)));
/* 495 */       if (Fs_kHz == 12) {
/* 496 */         lag = Inlines.silk_RSHIFT(Inlines.silk_SMULBB(lag, 3), 1);
/* 497 */       } else if (Fs_kHz == 16) {
/* 498 */         lag = Inlines.silk_LSHIFT(lag, 1);
/*     */       } else {
/* 500 */         lag = Inlines.silk_SMULBB(lag, 3);
/*     */       } 
/*     */       
/* 503 */       lag = Inlines.silk_LIMIT_int(lag, min_lag, max_lag);
/* 504 */       int start_lag = Inlines.silk_max_int(lag - 2, min_lag);
/* 505 */       int end_lag = Inlines.silk_min_int(lag + 2, max_lag);
/* 506 */       int lag_new = lag;
/*     */       
/* 508 */       CBimax = 0;
/*     */ 
/*     */       
/* 511 */       CCmax = Integer.MIN_VALUE;
/*     */       
/* 513 */       for (k = 0; k < nb_subfr; k++) {
/* 514 */         pitch_out[k] = lag + 2 * SilkTables.silk_CB_lags_stage2[k][CBimax_old];
/*     */       }
/*     */ 
/*     */       
/* 518 */       if (nb_subfr == 4) {
/* 519 */         nb_cbk_search = SilkTables.silk_nb_cbk_searchs_stage3[complexity];
/* 520 */         Lag_CB_ptr = SilkTables.silk_CB_lags_stage3;
/*     */       } else {
/* 522 */         nb_cbk_search = 12;
/* 523 */         Lag_CB_ptr = SilkTables.silk_CB_lags_stage3_10_ms;
/*     */       } 
/*     */ 
/*     */       
/* 527 */       silk_pe_stage3_vals[] energies_st3 = new silk_pe_stage3_vals[nb_subfr * nb_cbk_search];
/* 528 */       silk_pe_stage3_vals[] cross_corr_st3 = new silk_pe_stage3_vals[nb_subfr * nb_cbk_search];
/* 529 */       for (int c = 0; c < nb_subfr * nb_cbk_search; c++) {
/* 530 */         energies_st3[c] = new silk_pe_stage3_vals();
/* 531 */         cross_corr_st3[c] = new silk_pe_stage3_vals();
/*     */       } 
/* 533 */       silk_P_Ana_calc_corr_st3(cross_corr_st3, input_frame_ptr, start_lag, sf_length, nb_subfr, complexity);
/* 534 */       silk_P_Ana_calc_energy_st3(energies_st3, input_frame_ptr, start_lag, sf_length, nb_subfr, complexity);
/*     */       
/* 536 */       int lag_counter = 0;
/* 537 */       Inlines.OpusAssert((lag == Inlines.silk_SAT16(lag)));
/* 538 */       int contour_bias_Q15 = Inlines.silk_DIV32_16(1638, lag);
/*     */       
/* 540 */       target = input_frame_ptr;
/* 541 */       target_ptr = 20 * Fs_kHz;
/* 542 */       int energy_target = Inlines.silk_ADD32(Inlines.silk_inner_prod_self(target, target_ptr, nb_subfr * sf_length), 1);
/* 543 */       for (int d = start_lag; d <= end_lag; d++) {
/* 544 */         for (int j = 0; j < nb_cbk_search; j++) {
/* 545 */           int CCmax_new, cross_corr = 0;
/* 546 */           energy = energy_target;
/* 547 */           for (k = 0; k < nb_subfr; k++) {
/* 548 */             cross_corr = Inlines.silk_ADD32(cross_corr, 
/* 549 */                 (Inlines.MatrixGet(cross_corr_st3, k, j, nb_cbk_search)).Values[lag_counter]);
/*     */             
/* 551 */             energy = Inlines.silk_ADD32(energy, 
/* 552 */                 (Inlines.MatrixGet(energies_st3, k, j, nb_cbk_search)).Values[lag_counter]);
/*     */             
/* 554 */             Inlines.OpusAssert((energy >= 0));
/*     */           } 
/* 556 */           if (cross_corr > 0) {
/* 557 */             CCmax_new = Inlines.silk_DIV32_varQ(cross_corr, energy, 14);
/*     */ 
/*     */             
/* 560 */             int diff = 32767 - Inlines.silk_MUL(contour_bias_Q15, j);
/*     */             
/* 562 */             Inlines.OpusAssert((diff == Inlines.silk_SAT16(diff)));
/* 563 */             CCmax_new = Inlines.silk_SMULWB(CCmax_new, diff);
/*     */           } else {
/*     */             
/* 566 */             CCmax_new = 0;
/*     */           } 
/*     */           
/* 569 */           if (CCmax_new > CCmax && d + SilkTables.silk_CB_lags_stage3[0][j] <= max_lag) {
/* 570 */             CCmax = CCmax_new;
/* 571 */             lag_new = d;
/* 572 */             CBimax = j;
/*     */           } 
/*     */         } 
/* 575 */         lag_counter++;
/*     */       } 
/*     */       
/* 578 */       for (k = 0; k < nb_subfr; k++) {
/* 579 */         pitch_out[k] = lag_new + Lag_CB_ptr[k][CBimax];
/* 580 */         pitch_out[k] = Inlines.silk_LIMIT(pitch_out[k], min_lag, 18 * Fs_kHz);
/*     */       } 
/* 582 */       lagIndex.Val = (short)(lag_new - min_lag);
/* 583 */       contourIndex.Val = (byte)CBimax;
/*     */     }
/*     */     else {
/*     */       
/* 587 */       for (k = 0; k < nb_subfr; k++) {
/* 588 */         pitch_out[k] = lag + Lag_CB_ptr[k][CBimax];
/* 589 */         pitch_out[k] = Inlines.silk_LIMIT(pitch_out[k], 16, 144);
/*     */       } 
/* 591 */       lagIndex.Val = (short)(lag - 16);
/* 592 */       contourIndex.Val = (byte)CBimax;
/*     */     } 
/* 594 */     Inlines.OpusAssert((lagIndex.Val >= 0));
/*     */ 
/*     */     
/* 597 */     return 0;
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
/*     */   private static void silk_P_Ana_calc_corr_st3(silk_pe_stage3_vals[] cross_corr_st3, short[] frame, int start_lag, int sf_length, int nb_subfr, int complexity) {
/*     */     int nb_cbk_search;
/*     */     byte[][] Lag_range_ptr, Lag_CB_ptr;
/* 630 */     Inlines.OpusAssert((complexity >= 0));
/* 631 */     Inlines.OpusAssert((complexity <= 2));
/*     */     
/* 633 */     if (nb_subfr == 4) {
/* 634 */       Lag_range_ptr = SilkTables.silk_Lag_range_stage3[complexity];
/* 635 */       Lag_CB_ptr = SilkTables.silk_CB_lags_stage3;
/* 636 */       nb_cbk_search = SilkTables.silk_nb_cbk_searchs_stage3[complexity];
/*     */     } else {
/* 638 */       Inlines.OpusAssert((nb_subfr == 2));
/* 639 */       Lag_range_ptr = SilkTables.silk_Lag_range_stage3_10_ms;
/* 640 */       Lag_CB_ptr = SilkTables.silk_CB_lags_stage3_10_ms;
/* 641 */       nb_cbk_search = 12;
/*     */     } 
/* 643 */     int[] scratch_mem = new int[22];
/* 644 */     int[] xcorr32 = new int[22];
/*     */     
/* 646 */     int target_ptr = Inlines.silk_LSHIFT(sf_length, 2);
/*     */     
/* 648 */     for (int k = 0; k < nb_subfr; k++) {
/* 649 */       int lag_counter = 0;
/*     */ 
/*     */       
/* 652 */       int lag_low = Lag_range_ptr[k][0];
/* 653 */       int lag_high = Lag_range_ptr[k][1];
/* 654 */       Inlines.OpusAssert((lag_high - lag_low + 1 <= 22));
/* 655 */       CeltPitchXCorr.pitch_xcorr(frame, target_ptr, frame, target_ptr - start_lag - lag_high, xcorr32, sf_length, lag_high - lag_low + 1); int j;
/* 656 */       for (j = lag_low; j <= lag_high; j++) {
/* 657 */         Inlines.OpusAssert((lag_counter < 22));
/* 658 */         scratch_mem[lag_counter] = xcorr32[lag_high - j];
/* 659 */         lag_counter++;
/*     */       } 
/*     */       
/* 662 */       int delta = Lag_range_ptr[k][0];
/* 663 */       for (int i = 0; i < nb_cbk_search; i++) {
/*     */ 
/*     */         
/* 666 */         int idx = Lag_CB_ptr[k][i] - delta;
/* 667 */         for (j = 0; j < 5; j++) {
/* 668 */           Inlines.OpusAssert((idx + j < 22));
/* 669 */           Inlines.OpusAssert((idx + j < lag_counter));
/* 670 */           (Inlines.MatrixGet(cross_corr_st3, k, i, nb_cbk_search)).Values[j] = scratch_mem[idx + j];
/*     */         } 
/*     */       } 
/*     */       
/* 674 */       target_ptr += sf_length;
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_P_Ana_calc_energy_st3(silk_pe_stage3_vals[] energies_st3, short[] frame, int start_lag, int sf_length, int nb_subfr, int complexity) {
/*     */     int nb_cbk_search;
/*     */     byte[][] Lag_range_ptr, Lag_CB_ptr;
/* 703 */     Inlines.OpusAssert((complexity >= 0));
/* 704 */     Inlines.OpusAssert((complexity <= 2));
/*     */     
/* 706 */     if (nb_subfr == 4) {
/* 707 */       Lag_range_ptr = SilkTables.silk_Lag_range_stage3[complexity];
/* 708 */       Lag_CB_ptr = SilkTables.silk_CB_lags_stage3;
/* 709 */       nb_cbk_search = SilkTables.silk_nb_cbk_searchs_stage3[complexity];
/*     */     } else {
/* 711 */       Inlines.OpusAssert((nb_subfr == 2));
/* 712 */       Lag_range_ptr = SilkTables.silk_Lag_range_stage3_10_ms;
/* 713 */       Lag_CB_ptr = SilkTables.silk_CB_lags_stage3_10_ms;
/* 714 */       nb_cbk_search = 12;
/*     */     } 
/* 716 */     int[] scratch_mem = new int[22];
/*     */     
/* 718 */     int target_ptr = Inlines.silk_LSHIFT(sf_length, 2);
/* 719 */     for (int k = 0; k < nb_subfr; k++) {
/* 720 */       int lag_counter = 0;
/*     */ 
/*     */       
/* 723 */       int basis_ptr = target_ptr - start_lag + Lag_range_ptr[k][0];
/* 724 */       int energy = Inlines.silk_inner_prod_self(frame, basis_ptr, sf_length);
/* 725 */       Inlines.OpusAssert((energy >= 0));
/* 726 */       scratch_mem[lag_counter] = energy;
/* 727 */       lag_counter++;
/*     */       
/* 729 */       int lag_diff = Lag_range_ptr[k][1] - Lag_range_ptr[k][0] + 1; int i;
/* 730 */       for (i = 1; i < lag_diff; i++) {
/*     */         
/* 732 */         energy -= Inlines.silk_SMULBB(frame[basis_ptr + sf_length - i], frame[basis_ptr + sf_length - i]);
/* 733 */         Inlines.OpusAssert((energy >= 0));
/*     */ 
/*     */         
/* 736 */         energy = Inlines.silk_ADD_SAT32(energy, Inlines.silk_SMULBB(frame[basis_ptr - i], frame[basis_ptr - i]));
/* 737 */         Inlines.OpusAssert((energy >= 0));
/* 738 */         Inlines.OpusAssert((lag_counter < 22));
/* 739 */         scratch_mem[lag_counter] = energy;
/* 740 */         lag_counter++;
/*     */       } 
/*     */       
/* 743 */       int delta = Lag_range_ptr[k][0];
/* 744 */       for (i = 0; i < nb_cbk_search; i++) {
/*     */ 
/*     */         
/* 747 */         int idx = Lag_CB_ptr[k][i] - delta;
/* 748 */         for (int j = 0; j < 5; j++) {
/* 749 */           Inlines.OpusAssert((idx + j < 22));
/* 750 */           Inlines.OpusAssert((idx + j < lag_counter));
/* 751 */           (Inlines.MatrixGet(energies_st3, k, i, nb_cbk_search)).Values[j] = scratch_mem[idx + j];
/* 752 */           Inlines.OpusAssert(((Inlines.MatrixGet(energies_st3, k, i, nb_cbk_search)).Values[j] >= 0));
/*     */         } 
/*     */       } 
/* 755 */       target_ptr += sf_length;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\PitchAnalysisCore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */