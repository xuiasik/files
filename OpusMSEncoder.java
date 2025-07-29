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
/*     */ public class OpusMSEncoder
/*     */ {
/*  39 */   final ChannelLayout layout = new ChannelLayout();
/*  40 */   int lfe_stream = 0;
/*  41 */   OpusApplication application = OpusApplication.OPUS_APPLICATION_AUDIO;
/*  42 */   OpusFramesize variable_duration = OpusFramesize.OPUS_FRAMESIZE_UNKNOWN;
/*  43 */   int surround = 0;
/*  44 */   int bitrate_bps = 0;
/*  45 */   final float[] subframe_mem = new float[3];
/*  46 */   OpusEncoder[] encoders = null;
/*  47 */   int[] window_mem = null;
/*  48 */   int[] preemph_mem = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetState() {
/*  67 */     this.subframe_mem[2] = 0.0F; this.subframe_mem[1] = 0.0F; this.subframe_mem[0] = 0.0F;
/*  68 */     if (this.surround != 0) {
/*  69 */       Arrays.MemSet(this.preemph_mem, 0, this.layout.nb_channels);
/*  70 */       Arrays.MemSet(this.window_mem, 0, this.layout.nb_channels * 120);
/*     */     } 
/*  72 */     int encoder_ptr = 0;
/*  73 */     for (int s = 0; s < this.layout.nb_streams; s++) {
/*  74 */       OpusEncoder enc = this.encoders[encoder_ptr++];
/*  75 */       enc.resetState();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static int validate_encoder_layout(ChannelLayout layout) {
/*  81 */     for (int s = 0; s < layout.nb_streams; s++) {
/*  82 */       if (s < layout.nb_coupled_streams) {
/*  83 */         if (OpusMultistream.get_left_channel(layout, s, -1) == -1) {
/*  84 */           return 0;
/*     */         }
/*  86 */         if (OpusMultistream.get_right_channel(layout, s, -1) == -1) {
/*  87 */           return 0;
/*     */         }
/*  89 */       } else if (OpusMultistream.get_mono_channel(layout, s, -1) == -1) {
/*  90 */         return 0;
/*     */       } 
/*     */     } 
/*  93 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   static void channel_pos(int channels, int[] pos) {
/*  98 */     if (channels == 4) {
/*  99 */       pos[0] = 1;
/* 100 */       pos[1] = 3;
/* 101 */       pos[2] = 1;
/* 102 */       pos[3] = 3;
/* 103 */     } else if (channels == 3 || channels == 5 || channels == 6) {
/* 104 */       pos[0] = 1;
/* 105 */       pos[1] = 2;
/* 106 */       pos[2] = 3;
/* 107 */       pos[3] = 1;
/* 108 */       pos[4] = 3;
/* 109 */       pos[5] = 0;
/* 110 */     } else if (channels == 7) {
/* 111 */       pos[0] = 1;
/* 112 */       pos[1] = 2;
/* 113 */       pos[2] = 3;
/* 114 */       pos[3] = 1;
/* 115 */       pos[4] = 3;
/* 116 */       pos[5] = 2;
/* 117 */       pos[6] = 0;
/* 118 */     } else if (channels == 8) {
/* 119 */       pos[0] = 1;
/* 120 */       pos[1] = 2;
/* 121 */       pos[2] = 3;
/* 122 */       pos[3] = 1;
/* 123 */       pos[4] = 3;
/* 124 */       pos[5] = 1;
/* 125 */       pos[6] = 3;
/* 126 */       pos[7] = 0;
/*     */     } 
/*     */   }
/*     */   
/* 130 */   private static final int[] diff_table = new int[] { 512, 300, 165, 87, 45, 23, 11, 6, 3, 0, 0, 0, 0, 0, 0, 0, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int MS_FRAME_TMP = 3832;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int logSum(int a, int b) {
/*     */     int max, diff;
/* 143 */     if (a > b) {
/* 144 */       max = a;
/* 145 */       diff = Inlines.SUB32(Inlines.EXTEND32(a), Inlines.EXTEND32(b));
/*     */     } else {
/* 147 */       max = b;
/* 148 */       diff = Inlines.SUB32(Inlines.EXTEND32(b), Inlines.EXTEND32(a));
/*     */     } 
/* 150 */     if (diff >= 8192) {
/* 151 */       return max;
/*     */     }
/* 153 */     int low = Inlines.SHR32(diff, 9);
/* 154 */     int frac = Inlines.SHL16(diff - Inlines.SHL16(low, 9), 6);
/* 155 */     return max + diff_table[low] + Inlines.MULT16_16_Q15(frac, Inlines.SUB16(diff_table[low + 1], diff_table[low]));
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
/*     */   static void surround_analysis(CeltMode celt_mode, short[] pcm, int pcm_ptr, int[] bandLogE, int[] mem, int[] preemph_mem, int len, int overlap, int channels, int rate) {
/* 170 */     int[] pos = { 0, 0, 0, 0, 0, 0, 0, 0 };
/*     */ 
/*     */ 
/*     */     
/* 174 */     int[][] bandE = Arrays.InitTwoDimensionalArrayInt(1, 21);
/* 175 */     int[][] maskLogE = Arrays.InitTwoDimensionalArrayInt(3, 21);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     int upsample = CeltCommon.resampling_factor(rate);
/* 181 */     int frame_size = len * upsample;
/*     */     int LM;
/* 183 */     for (LM = 0; LM < celt_mode.maxLM && 
/* 184 */       celt_mode.shortMdctSize << LM != frame_size; LM++);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     int[] input = new int[frame_size + overlap];
/* 190 */     short[] x = new short[len];
/* 191 */     int[][] freq = Arrays.InitTwoDimensionalArrayInt(1, frame_size);
/*     */     
/* 193 */     channel_pos(channels, pos);
/*     */     int c;
/* 195 */     for (c = 0; c < 3; c++) {
/* 196 */       for (int j = 0; j < 21; j++) {
/* 197 */         maskLogE[c][j] = -28672;
/*     */       }
/*     */     } 
/*     */     
/* 201 */     for (c = 0; c < channels; c++) {
/* 202 */       System.arraycopy(mem, c * overlap, input, 0, overlap);
/* 203 */       opus_copy_channel_in_short(x, 0, 1, pcm, pcm_ptr, channels, c, len);
/* 204 */       BoxedValueInt boxed_preemph = new BoxedValueInt(preemph_mem[c]);
/* 205 */       CeltCommon.celt_preemphasis(x, input, overlap, frame_size, 1, upsample, celt_mode.preemph, boxed_preemph, 0);
/* 206 */       preemph_mem[c] = boxed_preemph.Val;
/*     */       
/* 208 */       MDCT.clt_mdct_forward(celt_mode.mdct, input, 0, freq[0], 0, celt_mode.window, overlap, celt_mode.maxLM - LM, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 218 */       if (upsample != 1) {
/* 219 */         int bound = len; int k;
/* 220 */         for (k = 0; k < bound; k++) {
/* 221 */           freq[0][k] = freq[0][k] * upsample;
/*     */         }
/* 223 */         for (; k < frame_size; k++) {
/* 224 */           freq[0][k] = 0;
/*     */         }
/*     */       } 
/*     */       
/* 228 */       Bands.compute_band_energies(celt_mode, freq, bandE, 21, 1, LM);
/* 229 */       QuantizeBands.amp2Log2(celt_mode, 21, 21, bandE[0], bandLogE, 21 * c, 1);
/*     */       int j;
/* 231 */       for (j = 1; j < 21; j++) {
/* 232 */         bandLogE[21 * c + j] = Inlines.MAX16(bandLogE[21 * c + j], bandLogE[21 * c + j - 1] - 1024);
/*     */       }
/* 234 */       for (j = 19; j >= 0; j--) {
/* 235 */         bandLogE[21 * c + j] = Inlines.MAX16(bandLogE[21 * c + j], bandLogE[21 * c + j + 1] - 2048);
/*     */       }
/* 237 */       if (pos[c] == 1) {
/* 238 */         for (j = 0; j < 21; j++) {
/* 239 */           maskLogE[0][j] = logSum(maskLogE[0][j], bandLogE[21 * c + j]);
/*     */         }
/* 241 */       } else if (pos[c] == 3) {
/* 242 */         for (j = 0; j < 21; j++) {
/* 243 */           maskLogE[2][j] = logSum(maskLogE[2][j], bandLogE[21 * c + j]);
/*     */         }
/* 245 */       } else if (pos[c] == 2) {
/* 246 */         for (j = 0; j < 21; j++) {
/* 247 */           maskLogE[0][j] = logSum(maskLogE[0][j], bandLogE[21 * c + j] - 512);
/* 248 */           maskLogE[2][j] = logSum(maskLogE[2][j], bandLogE[21 * c + j] - 512);
/*     */         } 
/*     */       } 
/* 251 */       System.arraycopy(input, frame_size, mem, c * overlap, overlap);
/*     */     }  int i;
/* 253 */     for (i = 0; i < 21; i++) {
/* 254 */       maskLogE[1][i] = Inlines.MIN32(maskLogE[0][i], maskLogE[2][i]);
/*     */     }
/* 256 */     int channel_offset = Inlines.HALF16(Inlines.celt_log2(32768 / (channels - 1)));
/* 257 */     for (c = 0; c < 3; c++) {
/* 258 */       for (i = 0; i < 21; i++) {
/* 259 */         maskLogE[c][i] = maskLogE[c][i] + channel_offset;
/*     */       }
/*     */     } 
/*     */     
/* 263 */     for (c = 0; c < channels; c++) {
/*     */       
/* 265 */       if (pos[c] != 0) {
/* 266 */         int[] mask = maskLogE[pos[c] - 1];
/* 267 */         for (i = 0; i < 21; i++) {
/* 268 */           bandLogE[21 * c + i] = bandLogE[21 * c + i] - mask[i];
/*     */         }
/*     */       } else {
/* 271 */         for (i = 0; i < 21; i++) {
/* 272 */           bandLogE[21 * c + i] = 0;
/*     */         }
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
/*     */   int opus_multistream_encoder_init(int Fs, int channels, int streams, int coupled_streams, short[] mapping, OpusApplication application, int surround) {
/* 290 */     if (channels > 255 || channels < 1 || coupled_streams > streams || streams < 1 || coupled_streams < 0 || streams > 255 - coupled_streams)
/*     */     {
/* 292 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/*     */     
/* 295 */     this.layout.nb_channels = channels;
/* 296 */     this.layout.nb_streams = streams;
/* 297 */     this.layout.nb_coupled_streams = coupled_streams;
/* 298 */     this.subframe_mem[2] = 0.0F; this.subframe_mem[1] = 0.0F; this.subframe_mem[0] = 0.0F;
/* 299 */     if (surround == 0) {
/* 300 */       this.lfe_stream = -1;
/*     */     }
/* 302 */     this.bitrate_bps = -1000;
/* 303 */     this.application = application;
/* 304 */     this.variable_duration = OpusFramesize.OPUS_FRAMESIZE_ARG; int i;
/* 305 */     for (i = 0; i < this.layout.nb_channels; i++) {
/* 306 */       this.layout.mapping[i] = mapping[i];
/*     */     }
/* 308 */     if (OpusMultistream.validate_layout(this.layout) == 0 || validate_encoder_layout(this.layout) == 0) {
/* 309 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/*     */     
/* 312 */     int encoder_ptr = 0;
/*     */     
/* 314 */     for (i = 0; i < this.layout.nb_coupled_streams; i++) {
/* 315 */       int ret = this.encoders[encoder_ptr].opus_init_encoder(Fs, 2, application);
/* 316 */       if (ret != OpusError.OPUS_OK) {
/* 317 */         return ret;
/*     */       }
/* 319 */       if (i == this.lfe_stream) {
/* 320 */         this.encoders[encoder_ptr].setIsLFE(true);
/*     */       }
/* 322 */       encoder_ptr++;
/*     */     } 
/* 324 */     for (; i < this.layout.nb_streams; i++) {
/* 325 */       int ret = this.encoders[encoder_ptr].opus_init_encoder(Fs, 1, application);
/* 326 */       if (i == this.lfe_stream) {
/* 327 */         this.encoders[encoder_ptr].setIsLFE(true);
/*     */       }
/* 329 */       if (ret != OpusError.OPUS_OK) {
/* 330 */         return ret;
/*     */       }
/* 332 */       encoder_ptr++;
/*     */     } 
/* 334 */     if (surround != 0) {
/* 335 */       Arrays.MemSet(this.preemph_mem, 0, channels);
/* 336 */       Arrays.MemSet(this.window_mem, 0, channels * 120);
/*     */     } 
/* 338 */     this.surround = surround;
/* 339 */     return OpusError.OPUS_OK;
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
/*     */   int opus_multistream_surround_encoder_init(int Fs, int channels, int mapping_family, BoxedValueInt streams, BoxedValueInt coupled_streams, short[] mapping, OpusApplication application) {
/* 351 */     streams.Val = 0;
/* 352 */     coupled_streams.Val = 0;
/* 353 */     if (channels > 255 || channels < 1) {
/* 354 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 356 */     this.lfe_stream = -1;
/* 357 */     if (mapping_family == 0) {
/* 358 */       if (channels == 1) {
/* 359 */         streams.Val = 1;
/* 360 */         coupled_streams.Val = 0;
/* 361 */         mapping[0] = 0;
/* 362 */       } else if (channels == 2) {
/* 363 */         streams.Val = 1;
/* 364 */         coupled_streams.Val = 1;
/* 365 */         mapping[0] = 0;
/* 366 */         mapping[1] = 1;
/*     */       } else {
/* 368 */         return OpusError.OPUS_UNIMPLEMENTED;
/*     */       } 
/* 370 */     } else if (mapping_family == 1 && channels <= 8 && channels >= 1) {
/*     */       
/* 372 */       streams.Val = (VorbisLayout.vorbis_mappings[channels - 1]).nb_streams;
/* 373 */       coupled_streams.Val = (VorbisLayout.vorbis_mappings[channels - 1]).nb_coupled_streams;
/* 374 */       for (int i = 0; i < channels; i++) {
/* 375 */         mapping[i] = (VorbisLayout.vorbis_mappings[channels - 1]).mapping[i];
/*     */       }
/* 377 */       if (channels >= 6) {
/* 378 */         this.lfe_stream = streams.Val - 1;
/*     */       }
/* 380 */     } else if (mapping_family == 255) {
/*     */       
/* 382 */       streams.Val = channels;
/* 383 */       coupled_streams.Val = 0; byte i;
/* 384 */       for (i = 0; i < channels; i = (byte)(i + 1)) {
/* 385 */         mapping[i] = (short)i;
/*     */       }
/*     */     } else {
/* 388 */       return OpusError.OPUS_UNIMPLEMENTED;
/*     */     } 
/* 390 */     return opus_multistream_encoder_init(Fs, channels, streams.Val, coupled_streams.Val, mapping, application, (
/* 391 */         channels > 2 && mapping_family == 1) ? 1 : 0);
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
/*     */   public static OpusMSEncoder Create(int Fs, int channels, int streams, int coupled_streams, short[] mapping, OpusApplication application) throws OpusException {
/* 404 */     if (channels > 255 || channels < 1 || coupled_streams > streams || streams < 1 || coupled_streams < 0 || streams > 255 - coupled_streams)
/*     */     {
/* 406 */       throw new IllegalArgumentException("Invalid channel / stream configuration");
/*     */     }
/* 408 */     OpusMSEncoder st = new OpusMSEncoder(streams, coupled_streams);
/* 409 */     int ret = st.opus_multistream_encoder_init(Fs, channels, streams, coupled_streams, mapping, application, 0);
/* 410 */     if (ret != OpusError.OPUS_OK) {
/* 411 */       if (ret == OpusError.OPUS_BAD_ARG) {
/* 412 */         throw new IllegalArgumentException("OPUS_BAD_ARG when creating MS encoder");
/*     */       }
/* 414 */       throw new OpusException("Could not create MS encoder", ret);
/*     */     } 
/* 416 */     return st;
/*     */   }
/*     */   
/*     */   static void GetStreamCount(int channels, int mapping_family, BoxedValueInt nb_streams, BoxedValueInt nb_coupled_streams) {
/* 420 */     if (mapping_family == 0) {
/* 421 */       if (channels == 1) {
/* 422 */         nb_streams.Val = 1;
/* 423 */         nb_coupled_streams.Val = 0;
/* 424 */       } else if (channels == 2) {
/* 425 */         nb_streams.Val = 1;
/* 426 */         nb_coupled_streams.Val = 1;
/*     */       } else {
/* 428 */         throw new IllegalArgumentException("More than 2 channels requires custom mappings");
/*     */       } 
/* 430 */     } else if (mapping_family == 1 && channels <= 8 && channels >= 1) {
/* 431 */       nb_streams.Val = (VorbisLayout.vorbis_mappings[channels - 1]).nb_streams;
/* 432 */       nb_coupled_streams.Val = (VorbisLayout.vorbis_mappings[channels - 1]).nb_coupled_streams;
/* 433 */     } else if (mapping_family == 255) {
/* 434 */       nb_streams.Val = channels;
/* 435 */       nb_coupled_streams.Val = 0;
/*     */     } else {
/* 437 */       throw new IllegalArgumentException("Invalid mapping family");
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
/*     */   public static OpusMSEncoder CreateSurround(int Fs, int channels, int mapping_family, BoxedValueInt streams, BoxedValueInt coupled_streams, short[] mapping, OpusApplication application) throws OpusException {
/* 452 */     if (channels > 255 || channels < 1 || application == OpusApplication.OPUS_APPLICATION_UNIMPLEMENTED) {
/* 453 */       throw new IllegalArgumentException("Invalid channel count or application");
/*     */     }
/* 455 */     BoxedValueInt nb_streams = new BoxedValueInt(0);
/* 456 */     BoxedValueInt nb_coupled_streams = new BoxedValueInt(0);
/* 457 */     GetStreamCount(channels, mapping_family, nb_streams, nb_coupled_streams);
/*     */     
/* 459 */     OpusMSEncoder st = new OpusMSEncoder(nb_streams.Val, nb_coupled_streams.Val);
/* 460 */     int ret = st.opus_multistream_surround_encoder_init(Fs, channels, mapping_family, streams, coupled_streams, mapping, application);
/* 461 */     if (ret != OpusError.OPUS_OK) {
/* 462 */       if (ret == OpusError.OPUS_BAD_ARG) {
/* 463 */         throw new IllegalArgumentException("Bad argument passed to CreateSurround");
/*     */       }
/* 465 */       throw new OpusException("Could not create multistream encoder", ret);
/*     */     } 
/* 467 */     return st;
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
/*     */   int surround_rate_allocation(int[] out_rates, int frame_size) {
/* 484 */     int channel_rate, stream_offset, rate_sum = 0;
/*     */     
/* 486 */     OpusEncoder ptr = this.encoders[0];
/* 487 */     int Fs = ptr.getSampleRate();
/*     */     
/* 489 */     if (this.bitrate_bps > this.layout.nb_channels * 40000) {
/* 490 */       stream_offset = 20000;
/*     */     } else {
/* 492 */       stream_offset = this.bitrate_bps / this.layout.nb_channels / 2;
/*     */     } 
/* 494 */     stream_offset += 60 * (Fs / frame_size - 50);
/*     */ 
/*     */ 
/*     */     
/* 498 */     int lfe_offset = 3500 + 60 * (Fs / frame_size - 50);
/*     */     
/* 500 */     int coupled_ratio = 512;
/*     */     
/* 502 */     int lfe_ratio = 32;
/*     */ 
/*     */     
/* 505 */     if (this.bitrate_bps == -1000) {
/* 506 */       channel_rate = Fs + 60 * Fs / frame_size;
/* 507 */     } else if (this.bitrate_bps == -1) {
/* 508 */       channel_rate = 300000;
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 514 */       int nb_lfe = (this.lfe_stream != -1) ? 1 : 0;
/* 515 */       int nb_coupled = this.layout.nb_coupled_streams;
/* 516 */       int nb_uncoupled = this.layout.nb_streams - nb_coupled - nb_lfe;
/* 517 */       int total = (nb_uncoupled << 8) + coupled_ratio * nb_coupled + nb_lfe * lfe_ratio;
/*     */ 
/*     */       
/* 520 */       channel_rate = 256 * (this.bitrate_bps - lfe_offset * nb_lfe - stream_offset * (nb_coupled + nb_uncoupled)) / total;
/*     */     } 
/*     */     
/* 523 */     for (int i = 0; i < this.layout.nb_streams; i++) {
/* 524 */       if (i < this.layout.nb_coupled_streams) {
/* 525 */         out_rates[i] = stream_offset + (channel_rate * coupled_ratio >> 8);
/* 526 */       } else if (i != this.lfe_stream) {
/* 527 */         out_rates[i] = stream_offset + channel_rate;
/*     */       } else {
/* 529 */         out_rates[i] = lfe_offset + (channel_rate * lfe_ratio >> 8);
/*     */       } 
/* 531 */       out_rates[i] = Inlines.IMAX(out_rates[i], 500);
/* 532 */       rate_sum += out_rates[i];
/*     */     } 
/* 534 */     return rate_sum;
/*     */   }
/*     */   
/*     */   private OpusMSEncoder(int nb_streams, int nb_coupled_streams) {
/* 538 */     this.MS_FRAME_TMP = 3832;
/*     */     if (nb_streams < 1 || nb_coupled_streams > nb_streams || nb_coupled_streams < 0) {
/*     */       throw new IllegalArgumentException("Invalid channel count in MS encoder");
/*     */     }
/*     */     this.encoders = new OpusEncoder[nb_streams];
/*     */     for (int c = 0; c < nb_streams; c++) {
/*     */       this.encoders[c] = new OpusEncoder();
/*     */     }
/*     */     int nb_channels = nb_coupled_streams * 2 + nb_streams - nb_coupled_streams;
/*     */     this.window_mem = new int[nb_channels * 120];
/*     */     this.preemph_mem = new int[nb_channels];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int opus_multistream_encode_native(short[] pcm, int pcm_ptr, int analysis_frame_size, byte[] data, int data_ptr, int max_data_bytes, int lsb_depth, int float_api) {
/* 556 */     byte[] tmp_data = new byte[3832];
/* 557 */     OpusRepacketizer rp = new OpusRepacketizer();
/*     */ 
/*     */     
/* 560 */     int[] bitrates = new int[256];
/* 561 */     int[] bandLogE = new int[42];
/* 562 */     int[] mem = null;
/* 563 */     int[] preemph_mem = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 568 */     if (this.surround != 0) {
/* 569 */       preemph_mem = this.preemph_mem;
/* 570 */       mem = this.window_mem;
/*     */     } 
/*     */     
/* 573 */     int encoder_ptr = 0;
/* 574 */     int Fs = this.encoders[encoder_ptr].getSampleRate();
/* 575 */     int vbr = this.encoders[encoder_ptr].getUseVBR() ? 1 : 0;
/* 576 */     CeltMode celt_mode = this.encoders[encoder_ptr].GetCeltMode();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 582 */     int channels = this.layout.nb_streams + this.layout.nb_coupled_streams;
/* 583 */     int delay_compensation = this.encoders[encoder_ptr].getLookahead();
/* 584 */     delay_compensation -= Fs / 400;
/* 585 */     int frame_size = CodecHelpers.compute_frame_size(pcm, pcm_ptr, analysis_frame_size, this.variable_duration, channels, Fs, this.bitrate_bps, delay_compensation, this.subframe_mem, (this.encoders[encoder_ptr]).analysis.enabled);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 590 */     if (400 * frame_size < Fs) {
/* 591 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/*     */ 
/*     */     
/* 595 */     if (400 * frame_size != Fs && 200 * frame_size != Fs && 100 * frame_size != Fs && 50 * frame_size != Fs && 25 * frame_size != Fs && 50 * frame_size != 3 * Fs)
/*     */     {
/*     */       
/* 598 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/*     */ 
/*     */     
/* 602 */     int smallest_packet = this.layout.nb_streams * 2 - 1;
/* 603 */     if (max_data_bytes < smallest_packet) {
/* 604 */       return OpusError.OPUS_BUFFER_TOO_SMALL;
/*     */     }
/* 606 */     short[] buf = new short[2 * frame_size];
/*     */     
/* 608 */     int[] bandSMR = new int[21 * this.layout.nb_channels];
/* 609 */     if (this.surround != 0) {
/* 610 */       surround_analysis(celt_mode, pcm, pcm_ptr, bandSMR, mem, preemph_mem, frame_size, 120, this.layout.nb_channels, Fs);
/*     */     }
/*     */ 
/*     */     
/* 614 */     int rate_sum = surround_rate_allocation(bitrates, frame_size);
/*     */     
/* 616 */     if (vbr == 0) {
/* 617 */       if (this.bitrate_bps == -1000) {
/* 618 */         max_data_bytes = Inlines.IMIN(max_data_bytes, 3 * rate_sum / 24 * Fs / frame_size);
/* 619 */       } else if (this.bitrate_bps != -1) {
/* 620 */         max_data_bytes = Inlines.IMIN(max_data_bytes, Inlines.IMAX(smallest_packet, 3 * this.bitrate_bps / 24 * Fs / frame_size));
/*     */       } 
/*     */     }
/*     */     
/*     */     int s;
/* 625 */     for (s = 0; s < this.layout.nb_streams; s++) {
/* 626 */       OpusEncoder enc = this.encoders[encoder_ptr];
/* 627 */       encoder_ptr++;
/* 628 */       enc.setBitrate(bitrates[s]);
/* 629 */       if (this.surround != 0) {
/*     */         
/* 631 */         int equiv_rate = this.bitrate_bps;
/* 632 */         if (frame_size * 50 < Fs) {
/* 633 */           equiv_rate -= 60 * (Fs / frame_size - 50) * this.layout.nb_channels;
/*     */         }
/* 635 */         if (equiv_rate > 10000 * this.layout.nb_channels) {
/* 636 */           enc.setBandwidth(OpusBandwidth.OPUS_BANDWIDTH_FULLBAND);
/* 637 */         } else if (equiv_rate > 7000 * this.layout.nb_channels) {
/* 638 */           enc.setBandwidth(OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND);
/* 639 */         } else if (equiv_rate > 5000 * this.layout.nb_channels) {
/* 640 */           enc.setBandwidth(OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND);
/*     */         } else {
/* 642 */           enc.setBandwidth(OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND);
/*     */         } 
/* 644 */         if (s < this.layout.nb_coupled_streams) {
/*     */           
/* 646 */           enc.setForceMode(OpusMode.MODE_CELT_ONLY);
/* 647 */           enc.setForceChannels(2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 652 */     encoder_ptr = 0;
/*     */     
/* 654 */     int tot_size = 0;
/* 655 */     for (s = 0; s < this.layout.nb_streams; s++) {
/*     */       int c1, c2;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 661 */       rp.Reset();
/* 662 */       OpusEncoder enc = this.encoders[encoder_ptr];
/* 663 */       if (s < this.layout.nb_coupled_streams) {
/*     */ 
/*     */         
/* 666 */         int left = OpusMultistream.get_left_channel(this.layout, s, -1);
/* 667 */         int right = OpusMultistream.get_right_channel(this.layout, s, -1);
/* 668 */         opus_copy_channel_in_short(buf, 0, 2, pcm, pcm_ptr, this.layout.nb_channels, left, frame_size);
/*     */         
/* 670 */         opus_copy_channel_in_short(buf, 1, 2, pcm, pcm_ptr, this.layout.nb_channels, right, frame_size);
/*     */         
/* 672 */         encoder_ptr++;
/* 673 */         if (this.surround != 0) {
/* 674 */           for (int i = 0; i < 21; i++) {
/* 675 */             bandLogE[i] = bandSMR[21 * left + i];
/* 676 */             bandLogE[21 + i] = bandSMR[21 * right + i];
/*     */           } 
/*     */         }
/* 679 */         c1 = left;
/* 680 */         c2 = right;
/*     */       } else {
/*     */         
/* 683 */         int chan = OpusMultistream.get_mono_channel(this.layout, s, -1);
/* 684 */         opus_copy_channel_in_short(buf, 0, 1, pcm, pcm_ptr, this.layout.nb_channels, chan, frame_size);
/*     */         
/* 686 */         encoder_ptr++;
/* 687 */         if (this.surround != 0) {
/* 688 */           for (int i = 0; i < 21; i++) {
/* 689 */             bandLogE[i] = bandSMR[21 * chan + i];
/*     */           }
/*     */         }
/* 692 */         c1 = chan;
/* 693 */         c2 = -1;
/*     */       } 
/* 695 */       if (this.surround != 0) {
/* 696 */         enc.SetEnergyMask(bandLogE);
/*     */       }
/*     */ 
/*     */       
/* 700 */       int curr_max = max_data_bytes - tot_size;
/*     */       
/* 702 */       curr_max -= Inlines.IMAX(0, 2 * (this.layout.nb_streams - s - 1) - 1);
/* 703 */       curr_max = Inlines.IMIN(curr_max, 3832);
/*     */       
/* 705 */       if (s != this.layout.nb_streams - 1) {
/* 706 */         curr_max -= (curr_max > 253) ? 2 : 1;
/*     */       }
/* 708 */       if (vbr == 0 && s == this.layout.nb_streams - 1) {
/* 709 */         enc.setBitrate(curr_max * 8 * Fs / frame_size);
/*     */       }
/* 711 */       int len = enc.opus_encode_native(buf, 0, frame_size, tmp_data, 0, curr_max, lsb_depth, pcm, pcm_ptr, analysis_frame_size, c1, c2, this.layout.nb_channels, float_api);
/*     */       
/* 713 */       if (len < 0) {
/* 714 */         return len;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 719 */       rp.addPacket(tmp_data, 0, len);
/* 720 */       len = rp.opus_repacketizer_out_range_impl(0, rp.getNumFrames(), data, data_ptr, max_data_bytes - tot_size, 
/* 721 */           (s != this.layout.nb_streams - 1) ? 1 : 0, (vbr == 0 && s == this.layout.nb_streams - 1) ? 1 : 0);
/* 722 */       data_ptr += len;
/* 723 */       tot_size += len;
/*     */     } 
/*     */     
/* 726 */     return tot_size;
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
/*     */   static void opus_copy_channel_in_short(short[] dst, int dst_ptr, int dst_stride, short[] src, int src_ptr, int src_stride, int src_channel, int frame_size) {
/* 740 */     for (int i = 0; i < frame_size; i++) {
/* 741 */       dst[dst_ptr + i * dst_stride] = src[i * src_stride + src_channel + src_ptr];
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
/*     */   public int encodeMultistream(short[] pcm, int pcm_offset, int frame_size, byte[] outputBuffer, int outputBuffer_offset, int max_data_bytes) {
/* 754 */     return opus_multistream_encode_native(pcm, pcm_offset, frame_size, outputBuffer, outputBuffer_offset, max_data_bytes, 16, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBitrate() {
/* 759 */     int value = 0;
/* 760 */     int encoder_ptr = 0;
/* 761 */     for (int s = 0; s < this.layout.nb_streams; s++) {
/* 762 */       OpusEncoder enc = this.encoders[encoder_ptr++];
/* 763 */       value += enc.getBitrate();
/*     */     } 
/* 765 */     return value;
/*     */   }
/*     */   
/*     */   public void setBitrate(int value) {
/* 769 */     if (value < 0 && value != -1000 && value != -1) {
/* 770 */       throw new IllegalArgumentException("Invalid bitrate");
/*     */     }
/* 772 */     this.bitrate_bps = value;
/*     */   }
/*     */   
/*     */   public OpusApplication getApplication() {
/* 776 */     return this.encoders[0].getApplication();
/*     */   }
/*     */   
/*     */   public void setApplication(OpusApplication value) {
/* 780 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 781 */       this.encoders[encoder_ptr].setApplication(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getForceChannels() {
/* 786 */     return this.encoders[0].getForceChannels();
/*     */   }
/*     */   
/*     */   public void setForceChannels(int value) {
/* 790 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 791 */       this.encoders[encoder_ptr].setForceChannels(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public OpusBandwidth getMaxBandwidth() {
/* 796 */     return this.encoders[0].getMaxBandwidth();
/*     */   }
/*     */   
/*     */   public void setMaxBandwidth(OpusBandwidth value) {
/* 800 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 801 */       this.encoders[encoder_ptr].setMaxBandwidth(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public OpusBandwidth getBandwidth() {
/* 806 */     return this.encoders[0].getBandwidth();
/*     */   }
/*     */   
/*     */   public void setBandwidth(OpusBandwidth value) {
/* 810 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 811 */       this.encoders[encoder_ptr].setBandwidth(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean getUseDTX() {
/* 816 */     return this.encoders[0].getUseDTX();
/*     */   }
/*     */   
/*     */   public void setUseDTX(boolean value) {
/* 820 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 821 */       this.encoders[encoder_ptr].setUseDTX(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getComplexity() {
/* 826 */     return this.encoders[0].getComplexity();
/*     */   }
/*     */   
/*     */   public void setComplexity(int value) {
/* 830 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 831 */       this.encoders[encoder_ptr].setComplexity(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public OpusMode getForceMode() {
/* 836 */     return this.encoders[0].getForceMode();
/*     */   }
/*     */   
/*     */   public void setForceMode(OpusMode value) {
/* 840 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 841 */       this.encoders[encoder_ptr].setForceMode(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean getUseInbandFEC() {
/* 846 */     return this.encoders[0].getUseInbandFEC();
/*     */   }
/*     */   
/*     */   public void setUseInbandFEC(boolean value) {
/* 850 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 851 */       this.encoders[encoder_ptr].setUseInbandFEC(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getPacketLossPercent() {
/* 856 */     return this.encoders[0].getPacketLossPercent();
/*     */   }
/*     */   
/*     */   public void setPacketLossPercent(int value) {
/* 860 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 861 */       this.encoders[encoder_ptr].setPacketLossPercent(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean getUseVBR() {
/* 866 */     return this.encoders[0].getUseVBR();
/*     */   }
/*     */   
/*     */   public void setUseVBR(boolean value) {
/* 870 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 871 */       this.encoders[encoder_ptr].setUseVBR(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean getUseConstrainedVBR() {
/* 876 */     return this.encoders[0].getUseConstrainedVBR();
/*     */   }
/*     */   
/*     */   public void setUseConstrainedVBR(boolean value) {
/* 880 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 881 */       this.encoders[encoder_ptr].setUseConstrainedVBR(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public OpusSignal getSignalType() {
/* 886 */     return this.encoders[0].getSignalType();
/*     */   }
/*     */   
/*     */   public void setSignalType(OpusSignal value) {
/* 890 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 891 */       this.encoders[encoder_ptr].setSignalType(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getLookahead() {
/* 896 */     return this.encoders[0].getLookahead();
/*     */   }
/*     */   
/*     */   public int getSampleRate() {
/* 900 */     return this.encoders[0].getSampleRate();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFinalRange() {
/* 905 */     int value = 0;
/* 906 */     int encoder_ptr = 0;
/* 907 */     for (int s = 0; s < this.layout.nb_streams; s++) {
/* 908 */       value ^= this.encoders[encoder_ptr++].getFinalRange();
/*     */     }
/* 910 */     return value;
/*     */   }
/*     */   
/*     */   public int getLSBDepth() {
/* 914 */     return this.encoders[0].getLSBDepth();
/*     */   }
/*     */   
/*     */   public void setLSBDepth(int value) {
/* 918 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 919 */       this.encoders[encoder_ptr].setLSBDepth(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean getPredictionDisabled() {
/* 924 */     return this.encoders[0].getPredictionDisabled();
/*     */   }
/*     */   
/*     */   public void setPredictionDisabled(boolean value) {
/* 928 */     for (int encoder_ptr = 0; encoder_ptr < this.layout.nb_streams; encoder_ptr++) {
/* 929 */       this.encoders[encoder_ptr].setPredictionDisabled(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public OpusFramesize getExpertFrameDuration() {
/* 934 */     return this.variable_duration;
/*     */   }
/*     */   
/*     */   public void setExpertFrameDuration(OpusFramesize value) {
/* 938 */     this.variable_duration = value;
/*     */   }
/*     */   
/*     */   public OpusEncoder getMultistreamEncoderState(int streamId) {
/* 942 */     if (streamId >= this.layout.nb_streams) {
/* 943 */       throw new IllegalArgumentException("Requested stream doesn't exist");
/*     */     }
/* 945 */     return this.encoders[streamId];
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\OpusMSEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */