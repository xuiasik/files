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
/*    */ class ChannelLayout
/*    */ {
/*    */   int nb_channels;
/*    */   int nb_streams;
/*    */   int nb_coupled_streams;
/* 42 */   final short[] mapping = new short[256];
/*    */   
/*    */   void Reset() {
/* 45 */     this.nb_channels = 0;
/* 46 */     this.nb_streams = 0;
/* 47 */     this.nb_coupled_streams = 0;
/* 48 */     Arrays.MemSet(this.mapping, (short)0);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\ChannelLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */