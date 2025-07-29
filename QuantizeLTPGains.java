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
/*     */ class QuantizeLTPGains
/*     */ {
/*     */   static void silk_quant_LTP_gains(short[] B_Q14, byte[] cbk_index, BoxedValueByte periodicity_index, BoxedValueInt sum_log_gain_Q7, int[] W_Q18, int mu_Q9, int lowComplexity, int nb_subfr) {
/*  47 */     byte[] temp_idx = new byte[4];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     int min_rate_dist_Q14 = Integer.MAX_VALUE;
/*  65 */     int best_sum_log_gain_Q7 = 0; int k;
/*  66 */     for (k = 0; k < 3; k++) {
/*     */ 
/*     */       
/*  69 */       int gain_safety = 51;
/*     */       
/*  71 */       short[] cl_ptr_Q5 = SilkTables.silk_LTP_gain_BITS_Q5_ptrs[k];
/*  72 */       byte[][] arrayOfByte = SilkTables.silk_LTP_vq_ptrs_Q7[k];
/*  73 */       short[] cbk_gain_ptr_Q7 = SilkTables.silk_LTP_vq_gain_ptrs_Q7[k];
/*  74 */       int cbk_size = SilkTables.silk_LTP_vq_sizes[k];
/*     */ 
/*     */       
/*  77 */       int W_Q18_ptr = 0;
/*  78 */       int b_Q14_ptr = 0;
/*     */       
/*  80 */       int rate_dist_Q14 = 0;
/*  81 */       int sum_log_gain_tmp_Q7 = sum_log_gain_Q7.Val;
/*  82 */       for (int i = 0; i < nb_subfr; i++) {
/*  83 */         int max_gain_Q7 = Inlines.silk_log2lin(5333 - sum_log_gain_tmp_Q7 + 896) - gain_safety;
/*     */ 
/*     */         
/*  86 */         BoxedValueByte temp_idx_box = new BoxedValueByte(temp_idx[i]);
/*  87 */         BoxedValueInt rate_dist_Q14_subfr_box = new BoxedValueInt(0);
/*  88 */         BoxedValueInt gain_Q7_box = new BoxedValueInt(0);
/*  89 */         VQ_WMat_EC.silk_VQ_WMat_EC(temp_idx_box, rate_dist_Q14_subfr_box, gain_Q7_box, B_Q14, b_Q14_ptr, W_Q18, W_Q18_ptr, arrayOfByte, cbk_gain_ptr_Q7, cl_ptr_Q5, mu_Q9, max_gain_Q7, cbk_size);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 104 */         int rate_dist_Q14_subfr = rate_dist_Q14_subfr_box.Val;
/* 105 */         int gain_Q7 = gain_Q7_box.Val;
/* 106 */         temp_idx[i] = temp_idx_box.Val;
/*     */         
/* 108 */         rate_dist_Q14 = Inlines.silk_ADD_POS_SAT32(rate_dist_Q14, rate_dist_Q14_subfr);
/* 109 */         sum_log_gain_tmp_Q7 = Inlines.silk_max(0, sum_log_gain_tmp_Q7 + 
/* 110 */             Inlines.silk_lin2log(gain_safety + gain_Q7) - 896);
/*     */         
/* 112 */         b_Q14_ptr += 5;
/* 113 */         W_Q18_ptr += 25;
/*     */       } 
/*     */ 
/*     */       
/* 117 */       rate_dist_Q14 = Inlines.silk_min(2147483646, rate_dist_Q14);
/*     */       
/* 119 */       if (rate_dist_Q14 < min_rate_dist_Q14) {
/* 120 */         min_rate_dist_Q14 = rate_dist_Q14;
/* 121 */         periodicity_index.Val = (byte)k;
/* 122 */         System.arraycopy(temp_idx, 0, cbk_index, 0, nb_subfr);
/* 123 */         best_sum_log_gain_Q7 = sum_log_gain_tmp_Q7;
/*     */       } 
/*     */ 
/*     */       
/* 127 */       if (lowComplexity != 0 && rate_dist_Q14 < 12304) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 132 */     byte[][] cbk_ptr_Q7 = SilkTables.silk_LTP_vq_ptrs_Q7[periodicity_index.Val];
/* 133 */     for (int j = 0; j < nb_subfr; j++) {
/* 134 */       for (k = 0; k < 5; k++) {
/* 135 */         B_Q14[j * 5 + k] = (short)Inlines.silk_LSHIFT(cbk_ptr_Q7[cbk_index[j]][k], 7);
/*     */       }
/*     */     } 
/*     */     
/* 139 */     sum_log_gain_Q7.Val = best_sum_log_gain_Q7;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\QuantizeLTPGains.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */