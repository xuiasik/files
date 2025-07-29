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
/*    */ class StereoWidthState
/*    */ {
/*    */   int XX;
/*    */   int XY;
/*    */   int YY;
/*    */   int smoothed_width;
/*    */   int max_follower;
/*    */   
/*    */   void Reset() {
/* 46 */     this.XX = 0;
/* 47 */     this.XY = 0;
/* 48 */     this.YY = 0;
/* 49 */     this.smoothed_width = 0;
/* 50 */     this.max_follower = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\StereoWidthState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */