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
/*    */ class PulseCache
/*    */ {
/* 39 */   int size = 0;
/* 40 */   short[] index = null;
/* 41 */   short[] bits = null;
/* 42 */   short[] caps = null;
/*    */   
/*    */   void Reset() {
/* 45 */     this.size = 0;
/* 46 */     this.index = null;
/* 47 */     this.bits = null;
/* 48 */     this.caps = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\PulseCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */