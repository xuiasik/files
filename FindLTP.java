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
/*     */ class FindLTP
/*     */ {
/*     */   private static final int LTP_CORRS_HEAD_ROOM = 2;
/*     */   
/*     */   static void silk_find_LTP(short[] b_Q14, int[] WLTP, BoxedValueInt LTPredCodGain_Q7, short[] r_lpc, int[] lag, int[] Wght_Q15, int subfr_length, int nb_subfr, int mem_offset, int[] corr_rshifts) {
/*  72 */     int[] b_Q16 = new int[5];
/*  73 */     int[] delta_b_Q14 = new int[5];
/*  74 */     int[] d_Q14 = new int[4];
/*  75 */     int[] nrg = new int[4];
/*     */     
/*  77 */     int[] w = new int[4];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     int[] Rr = new int[5];
/*  85 */     int[] rr = new int[4];
/*     */ 
/*     */     
/*  88 */     int b_Q14_ptr = 0;
/*  89 */     int WLTP_ptr = 0;
/*  90 */     int r_ptr = mem_offset; int k;
/*  91 */     for (k = 0; k < nb_subfr; k++) {
/*  92 */       int lag_ptr = r_ptr - lag[k] + 2;
/*  93 */       BoxedValueInt boxed_rr = new BoxedValueInt(0);
/*  94 */       BoxedValueInt boxed_rr_shift = new BoxedValueInt(0);
/*  95 */       SumSqrShift.silk_sum_sqr_shift(boxed_rr, boxed_rr_shift, r_lpc, r_ptr, subfr_length);
/*     */       
/*  97 */       rr[k] = boxed_rr.Val;
/*  98 */       int rr_shifts = boxed_rr_shift.Val;
/*     */ 
/*     */       
/* 101 */       int LZs = Inlines.silk_CLZ32(rr[k]);
/* 102 */       if (LZs < 2) {
/* 103 */         rr[k] = Inlines.silk_RSHIFT_ROUND(rr[k], 2 - LZs);
/* 104 */         rr_shifts += 2 - LZs;
/*     */       } 
/* 106 */       corr_rshifts[k] = rr_shifts;
/* 107 */       BoxedValueInt boxed_shifts = new BoxedValueInt(corr_rshifts[k]);
/* 108 */       CorrelateMatrix.silk_corrMatrix(r_lpc, lag_ptr, subfr_length, 5, 2, WLTP, WLTP_ptr, boxed_shifts);
/*     */       
/* 110 */       corr_rshifts[k] = boxed_shifts.Val;
/*     */ 
/*     */       
/* 113 */       CorrelateMatrix.silk_corrVector(r_lpc, lag_ptr, r_lpc, r_ptr, subfr_length, 5, Rr, corr_rshifts[k]);
/*     */       
/* 115 */       if (corr_rshifts[k] > rr_shifts) {
/* 116 */         rr[k] = Inlines.silk_RSHIFT(rr[k], corr_rshifts[k] - rr_shifts);
/*     */       }
/*     */       
/* 119 */       Inlines.OpusAssert((rr[k] >= 0));
/*     */       
/* 121 */       int regu = 1;
/* 122 */       regu = Inlines.silk_SMLAWB(regu, rr[k], 1092);
/* 123 */       regu = Inlines.silk_SMLAWB(regu, Inlines.MatrixGet(WLTP, WLTP_ptr, 0, 0, 5), 1092);
/* 124 */       regu = Inlines.silk_SMLAWB(regu, Inlines.MatrixGet(WLTP, WLTP_ptr, 4, 4, 5), 1092);
/* 125 */       RegularizeCorrelations.silk_regularize_correlations(WLTP, WLTP_ptr, rr, k, regu, 5);
/*     */       
/* 127 */       LinearAlgebra.silk_solve_LDL(WLTP, WLTP_ptr, 5, Rr, b_Q16);
/*     */ 
/*     */ 
/*     */       
/* 131 */       silk_fit_LTP(b_Q16, b_Q14, b_Q14_ptr);
/*     */ 
/*     */       
/* 134 */       nrg[k] = ResidualEnergy.silk_residual_energy16_covar(b_Q14, b_Q14_ptr, WLTP, WLTP_ptr, Rr, rr[k], 5, 14);
/*     */ 
/*     */ 
/*     */       
/* 138 */       int m = Inlines.silk_min_int(corr_rshifts[k], 2);
/*     */       
/* 140 */       int denom32 = Inlines.silk_LSHIFT_SAT32(Inlines.silk_SMULWB(nrg[k], Wght_Q15[k]), 1 + m) + Inlines.silk_RSHIFT(Inlines.silk_SMULWB(subfr_length, 655), corr_rshifts[k] - m);
/*     */       
/* 142 */       denom32 = Inlines.silk_max(denom32, 1);
/* 143 */       Inlines.OpusAssert((Wght_Q15[k] << 16L < 2147483647L));
/*     */       
/* 145 */       int j = Inlines.silk_DIV32(Inlines.silk_LSHIFT(Wght_Q15[k], 16), denom32);
/*     */       
/* 147 */       j = Inlines.silk_RSHIFT(j, 31 + corr_rshifts[k] - m - 26);
/*     */ 
/*     */ 
/*     */       
/* 151 */       int WLTP_max = 0;
/* 152 */       for (int i = WLTP_ptr; i < WLTP_ptr + 25; i++) {
/* 153 */         WLTP_max = Inlines.silk_max(WLTP[i], WLTP_max);
/*     */       }
/* 155 */       int lshift = Inlines.silk_CLZ32(WLTP_max) - 1 - 3;
/*     */       
/* 157 */       Inlines.OpusAssert((8 + lshift >= 0));
/* 158 */       if (8 + lshift < 31) {
/* 159 */         j = Inlines.silk_min_32(j, Inlines.silk_LSHIFT(1, 8 + lshift));
/*     */       }
/*     */       
/* 162 */       Inlines.silk_scale_vector32_Q26_lshift_18(WLTP, WLTP_ptr, j, 25);
/*     */ 
/*     */       
/* 165 */       w[k] = Inlines.MatrixGet(WLTP, WLTP_ptr, 2, 2, 5);
/*     */       
/* 167 */       Inlines.OpusAssert((w[k] >= 0));
/*     */       
/* 169 */       r_ptr += subfr_length;
/* 170 */       b_Q14_ptr += 5;
/* 171 */       WLTP_ptr += 25;
/*     */     } 
/*     */     
/* 174 */     int maxRshifts = 0;
/* 175 */     for (k = 0; k < nb_subfr; k++) {
/* 176 */       maxRshifts = Inlines.silk_max_int(corr_rshifts[k], maxRshifts);
/*     */     }
/*     */ 
/*     */     
/* 180 */     if (LTPredCodGain_Q7 != null) {
/* 181 */       int LPC_LTP_res_nrg = 0;
/* 182 */       int LPC_res_nrg = 0;
/* 183 */       Inlines.OpusAssert(true);
/*     */       
/* 185 */       for (k = 0; k < nb_subfr; k++) {
/* 186 */         LPC_res_nrg = Inlines.silk_ADD32(LPC_res_nrg, Inlines.silk_RSHIFT(Inlines.silk_ADD32(Inlines.silk_SMULWB(rr[k], Wght_Q15[k]), 1), 1 + maxRshifts - corr_rshifts[k]));
/*     */         
/* 188 */         LPC_LTP_res_nrg = Inlines.silk_ADD32(LPC_LTP_res_nrg, Inlines.silk_RSHIFT(Inlines.silk_ADD32(Inlines.silk_SMULWB(nrg[k], Wght_Q15[k]), 1), 1 + maxRshifts - corr_rshifts[k]));
/*     */       } 
/*     */       
/* 191 */       LPC_LTP_res_nrg = Inlines.silk_max(LPC_LTP_res_nrg, 1);
/*     */ 
/*     */       
/* 194 */       int div_Q16 = Inlines.silk_DIV32_varQ(LPC_res_nrg, LPC_LTP_res_nrg, 16);
/* 195 */       LTPredCodGain_Q7.Val = Inlines.silk_SMULBB(3, Inlines.silk_lin2log(div_Q16) - 2048);
/*     */       
/* 197 */       Inlines.OpusAssert((LTPredCodGain_Q7.Val == Inlines.silk_SAT16(Inlines.silk_MUL(3, Inlines.silk_lin2log(div_Q16) - 2048))));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 202 */     b_Q14_ptr = 0;
/* 203 */     for (k = 0; k < nb_subfr; k++) {
/* 204 */       d_Q14[k] = 0;
/* 205 */       for (int i = b_Q14_ptr; i < b_Q14_ptr + 5; i++) {
/* 206 */         d_Q14[k] = d_Q14[k] + b_Q14[i];
/*     */       }
/* 208 */       b_Q14_ptr += 5;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 214 */     int max_abs_d_Q14 = 0;
/* 215 */     int max_w_bits = 0;
/* 216 */     for (k = 0; k < nb_subfr; k++) {
/* 217 */       max_abs_d_Q14 = Inlines.silk_max_32(max_abs_d_Q14, Inlines.silk_abs(d_Q14[k]));
/*     */ 
/*     */       
/* 220 */       max_w_bits = Inlines.silk_max_32(max_w_bits, 32 - Inlines.silk_CLZ32(w[k]) + corr_rshifts[k] - maxRshifts);
/*     */     } 
/*     */ 
/*     */     
/* 224 */     Inlines.OpusAssert((max_abs_d_Q14 <= 163840));
/*     */ 
/*     */     
/* 227 */     int extra_shifts = max_w_bits + 32 - Inlines.silk_CLZ32(max_abs_d_Q14) - 14;
/*     */ 
/*     */     
/* 230 */     extra_shifts -= 29 + maxRshifts;
/*     */     
/* 232 */     extra_shifts = Inlines.silk_max_int(extra_shifts, 0);
/*     */     
/* 234 */     int maxRshifts_wxtra = maxRshifts + extra_shifts;
/*     */     
/* 236 */     int temp32 = Inlines.silk_RSHIFT(262, maxRshifts + extra_shifts) + 1;
/*     */     
/* 238 */     int wd = 0;
/* 239 */     for (k = 0; k < nb_subfr; k++) {
/*     */       
/* 241 */       temp32 = Inlines.silk_ADD32(temp32, Inlines.silk_RSHIFT(w[k], maxRshifts_wxtra - corr_rshifts[k]));
/*     */       
/* 243 */       wd = Inlines.silk_ADD32(wd, Inlines.silk_LSHIFT(Inlines.silk_SMULWW(Inlines.silk_RSHIFT(w[k], maxRshifts_wxtra - corr_rshifts[k]), d_Q14[k]), 2));
/*     */     } 
/*     */     
/* 246 */     int m_Q12 = Inlines.silk_DIV32_varQ(wd, temp32, 12);
/*     */     
/* 248 */     b_Q14_ptr = 0;
/* 249 */     for (k = 0; k < nb_subfr; k++) {
/*     */       
/* 251 */       if (2 - corr_rshifts[k] > 0) {
/* 252 */         temp32 = Inlines.silk_RSHIFT(w[k], 2 - corr_rshifts[k]);
/*     */       } else {
/* 254 */         temp32 = Inlines.silk_LSHIFT_SAT32(w[k], corr_rshifts[k] - 2);
/*     */       } 
/*     */       
/* 257 */       int g_Q26 = Inlines.silk_MUL(
/* 258 */           Inlines.silk_DIV32(6710887, 
/*     */             
/* 260 */             Inlines.silk_RSHIFT(6710887, 10) + temp32), 
/* 261 */           Inlines.silk_LSHIFT_SAT32(Inlines.silk_SUB_SAT32(m_Q12, Inlines.silk_RSHIFT(d_Q14[k], 2)), 4));
/*     */ 
/*     */       
/* 264 */       temp32 = 0; int i;
/* 265 */       for (i = 0; i < 5; i++) {
/* 266 */         delta_b_Q14[i] = Inlines.silk_max_16(b_Q14[b_Q14_ptr + i], (short)1638);
/*     */         
/* 268 */         temp32 += delta_b_Q14[i];
/*     */       } 
/*     */       
/* 271 */       temp32 = Inlines.silk_DIV32(g_Q26, temp32);
/*     */       
/* 273 */       for (i = 0; i < 5; i++) {
/* 274 */         b_Q14[b_Q14_ptr + i] = (short)Inlines.silk_LIMIT_32(b_Q14[b_Q14_ptr + i] + Inlines.silk_SMULWB(Inlines.silk_LSHIFT_SAT32(temp32, 4), delta_b_Q14[i]), -16000, 28000);
/*     */       }
/* 276 */       b_Q14_ptr += 5;
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
/*     */   static void silk_fit_LTP(int[] LTP_coefs_Q16, short[] LTP_coefs_Q14, int LTP_coefs_Q14_ptr) {
/* 292 */     for (int i = 0; i < 5; i++)
/* 293 */       LTP_coefs_Q14[LTP_coefs_Q14_ptr + i] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(LTP_coefs_Q16[i], 2)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\FindLTP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */