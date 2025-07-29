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
/*     */ public class OpusMSDecoder
/*     */ {
/*  39 */   ChannelLayout layout = new ChannelLayout();
/*  40 */   OpusDecoder[] decoders = null;
/*     */   
/*     */   private OpusMSDecoder(int nb_streams, int nb_coupled_streams) {
/*  43 */     this.decoders = new OpusDecoder[nb_streams];
/*  44 */     for (int c = 0; c < nb_streams; c++) {
/*  45 */       this.decoders[c] = new OpusDecoder();
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
/*     */   int opus_multistream_decoder_init(int Fs, int channels, int streams, int coupled_streams, short[] mapping) {
/*  57 */     int decoder_ptr = 0;
/*     */     
/*  59 */     if (channels > 255 || channels < 1 || coupled_streams > streams || streams < 1 || coupled_streams < 0 || streams > 255 - coupled_streams)
/*     */     {
/*  61 */       throw new IllegalArgumentException("Invalid channel or coupled stream count");
/*     */     }
/*     */     
/*  64 */     this.layout.nb_channels = channels;
/*  65 */     this.layout.nb_streams = streams;
/*  66 */     this.layout.nb_coupled_streams = coupled_streams;
/*     */     int i;
/*  68 */     for (i = 0; i < this.layout.nb_channels; i++) {
/*  69 */       this.layout.mapping[i] = mapping[i];
/*     */     }
/*  71 */     if (OpusMultistream.validate_layout(this.layout) == 0) {
/*  72 */       throw new IllegalArgumentException("Invalid surround channel layout");
/*     */     }
/*     */     
/*  75 */     for (i = 0; i < this.layout.nb_coupled_streams; i++) {
/*  76 */       int ret = this.decoders[decoder_ptr].opus_decoder_init(Fs, 2);
/*  77 */       if (ret != OpusError.OPUS_OK) {
/*  78 */         return ret;
/*     */       }
/*  80 */       decoder_ptr++;
/*     */     } 
/*  82 */     for (; i < this.layout.nb_streams; i++) {
/*  83 */       int ret = this.decoders[decoder_ptr].opus_decoder_init(Fs, 1);
/*  84 */       if (ret != OpusError.OPUS_OK) {
/*  85 */         return ret;
/*     */       }
/*  87 */       decoder_ptr++;
/*     */     } 
/*  89 */     return OpusError.OPUS_OK;
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
/*     */   public static OpusMSDecoder create(int Fs, int channels, int streams, int coupled_streams, short[] mapping) throws OpusException {
/* 109 */     if (channels > 255 || channels < 1 || coupled_streams > streams || streams < 1 || coupled_streams < 0 || streams > 255 - coupled_streams)
/*     */     {
/* 111 */       throw new IllegalArgumentException("Invalid channel / stream configuration");
/*     */     }
/* 113 */     OpusMSDecoder st = new OpusMSDecoder(streams, coupled_streams);
/* 114 */     int ret = st.opus_multistream_decoder_init(Fs, channels, streams, coupled_streams, mapping);
/* 115 */     if (ret != OpusError.OPUS_OK) {
/* 116 */       if (ret == OpusError.OPUS_BAD_ARG) {
/* 117 */         throw new IllegalArgumentException("Bad argument while creating MS decoder");
/*     */       }
/* 119 */       throw new OpusException("Could not create MS decoder", ret);
/*     */     } 
/* 121 */     return st;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int opus_multistream_packet_validate(byte[] data, int data_ptr, int len, int nb_streams, int Fs) {
/* 128 */     BoxedValueByte toc = new BoxedValueByte((byte)0);
/* 129 */     short[] size = new short[48];
/* 130 */     int samples = 0;
/* 131 */     BoxedValueInt packet_offset = new BoxedValueInt(0);
/* 132 */     BoxedValueInt dummy = new BoxedValueInt(0);
/*     */     
/* 134 */     for (int s = 0; s < nb_streams; s++) {
/*     */       
/* 136 */       if (len <= 0) {
/* 137 */         return OpusError.OPUS_INVALID_PACKET;
/*     */       }
/*     */       
/* 140 */       int count = OpusPacketInfo.opus_packet_parse_impl(data, data_ptr, len, (s != nb_streams - 1) ? 1 : 0, toc, null, 0, size, 0, dummy, packet_offset);
/*     */       
/* 142 */       if (count < 0) {
/* 143 */         return count;
/*     */       }
/*     */       
/* 146 */       int tmp_samples = OpusPacketInfo.getNumSamples(data, data_ptr, packet_offset.Val, Fs);
/* 147 */       if (s != 0 && samples != tmp_samples) {
/* 148 */         return OpusError.OPUS_INVALID_PACKET;
/*     */       }
/* 150 */       samples = tmp_samples;
/* 151 */       data_ptr += packet_offset.Val;
/* 152 */       len -= packet_offset.Val;
/*     */     } 
/*     */     
/* 155 */     return samples;
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
/*     */   int opus_multistream_decode_native(byte[] data, int data_ptr, int len, short[] pcm, int pcm_ptr, int frame_size, int decode_fec, int soft_clip) {
/* 171 */     int do_plc = 0;
/*     */ 
/*     */ 
/*     */     
/* 175 */     int Fs = getSampleRate();
/* 176 */     frame_size = Inlines.IMIN(frame_size, Fs / 25 * 3);
/* 177 */     short[] buf = new short[2 * frame_size];
/* 178 */     int decoder_ptr = 0;
/*     */     
/* 180 */     if (len == 0) {
/* 181 */       do_plc = 1;
/*     */     }
/* 183 */     if (len < 0) {
/* 184 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 186 */     if (do_plc == 0 && len < 2 * this.layout.nb_streams - 1) {
/* 187 */       return OpusError.OPUS_INVALID_PACKET;
/*     */     }
/* 189 */     if (do_plc == 0) {
/* 190 */       int ret = opus_multistream_packet_validate(data, data_ptr, len, this.layout.nb_streams, Fs);
/* 191 */       if (ret < 0)
/* 192 */         return ret; 
/* 193 */       if (ret > frame_size) {
/* 194 */         return OpusError.OPUS_BUFFER_TOO_SMALL;
/*     */       }
/*     */     } 
/* 197 */     for (int s = 0; s < this.layout.nb_streams; s++) {
/*     */ 
/*     */ 
/*     */       
/* 201 */       OpusDecoder dec = this.decoders[decoder_ptr++];
/*     */       
/* 203 */       if (do_plc == 0 && len <= 0) {
/* 204 */         return OpusError.OPUS_INTERNAL_ERROR;
/*     */       }
/* 206 */       BoxedValueInt packet_offset = new BoxedValueInt(0);
/* 207 */       int ret = dec.opus_decode_native(data, data_ptr, len, buf, 0, frame_size, decode_fec, 
/*     */           
/* 209 */           (s != this.layout.nb_streams - 1) ? 1 : 0, packet_offset, soft_clip);
/* 210 */       data_ptr += packet_offset.Val;
/* 211 */       len -= packet_offset.Val;
/* 212 */       if (ret <= 0) {
/* 213 */         return ret;
/*     */       }
/* 215 */       frame_size = ret;
/* 216 */       if (s < this.layout.nb_coupled_streams) {
/*     */         
/* 218 */         int prev = -1;
/*     */         int chan;
/* 220 */         while ((chan = OpusMultistream.get_left_channel(this.layout, s, prev)) != -1) {
/* 221 */           opus_copy_channel_out_short(pcm, pcm_ptr, this.layout.nb_channels, chan, buf, 0, 2, frame_size);
/*     */           
/* 223 */           prev = chan;
/*     */         } 
/* 225 */         prev = -1;
/*     */         
/* 227 */         while ((chan = OpusMultistream.get_right_channel(this.layout, s, prev)) != -1) {
/* 228 */           opus_copy_channel_out_short(pcm, pcm_ptr, this.layout.nb_channels, chan, buf, 1, 2, frame_size);
/*     */           
/* 230 */           prev = chan;
/*     */         } 
/*     */       } else {
/*     */         
/* 234 */         int prev = -1;
/*     */         int chan;
/* 236 */         while ((chan = OpusMultistream.get_mono_channel(this.layout, s, prev)) != -1) {
/* 237 */           opus_copy_channel_out_short(pcm, pcm_ptr, this.layout.nb_channels, chan, buf, 0, 1, frame_size);
/*     */           
/* 239 */           prev = chan;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 244 */     for (int c = 0; c < this.layout.nb_channels; c++) {
/* 245 */       if (this.layout.mapping[c] == 255) {
/* 246 */         opus_copy_channel_out_short(pcm, pcm_ptr, this.layout.nb_channels, c, null, 0, 0, frame_size);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 251 */     return frame_size;
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
/*     */   static void opus_copy_channel_out_short(short[] dst, int dst_ptr, int dst_stride, int dst_channel, short[] src, int src_ptr, int src_stride, int frame_size) {
/* 265 */     if (src != null) {
/* 266 */       for (int i = 0; i < frame_size; i++) {
/* 267 */         dst[i * dst_stride + dst_channel + dst_ptr] = src[i * src_stride + src_ptr];
/*     */       }
/*     */     } else {
/* 270 */       for (int i = 0; i < frame_size; i++) {
/* 271 */         dst[i * dst_stride + dst_channel + dst_ptr] = 0;
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
/*     */   public int decodeMultistream(byte[] data, int data_offset, int len, short[] out_pcm, int out_pcm_offset, int frame_size, int decode_fec) {
/* 285 */     return opus_multistream_decode_native(data, data_offset, len, out_pcm, out_pcm_offset, frame_size, decode_fec, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public OpusBandwidth getBandwidth() {
/* 290 */     if (this.decoders == null || this.decoders.length == 0) {
/* 291 */       throw new IllegalStateException("Decoder not initialized");
/*     */     }
/* 293 */     return this.decoders[0].getBandwidth();
/*     */   }
/*     */   
/*     */   public int getSampleRate() {
/* 297 */     if (this.decoders == null || this.decoders.length == 0) {
/* 298 */       throw new IllegalStateException("Decoder not initialized");
/*     */     }
/* 300 */     return this.decoders[0].getSampleRate();
/*     */   }
/*     */   
/*     */   public int getGain() {
/* 304 */     if (this.decoders == null || this.decoders.length == 0) {
/* 305 */       throw new IllegalStateException("Decoder not initialized");
/*     */     }
/* 307 */     return this.decoders[0].getGain();
/*     */   }
/*     */   
/*     */   public void setGain(int value) {
/* 311 */     for (int s = 0; s < this.layout.nb_streams; s++) {
/* 312 */       this.decoders[s].setGain(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getLastPacketDuration() {
/* 317 */     if (this.decoders == null || this.decoders.length == 0) {
/* 318 */       return OpusError.OPUS_INVALID_STATE;
/*     */     }
/* 320 */     return this.decoders[0].getLastPacketDuration();
/*     */   }
/*     */   
/*     */   public int getFinalRange() {
/* 324 */     int value = 0;
/* 325 */     for (int s = 0; s < this.layout.nb_streams; s++) {
/* 326 */       value ^= this.decoders[s].getFinalRange();
/*     */     }
/* 328 */     return value;
/*     */   }
/*     */   
/*     */   public void ResetState() {
/* 332 */     for (int s = 0; s < this.layout.nb_streams; s++) {
/* 333 */       this.decoders[s].resetState();
/*     */     }
/*     */   }
/*     */   
/*     */   public OpusDecoder GetMultistreamDecoderState(int streamId) {
/* 338 */     return this.decoders[streamId];
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\OpusMSDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */