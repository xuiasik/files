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
/*    */ class FFTState
/*    */ {
/* 39 */   int nfft = 0;
/* 40 */   short scale = 0;
/* 41 */   int scale_shift = 0;
/* 42 */   int shift = 0;
/* 43 */   short[] factors = new short[16];
/* 44 */   short[] bitrev = null;
/* 45 */   short[] twiddles = null;
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\FFTState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */