/*     */ package de.maxhenkel.voicechat.concentus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ApplySineWindow
/*     */ {
/*  46 */   private static final short[] freq_table_Q16 = new short[] { 12111, 9804, 8235, 7100, 6239, 5565, 5022, 4575, 4202, 3885, 3612, 3375, 3167, 2984, 2820, 2674, 2542, 2422, 2313, 2214, 2123, 2038, 1961, 1889, 1822, 1760, 1702 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_apply_sine_window(short[] px_win, int px_win_ptr, short[] px, int px_ptr, int win_type, int length) {
/*     */     int S0_Q16, S1_Q16;
/*  62 */     Inlines.OpusAssert((win_type == 1 || win_type == 2));
/*     */ 
/*     */     
/*  65 */     Inlines.OpusAssert((length >= 16 && length <= 120));
/*  66 */     Inlines.OpusAssert(((length & 0x3) == 0));
/*     */ 
/*     */     
/*  69 */     int k = (length >> 2) - 4;
/*  70 */     Inlines.OpusAssert((k >= 0 && k <= 26));
/*  71 */     int f_Q16 = freq_table_Q16[k];
/*     */ 
/*     */     
/*  74 */     int c_Q16 = Inlines.silk_SMULWB(f_Q16, -f_Q16);
/*  75 */     Inlines.OpusAssert((c_Q16 >= -32768));
/*     */ 
/*     */     
/*  78 */     if (win_type == 1) {
/*     */       
/*  80 */       S0_Q16 = 0;
/*     */       
/*  82 */       S1_Q16 = f_Q16 + Inlines.silk_RSHIFT(length, 3);
/*     */     } else {
/*     */       
/*  85 */       S0_Q16 = 65536;
/*     */       
/*  87 */       S1_Q16 = 65536 + Inlines.silk_RSHIFT(c_Q16, 1) + Inlines.silk_RSHIFT(length, 4);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  92 */     for (k = 0; k < length; k += 4) {
/*  93 */       int pxwk = px_win_ptr + k;
/*  94 */       int pxk = px_ptr + k;
/*  95 */       px_win[pxwk] = (short)Inlines.silk_SMULWB(Inlines.silk_RSHIFT(S0_Q16 + S1_Q16, 1), px[pxk]);
/*  96 */       px_win[pxwk + 1] = (short)Inlines.silk_SMULWB(S1_Q16, px[pxk + 1]);
/*  97 */       S0_Q16 = Inlines.silk_SMULWB(S1_Q16, c_Q16) + Inlines.silk_LSHIFT(S1_Q16, 1) - S0_Q16 + 1;
/*  98 */       S0_Q16 = Inlines.silk_min(S0_Q16, 65536);
/*     */       
/* 100 */       px_win[pxwk + 2] = (short)Inlines.silk_SMULWB(Inlines.silk_RSHIFT(S0_Q16 + S1_Q16, 1), px[pxk + 2]);
/* 101 */       px_win[pxwk + 3] = (short)Inlines.silk_SMULWB(S0_Q16, px[pxk + 3]);
/* 102 */       S1_Q16 = Inlines.silk_SMULWB(S0_Q16, c_Q16) + Inlines.silk_LSHIFT(S0_Q16, 1) - S1_Q16;
/* 103 */       S1_Q16 = Inlines.silk_min(S1_Q16, 65536);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\ApplySineWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */