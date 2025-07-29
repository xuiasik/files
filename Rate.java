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
/*     */ class Rate
/*     */ {
/*  39 */   private static final byte[] LOG2_FRAC_TABLE = new byte[] { 0, 8, 13, 16, 19, 21, 23, 24, 26, 27, 28, 29, 30, 31, 32, 32, 33, 34, 34, 35, 36, 36, 37, 37 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int ALLOC_STEPS = 6;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int get_pulses(int i) {
/*  50 */     return (i < 8) ? i : (8 + (i & 0x7) << (i >> 3) - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int bits2pulses(CeltMode m, int band, int LM, int bits) {
/*  57 */     LM++;
/*  58 */     short[] cache = m.cache.bits;
/*  59 */     int cache_ptr = m.cache.index[LM * m.nbEBands + band];
/*     */     
/*  61 */     int lo = 0;
/*  62 */     int hi = cache[cache_ptr];
/*  63 */     bits--;
/*  64 */     for (int i = 0; i < 6; i++) {
/*  65 */       int mid = lo + hi + 1 >> 1;
/*     */       
/*  67 */       if (cache[cache_ptr + mid] >= bits) {
/*  68 */         hi = mid;
/*     */       } else {
/*  70 */         lo = mid;
/*     */       } 
/*     */     } 
/*  73 */     if (bits - ((lo == 0) ? -1 : cache[cache_ptr + lo]) <= cache[cache_ptr + hi] - bits) {
/*  74 */       return lo;
/*     */     }
/*  76 */     return hi;
/*     */   }
/*     */ 
/*     */   
/*     */   static int pulses2bits(CeltMode m, int band, int LM, int pulses) {
/*  81 */     LM++;
/*  82 */     return (pulses == 0) ? 0 : (m.cache.bits[m.cache.index[LM * m.nbEBands + band] + pulses] + 1);
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
/*     */   static int interp_bits2pulses(CeltMode m, int start, int end, int skip_start, int[] bits1, int[] bits2, int[] thresh, int[] cap, int total, BoxedValueInt _balance, int skip_rsv, BoxedValueInt intensity, int intensity_rsv, BoxedValueInt dual_stereo, int dual_stereo_rsv, int[] bits, int[] ebits, int[] fine_priority, int C, int LM, EntropyCoder ec, int encode, int prev, int signalBandwidth) {
/*  94 */     int codedBands = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     int alloc_floor = C << 3;
/* 101 */     int stereo = (C > 1) ? 1 : 0;
/*     */     
/* 103 */     int logM = LM << 3;
/* 104 */     int lo = 0;
/* 105 */     int hi = 64;
/* 106 */     for (int i = 0; i < 6; i++) {
/* 107 */       int mid = lo + hi >> 1;
/* 108 */       int k = 0;
/* 109 */       int i1 = 0;
/* 110 */       for (int n = end; n-- > start; ) {
/* 111 */         int tmp = bits1[n] + (mid * bits2[n] >> 6);
/* 112 */         if (tmp >= thresh[n] || i1 != 0) {
/* 113 */           i1 = 1;
/*     */           
/* 115 */           k += Inlines.IMIN(tmp, cap[n]); continue;
/* 116 */         }  if (tmp >= alloc_floor) {
/* 117 */           k += alloc_floor;
/*     */         }
/*     */       } 
/* 120 */       if (k > total) {
/* 121 */         hi = mid;
/*     */       } else {
/* 123 */         lo = mid;
/*     */       } 
/*     */     } 
/* 126 */     int psum = 0;
/*     */     
/* 128 */     int done = 0; int j;
/* 129 */     for (j = end; j-- > start; ) {
/* 130 */       int tmp = bits1[j] + (lo * bits2[j] >> 6);
/* 131 */       if (tmp < thresh[j] && done == 0) {
/* 132 */         if (tmp >= alloc_floor) {
/* 133 */           tmp = alloc_floor;
/*     */         } else {
/* 135 */           tmp = 0;
/*     */         } 
/*     */       } else {
/* 138 */         done = 1;
/*     */       } 
/*     */ 
/*     */       
/* 142 */       tmp = Inlines.IMIN(tmp, cap[j]);
/* 143 */       bits[j] = tmp;
/* 144 */       psum += tmp;
/*     */     } 
/*     */ 
/*     */     
/* 148 */     for (codedBands = end;; codedBands--) {
/*     */ 
/*     */ 
/*     */       
/* 152 */       j = codedBands - 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 159 */       if (j <= skip_start) {
/*     */         
/* 161 */         total += skip_rsv;
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 167 */       int k = total - psum;
/* 168 */       int n = Inlines.celt_udiv(k, m.eBands[codedBands] - m.eBands[start]);
/* 169 */       k -= (m.eBands[codedBands] - m.eBands[start]) * n;
/* 170 */       int rem = Inlines.IMAX(k - m.eBands[j] - m.eBands[start], 0);
/* 171 */       int band_width = m.eBands[codedBands] - m.eBands[j];
/* 172 */       int band_bits = bits[j] + n * band_width + rem;
/*     */ 
/*     */ 
/*     */       
/* 176 */       if (band_bits >= Inlines.IMAX(thresh[j], alloc_floor + 8)) {
/* 177 */         if (encode != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 183 */           if (codedBands <= start + 2 || (band_bits > ((j < prev) ? 7 : 9) * band_width << LM << 3 >> 4 && j <= signalBandwidth)) {
/* 184 */             ec.enc_bit_logp(1, 1);
/*     */             break;
/*     */           } 
/* 187 */           ec.enc_bit_logp(0, 1);
/* 188 */         } else if (ec.dec_bit_logp(1L) != 0) {
/*     */           break;
/*     */         } 
/*     */         
/* 192 */         psum += 8;
/* 193 */         band_bits -= 8;
/*     */       } 
/*     */       
/* 196 */       psum -= bits[j] + intensity_rsv;
/* 197 */       if (intensity_rsv > 0) {
/* 198 */         intensity_rsv = LOG2_FRAC_TABLE[j - start];
/*     */       }
/* 200 */       psum += intensity_rsv;
/* 201 */       if (band_bits >= alloc_floor) {
/*     */         
/* 203 */         psum += alloc_floor;
/* 204 */         bits[j] = alloc_floor;
/*     */       } else {
/*     */         
/* 207 */         bits[j] = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     Inlines.OpusAssert((codedBands > start));
/*     */     
/* 213 */     if (intensity_rsv > 0) {
/* 214 */       if (encode != 0) {
/* 215 */         intensity.Val = Inlines.IMIN(intensity.Val, codedBands);
/* 216 */         ec.enc_uint((intensity.Val - start), (codedBands + 1 - start));
/*     */       } else {
/* 218 */         intensity.Val = start + (int)ec.dec_uint((codedBands + 1 - start));
/*     */       } 
/*     */     } else {
/* 221 */       intensity.Val = 0;
/*     */     } 
/*     */     
/* 224 */     if (intensity.Val <= start) {
/* 225 */       total += dual_stereo_rsv;
/* 226 */       dual_stereo_rsv = 0;
/*     */     } 
/* 228 */     if (dual_stereo_rsv > 0) {
/* 229 */       if (encode != 0) {
/* 230 */         ec.enc_bit_logp(dual_stereo.Val, 1);
/*     */       } else {
/* 232 */         dual_stereo.Val = ec.dec_bit_logp(1L);
/*     */       } 
/*     */     } else {
/* 235 */       dual_stereo.Val = 0;
/*     */     } 
/*     */ 
/*     */     
/* 239 */     int left = total - psum;
/* 240 */     int percoeff = Inlines.celt_udiv(left, m.eBands[codedBands] - m.eBands[start]);
/* 241 */     left -= (m.eBands[codedBands] - m.eBands[start]) * percoeff;
/* 242 */     for (j = start; j < codedBands; j++) {
/* 243 */       bits[j] = bits[j] + percoeff * (m.eBands[j + 1] - m.eBands[j]);
/*     */     }
/* 245 */     for (j = start; j < codedBands; j++) {
/* 246 */       int tmp = Inlines.IMIN(left, m.eBands[j + 1] - m.eBands[j]);
/* 247 */       bits[j] = bits[j] + tmp;
/* 248 */       left -= tmp;
/*     */     } 
/*     */ 
/*     */     
/* 252 */     int balance = 0;
/* 253 */     for (j = start; j < codedBands; j++) {
/*     */       int excess;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 259 */       Inlines.OpusAssert((bits[j] >= 0));
/* 260 */       int N0 = m.eBands[j + 1] - m.eBands[j];
/* 261 */       int N = N0 << LM;
/* 262 */       int bit = bits[j] + balance;
/*     */       
/* 264 */       if (N > 1) {
/* 265 */         excess = Inlines.MAX32(bit - cap[j], 0);
/* 266 */         bits[j] = bit - excess;
/*     */ 
/*     */         
/* 269 */         int den = C * N + ((C == 2 && N > 2 && dual_stereo.Val == 0 && j < intensity.Val) ? 1 : 0);
/*     */         
/* 271 */         int NClogN = den * (m.logN[j] + logM);
/*     */ 
/*     */ 
/*     */         
/* 275 */         int offset = (NClogN >> 1) - den * 21;
/*     */ 
/*     */         
/* 278 */         if (N == 2) {
/* 279 */           offset += den << 3 >> 2;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 284 */         if (bits[j] + offset < den * 2 << 3) {
/* 285 */           offset += NClogN >> 2;
/* 286 */         } else if (bits[j] + offset < den * 3 << 3) {
/* 287 */           offset += NClogN >> 3;
/*     */         } 
/*     */ 
/*     */         
/* 291 */         ebits[j] = Inlines.IMAX(0, bits[j] + offset + (den << 2));
/* 292 */         ebits[j] = Inlines.celt_udiv(ebits[j], den) >> 3;
/*     */ 
/*     */         
/* 295 */         if (C * ebits[j] > bits[j] >> 3) {
/* 296 */           ebits[j] = bits[j] >> stereo >> 3;
/*     */         }
/*     */ 
/*     */         
/* 300 */         ebits[j] = Inlines.IMIN(ebits[j], 8);
/*     */ 
/*     */ 
/*     */         
/* 304 */         fine_priority[j] = (ebits[j] * (den << 3) >= bits[j] + offset) ? 1 : 0;
/*     */ 
/*     */         
/* 307 */         bits[j] = bits[j] - (C * ebits[j] << 3);
/*     */       }
/*     */       else {
/*     */         
/* 311 */         excess = Inlines.MAX32(0, bit - (C << 3));
/* 312 */         bits[j] = bit - excess;
/* 313 */         ebits[j] = 0;
/* 314 */         fine_priority[j] = 1;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 320 */       if (excess > 0) {
/*     */ 
/*     */         
/* 323 */         int extra_fine = Inlines.IMIN(excess >> stereo + 3, 8 - ebits[j]);
/* 324 */         ebits[j] = ebits[j] + extra_fine;
/* 325 */         int extra_bits = extra_fine * C << 3;
/* 326 */         fine_priority[j] = (extra_bits >= excess - balance) ? 1 : 0;
/* 327 */         excess -= extra_bits;
/*     */       } 
/* 329 */       balance = excess;
/*     */       
/* 331 */       Inlines.OpusAssert((bits[j] >= 0));
/* 332 */       Inlines.OpusAssert((ebits[j] >= 0));
/*     */     } 
/*     */ 
/*     */     
/* 336 */     _balance.Val = balance;
/*     */ 
/*     */     
/* 339 */     for (; j < end; j++) {
/* 340 */       ebits[j] = bits[j] >> stereo >> 3;
/* 341 */       Inlines.OpusAssert((C * ebits[j] << 3 == bits[j]));
/* 342 */       bits[j] = 0;
/* 343 */       fine_priority[j] = (ebits[j] < 1) ? 1 : 0;
/*     */     } 
/*     */     
/* 346 */     return codedBands;
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
/*     */   static int compute_allocation(CeltMode m, int start, int end, int[] offsets, int[] cap, int alloc_trim, BoxedValueInt intensity, BoxedValueInt dual_stereo, int total, BoxedValueInt balance, int[] pulses, int[] ebits, int[] fine_priority, int C, int LM, EntropyCoder ec, int encode, int prev, int signalBandwidth) {
/* 358 */     total = Inlines.IMAX(total, 0);
/* 359 */     int len = m.nbEBands;
/* 360 */     int skip_start = start;
/*     */     
/* 362 */     int skip_rsv = (total >= 8) ? 8 : 0;
/* 363 */     total -= skip_rsv;
/*     */     
/* 365 */     int dual_stereo_rsv = 0, intensity_rsv = dual_stereo_rsv;
/* 366 */     if (C == 2) {
/* 367 */       intensity_rsv = LOG2_FRAC_TABLE[end - start];
/* 368 */       if (intensity_rsv > total) {
/* 369 */         intensity_rsv = 0;
/*     */       } else {
/* 371 */         total -= intensity_rsv;
/* 372 */         dual_stereo_rsv = (total >= 8) ? 8 : 0;
/* 373 */         total -= dual_stereo_rsv;
/*     */       } 
/*     */     } 
/*     */     
/* 377 */     int[] bits1 = new int[len];
/* 378 */     int[] bits2 = new int[len];
/* 379 */     int[] thresh = new int[len];
/* 380 */     int[] trim_offset = new int[len];
/*     */     int j;
/* 382 */     for (j = start; j < end; j++) {
/*     */       
/* 384 */       thresh[j] = Inlines.IMAX(C << 3, 3 * (m.eBands[j + 1] - m.eBands[j]) << LM << 3 >> 4);
/*     */       
/* 386 */       trim_offset[j] = C * (m.eBands[j + 1] - m.eBands[j]) * (alloc_trim - 5 - LM) * (end - j - 1) * (1 << LM + 3) >> 6;
/*     */ 
/*     */ 
/*     */       
/* 390 */       if (m.eBands[j + 1] - m.eBands[j] << LM == 1) {
/* 391 */         trim_offset[j] = trim_offset[j] - (C << 3);
/*     */       }
/*     */     } 
/* 394 */     int lo = 1;
/* 395 */     int hi = m.nbAllocVectors - 1;
/*     */     do {
/* 397 */       int done = 0;
/* 398 */       int psum = 0;
/* 399 */       int mid = lo + hi >> 1;
/* 400 */       for (j = end; j-- > start; ) {
/*     */         
/* 402 */         int N = m.eBands[j + 1] - m.eBands[j];
/* 403 */         int bitsj = C * N * m.allocVectors[mid * len + j] << LM >> 2;
/*     */         
/* 405 */         if (bitsj > 0) {
/* 406 */           bitsj = Inlines.IMAX(0, bitsj + trim_offset[j]);
/*     */         }
/*     */         
/* 409 */         bitsj += offsets[j];
/*     */         
/* 411 */         if (bitsj >= thresh[j] || done != 0) {
/* 412 */           done = 1;
/*     */           
/* 414 */           psum += Inlines.IMIN(bitsj, cap[j]); continue;
/* 415 */         }  if (bitsj >= C << 3) {
/* 416 */           psum += C << 3;
/*     */         }
/*     */       } 
/* 419 */       if (psum > total) {
/* 420 */         hi = mid - 1;
/*     */       } else {
/* 422 */         lo = mid + 1;
/*     */       }
/*     */     
/* 425 */     } while (lo <= hi);
/*     */     
/* 427 */     hi = lo--;
/*     */ 
/*     */     
/* 430 */     for (j = start; j < end; j++) {
/*     */       
/* 432 */       int N = m.eBands[j + 1] - m.eBands[j];
/* 433 */       int bits1j = C * N * m.allocVectors[lo * len + j] << LM >> 2;
/*     */       
/* 435 */       int bits2j = (hi >= m.nbAllocVectors) ? cap[j] : (C * N * m.allocVectors[hi * len + j] << LM >> 2);
/* 436 */       if (bits1j > 0) {
/* 437 */         bits1j = Inlines.IMAX(0, bits1j + trim_offset[j]);
/*     */       }
/* 439 */       if (bits2j > 0) {
/* 440 */         bits2j = Inlines.IMAX(0, bits2j + trim_offset[j]);
/*     */       }
/* 442 */       if (lo > 0) {
/* 443 */         bits1j += offsets[j];
/*     */       }
/* 445 */       bits2j += offsets[j];
/* 446 */       if (offsets[j] > 0) {
/* 447 */         skip_start = j;
/*     */       }
/* 449 */       bits2j = Inlines.IMAX(0, bits2j - bits1j);
/* 450 */       bits1[j] = bits1j;
/* 451 */       bits2[j] = bits2j;
/*     */     } 
/*     */     
/* 454 */     int codedBands = interp_bits2pulses(m, start, end, skip_start, bits1, bits2, thresh, cap, total, balance, skip_rsv, intensity, intensity_rsv, dual_stereo, dual_stereo_rsv, pulses, ebits, fine_priority, C, LM, ec, encode, prev, signalBandwidth);
/*     */ 
/*     */ 
/*     */     
/* 458 */     return codedBands;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Rate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */