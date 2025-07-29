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
/*      */ class CeltCommon
/*      */ {
/*   40 */   private static final short[] inv_table = new short[] { 255, 255, 156, 110, 86, 70, 59, 51, 45, 40, 37, 33, 31, 28, 26, 25, 23, 22, 21, 20, 19, 18, 17, 16, 16, 15, 15, 14, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 9, 9, 8, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2 };
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
/*      */   static int compute_vbr(CeltMode mode, AnalysisInfo analysis, int base_target, int LM, int bitrate, int lastCodedBands, int C, int intensity, int constrained_vbr, int stereo_saving, int tot_boost, int tf_estimate, int pitch_change, int maxDepth, OpusFramesize variable_duration, int lfe, int has_surround_mask, int surround_masking, int temporal_vbr) {
/*   64 */     int nbEBands = mode.nbEBands;
/*   65 */     short[] eBands = mode.eBands;
/*      */     
/*   67 */     int coded_bands = (lastCodedBands != 0) ? lastCodedBands : nbEBands;
/*   68 */     int coded_bins = eBands[coded_bands] << LM;
/*   69 */     if (C == 2) {
/*   70 */       coded_bins += eBands[Inlines.IMIN(intensity, coded_bands)] << LM;
/*      */     }
/*      */     
/*   73 */     int target = base_target;
/*   74 */     if (analysis.enabled && analysis.valid != 0 && analysis.activity < 0.4D) {
/*   75 */       target -= (int)((coded_bins << 3) * (0.4F - analysis.activity));
/*      */     }
/*      */ 
/*      */     
/*   79 */     if (C == 2) {
/*      */ 
/*      */ 
/*      */       
/*   83 */       int coded_stereo_bands = Inlines.IMIN(intensity, coded_bands);
/*   84 */       int coded_stereo_dof = (eBands[coded_stereo_bands] << LM) - coded_stereo_bands;
/*      */       
/*   86 */       int max_frac = Inlines.DIV32_16(Inlines.MULT16_16(26214, coded_stereo_dof), coded_bins);
/*   87 */       stereo_saving = Inlines.MIN16(stereo_saving, 256);
/*      */       
/*   89 */       target -= Inlines.MIN32(Inlines.MULT16_32_Q15(max_frac, target), 
/*   90 */           Inlines.SHR32(Inlines.MULT16_16(stereo_saving - 26, coded_stereo_dof << 3), 8));
/*      */     } 
/*      */     
/*   93 */     target += tot_boost - (16 << LM);
/*      */ 
/*      */     
/*   96 */     int tf_calibration = (variable_duration == OpusFramesize.OPUS_FRAMESIZE_VARIABLE) ? 328 : 655;
/*   97 */     target += Inlines.SHL32(Inlines.MULT16_32_Q15(tf_estimate - tf_calibration, target), 1);
/*      */ 
/*      */     
/*  100 */     if (analysis.enabled && analysis.valid != 0 && lfe == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  105 */       float tonal = Inlines.MAX16(0.0F, analysis.tonality - 0.15F) - 0.09F;
/*  106 */       int tonal_target = target + (int)((coded_bins << 3) * 1.2F * tonal);
/*  107 */       if (pitch_change != 0) {
/*  108 */         tonal_target += (int)((coded_bins << 3) * 0.8F);
/*      */       }
/*  110 */       target = tonal_target;
/*      */     } 
/*      */     
/*  113 */     if (has_surround_mask != 0 && lfe == 0) {
/*  114 */       int surround_target = target + Inlines.SHR32(Inlines.MULT16_16(surround_masking, coded_bins << 3), 10);
/*      */       
/*  116 */       target = Inlines.IMAX(target / 4, surround_target);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  122 */     int bins = eBands[nbEBands - 2] << LM;
/*      */     
/*  124 */     int floor_depth = Inlines.SHR32(Inlines.MULT16_16(C * bins << 3, maxDepth), 10);
/*  125 */     floor_depth = Inlines.IMAX(floor_depth, target >> 2);
/*  126 */     target = Inlines.IMIN(target, floor_depth);
/*      */ 
/*      */ 
/*      */     
/*  130 */     if ((has_surround_mask == 0 || lfe != 0) && (constrained_vbr != 0 || bitrate < 64000)) {
/*      */       
/*  132 */       int rate_factor = Inlines.MAX16(0, bitrate - 32000);
/*  133 */       if (constrained_vbr != 0) {
/*  134 */         rate_factor = Inlines.MIN16(rate_factor, 21955);
/*      */       }
/*  136 */       target = base_target + Inlines.MULT16_32_Q15(rate_factor, target - base_target);
/*      */     } 
/*      */     
/*  139 */     if (has_surround_mask == 0 && tf_estimate < 3277) {
/*      */ 
/*      */       
/*  142 */       int amount = Inlines.MULT16_16_Q15(3329, Inlines.IMAX(0, Inlines.IMIN(32000, 96000 - bitrate)));
/*  143 */       int tvbr_factor = Inlines.SHR32(Inlines.MULT16_16(temporal_vbr, amount), 10);
/*  144 */       target += Inlines.MULT16_32_Q15(tvbr_factor, target);
/*      */     } 
/*      */ 
/*      */     
/*  148 */     target = Inlines.IMIN(2 * base_target, target);
/*      */     
/*  150 */     return target;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int transient_analysis(int[][] input, int len, int C, BoxedValueInt tf_estimate, BoxedValueInt tf_chan) {
/*  157 */     int is_transient = 0;
/*  158 */     int mask_metric = 0;
/*      */ 
/*      */ 
/*      */     
/*  162 */     tf_chan.Val = 0;
/*  163 */     int[] tmp = new int[len];
/*      */     
/*  165 */     int len2 = len / 2;
/*  166 */     for (int c = 0; c < C; c++) {
/*      */       
/*  168 */       int unmask = 0;
/*      */ 
/*      */       
/*  171 */       int mem0 = 0;
/*  172 */       int mem1 = 0;
/*      */       int i;
/*  174 */       for (i = 0; i < len; i++) {
/*      */         
/*  176 */         int x = Inlines.SHR32(input[c][i], 12);
/*  177 */         int y = Inlines.ADD32(mem0, x);
/*  178 */         mem0 = mem1 + y - Inlines.SHL32(x, 1);
/*  179 */         mem1 = x - Inlines.SHR32(y, 1);
/*  180 */         tmp[i] = Inlines.EXTRACT16(Inlines.SHR32(y, 2));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  185 */       Arrays.MemSet(tmp, 0, 12);
/*      */ 
/*      */ 
/*      */       
/*  189 */       int shift = 0;
/*  190 */       shift = 14 - Inlines.celt_ilog2(1 + Inlines.celt_maxabs32(tmp, 0, len));
/*  191 */       if (shift != 0) {
/*  192 */         for (i = 0; i < len; i++) {
/*  193 */           tmp[i] = Inlines.SHL16(tmp[i], shift);
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*  198 */       int mean = 0;
/*  199 */       mem0 = 0;
/*      */ 
/*      */       
/*  202 */       for (i = 0; i < len2; i++) {
/*  203 */         int x2 = Inlines.PSHR32(Inlines.MULT16_16(tmp[2 * i], tmp[2 * i]) + Inlines.MULT16_16(tmp[2 * i + 1], tmp[2 * i + 1]), 16);
/*  204 */         mean += x2;
/*  205 */         tmp[i] = mem0 + Inlines.PSHR32(x2 - mem0, 4);
/*  206 */         mem0 = tmp[i];
/*      */       } 
/*      */       
/*  209 */       mem0 = 0;
/*  210 */       int maxE = 0;
/*      */       
/*  212 */       for (i = len2 - 1; i >= 0; i--) {
/*  213 */         tmp[i] = mem0 + Inlines.PSHR32(tmp[i] - mem0, 3);
/*  214 */         mem0 = tmp[i];
/*  215 */         maxE = Inlines.MAX16(maxE, mem0);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  226 */       mean = Inlines.MULT16_16(Inlines.celt_sqrt(mean), Inlines.celt_sqrt(Inlines.MULT16_16(maxE, len2 >> 1)));
/*      */       
/*  228 */       int norm = Inlines.SHL32(len2, 20) / Inlines.ADD32(1, Inlines.SHR32(mean, 1));
/*      */ 
/*      */       
/*  231 */       unmask = 0;
/*  232 */       for (i = 12; i < len2 - 5; i += 4) {
/*      */         
/*  234 */         int id = Inlines.MAX32(0, Inlines.MIN32(127, Inlines.MULT16_32_Q15(tmp[i] + 1, norm)));
/*      */         
/*  236 */         unmask += inv_table[id];
/*      */       } 
/*      */ 
/*      */       
/*  240 */       unmask = 64 * unmask * 4 / 6 * (len2 - 17);
/*  241 */       if (unmask > mask_metric) {
/*  242 */         tf_chan.Val = c;
/*  243 */         mask_metric = unmask;
/*      */       } 
/*      */     } 
/*  246 */     is_transient = (mask_metric > 200) ? 1 : 0;
/*      */ 
/*      */     
/*  249 */     int tf_max = Inlines.MAX16(0, Inlines.celt_sqrt(27 * mask_metric) - 42);
/*      */     
/*  251 */     tf_estimate.Val = Inlines.celt_sqrt(Inlines.MAX32(0, Inlines.SHL32(Inlines.MULT16_16(113, Inlines.MIN16(163, tf_max)), 14) - 37312528));
/*      */ 
/*      */ 
/*      */     
/*  255 */     return is_transient;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int patch_transient_decision(int[][] newE, int[][] oldE, int nbEBands, int start, int end, int C) {
/*  263 */     int mean_diff = 0;
/*  264 */     int[] spread_old = new int[26];
/*      */ 
/*      */     
/*  267 */     if (C == 1) {
/*  268 */       spread_old[start] = oldE[0][start];
/*  269 */       for (int j = start + 1; j < end; j++) {
/*  270 */         spread_old[j] = Inlines.MAX16(spread_old[j - 1] - 1024, oldE[0][j]);
/*      */       }
/*      */     } else {
/*  273 */       spread_old[start] = Inlines.MAX16(oldE[0][start], oldE[1][start]);
/*  274 */       for (int j = start + 1; j < end; j++)
/*  275 */         spread_old[j] = Inlines.MAX16(spread_old[j - 1] - 1024, 
/*  276 */             Inlines.MAX16(oldE[0][j], oldE[1][j])); 
/*      */     } 
/*      */     int i;
/*  279 */     for (i = end - 2; i >= start; i--) {
/*  280 */       spread_old[i] = Inlines.MAX16(spread_old[i], spread_old[i + 1] - 1024);
/*      */     }
/*      */     
/*  283 */     int c = 0;
/*      */     while (true) {
/*  285 */       for (i = Inlines.IMAX(2, start); i < end - 1; i++) {
/*      */         
/*  287 */         int x1 = Inlines.MAX16(0, newE[c][i]);
/*  288 */         int x2 = Inlines.MAX16(0, spread_old[i]);
/*  289 */         mean_diff = Inlines.ADD32(mean_diff, Inlines.MAX16(0, Inlines.SUB16(x1, x2)));
/*      */       } 
/*  291 */       if (++c >= C) {
/*  292 */         mean_diff = Inlines.DIV32(mean_diff, C * (end - 1 - Inlines.IMAX(2, start)));
/*      */         
/*  294 */         return (mean_diff > 1024) ? 1 : 0;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void compute_mdcts(CeltMode mode, int shortBlocks, int[][] input, int[][] output, int C, int CC, int LM, int upsample) {
/*  303 */     int N, B, shift, overlap = mode.overlap;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  308 */     if (shortBlocks != 0) {
/*  309 */       B = shortBlocks;
/*  310 */       N = mode.shortMdctSize;
/*  311 */       shift = mode.maxLM;
/*      */     } else {
/*  313 */       B = 1;
/*  314 */       N = mode.shortMdctSize << LM;
/*  315 */       shift = mode.maxLM - LM;
/*      */     } 
/*  317 */     int c = 0;
/*      */     do {
/*  319 */       for (int b = 0; b < B; b++)
/*      */       {
/*  321 */         MDCT.clt_mdct_forward(mode.mdct, input[c], b * N, output[c], b, mode.window, overlap, shift, B);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  332 */     while (++c < CC);
/*      */     
/*  334 */     if (CC == 2 && C == 1) {
/*  335 */       for (int i = 0; i < B * N; i++) {
/*  336 */         output[0][i] = Inlines.ADD32(Inlines.HALF32(output[0][i]), Inlines.HALF32(output[1][i]));
/*      */       }
/*      */     }
/*  339 */     if (upsample != 1) {
/*  340 */       c = 0;
/*      */       do {
/*  342 */         int bound = B * N / upsample;
/*  343 */         for (int i = 0; i < bound; i++) {
/*  344 */           output[c][i] = output[c][i] * upsample;
/*      */         }
/*  346 */         Arrays.MemSetWithOffset(output[c], 0, bound, B * N - bound);
/*  347 */       } while (++c < C);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void celt_preemphasis(short[] pcmp, int pcmp_ptr, int[] inp, int inp_ptr, int N, int CC, int upsample, int[] coef, BoxedValueInt mem, int clip) {
/*  358 */     int coef0 = coef[0];
/*  359 */     int m = mem.Val;
/*      */ 
/*      */     
/*  362 */     if (coef[1] == 0 && upsample == 1 && clip == 0) {
/*  363 */       for (int j = 0; j < N; j++) {
/*  364 */         int x = pcmp[pcmp_ptr + CC * j];
/*      */         
/*  366 */         inp[inp_ptr + j] = Inlines.SHL32(x, 12) - m;
/*  367 */         m = Inlines.SHR32(Inlines.MULT16_16(coef0, x), 3);
/*      */       } 
/*  369 */       mem.Val = m;
/*      */       
/*      */       return;
/*      */     } 
/*  373 */     int Nu = N / upsample;
/*  374 */     if (upsample != 1)
/*  375 */       Arrays.MemSetWithOffset(inp, 0, inp_ptr, N); 
/*      */     int i;
/*  377 */     for (i = 0; i < Nu; i++) {
/*  378 */       inp[inp_ptr + i * upsample] = pcmp[pcmp_ptr + CC * i];
/*      */     }
/*      */     
/*  381 */     for (i = 0; i < N; i++) {
/*      */       
/*  383 */       int x = inp[inp_ptr + i];
/*      */       
/*  385 */       inp[inp_ptr + i] = Inlines.SHL32(x, 12) - m;
/*  386 */       m = Inlines.SHR32(Inlines.MULT16_16(coef0, x), 3);
/*      */     } 
/*      */     
/*  389 */     mem.Val = m;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void celt_preemphasis(short[] pcmp, int[] inp, int inp_ptr, int N, int CC, int upsample, int[] coef, BoxedValueInt mem, int clip) {
/*  399 */     int coef0 = coef[0];
/*  400 */     int m = mem.Val;
/*      */ 
/*      */     
/*  403 */     if (coef[1] == 0 && upsample == 1 && clip == 0) {
/*  404 */       for (int j = 0; j < N; j++) {
/*      */         
/*  406 */         int x = pcmp[CC * j];
/*      */         
/*  408 */         inp[inp_ptr + j] = Inlines.SHL32(x, 12) - m;
/*  409 */         m = Inlines.SHR32(Inlines.MULT16_16(coef0, x), 3);
/*      */       } 
/*  411 */       mem.Val = m;
/*      */       
/*      */       return;
/*      */     } 
/*  415 */     int Nu = N / upsample;
/*  416 */     if (upsample != 1)
/*  417 */       Arrays.MemSetWithOffset(inp, 0, inp_ptr, N); 
/*      */     int i;
/*  419 */     for (i = 0; i < Nu; i++) {
/*  420 */       inp[inp_ptr + i * upsample] = pcmp[CC * i];
/*      */     }
/*      */     
/*  423 */     for (i = 0; i < N; i++) {
/*      */       
/*  425 */       int x = inp[inp_ptr + i];
/*      */       
/*  427 */       inp[inp_ptr + i] = Inlines.SHL32(x, 12) - m;
/*  428 */       m = Inlines.SHR32(Inlines.MULT16_16(coef0, x), 3);
/*      */     } 
/*      */     
/*  431 */     mem.Val = m;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int l1_metric(int[] tmp, int N, int LM, int bias) {
/*  437 */     int L1 = 0;
/*  438 */     for (int i = 0; i < N; i++) {
/*  439 */       L1 += Inlines.EXTEND32(Inlines.ABS32(tmp[i]));
/*      */     }
/*      */ 
/*      */     
/*  443 */     L1 = Inlines.MAC16_32_Q15(L1, LM * bias, L1);
/*  444 */     return L1;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int tf_analysis(CeltMode m, int len, int isTransient, int[] tf_res, int lambda, int[][] X, int N0, int LM, BoxedValueInt tf_sum, int tf_estimate, int tf_chan) {
/*  460 */     int[] selcost = new int[2];
/*  461 */     int tf_select = 0;
/*      */ 
/*      */     
/*  464 */     int bias = Inlines.MULT16_16_Q14(1311, Inlines.MAX16(-4096, 8192 - tf_estimate));
/*      */ 
/*      */     
/*  467 */     int[] metric = new int[len];
/*  468 */     int[] tmp = new int[m.eBands[len] - m.eBands[len - 1] << LM];
/*  469 */     int[] tmp_1 = new int[m.eBands[len] - m.eBands[len - 1] << LM];
/*  470 */     int[] path0 = new int[len];
/*  471 */     int[] path1 = new int[len];
/*      */     
/*  473 */     tf_sum.Val = 0; int i;
/*  474 */     for (i = 0; i < len; i++) {
/*      */ 
/*      */ 
/*      */       
/*  478 */       int best_level = 0;
/*  479 */       int N = m.eBands[i + 1] - m.eBands[i] << LM;
/*      */       
/*  481 */       int narrow = (m.eBands[i + 1] - m.eBands[i] == 1) ? 1 : 0;
/*  482 */       System.arraycopy(X[tf_chan], m.eBands[i] << LM, tmp, 0, N);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  487 */       int L1 = l1_metric(tmp, N, (isTransient != 0) ? LM : 0, bias);
/*  488 */       int best_L1 = L1;
/*      */       
/*  490 */       if (isTransient != 0 && narrow == 0) {
/*  491 */         System.arraycopy(tmp, 0, tmp_1, 0, N);
/*  492 */         Bands.haar1ZeroOffset(tmp_1, N >> LM, 1 << LM);
/*  493 */         L1 = l1_metric(tmp_1, N, LM + 1, bias);
/*  494 */         if (L1 < best_L1) {
/*  495 */           best_L1 = L1;
/*  496 */           best_level = -1;
/*      */         } 
/*      */       } 
/*      */       
/*  500 */       for (int k = 0; k < LM + ((isTransient == 0 && narrow == 0) ? 1 : 0); k++) {
/*      */         int B;
/*      */         
/*  503 */         if (isTransient != 0) {
/*  504 */           B = LM - k - 1;
/*      */         } else {
/*  506 */           B = k + 1;
/*      */         } 
/*      */         
/*  509 */         Bands.haar1ZeroOffset(tmp, N >> k, 1 << k);
/*      */         
/*  511 */         L1 = l1_metric(tmp, N, B, bias);
/*      */         
/*  513 */         if (L1 < best_L1) {
/*  514 */           best_L1 = L1;
/*  515 */           best_level = k + 1;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  520 */       if (isTransient != 0) {
/*  521 */         metric[i] = 2 * best_level;
/*      */       } else {
/*  523 */         metric[i] = -2 * best_level;
/*      */       } 
/*  525 */       tf_sum.Val += ((isTransient != 0) ? LM : 0) - metric[i] / 2;
/*      */ 
/*      */       
/*  528 */       if (narrow != 0 && (metric[i] == 0 || metric[i] == -2 * LM)) {
/*  529 */         metric[i] = metric[i] - 1;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  535 */     tf_select = 0;
/*  536 */     for (int sel = 0; sel < 2; sel++) {
/*  537 */       int j = 0;
/*  538 */       int k = (isTransient != 0) ? 0 : lambda;
/*  539 */       for (i = 1; i < len; i++) {
/*      */         
/*  541 */         int curr0 = Inlines.IMIN(j, k + lambda);
/*  542 */         int curr1 = Inlines.IMIN(j + lambda, k);
/*  543 */         j = curr0 + Inlines.abs(metric[i] - 2 * CeltTables.tf_select_table[LM][4 * isTransient + 2 * sel + 0]);
/*  544 */         k = curr1 + Inlines.abs(metric[i] - 2 * CeltTables.tf_select_table[LM][4 * isTransient + 2 * sel + 1]);
/*      */       } 
/*  546 */       j = Inlines.IMIN(j, k);
/*  547 */       selcost[sel] = j;
/*      */     } 
/*      */ 
/*      */     
/*  551 */     if (selcost[1] < selcost[0] && isTransient != 0) {
/*  552 */       tf_select = 1;
/*      */     }
/*  554 */     int cost0 = 0;
/*  555 */     int cost1 = (isTransient != 0) ? 0 : lambda;
/*      */     
/*  557 */     for (i = 1; i < len; i++) {
/*      */ 
/*      */ 
/*      */       
/*  561 */       int curr0, curr1, from0 = cost0;
/*  562 */       int from1 = cost1 + lambda;
/*  563 */       if (from0 < from1) {
/*  564 */         curr0 = from0;
/*  565 */         path0[i] = 0;
/*      */       } else {
/*  567 */         curr0 = from1;
/*  568 */         path0[i] = 1;
/*      */       } 
/*      */       
/*  571 */       from0 = cost0 + lambda;
/*  572 */       from1 = cost1;
/*  573 */       if (from0 < from1) {
/*  574 */         curr1 = from0;
/*  575 */         path1[i] = 0;
/*      */       } else {
/*  577 */         curr1 = from1;
/*  578 */         path1[i] = 1;
/*      */       } 
/*  580 */       cost0 = curr0 + Inlines.abs(metric[i] - 2 * CeltTables.tf_select_table[LM][4 * isTransient + 2 * tf_select + 0]);
/*  581 */       cost1 = curr1 + Inlines.abs(metric[i] - 2 * CeltTables.tf_select_table[LM][4 * isTransient + 2 * tf_select + 1]);
/*      */     } 
/*  583 */     tf_res[len - 1] = (cost0 < cost1) ? 0 : 1;
/*      */     
/*  585 */     for (i = len - 2; i >= 0; i--) {
/*  586 */       if (tf_res[i + 1] == 1) {
/*  587 */         tf_res[i] = path1[i + 1];
/*      */       } else {
/*  589 */         tf_res[i] = path0[i + 1];
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  594 */     return tf_select;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void tf_encode(int start, int end, int isTransient, int[] tf_res, int LM, int tf_select, EntropyCoder enc) {
/*  604 */     int budget = enc.storage * 8;
/*  605 */     int tell = enc.tell();
/*  606 */     int logp = (isTransient != 0) ? 2 : 4;
/*      */     
/*  608 */     int tf_select_rsv = (LM > 0 && tell + logp + 1 <= budget) ? 1 : 0;
/*  609 */     budget -= tf_select_rsv;
/*  610 */     int tf_changed = 0, curr = tf_changed; int i;
/*  611 */     for (i = start; i < end; i++) {
/*  612 */       if (tell + logp <= budget) {
/*  613 */         enc.enc_bit_logp(tf_res[i] ^ curr, logp);
/*  614 */         tell = enc.tell();
/*  615 */         curr = tf_res[i];
/*  616 */         tf_changed |= curr;
/*      */       } else {
/*  618 */         tf_res[i] = curr;
/*      */       } 
/*  620 */       logp = (isTransient != 0) ? 4 : 5;
/*      */     } 
/*      */     
/*  623 */     if (tf_select_rsv != 0 && CeltTables.tf_select_table[LM][4 * isTransient + 0 + tf_changed] != CeltTables.tf_select_table[LM][4 * isTransient + 2 + tf_changed]) {
/*      */ 
/*      */       
/*  626 */       enc.enc_bit_logp(tf_select, 1);
/*      */     } else {
/*  628 */       tf_select = 0;
/*      */     } 
/*  630 */     for (i = start; i < end; i++) {
/*  631 */       tf_res[i] = CeltTables.tf_select_table[LM][4 * isTransient + 2 * tf_select + tf_res[i]];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int alloc_trim_analysis(CeltMode m, int[][] X, int[][] bandLogE, int end, int LM, int C, AnalysisInfo analysis, BoxedValueInt stereo_saving, int tf_estimate, int intensity, int surround_trim) {
/*  641 */     int diff = 0;
/*      */ 
/*      */     
/*  644 */     int trim = 1280;
/*      */     
/*  646 */     if (C == 2) {
/*  647 */       int sum = 0;
/*      */ 
/*      */       
/*      */       int i;
/*      */       
/*  652 */       for (i = 0; i < 8; i++) {
/*      */         
/*  654 */         int partial = Kernels.celt_inner_prod(X[0], m.eBands[i] << LM, X[1], m.eBands[i] << LM, m.eBands[i + 1] - m.eBands[i] << LM);
/*      */         
/*  656 */         sum = Inlines.ADD16(sum, Inlines.EXTRACT16(Inlines.SHR32(partial, 18)));
/*      */       } 
/*  658 */       sum = Inlines.MULT16_16_Q15(4096, sum);
/*  659 */       sum = Inlines.MIN16(1024, Inlines.ABS32(sum));
/*  660 */       int minXC = sum;
/*  661 */       for (i = 8; i < intensity; i++) {
/*      */         
/*  663 */         int partial = Kernels.celt_inner_prod(X[0], m.eBands[i] << LM, X[1], m.eBands[i] << LM, m.eBands[i + 1] - m.eBands[i] << LM);
/*      */         
/*  665 */         minXC = Inlines.MIN16(minXC, Inlines.ABS16(Inlines.EXTRACT16(Inlines.SHR32(partial, 18))));
/*      */       } 
/*  667 */       minXC = Inlines.MIN16(1024, Inlines.ABS32(minXC));
/*      */ 
/*      */       
/*  670 */       int logXC = Inlines.celt_log2(1049625 - Inlines.MULT16_16(sum, sum));
/*      */       
/*  672 */       int logXC2 = Inlines.MAX16(Inlines.HALF16(logXC), Inlines.celt_log2(1049625 - Inlines.MULT16_16(minXC, minXC)));
/*      */       
/*  674 */       logXC = Inlines.PSHR32(logXC - 6144, 2);
/*  675 */       logXC2 = Inlines.PSHR32(logXC2 - 6144, 2);
/*      */       
/*  677 */       trim += Inlines.MAX16(-1024, Inlines.MULT16_16_Q15(24576, logXC));
/*  678 */       stereo_saving.Val = Inlines.MIN16(stereo_saving.Val + 64, 0 - Inlines.HALF16(logXC2));
/*      */     } 
/*      */ 
/*      */     
/*  682 */     int c = 0;
/*      */     while (true) {
/*  684 */       for (int i = 0; i < end - 1; i++) {
/*  685 */         diff += bandLogE[c][i] * (2 + 2 * i - end);
/*      */       }
/*  687 */       if (++c >= C) {
/*  688 */         diff /= C * (end - 1);
/*      */         
/*  690 */         trim -= Inlines.MAX16(Inlines.NEG16((short)512), Inlines.MIN16(512, Inlines.SHR16(diff + 1024, 2) / 6));
/*  691 */         trim -= Inlines.SHR16(surround_trim, 2);
/*  692 */         trim -= 2 * Inlines.SHR16(tf_estimate, 6);
/*  693 */         if (analysis.enabled && analysis.valid != 0) {
/*  694 */           trim -= Inlines.MAX16(-512, Inlines.MIN16(512, (int)(512.0F * (analysis.tonality_slope + 0.05F))));
/*      */         }
/*      */         
/*  697 */         int trim_index = Inlines.PSHR32(trim, 8);
/*  698 */         trim_index = Inlines.IMAX(0, Inlines.IMIN(10, trim_index));
/*      */ 
/*      */         
/*  701 */         return trim_index;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static int stereo_analysis(CeltMode m, int[][] X, int LM) {
/*  708 */     int sumLR = 1, sumMS = 1;
/*      */ 
/*      */     
/*  711 */     for (int i = 0; i < 13; i++) {
/*      */       
/*  713 */       for (int j = m.eBands[i] << LM; j < m.eBands[i + 1] << LM; j++) {
/*      */ 
/*      */         
/*  716 */         int L = Inlines.EXTEND32(X[0][j]);
/*  717 */         int R = Inlines.EXTEND32(X[1][j]);
/*  718 */         int M = Inlines.ADD32(L, R);
/*  719 */         int S = Inlines.SUB32(L, R);
/*  720 */         sumLR = Inlines.ADD32(sumLR, Inlines.ADD32(Inlines.ABS32(L), Inlines.ABS32(R)));
/*  721 */         sumMS = Inlines.ADD32(sumMS, Inlines.ADD32(Inlines.ABS32(M), Inlines.ABS32(S)));
/*      */       } 
/*      */     } 
/*  724 */     sumMS = Inlines.MULT16_32_Q15((short)23170, sumMS);
/*  725 */     int thetas = 13;
/*      */     
/*  727 */     if (LM <= 1) {
/*  728 */       thetas -= 8;
/*      */     }
/*  730 */     return 
/*  731 */       (Inlines.MULT16_32_Q15((m.eBands[13] << LM + 1) + thetas, sumMS) > Inlines.MULT16_32_Q15(m.eBands[13] << LM + 1, sumLR)) ? 1 : 0;
/*      */   }
/*      */ 
/*      */   
/*      */   static int median_of_5(int[] x, int x_ptr) {
/*  736 */     int t0, t1, t3, t4, t2 = x[x_ptr + 2];
/*  737 */     if (x[x_ptr] > x[x_ptr + 1]) {
/*  738 */       t0 = x[x_ptr + 1];
/*  739 */       t1 = x[x_ptr];
/*      */     } else {
/*  741 */       t0 = x[x_ptr];
/*  742 */       t1 = x[x_ptr + 1];
/*      */     } 
/*  744 */     if (x[x_ptr + 3] > x[x_ptr + 4]) {
/*  745 */       t3 = x[x_ptr + 4];
/*  746 */       t4 = x[x_ptr + 3];
/*      */     } else {
/*  748 */       t3 = x[x_ptr + 3];
/*  749 */       t4 = x[x_ptr + 4];
/*      */     } 
/*  751 */     if (t0 > t3) {
/*      */       
/*  753 */       int tmp = t3;
/*  754 */       t3 = t0;
/*  755 */       t0 = tmp;
/*  756 */       tmp = t4;
/*  757 */       t4 = t1;
/*  758 */       t1 = tmp;
/*      */     } 
/*  760 */     if (t2 > t1) {
/*  761 */       if (t1 < t3) {
/*  762 */         return Inlines.MIN16(t2, t3);
/*      */       }
/*  764 */       return Inlines.MIN16(t4, t1);
/*      */     } 
/*  766 */     if (t2 < t3) {
/*  767 */       return Inlines.MIN16(t1, t3);
/*      */     }
/*  769 */     return Inlines.MIN16(t2, t4);
/*      */   }
/*      */ 
/*      */   
/*      */   static int median_of_3(int[] x, int x_ptr) {
/*      */     int t0, t1;
/*  775 */     if (x[x_ptr] > x[x_ptr + 1]) {
/*  776 */       t0 = x[x_ptr + 1];
/*  777 */       t1 = x[x_ptr];
/*      */     } else {
/*  779 */       t0 = x[x_ptr];
/*  780 */       t1 = x[x_ptr + 1];
/*      */     } 
/*  782 */     int t2 = x[x_ptr + 2];
/*  783 */     if (t1 < t2)
/*  784 */       return t1; 
/*  785 */     if (t0 < t2) {
/*  786 */       return t2;
/*      */     }
/*  788 */     return t0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int dynalloc_analysis(int[][] bandLogE, int[][] bandLogE2, int nbEBands, int start, int end, int C, int[] offsets, int lsb_depth, short[] logN, int isTransient, int vbr, int constrained_vbr, short[] eBands, int LM, int effectiveBytes, BoxedValueInt tot_boost_, int lfe, int[] surround_dynalloc) {
/*  797 */     int tot_boost = 0;
/*      */     
/*  799 */     int[][] follower = Arrays.InitTwoDimensionalArrayInt(2, nbEBands);
/*  800 */     int[] noise_floor = new int[C * nbEBands];
/*      */     
/*  802 */     Arrays.MemSet(offsets, 0, nbEBands);
/*      */     
/*  804 */     int maxDepth = -32666; int i;
/*  805 */     for (i = 0; i < end; i++)
/*      */     {
/*      */       
/*  808 */       noise_floor[i] = Inlines.MULT16_16((short)64, logN[i]) + 512 + 
/*  809 */         Inlines.SHL16(9 - lsb_depth, 10) - Inlines.SHL16((short)CeltTables.eMeans[i], 6) + 
/*  810 */         Inlines.MULT16_16(6, (i + 5) * (i + 5));
/*      */     }
/*  812 */     int c = 0;
/*      */     do {
/*  814 */       for (i = 0; i < end; i++) {
/*  815 */         maxDepth = Inlines.MAX16(maxDepth, bandLogE[c][i] - noise_floor[i]);
/*      */       }
/*  817 */     } while (++c < C);
/*      */     
/*  819 */     if (effectiveBytes > 50 && LM >= 1 && lfe == 0) {
/*  820 */       int last = 0;
/*  821 */       c = 0;
/*      */ 
/*      */       
/*      */       while (true) {
/*  825 */         int[] f = follower[c];
/*  826 */         f[0] = bandLogE2[c][0];
/*  827 */         for (i = 1; i < end; i++) {
/*      */ 
/*      */ 
/*      */           
/*  831 */           if (bandLogE2[c][i] > bandLogE2[c][i - 1] + 512) {
/*  832 */             last = i;
/*      */           }
/*  834 */           f[i] = Inlines.MIN16(f[i - 1] + 1536, bandLogE2[c][i]);
/*      */         } 
/*  836 */         for (i = last - 1; i >= 0; i--) {
/*  837 */           f[i] = Inlines.MIN16(f[i], Inlines.MIN16(f[i + 1] + 2048, bandLogE2[c][i]));
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  843 */         int offset = 1024;
/*  844 */         for (i = 2; i < end - 2; i++) {
/*  845 */           f[i] = Inlines.MAX16(f[i], median_of_5(bandLogE2[c], i - 2) - offset);
/*      */         }
/*  847 */         int tmp = median_of_3(bandLogE2[c], 0) - offset;
/*  848 */         f[0] = Inlines.MAX16(f[0], tmp);
/*  849 */         f[1] = Inlines.MAX16(f[1], tmp);
/*  850 */         tmp = median_of_3(bandLogE2[c], end - 3) - offset;
/*  851 */         f[end - 2] = Inlines.MAX16(f[end - 2], tmp);
/*  852 */         f[end - 1] = Inlines.MAX16(f[end - 1], tmp);
/*      */         
/*  854 */         for (i = 0; i < end; i++) {
/*  855 */           f[i] = Inlines.MAX16(f[i], noise_floor[i]);
/*      */         }
/*  857 */         if (++c >= C) {
/*  858 */           if (C == 2) {
/*  859 */             for (i = start; i < end; i++) {
/*      */               
/*  861 */               follower[1][i] = Inlines.MAX16(follower[1][i], follower[0][i] - 4096);
/*  862 */               follower[0][i] = Inlines.MAX16(follower[0][i], follower[1][i] - 4096);
/*  863 */               follower[0][i] = Inlines.HALF16(Inlines.MAX16(0, bandLogE[0][i] - follower[0][i]) + Inlines.MAX16(0, bandLogE[1][i] - follower[1][i]));
/*      */             } 
/*      */           } else {
/*  866 */             for (i = start; i < end; i++) {
/*  867 */               follower[0][i] = Inlines.MAX16(0, bandLogE[0][i] - follower[0][i]);
/*      */             }
/*      */           } 
/*  870 */           for (i = start; i < end; i++) {
/*  871 */             follower[0][i] = Inlines.MAX16(follower[0][i], surround_dynalloc[i]);
/*      */           }
/*      */           
/*  874 */           if ((vbr == 0 || constrained_vbr != 0) && isTransient == 0) {
/*  875 */             for (i = start; i < end; i++) {
/*  876 */               follower[0][i] = Inlines.HALF16(follower[0][i]);
/*      */             }
/*      */           }
/*  879 */           for (i = start; i < end; i++) {
/*      */             int boost, boost_bits;
/*      */ 
/*      */ 
/*      */             
/*  884 */             if (i < 8) {
/*  885 */               follower[0][i] = follower[0][i] * 2;
/*      */             }
/*  887 */             if (i >= 12) {
/*  888 */               follower[0][i] = Inlines.HALF16(follower[0][i]);
/*      */             }
/*  890 */             follower[0][i] = Inlines.MIN16(follower[0][i], 4096);
/*      */             
/*  892 */             int width = C * (eBands[i + 1] - eBands[i]) << LM;
/*  893 */             if (width < 6) {
/*  894 */               boost = Inlines.SHR32(follower[0][i], 10);
/*  895 */               boost_bits = boost * width << 3;
/*  896 */             } else if (width > 48) {
/*  897 */               boost = Inlines.SHR32(follower[0][i] * 8, 10);
/*  898 */               boost_bits = (boost * width << 3) / 8;
/*      */             } else {
/*  900 */               boost = Inlines.SHR32(follower[0][i] * width / 6, 10);
/*  901 */               boost_bits = boost * 6 << 3;
/*      */             } 
/*      */             
/*  904 */             if ((vbr == 0 || (constrained_vbr != 0 && isTransient == 0)) && tot_boost + boost_bits >> 3 >> 3 > effectiveBytes / 4) {
/*      */               
/*  906 */               int cap = effectiveBytes / 4 << 3 << 3;
/*  907 */               offsets[i] = cap - tot_boost;
/*  908 */               tot_boost = cap;
/*      */               break;
/*      */             } 
/*  911 */             offsets[i] = boost;
/*  912 */             tot_boost += boost_bits;
/*      */           }  break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  917 */     tot_boost_.Val = tot_boost;
/*      */     
/*  919 */     return maxDepth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void deemphasis(int[][] input, int[] input_ptrs, short[] pcm, int pcm_ptr, int N, int C, int downsample, int[] coef, int[] mem, int accum) {
/*  926 */     int apply_downsampling = 0;
/*      */     
/*  928 */     int[] scratch = new int[N];
/*  929 */     int coef0 = coef[0];
/*  930 */     int Nd = N / downsample;
/*  931 */     int c = 0;
/*      */ 
/*      */ 
/*      */     
/*      */     do {
/*  936 */       int m = mem[c];
/*  937 */       int[] x = input[c];
/*  938 */       int x_ptr = input_ptrs[c];
/*  939 */       int y = pcm_ptr + c;
/*  940 */       if (downsample > 1) {
/*      */         
/*  942 */         for (int j = 0; j < N; j++) {
/*  943 */           int tmp = x[x_ptr + j] + m + 0;
/*  944 */           m = Inlines.MULT16_32_Q15(coef0, tmp);
/*  945 */           scratch[j] = tmp;
/*      */         } 
/*  947 */         apply_downsampling = 1;
/*  948 */       } else if (accum != 0) {
/*      */         
/*  950 */         for (int j = 0; j < N; j++) {
/*  951 */           int tmp = x[x_ptr + j] + m + 0;
/*  952 */           m = Inlines.MULT16_32_Q15(coef0, tmp);
/*  953 */           pcm[y + j * C] = Inlines.SAT16(Inlines.ADD32(pcm[y + j * C], Inlines.SIG2WORD16(tmp)));
/*      */         } 
/*      */       } else {
/*  956 */         for (int j = 0; j < N; j++) {
/*  957 */           int tmp = x[x_ptr + j] + m + 0;
/*  958 */           if (x[x_ptr + j] > 0 && m > 0 && tmp < 0) {
/*      */             
/*  960 */             tmp = Integer.MAX_VALUE;
/*  961 */             m = Integer.MAX_VALUE;
/*      */           } else {
/*  963 */             m = Inlines.MULT16_32_Q15(coef0, tmp);
/*      */           } 
/*  965 */           pcm[y + j * C] = Inlines.SIG2WORD16(tmp);
/*      */         } 
/*      */       } 
/*  968 */       mem[c] = m;
/*      */       
/*  970 */       if (apply_downsampling == 0) {
/*      */         continue;
/*      */       }
/*  973 */       for (byte b = 0; b < Nd; b++) {
/*  974 */         pcm[y + b * C] = Inlines.SIG2WORD16(scratch[b * downsample]);
/*      */       
/*      */       }
/*      */     }
/*  978 */     while (++c < C);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void celt_synthesis(CeltMode mode, int[][] X, int[][] out_syn, int[] out_syn_ptrs, int[] oldBandE, int start, int effEnd, int C, int CC, int isTransient, int LM, int downsample, int silence) {
/*  996 */     int B, NB, shift, overlap = mode.overlap;
/*  997 */     int nbEBands = mode.nbEBands;
/*  998 */     int N = mode.shortMdctSize << LM;
/*  999 */     int[] freq = new int[N];
/*      */ 
/*      */ 
/*      */     
/* 1003 */     int M = 1 << LM;
/*      */     
/* 1005 */     if (isTransient != 0) {
/* 1006 */       B = M;
/* 1007 */       NB = mode.shortMdctSize;
/* 1008 */       shift = mode.maxLM;
/*      */     } else {
/* 1010 */       B = 1;
/* 1011 */       NB = mode.shortMdctSize << LM;
/* 1012 */       shift = mode.maxLM - LM;
/*      */     } 
/*      */     
/* 1015 */     if (CC == 2 && C == 1) {
/*      */ 
/*      */       
/* 1018 */       Bands.denormalise_bands(mode, X[0], freq, 0, oldBandE, 0, start, effEnd, M, downsample, silence);
/*      */ 
/*      */       
/* 1021 */       int freq2 = out_syn_ptrs[1] + overlap / 2;
/* 1022 */       System.arraycopy(freq, 0, out_syn[1], freq2, N); int b;
/* 1023 */       for (b = 0; b < B; b++) {
/* 1024 */         MDCT.clt_mdct_backward(mode.mdct, out_syn[1], freq2 + b, out_syn[0], out_syn_ptrs[0] + NB * b, mode.window, overlap, shift, B);
/*      */       }
/* 1026 */       for (b = 0; b < B; b++) {
/* 1027 */         MDCT.clt_mdct_backward(mode.mdct, freq, b, out_syn[1], out_syn_ptrs[1] + NB * b, mode.window, overlap, shift, B);
/*      */       }
/* 1029 */     } else if (CC == 1 && C == 2) {
/*      */       
/* 1031 */       int freq2 = out_syn_ptrs[0] + overlap / 2;
/* 1032 */       Bands.denormalise_bands(mode, X[0], freq, 0, oldBandE, 0, start, effEnd, M, downsample, silence);
/*      */ 
/*      */       
/* 1035 */       Bands.denormalise_bands(mode, X[1], out_syn[0], freq2, oldBandE, nbEBands, start, effEnd, M, downsample, silence);
/*      */       
/* 1037 */       for (int i = 0; i < N; i++) {
/* 1038 */         freq[i] = Inlines.HALF32(Inlines.ADD32(freq[i], out_syn[0][freq2 + i]));
/*      */       }
/* 1040 */       for (int b = 0; b < B; b++) {
/* 1041 */         MDCT.clt_mdct_backward(mode.mdct, freq, b, out_syn[0], out_syn_ptrs[0] + NB * b, mode.window, overlap, shift, B);
/*      */       }
/*      */     } else {
/*      */       
/* 1045 */       int c = 0;
/*      */       do {
/* 1047 */         Bands.denormalise_bands(mode, X[c], freq, 0, oldBandE, c * nbEBands, start, effEnd, M, downsample, silence);
/*      */         
/* 1049 */         for (int b = 0; b < B; b++) {
/* 1050 */           MDCT.clt_mdct_backward(mode.mdct, freq, b, out_syn[c], out_syn_ptrs[c] + NB * b, mode.window, overlap, shift, B);
/*      */         }
/* 1052 */       } while (++c < CC);
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
/*      */   static void tf_decode(int start, int end, int isTransient, int[] tf_res, int LM, EntropyCoder dec) {
/* 1065 */     int budget = dec.storage * 8;
/* 1066 */     int tell = dec.tell();
/* 1067 */     int logp = (isTransient != 0) ? 2 : 4;
/* 1068 */     int tf_select_rsv = (LM > 0 && tell + logp + 1 <= budget) ? 1 : 0;
/* 1069 */     budget -= tf_select_rsv;
/* 1070 */     int curr = 0, tf_changed = curr; int i;
/* 1071 */     for (i = start; i < end; i++) {
/* 1072 */       if (tell + logp <= budget) {
/* 1073 */         curr ^= dec.dec_bit_logp(logp);
/* 1074 */         tell = dec.tell();
/* 1075 */         tf_changed |= curr;
/*      */       } 
/* 1077 */       tf_res[i] = curr;
/* 1078 */       logp = (isTransient != 0) ? 4 : 5;
/*      */     } 
/* 1080 */     int tf_select = 0;
/* 1081 */     if (tf_select_rsv != 0 && CeltTables.tf_select_table[LM][4 * isTransient + 0 + tf_changed] != CeltTables.tf_select_table[LM][4 * isTransient + 2 + tf_changed])
/*      */     {
/*      */       
/* 1084 */       tf_select = dec.dec_bit_logp(1L);
/*      */     }
/* 1086 */     for (i = start; i < end; i++) {
/* 1087 */       tf_res[i] = CeltTables.tf_select_table[LM][4 * isTransient + 2 * tf_select + tf_res[i]];
/*      */     }
/*      */   }
/*      */   
/*      */   static int celt_plc_pitch_search(int[][] decode_mem, int C) {
/* 1092 */     BoxedValueInt pitch_index = new BoxedValueInt(0);
/* 1093 */     int[] lp_pitch_buf = new int[1024];
/* 1094 */     Pitch.pitch_downsample(decode_mem, lp_pitch_buf, 2048, C);
/*      */     
/* 1096 */     Pitch.pitch_search(lp_pitch_buf, 360, lp_pitch_buf, 1328, 620, pitch_index);
/*      */ 
/*      */     
/* 1099 */     pitch_index.Val = 720 - pitch_index.Val;
/*      */     
/* 1101 */     return pitch_index.Val;
/*      */   }
/*      */ 
/*      */   
/*      */   static int resampling_factor(int rate) {
/* 1106 */     switch (rate)
/*      */     { case 48000:
/* 1108 */         ret = 1;
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
/* 1127 */         return ret;case 24000: ret = 2; return ret;case 16000: ret = 3; return ret;case 12000: ret = 4; return ret;case 8000: ret = 6; return ret; }  Inlines.OpusAssert(false); int ret = 0; return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void comb_filter_const(int[] y, int y_ptr, int[] x, int x_ptr, int T, int N, int g10, int g11, int g12) {
/* 1134 */     int xpt = x_ptr - T;
/* 1135 */     int x4 = x[xpt - 2];
/* 1136 */     int x3 = x[xpt - 1];
/* 1137 */     int x2 = x[xpt];
/* 1138 */     int x1 = x[xpt + 1];
/* 1139 */     for (int i = 0; i < N; i++) {
/* 1140 */       int x0 = x[xpt + i + 2];
/* 1141 */       y[y_ptr + i] = x[x_ptr + i] + 
/* 1142 */         Inlines.MULT16_32_Q15(g10, x2) + 
/* 1143 */         Inlines.MULT16_32_Q15(g11, Inlines.ADD32(x1, x3)) + 
/* 1144 */         Inlines.MULT16_32_Q15(g12, Inlines.ADD32(x0, x4));
/* 1145 */       x4 = x3;
/* 1146 */       x3 = x2;
/* 1147 */       x2 = x1;
/* 1148 */       x1 = x0;
/*      */     } 
/*      */   }
/*      */   
/* 1152 */   private static final short[][] gains = new short[][] { { 10048, 7112, 4248 }, { 15200, 8784, 0 }, { 26208, 3280, 0 } };
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
/*      */   static void comb_filter(int[] y, int y_ptr, int[] x, int x_ptr, int T0, int T1, int N, int g0, int g1, int tapset0, int tapset1, int[] window, int overlap) {
/* 1166 */     if (g0 == 0 && g1 == 0) {
/*      */       
/* 1168 */       if (x_ptr != y_ptr);
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1174 */     int g00 = Inlines.MULT16_16_P15(g0, gains[tapset0][0]);
/* 1175 */     int g01 = Inlines.MULT16_16_P15(g0, gains[tapset0][1]);
/* 1176 */     int g02 = Inlines.MULT16_16_P15(g0, gains[tapset0][2]);
/* 1177 */     int g10 = Inlines.MULT16_16_P15(g1, gains[tapset1][0]);
/* 1178 */     int g11 = Inlines.MULT16_16_P15(g1, gains[tapset1][1]);
/* 1179 */     int g12 = Inlines.MULT16_16_P15(g1, gains[tapset1][2]);
/* 1180 */     int x1 = x[x_ptr - T1 + 1];
/* 1181 */     int x2 = x[x_ptr - T1];
/* 1182 */     int x3 = x[x_ptr - T1 - 1];
/* 1183 */     int x4 = x[x_ptr - T1 - 2];
/*      */     
/* 1185 */     if (g0 == g1 && T0 == T1 && tapset0 == tapset1)
/* 1186 */       overlap = 0; 
/*      */     int i;
/* 1188 */     for (i = 0; i < overlap; i++) {
/*      */       
/* 1190 */       int x0 = x[x_ptr + i - T1 + 2];
/* 1191 */       int f = Inlines.MULT16_16_Q15(window[i], window[i]);
/* 1192 */       y[y_ptr + i] = x[x_ptr + i] + 
/* 1193 */         Inlines.MULT16_32_Q15(Inlines.MULT16_16_Q15((short)(32767 - f), g00), x[x_ptr + i - T0]) + 
/* 1194 */         Inlines.MULT16_32_Q15(Inlines.MULT16_16_Q15((short)(32767 - f), g01), Inlines.ADD32(x[x_ptr + i - T0 + 1], x[x_ptr + i - T0 - 1])) + 
/* 1195 */         Inlines.MULT16_32_Q15(Inlines.MULT16_16_Q15((short)(32767 - f), g02), Inlines.ADD32(x[x_ptr + i - T0 + 2], x[x_ptr + i - T0 - 2])) + 
/* 1196 */         Inlines.MULT16_32_Q15(Inlines.MULT16_16_Q15(f, g10), x2) + 
/* 1197 */         Inlines.MULT16_32_Q15(Inlines.MULT16_16_Q15(f, g11), Inlines.ADD32(x1, x3)) + 
/* 1198 */         Inlines.MULT16_32_Q15(Inlines.MULT16_16_Q15(f, g12), Inlines.ADD32(x0, x4));
/* 1199 */       x4 = x3;
/* 1200 */       x3 = x2;
/* 1201 */       x2 = x1;
/* 1202 */       x1 = x0;
/*      */     } 
/*      */     
/* 1205 */     if (g1 == 0) {
/*      */       
/* 1207 */       if (x_ptr != y_ptr);
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/* 1214 */     comb_filter_const(y, y_ptr + i, x, x_ptr + i, T1, N - i, g10, g11, g12);
/*      */   }
/*      */   
/* 1217 */   private static final byte[][] tf_select_table = new byte[][] { { 0, -1, 0, -1, 0, -1, 0, -1 }, { 0, -1, 0, -2, 1, 0, 1, -1 }, { 0, -2, 0, -3, 2, 0, 1, -1 }, { 0, -2, 0, -3, 3, 0, 1, -1 } };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void init_caps(CeltMode m, int[] cap, int LM, int C) {
/* 1225 */     for (int i = 0; i < m.nbEBands; i++) {
/*      */       
/* 1227 */       int N = m.eBands[i + 1] - m.eBands[i] << LM;
/* 1228 */       cap[i] = (m.cache.caps[m.nbEBands * (2 * LM + C - 1) + i] + 64) * C * N >> 2;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\CeltCommon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */