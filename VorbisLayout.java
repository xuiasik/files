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
/*    */ class VorbisLayout
/*    */ {
/*    */   int nb_streams;
/*    */   int nb_coupled_streams;
/*    */   short[] mapping;
/*    */   
/*    */   VorbisLayout(int streams, int coupled_streams, short[] map) {
/* 40 */     this.nb_streams = streams;
/* 41 */     this.nb_coupled_streams = coupled_streams;
/* 42 */     this.mapping = map;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   static final VorbisLayout[] vorbis_mappings = new VorbisLayout[] { new VorbisLayout(1, 0, new short[] { 0 }), new VorbisLayout(1, 1, new short[] { 0, 1 }), new VorbisLayout(2, 1, new short[] { 0, 2, 1 }), new VorbisLayout(2, 2, new short[] { 0, 1, 2, 3 }), new VorbisLayout(3, 2, new short[] { 0, 4, 1, 2, 3 }), new VorbisLayout(4, 2, new short[] { 0, 4, 1, 2, 3, 5 }), new VorbisLayout(4, 3, new short[] { 0, 4, 1, 2, 3, 5, 6 }), new VorbisLayout(5, 3, new short[] { 0, 6, 1, 2, 3, 4, 5, 7 }) };
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\VorbisLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */