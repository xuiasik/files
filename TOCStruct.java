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
/*    */ class TOCStruct
/*    */ {
/* 42 */   int VADFlag = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   final int[] VADFlags = new int[3];
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   int inbandFECFlag = 0;
/*    */   
/*    */   void Reset() {
/* 55 */     this.VADFlag = 0;
/* 56 */     Arrays.MemSet(this.VADFlags, 0, 3);
/* 57 */     this.inbandFECFlag = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\TOCStruct.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */