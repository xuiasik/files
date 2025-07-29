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
/*     */ class DecodeAPI
/*     */ {
/*     */   static int silk_InitDecoder(SilkDecoder decState) {
/*  43 */     decState.Reset();
/*     */     
/*  45 */     int ret = SilkError.SILK_NO_ERROR;
/*  46 */     SilkChannelDecoder[] channel_states = decState.channel_state;
/*     */     
/*  48 */     for (int n = 0; n < 2; n++) {
/*  49 */       ret = channel_states[n].silk_init_decoder();
/*     */     }
/*     */     
/*  52 */     decState.sStereo.Reset();
/*     */ 
/*     */     
/*  55 */     decState.prev_decode_only_middle = 0;
/*     */     
/*  57 */     return ret;
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
/*     */   static int silk_Decode(SilkDecoder psDec, DecControlState decControl, int lostFlag, int newPacketFlag, EntropyCoder psRangeDec, short[] samplesOut, int samplesOut_ptr, BoxedValueInt nSamplesOut) {
/*     */     short[] samplesOut_tmp, resample_out;
/*  71 */     int resample_out_ptr, has_side, decode_only_middle = 0, ret = SilkError.SILK_NO_ERROR;
/*     */     
/*  73 */     BoxedValueInt nSamplesOutDec = new BoxedValueInt(0);
/*     */     
/*  75 */     int[] samplesOut_tmp_ptrs = new int[2];
/*     */ 
/*     */ 
/*     */     
/*  79 */     int[] MS_pred_Q13 = { 0, 0 };
/*     */ 
/*     */     
/*  82 */     SilkChannelDecoder[] channel_state = psDec.channel_state;
/*     */ 
/*     */ 
/*     */     
/*  86 */     nSamplesOut.Val = 0;
/*     */     
/*  88 */     Inlines.OpusAssert((decControl.nChannelsInternal == 1 || decControl.nChannelsInternal == 2));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     if (newPacketFlag != 0) {
/*  98 */       for (int i = 0; i < decControl.nChannelsInternal; i++) {
/*  99 */         (channel_state[i]).nFramesDecoded = 0;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 105 */     if (decControl.nChannelsInternal > psDec.nChannelsInternal) {
/* 106 */       ret += channel_state[1].silk_init_decoder();
/*     */     }
/*     */ 
/*     */     
/* 110 */     int stereo_to_mono = (decControl.nChannelsInternal == 1 && psDec.nChannelsInternal == 2 && decControl.internalSampleRate == 1000 * (channel_state[0]).fs_kHz) ? 1 : 0;
/*     */     
/* 112 */     if ((channel_state[0]).nFramesDecoded == 0) {
/* 113 */       for (int i = 0; i < decControl.nChannelsInternal; i++) {
/*     */         
/* 115 */         if (decControl.payloadSize_ms == 0) {
/*     */           
/* 117 */           (channel_state[i]).nFramesPerPacket = 1;
/* 118 */           (channel_state[i]).nb_subfr = 2;
/* 119 */         } else if (decControl.payloadSize_ms == 10) {
/* 120 */           (channel_state[i]).nFramesPerPacket = 1;
/* 121 */           (channel_state[i]).nb_subfr = 2;
/* 122 */         } else if (decControl.payloadSize_ms == 20) {
/* 123 */           (channel_state[i]).nFramesPerPacket = 1;
/* 124 */           (channel_state[i]).nb_subfr = 4;
/* 125 */         } else if (decControl.payloadSize_ms == 40) {
/* 126 */           (channel_state[i]).nFramesPerPacket = 2;
/* 127 */           (channel_state[i]).nb_subfr = 4;
/* 128 */         } else if (decControl.payloadSize_ms == 60) {
/* 129 */           (channel_state[i]).nFramesPerPacket = 3;
/* 130 */           (channel_state[i]).nb_subfr = 4;
/*     */         } else {
/* 132 */           Inlines.OpusAssert(false);
/* 133 */           return SilkError.SILK_DEC_INVALID_FRAME_SIZE;
/*     */         } 
/* 135 */         int fs_kHz_dec = (decControl.internalSampleRate >> 10) + 1;
/* 136 */         if (fs_kHz_dec != 8 && fs_kHz_dec != 12 && fs_kHz_dec != 16) {
/* 137 */           Inlines.OpusAssert(false);
/* 138 */           return SilkError.SILK_DEC_INVALID_SAMPLING_FREQUENCY;
/*     */         } 
/* 140 */         ret += channel_state[i].silk_decoder_set_fs(fs_kHz_dec, decControl.API_sampleRate);
/*     */       } 
/*     */     }
/*     */     
/* 144 */     if (decControl.nChannelsAPI == 2 && decControl.nChannelsInternal == 2 && (psDec.nChannelsAPI == 1 || psDec.nChannelsInternal == 1)) {
/* 145 */       Arrays.MemSet(psDec.sStereo.pred_prev_Q13, (short)0, 2);
/* 146 */       Arrays.MemSet(psDec.sStereo.sSide, (short)0, 2);
/* 147 */       (channel_state[1]).resampler_state.Assign((channel_state[0]).resampler_state);
/*     */     } 
/* 149 */     psDec.nChannelsAPI = decControl.nChannelsAPI;
/* 150 */     psDec.nChannelsInternal = decControl.nChannelsInternal;
/*     */     
/* 152 */     if (decControl.API_sampleRate > 48000 || decControl.API_sampleRate < 8000) {
/* 153 */       ret = SilkError.SILK_DEC_INVALID_SAMPLING_FREQUENCY;
/* 154 */       return ret;
/*     */     } 
/*     */     
/* 157 */     if (lostFlag != 1 && (channel_state[0]).nFramesDecoded == 0) {
/*     */       int i;
/*     */       
/* 160 */       for (i = 0; i < decControl.nChannelsInternal; i++) {
/* 161 */         for (int j = 0; j < (channel_state[i]).nFramesPerPacket; j++) {
/* 162 */           (channel_state[i]).VAD_flags[j] = psRangeDec.dec_bit_logp(1L);
/*     */         }
/* 164 */         (channel_state[i]).LBRR_flag = psRangeDec.dec_bit_logp(1L);
/*     */       } 
/*     */       
/* 167 */       for (i = 0; i < decControl.nChannelsInternal; i++) {
/* 168 */         Arrays.MemSet((channel_state[i]).LBRR_flags, 0, 3);
/* 169 */         if ((channel_state[i]).LBRR_flag != 0) {
/* 170 */           if ((channel_state[i]).nFramesPerPacket == 1) {
/* 171 */             (channel_state[i]).LBRR_flags[0] = 1;
/*     */           } else {
/* 173 */             int LBRR_symbol = psRangeDec.dec_icdf(SilkTables.silk_LBRR_flags_iCDF_ptr[(channel_state[i]).nFramesPerPacket - 2], 8) + 1;
/* 174 */             for (int j = 0; j < (channel_state[i]).nFramesPerPacket; j++) {
/* 175 */               (channel_state[i]).LBRR_flags[j] = Inlines.silk_RSHIFT(LBRR_symbol, j) & 0x1;
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 181 */       if (lostFlag == 0)
/*     */       {
/* 183 */         for (int j = 0; j < (channel_state[0]).nFramesPerPacket; j++) {
/* 184 */           for (i = 0; i < decControl.nChannelsInternal; i++) {
/* 185 */             if ((channel_state[i]).LBRR_flags[j] != 0) {
/* 186 */               int condCoding; short[] pulses = new short[320];
/*     */ 
/*     */               
/* 189 */               if (decControl.nChannelsInternal == 2 && i == 0) {
/* 190 */                 Stereo.silk_stereo_decode_pred(psRangeDec, MS_pred_Q13);
/* 191 */                 if ((channel_state[1]).LBRR_flags[j] == 0) {
/* 192 */                   BoxedValueInt decodeOnlyMiddleBoxed = new BoxedValueInt(decode_only_middle);
/* 193 */                   Stereo.silk_stereo_decode_mid_only(psRangeDec, decodeOnlyMiddleBoxed);
/* 194 */                   decode_only_middle = decodeOnlyMiddleBoxed.Val;
/*     */                 } 
/*     */               } 
/*     */               
/* 198 */               if (j > 0 && (channel_state[i]).LBRR_flags[j - 1] != 0) {
/* 199 */                 condCoding = 2;
/*     */               } else {
/* 201 */                 condCoding = 0;
/*     */               } 
/* 203 */               DecodeIndices.silk_decode_indices(channel_state[i], psRangeDec, j, 1, condCoding);
/* 204 */               DecodePulses.silk_decode_pulses(psRangeDec, pulses, (channel_state[i]).indices.signalType, (channel_state[i]).indices.quantOffsetType, (channel_state[i]).frame_length);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 213 */     if (decControl.nChannelsInternal == 2) {
/* 214 */       if (lostFlag == 0 || (lostFlag == 2 && (channel_state[0]).LBRR_flags[(channel_state[0]).nFramesDecoded] == 1)) {
/*     */         
/* 216 */         Stereo.silk_stereo_decode_pred(psRangeDec, MS_pred_Q13);
/*     */         
/* 218 */         if ((lostFlag == 0 && (channel_state[1]).VAD_flags[(channel_state[0]).nFramesDecoded] == 0) || (lostFlag == 2 && (channel_state[1]).LBRR_flags[(channel_state[0]).nFramesDecoded] == 0)) {
/*     */           
/* 220 */           BoxedValueInt decodeOnlyMiddleBoxed = new BoxedValueInt(decode_only_middle);
/* 221 */           Stereo.silk_stereo_decode_mid_only(psRangeDec, decodeOnlyMiddleBoxed);
/* 222 */           decode_only_middle = decodeOnlyMiddleBoxed.Val;
/*     */         } else {
/* 224 */           decode_only_middle = 0;
/*     */         } 
/*     */       } else {
/* 227 */         for (int i = 0; i < 2; i++) {
/* 228 */           MS_pred_Q13[i] = psDec.sStereo.pred_prev_Q13[i];
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 234 */     if (decControl.nChannelsInternal == 2 && decode_only_middle == 0 && psDec.prev_decode_only_middle == 1) {
/* 235 */       Arrays.MemSet((psDec.channel_state[1]).outBuf, (short)0, 480);
/* 236 */       Arrays.MemSet((psDec.channel_state[1]).sLPC_Q14_buf, 0, 16);
/* 237 */       (psDec.channel_state[1]).lagPrev = 100;
/* 238 */       (psDec.channel_state[1]).LastGainIndex = 10;
/* 239 */       (psDec.channel_state[1]).prevSignalType = 0;
/* 240 */       (psDec.channel_state[1]).first_frame_after_reset = 1;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     int delay_stack_alloc = (decControl.internalSampleRate * decControl.nChannelsInternal < decControl.API_sampleRate * decControl.nChannelsAPI) ? 1 : 0;
/*     */     
/* 249 */     if (delay_stack_alloc != 0) {
/* 250 */       samplesOut_tmp = samplesOut;
/* 251 */       samplesOut_tmp_ptrs[0] = samplesOut_ptr;
/* 252 */       samplesOut_tmp_ptrs[1] = samplesOut_ptr + (channel_state[0]).frame_length + 2;
/*     */     } else {
/* 254 */       short[] samplesOut1_tmp_storage1 = new short[decControl.nChannelsInternal * ((channel_state[0]).frame_length + 2)];
/* 255 */       samplesOut_tmp = samplesOut1_tmp_storage1;
/* 256 */       samplesOut_tmp_ptrs[0] = 0;
/* 257 */       samplesOut_tmp_ptrs[1] = (channel_state[0]).frame_length + 2;
/*     */     } 
/*     */     
/* 260 */     if (lostFlag == 0) {
/* 261 */       has_side = (decode_only_middle == 0) ? 1 : 0;
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 266 */       has_side = (psDec.prev_decode_only_middle == 0 || (decControl.nChannelsInternal == 2 && lostFlag == 2 && (channel_state[1]).LBRR_flags[(channel_state[1]).nFramesDecoded] == 1)) ? 1 : 0;
/*     */     } 
/*     */     int n;
/* 269 */     for (n = 0; n < decControl.nChannelsInternal; n++) {
/* 270 */       if (n == 0 || has_side != 0) {
/*     */ 
/*     */ 
/*     */         
/* 274 */         int condCoding, FrameIndex = (channel_state[0]).nFramesDecoded - n;
/*     */         
/* 276 */         if (FrameIndex <= 0) {
/* 277 */           condCoding = 0;
/* 278 */         } else if (lostFlag == 2) {
/* 279 */           condCoding = ((channel_state[n]).LBRR_flags[FrameIndex - 1] != 0) ? 2 : 0;
/* 280 */         } else if (n > 0 && psDec.prev_decode_only_middle != 0) {
/*     */ 
/*     */           
/* 283 */           condCoding = 1;
/*     */         } else {
/* 285 */           condCoding = 2;
/*     */         } 
/* 287 */         ret += channel_state[n].silk_decode_frame(psRangeDec, samplesOut_tmp, samplesOut_tmp_ptrs[n] + 2, nSamplesOutDec, lostFlag, condCoding);
/*     */       } else {
/* 289 */         Arrays.MemSetWithOffset(samplesOut_tmp, (short)0, samplesOut_tmp_ptrs[n] + 2, nSamplesOutDec.Val);
/*     */       } 
/* 291 */       (channel_state[n]).nFramesDecoded++;
/*     */     } 
/*     */     
/* 294 */     if (decControl.nChannelsAPI == 2 && decControl.nChannelsInternal == 2) {
/*     */       
/* 296 */       Stereo.silk_stereo_MS_to_LR(psDec.sStereo, samplesOut_tmp, samplesOut_tmp_ptrs[0], samplesOut_tmp, samplesOut_tmp_ptrs[1], MS_pred_Q13, (channel_state[0]).fs_kHz, nSamplesOutDec.Val);
/*     */     } else {
/*     */       
/* 299 */       System.arraycopy(psDec.sStereo.sMid, 0, samplesOut_tmp, samplesOut_tmp_ptrs[0], 2);
/* 300 */       System.arraycopy(samplesOut_tmp, samplesOut_tmp_ptrs[0] + nSamplesOutDec.Val, psDec.sStereo.sMid, 0, 2);
/*     */     } 
/*     */ 
/*     */     
/* 304 */     nSamplesOut.Val = Inlines.silk_DIV32(nSamplesOutDec.Val * decControl.API_sampleRate, Inlines.silk_SMULBB((channel_state[0]).fs_kHz, 1000));
/*     */ 
/*     */     
/* 307 */     if (decControl.nChannelsAPI == 2) {
/* 308 */       short[] samplesOut2_tmp = new short[nSamplesOut.Val];
/* 309 */       resample_out = samplesOut2_tmp;
/* 310 */       resample_out_ptr = 0;
/*     */     } else {
/* 312 */       resample_out = samplesOut;
/* 313 */       resample_out_ptr = samplesOut_ptr;
/*     */     } 
/*     */     
/* 316 */     if (delay_stack_alloc != 0) {
/* 317 */       short[] samplesOut1_tmp_storage2 = new short[decControl.nChannelsInternal * ((channel_state[0]).frame_length + 2)];
/* 318 */       System.arraycopy(samplesOut, samplesOut_ptr, samplesOut1_tmp_storage2, 0, decControl.nChannelsInternal * ((channel_state[0]).frame_length + 2));
/* 319 */       samplesOut_tmp = samplesOut1_tmp_storage2;
/* 320 */       samplesOut_tmp_ptrs[0] = 0;
/* 321 */       samplesOut_tmp_ptrs[1] = (channel_state[0]).frame_length + 2;
/*     */     } 
/* 323 */     for (n = 0; n < Inlines.silk_min(decControl.nChannelsAPI, decControl.nChannelsInternal); n++) {
/*     */ 
/*     */       
/* 326 */       ret += Resampler.silk_resampler((channel_state[n]).resampler_state, resample_out, resample_out_ptr, samplesOut_tmp, samplesOut_tmp_ptrs[n] + 1, nSamplesOutDec.Val);
/*     */ 
/*     */       
/* 329 */       if (decControl.nChannelsAPI == 2) {
/* 330 */         int nptr = samplesOut_ptr + n;
/* 331 */         for (int i = 0; i < nSamplesOut.Val; i++) {
/* 332 */           samplesOut[nptr + 2 * i] = resample_out[resample_out_ptr + i];
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 338 */     if (decControl.nChannelsAPI == 2 && decControl.nChannelsInternal == 1) {
/* 339 */       if (stereo_to_mono != 0) {
/*     */ 
/*     */         
/* 342 */         ret += Resampler.silk_resampler((channel_state[1]).resampler_state, resample_out, resample_out_ptr, samplesOut_tmp, samplesOut_tmp_ptrs[0] + 1, nSamplesOutDec.Val);
/*     */         
/* 344 */         for (int i = 0; i < nSamplesOut.Val; i++) {
/* 345 */           samplesOut[samplesOut_ptr + 1 + 2 * i] = resample_out[resample_out_ptr + i];
/*     */         }
/*     */       } else {
/* 348 */         for (int i = 0; i < nSamplesOut.Val; i++) {
/* 349 */           samplesOut[samplesOut_ptr + 1 + 2 * i] = samplesOut[samplesOut_ptr + 2 * i];
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 355 */     if ((channel_state[0]).prevSignalType == 2) {
/* 356 */       int[] mult_tab = { 6, 4, 3 };
/* 357 */       decControl.prevPitchLag = (channel_state[0]).lagPrev * mult_tab[(channel_state[0]).fs_kHz - 8 >> 2];
/*     */     } else {
/* 359 */       decControl.prevPitchLag = 0;
/*     */     } 
/*     */     
/* 362 */     if (lostFlag == 1) {
/*     */ 
/*     */       
/* 365 */       for (int i = 0; i < psDec.nChannelsInternal; i++) {
/* 366 */         (psDec.channel_state[i]).LastGainIndex = 10;
/*     */       }
/*     */     } else {
/* 369 */       psDec.prev_decode_only_middle = decode_only_middle;
/*     */     } 
/*     */     
/* 372 */     return ret;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\DecodeAPI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */