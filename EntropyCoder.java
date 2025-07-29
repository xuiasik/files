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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EntropyCoder
/*     */ {
/*  88 */   private final int EC_WINDOW_SIZE = 32;
/*     */ 
/*     */   
/*  91 */   private final int EC_UINT_BITS = 8;
/*     */ 
/*     */ 
/*     */   
/*     */   static final int BITRES = 3;
/*     */ 
/*     */   
/*  98 */   private final int EC_SYM_BITS = 8;
/*     */ 
/*     */   
/* 101 */   private final int EC_CODE_BITS = 32;
/*     */ 
/*     */   
/* 104 */   private final long EC_SYM_MAX = 255L;
/*     */ 
/*     */   
/* 107 */   private final int EC_CODE_SHIFT = 23;
/*     */ 
/*     */   
/* 110 */   private final long EC_CODE_TOP = 2147483648L;
/*     */ 
/*     */   
/* 113 */   private final long EC_CODE_BOT = 8388608L;
/*     */ 
/*     */   
/* 116 */   private final int EC_CODE_EXTRA = 7;
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] buf;
/*     */ 
/*     */ 
/*     */   
/*     */   private int buf_ptr;
/*     */ 
/*     */   
/*     */   int storage;
/*     */ 
/*     */   
/*     */   int end_offs;
/*     */ 
/*     */   
/*     */   long end_window;
/*     */ 
/*     */   
/*     */   int nend_bits;
/*     */ 
/*     */   
/*     */   int nbits_total;
/*     */ 
/*     */   
/*     */   int offs;
/*     */ 
/*     */   
/*     */   long rng;
/*     */ 
/*     */   
/*     */   long val;
/*     */ 
/*     */   
/*     */   long ext;
/*     */ 
/*     */   
/*     */   int rem;
/*     */ 
/*     */   
/*     */   int error;
/*     */ 
/*     */ 
/*     */   
/*     */   EntropyCoder() {
/* 162 */     Reset();
/*     */   }
/*     */   
/*     */   void Reset() {
/* 166 */     this.buf = null;
/* 167 */     this.buf_ptr = 0;
/* 168 */     this.storage = 0;
/* 169 */     this.end_offs = 0;
/* 170 */     this.end_window = 0L;
/* 171 */     this.nend_bits = 0;
/* 172 */     this.offs = 0;
/* 173 */     this.rng = 0L;
/* 174 */     this.val = 0L;
/* 175 */     this.ext = 0L;
/* 176 */     this.rem = 0;
/* 177 */     this.error = 0;
/*     */   }
/*     */   
/*     */   void Assign(EntropyCoder other) {
/* 181 */     this.buf = other.buf;
/* 182 */     this.buf_ptr = other.buf_ptr;
/* 183 */     this.storage = other.storage;
/* 184 */     this.end_offs = other.end_offs;
/* 185 */     this.end_window = other.end_window;
/* 186 */     this.nend_bits = other.nend_bits;
/* 187 */     this.nbits_total = other.nbits_total;
/* 188 */     this.offs = other.offs;
/* 189 */     this.rng = other.rng;
/* 190 */     this.val = other.val;
/* 191 */     this.ext = other.ext;
/* 192 */     this.rem = other.rem;
/* 193 */     this.error = other.error;
/*     */   }
/*     */   
/*     */   byte[] get_buffer() {
/* 197 */     byte[] convertedBuf = new byte[this.storage];
/* 198 */     System.arraycopy(this.buf, this.buf_ptr, convertedBuf, 0, this.storage);
/* 199 */     return convertedBuf;
/*     */   }
/*     */   
/*     */   void write_buffer(byte[] data, int data_ptr, int target_offset, int size) {
/* 203 */     System.arraycopy(data, data_ptr, this.buf, this.buf_ptr + target_offset, size);
/*     */   }
/*     */   
/*     */   int read_byte() {
/* 207 */     return (this.offs < this.storage) ? Inlines.SignedByteToUnsignedInt(this.buf[this.buf_ptr + this.offs++]) : 0;
/*     */   }
/*     */   
/*     */   int read_byte_from_end() {
/* 211 */     return (this.end_offs < this.storage) ? 
/* 212 */       Inlines.SignedByteToUnsignedInt(this.buf[this.buf_ptr + this.storage - ++this.end_offs]) : 0;
/*     */   }
/*     */   
/*     */   int write_byte(long _value) {
/* 216 */     if (this.offs + this.end_offs >= this.storage) {
/* 217 */       return -1;
/*     */     }
/* 219 */     this.buf[this.buf_ptr + this.offs++] = (byte)(int)(_value & 0xFFL);
/* 220 */     return 0;
/*     */   }
/*     */   
/*     */   int write_byte_at_end(long _value) {
/* 224 */     if (this.offs + this.end_offs >= this.storage) {
/* 225 */       return -1;
/*     */     }
/*     */     
/* 228 */     this.buf[this.buf_ptr + this.storage - ++this.end_offs] = (byte)(int)(_value & 0xFFL);
/* 229 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void dec_normalize() {
/* 238 */     while (this.rng <= 8388608L) {
/*     */       
/* 240 */       this.nbits_total += 8;
/* 241 */       this.rng = Inlines.CapToUInt32(this.rng << 8L);
/*     */ 
/*     */       
/* 244 */       int sym = this.rem;
/*     */ 
/*     */       
/* 247 */       this.rem = read_byte();
/*     */ 
/*     */       
/* 250 */       sym = (sym << 8 | this.rem) >> 1;
/*     */ 
/*     */       
/* 253 */       this.val = Inlines.CapToUInt32((this.val << 8L) + (0xFFL & (sym ^ 0xFFFFFFFF)) & 0x7FFFFFFFL);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void dec_init(byte[] _buf, int _buf_ptr, int _storage) {
/* 259 */     this.buf = _buf;
/* 260 */     this.buf_ptr = _buf_ptr;
/* 261 */     this.storage = _storage;
/* 262 */     this.end_offs = 0;
/* 263 */     this.end_window = 0L;
/* 264 */     this.nend_bits = 0;
/*     */ 
/*     */ 
/*     */     
/* 268 */     this.nbits_total = 9;
/*     */     
/* 270 */     this.offs = 0;
/* 271 */     this.rng = 128L;
/* 272 */     this.rem = read_byte();
/* 273 */     this.val = Inlines.CapToUInt32(this.rng - 1L - (this.rem >> 1));
/* 274 */     this.error = 0;
/*     */     
/* 276 */     dec_normalize();
/*     */   }
/*     */   
/*     */   long decode(long _ft) {
/* 280 */     _ft = Inlines.CapToUInt32(_ft);
/* 281 */     this.ext = Inlines.CapToUInt32(this.rng / _ft);
/* 282 */     long s = Inlines.CapToUInt32(this.val / this.ext);
/* 283 */     return Inlines.CapToUInt32(_ft - Inlines.EC_MINI(Inlines.CapToUInt32(s + 1L), _ft));
/*     */   }
/*     */   
/*     */   long decode_bin(int _bits) {
/* 287 */     this.ext = this.rng >> _bits;
/* 288 */     long s = Inlines.CapToUInt32(this.val / this.ext);
/* 289 */     return Inlines.CapToUInt32(Inlines.CapToUInt32(1L << _bits) - Inlines.EC_MINI(Inlines.CapToUInt32(s + 1L), 1L << _bits));
/*     */   }
/*     */   
/*     */   void dec_update(long _fl, long _fh, long _ft) {
/* 293 */     _fl = Inlines.CapToUInt32(_fl);
/* 294 */     _fh = Inlines.CapToUInt32(_fh);
/* 295 */     _ft = Inlines.CapToUInt32(_ft);
/* 296 */     long s = Inlines.CapToUInt32(this.ext * (_ft - _fh));
/* 297 */     this.val -= s;
/* 298 */     this.rng = (_fl > 0L) ? Inlines.CapToUInt32(this.ext * (_fh - _fl)) : (this.rng - s);
/* 299 */     dec_normalize();
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
/*     */   int dec_bit_logp(long _logp) {
/* 313 */     long r = this.rng;
/* 314 */     long d = this.val;
/* 315 */     long s = r >> (int)_logp;
/* 316 */     int ret = (d < s) ? 1 : 0;
/* 317 */     if (ret == 0) {
/* 318 */       this.val = Inlines.CapToUInt32(d - s);
/*     */     }
/* 320 */     this.rng = (ret != 0) ? s : (r - s);
/* 321 */     dec_normalize();
/* 322 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   int dec_icdf(short[] _icdf, int _ftb) {
/* 327 */     long s = this.rng;
/* 328 */     long d = this.val;
/* 329 */     long r = s >> _ftb;
/* 330 */     int ret = -1;
/*     */     while (true) {
/* 332 */       long t = s;
/* 333 */       s = Inlines.CapToUInt32(r * _icdf[++ret]);
/* 334 */       if (d >= s) {
/* 335 */         this.val = Inlines.CapToUInt32(d - s);
/* 336 */         this.rng = Inlines.CapToUInt32(t - s);
/* 337 */         dec_normalize();
/* 338 */         return ret;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   int dec_icdf(short[] _icdf, int _icdf_offset, int _ftb) {
/* 343 */     long s = this.rng;
/* 344 */     long d = this.val;
/* 345 */     long r = s >> _ftb;
/* 346 */     int ret = _icdf_offset - 1;
/*     */     while (true) {
/* 348 */       long t = s;
/* 349 */       s = Inlines.CapToUInt32(r * _icdf[++ret]);
/* 350 */       if (d >= s) {
/* 351 */         this.val = Inlines.CapToUInt32(d - s);
/* 352 */         this.rng = Inlines.CapToUInt32(t - s);
/* 353 */         dec_normalize();
/* 354 */         return ret - _icdf_offset;
/*     */       } 
/*     */     } 
/*     */   } long dec_uint(long _ft) {
/* 358 */     _ft = Inlines.CapToUInt32(_ft);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 363 */     Inlines.OpusAssert((_ft > 1L));
/* 364 */     _ft--;
/* 365 */     int ftb = Inlines.EC_ILOG(_ft);
/* 366 */     if (ftb > 8) {
/*     */       
/* 368 */       ftb -= 8;
/* 369 */       long ft = Inlines.CapToUInt32((_ft >> ftb) + 1L);
/* 370 */       long l1 = Inlines.CapToUInt32(decode(ft));
/* 371 */       dec_update(l1, l1 + 1L, ft);
/* 372 */       long t = Inlines.CapToUInt32(l1 << ftb | dec_bits(ftb));
/* 373 */       if (t <= _ft) {
/* 374 */         return t;
/*     */       }
/* 376 */       this.error = 1;
/* 377 */       return _ft;
/*     */     } 
/* 379 */     _ft++;
/* 380 */     long s = Inlines.CapToUInt32(decode(_ft));
/* 381 */     dec_update(s, s + 1L, _ft);
/* 382 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int dec_bits(int _bits) {
/* 390 */     long window = this.end_window;
/* 391 */     int available = this.nend_bits;
/* 392 */     if (available < _bits) {
/*     */       do {
/* 394 */         window = Inlines.CapToUInt32(window | (read_byte_from_end() << available));
/* 395 */         available += 8;
/* 396 */       } while (available <= 24);
/*     */     }
/* 398 */     int ret = (int)(0xFFFFFFFFFFFFFFFFL & window & ((1 << _bits) - 1));
/* 399 */     window >>= _bits;
/* 400 */     available -= _bits;
/* 401 */     this.end_window = Inlines.CapToUInt32(window);
/* 402 */     this.nend_bits = available;
/* 403 */     this.nbits_total += _bits;
/* 404 */     return ret;
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
/*     */   void enc_carry_out(int _c) {
/* 422 */     if (_c != 255L) {
/*     */ 
/*     */       
/* 425 */       int carry = _c >> 8;
/*     */ 
/*     */ 
/*     */       
/* 429 */       if (this.rem >= 0) {
/* 430 */         this.error |= write_byte(Inlines.CapToUInt32(this.rem + carry));
/*     */       }
/*     */       
/* 433 */       if (this.ext > 0L) {
/*     */         
/* 435 */         long sym = 255L + carry & 0xFFL;
/*     */         do {
/* 437 */           this.error |= write_byte(sym);
/* 438 */         } while (--this.ext > 0L);
/*     */       } 
/*     */       
/* 441 */       this.rem = (int)(_c & 0xFFL);
/*     */     } else {
/* 443 */       this.ext++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void enc_normalize() {
/* 449 */     while (this.rng <= 8388608L) {
/* 450 */       enc_carry_out((int)(this.val >> 23L));
/*     */       
/* 452 */       this.val = Inlines.CapToUInt32(this.val << 8L & 0x7FFFFFFFL);
/* 453 */       this.rng = Inlines.CapToUInt32(this.rng << 8L);
/* 454 */       this.nbits_total += 8;
/*     */     } 
/*     */   }
/*     */   
/*     */   void enc_init(byte[] _buf, int buf_ptr, int _size) {
/* 459 */     this.buf = _buf;
/* 460 */     this.buf_ptr = buf_ptr;
/* 461 */     this.end_offs = 0;
/* 462 */     this.end_window = 0L;
/* 463 */     this.nend_bits = 0;
/*     */     
/* 465 */     this.nbits_total = 33;
/* 466 */     this.offs = 0;
/* 467 */     this.rng = Inlines.CapToUInt32(2147483648L);
/* 468 */     this.rem = -1;
/* 469 */     this.val = 0L;
/* 470 */     this.ext = 0L;
/* 471 */     this.storage = _size;
/* 472 */     this.error = 0;
/*     */   }
/*     */   
/*     */   void encode(long _fl, long _fh, long _ft) {
/* 476 */     _fl = Inlines.CapToUInt32(_fl);
/* 477 */     _fh = Inlines.CapToUInt32(_fh);
/* 478 */     _ft = Inlines.CapToUInt32(_ft);
/* 479 */     long r = Inlines.CapToUInt32(this.rng / _ft);
/* 480 */     if (_fl > 0L) {
/* 481 */       this.val += Inlines.CapToUInt32(this.rng - r * (_ft - _fl));
/* 482 */       this.rng = Inlines.CapToUInt32(r * (_fh - _fl));
/*     */     } else {
/* 484 */       this.rng = Inlines.CapToUInt32(this.rng - r * (_ft - _fh));
/*     */     } 
/*     */     
/* 487 */     enc_normalize();
/*     */   }
/*     */   
/*     */   void encode_bin(long _fl, long _fh, int _bits) {
/* 491 */     _fl = Inlines.CapToUInt32(_fl);
/* 492 */     _fh = Inlines.CapToUInt32(_fh);
/* 493 */     long r = Inlines.CapToUInt32(this.rng >> _bits);
/* 494 */     if (_fl > 0L) {
/* 495 */       this.val = Inlines.CapToUInt32(this.val + Inlines.CapToUInt32(this.rng - r * ((1 << _bits) - _fl)));
/* 496 */       this.rng = Inlines.CapToUInt32(r * (_fh - _fl));
/*     */     } else {
/* 498 */       this.rng = Inlines.CapToUInt32(this.rng - r * ((1 << _bits) - _fh));
/*     */     } 
/*     */     
/* 501 */     enc_normalize();
/*     */   }
/*     */ 
/*     */   
/*     */   void enc_bit_logp(int _val, int _logp) {
/* 506 */     long r = this.rng;
/* 507 */     long l = this.val;
/* 508 */     long s = r >> _logp;
/* 509 */     r -= s;
/* 510 */     if (_val != 0) {
/* 511 */       this.val = Inlines.CapToUInt32(l + r);
/*     */     }
/*     */     
/* 514 */     this.rng = (_val != 0) ? s : r;
/* 515 */     enc_normalize();
/*     */   }
/*     */   
/*     */   void enc_icdf(int _s, short[] _icdf, int _ftb) {
/* 519 */     long r = Inlines.CapToUInt32(this.rng >> _ftb);
/* 520 */     if (_s > 0) {
/* 521 */       this.val += Inlines.CapToUInt32(this.rng - Inlines.CapToUInt32(r * _icdf[_s - 1]));
/* 522 */       this.rng = r * Inlines.CapToUInt32(_icdf[_s - 1] - _icdf[_s]);
/*     */     } else {
/* 524 */       this.rng = Inlines.CapToUInt32(this.rng - r * _icdf[_s]);
/*     */     } 
/* 526 */     enc_normalize();
/*     */   }
/*     */   
/*     */   void enc_icdf(int _s, short[] _icdf, int icdf_ptr, int _ftb) {
/* 530 */     long r = Inlines.CapToUInt32(this.rng >> _ftb);
/* 531 */     if (_s > 0) {
/* 532 */       this.val += Inlines.CapToUInt32(this.rng - Inlines.CapToUInt32(r * _icdf[icdf_ptr + _s - 1]));
/* 533 */       this.rng = Inlines.CapToUInt32(r * Inlines.CapToUInt32(_icdf[icdf_ptr + _s - 1] - _icdf[icdf_ptr + _s]));
/*     */     } else {
/* 535 */       this.rng = Inlines.CapToUInt32(this.rng - r * _icdf[icdf_ptr + _s]);
/*     */     } 
/* 537 */     enc_normalize();
/*     */   }
/*     */   
/*     */   void enc_uint(long _fl, long _ft) {
/* 541 */     _fl = Inlines.CapToUInt32(_fl);
/* 542 */     _ft = Inlines.CapToUInt32(_ft);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 548 */     Inlines.OpusAssert((_ft > 1L));
/* 549 */     _ft--;
/* 550 */     int ftb = Inlines.EC_ILOG(_ft);
/* 551 */     if (ftb > 8) {
/* 552 */       ftb -= 8;
/* 553 */       long ft = Inlines.CapToUInt32((_ft >> ftb) + 1L);
/* 554 */       long fl = Inlines.CapToUInt32(_fl >> ftb);
/* 555 */       encode(fl, fl + 1L, ft);
/* 556 */       enc_bits(_fl & Inlines.CapToUInt32((1 << ftb) - 1), ftb);
/*     */     } else {
/* 558 */       encode(_fl, _fl + 1L, _ft + 1L);
/*     */     } 
/*     */   }
/*     */   
/*     */   void enc_bits(long _fl, int _bits) {
/* 563 */     _fl = Inlines.CapToUInt32(_fl);
/*     */ 
/*     */     
/* 566 */     long window = this.end_window;
/* 567 */     int used = this.nend_bits;
/* 568 */     Inlines.OpusAssert((_bits > 0));
/*     */     
/* 570 */     if (used + _bits > 32) {
/*     */       do {
/* 572 */         this.error |= write_byte_at_end(window & 0xFFL);
/* 573 */         window >>= 8L;
/* 574 */         used -= 8;
/* 575 */       } while (used >= 8);
/*     */     }
/*     */     
/* 578 */     window |= Inlines.CapToUInt32(_fl << used);
/* 579 */     used += _bits;
/* 580 */     this.end_window = window;
/* 581 */     this.nend_bits = used;
/* 582 */     this.nbits_total += _bits;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void enc_patch_initial_bits(long _val, int _nbits) {
/* 588 */     Inlines.OpusAssert((_nbits <= 8));
/* 589 */     int shift = 8 - _nbits;
/* 590 */     long mask = ((1 << _nbits) - 1 << shift);
/*     */     
/* 592 */     if (this.offs > 0) {
/*     */       
/* 594 */       this.buf[this.buf_ptr] = (byte)(int)(this.buf[this.buf_ptr] & (mask ^ 0xFFFFFFFFFFFFFFFFL) | Inlines.CapToUInt32(_val << shift));
/* 595 */     } else if (this.rem >= 0) {
/*     */       
/* 597 */       this.rem = (int)Inlines.CapToUInt32(Inlines.CapToUInt32(this.rem & (mask ^ 0xFFFFFFFFFFFFFFFFL) | _val) << shift);
/* 598 */     } else if (this.rng <= 2147483648L >> _nbits) {
/*     */       
/* 600 */       this.val = Inlines.CapToUInt32(this.val & (mask << 23L ^ 0xFFFFFFFFFFFFFFFFL) | 
/* 601 */           Inlines.CapToUInt32(Inlines.CapToUInt32(_val) << 23 + shift));
/*     */     } else {
/*     */       
/* 604 */       this.error = -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   void enc_shrink(int _size) {
/* 609 */     Inlines.OpusAssert((this.offs + this.end_offs <= _size));
/* 610 */     Arrays.MemMove(this.buf, this.buf_ptr + _size - this.end_offs, this.buf_ptr + this.storage - this.end_offs, this.end_offs);
/* 611 */     this.storage = _size;
/*     */   }
/*     */   
/*     */   int range_bytes() {
/* 615 */     return this.offs;
/*     */   }
/*     */   
/*     */   int get_error() {
/* 619 */     return this.error;
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
/*     */   int tell() {
/* 632 */     int returnVal = this.nbits_total - Inlines.EC_ILOG(this.rng);
/* 633 */     return returnVal;
/*     */   }
/*     */   
/* 636 */   private static final int[] correction = new int[] { 35733, 38967, 42495, 46340, 50535, 55109, 60097, 65535 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int tell_frac() {
/* 650 */     int nbits = this.nbits_total << 3;
/* 651 */     int l = Inlines.EC_ILOG(this.rng);
/* 652 */     int r = (int)(this.rng >> l - 16);
/* 653 */     long b = Inlines.CapToUInt32((r >> 12) - 8);
/* 654 */     b = Inlines.CapToUInt32(b + ((r > correction[(int)b]) ? 1L : 0L));
/* 655 */     l = (int)((l << 3) + b);
/* 656 */     return nbits - l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void enc_done() {
/* 667 */     int l = 32 - Inlines.EC_ILOG(this.rng);
/* 668 */     long msk = Inlines.CapToUInt32(2147483647L >>> l);
/* 669 */     long end = Inlines.CapToUInt32(Inlines.CapToUInt32(this.val + msk) & (msk ^ 0xFFFFFFFFFFFFFFFFL));
/*     */     
/* 671 */     if ((end | msk) >= this.val + this.rng) {
/* 672 */       l++;
/* 673 */       msk >>= 1L;
/* 674 */       end = Inlines.CapToUInt32(Inlines.CapToUInt32(this.val + msk) & (msk ^ 0xFFFFFFFFFFFFFFFFL));
/*     */     } 
/*     */     
/* 677 */     while (l > 0) {
/* 678 */       enc_carry_out((int)(end >> 23L));
/* 679 */       end = Inlines.CapToUInt32(end << 8L & 0x7FFFFFFFL);
/* 680 */       l -= 8;
/*     */     } 
/*     */ 
/*     */     
/* 684 */     if (this.rem >= 0 || this.ext > 0L) {
/* 685 */       enc_carry_out(0);
/*     */     }
/*     */ 
/*     */     
/* 689 */     long window = this.end_window;
/* 690 */     int used = this.nend_bits;
/*     */     
/* 692 */     while (used >= 8) {
/* 693 */       this.error |= write_byte_at_end(window & 0xFFL);
/* 694 */       window >>= 8L;
/* 695 */       used -= 8;
/*     */     } 
/*     */ 
/*     */     
/* 699 */     if (this.error == 0) {
/* 700 */       Arrays.MemSetWithOffset(this.buf, (byte)0, this.buf_ptr + this.offs, this.storage - this.offs - this.end_offs);
/* 701 */       if (used > 0)
/*     */       {
/* 703 */         if (this.end_offs >= this.storage) {
/* 704 */           this.error = -1;
/*     */         } else {
/* 706 */           l = -l;
/*     */ 
/*     */           
/* 709 */           if (this.offs + this.end_offs >= this.storage && l < used) {
/* 710 */             window = Inlines.CapToUInt32(window & ((1 << l) - 1));
/* 711 */             this.error = -1;
/*     */           } 
/*     */           
/* 714 */           int z = this.buf_ptr + this.storage - this.end_offs - 1;
/* 715 */           this.buf[z] = (byte)(this.buf[z] | (byte)(int)(window & 0xFFL));
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\EntropyCoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */