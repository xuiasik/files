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
/*     */ public class CodecHelpers
/*     */ {
/*     */   private static final int MAX_DYNAMIC_FRAMESIZE = 24;
/*     */   
/*     */   static byte gen_toc(OpusMode mode, int framerate, OpusBandwidth bandwidth, int channels) {
/*  42 */     int period = 0;
/*  43 */     while (framerate < 400) {
/*  44 */       framerate <<= 1;
/*  45 */       period++;
/*     */     } 
/*  47 */     if (mode == OpusMode.MODE_SILK_ONLY) {
/*  48 */       toc = (short)(OpusBandwidthHelpers.GetOrdinal(bandwidth) - OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND) << 5);
/*  49 */       toc = (short)(toc | (short)(period - 2 << 3));
/*  50 */     } else if (mode == OpusMode.MODE_CELT_ONLY) {
/*  51 */       int tmp = OpusBandwidthHelpers.GetOrdinal(bandwidth) - OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND);
/*  52 */       if (tmp < 0) {
/*  53 */         tmp = 0;
/*     */       }
/*  55 */       toc = 128;
/*  56 */       toc = (short)(toc | (short)(tmp << 5));
/*  57 */       toc = (short)(toc | (short)(period << 3));
/*     */     } else {
/*  59 */       toc = 96;
/*  60 */       toc = (short)(toc | (short)(OpusBandwidthHelpers.GetOrdinal(bandwidth) - OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND) << 4));
/*  61 */       toc = (short)(toc | (short)(period - 2 << 3));
/*     */     } 
/*  63 */     short toc = (short)(toc | (short)(((channels == 2) ? 1 : 0) << 2));
/*  64 */     return (byte)(0xFF & toc);
/*     */   }
/*     */   
/*     */   static void hp_cutoff(short[] input, int input_ptr, int cutoff_Hz, short[] output, int output_ptr, int[] hp_mem, int len, int channels, int Fs) {
/*  68 */     int[] B_Q28 = new int[3];
/*  69 */     int[] A_Q28 = new int[2];
/*     */ 
/*     */     
/*  72 */     Inlines.OpusAssert((cutoff_Hz <= 869074));
/*  73 */     int Fc_Q19 = Inlines.silk_DIV32_16(Inlines.silk_SMULBB(2471, cutoff_Hz), Fs / 1000);
/*  74 */     Inlines.OpusAssert((Fc_Q19 > 0 && Fc_Q19 < 32768));
/*     */     
/*  76 */     int r_Q28 = 268435456 - Inlines.silk_MUL(471, Fc_Q19);
/*     */ 
/*     */ 
/*     */     
/*  80 */     B_Q28[0] = r_Q28;
/*  81 */     B_Q28[1] = Inlines.silk_LSHIFT(-r_Q28, 1);
/*  82 */     B_Q28[2] = r_Q28;
/*     */ 
/*     */     
/*  85 */     int r_Q22 = Inlines.silk_RSHIFT(r_Q28, 6);
/*  86 */     A_Q28[0] = Inlines.silk_SMULWW(r_Q22, Inlines.silk_SMULWW(Fc_Q19, Fc_Q19) - 8388608);
/*  87 */     A_Q28[1] = Inlines.silk_SMULWW(r_Q22, r_Q22);
/*     */     
/*  89 */     Filters.silk_biquad_alt(input, input_ptr, B_Q28, A_Q28, hp_mem, 0, output, output_ptr, len, channels);
/*  90 */     if (channels == 2) {
/*  91 */       Filters.silk_biquad_alt(input, input_ptr + 1, B_Q28, A_Q28, hp_mem, 2, output, output_ptr + 1, len, channels);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void dc_reject(short[] input, int input_ptr, int cutoff_Hz, short[] output, int output_ptr, int[] hp_mem, int len, int channels, int Fs) {
/* 100 */     int shift = Inlines.celt_ilog2(Fs / cutoff_Hz * 3);
/* 101 */     for (int c = 0; c < channels; c++) {
/* 102 */       for (int i = 0; i < len; i++) {
/*     */         
/* 104 */         int x = Inlines.SHL32(Inlines.EXTEND32(input[channels * i + c + input_ptr]), 15);
/*     */         
/* 106 */         int tmp = x - hp_mem[2 * c];
/* 107 */         hp_mem[2 * c] = hp_mem[2 * c] + Inlines.PSHR32(x - hp_mem[2 * c], shift);
/*     */         
/* 109 */         int y = tmp - hp_mem[2 * c + 1];
/* 110 */         hp_mem[2 * c + 1] = hp_mem[2 * c + 1] + Inlines.PSHR32(tmp - hp_mem[2 * c + 1], shift);
/* 111 */         output[channels * i + c + output_ptr] = Inlines.EXTRACT16(Inlines.SATURATE(Inlines.PSHR32(y, 15), 32767));
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
/*     */   
/*     */   static void stereo_fade(short[] pcm_buf, int g1, int g2, int overlap48, int frame_size, int channels, int[] window, int Fs) {
/* 128 */     int inc = 48000 / Fs;
/* 129 */     int overlap = overlap48 / inc;
/* 130 */     g1 = 32767 - g1;
/* 131 */     g2 = 32767 - g2; int i;
/* 132 */     for (i = 0; i < overlap; i++) {
/*     */ 
/*     */       
/* 135 */       int w = Inlines.MULT16_16_Q15(window[i * inc], window[i * inc]);
/* 136 */       int g = Inlines.SHR32(Inlines.MAC16_16(Inlines.MULT16_16(w, g2), 32767 - w, g1), 15);
/*     */       
/* 138 */       int diff = Inlines.EXTRACT16(Inlines.HALF32(pcm_buf[i * channels] - pcm_buf[i * channels + 1]));
/* 139 */       diff = Inlines.MULT16_16_Q15(g, diff);
/* 140 */       pcm_buf[i * channels] = (short)(pcm_buf[i * channels] - diff);
/* 141 */       pcm_buf[i * channels + 1] = (short)(pcm_buf[i * channels + 1] + diff);
/*     */     } 
/* 143 */     for (; i < frame_size; i++) {
/*     */       
/* 145 */       int diff = Inlines.EXTRACT16(Inlines.HALF32(pcm_buf[i * channels] - pcm_buf[i * channels + 1]));
/* 146 */       diff = Inlines.MULT16_16_Q15(g2, diff);
/* 147 */       pcm_buf[i * channels] = (short)(pcm_buf[i * channels] - diff);
/* 148 */       pcm_buf[i * channels + 1] = (short)(pcm_buf[i * channels + 1] + diff);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void gain_fade(short[] buffer, int buf_ptr, int g1, int g2, int overlap48, int frame_size, int channels, int[] window, int Fs) {
/* 158 */     int inc = 48000 / Fs;
/* 159 */     int overlap = overlap48 / inc;
/* 160 */     if (channels == 1) {
/* 161 */       for (int i = 0; i < overlap; i++) {
/*     */         
/* 163 */         int w = Inlines.MULT16_16_Q15(window[i * inc], window[i * inc]);
/* 164 */         int g = Inlines.SHR32(Inlines.MAC16_16(Inlines.MULT16_16(w, g2), 32767 - w, g1), 15);
/*     */         
/* 166 */         buffer[buf_ptr + i] = (short)Inlines.MULT16_16_Q15(g, buffer[buf_ptr + i]);
/*     */       } 
/*     */     } else {
/* 169 */       for (int i = 0; i < overlap; i++) {
/*     */         
/* 171 */         int w = Inlines.MULT16_16_Q15(window[i * inc], window[i * inc]);
/* 172 */         int g = Inlines.SHR32(Inlines.MAC16_16(Inlines.MULT16_16(w, g2), 32767 - w, g1), 15);
/*     */         
/* 174 */         buffer[buf_ptr + i * 2] = (short)Inlines.MULT16_16_Q15(g, buffer[buf_ptr + i * 2]);
/* 175 */         buffer[buf_ptr + i * 2 + 1] = (short)Inlines.MULT16_16_Q15(g, buffer[buf_ptr + i * 2 + 1]);
/*     */       } 
/*     */     } 
/* 178 */     int c = 0;
/*     */     do {
/* 180 */       for (int i = overlap; i < frame_size; i++) {
/* 181 */         buffer[buf_ptr + i * channels + c] = (short)Inlines.MULT16_16_Q15(g2, buffer[buf_ptr + i * channels + c]);
/*     */       }
/* 183 */     } while (++c < channels);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static float transient_boost(float[] E, int E_ptr, float[] E_1, int LM, int maxM) {
/* 193 */     float sumE = 0.0F, sumE_1 = 0.0F;
/*     */ 
/*     */     
/* 196 */     int M = Inlines.IMIN(maxM, (1 << LM) + 1);
/* 197 */     for (int i = E_ptr; i < M + E_ptr; i++) {
/* 198 */       sumE += E[i];
/* 199 */       sumE_1 += E_1[i];
/*     */     } 
/* 201 */     float metric = sumE * sumE_1 / (M * M);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     return Inlines.MIN16(1.0F, (float)Math.sqrt(Inlines.MAX16(0.0F, 0.05F * (metric - 2.0F))));
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
/*     */   static int transient_viterbi(float[] E, float[] E_1, int N, int frame_cost, int rate) {
/* 231 */     float factor, cost[][] = Arrays.InitTwoDimensionalArrayFloat(24, 16);
/* 232 */     int[][] states = Arrays.InitTwoDimensionalArrayInt(24, 16);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     if (rate < 80) {
/* 238 */       factor = 0.0F;
/* 239 */     } else if (rate > 160) {
/* 240 */       factor = 1.0F;
/*     */     } else {
/* 242 */       factor = (rate - 80.0F) / 80.0F;
/*     */     } 
/*     */     
/*     */     int i;
/*     */     
/* 247 */     for (i = 0; i < 16; i++) {
/*     */       
/* 249 */       states[0][i] = -1;
/* 250 */       cost[0][i] = 1.0E10F;
/*     */     } 
/* 252 */     for (i = 0; i < 4; i++) {
/* 253 */       cost[0][1 << i] = (frame_cost + rate * (1 << i)) * (1.0F + factor * transient_boost(E, 0, E_1, i, N + 1));
/* 254 */       states[0][1 << i] = i;
/*     */     } 
/* 256 */     for (i = 1; i < N; i++) {
/*     */       int j;
/*     */ 
/*     */       
/* 260 */       for (j = 2; j < 16; j++) {
/* 261 */         cost[i][j] = cost[i - 1][j - 1];
/* 262 */         states[i][j] = j - 1;
/*     */       } 
/*     */ 
/*     */       
/* 266 */       for (j = 0; j < 4; j++) {
/*     */ 
/*     */ 
/*     */         
/* 270 */         states[i][1 << j] = 1;
/* 271 */         float min_cost = cost[i - 1][1];
/* 272 */         for (int k = 1; k < 4; k++) {
/* 273 */           float tmp = cost[i - 1][(1 << k + 1) - 1];
/* 274 */           if (tmp < min_cost) {
/* 275 */             states[i][1 << j] = (1 << k + 1) - 1;
/* 276 */             min_cost = tmp;
/*     */           } 
/*     */         } 
/* 279 */         float curr_cost = (frame_cost + rate * (1 << j)) * (1.0F + factor * transient_boost(E, i, E_1, j, N - i + 1));
/* 280 */         cost[i][1 << j] = min_cost;
/*     */         
/* 282 */         if (N - i < 1 << j) {
/* 283 */           cost[i][1 << j] = cost[i][1 << j] + curr_cost * (N - i) / (1 << j);
/*     */         } else {
/* 285 */           cost[i][1 << j] = cost[i][1 << j] + curr_cost;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 290 */     int best_state = 1;
/* 291 */     float best_cost = cost[N - 1][1];
/*     */     
/* 293 */     for (i = 2; i < 16; i++) {
/* 294 */       if (cost[N - 1][i] < best_cost) {
/* 295 */         best_cost = cost[N - 1][i];
/* 296 */         best_state = i;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 301 */     for (i = N - 1; i >= 0; i--)
/*     */     {
/* 303 */       best_state = states[i][best_state];
/*     */     }
/*     */     
/* 306 */     return best_state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int optimize_framesize(short[] x, int x_ptr, int len, int C, int Fs, int bitrate, int tonality, float[] mem, int buffering) {
/*     */     int pos, offset;
/* 313 */     float[] e = new float[28];
/* 314 */     float[] e_1 = new float[27];
/*     */     
/* 316 */     int bestLM = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     int subframe = Fs / 400;
/* 323 */     int[] sub = new int[subframe];
/* 324 */     e[0] = mem[0];
/* 325 */     e_1[0] = 1.0F / (1.0F + mem[0]);
/* 326 */     if (buffering != 0) {
/*     */ 
/*     */       
/* 329 */       offset = 2 * subframe - buffering;
/* 330 */       Inlines.OpusAssert((offset >= 0 && offset <= subframe));
/* 331 */       len -= offset;
/* 332 */       e[1] = mem[1];
/* 333 */       e_1[1] = 1.0F / (1.0F + mem[1]);
/* 334 */       e[2] = mem[2];
/* 335 */       e_1[2] = 1.0F / (1.0F + mem[2]);
/* 336 */       pos = 3;
/*     */     } else {
/* 338 */       pos = 1;
/* 339 */       offset = 0;
/*     */     } 
/* 341 */     int N = Inlines.IMIN(len / subframe, 24);
/*     */     
/* 343 */     int memx = 0; int i;
/* 344 */     for (i = 0; i < N; i++) {
/*     */ 
/*     */ 
/*     */       
/* 348 */       float tmp = 1.0F;
/*     */       
/* 350 */       Downmix.downmix_int(x, x_ptr, sub, 0, subframe, i * subframe + offset, 0, -2, C);
/* 351 */       if (i == 0) {
/* 352 */         memx = sub[0];
/*     */       }
/* 354 */       for (int j = 0; j < subframe; j++) {
/* 355 */         int tmpx = sub[j];
/* 356 */         tmp += (tmpx - memx) * (tmpx - memx);
/* 357 */         memx = tmpx;
/*     */       } 
/* 359 */       e[i + pos] = tmp;
/* 360 */       e_1[i + pos] = 1.0F / tmp;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 365 */     e[i + pos] = e[i + pos - 1];
/* 366 */     if (buffering != 0) {
/* 367 */       N = Inlines.IMIN(24, N + 2);
/*     */     }
/* 369 */     bestLM = transient_viterbi(e, e_1, N, (int)((1.0F + 0.5F * tonality) * (60 * C + 40)), bitrate / 400);
/* 370 */     mem[0] = e[1 << bestLM];
/* 371 */     if (buffering != 0) {
/* 372 */       mem[1] = e[(1 << bestLM) + 1];
/* 373 */       mem[2] = e[(1 << bestLM) + 2];
/*     */     } 
/* 375 */     return bestLM;
/*     */   }
/*     */   
/*     */   static int frame_size_select(int frame_size, OpusFramesize variable_duration, int Fs) {
/*     */     int new_size;
/* 380 */     if (frame_size < Fs / 400) {
/* 381 */       return -1;
/*     */     }
/* 383 */     if (variable_duration == OpusFramesize.OPUS_FRAMESIZE_ARG) {
/* 384 */       new_size = frame_size;
/* 385 */     } else if (variable_duration == OpusFramesize.OPUS_FRAMESIZE_VARIABLE) {
/* 386 */       new_size = Fs / 50;
/* 387 */     } else if (OpusFramesizeHelpers.GetOrdinal(variable_duration) >= OpusFramesizeHelpers.GetOrdinal(OpusFramesize.OPUS_FRAMESIZE_2_5_MS) && 
/* 388 */       OpusFramesizeHelpers.GetOrdinal(variable_duration) <= OpusFramesizeHelpers.GetOrdinal(OpusFramesize.OPUS_FRAMESIZE_60_MS)) {
/* 389 */       new_size = Inlines.IMIN(3 * Fs / 50, Fs / 400 << OpusFramesizeHelpers.GetOrdinal(variable_duration) - OpusFramesizeHelpers.GetOrdinal(OpusFramesize.OPUS_FRAMESIZE_2_5_MS));
/*     */     } else {
/* 391 */       return -1;
/*     */     } 
/* 393 */     if (new_size > frame_size) {
/* 394 */       return -1;
/*     */     }
/* 396 */     if (400 * new_size != Fs && 200 * new_size != Fs && 100 * new_size != Fs && 50 * new_size != Fs && 25 * new_size != Fs && 50 * new_size != 3 * Fs)
/*     */     {
/* 398 */       return -1;
/*     */     }
/* 400 */     return new_size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int compute_frame_size(short[] analysis_pcm, int analysis_pcm_ptr, int frame_size, OpusFramesize variable_duration, int C, int Fs, int bitrate_bps, int delay_compensation, float[] subframe_mem, boolean analysis_enabled) {
/* 408 */     if (analysis_enabled && variable_duration == OpusFramesize.OPUS_FRAMESIZE_VARIABLE && frame_size >= Fs / 200) {
/* 409 */       int LM = 3;
/* 410 */       LM = optimize_framesize(analysis_pcm, analysis_pcm_ptr, frame_size, C, Fs, bitrate_bps, 0, subframe_mem, delay_compensation);
/*     */       
/* 412 */       while (Fs / 400 << LM > frame_size) {
/* 413 */         LM--;
/*     */       }
/* 415 */       frame_size = Fs / 400 << LM;
/*     */     } else {
/* 417 */       frame_size = frame_size_select(frame_size, variable_duration, Fs);
/*     */     } 
/*     */     
/* 420 */     if (frame_size < 0) {
/* 421 */       return -1;
/*     */     }
/* 423 */     return frame_size;
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
/*     */   static int compute_stereo_width(short[] pcm, int pcm_ptr, int frame_size, int Fs, StereoWidthState mem) {
/* 437 */     int frame_rate = Fs / frame_size;
/* 438 */     int short_alpha = 32767 - 819175 / Inlines.IMAX(50, frame_rate);
/* 439 */     int yy = 0, xy = yy, xx = xy;
/* 440 */     for (int i = 0; i < frame_size - 3; i += 4) {
/* 441 */       int pxx = 0;
/* 442 */       int pxy = 0;
/* 443 */       int pyy = 0;
/*     */       
/* 445 */       int p2i = pcm_ptr + 2 * i;
/* 446 */       int x = pcm[p2i];
/* 447 */       int y = pcm[p2i + 1];
/* 448 */       pxx = Inlines.SHR32(Inlines.MULT16_16(x, x), 2);
/* 449 */       pxy = Inlines.SHR32(Inlines.MULT16_16(x, y), 2);
/* 450 */       pyy = Inlines.SHR32(Inlines.MULT16_16(y, y), 2);
/* 451 */       x = pcm[p2i + 2];
/* 452 */       y = pcm[p2i + 3];
/* 453 */       pxx += Inlines.SHR32(Inlines.MULT16_16(x, x), 2);
/* 454 */       pxy += Inlines.SHR32(Inlines.MULT16_16(x, y), 2);
/* 455 */       pyy += Inlines.SHR32(Inlines.MULT16_16(y, y), 2);
/* 456 */       x = pcm[p2i + 4];
/* 457 */       y = pcm[p2i + 5];
/* 458 */       pxx += Inlines.SHR32(Inlines.MULT16_16(x, x), 2);
/* 459 */       pxy += Inlines.SHR32(Inlines.MULT16_16(x, y), 2);
/* 460 */       pyy += Inlines.SHR32(Inlines.MULT16_16(y, y), 2);
/* 461 */       x = pcm[p2i + 6];
/* 462 */       y = pcm[p2i + 7];
/* 463 */       pxx += Inlines.SHR32(Inlines.MULT16_16(x, x), 2);
/* 464 */       pxy += Inlines.SHR32(Inlines.MULT16_16(x, y), 2);
/* 465 */       pyy += Inlines.SHR32(Inlines.MULT16_16(y, y), 2);
/*     */       
/* 467 */       xx += Inlines.SHR32(pxx, 10);
/* 468 */       xy += Inlines.SHR32(pxy, 10);
/* 469 */       yy += Inlines.SHR32(pyy, 10);
/*     */     } 
/*     */     
/* 472 */     mem.XX += Inlines.MULT16_32_Q15(short_alpha, xx - mem.XX);
/* 473 */     mem.XY += Inlines.MULT16_32_Q15(short_alpha, xy - mem.XY);
/* 474 */     mem.YY += Inlines.MULT16_32_Q15(short_alpha, yy - mem.YY);
/* 475 */     mem.XX = Inlines.MAX32(0, mem.XX);
/* 476 */     mem.XY = Inlines.MAX32(0, mem.XY);
/* 477 */     mem.YY = Inlines.MAX32(0, mem.YY);
/* 478 */     if (Inlines.MAX32(mem.XX, mem.YY) > 210) {
/* 479 */       int sqrt_xx = Inlines.celt_sqrt(mem.XX);
/* 480 */       int sqrt_yy = Inlines.celt_sqrt(mem.YY);
/* 481 */       int qrrt_xx = Inlines.celt_sqrt(sqrt_xx);
/* 482 */       int qrrt_yy = Inlines.celt_sqrt(sqrt_yy);
/*     */       
/* 484 */       mem.XY = Inlines.MIN32(mem.XY, sqrt_xx * sqrt_yy);
/* 485 */       int corr = Inlines.SHR32(Inlines.frac_div32(mem.XY, 1 + Inlines.MULT16_16(sqrt_xx, sqrt_yy)), 16);
/*     */       
/* 487 */       int ldiff = 32767 * Inlines.ABS16(qrrt_xx - qrrt_yy) / (1 + qrrt_xx + qrrt_yy);
/* 488 */       int width = Inlines.MULT16_16_Q15(Inlines.celt_sqrt(1073741824 - Inlines.MULT16_16(corr, corr)), ldiff);
/*     */       
/* 490 */       mem.smoothed_width += (width - mem.smoothed_width) / frame_rate;
/*     */       
/* 492 */       mem.max_follower = Inlines.MAX16(mem.max_follower - 655 / frame_rate, mem.smoothed_width);
/*     */     } else {
/* 494 */       int width = 0;
/* 495 */       int corr = 32767;
/* 496 */       int ldiff = 0;
/*     */     } 
/*     */     
/* 499 */     return Inlines.EXTRACT16(Inlines.MIN32(32767, 20 * mem.max_follower));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void smooth_fade(short[] in1, int in1_ptr, short[] in2, int in2_ptr, short[] output, int output_ptr, int overlap, int channels, int[] window, int Fs) {
/* 506 */     int inc = 48000 / Fs;
/* 507 */     for (int c = 0; c < channels; c++) {
/* 508 */       for (int i = 0; i < overlap; i++) {
/* 509 */         int w = Inlines.MULT16_16_Q15(window[i * inc], window[i * inc]);
/* 510 */         output[output_ptr + i * channels + c] = (short)Inlines.SHR32(Inlines.MAC16_16(Inlines.MULT16_16(w, in2[in2_ptr + i * channels + c]), 32767 - w, in1[in1_ptr + i * channels + c]), 15);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String opus_strerror(int error) {
/* 611 */     String[] error_strings = { "success", "invalid argument", "buffer too small", "error", "corrupted stream", "request not implemented", "invalid state", "memory allocation failed" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 621 */     if (error > 0 || error < -7) {
/* 622 */       return "unknown error";
/*     */     }
/* 624 */     return error_strings[-error];
/*     */   }
/*     */ 
/*     */   
/*     */   public static String GetVersionString() {
/* 629 */     return "concentus 1.0a-java-fixed";
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\CodecHelpers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */