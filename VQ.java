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
/*     */ class VQ
/*     */ {
/*     */   static void exp_rotation1(int[] X, int X_ptr, int len, int stride, int c, int s) {
/*  43 */     int Xptr = X_ptr;
/*  44 */     int ms = Inlines.NEG16(s); int i;
/*  45 */     for (i = 0; i < len - stride; i++) {
/*     */       
/*  47 */       int x1 = X[Xptr];
/*  48 */       int x2 = X[Xptr + stride];
/*  49 */       X[Xptr + stride] = Inlines.EXTRACT16(Inlines.PSHR32(Inlines.MAC16_16(Inlines.MULT16_16(c, x2), s, x1), 15));
/*  50 */       X[Xptr] = Inlines.EXTRACT16(Inlines.PSHR32(Inlines.MAC16_16(Inlines.MULT16_16(c, x1), ms, x2), 15));
/*  51 */       Xptr++;
/*     */     } 
/*  53 */     Xptr = X_ptr + len - 2 * stride - 1;
/*  54 */     for (i = len - 2 * stride - 1; i >= 0; i--) {
/*     */       
/*  56 */       int x1 = X[Xptr];
/*  57 */       int x2 = X[Xptr + stride];
/*  58 */       X[Xptr + stride] = Inlines.EXTRACT16(Inlines.PSHR32(Inlines.MAC16_16(Inlines.MULT16_16(c, x2), s, x1), 15));
/*  59 */       X[Xptr] = Inlines.EXTRACT16(Inlines.PSHR32(Inlines.MAC16_16(Inlines.MULT16_16(c, x1), ms, x2), 15));
/*  60 */       Xptr--;
/*     */     } 
/*     */   }
/*     */   
/*  64 */   private static int[] SPREAD_FACTOR = new int[] { 15, 10, 5 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void exp_rotation(int[] X, int X_ptr, int len, int dir, int stride, int K, int spread) {
/*  70 */     int stride2 = 0;
/*     */ 
/*     */     
/*  73 */     if (2 * K >= len || spread == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  77 */     int factor = SPREAD_FACTOR[spread - 1];
/*     */     
/*  79 */     int gain = Inlines.celt_div(Inlines.MULT16_16(32767, len), len + factor * K);
/*  80 */     int theta = Inlines.HALF16(Inlines.MULT16_16_Q15(gain, gain));
/*     */     
/*  82 */     int c = Inlines.celt_cos_norm(Inlines.EXTEND32(theta));
/*  83 */     int s = Inlines.celt_cos_norm(Inlines.EXTEND32(Inlines.SUB16(32767, theta)));
/*     */ 
/*     */     
/*  86 */     if (len >= 8 * stride) {
/*  87 */       stride2 = 1;
/*     */ 
/*     */       
/*  90 */       while ((stride2 * stride2 + stride2) * stride + (stride >> 2) < len) {
/*  91 */         stride2++;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  97 */     len = Inlines.celt_udiv(len, stride);
/*  98 */     for (int i = 0; i < stride; i++) {
/*  99 */       if (dir < 0) {
/* 100 */         if (stride2 != 0) {
/* 101 */           exp_rotation1(X, X_ptr + i * len, len, stride2, s, c);
/*     */         }
/*     */         
/* 104 */         exp_rotation1(X, X_ptr + i * len, len, 1, c, s);
/*     */       } else {
/* 106 */         exp_rotation1(X, X_ptr + i * len, len, 1, c, (short)(0 - s));
/*     */         
/* 108 */         if (stride2 != 0) {
/* 109 */           exp_rotation1(X, X_ptr + i * len, len, stride2, s, (short)(0 - c));
/*     */         }
/*     */       } 
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
/*     */   static void normalise_residual(int[] iy, int[] X, int X_ptr, int N, int Ryy, int gain) {
/* 126 */     int k = Inlines.celt_ilog2(Ryy) >> 1;
/* 127 */     int t = Inlines.VSHR32(Ryy, 2 * (k - 7));
/* 128 */     int g = Inlines.MULT16_16_P15(Inlines.celt_rsqrt_norm(t), gain);
/*     */     
/* 130 */     int i = 0;
/*     */     do {
/* 132 */       X[X_ptr + i] = Inlines.EXTRACT16(Inlines.PSHR32(Inlines.MULT16_16(g, iy[i]), k + 1));
/* 133 */     } while (++i < N);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int extract_collapse_mask(int[] iy, int N, int B) {
/* 140 */     if (B <= 1) {
/* 141 */       return 1;
/*     */     }
/*     */ 
/*     */     
/* 145 */     int N0 = Inlines.celt_udiv(N, B);
/* 146 */     int collapse_mask = 0;
/* 147 */     int i = 0;
/*     */     
/*     */     do {
/* 150 */       int tmp = 0;
/* 151 */       int j = 0;
/*     */       do {
/* 153 */         tmp |= iy[i * N0 + j];
/* 154 */       } while (++j < N0);
/*     */       
/* 156 */       collapse_mask |= ((tmp != 0) ? 1 : 0) << i;
/* 157 */     } while (++i < B);
/*     */     
/* 159 */     return collapse_mask;
/*     */   }
/*     */ 
/*     */   
/*     */   static int alg_quant(int[] X, int X_ptr, int N, int K, int spread, int B, EntropyCoder enc) {
/* 164 */     int[] y = new int[N];
/* 165 */     int[] iy = new int[N];
/* 166 */     int[] signx = new int[N];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     Inlines.OpusAssert((K > 0), "alg_quant() needs at least one pulse");
/* 176 */     Inlines.OpusAssert((N > 1), "alg_quant() needs at least two dimensions");
/*     */     
/* 178 */     exp_rotation(X, X_ptr, N, 1, B, K, spread);
/*     */ 
/*     */     
/* 181 */     int sum = 0;
/* 182 */     int j = 0;
/*     */     do {
/* 184 */       if (X[X_ptr + j] > 0) {
/* 185 */         signx[j] = 1;
/*     */       } else {
/* 187 */         signx[j] = -1;
/* 188 */         X[X_ptr + j] = 0 - X[X_ptr + j];
/*     */       } 
/*     */       
/* 191 */       iy[j] = 0;
/* 192 */       y[j] = 0;
/* 193 */     } while (++j < N);
/*     */     
/* 195 */     int yy = 0, xy = yy;
/*     */     
/* 197 */     int pulsesLeft = K;
/*     */ 
/*     */     
/* 200 */     if (K > N >> 1) {
/*     */       
/* 202 */       j = 0;
/*     */       do {
/* 204 */         sum += X[X_ptr + j];
/* 205 */       } while (++j < N);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 210 */       if (sum <= K) {
/* 211 */         X[X_ptr] = 16384;
/* 212 */         j = X_ptr + 1;
/*     */         do {
/* 214 */           X[j] = 0;
/* 215 */         } while (++j < N + X_ptr);
/*     */         
/* 217 */         sum = 16384;
/*     */       } 
/*     */       
/* 220 */       int rcp = Inlines.EXTRACT16(Inlines.MULT16_32_Q16(K - 1, Inlines.celt_rcp(sum)));
/* 221 */       j = 0;
/*     */ 
/*     */       
/*     */       do {
/* 225 */         iy[j] = Inlines.MULT16_16_Q15(X[X_ptr + j], rcp);
/* 226 */         y[j] = iy[j];
/* 227 */         yy = Inlines.MAC16_16(yy, y[j], y[j]);
/* 228 */         xy = Inlines.MAC16_16(xy, X[X_ptr + j], y[j]);
/* 229 */         y[j] = y[j] * 2;
/* 230 */         pulsesLeft -= iy[j];
/* 231 */       } while (++j < N);
/*     */     } 
/*     */     
/* 234 */     Inlines.OpusAssert((pulsesLeft >= 1), "Allocated too many pulses in the quick pass");
/*     */ 
/*     */ 
/*     */     
/* 238 */     if (pulsesLeft > N + 3) {
/* 239 */       int tmp = pulsesLeft;
/* 240 */       yy = Inlines.MAC16_16(yy, tmp, tmp);
/* 241 */       yy = Inlines.MAC16_16(yy, tmp, y[0]);
/* 242 */       iy[0] = iy[0] + pulsesLeft;
/* 243 */       pulsesLeft = 0;
/*     */     } 
/*     */     
/* 246 */     int s = 1;
/* 247 */     for (int i = 0; i < pulsesLeft; i++) {
/*     */       
/* 249 */       int best_num = -32767;
/* 250 */       int best_den = 0;
/* 251 */       int rshift = 1 + Inlines.celt_ilog2(K - pulsesLeft + i + 1);
/* 252 */       int best_id = 0;
/*     */ 
/*     */       
/* 255 */       yy = Inlines.ADD16(yy, 1);
/* 256 */       j = 0;
/*     */ 
/*     */       
/*     */       do {
/* 260 */         int Rxy = Inlines.EXTRACT16(Inlines.SHR32(Inlines.ADD32(xy, Inlines.EXTEND32(X[X_ptr + j])), rshift));
/*     */         
/* 262 */         int Ryy = Inlines.ADD16(yy, y[j]);
/*     */ 
/*     */ 
/*     */         
/* 266 */         Rxy = Inlines.MULT16_16_Q15(Rxy, Rxy);
/*     */ 
/*     */ 
/*     */         
/* 270 */         if (Inlines.MULT16_16(best_den, Rxy) <= Inlines.MULT16_16(Ryy, best_num))
/* 271 */           continue;  best_den = Ryy;
/* 272 */         best_num = Rxy;
/* 273 */         best_id = j;
/*     */       }
/* 275 */       while (++j < N);
/*     */ 
/*     */       
/* 278 */       xy = Inlines.ADD32(xy, Inlines.EXTEND32(X[X_ptr + best_id]));
/*     */       
/* 280 */       yy = Inlines.ADD16(yy, y[best_id]);
/*     */ 
/*     */ 
/*     */       
/* 284 */       y[best_id] = y[best_id] + 2 * s;
/* 285 */       iy[best_id] = iy[best_id] + 1;
/*     */     } 
/*     */ 
/*     */     
/* 289 */     j = 0;
/*     */     do {
/* 291 */       X[X_ptr + j] = Inlines.MULT16_16(signx[j], X[X_ptr + j]);
/* 292 */       if (signx[j] >= 0)
/* 293 */         continue;  iy[j] = -iy[j];
/*     */     }
/* 295 */     while (++j < N);
/*     */     
/* 297 */     CWRS.encode_pulses(iy, N, K, enc);
/*     */     
/* 299 */     int collapse_mask = extract_collapse_mask(iy, N, B);
/*     */     
/* 301 */     return collapse_mask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int alg_unquant(int[] X, int X_ptr, int N, int K, int spread, int B, EntropyCoder dec, int gain) {
/* 312 */     int[] iy = new int[N];
/* 313 */     Inlines.OpusAssert((K > 0), "alg_unquant() needs at least one pulse");
/* 314 */     Inlines.OpusAssert((N > 1), "alg_unquant() needs at least two dimensions");
/* 315 */     int Ryy = CWRS.decode_pulses(iy, N, K, dec);
/* 316 */     normalise_residual(iy, X, X_ptr, N, Ryy, gain);
/* 317 */     exp_rotation(X, X_ptr, N, -1, B, K, spread);
/* 318 */     int collapse_mask = extract_collapse_mask(iy, N, B);
/*     */     
/* 320 */     return collapse_mask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void renormalise_vector(int[] X, int X_ptr, int N, int gain) {
/* 330 */     int E = 1 + Kernels.celt_inner_prod(X, X_ptr, X, X_ptr, N);
/* 331 */     int k = Inlines.celt_ilog2(E) >> 1;
/* 332 */     int t = Inlines.VSHR32(E, 2 * (k - 7));
/* 333 */     int g = Inlines.MULT16_16_P15(Inlines.celt_rsqrt_norm(t), gain);
/*     */     
/* 335 */     int xptr = X_ptr;
/* 336 */     for (int i = 0; i < N; i++) {
/* 337 */       X[xptr] = Inlines.EXTRACT16(Inlines.PSHR32(Inlines.MULT16_16(g, X[xptr]), k + 1));
/* 338 */       xptr++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int stereo_itheta(int[] X, int X_ptr, int[] Y, int Y_ptr, int stereo, int N) {
/* 349 */     int Eside = 1, Emid = Eside;
/* 350 */     if (stereo != 0) {
/* 351 */       for (int i = 0; i < N; i++) {
/*     */         
/* 353 */         int m = Inlines.ADD16(Inlines.SHR16(X[X_ptr + i], 1), Inlines.SHR16(Y[Y_ptr + i], 1));
/* 354 */         int s = Inlines.SUB16(Inlines.SHR16(X[X_ptr + i], 1), Inlines.SHR16(Y[Y_ptr + i], 1));
/* 355 */         Emid = Inlines.MAC16_16(Emid, m, m);
/* 356 */         Eside = Inlines.MAC16_16(Eside, s, s);
/*     */       } 
/*     */     } else {
/* 359 */       Emid += Kernels.celt_inner_prod(X, X_ptr, X, X_ptr, N);
/* 360 */       Eside += Kernels.celt_inner_prod(Y, Y_ptr, Y, Y_ptr, N);
/*     */     } 
/* 362 */     int mid = Inlines.celt_sqrt(Emid);
/* 363 */     int side = Inlines.celt_sqrt(Eside);
/*     */     
/* 365 */     int itheta = Inlines.MULT16_16_Q15(20861, Inlines.celt_atan2p(side, mid));
/*     */     
/* 367 */     return itheta;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\VQ.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */