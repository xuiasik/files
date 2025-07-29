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
/*     */ class EncodeAPI
/*     */ {
/*     */   static int silk_InitEncoder(SilkEncoder encState, EncControlState encStatus) {
/*  44 */     int ret = SilkError.SILK_NO_ERROR;
/*     */ 
/*     */     
/*  47 */     encState.Reset();
/*     */     
/*  49 */     for (int n = 0; n < 2; n++) {
/*  50 */       ret += SilkEncoder.silk_init_encoder(encState.state_Fxx[n]);
/*  51 */       Inlines.OpusAssert((ret == SilkError.SILK_NO_ERROR));
/*     */     } 
/*     */     
/*  54 */     encState.nChannelsAPI = 1;
/*  55 */     encState.nChannelsInternal = 1;
/*     */ 
/*     */     
/*  58 */     ret += silk_QueryEncoder(encState, encStatus);
/*  59 */     Inlines.OpusAssert((ret == SilkError.SILK_NO_ERROR));
/*     */     
/*  61 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int silk_QueryEncoder(SilkEncoder encState, EncControlState encStatus) {
/*  71 */     int ret = SilkError.SILK_NO_ERROR;
/*  72 */     SilkChannelEncoder state_Fxx = encState.state_Fxx[0];
/*     */     
/*  74 */     encStatus.Reset();
/*     */     
/*  76 */     encStatus.nChannelsAPI = encState.nChannelsAPI;
/*  77 */     encStatus.nChannelsInternal = encState.nChannelsInternal;
/*  78 */     encStatus.API_sampleRate = state_Fxx.API_fs_Hz;
/*  79 */     encStatus.maxInternalSampleRate = state_Fxx.maxInternal_fs_Hz;
/*  80 */     encStatus.minInternalSampleRate = state_Fxx.minInternal_fs_Hz;
/*  81 */     encStatus.desiredInternalSampleRate = state_Fxx.desiredInternal_fs_Hz;
/*  82 */     encStatus.payloadSize_ms = state_Fxx.PacketSize_ms;
/*  83 */     encStatus.bitRate = state_Fxx.TargetRate_bps;
/*  84 */     encStatus.packetLossPercentage = state_Fxx.PacketLoss_perc;
/*  85 */     encStatus.complexity = state_Fxx.Complexity;
/*  86 */     encStatus.useInBandFEC = state_Fxx.useInBandFEC;
/*  87 */     encStatus.useDTX = state_Fxx.useDTX;
/*  88 */     encStatus.useCBR = state_Fxx.useCBR;
/*  89 */     encStatus.internalSampleRate = Inlines.silk_SMULBB(state_Fxx.fs_kHz, 1000);
/*  90 */     encStatus.allowBandwidthSwitch = state_Fxx.allow_bandwidth_switch;
/*  91 */     encStatus.inWBmodeWithoutVariableLP = (state_Fxx.fs_kHz == 16 && state_Fxx.sLP.mode == 0) ? 1 : 0;
/*     */     
/*  93 */     return ret;
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
/*     */   static int silk_Encode(SilkEncoder psEnc, EncControlState encControl, short[] samplesIn, int nSamplesIn, EntropyCoder psRangeEnc, BoxedValueInt nBytesOut, int prefillFlag) {
/* 117 */     int ret = SilkError.SILK_NO_ERROR;
/* 118 */     int tmp_payloadSize_ms = 0, tmp_complexity = 0;
/*     */     
/* 120 */     int nSamplesFromInput = 0;
/*     */ 
/*     */     
/* 123 */     int[] MStargetRates_bps = new int[2];
/*     */ 
/*     */     
/* 126 */     nBytesOut.Val = 0;
/*     */     
/* 128 */     if (encControl.reducedDependency != 0) {
/* 129 */       (psEnc.state_Fxx[0]).first_frame_after_reset = 1;
/* 130 */       (psEnc.state_Fxx[1]).first_frame_after_reset = 1;
/*     */     } 
/* 132 */     (psEnc.state_Fxx[1]).nFramesEncoded = 0;
/*     */ 
/*     */     
/* 135 */     ret += encControl.check_control_input();
/* 136 */     if (ret != SilkError.SILK_NO_ERROR) {
/* 137 */       Inlines.OpusAssert(false);
/* 138 */       return ret;
/*     */     } 
/*     */     
/* 141 */     encControl.switchReady = 0;
/*     */     
/* 143 */     if (encControl.nChannelsInternal > psEnc.nChannelsInternal) {
/*     */       
/* 145 */       ret += SilkEncoder.silk_init_encoder(psEnc.state_Fxx[1]);
/*     */       
/* 147 */       Arrays.MemSet(psEnc.sStereo.pred_prev_Q13, (short)0, 2);
/* 148 */       Arrays.MemSet(psEnc.sStereo.sSide, (short)0, 2);
/* 149 */       psEnc.sStereo.mid_side_amp_Q0[0] = 0;
/* 150 */       psEnc.sStereo.mid_side_amp_Q0[1] = 1;
/* 151 */       psEnc.sStereo.mid_side_amp_Q0[2] = 0;
/* 152 */       psEnc.sStereo.mid_side_amp_Q0[3] = 1;
/* 153 */       psEnc.sStereo.width_prev_Q14 = 0;
/* 154 */       psEnc.sStereo.smth_width_Q14 = 16384;
/* 155 */       if (psEnc.nChannelsAPI == 2) {
/* 156 */         (psEnc.state_Fxx[1]).resampler_state.Assign((psEnc.state_Fxx[0]).resampler_state);
/* 157 */         System.arraycopy((psEnc.state_Fxx[0]).In_HP_State, 0, (psEnc.state_Fxx[1]).In_HP_State, 0, 2);
/*     */       } 
/*     */     } 
/*     */     
/* 161 */     int transition = (encControl.payloadSize_ms != (psEnc.state_Fxx[0]).PacketSize_ms || psEnc.nChannelsInternal != encControl.nChannelsInternal) ? 1 : 0;
/*     */     
/* 163 */     psEnc.nChannelsAPI = encControl.nChannelsAPI;
/* 164 */     psEnc.nChannelsInternal = encControl.nChannelsInternal;
/*     */     
/* 166 */     int nBlocksOf10ms = Inlines.silk_DIV32(100 * nSamplesIn, encControl.API_sampleRate);
/* 167 */     int tot_blocks = (nBlocksOf10ms > 1) ? (nBlocksOf10ms >> 1) : 1;
/* 168 */     int curr_block = 0;
/* 169 */     if (prefillFlag != 0) {
/*     */       
/* 171 */       if (nBlocksOf10ms != 1) {
/* 172 */         Inlines.OpusAssert(false);
/* 173 */         return SilkError.SILK_ENC_INPUT_INVALID_NO_OF_SAMPLES;
/*     */       } 
/*     */       int i;
/* 176 */       for (i = 0; i < encControl.nChannelsInternal; i++) {
/* 177 */         ret += SilkEncoder.silk_init_encoder(psEnc.state_Fxx[i]);
/* 178 */         Inlines.OpusAssert((ret == SilkError.SILK_NO_ERROR));
/*     */       } 
/* 180 */       tmp_payloadSize_ms = encControl.payloadSize_ms;
/* 181 */       encControl.payloadSize_ms = 10;
/* 182 */       tmp_complexity = encControl.complexity;
/* 183 */       encControl.complexity = 0;
/* 184 */       for (i = 0; i < encControl.nChannelsInternal; i++) {
/* 185 */         (psEnc.state_Fxx[i]).controlled_since_last_payload = 0;
/* 186 */         (psEnc.state_Fxx[i]).prefillFlag = 1;
/*     */       } 
/*     */     } else {
/*     */       
/* 190 */       if (nBlocksOf10ms * encControl.API_sampleRate != 100 * nSamplesIn || nSamplesIn < 0) {
/* 191 */         Inlines.OpusAssert(false);
/* 192 */         return SilkError.SILK_ENC_INPUT_INVALID_NO_OF_SAMPLES;
/*     */       } 
/*     */       
/* 195 */       if (1000 * nSamplesIn > encControl.payloadSize_ms * encControl.API_sampleRate) {
/* 196 */         Inlines.OpusAssert(false);
/* 197 */         return SilkError.SILK_ENC_INPUT_INVALID_NO_OF_SAMPLES;
/*     */       } 
/*     */     } 
/*     */     
/* 201 */     int TargetRate_bps = Inlines.silk_RSHIFT32(encControl.bitRate, encControl.nChannelsInternal - 1);
/*     */     int n;
/* 203 */     for (n = 0; n < encControl.nChannelsInternal; n++) {
/*     */       
/* 205 */       int force_fs_kHz = (n == 1) ? (psEnc.state_Fxx[0]).fs_kHz : 0;
/* 206 */       ret += psEnc.state_Fxx[n].silk_control_encoder(encControl, TargetRate_bps, psEnc.allowBandwidthSwitch, n, force_fs_kHz);
/*     */       
/* 208 */       if (ret != SilkError.SILK_NO_ERROR) {
/* 209 */         Inlines.OpusAssert(false);
/* 210 */         return ret;
/*     */       } 
/*     */       
/* 213 */       if ((psEnc.state_Fxx[n]).first_frame_after_reset != 0 || transition != 0) {
/* 214 */         for (int i = 0; i < (psEnc.state_Fxx[0]).nFramesPerPacket; i++) {
/* 215 */           (psEnc.state_Fxx[n]).LBRR_flags[i] = 0;
/*     */         }
/*     */       }
/*     */       
/* 219 */       (psEnc.state_Fxx[n]).inDTX = (psEnc.state_Fxx[n]).useDTX;
/*     */     } 
/*     */     
/* 222 */     Inlines.OpusAssert((encControl.nChannelsInternal == 1 || (psEnc.state_Fxx[0]).fs_kHz == (psEnc.state_Fxx[1]).fs_kHz));
/*     */ 
/*     */     
/* 225 */     int nSamplesToBufferMax = 10 * nBlocksOf10ms * (psEnc.state_Fxx[0]).fs_kHz;
/*     */     
/* 227 */     int nSamplesFromInputMax = Inlines.silk_DIV32_16(nSamplesToBufferMax * (psEnc.state_Fxx[0]).API_fs_Hz, (short)((psEnc.state_Fxx[0]).fs_kHz * 1000));
/*     */ 
/*     */ 
/*     */     
/* 231 */     short[] buf = new short[nSamplesFromInputMax];
/*     */     
/* 233 */     int samplesIn_ptr = 0;
/*     */     while (true) {
/* 235 */       int nSamplesToBuffer = (psEnc.state_Fxx[0]).frame_length - (psEnc.state_Fxx[0]).inputBufIx;
/* 236 */       nSamplesToBuffer = Inlines.silk_min(nSamplesToBuffer, nSamplesToBufferMax);
/* 237 */       nSamplesFromInput = Inlines.silk_DIV32_16(nSamplesToBuffer * (psEnc.state_Fxx[0]).API_fs_Hz, (psEnc.state_Fxx[0]).fs_kHz * 1000);
/*     */ 
/*     */       
/* 240 */       if (encControl.nChannelsAPI == 2 && encControl.nChannelsInternal == 2) {
/* 241 */         int id = (psEnc.state_Fxx[0]).nFramesEncoded;
/* 242 */         for (n = 0; n < nSamplesFromInput; n++) {
/* 243 */           buf[n] = samplesIn[samplesIn_ptr + 2 * n];
/*     */         }
/*     */ 
/*     */         
/* 247 */         if (psEnc.nPrevChannelsInternal == 1 && id == 0)
/*     */         {
/* 249 */           (psEnc.state_Fxx[1]).resampler_state.Assign((psEnc.state_Fxx[0]).resampler_state);
/*     */         }
/*     */         
/* 252 */         ret += Resampler.silk_resampler((psEnc.state_Fxx[0]).resampler_state, (psEnc.state_Fxx[0]).inputBuf, (psEnc.state_Fxx[0]).inputBufIx + 2, buf, 0, nSamplesFromInput);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 260 */         (psEnc.state_Fxx[0]).inputBufIx += nSamplesToBuffer;
/*     */         
/* 262 */         nSamplesToBuffer = (psEnc.state_Fxx[1]).frame_length - (psEnc.state_Fxx[1]).inputBufIx;
/* 263 */         nSamplesToBuffer = Inlines.silk_min(nSamplesToBuffer, 10 * nBlocksOf10ms * (psEnc.state_Fxx[1]).fs_kHz);
/* 264 */         for (n = 0; n < nSamplesFromInput; n++) {
/* 265 */           buf[n] = samplesIn[samplesIn_ptr + 2 * n + 1];
/*     */         }
/* 267 */         ret += Resampler.silk_resampler((psEnc.state_Fxx[1]).resampler_state, (psEnc.state_Fxx[1]).inputBuf, (psEnc.state_Fxx[1]).inputBufIx + 2, buf, 0, nSamplesFromInput);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 275 */         (psEnc.state_Fxx[1]).inputBufIx += nSamplesToBuffer;
/* 276 */       } else if (encControl.nChannelsAPI == 2 && encControl.nChannelsInternal == 1) {
/*     */         
/* 278 */         for (n = 0; n < nSamplesFromInput; n++) {
/* 279 */           int sum = samplesIn[samplesIn_ptr + 2 * n] + samplesIn[samplesIn_ptr + 2 * n + 1];
/* 280 */           buf[n] = (short)Inlines.silk_RSHIFT_ROUND(sum, 1);
/*     */         } 
/*     */         
/* 283 */         ret += Resampler.silk_resampler((psEnc.state_Fxx[0]).resampler_state, (psEnc.state_Fxx[0]).inputBuf, (psEnc.state_Fxx[0]).inputBufIx + 2, buf, 0, nSamplesFromInput);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 292 */         if (psEnc.nPrevChannelsInternal == 2 && (psEnc.state_Fxx[0]).nFramesEncoded == 0) {
/* 293 */           ret += Resampler.silk_resampler((psEnc.state_Fxx[1]).resampler_state, (psEnc.state_Fxx[1]).inputBuf, (psEnc.state_Fxx[1]).inputBufIx + 2, buf, 0, nSamplesFromInput);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 301 */           for (n = 0; n < (psEnc.state_Fxx[0]).frame_length; n++) {
/* 302 */             (psEnc.state_Fxx[0]).inputBuf[(psEnc.state_Fxx[0]).inputBufIx + n + 2] = 
/* 303 */               (short)Inlines.silk_RSHIFT((psEnc.state_Fxx[0]).inputBuf[(psEnc.state_Fxx[0]).inputBufIx + n + 2] + (psEnc.state_Fxx[1]).inputBuf[(psEnc.state_Fxx[1]).inputBufIx + n + 2], 1);
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 308 */         (psEnc.state_Fxx[0]).inputBufIx += nSamplesToBuffer;
/*     */       } else {
/* 310 */         Inlines.OpusAssert((encControl.nChannelsAPI == 1 && encControl.nChannelsInternal == 1));
/* 311 */         System.arraycopy(samplesIn, samplesIn_ptr, buf, 0, nSamplesFromInput);
/* 312 */         ret += Resampler.silk_resampler((psEnc.state_Fxx[0]).resampler_state, (psEnc.state_Fxx[0]).inputBuf, (psEnc.state_Fxx[0]).inputBufIx + 2, buf, 0, nSamplesFromInput);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 320 */         (psEnc.state_Fxx[0]).inputBufIx += nSamplesToBuffer;
/*     */       } 
/*     */       
/* 323 */       samplesIn_ptr += nSamplesFromInput * encControl.nChannelsAPI;
/* 324 */       nSamplesIn -= nSamplesFromInput;
/*     */ 
/*     */       
/* 327 */       psEnc.allowBandwidthSwitch = 0;
/*     */ 
/*     */       
/* 330 */       if ((psEnc.state_Fxx[0]).inputBufIx >= (psEnc.state_Fxx[0]).frame_length) {
/*     */         
/* 332 */         Inlines.OpusAssert(((psEnc.state_Fxx[0]).inputBufIx == (psEnc.state_Fxx[0]).frame_length));
/* 333 */         Inlines.OpusAssert((encControl.nChannelsInternal == 1 || (psEnc.state_Fxx[1]).inputBufIx == (psEnc.state_Fxx[1]).frame_length));
/*     */ 
/*     */         
/* 336 */         if ((psEnc.state_Fxx[0]).nFramesEncoded == 0 && prefillFlag == 0) {
/*     */           
/* 338 */           short[] iCDF = { 0, 0 };
/* 339 */           iCDF[0] = (short)(256 - Inlines.silk_RSHIFT(256, ((psEnc.state_Fxx[0]).nFramesPerPacket + 1) * encControl.nChannelsInternal));
/* 340 */           psRangeEnc.enc_icdf(0, iCDF, 8);
/*     */ 
/*     */ 
/*     */           
/* 344 */           for (n = 0; n < encControl.nChannelsInternal; n++) {
/* 345 */             int LBRR_symbol = 0;
/* 346 */             for (int j = 0; j < (psEnc.state_Fxx[n]).nFramesPerPacket; j++) {
/* 347 */               LBRR_symbol |= Inlines.silk_LSHIFT((psEnc.state_Fxx[n]).LBRR_flags[j], j);
/*     */             }
/*     */             
/* 350 */             (psEnc.state_Fxx[n]).LBRR_flag = (byte)((LBRR_symbol > 0) ? 1 : 0);
/* 351 */             if (LBRR_symbol != 0 && (psEnc.state_Fxx[n]).nFramesPerPacket > 1) {
/* 352 */               psRangeEnc.enc_icdf(LBRR_symbol - 1, SilkTables.silk_LBRR_flags_iCDF_ptr[(psEnc.state_Fxx[n]).nFramesPerPacket - 2], 8);
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 357 */           for (int i = 0; i < (psEnc.state_Fxx[0]).nFramesPerPacket; i++) {
/* 358 */             for (n = 0; n < encControl.nChannelsInternal; n++) {
/* 359 */               if ((psEnc.state_Fxx[n]).LBRR_flags[i] != 0) {
/*     */                 int condCoding;
/*     */                 
/* 362 */                 if (encControl.nChannelsInternal == 2 && n == 0) {
/* 363 */                   Stereo.silk_stereo_encode_pred(psRangeEnc, psEnc.sStereo.predIx[i]);
/*     */                   
/* 365 */                   if ((psEnc.state_Fxx[1]).LBRR_flags[i] == 0) {
/* 366 */                     Stereo.silk_stereo_encode_mid_only(psRangeEnc, psEnc.sStereo.mid_only_flags[i]);
/*     */                   }
/*     */                 } 
/*     */ 
/*     */                 
/* 371 */                 if (i > 0 && (psEnc.state_Fxx[n]).LBRR_flags[i - 1] != 0) {
/* 372 */                   condCoding = 2;
/*     */                 } else {
/* 374 */                   condCoding = 0;
/*     */                 } 
/*     */                 
/* 377 */                 EncodeIndices.silk_encode_indices(psEnc.state_Fxx[n], psRangeEnc, i, 1, condCoding);
/* 378 */                 EncodePulses.silk_encode_pulses(psRangeEnc, ((psEnc.state_Fxx[n]).indices_LBRR[i]).signalType, ((psEnc.state_Fxx[n]).indices_LBRR[i]).quantOffsetType, (psEnc.state_Fxx[n]).pulses_LBRR[i], (psEnc.state_Fxx[n]).frame_length);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 385 */           for (n = 0; n < encControl.nChannelsInternal; n++) {
/* 386 */             Arrays.MemSet((psEnc.state_Fxx[n]).LBRR_flags, 0, 3);
/*     */           }
/*     */           
/* 389 */           psEnc.nBitsUsedLBRR = psRangeEnc.tell();
/*     */         } 
/*     */         
/* 392 */         HPVariableCutoff.silk_HP_variable_cutoff(psEnc.state_Fxx);
/*     */ 
/*     */         
/* 395 */         int nBits = Inlines.silk_DIV32_16(Inlines.silk_MUL(encControl.bitRate, encControl.payloadSize_ms), 1000);
/*     */ 
/*     */         
/* 398 */         if (prefillFlag == 0) {
/* 399 */           nBits -= psEnc.nBitsUsedLBRR;
/*     */         }
/*     */ 
/*     */         
/* 403 */         nBits = Inlines.silk_DIV32_16(nBits, (psEnc.state_Fxx[0]).nFramesPerPacket);
/*     */ 
/*     */         
/* 406 */         if (encControl.payloadSize_ms == 10) {
/* 407 */           TargetRate_bps = Inlines.silk_SMULBB(nBits, 100);
/*     */         } else {
/* 409 */           TargetRate_bps = Inlines.silk_SMULBB(nBits, 50);
/*     */         } 
/*     */ 
/*     */         
/* 413 */         TargetRate_bps -= Inlines.silk_DIV32_16(Inlines.silk_MUL(psEnc.nBitsExceeded, 1000), 500);
/*     */         
/* 415 */         if (prefillFlag == 0 && (psEnc.state_Fxx[0]).nFramesEncoded > 0) {
/*     */           
/* 417 */           int bitsBalance = psRangeEnc.tell() - psEnc.nBitsUsedLBRR - nBits * (psEnc.state_Fxx[0]).nFramesEncoded;
/* 418 */           TargetRate_bps -= Inlines.silk_DIV32_16(Inlines.silk_MUL(bitsBalance, 1000), 500);
/*     */         } 
/*     */ 
/*     */         
/* 422 */         TargetRate_bps = Inlines.silk_LIMIT(TargetRate_bps, encControl.bitRate, 5000);
/*     */ 
/*     */         
/* 425 */         if (encControl.nChannelsInternal == 2) {
/* 426 */           BoxedValueByte midOnlyFlagBoxed = new BoxedValueByte(psEnc.sStereo.mid_only_flags[(psEnc.state_Fxx[0]).nFramesEncoded]);
/* 427 */           Stereo.silk_stereo_LR_to_MS(psEnc.sStereo, (psEnc.state_Fxx[0]).inputBuf, 2, (psEnc.state_Fxx[1]).inputBuf, 2, psEnc.sStereo.predIx[(psEnc.state_Fxx[0]).nFramesEncoded], midOnlyFlagBoxed, MStargetRates_bps, TargetRate_bps, (psEnc.state_Fxx[0]).speech_activity_Q8, encControl.toMono, (psEnc.state_Fxx[0]).fs_kHz, (psEnc.state_Fxx[0]).frame_length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 441 */           psEnc.sStereo.mid_only_flags[(psEnc.state_Fxx[0]).nFramesEncoded] = midOnlyFlagBoxed.Val;
/*     */           
/* 443 */           if (midOnlyFlagBoxed.Val == 0) {
/*     */             
/* 445 */             if (psEnc.prev_decode_only_middle == 1) {
/* 446 */               (psEnc.state_Fxx[1]).sShape.Reset();
/* 447 */               (psEnc.state_Fxx[1]).sPrefilt.Reset();
/* 448 */               (psEnc.state_Fxx[1]).sNSQ.Reset();
/* 449 */               Arrays.MemSet((psEnc.state_Fxx[1]).prev_NLSFq_Q15, (short)0, 16);
/* 450 */               Arrays.MemSet((psEnc.state_Fxx[1]).sLP.In_LP_State, 0, 2);
/*     */               
/* 452 */               (psEnc.state_Fxx[1]).prevLag = 100;
/* 453 */               (psEnc.state_Fxx[1]).sNSQ.lagPrev = 100;
/* 454 */               (psEnc.state_Fxx[1]).sShape.LastGainIndex = 10;
/* 455 */               (psEnc.state_Fxx[1]).prevSignalType = 0;
/* 456 */               (psEnc.state_Fxx[1]).sNSQ.prev_gain_Q16 = 65536;
/* 457 */               (psEnc.state_Fxx[1]).first_frame_after_reset = 1;
/*     */             } 
/*     */             
/* 460 */             psEnc.state_Fxx[1].silk_encode_do_VAD();
/*     */           } else {
/* 462 */             (psEnc.state_Fxx[1]).VAD_flags[(psEnc.state_Fxx[0]).nFramesEncoded] = 0;
/*     */           } 
/*     */           
/* 465 */           if (prefillFlag == 0) {
/* 466 */             Stereo.silk_stereo_encode_pred(psRangeEnc, psEnc.sStereo.predIx[(psEnc.state_Fxx[0]).nFramesEncoded]);
/* 467 */             if ((psEnc.state_Fxx[1]).VAD_flags[(psEnc.state_Fxx[0]).nFramesEncoded] == 0) {
/* 468 */               Stereo.silk_stereo_encode_mid_only(psRangeEnc, psEnc.sStereo.mid_only_flags[(psEnc.state_Fxx[0]).nFramesEncoded]);
/*     */             }
/*     */           } 
/*     */         } else {
/*     */           
/* 473 */           System.arraycopy(psEnc.sStereo.sMid, 0, (psEnc.state_Fxx[0]).inputBuf, 0, 2);
/* 474 */           System.arraycopy((psEnc.state_Fxx[0]).inputBuf, (psEnc.state_Fxx[0]).frame_length, psEnc.sStereo.sMid, 0, 2);
/*     */         } 
/*     */         
/* 477 */         psEnc.state_Fxx[0].silk_encode_do_VAD();
/*     */ 
/*     */         
/* 480 */         for (n = 0; n < encControl.nChannelsInternal; n++) {
/*     */ 
/*     */ 
/*     */           
/* 484 */           int channelRate_bps, maxBits = encControl.maxBits;
/* 485 */           if (tot_blocks == 2 && curr_block == 0) {
/* 486 */             maxBits = maxBits * 3 / 5;
/* 487 */           } else if (tot_blocks == 3) {
/* 488 */             if (curr_block == 0) {
/* 489 */               maxBits = maxBits * 2 / 5;
/* 490 */             } else if (curr_block == 1) {
/* 491 */               maxBits = maxBits * 3 / 4;
/*     */             } 
/*     */           } 
/*     */           
/* 495 */           int useCBR = (encControl.useCBR != 0 && curr_block == tot_blocks - 1) ? 1 : 0;
/*     */           
/* 497 */           if (encControl.nChannelsInternal == 1) {
/* 498 */             channelRate_bps = TargetRate_bps;
/*     */           } else {
/* 500 */             channelRate_bps = MStargetRates_bps[n];
/* 501 */             if (n == 0 && MStargetRates_bps[1] > 0) {
/* 502 */               useCBR = 0;
/*     */               
/* 504 */               maxBits -= encControl.maxBits / tot_blocks * 2;
/*     */             } 
/*     */           } 
/*     */           
/* 508 */           if (channelRate_bps > 0) {
/*     */             int condCoding;
/*     */             
/* 511 */             psEnc.state_Fxx[n].silk_control_SNR(channelRate_bps);
/*     */ 
/*     */             
/* 514 */             if ((psEnc.state_Fxx[0]).nFramesEncoded - n <= 0) {
/* 515 */               condCoding = 0;
/* 516 */             } else if (n > 0 && psEnc.prev_decode_only_middle != 0) {
/*     */ 
/*     */               
/* 519 */               condCoding = 1;
/*     */             } else {
/* 521 */               condCoding = 2;
/*     */             } 
/*     */             
/* 524 */             ret += psEnc.state_Fxx[n].silk_encode_frame(nBytesOut, psRangeEnc, condCoding, maxBits, useCBR);
/* 525 */             Inlines.OpusAssert((ret == SilkError.SILK_NO_ERROR));
/*     */           } 
/*     */           
/* 528 */           (psEnc.state_Fxx[n]).controlled_since_last_payload = 0;
/* 529 */           (psEnc.state_Fxx[n]).inputBufIx = 0;
/* 530 */           (psEnc.state_Fxx[n]).nFramesEncoded++;
/*     */         } 
/*     */         
/* 533 */         psEnc.prev_decode_only_middle = psEnc.sStereo.mid_only_flags[(psEnc.state_Fxx[0]).nFramesEncoded - 1];
/*     */ 
/*     */         
/* 536 */         if (nBytesOut.Val > 0 && (psEnc.state_Fxx[0]).nFramesEncoded == (psEnc.state_Fxx[0]).nFramesPerPacket) {
/* 537 */           int flags = 0;
/* 538 */           for (n = 0; n < encControl.nChannelsInternal; n++) {
/* 539 */             for (int i = 0; i < (psEnc.state_Fxx[n]).nFramesPerPacket; i++) {
/* 540 */               flags = Inlines.silk_LSHIFT(flags, 1);
/* 541 */               flags |= (psEnc.state_Fxx[n]).VAD_flags[i];
/*     */             } 
/* 543 */             flags = Inlines.silk_LSHIFT(flags, 1);
/* 544 */             flags |= (psEnc.state_Fxx[n]).LBRR_flag;
/*     */           } 
/*     */           
/* 547 */           if (prefillFlag == 0) {
/* 548 */             psRangeEnc.enc_patch_initial_bits(flags, ((psEnc.state_Fxx[0]).nFramesPerPacket + 1) * encControl.nChannelsInternal);
/*     */           }
/*     */ 
/*     */           
/* 552 */           if ((psEnc.state_Fxx[0]).inDTX != 0 && (encControl.nChannelsInternal == 1 || (psEnc.state_Fxx[1]).inDTX != 0)) {
/* 553 */             nBytesOut.Val = 0;
/*     */           }
/*     */           
/* 556 */           psEnc.nBitsExceeded += nBytesOut.Val * 8;
/* 557 */           psEnc.nBitsExceeded -= Inlines.silk_DIV32_16(Inlines.silk_MUL(encControl.bitRate, encControl.payloadSize_ms), 1000);
/* 558 */           psEnc.nBitsExceeded = Inlines.silk_LIMIT(psEnc.nBitsExceeded, 0, 10000);
/*     */ 
/*     */           
/* 561 */           int speech_act_thr_for_switch_Q8 = Inlines.silk_SMLAWB(13, 3188, psEnc.timeSinceSwitchAllowed_ms);
/*     */ 
/*     */           
/* 564 */           if ((psEnc.state_Fxx[0]).speech_activity_Q8 < speech_act_thr_for_switch_Q8) {
/* 565 */             psEnc.allowBandwidthSwitch = 1;
/* 566 */             psEnc.timeSinceSwitchAllowed_ms = 0;
/*     */           } else {
/* 568 */             psEnc.allowBandwidthSwitch = 0;
/* 569 */             psEnc.timeSinceSwitchAllowed_ms += encControl.payloadSize_ms;
/*     */           } 
/*     */         } 
/*     */         
/* 573 */         if (nSamplesIn == 0) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 580 */         curr_block++; continue;
/*     */       }  break;
/*     */     } 
/* 583 */     psEnc.nPrevChannelsInternal = encControl.nChannelsInternal;
/*     */     
/* 585 */     encControl.allowBandwidthSwitch = psEnc.allowBandwidthSwitch;
/* 586 */     encControl.inWBmodeWithoutVariableLP = ((psEnc.state_Fxx[0]).fs_kHz == 16 && (psEnc.state_Fxx[0]).sLP.mode == 0) ? 1 : 0;
/* 587 */     encControl.internalSampleRate = Inlines.silk_SMULBB((psEnc.state_Fxx[0]).fs_kHz, 1000);
/* 588 */     encControl.stereoWidth_Q14 = (encControl.toMono != 0) ? 0 : psEnc.sStereo.smth_width_Q14;
/*     */     
/* 590 */     if (prefillFlag != 0) {
/* 591 */       encControl.payloadSize_ms = tmp_payloadSize_ms;
/* 592 */       encControl.complexity = tmp_complexity;
/*     */       
/* 594 */       for (n = 0; n < encControl.nChannelsInternal; n++) {
/* 595 */         (psEnc.state_Fxx[n]).controlled_since_last_payload = 0;
/* 596 */         (psEnc.state_Fxx[n]).prefillFlag = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 600 */     return ret;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\EncodeAPI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */