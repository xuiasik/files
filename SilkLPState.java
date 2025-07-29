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
/*    */ class SilkLPState
/*    */ {
/* 42 */   final int[] In_LP_State = new int[2];
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   int transition_frame_no = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   int mode = 0;
/*    */   
/*    */   void Reset() {
/* 55 */     this.In_LP_State[0] = 0;
/* 56 */     this.In_LP_State[1] = 0;
/* 57 */     this.transition_frame_no = 0;
/* 58 */     this.mode = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void silk_LP_variable_cutoff(short[] frame, int frame_ptr, int frame_length) {
/* 70 */     int[] B_Q28 = new int[3];
/* 71 */     int[] A_Q28 = new int[2];
/* 72 */     int fac_Q16 = 0;
/* 73 */     int ind = 0;
/*    */     
/* 75 */     Inlines.OpusAssert((this.transition_frame_no >= 0 && this.transition_frame_no <= 256));
/*    */ 
/*    */     
/* 78 */     if (this.mode != 0) {
/*    */       
/* 80 */       fac_Q16 = Inlines.silk_LSHIFT(256 - this.transition_frame_no, 10);
/*    */       
/* 82 */       ind = Inlines.silk_RSHIFT(fac_Q16, 16);
/* 83 */       fac_Q16 -= Inlines.silk_LSHIFT(ind, 16);
/*    */       
/* 85 */       Inlines.OpusAssert((ind >= 0));
/* 86 */       Inlines.OpusAssert((ind < 5));
/*    */ 
/*    */       
/* 89 */       Filters.silk_LP_interpolate_filter_taps(B_Q28, A_Q28, ind, fac_Q16);
/*    */ 
/*    */       
/* 92 */       this.transition_frame_no = Inlines.silk_LIMIT(this.transition_frame_no + this.mode, 0, 256);
/*    */ 
/*    */       
/* 95 */       Inlines.OpusAssert(true);
/* 96 */       Filters.silk_biquad_alt(frame, frame_ptr, B_Q28, A_Q28, this.In_LP_State, frame, frame_ptr, frame_length, 1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SilkLPState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */