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
/*     */ class PLC
/*     */ {
/*     */   private static final int NB_ATT = 2;
/*  40 */   private static final short[] HARM_ATT_Q15 = new short[] { 32440, 31130 };
/*     */   
/*  42 */   private static final short[] PLC_RAND_ATTENUATE_V_Q15 = new short[] { 31130, 26214 };
/*     */   
/*  44 */   private static final short[] PLC_RAND_ATTENUATE_UV_Q15 = new short[] { 32440, 29491 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_PLC_Reset(SilkChannelDecoder psDec) {
/*  51 */     psDec.sPLC.pitchL_Q8 = Inlines.silk_LSHIFT(psDec.frame_length, 7);
/*  52 */     psDec.sPLC.prevGain_Q16[0] = 65536;
/*  53 */     psDec.sPLC.prevGain_Q16[1] = 65536;
/*  54 */     psDec.sPLC.subfr_length = 20;
/*  55 */     psDec.sPLC.nb_subfr = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_PLC(SilkChannelDecoder psDec, SilkDecoderControl psDecCtrl, short[] frame, int frame_ptr, int lost) {
/*  66 */     if (psDec.fs_kHz != psDec.sPLC.fs_kHz) {
/*  67 */       silk_PLC_Reset(psDec);
/*  68 */       psDec.sPLC.fs_kHz = psDec.fs_kHz;
/*     */     } 
/*     */     
/*  71 */     if (lost != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  79 */       silk_PLC_conceal(psDec, psDecCtrl, frame, frame_ptr);
/*     */       
/*  81 */       psDec.lossCnt++;
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/*  90 */       silk_PLC_update(psDec, psDecCtrl);
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
/*     */   static void silk_PLC_update(SilkChannelDecoder psDec, SilkDecoderControl psDecCtrl) {
/* 107 */     PLCStruct psPLC = psDec.sPLC;
/*     */ 
/*     */     
/* 110 */     psDec.prevSignalType = psDec.indices.signalType;
/* 111 */     int LTP_Gain_Q14 = 0;
/* 112 */     if (psDec.indices.signalType == 2) {
/*     */       
/* 114 */       for (int j = 0; j * psDec.subfr_length < psDecCtrl.pitchL[psDec.nb_subfr - 1] && 
/* 115 */         j != psDec.nb_subfr; j++) {
/*     */ 
/*     */         
/* 118 */         int temp_LTP_Gain_Q14 = 0;
/* 119 */         for (int i = 0; i < 5; i++) {
/* 120 */           temp_LTP_Gain_Q14 += psDecCtrl.LTPCoef_Q14[(psDec.nb_subfr - 1 - j) * 5 + i];
/*     */         }
/* 122 */         if (temp_LTP_Gain_Q14 > LTP_Gain_Q14) {
/* 123 */           LTP_Gain_Q14 = temp_LTP_Gain_Q14;
/*     */           
/* 125 */           System.arraycopy(psDecCtrl.LTPCoef_Q14, Inlines.silk_SMULBB(psDec.nb_subfr - 1 - j, 5), psPLC.LTPCoef_Q14, 0, 5);
/*     */           
/* 127 */           psPLC.pitchL_Q8 = Inlines.silk_LSHIFT(psDecCtrl.pitchL[psDec.nb_subfr - 1 - j], 8);
/*     */         } 
/*     */       } 
/*     */       
/* 131 */       Arrays.MemSet(psPLC.LTPCoef_Q14, (short)0, 5);
/* 132 */       psPLC.LTPCoef_Q14[2] = (short)LTP_Gain_Q14;
/*     */ 
/*     */       
/* 135 */       if (LTP_Gain_Q14 < 11469) {
/*     */ 
/*     */ 
/*     */         
/* 139 */         int tmp = Inlines.silk_LSHIFT(11469, 10);
/* 140 */         int scale_Q10 = Inlines.silk_DIV32(tmp, Inlines.silk_max(LTP_Gain_Q14, 1));
/* 141 */         for (int i = 0; i < 5; i++) {
/* 142 */           psPLC.LTPCoef_Q14[i] = (short)Inlines.silk_RSHIFT(Inlines.silk_SMULBB(psPLC.LTPCoef_Q14[i], scale_Q10), 10);
/*     */         }
/* 144 */       } else if (LTP_Gain_Q14 > 15565) {
/*     */ 
/*     */ 
/*     */         
/* 148 */         int tmp = Inlines.silk_LSHIFT(15565, 14);
/* 149 */         int scale_Q14 = Inlines.silk_DIV32(tmp, Inlines.silk_max(LTP_Gain_Q14, 1));
/* 150 */         for (int i = 0; i < 5; i++) {
/* 151 */           psPLC.LTPCoef_Q14[i] = (short)Inlines.silk_RSHIFT(Inlines.silk_SMULBB(psPLC.LTPCoef_Q14[i], scale_Q14), 14);
/*     */         }
/*     */       } 
/*     */     } else {
/* 155 */       psPLC.pitchL_Q8 = Inlines.silk_LSHIFT(Inlines.silk_SMULBB(psDec.fs_kHz, 18), 8);
/* 156 */       Arrays.MemSet(psPLC.LTPCoef_Q14, (short)0, 5);
/*     */     } 
/*     */ 
/*     */     
/* 160 */     System.arraycopy(psDecCtrl.PredCoef_Q12[1], 0, psPLC.prevLPC_Q12, 0, psDec.LPC_order);
/* 161 */     psPLC.prevLTP_scale_Q14 = (short)psDecCtrl.LTP_scale_Q14;
/*     */ 
/*     */     
/* 164 */     System.arraycopy(psDecCtrl.Gains_Q16, psDec.nb_subfr - 2, psPLC.prevGain_Q16, 0, 2);
/*     */     
/* 166 */     psPLC.subfr_length = psDec.subfr_length;
/* 167 */     psPLC.nb_subfr = psDec.nb_subfr;
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
/*     */   static void silk_PLC_energy(BoxedValueInt energy1, BoxedValueInt shift1, BoxedValueInt energy2, BoxedValueInt shift2, int[] exc_Q14, int[] prevGain_Q10, int subfr_length, int nb_subfr) {
/* 191 */     int exc_buf_ptr = 0;
/* 192 */     short[] exc_buf = new short[2 * subfr_length];
/*     */ 
/*     */ 
/*     */     
/* 196 */     for (int k = 0; k < 2; k++) {
/* 197 */       for (int i = 0; i < subfr_length; i++) {
/* 198 */         exc_buf[exc_buf_ptr + i] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT(
/* 199 */               Inlines.silk_SMULWW(exc_Q14[i + (k + nb_subfr - 2) * subfr_length], prevGain_Q10[k]), 8));
/*     */       }
/* 201 */       exc_buf_ptr += subfr_length;
/*     */     } 
/*     */ 
/*     */     
/* 205 */     SumSqrShift.silk_sum_sqr_shift(energy1, shift1, exc_buf, subfr_length);
/* 206 */     SumSqrShift.silk_sum_sqr_shift(energy2, shift2, exc_buf, subfr_length, subfr_length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_PLC_conceal(SilkChannelDecoder psDec, SilkDecoderControl psDecCtrl, short[] frame, int frame_ptr) {
/*     */     int rand_Gain_Q15, rand_ptr;
/* 218 */     BoxedValueInt energy1 = new BoxedValueInt(0);
/* 219 */     BoxedValueInt energy2 = new BoxedValueInt(0);
/* 220 */     BoxedValueInt shift1 = new BoxedValueInt(0);
/* 221 */     BoxedValueInt shift2 = new BoxedValueInt(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     short[] sLTP = new short[psDec.ltp_mem_length];
/* 229 */     int[] sLTP_Q14 = new int[psDec.ltp_mem_length + psDec.frame_length];
/* 230 */     PLCStruct psPLC = psDec.sPLC;
/* 231 */     int[] prevGain_Q10 = new int[2];
/*     */     
/* 233 */     prevGain_Q10[0] = Inlines.silk_RSHIFT(psPLC.prevGain_Q16[0], 6);
/* 234 */     prevGain_Q10[1] = Inlines.silk_RSHIFT(psPLC.prevGain_Q16[1], 6);
/*     */     
/* 236 */     if (psDec.first_frame_after_reset != 0) {
/* 237 */       Arrays.MemSet(psPLC.prevLPC_Q12, (short)0, 16);
/*     */     }
/*     */     
/* 240 */     silk_PLC_energy(energy1, shift1, energy2, shift2, psDec.exc_Q14, prevGain_Q10, psDec.subfr_length, psDec.nb_subfr);
/*     */     
/* 242 */     if (Inlines.silk_RSHIFT(energy1.Val, shift2.Val) < Inlines.silk_RSHIFT(energy2.Val, shift1.Val)) {
/*     */       
/* 244 */       rand_ptr = Inlines.silk_max_int(0, (psPLC.nb_subfr - 1) * psPLC.subfr_length - 128);
/*     */     } else {
/*     */       
/* 247 */       rand_ptr = Inlines.silk_max_int(0, psPLC.nb_subfr * psPLC.subfr_length - 128);
/*     */     } 
/*     */ 
/*     */     
/* 251 */     short[] B_Q14 = psPLC.LTPCoef_Q14;
/* 252 */     short rand_scale_Q14 = psPLC.randScale_Q14;
/*     */ 
/*     */     
/* 255 */     int harm_Gain_Q15 = HARM_ATT_Q15[Inlines.silk_min_int(1, psDec.lossCnt)];
/* 256 */     if (psDec.prevSignalType == 2) {
/* 257 */       rand_Gain_Q15 = PLC_RAND_ATTENUATE_V_Q15[Inlines.silk_min_int(1, psDec.lossCnt)];
/*     */     } else {
/* 259 */       rand_Gain_Q15 = PLC_RAND_ATTENUATE_UV_Q15[Inlines.silk_min_int(1, psDec.lossCnt)];
/*     */     } 
/*     */ 
/*     */     
/* 263 */     BWExpander.silk_bwexpander(psPLC.prevLPC_Q12, psDec.LPC_order, 64881);
/*     */ 
/*     */     
/* 266 */     if (psDec.lossCnt == 0) {
/* 267 */       rand_scale_Q14 = 16384;
/*     */ 
/*     */       
/* 270 */       if (psDec.prevSignalType == 2) {
/* 271 */         for (int j = 0; j < 5; j++) {
/* 272 */           rand_scale_Q14 = (short)(rand_scale_Q14 - B_Q14[j]);
/*     */         }
/* 274 */         rand_scale_Q14 = Inlines.silk_max_16((short)3277, rand_scale_Q14);
/*     */         
/* 276 */         rand_scale_Q14 = (short)Inlines.silk_RSHIFT(Inlines.silk_SMULBB(rand_scale_Q14, psPLC.prevLTP_scale_Q14), 14);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 281 */         int invGain_Q30 = LPCInversePredGain.silk_LPC_inverse_pred_gain(psPLC.prevLPC_Q12, psDec.LPC_order);
/*     */         
/* 283 */         int down_scale_Q30 = Inlines.silk_min_32(Inlines.silk_RSHIFT(1073741824, 3), invGain_Q30);
/* 284 */         down_scale_Q30 = Inlines.silk_max_32(Inlines.silk_RSHIFT(1073741824, 8), down_scale_Q30);
/* 285 */         down_scale_Q30 = Inlines.silk_LSHIFT(down_scale_Q30, 3);
/*     */         
/* 287 */         rand_Gain_Q15 = Inlines.silk_RSHIFT(Inlines.silk_SMULWB(down_scale_Q30, rand_Gain_Q15), 14);
/*     */       } 
/*     */     } 
/*     */     
/* 291 */     int rand_seed = psPLC.rand_seed;
/* 292 */     int lag = Inlines.silk_RSHIFT_ROUND(psPLC.pitchL_Q8, 8);
/* 293 */     int sLTP_buf_idx = psDec.ltp_mem_length;
/*     */ 
/*     */     
/* 296 */     int idx = psDec.ltp_mem_length - lag - psDec.LPC_order - 2;
/* 297 */     Inlines.OpusAssert((idx > 0));
/* 298 */     Filters.silk_LPC_analysis_filter(sLTP, idx, psDec.outBuf, idx, psPLC.prevLPC_Q12, 0, psDec.ltp_mem_length - idx, psDec.LPC_order);
/*     */     
/* 300 */     int inv_gain_Q30 = Inlines.silk_INVERSE32_varQ(psPLC.prevGain_Q16[1], 46);
/* 301 */     inv_gain_Q30 = Inlines.silk_min(inv_gain_Q30, 1073741823); int i;
/* 302 */     for (i = idx + psDec.LPC_order; i < psDec.ltp_mem_length; i++) {
/* 303 */       sLTP_Q14[i] = Inlines.silk_SMULWB(inv_gain_Q30, sLTP[i]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 313 */     for (int k = 0; k < psDec.nb_subfr; k++) {
/*     */       
/* 315 */       int pred_lag_ptr = sLTP_buf_idx - lag + 2;
/* 316 */       for (i = 0; i < psDec.subfr_length; i++) {
/*     */ 
/*     */         
/* 319 */         int LTP_pred_Q12 = 2;
/* 320 */         LTP_pred_Q12 = Inlines.silk_SMLAWB(LTP_pred_Q12, sLTP_Q14[pred_lag_ptr], B_Q14[0]);
/* 321 */         LTP_pred_Q12 = Inlines.silk_SMLAWB(LTP_pred_Q12, sLTP_Q14[pred_lag_ptr - 1], B_Q14[1]);
/* 322 */         LTP_pred_Q12 = Inlines.silk_SMLAWB(LTP_pred_Q12, sLTP_Q14[pred_lag_ptr - 2], B_Q14[2]);
/* 323 */         LTP_pred_Q12 = Inlines.silk_SMLAWB(LTP_pred_Q12, sLTP_Q14[pred_lag_ptr - 3], B_Q14[3]);
/* 324 */         LTP_pred_Q12 = Inlines.silk_SMLAWB(LTP_pred_Q12, sLTP_Q14[pred_lag_ptr - 4], B_Q14[4]);
/* 325 */         pred_lag_ptr++;
/*     */ 
/*     */         
/* 328 */         rand_seed = Inlines.silk_RAND(rand_seed);
/* 329 */         idx = Inlines.silk_RSHIFT(rand_seed, 25) & 0x7F;
/* 330 */         sLTP_Q14[sLTP_buf_idx] = Inlines.silk_LSHIFT32(Inlines.silk_SMLAWB(LTP_pred_Q12, psDec.exc_Q14[rand_ptr + idx], rand_scale_Q14), 2);
/* 331 */         sLTP_buf_idx++;
/*     */       } 
/*     */ 
/*     */       
/* 335 */       for (int j = 0; j < 5; j++) {
/* 336 */         B_Q14[j] = (short)Inlines.silk_RSHIFT(Inlines.silk_SMULBB(harm_Gain_Q15, B_Q14[j]), 15);
/*     */       }
/*     */       
/* 339 */       rand_scale_Q14 = (short)Inlines.silk_RSHIFT(Inlines.silk_SMULBB(rand_scale_Q14, rand_Gain_Q15), 15);
/*     */ 
/*     */       
/* 342 */       psPLC.pitchL_Q8 = Inlines.silk_SMLAWB(psPLC.pitchL_Q8, psPLC.pitchL_Q8, 655);
/* 343 */       psPLC.pitchL_Q8 = Inlines.silk_min_32(psPLC.pitchL_Q8, Inlines.silk_LSHIFT(Inlines.silk_SMULBB(18, psDec.fs_kHz), 8));
/* 344 */       lag = Inlines.silk_RSHIFT_ROUND(psPLC.pitchL_Q8, 8);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 354 */     int sLPC_Q14_ptr = psDec.ltp_mem_length - 16;
/*     */ 
/*     */     
/* 357 */     System.arraycopy(psDec.sLPC_Q14_buf, 0, sLTP_Q14, sLPC_Q14_ptr, 16);
/*     */     
/* 359 */     Inlines.OpusAssert((psDec.LPC_order >= 10));
/*     */     
/* 361 */     for (i = 0; i < psDec.frame_length; i++) {
/*     */       
/* 363 */       int sLPCmaxi = sLPC_Q14_ptr + 16 + i;
/*     */       
/* 365 */       int LPC_pred_Q10 = Inlines.silk_RSHIFT(psDec.LPC_order, 1);
/* 366 */       LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLTP_Q14[sLPCmaxi - 1], psPLC.prevLPC_Q12[0]);
/* 367 */       LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLTP_Q14[sLPCmaxi - 2], psPLC.prevLPC_Q12[1]);
/* 368 */       LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLTP_Q14[sLPCmaxi - 3], psPLC.prevLPC_Q12[2]);
/* 369 */       LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLTP_Q14[sLPCmaxi - 4], psPLC.prevLPC_Q12[3]);
/* 370 */       LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLTP_Q14[sLPCmaxi - 5], psPLC.prevLPC_Q12[4]);
/* 371 */       LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLTP_Q14[sLPCmaxi - 6], psPLC.prevLPC_Q12[5]);
/* 372 */       LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLTP_Q14[sLPCmaxi - 7], psPLC.prevLPC_Q12[6]);
/* 373 */       LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLTP_Q14[sLPCmaxi - 8], psPLC.prevLPC_Q12[7]);
/* 374 */       LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLTP_Q14[sLPCmaxi - 9], psPLC.prevLPC_Q12[8]);
/* 375 */       LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLTP_Q14[sLPCmaxi - 10], psPLC.prevLPC_Q12[9]);
/* 376 */       for (int j = 10; j < psDec.LPC_order; j++) {
/* 377 */         LPC_pred_Q10 = Inlines.silk_SMLAWB(LPC_pred_Q10, sLTP_Q14[sLPCmaxi - j - 1], psPLC.prevLPC_Q12[j]);
/*     */       }
/*     */ 
/*     */       
/* 381 */       sLTP_Q14[sLPCmaxi] = Inlines.silk_ADD_LSHIFT32(sLTP_Q14[sLPCmaxi], LPC_pred_Q10, 4);
/*     */ 
/*     */       
/* 384 */       frame[frame_ptr + i] = (short)Inlines.silk_SAT16(Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(Inlines.silk_SMULWW(sLTP_Q14[sLPCmaxi], prevGain_Q10[1]), 8)));
/*     */     } 
/*     */ 
/*     */     
/* 388 */     System.arraycopy(sLTP_Q14, sLPC_Q14_ptr + psDec.frame_length, psDec.sLPC_Q14_buf, 0, 16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 397 */     psPLC.rand_seed = rand_seed;
/* 398 */     psPLC.randScale_Q14 = rand_scale_Q14;
/* 399 */     for (i = 0; i < 4; i++) {
/* 400 */       psDecCtrl.pitchL[i] = lag;
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
/*     */   static void silk_PLC_glue_frames(SilkChannelDecoder psDec, short[] frame, int frame_ptr, int length) {
/* 412 */     BoxedValueInt energy_shift = new BoxedValueInt(0);
/* 413 */     BoxedValueInt energy = new BoxedValueInt(0);
/* 414 */     PLCStruct psPLC = psDec.sPLC;
/*     */     
/* 416 */     if (psDec.lossCnt != 0) {
/*     */       
/* 418 */       BoxedValueInt boxed_conc_e = new BoxedValueInt(0);
/* 419 */       BoxedValueInt boxed_conc_shift = new BoxedValueInt(0);
/* 420 */       SumSqrShift.silk_sum_sqr_shift(boxed_conc_e, boxed_conc_shift, frame, frame_ptr, length);
/* 421 */       psPLC.conc_energy = boxed_conc_e.Val;
/* 422 */       psPLC.conc_energy_shift = boxed_conc_shift.Val;
/*     */       
/* 424 */       psPLC.last_frame_lost = 1;
/*     */     } else {
/* 426 */       if (psDec.sPLC.last_frame_lost != 0) {
/*     */         
/* 428 */         SumSqrShift.silk_sum_sqr_shift(energy, energy_shift, frame, frame_ptr, length);
/*     */ 
/*     */         
/* 431 */         if (energy_shift.Val > psPLC.conc_energy_shift) {
/* 432 */           psPLC.conc_energy = Inlines.silk_RSHIFT(psPLC.conc_energy, energy_shift.Val - psPLC.conc_energy_shift);
/* 433 */         } else if (energy_shift.Val < psPLC.conc_energy_shift) {
/* 434 */           energy.Val = Inlines.silk_RSHIFT(energy.Val, psPLC.conc_energy_shift - energy_shift.Val);
/*     */         } 
/*     */ 
/*     */         
/* 438 */         if (energy.Val > psPLC.conc_energy) {
/*     */ 
/*     */ 
/*     */           
/* 442 */           int LZ = Inlines.silk_CLZ32(psPLC.conc_energy);
/* 443 */           LZ--;
/* 444 */           psPLC.conc_energy = Inlines.silk_LSHIFT(psPLC.conc_energy, LZ);
/* 445 */           energy.Val = Inlines.silk_RSHIFT(energy.Val, Inlines.silk_max_32(24 - LZ, 0));
/*     */           
/* 447 */           int frac_Q24 = Inlines.silk_DIV32(psPLC.conc_energy, Inlines.silk_max(energy.Val, 1));
/*     */           
/* 449 */           int gain_Q16 = Inlines.silk_LSHIFT(Inlines.silk_SQRT_APPROX(frac_Q24), 4);
/* 450 */           int slope_Q16 = Inlines.silk_DIV32_16(65536 - gain_Q16, length);
/*     */           
/* 452 */           slope_Q16 = Inlines.silk_LSHIFT(slope_Q16, 2);
/*     */           
/* 454 */           for (int i = frame_ptr; i < frame_ptr + length; i++) {
/* 455 */             frame[i] = (short)Inlines.silk_SMULWB(gain_Q16, frame[i]);
/* 456 */             gain_Q16 += slope_Q16;
/* 457 */             if (gain_Q16 > 65536) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 463 */       psPLC.last_frame_lost = 0;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\PLC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */