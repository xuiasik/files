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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EncodeIndices
/*     */ {
/*     */   static void silk_encode_indices(SilkChannelEncoder psEncC, EntropyCoder psRangeEnc, int FrameIndex, int encode_LBRR, int condCoding) {
/*     */     SideInfoIndices psIndices;
/*  52 */     short[] ec_ix = new short[16];
/*  53 */     short[] pred_Q8 = new short[16];
/*     */ 
/*     */     
/*  56 */     if (encode_LBRR != 0) {
/*  57 */       psIndices = psEncC.indices_LBRR[FrameIndex];
/*     */     } else {
/*  59 */       psIndices = psEncC.indices;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     int typeOffset = 2 * psIndices.signalType + psIndices.quantOffsetType;
/*  70 */     Inlines.OpusAssert((typeOffset >= 0 && typeOffset < 6));
/*  71 */     Inlines.OpusAssert((encode_LBRR == 0 || typeOffset >= 2));
/*  72 */     if (encode_LBRR != 0 || typeOffset >= 2) {
/*  73 */       psRangeEnc.enc_icdf(typeOffset - 2, SilkTables.silk_type_offset_VAD_iCDF, 8);
/*     */     } else {
/*  75 */       psRangeEnc.enc_icdf(typeOffset, SilkTables.silk_type_offset_no_VAD_iCDF, 8);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     if (condCoding == 2) {
/*     */       
/*  88 */       Inlines.OpusAssert((psIndices.GainsIndices[0] >= 0 && psIndices.GainsIndices[0] < 41));
/*  89 */       psRangeEnc.enc_icdf(psIndices.GainsIndices[0], SilkTables.silk_delta_gain_iCDF, 8);
/*     */     } else {
/*     */       
/*  92 */       Inlines.OpusAssert((psIndices.GainsIndices[0] >= 0 && psIndices.GainsIndices[0] < 64));
/*  93 */       psRangeEnc.enc_icdf(Inlines.silk_RSHIFT(psIndices.GainsIndices[0], 3), SilkTables.silk_gain_iCDF[psIndices.signalType], 8);
/*  94 */       psRangeEnc.enc_icdf(psIndices.GainsIndices[0] & 0x7, SilkTables.silk_uniform8_iCDF, 8);
/*     */     } 
/*     */     
/*     */     int i;
/*  98 */     for (i = 1; i < psEncC.nb_subfr; i++) {
/*  99 */       Inlines.OpusAssert((psIndices.GainsIndices[i] >= 0 && psIndices.GainsIndices[i] < 41));
/* 100 */       psRangeEnc.enc_icdf(psIndices.GainsIndices[i], SilkTables.silk_delta_gain_iCDF, 8);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     psRangeEnc.enc_icdf(psIndices.NLSFIndices[0], psEncC.psNLSF_CB.CB1_iCDF, (psIndices.signalType >> 1) * psEncC.psNLSF_CB.nVectors, 8);
/* 111 */     NLSF.silk_NLSF_unpack(ec_ix, pred_Q8, psEncC.psNLSF_CB, psIndices.NLSFIndices[0]);
/* 112 */     Inlines.OpusAssert((psEncC.psNLSF_CB.order == psEncC.predictLPCOrder));
/*     */     
/* 114 */     for (i = 0; i < psEncC.psNLSF_CB.order; i++) {
/* 115 */       if (psIndices.NLSFIndices[i + 1] >= 4) {
/* 116 */         psRangeEnc.enc_icdf(8, psEncC.psNLSF_CB.ec_iCDF, ec_ix[i], 8);
/* 117 */         psRangeEnc.enc_icdf(psIndices.NLSFIndices[i + 1] - 4, SilkTables.silk_NLSF_EXT_iCDF, 8);
/* 118 */       } else if (psIndices.NLSFIndices[i + 1] <= -4) {
/* 119 */         psRangeEnc.enc_icdf(0, psEncC.psNLSF_CB.ec_iCDF, ec_ix[i], 8);
/* 120 */         psRangeEnc.enc_icdf(-psIndices.NLSFIndices[i + 1] - 4, SilkTables.silk_NLSF_EXT_iCDF, 8);
/*     */       } else {
/* 122 */         psRangeEnc.enc_icdf(psIndices.NLSFIndices[i + 1] + 4, psEncC.psNLSF_CB.ec_iCDF, ec_ix[i], 8);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 127 */     if (psEncC.nb_subfr == 4) {
/* 128 */       Inlines.OpusAssert((psIndices.NLSFInterpCoef_Q2 >= 0 && psIndices.NLSFInterpCoef_Q2 < 5));
/* 129 */       psRangeEnc.enc_icdf(psIndices.NLSFInterpCoef_Q2, SilkTables.silk_NLSF_interpolation_factor_iCDF, 8);
/*     */     } 
/*     */     
/* 132 */     if (psIndices.signalType == 2) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 141 */       int encode_absolute_lagIndex = 1;
/* 142 */       if (condCoding == 2 && psEncC.ec_prevSignalType == 2) {
/*     */         
/* 144 */         int delta_lagIndex = psIndices.lagIndex - psEncC.ec_prevLagIndex;
/*     */         
/* 146 */         if (delta_lagIndex < -8 || delta_lagIndex > 11) {
/* 147 */           delta_lagIndex = 0;
/*     */         } else {
/* 149 */           delta_lagIndex += 9;
/* 150 */           encode_absolute_lagIndex = 0;
/*     */         } 
/*     */ 
/*     */         
/* 154 */         Inlines.OpusAssert((delta_lagIndex >= 0 && delta_lagIndex < 21));
/* 155 */         psRangeEnc.enc_icdf(delta_lagIndex, SilkTables.silk_pitch_delta_iCDF, 8);
/*     */       } 
/*     */       
/* 158 */       if (encode_absolute_lagIndex != 0) {
/*     */ 
/*     */         
/* 161 */         int pitch_high_bits = Inlines.silk_DIV32_16(psIndices.lagIndex, Inlines.silk_RSHIFT(psEncC.fs_kHz, 1));
/* 162 */         int pitch_low_bits = psIndices.lagIndex - Inlines.silk_SMULBB(pitch_high_bits, Inlines.silk_RSHIFT(psEncC.fs_kHz, 1));
/* 163 */         Inlines.OpusAssert((pitch_low_bits < psEncC.fs_kHz / 2));
/* 164 */         Inlines.OpusAssert((pitch_high_bits < 32));
/* 165 */         psRangeEnc.enc_icdf(pitch_high_bits, SilkTables.silk_pitch_lag_iCDF, 8);
/* 166 */         psRangeEnc.enc_icdf(pitch_low_bits, psEncC.pitch_lag_low_bits_iCDF, 8);
/*     */       } 
/* 168 */       psEncC.ec_prevLagIndex = psIndices.lagIndex;
/*     */ 
/*     */       
/* 171 */       Inlines.OpusAssert((psIndices.contourIndex >= 0));
/* 172 */       Inlines.OpusAssert(((psIndices.contourIndex < 34 && psEncC.fs_kHz > 8 && psEncC.nb_subfr == 4) || (psIndices.contourIndex < 11 && psEncC.fs_kHz == 8 && psEncC.nb_subfr == 4) || (psIndices.contourIndex < 12 && psEncC.fs_kHz > 8 && psEncC.nb_subfr == 2) || (psIndices.contourIndex < 3 && psEncC.fs_kHz == 8 && psEncC.nb_subfr == 2)));
/* 173 */       psRangeEnc.enc_icdf(psIndices.contourIndex, psEncC.pitch_contour_iCDF, 8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 183 */       Inlines.OpusAssert((psIndices.PERIndex >= 0 && psIndices.PERIndex < 3));
/* 184 */       psRangeEnc.enc_icdf(psIndices.PERIndex, SilkTables.silk_LTP_per_index_iCDF, 8);
/*     */ 
/*     */       
/* 187 */       for (int k = 0; k < psEncC.nb_subfr; k++) {
/* 188 */         Inlines.OpusAssert((psIndices.LTPIndex[k] >= 0 && psIndices.LTPIndex[k] < 8 << psIndices.PERIndex));
/* 189 */         psRangeEnc.enc_icdf(psIndices.LTPIndex[k], SilkTables.silk_LTP_gain_iCDF_ptrs[psIndices.PERIndex], 8);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 199 */       if (condCoding == 0) {
/* 200 */         Inlines.OpusAssert((psIndices.LTP_scaleIndex >= 0 && psIndices.LTP_scaleIndex < 3));
/* 201 */         psRangeEnc.enc_icdf(psIndices.LTP_scaleIndex, SilkTables.silk_LTPscale_iCDF, 8);
/*     */       } 
/*     */       
/* 204 */       Inlines.OpusAssert((condCoding == 0 || psIndices.LTP_scaleIndex == 0));
/*     */     } 
/*     */     
/* 207 */     psEncC.ec_prevSignalType = psIndices.signalType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     Inlines.OpusAssert((psIndices.Seed >= 0 && psIndices.Seed < 4));
/* 217 */     psRangeEnc.enc_icdf(psIndices.Seed, SilkTables.silk_uniform4_iCDF, 8);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\EncodeIndices.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */