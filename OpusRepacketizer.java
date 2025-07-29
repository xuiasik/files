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
/*     */ public class OpusRepacketizer
/*     */ {
/*  39 */   byte toc = 0;
/*  40 */   int nb_frames = 0;
/*  41 */   final byte[][] frames = new byte[48][];
/*  42 */   final short[] len = new short[48];
/*  43 */   int framesize = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void Reset() {
/*  63 */     this.nb_frames = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OpusRepacketizer() {
/*  71 */     Reset();
/*     */   }
/*     */   
/*     */   int opus_repacketizer_cat_impl(byte[] data, int data_ptr, int len, int self_delimited) {
/*  75 */     BoxedValueByte dummy_toc = new BoxedValueByte((byte)0);
/*  76 */     BoxedValueInt dummy_offset = new BoxedValueInt(0);
/*     */ 
/*     */     
/*  79 */     if (len < 1) {
/*  80 */       return OpusError.OPUS_INVALID_PACKET;
/*     */     }
/*     */     
/*  83 */     if (this.nb_frames == 0) {
/*  84 */       this.toc = data[data_ptr];
/*  85 */       this.framesize = OpusPacketInfo.getNumSamplesPerFrame(data, data_ptr, 8000);
/*  86 */     } else if ((this.toc & 0xFC) != (data[data_ptr] & 0xFC)) {
/*     */       
/*  88 */       return OpusError.OPUS_INVALID_PACKET;
/*     */     } 
/*  90 */     int curr_nb_frames = OpusPacketInfo.getNumFrames(data, data_ptr, len);
/*  91 */     if (curr_nb_frames < 1) {
/*  92 */       return OpusError.OPUS_INVALID_PACKET;
/*     */     }
/*     */ 
/*     */     
/*  96 */     if ((curr_nb_frames + this.nb_frames) * this.framesize > 960) {
/*  97 */       return OpusError.OPUS_INVALID_PACKET;
/*     */     }
/*     */     
/* 100 */     int ret = OpusPacketInfo.opus_packet_parse_impl(data, data_ptr, len, self_delimited, dummy_toc, this.frames, this.nb_frames, this.len, this.nb_frames, dummy_offset, dummy_offset);
/* 101 */     if (ret < 1) {
/* 102 */       return ret;
/*     */     }
/*     */     
/* 105 */     this.nb_frames += curr_nb_frames;
/* 106 */     return OpusError.OPUS_OK;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int addPacket(byte[] data, int data_offset, int len) {
/* 150 */     return opus_repacketizer_cat_impl(data, data_offset, len, 0);
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
/*     */   public int getNumFrames() {
/* 166 */     return this.nb_frames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int opus_repacketizer_out_range_impl(int begin, int end, byte[] data, int data_ptr, int maxlen, int self_delimited, int pad) {
/*     */     int tot_size;
/* 175 */     if (begin < 0 || begin >= end || end > this.nb_frames)
/*     */     {
/* 177 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 179 */     int count = end - begin;
/*     */     
/* 181 */     if (self_delimited != 0) {
/* 182 */       tot_size = 1 + ((this.len[count - 1] >= 252) ? 1 : 0);
/*     */     } else {
/* 184 */       tot_size = 0;
/*     */     } 
/*     */     
/* 187 */     int ptr = data_ptr;
/* 188 */     if (count == 1) {
/*     */       
/* 190 */       tot_size += this.len[0] + 1;
/* 191 */       if (tot_size > maxlen) {
/* 192 */         return OpusError.OPUS_BUFFER_TOO_SMALL;
/*     */       }
/* 194 */       data[ptr++] = (byte)(this.toc & 0xFC);
/* 195 */     } else if (count == 2) {
/* 196 */       if (this.len[1] == this.len[0]) {
/*     */         
/* 198 */         tot_size += 2 * this.len[0] + 1;
/* 199 */         if (tot_size > maxlen) {
/* 200 */           return OpusError.OPUS_BUFFER_TOO_SMALL;
/*     */         }
/* 202 */         data[ptr++] = (byte)(this.toc & 0xFC | 0x1);
/*     */       } else {
/*     */         
/* 205 */         tot_size += this.len[0] + this.len[1] + 2 + ((this.len[0] >= 252) ? 1 : 0);
/* 206 */         if (tot_size > maxlen) {
/* 207 */           return OpusError.OPUS_BUFFER_TOO_SMALL;
/*     */         }
/* 209 */         data[ptr++] = (byte)(this.toc & 0xFC | 0x2);
/* 210 */         ptr += OpusPacketInfo.encode_size(this.len[0], data, ptr);
/*     */       } 
/*     */     } 
/* 213 */     if (count > 2 || (pad != 0 && tot_size < maxlen)) {
/*     */ 
/*     */       
/* 216 */       int pad_amount = 0;
/*     */ 
/*     */       
/* 219 */       ptr = data_ptr;
/* 220 */       if (self_delimited != 0) {
/* 221 */         tot_size = 1 + ((this.len[count - 1] >= 252) ? 1 : 0);
/*     */       } else {
/* 223 */         tot_size = 0;
/*     */       } 
/* 225 */       int vbr = 0; int j;
/* 226 */       for (j = 1; j < count; j++) {
/* 227 */         if (this.len[j] != this.len[0]) {
/* 228 */           vbr = 1;
/*     */           break;
/*     */         } 
/*     */       } 
/* 232 */       if (vbr != 0) {
/* 233 */         tot_size += 2;
/* 234 */         for (j = 0; j < count - 1; j++) {
/* 235 */           tot_size += 1 + ((this.len[j] >= 252) ? 1 : 0) + this.len[j];
/*     */         }
/* 237 */         tot_size += this.len[count - 1];
/*     */         
/* 239 */         if (tot_size > maxlen) {
/* 240 */           return OpusError.OPUS_BUFFER_TOO_SMALL;
/*     */         }
/* 242 */         data[ptr++] = (byte)(this.toc & 0xFC | 0x3);
/* 243 */         data[ptr++] = (byte)(count | 0x80);
/*     */       } else {
/* 245 */         tot_size += count * this.len[0] + 2;
/* 246 */         if (tot_size > maxlen) {
/* 247 */           return OpusError.OPUS_BUFFER_TOO_SMALL;
/*     */         }
/* 249 */         data[ptr++] = (byte)(this.toc & 0xFC | 0x3);
/* 250 */         data[ptr++] = (byte)count;
/*     */       } 
/*     */       
/* 253 */       pad_amount = (pad != 0) ? (maxlen - tot_size) : 0;
/*     */       
/* 255 */       if (pad_amount != 0) {
/*     */         
/* 257 */         data[data_ptr + 1] = (byte)(data[data_ptr + 1] | 0x40);
/* 258 */         int nb_255s = (pad_amount - 1) / 255;
/* 259 */         for (j = 0; j < nb_255s; j++) {
/* 260 */           data[ptr++] = -1;
/*     */         }
/*     */         
/* 263 */         data[ptr++] = (byte)(pad_amount - 255 * nb_255s - 1);
/* 264 */         tot_size += pad_amount;
/*     */       } 
/*     */       
/* 267 */       if (vbr != 0) {
/* 268 */         for (j = 0; j < count - 1; j++) {
/* 269 */           ptr += OpusPacketInfo.encode_size(this.len[j], data, ptr);
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/* 274 */     if (self_delimited != 0) {
/* 275 */       int sdlen = OpusPacketInfo.encode_size(this.len[count - 1], data, ptr);
/* 276 */       ptr += sdlen;
/*     */     } 
/*     */ 
/*     */     
/* 280 */     for (int i = begin; i < count + begin; i++) {
/*     */       
/* 282 */       if (data == this.frames[i]) {
/*     */ 
/*     */         
/* 285 */         Arrays.MemMove(data, 0, ptr, this.len[i]);
/*     */       } else {
/* 287 */         System.arraycopy(this.frames[i], 0, data, ptr, this.len[i]);
/*     */       } 
/* 289 */       ptr += this.len[i];
/*     */     } 
/*     */     
/* 292 */     if (pad != 0)
/*     */     {
/* 294 */       Arrays.MemSetWithOffset(data, (byte)0, ptr, data_ptr + maxlen - ptr);
/*     */     }
/*     */     
/* 297 */     return tot_size;
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
/*     */   public int createPacket(int begin, int end, byte[] data, int data_offset, int maxlen) {
/* 328 */     return opus_repacketizer_out_range_impl(begin, end, data, data_offset, maxlen, 0, 0);
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
/*     */   public int createPacket(byte[] data, int data_offset, int maxlen) {
/* 361 */     return opus_repacketizer_out_range_impl(0, this.nb_frames, data, data_offset, maxlen, 0, 0);
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
/*     */   public static int padPacket(byte[] data, int data_offset, int len, int new_len) {
/* 381 */     OpusRepacketizer rp = new OpusRepacketizer();
/*     */     
/* 383 */     if (len < 1) {
/* 384 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 386 */     if (len == new_len)
/* 387 */       return OpusError.OPUS_OK; 
/* 388 */     if (len > new_len) {
/* 389 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 391 */     rp.Reset();
/*     */     
/* 393 */     Arrays.MemMove(data, data_offset, data_offset + new_len - len, len);
/*     */     
/* 395 */     rp.addPacket(data, data_offset + new_len - len, len);
/* 396 */     int ret = rp.opus_repacketizer_out_range_impl(0, rp.nb_frames, data, data_offset, new_len, 0, 1);
/* 397 */     if (ret > 0) {
/* 398 */       return OpusError.OPUS_OK;
/*     */     }
/* 400 */     return ret;
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
/*     */   public static int unpadPacket(byte[] data, int data_offset, int len) {
/* 419 */     if (len < 1) {
/* 420 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/*     */     
/* 423 */     OpusRepacketizer rp = new OpusRepacketizer();
/* 424 */     rp.Reset();
/* 425 */     int ret = rp.addPacket(data, data_offset, len);
/* 426 */     if (ret < 0) {
/* 427 */       return ret;
/*     */     }
/* 429 */     ret = rp.opus_repacketizer_out_range_impl(0, rp.nb_frames, data, data_offset, len, 0, 0);
/* 430 */     Inlines.OpusAssert((ret > 0 && ret <= len));
/*     */     
/* 432 */     return ret;
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
/*     */   public static int padMultistreamPacket(byte[] data, int data_offset, int len, int new_len, int nb_streams) {
/* 455 */     BoxedValueByte dummy_toc = new BoxedValueByte((byte)0);
/* 456 */     short[] size = new short[48];
/* 457 */     BoxedValueInt packet_offset = new BoxedValueInt(0);
/* 458 */     BoxedValueInt dummy_offset = new BoxedValueInt(0);
/*     */ 
/*     */     
/* 461 */     if (len < 1) {
/* 462 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 464 */     if (len == new_len)
/* 465 */       return OpusError.OPUS_OK; 
/* 466 */     if (len > new_len) {
/* 467 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 469 */     int amount = new_len - len;
/*     */     
/* 471 */     for (int s = 0; s < nb_streams - 1; s++) {
/* 472 */       if (len <= 0) {
/* 473 */         return OpusError.OPUS_INVALID_PACKET;
/*     */       }
/* 475 */       int count = OpusPacketInfo.opus_packet_parse_impl(data, data_offset, len, 1, dummy_toc, null, 0, size, 0, dummy_offset, packet_offset);
/*     */       
/* 477 */       if (count < 0) {
/* 478 */         return count;
/*     */       }
/* 480 */       data_offset += packet_offset.Val;
/* 481 */       len -= packet_offset.Val;
/*     */     } 
/* 483 */     return padPacket(data, data_offset, len, len + amount);
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
/*     */   public static int unpadMultistreamPacket(byte[] data, int data_offset, int len, int nb_streams) {
/* 505 */     BoxedValueByte dummy_toc = new BoxedValueByte((byte)0);
/* 506 */     short[] size = new short[48];
/* 507 */     BoxedValueInt packet_offset = new BoxedValueInt(0);
/* 508 */     BoxedValueInt dummy_offset = new BoxedValueInt(0);
/* 509 */     OpusRepacketizer rp = new OpusRepacketizer();
/*     */ 
/*     */ 
/*     */     
/* 513 */     if (len < 1) {
/* 514 */       return OpusError.OPUS_BAD_ARG;
/*     */     }
/* 516 */     int dst = data_offset;
/* 517 */     int dst_len = 0;
/*     */     
/* 519 */     for (int s = 0; s < nb_streams; s++) {
/*     */       
/* 521 */       int self_delimited = ((s != nb_streams) ? 1 : 0) - 1;
/* 522 */       if (len <= 0) {
/* 523 */         return OpusError.OPUS_INVALID_PACKET;
/*     */       }
/* 525 */       rp.Reset();
/* 526 */       int ret = OpusPacketInfo.opus_packet_parse_impl(data, data_offset, len, self_delimited, dummy_toc, null, 0, size, 0, dummy_offset, packet_offset);
/*     */       
/* 528 */       if (ret < 0) {
/* 529 */         return ret;
/*     */       }
/* 531 */       ret = rp.opus_repacketizer_cat_impl(data, data_offset, packet_offset.Val, self_delimited);
/* 532 */       if (ret < 0) {
/* 533 */         return ret;
/*     */       }
/* 535 */       ret = rp.opus_repacketizer_out_range_impl(0, rp.nb_frames, data, dst, len, self_delimited, 0);
/* 536 */       if (ret < 0) {
/* 537 */         return ret;
/*     */       }
/* 539 */       dst_len += ret;
/*     */       
/* 541 */       dst += ret;
/* 542 */       data_offset += packet_offset.Val;
/* 543 */       len -= packet_offset.Val;
/*     */     } 
/* 545 */     return dst_len;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\OpusRepacketizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */