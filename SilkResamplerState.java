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
/*    */ class SilkResamplerState
/*    */ {
/* 36 */   final int[] sIIR = new int[6];
/*    */   
/* 38 */   final int[] sFIR_i32 = new int[36];
/* 39 */   final short[] sFIR_i16 = new short[36];
/*    */   
/* 41 */   final short[] delayBuf = new short[48];
/* 42 */   int resampler_function = 0;
/* 43 */   int batchSize = 0;
/* 44 */   int invRatio_Q16 = 0;
/* 45 */   int FIR_Order = 0;
/* 46 */   int FIR_Fracs = 0;
/* 47 */   int Fs_in_kHz = 0;
/* 48 */   int Fs_out_kHz = 0;
/* 49 */   int inputDelay = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   short[] Coefs = null;
/*    */   
/*    */   void Reset() {
/* 57 */     Arrays.MemSet(this.sIIR, 0, 6);
/* 58 */     Arrays.MemSet(this.sFIR_i32, 0, 36);
/* 59 */     Arrays.MemSet(this.sFIR_i16, (short)0, 36);
/* 60 */     Arrays.MemSet(this.delayBuf, (short)0, 48);
/* 61 */     this.resampler_function = 0;
/* 62 */     this.batchSize = 0;
/* 63 */     this.invRatio_Q16 = 0;
/* 64 */     this.FIR_Order = 0;
/* 65 */     this.FIR_Fracs = 0;
/* 66 */     this.Fs_in_kHz = 0;
/* 67 */     this.Fs_out_kHz = 0;
/* 68 */     this.inputDelay = 0;
/* 69 */     this.Coefs = null;
/*    */   }
/*    */   
/*    */   void Assign(SilkResamplerState other) {
/* 73 */     this.resampler_function = other.resampler_function;
/* 74 */     this.batchSize = other.batchSize;
/* 75 */     this.invRatio_Q16 = other.invRatio_Q16;
/* 76 */     this.FIR_Order = other.FIR_Order;
/* 77 */     this.FIR_Fracs = other.FIR_Fracs;
/* 78 */     this.Fs_in_kHz = other.Fs_in_kHz;
/* 79 */     this.Fs_out_kHz = other.Fs_out_kHz;
/* 80 */     this.inputDelay = other.inputDelay;
/* 81 */     this.Coefs = other.Coefs;
/* 82 */     System.arraycopy(other.sIIR, 0, this.sIIR, 0, 6);
/* 83 */     System.arraycopy(other.sFIR_i32, 0, this.sFIR_i32, 0, 36);
/* 84 */     System.arraycopy(other.sFIR_i16, 0, this.sFIR_i16, 0, 36);
/* 85 */     System.arraycopy(other.delayBuf, 0, this.delayBuf, 0, 48);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SilkResamplerState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */