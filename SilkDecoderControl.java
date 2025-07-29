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
/*    */ class SilkDecoderControl
/*    */ {
/* 40 */   final int[] pitchL = new int[4];
/* 41 */   final int[] Gains_Q16 = new int[4];
/*    */ 
/*    */   
/* 44 */   final short[][] PredCoef_Q12 = Arrays.InitTwoDimensionalArrayShort(2, 16);
/* 45 */   final short[] LTPCoef_Q14 = new short[20];
/* 46 */   int LTP_scale_Q14 = 0;
/*    */   
/*    */   void Reset() {
/* 49 */     Arrays.MemSet(this.pitchL, 0, 4);
/* 50 */     Arrays.MemSet(this.Gains_Q16, 0, 4);
/* 51 */     Arrays.MemSet(this.PredCoef_Q12[0], (short)0, 16);
/* 52 */     Arrays.MemSet(this.PredCoef_Q12[1], (short)0, 16);
/* 53 */     Arrays.MemSet(this.LTPCoef_Q14, (short)0, 20);
/* 54 */     this.LTP_scale_Q14 = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SilkDecoderControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */