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
/*     */ class EncodePulses
/*     */ {
/*     */   static int combine_and_check(int[] pulses_comb, int pulses_comb_ptr, int[] pulses_in, int pulses_in_ptr, int max_pulses, int len) {
/*  51 */     for (int k = 0; k < len; k++) {
/*  52 */       int k2p = 2 * k + pulses_in_ptr;
/*  53 */       int sum = pulses_in[k2p] + pulses_in[k2p + 1];
/*  54 */       if (sum > max_pulses) {
/*  55 */         return 1;
/*     */       }
/*  57 */       pulses_comb[pulses_comb_ptr + k] = sum;
/*     */     } 
/*  59 */     return 0;
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
/*     */   static int combine_and_check(int[] pulses_comb, int[] pulses_in, int max_pulses, int len) {
/*  75 */     for (int k = 0; k < len; k++) {
/*  76 */       int sum = pulses_in[2 * k] + pulses_in[2 * k + 1];
/*  77 */       if (sum > max_pulses) {
/*  78 */         return 1;
/*     */       }
/*  80 */       pulses_comb[k] = sum;
/*     */     } 
/*  82 */     return 0;
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
/*     */   static void silk_encode_pulses(EntropyCoder psRangeEnc, int signalType, int quantOffsetType, byte[] pulses, int frame_length) {
/*  99 */     int RateLevelIndex = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     int[] pulses_comb = new int[8];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     Arrays.MemSet(pulses_comb, 0, 8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     Inlines.OpusAssert(true);
/* 120 */     int iter = Inlines.silk_RSHIFT(frame_length, 4);
/* 121 */     if (iter * 16 < frame_length) {
/* 122 */       Inlines.OpusAssert((frame_length == 120));
/*     */       
/* 124 */       iter++;
/* 125 */       Arrays.MemSetWithOffset(pulses, (byte)0, frame_length, 16);
/*     */     } 
/*     */ 
/*     */     
/* 129 */     int[] abs_pulses = new int[iter * 16];
/* 130 */     Inlines.OpusAssert(true);
/*     */     
/*     */     int i;
/* 133 */     for (i = 0; i < iter * 16; i += 4) {
/* 134 */       abs_pulses[i + 0] = Inlines.silk_abs(pulses[i + 0]);
/* 135 */       abs_pulses[i + 1] = Inlines.silk_abs(pulses[i + 1]);
/* 136 */       abs_pulses[i + 2] = Inlines.silk_abs(pulses[i + 2]);
/* 137 */       abs_pulses[i + 3] = Inlines.silk_abs(pulses[i + 3]);
/*     */     } 
/*     */ 
/*     */     
/* 141 */     int[] sum_pulses = new int[iter];
/* 142 */     int[] nRshifts = new int[iter];
/* 143 */     int abs_pulses_ptr = 0;
/* 144 */     for (i = 0; i < iter; i++) {
/* 145 */       nRshifts[i] = 0;
/*     */ 
/*     */       
/*     */       while (true) {
/* 149 */         int scale_down = combine_and_check(pulses_comb, 0, abs_pulses, abs_pulses_ptr, SilkTables.silk_max_pulses_table[0], 8);
/*     */         
/* 151 */         scale_down += combine_and_check(pulses_comb, pulses_comb, SilkTables.silk_max_pulses_table[1], 4);
/*     */         
/* 153 */         scale_down += combine_and_check(pulses_comb, pulses_comb, SilkTables.silk_max_pulses_table[2], 2);
/*     */         
/* 155 */         scale_down += combine_and_check(sum_pulses, i, pulses_comb, 0, SilkTables.silk_max_pulses_table[3], 1);
/*     */         
/* 157 */         if (scale_down != 0) {
/*     */           
/* 159 */           nRshifts[i] = nRshifts[i] + 1;
/* 160 */           for (int j = abs_pulses_ptr; j < abs_pulses_ptr + 16; j++) {
/* 161 */             abs_pulses[j] = Inlines.silk_RSHIFT(abs_pulses[j], 1);
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/* 169 */       abs_pulses_ptr += 16;
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
/* 180 */     int minSumBits_Q5 = Integer.MAX_VALUE; int k;
/* 181 */     for (k = 0; k < 9; k++) {
/* 182 */       short[] nBits_ptr = SilkTables.silk_pulses_per_block_BITS_Q5[k];
/* 183 */       int sumBits_Q5 = SilkTables.silk_rate_levels_BITS_Q5[signalType >> 1][k];
/* 184 */       for (i = 0; i < iter; i++) {
/* 185 */         if (nRshifts[i] > 0) {
/* 186 */           sumBits_Q5 += nBits_ptr[17];
/*     */         } else {
/* 188 */           sumBits_Q5 += nBits_ptr[sum_pulses[i]];
/*     */         } 
/*     */       } 
/* 191 */       if (sumBits_Q5 < minSumBits_Q5) {
/* 192 */         minSumBits_Q5 = sumBits_Q5;
/* 193 */         RateLevelIndex = k;
/*     */       } 
/*     */     } 
/*     */     
/* 197 */     psRangeEnc.enc_icdf(RateLevelIndex, SilkTables.silk_rate_levels_iCDF[signalType >> 1], 8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     for (i = 0; i < iter; i++) {
/* 207 */       if (nRshifts[i] == 0) {
/* 208 */         psRangeEnc.enc_icdf(sum_pulses[i], SilkTables.silk_pulses_per_block_iCDF[RateLevelIndex], 8);
/*     */       } else {
/* 210 */         psRangeEnc.enc_icdf(17, SilkTables.silk_pulses_per_block_iCDF[RateLevelIndex], 8);
/* 211 */         for (k = 0; k < nRshifts[i] - 1; k++) {
/* 212 */           psRangeEnc.enc_icdf(17, SilkTables.silk_pulses_per_block_iCDF[9], 8);
/*     */         }
/*     */         
/* 215 */         psRangeEnc.enc_icdf(sum_pulses[i], SilkTables.silk_pulses_per_block_iCDF[9], 8);
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
/* 226 */     for (i = 0; i < iter; i++) {
/* 227 */       if (sum_pulses[i] > 0) {
/* 228 */         ShellCoder.silk_shell_encoder(psRangeEnc, abs_pulses, i * 16);
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
/* 239 */     for (i = 0; i < iter; i++) {
/* 240 */       if (nRshifts[i] > 0) {
/* 241 */         int pulses_ptr = i * 16;
/* 242 */         int nLS = nRshifts[i] - 1;
/* 243 */         for (k = 0; k < 16; k++) {
/* 244 */           int abs_q = (byte)Inlines.silk_abs(pulses[pulses_ptr + k]);
/* 245 */           for (int j = nLS; j > 0; j--) {
/* 246 */             int m = Inlines.silk_RSHIFT(abs_q, j) & 0x1;
/* 247 */             psRangeEnc.enc_icdf(m, SilkTables.silk_lsb_iCDF, 8);
/*     */           } 
/* 249 */           int bit = abs_q & 0x1;
/* 250 */           psRangeEnc.enc_icdf(bit, SilkTables.silk_lsb_iCDF, 8);
/*     */         } 
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
/* 262 */     CodeSigns.silk_encode_signs(psRangeEnc, pulses, frame_length, signalType, quantOffsetType, sum_pulses);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\EncodePulses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */