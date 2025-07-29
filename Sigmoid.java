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
/*    */ class Sigmoid
/*    */ {
/* 39 */   private static final int[] sigm_LUT_slope_Q10 = new int[] { 237, 153, 73, 30, 12, 7 };
/*    */ 
/*    */ 
/*    */   
/* 43 */   private static final int[] sigm_LUT_pos_Q15 = new int[] { 16384, 23955, 28861, 31213, 32178, 32548 };
/*    */ 
/*    */ 
/*    */   
/* 47 */   private static final int[] sigm_LUT_neg_Q15 = new int[] { 16384, 8812, 3906, 1554, 589, 219 };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static int silk_sigm_Q15(int in_Q5) {
/* 54 */     if (in_Q5 < 0) {
/*    */       
/* 56 */       in_Q5 = -in_Q5;
/* 57 */       if (in_Q5 >= 192) {
/* 58 */         return 0;
/*    */       }
/*    */ 
/*    */       
/* 62 */       int i = Inlines.silk_RSHIFT(in_Q5, 5);
/* 63 */       return sigm_LUT_neg_Q15[i] - Inlines.silk_SMULBB(sigm_LUT_slope_Q10[i], in_Q5 & 0x1F);
/*    */     } 
/* 65 */     if (in_Q5 >= 192) {
/* 66 */       return 32767;
/*    */     }
/*    */ 
/*    */     
/* 70 */     int ind = Inlines.silk_RSHIFT(in_Q5, 5);
/* 71 */     return sigm_LUT_pos_Q15[ind] + Inlines.silk_SMULBB(sigm_LUT_slope_Q10[ind], in_Q5 & 0x1F);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Sigmoid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */