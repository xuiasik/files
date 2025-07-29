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
/*     */ class Stereo
/*     */ {
/*     */   static void silk_stereo_decode_pred(EntropyCoder psRangeDec, int[] pred_Q13) {
/*  45 */     int[][] ix = Arrays.InitTwoDimensionalArrayInt(2, 3);
/*     */ 
/*     */ 
/*     */     
/*  49 */     int n = psRangeDec.dec_icdf(SilkTables.silk_stereo_pred_joint_iCDF, 8);
/*  50 */     ix[0][2] = Inlines.silk_DIV32_16(n, 5);
/*  51 */     ix[1][2] = n - 5 * ix[0][2];
/*  52 */     for (n = 0; n < 2; n++) {
/*  53 */       ix[n][0] = psRangeDec.dec_icdf(SilkTables.silk_uniform3_iCDF, 8);
/*  54 */       ix[n][1] = psRangeDec.dec_icdf(SilkTables.silk_uniform5_iCDF, 8);
/*     */     } 
/*     */ 
/*     */     
/*  58 */     for (n = 0; n < 2; n++) {
/*  59 */       ix[n][0] = ix[n][0] + 3 * ix[n][2];
/*  60 */       int low_Q13 = SilkTables.silk_stereo_pred_quant_Q13[ix[n][0]];
/*  61 */       int step_Q13 = Inlines.silk_SMULWB(SilkTables.silk_stereo_pred_quant_Q13[ix[n][0] + 1] - low_Q13, 6554);
/*     */       
/*  63 */       pred_Q13[n] = Inlines.silk_SMLABB(low_Q13, step_Q13, 2 * ix[n][1] + 1);
/*     */     } 
/*     */ 
/*     */     
/*  67 */     pred_Q13[0] = pred_Q13[0] - pred_Q13[1];
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
/*     */   static void silk_stereo_decode_mid_only(EntropyCoder psRangeDec, BoxedValueInt decode_only_mid) {
/*  80 */     decode_only_mid.Val = psRangeDec.dec_icdf(SilkTables.silk_stereo_only_code_mid_iCDF, 8);
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
/*     */   static void silk_stereo_encode_pred(EntropyCoder psRangeEnc, byte[][] ix) {
/*  92 */     int n = 5 * ix[0][2] + ix[1][2];
/*  93 */     Inlines.OpusAssert((n < 25));
/*  94 */     psRangeEnc.enc_icdf(n, SilkTables.silk_stereo_pred_joint_iCDF, 8);
/*  95 */     for (n = 0; n < 2; n++) {
/*  96 */       Inlines.OpusAssert((ix[n][0] < 3));
/*  97 */       Inlines.OpusAssert((ix[n][1] < 5));
/*  98 */       psRangeEnc.enc_icdf(ix[n][0], SilkTables.silk_uniform3_iCDF, 8);
/*  99 */       psRangeEnc.enc_icdf(ix[n][1], SilkTables.silk_uniform5_iCDF, 8);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_stereo_encode_mid_only(EntropyCoder psRangeEnc, byte mid_only_flag) {
/* 110 */     psRangeEnc.enc_icdf(mid_only_flag, SilkTables.silk_stereo_only_code_mid_iCDF, 8);
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
/*     */   static int silk_stereo_find_predictor(BoxedValueInt ratio_Q14, short[] x, short[] y, int[] mid_res_amp_Q0, int mid_res_amp_Q0_ptr, int length, int smooth_coef_Q16) {
/* 133 */     BoxedValueInt nrgx = new BoxedValueInt(0);
/* 134 */     BoxedValueInt nrgy = new BoxedValueInt(0);
/* 135 */     BoxedValueInt scale1 = new BoxedValueInt(0);
/* 136 */     BoxedValueInt scale2 = new BoxedValueInt(0);
/*     */ 
/*     */ 
/*     */     
/* 140 */     SumSqrShift.silk_sum_sqr_shift(nrgx, scale1, x, length);
/* 141 */     SumSqrShift.silk_sum_sqr_shift(nrgy, scale2, y, length);
/* 142 */     int scale = Inlines.silk_max_int(scale1.Val, scale2.Val);
/* 143 */     scale += scale & 0x1;
/*     */     
/* 145 */     nrgy.Val = Inlines.silk_RSHIFT32(nrgy.Val, scale - scale2.Val);
/* 146 */     nrgx.Val = Inlines.silk_RSHIFT32(nrgx.Val, scale - scale1.Val);
/* 147 */     nrgx.Val = Inlines.silk_max_int(nrgx.Val, 1);
/* 148 */     int corr = Inlines.silk_inner_prod_aligned_scale(x, y, scale, length);
/* 149 */     int pred_Q13 = Inlines.silk_DIV32_varQ(corr, nrgx.Val, 13);
/* 150 */     pred_Q13 = Inlines.silk_LIMIT(pred_Q13, -16384, 16384);
/* 151 */     int pred2_Q10 = Inlines.silk_SMULWB(pred_Q13, pred_Q13);
/*     */ 
/*     */     
/* 154 */     smooth_coef_Q16 = Inlines.silk_max_int(smooth_coef_Q16, Inlines.silk_abs(pred2_Q10));
/*     */ 
/*     */     
/* 157 */     Inlines.OpusAssert((smooth_coef_Q16 < 32768));
/* 158 */     scale = Inlines.silk_RSHIFT(scale, 1);
/* 159 */     mid_res_amp_Q0[mid_res_amp_Q0_ptr] = Inlines.silk_SMLAWB(mid_res_amp_Q0[mid_res_amp_Q0_ptr], 
/* 160 */         Inlines.silk_LSHIFT(Inlines.silk_SQRT_APPROX(nrgx.Val), scale) - mid_res_amp_Q0[mid_res_amp_Q0_ptr], smooth_coef_Q16);
/*     */     
/* 162 */     nrgy.Val = Inlines.silk_SUB_LSHIFT32(nrgy.Val, Inlines.silk_SMULWB(corr, pred_Q13), 4);
/* 163 */     nrgy.Val = Inlines.silk_ADD_LSHIFT32(nrgy.Val, Inlines.silk_SMULWB(nrgx.Val, pred2_Q10), 6);
/* 164 */     mid_res_amp_Q0[mid_res_amp_Q0_ptr + 1] = Inlines.silk_SMLAWB(mid_res_amp_Q0[mid_res_amp_Q0_ptr + 1], 
/* 165 */         Inlines.silk_LSHIFT(Inlines.silk_SQRT_APPROX(nrgy.Val), scale) - mid_res_amp_Q0[mid_res_amp_Q0_ptr + 1], smooth_coef_Q16);
/*     */ 
/*     */     
/* 168 */     ratio_Q14.Val = Inlines.silk_DIV32_varQ(mid_res_amp_Q0[mid_res_amp_Q0_ptr + 1], Inlines.silk_max(mid_res_amp_Q0[mid_res_amp_Q0_ptr], 1), 14);
/* 169 */     ratio_Q14.Val = Inlines.silk_LIMIT(ratio_Q14.Val, 0, 32767);
/*     */     
/* 171 */     return pred_Q13;
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
/*     */ 
/*     */   
/*     */   static void silk_stereo_LR_to_MS(StereoEncodeState state, short[] x1, int x1_ptr, short[] x2, int x2_ptr, byte[][] ix, BoxedValueByte mid_only_flag, int[] mid_side_rates_bps, int total_rate_bps, int prev_speech_act_Q8, int toMono, int fs_kHz, int frame_length) {
/* 204 */     int width_Q14, pred_Q13[] = new int[2];
/*     */     
/* 206 */     BoxedValueInt LP_ratio_Q14 = new BoxedValueInt(0);
/* 207 */     BoxedValueInt HP_ratio_Q14 = new BoxedValueInt(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     int mid = x1_ptr - 2;
/*     */     
/* 215 */     short[] side = new short[frame_length + 2];
/*     */     
/*     */     int n;
/* 218 */     for (n = 0; n < frame_length + 2; n++) {
/* 219 */       int sum = x1[x1_ptr + n - 2] + x2[x2_ptr + n - 2];
/* 220 */       int diff = x1[x1_ptr + n - 2] - x2[x2_ptr + n - 2];
/* 221 */       x1[mid + n] = (short)Inlines.silk_RSHIFT_ROUND(sum, 1);
/* 222 */       side[n] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(diff, 1));
/*     */     } 
/*     */ 
/*     */     
/* 226 */     System.arraycopy(state.sMid, 0, x1, mid, 2);
/* 227 */     System.arraycopy(state.sSide, 0, side, 0, 2);
/* 228 */     System.arraycopy(x1, mid + frame_length, state.sMid, 0, 2);
/* 229 */     System.arraycopy(side, frame_length, state.sSide, 0, 2);
/*     */ 
/*     */     
/* 232 */     short[] LP_mid = new short[frame_length];
/* 233 */     short[] HP_mid = new short[frame_length];
/* 234 */     for (n = 0; n < frame_length; n++) {
/* 235 */       int sum = Inlines.silk_RSHIFT_ROUND(Inlines.silk_ADD_LSHIFT32(x1[mid + n] + x1[mid + n + 2], x1[mid + n + 1], 1), 2);
/* 236 */       LP_mid[n] = (short)sum;
/* 237 */       HP_mid[n] = (short)(x1[mid + n + 1] - sum);
/*     */     } 
/*     */ 
/*     */     
/* 241 */     short[] LP_side = new short[frame_length];
/* 242 */     short[] HP_side = new short[frame_length];
/* 243 */     for (n = 0; n < frame_length; n++) {
/* 244 */       int sum = Inlines.silk_RSHIFT_ROUND(Inlines.silk_ADD_LSHIFT32(side[n] + side[n + 2], side[n + 1], 1), 2);
/* 245 */       LP_side[n] = (short)sum;
/* 246 */       HP_side[n] = (short)(side[n + 1] - sum);
/*     */     } 
/*     */ 
/*     */     
/* 250 */     int is10msFrame = (frame_length == 10 * fs_kHz) ? 1 : 0;
/*     */     
/* 252 */     int smooth_coef_Q16 = (is10msFrame != 0) ? 328 : 655;
/* 253 */     smooth_coef_Q16 = Inlines.silk_SMULWB(Inlines.silk_SMULBB(prev_speech_act_Q8, prev_speech_act_Q8), smooth_coef_Q16);
/*     */     
/* 255 */     pred_Q13[0] = silk_stereo_find_predictor(LP_ratio_Q14, LP_mid, LP_side, state.mid_side_amp_Q0, 0, frame_length, smooth_coef_Q16);
/* 256 */     pred_Q13[1] = silk_stereo_find_predictor(HP_ratio_Q14, HP_mid, HP_side, state.mid_side_amp_Q0, 2, frame_length, smooth_coef_Q16);
/*     */ 
/*     */     
/* 259 */     int frac_Q16 = Inlines.silk_SMLABB(HP_ratio_Q14.Val, LP_ratio_Q14.Val, 3);
/* 260 */     frac_Q16 = Inlines.silk_min(frac_Q16, 65536);
/*     */ 
/*     */     
/* 263 */     total_rate_bps -= (is10msFrame != 0) ? 1200 : 600;
/*     */     
/* 265 */     if (total_rate_bps < 1) {
/* 266 */       total_rate_bps = 1;
/*     */     }
/* 268 */     int min_mid_rate_bps = Inlines.silk_SMLABB(2000, fs_kHz, 900);
/* 269 */     Inlines.OpusAssert((min_mid_rate_bps < 32767));
/*     */     
/* 271 */     int frac_3_Q16 = Inlines.silk_MUL(3, frac_Q16);
/* 272 */     mid_side_rates_bps[0] = Inlines.silk_DIV32_varQ(total_rate_bps, 851968 + frac_3_Q16, 19);
/*     */     
/* 274 */     if (mid_side_rates_bps[0] < min_mid_rate_bps) {
/* 275 */       mid_side_rates_bps[0] = min_mid_rate_bps;
/* 276 */       mid_side_rates_bps[1] = total_rate_bps - mid_side_rates_bps[0];
/*     */       
/* 278 */       width_Q14 = Inlines.silk_DIV32_varQ(Inlines.silk_LSHIFT(mid_side_rates_bps[1], 1) - min_mid_rate_bps, 
/* 279 */           Inlines.silk_SMULWB(65536 + frac_3_Q16, min_mid_rate_bps), 16);
/* 280 */       width_Q14 = Inlines.silk_LIMIT(width_Q14, 0, 16384);
/*     */     } else {
/* 282 */       mid_side_rates_bps[1] = total_rate_bps - mid_side_rates_bps[0];
/* 283 */       width_Q14 = 16384;
/*     */     } 
/*     */ 
/*     */     
/* 287 */     state.smth_width_Q14 = (short)Inlines.silk_SMLAWB(state.smth_width_Q14, width_Q14 - state.smth_width_Q14, smooth_coef_Q16);
/*     */ 
/*     */     
/* 290 */     mid_only_flag.Val = 0;
/* 291 */     if (toMono != 0) {
/*     */       
/* 293 */       width_Q14 = 0;
/* 294 */       pred_Q13[0] = 0;
/* 295 */       pred_Q13[1] = 0;
/* 296 */       silk_stereo_quant_pred(pred_Q13, ix);
/* 297 */     } else if (state.width_prev_Q14 == 0 && (8 * total_rate_bps < 13 * min_mid_rate_bps || 
/* 298 */       Inlines.silk_SMULWB(frac_Q16, state.smth_width_Q14) < 819)) {
/*     */ 
/*     */       
/* 301 */       pred_Q13[0] = Inlines.silk_RSHIFT(Inlines.silk_SMULBB(state.smth_width_Q14, pred_Q13[0]), 14);
/* 302 */       pred_Q13[1] = Inlines.silk_RSHIFT(Inlines.silk_SMULBB(state.smth_width_Q14, pred_Q13[1]), 14);
/* 303 */       silk_stereo_quant_pred(pred_Q13, ix);
/*     */       
/* 305 */       width_Q14 = 0;
/* 306 */       pred_Q13[0] = 0;
/* 307 */       pred_Q13[1] = 0;
/* 308 */       mid_side_rates_bps[0] = total_rate_bps;
/* 309 */       mid_side_rates_bps[1] = 0;
/* 310 */       mid_only_flag.Val = 1;
/* 311 */     } else if (state.width_prev_Q14 != 0 && (8 * total_rate_bps < 11 * min_mid_rate_bps || 
/* 312 */       Inlines.silk_SMULWB(frac_Q16, state.smth_width_Q14) < 328)) {
/*     */ 
/*     */       
/* 315 */       pred_Q13[0] = Inlines.silk_RSHIFT(Inlines.silk_SMULBB(state.smth_width_Q14, pred_Q13[0]), 14);
/* 316 */       pred_Q13[1] = Inlines.silk_RSHIFT(Inlines.silk_SMULBB(state.smth_width_Q14, pred_Q13[1]), 14);
/* 317 */       silk_stereo_quant_pred(pred_Q13, ix);
/*     */       
/* 319 */       width_Q14 = 0;
/* 320 */       pred_Q13[0] = 0;
/* 321 */       pred_Q13[1] = 0;
/* 322 */     } else if (state.smth_width_Q14 > 15565) {
/*     */       
/* 324 */       silk_stereo_quant_pred(pred_Q13, ix);
/* 325 */       width_Q14 = 16384;
/*     */     } else {
/*     */       
/* 328 */       pred_Q13[0] = Inlines.silk_RSHIFT(Inlines.silk_SMULBB(state.smth_width_Q14, pred_Q13[0]), 14);
/* 329 */       pred_Q13[1] = Inlines.silk_RSHIFT(Inlines.silk_SMULBB(state.smth_width_Q14, pred_Q13[1]), 14);
/* 330 */       silk_stereo_quant_pred(pred_Q13, ix);
/* 331 */       width_Q14 = state.smth_width_Q14;
/*     */     } 
/*     */ 
/*     */     
/* 335 */     if (mid_only_flag.Val == 1) {
/* 336 */       state.silent_side_len = (short)(state.silent_side_len + (short)(frame_length - 8 * fs_kHz));
/* 337 */       if (state.silent_side_len < 5 * fs_kHz) {
/* 338 */         mid_only_flag.Val = 0;
/*     */       } else {
/*     */         
/* 341 */         state.silent_side_len = 10000;
/*     */       } 
/*     */     } else {
/* 344 */       state.silent_side_len = 0;
/*     */     } 
/*     */     
/* 347 */     if (mid_only_flag.Val == 0 && mid_side_rates_bps[1] < 1) {
/* 348 */       mid_side_rates_bps[1] = 1;
/* 349 */       mid_side_rates_bps[0] = Inlines.silk_max_int(1, total_rate_bps - mid_side_rates_bps[1]);
/*     */     } 
/*     */ 
/*     */     
/* 353 */     int pred0_Q13 = -state.pred_prev_Q13[0];
/* 354 */     int pred1_Q13 = -state.pred_prev_Q13[1];
/* 355 */     int w_Q24 = Inlines.silk_LSHIFT(state.width_prev_Q14, 10);
/* 356 */     int denom_Q16 = Inlines.silk_DIV32_16(65536, 8 * fs_kHz);
/* 357 */     int delta0_Q13 = 0 - Inlines.silk_RSHIFT_ROUND(Inlines.silk_SMULBB(pred_Q13[0] - state.pred_prev_Q13[0], denom_Q16), 16);
/* 358 */     int delta1_Q13 = 0 - Inlines.silk_RSHIFT_ROUND(Inlines.silk_SMULBB(pred_Q13[1] - state.pred_prev_Q13[1], denom_Q16), 16);
/* 359 */     int deltaw_Q24 = Inlines.silk_LSHIFT(Inlines.silk_SMULWB(width_Q14 - state.width_prev_Q14, denom_Q16), 10);
/* 360 */     for (n = 0; n < 8 * fs_kHz; n++) {
/* 361 */       pred0_Q13 += delta0_Q13;
/* 362 */       pred1_Q13 += delta1_Q13;
/* 363 */       w_Q24 += deltaw_Q24;
/* 364 */       int sum = Inlines.silk_LSHIFT(Inlines.silk_ADD_LSHIFT(x1[mid + n] + x1[mid + n + 2], x1[mid + n + 1], 1), 9);
/*     */       
/* 366 */       sum = Inlines.silk_SMLAWB(Inlines.silk_SMULWB(w_Q24, side[n + 1]), sum, pred0_Q13);
/*     */       
/* 368 */       sum = Inlines.silk_SMLAWB(sum, Inlines.silk_LSHIFT(x1[mid + n + 1], 11), pred1_Q13);
/*     */       
/* 370 */       x2[x2_ptr + n - 1] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(sum, 8));
/*     */     } 
/*     */     
/* 373 */     pred0_Q13 = 0 - pred_Q13[0];
/* 374 */     pred1_Q13 = 0 - pred_Q13[1];
/* 375 */     w_Q24 = Inlines.silk_LSHIFT(width_Q14, 10);
/* 376 */     for (n = 8 * fs_kHz; n < frame_length; n++) {
/* 377 */       int sum = Inlines.silk_LSHIFT(Inlines.silk_ADD_LSHIFT(x1[mid + n] + x1[mid + n + 2], x1[mid + n + 1], 1), 9);
/*     */       
/* 379 */       sum = Inlines.silk_SMLAWB(Inlines.silk_SMULWB(w_Q24, side[n + 1]), sum, pred0_Q13);
/*     */       
/* 381 */       sum = Inlines.silk_SMLAWB(sum, Inlines.silk_LSHIFT(x1[mid + n + 1], 11), pred1_Q13);
/*     */       
/* 383 */       x2[x2_ptr + n - 1] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(sum, 8));
/*     */     } 
/* 385 */     state.pred_prev_Q13[0] = (short)pred_Q13[0];
/* 386 */     state.pred_prev_Q13[1] = (short)pred_Q13[1];
/* 387 */     state.width_prev_Q14 = (short)width_Q14;
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
/*     */   static void silk_stereo_MS_to_LR(StereoDecodeState state, short[] x1, int x1_ptr, short[] x2, int x2_ptr, int[] pred_Q13, int fs_kHz, int frame_length) {
/* 412 */     System.arraycopy(state.sMid, 0, x1, x1_ptr, 2);
/* 413 */     System.arraycopy(state.sSide, 0, x2, x2_ptr, 2);
/* 414 */     System.arraycopy(x1, x1_ptr + frame_length, state.sMid, 0, 2);
/* 415 */     System.arraycopy(x2, x2_ptr + frame_length, state.sSide, 0, 2);
/*     */ 
/*     */     
/* 418 */     int pred0_Q13 = state.pred_prev_Q13[0];
/* 419 */     int pred1_Q13 = state.pred_prev_Q13[1];
/* 420 */     int denom_Q16 = Inlines.silk_DIV32_16(65536, 8 * fs_kHz);
/* 421 */     int delta0_Q13 = Inlines.silk_RSHIFT_ROUND(Inlines.silk_SMULBB(pred_Q13[0] - state.pred_prev_Q13[0], denom_Q16), 16);
/* 422 */     int delta1_Q13 = Inlines.silk_RSHIFT_ROUND(Inlines.silk_SMULBB(pred_Q13[1] - state.pred_prev_Q13[1], denom_Q16), 16); int n;
/* 423 */     for (n = 0; n < 8 * fs_kHz; n++) {
/* 424 */       pred0_Q13 += delta0_Q13;
/* 425 */       pred1_Q13 += delta1_Q13;
/* 426 */       int sum = Inlines.silk_LSHIFT(Inlines.silk_ADD_LSHIFT(x1[x1_ptr + n] + x1[x1_ptr + n + 2], x1[x1_ptr + n + 1], 1), 9);
/*     */       
/* 428 */       sum = Inlines.silk_SMLAWB(Inlines.silk_LSHIFT(x2[x2_ptr + n + 1], 8), sum, pred0_Q13);
/*     */       
/* 430 */       sum = Inlines.silk_SMLAWB(sum, Inlines.silk_LSHIFT(x1[x1_ptr + n + 1], 11), pred1_Q13);
/*     */       
/* 432 */       x2[x2_ptr + n + 1] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(sum, 8));
/*     */     } 
/* 434 */     pred0_Q13 = pred_Q13[0];
/* 435 */     pred1_Q13 = pred_Q13[1];
/* 436 */     for (n = 8 * fs_kHz; n < frame_length; n++) {
/* 437 */       int sum = Inlines.silk_LSHIFT(Inlines.silk_ADD_LSHIFT(x1[x1_ptr + n] + x1[x1_ptr + n + 2], x1[x1_ptr + n + 1], 1), 9);
/*     */       
/* 439 */       sum = Inlines.silk_SMLAWB(Inlines.silk_LSHIFT(x2[x2_ptr + n + 1], 8), sum, pred0_Q13);
/*     */       
/* 441 */       sum = Inlines.silk_SMLAWB(sum, Inlines.silk_LSHIFT(x1[x1_ptr + n + 1], 11), pred1_Q13);
/*     */       
/* 443 */       x2[x2_ptr + n + 1] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(sum, 8));
/*     */     } 
/* 445 */     state.pred_prev_Q13[0] = (short)pred_Q13[0];
/* 446 */     state.pred_prev_Q13[1] = (short)pred_Q13[1];
/*     */ 
/*     */     
/* 449 */     for (n = 0; n < frame_length; n++) {
/* 450 */       int sum = x1[x1_ptr + n + 1] + x2[x2_ptr + n + 1];
/* 451 */       int diff = x1[x1_ptr + n + 1] - x2[x2_ptr + n + 1];
/* 452 */       x1[x1_ptr + n + 1] = (short)Inlines.silk_SAT16(sum);
/* 453 */       x2[x2_ptr + n + 1] = (short)Inlines.silk_SAT16(diff);
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
/*     */   static void silk_stereo_quant_pred(int[] pred_Q13, byte[][] ix) {
/* 467 */     int quant_pred_Q13 = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 472 */     Arrays.MemSet(ix[0], (byte)0, 3);
/* 473 */     Arrays.MemSet(ix[1], (byte)0, 3);
/*     */ 
/*     */     
/* 476 */     for (int n = 0; n < 2; n++) {
/* 477 */       boolean done = false;
/*     */ 
/*     */       
/* 480 */       int err_min_Q13 = Integer.MAX_VALUE;
/* 481 */       for (byte i = 0; !done && i < 15; i = (byte)(i + 1)) {
/* 482 */         int low_Q13 = SilkTables.silk_stereo_pred_quant_Q13[i];
/* 483 */         int step_Q13 = Inlines.silk_SMULWB(SilkTables.silk_stereo_pred_quant_Q13[i + 1] - low_Q13, 6554);
/*     */ 
/*     */         
/* 486 */         for (byte j = 0; !done && j < 5; j = (byte)(j + 1)) {
/* 487 */           int lvl_Q13 = Inlines.silk_SMLABB(low_Q13, step_Q13, 2 * j + 1);
/* 488 */           int err_Q13 = Inlines.silk_abs(pred_Q13[n] - lvl_Q13);
/* 489 */           if (err_Q13 < err_min_Q13) {
/* 490 */             err_min_Q13 = err_Q13;
/* 491 */             quant_pred_Q13 = lvl_Q13;
/* 492 */             ix[n][0] = i;
/* 493 */             ix[n][1] = j;
/*     */           } else {
/*     */             
/* 496 */             done = true;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 501 */       ix[n][2] = (byte)Inlines.silk_DIV32_16(ix[n][0], 3);
/* 502 */       ix[n][0] = (byte)(ix[n][0] - (byte)(ix[n][2] * 3));
/* 503 */       pred_Q13[n] = quant_pred_Q13;
/*     */     } 
/*     */ 
/*     */     
/* 507 */     pred_Q13[0] = pred_Q13[0] - pred_Q13[1];
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Stereo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */