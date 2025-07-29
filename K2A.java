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
/*    */ class K2A
/*    */ {
/*    */   static void silk_k2a(int[] A_Q24, short[] rc_Q15, int order) {
/* 43 */     int[] Atmp = new int[16];
/*    */     
/* 45 */     for (int k = 0; k < order; k++) {
/* 46 */       int n; for (n = 0; n < k; n++) {
/* 47 */         Atmp[n] = A_Q24[n];
/*    */       }
/* 49 */       for (n = 0; n < k; n++) {
/* 50 */         A_Q24[n] = Inlines.silk_SMLAWB(A_Q24[n], Inlines.silk_LSHIFT(Atmp[k - n - 1], 1), rc_Q15[k]);
/*    */       }
/* 52 */       A_Q24[k] = 0 - Inlines.silk_LSHIFT(rc_Q15[k], 9);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void silk_k2a_Q16(int[] A_Q24, int[] rc_Q16, int order) {
/* 63 */     int[] Atmp = new int[16];
/*    */     
/* 65 */     for (int k = 0; k < order; k++) {
/* 66 */       int n; for (n = 0; n < k; n++) {
/* 67 */         Atmp[n] = A_Q24[n];
/*    */       }
/* 69 */       for (n = 0; n < k; n++) {
/* 70 */         A_Q24[n] = Inlines.silk_SMLAWW(A_Q24[n], Atmp[k - n - 1], rc_Q16[k]);
/*    */       }
/* 72 */       A_Q24[k] = 0 - Inlines.silk_LSHIFT(rc_Q16[k], 8);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\K2A.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */