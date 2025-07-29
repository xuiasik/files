/*      */ package de.maxhenkel.voicechat.concentus;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class Bands
/*      */ {
/*      */   static int hysteresis_decision(int val, int[] thresholds, int[] hysteresis, int N, int prev) {
/*      */     int i;
/*   46 */     for (i = 0; i < N && 
/*   47 */       val >= thresholds[i]; i++);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   52 */     if (i > prev && val < thresholds[prev] + hysteresis[prev]) {
/*   53 */       i = prev;
/*      */     }
/*      */     
/*   56 */     if (i < prev && val > thresholds[prev - 1] - hysteresis[prev - 1]) {
/*   57 */       i = prev;
/*      */     }
/*      */     
/*   60 */     return i;
/*      */   }
/*      */   
/*      */   static int celt_lcg_rand(int seed) {
/*   64 */     return 1664525 * seed + 1013904223;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int bitexact_cos(int x) {
/*   72 */     int tmp = 4096 + x * x >> 13;
/*   73 */     Inlines.OpusAssert((tmp <= 32767));
/*   74 */     int x2 = tmp;
/*   75 */     x2 = 32767 - x2 + Inlines.FRAC_MUL16(x2, -7651 + Inlines.FRAC_MUL16(x2, 8277 + Inlines.FRAC_MUL16(-626, x2)));
/*   76 */     Inlines.OpusAssert((x2 <= 32766));
/*   77 */     return 1 + x2;
/*      */   }
/*      */   
/*      */   static int bitexact_log2tan(int isin, int icos) {
/*   81 */     int lc = Inlines.EC_ILOG(icos);
/*   82 */     int ls = Inlines.EC_ILOG(isin);
/*   83 */     icos <<= 15 - lc;
/*   84 */     isin <<= 15 - ls;
/*   85 */     return (ls - lc) * 2048 + 
/*   86 */       Inlines.FRAC_MUL16(isin, Inlines.FRAC_MUL16(isin, -2597) + 7932) - 
/*   87 */       Inlines.FRAC_MUL16(icos, Inlines.FRAC_MUL16(icos, -2597) + 7932);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void compute_band_energies(CeltMode m, int[][] X, int[][] bandE, int end, int C, int LM) {
/*   93 */     short[] eBands = m.eBands;
/*   94 */     int N = m.shortMdctSize << LM;
/*   95 */     int c = 0;
/*      */     
/*      */     do {
/*   98 */       for (int i = 0; i < end; i++) {
/*      */         
/*  100 */         int maxval = 0;
/*  101 */         int sum = 0;
/*  102 */         maxval = Inlines.celt_maxabs32(X[c], eBands[i] << LM, eBands[i + 1] - eBands[i] << LM);
/*  103 */         if (maxval > 0) {
/*  104 */           int shift = Inlines.celt_ilog2(maxval) - 14 + ((m.logN[i] >> 3) + LM + 1 >> 1);
/*  105 */           int j = eBands[i] << LM;
/*  106 */           if (shift > 0) {
/*      */             do {
/*  108 */               sum = Inlines.MAC16_16(sum, Inlines.EXTRACT16(Inlines.SHR32(X[c][j], shift)), 
/*  109 */                   Inlines.EXTRACT16(Inlines.SHR32(X[c][j], shift)));
/*  110 */             } while (++j < eBands[i + 1] << LM);
/*      */           } else {
/*      */             do {
/*  113 */               sum = Inlines.MAC16_16(sum, Inlines.EXTRACT16(Inlines.SHL32(X[c][j], -shift)), 
/*  114 */                   Inlines.EXTRACT16(Inlines.SHL32(X[c][j], -shift)));
/*  115 */             } while (++j < eBands[i + 1] << LM);
/*      */           } 
/*      */           
/*  118 */           bandE[c][i] = 1 + Inlines.VSHR32(Inlines.celt_sqrt(sum), -shift);
/*      */         } else {
/*  120 */           bandE[c][i] = 1;
/*      */         }
/*      */       
/*      */       } 
/*  124 */     } while (++c < C);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void normalise_bands(CeltMode m, int[][] freq, int[][] X, int[][] bandE, int end, int C, int M) {
/*  130 */     short[] eBands = m.eBands;
/*  131 */     int c = 0;
/*      */     while (true) {
/*  133 */       int i = 0;
/*      */ 
/*      */ 
/*      */       
/*      */       while (true) {
/*  138 */         int shift = Inlines.celt_zlog2(bandE[c][i]) - 13;
/*  139 */         int E = Inlines.VSHR32(bandE[c][i], shift);
/*  140 */         int g = Inlines.EXTRACT16(Inlines.celt_rcp(Inlines.SHL32(E, 3)));
/*  141 */         int j = M * eBands[i];
/*      */         do {
/*  143 */           X[c][j] = Inlines.MULT16_16_Q15(Inlines.VSHR32(freq[c][j], shift - 1), g);
/*  144 */         } while (++j < M * eBands[i + 1] || 
/*  145 */           ++i < end || 
/*  146 */           ++c < C);
/*      */         break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void denormalise_bands(CeltMode m, int[] X, int[] freq, int freq_ptr, int[] bandLogE, int bandLogE_ptr, int start, int end, int M, int downsample, int silence) {
/*  157 */     short[] eBands = m.eBands;
/*  158 */     int N = M * m.shortMdctSize;
/*  159 */     int bound = M * eBands[end];
/*  160 */     if (downsample != 1) {
/*  161 */       bound = Inlines.IMIN(bound, N / downsample);
/*      */     }
/*  163 */     if (silence != 0) {
/*  164 */       bound = 0;
/*  165 */       start = end = 0;
/*      */     } 
/*  167 */     int f = freq_ptr;
/*  168 */     int x = M * eBands[start];
/*      */     int i;
/*  170 */     for (i = 0; i < M * eBands[start]; i++) {
/*  171 */       freq[f++] = 0;
/*      */     }
/*      */     
/*  174 */     for (i = start; i < end; i++) {
/*      */       char c;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  180 */       int j = M * eBands[i];
/*  181 */       int band_end = M * eBands[i + 1];
/*  182 */       int lg = Inlines.ADD16(bandLogE[bandLogE_ptr + i], Inlines.SHL16((short)CeltTables.eMeans[i], 6));
/*      */ 
/*      */       
/*  185 */       int shift = 16 - (lg >> 10);
/*  186 */       if (shift > 31) {
/*  187 */         shift = 0;
/*  188 */         c = Character.MIN_VALUE;
/*      */       } else {
/*      */         
/*  191 */         c = Inlines.celt_exp2_frac(lg & 0x3FF);
/*      */       } 
/*      */       
/*  194 */       if (shift < 0) {
/*      */ 
/*      */ 
/*      */         
/*  198 */         if (shift < -2) {
/*  199 */           c = '翿';
/*  200 */           shift = -2;
/*      */         } 
/*      */         do {
/*  203 */           freq[f] = Inlines.SHR32(Inlines.MULT16_16(X[x], c), -shift);
/*  204 */         } while (++j < band_end);
/*      */       } else {
/*      */         do {
/*  207 */           freq[f++] = Inlines.SHR32(Inlines.MULT16_16(X[x++], c), shift);
/*  208 */         } while (++j < band_end);
/*      */       } 
/*      */     } 
/*      */     
/*  212 */     Inlines.OpusAssert((start <= end));
/*  213 */     Arrays.MemSetWithOffset(freq, 0, freq_ptr + bound, N - bound);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void anti_collapse(CeltMode m, int[][] X_, short[] collapse_masks, int LM, int C, int size, int start, int end, int[] logE, int[] prev1logE, int[] prev2logE, int[] pulses, int seed) {
/*  221 */     for (int i = start; i < end; ) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  228 */       int N0 = m.eBands[i + 1] - m.eBands[i];
/*      */       
/*  230 */       Inlines.OpusAssert((pulses[i] >= 0));
/*  231 */       int depth = Inlines.celt_udiv(1 + pulses[i], m.eBands[i + 1] - m.eBands[i]) >> LM;
/*      */       
/*  233 */       int thresh32 = Inlines.SHR32(Inlines.celt_exp2(0 - Inlines.SHL16(depth, 7)), 1);
/*  234 */       int thresh = Inlines.MULT16_32_Q15((short)16384, Inlines.MIN32(32767, thresh32));
/*      */ 
/*      */       
/*  237 */       int t = N0 << LM;
/*  238 */       int shift = Inlines.celt_ilog2(t) >> 1;
/*  239 */       t = Inlines.SHL32(t, 7 - shift << 1);
/*  240 */       int sqrt_1 = Inlines.celt_rsqrt_norm(t);
/*      */ 
/*      */       
/*  243 */       int c = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       while (true) {
/*  250 */         int renormalize = 0;
/*  251 */         int prev1 = prev1logE[c * m.nbEBands + i];
/*  252 */         int prev2 = prev2logE[c * m.nbEBands + i];
/*  253 */         if (C == 1) {
/*  254 */           prev1 = Inlines.MAX16(prev1, prev1logE[m.nbEBands + i]);
/*  255 */           prev2 = Inlines.MAX16(prev2, prev2logE[m.nbEBands + i]);
/*      */         } 
/*  257 */         int Ediff = Inlines.EXTEND32(logE[c * m.nbEBands + i]) - Inlines.EXTEND32(Inlines.MIN16(prev1, prev2));
/*  258 */         Ediff = Inlines.MAX32(0, Ediff);
/*      */         
/*  260 */         if (Ediff < 16384) {
/*  261 */           int r32 = Inlines.SHR32(Inlines.celt_exp2((short)(0 - Inlines.EXTRACT16(Ediff))), 1);
/*  262 */           r = 2 * Inlines.MIN16(16383, r32);
/*      */         } else {
/*  264 */           r = 0;
/*      */         } 
/*  266 */         if (LM == 3) {
/*  267 */           r = Inlines.MULT16_16_Q14(23170, Inlines.MIN32(23169, r));
/*      */         }
/*  269 */         int r = Inlines.SHR16(Inlines.MIN16(thresh, r), 1);
/*  270 */         r = Inlines.SHR32(Inlines.MULT16_16_Q15(sqrt_1, r), shift);
/*      */         
/*  272 */         int X = m.eBands[i] << LM;
/*  273 */         for (int k = 0; k < 1 << LM; k++) {
/*      */           
/*  275 */           if ((collapse_masks[i * C + c] & 1 << k) == 0) {
/*      */             
/*  277 */             int Xk = X + k;
/*  278 */             for (int j = 0; j < N0; j++) {
/*  279 */               seed = celt_lcg_rand(seed);
/*  280 */               X_[c][Xk + (j << LM)] = ((seed & 0x8000) != 0) ? r : (0 - r);
/*      */             } 
/*  282 */             renormalize = 1;
/*      */           } 
/*      */         } 
/*      */         
/*  286 */         if (renormalize != 0) {
/*  287 */           VQ.renormalise_vector(X_[c], X, N0 << LM, 32767);
/*      */         }
/*  289 */         if (++c >= C)
/*      */           i++; 
/*      */       } 
/*      */     } 
/*      */   } static void intensity_stereo(CeltMode m, int[] X, int X_ptr, int[] Y, int Y_ptr, int[][] bandE, int bandID, int N) {
/*  294 */     int i = bandID;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  299 */     int shift = Inlines.celt_zlog2(Inlines.MAX32(bandE[0][i], bandE[1][i])) - 13;
/*  300 */     int left = Inlines.VSHR32(bandE[0][i], shift);
/*  301 */     int right = Inlines.VSHR32(bandE[1][i], shift);
/*  302 */     int norm = 1 + Inlines.celt_sqrt(1 + Inlines.MULT16_16(left, left) + Inlines.MULT16_16(right, right));
/*  303 */     int a1 = Inlines.DIV32_16(Inlines.SHL32(left, 14), norm);
/*  304 */     int a2 = Inlines.DIV32_16(Inlines.SHL32(right, 14), norm);
/*  305 */     for (int j = 0; j < N; j++) {
/*      */       
/*  307 */       int l = X[X_ptr + j];
/*  308 */       int r = Y[Y_ptr + j];
/*  309 */       X[X_ptr + j] = Inlines.EXTRACT16(Inlines.SHR32(Inlines.MAC16_16(Inlines.MULT16_16(a1, l), a2, r), 14));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void stereo_split(int[] X, int X_ptr, int[] Y, int Y_ptr, int N) {
/*  316 */     for (int j = 0; j < N; j++) {
/*      */       
/*  318 */       int l = Inlines.MULT16_16(23170, X[X_ptr + j]);
/*  319 */       int r = Inlines.MULT16_16(23170, Y[Y_ptr + j]);
/*  320 */       X[X_ptr + j] = Inlines.EXTRACT16(Inlines.SHR32(Inlines.ADD32(l, r), 15));
/*  321 */       Y[Y_ptr + j] = Inlines.EXTRACT16(Inlines.SHR32(Inlines.SUB32(r, l), 15));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void stereo_merge(int[] X, int X_ptr, int[] Y, int Y_ptr, int mid, int N) {
/*  327 */     BoxedValueInt xp = new BoxedValueInt(0);
/*  328 */     BoxedValueInt side = new BoxedValueInt(0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  335 */     Kernels.dual_inner_prod(Y, Y_ptr, X, X_ptr, Y, Y_ptr, N, xp, side);
/*      */     
/*  337 */     xp.Val = Inlines.MULT16_32_Q15(mid, xp.Val);
/*      */     
/*  339 */     int mid2 = Inlines.SHR16(mid, 1);
/*  340 */     int El = Inlines.MULT16_16(mid2, mid2) + side.Val - 2 * xp.Val;
/*  341 */     int Er = Inlines.MULT16_16(mid2, mid2) + side.Val + 2 * xp.Val;
/*  342 */     if (Er < 161061 || El < 161061) {
/*  343 */       System.arraycopy(X, X_ptr, Y, Y_ptr, N);
/*      */       
/*      */       return;
/*      */     } 
/*  347 */     int kl = Inlines.celt_ilog2(El) >> 1;
/*  348 */     int kr = Inlines.celt_ilog2(Er) >> 1;
/*  349 */     int t = Inlines.VSHR32(El, kl - 7 << 1);
/*  350 */     int lgain = Inlines.celt_rsqrt_norm(t);
/*  351 */     t = Inlines.VSHR32(Er, kr - 7 << 1);
/*  352 */     int rgain = Inlines.celt_rsqrt_norm(t);
/*      */     
/*  354 */     if (kl < 7) {
/*  355 */       kl = 7;
/*      */     }
/*  357 */     if (kr < 7) {
/*  358 */       kr = 7;
/*      */     }
/*      */     
/*  361 */     for (int j = 0; j < N; j++) {
/*      */ 
/*      */       
/*  364 */       int l = Inlines.MULT16_16_P15(mid, X[X_ptr + j]);
/*  365 */       int r = Y[Y_ptr + j];
/*  366 */       X[X_ptr + j] = Inlines.EXTRACT16(Inlines.PSHR32(Inlines.MULT16_16(lgain, Inlines.SUB16(l, r)), kl + 1));
/*  367 */       Y[Y_ptr + j] = Inlines.EXTRACT16(Inlines.PSHR32(Inlines.MULT16_16(rgain, Inlines.ADD16(l, r)), kr + 1));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int spreading_decision(CeltMode m, int[][] X, BoxedValueInt average, int last_decision, BoxedValueInt hf_average, BoxedValueInt tapset_decision, int update_hf, int end, int C, int M) {
/*  376 */     int decision, sum = 0, nbBands = 0;
/*  377 */     short[] eBands = m.eBands;
/*      */     
/*  379 */     int hf_sum = 0;
/*      */     
/*  381 */     Inlines.OpusAssert((end > 0));
/*      */     
/*  383 */     if (M * (eBands[end] - eBands[end - 1]) <= 8) {
/*  384 */       return 0;
/*      */     }
/*      */     
/*  387 */     int c = 0;
/*      */     
/*      */     do {
/*  390 */       for (int i = 0; i < end; i++) {
/*  391 */         int tmp = 0;
/*  392 */         int[] tcount = { 0, 0, 0 };
/*  393 */         int[] x = X[c];
/*  394 */         int x_ptr = M * eBands[i];
/*  395 */         int N = M * (eBands[i + 1] - eBands[i]);
/*  396 */         if (N > 8)
/*      */         
/*      */         { 
/*      */           
/*  400 */           for (int j = x_ptr; j < N + x_ptr; j++) {
/*      */ 
/*      */ 
/*      */             
/*  404 */             int x2N = Inlines.MULT16_16(Inlines.MULT16_16_Q15(x[j], x[j]), N);
/*  405 */             if (x2N < 2048) {
/*  406 */               tcount[0] = tcount[0] + 1;
/*      */             }
/*  408 */             if (x2N < 512) {
/*  409 */               tcount[1] = tcount[1] + 1;
/*      */             }
/*  411 */             if (x2N < 128) {
/*  412 */               tcount[2] = tcount[2] + 1;
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/*  417 */           if (i > m.nbEBands - 4) {
/*  418 */             hf_sum += Inlines.celt_udiv(32 * (tcount[1] + tcount[0]), N);
/*      */           }
/*      */           
/*  421 */           tmp = ((2 * tcount[2] >= N) ? 1 : 0) + ((2 * tcount[1] >= N) ? 1 : 0) + ((2 * tcount[0] >= N) ? 1 : 0);
/*  422 */           sum += tmp * 256;
/*  423 */           nbBands++; } 
/*      */       } 
/*  425 */     } while (++c < C);
/*      */     
/*  427 */     if (update_hf != 0) {
/*  428 */       if (hf_sum != 0) {
/*  429 */         hf_sum = Inlines.celt_udiv(hf_sum, C * (4 - m.nbEBands + end));
/*      */       }
/*      */       
/*  432 */       hf_average.Val = hf_average.Val + hf_sum >> 1;
/*  433 */       hf_sum = hf_average.Val;
/*      */       
/*  435 */       if (tapset_decision.Val == 2) {
/*  436 */         hf_sum += 4;
/*  437 */       } else if (tapset_decision.Val == 0) {
/*  438 */         hf_sum -= 4;
/*      */       } 
/*  440 */       if (hf_sum > 22) {
/*  441 */         tapset_decision.Val = 2;
/*  442 */       } else if (hf_sum > 18) {
/*  443 */         tapset_decision.Val = 1;
/*      */       } else {
/*  445 */         tapset_decision.Val = 0;
/*      */       } 
/*      */     } 
/*      */     
/*  449 */     Inlines.OpusAssert((nbBands > 0));
/*      */     
/*  451 */     Inlines.OpusAssert((sum >= 0));
/*  452 */     sum = Inlines.celt_udiv(sum, nbBands);
/*      */ 
/*      */     
/*  455 */     sum = sum + average.Val >> 1;
/*  456 */     average.Val = sum;
/*      */ 
/*      */     
/*  459 */     sum = 3 * sum + (3 - last_decision << 7) + 64 + 2 >> 2;
/*  460 */     if (sum < 80) {
/*  461 */       decision = 3;
/*  462 */     } else if (sum < 256) {
/*  463 */       decision = 2;
/*  464 */     } else if (sum < 384) {
/*  465 */       decision = 1;
/*      */     } else {
/*  467 */       decision = 0;
/*      */     } 
/*  469 */     return decision;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void deinterleave_hadamard(int[] X, int X_ptr, int N0, int stride, int hadamard) {
/*  475 */     int N = N0 * stride;
/*  476 */     int[] tmp = new int[N];
/*      */     
/*  478 */     Inlines.OpusAssert((stride > 0));
/*  479 */     if (hadamard != 0) {
/*  480 */       int ordery = stride - 2;
/*      */       
/*  482 */       for (int i = 0; i < stride; i++) {
/*  483 */         for (int j = 0; j < N0; j++) {
/*  484 */           tmp[CeltTables.ordery_table[ordery + i] * N0 + j] = X[j * stride + i + X_ptr];
/*      */         }
/*      */       } 
/*      */     } else {
/*  488 */       for (int i = 0; i < stride; i++) {
/*  489 */         for (int j = 0; j < N0; j++) {
/*  490 */           tmp[i * N0 + j] = X[j * stride + i + X_ptr];
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  495 */     System.arraycopy(tmp, 0, X, X_ptr, N);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void interleave_hadamard(int[] X, int X_ptr, int N0, int stride, int hadamard) {
/*  501 */     int N = N0 * stride;
/*  502 */     int[] tmp = new int[N];
/*      */     
/*  504 */     if (hadamard != 0) {
/*  505 */       int ordery = stride - 2;
/*  506 */       for (int i = 0; i < stride; i++) {
/*  507 */         for (int j = 0; j < N0; j++) {
/*  508 */           tmp[j * stride + i] = X[CeltTables.ordery_table[ordery + i] * N0 + j + X_ptr];
/*      */         }
/*      */       } 
/*      */     } else {
/*  512 */       for (int i = 0; i < stride; i++) {
/*  513 */         for (int j = 0; j < N0; j++) {
/*  514 */           tmp[j * stride + i] = X[i * N0 + j + X_ptr];
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  519 */     System.arraycopy(tmp, 0, X, X_ptr, N);
/*      */   }
/*      */ 
/*      */   
/*      */   static void haar1(int[] X, int X_ptr, int N0, int stride) {
/*  524 */     N0 >>= 1;
/*  525 */     for (int i = 0; i < stride; i++) {
/*  526 */       for (int j = 0; j < N0; j++) {
/*  527 */         int tmpidx = X_ptr + i + stride * 2 * j;
/*      */         
/*  529 */         int tmp1 = Inlines.MULT16_16(23170, X[tmpidx]);
/*  530 */         int tmp2 = Inlines.MULT16_16(23170, X[tmpidx + stride]);
/*  531 */         X[tmpidx] = Inlines.EXTRACT16(Inlines.PSHR32(Inlines.ADD32(tmp1, tmp2), 15));
/*  532 */         X[tmpidx + stride] = Inlines.EXTRACT16(Inlines.PSHR32(Inlines.SUB32(tmp1, tmp2), 15));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void haar1ZeroOffset(int[] X, int N0, int stride) {
/*  539 */     N0 >>= 1;
/*  540 */     for (int i = 0; i < stride; i++) {
/*  541 */       for (int j = 0; j < N0; j++) {
/*  542 */         int tmpidx = i + stride * 2 * j;
/*      */         
/*  544 */         int tmp1 = Inlines.MULT16_16(23170, X[tmpidx]);
/*  545 */         int tmp2 = Inlines.MULT16_16(23170, X[tmpidx + stride]);
/*  546 */         X[tmpidx] = Inlines.EXTRACT16(Inlines.PSHR32(Inlines.ADD32(tmp1, tmp2), 15));
/*  547 */         X[tmpidx + stride] = Inlines.EXTRACT16(Inlines.PSHR32(Inlines.SUB32(tmp1, tmp2), 15));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   static int compute_qn(int N, int b, int offset, int pulse_cap, int stereo) {
/*      */     int qn;
/*  553 */     short[] exp2_table8 = { 16384, 17866, 19483, 21247, 23170, 25267, 27554, 30048 };
/*      */ 
/*      */     
/*  556 */     int N2 = 2 * N - 1;
/*  557 */     if (stereo != 0 && N == 2) {
/*  558 */       N2--;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  564 */     int qb = Inlines.celt_sudiv(b + N2 * offset, N2);
/*  565 */     qb = Inlines.IMIN(b - pulse_cap - 32, qb);
/*      */     
/*  567 */     qb = Inlines.IMIN(64, qb);
/*      */     
/*  569 */     if (qb < 4) {
/*  570 */       qn = 1;
/*      */     } else {
/*  572 */       qn = exp2_table8[qb & 0x7] >> 14 - (qb >> 3);
/*  573 */       qn = qn + 1 >> 1 << 1;
/*      */     } 
/*  575 */     Inlines.OpusAssert((qn <= 256));
/*  576 */     return qn;
/*      */   }
/*      */ 
/*      */   
/*      */   public static class band_ctx
/*      */   {
/*      */     public int encode;
/*      */     
/*      */     public CeltMode m;
/*      */     
/*      */     public int i;
/*      */     
/*      */     public int intensity;
/*      */     public int spread;
/*      */     public int tf_change;
/*      */     public EntropyCoder ec;
/*      */     public int remaining_bits;
/*      */     public int[][] bandE;
/*      */     public int seed;
/*      */   }
/*      */   
/*      */   public static class split_ctx
/*      */   {
/*      */     public int inv;
/*      */     public int imid;
/*      */     public int iside;
/*      */     public int delta;
/*      */     public int itheta;
/*      */     public int qalloc;
/*      */   }
/*      */   
/*      */   static void compute_theta(band_ctx ctx, split_ctx sctx, int[] X, int X_ptr, int[] Y, int Y_ptr, int N, BoxedValueInt b, int B, int B0, int LM, int stereo, BoxedValueInt fill) {
/*  608 */     int delta, imid, iside, itheta = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  615 */     int inv = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  623 */     int encode = ctx.encode;
/*  624 */     CeltMode m = ctx.m;
/*  625 */     int i = ctx.i;
/*  626 */     int intensity = ctx.intensity;
/*  627 */     EntropyCoder ec = ctx.ec;
/*  628 */     int[][] bandE = ctx.bandE;
/*      */ 
/*      */     
/*  631 */     int pulse_cap = m.logN[i] + LM * 8;
/*  632 */     int offset = (pulse_cap >> 1) - ((stereo != 0 && N == 2) ? 16 : 4);
/*  633 */     int qn = compute_qn(N, b.Val, offset, pulse_cap, stereo);
/*  634 */     if (stereo != 0 && i >= intensity) {
/*  635 */       qn = 1;
/*      */     }
/*      */     
/*  638 */     if (encode != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  643 */       itheta = VQ.stereo_itheta(X, X_ptr, Y, Y_ptr, stereo, N);
/*      */     }
/*      */     
/*  646 */     int tell = ec.tell_frac();
/*      */     
/*  648 */     if (qn != 1) {
/*  649 */       if (encode != 0) {
/*  650 */         itheta = itheta * qn + 8192 >> 14;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  655 */       if (stereo != 0 && N > 2) {
/*  656 */         int p0 = 3;
/*  657 */         int x = itheta;
/*  658 */         int x0 = qn / 2;
/*  659 */         long ft = Inlines.CapToUInt32(p0 * (x0 + 1) + x0);
/*      */         
/*  661 */         if (encode != 0) {
/*  662 */           ec.encode((
/*  663 */               (x <= x0) ? (
/*  664 */               p0 * x) : (
/*  665 */               x - 1 - x0 + (x0 + 1) * p0)), (
/*  666 */               (x <= x0) ? (
/*  667 */               p0 * (x + 1)) : (
/*  668 */               x - x0 + (x0 + 1) * p0)), ft);
/*      */         } else {
/*      */           
/*  671 */           int fs = (int)ec.decode(ft);
/*  672 */           if (fs < (x0 + 1) * p0) {
/*  673 */             x = fs / p0;
/*      */           } else {
/*  675 */             x = x0 + 1 + fs - (x0 + 1) * p0;
/*      */           } 
/*      */           
/*  678 */           ec.dec_update((
/*  679 */               (x <= x0) ? (
/*  680 */               p0 * x) : (
/*  681 */               x - 1 - x0 + (x0 + 1) * p0)), (
/*  682 */               (x <= x0) ? (
/*  683 */               p0 * (x + 1)) : (
/*  684 */               x - x0 + (x0 + 1) * p0)), ft);
/*      */           
/*  686 */           itheta = x;
/*      */         } 
/*  688 */       } else if (B0 > 1 || stereo != 0) {
/*      */         
/*  690 */         if (encode != 0) {
/*  691 */           ec.enc_uint(itheta, (qn + 1));
/*      */         } else {
/*  693 */           itheta = (int)ec.dec_uint((qn + 1));
/*      */         } 
/*      */       } else {
/*  696 */         int fs = 1;
/*  697 */         int ft = ((qn >> 1) + 1) * ((qn >> 1) + 1);
/*  698 */         if (encode != 0) {
/*      */ 
/*      */           
/*  701 */           fs = (itheta <= qn >> 1) ? (itheta + 1) : (qn + 1 - itheta);
/*      */           
/*  703 */           int fl = (itheta <= qn >> 1) ? (itheta * (itheta + 1) >> 1) : (ft - ((qn + 1 - itheta) * (qn + 2 - itheta) >> 1));
/*      */           
/*  705 */           ec.encode(fl, (fl + fs), ft);
/*      */         } else {
/*      */           
/*  708 */           int fl = 0;
/*      */           
/*  710 */           int fm = (int)ec.decode(ft);
/*      */           
/*  712 */           if (fm < (qn >> 1) * ((qn >> 1) + 1) >> 1) {
/*  713 */             itheta = Inlines.isqrt32((8 * fm + 1)) - 1 >> 1;
/*  714 */             fs = itheta + 1;
/*  715 */             fl = itheta * (itheta + 1) >> 1;
/*      */           } else {
/*  717 */             itheta = 2 * (qn + 1) - Inlines.isqrt32((8 * (ft - fm - 1) + 1)) >> 1;
/*  718 */             fs = qn + 1 - itheta;
/*  719 */             fl = ft - ((qn + 1 - itheta) * (qn + 2 - itheta) >> 1);
/*      */           } 
/*      */           
/*  722 */           ec.dec_update(fl, (fl + fs), ft);
/*      */         } 
/*      */       } 
/*  725 */       Inlines.OpusAssert((itheta >= 0));
/*  726 */       itheta = Inlines.celt_udiv(itheta * 16384, qn);
/*  727 */       if (encode != 0 && stereo != 0) {
/*  728 */         if (itheta == 0) {
/*  729 */           intensity_stereo(m, X, X_ptr, Y, Y_ptr, bandE, i, N);
/*      */         } else {
/*  731 */           stereo_split(X, X_ptr, Y, Y_ptr, N);
/*      */         } 
/*      */       }
/*  734 */     } else if (stereo != 0) {
/*  735 */       if (encode != 0) {
/*  736 */         inv = (itheta > 8192) ? 1 : 0;
/*  737 */         if (inv != 0)
/*      */         {
/*  739 */           for (int j = 0; j < N; j++) {
/*  740 */             Y[Y_ptr + j] = 0 - Y[Y_ptr + j];
/*      */           }
/*      */         }
/*  743 */         intensity_stereo(m, X, X_ptr, Y, Y_ptr, bandE, i, N);
/*      */       } 
/*  745 */       if (b.Val > 16 && ctx.remaining_bits > 16) {
/*  746 */         if (encode != 0) {
/*  747 */           ec.enc_bit_logp(inv, 2);
/*      */         } else {
/*  749 */           inv = ec.dec_bit_logp(2L);
/*      */         } 
/*      */       } else {
/*  752 */         inv = 0;
/*      */       } 
/*  754 */       itheta = 0;
/*      */     } 
/*  756 */     int qalloc = ec.tell_frac() - tell;
/*  757 */     b.Val -= qalloc;
/*      */     
/*  759 */     if (itheta == 0) {
/*  760 */       imid = 32767;
/*  761 */       iside = 0;
/*  762 */       fill.Val &= (1 << B) - 1;
/*  763 */       delta = -16384;
/*  764 */     } else if (itheta == 16384) {
/*  765 */       imid = 0;
/*  766 */       iside = 32767;
/*  767 */       fill.Val &= (1 << B) - 1 << B;
/*  768 */       delta = 16384;
/*      */     } else {
/*  770 */       imid = bitexact_cos((short)itheta);
/*  771 */       iside = bitexact_cos((short)(16384 - itheta));
/*      */ 
/*      */       
/*  774 */       delta = Inlines.FRAC_MUL16(N - 1 << 7, bitexact_log2tan(iside, imid));
/*      */     } 
/*      */     
/*  777 */     sctx.inv = inv;
/*  778 */     sctx.imid = imid;
/*  779 */     sctx.iside = iside;
/*  780 */     sctx.delta = delta;
/*  781 */     sctx.itheta = itheta;
/*  782 */     sctx.qalloc = qalloc;
/*      */   }
/*      */ 
/*      */   
/*      */   static int quant_band_n1(band_ctx ctx, int[] X, int X_ptr, int[] Y, int Y_ptr, int b, int[] lowband_out, int lowband_out_ptr) {
/*  787 */     int resynth = (ctx.encode == 0) ? 1 : 0;
/*      */ 
/*      */     
/*  790 */     int[] x = X;
/*  791 */     int x_ptr = X_ptr;
/*      */ 
/*      */ 
/*      */     
/*  795 */     int encode = ctx.encode;
/*  796 */     EntropyCoder ec = ctx.ec;
/*      */     
/*  798 */     int stereo = (Y != null) ? 1 : 0;
/*  799 */     int c = 0;
/*      */     while (true) {
/*  801 */       int sign = 0;
/*  802 */       if (ctx.remaining_bits >= 8) {
/*  803 */         if (encode != 0) {
/*  804 */           sign = (x[x_ptr] < 0) ? 1 : 0;
/*  805 */           ec.enc_bits(sign, 1);
/*      */         } else {
/*  807 */           sign = ec.dec_bits(1);
/*      */         } 
/*  809 */         ctx.remaining_bits -= 8;
/*  810 */         b -= 8;
/*      */       } 
/*  812 */       if (resynth != 0) {
/*  813 */         x[x_ptr] = (sign != 0) ? -16384 : 16384;
/*      */       }
/*  815 */       x = Y;
/*  816 */       x_ptr = Y_ptr;
/*  817 */       if (++c >= 1 + stereo) {
/*  818 */         if (lowband_out != null) {
/*  819 */           lowband_out[lowband_out_ptr] = Inlines.SHR16(X[X_ptr], 4);
/*      */         }
/*      */         
/*  822 */         return 1;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int quant_partition(band_ctx ctx, int[] X, int X_ptr, int N, int b, int B, int[] lowband, int lowband_ptr, int LM, int gain, int fill) {
/*  836 */     int imid = 0, iside = 0;
/*  837 */     int B0 = B;
/*  838 */     int mid = 0, side = 0;
/*  839 */     int cm = 0;
/*  840 */     int resynth = (ctx.encode == 0) ? 1 : 0;
/*  841 */     int Y = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  848 */     int encode = ctx.encode;
/*  849 */     CeltMode m = ctx.m;
/*  850 */     int i = ctx.i;
/*  851 */     int spread = ctx.spread;
/*  852 */     EntropyCoder ec = ctx.ec;
/*  853 */     short[] cache = m.cache.bits;
/*      */     
/*  855 */     int cache_ptr = m.cache.index[(LM + 1) * m.nbEBands + i];
/*  856 */     if (LM != -1 && b > cache[cache_ptr + cache[cache_ptr]] + 12 && N > 2) {
/*      */ 
/*      */ 
/*      */       
/*  860 */       split_ctx sctx = new split_ctx();
/*  861 */       int next_lowband2 = 0;
/*      */ 
/*      */       
/*  864 */       N >>= 1;
/*  865 */       Y = X_ptr + N;
/*  866 */       LM--;
/*  867 */       if (B == 1) {
/*  868 */         fill = fill & 0x1 | fill << 1;
/*      */       }
/*      */       
/*  871 */       B = B + 1 >> 1;
/*      */       
/*  873 */       BoxedValueInt boxed_b = new BoxedValueInt(b);
/*  874 */       BoxedValueInt boxed_fill = new BoxedValueInt(fill);
/*  875 */       compute_theta(ctx, sctx, X, X_ptr, X, Y, N, boxed_b, B, B0, LM, 0, boxed_fill);
/*  876 */       b = boxed_b.Val;
/*  877 */       fill = boxed_fill.Val;
/*      */       
/*  879 */       imid = sctx.imid;
/*  880 */       iside = sctx.iside;
/*  881 */       int delta = sctx.delta;
/*  882 */       int itheta = sctx.itheta;
/*  883 */       int qalloc = sctx.qalloc;
/*  884 */       mid = imid;
/*  885 */       side = iside;
/*      */ 
/*      */       
/*  888 */       if (B0 > 1 && (itheta & 0x3FFF) != 0) {
/*  889 */         if (itheta > 8192) {
/*  890 */           delta -= delta >> 4 - LM;
/*      */         } else {
/*  892 */           delta = Inlines.IMIN(0, delta + (N << 3 >> 5 - LM));
/*      */         } 
/*      */       }
/*  895 */       int mbits = Inlines.IMAX(0, Inlines.IMIN(b, (b - delta) / 2));
/*  896 */       int sbits = b - mbits;
/*  897 */       ctx.remaining_bits -= qalloc;
/*      */       
/*  899 */       if (lowband != null) {
/*  900 */         next_lowband2 = lowband_ptr + N;
/*      */       }
/*      */ 
/*      */       
/*  904 */       int rebalance = ctx.remaining_bits;
/*  905 */       if (mbits >= sbits) {
/*  906 */         cm = quant_partition(ctx, X, X_ptr, N, mbits, B, lowband, lowband_ptr, LM, 
/*      */             
/*  908 */             Inlines.MULT16_16_P15(gain, mid), fill);
/*  909 */         rebalance = mbits - rebalance - ctx.remaining_bits;
/*  910 */         if (rebalance > 24 && itheta != 0) {
/*  911 */           sbits += rebalance - 24;
/*      */         }
/*  913 */         cm |= quant_partition(ctx, X, Y, N, sbits, B, lowband, next_lowband2, LM, 
/*      */             
/*  915 */             Inlines.MULT16_16_P15(gain, side), fill >> B) << B0 >> 1;
/*      */       } else {
/*  917 */         cm = quant_partition(ctx, X, Y, N, sbits, B, lowband, next_lowband2, LM, 
/*      */             
/*  919 */             Inlines.MULT16_16_P15(gain, side), fill >> B) << B0 >> 1;
/*  920 */         rebalance = sbits - rebalance - ctx.remaining_bits;
/*  921 */         if (rebalance > 24 && itheta != 16384) {
/*  922 */           mbits += rebalance - 24;
/*      */         }
/*  924 */         cm |= quant_partition(ctx, X, X_ptr, N, mbits, B, lowband, lowband_ptr, LM, 
/*      */             
/*  926 */             Inlines.MULT16_16_P15(gain, mid), fill);
/*      */       } 
/*      */     } else {
/*      */       
/*  930 */       int q = Rate.bits2pulses(m, i, LM, b);
/*  931 */       int curr_bits = Rate.pulses2bits(m, i, LM, q);
/*  932 */       ctx.remaining_bits -= curr_bits;
/*      */ 
/*      */       
/*  935 */       while (ctx.remaining_bits < 0 && q > 0) {
/*  936 */         ctx.remaining_bits += curr_bits;
/*  937 */         q--;
/*  938 */         curr_bits = Rate.pulses2bits(m, i, LM, q);
/*  939 */         ctx.remaining_bits -= curr_bits;
/*      */       } 
/*      */       
/*  942 */       if (q != 0) {
/*  943 */         int K = Rate.get_pulses(q);
/*      */ 
/*      */         
/*  946 */         if (encode != 0) {
/*  947 */           cm = VQ.alg_quant(X, X_ptr, N, K, spread, B, ec);
/*      */         } else {
/*  949 */           cm = VQ.alg_unquant(X, X_ptr, N, K, spread, B, ec, gain);
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*  955 */       else if (resynth != 0) {
/*      */ 
/*      */ 
/*      */         
/*  959 */         int cm_mask = (1 << B) - 1;
/*  960 */         fill &= cm_mask;
/*      */         
/*  962 */         if (fill == 0) {
/*  963 */           Arrays.MemSetWithOffset(X, 0, X_ptr, N);
/*      */         } else {
/*  965 */           if (lowband == null) {
/*      */             
/*  967 */             for (int j = 0; j < N; j++) {
/*  968 */               ctx.seed = celt_lcg_rand(ctx.seed);
/*  969 */               X[X_ptr + j] = ctx.seed >> 20;
/*      */             } 
/*  971 */             cm = cm_mask;
/*      */           } else {
/*      */             
/*  974 */             for (int j = 0; j < N; j++) {
/*      */               
/*  976 */               ctx.seed = celt_lcg_rand(ctx.seed);
/*      */               
/*  978 */               int tmp = 4;
/*  979 */               tmp = ((ctx.seed & 0x8000) != 0) ? tmp : (0 - tmp);
/*  980 */               X[X_ptr + j] = lowband[lowband_ptr + j] + tmp;
/*      */             } 
/*  982 */             cm = fill;
/*      */           } 
/*      */           
/*  985 */           VQ.renormalise_vector(X, X_ptr, N, gain);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  991 */     return cm;
/*      */   }
/*      */   
/*  994 */   private static final byte[] bit_interleave_table = new byte[] { 0, 1, 1, 1, 2, 3, 3, 3, 2, 3, 3, 3, 2, 3, 3, 3 };
/*      */   
/*  996 */   private static final short[] bit_deinterleave_table = new short[] { 0, 3, 12, 15, 48, 51, 60, 63, 192, 195, 204, 207, 240, 243, 252, 255 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int quant_band(band_ctx ctx, int[] X, int X_ptr, int N, int b, int B, int[] lowband, int lowband_ptr, int LM, int[] lowband_out, int lowband_out_ptr, int gain, int[] lowband_scratch, int lowband_scratch_ptr, int fill) {
/* 1006 */     int N0 = N;
/* 1007 */     int N_B = N;
/*      */     
/* 1009 */     int B0 = B;
/* 1010 */     int time_divide = 0;
/* 1011 */     int recombine = 0;
/*      */     
/* 1013 */     int cm = 0;
/* 1014 */     int resynth = (ctx.encode == 0) ? 1 : 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1019 */     int encode = ctx.encode;
/* 1020 */     int tf_change = ctx.tf_change;
/*      */     
/* 1022 */     int longBlocks = (B0 == 1) ? 1 : 0;
/*      */     
/* 1024 */     N_B = Inlines.celt_udiv(N_B, B);
/*      */ 
/*      */     
/* 1027 */     if (N == 1) {
/* 1028 */       return quant_band_n1(ctx, X, X_ptr, null, 0, b, lowband_out, lowband_out_ptr);
/*      */     }
/*      */     
/* 1031 */     if (tf_change > 0) {
/* 1032 */       recombine = tf_change;
/*      */     }
/*      */ 
/*      */     
/* 1036 */     if (lowband_scratch != null && lowband != null && (recombine != 0 || ((N_B & 0x1) == 0 && tf_change < 0) || B0 > 1)) {
/* 1037 */       System.arraycopy(lowband, lowband_ptr, lowband_scratch, lowband_scratch_ptr, N);
/* 1038 */       lowband = lowband_scratch;
/* 1039 */       lowband_ptr = lowband_scratch_ptr;
/*      */     } 
/*      */     int k;
/* 1042 */     for (k = 0; k < recombine; k++) {
/* 1043 */       if (encode != 0) {
/* 1044 */         haar1(X, X_ptr, N >> k, 1 << k);
/*      */       }
/* 1046 */       if (lowband != null) {
/* 1047 */         haar1(lowband, lowband_ptr, N >> k, 1 << k);
/*      */       }
/* 1049 */       int idx1 = fill & 0xF;
/* 1050 */       int idx2 = fill >> 4;
/* 1051 */       if (idx1 < 0) {
/* 1052 */         System.out.println("e");
/*      */       }
/* 1054 */       if (idx2 < 0) {
/* 1055 */         System.out.println("e");
/*      */       }
/* 1057 */       fill = bit_interleave_table[fill & 0xF] | bit_interleave_table[fill >> 4] << 2;
/*      */     } 
/* 1059 */     B >>= recombine;
/* 1060 */     N_B <<= recombine;
/*      */ 
/*      */     
/* 1063 */     while ((N_B & 0x1) == 0 && tf_change < 0) {
/* 1064 */       if (encode != 0) {
/* 1065 */         haar1(X, X_ptr, N_B, B);
/*      */       }
/* 1067 */       if (lowband != null) {
/* 1068 */         haar1(lowband, lowband_ptr, N_B, B);
/*      */       }
/* 1070 */       fill |= fill << B;
/* 1071 */       B <<= 1;
/* 1072 */       N_B >>= 1;
/* 1073 */       time_divide++;
/* 1074 */       tf_change++;
/*      */     } 
/* 1076 */     B0 = B;
/* 1077 */     int N_B0 = N_B;
/*      */ 
/*      */     
/* 1080 */     if (B0 > 1) {
/* 1081 */       if (encode != 0) {
/* 1082 */         deinterleave_hadamard(X, X_ptr, N_B >> recombine, B0 << recombine, longBlocks);
/*      */       }
/* 1084 */       if (lowband != null) {
/* 1085 */         deinterleave_hadamard(lowband, lowband_ptr, N_B >> recombine, B0 << recombine, longBlocks);
/*      */       }
/*      */     } 
/*      */     
/* 1089 */     cm = quant_partition(ctx, X, X_ptr, N, b, B, lowband, lowband_ptr, LM, gain, fill);
/*      */ 
/*      */     
/* 1092 */     if (resynth != 0) {
/*      */       
/* 1094 */       if (B0 > 1) {
/* 1095 */         interleave_hadamard(X, X_ptr, N_B >> recombine, B0 << recombine, longBlocks);
/*      */       }
/*      */ 
/*      */       
/* 1099 */       N_B = N_B0;
/* 1100 */       B = B0;
/* 1101 */       for (k = 0; k < time_divide; k++) {
/* 1102 */         B >>= 1;
/* 1103 */         N_B <<= 1;
/* 1104 */         cm |= cm >> B;
/* 1105 */         haar1(X, X_ptr, N_B, B);
/*      */       } 
/*      */       
/* 1108 */       for (k = 0; k < recombine; k++) {
/* 1109 */         cm = bit_deinterleave_table[cm];
/* 1110 */         haar1(X, X_ptr, N0 >> k, 1 << k);
/*      */       } 
/* 1112 */       B <<= recombine;
/*      */ 
/*      */       
/* 1115 */       if (lowband_out != null) {
/*      */ 
/*      */         
/* 1118 */         int n = Inlines.celt_sqrt(Inlines.SHL32(N0, 22));
/* 1119 */         for (int j = 0; j < N0; j++) {
/* 1120 */           lowband_out[lowband_out_ptr + j] = Inlines.MULT16_16_Q15(n, X[X_ptr + j]);
/*      */         }
/*      */       } 
/*      */       
/* 1124 */       cm &= (1 << B) - 1;
/*      */     } 
/* 1126 */     return cm;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int quant_band_stereo(band_ctx ctx, int[] X, int X_ptr, int[] Y, int Y_ptr, int N, int b, int B, int[] lowband, int lowband_ptr, int LM, int[] lowband_out, int lowband_out_ptr, int[] lowband_scratch, int lowband_scratch_ptr, int fill) {
/* 1134 */     int imid = 0, iside = 0;
/* 1135 */     int inv = 0;
/* 1136 */     int mid = 0, side = 0;
/* 1137 */     int cm = 0;
/* 1138 */     int resynth = (ctx.encode == 0) ? 1 : 0;
/*      */ 
/*      */ 
/*      */     
/* 1142 */     split_ctx sctx = new split_ctx();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1147 */     int encode = ctx.encode;
/* 1148 */     EntropyCoder ec = ctx.ec;
/*      */ 
/*      */     
/* 1151 */     if (N == 1) {
/* 1152 */       return quant_band_n1(ctx, X, X_ptr, Y, Y_ptr, b, lowband_out, lowband_out_ptr);
/*      */     }
/*      */     
/* 1155 */     int orig_fill = fill;
/*      */     
/* 1157 */     BoxedValueInt boxed_b = new BoxedValueInt(b);
/* 1158 */     BoxedValueInt boxed_fill = new BoxedValueInt(fill);
/* 1159 */     compute_theta(ctx, sctx, X, X_ptr, Y, Y_ptr, N, boxed_b, B, B, LM, 1, boxed_fill);
/* 1160 */     b = boxed_b.Val;
/* 1161 */     fill = boxed_fill.Val;
/*      */     
/* 1163 */     inv = sctx.inv;
/* 1164 */     imid = sctx.imid;
/* 1165 */     iside = sctx.iside;
/* 1166 */     int delta = sctx.delta;
/* 1167 */     int itheta = sctx.itheta;
/* 1168 */     int qalloc = sctx.qalloc;
/* 1169 */     mid = imid;
/* 1170 */     side = iside;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1175 */     if (N == 2) {
/*      */       
/* 1177 */       int x2[], y2[], x2_ptr, sign = 0;
/*      */ 
/*      */       
/* 1180 */       int mbits = b;
/* 1181 */       int sbits = 0;
/*      */       
/* 1183 */       if (itheta != 0 && itheta != 16384) {
/* 1184 */         sbits = 8;
/*      */       }
/* 1186 */       mbits -= sbits;
/* 1187 */       int c = (itheta > 8192) ? 1 : 0;
/* 1188 */       ctx.remaining_bits -= qalloc + sbits;
/* 1189 */       if (c != 0) {
/* 1190 */         x2 = Y;
/* 1191 */         x2_ptr = Y_ptr;
/* 1192 */         y2 = X;
/* 1193 */         int y2_ptr = X_ptr;
/*      */       } else {
/* 1195 */         x2 = X;
/* 1196 */         x2_ptr = X_ptr;
/* 1197 */         y2 = Y;
/* 1198 */         int y2_ptr = Y_ptr;
/*      */       } 
/*      */       
/* 1201 */       if (sbits != 0) {
/* 1202 */         if (encode != 0) {
/*      */           
/* 1204 */           sign = (x2[x2_ptr] * y2[Y_ptr + 1] - x2[x2_ptr + 1] * y2[Y_ptr] < 0) ? 1 : 0;
/* 1205 */           ec.enc_bits(sign, 1);
/*      */         } else {
/* 1207 */           sign = ec.dec_bits(1);
/*      */         } 
/*      */       }
/* 1210 */       sign = 1 - 2 * sign;
/*      */ 
/*      */       
/* 1213 */       cm = quant_band(ctx, x2, x2_ptr, N, mbits, B, lowband, lowband_ptr, LM, lowband_out, lowband_out_ptr, 32767, lowband_scratch, lowband_scratch_ptr, orig_fill);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1218 */       y2[Y_ptr] = (0 - sign) * x2[x2_ptr + 1];
/* 1219 */       y2[Y_ptr + 1] = sign * x2[x2_ptr];
/* 1220 */       if (resynth != 0)
/*      */       {
/* 1222 */         X[X_ptr] = Inlines.MULT16_16_Q15(mid, X[X_ptr]);
/* 1223 */         X[X_ptr + 1] = Inlines.MULT16_16_Q15(mid, X[X_ptr + 1]);
/* 1224 */         Y[Y_ptr] = Inlines.MULT16_16_Q15(side, Y[Y_ptr]);
/* 1225 */         Y[Y_ptr + 1] = Inlines.MULT16_16_Q15(side, Y[Y_ptr + 1]);
/* 1226 */         int tmp = X[X_ptr];
/* 1227 */         X[X_ptr] = Inlines.SUB16(tmp, Y[Y_ptr]);
/* 1228 */         Y[Y_ptr] = Inlines.ADD16(tmp, Y[Y_ptr]);
/* 1229 */         tmp = X[X_ptr + 1];
/* 1230 */         X[X_ptr + 1] = Inlines.SUB16(tmp, Y[Y_ptr + 1]);
/* 1231 */         Y[Y_ptr + 1] = Inlines.ADD16(tmp, Y[Y_ptr + 1]);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1237 */       int mbits = Inlines.IMAX(0, Inlines.IMIN(b, (b - delta) / 2));
/* 1238 */       int sbits = b - mbits;
/* 1239 */       ctx.remaining_bits -= qalloc;
/*      */       
/* 1241 */       int rebalance = ctx.remaining_bits;
/* 1242 */       if (mbits >= sbits) {
/*      */ 
/*      */         
/* 1245 */         cm = quant_band(ctx, X, X_ptr, N, mbits, B, lowband, lowband_ptr, LM, lowband_out, lowband_out_ptr, 32767, lowband_scratch, lowband_scratch_ptr, fill);
/*      */ 
/*      */         
/* 1248 */         rebalance = mbits - rebalance - ctx.remaining_bits;
/* 1249 */         if (rebalance > 24 && itheta != 0) {
/* 1250 */           sbits += rebalance - 24;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1255 */         cm |= quant_band(ctx, Y, Y_ptr, N, sbits, B, null, 0, LM, null, 0, side, null, 0, fill >> B);
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1261 */         cm = quant_band(ctx, Y, Y_ptr, N, sbits, B, null, 0, LM, null, 0, side, null, 0, fill >> B);
/*      */ 
/*      */         
/* 1264 */         rebalance = sbits - rebalance - ctx.remaining_bits;
/* 1265 */         if (rebalance > 24 && itheta != 16384) {
/* 1266 */           mbits += rebalance - 24;
/*      */         }
/*      */ 
/*      */         
/* 1270 */         cm |= quant_band(ctx, X, X_ptr, N, mbits, B, lowband, lowband_ptr, LM, lowband_out, lowband_out_ptr, 32767, lowband_scratch, lowband_scratch_ptr, fill);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1278 */     if (resynth != 0) {
/* 1279 */       if (N != 2) {
/* 1280 */         stereo_merge(X, X_ptr, Y, Y_ptr, mid, N);
/*      */       }
/* 1282 */       if (inv != 0)
/*      */       {
/* 1284 */         for (int j = Y_ptr; j < N + Y_ptr; j++) {
/* 1285 */           Y[j] = (short)(0 - Y[j]);
/*      */         }
/*      */       }
/*      */     } 
/*      */     
/* 1290 */     return cm;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void quant_all_bands(int encode, CeltMode m, int start, int end, int[] X_, int[] Y_, short[] collapse_masks, int[][] bandE, int[] pulses, int shortBlocks, int spread, int dual_stereo, int intensity, int[] tf_res, int total_bits, int balance, EntropyCoder ec, int LM, int codedBands, BoxedValueInt seed) {
/* 1301 */     short[] eBands = m.eBands;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1309 */     int update_lowband = 1;
/* 1310 */     int C = (Y_ != null) ? 2 : 1;
/*      */     
/* 1312 */     int resynth = (encode == 0) ? 1 : 0;
/* 1313 */     band_ctx ctx = new band_ctx();
/*      */     
/* 1315 */     int M = 1 << LM;
/* 1316 */     int B = (shortBlocks != 0) ? M : 1;
/* 1317 */     int norm_offset = M * eBands[start];
/*      */ 
/*      */ 
/*      */     
/* 1321 */     int[] norm = new int[C * (M * eBands[m.nbEBands - 1] - norm_offset)];
/* 1322 */     int norm2 = M * eBands[m.nbEBands - 1] - norm_offset;
/*      */ 
/*      */ 
/*      */     
/* 1326 */     int[] lowband_scratch = X_;
/* 1327 */     int lowband_scratch_ptr = M * eBands[m.nbEBands - 1];
/*      */     
/* 1329 */     int lowband_offset = 0;
/* 1330 */     ctx.bandE = bandE;
/* 1331 */     ctx.ec = ec;
/* 1332 */     ctx.encode = encode;
/* 1333 */     ctx.intensity = intensity;
/* 1334 */     ctx.m = m;
/* 1335 */     ctx.seed = seed.Val;
/* 1336 */     ctx.spread = spread;
/* 1337 */     for (int i = start; i < end; i++) {
/*      */       int b, arrayOfInt1[];
/*      */       
/*      */       long x_cm, y_cm;
/*      */       
/* 1342 */       int effective_lowband = -1;
/*      */ 
/*      */       
/* 1345 */       int Y_ptr = 0;
/* 1346 */       int tf_change = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1351 */       ctx.i = i;
/* 1352 */       int last = (i == end - 1) ? 1 : 0;
/*      */       
/* 1354 */       int[] X = X_;
/* 1355 */       int X_ptr = M * eBands[i];
/* 1356 */       if (Y_ != null) {
/* 1357 */         arrayOfInt1 = Y_;
/* 1358 */         Y_ptr = M * eBands[i];
/*      */       } else {
/* 1360 */         arrayOfInt1 = null;
/*      */       } 
/* 1362 */       int N = M * eBands[i + 1] - M * eBands[i];
/* 1363 */       int tell = ec.tell_frac();
/*      */ 
/*      */       
/* 1366 */       if (i != start) {
/* 1367 */         balance -= tell;
/*      */       }
/* 1369 */       int remaining_bits = total_bits - tell - 1;
/* 1370 */       ctx.remaining_bits = remaining_bits;
/* 1371 */       if (i <= codedBands - 1) {
/* 1372 */         int curr_balance = Inlines.celt_sudiv(balance, Inlines.IMIN(3, codedBands - i));
/* 1373 */         b = Inlines.IMAX(0, Inlines.IMIN(16383, Inlines.IMIN(remaining_bits + 1, pulses[i] + curr_balance)));
/*      */       } else {
/* 1375 */         b = 0;
/*      */       } 
/*      */       
/* 1378 */       if (resynth != 0 && M * eBands[i] - N >= M * eBands[start] && (update_lowband != 0 || lowband_offset == 0)) {
/* 1379 */         lowband_offset = i;
/*      */       }
/*      */       
/* 1382 */       tf_change = tf_res[i];
/* 1383 */       ctx.tf_change = tf_change;
/* 1384 */       if (i >= m.effEBands) {
/* 1385 */         X = norm;
/* 1386 */         X_ptr = 0;
/* 1387 */         if (Y_ != null) {
/* 1388 */           arrayOfInt1 = norm;
/* 1389 */           Y_ptr = 0;
/*      */         } 
/* 1391 */         lowband_scratch = null;
/*      */       } 
/* 1393 */       if (i == end - 1) {
/* 1394 */         lowband_scratch = null;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1399 */       if (lowband_offset != 0 && (spread != 3 || B > 1 || tf_change < 0)) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1404 */         effective_lowband = Inlines.IMAX(0, M * eBands[lowband_offset] - norm_offset - N);
/* 1405 */         int fold_start = lowband_offset;
/* 1406 */         while (M * eBands[--fold_start] > effective_lowband + norm_offset);
/* 1407 */         int fold_end = lowband_offset - 1;
/* 1408 */         while (M * eBands[++fold_end] < effective_lowband + norm_offset + N);
/* 1409 */         x_cm = y_cm = 0L;
/* 1410 */         int fold_i = fold_start;
/*      */         do {
/* 1412 */           x_cm |= collapse_masks[fold_i * C + 0];
/* 1413 */           y_cm |= collapse_masks[fold_i * C + C - 1];
/* 1414 */         } while (++fold_i < fold_end);
/*      */       } else {
/*      */         
/* 1417 */         x_cm = y_cm = ((1 << B) - 1);
/*      */       } 
/*      */       
/* 1420 */       if (dual_stereo != 0 && i == intensity) {
/*      */ 
/*      */ 
/*      */         
/* 1424 */         dual_stereo = 0;
/* 1425 */         if (resynth != 0) {
/* 1426 */           for (int j = 0; j < M * eBands[i] - norm_offset; j++) {
/* 1427 */             norm[j] = Inlines.HALF32(norm[j] + norm[norm2 + j]);
/*      */           }
/*      */         }
/*      */       } 
/* 1431 */       if (dual_stereo != 0) {
/* 1432 */         x_cm = quant_band(ctx, X, X_ptr, N, b / 2, B, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1438 */             (effective_lowband != -1) ? norm : null, effective_lowband, LM, 
/*      */ 
/*      */             
/* 1441 */             (last != 0) ? null : norm, M * eBands[i] - norm_offset, 32767, lowband_scratch, lowband_scratch_ptr, (int)x_cm);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1447 */         y_cm = quant_band(ctx, arrayOfInt1, Y_ptr, N, b / 2, B, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1454 */             (effective_lowband != -1) ? norm : null, norm2 + effective_lowband, LM, 
/*      */ 
/*      */             
/* 1457 */             (last != 0) ? null : norm, norm2 + M * eBands[i] - norm_offset, 32767, lowband_scratch, lowband_scratch_ptr, (int)y_cm);
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1464 */         if (arrayOfInt1 != null) {
/* 1465 */           x_cm = quant_band_stereo(ctx, X, X_ptr, arrayOfInt1, Y_ptr, N, b, B, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1474 */               (effective_lowband != -1) ? norm : null, effective_lowband, LM, 
/*      */ 
/*      */               
/* 1477 */               (last != 0) ? null : norm, M * eBands[i] - norm_offset, lowband_scratch, lowband_scratch_ptr, (int)(x_cm | y_cm));
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 1483 */           x_cm = quant_band(ctx, X, X_ptr, N, b, B, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1490 */               (effective_lowband != -1) ? norm : null, effective_lowband, LM, 
/*      */ 
/*      */               
/* 1493 */               (last != 0) ? null : norm, M * eBands[i] - norm_offset, 32767, lowband_scratch, lowband_scratch_ptr, (int)(x_cm | y_cm));
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1500 */         y_cm = x_cm;
/*      */       } 
/* 1502 */       collapse_masks[i * C + 0] = (short)(int)(x_cm & 0xFFL);
/* 1503 */       collapse_masks[i * C + C - 1] = (short)(int)(y_cm & 0xFFL);
/* 1504 */       balance += pulses[i] + tell;
/*      */ 
/*      */       
/* 1507 */       update_lowband = (b > N << 3) ? 1 : 0;
/*      */     } 
/*      */     
/* 1510 */     seed.Val = ctx.seed;
/*      */   }
/*      */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Bands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */