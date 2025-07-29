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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class OpusFramesizeHelpers
/*    */ {
/*    */   static int GetOrdinal(OpusFramesize size) {
/* 79 */     switch (size) {
/*    */       case OPUS_FRAMESIZE_ARG:
/* 81 */         return 1;
/*    */       case OPUS_FRAMESIZE_2_5_MS:
/* 83 */         return 2;
/*    */       case OPUS_FRAMESIZE_5_MS:
/* 85 */         return 3;
/*    */       case OPUS_FRAMESIZE_10_MS:
/* 87 */         return 4;
/*    */       case OPUS_FRAMESIZE_20_MS:
/* 89 */         return 5;
/*    */       case OPUS_FRAMESIZE_40_MS:
/* 91 */         return 6;
/*    */       case OPUS_FRAMESIZE_60_MS:
/* 93 */         return 7;
/*    */       case OPUS_FRAMESIZE_VARIABLE:
/* 95 */         return 8;
/*    */     } 
/*    */     
/* 98 */     return -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\OpusFramesizeHelpers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */