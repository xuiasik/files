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
/*     */ class CeltDecoder
/*     */ {
/*  42 */   CeltMode mode = null;
/*  43 */   int overlap = 0;
/*  44 */   int channels = 0;
/*  45 */   int stream_channels = 0;
/*     */   
/*  47 */   int downsample = 0;
/*  48 */   int start = 0;
/*  49 */   int end = 0;
/*  50 */   int signalling = 0;
/*     */ 
/*     */   
/*  53 */   int rng = 0;
/*  54 */   int error = 0;
/*  55 */   int last_pitch_index = 0;
/*  56 */   int loss_count = 0;
/*  57 */   int postfilter_period = 0;
/*  58 */   int postfilter_period_old = 0;
/*  59 */   int postfilter_gain = 0;
/*  60 */   int postfilter_gain_old = 0;
/*  61 */   int postfilter_tapset = 0;
/*  62 */   int postfilter_tapset_old = 0;
/*     */   
/*  64 */   final int[] preemph_memD = new int[2];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   int[][] decode_mem = null;
/*  79 */   int[][] lpc = null;
/*  80 */   int[] oldEBands = null;
/*  81 */   int[] oldLogE = null;
/*  82 */   int[] oldLogE2 = null;
/*  83 */   int[] backgroundLogE = null;
/*     */   
/*     */   private void Reset() {
/*  86 */     this.mode = null;
/*  87 */     this.overlap = 0;
/*  88 */     this.channels = 0;
/*  89 */     this.stream_channels = 0;
/*  90 */     this.downsample = 0;
/*  91 */     this.start = 0;
/*  92 */     this.end = 0;
/*  93 */     this.signalling = 0;
/*  94 */     PartialReset();
/*     */   }
/*     */   
/*     */   private void PartialReset() {
/*  98 */     this.rng = 0;
/*  99 */     this.error = 0;
/* 100 */     this.last_pitch_index = 0;
/* 101 */     this.loss_count = 0;
/* 102 */     this.postfilter_period = 0;
/* 103 */     this.postfilter_period_old = 0;
/* 104 */     this.postfilter_gain = 0;
/* 105 */     this.postfilter_gain_old = 0;
/* 106 */     this.postfilter_tapset = 0;
/* 107 */     this.postfilter_tapset_old = 0;
/* 108 */     Arrays.MemSet(this.preemph_memD, 0, 2);
/* 109 */     this.decode_mem = null;
/* 110 */     this.lpc = null;
/* 111 */     this.oldEBands = null;
/* 112 */     this.oldLogE = null;
/* 113 */     this.oldLogE2 = null;
/* 114 */     this.backgroundLogE = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void ResetState() {
/* 120 */     PartialReset();
/*     */ 
/*     */     
/* 123 */     this.decode_mem = new int[this.channels][];
/* 124 */     this.lpc = new int[this.channels][];
/* 125 */     for (int c = 0; c < this.channels; c++) {
/* 126 */       this.decode_mem[c] = new int[2048 + this.mode.overlap];
/* 127 */       this.lpc[c] = new int[24];
/*     */     } 
/* 129 */     this.oldEBands = new int[2 * this.mode.nbEBands];
/* 130 */     this.oldLogE = new int[2 * this.mode.nbEBands];
/* 131 */     this.oldLogE2 = new int[2 * this.mode.nbEBands];
/* 132 */     this.backgroundLogE = new int[2 * this.mode.nbEBands];
/*     */     
/* 134 */     for (int i = 0; i < 2 * this.mode.nbEBands; i++) {
/* 135 */       this.oldLogE2[i] = -28672; this.oldLogE[i] = -28672;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   int celt_decoder_init(int sampling_rate, int channels) {
/* 141 */     int ret = opus_custom_decoder_init(CeltMode.mode48000_960_120, channels);
/* 142 */     if (ret != OpusError.OPUS_OK) {
/* 143 */       return ret;
/*     */     }
/* 145 */     this.downsample = CeltCommon.resampling_factor(sampling_rate);
/* 146 */     if (this.downsample == 0) {
/* 147 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 149 */     return OpusError.OPUS_OK;
/*     */   }
/*     */ 
/*     */   
/*     */   private int opus_custom_decoder_init(CeltMode mode, int channels) {
/* 154 */     if (channels < 0 || channels > 2) {
/* 155 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/*     */     
/* 158 */     if (this == null) {
/* 159 */       return OpusError.OPUS_ALLOC_FAIL;
/*     */     }
/*     */     
/* 162 */     Reset();
/*     */     
/* 164 */     this.mode = mode;
/* 165 */     this.overlap = mode.overlap;
/* 166 */     this.stream_channels = this.channels = channels;
/*     */     
/* 168 */     this.downsample = 1;
/* 169 */     this.start = 0;
/* 170 */     this.end = this.mode.effEBands;
/* 171 */     this.signalling = 1;
/*     */     
/* 173 */     this.loss_count = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     ResetState();
/*     */     
/* 183 */     return OpusError.OPUS_OK;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void celt_decode_lost(int N, int LM) {
/* 189 */     int pitch_index, C = this.channels;
/* 190 */     int[][] out_syn = new int[2][];
/* 191 */     int[] out_syn_ptrs = new int[2];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     CeltMode mode = this.mode;
/* 199 */     int nbEBands = mode.nbEBands;
/* 200 */     int overlap = mode.overlap;
/* 201 */     short[] eBands = mode.eBands;
/*     */     
/* 203 */     int c = 0;
/*     */     do {
/* 205 */       out_syn[c] = this.decode_mem[c];
/* 206 */       out_syn_ptrs[c] = 2048 - N;
/* 207 */     } while (++c < C);
/*     */     
/* 209 */     int noise_based = (this.loss_count >= 5 || this.start != 0) ? 1 : 0;
/* 210 */     if (noise_based != 0)
/*     */     
/*     */     { 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 217 */       int end = this.end;
/* 218 */       int effEnd = Inlines.IMAX(this.start, Inlines.IMIN(end, mode.effEBands));
/*     */       
/* 220 */       int[][] X = Arrays.InitTwoDimensionalArrayInt(C, N);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 226 */       int decay = (this.loss_count == 0) ? 1536 : 512;
/* 227 */       c = 0; while (true)
/*     */       { int i;
/* 229 */         for (i = this.start; i < end; i++) {
/* 230 */           this.oldEBands[c * nbEBands + i] = Inlines.MAX16(this.backgroundLogE[c * nbEBands + i], this.oldEBands[c * nbEBands + i] - decay);
/*     */         }
/* 232 */         if (++c >= C) {
/* 233 */           int seed = this.rng;
/* 234 */           for (c = 0; c < C; c++) {
/* 235 */             for (i = this.start; i < effEnd; i++) {
/*     */ 
/*     */ 
/*     */               
/* 239 */               int boffs = eBands[i] << LM;
/* 240 */               int blen = eBands[i + 1] - eBands[i] << LM;
/* 241 */               for (int j = 0; j < blen; j++) {
/* 242 */                 seed = Bands.celt_lcg_rand(seed);
/* 243 */                 X[c][boffs + j] = seed >> 20;
/*     */               } 
/*     */               
/* 246 */               VQ.renormalise_vector(X[c], 0, blen, 32767);
/*     */             } 
/*     */           } 
/* 249 */           this.rng = seed;
/*     */           
/* 251 */           c = 0;
/*     */           do {
/* 253 */             Arrays.MemMove(this.decode_mem[c], N, 0, 2048 - N + (overlap >> 1));
/* 254 */           } while (++c < C);
/*     */           
/* 256 */           CeltCommon.celt_synthesis(mode, X, out_syn, out_syn_ptrs, this.oldEBands, this.start, effEnd, C, C, 0, LM, this.downsample, 0);
/*     */         } else {
/*     */           continue;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 434 */         this.loss_count++; return; }  }  int fade = 32767; if (this.loss_count == 0) { this.last_pitch_index = pitch_index = CeltCommon.celt_plc_pitch_search(this.decode_mem, C); } else { pitch_index = this.last_pitch_index; fade = 26214; }  int[] etmp = new int[overlap]; int[] exc = new int[1024]; int[] window = mode.window; c = 0; do { int S1 = 0; int[] buf = this.decode_mem[c]; int i; for (i = 0; i < 1024; i++) exc[i] = Inlines.ROUND16(buf[1024 + i], 12);  if (this.loss_count == 0) { int[] ac = new int[25]; Autocorrelation._celt_autocorr(exc, ac, window, overlap, 24, 1024); ac[0] = ac[0] + Inlines.SHR32(ac[0], 13); for (i = 1; i <= 24; i++) ac[i] = ac[i] - Inlines.MULT16_32_Q15(2 * i * i, ac[i]);  CeltLPC.celt_lpc(this.lpc[c], ac, 24); }  int exc_length = Inlines.IMIN(2 * pitch_index, 1024); int[] arrayOfInt1 = new int[24]; for (i = 0; i < 24; i++) arrayOfInt1[i] = Inlines.ROUND16(buf[2048 - exc_length - 1 - i], 12);  Kernels.celt_fir(exc, 1024 - exc_length, this.lpc[c], 0, exc, 1024 - exc_length, exc_length, 24, arrayOfInt1); int E1 = 1, E2 = 1; int shift = Inlines.IMAX(0, 2 * Inlines.celt_zlog2(Inlines.celt_maxabs16(exc, 1024 - exc_length, exc_length)) - 20); int decay_length = exc_length >> 1; for (i = 0; i < decay_length; i++) { int e = exc[1024 - decay_length + i]; E1 += Inlines.SHR32(Inlines.MULT16_16(e, e), shift); e = exc[1024 - 2 * decay_length + i]; E2 += Inlines.SHR32(Inlines.MULT16_16(e, e), shift); }  E1 = Inlines.MIN32(E1, E2); int decay = Inlines.celt_sqrt(Inlines.frac_div32(Inlines.SHR32(E1, 1), E2)); Arrays.MemMove(buf, N, 0, 2048 - N); int extrapolation_offset = 1024 - pitch_index; int extrapolation_len = N + overlap; int attenuation = Inlines.MULT16_16_Q15(fade, decay); for (int j = 0; i < extrapolation_len; i++, j++) { if (j >= pitch_index) { j -= pitch_index; attenuation = Inlines.MULT16_16_Q15(attenuation, decay); }  buf[2048 - N + i] = Inlines.SHL32(Inlines.MULT16_16_Q15(attenuation, exc[extrapolation_offset + j]), 12); int tmp = Inlines.ROUND16(buf[1024 - N + extrapolation_offset + j], 12); S1 += Inlines.SHR32(Inlines.MULT16_16(tmp, tmp), 8); }  int[] lpc_mem = new int[24]; for (i = 0; i < 24; i++) lpc_mem[i] = Inlines.ROUND16(buf[2048 - N - 1 - i], 12);  CeltLPC.celt_iir(buf, 2048 - N, this.lpc[c], buf, 2048 - N, extrapolation_len, 24, lpc_mem); int S2 = 0; for (i = 0; i < extrapolation_len; i++) { int tmp = Inlines.ROUND16(buf[2048 - N + i], 12); S2 += Inlines.SHR32(Inlines.MULT16_16(tmp, tmp), 8); }  if (S1 <= Inlines.SHR32(S2, 2)) { for (i = 0; i < extrapolation_len; i++) buf[2048 - N + i] = 0;  } else if (S1 < S2) { int ratio = Inlines.celt_sqrt(Inlines.frac_div32(Inlines.SHR32(S1, 1) + 1, S2 + 1)); for (i = 0; i < overlap; i++) { int tmp_g = 32767 - Inlines.MULT16_16_Q15(window[i], 32767 - ratio); buf[2048 - N + i] = Inlines.MULT16_32_Q15(tmp_g, buf[2048 - N + i]); }  for (i = overlap; i < extrapolation_len; i++) buf[2048 - N + i] = Inlines.MULT16_32_Q15(ratio, buf[2048 - N + i]);  }  CeltCommon.comb_filter(etmp, 0, buf, 2048, this.postfilter_period, this.postfilter_period, overlap, -this.postfilter_gain, -this.postfilter_gain, this.postfilter_tapset, this.postfilter_tapset, null, 0); for (i = 0; i < overlap / 2; i++) buf[2048 + i] = Inlines.MULT16_32_Q15(window[i], etmp[overlap - 1 - i]) + Inlines.MULT16_32_Q15(window[overlap - i - 1], etmp[i]);  } while (++c < C); this.loss_count++;
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
/*     */   int celt_decode_with_ec(byte[] data, int data_ptr, int len, short[] pcm, int pcm_ptr, int frame_size, EntropyCoder dec, int accum) {
/* 450 */     int shortBlocks, isTransient, silence, out_syn[][] = new int[2][];
/* 451 */     int[] out_syn_ptrs = new int[2];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 457 */     int CC = this.channels;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 466 */     int intensity = 0;
/* 467 */     int dual_stereo = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 474 */     int anti_collapse_on = 0;
/*     */     
/* 476 */     int C = this.stream_channels;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 482 */     CeltMode mode = this.mode;
/* 483 */     int nbEBands = mode.nbEBands;
/* 484 */     int overlap = mode.overlap;
/* 485 */     short[] eBands = mode.eBands;
/* 486 */     int start = this.start;
/* 487 */     int end = this.end;
/* 488 */     frame_size *= this.downsample;
/*     */     
/* 490 */     int[] oldBandE = this.oldEBands;
/* 491 */     int[] oldLogE = this.oldLogE;
/* 492 */     int[] oldLogE2 = this.oldLogE2;
/* 493 */     int[] backgroundLogE = this.backgroundLogE;
/*     */     
/*     */     int LM;
/* 496 */     for (LM = 0; LM <= mode.maxLM && 
/* 497 */       mode.shortMdctSize << LM != frame_size; LM++);
/*     */ 
/*     */ 
/*     */     
/* 501 */     if (LM > mode.maxLM) {
/* 502 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/*     */     
/* 505 */     int M = 1 << LM;
/*     */     
/* 507 */     if (len < 0 || len > 1275 || pcm == null) {
/* 508 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/*     */     
/* 511 */     int N = M * mode.shortMdctSize;
/* 512 */     int c = 0;
/*     */     do {
/* 514 */       out_syn[c] = this.decode_mem[c];
/* 515 */       out_syn_ptrs[c] = 2048 - N;
/* 516 */     } while (++c < CC);
/*     */     
/* 518 */     int effEnd = end;
/* 519 */     if (effEnd > mode.effEBands) {
/* 520 */       effEnd = mode.effEBands;
/*     */     }
/*     */     
/* 523 */     if (data == null || len <= 1) {
/* 524 */       celt_decode_lost(N, LM);
/* 525 */       CeltCommon.deemphasis(out_syn, out_syn_ptrs, pcm, pcm_ptr, N, CC, this.downsample, mode.preemph, this.preemph_memD, accum);
/*     */       
/* 527 */       return frame_size / this.downsample;
/*     */     } 
/*     */     
/* 530 */     if (dec == null) {
/*     */ 
/*     */       
/* 533 */       dec = new EntropyCoder();
/* 534 */       dec.dec_init(data, data_ptr, len);
/*     */     } 
/*     */     
/* 537 */     if (C == 1) {
/* 538 */       for (int j = 0; j < nbEBands; j++) {
/* 539 */         oldBandE[j] = Inlines.MAX16(oldBandE[j], oldBandE[nbEBands + j]);
/*     */       }
/*     */     }
/*     */     
/* 543 */     int total_bits = len * 8;
/* 544 */     int tell = dec.tell();
/*     */     
/* 546 */     if (tell >= total_bits) {
/* 547 */       silence = 1;
/* 548 */     } else if (tell == 1) {
/* 549 */       silence = dec.dec_bit_logp(15L);
/*     */     } else {
/* 551 */       silence = 0;
/*     */     } 
/*     */     
/* 554 */     if (silence != 0) {
/*     */       
/* 556 */       tell = len * 8;
/* 557 */       dec.nbits_total += tell - dec.tell();
/*     */     } 
/*     */     
/* 560 */     int postfilter_gain = 0;
/* 561 */     int postfilter_pitch = 0;
/* 562 */     int postfilter_tapset = 0;
/* 563 */     if (start == 0 && tell + 16 <= total_bits) {
/* 564 */       if (dec.dec_bit_logp(1L) != 0) {
/*     */         
/* 566 */         int octave = (int)dec.dec_uint(6L);
/* 567 */         postfilter_pitch = (16 << octave) + dec.dec_bits(4 + octave) - 1;
/* 568 */         int qg = dec.dec_bits(3);
/* 569 */         if (dec.tell() + 2 <= total_bits) {
/* 570 */           postfilter_tapset = dec.dec_icdf(CeltTables.tapset_icdf, 2);
/*     */         }
/* 572 */         postfilter_gain = 3072 * (qg + 1);
/*     */       } 
/* 574 */       tell = dec.tell();
/*     */     } 
/*     */     
/* 577 */     if (LM > 0 && tell + 3 <= total_bits) {
/* 578 */       isTransient = dec.dec_bit_logp(3L);
/* 579 */       tell = dec.tell();
/*     */     } else {
/* 581 */       isTransient = 0;
/*     */     } 
/*     */     
/* 584 */     if (isTransient != 0) {
/* 585 */       shortBlocks = M;
/*     */     } else {
/* 587 */       shortBlocks = 0;
/*     */     } 
/*     */ 
/*     */     
/* 591 */     int intra_ener = (tell + 3 <= total_bits) ? dec.dec_bit_logp(3L) : 0;
/*     */     
/* 593 */     QuantizeBands.unquant_coarse_energy(mode, start, end, oldBandE, intra_ener, dec, C, LM);
/*     */ 
/*     */     
/* 596 */     int[] tf_res = new int[nbEBands];
/* 597 */     CeltCommon.tf_decode(start, end, isTransient, tf_res, LM, dec);
/*     */     
/* 599 */     tell = dec.tell();
/* 600 */     int spread_decision = 2;
/* 601 */     if (tell + 4 <= total_bits) {
/* 602 */       spread_decision = dec.dec_icdf(CeltTables.spread_icdf, 5);
/*     */     }
/*     */     
/* 605 */     int[] cap = new int[nbEBands];
/*     */     
/* 607 */     CeltCommon.init_caps(mode, cap, LM, C);
/*     */     
/* 609 */     int[] offsets = new int[nbEBands];
/*     */     
/* 611 */     int dynalloc_logp = 6;
/* 612 */     total_bits <<= 3;
/* 613 */     tell = dec.tell_frac(); int i;
/* 614 */     for (i = start; i < end; i++) {
/*     */ 
/*     */ 
/*     */       
/* 618 */       int width = C * (eBands[i + 1] - eBands[i]) << LM;
/*     */ 
/*     */       
/* 621 */       int quanta = Inlines.IMIN(width << 3, Inlines.IMAX(48, width));
/* 622 */       int dynalloc_loop_logp = dynalloc_logp;
/* 623 */       int boost = 0;
/* 624 */       while (tell + (dynalloc_loop_logp << 3) < total_bits && boost < cap[i]) {
/*     */         
/* 626 */         int flag = dec.dec_bit_logp(dynalloc_loop_logp);
/* 627 */         tell = dec.tell_frac();
/* 628 */         if (flag == 0) {
/*     */           break;
/*     */         }
/* 631 */         boost += quanta;
/* 632 */         total_bits -= quanta;
/* 633 */         dynalloc_loop_logp = 1;
/*     */       } 
/* 635 */       offsets[i] = boost;
/*     */       
/* 637 */       if (boost > 0) {
/* 638 */         dynalloc_logp = Inlines.IMAX(2, dynalloc_logp - 1);
/*     */       }
/*     */     } 
/*     */     
/* 642 */     int[] fine_quant = new int[nbEBands];
/*     */     
/* 644 */     int alloc_trim = (tell + 48 <= total_bits) ? dec.dec_icdf(CeltTables.trim_icdf, 7) : 5;
/*     */     
/* 646 */     int bits = (len * 8 << 3) - dec.tell_frac() - 1;
/* 647 */     int anti_collapse_rsv = (isTransient != 0 && LM >= 2 && bits >= LM + 2 << 3) ? 8 : 0;
/* 648 */     bits -= anti_collapse_rsv;
/*     */     
/* 650 */     int[] pulses = new int[nbEBands];
/* 651 */     int[] fine_priority = new int[nbEBands];
/*     */     
/* 653 */     BoxedValueInt boxed_intensity = new BoxedValueInt(intensity);
/* 654 */     BoxedValueInt boxed_dual_stereo = new BoxedValueInt(dual_stereo);
/* 655 */     BoxedValueInt boxed_balance = new BoxedValueInt(0);
/* 656 */     int codedBands = Rate.compute_allocation(mode, start, end, offsets, cap, alloc_trim, boxed_intensity, boxed_dual_stereo, bits, boxed_balance, pulses, fine_quant, fine_priority, C, LM, dec, 0, 0, 0);
/*     */ 
/*     */     
/* 659 */     intensity = boxed_intensity.Val;
/* 660 */     dual_stereo = boxed_dual_stereo.Val;
/* 661 */     int balance = boxed_balance.Val;
/*     */     
/* 663 */     QuantizeBands.unquant_fine_energy(mode, start, end, oldBandE, fine_quant, dec, C);
/*     */     
/* 665 */     c = 0;
/*     */     do {
/* 667 */       Arrays.MemMove(this.decode_mem[c], N, 0, 2048 - N + overlap / 2);
/* 668 */     } while (++c < CC);
/*     */ 
/*     */     
/* 671 */     short[] collapse_masks = new short[C * nbEBands];
/*     */     
/* 673 */     int[][] X = Arrays.InitTwoDimensionalArrayInt(C, N);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 678 */     BoxedValueInt boxed_rng = new BoxedValueInt(this.rng);
/* 679 */     Bands.quant_all_bands(0, mode, start, end, X[0], (C == 2) ? X[1] : null, collapse_masks, null, pulses, shortBlocks, spread_decision, dual_stereo, intensity, tf_res, len * 64 - anti_collapse_rsv, balance, dec, LM, codedBands, boxed_rng);
/*     */ 
/*     */     
/* 682 */     this.rng = boxed_rng.Val;
/*     */     
/* 684 */     if (anti_collapse_rsv > 0) {
/* 685 */       anti_collapse_on = dec.dec_bits(1);
/*     */     }
/*     */     
/* 688 */     QuantizeBands.unquant_energy_finalise(mode, start, end, oldBandE, fine_quant, fine_priority, len * 8 - dec
/* 689 */         .tell(), dec, C);
/*     */     
/* 691 */     if (anti_collapse_on != 0) {
/* 692 */       Bands.anti_collapse(mode, X, collapse_masks, LM, C, N, start, end, oldBandE, oldLogE, oldLogE2, pulses, this.rng);
/*     */     }
/*     */ 
/*     */     
/* 696 */     if (silence != 0) {
/* 697 */       for (i = 0; i < C * nbEBands; i++) {
/* 698 */         oldBandE[i] = -28672;
/*     */       }
/*     */     }
/*     */     
/* 702 */     CeltCommon.celt_synthesis(mode, X, out_syn, out_syn_ptrs, oldBandE, start, effEnd, C, CC, isTransient, LM, this.downsample, silence);
/*     */ 
/*     */     
/* 705 */     c = 0;
/*     */     while (true) {
/* 707 */       this.postfilter_period = Inlines.IMAX(this.postfilter_period, 15);
/* 708 */       this.postfilter_period_old = Inlines.IMAX(this.postfilter_period_old, 15);
/* 709 */       CeltCommon.comb_filter(out_syn[c], out_syn_ptrs[c], out_syn[c], out_syn_ptrs[c], this.postfilter_period_old, this.postfilter_period, mode.shortMdctSize, this.postfilter_gain_old, this.postfilter_gain, this.postfilter_tapset_old, this.postfilter_tapset, mode.window, overlap);
/*     */ 
/*     */       
/* 712 */       if (LM != 0) {
/* 713 */         CeltCommon.comb_filter(out_syn[c], out_syn_ptrs[c] + mode.shortMdctSize, out_syn[c], out_syn_ptrs[c] + mode.shortMdctSize, this.postfilter_period, postfilter_pitch, N - mode.shortMdctSize, this.postfilter_gain, postfilter_gain, this.postfilter_tapset, postfilter_tapset, mode.window, overlap);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 721 */       if (++c >= CC) {
/* 722 */         this.postfilter_period_old = this.postfilter_period;
/* 723 */         this.postfilter_gain_old = this.postfilter_gain;
/* 724 */         this.postfilter_tapset_old = this.postfilter_tapset;
/* 725 */         this.postfilter_period = postfilter_pitch;
/* 726 */         this.postfilter_gain = postfilter_gain;
/* 727 */         this.postfilter_tapset = postfilter_tapset;
/* 728 */         if (LM != 0) {
/* 729 */           this.postfilter_period_old = this.postfilter_period;
/* 730 */           this.postfilter_gain_old = this.postfilter_gain;
/* 731 */           this.postfilter_tapset_old = this.postfilter_tapset;
/*     */         } 
/*     */         
/* 734 */         if (C == 1) {
/* 735 */           System.arraycopy(oldBandE, 0, oldBandE, nbEBands, nbEBands);
/*     */         }
/*     */ 
/*     */         
/* 739 */         if (isTransient == 0) {
/*     */           int max_background_increase;
/* 741 */           System.arraycopy(oldLogE, 0, oldLogE2, 0, 2 * nbEBands);
/* 742 */           System.arraycopy(oldBandE, 0, oldLogE, 0, 2 * nbEBands);
/*     */ 
/*     */ 
/*     */           
/* 746 */           if (this.loss_count < 10) {
/* 747 */             max_background_increase = M * 1;
/*     */           } else {
/* 749 */             max_background_increase = 1024;
/*     */           } 
/* 751 */           for (i = 0; i < 2 * nbEBands; i++) {
/* 752 */             backgroundLogE[i] = Inlines.MIN16(backgroundLogE[i] + max_background_increase, oldBandE[i]);
/*     */           }
/*     */         } else {
/* 755 */           for (i = 0; i < 2 * nbEBands; i++) {
/* 756 */             oldLogE[i] = Inlines.MIN16(oldLogE[i], oldBandE[i]);
/*     */           }
/*     */         } 
/* 759 */         c = 0;
/*     */         while (true) {
/* 761 */           for (i = 0; i < start; i++) {
/* 762 */             oldBandE[c * nbEBands + i] = 0;
/* 763 */             oldLogE2[c * nbEBands + i] = -28672; oldLogE[c * nbEBands + i] = -28672;
/*     */           } 
/* 765 */           for (i = end; i < nbEBands; i++) {
/* 766 */             oldBandE[c * nbEBands + i] = 0;
/* 767 */             oldLogE2[c * nbEBands + i] = -28672; oldLogE[c * nbEBands + i] = -28672;
/*     */           } 
/* 769 */           if (++c >= 2) {
/* 770 */             this.rng = (int)dec.rng;
/*     */             
/* 772 */             CeltCommon.deemphasis(out_syn, out_syn_ptrs, pcm, pcm_ptr, N, CC, this.downsample, mode.preemph, this.preemph_memD, accum);
/* 773 */             this.loss_count = 0;
/*     */             
/* 775 */             if (dec.tell() > 8 * len) {
/* 776 */               return OpusError.OPUS_INTERNAL_ERROR;
/*     */             }
/* 778 */             if (dec.get_error() != 0) {
/* 779 */               this.error = 1;
/*     */             }
/* 781 */             return frame_size / this.downsample;
/*     */           } 
/*     */         }  break;
/*     */       } 
/* 785 */     }  } void SetStartBand(int value) { if (value < 0 || value >= this.mode.nbEBands) {
/* 786 */       throw new IllegalArgumentException("Start band above max number of ebands (or negative)");
/*     */     }
/* 788 */     this.start = value; }
/*     */ 
/*     */   
/*     */   void SetEndBand(int value) {
/* 792 */     if (value < 1 || value > this.mode.nbEBands) {
/* 793 */       throw new IllegalArgumentException("End band above max number of ebands (or less than 1)");
/*     */     }
/* 795 */     this.end = value;
/*     */   }
/*     */   
/*     */   void SetChannels(int value) {
/* 799 */     if (value < 1 || value > 2) {
/* 800 */       throw new IllegalArgumentException("Channel count must be 1 or 2");
/*     */     }
/* 802 */     this.stream_channels = value;
/*     */   }
/*     */   
/*     */   int GetAndClearError() {
/* 806 */     int returnVal = this.error;
/* 807 */     this.error = 0;
/* 808 */     return returnVal;
/*     */   }
/*     */   
/*     */   public int GetLookahead() {
/* 812 */     return this.overlap / this.downsample;
/*     */   }
/*     */   
/*     */   public int GetPitch() {
/* 816 */     return this.postfilter_period;
/*     */   }
/*     */   
/*     */   public CeltMode GetMode() {
/* 820 */     return this.mode;
/*     */   }
/*     */   
/*     */   public void SetSignalling(int value) {
/* 824 */     this.signalling = value;
/*     */   }
/*     */   
/*     */   public int GetFinalRange() {
/* 828 */     return this.rng;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\CeltDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */