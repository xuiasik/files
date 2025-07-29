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
/*      */ class SilkChannelEncoder
/*      */ {
/*   39 */   final int[] In_HP_State = new int[2];
/*      */   
/*   41 */   int variable_HP_smth1_Q15 = 0;
/*      */   
/*   43 */   int variable_HP_smth2_Q15 = 0;
/*      */   
/*   45 */   final SilkLPState sLP = new SilkLPState();
/*      */   
/*   47 */   final SilkVADState sVAD = new SilkVADState();
/*      */   
/*   49 */   final SilkNSQState sNSQ = new SilkNSQState();
/*      */   
/*   51 */   final short[] prev_NLSFq_Q15 = new short[16];
/*      */   
/*   53 */   int speech_activity_Q8 = 0;
/*      */   
/*   55 */   int allow_bandwidth_switch = 0;
/*      */   
/*   57 */   byte LBRRprevLastGainIndex = 0;
/*   58 */   byte prevSignalType = 0;
/*   59 */   int prevLag = 0;
/*   60 */   int pitch_LPC_win_length = 0;
/*   61 */   int max_pitch_lag = 0;
/*      */   
/*   63 */   int API_fs_Hz = 0;
/*      */   
/*   65 */   int prev_API_fs_Hz = 0;
/*      */   
/*   67 */   int maxInternal_fs_Hz = 0;
/*      */   
/*   69 */   int minInternal_fs_Hz = 0;
/*      */   
/*   71 */   int desiredInternal_fs_Hz = 0;
/*      */   
/*   73 */   int fs_kHz = 0;
/*      */   
/*   75 */   int nb_subfr = 0;
/*      */   
/*   77 */   int frame_length = 0;
/*      */   
/*   79 */   int subfr_length = 0;
/*      */   
/*   81 */   int ltp_mem_length = 0;
/*      */   
/*   83 */   int la_pitch = 0;
/*      */   
/*   85 */   int la_shape = 0;
/*      */   
/*   87 */   int shapeWinLength = 0;
/*      */   
/*   89 */   int TargetRate_bps = 0;
/*      */   
/*   91 */   int PacketSize_ms = 0;
/*      */   
/*   93 */   int PacketLoss_perc = 0;
/*      */   
/*   95 */   int frameCounter = 0;
/*   96 */   int Complexity = 0;
/*      */   
/*   98 */   int nStatesDelayedDecision = 0;
/*      */   
/*  100 */   int useInterpolatedNLSFs = 0;
/*      */   
/*  102 */   int shapingLPCOrder = 0;
/*      */   
/*  104 */   int predictLPCOrder = 0;
/*      */   
/*  106 */   int pitchEstimationComplexity = 0;
/*      */   
/*  108 */   int pitchEstimationLPCOrder = 0;
/*      */   
/*  110 */   int pitchEstimationThreshold_Q16 = 0;
/*      */   
/*  112 */   int LTPQuantLowComplexity = 0;
/*      */   
/*  114 */   int mu_LTP_Q9 = 0;
/*      */   
/*  116 */   int sum_log_gain_Q7 = 0;
/*      */   
/*  118 */   int NLSF_MSVQ_Survivors = 0;
/*      */   
/*  120 */   int first_frame_after_reset = 0;
/*      */   
/*  122 */   int controlled_since_last_payload = 0;
/*      */   
/*  124 */   int warping_Q16 = 0;
/*      */   
/*  126 */   int useCBR = 0;
/*      */   
/*  128 */   int prefillFlag = 0;
/*      */   
/*  130 */   short[] pitch_lag_low_bits_iCDF = null;
/*      */   
/*  132 */   short[] pitch_contour_iCDF = null;
/*      */   
/*  134 */   NLSFCodebook psNLSF_CB = null;
/*      */   
/*  136 */   final int[] input_quality_bands_Q15 = new int[4];
/*  137 */   int input_tilt_Q15 = 0;
/*  138 */   int SNR_dB_Q7 = 0;
/*      */ 
/*      */   
/*  141 */   final byte[] VAD_flags = new byte[3];
/*  142 */   byte LBRR_flag = 0;
/*  143 */   final int[] LBRR_flags = new int[3];
/*      */   
/*  145 */   final SideInfoIndices indices = new SideInfoIndices();
/*  146 */   final byte[] pulses = new byte[320];
/*      */ 
/*      */   
/*  149 */   final short[] inputBuf = new short[322];
/*      */   
/*  151 */   int inputBufIx = 0;
/*  152 */   int nFramesPerPacket = 0;
/*  153 */   int nFramesEncoded = 0;
/*      */ 
/*      */   
/*  156 */   int nChannelsAPI = 0;
/*  157 */   int nChannelsInternal = 0;
/*  158 */   int channelNb = 0;
/*      */ 
/*      */   
/*  161 */   int frames_since_onset = 0;
/*      */ 
/*      */   
/*  164 */   int ec_prevSignalType = 0;
/*  165 */   short ec_prevLagIndex = 0;
/*      */   
/*  167 */   final SilkResamplerState resampler_state = new SilkResamplerState();
/*      */ 
/*      */   
/*  170 */   int useDTX = 0;
/*      */   
/*  172 */   int inDTX = 0;
/*      */   
/*  174 */   int noSpeechCounter = 0;
/*      */ 
/*      */ 
/*      */   
/*  178 */   int useInBandFEC = 0;
/*      */   
/*  180 */   int LBRR_enabled = 0;
/*      */   
/*  182 */   int LBRR_GainIncreases = 0;
/*      */   
/*  184 */   final SideInfoIndices[] indices_LBRR = new SideInfoIndices[3];
/*  185 */   final byte[][] pulses_LBRR = Arrays.InitTwoDimensionalArrayByte(3, 320);
/*      */ 
/*      */   
/*  188 */   final SilkShapeState sShape = new SilkShapeState();
/*      */ 
/*      */   
/*  191 */   final SilkPrefilterState sPrefilt = new SilkPrefilterState();
/*      */ 
/*      */   
/*  194 */   final short[] x_buf = new short[720];
/*      */ 
/*      */   
/*  197 */   int LTPCorr_Q15 = 0;
/*      */   
/*      */   SilkChannelEncoder() {
/*  200 */     for (int c = 0; c < 3; c++) {
/*  201 */       this.indices_LBRR[c] = new SideInfoIndices();
/*      */     }
/*      */   }
/*      */   
/*      */   void Reset() {
/*  206 */     Arrays.MemSet(this.In_HP_State, 0, 2);
/*  207 */     this.variable_HP_smth1_Q15 = 0;
/*  208 */     this.variable_HP_smth2_Q15 = 0;
/*  209 */     this.sLP.Reset();
/*  210 */     this.sVAD.Reset();
/*  211 */     this.sNSQ.Reset();
/*  212 */     Arrays.MemSet(this.prev_NLSFq_Q15, (short)0, 16);
/*  213 */     this.speech_activity_Q8 = 0;
/*  214 */     this.allow_bandwidth_switch = 0;
/*  215 */     this.LBRRprevLastGainIndex = 0;
/*  216 */     this.prevSignalType = 0;
/*  217 */     this.prevLag = 0;
/*  218 */     this.pitch_LPC_win_length = 0;
/*  219 */     this.max_pitch_lag = 0;
/*  220 */     this.API_fs_Hz = 0;
/*  221 */     this.prev_API_fs_Hz = 0;
/*  222 */     this.maxInternal_fs_Hz = 0;
/*  223 */     this.minInternal_fs_Hz = 0;
/*  224 */     this.desiredInternal_fs_Hz = 0;
/*  225 */     this.fs_kHz = 0;
/*  226 */     this.nb_subfr = 0;
/*  227 */     this.frame_length = 0;
/*  228 */     this.subfr_length = 0;
/*  229 */     this.ltp_mem_length = 0;
/*  230 */     this.la_pitch = 0;
/*  231 */     this.la_shape = 0;
/*  232 */     this.shapeWinLength = 0;
/*  233 */     this.TargetRate_bps = 0;
/*  234 */     this.PacketSize_ms = 0;
/*  235 */     this.PacketLoss_perc = 0;
/*  236 */     this.frameCounter = 0;
/*  237 */     this.Complexity = 0;
/*  238 */     this.nStatesDelayedDecision = 0;
/*  239 */     this.useInterpolatedNLSFs = 0;
/*  240 */     this.shapingLPCOrder = 0;
/*  241 */     this.predictLPCOrder = 0;
/*  242 */     this.pitchEstimationComplexity = 0;
/*  243 */     this.pitchEstimationLPCOrder = 0;
/*  244 */     this.pitchEstimationThreshold_Q16 = 0;
/*  245 */     this.LTPQuantLowComplexity = 0;
/*  246 */     this.mu_LTP_Q9 = 0;
/*  247 */     this.sum_log_gain_Q7 = 0;
/*  248 */     this.NLSF_MSVQ_Survivors = 0;
/*  249 */     this.first_frame_after_reset = 0;
/*  250 */     this.controlled_since_last_payload = 0;
/*  251 */     this.warping_Q16 = 0;
/*  252 */     this.useCBR = 0;
/*  253 */     this.prefillFlag = 0;
/*  254 */     this.pitch_lag_low_bits_iCDF = null;
/*  255 */     this.pitch_contour_iCDF = null;
/*  256 */     this.psNLSF_CB = null;
/*  257 */     Arrays.MemSet(this.input_quality_bands_Q15, 0, 4);
/*  258 */     this.input_tilt_Q15 = 0;
/*  259 */     this.SNR_dB_Q7 = 0;
/*  260 */     Arrays.MemSet(this.VAD_flags, (byte)0, 3);
/*  261 */     this.LBRR_flag = 0;
/*  262 */     Arrays.MemSet(this.LBRR_flags, 0, 3);
/*  263 */     this.indices.Reset();
/*  264 */     Arrays.MemSet(this.pulses, (byte)0, 320);
/*  265 */     Arrays.MemSet(this.inputBuf, (short)0, 322);
/*  266 */     this.inputBufIx = 0;
/*  267 */     this.nFramesPerPacket = 0;
/*  268 */     this.nFramesEncoded = 0;
/*  269 */     this.nChannelsAPI = 0;
/*  270 */     this.nChannelsInternal = 0;
/*  271 */     this.channelNb = 0;
/*  272 */     this.frames_since_onset = 0;
/*  273 */     this.ec_prevSignalType = 0;
/*  274 */     this.ec_prevLagIndex = 0;
/*  275 */     this.resampler_state.Reset();
/*  276 */     this.useDTX = 0;
/*  277 */     this.inDTX = 0;
/*  278 */     this.noSpeechCounter = 0;
/*  279 */     this.useInBandFEC = 0;
/*  280 */     this.LBRR_enabled = 0;
/*  281 */     this.LBRR_GainIncreases = 0;
/*  282 */     for (int c = 0; c < 3; c++) {
/*  283 */       this.indices_LBRR[c].Reset();
/*  284 */       Arrays.MemSet(this.pulses_LBRR[c], (byte)0, 320);
/*      */     } 
/*  286 */     this.sShape.Reset();
/*  287 */     this.sPrefilt.Reset();
/*  288 */     Arrays.MemSet(this.x_buf, (short)0, 720);
/*  289 */     this.LTPCorr_Q15 = 0;
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
/*      */   int silk_control_encoder(EncControlState encControl, int TargetRate_bps, int allow_bw_switch, int channelNb, int force_fs_kHz) {
/*  309 */     int ret = SilkError.SILK_NO_ERROR;
/*      */     
/*  311 */     this.useDTX = encControl.useDTX;
/*  312 */     this.useCBR = encControl.useCBR;
/*  313 */     this.API_fs_Hz = encControl.API_sampleRate;
/*  314 */     this.maxInternal_fs_Hz = encControl.maxInternalSampleRate;
/*  315 */     this.minInternal_fs_Hz = encControl.minInternalSampleRate;
/*  316 */     this.desiredInternal_fs_Hz = encControl.desiredInternalSampleRate;
/*  317 */     this.useInBandFEC = encControl.useInBandFEC;
/*  318 */     this.nChannelsAPI = encControl.nChannelsAPI;
/*  319 */     this.nChannelsInternal = encControl.nChannelsInternal;
/*  320 */     this.allow_bandwidth_switch = allow_bw_switch;
/*  321 */     this.channelNb = channelNb;
/*      */     
/*  323 */     if (this.controlled_since_last_payload != 0 && this.prefillFlag == 0) {
/*  324 */       if (this.API_fs_Hz != this.prev_API_fs_Hz && this.fs_kHz > 0)
/*      */       {
/*  326 */         ret = silk_setup_resamplers(this.fs_kHz);
/*      */       }
/*  328 */       return ret;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  339 */     int fs_kHz = silk_control_audio_bandwidth(encControl);
/*  340 */     if (force_fs_kHz != 0) {
/*  341 */       fs_kHz = force_fs_kHz;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  350 */     ret = silk_setup_resamplers(fs_kHz);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  359 */     ret = silk_setup_fs(fs_kHz, encControl.payloadSize_ms);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  368 */     ret = silk_setup_complexity(encControl.complexity);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  377 */     this.PacketLoss_perc = encControl.packetLossPercentage;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  386 */     ret = silk_setup_LBRR(TargetRate_bps);
/*      */     
/*  388 */     this.controlled_since_last_payload = 1;
/*      */     
/*  390 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int silk_setup_resamplers(int fs_kHz) {
/*  400 */     int ret = 0;
/*      */     
/*  402 */     if (this.fs_kHz != fs_kHz || this.prev_API_fs_Hz != this.API_fs_Hz) {
/*  403 */       if (this.fs_kHz == 0) {
/*      */         
/*  405 */         ret += Resampler.silk_resampler_init(this.resampler_state, this.API_fs_Hz, fs_kHz * 1000, 1);
/*      */       } else {
/*      */         
/*  408 */         SilkResamplerState temp_resampler_state = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  414 */         int buf_length_ms = Inlines.silk_LSHIFT(this.nb_subfr * 5, 1) + 5;
/*  415 */         int old_buf_samples = buf_length_ms * this.fs_kHz;
/*      */ 
/*      */         
/*  418 */         temp_resampler_state = new SilkResamplerState();
/*  419 */         ret += Resampler.silk_resampler_init(temp_resampler_state, Inlines.silk_SMULBB(this.fs_kHz, 1000), this.API_fs_Hz, 0);
/*      */ 
/*      */         
/*  422 */         int api_buf_samples = buf_length_ms * Inlines.silk_DIV32_16(this.API_fs_Hz, 1000);
/*      */ 
/*      */         
/*  425 */         short[] x_buf_API_fs_Hz = new short[api_buf_samples];
/*  426 */         ret += Resampler.silk_resampler(temp_resampler_state, x_buf_API_fs_Hz, 0, this.x_buf, 0, old_buf_samples);
/*      */ 
/*      */         
/*  429 */         ret += Resampler.silk_resampler_init(this.resampler_state, this.API_fs_Hz, Inlines.silk_SMULBB(fs_kHz, 1000), 1);
/*      */ 
/*      */         
/*  432 */         ret += Resampler.silk_resampler(this.resampler_state, this.x_buf, 0, x_buf_API_fs_Hz, 0, api_buf_samples);
/*      */       } 
/*      */     }
/*      */     
/*  436 */     this.prev_API_fs_Hz = this.API_fs_Hz;
/*      */     
/*  438 */     return ret;
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
/*      */   private int silk_setup_fs(int fs_kHz, int PacketSize_ms) {
/*  451 */     int ret = SilkError.SILK_NO_ERROR;
/*      */ 
/*      */     
/*  454 */     if (PacketSize_ms != this.PacketSize_ms) {
/*  455 */       if (PacketSize_ms != 10 && PacketSize_ms != 20 && PacketSize_ms != 40 && PacketSize_ms != 60)
/*      */       {
/*      */ 
/*      */         
/*  459 */         ret = SilkError.SILK_ENC_PACKET_SIZE_NOT_SUPPORTED;
/*      */       }
/*  461 */       if (PacketSize_ms <= 10) {
/*  462 */         this.nFramesPerPacket = 1;
/*  463 */         this.nb_subfr = (PacketSize_ms == 10) ? 2 : 1;
/*  464 */         this.frame_length = Inlines.silk_SMULBB(PacketSize_ms, fs_kHz);
/*  465 */         this.pitch_LPC_win_length = Inlines.silk_SMULBB(14, fs_kHz);
/*  466 */         if (this.fs_kHz == 8) {
/*  467 */           this.pitch_contour_iCDF = SilkTables.silk_pitch_contour_10_ms_NB_iCDF;
/*      */         } else {
/*  469 */           this.pitch_contour_iCDF = SilkTables.silk_pitch_contour_10_ms_iCDF;
/*      */         } 
/*      */       } else {
/*  472 */         this.nFramesPerPacket = Inlines.silk_DIV32_16(PacketSize_ms, 20);
/*  473 */         this.nb_subfr = 4;
/*  474 */         this.frame_length = Inlines.silk_SMULBB(20, fs_kHz);
/*  475 */         this.pitch_LPC_win_length = Inlines.silk_SMULBB(24, fs_kHz);
/*  476 */         if (this.fs_kHz == 8) {
/*  477 */           this.pitch_contour_iCDF = SilkTables.silk_pitch_contour_NB_iCDF;
/*      */         } else {
/*  479 */           this.pitch_contour_iCDF = SilkTables.silk_pitch_contour_iCDF;
/*      */         } 
/*      */       } 
/*  482 */       this.PacketSize_ms = PacketSize_ms;
/*  483 */       this.TargetRate_bps = 0;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  488 */     Inlines.OpusAssert((fs_kHz == 8 || fs_kHz == 12 || fs_kHz == 16));
/*  489 */     Inlines.OpusAssert((this.nb_subfr == 2 || this.nb_subfr == 4));
/*  490 */     if (this.fs_kHz != fs_kHz) {
/*      */       
/*  492 */       this.sShape.Reset();
/*  493 */       this.sPrefilt.Reset();
/*  494 */       this.sNSQ.Reset();
/*  495 */       Arrays.MemSet(this.prev_NLSFq_Q15, (short)0, 16);
/*  496 */       Arrays.MemSet(this.sLP.In_LP_State, 0, 2);
/*  497 */       this.inputBufIx = 0;
/*  498 */       this.nFramesEncoded = 0;
/*  499 */       this.TargetRate_bps = 0;
/*      */ 
/*      */ 
/*      */       
/*  503 */       this.prevLag = 100;
/*  504 */       this.first_frame_after_reset = 1;
/*  505 */       this.sPrefilt.lagPrev = 100;
/*  506 */       this.sShape.LastGainIndex = 10;
/*  507 */       this.sNSQ.lagPrev = 100;
/*  508 */       this.sNSQ.prev_gain_Q16 = 65536;
/*  509 */       this.prevSignalType = 0;
/*      */       
/*  511 */       this.fs_kHz = fs_kHz;
/*  512 */       if (this.fs_kHz == 8) {
/*  513 */         if (this.nb_subfr == 4) {
/*  514 */           this.pitch_contour_iCDF = SilkTables.silk_pitch_contour_NB_iCDF;
/*      */         } else {
/*  516 */           this.pitch_contour_iCDF = SilkTables.silk_pitch_contour_10_ms_NB_iCDF;
/*      */         } 
/*  518 */       } else if (this.nb_subfr == 4) {
/*  519 */         this.pitch_contour_iCDF = SilkTables.silk_pitch_contour_iCDF;
/*      */       } else {
/*  521 */         this.pitch_contour_iCDF = SilkTables.silk_pitch_contour_10_ms_iCDF;
/*      */       } 
/*      */       
/*  524 */       if (this.fs_kHz == 8 || this.fs_kHz == 12) {
/*  525 */         this.predictLPCOrder = 10;
/*  526 */         this.psNLSF_CB = SilkTables.silk_NLSF_CB_NB_MB;
/*      */       } else {
/*  528 */         this.predictLPCOrder = 16;
/*  529 */         this.psNLSF_CB = SilkTables.silk_NLSF_CB_WB;
/*      */       } 
/*      */       
/*  532 */       this.subfr_length = 5 * fs_kHz;
/*  533 */       this.frame_length = Inlines.silk_SMULBB(this.subfr_length, this.nb_subfr);
/*  534 */       this.ltp_mem_length = Inlines.silk_SMULBB(20, fs_kHz);
/*  535 */       this.la_pitch = Inlines.silk_SMULBB(2, fs_kHz);
/*  536 */       this.max_pitch_lag = Inlines.silk_SMULBB(18, fs_kHz);
/*      */       
/*  538 */       if (this.nb_subfr == 4) {
/*  539 */         this.pitch_LPC_win_length = Inlines.silk_SMULBB(24, fs_kHz);
/*      */       } else {
/*  541 */         this.pitch_LPC_win_length = Inlines.silk_SMULBB(14, fs_kHz);
/*      */       } 
/*      */       
/*  544 */       if (this.fs_kHz == 16) {
/*  545 */         this.mu_LTP_Q9 = 10;
/*  546 */         this.pitch_lag_low_bits_iCDF = SilkTables.silk_uniform8_iCDF;
/*  547 */       } else if (this.fs_kHz == 12) {
/*  548 */         this.mu_LTP_Q9 = 13;
/*  549 */         this.pitch_lag_low_bits_iCDF = SilkTables.silk_uniform6_iCDF;
/*      */       } else {
/*  551 */         this.mu_LTP_Q9 = 15;
/*  552 */         this.pitch_lag_low_bits_iCDF = SilkTables.silk_uniform4_iCDF;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  557 */     Inlines.OpusAssert((this.subfr_length * this.nb_subfr == this.frame_length));
/*      */     
/*  559 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int silk_setup_complexity(int Complexity) {
/*  569 */     int ret = 0;
/*      */ 
/*      */     
/*  572 */     Inlines.OpusAssert((Complexity >= 0 && Complexity <= 10));
/*  573 */     if (Complexity < 2) {
/*  574 */       this.pitchEstimationComplexity = 0;
/*  575 */       this.pitchEstimationThreshold_Q16 = 52429;
/*  576 */       this.pitchEstimationLPCOrder = 6;
/*  577 */       this.shapingLPCOrder = 8;
/*  578 */       this.la_shape = 3 * this.fs_kHz;
/*  579 */       this.nStatesDelayedDecision = 1;
/*  580 */       this.useInterpolatedNLSFs = 0;
/*  581 */       this.LTPQuantLowComplexity = 1;
/*  582 */       this.NLSF_MSVQ_Survivors = 2;
/*  583 */       this.warping_Q16 = 0;
/*  584 */     } else if (Complexity < 4) {
/*  585 */       this.pitchEstimationComplexity = 1;
/*  586 */       this.pitchEstimationThreshold_Q16 = 49807;
/*  587 */       this.pitchEstimationLPCOrder = 8;
/*  588 */       this.shapingLPCOrder = 10;
/*  589 */       this.la_shape = 5 * this.fs_kHz;
/*  590 */       this.nStatesDelayedDecision = 1;
/*  591 */       this.useInterpolatedNLSFs = 0;
/*  592 */       this.LTPQuantLowComplexity = 0;
/*  593 */       this.NLSF_MSVQ_Survivors = 4;
/*  594 */       this.warping_Q16 = 0;
/*  595 */     } else if (Complexity < 6) {
/*  596 */       this.pitchEstimationComplexity = 1;
/*  597 */       this.pitchEstimationThreshold_Q16 = 48497;
/*  598 */       this.pitchEstimationLPCOrder = 10;
/*  599 */       this.shapingLPCOrder = 12;
/*  600 */       this.la_shape = 5 * this.fs_kHz;
/*  601 */       this.nStatesDelayedDecision = 2;
/*  602 */       this.useInterpolatedNLSFs = 1;
/*  603 */       this.LTPQuantLowComplexity = 0;
/*  604 */       this.NLSF_MSVQ_Survivors = 8;
/*  605 */       this.warping_Q16 = this.fs_kHz * 983;
/*  606 */     } else if (Complexity < 8) {
/*  607 */       this.pitchEstimationComplexity = 1;
/*  608 */       this.pitchEstimationThreshold_Q16 = 47186;
/*  609 */       this.pitchEstimationLPCOrder = 12;
/*  610 */       this.shapingLPCOrder = 14;
/*  611 */       this.la_shape = 5 * this.fs_kHz;
/*  612 */       this.nStatesDelayedDecision = 3;
/*  613 */       this.useInterpolatedNLSFs = 1;
/*  614 */       this.LTPQuantLowComplexity = 0;
/*  615 */       this.NLSF_MSVQ_Survivors = 16;
/*  616 */       this.warping_Q16 = this.fs_kHz * 983;
/*      */     } else {
/*  618 */       this.pitchEstimationComplexity = 2;
/*  619 */       this.pitchEstimationThreshold_Q16 = 45875;
/*  620 */       this.pitchEstimationLPCOrder = 16;
/*  621 */       this.shapingLPCOrder = 16;
/*  622 */       this.la_shape = 5 * this.fs_kHz;
/*  623 */       this.nStatesDelayedDecision = 4;
/*  624 */       this.useInterpolatedNLSFs = 1;
/*  625 */       this.LTPQuantLowComplexity = 0;
/*  626 */       this.NLSF_MSVQ_Survivors = 32;
/*  627 */       this.warping_Q16 = this.fs_kHz * 983;
/*      */     } 
/*      */ 
/*      */     
/*  631 */     this.pitchEstimationLPCOrder = Inlines.silk_min_int(this.pitchEstimationLPCOrder, this.predictLPCOrder);
/*  632 */     this.shapeWinLength = 5 * this.fs_kHz + 2 * this.la_shape;
/*  633 */     this.Complexity = Complexity;
/*      */     
/*  635 */     Inlines.OpusAssert((this.pitchEstimationLPCOrder <= 16));
/*  636 */     Inlines.OpusAssert((this.shapingLPCOrder <= 16));
/*  637 */     Inlines.OpusAssert((this.nStatesDelayedDecision <= 4));
/*  638 */     Inlines.OpusAssert((this.warping_Q16 <= 32767));
/*  639 */     Inlines.OpusAssert((this.la_shape <= 80));
/*  640 */     Inlines.OpusAssert((this.shapeWinLength <= 240));
/*  641 */     Inlines.OpusAssert((this.NLSF_MSVQ_Survivors <= 32));
/*      */     
/*  643 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int silk_setup_LBRR(int TargetRate_bps) {
/*  654 */     int ret = SilkError.SILK_NO_ERROR;
/*      */ 
/*      */     
/*  657 */     int LBRR_in_previous_packet = this.LBRR_enabled;
/*  658 */     this.LBRR_enabled = 0;
/*  659 */     if (this.useInBandFEC != 0 && this.PacketLoss_perc > 0) {
/*  660 */       if (this.fs_kHz == 8) {
/*  661 */         LBRR_rate_thres_bps = 12000;
/*  662 */       } else if (this.fs_kHz == 12) {
/*  663 */         LBRR_rate_thres_bps = 14000;
/*      */       } else {
/*  665 */         LBRR_rate_thres_bps = 16000;
/*      */       } 
/*      */       
/*  668 */       int LBRR_rate_thres_bps = Inlines.silk_SMULWB(Inlines.silk_MUL(LBRR_rate_thres_bps, 125 - Inlines.silk_min(this.PacketLoss_perc, 25)), 655);
/*      */       
/*  670 */       if (TargetRate_bps > LBRR_rate_thres_bps) {
/*      */         
/*  672 */         if (LBRR_in_previous_packet == 0) {
/*      */           
/*  674 */           this.LBRR_GainIncreases = 7;
/*      */         } else {
/*  676 */           this.LBRR_GainIncreases = Inlines.silk_max_int(7 - Inlines.silk_SMULWB(this.PacketLoss_perc, 26214), 2);
/*      */         } 
/*  678 */         this.LBRR_enabled = 1;
/*      */       } 
/*      */     } 
/*      */     
/*  682 */     return ret;
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
/*      */   int silk_control_audio_bandwidth(EncControlState encControl) {
/*  695 */     int fs_kHz = this.fs_kHz;
/*  696 */     int fs_Hz = Inlines.silk_SMULBB(fs_kHz, 1000);
/*      */     
/*  698 */     if (fs_Hz == 0) {
/*      */       
/*  700 */       fs_Hz = Inlines.silk_min(this.desiredInternal_fs_Hz, this.API_fs_Hz);
/*  701 */       fs_kHz = Inlines.silk_DIV32_16(fs_Hz, 1000);
/*  702 */     } else if (fs_Hz > this.API_fs_Hz || fs_Hz > this.maxInternal_fs_Hz || fs_Hz < this.minInternal_fs_Hz) {
/*      */       
/*  704 */       fs_Hz = this.API_fs_Hz;
/*  705 */       fs_Hz = Inlines.silk_min(fs_Hz, this.maxInternal_fs_Hz);
/*  706 */       fs_Hz = Inlines.silk_max(fs_Hz, this.minInternal_fs_Hz);
/*  707 */       fs_kHz = Inlines.silk_DIV32_16(fs_Hz, 1000);
/*      */     } else {
/*      */       
/*  710 */       if (this.sLP.transition_frame_no >= 256)
/*      */       {
/*  712 */         this.sLP.mode = 0;
/*      */       }
/*      */       
/*  715 */       if (this.allow_bandwidth_switch != 0 || encControl.opusCanSwitch != 0)
/*      */       {
/*  717 */         if (Inlines.silk_SMULBB(this.fs_kHz, 1000) > this.desiredInternal_fs_Hz) {
/*      */           
/*  719 */           if (this.sLP.mode == 0) {
/*      */             
/*  721 */             this.sLP.transition_frame_no = 256;
/*      */ 
/*      */             
/*  724 */             Arrays.MemSet(this.sLP.In_LP_State, 0, 2);
/*      */           } 
/*      */           
/*  727 */           if (encControl.opusCanSwitch != 0) {
/*      */             
/*  729 */             this.sLP.mode = 0;
/*      */ 
/*      */             
/*  732 */             fs_kHz = (this.fs_kHz == 16) ? 12 : 8;
/*  733 */           } else if (this.sLP.transition_frame_no <= 0) {
/*  734 */             encControl.switchReady = 1;
/*      */             
/*  736 */             encControl.maxBits -= encControl.maxBits * 5 / (encControl.payloadSize_ms + 5);
/*      */           } else {
/*      */             
/*  739 */             this.sLP.mode = -2;
/*      */           } 
/*  741 */         } else if (Inlines.silk_SMULBB(this.fs_kHz, 1000) < this.desiredInternal_fs_Hz) {
/*      */           
/*  743 */           if (encControl.opusCanSwitch != 0) {
/*      */             
/*  745 */             fs_kHz = (this.fs_kHz == 8) ? 12 : 16;
/*      */ 
/*      */             
/*  748 */             this.sLP.transition_frame_no = 0;
/*      */ 
/*      */             
/*  751 */             Arrays.MemSet(this.sLP.In_LP_State, 0, 2);
/*      */ 
/*      */             
/*  754 */             this.sLP.mode = 1;
/*  755 */           } else if (this.sLP.mode == 0) {
/*  756 */             encControl.switchReady = 1;
/*      */             
/*  758 */             encControl.maxBits -= encControl.maxBits * 5 / (encControl.payloadSize_ms + 5);
/*      */           } else {
/*      */             
/*  761 */             this.sLP.mode = 1;
/*      */           } 
/*  763 */         } else if (this.sLP.mode < 0) {
/*  764 */           this.sLP.mode = 1;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  769 */     return fs_kHz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int silk_control_SNR(int TargetRate_bps) {
/*  776 */     int ret = SilkError.SILK_NO_ERROR;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  781 */     TargetRate_bps = Inlines.silk_LIMIT(TargetRate_bps, 5000, 80000);
/*  782 */     if (TargetRate_bps != this.TargetRate_bps) {
/*  783 */       int[] rateTable; this.TargetRate_bps = TargetRate_bps;
/*      */ 
/*      */       
/*  786 */       if (this.fs_kHz == 8) {
/*  787 */         rateTable = SilkTables.silk_TargetRate_table_NB;
/*  788 */       } else if (this.fs_kHz == 12) {
/*  789 */         rateTable = SilkTables.silk_TargetRate_table_MB;
/*      */       } else {
/*  791 */         rateTable = SilkTables.silk_TargetRate_table_WB;
/*      */       } 
/*      */ 
/*      */       
/*  795 */       if (this.nb_subfr == 2) {
/*  796 */         TargetRate_bps -= 2200;
/*      */       }
/*      */ 
/*      */       
/*  800 */       for (int k = 1; k < 8; k++) {
/*  801 */         if (TargetRate_bps <= rateTable[k]) {
/*  802 */           int frac_Q6 = Inlines.silk_DIV32(Inlines.silk_LSHIFT(TargetRate_bps - rateTable[k - 1], 6), rateTable[k] - rateTable[k - 1]);
/*      */           
/*  804 */           this.SNR_dB_Q7 = Inlines.silk_LSHIFT(SilkTables.silk_SNR_table_Q1[k - 1], 6) + Inlines.silk_MUL(frac_Q6, SilkTables.silk_SNR_table_Q1[k] - SilkTables.silk_SNR_table_Q1[k - 1]);
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  810 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void silk_encode_do_VAD() {
/*  821 */     VoiceActivityDetection.silk_VAD_GetSA_Q8(this, this.inputBuf, 1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  830 */     if (this.speech_activity_Q8 < 13) {
/*  831 */       this.indices.signalType = 0;
/*  832 */       this.noSpeechCounter++;
/*  833 */       if (this.noSpeechCounter < 10) {
/*  834 */         this.inDTX = 0;
/*  835 */       } else if (this.noSpeechCounter > 30) {
/*  836 */         this.noSpeechCounter = 10;
/*  837 */         this.inDTX = 0;
/*      */       } 
/*  839 */       this.VAD_flags[this.nFramesEncoded] = 0;
/*      */     } else {
/*  841 */       this.noSpeechCounter = 0;
/*  842 */       this.inDTX = 0;
/*  843 */       this.indices.signalType = 1;
/*  844 */       this.VAD_flags[this.nFramesEncoded] = 1;
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
/*      */   int silk_encode_frame(BoxedValueInt pnBytesOut, EntropyCoder psRangeEnc, int condCoding, int maxBits, int useCBR) {
/*  862 */     SilkEncoderControl sEncCtrl = new SilkEncoderControl();
/*  863 */     int ret = 0;
/*      */     
/*  865 */     EntropyCoder sRangeEnc_copy = new EntropyCoder();
/*  866 */     EntropyCoder sRangeEnc_copy2 = new EntropyCoder();
/*  867 */     SilkNSQState sNSQ_copy = new SilkNSQState();
/*  868 */     SilkNSQState sNSQ_copy2 = new SilkNSQState();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  878 */     byte LastGainIndex_copy2 = 0;
/*  879 */     int gainMult_upper = 0, gainMult_lower = gainMult_upper, nBits_upper = gainMult_lower, nBits_lower = nBits_upper;
/*      */     
/*  881 */     this.indices.Seed = (byte)(this.frameCounter++ & 0x3);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  891 */     int x_frame = this.ltp_mem_length;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  900 */     this.sLP.silk_LP_variable_cutoff(this.inputBuf, 1, this.frame_length);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  909 */     System.arraycopy(this.inputBuf, 1, this.x_buf, x_frame + 5 * this.fs_kHz, this.frame_length);
/*      */     
/*  911 */     if (this.prefillFlag == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  917 */       short[] res_pitch = new short[this.la_pitch + this.frame_length + this.ltp_mem_length];
/*      */       
/*  919 */       int res_pitch_frame = this.ltp_mem_length;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  928 */       FindPitchLags.silk_find_pitch_lags(this, sEncCtrl, res_pitch, this.x_buf, x_frame);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  937 */       NoiseShapeAnalysis.silk_noise_shape_analysis(this, sEncCtrl, res_pitch, res_pitch_frame, this.x_buf, x_frame);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  946 */       FindPredCoefs.silk_find_pred_coefs(this, sEncCtrl, res_pitch, this.x_buf, x_frame, condCoding);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  955 */       ProcessGains.silk_process_gains(this, sEncCtrl, condCoding);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  964 */       int[] xfw_Q3 = new int[this.frame_length];
/*  965 */       Filters.silk_prefilter(this, sEncCtrl, xfw_Q3, this.x_buf, x_frame);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  974 */       silk_LBRR_encode(sEncCtrl, xfw_Q3, condCoding);
/*      */ 
/*      */       
/*  977 */       int maxIter = 6;
/*  978 */       short gainMult_Q8 = 256;
/*  979 */       int found_lower = 0;
/*  980 */       int found_upper = 0;
/*  981 */       int gainsID = GainQuantization.silk_gains_ID(this.indices.GainsIndices, this.nb_subfr);
/*  982 */       int gainsID_lower = -1;
/*  983 */       int gainsID_upper = -1;
/*      */       
/*  985 */       sRangeEnc_copy.Assign(psRangeEnc);
/*  986 */       sNSQ_copy.Assign(this.sNSQ);
/*  987 */       byte seed_copy = this.indices.Seed;
/*  988 */       short ec_prevLagIndex_copy = this.ec_prevLagIndex;
/*  989 */       int ec_prevSignalType_copy = this.ec_prevSignalType;
/*  990 */       byte[] ec_buf_copy = new byte[1275];
/*  991 */       for (int iter = 0;; iter++) {
/*  992 */         int nBits; if (gainsID == gainsID_lower) {
/*  993 */           nBits = nBits_lower;
/*  994 */         } else if (gainsID == gainsID_upper) {
/*  995 */           nBits = nBits_upper;
/*      */         } else {
/*      */           
/*  998 */           if (iter > 0) {
/*  999 */             psRangeEnc.Assign(sRangeEnc_copy);
/* 1000 */             this.sNSQ.Assign(sNSQ_copy);
/* 1001 */             this.indices.Seed = seed_copy;
/* 1002 */             this.ec_prevLagIndex = ec_prevLagIndex_copy;
/* 1003 */             this.ec_prevSignalType = ec_prevSignalType_copy;
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1013 */           if (this.nStatesDelayedDecision > 1 || this.warping_Q16 > 0) {
/* 1014 */             this.sNSQ.silk_NSQ_del_dec(this, this.indices, xfw_Q3, this.pulses, sEncCtrl.PredCoef_Q12, sEncCtrl.LTPCoef_Q14, sEncCtrl.AR2_Q13, sEncCtrl.HarmShapeGain_Q14, sEncCtrl.Tilt_Q14, sEncCtrl.LF_shp_Q14, sEncCtrl.Gains_Q16, sEncCtrl.pitchL, sEncCtrl.Lambda_Q10, sEncCtrl.LTP_scale_Q14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1030 */             this.sNSQ.silk_NSQ(this, this.indices, xfw_Q3, this.pulses, sEncCtrl.PredCoef_Q12, sEncCtrl.LTPCoef_Q14, sEncCtrl.AR2_Q13, sEncCtrl.HarmShapeGain_Q14, sEncCtrl.Tilt_Q14, sEncCtrl.LF_shp_Q14, sEncCtrl.Gains_Q16, sEncCtrl.pitchL, sEncCtrl.Lambda_Q10, sEncCtrl.LTP_scale_Q14);
/*      */           } 
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
/* 1054 */           EncodeIndices.silk_encode_indices(this, psRangeEnc, this.nFramesEncoded, 0, condCoding);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1063 */           EncodePulses.silk_encode_pulses(psRangeEnc, this.indices.signalType, this.indices.quantOffsetType, this.pulses, this.frame_length);
/*      */ 
/*      */           
/* 1066 */           nBits = psRangeEnc.tell();
/*      */           
/* 1068 */           if (useCBR == 0 && iter == 0 && nBits <= maxBits) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */         
/* 1073 */         if (iter == maxIter) {
/* 1074 */           if (found_lower != 0 && (gainsID == gainsID_lower || nBits > maxBits)) {
/*      */             
/* 1076 */             psRangeEnc.Assign(sRangeEnc_copy2);
/* 1077 */             Inlines.OpusAssert((sRangeEnc_copy2.offs <= 1275));
/* 1078 */             psRangeEnc.write_buffer(ec_buf_copy, 0, 0, sRangeEnc_copy2.offs);
/* 1079 */             this.sNSQ.Assign(sNSQ_copy2);
/* 1080 */             this.sShape.LastGainIndex = LastGainIndex_copy2;
/*      */           } 
/*      */           
/*      */           break;
/*      */         } 
/* 1085 */         if (nBits > maxBits) {
/* 1086 */           if (found_lower == 0 && iter >= 2) {
/*      */             
/* 1088 */             sEncCtrl.Lambda_Q10 = Inlines.silk_ADD_RSHIFT32(sEncCtrl.Lambda_Q10, sEncCtrl.Lambda_Q10, 1);
/* 1089 */             found_upper = 0;
/* 1090 */             gainsID_upper = -1;
/*      */           } else {
/* 1092 */             found_upper = 1;
/* 1093 */             nBits_upper = nBits;
/* 1094 */             gainMult_upper = gainMult_Q8;
/* 1095 */             gainsID_upper = gainsID;
/*      */           } 
/* 1097 */         } else if (nBits < maxBits - 5) {
/* 1098 */           found_lower = 1;
/* 1099 */           nBits_lower = nBits;
/* 1100 */           gainMult_lower = gainMult_Q8;
/* 1101 */           if (gainsID != gainsID_lower) {
/* 1102 */             gainsID_lower = gainsID;
/*      */             
/* 1104 */             sRangeEnc_copy2.Assign(psRangeEnc);
/* 1105 */             Inlines.OpusAssert((psRangeEnc.offs <= 1275));
/* 1106 */             System.arraycopy(psRangeEnc.get_buffer(), 0, ec_buf_copy, 0, psRangeEnc.offs);
/* 1107 */             sNSQ_copy2.Assign(this.sNSQ);
/* 1108 */             LastGainIndex_copy2 = this.sShape.LastGainIndex;
/*      */           } 
/*      */         } else {
/*      */           break;
/*      */         } 
/*      */ 
/*      */         
/* 1115 */         if ((found_lower & found_upper) == 0) {
/*      */ 
/*      */           
/* 1118 */           int gain_factor_Q16 = Inlines.silk_log2lin(Inlines.silk_LSHIFT(nBits - maxBits, 7) / this.frame_length + 2048);
/* 1119 */           gain_factor_Q16 = Inlines.silk_min_32(gain_factor_Q16, 131072);
/* 1120 */           if (nBits > maxBits) {
/* 1121 */             gain_factor_Q16 = Inlines.silk_max_32(gain_factor_Q16, 85197);
/*      */           }
/*      */           
/* 1124 */           gainMult_Q8 = (short)Inlines.silk_SMULWB(gain_factor_Q16, gainMult_Q8);
/*      */         } else {
/*      */           
/* 1127 */           gainMult_Q8 = (short)(gainMult_lower + Inlines.silk_DIV32_16(Inlines.silk_MUL(gainMult_upper - gainMult_lower, maxBits - nBits_lower), nBits_upper - nBits_lower));
/*      */           
/* 1129 */           if (gainMult_Q8 > Inlines.silk_ADD_RSHIFT32(gainMult_lower, gainMult_upper - gainMult_lower, 2)) {
/* 1130 */             gainMult_Q8 = (short)Inlines.silk_ADD_RSHIFT32(gainMult_lower, gainMult_upper - gainMult_lower, 2);
/* 1131 */           } else if (gainMult_Q8 < Inlines.silk_SUB_RSHIFT32(gainMult_upper, gainMult_upper - gainMult_lower, 2)) {
/* 1132 */             gainMult_Q8 = (short)Inlines.silk_SUB_RSHIFT32(gainMult_upper, gainMult_upper - gainMult_lower, 2);
/*      */           } 
/*      */         } 
/*      */         
/* 1136 */         for (int i = 0; i < this.nb_subfr; i++) {
/* 1137 */           sEncCtrl.Gains_Q16[i] = Inlines.silk_LSHIFT_SAT32(Inlines.silk_SMULWB(sEncCtrl.GainsUnq_Q16[i], gainMult_Q8), 8);
/*      */         }
/*      */ 
/*      */         
/* 1141 */         this.sShape.LastGainIndex = sEncCtrl.lastGainIndexPrev;
/* 1142 */         BoxedValueByte boxed_gainIndex = new BoxedValueByte(this.sShape.LastGainIndex);
/* 1143 */         GainQuantization.silk_gains_quant(this.indices.GainsIndices, sEncCtrl.Gains_Q16, boxed_gainIndex, 
/* 1144 */             (condCoding == 2) ? 1 : 0, this.nb_subfr);
/* 1145 */         this.sShape.LastGainIndex = boxed_gainIndex.Val;
/*      */ 
/*      */         
/* 1148 */         gainsID = GainQuantization.silk_gains_ID(this.indices.GainsIndices, this.nb_subfr);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1153 */     Arrays.MemMove(this.x_buf, this.frame_length, 0, this.ltp_mem_length + 5 * this.fs_kHz);
/*      */ 
/*      */     
/* 1156 */     if (this.prefillFlag != 0) {
/*      */       
/* 1158 */       pnBytesOut.Val = 0;
/*      */       
/* 1160 */       return ret;
/*      */     } 
/*      */ 
/*      */     
/* 1164 */     this.prevLag = sEncCtrl.pitchL[this.nb_subfr - 1];
/* 1165 */     this.prevSignalType = this.indices.signalType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1174 */     this.first_frame_after_reset = 0;
/*      */     
/* 1176 */     pnBytesOut.Val = Inlines.silk_RSHIFT(psRangeEnc.tell() + 7, 3);
/*      */     
/* 1178 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void silk_LBRR_encode(SilkEncoderControl thisCtrl, int[] xfw_Q3, int condCoding) {
/* 1187 */     int[] TempGains_Q16 = new int[this.nb_subfr];
/* 1188 */     SideInfoIndices psIndices_LBRR = this.indices_LBRR[this.nFramesEncoded];
/* 1189 */     SilkNSQState sNSQ_LBRR = new SilkNSQState();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1198 */     if (this.LBRR_enabled != 0 && this.speech_activity_Q8 > 77) {
/* 1199 */       this.LBRR_flags[this.nFramesEncoded] = 1;
/*      */ 
/*      */       
/* 1202 */       sNSQ_LBRR.Assign(this.sNSQ);
/* 1203 */       psIndices_LBRR.Assign(this.indices);
/*      */ 
/*      */       
/* 1206 */       System.arraycopy(thisCtrl.Gains_Q16, 0, TempGains_Q16, 0, this.nb_subfr);
/*      */       
/* 1208 */       if (this.nFramesEncoded == 0 || this.LBRR_flags[this.nFramesEncoded - 1] == 0) {
/*      */         
/* 1210 */         this.LBRRprevLastGainIndex = this.sShape.LastGainIndex;
/*      */ 
/*      */         
/* 1213 */         psIndices_LBRR.GainsIndices[0] = (byte)(psIndices_LBRR.GainsIndices[0] + this.LBRR_GainIncreases);
/* 1214 */         psIndices_LBRR.GainsIndices[0] = (byte)Inlines.silk_min_int(psIndices_LBRR.GainsIndices[0], 63);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1219 */       BoxedValueByte boxed_gainIndex = new BoxedValueByte(this.LBRRprevLastGainIndex);
/* 1220 */       GainQuantization.silk_gains_dequant(thisCtrl.Gains_Q16, psIndices_LBRR.GainsIndices, boxed_gainIndex, 
/* 1221 */           (condCoding == 2) ? 1 : 0, this.nb_subfr);
/* 1222 */       this.LBRRprevLastGainIndex = boxed_gainIndex.Val;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1231 */       if (this.nStatesDelayedDecision > 1 || this.warping_Q16 > 0) {
/* 1232 */         sNSQ_LBRR.silk_NSQ_del_dec(this, psIndices_LBRR, xfw_Q3, this.pulses_LBRR[this.nFramesEncoded], thisCtrl.PredCoef_Q12, thisCtrl.LTPCoef_Q14, thisCtrl.AR2_Q13, thisCtrl.HarmShapeGain_Q14, thisCtrl.Tilt_Q14, thisCtrl.LF_shp_Q14, thisCtrl.Gains_Q16, thisCtrl.pitchL, thisCtrl.Lambda_Q10, thisCtrl.LTP_scale_Q14);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1247 */         sNSQ_LBRR.silk_NSQ(this, psIndices_LBRR, xfw_Q3, this.pulses_LBRR[this.nFramesEncoded], thisCtrl.PredCoef_Q12, thisCtrl.LTPCoef_Q14, thisCtrl.AR2_Q13, thisCtrl.HarmShapeGain_Q14, thisCtrl.Tilt_Q14, thisCtrl.LF_shp_Q14, thisCtrl.Gains_Q16, thisCtrl.pitchL, thisCtrl.Lambda_Q10, thisCtrl.LTP_scale_Q14);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1264 */       System.arraycopy(TempGains_Q16, 0, thisCtrl.Gains_Q16, 0, this.nb_subfr);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SilkChannelEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */