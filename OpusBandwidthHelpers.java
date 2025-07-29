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
/*    */ class OpusBandwidthHelpers
/*    */ {
/*    */   static int GetOrdinal(OpusBandwidth bw) {
/* 51 */     switch (bw) {
/*    */       case OPUS_BANDWIDTH_NARROWBAND:
/* 53 */         return 1;
/*    */       case OPUS_BANDWIDTH_MEDIUMBAND:
/* 55 */         return 2;
/*    */       case OPUS_BANDWIDTH_WIDEBAND:
/* 57 */         return 3;
/*    */       case OPUS_BANDWIDTH_SUPERWIDEBAND:
/* 59 */         return 4;
/*    */       case OPUS_BANDWIDTH_FULLBAND:
/* 61 */         return 5;
/*    */     } 
/*    */     
/* 64 */     return -1;
/*    */   }
/*    */   
/*    */   static OpusBandwidth GetBandwidth(int ordinal) {
/* 68 */     switch (ordinal) {
/*    */       case 1:
/* 70 */         return OpusBandwidth.OPUS_BANDWIDTH_NARROWBAND;
/*    */       case 2:
/* 72 */         return OpusBandwidth.OPUS_BANDWIDTH_MEDIUMBAND;
/*    */       case 3:
/* 74 */         return OpusBandwidth.OPUS_BANDWIDTH_WIDEBAND;
/*    */       case 4:
/* 76 */         return OpusBandwidth.OPUS_BANDWIDTH_SUPERWIDEBAND;
/*    */       case 5:
/* 78 */         return OpusBandwidth.OPUS_BANDWIDTH_FULLBAND;
/*    */     } 
/*    */     
/* 81 */     return OpusBandwidth.OPUS_BANDWIDTH_AUTO;
/*    */   }
/*    */   
/*    */   static OpusBandwidth MIN(OpusBandwidth a, OpusBandwidth b) {
/* 85 */     if (GetOrdinal(a) < GetOrdinal(b)) {
/* 86 */       return a;
/*    */     }
/* 88 */     return b;
/*    */   }
/*    */   
/*    */   static OpusBandwidth MAX(OpusBandwidth a, OpusBandwidth b) {
/* 92 */     if (GetOrdinal(a) > GetOrdinal(b)) {
/* 93 */       return a;
/*    */     }
/* 95 */     return b;
/*    */   }
/*    */   
/*    */   static OpusBandwidth SUBTRACT(OpusBandwidth a, int b) {
/* 99 */     return GetBandwidth(GetOrdinal(a) - b);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\OpusBandwidthHelpers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */