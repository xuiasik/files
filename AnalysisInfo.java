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
/*    */ class AnalysisInfo
/*    */ {
/*    */   boolean enabled = false;
/* 40 */   int valid = 0;
/* 41 */   float tonality = 0.0F;
/* 42 */   float tonality_slope = 0.0F;
/* 43 */   float noisiness = 0.0F;
/* 44 */   float activity = 0.0F;
/* 45 */   float music_prob = 0.0F;
/* 46 */   int bandwidth = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void Assign(AnalysisInfo other) {
/* 52 */     this.valid = other.valid;
/* 53 */     this.tonality = other.tonality;
/* 54 */     this.tonality_slope = other.tonality_slope;
/* 55 */     this.noisiness = other.noisiness;
/* 56 */     this.activity = other.activity;
/* 57 */     this.music_prob = other.music_prob;
/* 58 */     this.bandwidth = other.bandwidth;
/*    */   }
/*    */   
/*    */   void Reset() {
/* 62 */     this.valid = 0;
/* 63 */     this.tonality = 0.0F;
/* 64 */     this.tonality_slope = 0.0F;
/* 65 */     this.noisiness = 0.0F;
/* 66 */     this.activity = 0.0F;
/* 67 */     this.music_prob = 0.0F;
/* 68 */     this.bandwidth = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\AnalysisInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */