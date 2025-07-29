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
/*     */ class LPCInversePredGain
/*     */ {
/*     */   private static final int QA = 24;
/*     */   private static final int A_LIMIT = 16773022;
/*     */   
/*     */   static int LPC_inverse_pred_gain_QA(int[][] A_QA, int order) {
/*  50 */     int[] Anew_QA = A_QA[order & 0x1];
/*     */     
/*  52 */     int invGain_Q30 = 1073741824;
/*  53 */     for (int k = order - 1; k > 0; k--) {
/*     */       
/*  55 */       if (Anew_QA[k] > 16773022 || Anew_QA[k] < -16773022) {
/*  56 */         return 0;
/*     */       }
/*     */ 
/*     */       
/*  60 */       int i = 0 - Inlines.silk_LSHIFT(Anew_QA[k], 7);
/*     */ 
/*     */       
/*  63 */       int j = 1073741824 - Inlines.silk_SMMUL(i, i);
/*  64 */       Inlines.OpusAssert((j > 32768));
/*     */       
/*  66 */       Inlines.OpusAssert((j <= 1073741824));
/*     */ 
/*     */       
/*  69 */       int mult2Q = 32 - Inlines.silk_CLZ32(Inlines.silk_abs(j));
/*  70 */       int rc_mult2 = Inlines.silk_INVERSE32_varQ(j, mult2Q + 30);
/*     */ 
/*     */ 
/*     */       
/*  74 */       invGain_Q30 = Inlines.silk_LSHIFT(Inlines.silk_SMMUL(invGain_Q30, j), 2);
/*  75 */       Inlines.OpusAssert((invGain_Q30 >= 0));
/*  76 */       Inlines.OpusAssert((invGain_Q30 <= 1073741824));
/*     */ 
/*     */       
/*  79 */       int[] Aold_QA = Anew_QA;
/*  80 */       Anew_QA = A_QA[k & 0x1];
/*     */ 
/*     */       
/*  83 */       for (int n = 0; n < k; n++) {
/*  84 */         int tmp_QA = Aold_QA[n] - Inlines.MUL32_FRAC_Q(Aold_QA[k - n - 1], i, 31);
/*  85 */         Anew_QA[n] = Inlines.MUL32_FRAC_Q(tmp_QA, rc_mult2, mult2Q);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  90 */     if (Anew_QA[0] > 16773022 || Anew_QA[0] < -16773022) {
/*  91 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*  95 */     int rc_Q31 = 0 - Inlines.silk_LSHIFT(Anew_QA[0], 7);
/*     */ 
/*     */     
/*  98 */     int rc_mult1_Q30 = 1073741824 - Inlines.silk_SMMUL(rc_Q31, rc_Q31);
/*     */ 
/*     */ 
/*     */     
/* 102 */     invGain_Q30 = Inlines.silk_LSHIFT(Inlines.silk_SMMUL(invGain_Q30, rc_mult1_Q30), 2);
/* 103 */     Inlines.OpusAssert((invGain_Q30 >= 0));
/* 104 */     Inlines.OpusAssert((invGain_Q30 <= 1073741824));
/*     */     
/* 106 */     return invGain_Q30;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int silk_LPC_inverse_pred_gain(short[] A_Q12, int order) {
/* 115 */     int[][] Atmp_QA = Arrays.InitTwoDimensionalArrayInt(2, 16);
/*     */     
/* 117 */     int DC_resp = 0;
/*     */     
/* 119 */     int[] Anew_QA = Atmp_QA[order & 0x1];
/*     */ 
/*     */     
/* 122 */     for (int k = 0; k < order; k++) {
/* 123 */       DC_resp += A_Q12[k];
/* 124 */       Anew_QA[k] = Inlines.silk_LSHIFT32(A_Q12[k], 12);
/*     */     } 
/*     */     
/* 127 */     if (DC_resp >= 4096) {
/* 128 */       return 0;
/*     */     }
/* 130 */     return LPC_inverse_pred_gain_QA(Atmp_QA, order);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int silk_LPC_inverse_pred_gain_Q24(int[] A_Q24, int order) {
/* 138 */     int[][] Atmp_QA = Arrays.InitTwoDimensionalArrayInt(2, 16);
/*     */ 
/*     */     
/* 141 */     int[] Anew_QA = Atmp_QA[order & 0x1];
/*     */ 
/*     */     
/* 144 */     for (int k = 0; k < order; k++) {
/* 145 */       Anew_QA[k] = Inlines.silk_RSHIFT32(A_Q24[k], 0);
/*     */     }
/*     */     
/* 148 */     return LPC_inverse_pred_gain_QA(Atmp_QA, order);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\LPCInversePredGain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */