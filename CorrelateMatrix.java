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
/*     */ class CorrelateMatrix
/*     */ {
/*     */   static void silk_corrVector(short[] x, int x_ptr, short[] t, int t_ptr, int L, int order, int[] Xt, int rshifts) {
/*  57 */     int ptr1 = x_ptr + order - 1;
/*     */     
/*  59 */     int ptr2 = t_ptr;
/*     */     
/*  61 */     if (rshifts > 0) {
/*     */       
/*  63 */       for (int lag = 0; lag < order; lag++) {
/*  64 */         int inner_prod = 0;
/*  65 */         for (int i = 0; i < L; i++) {
/*  66 */           inner_prod += Inlines.silk_RSHIFT32(Inlines.silk_SMULBB(x[ptr1 + i], t[ptr2 + i]), rshifts);
/*     */         }
/*  68 */         Xt[lag] = inner_prod;
/*     */         
/*  70 */         ptr1--;
/*     */       } 
/*     */     } else {
/*     */       
/*  74 */       Inlines.OpusAssert((rshifts == 0));
/*  75 */       for (int lag = 0; lag < order; lag++) {
/*  76 */         Xt[lag] = Inlines.silk_inner_prod(x, ptr1, t, ptr2, L);
/*     */         
/*  78 */         ptr1--;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_corrMatrix(short[] x, int x_ptr, int L, int order, int head_room, int[] XX, int XX_ptr, BoxedValueInt rshifts) {
/* 100 */     BoxedValueInt boxed_energy = new BoxedValueInt(0);
/* 101 */     BoxedValueInt boxed_rshifts_local = new BoxedValueInt(0);
/* 102 */     SumSqrShift.silk_sum_sqr_shift(boxed_energy, boxed_rshifts_local, x, x_ptr, L + order - 1);
/* 103 */     int energy = boxed_energy.Val;
/* 104 */     int rshifts_local = boxed_rshifts_local.Val;
/*     */ 
/*     */     
/* 107 */     int head_room_rshifts = Inlines.silk_max(head_room - Inlines.silk_CLZ32(energy), 0);
/*     */     
/* 109 */     energy = Inlines.silk_RSHIFT32(energy, head_room_rshifts);
/* 110 */     rshifts_local += head_room_rshifts;
/*     */     
/*     */     int i;
/*     */     
/* 114 */     for (i = x_ptr; i < x_ptr + order - 1; i++) {
/* 115 */       energy -= Inlines.silk_RSHIFT32(Inlines.silk_SMULBB(x[i], x[i]), rshifts_local);
/*     */     }
/* 117 */     if (rshifts_local < rshifts.Val) {
/*     */       
/* 119 */       energy = Inlines.silk_RSHIFT32(energy, rshifts.Val - rshifts_local);
/* 120 */       rshifts_local = rshifts.Val;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 125 */     Inlines.MatrixSet(XX, XX_ptr, 0, 0, order, energy);
/* 126 */     int ptr1 = x_ptr + order - 1;
/*     */     int j;
/* 128 */     for (j = 1; j < order; j++) {
/* 129 */       energy = Inlines.silk_SUB32(energy, Inlines.silk_RSHIFT32(Inlines.silk_SMULBB(x[ptr1 + L - j], x[ptr1 + L - j]), rshifts_local));
/* 130 */       energy = Inlines.silk_ADD32(energy, Inlines.silk_RSHIFT32(Inlines.silk_SMULBB(x[ptr1 - j], x[ptr1 - j]), rshifts_local));
/* 131 */       Inlines.MatrixSet(XX, XX_ptr, j, j, order, energy);
/*     */     } 
/*     */     
/* 134 */     int ptr2 = x_ptr + order - 2;
/*     */ 
/*     */     
/* 137 */     if (rshifts_local > 0) {
/*     */       
/* 139 */       for (int lag = 1; lag < order; lag++) {
/*     */         
/* 141 */         energy = 0;
/* 142 */         for (i = 0; i < L; i++) {
/* 143 */           energy += Inlines.silk_RSHIFT32(Inlines.silk_SMULBB(x[ptr1 + i], x[ptr2 + i]), rshifts_local);
/*     */         }
/*     */         
/* 146 */         Inlines.MatrixSet(XX, XX_ptr, lag, 0, order, energy);
/* 147 */         Inlines.MatrixSet(XX, XX_ptr, 0, lag, order, energy);
/* 148 */         for (j = 1; j < order - lag; j++) {
/* 149 */           energy = Inlines.silk_SUB32(energy, Inlines.silk_RSHIFT32(Inlines.silk_SMULBB(x[ptr1 + L - j], x[ptr2 + L - j]), rshifts_local));
/* 150 */           energy = Inlines.silk_ADD32(energy, Inlines.silk_RSHIFT32(Inlines.silk_SMULBB(x[ptr1 - j], x[ptr2 - j]), rshifts_local));
/* 151 */           Inlines.MatrixSet(XX, XX_ptr, lag + j, j, order, energy);
/* 152 */           Inlines.MatrixSet(XX, XX_ptr, j, lag + j, order, energy);
/*     */         } 
/* 154 */         ptr2--;
/*     */       } 
/*     */     } else {
/*     */       
/* 158 */       for (int lag = 1; lag < order; lag++) {
/*     */         
/* 160 */         energy = Inlines.silk_inner_prod(x, ptr1, x, ptr2, L);
/* 161 */         Inlines.MatrixSet(XX, XX_ptr, lag, 0, order, energy);
/* 162 */         Inlines.MatrixSet(XX, XX_ptr, 0, lag, order, energy);
/*     */         
/* 164 */         for (j = 1; j < order - lag; j++) {
/* 165 */           energy = Inlines.silk_SUB32(energy, Inlines.silk_SMULBB(x[ptr1 + L - j], x[ptr2 + L - j]));
/* 166 */           energy = Inlines.silk_SMLABB(energy, x[ptr1 - j], x[ptr2 - j]);
/* 167 */           Inlines.MatrixSet(XX, XX_ptr, lag + j, j, order, energy);
/* 168 */           Inlines.MatrixSet(XX, XX_ptr, j, lag + j, order, energy);
/*     */         } 
/* 170 */         ptr2--;
/*     */       } 
/*     */     } 
/* 173 */     rshifts.Val = rshifts_local;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\CorrelateMatrix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */