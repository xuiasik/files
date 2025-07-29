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
/*     */ class BurgModified
/*     */ {
/*     */   private static final int MAX_FRAME_SIZE = 384;
/*     */   private static final int QA = 25;
/*     */   private static final int N_BITS_HEAD_ROOM = 2;
/*     */   private static final int MIN_RSHIFTS = -16;
/*     */   private static final int MAX_RSHIFTS = 7;
/*     */   
/*     */   static void silk_burg_modified(BoxedValueInt res_nrg, BoxedValueInt res_nrg_Q, int[] A_Q16, short[] x, int x_ptr, int minInvGain_Q30, int subfr_length, int nb_subfr, int D) {
/*  58 */     int C0, C_first_row[] = new int[16];
/*  59 */     int[] C_last_row = new int[16];
/*  60 */     int[] Af_QA = new int[16];
/*  61 */     int[] CAf = new int[17];
/*  62 */     int[] CAb = new int[17];
/*  63 */     int[] xcorr = new int[16];
/*     */ 
/*     */     
/*  66 */     Inlines.OpusAssert((subfr_length * nb_subfr <= 384));
/*     */ 
/*     */     
/*  69 */     long C0_64 = Inlines.silk_inner_prod16_aligned_64(x, x_ptr, x, x_ptr, subfr_length * nb_subfr);
/*  70 */     int lz = Inlines.silk_CLZ64(C0_64);
/*  71 */     int rshifts = 35 - lz;
/*  72 */     if (rshifts > 7) {
/*  73 */       rshifts = 7;
/*     */     }
/*  75 */     if (rshifts < -16) {
/*  76 */       rshifts = -16;
/*     */     }
/*     */     
/*  79 */     if (rshifts > 0) {
/*  80 */       C0 = (int)Inlines.silk_RSHIFT64(C0_64, rshifts);
/*     */     } else {
/*  82 */       C0 = Inlines.silk_LSHIFT32((int)C0_64, -rshifts);
/*     */     } 
/*     */     
/*  85 */     CAf[0] = C0 + Inlines.silk_SMMUL(42950, C0) + 1; CAb[0] = C0 + Inlines.silk_SMMUL(42950, C0) + 1;
/*     */     
/*  87 */     Arrays.MemSet(C_first_row, 0, 16);
/*  88 */     if (rshifts > 0) {
/*  89 */       for (int s = 0; s < nb_subfr; s++) {
/*  90 */         int x_offset = x_ptr + s * subfr_length;
/*  91 */         for (int i = 1; i < D + 1; i++) {
/*  92 */           C_first_row[i - 1] = C_first_row[i - 1] + (int)Inlines.silk_RSHIFT64(
/*  93 */               Inlines.silk_inner_prod16_aligned_64(x, x_offset, x, x_offset + i, subfr_length - i), rshifts);
/*     */         }
/*     */       } 
/*     */     } else {
/*  97 */       for (int s = 0; s < nb_subfr; s++) {
/*     */ 
/*     */         
/* 100 */         int x_offset = x_ptr + s * subfr_length;
/* 101 */         CeltPitchXCorr.pitch_xcorr(x, x_offset, x, x_offset + 1, xcorr, subfr_length - D, D); int i;
/* 102 */         for (i = 1; i < D + 1; i++) {
/* 103 */           int d; for (int j = i + subfr_length - D; j < subfr_length; j++) {
/* 104 */             d = Inlines.MAC16_16(d, x[x_offset + j], x[x_offset + j - i]);
/*     */           }
/* 106 */           xcorr[i - 1] = xcorr[i - 1] + d;
/*     */         } 
/* 108 */         for (i = 1; i < D + 1; i++) {
/* 109 */           C_first_row[i - 1] = C_first_row[i - 1] + Inlines.silk_LSHIFT32(xcorr[i - 1], -rshifts);
/*     */         }
/*     */       } 
/*     */     } 
/* 113 */     System.arraycopy(C_first_row, 0, C_last_row, 0, 16);
/*     */ 
/*     */     
/* 116 */     CAf[0] = C0 + Inlines.silk_SMMUL(42950, C0) + 1; CAb[0] = C0 + Inlines.silk_SMMUL(42950, C0) + 1;
/*     */ 
/*     */     
/* 119 */     int invGain_Q30 = 1073741824;
/* 120 */     int reached_max_gain = 0;
/* 121 */     for (int n = 0; n < D; n++) {
/*     */       int rc_Q31;
/*     */ 
/*     */ 
/*     */       
/* 126 */       if (rshifts > -2) {
/* 127 */         for (byte b = 0; b < nb_subfr; b++) {
/* 128 */           int x_offset = x_ptr + b * subfr_length;
/* 129 */           int x1 = -Inlines.silk_LSHIFT32(x[x_offset + n], 16 - rshifts);
/*     */           
/* 131 */           int x2 = -Inlines.silk_LSHIFT32(x[x_offset + subfr_length - n - 1], 16 - rshifts);
/*     */           
/* 133 */           int j = Inlines.silk_LSHIFT32(x[x_offset + n], 9);
/*     */           
/* 135 */           int m = Inlines.silk_LSHIFT32(x[x_offset + subfr_length - n - 1], 9);
/*     */           int i;
/* 137 */           for (i = 0; i < n; i++) {
/* 138 */             C_first_row[i] = Inlines.silk_SMLAWB(C_first_row[i], x1, x[x_offset + n - i - 1]);
/*     */             
/* 140 */             C_last_row[i] = Inlines.silk_SMLAWB(C_last_row[i], x2, x[x_offset + subfr_length - n + i]);
/*     */             
/* 142 */             int Atmp_QA = Af_QA[i];
/* 143 */             j = Inlines.silk_SMLAWB(j, Atmp_QA, x[x_offset + n - i - 1]);
/*     */             
/* 145 */             m = Inlines.silk_SMLAWB(m, Atmp_QA, x[x_offset + subfr_length - n + i]);
/*     */           } 
/*     */           
/* 148 */           j = Inlines.silk_LSHIFT32(-j, 7 - rshifts);
/*     */           
/* 150 */           m = Inlines.silk_LSHIFT32(-m, 7 - rshifts);
/*     */           
/* 152 */           for (i = 0; i <= n; i++) {
/* 153 */             CAf[i] = Inlines.silk_SMLAWB(CAf[i], j, x[x_offset + n - i]);
/*     */             
/* 155 */             CAb[i] = Inlines.silk_SMLAWB(CAb[i], m, x[x_offset + subfr_length - n + i - 1]);
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 160 */         for (byte b = 0; b < nb_subfr; b++) {
/* 161 */           int x_offset = x_ptr + b * subfr_length;
/* 162 */           int x1 = -Inlines.silk_LSHIFT32(x[x_offset + n], -rshifts);
/*     */           
/* 164 */           int x2 = -Inlines.silk_LSHIFT32(x[x_offset + subfr_length - n - 1], -rshifts);
/*     */           
/* 166 */           int j = Inlines.silk_LSHIFT32(x[x_offset + n], 17);
/*     */           
/* 168 */           int m = Inlines.silk_LSHIFT32(x[x_offset + subfr_length - n - 1], 17);
/*     */           int i;
/* 170 */           for (i = 0; i < n; i++) {
/* 171 */             C_first_row[i] = Inlines.silk_MLA(C_first_row[i], x1, x[x_offset + n - i - 1]);
/*     */             
/* 173 */             C_last_row[i] = Inlines.silk_MLA(C_last_row[i], x2, x[x_offset + subfr_length - n + i]);
/*     */             
/* 175 */             int Atmp1 = Inlines.silk_RSHIFT_ROUND(Af_QA[i], 8);
/*     */             
/* 177 */             j = Inlines.silk_MLA(j, x[x_offset + n - i - 1], Atmp1);
/*     */             
/* 179 */             m = Inlines.silk_MLA(m, x[x_offset + subfr_length - n + i], Atmp1);
/*     */           } 
/*     */           
/* 182 */           j = -j;
/*     */           
/* 184 */           m = -m;
/*     */           
/* 186 */           for (i = 0; i <= n; i++) {
/* 187 */             CAf[i] = Inlines.silk_SMLAWW(CAf[i], j, 
/* 188 */                 Inlines.silk_LSHIFT32(x[x_offset + n - i], -rshifts - 1));
/*     */             
/* 190 */             CAb[i] = Inlines.silk_SMLAWW(CAb[i], m, 
/* 191 */                 Inlines.silk_LSHIFT32(x[x_offset + subfr_length - n + i - 1], -rshifts - 1));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 198 */       int tmp1 = C_first_row[n];
/*     */       
/* 200 */       int tmp2 = C_last_row[n];
/*     */       
/* 202 */       int num = 0;
/*     */       
/* 204 */       int nrg = Inlines.silk_ADD32(CAb[0], CAf[0]);
/*     */       int k;
/* 206 */       for (k = 0; k < n; k++) {
/* 207 */         int Atmp_QA = Af_QA[k];
/* 208 */         lz = Inlines.silk_CLZ32(Inlines.silk_abs(Atmp_QA)) - 1;
/* 209 */         lz = Inlines.silk_min(7, lz);
/* 210 */         int Atmp1 = Inlines.silk_LSHIFT32(Atmp_QA, lz);
/*     */ 
/*     */         
/* 213 */         tmp1 = Inlines.silk_ADD_LSHIFT32(tmp1, Inlines.silk_SMMUL(C_last_row[n - k - 1], Atmp1), 7 - lz);
/*     */         
/* 215 */         tmp2 = Inlines.silk_ADD_LSHIFT32(tmp2, Inlines.silk_SMMUL(C_first_row[n - k - 1], Atmp1), 7 - lz);
/*     */         
/* 217 */         num = Inlines.silk_ADD_LSHIFT32(num, Inlines.silk_SMMUL(CAb[n - k], Atmp1), 7 - lz);
/*     */         
/* 219 */         nrg = Inlines.silk_ADD_LSHIFT32(nrg, Inlines.silk_SMMUL(Inlines.silk_ADD32(CAb[k + 1], CAf[k + 1]), Atmp1), 7 - lz);
/*     */       } 
/*     */ 
/*     */       
/* 223 */       CAf[n + 1] = tmp1;
/*     */       
/* 225 */       CAb[n + 1] = tmp2;
/*     */       
/* 227 */       num = Inlines.silk_ADD32(num, tmp2);
/*     */       
/* 229 */       num = Inlines.silk_LSHIFT32(-num, 1);
/*     */ 
/*     */ 
/*     */       
/* 233 */       if (Inlines.silk_abs(num) < nrg) {
/* 234 */         rc_Q31 = Inlines.silk_DIV32_varQ(num, nrg, 31);
/*     */       } else {
/* 236 */         rc_Q31 = (num > 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
/*     */       } 
/*     */ 
/*     */       
/* 240 */       tmp1 = 1073741824 - Inlines.silk_SMMUL(rc_Q31, rc_Q31);
/* 241 */       tmp1 = Inlines.silk_LSHIFT(Inlines.silk_SMMUL(invGain_Q30, tmp1), 2);
/* 242 */       if (tmp1 <= minInvGain_Q30) {
/*     */         
/* 244 */         tmp2 = 1073741824 - Inlines.silk_DIV32_varQ(minInvGain_Q30, invGain_Q30, 30);
/*     */         
/* 246 */         rc_Q31 = Inlines.silk_SQRT_APPROX(tmp2);
/*     */ 
/*     */         
/* 249 */         rc_Q31 = Inlines.silk_RSHIFT32(rc_Q31 + Inlines.silk_DIV32(tmp2, rc_Q31), 1);
/*     */         
/* 251 */         rc_Q31 = Inlines.silk_LSHIFT32(rc_Q31, 16);
/*     */         
/* 253 */         if (num < 0)
/*     */         {
/* 255 */           rc_Q31 = -rc_Q31;
/*     */         }
/* 257 */         invGain_Q30 = minInvGain_Q30;
/* 258 */         reached_max_gain = 1;
/*     */       } else {
/* 260 */         invGain_Q30 = tmp1;
/*     */       } 
/*     */ 
/*     */       
/* 264 */       for (k = 0; k < n + 1 >> 1; k++) {
/* 265 */         tmp1 = Af_QA[k];
/*     */         
/* 267 */         tmp2 = Af_QA[n - k - 1];
/*     */         
/* 269 */         Af_QA[k] = Inlines.silk_ADD_LSHIFT32(tmp1, Inlines.silk_SMMUL(tmp2, rc_Q31), 1);
/*     */         
/* 271 */         Af_QA[n - k - 1] = Inlines.silk_ADD_LSHIFT32(tmp2, Inlines.silk_SMMUL(tmp1, rc_Q31), 1);
/*     */       } 
/*     */       
/* 274 */       Af_QA[n] = Inlines.silk_RSHIFT32(rc_Q31, 6);
/*     */ 
/*     */       
/* 277 */       if (reached_max_gain != 0) {
/*     */         
/* 279 */         for (k = n + 1; k < D; k++) {
/* 280 */           Af_QA[k] = 0;
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 286 */       for (k = 0; k <= n + 1; k++) {
/* 287 */         tmp1 = CAf[k];
/*     */         
/* 289 */         tmp2 = CAb[n - k + 1];
/*     */         
/* 291 */         CAf[k] = Inlines.silk_ADD_LSHIFT32(tmp1, Inlines.silk_SMMUL(tmp2, rc_Q31), 1);
/*     */         
/* 293 */         CAb[n - k + 1] = Inlines.silk_ADD_LSHIFT32(tmp2, Inlines.silk_SMMUL(tmp1, rc_Q31), 1);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 298 */     if (reached_max_gain != 0) {
/* 299 */       for (int k = 0; k < D; k++)
/*     */       {
/* 301 */         A_Q16[k] = -Inlines.silk_RSHIFT_ROUND(Af_QA[k], 9);
/*     */       }
/*     */       
/* 304 */       if (rshifts > 0) {
/* 305 */         for (byte b = 0; b < nb_subfr; b++) {
/* 306 */           int x_offset = x_ptr + b * subfr_length;
/* 307 */           C0 -= (int)Inlines.silk_RSHIFT64(Inlines.silk_inner_prod16_aligned_64(x, x_offset, x, x_offset, D), rshifts);
/*     */         } 
/*     */       } else {
/* 310 */         for (byte b = 0; b < nb_subfr; b++) {
/* 311 */           int x_offset = x_ptr + b * subfr_length;
/* 312 */           C0 -= Inlines.silk_LSHIFT32(Inlines.silk_inner_prod_self(x, x_offset, D), -rshifts);
/*     */         } 
/*     */       } 
/*     */       
/* 316 */       res_nrg.Val = Inlines.silk_LSHIFT(Inlines.silk_SMMUL(invGain_Q30, C0), 2);
/* 317 */       res_nrg_Q.Val = 0 - rshifts;
/*     */     } else {
/*     */       
/* 320 */       int nrg = CAf[0];
/*     */       
/* 322 */       int tmp1 = 65536;
/*     */       
/* 324 */       for (int k = 0; k < D; k++) {
/* 325 */         int Atmp1 = Inlines.silk_RSHIFT_ROUND(Af_QA[k], 9);
/*     */         
/* 327 */         nrg = Inlines.silk_SMLAWW(nrg, CAf[k + 1], Atmp1);
/*     */         
/* 329 */         tmp1 = Inlines.silk_SMLAWW(tmp1, Atmp1, Atmp1);
/*     */         
/* 331 */         A_Q16[k] = -Atmp1;
/*     */       } 
/* 333 */       res_nrg.Val = Inlines.silk_SMLAWW(nrg, Inlines.silk_SMMUL(42950, C0), -tmp1);
/* 334 */       res_nrg_Q.Val = -rshifts;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\BurgModified.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */