/*     */ package de.maxhenkel.voicechat.concentus;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OpusPacketInfo
/*     */ {
/*     */   public byte TOCByte;
/*     */   public List<Byte[]> Frames;
/*     */   public int PayloadOffset;
/*     */   
/*     */   private OpusPacketInfo(byte toc, List<Byte[]> frames, int payloadOffset) {
/*  58 */     this.TOCByte = toc;
/*  59 */     this.Frames = frames;
/*  60 */     this.PayloadOffset = payloadOffset;
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
/*     */   public static OpusPacketInfo parseOpusPacket(byte[] packet, int packet_offset, int len) throws OpusException {
/*  75 */     int numFrames = getNumFrames(packet, packet_offset, len);
/*     */     
/*  77 */     BoxedValueInt payload_offset = new BoxedValueInt(0);
/*  78 */     BoxedValueByte out_toc = new BoxedValueByte((byte)0);
/*  79 */     byte[][] frames = new byte[numFrames][];
/*  80 */     short[] size = new short[numFrames];
/*  81 */     BoxedValueInt packetOffset = new BoxedValueInt(0);
/*  82 */     int error = opus_packet_parse_impl(packet, packet_offset, len, 0, out_toc, frames, 0, size, 0, payload_offset, packetOffset);
/*  83 */     if (error < 0) {
/*  84 */       throw new OpusException("An error occurred while parsing the packet", error);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  89 */     ArrayList<Byte[]> copiedFrames = (ArrayList)new ArrayList<>();
/*  90 */     for (int c = 0; c < frames.length; c++) {
/*     */       
/*  92 */       Byte[] convertedFrame = new Byte[(frames[c]).length];
/*  93 */       for (int d = 0; d < convertedFrame.length; d++) {
/*  94 */         convertedFrame[d] = Byte.valueOf(frames[c][d]);
/*     */       }
/*  96 */       copiedFrames.add(convertedFrame);
/*     */     } 
/*     */     
/*  99 */     return new OpusPacketInfo(out_toc.Val, (List<Byte[]>)copiedFrames, payload_offset.Val);
/*     */   }
/*     */   
/*     */   public static int getNumSamplesPerFrame(byte[] packet, int packet_offset, int Fs) {
/*     */     int audiosize;
/* 104 */     if ((packet[packet_offset] & 0x80) != 0) {
/* 105 */       audiosize = packet[packet_offset] >> 3 & 0x3;
/* 106 */       audiosize = (Fs << audiosize) / 400;
/* 107 */     } else if ((packet[packet_offset] & 0x60) == 96) {
/* 108 */       audiosize = ((packet[packet_offset] & 0x8) != 0) ? (Fs / 50) : (Fs / 100);
/*     */     } else {
/* 110 */       audiosize = packet[packet_offset] >> 3 & 0x3;
/* 111 */       if (audiosize == 3) {
/* 112 */         audiosize = Fs * 60 / 1000;
/*     */       } else {
/* 114 */         audiosize = (Fs << audiosize) / 100;
/*     */       } 
/*     */     } 
/* 117 */     return audiosize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OpusBandwidth getBandwidth(byte[] packet, int packet_offset) {
/*     */     OpusBandwidth bandwidth;
/* 127 */     if ((packet[packet_offset] & 0x80) != 0) {
/* 128 */       bandwidth = OpusBandwidthHelpers.GetBandwidth(OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND) + (packet[packet_offset] >> 5 & 0x3));
/* 129 */       if (bandwidth == OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND) {
/* 130 */         bandwidth = OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND;
/*     */       }
/* 132 */     } else if ((packet[packet_offset] & 0x60) == 96) {
/*     */       
/* 134 */       bandwidth = ((packet[packet_offset] & 0x10) != 0) ? OpusBandwidth.OPUS_BANDWIDTH_FULLBAND : OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND;
/*     */     } else {
/* 136 */       bandwidth = OpusBandwidthHelpers.GetBandwidth(OpusBandwidthHelpers.GetOrdinal(OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND) + (packet[packet_offset] >> 5 & 0x3));
/*     */     } 
/* 138 */     return bandwidth;
/*     */   }
/*     */   
/*     */   public static int getNumEncodedChannels(byte[] packet, int packet_offset) {
/* 142 */     return ((packet[packet_offset] & 0x4) != 0) ? 2 : 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNumFrames(byte[] packet, int packet_offset, int len) {
/* 153 */     if (len < 1) {
/* 154 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 156 */     int count = packet[packet_offset] & 0x3;
/* 157 */     if (count == 0)
/* 158 */       return 1; 
/* 159 */     if (count != 3)
/* 160 */       return 2; 
/* 161 */     if (len < 2) {
/* 162 */       return OpusError.OPUS_INVALID_PACKET;
/*     */     }
/* 164 */     return packet[packet_offset + 1] & 0x3F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNumSamples(byte[] packet, int packet_offset, int len, int Fs) {
/* 171 */     int count = getNumFrames(packet, packet_offset, len);
/*     */     
/* 173 */     if (count < 0) {
/* 174 */       return count;
/*     */     }
/*     */     
/* 177 */     int samples = count * getNumSamplesPerFrame(packet, packet_offset, Fs);
/*     */     
/* 179 */     if (samples * 25 > Fs * 3) {
/* 180 */       return OpusError.OPUS_INVALID_PACKET;
/*     */     }
/* 182 */     return samples;
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
/*     */   public static int getNumSamples(OpusDecoder dec, byte[] packet, int packet_offset, int len) {
/* 195 */     return getNumSamples(packet, packet_offset, len, dec.Fs);
/*     */   }
/*     */   
/*     */   public static OpusMode getEncoderMode(byte[] packet, int packet_offset) {
/*     */     OpusMode mode;
/* 200 */     if ((packet[packet_offset] & 0x80) != 0) {
/* 201 */       mode = OpusMode.MODE_CELT_ONLY;
/* 202 */     } else if ((packet[packet_offset] & 0x60) == 96) {
/* 203 */       mode = OpusMode.MODE_HYBRID;
/*     */     } else {
/* 205 */       mode = OpusMode.MODE_SILK_ONLY;
/*     */     } 
/* 207 */     return mode;
/*     */   }
/*     */   
/*     */   static int encode_size(int size, byte[] data, int data_ptr) {
/* 211 */     if (size < 252) {
/* 212 */       data[data_ptr] = (byte)(size & 0xFF);
/* 213 */       return 1;
/*     */     } 
/* 215 */     int dp1 = 252 + (size & 0x3);
/* 216 */     data[data_ptr] = (byte)(dp1 & 0xFF);
/* 217 */     data[data_ptr + 1] = (byte)(size - dp1 >> 2);
/* 218 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   static int parse_size(byte[] data, int data_ptr, int len, BoxedValueShort size) {
/* 223 */     if (len < 1) {
/* 224 */       size.Val = -1;
/* 225 */       return -1;
/* 226 */     }  if (Inlines.SignedByteToUnsignedInt(data[data_ptr]) < 252) {
/* 227 */       size.Val = (short)Inlines.SignedByteToUnsignedInt(data[data_ptr]);
/* 228 */       return 1;
/* 229 */     }  if (len < 2) {
/* 230 */       size.Val = -1;
/* 231 */       return -1;
/*     */     } 
/* 233 */     size.Val = (short)(4 * Inlines.SignedByteToUnsignedInt(data[data_ptr + 1]) + Inlines.SignedByteToUnsignedInt(data[data_ptr]));
/* 234 */     return 2;
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
/*     */   static int opus_packet_parse_impl(byte[] data, int data_ptr, int len, int self_delimited, BoxedValueByte out_toc, byte[][] frames, int frames_ptr, short[] sizes, int sizes_ptr, BoxedValueInt payload_offset, BoxedValueInt packet_offset) {
/*     */     int bytes, count, ch;
/*     */     BoxedValueShort boxed_size;
/* 249 */     int pad = 0;
/* 250 */     int data0 = data_ptr;
/* 251 */     out_toc.Val = 0;
/* 252 */     payload_offset.Val = 0;
/* 253 */     packet_offset.Val = 0;
/*     */     
/* 255 */     if (sizes == null || len < 0) {
/* 256 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 258 */     if (len == 0) {
/* 259 */       return OpusError.OPUS_INVALID_PACKET;
/*     */     }
/*     */     
/* 262 */     int framesize = getNumSamplesPerFrame(data, data_ptr, 48000);
/*     */     
/* 264 */     int cbr = 0;
/* 265 */     byte toc = data[data_ptr++];
/*     */     
/* 267 */     int last_size = --len;
/* 268 */     switch (toc & 0x3) {
/*     */       
/*     */       case 0:
/* 271 */         count = 1;
/*     */         break;
/*     */       
/*     */       case 1:
/* 275 */         count = 2;
/* 276 */         cbr = 1;
/* 277 */         if (self_delimited == 0) {
/* 278 */           if ((len & 0x1) != 0) {
/* 279 */             return OpusError.OPUS_INVALID_PACKET;
/*     */           }
/* 281 */           last_size = len / 2;
/*     */           
/* 283 */           sizes[sizes_ptr] = (short)last_size;
/*     */         } 
/*     */         break;
/*     */       
/*     */       case 2:
/* 288 */         count = 2;
/* 289 */         boxed_size = new BoxedValueShort(sizes[sizes_ptr]);
/* 290 */         bytes = parse_size(data, data_ptr, len, boxed_size);
/* 291 */         sizes[sizes_ptr] = boxed_size.Val;
/* 292 */         len -= bytes;
/* 293 */         if (sizes[sizes_ptr] < 0 || sizes[sizes_ptr] > len) {
/* 294 */           return OpusError.OPUS_INVALID_PACKET;
/*     */         }
/* 296 */         data_ptr += bytes;
/* 297 */         last_size = len - sizes[sizes_ptr];
/*     */         break;
/*     */ 
/*     */       
/*     */       default:
/* 302 */         if (len < 1) {
/* 303 */           return OpusError.OPUS_INVALID_PACKET;
/*     */         }
/*     */         
/* 306 */         ch = Inlines.SignedByteToUnsignedInt(data[data_ptr++]);
/* 307 */         count = ch & 0x3F;
/* 308 */         if (count <= 0 || framesize * count > 5760) {
/* 309 */           return OpusError.OPUS_INVALID_PACKET;
/*     */         }
/* 311 */         len--;
/*     */         
/* 313 */         if ((ch & 0x40) != 0) {
/*     */           int p;
/*     */           
/*     */           do {
/* 317 */             if (len <= 0) {
/* 318 */               return OpusError.OPUS_INVALID_PACKET;
/*     */             }
/* 320 */             p = Inlines.SignedByteToUnsignedInt(data[data_ptr++]);
/* 321 */             len--;
/* 322 */             int tmp = (p == 255) ? 254 : p;
/* 323 */             len -= tmp;
/* 324 */             pad += tmp;
/* 325 */           } while (p == 255);
/*     */         } 
/* 327 */         if (len < 0) {
/* 328 */           return OpusError.OPUS_INVALID_PACKET;
/*     */         }
/*     */         
/* 331 */         cbr = ((ch & 0x80) != 0) ? 0 : 1;
/* 332 */         if (cbr == 0) {
/*     */           
/* 334 */           last_size = len;
/* 335 */           for (int j = 0; j < count - 1; j++) {
/* 336 */             boxed_size = new BoxedValueShort(sizes[sizes_ptr + j]);
/* 337 */             bytes = parse_size(data, data_ptr, len, boxed_size);
/* 338 */             sizes[sizes_ptr + j] = boxed_size.Val;
/* 339 */             len -= bytes;
/* 340 */             if (sizes[sizes_ptr + j] < 0 || sizes[sizes_ptr + j] > len) {
/* 341 */               return OpusError.OPUS_INVALID_PACKET;
/*     */             }
/* 343 */             data_ptr += bytes;
/* 344 */             last_size -= bytes + sizes[sizes_ptr + j];
/*     */           } 
/* 346 */           if (last_size < 0)
/* 347 */             return OpusError.OPUS_INVALID_PACKET;  break;
/*     */         } 
/* 349 */         if (self_delimited == 0) {
/*     */           
/* 351 */           last_size = len / count;
/* 352 */           if (last_size * count != len) {
/* 353 */             return OpusError.OPUS_INVALID_PACKET;
/*     */           }
/* 355 */           for (int j = 0; j < count - 1; j++) {
/* 356 */             sizes[sizes_ptr + j] = (short)last_size;
/*     */           }
/*     */         } 
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 363 */     if (self_delimited != 0) {
/* 364 */       boxed_size = new BoxedValueShort(sizes[sizes_ptr + count - 1]);
/* 365 */       bytes = parse_size(data, data_ptr, len, boxed_size);
/* 366 */       sizes[sizes_ptr + count - 1] = boxed_size.Val;
/* 367 */       len -= bytes;
/* 368 */       if (sizes[sizes_ptr + count - 1] < 0 || sizes[sizes_ptr + count - 1] > len) {
/* 369 */         return OpusError.OPUS_INVALID_PACKET;
/*     */       }
/* 371 */       data_ptr += bytes;
/*     */       
/* 373 */       if (cbr != 0) {
/* 374 */         if (sizes[sizes_ptr + count - 1] * count > len) {
/* 375 */           return OpusError.OPUS_INVALID_PACKET;
/*     */         }
/* 377 */         for (int j = 0; j < count - 1; j++) {
/* 378 */           sizes[sizes_ptr + j] = sizes[sizes_ptr + count - 1];
/*     */         }
/* 380 */       } else if (bytes + sizes[sizes_ptr + count - 1] > last_size) {
/* 381 */         return OpusError.OPUS_INVALID_PACKET;
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 387 */       if (last_size > 1275) {
/* 388 */         return OpusError.OPUS_INVALID_PACKET;
/*     */       }
/* 390 */       sizes[sizes_ptr + count - 1] = (short)last_size;
/*     */     } 
/*     */     
/* 393 */     payload_offset.Val = data_ptr - data0;
/*     */     
/* 395 */     for (int i = 0; i < count; i++) {
/* 396 */       if (frames != null) {
/*     */ 
/*     */         
/* 399 */         frames[frames_ptr + i] = new byte[data.length - data_ptr];
/* 400 */         System.arraycopy(data, data_ptr, frames[frames_ptr + i], 0, data.length - data_ptr);
/*     */       } 
/* 402 */       data_ptr += sizes[sizes_ptr + i];
/*     */     } 
/*     */     
/* 405 */     packet_offset.Val = pad + data_ptr - data0;
/*     */     
/* 407 */     out_toc.Val = toc;
/*     */     
/* 409 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\OpusPacketInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */