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
/*     */ class FindLPC
/*     */ {
/*     */   static void silk_find_LPC(SilkChannelEncoder psEncC, short[] NLSF_Q15, short[] x, int minInvGain_Q30) {
/*  44 */     int[] a_Q16 = new int[16];
/*     */     
/*  46 */     BoxedValueInt res_nrg0 = new BoxedValueInt(0);
/*  47 */     BoxedValueInt res_nrg1 = new BoxedValueInt(0);
/*  48 */     BoxedValueInt rshift0 = new BoxedValueInt(0);
/*  49 */     BoxedValueInt rshift1 = new BoxedValueInt(0);
/*  50 */     BoxedValueInt scratch_box1 = new BoxedValueInt(0);
/*  51 */     BoxedValueInt scratch_box2 = new BoxedValueInt(0);
/*     */ 
/*     */     
/*  54 */     int[] a_tmp_Q16 = new int[16];
/*     */ 
/*     */     
/*  57 */     short[] a_tmp_Q12 = new short[16];
/*  58 */     short[] NLSF0_Q15 = new short[16];
/*     */     
/*  60 */     int subfr_length = psEncC.subfr_length + psEncC.predictLPCOrder;
/*     */ 
/*     */     
/*  63 */     psEncC.indices.NLSFInterpCoef_Q2 = 4;
/*     */ 
/*     */     
/*  66 */     BurgModified.silk_burg_modified(scratch_box1, scratch_box2, a_Q16, x, 0, minInvGain_Q30, subfr_length, psEncC.nb_subfr, psEncC.predictLPCOrder);
/*  67 */     int res_nrg = scratch_box1.Val;
/*  68 */     int res_nrg_Q = scratch_box2.Val;
/*     */     
/*  70 */     if (psEncC.useInterpolatedNLSFs != 0 && psEncC.first_frame_after_reset == 0 && psEncC.nb_subfr == 4) {
/*     */ 
/*     */ 
/*     */       
/*  74 */       BurgModified.silk_burg_modified(scratch_box1, scratch_box2, a_tmp_Q16, x, 2 * subfr_length, minInvGain_Q30, subfr_length, 2, psEncC.predictLPCOrder);
/*  75 */       int res_tmp_nrg = scratch_box1.Val;
/*  76 */       int res_tmp_nrg_Q = scratch_box2.Val;
/*     */ 
/*     */ 
/*     */       
/*  80 */       int shift = res_tmp_nrg_Q - res_nrg_Q;
/*  81 */       if (shift >= 0) {
/*  82 */         if (shift < 32) {
/*  83 */           res_nrg -= Inlines.silk_RSHIFT(res_tmp_nrg, shift);
/*     */         }
/*     */       } else {
/*  86 */         Inlines.OpusAssert((shift > -32));
/*  87 */         res_nrg = Inlines.silk_RSHIFT(res_nrg, -shift) - res_tmp_nrg;
/*  88 */         res_nrg_Q = res_tmp_nrg_Q;
/*     */       } 
/*     */ 
/*     */       
/*  92 */       NLSF.silk_A2NLSF(NLSF_Q15, a_tmp_Q16, psEncC.predictLPCOrder);
/*     */       
/*  94 */       short[] LPC_res = new short[2 * subfr_length];
/*     */ 
/*     */       
/*  97 */       for (int k = 3; k >= 0; k--) {
/*     */         int isInterpLower, res_nrg_interp_Q;
/*  99 */         Inlines.silk_interpolate(NLSF0_Q15, psEncC.prev_NLSFq_Q15, NLSF_Q15, k, psEncC.predictLPCOrder);
/*     */ 
/*     */         
/* 102 */         NLSF.silk_NLSF2A(a_tmp_Q12, NLSF0_Q15, psEncC.predictLPCOrder);
/*     */ 
/*     */         
/* 105 */         Filters.silk_LPC_analysis_filter(LPC_res, 0, x, 0, a_tmp_Q12, 0, 2 * subfr_length, psEncC.predictLPCOrder);
/*     */         
/* 107 */         SumSqrShift.silk_sum_sqr_shift(res_nrg0, rshift0, LPC_res, psEncC.predictLPCOrder, subfr_length - psEncC.predictLPCOrder);
/*     */         
/* 109 */         SumSqrShift.silk_sum_sqr_shift(res_nrg1, rshift1, LPC_res, psEncC.predictLPCOrder + subfr_length, subfr_length - psEncC.predictLPCOrder);
/*     */ 
/*     */         
/* 112 */         shift = rshift0.Val - rshift1.Val;
/* 113 */         if (shift >= 0) {
/* 114 */           res_nrg1.Val = Inlines.silk_RSHIFT(res_nrg1.Val, shift);
/* 115 */           res_nrg_interp_Q = 0 - rshift0.Val;
/*     */         } else {
/* 117 */           res_nrg0.Val = Inlines.silk_RSHIFT(res_nrg0.Val, 0 - shift);
/* 118 */           res_nrg_interp_Q = 0 - rshift1.Val;
/*     */         } 
/* 120 */         int res_nrg_interp = Inlines.silk_ADD32(res_nrg0.Val, res_nrg1.Val);
/*     */ 
/*     */         
/* 123 */         shift = res_nrg_interp_Q - res_nrg_Q;
/* 124 */         if (shift >= 0) {
/* 125 */           if (Inlines.silk_RSHIFT(res_nrg_interp, shift) < res_nrg) {
/* 126 */             isInterpLower = 1;
/*     */           } else {
/* 128 */             isInterpLower = 0;
/*     */           } 
/* 130 */         } else if (-shift < 32) {
/* 131 */           if (res_nrg_interp < Inlines.silk_RSHIFT(res_nrg, -shift)) {
/* 132 */             isInterpLower = 1;
/*     */           } else {
/* 134 */             isInterpLower = 0;
/*     */           } 
/*     */         } else {
/* 137 */           isInterpLower = 0;
/*     */         } 
/*     */ 
/*     */         
/* 141 */         if (isInterpLower == 1) {
/*     */           
/* 143 */           res_nrg = res_nrg_interp;
/* 144 */           res_nrg_Q = res_nrg_interp_Q;
/* 145 */           psEncC.indices.NLSFInterpCoef_Q2 = (byte)k;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 150 */     if (psEncC.indices.NLSFInterpCoef_Q2 == 4)
/*     */     {
/* 152 */       NLSF.silk_A2NLSF(NLSF_Q15, a_Q16, psEncC.predictLPCOrder);
/*     */     }
/*     */     
/* 155 */     Inlines.OpusAssert((psEncC.indices.NLSFInterpCoef_Q2 == 4 || (psEncC.useInterpolatedNLSFs != 0 && psEncC.first_frame_after_reset == 0 && psEncC.nb_subfr == 4)));
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\FindLPC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */