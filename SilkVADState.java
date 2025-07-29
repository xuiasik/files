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
/*    */ class SilkVADState
/*    */ {
/* 42 */   final int[] AnaState = new int[2];
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   final int[] AnaState1 = new int[2];
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   final int[] AnaState2 = new int[2];
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   final int[] XnrgSubfr = new int[4];
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   final int[] NrgRatioSmth_Q8 = new int[4];
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   short HPstate = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 72 */   final int[] NL = new int[4];
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   final int[] inv_NL = new int[4];
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   final int[] NoiseLevelBias = new int[4];
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 87 */   int counter = 0;
/*    */   
/*    */   void Reset() {
/* 90 */     Arrays.MemSet(this.AnaState, 0, 2);
/* 91 */     Arrays.MemSet(this.AnaState1, 0, 2);
/* 92 */     Arrays.MemSet(this.AnaState2, 0, 2);
/* 93 */     Arrays.MemSet(this.XnrgSubfr, 0, 4);
/* 94 */     Arrays.MemSet(this.NrgRatioSmth_Q8, 0, 4);
/* 95 */     this.HPstate = 0;
/* 96 */     Arrays.MemSet(this.NL, 0, 4);
/* 97 */     Arrays.MemSet(this.inv_NL, 0, 4);
/* 98 */     Arrays.MemSet(this.NoiseLevelBias, 0, 4);
/* 99 */     this.counter = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SilkVADState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */