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
/*     */ class Kernels
/*     */ {
/*     */   static void celt_fir(short[] x, int x_ptr, short[] num, short[] y, int y_ptr, int N, int ord, short[] mem) {
/*  50 */     short[] rnum = new short[ord];
/*  51 */     short[] local_x = new short[N + ord];
/*     */     int i;
/*  53 */     for (i = 0; i < ord; i++) {
/*  54 */       rnum[i] = num[ord - i - 1];
/*     */     }
/*     */     
/*  57 */     for (i = 0; i < ord; i++) {
/*  58 */       local_x[i] = mem[ord - i - 1];
/*     */     }
/*     */     
/*  61 */     for (i = 0; i < N; i++) {
/*  62 */       local_x[i + ord] = x[x_ptr + i];
/*     */     }
/*     */     
/*  65 */     for (i = 0; i < ord; i++) {
/*  66 */       mem[i] = x[x_ptr + N - i - 1];
/*     */     }
/*     */     
/*  69 */     BoxedValueInt sum0 = new BoxedValueInt(0);
/*  70 */     BoxedValueInt sum1 = new BoxedValueInt(0);
/*  71 */     BoxedValueInt sum2 = new BoxedValueInt(0);
/*  72 */     BoxedValueInt sum3 = new BoxedValueInt(0);
/*     */     
/*  74 */     for (i = 0; i < N - 3; i += 4) {
/*  75 */       sum0.Val = 0;
/*  76 */       sum1.Val = 0;
/*  77 */       sum2.Val = 0;
/*  78 */       sum3.Val = 0;
/*  79 */       xcorr_kernel(rnum, 0, local_x, i, sum0, sum1, sum2, sum3, ord);
/*  80 */       y[y_ptr + i] = Inlines.SATURATE16(Inlines.ADD32(Inlines.EXTEND32(x[x_ptr + i]), Inlines.PSHR32(sum0.Val, 12)));
/*  81 */       y[y_ptr + i + 1] = Inlines.SATURATE16(Inlines.ADD32(Inlines.EXTEND32(x[x_ptr + i + 1]), Inlines.PSHR32(sum1.Val, 12)));
/*  82 */       y[y_ptr + i + 2] = Inlines.SATURATE16(Inlines.ADD32(Inlines.EXTEND32(x[x_ptr + i + 2]), Inlines.PSHR32(sum2.Val, 12)));
/*  83 */       y[y_ptr + i + 3] = Inlines.SATURATE16(Inlines.ADD32(Inlines.EXTEND32(x[x_ptr + i + 3]), Inlines.PSHR32(sum3.Val, 12)));
/*     */     } 
/*     */     
/*  86 */     for (; i < N; i++) {
/*  87 */       int sum = 0;
/*     */       
/*  89 */       for (int j = 0; j < ord; j++) {
/*  90 */         sum = Inlines.MAC16_16(sum, rnum[j], local_x[i + j]);
/*     */       }
/*     */       
/*  93 */       y[y_ptr + i] = Inlines.SATURATE16(Inlines.ADD32(Inlines.EXTEND32(x[x_ptr + i]), Inlines.PSHR32(sum, 12)));
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
/*     */   static void celt_fir(int[] x, int x_ptr, int[] num, int num_ptr, int[] y, int y_ptr, int N, int ord, int[] mem) {
/* 109 */     int[] rnum = new int[ord];
/* 110 */     int[] local_x = new int[N + ord];
/*     */     int i;
/* 112 */     for (i = 0; i < ord; i++) {
/* 113 */       rnum[i] = num[num_ptr + ord - i - 1];
/*     */     }
/*     */     
/* 116 */     for (i = 0; i < ord; i++) {
/* 117 */       local_x[i] = mem[ord - i - 1];
/*     */     }
/*     */     
/* 120 */     for (i = 0; i < N; i++) {
/* 121 */       local_x[i + ord] = x[x_ptr + i];
/*     */     }
/*     */     
/* 124 */     for (i = 0; i < ord; i++) {
/* 125 */       mem[i] = x[x_ptr + N - i - 1];
/*     */     }
/*     */     
/* 128 */     BoxedValueInt sum0 = new BoxedValueInt(0);
/* 129 */     BoxedValueInt sum1 = new BoxedValueInt(0);
/* 130 */     BoxedValueInt sum2 = new BoxedValueInt(0);
/* 131 */     BoxedValueInt sum3 = new BoxedValueInt(0);
/*     */     
/* 133 */     for (i = 0; i < N - 3; i += 4) {
/* 134 */       sum0.Val = 0;
/* 135 */       sum1.Val = 0;
/* 136 */       sum2.Val = 0;
/* 137 */       sum3.Val = 0;
/* 138 */       xcorr_kernel(rnum, local_x, i, sum0, sum1, sum2, sum3, ord);
/* 139 */       y[y_ptr + i] = Inlines.SATURATE16(Inlines.ADD32(Inlines.EXTEND32(x[x_ptr + i]), Inlines.PSHR32(sum0.Val, 12)));
/* 140 */       y[y_ptr + i + 1] = Inlines.SATURATE16(Inlines.ADD32(Inlines.EXTEND32(x[x_ptr + i + 1]), Inlines.PSHR32(sum1.Val, 12)));
/* 141 */       y[y_ptr + i + 2] = Inlines.SATURATE16(Inlines.ADD32(Inlines.EXTEND32(x[x_ptr + i + 2]), Inlines.PSHR32(sum2.Val, 12)));
/* 142 */       y[y_ptr + i + 3] = Inlines.SATURATE16(Inlines.ADD32(Inlines.EXTEND32(x[x_ptr + i + 3]), Inlines.PSHR32(sum3.Val, 12)));
/*     */     } 
/*     */     
/* 145 */     for (; i < N; i++) {
/* 146 */       int sum = 0;
/*     */       
/* 148 */       for (int j = 0; j < ord; j++) {
/* 149 */         sum = Inlines.MAC16_16(sum, rnum[j], local_x[i + j]);
/*     */       }
/*     */       
/* 152 */       y[y_ptr + i] = Inlines.SATURATE16(Inlines.ADD32(Inlines.EXTEND32(x[x_ptr + i]), Inlines.PSHR32(sum, 12)));
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
/*     */   static void xcorr_kernel(short[] x, int x_ptr, short[] y, int y_ptr, BoxedValueInt _sum0, BoxedValueInt _sum1, BoxedValueInt _sum2, BoxedValueInt _sum3, int len) {
/* 164 */     int sum0 = _sum0.Val;
/* 165 */     int sum1 = _sum1.Val;
/* 166 */     int sum2 = _sum2.Val;
/* 167 */     int sum3 = _sum3.Val;
/*     */ 
/*     */     
/* 170 */     Inlines.OpusAssert((len >= 3));
/* 171 */     short y_3 = 0;
/*     */     
/* 173 */     short y_0 = y[y_ptr++];
/* 174 */     short y_1 = y[y_ptr++];
/* 175 */     short y_2 = y[y_ptr++]; int j;
/* 176 */     for (j = 0; j < len - 3; j += 4) {
/*     */       
/* 178 */       short tmp = x[x_ptr++];
/* 179 */       y_3 = y[y_ptr++];
/* 180 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_0);
/* 181 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_1);
/* 182 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_2);
/* 183 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_3);
/* 184 */       tmp = x[x_ptr++];
/* 185 */       y_0 = y[y_ptr++];
/* 186 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_1);
/* 187 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_2);
/* 188 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_3);
/* 189 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_0);
/* 190 */       tmp = x[x_ptr++];
/* 191 */       y_1 = y[y_ptr++];
/* 192 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_2);
/* 193 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_3);
/* 194 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_0);
/* 195 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_1);
/* 196 */       tmp = x[x_ptr++];
/* 197 */       y_2 = y[y_ptr++];
/* 198 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_3);
/* 199 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_0);
/* 200 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_1);
/* 201 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_2);
/*     */     } 
/* 203 */     if (j++ < len) {
/*     */       
/* 205 */       short tmp = x[x_ptr++];
/* 206 */       y_3 = y[y_ptr++];
/* 207 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_0);
/* 208 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_1);
/* 209 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_2);
/* 210 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_3);
/*     */     } 
/* 212 */     if (j++ < len) {
/*     */       
/* 214 */       short tmp = x[x_ptr++];
/* 215 */       y_0 = y[y_ptr++];
/* 216 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_1);
/* 217 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_2);
/* 218 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_3);
/* 219 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_0);
/*     */     } 
/* 221 */     if (j < len) {
/*     */       
/* 223 */       short tmp = x[x_ptr++];
/* 224 */       y_1 = y[y_ptr++];
/* 225 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_2);
/* 226 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_3);
/* 227 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_0);
/* 228 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_1);
/*     */     } 
/*     */     
/* 231 */     _sum0.Val = sum0;
/* 232 */     _sum1.Val = sum1;
/* 233 */     _sum2.Val = sum2;
/* 234 */     _sum3.Val = sum3;
/*     */   }
/*     */   
/*     */   static void xcorr_kernel(int[] x, int[] y, int y_ptr, BoxedValueInt _sum0, BoxedValueInt _sum1, BoxedValueInt _sum2, BoxedValueInt _sum3, int len) {
/* 238 */     int sum0 = _sum0.Val;
/* 239 */     int sum1 = _sum1.Val;
/* 240 */     int sum2 = _sum2.Val;
/* 241 */     int sum3 = _sum3.Val;
/*     */ 
/*     */     
/* 244 */     int x_ptr = 0;
/* 245 */     Inlines.OpusAssert((len >= 3));
/* 246 */     int y_3 = 0;
/*     */     
/* 248 */     int y_0 = y[y_ptr++];
/* 249 */     int y_1 = y[y_ptr++];
/* 250 */     int y_2 = y[y_ptr++]; int j;
/* 251 */     for (j = 0; j < len - 3; j += 4) {
/*     */       
/* 253 */       int tmp = x[x_ptr++];
/* 254 */       y_3 = y[y_ptr++];
/* 255 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_0);
/* 256 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_1);
/* 257 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_2);
/* 258 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_3);
/* 259 */       tmp = x[x_ptr++];
/* 260 */       y_0 = y[y_ptr++];
/* 261 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_1);
/* 262 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_2);
/* 263 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_3);
/* 264 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_0);
/* 265 */       tmp = x[x_ptr++];
/* 266 */       y_1 = y[y_ptr++];
/* 267 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_2);
/* 268 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_3);
/* 269 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_0);
/* 270 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_1);
/* 271 */       tmp = x[x_ptr++];
/* 272 */       y_2 = y[y_ptr++];
/* 273 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_3);
/* 274 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_0);
/* 275 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_1);
/* 276 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_2);
/*     */     } 
/* 278 */     if (j++ < len) {
/*     */       
/* 280 */       int tmp = x[x_ptr++];
/* 281 */       y_3 = y[y_ptr++];
/* 282 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_0);
/* 283 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_1);
/* 284 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_2);
/* 285 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_3);
/*     */     } 
/* 287 */     if (j++ < len) {
/*     */       
/* 289 */       int tmp = x[x_ptr++];
/* 290 */       y_0 = y[y_ptr++];
/* 291 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_1);
/* 292 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_2);
/* 293 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_3);
/* 294 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_0);
/*     */     } 
/* 296 */     if (j < len) {
/*     */       
/* 298 */       int tmp = x[x_ptr++];
/* 299 */       y_1 = y[y_ptr++];
/* 300 */       sum0 = Inlines.MAC16_16(sum0, tmp, y_2);
/* 301 */       sum1 = Inlines.MAC16_16(sum1, tmp, y_3);
/* 302 */       sum2 = Inlines.MAC16_16(sum2, tmp, y_0);
/* 303 */       sum3 = Inlines.MAC16_16(sum3, tmp, y_1);
/*     */     } 
/*     */     
/* 306 */     _sum0.Val = sum0;
/* 307 */     _sum1.Val = sum1;
/* 308 */     _sum2.Val = sum2;
/* 309 */     _sum3.Val = sum3;
/*     */   }
/*     */ 
/*     */   
/*     */   static int celt_inner_prod(short[] x, int x_ptr, short[] y, int y_ptr, int N) {
/* 314 */     int xy = 0;
/* 315 */     for (int i = 0; i < N; i++) {
/* 316 */       xy = Inlines.MAC16_16(xy, x[x_ptr + i], y[y_ptr + i]);
/*     */     }
/* 318 */     return xy;
/*     */   }
/*     */ 
/*     */   
/*     */   static int celt_inner_prod(short[] x, short[] y, int y_ptr, int N) {
/* 323 */     int xy = 0;
/* 324 */     for (int i = 0; i < N; i++) {
/* 325 */       xy = Inlines.MAC16_16(xy, x[i], y[y_ptr + i]);
/*     */     }
/* 327 */     return xy;
/*     */   }
/*     */ 
/*     */   
/*     */   static int celt_inner_prod(int[] x, int x_ptr, int[] y, int y_ptr, int N) {
/* 332 */     int xy = 0;
/* 333 */     for (int i = 0; i < N; i++) {
/* 334 */       xy = Inlines.MAC16_16(xy, x[x_ptr + i], y[y_ptr + i]);
/*     */     }
/* 336 */     return xy;
/*     */   }
/*     */ 
/*     */   
/*     */   static void dual_inner_prod(int[] x, int x_ptr, int[] y01, int y01_ptr, int[] y02, int y02_ptr, int N, BoxedValueInt xy1, BoxedValueInt xy2) {
/* 341 */     int xy01 = 0;
/* 342 */     int xy02 = 0;
/* 343 */     for (int i = 0; i < N; i++) {
/* 344 */       xy01 = Inlines.MAC16_16(xy01, x[x_ptr + i], y01[y01_ptr + i]);
/* 345 */       xy02 = Inlines.MAC16_16(xy02, x[x_ptr + i], y02[y02_ptr + i]);
/*     */     } 
/* 347 */     xy1.Val = xy01;
/* 348 */     xy2.Val = xy02;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Kernels.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */