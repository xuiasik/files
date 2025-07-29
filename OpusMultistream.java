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
/*    */ class OpusMultistream
/*    */ {
/*    */   static int validate_layout(ChannelLayout layout) {
/* 42 */     int max_channel = layout.nb_streams + layout.nb_coupled_streams;
/* 43 */     if (max_channel > 255) {
/* 44 */       return 0;
/*    */     }
/* 46 */     for (int i = 0; i < layout.nb_channels; i++) {
/* 47 */       if (layout.mapping[i] >= max_channel && layout.mapping[i] != 255) {
/* 48 */         return 0;
/*    */       }
/*    */     } 
/* 51 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   static int get_left_channel(ChannelLayout layout, int stream_id, int prev) {
/* 56 */     int i = (prev < 0) ? 0 : (prev + 1);
/* 57 */     for (; i < layout.nb_channels; i++) {
/* 58 */       if (layout.mapping[i] == stream_id * 2) {
/* 59 */         return i;
/*    */       }
/*    */     } 
/* 62 */     return -1;
/*    */   }
/*    */ 
/*    */   
/*    */   static int get_right_channel(ChannelLayout layout, int stream_id, int prev) {
/* 67 */     int i = (prev < 0) ? 0 : (prev + 1);
/* 68 */     for (; i < layout.nb_channels; i++) {
/* 69 */       if (layout.mapping[i] == stream_id * 2 + 1) {
/* 70 */         return i;
/*    */       }
/*    */     } 
/* 73 */     return -1;
/*    */   }
/*    */ 
/*    */   
/*    */   static int get_mono_channel(ChannelLayout layout, int stream_id, int prev) {
/* 78 */     int i = (prev < 0) ? 0 : (prev + 1);
/* 79 */     for (; i < layout.nb_channels; i++) {
/* 80 */       if (layout.mapping[i] == stream_id + layout.nb_coupled_streams) {
/* 81 */         return i;
/*    */       }
/*    */     } 
/* 84 */     return -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\OpusMultistream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */