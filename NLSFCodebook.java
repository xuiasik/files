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
/*    */ class NLSFCodebook
/*    */ {
/* 39 */   short nVectors = 0;
/*    */   
/* 41 */   short order = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   short quantStepSize_Q16 = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   short invQuantStepSize_Q6 = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   short[] CB1_NLSF_Q8 = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   short[] CB1_iCDF = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   short[] pred_Q8 = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   short[] ec_sel = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   short[] ec_iCDF = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 81 */   short[] ec_Rates_Q5 = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 86 */   short[] deltaMin_Q15 = null;
/*    */   
/*    */   void Reset() {
/* 89 */     this.nVectors = 0;
/* 90 */     this.order = 0;
/* 91 */     this.quantStepSize_Q16 = 0;
/* 92 */     this.invQuantStepSize_Q6 = 0;
/* 93 */     this.CB1_NLSF_Q8 = null;
/* 94 */     this.CB1_iCDF = null;
/* 95 */     this.pred_Q8 = null;
/* 96 */     this.ec_sel = null;
/* 97 */     this.ec_iCDF = null;
/* 98 */     this.ec_Rates_Q5 = null;
/* 99 */     this.deltaMin_Q15 = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\NLSFCodebook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */