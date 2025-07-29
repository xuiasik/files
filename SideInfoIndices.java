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
/*    */ class SideInfoIndices
/*    */ {
/* 36 */   final byte[] GainsIndices = new byte[4];
/* 37 */   final byte[] LTPIndex = new byte[4];
/* 38 */   final byte[] NLSFIndices = new byte[17];
/* 39 */   short lagIndex = 0;
/* 40 */   byte contourIndex = 0;
/* 41 */   byte signalType = 0;
/* 42 */   byte quantOffsetType = 0;
/* 43 */   byte NLSFInterpCoef_Q2 = 0;
/* 44 */   byte PERIndex = 0;
/* 45 */   byte LTP_scaleIndex = 0;
/* 46 */   byte Seed = 0;
/*    */   
/*    */   void Reset() {
/* 49 */     Arrays.MemSet(this.GainsIndices, (byte)0, 4);
/* 50 */     Arrays.MemSet(this.LTPIndex, (byte)0, 4);
/* 51 */     Arrays.MemSet(this.NLSFIndices, (byte)0, 17);
/* 52 */     this.lagIndex = 0;
/* 53 */     this.contourIndex = 0;
/* 54 */     this.signalType = 0;
/* 55 */     this.quantOffsetType = 0;
/* 56 */     this.NLSFInterpCoef_Q2 = 0;
/* 57 */     this.PERIndex = 0;
/* 58 */     this.LTP_scaleIndex = 0;
/* 59 */     this.Seed = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void Assign(SideInfoIndices other) {
/* 67 */     System.arraycopy(other.GainsIndices, 0, this.GainsIndices, 0, 4);
/* 68 */     System.arraycopy(other.LTPIndex, 0, this.LTPIndex, 0, 4);
/* 69 */     System.arraycopy(other.NLSFIndices, 0, this.NLSFIndices, 0, 17);
/* 70 */     this.lagIndex = other.lagIndex;
/* 71 */     this.contourIndex = other.contourIndex;
/* 72 */     this.signalType = other.signalType;
/* 73 */     this.quantOffsetType = other.quantOffsetType;
/* 74 */     this.NLSFInterpCoef_Q2 = other.NLSFInterpCoef_Q2;
/* 75 */     this.PERIndex = other.PERIndex;
/* 76 */     this.LTP_scaleIndex = other.LTP_scaleIndex;
/* 77 */     this.Seed = other.Seed;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SideInfoIndices.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */