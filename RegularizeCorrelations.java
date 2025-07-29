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
/*    */ class RegularizeCorrelations
/*    */ {
/*    */   static void silk_regularize_correlations(int[] XX, int XX_ptr, int[] xx, int xx_ptr, int noise, int D) {
/* 46 */     for (int i = 0; i < D; i++) {
/* 47 */       Inlines.MatrixSet(XX, XX_ptr, i, i, D, Inlines.silk_ADD32(Inlines.MatrixGet(XX, XX_ptr, i, i, D), noise));
/*    */     }
/* 49 */     xx[xx_ptr] = xx[xx_ptr] + noise;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\RegularizeCorrelations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */