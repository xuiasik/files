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
/*    */ class MDCTLookup
/*    */ {
/* 39 */   int n = 0;
/*    */   
/* 41 */   int maxshift = 0;
/*    */ 
/*    */   
/* 44 */   FFTState[] kfft = new FFTState[4];
/*    */   
/* 46 */   short[] trig = null;
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\MDCTLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */