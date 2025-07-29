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
/*     */ class DecodePulses
/*     */ {
/*     */   static void silk_decode_pulses(EntropyCoder psRangeDec, short[] pulses, int signalType, int quantOffsetType, int frame_length) {
/*  51 */     int[] sum_pulses = new int[20];
/*  52 */     int[] nLshifts = new int[20];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     int RateLevelIndex = psRangeDec.dec_icdf(SilkTables.silk_rate_levels_iCDF[signalType >> 1], 8);
/*     */ 
/*     */     
/*  65 */     Inlines.OpusAssert(true);
/*  66 */     int iter = Inlines.silk_RSHIFT(frame_length, 4);
/*  67 */     if (iter * 16 < frame_length) {
/*  68 */       Inlines.OpusAssert((frame_length == 120));
/*     */       
/*  70 */       iter++;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int i;
/*     */ 
/*     */ 
/*     */     
/*  80 */     for (i = 0; i < iter; i++) {
/*  81 */       nLshifts[i] = 0;
/*  82 */       sum_pulses[i] = psRangeDec.dec_icdf(SilkTables.silk_pulses_per_block_iCDF[RateLevelIndex], 8);
/*     */ 
/*     */       
/*  85 */       while (sum_pulses[i] == 17) {
/*  86 */         nLshifts[i] = nLshifts[i] + 1;
/*     */         
/*  88 */         sum_pulses[i] = psRangeDec.dec_icdf(SilkTables.silk_pulses_per_block_iCDF[9], 
/*  89 */             (nLshifts[i] == 10) ? 1 : 0, 8);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     for (i = 0; i < iter; i++) {
/* 101 */       if (sum_pulses[i] > 0) {
/* 102 */         ShellCoder.silk_shell_decoder(pulses, Inlines.silk_SMULBB(i, 16), psRangeDec, sum_pulses[i]);
/*     */       } else {
/* 104 */         Arrays.MemSetWithOffset(pulses, (short)0, Inlines.silk_SMULBB(i, 16), 16);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     for (i = 0; i < iter; i++) {
/* 116 */       if (nLshifts[i] > 0) {
/* 117 */         int nLS = nLshifts[i];
/* 118 */         int pulses_ptr = Inlines.silk_SMULBB(i, 16);
/* 119 */         for (int k = 0; k < 16; k++) {
/* 120 */           int abs_q = pulses[pulses_ptr + k];
/* 121 */           for (int j = 0; j < nLS; j++) {
/* 122 */             abs_q = Inlines.silk_LSHIFT(abs_q, 1);
/* 123 */             abs_q += psRangeDec.dec_icdf(SilkTables.silk_lsb_iCDF, 8);
/*     */           } 
/* 125 */           pulses[pulses_ptr + k] = (short)abs_q;
/*     */         } 
/*     */         
/* 128 */         sum_pulses[i] = sum_pulses[i] | nLS << 5;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     CodeSigns.silk_decode_signs(psRangeDec, pulses, frame_length, signalType, quantOffsetType, sum_pulses);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\DecodePulses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */