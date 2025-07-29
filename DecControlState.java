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
/*    */ class DecControlState
/*    */ {
/* 40 */   int nChannelsAPI = 0;
/*    */ 
/*    */   
/* 43 */   int nChannelsInternal = 0;
/*    */ 
/*    */   
/* 46 */   int API_sampleRate = 0;
/*    */ 
/*    */   
/* 49 */   int internalSampleRate = 0;
/*    */ 
/*    */   
/* 52 */   int payloadSize_ms = 0;
/*    */ 
/*    */   
/* 55 */   int prevPitchLag = 0;
/*    */   
/*    */   void Reset() {
/* 58 */     this.nChannelsAPI = 0;
/* 59 */     this.nChannelsInternal = 0;
/* 60 */     this.API_sampleRate = 0;
/* 61 */     this.internalSampleRate = 0;
/* 62 */     this.payloadSize_ms = 0;
/* 63 */     this.prevPitchLag = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\DecControlState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */