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
/*     */ class CeltLPC
/*     */ {
/*     */   static void celt_lpc(int[] _lpc, int[] ac, int p) {
/*  45 */     int error = ac[0];
/*  46 */     int[] lpc = new int[p];
/*     */ 
/*     */     
/*  49 */     if (ac[0] != 0) {
/*  50 */       for (int j = 0; j < p; j++) {
/*     */         
/*  52 */         int rr = 0; int k;
/*  53 */         for (k = 0; k < j; k++) {
/*  54 */           rr += Inlines.MULT32_32_Q31(lpc[k], ac[j - k]);
/*     */         }
/*  56 */         rr += Inlines.SHR32(ac[j + 1], 3);
/*  57 */         int r = 0 - Inlines.frac_div32(Inlines.SHL32(rr, 3), error);
/*     */         
/*  59 */         lpc[j] = Inlines.SHR32(r, 3);
/*     */         
/*  61 */         for (k = 0; k < j + 1 >> 1; k++) {
/*     */           
/*  63 */           int tmp1 = lpc[k];
/*  64 */           int tmp2 = lpc[j - 1 - k];
/*  65 */           lpc[k] = tmp1 + Inlines.MULT32_32_Q31(r, tmp2);
/*  66 */           lpc[j - 1 - k] = tmp2 + Inlines.MULT32_32_Q31(r, tmp1);
/*     */         } 
/*     */         
/*  69 */         error -= Inlines.MULT32_32_Q31(Inlines.MULT32_32_Q31(r, r), error);
/*     */ 
/*     */         
/*  72 */         if (error < Inlines.SHR32(ac[0], 10)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*  78 */     for (int i = 0; i < p; i++) {
/*  79 */       _lpc[i] = Inlines.ROUND16(lpc[i], 16);
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
/*     */   static void celt_iir(int[] _x, int _x_ptr, int[] den, int[] _y, int _y_ptr, int N, int ord, int[] mem) {
/*  93 */     int[] rden = new int[ord];
/*  94 */     int[] y = new int[N + ord];
/*  95 */     Inlines.OpusAssert(((ord & 0x3) == 0));
/*     */     
/*  97 */     BoxedValueInt _sum0 = new BoxedValueInt(0);
/*  98 */     BoxedValueInt _sum1 = new BoxedValueInt(0);
/*  99 */     BoxedValueInt _sum2 = new BoxedValueInt(0);
/* 100 */     BoxedValueInt _sum3 = new BoxedValueInt(0);
/*     */     
/*     */     int i;
/* 103 */     for (i = 0; i < ord; i++) {
/* 104 */       rden[i] = den[ord - i - 1];
/*     */     }
/* 106 */     for (i = 0; i < ord; i++) {
/* 107 */       y[i] = 0 - mem[ord - i - 1];
/*     */     }
/* 109 */     for (; i < N + ord; i++) {
/* 110 */       y[i] = 0;
/*     */     }
/* 112 */     for (i = 0; i < N - 3; i += 4) {
/*     */       
/* 114 */       _sum0.Val = _x[_x_ptr + i];
/* 115 */       _sum1.Val = _x[_x_ptr + i + 1];
/* 116 */       _sum2.Val = _x[_x_ptr + i + 2];
/* 117 */       _sum3.Val = _x[_x_ptr + i + 3];
/* 118 */       Kernels.xcorr_kernel(rden, y, i, _sum0, _sum1, _sum2, _sum3, ord);
/* 119 */       int sum0 = _sum0.Val;
/* 120 */       int sum1 = _sum1.Val;
/* 121 */       int sum2 = _sum2.Val;
/* 122 */       int sum3 = _sum3.Val;
/*     */ 
/*     */       
/* 125 */       y[i + ord] = 0 - Inlines.ROUND16(sum0, 12);
/* 126 */       _y[_y_ptr + i] = sum0;
/* 127 */       sum1 = Inlines.MAC16_16(sum1, y[i + ord], den[0]);
/* 128 */       y[i + ord + 1] = 0 - Inlines.ROUND16(sum1, 12);
/* 129 */       _y[_y_ptr + i + 1] = sum1;
/* 130 */       sum2 = Inlines.MAC16_16(sum2, y[i + ord + 1], den[0]);
/* 131 */       sum2 = Inlines.MAC16_16(sum2, y[i + ord], den[1]);
/* 132 */       y[i + ord + 2] = 0 - Inlines.ROUND16(sum2, 12);
/* 133 */       _y[_y_ptr + i + 2] = sum2;
/*     */       
/* 135 */       sum3 = Inlines.MAC16_16(sum3, y[i + ord + 2], den[0]);
/* 136 */       sum3 = Inlines.MAC16_16(sum3, y[i + ord + 1], den[1]);
/* 137 */       sum3 = Inlines.MAC16_16(sum3, y[i + ord], den[2]);
/* 138 */       y[i + ord + 3] = 0 - Inlines.ROUND16(sum3, 12);
/* 139 */       _y[_y_ptr + i + 3] = sum3;
/*     */     } 
/* 141 */     for (; i < N; i++) {
/* 142 */       int sum = _x[_x_ptr + i];
/* 143 */       for (int j = 0; j < ord; j++) {
/* 144 */         sum -= Inlines.MULT16_16(rden[j], y[i + j]);
/*     */       }
/* 146 */       y[i + ord] = Inlines.ROUND16(sum, 12);
/* 147 */       _y[_y_ptr + i] = sum;
/*     */     } 
/* 149 */     for (i = 0; i < ord; i++)
/* 150 */       mem[i] = _y[_y_ptr + N - i - 1]; 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\CeltLPC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */