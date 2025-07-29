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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Resampler
/*     */ {
/*     */   private static final int USE_silk_resampler_copy = 0;
/*     */   private static final int USE_silk_resampler_private_up2_HQ_wrapper = 1;
/*     */   private static final int USE_silk_resampler_private_IIR_FIR = 2;
/*     */   private static final int USE_silk_resampler_private_down_FIR = 3;
/*     */   private static final int ORDER_FIR = 4;
/*     */   
/*     */   private static int rateID(int R) {
/*  66 */     return ((R >> 12) - ((R > 16000) ? 1 : 0) >> ((R > 24000) ? 1 : 0)) - 1;
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
/*     */   static int silk_resampler_init(SilkResamplerState S, int Fs_Hz_in, int Fs_Hz_out, int forEnc) {
/*  85 */     S.Reset();
/*     */ 
/*     */     
/*  88 */     if (forEnc != 0) {
/*  89 */       if ((Fs_Hz_in != 8000 && Fs_Hz_in != 12000 && Fs_Hz_in != 16000 && Fs_Hz_in != 24000 && Fs_Hz_in != 48000) || (Fs_Hz_out != 8000 && Fs_Hz_out != 12000 && Fs_Hz_out != 16000)) {
/*     */         
/*  91 */         Inlines.OpusAssert(false);
/*  92 */         return -1;
/*     */       } 
/*  94 */       S.inputDelay = SilkTables.delay_matrix_enc[rateID(Fs_Hz_in)][rateID(Fs_Hz_out)];
/*     */     } else {
/*  96 */       if ((Fs_Hz_in != 8000 && Fs_Hz_in != 12000 && Fs_Hz_in != 16000) || (Fs_Hz_out != 8000 && Fs_Hz_out != 12000 && Fs_Hz_out != 16000 && Fs_Hz_out != 24000 && Fs_Hz_out != 48000)) {
/*     */         
/*  98 */         Inlines.OpusAssert(false);
/*  99 */         return -1;
/*     */       } 
/* 101 */       S.inputDelay = SilkTables.delay_matrix_dec[rateID(Fs_Hz_in)][rateID(Fs_Hz_out)];
/*     */     } 
/*     */     
/* 104 */     S.Fs_in_kHz = Inlines.silk_DIV32_16(Fs_Hz_in, 1000);
/* 105 */     S.Fs_out_kHz = Inlines.silk_DIV32_16(Fs_Hz_out, 1000);
/*     */ 
/*     */     
/* 108 */     S.batchSize = S.Fs_in_kHz * 10;
/*     */ 
/*     */     
/* 111 */     int up2x = 0;
/* 112 */     if (Fs_Hz_out > Fs_Hz_in) {
/*     */       
/* 114 */       if (Fs_Hz_out == Inlines.silk_MUL(Fs_Hz_in, 2)) {
/*     */ 
/*     */         
/* 117 */         S.resampler_function = 1;
/*     */       } else {
/*     */         
/* 120 */         S.resampler_function = 2;
/* 121 */         up2x = 1;
/*     */       } 
/* 123 */     } else if (Fs_Hz_out < Fs_Hz_in) {
/*     */       
/* 125 */       S.resampler_function = 3;
/* 126 */       if (Inlines.silk_MUL(Fs_Hz_out, 4) == Inlines.silk_MUL(Fs_Hz_in, 3)) {
/*     */         
/* 128 */         S.FIR_Fracs = 3;
/* 129 */         S.FIR_Order = 18;
/* 130 */         S.Coefs = SilkTables.silk_Resampler_3_4_COEFS;
/* 131 */       } else if (Inlines.silk_MUL(Fs_Hz_out, 3) == Inlines.silk_MUL(Fs_Hz_in, 2)) {
/*     */         
/* 133 */         S.FIR_Fracs = 2;
/* 134 */         S.FIR_Order = 18;
/* 135 */         S.Coefs = SilkTables.silk_Resampler_2_3_COEFS;
/* 136 */       } else if (Inlines.silk_MUL(Fs_Hz_out, 2) == Fs_Hz_in) {
/*     */         
/* 138 */         S.FIR_Fracs = 1;
/* 139 */         S.FIR_Order = 24;
/* 140 */         S.Coefs = SilkTables.silk_Resampler_1_2_COEFS;
/* 141 */       } else if (Inlines.silk_MUL(Fs_Hz_out, 3) == Fs_Hz_in) {
/*     */         
/* 143 */         S.FIR_Fracs = 1;
/* 144 */         S.FIR_Order = 36;
/* 145 */         S.Coefs = SilkTables.silk_Resampler_1_3_COEFS;
/* 146 */       } else if (Inlines.silk_MUL(Fs_Hz_out, 4) == Fs_Hz_in) {
/*     */         
/* 148 */         S.FIR_Fracs = 1;
/* 149 */         S.FIR_Order = 36;
/* 150 */         S.Coefs = SilkTables.silk_Resampler_1_4_COEFS;
/* 151 */       } else if (Inlines.silk_MUL(Fs_Hz_out, 6) == Fs_Hz_in) {
/*     */         
/* 153 */         S.FIR_Fracs = 1;
/* 154 */         S.FIR_Order = 36;
/* 155 */         S.Coefs = SilkTables.silk_Resampler_1_6_COEFS;
/*     */       } else {
/*     */         
/* 158 */         Inlines.OpusAssert(false);
/* 159 */         return -1;
/*     */       } 
/*     */     } else {
/*     */       
/* 163 */       S.resampler_function = 0;
/*     */     } 
/*     */ 
/*     */     
/* 167 */     S.invRatio_Q16 = Inlines.silk_LSHIFT32(Inlines.silk_DIV32(Inlines.silk_LSHIFT32(Fs_Hz_in, 14 + up2x), Fs_Hz_out), 2);
/*     */ 
/*     */     
/* 170 */     while (Inlines.silk_SMULWW(S.invRatio_Q16, Fs_Hz_out) < Inlines.silk_LSHIFT32(Fs_Hz_in, up2x)) {
/* 171 */       S.invRatio_Q16++;
/*     */     }
/*     */     
/* 174 */     return 0;
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
/*     */ 
/*     */   
/*     */   static int silk_resampler(SilkResamplerState S, short[] output, int output_ptr, short[] input, int input_ptr, int inLen) {
/* 196 */     Inlines.OpusAssert((inLen >= S.Fs_in_kHz));
/*     */     
/* 198 */     Inlines.OpusAssert((S.inputDelay <= S.Fs_in_kHz));
/*     */     
/* 200 */     int nSamples = S.Fs_in_kHz - S.inputDelay;
/*     */     
/* 202 */     short[] delayBufPtr = S.delayBuf;
/*     */ 
/*     */     
/* 205 */     System.arraycopy(input, input_ptr, delayBufPtr, S.inputDelay, nSamples);
/*     */     
/* 207 */     switch (S.resampler_function)
/*     */     { case 1:
/* 209 */         silk_resampler_private_up2_HQ(S.sIIR, output, output_ptr, delayBufPtr, 0, S.Fs_in_kHz);
/* 210 */         silk_resampler_private_up2_HQ(S.sIIR, output, output_ptr + S.Fs_out_kHz, input, input_ptr + nSamples, inLen - S.Fs_in_kHz);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 227 */         System.arraycopy(input, input_ptr + inLen - S.inputDelay, delayBufPtr, 0, S.inputDelay);
/*     */         
/* 229 */         return SilkError.SILK_NO_ERROR;case 2: silk_resampler_private_IIR_FIR(S, output, output_ptr, delayBufPtr, 0, S.Fs_in_kHz); silk_resampler_private_IIR_FIR(S, output, output_ptr + S.Fs_out_kHz, input, input_ptr + nSamples, inLen - S.Fs_in_kHz); System.arraycopy(input, input_ptr + inLen - S.inputDelay, delayBufPtr, 0, S.inputDelay); return SilkError.SILK_NO_ERROR;case 3: silk_resampler_private_down_FIR(S, output, output_ptr, delayBufPtr, 0, S.Fs_in_kHz); silk_resampler_private_down_FIR(S, output, output_ptr + S.Fs_out_kHz, input, input_ptr + nSamples, inLen - S.Fs_in_kHz); System.arraycopy(input, input_ptr + inLen - S.inputDelay, delayBufPtr, 0, S.inputDelay); return SilkError.SILK_NO_ERROR; }  System.arraycopy(delayBufPtr, 0, output, output_ptr, S.Fs_in_kHz); System.arraycopy(input, input_ptr + nSamples, output, output_ptr + S.Fs_out_kHz, inLen - S.Fs_in_kHz); System.arraycopy(input, input_ptr + inLen - S.inputDelay, delayBufPtr, 0, S.inputDelay); return SilkError.SILK_NO_ERROR;
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
/*     */   static void silk_resampler_down2(int[] S, short[] output, short[] input, int inLen) {
/* 244 */     int len2 = Inlines.silk_RSHIFT32(inLen, 1);
/*     */ 
/*     */     
/* 247 */     Inlines.OpusAssert(true);
/* 248 */     Inlines.OpusAssert(true);
/*     */ 
/*     */     
/* 251 */     for (int k = 0; k < len2; k++) {
/*     */       
/* 253 */       int in32 = Inlines.silk_LSHIFT(input[2 * k], 10);
/*     */ 
/*     */       
/* 256 */       int Y = Inlines.silk_SUB32(in32, S[0]);
/* 257 */       int X = Inlines.silk_SMLAWB(Y, Y, -25727);
/* 258 */       int out32 = Inlines.silk_ADD32(S[0], X);
/* 259 */       S[0] = Inlines.silk_ADD32(in32, X);
/*     */ 
/*     */       
/* 262 */       in32 = Inlines.silk_LSHIFT(input[2 * k + 1], 10);
/*     */ 
/*     */       
/* 265 */       Y = Inlines.silk_SUB32(in32, S[1]);
/* 266 */       X = Inlines.silk_SMULWB(Y, 9872);
/* 267 */       out32 = Inlines.silk_ADD32(out32, S[1]);
/* 268 */       out32 = Inlines.silk_ADD32(out32, X);
/* 269 */       S[1] = Inlines.silk_ADD32(in32, X);
/*     */ 
/*     */       
/* 272 */       output[k] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(out32, 11));
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
/*     */   static void silk_resampler_down2_3(int[] S, short[] output, short[] input, int inLen) {
/* 289 */     int nSamplesIn, buf[] = new int[484];
/*     */     
/* 291 */     int input_ptr = 0;
/* 292 */     int output_ptr = 0;
/*     */ 
/*     */     
/* 295 */     System.arraycopy(S, 0, buf, 0, 4);
/*     */ 
/*     */     
/*     */     while (true) {
/* 299 */       nSamplesIn = Inlines.silk_min(inLen, 480);
/*     */ 
/*     */       
/* 302 */       silk_resampler_private_AR2(S, 4, buf, 4, input, input_ptr, SilkTables.silk_Resampler_2_3_COEFS_LQ, nSamplesIn);
/*     */ 
/*     */ 
/*     */       
/* 306 */       int buf_ptr = 0;
/* 307 */       int counter = nSamplesIn;
/* 308 */       while (counter > 2) {
/*     */         
/* 310 */         int res_Q6 = Inlines.silk_SMULWB(buf[buf_ptr], SilkTables.silk_Resampler_2_3_COEFS_LQ[2]);
/* 311 */         res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 1], SilkTables.silk_Resampler_2_3_COEFS_LQ[3]);
/* 312 */         res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 2], SilkTables.silk_Resampler_2_3_COEFS_LQ[5]);
/* 313 */         res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 3], SilkTables.silk_Resampler_2_3_COEFS_LQ[4]);
/*     */ 
/*     */         
/* 316 */         output[output_ptr++] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(res_Q6, 6));
/*     */         
/* 318 */         res_Q6 = Inlines.silk_SMULWB(buf[buf_ptr + 1], SilkTables.silk_Resampler_2_3_COEFS_LQ[4]);
/* 319 */         res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 2], SilkTables.silk_Resampler_2_3_COEFS_LQ[5]);
/* 320 */         res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 3], SilkTables.silk_Resampler_2_3_COEFS_LQ[3]);
/* 321 */         res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 4], SilkTables.silk_Resampler_2_3_COEFS_LQ[2]);
/*     */ 
/*     */         
/* 324 */         output[output_ptr++] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(res_Q6, 6));
/*     */         
/* 326 */         buf_ptr += 3;
/* 327 */         counter -= 3;
/*     */       } 
/*     */       
/* 330 */       input_ptr += nSamplesIn;
/* 331 */       inLen -= nSamplesIn;
/*     */       
/* 333 */       if (inLen > 0) {
/*     */         
/* 335 */         System.arraycopy(buf, nSamplesIn, buf, 0, 4);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/* 342 */     System.arraycopy(buf, nSamplesIn, S, 0, 4);
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
/*     */ 
/*     */   
/*     */   static void silk_resampler_private_AR2(int[] S, int S_ptr, int[] out_Q8, int out_Q8_ptr, short[] input, int input_ptr, short[] A_Q14, int len) {
/* 364 */     for (int k = 0; k < len; k++) {
/* 365 */       int out32 = Inlines.silk_ADD_LSHIFT32(S[S_ptr], input[input_ptr + k], 8);
/* 366 */       out_Q8[out_Q8_ptr + k] = out32;
/* 367 */       out32 = Inlines.silk_LSHIFT(out32, 2);
/* 368 */       S[S_ptr] = Inlines.silk_SMLAWB(S[S_ptr + 1], out32, A_Q14[0]);
/* 369 */       S[S_ptr + 1] = Inlines.silk_SMULWB(out32, A_Q14[1]);
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
/*     */   static int silk_resampler_private_down_FIR_INTERPOL(short[] output, int output_ptr, int[] buf, short[] FIR_Coefs, int FIR_Coefs_ptr, int FIR_Order, int FIR_Fracs, int max_index_Q16, int index_increment_Q16) {
/*     */     int index_Q16;
/* 388 */     switch (FIR_Order)
/*     */     { case 18:
/* 390 */         for (index_Q16 = 0; index_Q16 < max_index_Q16; index_Q16 += index_increment_Q16) {
/*     */           
/* 392 */           int buf_ptr = Inlines.silk_RSHIFT(index_Q16, 16);
/*     */ 
/*     */           
/* 395 */           int interpol_ind = Inlines.silk_SMULWB(index_Q16 & 0xFFFF, FIR_Fracs);
/*     */ 
/*     */           
/* 398 */           int interpol_ptr = FIR_Coefs_ptr + 9 * interpol_ind;
/* 399 */           int res_Q6 = Inlines.silk_SMULWB(buf[buf_ptr + 0], FIR_Coefs[interpol_ptr + 0]);
/* 400 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 1], FIR_Coefs[interpol_ptr + 1]);
/* 401 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 2], FIR_Coefs[interpol_ptr + 2]);
/* 402 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 3], FIR_Coefs[interpol_ptr + 3]);
/* 403 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 4], FIR_Coefs[interpol_ptr + 4]);
/* 404 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 5], FIR_Coefs[interpol_ptr + 5]);
/* 405 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 6], FIR_Coefs[interpol_ptr + 6]);
/* 406 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 7], FIR_Coefs[interpol_ptr + 7]);
/* 407 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 8], FIR_Coefs[interpol_ptr + 8]);
/* 408 */           interpol_ptr = FIR_Coefs_ptr + 9 * (FIR_Fracs - 1 - interpol_ind);
/* 409 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 17], FIR_Coefs[interpol_ptr + 0]);
/* 410 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 16], FIR_Coefs[interpol_ptr + 1]);
/* 411 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 15], FIR_Coefs[interpol_ptr + 2]);
/* 412 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 14], FIR_Coefs[interpol_ptr + 3]);
/* 413 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 13], FIR_Coefs[interpol_ptr + 4]);
/* 414 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 12], FIR_Coefs[interpol_ptr + 5]);
/* 415 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 11], FIR_Coefs[interpol_ptr + 6]);
/* 416 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 10], FIR_Coefs[interpol_ptr + 7]);
/* 417 */           res_Q6 = Inlines.silk_SMLAWB(res_Q6, buf[buf_ptr + 9], FIR_Coefs[interpol_ptr + 8]);
/*     */ 
/*     */           
/* 420 */           output[output_ptr++] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(res_Q6, 6));
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 480 */         return output_ptr;case 24: for (index_Q16 = 0; index_Q16 < max_index_Q16; index_Q16 += index_increment_Q16) { int buf_ptr = Inlines.silk_RSHIFT(index_Q16, 16); int res_Q6 = Inlines.silk_SMULWB(Inlines.silk_ADD32(buf[buf_ptr + 0], buf[buf_ptr + 23]), FIR_Coefs[FIR_Coefs_ptr + 0]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 1], buf[buf_ptr + 22]), FIR_Coefs[FIR_Coefs_ptr + 1]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 2], buf[buf_ptr + 21]), FIR_Coefs[FIR_Coefs_ptr + 2]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 3], buf[buf_ptr + 20]), FIR_Coefs[FIR_Coefs_ptr + 3]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 4], buf[buf_ptr + 19]), FIR_Coefs[FIR_Coefs_ptr + 4]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 5], buf[buf_ptr + 18]), FIR_Coefs[FIR_Coefs_ptr + 5]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 6], buf[buf_ptr + 17]), FIR_Coefs[FIR_Coefs_ptr + 6]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 7], buf[buf_ptr + 16]), FIR_Coefs[FIR_Coefs_ptr + 7]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 8], buf[buf_ptr + 15]), FIR_Coefs[FIR_Coefs_ptr + 8]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 9], buf[buf_ptr + 14]), FIR_Coefs[FIR_Coefs_ptr + 9]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 10], buf[buf_ptr + 13]), FIR_Coefs[FIR_Coefs_ptr + 10]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 11], buf[buf_ptr + 12]), FIR_Coefs[FIR_Coefs_ptr + 11]); output[output_ptr++] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(res_Q6, 6)); }  return output_ptr;case 36: for (index_Q16 = 0; index_Q16 < max_index_Q16; index_Q16 += index_increment_Q16) { int buf_ptr = Inlines.silk_RSHIFT(index_Q16, 16); int res_Q6 = Inlines.silk_SMULWB(Inlines.silk_ADD32(buf[buf_ptr + 0], buf[buf_ptr + 35]), FIR_Coefs[FIR_Coefs_ptr + 0]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 1], buf[buf_ptr + 34]), FIR_Coefs[FIR_Coefs_ptr + 1]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 2], buf[buf_ptr + 33]), FIR_Coefs[FIR_Coefs_ptr + 2]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 3], buf[buf_ptr + 32]), FIR_Coefs[FIR_Coefs_ptr + 3]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 4], buf[buf_ptr + 31]), FIR_Coefs[FIR_Coefs_ptr + 4]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 5], buf[buf_ptr + 30]), FIR_Coefs[FIR_Coefs_ptr + 5]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 6], buf[buf_ptr + 29]), FIR_Coefs[FIR_Coefs_ptr + 6]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 7], buf[buf_ptr + 28]), FIR_Coefs[FIR_Coefs_ptr + 7]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 8], buf[buf_ptr + 27]), FIR_Coefs[FIR_Coefs_ptr + 8]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 9], buf[buf_ptr + 26]), FIR_Coefs[FIR_Coefs_ptr + 9]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 10], buf[buf_ptr + 25]), FIR_Coefs[FIR_Coefs_ptr + 10]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 11], buf[buf_ptr + 24]), FIR_Coefs[FIR_Coefs_ptr + 11]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 12], buf[buf_ptr + 23]), FIR_Coefs[FIR_Coefs_ptr + 12]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 13], buf[buf_ptr + 22]), FIR_Coefs[FIR_Coefs_ptr + 13]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 14], buf[buf_ptr + 21]), FIR_Coefs[FIR_Coefs_ptr + 14]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 15], buf[buf_ptr + 20]), FIR_Coefs[FIR_Coefs_ptr + 15]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 16], buf[buf_ptr + 19]), FIR_Coefs[FIR_Coefs_ptr + 16]); res_Q6 = Inlines.silk_SMLAWB(res_Q6, Inlines.silk_ADD32(buf[buf_ptr + 17], buf[buf_ptr + 18]), FIR_Coefs[FIR_Coefs_ptr + 17]); output[output_ptr++] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(res_Q6, 6)); }  return output_ptr; }  Inlines.OpusAssert(false); return output_ptr;
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
/*     */   static void silk_resampler_private_down_FIR(SilkResamplerState S, short[] output, int output_ptr, short[] input, int input_ptr, int inLen) {
/* 499 */     int nSamplesIn, buf[] = new int[S.batchSize + S.FIR_Order];
/*     */ 
/*     */     
/* 502 */     System.arraycopy(S.sFIR_i32, 0, buf, 0, S.FIR_Order);
/*     */ 
/*     */     
/* 505 */     int index_increment_Q16 = S.invRatio_Q16;
/*     */     while (true) {
/* 507 */       nSamplesIn = Inlines.silk_min(inLen, S.batchSize);
/*     */ 
/*     */       
/* 510 */       silk_resampler_private_AR2(S.sIIR, 0, buf, S.FIR_Order, input, input_ptr, S.Coefs, nSamplesIn);
/*     */       
/* 512 */       int max_index_Q16 = Inlines.silk_LSHIFT32(nSamplesIn, 16);
/*     */ 
/*     */       
/* 515 */       output_ptr = silk_resampler_private_down_FIR_INTERPOL(output, output_ptr, buf, S.Coefs, 2, S.FIR_Order, S.FIR_Fracs, max_index_Q16, index_increment_Q16);
/*     */ 
/*     */       
/* 518 */       input_ptr += nSamplesIn;
/* 519 */       inLen -= nSamplesIn;
/*     */       
/* 521 */       if (inLen > 1) {
/*     */         
/* 523 */         System.arraycopy(buf, nSamplesIn, buf, 0, S.FIR_Order);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/* 530 */     System.arraycopy(buf, nSamplesIn, S.sFIR_i32, 0, S.FIR_Order);
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
/*     */   static int silk_resampler_private_IIR_FIR_INTERPOL(short[] output, int output_ptr, short[] buf, int max_index_Q16, int index_increment_Q16) {
/*     */     int index_Q16;
/* 544 */     for (index_Q16 = 0; index_Q16 < max_index_Q16; index_Q16 += index_increment_Q16) {
/* 545 */       int table_index = Inlines.silk_SMULWB(index_Q16 & 0xFFFF, 12);
/* 546 */       int buf_ptr = index_Q16 >> 16;
/*     */       
/* 548 */       int res_Q15 = Inlines.silk_SMULBB(buf[buf_ptr], SilkTables.silk_resampler_frac_FIR_12[table_index][0]);
/* 549 */       res_Q15 = Inlines.silk_SMLABB(res_Q15, buf[buf_ptr + 1], SilkTables.silk_resampler_frac_FIR_12[table_index][1]);
/* 550 */       res_Q15 = Inlines.silk_SMLABB(res_Q15, buf[buf_ptr + 2], SilkTables.silk_resampler_frac_FIR_12[table_index][2]);
/* 551 */       res_Q15 = Inlines.silk_SMLABB(res_Q15, buf[buf_ptr + 3], SilkTables.silk_resampler_frac_FIR_12[table_index][3]);
/* 552 */       res_Q15 = Inlines.silk_SMLABB(res_Q15, buf[buf_ptr + 4], SilkTables.silk_resampler_frac_FIR_12[11 - table_index][3]);
/* 553 */       res_Q15 = Inlines.silk_SMLABB(res_Q15, buf[buf_ptr + 5], SilkTables.silk_resampler_frac_FIR_12[11 - table_index][2]);
/* 554 */       res_Q15 = Inlines.silk_SMLABB(res_Q15, buf[buf_ptr + 6], SilkTables.silk_resampler_frac_FIR_12[11 - table_index][1]);
/* 555 */       res_Q15 = Inlines.silk_SMLABB(res_Q15, buf[buf_ptr + 7], SilkTables.silk_resampler_frac_FIR_12[11 - table_index][0]);
/* 556 */       output[output_ptr++] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(res_Q15, 15));
/*     */     } 
/* 558 */     return output_ptr;
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
/*     */   static void silk_resampler_private_IIR_FIR(SilkResamplerState S, short[] output, int output_ptr, short[] input, int input_ptr, int inLen) {
/*     */     int nSamplesIn;
/* 578 */     short[] buf = new short[2 * S.batchSize + 8];
/*     */ 
/*     */     
/* 581 */     System.arraycopy(S.sFIR_i16, 0, buf, 0, 8);
/*     */ 
/*     */     
/* 584 */     int index_increment_Q16 = S.invRatio_Q16;
/*     */     while (true) {
/* 586 */       nSamplesIn = Inlines.silk_min(inLen, S.batchSize);
/*     */ 
/*     */       
/* 589 */       silk_resampler_private_up2_HQ(S.sIIR, buf, 8, input, input_ptr, nSamplesIn);
/*     */       
/* 591 */       int max_index_Q16 = Inlines.silk_LSHIFT32(nSamplesIn, 17);
/*     */       
/* 593 */       output_ptr = silk_resampler_private_IIR_FIR_INTERPOL(output, output_ptr, buf, max_index_Q16, index_increment_Q16);
/* 594 */       input_ptr += nSamplesIn;
/* 595 */       inLen -= nSamplesIn;
/*     */       
/* 597 */       if (inLen > 0) {
/*     */         
/* 599 */         System.arraycopy(buf, nSamplesIn << 1, buf, 0, 8);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/* 606 */     System.arraycopy(buf, nSamplesIn << 1, S.sFIR_i16, 0, 8);
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
/*     */ 
/*     */   
/*     */   static void silk_resampler_private_up2_HQ(int[] S, short[] output, int output_ptr, short[] input, int input_ptr, int len) {
/* 628 */     Inlines.OpusAssert((SilkTables.silk_resampler_up2_hq_0[0] > 0));
/* 629 */     Inlines.OpusAssert((SilkTables.silk_resampler_up2_hq_0[1] > 0));
/* 630 */     Inlines.OpusAssert((SilkTables.silk_resampler_up2_hq_0[2] < 0));
/* 631 */     Inlines.OpusAssert((SilkTables.silk_resampler_up2_hq_1[0] > 0));
/* 632 */     Inlines.OpusAssert((SilkTables.silk_resampler_up2_hq_1[1] > 0));
/* 633 */     Inlines.OpusAssert((SilkTables.silk_resampler_up2_hq_1[2] < 0));
/*     */ 
/*     */     
/* 636 */     for (int k = 0; k < len; k++) {
/*     */       
/* 638 */       int in32 = Inlines.silk_LSHIFT(input[input_ptr + k], 10);
/*     */ 
/*     */       
/* 641 */       int Y = Inlines.silk_SUB32(in32, S[0]);
/* 642 */       int X = Inlines.silk_SMULWB(Y, SilkTables.silk_resampler_up2_hq_0[0]);
/* 643 */       int out32_1 = Inlines.silk_ADD32(S[0], X);
/* 644 */       S[0] = Inlines.silk_ADD32(in32, X);
/*     */ 
/*     */       
/* 647 */       Y = Inlines.silk_SUB32(out32_1, S[1]);
/* 648 */       X = Inlines.silk_SMULWB(Y, SilkTables.silk_resampler_up2_hq_0[1]);
/* 649 */       int out32_2 = Inlines.silk_ADD32(S[1], X);
/* 650 */       S[1] = Inlines.silk_ADD32(out32_1, X);
/*     */ 
/*     */       
/* 653 */       Y = Inlines.silk_SUB32(out32_2, S[2]);
/* 654 */       X = Inlines.silk_SMLAWB(Y, Y, SilkTables.silk_resampler_up2_hq_0[2]);
/* 655 */       out32_1 = Inlines.silk_ADD32(S[2], X);
/* 656 */       S[2] = Inlines.silk_ADD32(out32_2, X);
/*     */ 
/*     */       
/* 659 */       output[output_ptr + 2 * k] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(out32_1, 10));
/*     */ 
/*     */       
/* 662 */       Y = Inlines.silk_SUB32(in32, S[3]);
/* 663 */       X = Inlines.silk_SMULWB(Y, SilkTables.silk_resampler_up2_hq_1[0]);
/* 664 */       out32_1 = Inlines.silk_ADD32(S[3], X);
/* 665 */       S[3] = Inlines.silk_ADD32(in32, X);
/*     */ 
/*     */       
/* 668 */       Y = Inlines.silk_SUB32(out32_1, S[4]);
/* 669 */       X = Inlines.silk_SMULWB(Y, SilkTables.silk_resampler_up2_hq_1[1]);
/* 670 */       out32_2 = Inlines.silk_ADD32(S[4], X);
/* 671 */       S[4] = Inlines.silk_ADD32(out32_1, X);
/*     */ 
/*     */       
/* 674 */       Y = Inlines.silk_SUB32(out32_2, S[5]);
/* 675 */       X = Inlines.silk_SMLAWB(Y, Y, SilkTables.silk_resampler_up2_hq_1[2]);
/* 676 */       out32_1 = Inlines.silk_ADD32(S[5], X);
/* 677 */       S[5] = Inlines.silk_ADD32(out32_2, X);
/*     */ 
/*     */       
/* 680 */       output[output_ptr + 2 * k + 1] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(out32_1, 10));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Resampler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */