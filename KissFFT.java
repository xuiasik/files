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
/*     */ class KissFFT
/*     */ {
/*     */   static final int MAXFACTORS = 8;
/*     */   
/*     */   static int S_MUL(int a, int b) {
/*  49 */     return Inlines.MULT16_32_Q15(b, a);
/*     */   }
/*     */   
/*     */   static int S_MUL(int a, short b) {
/*  53 */     return Inlines.MULT16_32_Q15(b, a);
/*     */   }
/*     */   
/*     */   static int HALF_OF(int x) {
/*  57 */     return x >> 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void kf_bfly2(int[] Fout, int fout_ptr, int m, int N) {
/*  65 */     short tw = 23170;
/*     */     
/*  67 */     Inlines.OpusAssert((m == 4));
/*  68 */     for (int i = 0; i < N; i++) {
/*     */       
/*  70 */       int Fout2 = fout_ptr + 8;
/*  71 */       int t_r = Fout[Fout2 + 0];
/*  72 */       int t_i = Fout[Fout2 + 1];
/*  73 */       Fout[Fout2 + 0] = Fout[fout_ptr + 0] - t_r;
/*  74 */       Fout[Fout2 + 1] = Fout[fout_ptr + 1] - t_i;
/*  75 */       Fout[fout_ptr + 0] = Fout[fout_ptr + 0] + t_r;
/*  76 */       Fout[fout_ptr + 1] = Fout[fout_ptr + 1] + t_i;
/*     */       
/*  78 */       t_r = S_MUL(Fout[Fout2 + 2] + Fout[Fout2 + 3], tw);
/*  79 */       t_i = S_MUL(Fout[Fout2 + 3] - Fout[Fout2 + 2], tw);
/*  80 */       Fout[Fout2 + 2] = Fout[fout_ptr + 2] - t_r;
/*  81 */       Fout[Fout2 + 3] = Fout[fout_ptr + 3] - t_i;
/*  82 */       Fout[fout_ptr + 2] = Fout[fout_ptr + 2] + t_r;
/*  83 */       Fout[fout_ptr + 3] = Fout[fout_ptr + 3] + t_i;
/*     */       
/*  85 */       t_r = Fout[Fout2 + 5];
/*  86 */       t_i = 0 - Fout[Fout2 + 4];
/*  87 */       Fout[Fout2 + 4] = Fout[fout_ptr + 4] - t_r;
/*  88 */       Fout[Fout2 + 5] = Fout[fout_ptr + 5] - t_i;
/*  89 */       Fout[fout_ptr + 4] = Fout[fout_ptr + 4] + t_r;
/*  90 */       Fout[fout_ptr + 5] = Fout[fout_ptr + 5] + t_i;
/*     */       
/*  92 */       t_r = S_MUL(Fout[Fout2 + 7] - Fout[Fout2 + 6], tw);
/*  93 */       t_i = S_MUL(0 - Fout[Fout2 + 7] - Fout[Fout2 + 6], tw);
/*  94 */       Fout[Fout2 + 6] = Fout[fout_ptr + 6] - t_r;
/*  95 */       Fout[Fout2 + 7] = Fout[fout_ptr + 7] - t_i;
/*  96 */       Fout[fout_ptr + 6] = Fout[fout_ptr + 6] + t_r;
/*  97 */       Fout[fout_ptr + 7] = Fout[fout_ptr + 7] + t_i;
/*     */       
/*  99 */       fout_ptr += 16;
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
/*     */   static void kf_bfly4(int[] Fout, int fout_ptr, int fstride, FFTState st, int m, int N, int mm) {
/* 114 */     if (m == 1) {
/*     */ 
/*     */       
/* 117 */       for (int i = 0; i < N; i++) {
/* 118 */         int scratch0 = Fout[fout_ptr + 0] - Fout[fout_ptr + 4];
/* 119 */         int scratch1 = Fout[fout_ptr + 1] - Fout[fout_ptr + 5];
/* 120 */         Fout[fout_ptr + 0] = Fout[fout_ptr + 0] + Fout[fout_ptr + 4];
/* 121 */         Fout[fout_ptr + 1] = Fout[fout_ptr + 1] + Fout[fout_ptr + 5];
/* 122 */         int scratch2 = Fout[fout_ptr + 2] + Fout[fout_ptr + 6];
/* 123 */         int scratch3 = Fout[fout_ptr + 3] + Fout[fout_ptr + 7];
/* 124 */         Fout[fout_ptr + 4] = Fout[fout_ptr + 0] - scratch2;
/* 125 */         Fout[fout_ptr + 5] = Fout[fout_ptr + 1] - scratch3;
/* 126 */         Fout[fout_ptr + 0] = Fout[fout_ptr + 0] + scratch2;
/* 127 */         Fout[fout_ptr + 1] = Fout[fout_ptr + 1] + scratch3;
/* 128 */         scratch2 = Fout[fout_ptr + 2] - Fout[fout_ptr + 6];
/* 129 */         scratch3 = Fout[fout_ptr + 3] - Fout[fout_ptr + 7];
/* 130 */         Fout[fout_ptr + 2] = scratch0 + scratch3;
/* 131 */         Fout[fout_ptr + 3] = scratch1 - scratch2;
/* 132 */         Fout[fout_ptr + 6] = scratch0 - scratch3;
/* 133 */         Fout[fout_ptr + 7] = scratch1 + scratch2;
/* 134 */         fout_ptr += 8;
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 140 */       int Fout_beg = fout_ptr;
/* 141 */       for (int i = 0; i < N; i++) {
/* 142 */         fout_ptr = Fout_beg + 2 * i * mm;
/* 143 */         int m1 = fout_ptr + 2 * m;
/* 144 */         int m2 = fout_ptr + 4 * m;
/* 145 */         int m3 = fout_ptr + 6 * m;
/* 146 */         int tw1 = 0, tw2 = tw1, tw3 = tw2;
/*     */         
/* 148 */         for (int j = 0; j < m; j++) {
/* 149 */           int scratch0 = S_MUL(Fout[m1], st.twiddles[tw1]) - S_MUL(Fout[m1 + 1], st.twiddles[tw1 + 1]);
/* 150 */           int scratch1 = S_MUL(Fout[m1], st.twiddles[tw1 + 1]) + S_MUL(Fout[m1 + 1], st.twiddles[tw1]);
/* 151 */           int scratch2 = S_MUL(Fout[m2], st.twiddles[tw2]) - S_MUL(Fout[m2 + 1], st.twiddles[tw2 + 1]);
/* 152 */           int scratch3 = S_MUL(Fout[m2], st.twiddles[tw2 + 1]) + S_MUL(Fout[m2 + 1], st.twiddles[tw2]);
/* 153 */           int scratch4 = S_MUL(Fout[m3], st.twiddles[tw3]) - S_MUL(Fout[m3 + 1], st.twiddles[tw3 + 1]);
/* 154 */           int scratch5 = S_MUL(Fout[m3], st.twiddles[tw3 + 1]) + S_MUL(Fout[m3 + 1], st.twiddles[tw3]);
/* 155 */           int scratch10 = Fout[fout_ptr] - scratch2;
/* 156 */           int scratch11 = Fout[fout_ptr + 1] - scratch3;
/* 157 */           Fout[fout_ptr] = Fout[fout_ptr] + scratch2;
/* 158 */           Fout[fout_ptr + 1] = Fout[fout_ptr + 1] + scratch3;
/* 159 */           int scratch6 = scratch0 + scratch4;
/* 160 */           int scratch7 = scratch1 + scratch5;
/* 161 */           int scratch8 = scratch0 - scratch4;
/* 162 */           int scratch9 = scratch1 - scratch5;
/* 163 */           Fout[m2] = Fout[fout_ptr] - scratch6;
/* 164 */           Fout[m2 + 1] = Fout[fout_ptr + 1] - scratch7;
/* 165 */           tw1 += fstride * 2;
/* 166 */           tw2 += fstride * 4;
/* 167 */           tw3 += fstride * 6;
/* 168 */           Fout[fout_ptr] = Fout[fout_ptr] + scratch6;
/* 169 */           Fout[fout_ptr + 1] = Fout[fout_ptr + 1] + scratch7;
/* 170 */           Fout[m1] = scratch10 + scratch9;
/* 171 */           Fout[m1 + 1] = scratch11 - scratch8;
/* 172 */           Fout[m3] = scratch10 - scratch9;
/* 173 */           Fout[m3 + 1] = scratch11 + scratch8;
/* 174 */           fout_ptr += 2;
/* 175 */           m1 += 2;
/* 176 */           m2 += 2;
/* 177 */           m3 += 2;
/*     */         } 
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
/*     */   static void kf_bfly3(int[] Fout, int fout_ptr, int fstride, FFTState st, int m, int N, int mm) {
/* 194 */     int m1 = 2 * m;
/* 195 */     int m2 = 4 * m;
/*     */ 
/*     */ 
/*     */     
/* 199 */     int Fout_beg = fout_ptr;
/*     */     
/* 201 */     for (int i = 0; i < N; ) {
/* 202 */       fout_ptr = Fout_beg + 2 * i * mm;
/* 203 */       int tw2 = 0, tw1 = tw2;
/*     */       
/* 205 */       int k = m;
/*     */       while (true) {
/* 207 */         int scratch2 = S_MUL(Fout[fout_ptr + m1], st.twiddles[tw1]) - S_MUL(Fout[fout_ptr + m1 + 1], st.twiddles[tw1 + 1]);
/* 208 */         int scratch3 = S_MUL(Fout[fout_ptr + m1], st.twiddles[tw1 + 1]) + S_MUL(Fout[fout_ptr + m1 + 1], st.twiddles[tw1]);
/* 209 */         int scratch4 = S_MUL(Fout[fout_ptr + m2], st.twiddles[tw2]) - S_MUL(Fout[fout_ptr + m2 + 1], st.twiddles[tw2 + 1]);
/* 210 */         int scratch5 = S_MUL(Fout[fout_ptr + m2], st.twiddles[tw2 + 1]) + S_MUL(Fout[fout_ptr + m2 + 1], st.twiddles[tw2]);
/*     */         
/* 212 */         int scratch6 = scratch2 + scratch4;
/* 213 */         int scratch7 = scratch3 + scratch5;
/* 214 */         int scratch0 = scratch2 - scratch4;
/* 215 */         int scratch1 = scratch3 - scratch5;
/*     */         
/* 217 */         tw1 += fstride * 2;
/* 218 */         tw2 += fstride * 4;
/*     */         
/* 220 */         Fout[fout_ptr + m1] = Fout[fout_ptr + 0] - HALF_OF(scratch6);
/* 221 */         Fout[fout_ptr + m1 + 1] = Fout[fout_ptr + 1] - HALF_OF(scratch7);
/*     */         
/* 223 */         scratch0 = S_MUL(scratch0, -28378);
/* 224 */         scratch1 = S_MUL(scratch1, -28378);
/*     */         
/* 226 */         Fout[fout_ptr + 0] = Fout[fout_ptr + 0] + scratch6;
/* 227 */         Fout[fout_ptr + 1] = Fout[fout_ptr + 1] + scratch7;
/*     */         
/* 229 */         Fout[fout_ptr + m2] = Fout[fout_ptr + m1] + scratch1;
/* 230 */         Fout[fout_ptr + m2 + 1] = Fout[fout_ptr + m1 + 1] - scratch0;
/*     */         
/* 232 */         Fout[fout_ptr + m1] = Fout[fout_ptr + m1] - scratch1;
/* 233 */         Fout[fout_ptr + m1 + 1] = Fout[fout_ptr + m1 + 1] + scratch0;
/*     */         
/* 235 */         fout_ptr += 2;
/* 236 */         if (--k == 0) {
/*     */           i++;
/*     */         }
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
/*     */   static void kf_bfly5(int[] Fout, int fout_ptr, int fstride, FFTState st, int m, int N, int mm) {
/* 257 */     int Fout_beg = fout_ptr;
/*     */     
/* 259 */     short ya_r = 10126;
/* 260 */     short ya_i = -31164;
/* 261 */     short yb_r = -26510;
/* 262 */     short yb_i = -19261;
/*     */ 
/*     */     
/* 265 */     for (int i = 0; i < N; i++) {
/* 266 */       int tw4 = 0, tw3 = tw4, tw2 = tw3, tw1 = tw2;
/* 267 */       fout_ptr = Fout_beg + 2 * i * mm;
/* 268 */       int Fout0 = fout_ptr;
/* 269 */       int Fout1 = fout_ptr + 2 * m;
/* 270 */       int Fout2 = fout_ptr + 4 * m;
/* 271 */       int Fout3 = fout_ptr + 6 * m;
/* 272 */       int Fout4 = fout_ptr + 8 * m;
/*     */ 
/*     */       
/* 275 */       for (int u = 0; u < m; u++) {
/* 276 */         int scratch0 = Fout[Fout0 + 0];
/* 277 */         int scratch1 = Fout[Fout0 + 1];
/*     */         
/* 279 */         int scratch2 = S_MUL(Fout[Fout1 + 0], st.twiddles[tw1]) - S_MUL(Fout[Fout1 + 1], st.twiddles[tw1 + 1]);
/* 280 */         int scratch3 = S_MUL(Fout[Fout1 + 0], st.twiddles[tw1 + 1]) + S_MUL(Fout[Fout1 + 1], st.twiddles[tw1]);
/* 281 */         int scratch4 = S_MUL(Fout[Fout2 + 0], st.twiddles[tw2]) - S_MUL(Fout[Fout2 + 1], st.twiddles[tw2 + 1]);
/* 282 */         int scratch5 = S_MUL(Fout[Fout2 + 0], st.twiddles[tw2 + 1]) + S_MUL(Fout[Fout2 + 1], st.twiddles[tw2]);
/* 283 */         int scratch6 = S_MUL(Fout[Fout3 + 0], st.twiddles[tw3]) - S_MUL(Fout[Fout3 + 1], st.twiddles[tw3 + 1]);
/* 284 */         int scratch7 = S_MUL(Fout[Fout3 + 0], st.twiddles[tw3 + 1]) + S_MUL(Fout[Fout3 + 1], st.twiddles[tw3]);
/* 285 */         int scratch8 = S_MUL(Fout[Fout4 + 0], st.twiddles[tw4]) - S_MUL(Fout[Fout4 + 1], st.twiddles[tw4 + 1]);
/* 286 */         int scratch9 = S_MUL(Fout[Fout4 + 0], st.twiddles[tw4 + 1]) + S_MUL(Fout[Fout4 + 1], st.twiddles[tw4]);
/*     */         
/* 288 */         tw1 += 2 * fstride;
/* 289 */         tw2 += 4 * fstride;
/* 290 */         tw3 += 6 * fstride;
/* 291 */         tw4 += 8 * fstride;
/*     */         
/* 293 */         int scratch14 = scratch2 + scratch8;
/* 294 */         int scratch15 = scratch3 + scratch9;
/* 295 */         int scratch20 = scratch2 - scratch8;
/* 296 */         int scratch21 = scratch3 - scratch9;
/* 297 */         int scratch16 = scratch4 + scratch6;
/* 298 */         int scratch17 = scratch5 + scratch7;
/* 299 */         int scratch18 = scratch4 - scratch6;
/* 300 */         int scratch19 = scratch5 - scratch7;
/*     */         
/* 302 */         Fout[Fout0 + 0] = Fout[Fout0 + 0] + scratch14 + scratch16;
/* 303 */         Fout[Fout0 + 1] = Fout[Fout0 + 1] + scratch15 + scratch17;
/*     */         
/* 305 */         int scratch10 = scratch0 + S_MUL(scratch14, ya_r) + S_MUL(scratch16, yb_r);
/* 306 */         int scratch11 = scratch1 + S_MUL(scratch15, ya_r) + S_MUL(scratch17, yb_r);
/*     */         
/* 308 */         int scratch12 = S_MUL(scratch21, ya_i) + S_MUL(scratch19, yb_i);
/* 309 */         int scratch13 = 0 - S_MUL(scratch20, ya_i) - S_MUL(scratch18, yb_i);
/*     */         
/* 311 */         Fout[Fout1 + 0] = scratch10 - scratch12;
/* 312 */         Fout[Fout1 + 1] = scratch11 - scratch13;
/* 313 */         Fout[Fout4 + 0] = scratch10 + scratch12;
/* 314 */         Fout[Fout4 + 1] = scratch11 + scratch13;
/*     */         
/* 316 */         int scratch22 = scratch0 + S_MUL(scratch14, yb_r) + S_MUL(scratch16, ya_r);
/* 317 */         int scratch23 = scratch1 + S_MUL(scratch15, yb_r) + S_MUL(scratch17, ya_r);
/* 318 */         int scratch24 = 0 - S_MUL(scratch21, yb_i) + S_MUL(scratch19, ya_i);
/* 319 */         int scratch25 = S_MUL(scratch20, yb_i) - S_MUL(scratch18, ya_i);
/*     */         
/* 321 */         Fout[Fout2 + 0] = scratch22 + scratch24;
/* 322 */         Fout[Fout2 + 1] = scratch23 + scratch25;
/* 323 */         Fout[Fout3 + 0] = scratch22 - scratch24;
/* 324 */         Fout[Fout3 + 1] = scratch23 - scratch25;
/*     */         
/* 326 */         Fout0 += 2;
/* 327 */         Fout1 += 2;
/* 328 */         Fout2 += 2;
/* 329 */         Fout3 += 2;
/* 330 */         Fout4 += 2;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void opus_fft_impl(FFTState st, int[] fout, int fout_ptr) {
/* 339 */     int[] fstride = new int[8];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 344 */     int shift = (st.shift > 0) ? st.shift : 0;
/*     */     
/* 346 */     fstride[0] = 1;
/* 347 */     int L = 0;
/*     */     do {
/* 349 */       int p = st.factors[2 * L];
/* 350 */       m = st.factors[2 * L + 1];
/* 351 */       fstride[L + 1] = fstride[L] * p;
/* 352 */       L++;
/* 353 */     } while (m != 1);
/*     */     
/* 355 */     int m = st.factors[2 * L - 1];
/* 356 */     for (int i = L - 1; i >= 0; i--) {
/* 357 */       int m2; if (i != 0) {
/* 358 */         m2 = st.factors[2 * i - 1];
/*     */       } else {
/* 360 */         m2 = 1;
/*     */       } 
/* 362 */       switch (st.factors[2 * i]) {
/*     */         case 2:
/* 364 */           kf_bfly2(fout, fout_ptr, m, fstride[i]);
/*     */           break;
/*     */         case 4:
/* 367 */           kf_bfly4(fout, fout_ptr, fstride[i] << shift, st, m, fstride[i], m2);
/*     */           break;
/*     */         case 3:
/* 370 */           kf_bfly3(fout, fout_ptr, fstride[i] << shift, st, m, fstride[i], m2);
/*     */           break;
/*     */         case 5:
/* 373 */           kf_bfly5(fout, fout_ptr, fstride[i] << shift, st, m, fstride[i], m2);
/*     */           break;
/*     */       } 
/* 376 */       m = m2;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void opus_fft(FFTState st, int[] fin, int[] fout) {
/* 383 */     int scale_shift = st.scale_shift - 1;
/* 384 */     short scale = st.scale;
/*     */     
/* 386 */     Inlines.OpusAssert((fin != fout), "In-place FFT not supported");
/*     */ 
/*     */     
/* 389 */     for (int i = 0; i < st.nfft; i++) {
/* 390 */       fout[2 * st.bitrev[i]] = Inlines.SHR32(Inlines.MULT16_32_Q16(scale, fin[2 * i]), scale_shift);
/* 391 */       fout[2 * st.bitrev[i] + 1] = Inlines.SHR32(Inlines.MULT16_32_Q16(scale, fin[2 * i + 1]), scale_shift);
/*     */     } 
/*     */     
/* 394 */     opus_fft_impl(st, fout, 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\KissFFT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */