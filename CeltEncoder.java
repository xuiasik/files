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
/*      */ class CeltEncoder
/*      */ {
/*   39 */   CeltMode mode = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   44 */   int channels = 0;
/*   45 */   int stream_channels = 0;
/*      */   
/*   47 */   int force_intra = 0;
/*   48 */   int clip = 0;
/*   49 */   int disable_pf = 0;
/*   50 */   int complexity = 0;
/*   51 */   int upsample = 0;
/*   52 */   int start = 0;
/*   53 */   int end = 0;
/*      */   
/*   55 */   int bitrate = 0;
/*   56 */   int vbr = 0;
/*   57 */   int signalling = 0;
/*      */ 
/*      */   
/*   60 */   int constrained_vbr = 0;
/*   61 */   int loss_rate = 0;
/*   62 */   int lsb_depth = 0;
/*   63 */   OpusFramesize variable_duration = OpusFramesize.OPUS_FRAMESIZE_UNKNOWN;
/*   64 */   int lfe = 0;
/*      */ 
/*      */   
/*   67 */   int rng = 0;
/*   68 */   int spread_decision = 0;
/*   69 */   int delayedIntra = 0;
/*   70 */   int tonal_average = 0;
/*   71 */   int lastCodedBands = 0;
/*   72 */   int hf_average = 0;
/*   73 */   int tapset_decision = 0;
/*      */   
/*   75 */   int prefilter_period = 0;
/*   76 */   int prefilter_gain = 0;
/*   77 */   int prefilter_tapset = 0;
/*   78 */   int consec_transient = 0;
/*   79 */   AnalysisInfo analysis = new AnalysisInfo();
/*      */   
/*   81 */   final int[] preemph_memE = new int[2];
/*   82 */   final int[] preemph_memD = new int[2];
/*      */ 
/*      */   
/*   85 */   int vbr_reservoir = 0;
/*   86 */   int vbr_drift = 0;
/*   87 */   int vbr_offset = 0;
/*   88 */   int vbr_count = 0;
/*   89 */   int overlap_max = 0;
/*   90 */   int stereo_saving = 0;
/*   91 */   int intensity = 0;
/*   92 */   int[] energy_mask = null;
/*   93 */   int spec_avg = 0;
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
/*  106 */   int[][] in_mem = null;
/*  107 */   int[][] prefilter_mem = null;
/*  108 */   int[][] oldBandE = null;
/*  109 */   int[][] oldLogE = null;
/*  110 */   int[][] oldLogE2 = null;
/*      */   
/*      */   private void Reset() {
/*  113 */     this.mode = null;
/*  114 */     this.channels = 0;
/*  115 */     this.stream_channels = 0;
/*  116 */     this.force_intra = 0;
/*  117 */     this.clip = 0;
/*  118 */     this.disable_pf = 0;
/*  119 */     this.complexity = 0;
/*  120 */     this.upsample = 0;
/*  121 */     this.start = 0;
/*  122 */     this.end = 0;
/*  123 */     this.bitrate = 0;
/*  124 */     this.vbr = 0;
/*  125 */     this.signalling = 0;
/*  126 */     this.constrained_vbr = 0;
/*  127 */     this.loss_rate = 0;
/*  128 */     this.lsb_depth = 0;
/*  129 */     this.variable_duration = OpusFramesize.OPUS_FRAMESIZE_UNKNOWN;
/*  130 */     this.lfe = 0;
/*  131 */     PartialReset();
/*      */   }
/*      */   
/*      */   private void PartialReset() {
/*  135 */     this.rng = 0;
/*  136 */     this.spread_decision = 0;
/*  137 */     this.delayedIntra = 0;
/*  138 */     this.tonal_average = 0;
/*  139 */     this.lastCodedBands = 0;
/*  140 */     this.hf_average = 0;
/*  141 */     this.tapset_decision = 0;
/*  142 */     this.prefilter_period = 0;
/*  143 */     this.prefilter_gain = 0;
/*  144 */     this.prefilter_tapset = 0;
/*  145 */     this.consec_transient = 0;
/*  146 */     this.analysis.Reset();
/*  147 */     this.preemph_memE[0] = 0;
/*  148 */     this.preemph_memE[1] = 0;
/*  149 */     this.preemph_memD[0] = 0;
/*  150 */     this.preemph_memD[1] = 0;
/*  151 */     this.vbr_reservoir = 0;
/*  152 */     this.vbr_drift = 0;
/*  153 */     this.vbr_offset = 0;
/*  154 */     this.vbr_count = 0;
/*  155 */     this.overlap_max = 0;
/*  156 */     this.stereo_saving = 0;
/*  157 */     this.intensity = 0;
/*  158 */     this.energy_mask = null;
/*  159 */     this.spec_avg = 0;
/*  160 */     this.in_mem = null;
/*  161 */     this.prefilter_mem = null;
/*  162 */     this.oldBandE = null;
/*  163 */     this.oldLogE = null;
/*  164 */     this.oldLogE2 = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void ResetState() {
/*  170 */     PartialReset();
/*      */ 
/*      */     
/*  173 */     this.in_mem = Arrays.InitTwoDimensionalArrayInt(this.channels, this.mode.overlap);
/*  174 */     this.prefilter_mem = Arrays.InitTwoDimensionalArrayInt(this.channels, 1024);
/*  175 */     this.oldBandE = Arrays.InitTwoDimensionalArrayInt(this.channels, this.mode.nbEBands);
/*  176 */     this.oldLogE = Arrays.InitTwoDimensionalArrayInt(this.channels, this.mode.nbEBands);
/*  177 */     this.oldLogE2 = Arrays.InitTwoDimensionalArrayInt(this.channels, this.mode.nbEBands);
/*      */     int i;
/*  179 */     for (i = 0; i < this.mode.nbEBands; i++) {
/*  180 */       this.oldLogE2[0][i] = -28672; this.oldLogE[0][i] = -28672;
/*      */     } 
/*  182 */     if (this.channels == 2) {
/*  183 */       for (i = 0; i < this.mode.nbEBands; i++) {
/*  184 */         this.oldLogE2[1][i] = -28672; this.oldLogE[1][i] = -28672;
/*      */       } 
/*      */     }
/*  187 */     this.vbr_offset = 0;
/*  188 */     this.delayedIntra = 1;
/*  189 */     this.spread_decision = 2;
/*  190 */     this.tonal_average = 256;
/*  191 */     this.hf_average = 0;
/*  192 */     this.tapset_decision = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   int opus_custom_encoder_init_arch(CeltMode mode, int channels) {
/*  197 */     if (channels < 0 || channels > 2) {
/*  198 */       return OpusError.OPUS_BAD_ARG;
/*      */     }
/*      */     
/*  201 */     if (this == null || mode == null) {
/*  202 */       return OpusError.OPUS_ALLOC_FAIL;
/*      */     }
/*      */     
/*  205 */     Reset();
/*      */     
/*  207 */     this.mode = mode;
/*  208 */     this.stream_channels = this.channels = channels;
/*      */     
/*  210 */     this.upsample = 1;
/*  211 */     this.start = 0;
/*  212 */     this.end = this.mode.effEBands;
/*  213 */     this.signalling = 1;
/*      */     
/*  215 */     this.constrained_vbr = 1;
/*  216 */     this.clip = 1;
/*      */     
/*  218 */     this.bitrate = -1;
/*  219 */     this.vbr = 0;
/*  220 */     this.force_intra = 0;
/*  221 */     this.complexity = 5;
/*  222 */     this.lsb_depth = 24;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  229 */     ResetState();
/*      */     
/*  231 */     return OpusError.OPUS_OK;
/*      */   }
/*      */ 
/*      */   
/*      */   int celt_encoder_init(int sampling_rate, int channels) {
/*  236 */     int ret = opus_custom_encoder_init_arch(CeltMode.mode48000_960_120, channels);
/*  237 */     if (ret != OpusError.OPUS_OK) {
/*  238 */       return ret;
/*      */     }
/*  240 */     this.upsample = CeltCommon.resampling_factor(sampling_rate);
/*  241 */     return OpusError.OPUS_OK;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   int run_prefilter(int[][] input, int[][] prefilter_mem, int CC, int N, int prefilter_tapset, BoxedValueInt pitch, BoxedValueInt gain, BoxedValueInt qgain, int enabled, int nbAvailableBytes) {
/*  247 */     int gain1, pf_on, qg, pre[][] = new int[CC][];
/*      */     
/*  249 */     BoxedValueInt pitch_index = new BoxedValueInt(0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  256 */     CeltMode mode = this.mode;
/*  257 */     int overlap = mode.overlap;
/*  258 */     for (int z = 0; z < CC; z++) {
/*  259 */       pre[z] = new int[N + 1024];
/*      */     }
/*      */     
/*  262 */     int c = 0;
/*      */     do {
/*  264 */       System.arraycopy(prefilter_mem[c], 0, pre[c], 0, 1024);
/*  265 */       System.arraycopy(input[c], overlap, pre[c], 1024, N);
/*  266 */     } while (++c < CC);
/*      */     
/*  268 */     if (enabled != 0) {
/*  269 */       int[] pitch_buf = new int[1024 + N >> 1];
/*      */       
/*  271 */       Pitch.pitch_downsample(pre, pitch_buf, 1024 + N, CC);
/*      */ 
/*      */       
/*  274 */       Pitch.pitch_search(pitch_buf, 512, pitch_buf, N, 979, pitch_index);
/*      */       
/*  276 */       pitch_index.Val = 1024 - pitch_index.Val;
/*  277 */       gain1 = Pitch.remove_doubling(pitch_buf, 1024, 15, N, pitch_index, this.prefilter_period, this.prefilter_gain);
/*      */       
/*  279 */       if (pitch_index.Val > 1022) {
/*  280 */         pitch_index.Val = 1022;
/*      */       }
/*  282 */       gain1 = Inlines.MULT16_16_Q15(22938, gain1);
/*      */       
/*  284 */       if (this.loss_rate > 2) {
/*  285 */         gain1 = Inlines.HALF32(gain1);
/*      */       }
/*  287 */       if (this.loss_rate > 4) {
/*  288 */         gain1 = Inlines.HALF32(gain1);
/*      */       }
/*  290 */       if (this.loss_rate > 8) {
/*  291 */         gain1 = 0;
/*      */       }
/*      */     } else {
/*  294 */       gain1 = 0;
/*  295 */       pitch_index.Val = 15;
/*      */     } 
/*      */ 
/*      */     
/*  299 */     int pf_threshold = 6554;
/*      */ 
/*      */     
/*  302 */     if (Inlines.abs(pitch_index.Val - this.prefilter_period) * 10 > pitch_index.Val) {
/*  303 */       pf_threshold += 6554;
/*      */     }
/*  305 */     if (nbAvailableBytes < 25) {
/*  306 */       pf_threshold += 3277;
/*      */     }
/*  308 */     if (nbAvailableBytes < 35) {
/*  309 */       pf_threshold += 3277;
/*      */     }
/*  311 */     if (this.prefilter_gain > 13107) {
/*  312 */       pf_threshold -= 3277;
/*      */     }
/*  314 */     if (this.prefilter_gain > 18022) {
/*  315 */       pf_threshold -= 3277;
/*      */     }
/*      */ 
/*      */     
/*  319 */     pf_threshold = Inlines.MAX16(pf_threshold, 6554);
/*      */     
/*  321 */     if (gain1 < pf_threshold) {
/*  322 */       gain1 = 0;
/*  323 */       pf_on = 0;
/*  324 */       qg = 0;
/*      */     }
/*      */     else {
/*      */       
/*  328 */       if (Inlines.ABS32(gain1 - this.prefilter_gain) < 3277) {
/*  329 */         gain1 = this.prefilter_gain;
/*      */       }
/*      */       
/*  332 */       qg = (gain1 + 1536 >> 10) / 3 - 1;
/*  333 */       qg = Inlines.IMAX(0, Inlines.IMIN(7, qg));
/*  334 */       gain1 = 3072 * (qg + 1);
/*  335 */       pf_on = 1;
/*      */     } 
/*      */ 
/*      */     
/*  339 */     c = 0;
/*      */     do {
/*  341 */       int offset = mode.shortMdctSize - overlap;
/*  342 */       this.prefilter_period = Inlines.IMAX(this.prefilter_period, 15);
/*  343 */       System.arraycopy(this.in_mem[c], 0, input[c], 0, overlap);
/*  344 */       if (offset != 0) {
/*  345 */         CeltCommon.comb_filter(input[c], overlap, pre[c], 1024, this.prefilter_period, this.prefilter_period, offset, -this.prefilter_gain, -this.prefilter_gain, this.prefilter_tapset, this.prefilter_tapset, null, 0);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  350 */       CeltCommon.comb_filter(input[c], overlap + offset, pre[c], 1024 + offset, this.prefilter_period, pitch_index.Val, N - offset, -this.prefilter_gain, -gain1, this.prefilter_tapset, prefilter_tapset, mode.window, overlap);
/*      */ 
/*      */       
/*  353 */       System.arraycopy(input[c], N, this.in_mem[c], 0, overlap);
/*      */       
/*  355 */       if (N > 1024) {
/*  356 */         System.arraycopy(pre[c], N, prefilter_mem[c], 0, 1024);
/*      */       } else {
/*  358 */         Arrays.MemMove(prefilter_mem[c], N, 0, 1024 - N);
/*  359 */         System.arraycopy(pre[c], 1024, prefilter_mem[c], 1024 - N, N);
/*      */       } 
/*  361 */     } while (++c < CC);
/*      */     
/*  363 */     gain.Val = gain1;
/*  364 */     pitch.Val = pitch_index.Val;
/*  365 */     qgain.Val = qg;
/*  366 */     return pf_on;
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
/*      */ 
/*      */   
/*      */   int celt_encode_with_ec(short[] pcm, int pcm_ptr, int frame_size, byte[] compressed, int compressed_ptr, int nbCompressedBytes, EntropyCoder enc) {
/*  386 */     int tf_select, nbFilledBytes, j, vbr_rate, shortBlocks = 0;
/*  387 */     int isTransient = 0;
/*  388 */     int CC = this.channels;
/*  389 */     int C = this.stream_channels;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  399 */     int pitch_index = 15;
/*  400 */     int gain1 = 0;
/*  401 */     int dual_stereo = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  409 */     int prefilter_tapset = 0;
/*      */ 
/*      */     
/*  412 */     int anti_collapse_on = 0;
/*  413 */     int silence = 0;
/*  414 */     int tf_chan = 0;
/*      */     
/*  416 */     int pitch_change = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  426 */     int transient_got_disabled = 0;
/*  427 */     int surround_masking = 0;
/*  428 */     int temporal_vbr = 0;
/*  429 */     int surround_trim = 0;
/*  430 */     int equiv_rate = 510000;
/*      */ 
/*      */     
/*  433 */     CeltMode mode = this.mode;
/*  434 */     int nbEBands = mode.nbEBands;
/*  435 */     int overlap = mode.overlap;
/*  436 */     short[] eBands = mode.eBands;
/*  437 */     int start = this.start;
/*  438 */     int end = this.end;
/*  439 */     int tf_estimate = 0;
/*  440 */     if (nbCompressedBytes < 2 || pcm == null) {
/*  441 */       return OpusError.OPUS_BAD_ARG;
/*      */     }
/*      */     
/*  444 */     frame_size *= this.upsample; int LM;
/*  445 */     for (LM = 0; LM <= mode.maxLM && 
/*  446 */       mode.shortMdctSize << LM != frame_size; LM++);
/*      */ 
/*      */ 
/*      */     
/*  450 */     if (LM > mode.maxLM) {
/*  451 */       return OpusError.OPUS_BAD_ARG;
/*      */     }
/*  453 */     int M = 1 << LM;
/*  454 */     int N = M * mode.shortMdctSize;
/*      */     
/*  456 */     if (enc == null) {
/*  457 */       tell = 1;
/*  458 */       nbFilledBytes = 0;
/*      */     } else {
/*  460 */       tell = enc.tell();
/*  461 */       nbFilledBytes = tell + 4 >> 3;
/*      */     } 
/*      */     
/*  464 */     Inlines.OpusAssert((this.signalling == 0));
/*      */ 
/*      */     
/*  467 */     nbCompressedBytes = Inlines.IMIN(nbCompressedBytes, 1275);
/*  468 */     int nbAvailableBytes = nbCompressedBytes - nbFilledBytes;
/*      */     
/*  470 */     if (this.vbr != 0 && this.bitrate != -1) {
/*  471 */       int den = mode.Fs >> 3;
/*  472 */       vbr_rate = (this.bitrate * frame_size + (den >> 1)) / den;
/*  473 */       j = vbr_rate >> 6;
/*      */     } else {
/*      */       
/*  476 */       vbr_rate = 0;
/*  477 */       int tmp = this.bitrate * frame_size;
/*  478 */       if (tell > 1) {
/*  479 */         tmp += tell;
/*      */       }
/*  481 */       if (this.bitrate != -1) {
/*  482 */         nbCompressedBytes = Inlines.IMAX(2, Inlines.IMIN(nbCompressedBytes, (tmp + 4 * mode.Fs) / 8 * mode.Fs - (
/*  483 */               (this.signalling != 0) ? 1 : 0)));
/*      */       }
/*  485 */       j = nbCompressedBytes;
/*      */     } 
/*  487 */     if (this.bitrate != -1) {
/*  488 */       equiv_rate = this.bitrate - (40 * C + 20) * ((400 >> LM) - 50);
/*      */     }
/*      */     
/*  491 */     if (enc == null) {
/*  492 */       enc = new EntropyCoder();
/*  493 */       enc.enc_init(compressed, compressed_ptr, nbCompressedBytes);
/*      */     } 
/*      */     
/*  496 */     if (vbr_rate > 0)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  501 */       if (this.constrained_vbr != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  508 */         int vbr_bound = vbr_rate;
/*  509 */         int max_allowed = Inlines.IMIN(Inlines.IMAX((tell == 1) ? 2 : 0, vbr_rate + vbr_bound - this.vbr_reservoir >> 6), nbAvailableBytes);
/*      */ 
/*      */         
/*  512 */         if (max_allowed < nbAvailableBytes) {
/*  513 */           nbCompressedBytes = nbFilledBytes + max_allowed;
/*  514 */           nbAvailableBytes = max_allowed;
/*  515 */           enc.enc_shrink(nbCompressedBytes);
/*      */         } 
/*      */       } 
/*      */     }
/*  519 */     int total_bits = nbCompressedBytes * 8;
/*      */     
/*  521 */     int effEnd = end;
/*  522 */     if (effEnd > mode.effEBands) {
/*  523 */       effEnd = mode.effEBands;
/*      */     }
/*      */     
/*  526 */     int[][] input = Arrays.InitTwoDimensionalArrayInt(CC, N + overlap);
/*      */     
/*  528 */     int sample_max = Inlines.MAX32(this.overlap_max, Inlines.celt_maxabs32(pcm, pcm_ptr, C * (N - overlap) / this.upsample));
/*  529 */     this.overlap_max = Inlines.celt_maxabs32(pcm, pcm_ptr + C * (N - overlap) / this.upsample, C * overlap / this.upsample);
/*  530 */     sample_max = Inlines.MAX32(sample_max, this.overlap_max);
/*  531 */     silence = (sample_max == 0) ? 1 : 0;
/*  532 */     if (tell == 1) {
/*  533 */       enc.enc_bit_logp(silence, 15);
/*      */     } else {
/*  535 */       silence = 0;
/*      */     } 
/*  537 */     if (silence != 0) {
/*      */       
/*  539 */       if (vbr_rate > 0) {
/*  540 */         j = nbCompressedBytes = Inlines.IMIN(nbCompressedBytes, nbFilledBytes + 2);
/*  541 */         total_bits = nbCompressedBytes * 8;
/*  542 */         nbAvailableBytes = 2;
/*  543 */         enc.enc_shrink(nbCompressedBytes);
/*      */       } 
/*      */ 
/*      */       
/*  547 */       tell = nbCompressedBytes * 8;
/*  548 */       enc.nbits_total += tell - enc.tell();
/*      */     } 
/*  550 */     int c = 0;
/*  551 */     BoxedValueInt boxed_memE = new BoxedValueInt(0);
/*      */     do {
/*  553 */       int need_clip = 0;
/*  554 */       boxed_memE.Val = this.preemph_memE[c];
/*  555 */       CeltCommon.celt_preemphasis(pcm, pcm_ptr + c, input[c], overlap, N, CC, this.upsample, mode.preemph, boxed_memE, need_clip);
/*      */       
/*  557 */       this.preemph_memE[c] = boxed_memE.Val;
/*  558 */     } while (++c < CC);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  565 */     int enabled = (((this.lfe != 0 && nbAvailableBytes > 3) || nbAvailableBytes > 12 * C) && start == 0 && silence == 0 && this.disable_pf == 0 && this.complexity >= 5 && (this.consec_transient == 0 || LM == 3 || this.variable_duration != OpusFramesize.OPUS_FRAMESIZE_VARIABLE)) ? 1 : 0;
/*      */     
/*  567 */     prefilter_tapset = this.tapset_decision;
/*  568 */     BoxedValueInt boxed_pitch_index = new BoxedValueInt(0);
/*  569 */     BoxedValueInt boxed_gain1 = new BoxedValueInt(0);
/*  570 */     BoxedValueInt boxed_qg = new BoxedValueInt(0);
/*  571 */     int pf_on = run_prefilter(input, this.prefilter_mem, CC, N, prefilter_tapset, boxed_pitch_index, boxed_gain1, boxed_qg, enabled, nbAvailableBytes);
/*  572 */     pitch_index = boxed_pitch_index.Val;
/*  573 */     gain1 = boxed_gain1.Val;
/*  574 */     int qg = boxed_qg.Val;
/*      */     
/*  576 */     if ((gain1 > 13107 || this.prefilter_gain > 13107) && (this.analysis.valid == 0 || this.analysis.tonality > 0.3D) && (pitch_index > 1.26D * this.prefilter_period || pitch_index < 0.79D * this.prefilter_period))
/*      */     {
/*  578 */       pitch_change = 1;
/*      */     }
/*  580 */     if (pf_on == 0) {
/*  581 */       if (start == 0 && tell + 16 <= total_bits) {
/*  582 */         enc.enc_bit_logp(0, 1);
/*      */       
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  588 */       enc.enc_bit_logp(1, 1);
/*  589 */       pitch_index++;
/*  590 */       int octave = Inlines.EC_ILOG(pitch_index) - 5;
/*  591 */       enc.enc_uint(octave, 6L);
/*  592 */       enc.enc_bits((pitch_index - (16 << octave)), 4 + octave);
/*  593 */       pitch_index--;
/*  594 */       enc.enc_bits(qg, 3);
/*  595 */       enc.enc_icdf(prefilter_tapset, CeltTables.tapset_icdf, 2);
/*      */     } 
/*      */ 
/*      */     
/*  599 */     isTransient = 0;
/*  600 */     shortBlocks = 0;
/*  601 */     if (this.complexity >= 1 && this.lfe == 0) {
/*  602 */       BoxedValueInt boxed_tf_estimate = new BoxedValueInt(0);
/*  603 */       BoxedValueInt boxed_tf_chan = new BoxedValueInt(0);
/*  604 */       isTransient = CeltCommon.transient_analysis(input, N + overlap, CC, boxed_tf_estimate, boxed_tf_chan);
/*      */       
/*  606 */       tf_estimate = boxed_tf_estimate.Val;
/*  607 */       tf_chan = boxed_tf_chan.Val;
/*      */     } 
/*      */     
/*  610 */     if (LM > 0 && enc.tell() + 3 <= total_bits) {
/*  611 */       if (isTransient != 0) {
/*  612 */         shortBlocks = M;
/*      */       }
/*      */     } else {
/*  615 */       isTransient = 0;
/*  616 */       transient_got_disabled = 1;
/*      */     } 
/*      */     
/*  619 */     int[][] freq = Arrays.InitTwoDimensionalArrayInt(CC, N);
/*      */ 
/*      */ 
/*      */     
/*  623 */     int[][] bandE = Arrays.InitTwoDimensionalArrayInt(CC, nbEBands);
/*  624 */     int[][] bandLogE = Arrays.InitTwoDimensionalArrayInt(CC, nbEBands);
/*      */     
/*  626 */     int secondMdct = (shortBlocks != 0 && this.complexity >= 8) ? 1 : 0;
/*  627 */     int[][] bandLogE2 = Arrays.InitTwoDimensionalArrayInt(CC, nbEBands);
/*      */     
/*  629 */     if (secondMdct != 0) {
/*  630 */       CeltCommon.compute_mdcts(mode, 0, input, freq, C, CC, LM, this.upsample);
/*  631 */       Bands.compute_band_energies(mode, freq, bandE, effEnd, C, LM);
/*  632 */       QuantizeBands.amp2Log2(mode, effEnd, end, bandE, bandLogE2, C); int k;
/*  633 */       for (k = 0; k < nbEBands; k++) {
/*  634 */         bandLogE2[0][k] = bandLogE2[0][k] + Inlines.HALF16(Inlines.SHL16(LM, 10));
/*      */       }
/*  636 */       if (C == 2) {
/*  637 */         for (k = 0; k < nbEBands; k++) {
/*  638 */           bandLogE2[1][k] = bandLogE2[1][k] + Inlines.HALF16(Inlines.SHL16(LM, 10));
/*      */         }
/*      */       }
/*      */     } 
/*      */     
/*  643 */     CeltCommon.compute_mdcts(mode, shortBlocks, input, freq, C, CC, LM, this.upsample);
/*  644 */     if (CC == 2 && C == 1) {
/*  645 */       tf_chan = 0;
/*      */     }
/*  647 */     Bands.compute_band_energies(mode, freq, bandE, effEnd, C, LM);
/*      */     
/*  649 */     if (this.lfe != 0) {
/*  650 */       for (int k = 2; k < end; k++) {
/*  651 */         bandE[0][k] = Inlines.IMIN(bandE[0][k], Inlines.MULT16_32_Q15((short)3, bandE[0][0]));
/*  652 */         bandE[0][k] = Inlines.MAX32(bandE[0][k], 1);
/*      */       } 
/*      */     }
/*      */     
/*  656 */     QuantizeBands.amp2Log2(mode, effEnd, end, bandE, bandLogE, C);
/*      */     
/*  658 */     int[] surround_dynalloc = new int[C * nbEBands];
/*      */ 
/*      */     
/*  661 */     if (start == 0 && this.energy_mask != null && this.lfe == 0) {
/*      */ 
/*      */ 
/*      */       
/*  665 */       int mask_avg = 0;
/*  666 */       int diff = 0;
/*  667 */       int count = 0;
/*  668 */       int mask_end = Inlines.IMAX(2, this.lastCodedBands);
/*  669 */       for (c = 0; c < C; c++) {
/*  670 */         for (int m = 0; m < mask_end; m++) {
/*      */           
/*  672 */           int mask = Inlines.MAX16(Inlines.MIN16(this.energy_mask[nbEBands * c + m], 256), -2048);
/*      */           
/*  674 */           if (mask > 0) {
/*  675 */             mask = Inlines.HALF16(mask);
/*      */           }
/*  677 */           mask_avg += Inlines.MULT16_16(mask, eBands[m + 1] - eBands[m]);
/*  678 */           count += eBands[m + 1] - eBands[m];
/*  679 */           diff += Inlines.MULT16_16(mask, 1 + 2 * m - mask_end);
/*      */         } 
/*      */       } 
/*  682 */       Inlines.OpusAssert((count > 0));
/*  683 */       mask_avg = Inlines.DIV32_16(mask_avg, count);
/*  684 */       mask_avg += 205;
/*  685 */       diff = diff * 6 / C * (mask_end - 1) * (mask_end + 1) * mask_end;
/*      */       
/*  687 */       diff = Inlines.HALF32(diff);
/*  688 */       diff = Inlines.MAX32(Inlines.MIN32(diff, 32), -32);
/*      */       int midband;
/*  690 */       for (midband = 0; eBands[midband + 1] < eBands[mask_end] / 2; midband++);
/*  691 */       int count_dynalloc = 0; int k;
/*  692 */       for (k = 0; k < mask_end; k++) {
/*      */ 
/*      */         
/*  695 */         int lin = mask_avg + diff * (k - midband);
/*  696 */         if (C == 2) {
/*  697 */           unmask = Inlines.MAX16(this.energy_mask[k], this.energy_mask[nbEBands + k]);
/*      */         } else {
/*  699 */           unmask = this.energy_mask[k];
/*      */         } 
/*  701 */         int unmask = Inlines.MIN16(unmask, 0);
/*  702 */         unmask -= lin;
/*  703 */         if (unmask > 256) {
/*  704 */           surround_dynalloc[k] = unmask - 256;
/*  705 */           count_dynalloc++;
/*      */         } 
/*      */       } 
/*  708 */       if (count_dynalloc >= 3) {
/*      */ 
/*      */         
/*  711 */         mask_avg += 256;
/*  712 */         if (mask_avg > 0) {
/*      */ 
/*      */           
/*  715 */           mask_avg = 0;
/*  716 */           diff = 0;
/*  717 */           Arrays.MemSet(surround_dynalloc, 0, mask_end);
/*      */         } else {
/*  719 */           for (k = 0; k < mask_end; k++) {
/*  720 */             surround_dynalloc[k] = Inlines.MAX16(0, surround_dynalloc[k] - 256);
/*      */           }
/*      */         } 
/*      */       } 
/*  724 */       mask_avg += 205;
/*      */       
/*  726 */       surround_trim = 64 * diff;
/*      */       
/*  728 */       surround_masking = mask_avg;
/*      */     } 
/*      */     
/*  731 */     if (this.lfe == 0) {
/*  732 */       int follow = -10240;
/*  733 */       int frame_avg = 0;
/*  734 */       int offset = (shortBlocks != 0) ? Inlines.HALF16(Inlines.SHL16(LM, 10)) : 0;
/*  735 */       for (int k = start; k < end; k++) {
/*  736 */         follow = Inlines.MAX16(follow - 1024, bandLogE[0][k] - offset);
/*  737 */         if (C == 2) {
/*  738 */           follow = Inlines.MAX16(follow, bandLogE[1][k] - offset);
/*      */         }
/*  740 */         frame_avg += follow;
/*      */       } 
/*  742 */       frame_avg /= end - start;
/*  743 */       temporal_vbr = Inlines.SUB16(frame_avg, this.spec_avg);
/*  744 */       temporal_vbr = Inlines.MIN16(3072, Inlines.MAX16(-1536, temporal_vbr));
/*  745 */       this.spec_avg += (short)Inlines.MULT16_16_Q15(655, temporal_vbr);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  751 */     if (secondMdct == 0) {
/*  752 */       System.arraycopy(bandLogE[0], 0, bandLogE2[0], 0, nbEBands);
/*  753 */       if (C == 2) {
/*  754 */         System.arraycopy(bandLogE[1], 0, bandLogE2[1], 0, nbEBands);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  760 */     if (LM > 0 && enc.tell() + 3 <= total_bits && isTransient == 0 && this.complexity >= 5 && this.lfe == 0 && 
/*  761 */       CeltCommon.patch_transient_decision(bandLogE, this.oldBandE, nbEBands, start, end, C) != 0) {
/*  762 */       isTransient = 1;
/*  763 */       shortBlocks = M;
/*  764 */       CeltCommon.compute_mdcts(mode, shortBlocks, input, freq, C, CC, LM, this.upsample);
/*  765 */       Bands.compute_band_energies(mode, freq, bandE, effEnd, C, LM);
/*  766 */       QuantizeBands.amp2Log2(mode, effEnd, end, bandE, bandLogE, C);
/*      */       int k;
/*  768 */       for (k = 0; k < nbEBands; k++) {
/*  769 */         bandLogE2[0][k] = bandLogE2[0][k] + Inlines.HALF16(Inlines.SHL16(LM, 10));
/*      */       }
/*  771 */       if (C == 2) {
/*  772 */         for (k = 0; k < nbEBands; k++) {
/*  773 */           bandLogE2[1][k] = bandLogE2[1][k] + Inlines.HALF16(Inlines.SHL16(LM, 10));
/*      */         }
/*      */       }
/*  776 */       tf_estimate = 3277;
/*      */     } 
/*      */ 
/*      */     
/*  780 */     if (LM > 0 && enc.tell() + 3 <= total_bits) {
/*  781 */       enc.enc_bit_logp(isTransient, 3);
/*      */     }
/*      */     
/*  784 */     int[][] X = Arrays.InitTwoDimensionalArrayInt(C, N);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  790 */     Bands.normalise_bands(mode, freq, X, bandE, effEnd, C, M);
/*      */     
/*  792 */     int[] tf_res = new int[nbEBands];
/*      */     
/*  794 */     if (j >= 15 * C && start == 0 && this.complexity >= 2 && this.lfe == 0) {
/*      */       int lambda;
/*  796 */       if (j < 40) {
/*  797 */         lambda = 12;
/*  798 */       } else if (j < 60) {
/*  799 */         lambda = 6;
/*  800 */       } else if (j < 100) {
/*  801 */         lambda = 4;
/*      */       } else {
/*  803 */         lambda = 3;
/*      */       } 
/*  805 */       lambda *= 2;
/*  806 */       BoxedValueInt boxed_tf_sum = new BoxedValueInt(0);
/*  807 */       tf_select = CeltCommon.tf_analysis(mode, effEnd, isTransient, tf_res, lambda, X, N, LM, boxed_tf_sum, tf_estimate, tf_chan);
/*  808 */       int tf_sum = boxed_tf_sum.Val;
/*      */       
/*  810 */       for (int k = effEnd; k < end; k++) {
/*  811 */         tf_res[k] = tf_res[effEnd - 1];
/*      */       }
/*      */     } else {
/*  814 */       int tf_sum = 0;
/*  815 */       for (int k = 0; k < end; k++) {
/*  816 */         tf_res[k] = isTransient;
/*      */       }
/*  818 */       tf_select = 0;
/*      */     } 
/*      */     
/*  821 */     int[][] error = Arrays.InitTwoDimensionalArrayInt(C, nbEBands);
/*  822 */     BoxedValueInt boxed_delayedintra = new BoxedValueInt(this.delayedIntra);
/*  823 */     QuantizeBands.quant_coarse_energy(mode, start, end, effEnd, bandLogE, this.oldBandE, total_bits, error, enc, C, LM, nbAvailableBytes, this.force_intra, boxed_delayedintra, 
/*      */ 
/*      */         
/*  826 */         (this.complexity >= 4) ? 1 : 0, this.loss_rate, this.lfe);
/*  827 */     this.delayedIntra = boxed_delayedintra.Val;
/*      */     
/*  829 */     CeltCommon.tf_encode(start, end, isTransient, tf_res, LM, tf_select, enc);
/*      */     
/*  831 */     if (enc.tell() + 4 <= total_bits) {
/*  832 */       if (this.lfe != 0) {
/*  833 */         this.tapset_decision = 0;
/*  834 */         this.spread_decision = 2;
/*  835 */       } else if (shortBlocks != 0 || this.complexity < 3 || nbAvailableBytes < 10 * C || start != 0) {
/*  836 */         if (this.complexity == 0) {
/*  837 */           this.spread_decision = 0;
/*      */         } else {
/*  839 */           this.spread_decision = 2;
/*      */         } 
/*      */       } else {
/*  842 */         BoxedValueInt boxed_tonal_average = new BoxedValueInt(this.tonal_average);
/*  843 */         BoxedValueInt boxed_tapset_decision = new BoxedValueInt(this.tapset_decision);
/*  844 */         BoxedValueInt boxed_hf_average = new BoxedValueInt(this.hf_average);
/*  845 */         this.spread_decision = Bands.spreading_decision(mode, X, boxed_tonal_average, this.spread_decision, boxed_hf_average, boxed_tapset_decision, (
/*      */             
/*  847 */             pf_on != 0 && shortBlocks == 0) ? 1 : 0, effEnd, C, M);
/*  848 */         this.tonal_average = boxed_tonal_average.Val;
/*  849 */         this.tapset_decision = boxed_tapset_decision.Val;
/*  850 */         this.hf_average = boxed_hf_average.Val;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  855 */       enc.enc_icdf(this.spread_decision, CeltTables.spread_icdf, 5);
/*      */     } 
/*      */     
/*  858 */     int[] offsets = new int[nbEBands];
/*      */     
/*  860 */     BoxedValueInt boxed_tot_boost = new BoxedValueInt(0);
/*  861 */     int maxDepth = CeltCommon.dynalloc_analysis(bandLogE, bandLogE2, nbEBands, start, end, C, offsets, this.lsb_depth, mode.logN, isTransient, this.vbr, this.constrained_vbr, eBands, LM, j, boxed_tot_boost, this.lfe, surround_dynalloc);
/*      */ 
/*      */     
/*  864 */     int tot_boost = boxed_tot_boost.Val;
/*      */ 
/*      */     
/*  867 */     if (this.lfe != 0) {
/*  868 */       offsets[0] = Inlines.IMIN(8, j / 3);
/*      */     }
/*  870 */     int[] cap = new int[nbEBands];
/*  871 */     CeltCommon.init_caps(mode, cap, LM, C);
/*      */     
/*  873 */     int dynalloc_logp = 6;
/*  874 */     total_bits <<= 3;
/*  875 */     int total_boost = 0;
/*  876 */     int tell = enc.tell_frac(); int i;
/*  877 */     for (i = start; i < end; i++) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  882 */       int width = C * (eBands[i + 1] - eBands[i]) << LM;
/*      */ 
/*      */       
/*  885 */       int quanta = Inlines.IMIN(width << 3, Inlines.IMAX(48, width));
/*  886 */       int dynalloc_loop_logp = dynalloc_logp;
/*  887 */       int boost = 0;
/*  888 */       int k = 0;
/*  889 */       for (; tell + (dynalloc_loop_logp << 3) < total_bits - total_boost && boost < cap[i]; k++) {
/*      */         
/*  891 */         int flag = (k < offsets[i]) ? 1 : 0;
/*  892 */         enc.enc_bit_logp(flag, dynalloc_loop_logp);
/*  893 */         tell = enc.tell_frac();
/*  894 */         if (flag == 0) {
/*      */           break;
/*      */         }
/*  897 */         boost += quanta;
/*  898 */         total_boost += quanta;
/*  899 */         dynalloc_loop_logp = 1;
/*      */       } 
/*      */       
/*  902 */       if (k != 0) {
/*  903 */         dynalloc_logp = Inlines.IMAX(2, dynalloc_logp - 1);
/*      */       }
/*  905 */       offsets[i] = boost;
/*      */     } 
/*      */     
/*  908 */     if (C == 2) {
/*      */       
/*  910 */       if (LM != 0) {
/*  911 */         dual_stereo = CeltCommon.stereo_analysis(mode, X, LM);
/*      */       }
/*      */       
/*  914 */       this.intensity = Bands.hysteresis_decision(equiv_rate / 1000, CeltTables.intensity_thresholds, CeltTables.intensity_histeresis, 21, this.intensity);
/*      */       
/*  916 */       this.intensity = Inlines.IMIN(end, Inlines.IMAX(start, this.intensity));
/*      */     } 
/*      */     
/*  919 */     int alloc_trim = 5;
/*  920 */     if (tell + 48 <= total_bits - total_boost) {
/*  921 */       if (this.lfe != 0) {
/*  922 */         alloc_trim = 5;
/*      */       } else {
/*  924 */         BoxedValueInt boxed_stereo_saving = new BoxedValueInt(this.stereo_saving);
/*  925 */         alloc_trim = CeltCommon.alloc_trim_analysis(mode, X, bandLogE, end, LM, C, this.analysis, boxed_stereo_saving, tf_estimate, this.intensity, surround_trim);
/*      */ 
/*      */         
/*  928 */         this.stereo_saving = boxed_stereo_saving.Val;
/*      */       } 
/*  930 */       enc.enc_icdf(alloc_trim, CeltTables.trim_icdf, 7);
/*  931 */       tell = enc.tell_frac();
/*      */     } 
/*      */ 
/*      */     
/*  935 */     if (vbr_rate > 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  941 */       int alpha, lm_diff = mode.maxLM - LM;
/*      */ 
/*      */ 
/*      */       
/*  945 */       nbCompressedBytes = Inlines.IMIN(nbCompressedBytes, 1275 >> 3 - LM);
/*  946 */       int base_target = vbr_rate - (40 * C + 20 << 3);
/*      */       
/*  948 */       if (this.constrained_vbr != 0) {
/*  949 */         base_target += this.vbr_offset >> lm_diff;
/*      */       }
/*      */       
/*  952 */       int target = CeltCommon.compute_vbr(mode, this.analysis, base_target, LM, equiv_rate, this.lastCodedBands, C, this.intensity, this.constrained_vbr, this.stereo_saving, tot_boost, tf_estimate, pitch_change, maxDepth, this.variable_duration, this.lfe, 
/*      */ 
/*      */           
/*  955 */           (this.energy_mask != null) ? 1 : 0, surround_masking, temporal_vbr);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  960 */       target += tell;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  965 */       int min_allowed = (tell + total_boost + 64 - 1 >> 6) + 2 - nbFilledBytes;
/*      */       
/*  967 */       nbAvailableBytes = target + 32 >> 6;
/*  968 */       nbAvailableBytes = Inlines.IMAX(min_allowed, nbAvailableBytes);
/*  969 */       nbAvailableBytes = Inlines.IMIN(nbCompressedBytes, nbAvailableBytes + nbFilledBytes) - nbFilledBytes;
/*      */ 
/*      */       
/*  972 */       int delta = target - vbr_rate;
/*      */       
/*  974 */       target = nbAvailableBytes << 6;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  981 */       if (silence != 0) {
/*  982 */         nbAvailableBytes = 2;
/*  983 */         target = 128;
/*  984 */         delta = 0;
/*      */       } 
/*      */       
/*  987 */       if (this.vbr_count < 970) {
/*  988 */         this.vbr_count++;
/*  989 */         alpha = Inlines.celt_rcp(Inlines.SHL32(this.vbr_count + 20, 16));
/*      */       } else {
/*  991 */         alpha = 33;
/*      */       } 
/*      */       
/*  994 */       if (this.constrained_vbr != 0) {
/*  995 */         this.vbr_reservoir += target - vbr_rate;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1000 */       if (this.constrained_vbr != 0) {
/* 1001 */         this.vbr_drift += Inlines.MULT16_32_Q15(alpha, delta * (1 << lm_diff) - this.vbr_offset - this.vbr_drift);
/* 1002 */         this.vbr_offset = -this.vbr_drift;
/*      */       } 
/*      */ 
/*      */       
/* 1006 */       if (this.constrained_vbr != 0 && this.vbr_reservoir < 0) {
/*      */         
/* 1008 */         int adjust = -this.vbr_reservoir / 64;
/*      */         
/* 1010 */         nbAvailableBytes += (silence != 0) ? 0 : adjust;
/* 1011 */         this.vbr_reservoir = 0;
/*      */       } 
/*      */       
/* 1014 */       nbCompressedBytes = Inlines.IMIN(nbCompressedBytes, nbAvailableBytes + nbFilledBytes);
/*      */ 
/*      */       
/* 1017 */       enc.enc_shrink(nbCompressedBytes);
/*      */     } 
/*      */ 
/*      */     
/* 1021 */     int[] fine_quant = new int[nbEBands];
/* 1022 */     int[] pulses = new int[nbEBands];
/* 1023 */     int[] fine_priority = new int[nbEBands];
/*      */ 
/*      */     
/* 1026 */     int bits = (nbCompressedBytes * 8 << 3) - enc.tell_frac() - 1;
/* 1027 */     int anti_collapse_rsv = (isTransient != 0 && LM >= 2 && bits >= LM + 2 << 3) ? 8 : 0;
/* 1028 */     bits -= anti_collapse_rsv;
/* 1029 */     int signalBandwidth = end - 1;
/*      */     
/* 1031 */     if (this.analysis.enabled && this.analysis.valid != 0) {
/*      */       int min_bandwidth;
/* 1033 */       if (equiv_rate < 32000 * C) {
/* 1034 */         min_bandwidth = 13;
/* 1035 */       } else if (equiv_rate < 48000 * C) {
/* 1036 */         min_bandwidth = 16;
/* 1037 */       } else if (equiv_rate < 60000 * C) {
/* 1038 */         min_bandwidth = 18;
/* 1039 */       } else if (equiv_rate < 80000 * C) {
/* 1040 */         min_bandwidth = 19;
/*      */       } else {
/* 1042 */         min_bandwidth = 20;
/*      */       } 
/* 1044 */       signalBandwidth = Inlines.IMAX(this.analysis.bandwidth, min_bandwidth);
/*      */     } 
/*      */     
/* 1047 */     if (this.lfe != 0) {
/* 1048 */       signalBandwidth = 1;
/*      */     }
/*      */     
/* 1051 */     BoxedValueInt boxed_intensity = new BoxedValueInt(this.intensity);
/* 1052 */     BoxedValueInt boxed_balance = new BoxedValueInt(0);
/* 1053 */     BoxedValueInt boxed_dual_stereo = new BoxedValueInt(dual_stereo);
/* 1054 */     int codedBands = Rate.compute_allocation(mode, start, end, offsets, cap, alloc_trim, boxed_intensity, boxed_dual_stereo, bits, boxed_balance, pulses, fine_quant, fine_priority, C, LM, enc, 1, this.lastCodedBands, signalBandwidth);
/*      */ 
/*      */     
/* 1057 */     this.intensity = boxed_intensity.Val;
/* 1058 */     int balance = boxed_balance.Val;
/* 1059 */     dual_stereo = boxed_dual_stereo.Val;
/*      */     
/* 1061 */     if (this.lastCodedBands != 0) {
/* 1062 */       this.lastCodedBands = Inlines.IMIN(this.lastCodedBands + 1, Inlines.IMAX(this.lastCodedBands - 1, codedBands));
/*      */     } else {
/* 1064 */       this.lastCodedBands = codedBands;
/*      */     } 
/*      */     
/* 1067 */     QuantizeBands.quant_fine_energy(mode, start, end, this.oldBandE, error, fine_quant, enc, C);
/*      */ 
/*      */     
/* 1070 */     short[] collapse_masks = new short[C * nbEBands];
/* 1071 */     BoxedValueInt boxed_rng = new BoxedValueInt(this.rng);
/* 1072 */     Bands.quant_all_bands(1, mode, start, end, X[0], (C == 2) ? X[1] : null, collapse_masks, bandE, pulses, shortBlocks, this.spread_decision, dual_stereo, this.intensity, tf_res, nbCompressedBytes * 64 - anti_collapse_rsv, balance, enc, LM, codedBands, boxed_rng);
/*      */ 
/*      */ 
/*      */     
/* 1076 */     this.rng = boxed_rng.Val;
/*      */     
/* 1078 */     if (anti_collapse_rsv > 0) {
/* 1079 */       anti_collapse_on = (this.consec_transient < 2) ? 1 : 0;
/* 1080 */       enc.enc_bits(anti_collapse_on, 1);
/*      */     } 
/*      */     
/* 1083 */     QuantizeBands.quant_energy_finalise(mode, start, end, this.oldBandE, error, fine_quant, fine_priority, nbCompressedBytes * 8 - enc.tell(), enc, C);
/*      */     
/* 1085 */     if (silence != 0) {
/* 1086 */       for (i = 0; i < nbEBands; i++) {
/* 1087 */         this.oldBandE[0][i] = -28672;
/*      */       }
/* 1089 */       if (C == 2) {
/* 1090 */         for (i = 0; i < nbEBands; i++) {
/* 1091 */           this.oldBandE[1][i] = -28672;
/*      */         }
/*      */       }
/*      */     } 
/*      */     
/* 1096 */     this.prefilter_period = pitch_index;
/* 1097 */     this.prefilter_gain = gain1;
/* 1098 */     this.prefilter_tapset = prefilter_tapset;
/*      */     
/* 1100 */     if (CC == 2 && C == 1) {
/* 1101 */       System.arraycopy(this.oldBandE[0], 0, this.oldBandE[1], 0, nbEBands);
/*      */     }
/*      */     
/* 1104 */     if (isTransient == 0) {
/* 1105 */       System.arraycopy(this.oldLogE[0], 0, this.oldLogE2[0], 0, nbEBands);
/* 1106 */       System.arraycopy(this.oldBandE[0], 0, this.oldLogE[0], 0, nbEBands);
/* 1107 */       if (CC == 2) {
/* 1108 */         System.arraycopy(this.oldLogE[1], 0, this.oldLogE2[1], 0, nbEBands);
/* 1109 */         System.arraycopy(this.oldBandE[1], 0, this.oldLogE[1], 0, nbEBands);
/*      */       } 
/*      */     } else {
/* 1112 */       for (i = 0; i < nbEBands; i++) {
/* 1113 */         this.oldLogE[0][i] = Inlines.MIN16(this.oldLogE[0][i], this.oldBandE[0][i]);
/*      */       }
/* 1115 */       if (CC == 2) {
/* 1116 */         for (i = 0; i < nbEBands; i++) {
/* 1117 */           this.oldLogE[1][i] = Inlines.MIN16(this.oldLogE[1][i], this.oldBandE[1][i]);
/*      */         }
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1123 */     c = 0;
/*      */     do {
/* 1125 */       for (i = 0; i < start; i++) {
/* 1126 */         this.oldBandE[c][i] = 0;
/* 1127 */         this.oldLogE2[c][i] = -28672; this.oldLogE[c][i] = -28672;
/*      */       } 
/* 1129 */       for (i = end; i < nbEBands; i++) {
/* 1130 */         this.oldBandE[c][i] = 0;
/* 1131 */         this.oldLogE2[c][i] = -28672; this.oldLogE[c][i] = -28672;
/*      */       } 
/* 1133 */     } while (++c < CC);
/*      */     
/* 1135 */     if (isTransient != 0 || transient_got_disabled != 0) {
/* 1136 */       this.consec_transient++;
/*      */     } else {
/* 1138 */       this.consec_transient = 0;
/*      */     } 
/* 1140 */     this.rng = (int)enc.rng;
/*      */ 
/*      */ 
/*      */     
/* 1144 */     enc.enc_done();
/*      */     
/* 1146 */     if (enc.get_error() != 0) {
/* 1147 */       return OpusError.OPUS_INTERNAL_ERROR;
/*      */     }
/* 1149 */     return nbCompressedBytes;
/*      */   }
/*      */ 
/*      */   
/*      */   void SetComplexity(int value) {
/* 1154 */     if (value < 0 || value > 10) {
/* 1155 */       throw new IllegalArgumentException("Complexity must be between 0 and 10 inclusive");
/*      */     }
/* 1157 */     this.complexity = value;
/*      */   }
/*      */   
/*      */   void SetStartBand(int value) {
/* 1161 */     if (value < 0 || value >= this.mode.nbEBands) {
/* 1162 */       throw new IllegalArgumentException("Start band above max number of ebands (or negative)");
/*      */     }
/* 1164 */     this.start = value;
/*      */   }
/*      */   
/*      */   void SetEndBand(int value) {
/* 1168 */     if (value < 1 || value > this.mode.nbEBands) {
/* 1169 */       throw new IllegalArgumentException("End band above max number of ebands (or less than 1)");
/*      */     }
/* 1171 */     this.end = value;
/*      */   }
/*      */   
/*      */   void SetPacketLossPercent(int value) {
/* 1175 */     if (value < 0 || value > 100) {
/* 1176 */       throw new IllegalArgumentException("Packet loss must be between 0 and 100");
/*      */     }
/* 1178 */     this.loss_rate = value;
/*      */   }
/*      */   
/*      */   void SetPrediction(int value) {
/* 1182 */     if (value < 0 || value > 2) {
/* 1183 */       throw new IllegalArgumentException("CELT prediction mode must be 0, 1, or 2");
/*      */     }
/* 1185 */     this.disable_pf = (value <= 1) ? 1 : 0;
/* 1186 */     this.force_intra = (value == 0) ? 1 : 0;
/*      */   }
/*      */   
/*      */   void SetVBRConstraint(boolean value) {
/* 1190 */     this.constrained_vbr = value ? 1 : 0;
/*      */   }
/*      */   
/*      */   void SetVBR(boolean value) {
/* 1194 */     this.vbr = value ? 1 : 0;
/*      */   }
/*      */   
/*      */   void SetBitrate(int value) {
/* 1198 */     if (value <= 500 && value != -1) {
/* 1199 */       throw new IllegalArgumentException("Bitrate out of range");
/*      */     }
/* 1201 */     value = Inlines.IMIN(value, 260000 * this.channels);
/* 1202 */     this.bitrate = value;
/*      */   }
/*      */   
/*      */   void SetChannels(int value) {
/* 1206 */     if (value < 1 || value > 2) {
/* 1207 */       throw new IllegalArgumentException("Channel count must be 1 or 2");
/*      */     }
/* 1209 */     this.stream_channels = value;
/*      */   }
/*      */   
/*      */   void SetLSBDepth(int value) {
/* 1213 */     if (value < 8 || value > 24) {
/* 1214 */       throw new IllegalArgumentException("Bit depth must be between 8 and 24");
/*      */     }
/* 1216 */     this.lsb_depth = value;
/*      */   }
/*      */   
/*      */   int GetLSBDepth() {
/* 1220 */     return this.lsb_depth;
/*      */   }
/*      */   
/*      */   void SetExpertFrameDuration(OpusFramesize value) {
/* 1224 */     this.variable_duration = value;
/*      */   }
/*      */   
/*      */   void SetSignalling(int value) {
/* 1228 */     this.signalling = value;
/*      */   }
/*      */   
/*      */   void SetAnalysis(AnalysisInfo value) {
/* 1232 */     if (value == null) {
/* 1233 */       throw new IllegalArgumentException("AnalysisInfo");
/*      */     }
/* 1235 */     this.analysis.Assign(value);
/*      */   }
/*      */   
/*      */   CeltMode GetMode() {
/* 1239 */     return this.mode;
/*      */   }
/*      */   
/*      */   int GetFinalRange() {
/* 1243 */     return this.rng;
/*      */   }
/*      */   
/*      */   void SetLFE(int value) {
/* 1247 */     this.lfe = value;
/*      */   }
/*      */   
/*      */   void SetEnergyMask(int[] value) {
/* 1251 */     this.energy_mask = value;
/*      */   }
/*      */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\CeltEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */