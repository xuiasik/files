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
/*     */ 
/*     */ class DecodeIndices
/*     */ {
/*     */   static void silk_decode_indices(SilkChannelDecoder psDec, EntropyCoder psRangeDec, int FrameIndex, int decode_LBRR, int condCoding) {
/*     */     int Ix;
/*  46 */     short[] ec_ix = new short[psDec.LPC_order];
/*  47 */     short[] pred_Q8 = new short[psDec.LPC_order];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     if (decode_LBRR != 0 || psDec.VAD_flags[FrameIndex] != 0) {
/*  57 */       Ix = psRangeDec.dec_icdf(SilkTables.silk_type_offset_VAD_iCDF, 8) + 2;
/*     */     } else {
/*  59 */       Ix = psRangeDec.dec_icdf(SilkTables.silk_type_offset_no_VAD_iCDF, 8);
/*     */     } 
/*  61 */     psDec.indices.signalType = (byte)Inlines.silk_RSHIFT(Ix, 1);
/*  62 */     psDec.indices.quantOffsetType = (byte)(Ix & 0x1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     if (condCoding == 2) {
/*     */       
/*  74 */       psDec.indices.GainsIndices[0] = (byte)psRangeDec.dec_icdf(SilkTables.silk_delta_gain_iCDF, 8);
/*     */     } else {
/*     */       
/*  77 */       psDec.indices.GainsIndices[0] = (byte)Inlines.silk_LSHIFT(psRangeDec.dec_icdf(SilkTables.silk_gain_iCDF[psDec.indices.signalType], 8), 3);
/*  78 */       psDec.indices.GainsIndices[0] = (byte)(psDec.indices.GainsIndices[0] + (byte)psRangeDec.dec_icdf(SilkTables.silk_uniform8_iCDF, 8));
/*     */     } 
/*     */     
/*     */     int i;
/*  82 */     for (i = 1; i < psDec.nb_subfr; i++) {
/*  83 */       psDec.indices.GainsIndices[i] = (byte)psRangeDec.dec_icdf(SilkTables.silk_delta_gain_iCDF, 8);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     psDec.indices.NLSFIndices[0] = (byte)psRangeDec.dec_icdf(psDec.psNLSF_CB.CB1_iCDF, (psDec.indices.signalType >> 1) * psDec.psNLSF_CB.nVectors, 8);
/*  94 */     NLSF.silk_NLSF_unpack(ec_ix, pred_Q8, psDec.psNLSF_CB, psDec.indices.NLSFIndices[0]);
/*  95 */     Inlines.OpusAssert((psDec.psNLSF_CB.order == psDec.LPC_order));
/*  96 */     for (i = 0; i < psDec.psNLSF_CB.order; i++) {
/*  97 */       Ix = psRangeDec.dec_icdf(psDec.psNLSF_CB.ec_iCDF, ec_ix[i], 8);
/*  98 */       if (Ix == 0) {
/*  99 */         Ix -= psRangeDec.dec_icdf(SilkTables.silk_NLSF_EXT_iCDF, 8);
/* 100 */       } else if (Ix == 8) {
/* 101 */         Ix += psRangeDec.dec_icdf(SilkTables.silk_NLSF_EXT_iCDF, 8);
/*     */       } 
/* 103 */       psDec.indices.NLSFIndices[i + 1] = (byte)(Ix - 4);
/*     */     } 
/*     */ 
/*     */     
/* 107 */     if (psDec.nb_subfr == 4) {
/* 108 */       psDec.indices.NLSFInterpCoef_Q2 = (byte)psRangeDec.dec_icdf(SilkTables.silk_NLSF_interpolation_factor_iCDF, 8);
/*     */     } else {
/* 110 */       psDec.indices.NLSFInterpCoef_Q2 = 4;
/*     */     } 
/*     */     
/* 113 */     if (psDec.indices.signalType == 2) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 122 */       int decode_absolute_lagIndex = 1;
/* 123 */       if (condCoding == 2 && psDec.ec_prevSignalType == 2) {
/*     */         
/* 125 */         int delta_lagIndex = (short)psRangeDec.dec_icdf(SilkTables.silk_pitch_delta_iCDF, 8);
/* 126 */         if (delta_lagIndex > 0) {
/* 127 */           delta_lagIndex -= 9;
/* 128 */           psDec.indices.lagIndex = (short)(psDec.ec_prevLagIndex + delta_lagIndex);
/* 129 */           decode_absolute_lagIndex = 0;
/*     */         } 
/*     */       } 
/* 132 */       if (decode_absolute_lagIndex != 0) {
/*     */         
/* 134 */         psDec.indices.lagIndex = (short)(psRangeDec.dec_icdf(SilkTables.silk_pitch_lag_iCDF, 8) * Inlines.silk_RSHIFT(psDec.fs_kHz, 1));
/* 135 */         psDec.indices.lagIndex = (short)(psDec.indices.lagIndex + (short)psRangeDec.dec_icdf(psDec.pitch_lag_low_bits_iCDF, 8));
/*     */       } 
/* 137 */       psDec.ec_prevLagIndex = psDec.indices.lagIndex;
/*     */ 
/*     */       
/* 140 */       psDec.indices.contourIndex = (byte)psRangeDec.dec_icdf(psDec.pitch_contour_iCDF, 8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 150 */       psDec.indices.PERIndex = (byte)psRangeDec.dec_icdf(SilkTables.silk_LTP_per_index_iCDF, 8);
/*     */       
/* 152 */       for (int k = 0; k < psDec.nb_subfr; k++) {
/* 153 */         psDec.indices.LTPIndex[k] = (byte)psRangeDec.dec_icdf(SilkTables.silk_LTP_gain_iCDF_ptrs[psDec.indices.PERIndex], 8);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 163 */       if (condCoding == 0) {
/* 164 */         psDec.indices.LTP_scaleIndex = (byte)psRangeDec.dec_icdf(SilkTables.silk_LTPscale_iCDF, 8);
/*     */       } else {
/* 166 */         psDec.indices.LTP_scaleIndex = 0;
/*     */       } 
/*     */     } 
/* 169 */     psDec.ec_prevSignalType = psDec.indices.signalType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     psDec.indices.Seed = (byte)psRangeDec.dec_icdf(SilkTables.silk_uniform4_iCDF, 8);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\DecodeIndices.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */