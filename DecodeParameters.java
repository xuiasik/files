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
/*     */ class DecodeParameters
/*     */ {
/*     */   static void silk_decode_parameters(SilkChannelDecoder psDec, SilkDecoderControl psDecCtrl, int condCoding) {
/*  43 */     short[] pNLSF_Q15 = new short[psDec.LPC_order];
/*  44 */     short[] pNLSF0_Q15 = new short[psDec.LPC_order];
/*     */ 
/*     */ 
/*     */     
/*  48 */     BoxedValueByte boxedLastGainIndex = new BoxedValueByte(psDec.LastGainIndex);
/*  49 */     GainQuantization.silk_gains_dequant(psDecCtrl.Gains_Q16, psDec.indices.GainsIndices, boxedLastGainIndex, 
/*  50 */         (condCoding == 2) ? 1 : 0, psDec.nb_subfr);
/*  51 */     psDec.LastGainIndex = boxedLastGainIndex.Val;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     NLSF.silk_NLSF_decode(pNLSF_Q15, psDec.indices.NLSFIndices, psDec.psNLSF_CB);
/*     */ 
/*     */     
/*  63 */     NLSF.silk_NLSF2A(psDecCtrl.PredCoef_Q12[1], pNLSF_Q15, psDec.LPC_order);
/*     */ 
/*     */ 
/*     */     
/*  67 */     if (psDec.first_frame_after_reset == 1) {
/*  68 */       psDec.indices.NLSFInterpCoef_Q2 = 4;
/*     */     }
/*     */     
/*  71 */     if (psDec.indices.NLSFInterpCoef_Q2 < 4) {
/*     */ 
/*     */       
/*  74 */       for (int i = 0; i < psDec.LPC_order; i++) {
/*  75 */         pNLSF0_Q15[i] = (short)(psDec.prevNLSF_Q15[i] + Inlines.silk_RSHIFT(Inlines.silk_MUL(psDec.indices.NLSFInterpCoef_Q2, pNLSF_Q15[i] - psDec.prevNLSF_Q15[i]), 2));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  80 */       NLSF.silk_NLSF2A(psDecCtrl.PredCoef_Q12[0], pNLSF0_Q15, psDec.LPC_order);
/*     */     } else {
/*     */       
/*  83 */       System.arraycopy(psDecCtrl.PredCoef_Q12[1], 0, psDecCtrl.PredCoef_Q12[0], 0, psDec.LPC_order);
/*     */     } 
/*     */     
/*  86 */     System.arraycopy(pNLSF_Q15, 0, psDec.prevNLSF_Q15, 0, psDec.LPC_order);
/*     */ 
/*     */     
/*  89 */     if (psDec.lossCnt != 0) {
/*  90 */       BWExpander.silk_bwexpander(psDecCtrl.PredCoef_Q12[0], psDec.LPC_order, 63570);
/*  91 */       BWExpander.silk_bwexpander(psDecCtrl.PredCoef_Q12[1], psDec.LPC_order, 63570);
/*     */     } 
/*     */     
/*  94 */     if (psDec.indices.signalType == 2) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 104 */       DecodePitch.silk_decode_pitch(psDec.indices.lagIndex, psDec.indices.contourIndex, psDecCtrl.pitchL, psDec.fs_kHz, psDec.nb_subfr);
/*     */ 
/*     */       
/* 107 */       byte[][] cbk_ptr_Q7 = SilkTables.silk_LTP_vq_ptrs_Q7[psDec.indices.PERIndex];
/*     */ 
/*     */       
/* 110 */       for (int k = 0; k < psDec.nb_subfr; k++) {
/* 111 */         int j = psDec.indices.LTPIndex[k];
/* 112 */         for (int i = 0; i < 5; i++) {
/* 113 */           psDecCtrl.LTPCoef_Q14[k * 5 + i] = (short)Inlines.silk_LSHIFT(cbk_ptr_Q7[j][i], 7);
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 124 */       int Ix = psDec.indices.LTP_scaleIndex;
/* 125 */       psDecCtrl.LTP_scale_Q14 = SilkTables.silk_LTPScales_table_Q14[Ix];
/*     */     } else {
/* 127 */       Arrays.MemSet(psDecCtrl.pitchL, 0, psDec.nb_subfr);
/* 128 */       Arrays.MemSet(psDecCtrl.LTPCoef_Q14, (short)0, 5 * psDec.nb_subfr);
/* 129 */       psDec.indices.PERIndex = 0;
/* 130 */       psDecCtrl.LTP_scale_Q14 = 0;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\DecodeParameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */