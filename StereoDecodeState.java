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
/*    */ class StereoDecodeState
/*    */ {
/* 36 */   final short[] pred_prev_Q13 = new short[2];
/* 37 */   final short[] sMid = new short[2];
/* 38 */   final short[] sSide = new short[2];
/*    */   
/*    */   void Reset() {
/* 41 */     Arrays.MemSet(this.pred_prev_Q13, (short)0, 2);
/* 42 */     Arrays.MemSet(this.sMid, (short)0, 2);
/* 43 */     Arrays.MemSet(this.sSide, (short)0, 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\StereoDecodeState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */