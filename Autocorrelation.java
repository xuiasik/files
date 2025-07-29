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
/*     */ class Autocorrelation
/*     */ {
/*     */   private static final int QC = 10;
/*     */   private static final int QS = 14;
/*     */   
/*     */   static void silk_autocorr(int[] results, BoxedValueInt scale, short[] inputData, int inputDataSize, int correlationCount) {
/*  44 */     int corrCount = Inlines.silk_min_int(inputDataSize, correlationCount);
/*  45 */     scale.Val = _celt_autocorr(inputData, results, corrCount - 1, inputDataSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int _celt_autocorr(short[] x, int[] ac, int lag, int n) {
/*  56 */     int fastN = n - lag;
/*     */ 
/*     */     
/*  59 */     short[] xx = new short[n];
/*  60 */     Inlines.OpusAssert((n > 0));
/*  61 */     short[] xptr = x;
/*     */     
/*  63 */     int shift = 0;
/*     */ 
/*     */     
/*  66 */     int ac0 = 1 + (n << 7);
/*  67 */     if ((n & 0x1) != 0)
/*  68 */       ac0 += Inlines.SHR32(Inlines.MULT16_16(xptr[0], xptr[0]), 9); 
/*     */     int i;
/*  70 */     for (i = n & 0x1; i < n; i += 2) {
/*  71 */       ac0 += Inlines.SHR32(Inlines.MULT16_16(xptr[i], xptr[i]), 9);
/*  72 */       ac0 += Inlines.SHR32(Inlines.MULT16_16(xptr[i + 1], xptr[i + 1]), 9);
/*     */     } 
/*  74 */     shift = Inlines.celt_ilog2(ac0) - 30 + 10;
/*  75 */     shift /= 2;
/*  76 */     if (shift > 0) {
/*  77 */       for (i = 0; i < n; i++) {
/*  78 */         xx[i] = (short)Inlines.PSHR32(xptr[i], shift);
/*     */       }
/*  80 */       xptr = xx;
/*     */     } else {
/*  82 */       shift = 0;
/*     */     } 
/*     */     
/*  85 */     CeltPitchXCorr.pitch_xcorr(xptr, xptr, ac, fastN, lag + 1);
/*  86 */     for (int k = 0; k <= lag; k++) {
/*  87 */       int d; for (i = k + fastN, d = 0; i < n; i++) {
/*  88 */         d = Inlines.MAC16_16(d, xptr[i], xptr[i - k]);
/*     */       }
/*  90 */       ac[k] = ac[k] + d;
/*     */     } 
/*  92 */     shift = 2 * shift;
/*  93 */     if (shift <= 0) {
/*  94 */       ac[0] = ac[0] + Inlines.SHL32(1, -shift);
/*     */     }
/*  96 */     if (ac[0] < 268435456) {
/*  97 */       int shift2 = 29 - Inlines.EC_ILOG(ac[0]);
/*  98 */       for (i = 0; i <= lag; i++) {
/*  99 */         ac[i] = Inlines.SHL32(ac[i], shift2);
/*     */       }
/* 101 */       shift -= shift2;
/* 102 */     } else if (ac[0] >= 536870912) {
/* 103 */       int shift2 = 1;
/* 104 */       if (ac[0] >= 1073741824) {
/* 105 */         shift2++;
/*     */       }
/* 107 */       for (i = 0; i <= lag; i++) {
/* 108 */         ac[i] = Inlines.SHR32(ac[i], shift2);
/*     */       }
/* 110 */       shift += shift2;
/*     */     } 
/*     */     
/* 113 */     return shift;
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
/*     */   static int _celt_autocorr(int[] x, int[] ac, int[] window, int overlap, int lag, int n) {
/* 125 */     int xptr[], fastN = n - lag;
/*     */ 
/*     */     
/* 128 */     int[] xx = new int[n];
/*     */     
/* 130 */     Inlines.OpusAssert((n > 0));
/* 131 */     Inlines.OpusAssert((overlap >= 0));
/*     */     
/* 133 */     if (overlap == 0) {
/* 134 */       xptr = x;
/*     */     } else {
/* 136 */       int j; for (j = 0; j < n; j++) {
/* 137 */         xx[j] = x[j];
/*     */       }
/* 139 */       for (j = 0; j < overlap; j++) {
/* 140 */         xx[j] = Inlines.MULT16_16_Q15(x[j], window[j]);
/* 141 */         xx[n - j - 1] = Inlines.MULT16_16_Q15(x[n - j - 1], window[j]);
/*     */       } 
/* 143 */       xptr = xx;
/*     */     } 
/*     */     
/* 146 */     int shift = 0;
/*     */ 
/*     */     
/* 149 */     int ac0 = 1 + (n << 7);
/* 150 */     if ((n & 0x1) != 0) {
/* 151 */       ac0 += Inlines.SHR32(Inlines.MULT16_16(xptr[0], xptr[0]), 9);
/*     */     }
/*     */     int i;
/* 154 */     for (i = n & 0x1; i < n; i += 2) {
/* 155 */       ac0 += Inlines.SHR32(Inlines.MULT16_16(xptr[i], xptr[i]), 9);
/* 156 */       ac0 += Inlines.SHR32(Inlines.MULT16_16(xptr[i + 1], xptr[i + 1]), 9);
/*     */     } 
/*     */     
/* 159 */     shift = Inlines.celt_ilog2(ac0) - 30 + 10;
/* 160 */     shift /= 2;
/* 161 */     if (shift > 0) {
/* 162 */       for (i = 0; i < n; i++) {
/* 163 */         xx[i] = Inlines.PSHR32(xptr[i], shift);
/*     */       }
/* 165 */       xptr = xx;
/*     */     } else {
/* 167 */       shift = 0;
/*     */     } 
/*     */     
/* 170 */     CeltPitchXCorr.pitch_xcorr(xptr, xptr, ac, fastN, lag + 1);
/* 171 */     for (int k = 0; k <= lag; k++) {
/* 172 */       int d; for (i = k + fastN, d = 0; i < n; i++) {
/* 173 */         d = Inlines.MAC16_16(d, xptr[i], xptr[i - k]);
/*     */       }
/* 175 */       ac[k] = ac[k] + d;
/*     */     } 
/*     */     
/* 178 */     shift = 2 * shift;
/* 179 */     if (shift <= 0) {
/* 180 */       ac[0] = ac[0] + Inlines.SHL32(1, -shift);
/*     */     }
/* 182 */     if (ac[0] < 268435456) {
/* 183 */       int shift2 = 29 - Inlines.EC_ILOG(ac[0]);
/* 184 */       for (i = 0; i <= lag; i++) {
/* 185 */         ac[i] = Inlines.SHL32(ac[i], shift2);
/*     */       }
/* 187 */       shift -= shift2;
/* 188 */     } else if (ac[0] >= 536870912) {
/* 189 */       int shift2 = 1;
/* 190 */       if (ac[0] >= 1073741824) {
/* 191 */         shift2++;
/*     */       }
/* 193 */       for (i = 0; i <= lag; i++) {
/* 194 */         ac[i] = Inlines.SHR32(ac[i], shift2);
/*     */       }
/* 196 */       shift += shift2;
/*     */     } 
/*     */     
/* 199 */     return shift;
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
/*     */   static void silk_warped_autocorrelation(int[] corr, BoxedValueInt scale, short[] input, int warping_Q16, int length, int order) {
/* 216 */     int[] state_QS = new int[17];
/* 217 */     long[] corr_QC = new long[17];
/*     */ 
/*     */     
/* 220 */     Inlines.OpusAssert(((order & 0x1) == 0));
/* 221 */     Inlines.OpusAssert(true);
/*     */ 
/*     */     
/* 224 */     for (int n = 0; n < length; n++) {
/* 225 */       int tmp1_QS = Inlines.silk_LSHIFT32(input[n], 14);
/*     */       
/* 227 */       for (int i = 0; i < order; i += 2) {
/*     */         
/* 229 */         int tmp2_QS = Inlines.silk_SMLAWB(state_QS[i], state_QS[i + 1] - tmp1_QS, warping_Q16);
/* 230 */         state_QS[i] = tmp1_QS;
/* 231 */         corr_QC[i] = corr_QC[i] + Inlines.silk_RSHIFT64(Inlines.silk_SMULL(tmp1_QS, state_QS[0]), 18);
/*     */         
/* 233 */         tmp1_QS = Inlines.silk_SMLAWB(state_QS[i + 1], state_QS[i + 2] - tmp2_QS, warping_Q16);
/* 234 */         state_QS[i + 1] = tmp2_QS;
/* 235 */         corr_QC[i + 1] = corr_QC[i + 1] + Inlines.silk_RSHIFT64(Inlines.silk_SMULL(tmp2_QS, state_QS[0]), 18);
/*     */       } 
/* 237 */       state_QS[order] = tmp1_QS;
/* 238 */       corr_QC[order] = corr_QC[order] + Inlines.silk_RSHIFT64(Inlines.silk_SMULL(tmp1_QS, state_QS[0]), 18);
/*     */     } 
/*     */     
/* 241 */     int lsh = Inlines.silk_CLZ64(corr_QC[0]) - 35;
/* 242 */     lsh = Inlines.silk_LIMIT(lsh, -22, 20);
/* 243 */     scale.Val = -(10 + lsh);
/* 244 */     Inlines.OpusAssert((scale.Val >= -30 && scale.Val <= 12));
/* 245 */     if (lsh >= 0) {
/* 246 */       for (int i = 0; i < order + 1; i++) {
/* 247 */         corr[i] = (int)Inlines.silk_LSHIFT64(corr_QC[i], lsh);
/*     */       }
/*     */     } else {
/* 250 */       for (int i = 0; i < order + 1; i++) {
/* 251 */         corr[i] = (int)Inlines.silk_RSHIFT64(corr_QC[i], -lsh);
/*     */       }
/*     */     } 
/* 254 */     Inlines.OpusAssert((corr_QC[0] >= 0L));
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Autocorrelation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */