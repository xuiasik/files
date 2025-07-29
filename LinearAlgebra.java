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
/*     */ class LinearAlgebra
/*     */ {
/*     */   static void silk_solve_LDL(int[] A, int A_ptr, int M, int[] b, int[] x_Q16) {
/*  44 */     Inlines.OpusAssert((M <= 16));
/*  45 */     int[] L_Q16 = new int[M * M];
/*  46 */     int[] Y = new int[16];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  54 */     int[] inv_D = new int[32];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     silk_LDL_factorize(A, A_ptr, M, L_Q16, inv_D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     silk_LS_SolveFirst(L_Q16, M, b, Y);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     silk_LS_divide_Q16(Y, inv_D, M);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     silk_LS_SolveLast(L_Q16, M, Y, x_Q16);
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
/*     */   private static void silk_LDL_factorize(int[] A, int A_ptr, int M, int[] L_Q16, int[] inv_D) {
/* 102 */     int[] v_Q0 = new int[M];
/*     */     
/* 104 */     int[] D_Q0 = new int[M];
/*     */ 
/*     */ 
/*     */     
/* 108 */     Inlines.OpusAssert((M <= 16));
/*     */     
/* 110 */     int status = 1;
/* 111 */     int diag_min_value = Inlines.silk_max_32(Inlines.silk_SMMUL(Inlines.silk_ADD_SAT32(A[A_ptr], A[A_ptr + Inlines.silk_SMULBB(M, M) - 1]), 21475), 512);
/* 112 */     for (int loop_count = 0; loop_count < M && status == 1; loop_count++) {
/* 113 */       status = 0;
/* 114 */       for (int j = 0; j < M; j++) {
/* 115 */         int[] scratch1 = L_Q16;
/* 116 */         int scratch1_ptr = Inlines.MatrixGetPointer(j, 0, M);
/* 117 */         int tmp_32 = 0; int i;
/* 118 */         for (i = 0; i < j; i++) {
/* 119 */           v_Q0[i] = Inlines.silk_SMULWW(D_Q0[i], scratch1[scratch1_ptr + i]);
/*     */           
/* 121 */           tmp_32 = Inlines.silk_SMLAWW(tmp_32, v_Q0[i], scratch1[scratch1_ptr + i]);
/*     */         } 
/*     */         
/* 124 */         tmp_32 = Inlines.silk_SUB32(Inlines.MatrixGet(A, A_ptr, j, j, M), tmp_32);
/*     */         
/* 126 */         if (tmp_32 < diag_min_value) {
/* 127 */           tmp_32 = Inlines.silk_SUB32(Inlines.silk_SMULBB(loop_count + 1, diag_min_value), tmp_32);
/*     */           
/* 129 */           for (i = 0; i < M; i++) {
/* 130 */             Inlines.MatrixSet(A, A_ptr, i, i, M, Inlines.silk_ADD32(Inlines.MatrixGet(A, A_ptr, i, i, M), tmp_32));
/*     */           }
/* 132 */           status = 1;
/*     */           break;
/*     */         } 
/* 135 */         D_Q0[j] = tmp_32;
/*     */ 
/*     */ 
/*     */         
/* 139 */         int one_div_diag_Q36 = Inlines.silk_INVERSE32_varQ(tmp_32, 36);
/*     */         
/* 141 */         int one_div_diag_Q40 = Inlines.silk_LSHIFT(one_div_diag_Q36, 4);
/*     */         
/* 143 */         int err = Inlines.silk_SUB32(16777216, Inlines.silk_SMULWW(tmp_32, one_div_diag_Q40));
/*     */         
/* 145 */         int one_div_diag_Q48 = Inlines.silk_SMULWW(err, one_div_diag_Q40);
/*     */ 
/*     */ 
/*     */         
/* 149 */         inv_D[j * 2 + 0] = one_div_diag_Q36;
/* 150 */         inv_D[j * 2 + 1] = one_div_diag_Q48;
/*     */         
/* 152 */         Inlines.MatrixSet(L_Q16, j, j, M, 65536);
/*     */         
/* 154 */         scratch1 = A;
/* 155 */         scratch1_ptr = Inlines.MatrixGetPointer(j, 0, M) + A_ptr;
/* 156 */         int[] scratch2 = L_Q16;
/* 157 */         int scratch2_ptr = Inlines.MatrixGetPointer(j + 1, 0, M);
/* 158 */         for (i = j + 1; i < M; i++) {
/* 159 */           tmp_32 = 0;
/* 160 */           for (int k = 0; k < j; k++) {
/* 161 */             tmp_32 = Inlines.silk_SMLAWW(tmp_32, v_Q0[k], scratch2[scratch2_ptr + k]);
/*     */           }
/*     */           
/* 164 */           tmp_32 = Inlines.silk_SUB32(scratch1[scratch1_ptr + i], tmp_32);
/*     */ 
/*     */ 
/*     */           
/* 168 */           Inlines.MatrixSet(L_Q16, i, j, M, Inlines.silk_ADD32(Inlines.silk_SMMUL(tmp_32, one_div_diag_Q48), 
/* 169 */                 Inlines.silk_RSHIFT(Inlines.silk_SMULWW(tmp_32, one_div_diag_Q36), 4)));
/*     */ 
/*     */           
/* 172 */           scratch2_ptr += M;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 177 */     Inlines.OpusAssert((status == 0));
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
/*     */   private static void silk_LS_divide_Q16(int[] T, int[] inv_D, int M) {
/* 189 */     for (int i = 0; i < M; i++) {
/* 190 */       int one_div_diag_Q36 = inv_D[i * 2 + 0];
/* 191 */       int one_div_diag_Q48 = inv_D[i * 2 + 1];
/*     */       
/* 193 */       int tmp_32 = T[i];
/* 194 */       T[i] = Inlines.silk_ADD32(Inlines.silk_SMMUL(tmp_32, one_div_diag_Q48), Inlines.silk_RSHIFT(Inlines.silk_SMULWW(tmp_32, one_div_diag_Q36), 4));
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
/*     */   private static void silk_LS_SolveFirst(int[] L_Q16, int M, int[] b, int[] x_Q16) {
/* 209 */     for (int i = 0; i < M; i++) {
/* 210 */       int ptr32 = Inlines.MatrixGetPointer(i, 0, M);
/* 211 */       int tmp_32 = 0;
/* 212 */       for (int j = 0; j < i; j++) {
/* 213 */         tmp_32 = Inlines.silk_SMLAWW(tmp_32, L_Q16[ptr32 + j], x_Q16[j]);
/*     */       }
/* 215 */       x_Q16[i] = Inlines.silk_SUB32(b[i], tmp_32);
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
/*     */   private static void silk_LS_SolveLast(int[] L_Q16, int M, int[] b, int[] x_Q16) {
/* 230 */     for (int i = M - 1; i >= 0; i--) {
/* 231 */       int ptr32 = Inlines.MatrixGetPointer(0, i, M);
/* 232 */       int tmp_32 = 0;
/* 233 */       for (int j = M - 1; j > i; j--) {
/* 234 */         tmp_32 = Inlines.silk_SMLAWW(tmp_32, L_Q16[ptr32 + Inlines.silk_SMULBB(j, M)], x_Q16[j]);
/*     */       }
/* 236 */       x_Q16[i] = Inlines.silk_SUB32(b[i], tmp_32);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\LinearAlgebra.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */