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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OpusDecoder
/*     */ {
/*     */   int channels;
/*     */   int Fs;
/*  58 */   final DecControlState DecControl = new DecControlState();
/*     */   
/*     */   int decode_gain;
/*     */   
/*     */   int stream_channels;
/*     */   OpusBandwidth bandwidth;
/*     */   OpusMode mode;
/*     */   OpusMode prev_mode;
/*     */   int frame_size;
/*     */   int prev_redundancy;
/*     */   int last_packet_duration;
/*     */   int rangeFinal;
/*  70 */   SilkDecoder SilkDecoder = new SilkDecoder();
/*  71 */   CeltDecoder Celt_Decoder = new CeltDecoder();
/*     */ 
/*     */   
/*     */   OpusDecoder() {}
/*     */   
/*     */   void reset() {
/*  77 */     this.channels = 0;
/*  78 */     this.Fs = 0;
/*     */ 
/*     */ 
/*     */     
/*  82 */     this.DecControl.Reset();
/*  83 */     this.decode_gain = 0;
/*  84 */     partialReset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void partialReset() {
/*  91 */     this.stream_channels = 0;
/*  92 */     this.bandwidth = OpusBandwidth.OPUS_BANDWIDTH_UNKNOWN;
/*  93 */     this.mode = OpusMode.MODE_UNKNOWN;
/*  94 */     this.prev_mode = OpusMode.MODE_UNKNOWN;
/*  95 */     this.frame_size = 0;
/*  96 */     this.prev_redundancy = 0;
/*  97 */     this.last_packet_duration = 0;
/*  98 */     this.rangeFinal = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int opus_decoder_init(int Fs, int channels) {
/* 109 */     if ((Fs != 48000 && Fs != 24000 && Fs != 16000 && Fs != 12000 && Fs != 8000) || (channels != 1 && channels != 2))
/*     */     {
/* 111 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 113 */     reset();
/*     */ 
/*     */     
/* 116 */     SilkDecoder silk_dec = this.SilkDecoder;
/* 117 */     CeltDecoder celt_dec = this.Celt_Decoder;
/* 118 */     this.stream_channels = this.channels = channels;
/*     */     
/* 120 */     this.Fs = Fs;
/* 121 */     this.DecControl.API_sampleRate = this.Fs;
/* 122 */     this.DecControl.nChannelsAPI = this.channels;
/*     */ 
/*     */     
/* 125 */     int ret = DecodeAPI.silk_InitDecoder(silk_dec);
/* 126 */     if (ret != 0) {
/* 127 */       return OpusError.OPUS_INTERNAL_ERROR;
/*     */     }
/*     */ 
/*     */     
/* 131 */     ret = celt_dec.celt_decoder_init(Fs, channels);
/* 132 */     if (ret != OpusError.OPUS_OK) {
/* 133 */       return OpusError.OPUS_INTERNAL_ERROR;
/*     */     }
/*     */     
/* 136 */     celt_dec.SetSignalling(0);
/*     */     
/* 138 */     this.prev_mode = OpusMode.MODE_UNKNOWN;
/* 139 */     this.frame_size = Fs / 400;
/* 140 */     return OpusError.OPUS_OK;
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
/*     */   public OpusDecoder(int Fs, int channels) throws OpusException {
/* 160 */     if (Fs != 48000 && Fs != 24000 && Fs != 16000 && Fs != 12000 && Fs != 8000) {
/* 161 */       throw new IllegalArgumentException("Sample rate is invalid (must be 8/12/16/24/48 Khz)");
/*     */     }
/* 163 */     if (channels != 1 && channels != 2) {
/* 164 */       throw new IllegalArgumentException("Number of channels must be 1 or 2");
/*     */     }
/*     */     
/* 167 */     int ret = opus_decoder_init(Fs, channels);
/* 168 */     if (ret != OpusError.OPUS_OK) {
/* 169 */       if (ret == OpusError.OPUS_BAD_ARG) {
/* 170 */         throw new IllegalArgumentException("OPUS_BAD_ARG when creating decoder");
/*     */       }
/* 172 */       throw new OpusException("Error while initializing decoder", ret);
/*     */     } 
/*     */   }
/*     */   
/* 176 */   private static final byte[] SILENCE = new byte[] { -1, -1 };
/*     */ 
/*     */   
/*     */   int opus_decode_frame(byte[] data, int data_ptr, int len, short[] pcm, int pcm_ptr, int frame_size, int decode_fec) {
/*     */     int audiosize;
/*     */     OpusMode mode;
/* 182 */     int silk_ret = 0, celt_ret = 0;
/* 183 */     EntropyCoder dec = new EntropyCoder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     short[] pcm_transition = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     int transition = 0;
/*     */     
/* 199 */     int redundancy = 0;
/* 200 */     int redundancy_bytes = 0;
/* 201 */     int celt_to_silk = 0;
/*     */ 
/*     */ 
/*     */     
/* 205 */     int redundant_rng = 0;
/*     */ 
/*     */     
/* 208 */     SilkDecoder silk_dec = this.SilkDecoder;
/* 209 */     CeltDecoder celt_dec = this.Celt_Decoder;
/* 210 */     int F20 = this.Fs / 50;
/* 211 */     int F10 = F20 >> 1;
/* 212 */     int F5 = F10 >> 1;
/* 213 */     int F2_5 = F5 >> 1;
/* 214 */     if (frame_size < F2_5)
/*     */     {
/* 216 */       return OpusError.OPUS_BUFFER_TOO_SMALL;
/*     */     }
/*     */     
/* 219 */     frame_size = Inlines.IMIN(frame_size, this.Fs / 25 * 3);
/*     */     
/* 221 */     if (len <= 1) {
/* 222 */       data = null;
/*     */       
/* 224 */       frame_size = Inlines.IMIN(frame_size, this.frame_size);
/*     */     } 
/* 226 */     if (data != null) {
/* 227 */       audiosize = this.frame_size;
/* 228 */       mode = this.mode;
/* 229 */       dec.dec_init(data, data_ptr, len);
/*     */     } else {
/* 231 */       audiosize = frame_size;
/* 232 */       mode = this.prev_mode;
/*     */       
/* 234 */       if (mode == OpusMode.MODE_UNKNOWN) {
/*     */         
/* 236 */         for (int i = pcm_ptr; i < pcm_ptr + audiosize * this.channels; i++) {
/* 237 */           pcm[i] = 0;
/*     */         }
/*     */         
/* 240 */         return audiosize;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 245 */       if (audiosize > F20) {
/*     */         do {
/* 247 */           int ret = opus_decode_frame(null, 0, 0, pcm, pcm_ptr, Inlines.IMIN(audiosize, F20), 0);
/* 248 */           if (ret < 0)
/*     */           {
/* 250 */             return ret;
/*     */           }
/* 252 */           pcm_ptr += ret * this.channels;
/* 253 */           audiosize -= ret;
/* 254 */         } while (audiosize > 0);
/*     */         
/* 256 */         return frame_size;
/* 257 */       }  if (audiosize < F20) {
/* 258 */         if (audiosize > F10) {
/* 259 */           audiosize = F10;
/* 260 */         } else if (mode != OpusMode.MODE_SILK_ONLY && audiosize > F5 && audiosize < F10) {
/* 261 */           audiosize = F5;
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 268 */     int celt_accum = (mode != OpusMode.MODE_CELT_ONLY && frame_size >= F10) ? 1 : 0;
/*     */     
/* 270 */     int pcm_transition_silk_size = 0;
/* 271 */     int pcm_transition_celt_size = 0;
/* 272 */     if (data != null && this.prev_mode != OpusMode.MODE_UNKNOWN && this.prev_mode != OpusMode.MODE_AUTO && ((mode == OpusMode.MODE_CELT_ONLY && this.prev_mode != OpusMode.MODE_CELT_ONLY && this.prev_redundancy == 0) || (mode != OpusMode.MODE_CELT_ONLY && this.prev_mode == OpusMode.MODE_CELT_ONLY))) {
/*     */       
/* 274 */       transition = 1;
/*     */       
/* 276 */       if (mode == OpusMode.MODE_CELT_ONLY) {
/* 277 */         pcm_transition_celt_size = F5 * this.channels;
/*     */       } else {
/* 279 */         pcm_transition_silk_size = F5 * this.channels;
/*     */       } 
/*     */     } 
/* 282 */     short[] pcm_transition_celt = new short[pcm_transition_celt_size];
/* 283 */     if (transition != 0 && mode == OpusMode.MODE_CELT_ONLY) {
/* 284 */       pcm_transition = pcm_transition_celt;
/* 285 */       opus_decode_frame(null, 0, 0, pcm_transition, 0, Inlines.IMIN(F5, audiosize), 0);
/*     */     } 
/* 287 */     if (audiosize > frame_size)
/*     */     {
/*     */       
/* 290 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 292 */     frame_size = audiosize;
/*     */ 
/*     */ 
/*     */     
/* 296 */     int pcm_silk_size = (mode != OpusMode.MODE_CELT_ONLY && celt_accum == 0) ? (Inlines.IMAX(F10, frame_size) * this.channels) : 0;
/* 297 */     short[] pcm_silk = new short[pcm_silk_size];
/*     */ 
/*     */     
/* 300 */     if (mode != OpusMode.MODE_CELT_ONLY) {
/*     */       short[] pcm_ptr2;
/*     */       
/* 303 */       int pcm_ptr2_ptr = 0;
/*     */       
/* 305 */       if (celt_accum != 0) {
/* 306 */         pcm_ptr2 = pcm;
/* 307 */         pcm_ptr2_ptr = pcm_ptr;
/*     */       } else {
/* 309 */         pcm_ptr2 = pcm_silk;
/* 310 */         pcm_ptr2_ptr = 0;
/*     */       } 
/*     */       
/* 313 */       if (this.prev_mode == OpusMode.MODE_CELT_ONLY) {
/* 314 */         DecodeAPI.silk_InitDecoder(silk_dec);
/*     */       }
/*     */ 
/*     */       
/* 318 */       this.DecControl.payloadSize_ms = Inlines.IMAX(10, 1000 * audiosize / this.Fs);
/*     */       
/* 320 */       if (data != null) {
/* 321 */         this.DecControl.nChannelsInternal = this.stream_channels;
/* 322 */         if (mode == OpusMode.MODE_SILK_ONLY) {
/* 323 */           if (this.bandwidth == OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND) {
/* 324 */             this.DecControl.internalSampleRate = 8000;
/* 325 */           } else if (this.bandwidth == OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND) {
/* 326 */             this.DecControl.internalSampleRate = 12000;
/* 327 */           } else if (this.bandwidth == OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND) {
/* 328 */             this.DecControl.internalSampleRate = 16000;
/*     */           } else {
/* 330 */             this.DecControl.internalSampleRate = 16000;
/* 331 */             Inlines.OpusAssert(false);
/*     */           } 
/*     */         } else {
/*     */           
/* 335 */           this.DecControl.internalSampleRate = 16000;
/*     */         } 
/*     */       } 
/*     */       
/* 339 */       int lost_flag = (data == null) ? 1 : (2 * decode_fec);
/* 340 */       int decoded_samples = 0;
/*     */       
/*     */       do {
/* 343 */         int first_frame = (decoded_samples == 0) ? 1 : 0;
/* 344 */         BoxedValueInt boxed_silk_frame_size = new BoxedValueInt(0);
/* 345 */         silk_ret = DecodeAPI.silk_Decode(silk_dec, this.DecControl, lost_flag, first_frame, dec, pcm_ptr2, pcm_ptr2_ptr, boxed_silk_frame_size);
/*     */         
/* 347 */         int silk_frame_size = boxed_silk_frame_size.Val;
/*     */         
/* 349 */         if (silk_ret != 0) {
/* 350 */           if (lost_flag != 0) {
/*     */             
/* 352 */             silk_frame_size = frame_size;
/* 353 */             Arrays.MemSetWithOffset(pcm_ptr2, (short)0, pcm_ptr2_ptr, frame_size * this.channels);
/*     */           } else {
/*     */             
/* 356 */             return OpusError.OPUS_INTERNAL_ERROR;
/*     */           } 
/*     */         }
/* 359 */         pcm_ptr2_ptr += silk_frame_size * this.channels;
/* 360 */         decoded_samples += silk_frame_size;
/* 361 */       } while (decoded_samples < frame_size);
/*     */     } 
/*     */     
/* 364 */     int start_band = 0;
/* 365 */     if (decode_fec == 0 && mode != OpusMode.MODE_CELT_ONLY && data != null && dec
/* 366 */       .tell() + 17 + 20 * ((this.mode == OpusMode.MODE_HYBRID) ? 1 : 0) <= 8 * len) {
/*     */       
/* 368 */       if (mode == OpusMode.MODE_HYBRID) {
/* 369 */         redundancy = dec.dec_bit_logp(12L);
/*     */       } else {
/* 371 */         redundancy = 1;
/*     */       } 
/* 373 */       if (redundancy != 0) {
/* 374 */         celt_to_silk = dec.dec_bit_logp(1L);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 379 */         redundancy_bytes = (mode == OpusMode.MODE_HYBRID) ? ((int)dec.dec_uint(256L) + 2) : (len - (dec.tell() + 7 >> 3));
/* 380 */         len -= redundancy_bytes;
/*     */ 
/*     */         
/* 383 */         if (len * 8 < dec.tell()) {
/* 384 */           len = 0;
/* 385 */           redundancy_bytes = 0;
/* 386 */           redundancy = 0;
/*     */         } 
/*     */         
/* 389 */         dec.storage -= redundancy_bytes;
/*     */       } 
/*     */     } 
/* 392 */     if (mode != OpusMode.MODE_CELT_ONLY) {
/* 393 */       start_band = 17;
/*     */     }
/*     */ 
/*     */     
/* 397 */     int endband = 21;
/*     */     
/* 399 */     switch (this.bandwidth) {
/*     */       case OPUS_BANDWIDTH_NARROWBAND:
/* 401 */         endband = 13;
/*     */         break;
/*     */       case OPUS_BANDWIDTH_MEDIUMBAND:
/*     */       case OPUS_BANDWIDTH_WIDEBAND:
/* 405 */         endband = 17;
/*     */         break;
/*     */       case OPUS_BANDWIDTH_SUPERWIDEBAND:
/* 408 */         endband = 19;
/*     */         break;
/*     */       case OPUS_BANDWIDTH_FULLBAND:
/* 411 */         endband = 21;
/*     */         break;
/*     */     } 
/* 414 */     celt_dec.SetEndBand(endband);
/* 415 */     celt_dec.SetChannels(this.stream_channels);
/*     */ 
/*     */     
/* 418 */     if (redundancy != 0) {
/* 419 */       transition = 0;
/* 420 */       pcm_transition_silk_size = 0;
/*     */     } 
/*     */     
/* 423 */     short[] pcm_transition_silk = new short[pcm_transition_silk_size];
/*     */     
/* 425 */     if (transition != 0 && mode != OpusMode.MODE_CELT_ONLY) {
/* 426 */       pcm_transition = pcm_transition_silk;
/* 427 */       opus_decode_frame(null, 0, 0, pcm_transition, 0, Inlines.IMIN(F5, audiosize), 0);
/*     */     } 
/*     */ 
/*     */     
/* 431 */     int redundant_audio_size = (redundancy != 0) ? (F5 * this.channels) : 0;
/* 432 */     short[] redundant_audio = new short[redundant_audio_size];
/*     */ 
/*     */     
/* 435 */     if (redundancy != 0 && celt_to_silk != 0) {
/* 436 */       celt_dec.SetStartBand(0);
/* 437 */       celt_dec.celt_decode_with_ec(data, data_ptr + len, redundancy_bytes, redundant_audio, 0, F5, null, 0);
/*     */       
/* 439 */       redundant_rng = celt_dec.GetFinalRange();
/*     */     } 
/*     */ 
/*     */     
/* 443 */     celt_dec.SetStartBand(start_band);
/*     */     
/* 445 */     if (mode != OpusMode.MODE_SILK_ONLY) {
/* 446 */       int celt_frame_size = Inlines.IMIN(F20, frame_size);
/*     */       
/* 448 */       if (mode != this.prev_mode && this.prev_mode != OpusMode.MODE_AUTO && this.prev_mode != OpusMode.MODE_UNKNOWN && this.prev_redundancy == 0) {
/* 449 */         celt_dec.ResetState();
/*     */       }
/*     */       
/* 452 */       celt_ret = celt_dec.celt_decode_with_ec((decode_fec != 0) ? null : data, data_ptr, len, pcm, pcm_ptr, celt_frame_size, dec, celt_accum);
/*     */     } else {
/*     */       
/* 455 */       if (celt_accum == 0) {
/* 456 */         for (int i = pcm_ptr; i < frame_size * this.channels + pcm_ptr; i++) {
/* 457 */           pcm[i] = 0;
/*     */         }
/*     */       }
/*     */ 
/*     */       
/* 462 */       if (this.prev_mode == OpusMode.MODE_HYBRID && (redundancy == 0 || celt_to_silk == 0 || this.prev_redundancy == 0)) {
/* 463 */         celt_dec.SetStartBand(0);
/* 464 */         celt_dec.celt_decode_with_ec(SILENCE, 0, 2, pcm, pcm_ptr, F2_5, null, celt_accum);
/*     */       } 
/*     */     } 
/*     */     
/* 468 */     if (mode != OpusMode.MODE_CELT_ONLY && celt_accum == 0) {
/* 469 */       for (int i = 0; i < frame_size * this.channels; i++) {
/* 470 */         pcm[pcm_ptr + i] = Inlines.SAT16(Inlines.ADD32(pcm[pcm_ptr + i], pcm_silk[i]));
/*     */       }
/*     */     }
/*     */     
/* 474 */     int[] window = (celt_dec.GetMode()).window;
/*     */ 
/*     */     
/* 477 */     if (redundancy != 0 && celt_to_silk == 0) {
/* 478 */       celt_dec.ResetState();
/* 479 */       celt_dec.SetStartBand(0);
/*     */       
/* 481 */       celt_dec.celt_decode_with_ec(data, data_ptr + len, redundancy_bytes, redundant_audio, 0, F5, null, 0);
/* 482 */       redundant_rng = celt_dec.GetFinalRange();
/* 483 */       CodecHelpers.smooth_fade(pcm, pcm_ptr + this.channels * (frame_size - F2_5), redundant_audio, this.channels * F2_5, pcm, pcm_ptr + this.channels * (frame_size - F2_5), F2_5, this.channels, window, this.Fs);
/*     */     } 
/*     */     
/* 486 */     if (redundancy != 0 && celt_to_silk != 0) {
/* 487 */       for (int c = 0; c < this.channels; c++) {
/* 488 */         for (int i = 0; i < F2_5; i++) {
/* 489 */           pcm[this.channels * i + c + pcm_ptr] = redundant_audio[this.channels * i + c];
/*     */         }
/*     */       } 
/* 492 */       CodecHelpers.smooth_fade(redundant_audio, this.channels * F2_5, pcm, pcm_ptr + this.channels * F2_5, pcm, pcm_ptr + this.channels * F2_5, F2_5, this.channels, window, this.Fs);
/*     */     } 
/*     */     
/* 495 */     if (transition != 0) {
/* 496 */       if (audiosize >= F5) {
/* 497 */         for (int i = 0; i < this.channels * F2_5; i++) {
/* 498 */           pcm[i] = pcm_transition[i];
/*     */         }
/* 500 */         CodecHelpers.smooth_fade(pcm_transition, this.channels * F2_5, pcm, pcm_ptr + this.channels * F2_5, pcm, pcm_ptr + this.channels * F2_5, F2_5, this.channels, window, this.Fs);
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 509 */         CodecHelpers.smooth_fade(pcm_transition, 0, pcm, pcm_ptr, pcm, pcm_ptr, F2_5, this.channels, window, this.Fs);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 515 */     if (this.decode_gain != 0) {
/*     */       
/* 517 */       int gain = Inlines.celt_exp2(Inlines.MULT16_16_P15(21771, this.decode_gain));
/* 518 */       for (int i = pcm_ptr; i < pcm_ptr + frame_size * this.channels; i++) {
/*     */         
/* 520 */         int x = Inlines.MULT16_32_P16(pcm[i], gain);
/* 521 */         pcm[i] = (short)Inlines.SATURATE(x, 32767);
/*     */       } 
/*     */     } 
/*     */     
/* 525 */     if (len <= 1) {
/* 526 */       this.rangeFinal = 0;
/*     */     } else {
/* 528 */       this.rangeFinal = (int)dec.rng ^ redundant_rng;
/*     */     } 
/*     */     
/* 531 */     this.prev_mode = mode;
/* 532 */     this.prev_redundancy = (redundancy != 0 && celt_to_silk == 0) ? 1 : 0;
/*     */     
/* 534 */     return (celt_ret < 0) ? celt_ret : audiosize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int opus_decode_native(byte[] data, int data_ptr, int len, short[] pcm_out, int pcm_out_ptr, int frame_size, int decode_fec, int self_delimited, BoxedValueInt packet_offset, int soft_clip) {
/* 543 */     packet_offset.Val = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 548 */     short[] size = new short[48];
/* 549 */     if (decode_fec < 0 || decode_fec > 1) {
/* 550 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/*     */     
/* 553 */     if ((decode_fec != 0 || len == 0 || data == null) && frame_size % this.Fs / 400 != 0) {
/* 554 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 556 */     if (len == 0 || data == null) {
/* 557 */       int pcm_count = 0;
/*     */       
/*     */       while (true)
/* 560 */       { int ret = opus_decode_frame(null, 0, 0, pcm_out, pcm_out_ptr + pcm_count * this.channels, frame_size - pcm_count, 0);
/* 561 */         if (ret < 0) {
/* 562 */           return ret;
/*     */         }
/* 564 */         pcm_count += ret;
/* 565 */         if (pcm_count >= frame_size)
/* 566 */         { Inlines.OpusAssert((pcm_count == frame_size));
/* 567 */           this.last_packet_duration = pcm_count;
/* 568 */           return pcm_count; }  } 
/* 569 */     }  if (len < 0) {
/* 570 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/*     */     
/* 573 */     OpusMode packet_mode = OpusPacketInfo.getEncoderMode(data, data_ptr);
/* 574 */     OpusBandwidth packet_bandwidth = OpusPacketInfo.getBandwidth(data, data_ptr);
/* 575 */     int packet_frame_size = OpusPacketInfo.getNumSamplesPerFrame(data, data_ptr, this.Fs);
/* 576 */     int packet_stream_channels = OpusPacketInfo.getNumEncodedChannels(data, data_ptr);
/*     */     
/* 578 */     BoxedValueByte boxed_toc = new BoxedValueByte((byte)0);
/* 579 */     BoxedValueInt boxed_offset = new BoxedValueInt(0);
/* 580 */     int count = OpusPacketInfo.opus_packet_parse_impl(data, data_ptr, len, self_delimited, boxed_toc, null, 0, size, 0, boxed_offset, packet_offset);
/*     */     
/* 582 */     int offset = boxed_offset.Val;
/*     */     
/* 584 */     if (count < 0) {
/* 585 */       return count;
/*     */     }
/*     */     
/* 588 */     data_ptr += offset;
/*     */     
/* 590 */     if (decode_fec != 0) {
/* 591 */       BoxedValueInt dummy = new BoxedValueInt(0);
/*     */ 
/*     */ 
/*     */       
/* 595 */       if (frame_size < packet_frame_size || packet_mode == OpusMode.MODE_CELT_ONLY || this.mode == OpusMode.MODE_CELT_ONLY) {
/* 596 */         return opus_decode_native(null, 0, 0, pcm_out, pcm_out_ptr, frame_size, 0, 0, dummy, soft_clip);
/*     */       }
/*     */       
/* 599 */       int duration_copy = this.last_packet_duration;
/* 600 */       if (frame_size - packet_frame_size != 0) {
/* 601 */         int j = opus_decode_native(null, 0, 0, pcm_out, pcm_out_ptr, frame_size - packet_frame_size, 0, 0, dummy, soft_clip);
/* 602 */         if (j < 0) {
/* 603 */           this.last_packet_duration = duration_copy;
/* 604 */           return j;
/*     */         } 
/* 606 */         Inlines.OpusAssert((j == frame_size - packet_frame_size));
/*     */       } 
/*     */       
/* 609 */       this.mode = packet_mode;
/* 610 */       this.bandwidth = packet_bandwidth;
/* 611 */       this.frame_size = packet_frame_size;
/* 612 */       this.stream_channels = packet_stream_channels;
/* 613 */       int ret = opus_decode_frame(data, data_ptr, size[0], pcm_out, pcm_out_ptr + this.channels * (frame_size - packet_frame_size), packet_frame_size, 1);
/*     */       
/* 615 */       if (ret < 0) {
/* 616 */         return ret;
/*     */       }
/* 618 */       this.last_packet_duration = frame_size;
/* 619 */       return frame_size;
/*     */     } 
/*     */ 
/*     */     
/* 623 */     if (count * packet_frame_size > frame_size) {
/* 624 */       return OpusError.OPUS_BUFFER_TOO_SMALL;
/*     */     }
/*     */ 
/*     */     
/* 628 */     this.mode = packet_mode;
/* 629 */     this.bandwidth = packet_bandwidth;
/* 630 */     this.frame_size = packet_frame_size;
/* 631 */     this.stream_channels = packet_stream_channels;
/*     */     
/* 633 */     int nb_samples = 0;
/* 634 */     for (int i = 0; i < count; i++) {
/*     */       
/* 636 */       int ret = opus_decode_frame(data, data_ptr, size[i], pcm_out, pcm_out_ptr + nb_samples * this.channels, frame_size - nb_samples, 0);
/* 637 */       if (ret < 0) {
/* 638 */         return ret;
/*     */       }
/* 640 */       Inlines.OpusAssert((ret == packet_frame_size));
/* 641 */       data_ptr += size[i];
/* 642 */       nb_samples += ret;
/*     */     } 
/* 644 */     this.last_packet_duration = nb_samples;
/*     */     
/* 646 */     return nb_samples;
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
/*     */   public int decode(byte[] in_data, int in_data_offset, int len, short[] out_pcm, int out_pcm_offset, int frame_size, boolean decode_fec) throws OpusException {
/* 686 */     if (frame_size <= 0) {
/* 687 */       throw new IllegalArgumentException("Frame size must be > 0");
/*     */     }
/*     */     
/*     */     try {
/* 691 */       BoxedValueInt dummy = new BoxedValueInt(0);
/* 692 */       int ret = opus_decode_native(in_data, in_data_offset, len, out_pcm, out_pcm_offset, frame_size, decode_fec ? 1 : 0, 0, dummy, 0);
/*     */       
/* 694 */       if (ret < 0) {
/*     */         
/* 696 */         if (ret == OpusError.OPUS_BAD_ARG) {
/* 697 */           throw new IllegalArgumentException("OPUS_BAD_ARG while decoding");
/*     */         }
/* 699 */         throw new OpusException("An error occurred during decoding", ret);
/*     */       } 
/*     */       
/* 702 */       return ret;
/* 703 */     } catch (ArithmeticException e) {
/* 704 */       throw new OpusException("Internal error during decoding: " + e.getMessage());
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
/*     */   public int decode(byte[] in_data, int in_data_offset, int len, byte[] out_pcm, int out_pcm_offset, int frame_size, boolean decode_fec) throws OpusException {
/* 731 */     short[] spcm = new short[Math.min(frame_size, 5760) * this.channels];
/* 732 */     int decSamples = decode(in_data, in_data_offset, len, spcm, 0, frame_size, decode_fec);
/*     */     
/* 734 */     for (int c = 0, idx = out_pcm_offset; c < spcm.length; c++) {
/* 735 */       out_pcm[idx++] = (byte)(spcm[c] & 0xFF);
/* 736 */       out_pcm[idx++] = (byte)(spcm[c] >> 8 & 0xFF);
/*     */     } 
/* 738 */     return decSamples;
/*     */   }
/*     */   
/*     */   public OpusBandwidth getBandwidth() {
/* 742 */     return this.bandwidth;
/*     */   }
/*     */   
/*     */   public int getFinalRange() {
/* 746 */     return this.rangeFinal;
/*     */   }
/*     */   
/*     */   public int getSampleRate() {
/* 750 */     return this.Fs;
/*     */   }
/*     */   
/*     */   public int getPitch() {
/* 754 */     if (this.prev_mode == OpusMode.MODE_CELT_ONLY) {
/* 755 */       return this.Celt_Decoder.GetPitch();
/*     */     }
/* 757 */     return this.DecControl.prevPitchLag;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGain() {
/* 762 */     return this.decode_gain;
/*     */   }
/*     */   
/*     */   public void setGain(int value) {
/* 766 */     if (value < -32768 || value > 32767) {
/* 767 */       throw new IllegalArgumentException("Gain must be within the range of a signed int16");
/*     */     }
/*     */     
/* 770 */     this.decode_gain = value;
/*     */   }
/*     */   
/*     */   public int getLastPacketDuration() {
/* 774 */     return this.last_packet_duration;
/*     */   }
/*     */   
/*     */   public void resetState() {
/* 778 */     partialReset();
/* 779 */     this.Celt_Decoder.ResetState();
/* 780 */     DecodeAPI.silk_InitDecoder(this.SilkDecoder);
/* 781 */     this.stream_channels = this.channels;
/* 782 */     this.frame_size = this.Fs / 400;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\OpusDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */