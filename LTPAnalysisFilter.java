/*    */ package de.maxhenkel.voicechat.concentus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class LTPAnalysisFilter
/*    */ {
/*    */   static void silk_LTP_analysis_filter(short[] LTP_res, short[] x, int x_ptr, short[] LTPCoef_Q14, int[] pitchL, int[] invGains_Q16, int subfr_length, int nb_subfr, int pre_length) {
/* 48 */     short[] Btmp_Q14 = new short[5];
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 53 */     int x_ptr2 = x_ptr;
/* 54 */     int LTP_res_ptr = 0;
/* 55 */     for (int k = 0; k < nb_subfr; k++) {
/* 56 */       int x_lag_ptr = x_ptr2 - pitchL[k];
/*    */       
/* 58 */       Btmp_Q14[0] = LTPCoef_Q14[k * 5];
/* 59 */       Btmp_Q14[1] = LTPCoef_Q14[k * 5 + 1];
/* 60 */       Btmp_Q14[2] = LTPCoef_Q14[k * 5 + 2];
/* 61 */       Btmp_Q14[3] = LTPCoef_Q14[k * 5 + 3];
/* 62 */       Btmp_Q14[4] = LTPCoef_Q14[k * 5 + 4];
/*    */ 
/*    */       
/* 65 */       for (int i = 0; i < subfr_length + pre_length; i++) {
/* 66 */         int LTP_res_ptri = LTP_res_ptr + i;
/* 67 */         LTP_res[LTP_res_ptri] = x[x_ptr2 + i];
/*    */ 
/*    */         
/* 70 */         int LTP_est = Inlines.silk_SMULBB(x[x_lag_ptr + 2], Btmp_Q14[0]);
/* 71 */         LTP_est = Inlines.silk_SMLABB_ovflw(LTP_est, x[x_lag_ptr + 1], Btmp_Q14[1]);
/* 72 */         LTP_est = Inlines.silk_SMLABB_ovflw(LTP_est, x[x_lag_ptr], Btmp_Q14[2]);
/* 73 */         LTP_est = Inlines.silk_SMLABB_ovflw(LTP_est, x[x_lag_ptr - 1], Btmp_Q14[3]);
/* 74 */         LTP_est = Inlines.silk_SMLABB_ovflw(LTP_est, x[x_lag_ptr - 2], Btmp_Q14[4]);
/*    */         
/* 76 */         LTP_est = Inlines.silk_RSHIFT_ROUND(LTP_est, 14);
/*    */ 
/*    */ 
/*    */         
/* 80 */         LTP_res[LTP_res_ptri] = (short)Inlines.silk_SAT16(x[x_ptr2 + i] - LTP_est);
/*    */ 
/*    */         
/* 83 */         LTP_res[LTP_res_ptri] = (short)Inlines.silk_SMULWB(invGains_Q16[k], LTP_res[LTP_res_ptri]);
/*    */         
/* 85 */         x_lag_ptr++;
/*    */       } 
/*    */ 
/*    */       
/* 89 */       LTP_res_ptr += subfr_length + pre_length;
/* 90 */       x_ptr2 += subfr_length;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\LTPAnalysisFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */