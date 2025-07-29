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
/*     */ class QuantizeBands
/*     */ {
/*  40 */   private static final int[] pred_coef = new int[] { 29440, 26112, 21248, 16384 };
/*  41 */   private static final int[] beta_coef = new int[] { 30147, 22282, 12124, 6554 };
/*     */   private static final int beta_intra = 4915;
/*  43 */   private static short[] small_energy_icdf = new short[] { 2, 1, 0 };
/*     */ 
/*     */   
/*     */   static int loss_distortion(int[][] eBands, int[][] oldEBands, int start, int end, int len, int C) {
/*  47 */     int dist = 0;
/*  48 */     int c = 0;
/*     */     do {
/*  50 */       for (int i = start; i < end; i++) {
/*  51 */         int d = Inlines.SUB16(Inlines.SHR16(eBands[c][i], 3), Inlines.SHR16(oldEBands[c][i], 3));
/*  52 */         dist = Inlines.MAC16_16(dist, d, d);
/*     */       } 
/*  54 */     } while (++c < C);
/*     */     
/*  56 */     return Inlines.MIN32(200, Inlines.SHR32(dist, 14));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int quant_coarse_energy_impl(CeltMode m, int start, int end, int[][] eBands, int[][] oldEBands, int budget, int tell, short[] prob_model, int[][] error, EntropyCoder enc, int C, int LM, int intra, int max_decay, int lfe) {
/*  65 */     int coef, beta, badness = 0;
/*  66 */     int[] prev = { 0, 0 };
/*     */ 
/*     */ 
/*     */     
/*  70 */     if (tell + 3 <= budget) {
/*  71 */       enc.enc_bit_logp(intra, 3);
/*     */     }
/*     */     
/*  74 */     if (intra != 0) {
/*  75 */       coef = 0;
/*  76 */       beta = 4915;
/*     */     } else {
/*  78 */       beta = beta_coef[LM];
/*  79 */       coef = pred_coef[LM];
/*     */     } 
/*     */ 
/*     */     
/*  83 */     for (int i = start; i < end; ) {
/*  84 */       int c = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true)
/*  93 */       { int x = eBands[c][i];
/*  94 */         int oldE = Inlines.MAX16(-9216, oldEBands[c][i]);
/*  95 */         int f = Inlines.SHL32(Inlines.EXTEND32(x), 7) - Inlines.PSHR32(Inlines.MULT16_16(coef, oldE), 8) - prev[c];
/*     */         
/*  97 */         int qi = f + 65536 >> 17;
/*  98 */         int decay_bound = Inlines.EXTRACT16(Inlines.MAX32(-28672, 
/*  99 */               Inlines.SUB32(oldEBands[c][i], max_decay)));
/*     */ 
/*     */         
/* 102 */         if (qi < 0 && x < decay_bound) {
/* 103 */           qi += Inlines.SHR16(Inlines.SUB16(decay_bound, x), 10);
/* 104 */           if (qi > 0) {
/* 105 */             qi = 0;
/*     */           }
/*     */         } 
/* 108 */         int qi0 = qi;
/*     */ 
/*     */         
/* 111 */         tell = enc.tell();
/* 112 */         int bits_left = budget - tell - 3 * C * (end - i);
/* 113 */         if (i != start && bits_left < 30) {
/* 114 */           if (bits_left < 24) {
/* 115 */             qi = Inlines.IMIN(1, qi);
/*     */           }
/* 117 */           if (bits_left < 16) {
/* 118 */             qi = Inlines.IMAX(-1, qi);
/*     */           }
/*     */         } 
/* 121 */         if (lfe != 0 && i >= 2) {
/* 122 */           qi = Inlines.IMIN(qi, 0);
/*     */         }
/* 124 */         if (budget - tell >= 15) {
/*     */           
/* 126 */           int pi = 2 * Inlines.IMIN(i, 20);
/* 127 */           BoxedValueInt boxed_qi = new BoxedValueInt(qi);
/* 128 */           Laplace.ec_laplace_encode(enc, boxed_qi, (prob_model[pi] << 7), prob_model[pi + 1] << 6);
/* 129 */           qi = boxed_qi.Val;
/* 130 */         } else if (budget - tell >= 2) {
/* 131 */           qi = Inlines.IMAX(-1, Inlines.IMIN(qi, 1));
/* 132 */           enc.enc_icdf(2 * qi ^ 0 - ((qi < 0) ? 1 : 0), small_energy_icdf, 2);
/* 133 */         } else if (budget - tell >= 1) {
/* 134 */           qi = Inlines.IMIN(0, qi);
/* 135 */           enc.enc_bit_logp(-qi, 1);
/*     */         } else {
/* 137 */           qi = -1;
/*     */         } 
/* 139 */         error[c][i] = Inlines.PSHR32(f, 7) - Inlines.SHL16(qi, 10);
/* 140 */         badness += Inlines.abs(qi0 - qi);
/* 141 */         int q = Inlines.SHL32(qi, 10);
/*     */         
/* 143 */         int tmp = Inlines.PSHR32(Inlines.MULT16_16(coef, oldE), 8) + prev[c] + Inlines.SHL32(q, 7);
/* 144 */         tmp = Inlines.MAX32(-3670016, tmp);
/* 145 */         oldEBands[c][i] = Inlines.PSHR32(tmp, 7);
/* 146 */         prev[c] = prev[c] + Inlines.SHL32(q, 7) - Inlines.MULT16_16(beta, Inlines.PSHR32(q, 8));
/* 147 */         if (++c >= C)
/*     */           i++;  } 
/* 149 */     }  return (lfe != 0) ? 0 : badness;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void quant_coarse_energy(CeltMode m, int start, int end, int effEnd, int[][] eBands, int[][] oldEBands, int budget, int[][] error, EntropyCoder enc, int C, int LM, int nbAvailableBytes, int force_intra, BoxedValueInt delayedIntra, int two_pass, int loss_rate, int lfe) {
/* 160 */     EntropyCoder enc_start_state = new EntropyCoder();
/*     */     
/* 162 */     int badness1 = 0;
/*     */ 
/*     */ 
/*     */     
/* 166 */     int intra = (force_intra != 0 || (two_pass == 0 && delayedIntra.Val > 2 * C * (end - start) && nbAvailableBytes > (end - start) * C)) ? 1 : 0;
/* 167 */     int intra_bias = budget * delayedIntra.Val * loss_rate / C * 512;
/* 168 */     int new_distortion = loss_distortion(eBands, oldEBands, start, effEnd, m.nbEBands, C);
/*     */     
/* 170 */     int tell = enc.tell();
/* 171 */     if (tell + 3 > budget) {
/* 172 */       two_pass = intra = 0;
/*     */     }
/*     */     
/* 175 */     int max_decay = 16384;
/* 176 */     if (end - start > 10) {
/* 177 */       max_decay = Inlines.MIN32(max_decay, Inlines.SHL32(nbAvailableBytes, 7));
/*     */     }
/* 179 */     if (lfe != 0) {
/* 180 */       max_decay = 3072;
/*     */     }
/* 182 */     enc_start_state.Assign(enc);
/*     */     
/* 184 */     int[][] oldEBands_intra = Arrays.InitTwoDimensionalArrayInt(C, m.nbEBands);
/* 185 */     int[][] error_intra = Arrays.InitTwoDimensionalArrayInt(C, m.nbEBands);
/* 186 */     System.arraycopy(oldEBands[0], 0, oldEBands_intra[0], 0, m.nbEBands);
/* 187 */     if (C == 2) {
/* 188 */       System.arraycopy(oldEBands[1], 0, oldEBands_intra[1], 0, m.nbEBands);
/*     */     }
/*     */     
/* 191 */     if (two_pass != 0 || intra != 0) {
/* 192 */       badness1 = quant_coarse_energy_impl(m, start, end, eBands, oldEBands_intra, budget, tell, CeltTables.e_prob_model[LM][1], error_intra, enc, C, LM, 1, max_decay, lfe);
/*     */     }
/*     */ 
/*     */     
/* 196 */     if (intra == 0) {
/*     */       
/* 198 */       EntropyCoder enc_intra_state = new EntropyCoder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 204 */       byte[] intra_bits = null;
/*     */       
/* 206 */       int tell_intra = enc.tell_frac();
/*     */       
/* 208 */       enc_intra_state.Assign(enc);
/*     */       
/* 210 */       int nstart_bytes = enc_start_state.range_bytes();
/* 211 */       int nintra_bytes = enc_intra_state.range_bytes();
/* 212 */       int intra_buf = nstart_bytes;
/* 213 */       int save_bytes = nintra_bytes - nstart_bytes;
/*     */       
/* 215 */       if (save_bytes != 0) {
/* 216 */         intra_bits = new byte[save_bytes];
/*     */         
/* 218 */         System.arraycopy(enc_intra_state.get_buffer(), intra_buf, intra_bits, 0, save_bytes);
/*     */       } 
/*     */       
/* 221 */       enc.Assign(enc_start_state);
/*     */       
/* 223 */       int badness2 = quant_coarse_energy_impl(m, start, end, eBands, oldEBands, budget, tell, CeltTables.e_prob_model[LM][intra], error, enc, C, LM, 0, max_decay, lfe);
/*     */ 
/*     */       
/* 226 */       if (two_pass != 0 && (badness1 < badness2 || (badness1 == badness2 && enc.tell_frac() + intra_bias > tell_intra))) {
/* 227 */         enc.Assign(enc_intra_state);
/*     */         
/* 229 */         if (intra_bits != null) {
/* 230 */           enc_intra_state.write_buffer(intra_bits, 0, intra_buf, nintra_bytes - nstart_bytes);
/*     */         }
/* 232 */         System.arraycopy(oldEBands_intra[0], 0, oldEBands[0], 0, m.nbEBands);
/* 233 */         System.arraycopy(error_intra[0], 0, error[0], 0, m.nbEBands);
/* 234 */         if (C == 2) {
/* 235 */           System.arraycopy(oldEBands_intra[1], 0, oldEBands[1], 0, m.nbEBands);
/* 236 */           System.arraycopy(error_intra[1], 0, error[1], 0, m.nbEBands);
/*     */         } 
/* 238 */         intra = 1;
/*     */       } 
/*     */     } else {
/* 241 */       System.arraycopy(oldEBands_intra[0], 0, oldEBands[0], 0, m.nbEBands);
/* 242 */       System.arraycopy(error_intra[0], 0, error[0], 0, m.nbEBands);
/* 243 */       if (C == 2) {
/* 244 */         System.arraycopy(oldEBands_intra[1], 0, oldEBands[1], 0, m.nbEBands);
/* 245 */         System.arraycopy(error_intra[1], 0, error[1], 0, m.nbEBands);
/*     */       } 
/*     */     } 
/*     */     
/* 249 */     if (intra != 0) {
/* 250 */       delayedIntra.Val = new_distortion;
/*     */     } else {
/* 252 */       delayedIntra.Val = Inlines.ADD32(Inlines.MULT16_32_Q15(Inlines.MULT16_16_Q15(pred_coef[LM], pred_coef[LM]), delayedIntra.Val), new_distortion);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void quant_fine_energy(CeltMode m, int start, int end, int[][] oldEBands, int[][] error, int[] fine_quant, EntropyCoder enc, int C) {
/* 261 */     for (int i = start; i < end; i++) {
/* 262 */       int frac = 1 << fine_quant[i];
/* 263 */       if (fine_quant[i] > 0) {
/*     */ 
/*     */         
/* 266 */         int c = 0;
/*     */ 
/*     */ 
/*     */         
/*     */         do {
/* 271 */           int q2 = error[c][i] + 512 >> 10 - fine_quant[i];
/* 272 */           if (q2 > frac - 1) {
/* 273 */             q2 = frac - 1;
/*     */           }
/* 275 */           if (q2 < 0) {
/* 276 */             q2 = 0;
/*     */           }
/* 278 */           enc.enc_bits(q2, fine_quant[i]);
/* 279 */           int offset = Inlines.SUB16(
/* 280 */               Inlines.SHR32(
/* 281 */                 Inlines.SHL32(q2, 10) + 512, fine_quant[i]), 512);
/*     */ 
/*     */           
/* 284 */           oldEBands[c][i] = oldEBands[c][i] + offset;
/* 285 */           error[c][i] = error[c][i] - offset;
/* 286 */         } while (++c < C);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void quant_energy_finalise(CeltMode m, int start, int end, int[][] oldEBands, int[][] error, int[] fine_quant, int[] fine_priority, int bits_left, EntropyCoder enc, int C) {
/* 294 */     for (int prio = 0; prio < 2; prio++) {
/* 295 */       for (int i = start; i < end && bits_left >= C; i++) {
/* 296 */         if (fine_quant[i] < 8 && fine_priority[i] == prio) {
/*     */ 
/*     */ 
/*     */           
/* 300 */           int c = 0;
/*     */ 
/*     */           
/*     */           do {
/* 304 */             int q2 = (error[c][i] < 0) ? 0 : 1;
/* 305 */             enc.enc_bits(q2, 1);
/* 306 */             int offset = Inlines.SHR16(Inlines.SHL16(q2, 10) - 512, fine_quant[i] + 1);
/* 307 */             oldEBands[c][i] = oldEBands[c][i] + offset;
/* 308 */             bits_left--;
/* 309 */           } while (++c < C);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } static void unquant_coarse_energy(CeltMode m, int start, int end, int[] oldEBands, int intra, EntropyCoder dec, int C, int LM) {
/*     */     int coef, beta;
/* 315 */     short[] prob_model = CeltTables.e_prob_model[LM][intra];
/*     */     
/* 317 */     int[] prev = { 0, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 323 */     if (intra != 0) {
/* 324 */       coef = 0;
/* 325 */       beta = 4915;
/*     */     } else {
/* 327 */       beta = beta_coef[LM];
/* 328 */       coef = pred_coef[LM];
/*     */     } 
/*     */     
/* 331 */     int budget = dec.storage * 8;
/*     */ 
/*     */     
/* 334 */     for (int i = start; i < end; ) {
/* 335 */       int c = 0;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         int qi;
/*     */ 
/*     */         
/* 343 */         Inlines.OpusAssert((c < 2));
/* 344 */         int tell = dec.tell();
/* 345 */         if (budget - tell >= 15) {
/*     */           
/* 347 */           int pi = 2 * Inlines.IMIN(i, 20);
/* 348 */           qi = Laplace.ec_laplace_decode(dec, (prob_model[pi] << 7), prob_model[pi + 1] << 6);
/*     */         }
/* 350 */         else if (budget - tell >= 2) {
/* 351 */           qi = dec.dec_icdf(small_energy_icdf, 2);
/* 352 */           qi = qi >> 1 ^ -(qi & 0x1);
/* 353 */         } else if (budget - tell >= 1) {
/* 354 */           qi = 0 - dec.dec_bit_logp(1L);
/*     */         } else {
/* 356 */           qi = -1;
/*     */         } 
/* 358 */         int q = Inlines.SHL32(qi, 10);
/*     */         
/* 360 */         oldEBands[i + c * m.nbEBands] = Inlines.MAX16(-9216, oldEBands[i + c * m.nbEBands]);
/* 361 */         int tmp = Inlines.PSHR32(Inlines.MULT16_16(coef, oldEBands[i + c * m.nbEBands]), 8) + prev[c] + Inlines.SHL32(q, 7);
/* 362 */         tmp = Inlines.MAX32(-3670016, tmp);
/* 363 */         oldEBands[i + c * m.nbEBands] = Inlines.PSHR32(tmp, 7);
/* 364 */         prev[c] = prev[c] + Inlines.SHL32(q, 7) - Inlines.MULT16_16(beta, Inlines.PSHR32(q, 8));
/* 365 */         if (++c >= C)
/*     */           i++; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static void unquant_fine_energy(CeltMode m, int start, int end, int[] oldEBands, int[] fine_quant, EntropyCoder dec, int C) {
/* 372 */     for (int i = start; i < end; i++) {
/* 373 */       if (fine_quant[i] > 0) {
/*     */ 
/*     */         
/* 376 */         int c = 0;
/*     */ 
/*     */         
/*     */         do {
/* 380 */           int q2 = dec.dec_bits(fine_quant[i]);
/* 381 */           int offset = Inlines.SUB16(Inlines.SHR32(
/* 382 */                 Inlines.SHL32(q2, 10) + 512, fine_quant[i]), 512);
/*     */ 
/*     */           
/* 385 */           oldEBands[i + c * m.nbEBands] = oldEBands[i + c * m.nbEBands] + offset;
/* 386 */         } while (++c < C);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void unquant_energy_finalise(CeltMode m, int start, int end, int[] oldEBands, int[] fine_quant, int[] fine_priority, int bits_left, EntropyCoder dec, int C) {
/* 394 */     for (int prio = 0; prio < 2; prio++) {
/* 395 */       for (int i = start; i < end && bits_left >= C; i++) {
/* 396 */         if (fine_quant[i] < 8 && fine_priority[i] == prio) {
/*     */ 
/*     */           
/* 399 */           int c = 0;
/*     */ 
/*     */           
/*     */           do {
/* 403 */             int q2 = dec.dec_bits(1);
/* 404 */             int offset = Inlines.SHR16(Inlines.SHL16(q2, 10) - 512, fine_quant[i] + 1);
/* 405 */             oldEBands[i + c * m.nbEBands] = oldEBands[i + c * m.nbEBands] + offset;
/* 406 */             bits_left--;
/* 407 */           } while (++c < C);
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
/*     */   static void amp2Log2(CeltMode m, int effEnd, int end, int[][] bandE, int[][] bandLogE, int C) {
/* 424 */     int c = 0; do {
/*     */       int i;
/* 426 */       for (i = 0; i < effEnd; i++) {
/* 427 */         bandLogE[c][i] = 
/* 428 */           Inlines.celt_log2(Inlines.SHL32(bandE[c][i], 2)) - 
/* 429 */           Inlines.SHL16(CeltTables.eMeans[i], 6);
/*     */       }
/* 431 */       for (i = effEnd; i < end; i++) {
/* 432 */         bandLogE[c][i] = -14336;
/*     */       }
/* 434 */     } while (++c < C);
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
/*     */   static void amp2Log2(CeltMode m, int effEnd, int end, int[] bandE, int[] bandLogE, int bandLogE_ptr, int C) {
/* 449 */     int c = 0; do {
/*     */       int i;
/* 451 */       for (i = 0; i < effEnd; i++) {
/* 452 */         bandLogE[bandLogE_ptr + c * m.nbEBands + i] = 
/* 453 */           Inlines.celt_log2(Inlines.SHL32(bandE[i + c * m.nbEBands], 2)) - 
/* 454 */           Inlines.SHL16(CeltTables.eMeans[i], 6);
/*     */       }
/* 456 */       for (i = effEnd; i < end; i++) {
/* 457 */         bandLogE[bandLogE_ptr + c * m.nbEBands + i] = -14336;
/*     */       }
/* 459 */     } while (++c < C);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\QuantizeBands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */