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
/*    */ class SilkDecoder
/*    */ {
/* 39 */   final SilkChannelDecoder[] channel_state = new SilkChannelDecoder[2];
/* 40 */   final StereoDecodeState sStereo = new StereoDecodeState();
/* 41 */   int nChannelsAPI = 0;
/* 42 */   int nChannelsInternal = 0;
/* 43 */   int prev_decode_only_middle = 0;
/*    */   
/*    */   SilkDecoder() {
/* 46 */     for (int c = 0; c < 2; c++) {
/* 47 */       this.channel_state[c] = new SilkChannelDecoder();
/*    */     }
/*    */   }
/*    */   
/*    */   void Reset() {
/* 52 */     for (int c = 0; c < 2; c++) {
/* 53 */       this.channel_state[c].Reset();
/*    */     }
/* 55 */     this.sStereo.Reset();
/* 56 */     this.nChannelsAPI = 0;
/* 57 */     this.nChannelsInternal = 0;
/* 58 */     this.prev_decode_only_middle = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SilkDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */