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
/*     */ class Analysis
/*     */ {
/*     */   private static final double M_PI = 3.141592653D;
/*     */   private static final float cA = 0.43157974F;
/*     */   private static final float cB = 0.678484F;
/*     */   private static final float cC = 0.08595542F;
/*     */   private static final float cE = 1.5707964F;
/*     */   private static final int NB_TONAL_SKIP_BANDS = 9;
/*     */   
/*     */   static float fast_atan2f(float y, float x) {
/*  50 */     if (Inlines.ABS16(x) + Inlines.ABS16(y) < 1.0E-9F) {
/*  51 */       x *= 1.0E12F;
/*  52 */       y *= 1.0E12F;
/*     */     } 
/*  54 */     float x2 = x * x;
/*  55 */     float y2 = y * y;
/*  56 */     if (x2 < y2) {
/*  57 */       float f = (y2 + 0.678484F * x2) * (y2 + 0.08595542F * x2);
/*  58 */       if (f != 0.0F) {
/*  59 */         return -x * y * (y2 + 0.43157974F * x2) / f + ((y < 0.0F) ? -1.5707964F : 1.5707964F);
/*     */       }
/*  61 */       return (y < 0.0F) ? -1.5707964F : 1.5707964F;
/*     */     } 
/*     */     
/*  64 */     float den = (x2 + 0.678484F * y2) * (x2 + 0.08595542F * y2);
/*  65 */     if (den != 0.0F) {
/*  66 */       return x * y * (x2 + 0.43157974F * y2) / den + ((y < 0.0F) ? -1.5707964F : 1.5707964F) - ((x * y < 0.0F) ? -1.5707964F : 1.5707964F);
/*     */     }
/*  68 */     return ((y < 0.0F) ? -1.5707964F : 1.5707964F) - ((x * y < 0.0F) ? -1.5707964F : 1.5707964F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void tonality_analysis_init(TonalityAnalysisState tonal) {
/*  74 */     tonal.Reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void tonality_get_info(TonalityAnalysisState tonal, AnalysisInfo info_out, int len) {
/*  83 */     int pos = tonal.read_pos;
/*  84 */     int curr_lookahead = tonal.write_pos - tonal.read_pos;
/*  85 */     if (curr_lookahead < 0) {
/*  86 */       curr_lookahead += 200;
/*     */     }
/*     */ 
/*     */     
/*  90 */     pos++;
/*  91 */     if (len > 480 && pos != tonal.write_pos && pos == 200) {
/*  92 */       pos = 0;
/*     */     }
/*     */     
/*  95 */     if (pos == tonal.write_pos) {
/*  96 */       pos--;
/*     */     }
/*  98 */     if (pos < 0) {
/*  99 */       pos = 199;
/*     */     }
/*     */     
/* 102 */     info_out.Assign(tonal.info[pos]);
/* 103 */     tonal.read_subframe += len / 120;
/* 104 */     while (tonal.read_subframe >= 4) {
/* 105 */       tonal.read_subframe -= 4;
/* 106 */       tonal.read_pos++;
/*     */     } 
/* 108 */     if (tonal.read_pos >= 200) {
/* 109 */       tonal.read_pos -= 200;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 114 */     curr_lookahead = Inlines.IMAX(curr_lookahead - 10, 0);
/*     */     
/* 116 */     float psum = 0.0F;
/*     */     
/*     */     int i;
/* 119 */     for (i = 0; i < 200 - curr_lookahead; i++) {
/* 120 */       psum += tonal.pmusic[i];
/*     */     }
/* 122 */     for (; i < 200; i++) {
/* 123 */       psum += tonal.pspeech[i];
/*     */     }
/* 125 */     psum = psum * tonal.music_confidence + (1.0F - psum) * tonal.speech_confidence;
/*     */ 
/*     */     
/* 128 */     info_out.music_prob = psum;
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
/*     */   static void tonality_analysis(TonalityAnalysisState tonal, CeltMode celt_mode, short[] x, int x_ptr, int len, int offset, int c1, int c2, int C, int lsb_depth) {
/* 148 */     int N = 480, N2 = 240;
/* 149 */     float[] A = tonal.angle;
/* 150 */     float[] dA = tonal.d_angle;
/* 151 */     float[] d2A = tonal.d2_angle;
/*     */ 
/*     */     
/* 154 */     float[] band_tonality = new float[18];
/* 155 */     float[] logE = new float[18];
/* 156 */     float[] BFCC = new float[8];
/* 157 */     float[] features = new float[25];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     float pi4 = 97.40909F;
/* 163 */     float slope = 0.0F;
/*     */ 
/*     */     
/* 166 */     float[] frame_probs = new float[2];
/*     */ 
/*     */ 
/*     */     
/* 170 */     int bandwidth = 0;
/* 171 */     float maxE = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     tonal.last_transition++;
/* 177 */     float alpha = 1.0F / Inlines.IMIN(20, 1 + tonal.count);
/* 178 */     float alphaE = 1.0F / Inlines.IMIN(50, 1 + tonal.count);
/* 179 */     float alphaE2 = 1.0F / Inlines.IMIN(1000, 1 + tonal.count);
/*     */     
/* 181 */     if (tonal.count < 4) {
/* 182 */       tonal.music_prob = 0.5F;
/*     */     }
/* 184 */     FFTState kfft = celt_mode.mdct.kfft[0];
/* 185 */     if (tonal.count == 0) {
/* 186 */       tonal.mem_fill = 240;
/*     */     }
/*     */     
/* 189 */     Downmix.downmix_int(x, x_ptr, tonal.inmem, tonal.mem_fill, Inlines.IMIN(len, 720 - tonal.mem_fill), offset, c1, c2, C);
/*     */     
/* 191 */     if (tonal.mem_fill + len < 720) {
/* 192 */       tonal.mem_fill += len;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 197 */     AnalysisInfo info = tonal.info[tonal.write_pos++];
/* 198 */     if (tonal.write_pos >= 200) {
/* 199 */       tonal.write_pos -= 200;
/*     */     }
/*     */     
/* 202 */     int[] input = new int[960];
/* 203 */     int[] output = new int[960];
/* 204 */     float[] tonality = new float[240];
/* 205 */     float[] noisiness = new float[240]; int i;
/* 206 */     for (i = 0; i < N2; i++) {
/* 207 */       float w = OpusTables.analysis_window[i];
/* 208 */       input[2 * i] = (int)(w * tonal.inmem[i]);
/* 209 */       input[2 * i + 1] = (int)(w * tonal.inmem[N2 + i]);
/* 210 */       input[2 * (N - i - 1)] = (int)(w * tonal.inmem[N - i - 1]);
/* 211 */       input[2 * (N - i - 1) + 1] = (int)(w * tonal.inmem[N + N2 - i - 1]);
/*     */     } 
/* 213 */     Arrays.MemMove(tonal.inmem, 480, 0, 240);
/*     */     
/* 215 */     int remaining = len - 720 - tonal.mem_fill;
/* 216 */     Downmix.downmix_int(x, x_ptr, tonal.inmem, 240, remaining, offset + 720 - tonal.mem_fill, c1, c2, C);
/* 217 */     tonal.mem_fill = 240 + remaining;
/*     */     
/* 219 */     KissFFT.opus_fft(kfft, input, output);
/*     */     
/* 221 */     for (i = 1; i < N2; i++) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 226 */       float X1r = output[2 * i] + output[2 * (N - i)];
/* 227 */       float X1i = output[2 * i + 1] - output[2 * (N - i) + 1];
/* 228 */       float X2r = output[2 * i + 1] + output[2 * (N - i) + 1];
/* 229 */       float X2i = output[2 * (N - i)] - output[2 * i];
/*     */       
/* 231 */       float angle = 0.15915494F * fast_atan2f(X1i, X1r);
/* 232 */       float d_angle = angle - A[i];
/* 233 */       float d2_angle = d_angle - dA[i];
/*     */       
/* 235 */       float angle2 = 0.15915494F * fast_atan2f(X2i, X2r);
/* 236 */       float d_angle2 = angle2 - angle;
/* 237 */       float d2_angle2 = d_angle2 - d_angle;
/*     */       
/* 239 */       float mod1 = d2_angle - (float)Math.floor((0.5F + d2_angle));
/* 240 */       noisiness[i] = Inlines.ABS16(mod1);
/* 241 */       mod1 *= mod1;
/* 242 */       mod1 *= mod1;
/*     */       
/* 244 */       float mod2 = d2_angle2 - (float)Math.floor((0.5F + d2_angle2));
/* 245 */       noisiness[i] = noisiness[i] + Inlines.ABS16(mod2);
/* 246 */       mod2 *= mod2;
/* 247 */       mod2 *= mod2;
/*     */       
/* 249 */       float avg_mod = 0.25F * (d2A[i] + 2.0F * mod1 + mod2);
/* 250 */       tonality[i] = 1.0F / (1.0F + 640.0F * pi4 * avg_mod) - 0.015F;
/*     */       
/* 252 */       A[i] = angle2;
/* 253 */       dA[i] = d_angle2;
/* 254 */       d2A[i] = mod2;
/*     */     } 
/*     */     
/* 257 */     float frame_tonality = 0.0F;
/* 258 */     float max_frame_tonality = 0.0F;
/*     */     
/* 260 */     info.activity = 0.0F;
/* 261 */     float frame_noisiness = 0.0F;
/* 262 */     float frame_stationarity = 0.0F;
/* 263 */     if (tonal.count == 0) {
/* 264 */       for (int j = 0; j < 18; j++) {
/* 265 */         tonal.lowE[j] = 1.0E10F;
/* 266 */         tonal.highE[j] = -1.0E10F;
/*     */       } 
/*     */     }
/* 269 */     float relativeE = 0.0F;
/* 270 */     float frame_loudness = 0.0F; int b;
/* 271 */     for (b = 0; b < 18; b++) {
/* 272 */       float E = 0.0F, tE = 0.0F, nE = 0.0F;
/*     */ 
/*     */       
/* 275 */       for (i = OpusTables.tbands[b]; i < OpusTables.tbands[b + 1]; i++) {
/* 276 */         float binE = output[2 * i] * output[2 * i] + output[2 * (N - i)] * output[2 * (N - i)] + output[2 * i + 1] * output[2 * i + 1] + output[2 * (N - i) + 1] * output[2 * (N - i) + 1];
/*     */ 
/*     */         
/* 279 */         binE *= 5.55E-17F;
/* 280 */         E += binE;
/* 281 */         tE += binE * tonality[i];
/* 282 */         nE += binE * 2.0F * (0.5F - noisiness[i]);
/*     */       } 
/*     */       
/* 285 */       tonal.E[tonal.E_count][b] = E;
/* 286 */       frame_noisiness += nE / (1.0E-15F + E);
/*     */       
/* 288 */       frame_loudness += (float)Math.sqrt((E + 1.0E-10F));
/* 289 */       logE[b] = (float)Math.log((E + 1.0E-10F));
/* 290 */       tonal.lowE[b] = Inlines.MIN32(logE[b], tonal.lowE[b] + 0.01F);
/* 291 */       tonal.highE[b] = Inlines.MAX32(logE[b], tonal.highE[b] - 0.1F);
/* 292 */       if (tonal.highE[b] < tonal.lowE[b] + 1.0F) {
/* 293 */         tonal.highE[b] = tonal.highE[b] + 0.5F;
/* 294 */         tonal.lowE[b] = tonal.lowE[b] - 0.5F;
/*     */       } 
/* 296 */       relativeE += (logE[b] - tonal.lowE[b]) / (1.0E-15F + tonal.highE[b] - tonal.lowE[b]);
/*     */       
/* 298 */       float L2 = 0.0F, L1 = L2;
/* 299 */       for (i = 0; i < 8; i++) {
/* 300 */         L1 += (float)Math.sqrt(tonal.E[i][b]);
/* 301 */         L2 += tonal.E[i][b];
/*     */       } 
/*     */       
/* 304 */       float stationarity = Inlines.MIN16(0.99F, L1 / (float)Math.sqrt(1.0E-15D + (8.0F * L2)));
/* 305 */       stationarity *= stationarity;
/* 306 */       stationarity *= stationarity;
/* 307 */       frame_stationarity += stationarity;
/*     */       
/* 309 */       band_tonality[b] = Inlines.MAX16(tE / (1.0E-15F + E), stationarity * tonal.prev_band_tonality[b]);
/* 310 */       frame_tonality += band_tonality[b];
/* 311 */       if (b >= 9) {
/* 312 */         frame_tonality -= band_tonality[b - 18 + 9];
/*     */       }
/* 314 */       max_frame_tonality = Inlines.MAX16(max_frame_tonality, (1.0F + 0.03F * (b - 18)) * frame_tonality);
/* 315 */       slope += band_tonality[b] * (b - 8);
/* 316 */       tonal.prev_band_tonality[b] = band_tonality[b];
/*     */     } 
/*     */     
/* 319 */     float bandwidth_mask = 0.0F;
/* 320 */     bandwidth = 0;
/* 321 */     maxE = 0.0F;
/* 322 */     float noise_floor = 5.7E-4F / (1 << Inlines.IMAX(0, lsb_depth - 8));
/* 323 */     noise_floor *= 1.3421773E8F;
/* 324 */     noise_floor *= noise_floor;
/* 325 */     for (b = 0; b < 21; b++) {
/* 326 */       float E = 0.0F;
/*     */ 
/*     */       
/* 329 */       int band_start = OpusTables.extra_bands[b];
/* 330 */       int band_end = OpusTables.extra_bands[b + 1];
/* 331 */       for (i = band_start; i < band_end; i++) {
/* 332 */         float binE = output[2 * i] * output[2 * i] + output[2 * (N - i)] * output[2 * (N - i)] + output[2 * i + 1] * output[2 * i + 1] + output[2 * (N - i) + 1] * output[2 * (N - i) + 1];
/*     */         
/* 334 */         E += binE;
/*     */       } 
/* 336 */       maxE = Inlines.MAX32(maxE, E);
/* 337 */       tonal.meanE[b] = Inlines.MAX32((1.0F - alphaE2) * tonal.meanE[b], E);
/* 338 */       E = Inlines.MAX32(E, tonal.meanE[b]);
/*     */       
/* 340 */       bandwidth_mask = Inlines.MAX32(0.05F * bandwidth_mask, E);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 347 */       if (E > 0.1D * bandwidth_mask && E * 1.0E9F > maxE && E > noise_floor * (band_end - band_start)) {
/* 348 */         bandwidth = b;
/*     */       }
/*     */     } 
/* 351 */     if (tonal.count <= 2) {
/* 352 */       bandwidth = 20;
/*     */     }
/* 354 */     frame_loudness = 20.0F * (float)Math.log10(frame_loudness);
/* 355 */     tonal.Etracker = Inlines.MAX32(tonal.Etracker - 0.03F, frame_loudness);
/* 356 */     tonal.lowECount *= 1.0F - alphaE;
/* 357 */     if (frame_loudness < tonal.Etracker - 30.0F) {
/* 358 */       tonal.lowECount += alphaE;
/*     */     }
/*     */     
/* 361 */     for (i = 0; i < 8; i++) {
/* 362 */       float sum = 0.0F;
/* 363 */       for (b = 0; b < 16; b++) {
/* 364 */         sum += OpusTables.dct_table[i * 16 + b] * logE[b];
/*     */       }
/* 366 */       BFCC[i] = sum;
/*     */     } 
/*     */     
/* 369 */     frame_stationarity /= 18.0F;
/* 370 */     relativeE /= 18.0F;
/* 371 */     if (tonal.count < 10) {
/* 372 */       relativeE = 0.5F;
/*     */     }
/* 374 */     frame_noisiness /= 18.0F;
/* 375 */     info.activity = frame_noisiness + (1.0F - frame_noisiness) * relativeE;
/* 376 */     frame_tonality = max_frame_tonality / 9.0F;
/* 377 */     frame_tonality = Inlines.MAX16(frame_tonality, tonal.prev_tonality * 0.8F);
/* 378 */     tonal.prev_tonality = frame_tonality;
/*     */     
/* 380 */     slope /= 64.0F;
/* 381 */     info.tonality_slope = slope;
/*     */     
/* 383 */     tonal.E_count = (tonal.E_count + 1) % 8;
/* 384 */     tonal.count++;
/* 385 */     info.tonality = frame_tonality;
/*     */     
/* 387 */     for (i = 0; i < 4; i++) {
/* 388 */       features[i] = -0.12299F * (BFCC[i] + tonal.mem[i + 24]) + 0.49195F * (tonal.mem[i] + tonal.mem[i + 16]) + 0.69693F * tonal.mem[i + 8] - 1.4349F * tonal.cmean[i];
/*     */     }
/*     */     
/* 391 */     for (i = 0; i < 4; i++) {
/* 392 */       tonal.cmean[i] = (1.0F - alpha) * tonal.cmean[i] + alpha * BFCC[i];
/*     */     }
/*     */     
/* 395 */     for (i = 0; i < 4; i++) {
/* 396 */       features[4 + i] = 0.63246F * (BFCC[i] - tonal.mem[i + 24]) + 0.31623F * (tonal.mem[i] - tonal.mem[i + 16]);
/*     */     }
/* 398 */     for (i = 0; i < 3; i++) {
/* 399 */       features[8 + i] = 0.53452F * (BFCC[i] + tonal.mem[i + 24]) - 0.26726F * (tonal.mem[i] + tonal.mem[i + 16]) - 0.53452F * tonal.mem[i + 8];
/*     */     }
/*     */     
/* 402 */     if (tonal.count > 5) {
/* 403 */       for (i = 0; i < 9; i++) {
/* 404 */         tonal.std[i] = (1.0F - alpha) * tonal.std[i] + alpha * features[i] * features[i];
/*     */       }
/*     */     }
/*     */     
/* 408 */     for (i = 0; i < 8; i++) {
/* 409 */       tonal.mem[i + 24] = tonal.mem[i + 16];
/* 410 */       tonal.mem[i + 16] = tonal.mem[i + 8];
/* 411 */       tonal.mem[i + 8] = tonal.mem[i];
/* 412 */       tonal.mem[i] = BFCC[i];
/*     */     } 
/* 414 */     for (i = 0; i < 9; i++) {
/* 415 */       features[11 + i] = (float)Math.sqrt(tonal.std[i]);
/*     */     }
/* 417 */     features[20] = info.tonality;
/* 418 */     features[21] = info.activity;
/* 419 */     features[22] = frame_stationarity;
/* 420 */     features[23] = info.tonality_slope;
/* 421 */     features[24] = tonal.lowECount;
/*     */     
/* 423 */     if (info.enabled) {
/* 424 */       MultiLayerPerceptron.mlp_process(OpusTables.net, features, frame_probs);
/* 425 */       frame_probs[0] = 0.5F * (frame_probs[0] + 1.0F);
/*     */       
/* 427 */       frame_probs[0] = 0.01F + 1.21F * frame_probs[0] * frame_probs[0] - 0.23F * (float)Math.pow(frame_probs[0], 10.0D);
/*     */       
/* 429 */       frame_probs[1] = 0.5F * frame_probs[1] + 0.5F;
/*     */       
/* 431 */       frame_probs[0] = frame_probs[1] * frame_probs[0] + (1.0F - frame_probs[1]) * 0.5F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 451 */       float tau = 5.0E-5F * frame_probs[1];
/* 452 */       float beta = 0.05F;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 457 */       float p = Inlines.MAX16(0.05F, Inlines.MIN16(0.95F, frame_probs[0]));
/* 458 */       float q = Inlines.MAX16(0.05F, Inlines.MIN16(0.95F, tonal.music_prob));
/* 459 */       beta = 0.01F + 0.05F * Inlines.ABS16(p - q) / (p * (1.0F - q) + q * (1.0F - p));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 464 */       float p0 = (1.0F - tonal.music_prob) * (1.0F - tau) + tonal.music_prob * tau;
/* 465 */       float p1 = tonal.music_prob * (1.0F - tau) + (1.0F - tonal.music_prob) * tau;
/*     */ 
/*     */       
/* 468 */       p0 *= (float)Math.pow((1.0F - frame_probs[0]), beta);
/* 469 */       p1 *= (float)Math.pow(frame_probs[0], beta);
/*     */       
/* 471 */       tonal.music_prob = p1 / (p0 + p1);
/* 472 */       info.music_prob = tonal.music_prob;
/*     */ 
/*     */       
/* 475 */       float psum = 1.0E-20F;
/*     */       
/* 477 */       float speech0 = (float)Math.pow((1.0F - frame_probs[0]), beta);
/* 478 */       float music0 = (float)Math.pow(frame_probs[0], beta);
/* 479 */       if (tonal.count == 1) {
/* 480 */         tonal.pspeech[0] = 0.5F;
/* 481 */         tonal.pmusic[0] = 0.5F;
/*     */       } 
/*     */ 
/*     */       
/* 485 */       float s0 = tonal.pspeech[0] + tonal.pspeech[1];
/* 486 */       float m0 = tonal.pmusic[0] + tonal.pmusic[1];
/*     */       
/* 488 */       tonal.pspeech[0] = s0 * (1.0F - tau) * speech0;
/* 489 */       tonal.pmusic[0] = m0 * (1.0F - tau) * music0;
/*     */       
/* 491 */       for (i = 1; i < 199; i++) {
/* 492 */         tonal.pspeech[i] = tonal.pspeech[i + 1] * speech0;
/* 493 */         tonal.pmusic[i] = tonal.pmusic[i + 1] * music0;
/*     */       } 
/*     */       
/* 496 */       tonal.pspeech[199] = m0 * tau * speech0;
/*     */       
/* 498 */       tonal.pmusic[199] = s0 * tau * music0;
/*     */ 
/*     */       
/* 501 */       for (i = 0; i < 200; i++) {
/* 502 */         psum += tonal.pspeech[i] + tonal.pmusic[i];
/*     */       }
/* 504 */       psum = 1.0F / psum;
/* 505 */       for (i = 0; i < 200; i++) {
/* 506 */         tonal.pspeech[i] = tonal.pspeech[i] * psum;
/* 507 */         tonal.pmusic[i] = tonal.pmusic[i] * psum;
/*     */       } 
/* 509 */       psum = tonal.pmusic[0];
/* 510 */       for (i = 1; i < 200; i++) {
/* 511 */         psum += tonal.pspeech[i];
/*     */       }
/*     */ 
/*     */       
/* 515 */       if (frame_probs[1] > 0.75D) {
/* 516 */         if (tonal.music_prob > 0.9D) {
/*     */           
/* 518 */           float adapt = 1.0F / ++tonal.music_confidence_count;
/* 519 */           tonal.music_confidence_count = Inlines.IMIN(tonal.music_confidence_count, 500);
/* 520 */           tonal.music_confidence += adapt * Inlines.MAX16(-0.2F, frame_probs[0] - tonal.music_confidence);
/*     */         } 
/* 522 */         if (tonal.music_prob < 0.1D) {
/*     */           
/* 524 */           float adapt = 1.0F / ++tonal.speech_confidence_count;
/* 525 */           tonal.speech_confidence_count = Inlines.IMIN(tonal.speech_confidence_count, 500);
/* 526 */           tonal.speech_confidence += adapt * Inlines.MIN16(0.2F, frame_probs[0] - tonal.speech_confidence);
/*     */         } 
/*     */       } else {
/* 529 */         if (tonal.music_confidence_count == 0) {
/* 530 */           tonal.music_confidence = 0.9F;
/*     */         }
/* 532 */         if (tonal.speech_confidence_count == 0) {
/* 533 */           tonal.speech_confidence = 0.1F;
/*     */         }
/*     */       } 
/*     */       
/* 537 */       if (tonal.last_music != ((tonal.music_prob > 0.5F) ? 1 : 0)) {
/* 538 */         tonal.last_transition = 0;
/*     */       }
/* 540 */       tonal.last_music = (tonal.music_prob > 0.5F) ? 1 : 0;
/*     */     } else {
/* 542 */       info.music_prob = 0.0F;
/*     */     } 
/*     */     
/* 545 */     info.bandwidth = bandwidth;
/* 546 */     info.noisiness = frame_noisiness;
/* 547 */     info.valid = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void run_analysis(TonalityAnalysisState analysis, CeltMode celt_mode, short[] analysis_pcm, int analysis_pcm_ptr, int analysis_frame_size, int frame_size, int c1, int c2, int C, int Fs, int lsb_depth, AnalysisInfo analysis_info) {
/* 556 */     if (analysis_pcm != null) {
/*     */       
/* 558 */       analysis_frame_size = Inlines.IMIN(195 * Fs / 100, analysis_frame_size);
/*     */       
/* 560 */       int pcm_len = analysis_frame_size - analysis.analysis_offset;
/* 561 */       int offset = analysis.analysis_offset;
/*     */       while (true) {
/* 563 */         tonality_analysis(analysis, celt_mode, analysis_pcm, analysis_pcm_ptr, Inlines.IMIN(480, pcm_len), offset, c1, c2, C, lsb_depth);
/* 564 */         offset += 480;
/* 565 */         pcm_len -= 480;
/* 566 */         if (pcm_len <= 0) {
/* 567 */           analysis.analysis_offset = analysis_frame_size;
/*     */           
/* 569 */           analysis.analysis_offset -= frame_size; break;
/*     */         } 
/*     */       } 
/* 572 */     }  analysis_info.valid = 0;
/* 573 */     tonality_get_info(analysis, analysis_info, frame_size);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Analysis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */