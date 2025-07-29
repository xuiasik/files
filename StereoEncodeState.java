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
/*    */ class StereoEncodeState
/*    */ {
/* 36 */   final short[] pred_prev_Q13 = new short[2];
/* 37 */   final short[] sMid = new short[2];
/* 38 */   final short[] sSide = new short[2];
/* 39 */   final int[] mid_side_amp_Q0 = new int[4];
/* 40 */   short smth_width_Q14 = 0;
/* 41 */   short width_prev_Q14 = 0;
/* 42 */   short silent_side_len = 0;
/* 43 */   final byte[][][] predIx = Arrays.InitThreeDimensionalArrayByte(3, 2, 3);
/* 44 */   final byte[] mid_only_flags = new byte[3];
/*    */   
/*    */   void Reset() {
/* 47 */     Arrays.MemSet(this.pred_prev_Q13, (short)0, 2);
/* 48 */     Arrays.MemSet(this.sMid, (short)0, 2);
/* 49 */     Arrays.MemSet(this.sSide, (short)0, 2);
/* 50 */     Arrays.MemSet(this.mid_side_amp_Q0, 0, 4);
/* 51 */     this.smth_width_Q14 = 0;
/* 52 */     this.width_prev_Q14 = 0;
/* 53 */     this.silent_side_len = 0;
/* 54 */     for (int x = 0; x < 3; x++) {
/* 55 */       for (int y = 0; y < 2; y++) {
/* 56 */         Arrays.MemSet(this.predIx[x][y], (byte)0, 3);
/*    */       }
/*    */     } 
/*    */     
/* 60 */     Arrays.MemSet(this.mid_only_flags, (byte)0, 3);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\StereoEncodeState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */