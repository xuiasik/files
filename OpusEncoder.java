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
/*      */ public class OpusEncoder
/*      */ {
/*   42 */   final EncControlState silk_mode = new EncControlState();
/*      */   OpusApplication application;
/*      */   int channels;
/*      */   int delay_compensation;
/*      */   int force_channels;
/*      */   OpusSignal signal_type;
/*      */   OpusBandwidth user_bandwidth;
/*      */   OpusBandwidth max_bandwidth;
/*      */   OpusMode user_forced_mode;
/*      */   int voice_ratio;
/*      */   int Fs;
/*      */   int use_vbr;
/*      */   int vbr_constraint;
/*      */   OpusFramesize variable_duration;
/*      */   int bitrate_bps;
/*      */   int user_bitrate_bps;
/*      */   int lsb_depth;
/*      */   int encoder_buffer;
/*      */   int lfe;
/*   61 */   final TonalityAnalysisState analysis = new TonalityAnalysisState();
/*      */   
/*      */   int stream_channels;
/*      */   
/*      */   short hybrid_stereo_width_Q14;
/*      */   int variable_HP_smth2_Q15;
/*      */   int prev_HB_gain;
/*   68 */   final int[] hp_mem = new int[4];
/*      */   
/*      */   OpusMode mode;
/*      */   OpusMode prev_mode;
/*      */   int prev_channels;
/*      */   int prev_framesize;
/*      */   OpusBandwidth bandwidth;
/*      */   int silk_bw_switch;
/*      */   int first;
/*      */   int[] energy_masking;
/*   78 */   final StereoWidthState width_mem = new StereoWidthState();
/*   79 */   final short[] delay_buffer = new short[960];
/*      */ 
/*      */   
/*      */   OpusBandwidth detected_bandwidth;
/*      */   
/*      */   int rangeFinal;
/*      */   
/*   86 */   final SilkEncoder SilkEncoder = new SilkEncoder();
/*   87 */   final CeltEncoder Celt_Encoder = new CeltEncoder();
/*      */ 
/*      */   
/*      */   OpusEncoder() {}
/*      */   
/*      */   void reset() {
/*   93 */     this.silk_mode.Reset();
/*   94 */     this.application = OpusApplication.OPUS_APPLICATION_UNIMPLEMENTED;
/*   95 */     this.channels = 0;
/*   96 */     this.delay_compensation = 0;
/*   97 */     this.force_channels = 0;
/*   98 */     this.signal_type = OpusSignal.OPUS_SIGNAL_UNKNOWN;
/*   99 */     this.user_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_UNKNOWN;
/*  100 */     this.max_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_UNKNOWN;
/*  101 */     this.user_forced_mode = OpusMode.MODE_UNKNOWN;
/*  102 */     this.voice_ratio = 0;
/*  103 */     this.Fs = 0;
/*  104 */     this.use_vbr = 0;
/*  105 */     this.vbr_constraint = 0;
/*  106 */     this.variable_duration = OpusFramesize.OPUS_FRAMESIZE_UNKNOWN;
/*  107 */     this.bitrate_bps = 0;
/*  108 */     this.user_bitrate_bps = 0;
/*  109 */     this.lsb_depth = 0;
/*  110 */     this.encoder_buffer = 0;
/*  111 */     this.lfe = 0;
/*  112 */     this.analysis.Reset();
/*  113 */     PartialReset();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void PartialReset() {
/*  120 */     this.stream_channels = 0;
/*  121 */     this.hybrid_stereo_width_Q14 = 0;
/*  122 */     this.variable_HP_smth2_Q15 = 0;
/*  123 */     this.prev_HB_gain = 0;
/*  124 */     Arrays.MemSet(this.hp_mem, 0, 4);
/*  125 */     this.mode = OpusMode.MODE_UNKNOWN;
/*  126 */     this.prev_mode = OpusMode.MODE_UNKNOWN;
/*  127 */     this.prev_channels = 0;
/*  128 */     this.prev_framesize = 0;
/*  129 */     this.bandwidth = OpusBandwidth.OPUS_BANDWIDTH_UNKNOWN;
/*  130 */     this.silk_bw_switch = 0;
/*  131 */     this.first = 0;
/*  132 */     this.energy_masking = null;
/*  133 */     this.width_mem.Reset();
/*  134 */     Arrays.MemSet(this.delay_buffer, (short)0, 960);
/*  135 */     this.detected_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_UNKNOWN;
/*  136 */     this.rangeFinal = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetState() {
/*  142 */     EncControlState dummy = new EncControlState();
/*  143 */     this.analysis.Reset();
/*  144 */     PartialReset();
/*      */     
/*  146 */     this.Celt_Encoder.ResetState();
/*  147 */     EncodeAPI.silk_InitEncoder(this.SilkEncoder, dummy);
/*  148 */     this.stream_channels = this.channels;
/*  149 */     this.hybrid_stereo_width_Q14 = 16384;
/*  150 */     this.prev_HB_gain = 32767;
/*  151 */     this.first = 1;
/*  152 */     this.mode = OpusMode.MODE_HYBRID;
/*  153 */     this.bandwidth = OpusBandwidth.OPUS_BANDWIDTH_FULLBAND;
/*  154 */     this.variable_HP_smth2_Q15 = Inlines.silk_LSHIFT(Inlines.silk_lin2log(60), 8);
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
/*      */   public OpusEncoder(int Fs, int channels, OpusApplication application) throws OpusException {
/*  190 */     if (Fs != 48000 && Fs != 24000 && Fs != 16000 && Fs != 12000 && Fs != 8000) {
/*  191 */       throw new IllegalArgumentException("Sample rate is invalid (must be 8/12/16/24/48 Khz)");
/*      */     }
/*  193 */     if (channels != 1 && channels != 2) {
/*  194 */       throw new IllegalArgumentException("Number of channels must be 1 or 2");
/*      */     }
/*      */     
/*  197 */     int ret = opus_init_encoder(Fs, channels, application);
/*  198 */     if (ret != OpusError.OPUS_OK) {
/*  199 */       if (ret == OpusError.OPUS_BAD_ARG) {
/*  200 */         throw new IllegalArgumentException("OPUS_BAD_ARG when creating encoder");
/*      */       }
/*  202 */       throw new OpusException("Error while initializing encoder", ret);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int opus_init_encoder(int Fs, int channels, OpusApplication application) {
/*  212 */     if ((Fs != 48000 && Fs != 24000 && Fs != 16000 && Fs != 12000 && Fs != 8000) || (channels != 1 && channels != 2) || application == OpusApplication.OPUS_APPLICATION_UNIMPLEMENTED)
/*      */     {
/*  214 */       return OpusError.OPUS_BAD_ARG;
/*      */     }
/*      */     
/*  217 */     reset();
/*      */     
/*  219 */     SilkEncoder silk_enc = this.SilkEncoder;
/*  220 */     CeltEncoder celt_enc = this.Celt_Encoder;
/*      */     
/*  222 */     this.stream_channels = this.channels = channels;
/*      */     
/*  224 */     this.Fs = Fs;
/*      */     
/*  226 */     int ret = EncodeAPI.silk_InitEncoder(silk_enc, this.silk_mode);
/*  227 */     if (ret != 0) {
/*  228 */       return OpusError.OPUS_INTERNAL_ERROR;
/*      */     }
/*      */ 
/*      */     
/*  232 */     this.silk_mode.nChannelsAPI = channels;
/*  233 */     this.silk_mode.nChannelsInternal = channels;
/*  234 */     this.silk_mode.API_sampleRate = this.Fs;
/*  235 */     this.silk_mode.maxInternalSampleRate = 16000;
/*  236 */     this.silk_mode.minInternalSampleRate = 8000;
/*  237 */     this.silk_mode.desiredInternalSampleRate = 16000;
/*  238 */     this.silk_mode.payloadSize_ms = 20;
/*  239 */     this.silk_mode.bitRate = 25000;
/*  240 */     this.silk_mode.packetLossPercentage = 0;
/*  241 */     this.silk_mode.complexity = 9;
/*  242 */     this.silk_mode.useInBandFEC = 0;
/*  243 */     this.silk_mode.useDTX = 0;
/*  244 */     this.silk_mode.useCBR = 0;
/*  245 */     this.silk_mode.reducedDependency = 0;
/*      */ 
/*      */ 
/*      */     
/*  249 */     int err = celt_enc.celt_encoder_init(Fs, channels);
/*  250 */     if (err != OpusError.OPUS_OK) {
/*  251 */       return OpusError.OPUS_INTERNAL_ERROR;
/*      */     }
/*      */     
/*  254 */     celt_enc.SetSignalling(0);
/*  255 */     celt_enc.SetComplexity(this.silk_mode.complexity);
/*      */     
/*  257 */     this.use_vbr = 1;
/*      */     
/*  259 */     this.vbr_constraint = 1;
/*  260 */     this.user_bitrate_bps = -1000;
/*  261 */     this.bitrate_bps = 3000 + Fs * channels;
/*  262 */     this.application = application;
/*  263 */     this.signal_type = OpusSignal.OPUS_SIGNAL_AUTO;
/*  264 */     this.user_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_AUTO;
/*  265 */     this.max_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_FULLBAND;
/*  266 */     this.force_channels = -1000;
/*  267 */     this.user_forced_mode = OpusMode.MODE_AUTO;
/*  268 */     this.voice_ratio = -1;
/*  269 */     this.encoder_buffer = this.Fs / 100;
/*  270 */     this.lsb_depth = 24;
/*  271 */     this.variable_duration = OpusFramesize.OPUS_FRAMESIZE_ARG;
/*      */ 
/*      */ 
/*      */     
/*  275 */     this.delay_compensation = this.Fs / 250;
/*      */     
/*  277 */     this.hybrid_stereo_width_Q14 = 16384;
/*  278 */     this.prev_HB_gain = 32767;
/*  279 */     this.variable_HP_smth2_Q15 = Inlines.silk_LSHIFT(Inlines.silk_lin2log(60), 8);
/*  280 */     this.first = 1;
/*  281 */     this.mode = OpusMode.MODE_HYBRID;
/*  282 */     this.bandwidth = OpusBandwidth.OPUS_BANDWIDTH_FULLBAND;
/*      */     
/*  284 */     Analysis.tonality_analysis_init(this.analysis);
/*      */     
/*  286 */     return OpusError.OPUS_OK;
/*      */   }
/*      */   
/*      */   int user_bitrate_to_bitrate(int frame_size, int max_data_bytes) {
/*  290 */     if (frame_size == 0) {
/*  291 */       frame_size = this.Fs / 400;
/*      */     }
/*  293 */     if (this.user_bitrate_bps == -1000)
/*  294 */       return 60 * this.Fs / frame_size + this.Fs * this.channels; 
/*  295 */     if (this.user_bitrate_bps == -1) {
/*  296 */       return max_data_bytes * 8 * this.Fs / frame_size;
/*      */     }
/*  298 */     return this.user_bitrate_bps;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int opus_encode_native(short[] pcm, int pcm_ptr, int frame_size, byte[] data, int data_ptr, int out_data_bytes, int lsb_depth, short[] analysis_pcm, int analysis_pcm_ptr, int analysis_size, int c1, int c2, int analysis_channels, int float_api) {
/*  327 */     int nb_compr_bytes, hp_freq_smth1, voice_est, delay_compensation, stereo_width, ret = 0;
/*      */     
/*  329 */     EntropyCoder enc = new EntropyCoder();
/*      */     
/*  331 */     int prefill = 0;
/*  332 */     int start_band = 0;
/*  333 */     int redundancy = 0;
/*  334 */     int redundancy_bytes = 0;
/*      */     
/*  336 */     int celt_to_silk = 0;
/*      */ 
/*      */     
/*  339 */     int to_celt = 0;
/*  340 */     int redundant_rng = 0;
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
/*  356 */     AnalysisInfo analysis_info = new AnalysisInfo();
/*  357 */     int analysis_read_pos_bak = -1;
/*  358 */     int analysis_read_subframe_bak = -1;
/*      */ 
/*      */     
/*  361 */     int max_data_bytes = Inlines.IMIN(1276, out_data_bytes);
/*      */     
/*  363 */     this.rangeFinal = 0;
/*  364 */     if ((this.variable_duration == OpusFramesize.OPUS_FRAMESIZE_UNKNOWN && 400 * frame_size != this.Fs && 200 * frame_size != this.Fs && 100 * frame_size != this.Fs && 50 * frame_size != this.Fs && 25 * frame_size != this.Fs && 50 * frame_size != 3 * this.Fs) || 400 * frame_size < this.Fs || max_data_bytes <= 0)
/*      */     {
/*      */ 
/*      */       
/*  368 */       return OpusError.OPUS_BAD_ARG;
/*      */     }
/*      */     
/*  371 */     SilkEncoder silk_enc = this.SilkEncoder;
/*  372 */     CeltEncoder celt_enc = this.Celt_Encoder;
/*  373 */     if (this.application == OpusApplication.OPUS_APPLICATION_RESTRICTED_LOWDELAY) {
/*  374 */       delay_compensation = 0;
/*      */     } else {
/*  376 */       delay_compensation = this.delay_compensation;
/*      */     } 
/*      */     
/*  379 */     lsb_depth = Inlines.IMIN(lsb_depth, this.lsb_depth);
/*  380 */     CeltMode celt_mode = celt_enc.GetMode();
/*  381 */     this.voice_ratio = -1;
/*      */     
/*  383 */     if (this.analysis.enabled) {
/*  384 */       analysis_info.valid = 0;
/*  385 */       if (this.silk_mode.complexity >= 7 && this.Fs == 48000) {
/*  386 */         analysis_read_pos_bak = this.analysis.read_pos;
/*  387 */         analysis_read_subframe_bak = this.analysis.read_subframe;
/*  388 */         Analysis.run_analysis(this.analysis, celt_mode, 
/*      */             
/*  390 */             (analysis_pcm != null) ? analysis_pcm : null, analysis_pcm_ptr, analysis_size, frame_size, c1, c2, analysis_channels, this.Fs, lsb_depth, analysis_info);
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
/*      */       
/*  402 */       this.detected_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_UNKNOWN;
/*  403 */       if (analysis_info.valid != 0) {
/*      */         
/*  405 */         if (this.signal_type == OpusSignal.OPUS_SIGNAL_AUTO) {
/*  406 */           this.voice_ratio = (int)Math.floor((0.5F + 100.0F * (1.0F - analysis_info.music_prob)));
/*      */         }
/*      */         
/*  409 */         int analysis_bandwidth = analysis_info.bandwidth;
/*  410 */         if (analysis_bandwidth <= 12) {
/*  411 */           this.detected_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND;
/*  412 */         } else if (analysis_bandwidth <= 14) {
/*  413 */           this.detected_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND;
/*  414 */         } else if (analysis_bandwidth <= 16) {
/*  415 */           this.detected_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND;
/*  416 */         } else if (analysis_bandwidth <= 18) {
/*  417 */           this.detected_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND;
/*      */         } else {
/*  419 */           this.detected_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_FULLBAND;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  424 */     if (this.channels == 2 && this.force_channels != 1) {
/*  425 */       stereo_width = CodecHelpers.compute_stereo_width(pcm, pcm_ptr, frame_size, this.Fs, this.width_mem);
/*      */     } else {
/*  427 */       stereo_width = 0;
/*      */     } 
/*  429 */     int total_buffer = delay_compensation;
/*  430 */     this.bitrate_bps = user_bitrate_to_bitrate(frame_size, max_data_bytes);
/*      */     
/*  432 */     int frame_rate = this.Fs / frame_size;
/*  433 */     if (this.use_vbr == 0) {
/*      */ 
/*      */       
/*  436 */       int frame_rate3 = 3 * this.Fs / frame_size;
/*      */       
/*  438 */       int cbrBytes = Inlines.IMIN((3 * this.bitrate_bps / 8 + frame_rate3 / 2) / frame_rate3, max_data_bytes);
/*  439 */       this.bitrate_bps = cbrBytes * frame_rate3 * 8 / 3;
/*  440 */       max_data_bytes = cbrBytes;
/*      */     } 
/*  442 */     if (max_data_bytes < 3 || this.bitrate_bps < 3 * frame_rate * 8 || (frame_rate < 50 && (max_data_bytes * frame_rate < 300 || this.bitrate_bps < 2400))) {
/*      */ 
/*      */       
/*  445 */       OpusMode tocmode = this.mode;
/*  446 */       OpusBandwidth bw = (this.bandwidth == OpusBandwidth.OPUS_BANDWIDTH_UNKNOWN) ? OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND : this.bandwidth;
/*  447 */       if (tocmode == OpusMode.MODE_UNKNOWN) {
/*  448 */         tocmode = OpusMode.MODE_SILK_ONLY;
/*      */       }
/*  450 */       if (frame_rate > 100) {
/*  451 */         tocmode = OpusMode.MODE_CELT_ONLY;
/*      */       }
/*  453 */       if (frame_rate < 50) {
/*  454 */         tocmode = OpusMode.MODE_SILK_ONLY;
/*      */       }
/*  456 */       if (tocmode == OpusMode.MODE_SILK_ONLY && OpusBandwidthHelpers.GetOrdinal(bw) > OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND)) {
/*  457 */         bw = OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND;
/*  458 */       } else if (tocmode == OpusMode.MODE_CELT_ONLY && OpusBandwidthHelpers.GetOrdinal(bw) == OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND)) {
/*  459 */         bw = OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND;
/*  460 */       } else if (tocmode == OpusMode.MODE_HYBRID && OpusBandwidthHelpers.GetOrdinal(bw) <= OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND)) {
/*  461 */         bw = OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND;
/*      */       } 
/*  463 */       data[data_ptr] = CodecHelpers.gen_toc(tocmode, frame_rate, bw, this.stream_channels);
/*  464 */       ret = 1;
/*  465 */       if (this.use_vbr == 0) {
/*  466 */         ret = OpusRepacketizer.padPacket(data, data_ptr, ret, max_data_bytes);
/*  467 */         if (ret == OpusError.OPUS_OK) {
/*  468 */           ret = max_data_bytes;
/*      */         }
/*      */       } 
/*  471 */       return ret;
/*      */     } 
/*  473 */     int max_rate = frame_rate * max_data_bytes * 8;
/*      */ 
/*      */     
/*  476 */     int equiv_rate = this.bitrate_bps - (40 * this.channels + 20) * (this.Fs / frame_size - 50);
/*      */     
/*  478 */     if (this.signal_type == OpusSignal.OPUS_SIGNAL_VOICE) {
/*  479 */       voice_est = 127;
/*  480 */     } else if (this.signal_type == OpusSignal.OPUS_SIGNAL_MUSIC) {
/*  481 */       voice_est = 0;
/*  482 */     } else if (this.voice_ratio >= 0) {
/*  483 */       voice_est = this.voice_ratio * 327 >> 8;
/*      */       
/*  485 */       if (this.application == OpusApplication.OPUS_APPLICATION_AUDIO) {
/*  486 */         voice_est = Inlines.IMIN(voice_est, 115);
/*      */       }
/*  488 */     } else if (this.application == OpusApplication.OPUS_APPLICATION_VOIP) {
/*  489 */       voice_est = 115;
/*      */     } else {
/*  491 */       voice_est = 48;
/*      */     } 
/*      */     
/*  494 */     if (this.force_channels != -1000 && this.channels == 2) {
/*  495 */       this.stream_channels = this.force_channels;
/*  496 */     } else if (this.channels == 2) {
/*      */       
/*  498 */       int stereo_threshold = 30000 + (voice_est * voice_est * 0 >> 14);
/*  499 */       if (this.stream_channels == 2) {
/*  500 */         stereo_threshold -= 1000;
/*      */       } else {
/*  502 */         stereo_threshold += 1000;
/*      */       } 
/*  504 */       this.stream_channels = (equiv_rate > stereo_threshold) ? 2 : 1;
/*      */     } else {
/*  506 */       this.stream_channels = this.channels;
/*      */     } 
/*  508 */     equiv_rate = this.bitrate_bps - (40 * this.stream_channels + 20) * (this.Fs / frame_size - 50);
/*      */ 
/*      */     
/*  511 */     if (this.application == OpusApplication.OPUS_APPLICATION_RESTRICTED_LOWDELAY) {
/*  512 */       this.mode = OpusMode.MODE_CELT_ONLY;
/*  513 */     } else if (this.user_forced_mode == OpusMode.MODE_AUTO) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  519 */       int mode_voice = Inlines.MULT16_32_Q15(32767 - stereo_width, OpusTables.mode_thresholds[0][0]) + Inlines.MULT16_32_Q15(stereo_width, OpusTables.mode_thresholds[1][0]);
/*      */       
/*  521 */       int mode_music = Inlines.MULT16_32_Q15(32767 - stereo_width, OpusTables.mode_thresholds[1][1]) + Inlines.MULT16_32_Q15(stereo_width, OpusTables.mode_thresholds[1][1]);
/*      */       
/*  523 */       int threshold = mode_music + (voice_est * voice_est * (mode_voice - mode_music) >> 14);
/*      */       
/*  525 */       if (this.application == OpusApplication.OPUS_APPLICATION_VOIP) {
/*  526 */         threshold += 8000;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  531 */       if (this.prev_mode == OpusMode.MODE_CELT_ONLY) {
/*  532 */         threshold -= 4000;
/*  533 */       } else if (this.prev_mode != OpusMode.MODE_AUTO && this.prev_mode != OpusMode.MODE_UNKNOWN) {
/*  534 */         threshold += 4000;
/*      */       } 
/*      */       
/*  537 */       this.mode = (equiv_rate >= threshold) ? OpusMode.MODE_CELT_ONLY : OpusMode.MODE_SILK_ONLY;
/*      */ 
/*      */       
/*  540 */       if (this.silk_mode.useInBandFEC != 0 && this.silk_mode.packetLossPercentage > 128 - voice_est >> 4) {
/*  541 */         this.mode = OpusMode.MODE_SILK_ONLY;
/*      */       }
/*      */       
/*  544 */       if (this.silk_mode.useDTX != 0 && voice_est > 100) {
/*  545 */         this.mode = OpusMode.MODE_SILK_ONLY;
/*      */       }
/*      */     } else {
/*  548 */       this.mode = this.user_forced_mode;
/*      */     } 
/*      */ 
/*      */     
/*  552 */     if (this.mode != OpusMode.MODE_CELT_ONLY && frame_size < this.Fs / 100) {
/*  553 */       this.mode = OpusMode.MODE_CELT_ONLY;
/*      */     }
/*  555 */     if (this.lfe != 0) {
/*  556 */       this.mode = OpusMode.MODE_CELT_ONLY;
/*      */     }
/*      */     
/*  559 */     if (max_data_bytes < ((frame_rate > 50) ? 12000 : 8000) * frame_size / this.Fs * 8) {
/*  560 */       this.mode = OpusMode.MODE_CELT_ONLY;
/*      */     }
/*      */     
/*  563 */     if (this.stream_channels == 1 && this.prev_channels == 2 && this.silk_mode.toMono == 0 && this.mode != OpusMode.MODE_CELT_ONLY && this.prev_mode != OpusMode.MODE_CELT_ONLY) {
/*      */ 
/*      */       
/*  566 */       this.silk_mode.toMono = 1;
/*  567 */       this.stream_channels = 2;
/*      */     } else {
/*  569 */       this.silk_mode.toMono = 0;
/*      */     } 
/*      */     
/*  572 */     if (this.prev_mode != OpusMode.MODE_AUTO && this.prev_mode != OpusMode.MODE_UNKNOWN && ((this.mode != OpusMode.MODE_CELT_ONLY && this.prev_mode == OpusMode.MODE_CELT_ONLY) || (this.mode == OpusMode.MODE_CELT_ONLY && this.prev_mode != OpusMode.MODE_CELT_ONLY))) {
/*      */ 
/*      */       
/*  575 */       redundancy = 1;
/*  576 */       celt_to_silk = (this.mode != OpusMode.MODE_CELT_ONLY) ? 1 : 0;
/*  577 */       if (celt_to_silk == 0)
/*      */       {
/*  579 */         if (frame_size >= this.Fs / 100) {
/*  580 */           this.mode = this.prev_mode;
/*  581 */           to_celt = 1;
/*      */         } else {
/*  583 */           redundancy = 0;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  588 */     if (this.silk_bw_switch != 0) {
/*  589 */       redundancy = 1;
/*  590 */       celt_to_silk = 1;
/*  591 */       this.silk_bw_switch = 0;
/*  592 */       prefill = 1;
/*      */     } 
/*      */     
/*  595 */     if (redundancy != 0) {
/*      */       
/*  597 */       redundancy_bytes = Inlines.IMIN(257, max_data_bytes * this.Fs / 200 / (frame_size + this.Fs / 200));
/*      */       
/*  599 */       if (this.use_vbr != 0) {
/*  600 */         redundancy_bytes = Inlines.IMIN(redundancy_bytes, this.bitrate_bps / 1600);
/*      */       }
/*      */     } 
/*      */     
/*  604 */     if (this.mode != OpusMode.MODE_CELT_ONLY && this.prev_mode == OpusMode.MODE_CELT_ONLY) {
/*  605 */       EncControlState dummy = new EncControlState();
/*  606 */       EncodeAPI.silk_InitEncoder(silk_enc, dummy);
/*  607 */       prefill = 1;
/*      */     } 
/*      */ 
/*      */     
/*  611 */     if (this.mode == OpusMode.MODE_CELT_ONLY || this.first != 0 || this.silk_mode.allowBandwidthSwitch != 0) {
/*      */ 
/*      */       
/*  614 */       int[] voice_bandwidth_thresholds, music_bandwidth_thresholds, bandwidth_thresholds = new int[8];
/*  615 */       OpusBandwidth bandwidth = OpusBandwidth.OPUS_BANDWIDTH_FULLBAND;
/*      */ 
/*      */       
/*  618 */       int equiv_rate2 = equiv_rate;
/*  619 */       if (this.mode != OpusMode.MODE_CELT_ONLY) {
/*      */         
/*  621 */         equiv_rate2 = equiv_rate2 * (45 + this.silk_mode.complexity) / 50;
/*      */         
/*  623 */         if (this.use_vbr == 0) {
/*  624 */           equiv_rate2 -= 1000;
/*      */         }
/*      */       } 
/*  627 */       if (this.channels == 2 && this.force_channels != 1) {
/*  628 */         voice_bandwidth_thresholds = OpusTables.stereo_voice_bandwidth_thresholds;
/*  629 */         music_bandwidth_thresholds = OpusTables.stereo_music_bandwidth_thresholds;
/*      */       } else {
/*  631 */         voice_bandwidth_thresholds = OpusTables.mono_voice_bandwidth_thresholds;
/*  632 */         music_bandwidth_thresholds = OpusTables.mono_music_bandwidth_thresholds;
/*      */       } 
/*      */       
/*  635 */       for (int i = 0; i < 8; i++) {
/*  636 */         bandwidth_thresholds[i] = music_bandwidth_thresholds[i] + (voice_est * voice_est * (voice_bandwidth_thresholds[i] - music_bandwidth_thresholds[i]) >> 14);
/*      */       }
/*      */ 
/*      */       
/*      */       do {
/*  641 */         int threshold = bandwidth_thresholds[2 * (OpusBandwidthHelpers.GetOrdinal(bandwidth) - OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND))];
/*  642 */         int hysteresis = bandwidth_thresholds[2 * (OpusBandwidthHelpers.GetOrdinal(bandwidth) - OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND)) + 1];
/*  643 */         if (this.first == 0) {
/*  644 */           if (OpusBandwidthHelpers.GetOrdinal(this.bandwidth) >= OpusBandwidthHelpers.GetOrdinal(bandwidth)) {
/*  645 */             threshold -= hysteresis;
/*      */           } else {
/*  647 */             threshold += hysteresis;
/*      */           } 
/*      */         }
/*  650 */         if (equiv_rate2 >= threshold) {
/*      */           break;
/*      */         }
/*      */         
/*  654 */         bandwidth = OpusBandwidthHelpers.SUBTRACT(bandwidth, 1);
/*  655 */       } while (OpusBandwidthHelpers.GetOrdinal(bandwidth) > OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND));
/*  656 */       this.bandwidth = bandwidth;
/*      */ 
/*      */       
/*  659 */       if (this.first == 0 && this.mode != OpusMode.MODE_CELT_ONLY && this.silk_mode.inWBmodeWithoutVariableLP == 0 && 
/*      */         
/*  661 */         OpusBandwidthHelpers.GetOrdinal(this.bandwidth) > OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND)) {
/*  662 */         this.bandwidth = OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND;
/*      */       }
/*      */     } 
/*      */     
/*  666 */     if (OpusBandwidthHelpers.GetOrdinal(this.bandwidth) > OpusBandwidthHelpers.GetOrdinal(this.max_bandwidth)) {
/*  667 */       this.bandwidth = this.max_bandwidth;
/*      */     }
/*      */     
/*  670 */     if (this.user_bandwidth != OpusBandwidth.OPUS_BANDWIDTH_AUTO) {
/*  671 */       this.bandwidth = this.user_bandwidth;
/*      */     }
/*      */ 
/*      */     
/*  675 */     if (this.mode != OpusMode.MODE_CELT_ONLY && max_rate < 15000) {
/*  676 */       this.bandwidth = OpusBandwidthHelpers.MIN(this.bandwidth, OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  681 */     if (this.Fs <= 24000 && OpusBandwidthHelpers.GetOrdinal(this.bandwidth) > OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND)) {
/*  682 */       this.bandwidth = OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND;
/*      */     }
/*  684 */     if (this.Fs <= 16000 && OpusBandwidthHelpers.GetOrdinal(this.bandwidth) > OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND)) {
/*  685 */       this.bandwidth = OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND;
/*      */     }
/*  687 */     if (this.Fs <= 12000 && OpusBandwidthHelpers.GetOrdinal(this.bandwidth) > OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND)) {
/*  688 */       this.bandwidth = OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND;
/*      */     }
/*  690 */     if (this.Fs <= 8000 && OpusBandwidthHelpers.GetOrdinal(this.bandwidth) > OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND)) {
/*  691 */       this.bandwidth = OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND;
/*      */     }
/*      */     
/*  694 */     if (this.detected_bandwidth != OpusBandwidth.OPUS_BANDWIDTH_UNKNOWN && this.user_bandwidth == OpusBandwidth.OPUS_BANDWIDTH_AUTO) {
/*      */       OpusBandwidth min_detected_bandwidth;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  700 */       if (equiv_rate <= 18000 * this.stream_channels && this.mode == OpusMode.MODE_CELT_ONLY) {
/*  701 */         min_detected_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND;
/*  702 */       } else if (equiv_rate <= 24000 * this.stream_channels && this.mode == OpusMode.MODE_CELT_ONLY) {
/*  703 */         min_detected_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND;
/*  704 */       } else if (equiv_rate <= 30000 * this.stream_channels) {
/*  705 */         min_detected_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND;
/*  706 */       } else if (equiv_rate <= 44000 * this.stream_channels) {
/*  707 */         min_detected_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND;
/*      */       } else {
/*  709 */         min_detected_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_FULLBAND;
/*      */       } 
/*      */       
/*  712 */       this.detected_bandwidth = OpusBandwidthHelpers.MAX(this.detected_bandwidth, min_detected_bandwidth);
/*  713 */       this.bandwidth = OpusBandwidthHelpers.MIN(this.bandwidth, this.detected_bandwidth);
/*      */     } 
/*  715 */     celt_enc.SetLSBDepth(lsb_depth);
/*      */ 
/*      */     
/*  718 */     if (this.mode == OpusMode.MODE_CELT_ONLY && this.bandwidth == OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND) {
/*  719 */       this.bandwidth = OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND;
/*      */     }
/*  721 */     if (this.lfe != 0) {
/*  722 */       this.bandwidth = OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND;
/*      */     }
/*      */ 
/*      */     
/*  726 */     if (frame_size > this.Fs / 50 && (this.mode == OpusMode.MODE_CELT_ONLY || OpusBandwidthHelpers.GetOrdinal(this.bandwidth) > OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND))) {
/*      */       int repacketize_len;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  736 */       if (this.analysis.enabled && analysis_read_pos_bak != -1) {
/*  737 */         this.analysis.read_pos = analysis_read_pos_bak;
/*  738 */         this.analysis.read_subframe = analysis_read_subframe_bak;
/*      */       } 
/*      */       
/*  741 */       int nb_frames = (frame_size > this.Fs / 25) ? 3 : 2;
/*  742 */       int bytes_per_frame = Inlines.IMIN(1276, (out_data_bytes - 3) / nb_frames);
/*      */       
/*  744 */       byte[] tmp_data = new byte[nb_frames * bytes_per_frame];
/*      */       
/*  746 */       OpusRepacketizer rp = new OpusRepacketizer();
/*      */       
/*  748 */       OpusMode bak_mode = this.user_forced_mode;
/*  749 */       OpusBandwidth bak_bandwidth = this.user_bandwidth;
/*  750 */       int bak_channels = this.force_channels;
/*      */       
/*  752 */       this.user_forced_mode = this.mode;
/*  753 */       this.user_bandwidth = this.bandwidth;
/*  754 */       this.force_channels = this.stream_channels;
/*  755 */       int bak_to_mono = this.silk_mode.toMono;
/*      */       
/*  757 */       if (bak_to_mono != 0) {
/*  758 */         this.force_channels = 1;
/*      */       } else {
/*  760 */         this.prev_channels = this.stream_channels;
/*      */       } 
/*  762 */       for (int i = 0; i < nb_frames; i++) {
/*      */         
/*  764 */         this.silk_mode.toMono = 0;
/*      */         
/*  766 */         if (to_celt != 0 && i == nb_frames - 1) {
/*  767 */           this.user_forced_mode = OpusMode.MODE_CELT_ONLY;
/*      */         }
/*  769 */         int tmp_len = opus_encode_native(pcm, pcm_ptr + i * this.channels * this.Fs / 50, this.Fs / 50, tmp_data, i * bytes_per_frame, bytes_per_frame, lsb_depth, null, 0, 0, c1, c2, analysis_channels, float_api);
/*      */ 
/*      */         
/*  772 */         if (tmp_len < 0)
/*      */         {
/*  774 */           return OpusError.OPUS_INTERNAL_ERROR;
/*      */         }
/*  776 */         ret = rp.addPacket(tmp_data, i * bytes_per_frame, tmp_len);
/*  777 */         if (ret < 0)
/*      */         {
/*  779 */           return OpusError.OPUS_INTERNAL_ERROR;
/*      */         }
/*      */       } 
/*  782 */       if (this.use_vbr != 0) {
/*  783 */         repacketize_len = out_data_bytes;
/*      */       } else {
/*  785 */         repacketize_len = Inlines.IMIN(3 * this.bitrate_bps / 1200 / nb_frames, out_data_bytes);
/*      */       } 
/*  787 */       ret = rp.opus_repacketizer_out_range_impl(0, nb_frames, data, data_ptr, repacketize_len, 0, (this.use_vbr == 0) ? 1 : 0);
/*  788 */       if (ret < 0) {
/*  789 */         return OpusError.OPUS_INTERNAL_ERROR;
/*      */       }
/*  791 */       this.user_forced_mode = bak_mode;
/*  792 */       this.user_bandwidth = bak_bandwidth;
/*  793 */       this.force_channels = bak_channels;
/*  794 */       this.silk_mode.toMono = bak_to_mono;
/*      */       
/*  796 */       return ret;
/*      */     } 
/*  798 */     OpusBandwidth curr_bandwidth = this.bandwidth;
/*      */ 
/*      */ 
/*      */     
/*  802 */     if (this.mode == OpusMode.MODE_SILK_ONLY && OpusBandwidthHelpers.GetOrdinal(curr_bandwidth) > OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND)) {
/*  803 */       this.mode = OpusMode.MODE_HYBRID;
/*      */     }
/*  805 */     if (this.mode == OpusMode.MODE_HYBRID && OpusBandwidthHelpers.GetOrdinal(curr_bandwidth) <= OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND)) {
/*  806 */       this.mode = OpusMode.MODE_SILK_ONLY;
/*      */     }
/*      */ 
/*      */     
/*  810 */     int bytes_target = Inlines.IMIN(max_data_bytes - redundancy_bytes, this.bitrate_bps * frame_size / this.Fs * 8) - 1;
/*      */     
/*  812 */     data_ptr++;
/*      */     
/*  814 */     enc.enc_init(data, data_ptr, max_data_bytes - 1);
/*      */     
/*  816 */     short[] pcm_buf = new short[(total_buffer + frame_size) * this.channels];
/*  817 */     System.arraycopy(this.delay_buffer, (this.encoder_buffer - total_buffer) * this.channels, pcm_buf, 0, total_buffer * this.channels);
/*      */     
/*  819 */     if (this.mode == OpusMode.MODE_CELT_ONLY) {
/*  820 */       hp_freq_smth1 = Inlines.silk_LSHIFT(Inlines.silk_lin2log(60), 8);
/*      */     } else {
/*  822 */       hp_freq_smth1 = (silk_enc.state_Fxx[0]).variable_HP_smth1_Q15;
/*      */     } 
/*      */     
/*  825 */     this.variable_HP_smth2_Q15 = Inlines.silk_SMLAWB(this.variable_HP_smth2_Q15, hp_freq_smth1 - this.variable_HP_smth2_Q15, 983);
/*      */ 
/*      */ 
/*      */     
/*  829 */     int cutoff_Hz = Inlines.silk_log2lin(Inlines.silk_RSHIFT(this.variable_HP_smth2_Q15, 8));
/*      */     
/*  831 */     if (this.application == OpusApplication.OPUS_APPLICATION_VOIP) {
/*  832 */       CodecHelpers.hp_cutoff(pcm, pcm_ptr, cutoff_Hz, pcm_buf, total_buffer * this.channels, this.hp_mem, frame_size, this.channels, this.Fs);
/*      */     } else {
/*  834 */       CodecHelpers.dc_reject(pcm, pcm_ptr, 3, pcm_buf, total_buffer * this.channels, this.hp_mem, frame_size, this.channels, this.Fs);
/*      */     } 
/*      */ 
/*      */     
/*  838 */     int HB_gain = 32767;
/*  839 */     if (this.mode != OpusMode.MODE_CELT_ONLY) {
/*      */       
/*  841 */       short[] pcm_silk = new short[this.channels * frame_size];
/*      */ 
/*      */       
/*  844 */       int total_bitRate = 8 * bytes_target * frame_rate;
/*  845 */       if (this.mode == OpusMode.MODE_HYBRID) {
/*      */ 
/*      */         
/*  848 */         this.silk_mode.bitRate = this.stream_channels * (5000 + ((this.Fs == 100 * frame_size) ? 1000 : 0));
/*  849 */         if (curr_bandwidth == OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND) {
/*      */           
/*  851 */           this.silk_mode.bitRate += (total_bitRate - this.silk_mode.bitRate) * 2 / 3;
/*      */         }
/*      */         else {
/*      */           
/*  855 */           this.silk_mode.bitRate += (total_bitRate - this.silk_mode.bitRate) * 3 / 5;
/*      */         } 
/*      */         
/*  858 */         if (this.silk_mode.bitRate > total_bitRate * 4 / 5) {
/*  859 */           this.silk_mode.bitRate = total_bitRate * 4 / 5;
/*      */         }
/*  861 */         if (this.energy_masking == null) {
/*      */           
/*  863 */           int celt_rate = total_bitRate - this.silk_mode.bitRate;
/*  864 */           int HB_gain_ref = (curr_bandwidth == OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND) ? 3000 : 3600;
/*  865 */           HB_gain = Inlines.SHL32(celt_rate, 9) / Inlines.SHR32(celt_rate + this.stream_channels * HB_gain_ref, 6);
/*  866 */           HB_gain = (HB_gain < 28086) ? (HB_gain + 4681) : 32767;
/*      */         } 
/*      */       } else {
/*      */         
/*  870 */         this.silk_mode.bitRate = total_bitRate;
/*      */       } 
/*      */ 
/*      */       
/*  874 */       if (this.energy_masking != null && this.use_vbr != 0 && this.lfe == 0) {
/*  875 */         int mask_sum = 0;
/*      */ 
/*      */ 
/*      */         
/*  879 */         int end = 17;
/*  880 */         short srate = 16000;
/*  881 */         if (this.bandwidth == OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND) {
/*  882 */           end = 13;
/*  883 */           srate = 8000;
/*  884 */         } else if (this.bandwidth == OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND) {
/*  885 */           end = 15;
/*  886 */           srate = 12000;
/*      */         } 
/*  888 */         for (int c = 0; c < this.channels; c++) {
/*  889 */           for (int i = 0; i < end; i++) {
/*      */             
/*  891 */             int mask = Inlines.MAX16(Inlines.MIN16(this.energy_masking[21 * c + i], 512), -2048);
/*      */             
/*  893 */             if (mask > 0) {
/*  894 */               mask = Inlines.HALF16(mask);
/*      */             }
/*  896 */             mask_sum += mask;
/*      */           } 
/*      */         } 
/*      */         
/*  900 */         int masking_depth = mask_sum / end * this.channels;
/*  901 */         masking_depth += 205;
/*  902 */         int rate_offset = Inlines.PSHR32(Inlines.MULT16_16(srate, masking_depth), 10);
/*  903 */         rate_offset = Inlines.MAX32(rate_offset, -2 * this.silk_mode.bitRate / 3);
/*      */         
/*  905 */         if (this.bandwidth == OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND || this.bandwidth == OpusBandwidth.OPUS_BANDWIDTH_FULLBAND) {
/*  906 */           this.silk_mode.bitRate += 3 * rate_offset / 5;
/*      */         } else {
/*  908 */           this.silk_mode.bitRate += rate_offset;
/*      */         } 
/*  910 */         bytes_target += rate_offset * frame_size / 8 * this.Fs;
/*      */       } 
/*      */       
/*  913 */       this.silk_mode.payloadSize_ms = 1000 * frame_size / this.Fs;
/*  914 */       this.silk_mode.nChannelsAPI = this.channels;
/*  915 */       this.silk_mode.nChannelsInternal = this.stream_channels;
/*  916 */       if (curr_bandwidth == OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND) {
/*  917 */         this.silk_mode.desiredInternalSampleRate = 8000;
/*  918 */       } else if (curr_bandwidth == OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND) {
/*  919 */         this.silk_mode.desiredInternalSampleRate = 12000;
/*      */       } else {
/*  921 */         Inlines.OpusAssert((this.mode == OpusMode.MODE_HYBRID || curr_bandwidth == OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND));
/*  922 */         this.silk_mode.desiredInternalSampleRate = 16000;
/*      */       } 
/*  924 */       if (this.mode == OpusMode.MODE_HYBRID) {
/*      */         
/*  926 */         this.silk_mode.minInternalSampleRate = 16000;
/*      */       } else {
/*  928 */         this.silk_mode.minInternalSampleRate = 8000;
/*      */       } 
/*      */       
/*  931 */       if (this.mode == OpusMode.MODE_SILK_ONLY) {
/*  932 */         int effective_max_rate = max_rate;
/*  933 */         this.silk_mode.maxInternalSampleRate = 16000;
/*  934 */         if (frame_rate > 50) {
/*  935 */           effective_max_rate = effective_max_rate * 2 / 3;
/*      */         }
/*  937 */         if (effective_max_rate < 13000) {
/*  938 */           this.silk_mode.maxInternalSampleRate = 12000;
/*  939 */           this.silk_mode.desiredInternalSampleRate = Inlines.IMIN(12000, this.silk_mode.desiredInternalSampleRate);
/*      */         } 
/*  941 */         if (effective_max_rate < 9600) {
/*  942 */           this.silk_mode.maxInternalSampleRate = 8000;
/*  943 */           this.silk_mode.desiredInternalSampleRate = Inlines.IMIN(8000, this.silk_mode.desiredInternalSampleRate);
/*      */         } 
/*      */       } else {
/*  946 */         this.silk_mode.maxInternalSampleRate = 16000;
/*      */       } 
/*      */       
/*  949 */       this.silk_mode.useCBR = (this.use_vbr == 0) ? 1 : 0;
/*      */ 
/*      */       
/*  952 */       int nBytes = Inlines.IMIN(1275, max_data_bytes - 1 - redundancy_bytes);
/*      */       
/*  954 */       this.silk_mode.maxBits = nBytes * 8;
/*      */       
/*  956 */       if (this.mode == OpusMode.MODE_HYBRID) {
/*  957 */         this.silk_mode.maxBits = this.silk_mode.maxBits * 9 / 10;
/*      */       }
/*  959 */       if (this.silk_mode.useCBR != 0) {
/*  960 */         this.silk_mode.maxBits = this.silk_mode.bitRate * frame_size / this.Fs * 8 * 8;
/*      */         
/*  962 */         this.silk_mode.bitRate = Inlines.IMAX(1, this.silk_mode.bitRate - 2000);
/*      */       } 
/*      */       
/*  965 */       if (prefill != 0) {
/*  966 */         BoxedValueInt zero = new BoxedValueInt(0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  975 */         int prefill_offset = this.channels * (this.encoder_buffer - this.delay_compensation - this.Fs / 400);
/*  976 */         CodecHelpers.gain_fade(this.delay_buffer, prefill_offset, 0, 32767, celt_mode.overlap, this.Fs / 400, this.channels, celt_mode.window, this.Fs);
/*      */         
/*  978 */         Arrays.MemSet(this.delay_buffer, (short)0, prefill_offset);
/*  979 */         System.arraycopy(this.delay_buffer, 0, pcm_silk, 0, this.encoder_buffer * this.channels);
/*      */         
/*  981 */         EncodeAPI.silk_Encode(silk_enc, this.silk_mode, pcm_silk, this.encoder_buffer, null, zero, 1);
/*      */       } 
/*      */       
/*  984 */       System.arraycopy(pcm_buf, total_buffer * this.channels, pcm_silk, 0, frame_size * this.channels);
/*      */       
/*  986 */       BoxedValueInt boxed_silkBytes = new BoxedValueInt(nBytes);
/*  987 */       ret = EncodeAPI.silk_Encode(silk_enc, this.silk_mode, pcm_silk, frame_size, enc, boxed_silkBytes, 0);
/*  988 */       nBytes = boxed_silkBytes.Val;
/*      */       
/*  990 */       if (ret != 0)
/*      */       {
/*      */ 
/*      */         
/*  994 */         return OpusError.OPUS_INTERNAL_ERROR;
/*      */       }
/*  996 */       if (nBytes == 0) {
/*  997 */         this.rangeFinal = 0;
/*  998 */         data[data_ptr - 1] = CodecHelpers.gen_toc(this.mode, this.Fs / frame_size, curr_bandwidth, this.stream_channels);
/*      */         
/* 1000 */         return 1;
/*      */       } 
/*      */       
/* 1003 */       if (this.mode == OpusMode.MODE_SILK_ONLY) {
/* 1004 */         if (this.silk_mode.internalSampleRate == 8000) {
/* 1005 */           curr_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND;
/* 1006 */         } else if (this.silk_mode.internalSampleRate == 12000) {
/* 1007 */           curr_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND;
/* 1008 */         } else if (this.silk_mode.internalSampleRate == 16000) {
/* 1009 */           curr_bandwidth = OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND;
/*      */         } 
/*      */       } else {
/* 1012 */         Inlines.OpusAssert((this.silk_mode.internalSampleRate == 16000));
/*      */       } 
/*      */       
/* 1015 */       this.silk_mode.opusCanSwitch = this.silk_mode.switchReady;
/* 1016 */       if (this.silk_mode.opusCanSwitch != 0) {
/* 1017 */         redundancy = 1;
/* 1018 */         celt_to_silk = 0;
/* 1019 */         this.silk_bw_switch = 1;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1025 */     int endband = 21;
/*      */     
/* 1027 */     switch (curr_bandwidth) {
/*      */       case OPUS_BANDWIDTH_NARROWBAND:
/* 1029 */         endband = 13;
/*      */         break;
/*      */       case OPUS_BANDWIDTH_MEDIUMBAND:
/*      */       case OPUS_BANDWIDTH_WIDEBAND:
/* 1033 */         endband = 17;
/*      */         break;
/*      */       case OPUS_BANDWIDTH_SUPERWIDEBAND:
/* 1036 */         endband = 19;
/*      */         break;
/*      */       case OPUS_BANDWIDTH_FULLBAND:
/* 1039 */         endband = 21;
/*      */         break;
/*      */     } 
/* 1042 */     celt_enc.SetEndBand(endband);
/* 1043 */     celt_enc.SetChannels(this.stream_channels);
/*      */     
/* 1045 */     celt_enc.SetBitrate(-1);
/* 1046 */     if (this.mode != OpusMode.MODE_SILK_ONLY) {
/* 1047 */       int celt_pred = 2;
/* 1048 */       celt_enc.SetVBR(false);
/*      */       
/* 1050 */       if (this.silk_mode.reducedDependency != 0) {
/* 1051 */         celt_pred = 0;
/*      */       }
/* 1053 */       celt_enc.SetPrediction(celt_pred);
/*      */       
/* 1055 */       if (this.mode == OpusMode.MODE_HYBRID) {
/*      */ 
/*      */         
/* 1058 */         int len = enc.tell() + 7 >> 3;
/* 1059 */         if (redundancy != 0) {
/* 1060 */           len += (this.mode == OpusMode.MODE_HYBRID) ? 3 : 1;
/*      */         }
/* 1062 */         if (this.use_vbr != 0) {
/* 1063 */           nb_compr_bytes = len + bytes_target - this.silk_mode.bitRate * frame_size / 8 * this.Fs;
/*      */         } else {
/*      */           
/* 1066 */           nb_compr_bytes = (len > bytes_target) ? len : bytes_target;
/*      */         } 
/* 1068 */       } else if (this.use_vbr != 0) {
/* 1069 */         int bonus = 0;
/* 1070 */         if (this.analysis.enabled && this.variable_duration == OpusFramesize.OPUS_FRAMESIZE_VARIABLE && frame_size != this.Fs / 50) {
/* 1071 */           bonus = (60 * this.stream_channels + 40) * (this.Fs / frame_size - 50);
/* 1072 */           if (analysis_info.valid != 0) {
/* 1073 */             bonus = (int)(bonus * (1.0F + 0.5F * analysis_info.tonality));
/*      */           }
/*      */         } 
/* 1076 */         celt_enc.SetVBR(true);
/* 1077 */         celt_enc.SetVBRConstraint((this.vbr_constraint != 0));
/* 1078 */         celt_enc.SetBitrate(this.bitrate_bps + bonus);
/* 1079 */         nb_compr_bytes = max_data_bytes - 1 - redundancy_bytes;
/*      */       } else {
/* 1081 */         nb_compr_bytes = bytes_target;
/*      */       } 
/*      */     } else {
/*      */       
/* 1085 */       nb_compr_bytes = 0;
/*      */     } 
/*      */     
/* 1088 */     short[] tmp_prefill = new short[this.channels * this.Fs / 400];
/* 1089 */     if (this.mode != OpusMode.MODE_SILK_ONLY && this.mode != this.prev_mode && this.prev_mode != OpusMode.MODE_AUTO && this.prev_mode != OpusMode.MODE_UNKNOWN) {
/* 1090 */       System.arraycopy(this.delay_buffer, (this.encoder_buffer - total_buffer - this.Fs / 400) * this.channels, tmp_prefill, 0, this.channels * this.Fs / 400);
/*      */     }
/*      */     
/* 1093 */     if (this.channels * (this.encoder_buffer - frame_size + total_buffer) > 0) {
/* 1094 */       Arrays.MemMove(this.delay_buffer, this.channels * frame_size, 0, this.channels * (this.encoder_buffer - frame_size - total_buffer));
/* 1095 */       System.arraycopy(pcm_buf, 0, this.delay_buffer, this.channels * (this.encoder_buffer - frame_size - total_buffer), (frame_size + total_buffer) * this.channels);
/*      */     } else {
/* 1097 */       System.arraycopy(pcm_buf, (frame_size + total_buffer - this.encoder_buffer) * this.channels, this.delay_buffer, 0, this.encoder_buffer * this.channels);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1102 */     if (this.prev_HB_gain < 32767 || HB_gain < 32767) {
/* 1103 */       CodecHelpers.gain_fade(pcm_buf, 0, this.prev_HB_gain, HB_gain, celt_mode.overlap, frame_size, this.channels, celt_mode.window, this.Fs);
/*      */     }
/*      */ 
/*      */     
/* 1107 */     this.prev_HB_gain = HB_gain;
/* 1108 */     if (this.mode != OpusMode.MODE_HYBRID || this.stream_channels == 1) {
/* 1109 */       this.silk_mode.stereoWidth_Q14 = Inlines.IMIN(16384, 2 * Inlines.IMAX(0, equiv_rate - 30000));
/*      */     }
/* 1111 */     if (this.energy_masking == null && this.channels == 2)
/*      */     {
/* 1113 */       if (this.hybrid_stereo_width_Q14 < 16384 || this.silk_mode.stereoWidth_Q14 < 16384) {
/*      */         
/* 1115 */         int g1 = this.hybrid_stereo_width_Q14;
/* 1116 */         int g2 = this.silk_mode.stereoWidth_Q14;
/* 1117 */         g1 = (g1 == 16384) ? 32767 : Inlines.SHL16(g1, 1);
/* 1118 */         g2 = (g2 == 16384) ? 32767 : Inlines.SHL16(g2, 1);
/* 1119 */         CodecHelpers.stereo_fade(pcm_buf, g1, g2, celt_mode.overlap, frame_size, this.channels, celt_mode.window, this.Fs);
/*      */         
/* 1121 */         this.hybrid_stereo_width_Q14 = (short)this.silk_mode.stereoWidth_Q14;
/*      */       } 
/*      */     }
/*      */     
/* 1125 */     if (this.mode != OpusMode.MODE_CELT_ONLY && enc.tell() + 17 + 20 * ((this.mode == OpusMode.MODE_HYBRID) ? 1 : 0) <= 8 * (max_data_bytes - 1)) {
/*      */       
/* 1127 */       if (this.mode == OpusMode.MODE_HYBRID && (redundancy != 0 || enc.tell() + 37 <= 8 * nb_compr_bytes)) {
/* 1128 */         enc.enc_bit_logp(redundancy, 12);
/*      */       }
/* 1130 */       if (redundancy != 0) {
/*      */         int max_redundancy;
/* 1132 */         enc.enc_bit_logp(celt_to_silk, 1);
/* 1133 */         if (this.mode == OpusMode.MODE_HYBRID) {
/* 1134 */           max_redundancy = max_data_bytes - 1 - nb_compr_bytes;
/*      */         } else {
/* 1136 */           max_redundancy = max_data_bytes - 1 - (enc.tell() + 7 >> 3);
/*      */         } 
/*      */ 
/*      */         
/* 1140 */         redundancy_bytes = Inlines.IMIN(max_redundancy, this.bitrate_bps / 1600);
/* 1141 */         redundancy_bytes = Inlines.IMIN(257, Inlines.IMAX(2, redundancy_bytes));
/* 1142 */         if (this.mode == OpusMode.MODE_HYBRID) {
/* 1143 */           enc.enc_uint((redundancy_bytes - 2), 256L);
/*      */         }
/*      */       } 
/*      */     } else {
/* 1147 */       redundancy = 0;
/*      */     } 
/*      */     
/* 1150 */     if (redundancy == 0) {
/* 1151 */       this.silk_bw_switch = 0;
/* 1152 */       redundancy_bytes = 0;
/*      */     } 
/* 1154 */     if (this.mode != OpusMode.MODE_CELT_ONLY) {
/* 1155 */       start_band = 17;
/*      */     }
/*      */     
/* 1158 */     if (this.mode == OpusMode.MODE_SILK_ONLY) {
/* 1159 */       ret = enc.tell() + 7 >> 3;
/* 1160 */       enc.enc_done();
/* 1161 */       nb_compr_bytes = ret;
/*      */     } else {
/* 1163 */       nb_compr_bytes = Inlines.IMIN(max_data_bytes - 1 - redundancy_bytes, nb_compr_bytes);
/* 1164 */       enc.enc_shrink(nb_compr_bytes);
/*      */     } 
/*      */     
/* 1167 */     if ((this.analysis.enabled && redundancy != 0) || this.mode != OpusMode.MODE_SILK_ONLY) {
/* 1168 */       analysis_info.enabled = this.analysis.enabled;
/* 1169 */       celt_enc.SetAnalysis(analysis_info);
/*      */     } 
/*      */     
/* 1172 */     if (redundancy != 0 && celt_to_silk != 0) {
/*      */       
/* 1174 */       celt_enc.SetStartBand(0);
/* 1175 */       celt_enc.SetVBR(false);
/* 1176 */       int err = celt_enc.celt_encode_with_ec(pcm_buf, 0, this.Fs / 200, data, data_ptr + nb_compr_bytes, redundancy_bytes, null);
/* 1177 */       if (err < 0) {
/* 1178 */         return OpusError.OPUS_INTERNAL_ERROR;
/*      */       }
/* 1180 */       redundant_rng = celt_enc.GetFinalRange();
/* 1181 */       celt_enc.ResetState();
/*      */     } 
/*      */     
/* 1184 */     celt_enc.SetStartBand(start_band);
/*      */     
/* 1186 */     if (this.mode != OpusMode.MODE_SILK_ONLY) {
/* 1187 */       if (this.mode != this.prev_mode && this.prev_mode != OpusMode.MODE_AUTO && this.prev_mode != OpusMode.MODE_UNKNOWN) {
/* 1188 */         byte[] dummy = new byte[2];
/* 1189 */         celt_enc.ResetState();
/*      */ 
/*      */         
/* 1192 */         celt_enc.celt_encode_with_ec(tmp_prefill, 0, this.Fs / 400, dummy, 0, 2, null);
/* 1193 */         celt_enc.SetPrediction(0);
/*      */       } 
/*      */       
/* 1196 */       if (enc.tell() <= 8 * nb_compr_bytes) {
/* 1197 */         ret = celt_enc.celt_encode_with_ec(pcm_buf, 0, frame_size, null, 0, nb_compr_bytes, enc);
/* 1198 */         if (ret < 0) {
/* 1199 */           return OpusError.OPUS_INTERNAL_ERROR;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1205 */     if (redundancy != 0 && celt_to_silk == 0) {
/*      */       
/* 1207 */       byte[] dummy = new byte[2];
/*      */       
/* 1209 */       int N2 = this.Fs / 200;
/* 1210 */       int N4 = this.Fs / 400;
/*      */       
/* 1212 */       celt_enc.ResetState();
/* 1213 */       celt_enc.SetStartBand(0);
/* 1214 */       celt_enc.SetPrediction(0);
/*      */ 
/*      */       
/* 1217 */       celt_enc.celt_encode_with_ec(pcm_buf, this.channels * (frame_size - N2 - N4), N4, dummy, 0, 2, null);
/*      */       
/* 1219 */       int err = celt_enc.celt_encode_with_ec(pcm_buf, this.channels * (frame_size - N2), N2, data, data_ptr + nb_compr_bytes, redundancy_bytes, null);
/* 1220 */       if (err < 0) {
/* 1221 */         return OpusError.OPUS_INTERNAL_ERROR;
/*      */       }
/* 1223 */       redundant_rng = celt_enc.GetFinalRange();
/*      */     } 
/*      */ 
/*      */     
/* 1227 */     data_ptr--;
/* 1228 */     data[data_ptr] = CodecHelpers.gen_toc(this.mode, this.Fs / frame_size, curr_bandwidth, this.stream_channels);
/*      */     
/* 1230 */     this.rangeFinal = (int)enc.rng ^ redundant_rng;
/*      */     
/* 1232 */     if (to_celt != 0) {
/* 1233 */       this.prev_mode = OpusMode.MODE_CELT_ONLY;
/*      */     } else {
/* 1235 */       this.prev_mode = this.mode;
/*      */     } 
/* 1237 */     this.prev_channels = this.stream_channels;
/* 1238 */     this.prev_framesize = frame_size;
/*      */     
/* 1240 */     this.first = 0;
/*      */ 
/*      */ 
/*      */     
/* 1244 */     if (enc.tell() > (max_data_bytes - 1) * 8) {
/* 1245 */       if (max_data_bytes < 2) {
/* 1246 */         return OpusError.OPUS_BUFFER_TOO_SMALL;
/*      */       }
/* 1248 */       data[data_ptr + 1] = 0;
/* 1249 */       ret = 1;
/* 1250 */       this.rangeFinal = 0;
/* 1251 */     } else if (this.mode == OpusMode.MODE_SILK_ONLY && redundancy == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1258 */       while (ret > 2 && data[data_ptr + ret] == 0) {
/* 1259 */         ret--;
/*      */       }
/*      */     } 
/*      */     
/* 1263 */     ret += 1 + redundancy_bytes;
/* 1264 */     if (this.use_vbr == 0) {
/* 1265 */       if (OpusRepacketizer.padPacket(data, data_ptr, ret, max_data_bytes) != OpusError.OPUS_OK) {
/* 1266 */         return OpusError.OPUS_INTERNAL_ERROR;
/*      */       }
/* 1268 */       ret = max_data_bytes;
/*      */     } 
/*      */     
/* 1271 */     return ret;
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
/*      */   public int encode(short[] in_pcm, int pcm_offset, int frame_size, byte[] out_data, int out_data_offset, int max_data_bytes) throws OpusException {
/*      */     int delay_compensation;
/* 1291 */     if (out_data_offset + max_data_bytes > out_data.length) {
/* 1292 */       throw new IllegalArgumentException("Output buffer is too small: Stated size is " + max_data_bytes + " bytes, actual size is " + (out_data.length - out_data_offset) + " bytes");
/*      */     }
/*      */ 
/*      */     
/* 1296 */     if (this.application == OpusApplication.OPUS_APPLICATION_RESTRICTED_LOWDELAY) {
/* 1297 */       delay_compensation = 0;
/*      */     } else {
/* 1299 */       delay_compensation = this.delay_compensation;
/*      */     } 
/*      */     
/* 1302 */     int internal_frame_size = CodecHelpers.compute_frame_size(in_pcm, pcm_offset, frame_size, this.variable_duration, this.channels, this.Fs, this.bitrate_bps, delay_compensation, this.analysis.subframe_mem, this.analysis.enabled);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1307 */     if (pcm_offset + internal_frame_size > in_pcm.length) {
/* 1308 */       throw new IllegalArgumentException("Not enough samples provided in input signal: Expected " + internal_frame_size + " samples, found " + (in_pcm.length - pcm_offset));
/*      */     }
/*      */     
/*      */     try {
/* 1312 */       int ret = opus_encode_native(in_pcm, pcm_offset, internal_frame_size, out_data, out_data_offset, max_data_bytes, 16, in_pcm, pcm_offset, frame_size, 0, -2, this.channels, 0);
/*      */ 
/*      */       
/* 1315 */       if (ret < 0) {
/*      */         
/* 1317 */         if (ret == OpusError.OPUS_BAD_ARG) {
/* 1318 */           throw new IllegalArgumentException("OPUS_BAD_ARG while encoding");
/*      */         }
/* 1320 */         throw new OpusException("An error occurred during encoding", ret);
/*      */       } 
/*      */       
/* 1323 */       return ret;
/* 1324 */     } catch (ArithmeticException e) {
/* 1325 */       throw new OpusException("Internal error during encoding: " + e.getMessage());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int encode(byte[] in_pcm, int pcm_offset, int frame_size, byte[] out_data, int out_data_offset, int max_data_bytes) throws OpusException {
/* 1346 */     short[] spcm = new short[frame_size * this.channels];
/* 1347 */     for (int c = 0, idx = pcm_offset; c < spcm.length; idx += 2, c++) {
/* 1348 */       spcm[c] = (short)((in_pcm[idx] & 0xFF | in_pcm[idx + 1] << 8) & 0xFFFF);
/*      */     }
/* 1350 */     return encode(spcm, 0, frame_size, out_data, out_data_offset, max_data_bytes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OpusApplication getApplication() {
/* 1359 */     return this.application;
/*      */   }
/*      */   
/*      */   public void setApplication(OpusApplication value) {
/* 1363 */     if (this.first == 0 && this.application != value) {
/* 1364 */       throw new IllegalArgumentException("Application cannot be changed after encoding has started");
/*      */     }
/* 1366 */     this.application = value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getBitrate() {
/* 1373 */     return user_bitrate_to_bitrate(this.prev_framesize, 1276);
/*      */   }
/*      */   
/*      */   public void setBitrate(int value) {
/* 1377 */     if (value != -1000 && value != -1) {
/* 1378 */       if (value <= 0)
/* 1379 */         throw new IllegalArgumentException("Bitrate must be positive"); 
/* 1380 */       if (value <= 500) {
/* 1381 */         value = 500;
/* 1382 */       } else if (value > 300000 * this.channels) {
/* 1383 */         value = 300000 * this.channels;
/*      */       } 
/*      */     } 
/*      */     
/* 1387 */     this.user_bitrate_bps = value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getForceChannels() {
/* 1395 */     return this.force_channels;
/*      */   }
/*      */   
/*      */   public void setForceChannels(int value) {
/* 1399 */     if ((value < 1 || value > this.channels) && value != -1000) {
/* 1400 */       throw new IllegalArgumentException("Force channels must be <= num. of channels");
/*      */     }
/*      */     
/* 1403 */     this.force_channels = value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OpusBandwidth getMaxBandwidth() {
/* 1410 */     return this.max_bandwidth;
/*      */   }
/*      */   
/*      */   public void setMaxBandwidth(OpusBandwidth value) {
/* 1414 */     this.max_bandwidth = value;
/* 1415 */     if (this.max_bandwidth == OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND) {
/* 1416 */       this.silk_mode.maxInternalSampleRate = 8000;
/* 1417 */     } else if (this.max_bandwidth == OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND) {
/* 1418 */       this.silk_mode.maxInternalSampleRate = 12000;
/*      */     } else {
/* 1420 */       this.silk_mode.maxInternalSampleRate = 16000;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OpusBandwidth getBandwidth() {
/* 1428 */     return this.bandwidth;
/*      */   }
/*      */   
/*      */   public void setBandwidth(OpusBandwidth value) {
/* 1432 */     this.user_bandwidth = value;
/* 1433 */     if (this.user_bandwidth == OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND) {
/* 1434 */       this.silk_mode.maxInternalSampleRate = 8000;
/* 1435 */     } else if (this.user_bandwidth == OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND) {
/* 1436 */       this.silk_mode.maxInternalSampleRate = 12000;
/*      */     } else {
/* 1438 */       this.silk_mode.maxInternalSampleRate = 16000;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseDTX() {
/* 1448 */     return (this.silk_mode.useDTX != 0);
/*      */   }
/*      */   
/*      */   public void setUseDTX(boolean value) {
/* 1452 */     this.silk_mode.useDTX = value ? 1 : 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getComplexity() {
/* 1459 */     return this.silk_mode.complexity;
/*      */   }
/*      */   
/*      */   public void setComplexity(int value) {
/* 1463 */     if (value < 0 || value > 10) {
/* 1464 */       throw new IllegalArgumentException("Complexity must be between 0 and 10");
/*      */     }
/* 1466 */     this.silk_mode.complexity = value;
/* 1467 */     this.Celt_Encoder.SetComplexity(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseInbandFEC() {
/* 1476 */     return (this.silk_mode.useInBandFEC != 0);
/*      */   }
/*      */   
/*      */   public void setUseInbandFEC(boolean value) {
/* 1480 */     this.silk_mode.useInBandFEC = value ? 1 : 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPacketLossPercent() {
/* 1488 */     return this.silk_mode.packetLossPercentage;
/*      */   }
/*      */   
/*      */   public void setPacketLossPercent(int value) {
/* 1492 */     if (value < 0 || value > 100) {
/* 1493 */       throw new IllegalArgumentException("Packet loss must be between 0 and 100");
/*      */     }
/* 1495 */     this.silk_mode.packetLossPercentage = value;
/* 1496 */     this.Celt_Encoder.SetPacketLossPercent(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseVBR() {
/* 1504 */     return (this.use_vbr != 0);
/*      */   }
/*      */   
/*      */   public void setUseVBR(boolean value) {
/* 1508 */     this.use_vbr = value ? 1 : 0;
/* 1509 */     this.silk_mode.useCBR = value ? 0 : 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseConstrainedVBR() {
/* 1516 */     return (this.vbr_constraint != 0);
/*      */   }
/*      */   
/*      */   public void setUseConstrainedVBR(boolean value) {
/* 1520 */     this.vbr_constraint = value ? 1 : 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OpusSignal getSignalType() {
/* 1527 */     return this.signal_type;
/*      */   }
/*      */   
/*      */   public void setSignalType(OpusSignal value) {
/* 1531 */     this.signal_type = value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLookahead() {
/* 1538 */     int returnVal = this.Fs / 400;
/* 1539 */     if (this.application != OpusApplication.OPUS_APPLICATION_RESTRICTED_LOWDELAY) {
/* 1540 */       returnVal += this.delay_compensation;
/*      */     }
/*      */     
/* 1543 */     return returnVal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSampleRate() {
/* 1550 */     return this.Fs;
/*      */   }
/*      */   
/*      */   public int getFinalRange() {
/* 1554 */     return this.rangeFinal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLSBDepth() {
/* 1562 */     return this.lsb_depth;
/*      */   }
/*      */   
/*      */   public void setLSBDepth(int value) {
/* 1566 */     if (value < 8 || value > 24) {
/* 1567 */       throw new IllegalArgumentException("LSB depth must be between 8 and 24");
/*      */     }
/*      */     
/* 1570 */     this.lsb_depth = value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OpusFramesize getExpertFrameDuration() {
/* 1578 */     return this.variable_duration;
/*      */   }
/*      */   
/*      */   public void setExpertFrameDuration(OpusFramesize value) {
/* 1582 */     this.variable_duration = value;
/* 1583 */     this.Celt_Encoder.SetExpertFrameDuration(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OpusMode getForceMode() {
/* 1593 */     return this.user_forced_mode;
/*      */   }
/*      */   
/*      */   public void setForceMode(OpusMode value) {
/* 1597 */     this.user_forced_mode = value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getIsLFE() {
/* 1604 */     return (this.lfe != 0);
/*      */   }
/*      */   
/*      */   public void setIsLFE(boolean value) {
/* 1608 */     this.lfe = value ? 1 : 0;
/* 1609 */     this.Celt_Encoder.SetLFE(value ? 1 : 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getPredictionDisabled() {
/* 1616 */     return (this.silk_mode.reducedDependency != 0);
/*      */   }
/*      */   
/*      */   public void setPredictionDisabled(boolean value) {
/* 1620 */     this.silk_mode.reducedDependency = value ? 1 : 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getEnableAnalysis() {
/* 1628 */     return this.analysis.enabled;
/*      */   }
/*      */   
/*      */   public void setEnableAnalysis(boolean value) {
/* 1632 */     this.analysis.enabled = value;
/*      */   }
/*      */   
/*      */   void SetEnergyMask(int[] value) {
/* 1636 */     this.energy_masking = value;
/* 1637 */     this.Celt_Encoder.SetEnergyMask(value);
/*      */   }
/*      */   
/*      */   CeltMode GetCeltMode() {
/* 1641 */     return this.Celt_Encoder.GetMode();
/*      */   }
/*      */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\OpusEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */