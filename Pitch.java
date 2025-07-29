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
/*     */ class Pitch
/*     */ {
/*     */   static void find_best_pitch(int[] xcorr, int[] y, int len, int max_pitch, int[] best_pitch, int yshift, int maxcorr) {
/*  44 */     int Syy = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  49 */     int xshift = Inlines.celt_ilog2(maxcorr) - 14;
/*     */     
/*  51 */     int best_num_0 = -1;
/*  52 */     int best_num_1 = -1;
/*  53 */     int best_den_0 = 0;
/*  54 */     int best_den_1 = 0;
/*  55 */     best_pitch[0] = 0;
/*  56 */     best_pitch[1] = 1;
/*  57 */     for (int j = 0; j < len; j++) {
/*  58 */       Syy = Inlines.ADD32(Syy, Inlines.SHR32(Inlines.MULT16_16(y[j], y[j]), yshift));
/*     */     }
/*  60 */     for (int i = 0; i < max_pitch; i++) {
/*  61 */       if (xcorr[i] > 0) {
/*     */ 
/*     */         
/*  64 */         int xcorr16 = Inlines.EXTRACT16(Inlines.VSHR32(xcorr[i], xshift));
/*  65 */         int num = Inlines.MULT16_16_Q15(xcorr16, xcorr16);
/*  66 */         if (Inlines.MULT16_32_Q15(num, best_den_1) > Inlines.MULT16_32_Q15(best_num_1, Syy)) {
/*  67 */           if (Inlines.MULT16_32_Q15(num, best_den_0) > Inlines.MULT16_32_Q15(best_num_0, Syy)) {
/*  68 */             best_num_1 = best_num_0;
/*  69 */             best_den_1 = best_den_0;
/*  70 */             best_pitch[1] = best_pitch[0];
/*  71 */             best_num_0 = num;
/*  72 */             best_den_0 = Syy;
/*  73 */             best_pitch[0] = i;
/*     */           } else {
/*  75 */             best_num_1 = num;
/*  76 */             best_den_1 = Syy;
/*  77 */             best_pitch[1] = i;
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/*  82 */       Syy += Inlines.SHR32(Inlines.MULT16_16(y[i + len], y[i + len]), yshift) - Inlines.SHR32(Inlines.MULT16_16(y[i], y[i]), yshift);
/*  83 */       Syy = Inlines.MAX32(1, Syy);
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
/*     */   static void celt_fir5(int[] x, int[] num, int[] y, int N, int[] mem) {
/*  95 */     int num0 = num[0];
/*  96 */     int num1 = num[1];
/*  97 */     int num2 = num[2];
/*  98 */     int num3 = num[3];
/*  99 */     int num4 = num[4];
/* 100 */     int mem0 = mem[0];
/* 101 */     int mem1 = mem[1];
/* 102 */     int mem2 = mem[2];
/* 103 */     int mem3 = mem[3];
/* 104 */     int mem4 = mem[4];
/* 105 */     for (int i = 0; i < N; i++) {
/* 106 */       int sum = Inlines.SHL32(Inlines.EXTEND32(x[i]), 12);
/* 107 */       sum = Inlines.MAC16_16(sum, num0, mem0);
/* 108 */       sum = Inlines.MAC16_16(sum, num1, mem1);
/* 109 */       sum = Inlines.MAC16_16(sum, num2, mem2);
/* 110 */       sum = Inlines.MAC16_16(sum, num3, mem3);
/* 111 */       sum = Inlines.MAC16_16(sum, num4, mem4);
/* 112 */       mem4 = mem3;
/* 113 */       mem3 = mem2;
/* 114 */       mem2 = mem1;
/* 115 */       mem1 = mem0;
/* 116 */       mem0 = x[i];
/* 117 */       y[i] = Inlines.ROUND16(sum, 12);
/*     */     } 
/* 119 */     mem[0] = mem0;
/* 120 */     mem[1] = mem1;
/* 121 */     mem[2] = mem2;
/* 122 */     mem[3] = mem3;
/* 123 */     mem[4] = mem4;
/*     */   }
/*     */ 
/*     */   
/*     */   static void pitch_downsample(int[][] x, int[] x_lp, int len, int C) {
/* 128 */     int[] ac = new int[5];
/* 129 */     int tmp = 32767;
/* 130 */     int[] lpc = new int[4];
/* 131 */     int[] mem = { 0, 0, 0, 0, 0 };
/* 132 */     int[] lpc2 = new int[5];
/* 133 */     int c1 = 26214;
/*     */ 
/*     */     
/* 136 */     int maxabs = Inlines.celt_maxabs32(x[0], 0, len);
/* 137 */     if (C == 2) {
/* 138 */       int maxabs_1 = Inlines.celt_maxabs32(x[1], 0, len);
/* 139 */       maxabs = Inlines.MAX32(maxabs, maxabs_1);
/*     */     } 
/* 141 */     if (maxabs < 1) {
/* 142 */       maxabs = 1;
/*     */     }
/* 144 */     int shift = Inlines.celt_ilog2(maxabs) - 10;
/* 145 */     if (shift < 0) {
/* 146 */       shift = 0;
/*     */     }
/* 148 */     if (C == 2) {
/* 149 */       shift++;
/*     */     }
/*     */     
/* 152 */     int halflen = len >> 1; int i;
/* 153 */     for (i = 1; i < halflen; i++) {
/* 154 */       x_lp[i] = Inlines.SHR32(Inlines.HALF32(Inlines.HALF32(x[0][2 * i - 1] + x[0][2 * i + 1]) + x[0][2 * i]), shift);
/*     */     }
/*     */     
/* 157 */     x_lp[0] = Inlines.SHR32(Inlines.HALF32(Inlines.HALF32(x[0][1]) + x[0][0]), shift);
/*     */     
/* 159 */     if (C == 2) {
/* 160 */       for (i = 1; i < halflen; i++) {
/* 161 */         x_lp[i] = x_lp[i] + Inlines.SHR32(Inlines.HALF32(Inlines.HALF32(x[1][2 * i - 1] + x[1][2 * i + 1]) + x[1][2 * i]), shift);
/*     */       }
/* 163 */       x_lp[0] = x_lp[0] + Inlines.SHR32(Inlines.HALF32(Inlines.HALF32(x[1][1]) + x[1][0]), shift);
/*     */     } 
/*     */     
/* 166 */     Autocorrelation._celt_autocorr(x_lp, ac, null, 0, 4, halflen);
/*     */ 
/*     */     
/* 169 */     ac[0] = ac[0] + Inlines.SHR32(ac[0], 13);
/*     */     
/* 171 */     for (i = 1; i <= 4; i++)
/*     */     {
/* 173 */       ac[i] = ac[i] - Inlines.MULT16_32_Q15(2 * i * i, ac[i]);
/*     */     }
/*     */     
/* 176 */     CeltLPC.celt_lpc(lpc, ac, 4);
/* 177 */     for (i = 0; i < 4; i++) {
/* 178 */       tmp = Inlines.MULT16_16_Q15(29491, tmp);
/* 179 */       lpc[i] = Inlines.MULT16_16_Q15(lpc[i], tmp);
/*     */     } 
/*     */     
/* 182 */     lpc2[0] = lpc[0] + 3277;
/* 183 */     lpc2[1] = lpc[1] + Inlines.MULT16_16_Q15(c1, lpc[0]);
/* 184 */     lpc2[2] = lpc[2] + Inlines.MULT16_16_Q15(c1, lpc[1]);
/* 185 */     lpc2[3] = lpc[3] + Inlines.MULT16_16_Q15(c1, lpc[2]);
/* 186 */     lpc2[4] = Inlines.MULT16_16_Q15(c1, lpc[3]);
/*     */     
/* 188 */     celt_fir5(x_lp, lpc2, x_lp, halflen, mem);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void pitch_search(int[] x_lp, int x_lp_ptr, int[] y, int len, int max_pitch, BoxedValueInt pitch) {
/* 196 */     int offset, best_pitch[] = { 0, 0 };
/*     */ 
/*     */     
/* 199 */     int shift = 0;
/*     */ 
/*     */     
/* 202 */     Inlines.OpusAssert((len > 0));
/* 203 */     Inlines.OpusAssert((max_pitch > 0));
/* 204 */     int lag = len + max_pitch;
/*     */     
/* 206 */     int[] x_lp4 = new int[len >> 2];
/* 207 */     int[] y_lp4 = new int[lag >> 2];
/* 208 */     int[] xcorr = new int[max_pitch >> 1];
/*     */     
/*     */     int j;
/* 211 */     for (j = 0; j < len >> 2; j++) {
/* 212 */       x_lp4[j] = x_lp[x_lp_ptr + 2 * j];
/*     */     }
/* 214 */     for (j = 0; j < lag >> 2; j++) {
/* 215 */       y_lp4[j] = y[2 * j];
/*     */     }
/*     */     
/* 218 */     int xmax = Inlines.celt_maxabs32(x_lp4, 0, len >> 2);
/* 219 */     int ymax = Inlines.celt_maxabs32(y_lp4, 0, lag >> 2);
/* 220 */     shift = Inlines.celt_ilog2(Inlines.MAX32(1, Inlines.MAX32(xmax, ymax))) - 11;
/* 221 */     if (shift > 0) {
/* 222 */       for (j = 0; j < len >> 2; j++) {
/* 223 */         x_lp4[j] = Inlines.SHR16(x_lp4[j], shift);
/*     */       }
/* 225 */       for (j = 0; j < lag >> 2; j++) {
/* 226 */         y_lp4[j] = Inlines.SHR16(y_lp4[j], shift);
/*     */       }
/*     */       
/* 229 */       shift *= 2;
/*     */     } else {
/* 231 */       shift = 0;
/*     */     } 
/*     */ 
/*     */     
/* 235 */     int maxcorr = CeltPitchXCorr.pitch_xcorr(x_lp4, y_lp4, xcorr, len >> 2, max_pitch >> 2);
/*     */     
/* 237 */     find_best_pitch(xcorr, y_lp4, len >> 2, max_pitch >> 2, best_pitch, 0, maxcorr);
/*     */ 
/*     */     
/* 240 */     maxcorr = 1;
/* 241 */     for (int i = 0; i < max_pitch >> 1; i++) {
/*     */       
/* 243 */       xcorr[i] = 0;
/* 244 */       if (Inlines.abs(i - 2 * best_pitch[0]) <= 2 || Inlines.abs(i - 2 * best_pitch[1]) <= 2) {
/*     */ 
/*     */         
/* 247 */         int sum = 0;
/* 248 */         for (j = 0; j < len >> 1; j++) {
/* 249 */           sum += Inlines.SHR32(Inlines.MULT16_16(x_lp[x_lp_ptr + j], y[i + j]), shift);
/*     */         }
/*     */         
/* 252 */         xcorr[i] = Inlines.MAX32(-1, sum);
/* 253 */         maxcorr = Inlines.MAX32(maxcorr, sum);
/*     */       } 
/* 255 */     }  find_best_pitch(xcorr, y, len >> 1, max_pitch >> 1, best_pitch, shift + 1, maxcorr);
/*     */ 
/*     */     
/* 258 */     if (best_pitch[0] > 0 && best_pitch[0] < (max_pitch >> 1) - 1) {
/*     */       
/* 260 */       int a = xcorr[best_pitch[0] - 1];
/* 261 */       int b = xcorr[best_pitch[0]];
/* 262 */       int c = xcorr[best_pitch[0] + 1];
/* 263 */       if (c - a > Inlines.MULT16_32_Q15((short)22938, b - a)) {
/* 264 */         offset = 1;
/* 265 */       } else if (a - c > Inlines.MULT16_32_Q15((short)22938, b - c)) {
/* 266 */         offset = -1;
/*     */       } else {
/* 268 */         offset = 0;
/*     */       } 
/*     */     } else {
/* 271 */       offset = 0;
/*     */     } 
/*     */     
/* 274 */     pitch.Val = 2 * best_pitch[0] - offset;
/*     */   }
/*     */   
/* 277 */   private static final int[] second_check = new int[] { 0, 0, 3, 2, 3, 2, 5, 2, 3, 2, 3, 2, 5, 2, 3, 2 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int remove_doubling(int[] x, int maxperiod, int minperiod, int N, BoxedValueInt T0_, int prev_period, int prev_gain) {
/* 285 */     int pg, offset, xcorr[] = new int[3];
/*     */ 
/*     */     
/* 288 */     int minperiod0 = minperiod;
/* 289 */     maxperiod /= 2;
/* 290 */     minperiod /= 2;
/* 291 */     T0_.Val /= 2;
/* 292 */     prev_period /= 2;
/* 293 */     N /= 2;
/* 294 */     int x_ptr = maxperiod;
/* 295 */     if (T0_.Val >= maxperiod) {
/* 296 */       T0_.Val = maxperiod - 1;
/*     */     }
/*     */     
/* 299 */     int T0 = T0_.Val, T = T0;
/* 300 */     int[] yy_lookup = new int[maxperiod + 1];
/* 301 */     BoxedValueInt boxed_xx = new BoxedValueInt(0);
/* 302 */     BoxedValueInt boxed_xy = new BoxedValueInt(0);
/* 303 */     BoxedValueInt boxed_xy2 = new BoxedValueInt(0);
/* 304 */     Kernels.dual_inner_prod(x, x_ptr, x, x_ptr, x, x_ptr - T0, N, boxed_xx, boxed_xy);
/* 305 */     int xx = boxed_xx.Val;
/* 306 */     int xy = boxed_xy.Val;
/* 307 */     yy_lookup[0] = xx;
/* 308 */     int yy = xx;
/* 309 */     for (int i = 1; i <= maxperiod; i++) {
/* 310 */       int xi = x_ptr - i;
/* 311 */       yy = yy + Inlines.MULT16_16(x[xi], x[xi]) - Inlines.MULT16_16(x[xi + N], x[xi + N]);
/* 312 */       yy_lookup[i] = Inlines.MAX32(0, yy);
/*     */     } 
/* 314 */     yy = yy_lookup[T0];
/* 315 */     int best_xy = xy;
/* 316 */     int best_yy = yy;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 321 */     int x2y2 = 1 + Inlines.HALF32(Inlines.MULT32_32_Q31(xx, yy));
/* 322 */     int sh = Inlines.celt_ilog2(x2y2) >> 1;
/* 323 */     int t = Inlines.VSHR32(x2y2, 2 * (sh - 7));
/* 324 */     int g = Inlines.VSHR32(Inlines.MULT16_32_Q15(Inlines.celt_rsqrt_norm(t), xy), sh + 1);
/* 325 */     int g0 = g;
/*     */     
/*     */     int k;
/*     */     
/* 329 */     for (k = 2; k <= 15; k++) {
/*     */ 
/*     */       
/* 332 */       int T1b, cont = 0;
/*     */       
/* 334 */       int T1 = Inlines.celt_udiv(2 * T0 + k, 2 * k);
/* 335 */       if (T1 < minperiod) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 340 */       if (k == 2) {
/* 341 */         if (T1 + T0 > maxperiod) {
/* 342 */           T1b = T0;
/*     */         } else {
/* 344 */           T1b = T0 + T1;
/*     */         } 
/*     */       } else {
/* 347 */         T1b = Inlines.celt_udiv(2 * second_check[k] * T0 + k, 2 * k);
/*     */       } 
/*     */       
/* 350 */       Kernels.dual_inner_prod(x, x_ptr, x, x_ptr - T1, x, x_ptr - T1b, N, boxed_xy, boxed_xy2);
/* 351 */       xy = boxed_xy.Val;
/* 352 */       int xy2 = boxed_xy2.Val;
/*     */       
/* 354 */       xy += xy2;
/* 355 */       yy = yy_lookup[T1] + yy_lookup[T1b];
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 360 */       int j = 1 + Inlines.MULT32_32_Q31(xx, yy);
/* 361 */       int m = Inlines.celt_ilog2(j) >> 1;
/* 362 */       int n = Inlines.VSHR32(j, 2 * (m - 7));
/* 363 */       int g1 = Inlines.VSHR32(Inlines.MULT16_32_Q15(Inlines.celt_rsqrt_norm(n), xy), m + 1);
/*     */ 
/*     */       
/* 366 */       if (Inlines.abs(T1 - prev_period) <= 1) {
/* 367 */         cont = prev_gain;
/* 368 */       } else if (Inlines.abs(T1 - prev_period) <= 2 && 5 * k * k < T0) {
/* 369 */         cont = Inlines.HALF16(prev_gain);
/*     */       } else {
/* 371 */         cont = 0;
/*     */       } 
/* 373 */       int thresh = Inlines.MAX16(9830, Inlines.MULT16_16_Q15(22938, g0) - cont);
/*     */ 
/*     */ 
/*     */       
/* 377 */       if (T1 < 3 * minperiod) {
/* 378 */         thresh = Inlines.MAX16(13107, Inlines.MULT16_16_Q15(27853, g0) - cont);
/* 379 */       } else if (T1 < 2 * minperiod) {
/* 380 */         thresh = Inlines.MAX16(16384, Inlines.MULT16_16_Q15(29491, g0) - cont);
/*     */       } 
/* 382 */       if (g1 > thresh) {
/* 383 */         best_xy = xy;
/* 384 */         best_yy = yy;
/* 385 */         T = T1;
/* 386 */         g = g1;
/*     */       } 
/*     */     } 
/*     */     
/* 390 */     best_xy = Inlines.MAX32(0, best_xy);
/* 391 */     if (best_yy <= best_xy) {
/* 392 */       pg = 32767;
/*     */     } else {
/* 394 */       pg = Inlines.SHR32(Inlines.frac_div32(best_xy, best_yy + 1), 16);
/*     */     } 
/*     */     
/* 397 */     for (k = 0; k < 3; k++) {
/* 398 */       xcorr[k] = Kernels.celt_inner_prod(x, x_ptr, x, x_ptr - T + k - 1, N);
/*     */     }
/*     */     
/* 401 */     if (xcorr[2] - xcorr[0] > Inlines.MULT16_32_Q15((short)22938, xcorr[1] - xcorr[0])) {
/* 402 */       offset = 1;
/* 403 */     } else if (xcorr[0] - xcorr[2] > Inlines.MULT16_32_Q15((short)22938, xcorr[1] - xcorr[2])) {
/* 404 */       offset = -1;
/*     */     } else {
/* 406 */       offset = 0;
/*     */     } 
/*     */     
/* 409 */     if (pg > g) {
/* 410 */       pg = g;
/*     */     }
/*     */     
/* 413 */     T0_.Val = 2 * T + offset;
/*     */     
/* 415 */     if (T0_.Val < minperiod0) {
/* 416 */       T0_.Val = minperiod0;
/*     */     }
/*     */     
/* 419 */     return pg;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Pitch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */