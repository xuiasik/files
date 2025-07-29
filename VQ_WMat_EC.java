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
/*     */ class VQ_WMat_EC
/*     */ {
/*     */   static void silk_VQ_WMat_EC(BoxedValueByte ind, BoxedValueInt rate_dist_Q14, BoxedValueInt gain_Q7, short[] in_Q14, int in_Q14_ptr, int[] W_Q18, int W_Q18_ptr, byte[][] cb_Q7, short[] cb_gain_Q7, short[] cl_Q5, int mu_Q9, int max_gain_Q7, int L) {
/*  54 */     int cb_row_Q7_ptr = 0;
/*  55 */     short[] diff_Q14 = new short[5];
/*     */ 
/*     */ 
/*     */     
/*  59 */     rate_dist_Q14.Val = Integer.MAX_VALUE;
/*  60 */     for (int k = 0; k < L; k++) {
/*     */       
/*  62 */       byte[] cb_row_Q7 = cb_Q7[cb_row_Q7_ptr++];
/*  63 */       int gain_tmp_Q7 = cb_gain_Q7[k];
/*     */       
/*  65 */       diff_Q14[0] = (short)(in_Q14[in_Q14_ptr] - Inlines.silk_LSHIFT(cb_row_Q7[0], 7));
/*  66 */       diff_Q14[1] = (short)(in_Q14[in_Q14_ptr + 1] - Inlines.silk_LSHIFT(cb_row_Q7[1], 7));
/*  67 */       diff_Q14[2] = (short)(in_Q14[in_Q14_ptr + 2] - Inlines.silk_LSHIFT(cb_row_Q7[2], 7));
/*  68 */       diff_Q14[3] = (short)(in_Q14[in_Q14_ptr + 3] - Inlines.silk_LSHIFT(cb_row_Q7[3], 7));
/*  69 */       diff_Q14[4] = (short)(in_Q14[in_Q14_ptr + 4] - Inlines.silk_LSHIFT(cb_row_Q7[4], 7));
/*     */ 
/*     */       
/*  72 */       int sum1_Q14 = Inlines.silk_SMULBB(mu_Q9, cl_Q5[k]);
/*     */ 
/*     */       
/*  75 */       sum1_Q14 = Inlines.silk_ADD_LSHIFT32(sum1_Q14, Inlines.silk_max(Inlines.silk_SUB32(gain_tmp_Q7, max_gain_Q7), 0), 10);
/*     */       
/*  77 */       Inlines.OpusAssert((sum1_Q14 >= 0));
/*     */ 
/*     */       
/*  80 */       int sum2_Q16 = Inlines.silk_SMULWB(W_Q18[W_Q18_ptr + 1], diff_Q14[1]);
/*  81 */       sum2_Q16 = Inlines.silk_SMLAWB(sum2_Q16, W_Q18[W_Q18_ptr + 2], diff_Q14[2]);
/*  82 */       sum2_Q16 = Inlines.silk_SMLAWB(sum2_Q16, W_Q18[W_Q18_ptr + 3], diff_Q14[3]);
/*  83 */       sum2_Q16 = Inlines.silk_SMLAWB(sum2_Q16, W_Q18[W_Q18_ptr + 4], diff_Q14[4]);
/*  84 */       sum2_Q16 = Inlines.silk_LSHIFT(sum2_Q16, 1);
/*  85 */       sum2_Q16 = Inlines.silk_SMLAWB(sum2_Q16, W_Q18[W_Q18_ptr], diff_Q14[0]);
/*  86 */       sum1_Q14 = Inlines.silk_SMLAWB(sum1_Q14, sum2_Q16, diff_Q14[0]);
/*     */ 
/*     */       
/*  89 */       sum2_Q16 = Inlines.silk_SMULWB(W_Q18[W_Q18_ptr + 7], diff_Q14[2]);
/*  90 */       sum2_Q16 = Inlines.silk_SMLAWB(sum2_Q16, W_Q18[W_Q18_ptr + 8], diff_Q14[3]);
/*  91 */       sum2_Q16 = Inlines.silk_SMLAWB(sum2_Q16, W_Q18[W_Q18_ptr + 9], diff_Q14[4]);
/*  92 */       sum2_Q16 = Inlines.silk_LSHIFT(sum2_Q16, 1);
/*  93 */       sum2_Q16 = Inlines.silk_SMLAWB(sum2_Q16, W_Q18[W_Q18_ptr + 6], diff_Q14[1]);
/*  94 */       sum1_Q14 = Inlines.silk_SMLAWB(sum1_Q14, sum2_Q16, diff_Q14[1]);
/*     */ 
/*     */       
/*  97 */       sum2_Q16 = Inlines.silk_SMULWB(W_Q18[W_Q18_ptr + 13], diff_Q14[3]);
/*  98 */       sum2_Q16 = Inlines.silk_SMLAWB(sum2_Q16, W_Q18[W_Q18_ptr + 14], diff_Q14[4]);
/*  99 */       sum2_Q16 = Inlines.silk_LSHIFT(sum2_Q16, 1);
/* 100 */       sum2_Q16 = Inlines.silk_SMLAWB(sum2_Q16, W_Q18[W_Q18_ptr + 12], diff_Q14[2]);
/* 101 */       sum1_Q14 = Inlines.silk_SMLAWB(sum1_Q14, sum2_Q16, diff_Q14[2]);
/*     */ 
/*     */       
/* 104 */       sum2_Q16 = Inlines.silk_SMULWB(W_Q18[W_Q18_ptr + 19], diff_Q14[4]);
/* 105 */       sum2_Q16 = Inlines.silk_LSHIFT(sum2_Q16, 1);
/* 106 */       sum2_Q16 = Inlines.silk_SMLAWB(sum2_Q16, W_Q18[W_Q18_ptr + 18], diff_Q14[3]);
/* 107 */       sum1_Q14 = Inlines.silk_SMLAWB(sum1_Q14, sum2_Q16, diff_Q14[3]);
/*     */ 
/*     */       
/* 110 */       sum2_Q16 = Inlines.silk_SMULWB(W_Q18[W_Q18_ptr + 24], diff_Q14[4]);
/* 111 */       sum1_Q14 = Inlines.silk_SMLAWB(sum1_Q14, sum2_Q16, diff_Q14[4]);
/*     */       
/* 113 */       Inlines.OpusAssert((sum1_Q14 >= 0));
/*     */ 
/*     */       
/* 116 */       if (sum1_Q14 < rate_dist_Q14.Val) {
/* 117 */         rate_dist_Q14.Val = sum1_Q14;
/* 118 */         ind.Val = (byte)k;
/* 119 */         gain_Q7.Val = gain_tmp_Q7;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\VQ_WMat_EC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */