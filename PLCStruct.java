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
/*    */ class PLCStruct
/*    */ {
/* 39 */   int pitchL_Q8 = 0;
/*    */   
/* 41 */   final short[] LTPCoef_Q14 = new short[5];
/*    */   
/* 43 */   final short[] prevLPC_Q12 = new short[16];
/* 44 */   int last_frame_lost = 0;
/*    */   
/* 46 */   int rand_seed = 0;
/*    */   
/* 48 */   short randScale_Q14 = 0;
/*    */   
/* 50 */   int conc_energy = 0;
/* 51 */   int conc_energy_shift = 0;
/* 52 */   short prevLTP_scale_Q14 = 0;
/* 53 */   final int[] prevGain_Q16 = new int[2];
/* 54 */   int fs_kHz = 0;
/* 55 */   int nb_subfr = 0;
/* 56 */   int subfr_length = 0;
/*    */   
/*    */   void Reset() {
/* 59 */     this.pitchL_Q8 = 0;
/* 60 */     Arrays.MemSet(this.LTPCoef_Q14, (short)0, 5);
/* 61 */     Arrays.MemSet(this.prevLPC_Q12, (short)0, 16);
/* 62 */     this.last_frame_lost = 0;
/* 63 */     this.rand_seed = 0;
/* 64 */     this.randScale_Q14 = 0;
/* 65 */     this.conc_energy = 0;
/* 66 */     this.conc_energy_shift = 0;
/* 67 */     this.prevLTP_scale_Q14 = 0;
/* 68 */     Arrays.MemSet(this.prevGain_Q16, 0, 2);
/* 69 */     this.fs_kHz = 0;
/* 70 */     this.nb_subfr = 0;
/* 71 */     this.subfr_length = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\PLCStruct.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */