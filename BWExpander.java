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
/*    */ class BWExpander
/*    */ {
/*    */   static void silk_bwexpander_32(int[] ar, int d, int chirp_Q16) {
/* 48 */     int chirp_minus_one_Q16 = chirp_Q16 - 65536;
/*    */     
/* 50 */     for (int i = 0; i < d - 1; i++) {
/* 51 */       ar[i] = Inlines.silk_SMULWW(chirp_Q16, ar[i]);
/* 52 */       chirp_Q16 += Inlines.silk_RSHIFT_ROUND(Inlines.silk_MUL(chirp_Q16, chirp_minus_one_Q16), 16);
/*    */     } 
/* 54 */     ar[d - 1] = Inlines.silk_SMULWW(chirp_Q16, ar[d - 1]);
/*    */   }
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
/*    */   static void silk_bwexpander(short[] ar, int d, int chirp_Q16) {
/* 68 */     int chirp_minus_one_Q16 = chirp_Q16 - 65536;
/*    */ 
/*    */ 
/*    */     
/* 72 */     for (int i = 0; i < d - 1; i++) {
/* 73 */       ar[i] = (short)Inlines.silk_RSHIFT_ROUND(Inlines.silk_MUL(chirp_Q16, ar[i]), 16);
/* 74 */       chirp_Q16 += Inlines.silk_RSHIFT_ROUND(Inlines.silk_MUL(chirp_Q16, chirp_minus_one_Q16), 16);
/*    */     } 
/* 76 */     ar[d - 1] = (short)Inlines.silk_RSHIFT_ROUND(Inlines.silk_MUL(chirp_Q16, ar[d - 1]), 16);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\BWExpander.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */