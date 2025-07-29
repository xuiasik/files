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
/*     */ class ResidualEnergy
/*     */ {
/*     */   static void silk_residual_energy(int[] nrgs, int[] nrgsQ, short[] x, short[][] a_Q12, int[] gains, int subfr_length, int nb_subfr, int LPC_order) {
/*  49 */     BoxedValueInt rshift = new BoxedValueInt(0);
/*  50 */     BoxedValueInt energy = new BoxedValueInt(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     int x_ptr = 0;
/*  57 */     int offset = LPC_order + subfr_length;
/*     */ 
/*     */     
/*  60 */     short[] LPC_res = new short[2 * offset];
/*  61 */     Inlines.OpusAssert(((nb_subfr >> 1) * 2 == nb_subfr)); int i;
/*  62 */     for (i = 0; i < nb_subfr >> 1; i++) {
/*     */       
/*  64 */       Filters.silk_LPC_analysis_filter(LPC_res, 0, x, x_ptr, a_Q12[i], 0, 2 * offset, LPC_order);
/*     */ 
/*     */       
/*  67 */       int LPC_res_ptr = LPC_order;
/*  68 */       for (int j = 0; j < 2; j++) {
/*     */         
/*  70 */         SumSqrShift.silk_sum_sqr_shift(energy, rshift, LPC_res, LPC_res_ptr, subfr_length);
/*  71 */         nrgs[i * 2 + j] = energy.Val;
/*     */ 
/*     */         
/*  74 */         nrgsQ[i * 2 + j] = 0 - rshift.Val;
/*     */ 
/*     */         
/*  77 */         LPC_res_ptr += offset;
/*     */       } 
/*     */       
/*  80 */       x_ptr += 2 * offset;
/*     */     } 
/*     */ 
/*     */     
/*  84 */     for (i = 0; i < nb_subfr; i++) {
/*     */       
/*  86 */       int lz1 = Inlines.silk_CLZ32(nrgs[i]) - 1;
/*  87 */       int lz2 = Inlines.silk_CLZ32(gains[i]) - 1;
/*     */       
/*  89 */       int tmp32 = Inlines.silk_LSHIFT32(gains[i], lz2);
/*     */ 
/*     */       
/*  92 */       tmp32 = Inlines.silk_SMMUL(tmp32, tmp32);
/*     */ 
/*     */ 
/*     */       
/*  96 */       nrgs[i] = Inlines.silk_SMMUL(tmp32, Inlines.silk_LSHIFT32(nrgs[i], lz1));
/*     */       
/*  98 */       nrgsQ[i] = nrgsQ[i] + lz1 + 2 * lz2 - 32 - 32;
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
/*     */   static int silk_residual_energy16_covar(short[] c, int c_ptr, int[] wXX, int wXX_ptr, int[] wXx, int wxx, int D, int cQ) {
/* 116 */     int[] cn = new int[D];
/*     */ 
/*     */ 
/*     */     
/* 120 */     Inlines.OpusAssert((D >= 0));
/* 121 */     Inlines.OpusAssert((D <= 16));
/* 122 */     Inlines.OpusAssert((cQ > 0));
/* 123 */     Inlines.OpusAssert((cQ < 16));
/*     */     
/* 125 */     int lshifts = 16 - cQ;
/* 126 */     int Qxtra = lshifts;
/*     */     
/* 128 */     int c_max = 0; int i;
/* 129 */     for (i = c_ptr; i < c_ptr + D; i++) {
/* 130 */       c_max = Inlines.silk_max_32(c_max, Inlines.silk_abs(c[i]));
/*     */     }
/* 132 */     Qxtra = Inlines.silk_min_int(Qxtra, Inlines.silk_CLZ32(c_max) - 17);
/*     */     
/* 134 */     int w_max = Inlines.silk_max_32(wXX[wXX_ptr], wXX[wXX_ptr + D * D - 1]);
/* 135 */     Qxtra = Inlines.silk_min_int(Qxtra, Inlines.silk_CLZ32(Inlines.silk_MUL(D, Inlines.silk_RSHIFT(Inlines.silk_SMULWB(w_max, c_max), 4))) - 5);
/* 136 */     Qxtra = Inlines.silk_max_int(Qxtra, 0);
/* 137 */     for (i = 0; i < D; i++) {
/* 138 */       cn[i] = Inlines.silk_LSHIFT(c[c_ptr + i], Qxtra);
/* 139 */       Inlines.OpusAssert((Inlines.silk_abs(cn[i]) <= 32768));
/*     */     } 
/*     */     
/* 142 */     lshifts -= Qxtra;
/*     */ 
/*     */     
/* 145 */     int tmp = 0;
/* 146 */     for (i = 0; i < D; i++) {
/* 147 */       tmp = Inlines.silk_SMLAWB(tmp, wXx[i], cn[i]);
/*     */     }
/* 149 */     int nrg = Inlines.silk_RSHIFT(wxx, 1 + lshifts) - tmp;
/*     */ 
/*     */ 
/*     */     
/* 153 */     int tmp2 = 0;
/* 154 */     for (i = 0; i < D; i++) {
/* 155 */       tmp = 0;
/* 156 */       int pRow = wXX_ptr + i * D;
/* 157 */       for (int j = i + 1; j < D; j++) {
/* 158 */         tmp = Inlines.silk_SMLAWB(tmp, wXX[pRow + j], cn[j]);
/*     */       }
/* 160 */       tmp = Inlines.silk_SMLAWB(tmp, Inlines.silk_RSHIFT(wXX[pRow + i], 1), cn[i]);
/* 161 */       tmp2 = Inlines.silk_SMLAWB(tmp2, tmp, cn[i]);
/*     */     } 
/* 163 */     nrg = Inlines.silk_ADD_LSHIFT32(nrg, tmp2, lshifts);
/*     */ 
/*     */ 
/*     */     
/* 167 */     if (nrg < 1) {
/* 168 */       nrg = 1;
/* 169 */     } else if (nrg > Inlines.silk_RSHIFT(2147483647, lshifts + 2)) {
/* 170 */       nrg = 1073741823;
/*     */     } else {
/* 172 */       nrg = Inlines.silk_LSHIFT(nrg, lshifts + 1);
/*     */     } 
/*     */     
/* 175 */     return nrg;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\ResidualEnergy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */