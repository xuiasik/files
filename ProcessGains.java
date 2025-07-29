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
/*     */ class ProcessGains
/*     */ {
/*     */   static void silk_process_gains(SilkChannelEncoder psEnc, SilkEncoderControl psEncCtrl, int condCoding) {
/*  42 */     SilkShapeState psShapeSt = psEnc.sShape;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     if (psEnc.indices.signalType == 2) {
/*     */       
/*  49 */       int s_Q16 = 0 - Sigmoid.silk_sigm_Q15(Inlines.silk_RSHIFT_ROUND(psEncCtrl.LTPredCodGain_Q7 - 1536, 4));
/*  50 */       for (int i = 0; i < psEnc.nb_subfr; i++) {
/*  51 */         psEncCtrl.Gains_Q16[i] = Inlines.silk_SMLAWB(psEncCtrl.Gains_Q16[i], psEncCtrl.Gains_Q16[i], s_Q16);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  57 */     int InvMaxSqrVal_Q16 = Inlines.silk_DIV32_16(Inlines.silk_log2lin(
/*  58 */           Inlines.silk_SMULWB(8894 - psEnc.SNR_dB_Q7, 21627)), psEnc.subfr_length);
/*     */     
/*  60 */     for (int k = 0; k < psEnc.nb_subfr; k++) {
/*     */       
/*  62 */       int ResNrg = psEncCtrl.ResNrg[k];
/*  63 */       int ResNrgPart = Inlines.silk_SMULWW(ResNrg, InvMaxSqrVal_Q16);
/*  64 */       if (psEncCtrl.ResNrgQ[k] > 0) {
/*  65 */         ResNrgPart = Inlines.silk_RSHIFT_ROUND(ResNrgPart, psEncCtrl.ResNrgQ[k]);
/*  66 */       } else if (ResNrgPart >= Inlines.silk_RSHIFT(2147483647, -psEncCtrl.ResNrgQ[k])) {
/*  67 */         ResNrgPart = Integer.MAX_VALUE;
/*     */       } else {
/*  69 */         ResNrgPart = Inlines.silk_LSHIFT(ResNrgPart, -psEncCtrl.ResNrgQ[k]);
/*     */       } 
/*  71 */       int gain = psEncCtrl.Gains_Q16[k];
/*  72 */       int gain_squared = Inlines.silk_ADD_SAT32(ResNrgPart, Inlines.silk_SMMUL(gain, gain));
/*  73 */       if (gain_squared < 32767) {
/*     */         
/*  75 */         gain_squared = Inlines.silk_SMLAWW(Inlines.silk_LSHIFT(ResNrgPart, 16), gain, gain);
/*  76 */         Inlines.OpusAssert((gain_squared > 0));
/*  77 */         gain = Inlines.silk_SQRT_APPROX(gain_squared);
/*     */         
/*  79 */         gain = Inlines.silk_min(gain, 8388607);
/*  80 */         psEncCtrl.Gains_Q16[k] = Inlines.silk_LSHIFT_SAT32(gain, 8);
/*     */       } else {
/*     */         
/*  83 */         gain = Inlines.silk_SQRT_APPROX(gain_squared);
/*     */         
/*  85 */         gain = Inlines.silk_min(gain, 32767);
/*  86 */         psEncCtrl.Gains_Q16[k] = Inlines.silk_LSHIFT_SAT32(gain, 16);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     System.arraycopy(psEncCtrl.Gains_Q16, 0, psEncCtrl.GainsUnq_Q16, 0, psEnc.nb_subfr);
/*  94 */     psEncCtrl.lastGainIndexPrev = psShapeSt.LastGainIndex;
/*     */ 
/*     */     
/*  97 */     BoxedValueByte boxed_lastGainIndex = new BoxedValueByte(psShapeSt.LastGainIndex);
/*  98 */     GainQuantization.silk_gains_quant(psEnc.indices.GainsIndices, psEncCtrl.Gains_Q16, boxed_lastGainIndex, 
/*  99 */         (condCoding == 2) ? 1 : 0, psEnc.nb_subfr);
/* 100 */     psShapeSt.LastGainIndex = boxed_lastGainIndex.Val;
/*     */ 
/*     */     
/* 103 */     if (psEnc.indices.signalType == 2) {
/* 104 */       if (psEncCtrl.LTPredCodGain_Q7 + Inlines.silk_RSHIFT(psEnc.input_tilt_Q15, 8) > 128) {
/* 105 */         psEnc.indices.quantOffsetType = 0;
/*     */       } else {
/* 107 */         psEnc.indices.quantOffsetType = 1;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 112 */     int quant_offset_Q10 = SilkTables.silk_Quantization_Offsets_Q10[psEnc.indices.signalType >> 1][psEnc.indices.quantOffsetType];
/* 113 */     psEncCtrl
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       .Lambda_Q10 = 1229 + Inlines.silk_SMULBB(-50, psEnc.nStatesDelayedDecision) + Inlines.silk_SMULWB(-52428, psEnc.speech_activity_Q8) + Inlines.silk_SMULWB(-409, psEncCtrl.input_quality_Q14) + Inlines.silk_SMULWB(-818, psEncCtrl.coding_quality_Q14) + Inlines.silk_SMULWB(52429, quant_offset_Q10);
/*     */     
/* 120 */     Inlines.OpusAssert((psEncCtrl.Lambda_Q10 > 0));
/* 121 */     Inlines.OpusAssert((psEncCtrl.Lambda_Q10 < 2048));
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\ProcessGains.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */