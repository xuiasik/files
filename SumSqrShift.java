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
/*     */ class SumSqrShift
/*     */ {
/*     */   static void silk_sum_sqr_shift(BoxedValueInt energy, BoxedValueInt shift, short[] x, int x_ptr, int len) {
/*  53 */     int nrg = 0;
/*  54 */     int shft = 0;
/*  55 */     len--;
/*     */     int i;
/*  57 */     for (i = 0; i < len; i += 2) {
/*  58 */       nrg = Inlines.silk_SMLABB_ovflw(nrg, x[x_ptr + i], x[x_ptr + i]);
/*  59 */       nrg = Inlines.silk_SMLABB_ovflw(nrg, x[x_ptr + i + 1], x[x_ptr + i + 1]);
/*  60 */       if (nrg < 0) {
/*     */         
/*  62 */         nrg = (int)Inlines.silk_RSHIFT_uint(nrg, 2);
/*  63 */         shft = 2;
/*  64 */         i += 2;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  69 */     for (; i < len; i += 2) {
/*  70 */       int nrg_tmp = Inlines.silk_SMULBB(x[x_ptr + i], x[x_ptr + i]);
/*  71 */       nrg_tmp = Inlines.silk_SMLABB_ovflw(nrg_tmp, x[x_ptr + i + 1], x[x_ptr + i + 1]);
/*  72 */       nrg = (int)Inlines.silk_ADD_RSHIFT_uint(nrg, nrg_tmp, shft);
/*  73 */       if (nrg < 0) {
/*     */         
/*  75 */         nrg = (int)Inlines.silk_RSHIFT_uint(nrg, 2);
/*  76 */         shft += 2;
/*     */       } 
/*     */     } 
/*     */     
/*  80 */     if (i == len) {
/*     */       
/*  82 */       int nrg_tmp = Inlines.silk_SMULBB(x[x_ptr + i], x[x_ptr + i]);
/*  83 */       nrg = (int)Inlines.silk_ADD_RSHIFT_uint(nrg, nrg_tmp, shft);
/*     */     } 
/*     */ 
/*     */     
/*  87 */     if ((nrg & 0xC0000000) != 0) {
/*  88 */       nrg = (int)Inlines.silk_RSHIFT_uint(nrg, 2);
/*  89 */       shft += 2;
/*     */     } 
/*     */ 
/*     */     
/*  93 */     shift.Val = shft;
/*  94 */     energy.Val = nrg;
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
/*     */   static void silk_sum_sqr_shift(BoxedValueInt energy, BoxedValueInt shift, short[] x, int len) {
/* 114 */     int nrg = 0;
/* 115 */     int shft = 0;
/* 116 */     len--;
/*     */     int i;
/* 118 */     for (i = 0; i < len; i += 2) {
/* 119 */       nrg = Inlines.silk_SMLABB_ovflw(nrg, x[i], x[i]);
/* 120 */       nrg = Inlines.silk_SMLABB_ovflw(nrg, x[i + 1], x[i + 1]);
/* 121 */       if (nrg < 0) {
/*     */         
/* 123 */         nrg = (int)Inlines.silk_RSHIFT_uint(nrg, 2);
/* 124 */         shft = 2;
/* 125 */         i += 2;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 130 */     for (; i < len; i += 2) {
/* 131 */       int nrg_tmp = Inlines.silk_SMULBB(x[i], x[i]);
/* 132 */       nrg_tmp = Inlines.silk_SMLABB_ovflw(nrg_tmp, x[i + 1], x[i + 1]);
/* 133 */       nrg = (int)Inlines.silk_ADD_RSHIFT_uint(nrg, nrg_tmp, shft);
/* 134 */       if (nrg < 0) {
/*     */         
/* 136 */         nrg = (int)Inlines.silk_RSHIFT_uint(nrg, 2);
/* 137 */         shft += 2;
/*     */       } 
/*     */     } 
/*     */     
/* 141 */     if (i == len) {
/*     */       
/* 143 */       int nrg_tmp = Inlines.silk_SMULBB(x[i], x[i]);
/* 144 */       nrg = (int)Inlines.silk_ADD_RSHIFT_uint(nrg, nrg_tmp, shft);
/*     */     } 
/*     */ 
/*     */     
/* 148 */     if ((nrg & 0xC0000000) != 0) {
/* 149 */       nrg = (int)Inlines.silk_RSHIFT_uint(nrg, 2);
/* 150 */       shft += 2;
/*     */     } 
/*     */ 
/*     */     
/* 154 */     shift.Val = shft;
/* 155 */     energy.Val = nrg;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SumSqrShift.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */