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
/*    */ class SilkShapeState
/*    */ {
/* 39 */   byte LastGainIndex = 0;
/* 40 */   int HarmBoost_smth_Q16 = 0;
/* 41 */   int HarmShapeGain_smth_Q16 = 0;
/* 42 */   int Tilt_smth_Q16 = 0;
/*    */   
/*    */   void Reset() {
/* 45 */     this.LastGainIndex = 0;
/* 46 */     this.HarmBoost_smth_Q16 = 0;
/* 47 */     this.HarmShapeGain_smth_Q16 = 0;
/* 48 */     this.Tilt_smth_Q16 = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SilkShapeState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */