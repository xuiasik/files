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
/*     */ class CodeSigns
/*     */ {
/*     */   private static int silk_enc_map(int a) {
/*  37 */     return Inlines.silk_RSHIFT(a, 15) + 1;
/*     */   }
/*     */   
/*     */   private static int silk_dec_map(int a) {
/*  41 */     return Inlines.silk_LSHIFT(a, 1) - 1;
/*     */   }
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
/*     */   static void silk_encode_signs(EntropyCoder psRangeEnc, byte[] pulses, int length, int signalType, int quantOffsetType, int[] sum_pulses) {
/*  61 */     short[] icdf = new short[2];
/*     */     
/*  63 */     short[] sign_icdf = SilkTables.silk_sign_iCDF;
/*     */ 
/*     */     
/*  66 */     icdf[1] = 0;
/*  67 */     int q_ptr = 0;
/*  68 */     int i = Inlines.silk_SMULBB(7, Inlines.silk_ADD_LSHIFT(quantOffsetType, signalType, 1));
/*  69 */     int icdf_ptr = i;
/*  70 */     length = Inlines.silk_RSHIFT(length + 8, 4);
/*  71 */     for (i = 0; i < length; i++) {
/*  72 */       int p = sum_pulses[i];
/*  73 */       if (p > 0) {
/*  74 */         icdf[0] = sign_icdf[icdf_ptr + Inlines.silk_min(p & 0x1F, 6)];
/*  75 */         for (int j = q_ptr; j < q_ptr + 16; j++) {
/*  76 */           if (pulses[j] != 0) {
/*  77 */             psRangeEnc.enc_icdf(silk_enc_map(pulses[j]), icdf, 8);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/*  82 */       q_ptr += 16;
/*     */     } 
/*     */   }
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
/*     */   static void silk_decode_signs(EntropyCoder psRangeDec, short[] pulses, int length, int signalType, int quantOffsetType, int[] sum_pulses) {
/* 103 */     short[] icdf = new short[2];
/*     */     
/* 105 */     short[] icdf_table = SilkTables.silk_sign_iCDF;
/*     */ 
/*     */     
/* 108 */     icdf[1] = 0;
/* 109 */     int q_ptr = 0;
/* 110 */     int i = Inlines.silk_SMULBB(7, Inlines.silk_ADD_LSHIFT(quantOffsetType, signalType, 1));
/* 111 */     int icdf_ptr = i;
/* 112 */     length = Inlines.silk_RSHIFT(length + 8, 4);
/*     */     
/* 114 */     for (i = 0; i < length; i++) {
/* 115 */       int p = sum_pulses[i];
/*     */       
/* 117 */       if (p > 0) {
/* 118 */         icdf[0] = icdf_table[icdf_ptr + Inlines.silk_min(p & 0x1F, 6)];
/* 119 */         for (int j = 0; j < 16; j++) {
/* 120 */           if (pulses[q_ptr + j] > 0)
/*     */           {
/* 122 */             pulses[q_ptr + j] = (short)(pulses[q_ptr + j] * (short)silk_dec_map(psRangeDec.dec_icdf(icdf, 8)));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 127 */       q_ptr += 16;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\CodeSigns.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */