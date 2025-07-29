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
/*     */ class Schur
/*     */ {
/*     */   static int silk_schur(short[] rc_Q15, int[] c, int order) {
/*  44 */     int[][] C = Arrays.InitTwoDimensionalArrayInt(17, 2);
/*     */ 
/*     */     
/*  47 */     Inlines.OpusAssert((order == 6 || order == 8 || order == 10 || order == 12 || order == 14 || order == 16));
/*     */ 
/*     */     
/*  50 */     int lz = Inlines.silk_CLZ32(c[0]);
/*     */ 
/*     */     
/*  53 */     if (lz < 2) {
/*     */       
/*  55 */       for (int k = 0; k < order + 1; k++) {
/*  56 */         C[k][1] = Inlines.silk_RSHIFT(c[k], 1); C[k][0] = Inlines.silk_RSHIFT(c[k], 1);
/*     */       } 
/*  58 */     } else if (lz > 2) {
/*     */       
/*  60 */       lz -= 2;
/*  61 */       for (int k = 0; k < order + 1; k++) {
/*  62 */         C[k][1] = Inlines.silk_LSHIFT(c[k], lz); C[k][0] = Inlines.silk_LSHIFT(c[k], lz);
/*     */       } 
/*     */     } else {
/*     */       
/*  66 */       for (int k = 0; k < order + 1; k++) {
/*  67 */         C[k][1] = c[k]; C[k][0] = c[k];
/*     */       } 
/*     */     } 
/*     */     byte b;
/*  71 */     for (b = 0; b < order; b++) {
/*     */       
/*  73 */       if (Inlines.silk_abs_int32(C[b + 1][0]) >= C[0][1]) {
/*  74 */         if (C[b + 1][0] > 0) {
/*  75 */           rc_Q15[b] = -32440;
/*     */         } else {
/*  77 */           rc_Q15[b] = 32440;
/*     */         } 
/*  79 */         b++;
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/*  84 */       int rc_tmp_Q15 = 0 - Inlines.silk_DIV32_16(C[b + 1][0], Inlines.silk_max_32(Inlines.silk_RSHIFT(C[0][1], 15), 1));
/*     */ 
/*     */       
/*  87 */       rc_tmp_Q15 = Inlines.silk_SAT16(rc_tmp_Q15);
/*     */ 
/*     */       
/*  90 */       rc_Q15[b] = (short)rc_tmp_Q15;
/*     */ 
/*     */       
/*  93 */       for (int n = 0; n < order - b; n++) {
/*  94 */         int Ctmp1 = C[n + b + 1][0];
/*  95 */         int Ctmp2 = C[n][1];
/*  96 */         C[n + b + 1][0] = Inlines.silk_SMLAWB(Ctmp1, Inlines.silk_LSHIFT(Ctmp2, 1), rc_tmp_Q15);
/*  97 */         C[n][1] = Inlines.silk_SMLAWB(Ctmp2, Inlines.silk_LSHIFT(Ctmp1, 1), rc_tmp_Q15);
/*     */       } 
/*     */     } 
/*     */     
/* 101 */     for (; b < order; b++) {
/* 102 */       rc_Q15[b] = 0;
/*     */     }
/*     */ 
/*     */     
/* 106 */     return Inlines.silk_max_32(1, C[0][1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int silk_schur64(int[] rc_Q16, int[] c, int order) {
/* 117 */     int[][] C = Arrays.InitTwoDimensionalArrayInt(17, 2);
/*     */ 
/*     */     
/* 120 */     Inlines.OpusAssert((order == 6 || order == 8 || order == 10 || order == 12 || order == 14 || order == 16));
/*     */ 
/*     */     
/* 123 */     if (c[0] <= 0) {
/* 124 */       Arrays.MemSet(rc_Q16, 0, order);
/* 125 */       return 0;
/*     */     } 
/*     */     int k;
/* 128 */     for (k = 0; k < order + 1; k++) {
/* 129 */       C[k][1] = c[k]; C[k][0] = c[k];
/*     */     } 
/*     */     
/* 132 */     for (k = 0; k < order; k++) {
/*     */       
/* 134 */       if (Inlines.silk_abs_int32(C[k + 1][0]) >= C[0][1]) {
/* 135 */         if (C[k + 1][0] > 0) {
/* 136 */           rc_Q16[k] = -64881;
/*     */         } else {
/* 138 */           rc_Q16[k] = 64881;
/*     */         } 
/* 140 */         k++;
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 145 */       int rc_tmp_Q31 = Inlines.silk_DIV32_varQ(-C[k + 1][0], C[0][1], 31);
/*     */ 
/*     */       
/* 148 */       rc_Q16[k] = Inlines.silk_RSHIFT_ROUND(rc_tmp_Q31, 15);
/*     */ 
/*     */       
/* 151 */       for (int n = 0; n < order - k; n++) {
/* 152 */         int Ctmp1_Q30 = C[n + k + 1][0];
/* 153 */         int Ctmp2_Q30 = C[n][1];
/*     */ 
/*     */         
/* 156 */         C[n + k + 1][0] = Ctmp1_Q30 + Inlines.silk_SMMUL(Inlines.silk_LSHIFT(Ctmp2_Q30, 1), rc_tmp_Q31);
/* 157 */         C[n][1] = Ctmp2_Q30 + Inlines.silk_SMMUL(Inlines.silk_LSHIFT(Ctmp1_Q30, 1), rc_tmp_Q31);
/*     */       } 
/*     */     } 
/*     */     
/* 161 */     for (; k < order; k++) {
/* 162 */       rc_Q16[k] = 0;
/*     */     }
/*     */     
/* 165 */     return Inlines.silk_max_32(1, C[0][1]);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Schur.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */