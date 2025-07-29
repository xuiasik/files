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
/*    */ class CNGState
/*    */ {
/* 39 */   final int[] CNG_exc_buf_Q14 = new int[320];
/* 40 */   final short[] CNG_smth_NLSF_Q15 = new short[16];
/* 41 */   final int[] CNG_synth_state = new int[16];
/* 42 */   int CNG_smth_Gain_Q16 = 0;
/* 43 */   int rand_seed = 0;
/* 44 */   int fs_kHz = 0;
/*    */   
/*    */   void Reset() {
/* 47 */     Arrays.MemSet(this.CNG_exc_buf_Q14, 0, 320);
/* 48 */     Arrays.MemSet(this.CNG_smth_NLSF_Q15, (short)0, 16);
/* 49 */     Arrays.MemSet(this.CNG_synth_state, 0, 16);
/* 50 */     this.CNG_smth_Gain_Q16 = 0;
/* 51 */     this.rand_seed = 0;
/* 52 */     this.fs_kHz = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\CNGState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */