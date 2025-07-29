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
/*     */ class Filters
/*     */ {
/*     */   private static final short A_fb1_20 = 10788;
/*     */   private static final short A_fb1_21 = -24290;
/*     */   private static final int QA = 24;
/*     */   private static final int A_LIMIT = 16773022;
/*     */   
/*     */   static void silk_warped_LPC_analysis_filter(int[] state, int[] res_Q2, short[] coef_Q13, int coef_Q13_ptr, short[] input, int input_ptr, short lambda_Q16, int length, int order) {
/*  51 */     Inlines.OpusAssert(((order & 0x1) == 0));
/*     */     
/*  53 */     for (int n = 0; n < length; n++) {
/*     */       
/*  55 */       int tmp2 = Inlines.silk_SMLAWB(state[0], state[1], lambda_Q16);
/*  56 */       state[0] = Inlines.silk_LSHIFT(input[input_ptr + n], 14);
/*     */       
/*  58 */       int tmp1 = Inlines.silk_SMLAWB(state[1], state[2] - tmp2, lambda_Q16);
/*  59 */       state[1] = tmp2;
/*  60 */       int acc_Q11 = Inlines.silk_RSHIFT(order, 1);
/*  61 */       acc_Q11 = Inlines.silk_SMLAWB(acc_Q11, tmp2, coef_Q13[coef_Q13_ptr]);
/*     */       
/*  63 */       for (int i = 2; i < order; i += 2) {
/*     */         
/*  65 */         tmp2 = Inlines.silk_SMLAWB(state[i], state[i + 1] - tmp1, lambda_Q16);
/*  66 */         state[i] = tmp1;
/*  67 */         acc_Q11 = Inlines.silk_SMLAWB(acc_Q11, tmp1, coef_Q13[coef_Q13_ptr + i - 1]);
/*     */         
/*  69 */         tmp1 = Inlines.silk_SMLAWB(state[i + 1], state[i + 2] - tmp2, lambda_Q16);
/*  70 */         state[i + 1] = tmp2;
/*  71 */         acc_Q11 = Inlines.silk_SMLAWB(acc_Q11, tmp2, coef_Q13[coef_Q13_ptr + i]);
/*     */       } 
/*  73 */       state[order] = tmp1;
/*  74 */       acc_Q11 = Inlines.silk_SMLAWB(acc_Q11, tmp1, coef_Q13[coef_Q13_ptr + order - 1]);
/*  75 */       res_Q2[n] = Inlines.silk_LSHIFT(input[input_ptr + n], 2) - Inlines.silk_RSHIFT_ROUND(acc_Q11, 9);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_prefilter(SilkChannelEncoder psEnc, SilkEncoderControl psEncCtrl, int[] xw_Q3, short[] x, int x_ptr) {
/*  86 */     SilkPrefilterState P = psEnc.sPrefilt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     short[] B_Q10 = new short[2];
/*     */ 
/*     */     
/*  99 */     int px = x_ptr;
/* 100 */     int pxw_Q3 = 0;
/* 101 */     int lag = P.lagPrev;
/* 102 */     int[] x_filt_Q12 = new int[psEnc.subfr_length];
/* 103 */     int[] st_res_Q2 = new int[psEnc.subfr_length];
/* 104 */     for (int k = 0; k < psEnc.nb_subfr; k++) {
/*     */       
/* 106 */       if (psEnc.indices.signalType == 2) {
/* 107 */         lag = psEncCtrl.pitchL[k];
/*     */       }
/*     */ 
/*     */       
/* 111 */       int HarmShapeGain_Q12 = Inlines.silk_SMULWB(psEncCtrl.HarmShapeGain_Q14[k], 16384 - psEncCtrl.HarmBoost_Q14[k]);
/* 112 */       Inlines.OpusAssert((HarmShapeGain_Q12 >= 0));
/* 113 */       int HarmShapeFIRPacked_Q12 = Inlines.silk_RSHIFT(HarmShapeGain_Q12, 2);
/* 114 */       HarmShapeFIRPacked_Q12 |= Inlines.silk_LSHIFT(Inlines.silk_RSHIFT(HarmShapeGain_Q12, 1), 16);
/* 115 */       int Tilt_Q14 = psEncCtrl.Tilt_Q14[k];
/* 116 */       int LF_shp_Q14 = psEncCtrl.LF_shp_Q14[k];
/* 117 */       int AR1_shp_Q13 = k * 16;
/*     */ 
/*     */       
/* 120 */       silk_warped_LPC_analysis_filter(P.sAR_shp, st_res_Q2, psEncCtrl.AR1_Q13, AR1_shp_Q13, x, px, (short)psEnc.warping_Q16, psEnc.subfr_length, psEnc.shapingLPCOrder);
/*     */ 
/*     */ 
/*     */       
/* 124 */       B_Q10[0] = (short)Inlines.silk_RSHIFT_ROUND(psEncCtrl.GainsPre_Q14[k], 4);
/* 125 */       int tmp_32 = Inlines.silk_SMLABB(3355443, psEncCtrl.HarmBoost_Q14[k], HarmShapeGain_Q12);
/*     */       
/* 127 */       tmp_32 = Inlines.silk_SMLABB(tmp_32, psEncCtrl.coding_quality_Q14, 410);
/*     */       
/* 129 */       tmp_32 = Inlines.silk_SMULWB(tmp_32, -psEncCtrl.GainsPre_Q14[k]);
/*     */       
/* 131 */       tmp_32 = Inlines.silk_RSHIFT_ROUND(tmp_32, 14);
/*     */       
/* 133 */       B_Q10[1] = (short)Inlines.silk_SAT16(tmp_32);
/* 134 */       x_filt_Q12[0] = Inlines.silk_MLA(Inlines.silk_MUL(st_res_Q2[0], B_Q10[0]), P.sHarmHP_Q2, B_Q10[1]);
/* 135 */       for (int j = 1; j < psEnc.subfr_length; j++) {
/* 136 */         x_filt_Q12[j] = Inlines.silk_MLA(Inlines.silk_MUL(st_res_Q2[j], B_Q10[0]), st_res_Q2[j - 1], B_Q10[1]);
/*     */       }
/* 138 */       P.sHarmHP_Q2 = st_res_Q2[psEnc.subfr_length - 1];
/*     */       
/* 140 */       silk_prefilt(P, x_filt_Q12, xw_Q3, pxw_Q3, HarmShapeFIRPacked_Q12, Tilt_Q14, LF_shp_Q14, lag, psEnc.subfr_length);
/*     */       
/* 142 */       px += psEnc.subfr_length;
/* 143 */       pxw_Q3 += psEnc.subfr_length;
/*     */     } 
/*     */     
/* 146 */     P.lagPrev = psEncCtrl.pitchL[psEnc.nb_subfr - 1];
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
/*     */   static void silk_prefilt(SilkPrefilterState P, int[] st_res_Q12, int[] xw_Q3, int xw_Q3_ptr, int HarmShapeFIRPacked_Q12, int Tilt_Q14, int LF_shp_Q14, int lag, int length) {
/* 168 */     short[] LTP_shp_buf = P.sLTP_shp;
/* 169 */     int LTP_shp_buf_idx = P.sLTP_shp_buf_idx;
/* 170 */     int sLF_AR_shp_Q12 = P.sLF_AR_shp_Q12;
/* 171 */     int sLF_MA_shp_Q12 = P.sLF_MA_shp_Q12;
/*     */     
/* 173 */     for (int i = 0; i < length; i++) {
/* 174 */       int n_LTP_Q12; if (lag > 0) {
/*     */         
/* 176 */         Inlines.OpusAssert(true);
/* 177 */         int idx = lag + LTP_shp_buf_idx;
/* 178 */         n_LTP_Q12 = Inlines.silk_SMULBB(LTP_shp_buf[idx - 1 - 1 & 0x1FF], HarmShapeFIRPacked_Q12);
/* 179 */         n_LTP_Q12 = Inlines.silk_SMLABT(n_LTP_Q12, LTP_shp_buf[idx - 1 & 0x1FF], HarmShapeFIRPacked_Q12);
/* 180 */         n_LTP_Q12 = Inlines.silk_SMLABB(n_LTP_Q12, LTP_shp_buf[idx - 1 + 1 & 0x1FF], HarmShapeFIRPacked_Q12);
/*     */       } else {
/* 182 */         n_LTP_Q12 = 0;
/*     */       } 
/*     */       
/* 185 */       int n_Tilt_Q10 = Inlines.silk_SMULWB(sLF_AR_shp_Q12, Tilt_Q14);
/* 186 */       int n_LF_Q10 = Inlines.silk_SMLAWB(Inlines.silk_SMULWT(sLF_AR_shp_Q12, LF_shp_Q14), sLF_MA_shp_Q12, LF_shp_Q14);
/*     */       
/* 188 */       sLF_AR_shp_Q12 = Inlines.silk_SUB32(st_res_Q12[i], Inlines.silk_LSHIFT(n_Tilt_Q10, 2));
/* 189 */       sLF_MA_shp_Q12 = Inlines.silk_SUB32(sLF_AR_shp_Q12, Inlines.silk_LSHIFT(n_LF_Q10, 2));
/*     */       
/* 191 */       LTP_shp_buf_idx = LTP_shp_buf_idx - 1 & 0x1FF;
/* 192 */       LTP_shp_buf[LTP_shp_buf_idx] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(sLF_MA_shp_Q12, 12));
/*     */       
/* 194 */       xw_Q3[xw_Q3_ptr + i] = Inlines.silk_RSHIFT_ROUND(Inlines.silk_SUB32(sLF_MA_shp_Q12, n_LTP_Q12), 9);
/*     */     } 
/*     */ 
/*     */     
/* 198 */     P.sLF_AR_shp_Q12 = sLF_AR_shp_Q12;
/* 199 */     P.sLF_MA_shp_Q12 = sLF_MA_shp_Q12;
/* 200 */     P.sLTP_shp_buf_idx = LTP_shp_buf_idx;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_biquad_alt(short[] input, int input_ptr, int[] B_Q28, int[] A_Q28, int[] S, short[] output, int output_ptr, int len, int stride) {
/* 228 */     int A0_L_Q28 = -A_Q28[0] & 0x3FFF;
/*     */     
/* 230 */     int A0_U_Q28 = Inlines.silk_RSHIFT(-A_Q28[0], 14);
/*     */     
/* 232 */     int A1_L_Q28 = -A_Q28[1] & 0x3FFF;
/*     */     
/* 234 */     int A1_U_Q28 = Inlines.silk_RSHIFT(-A_Q28[1], 14);
/*     */ 
/*     */     
/* 237 */     for (int k = 0; k < len; k++) {
/*     */       
/* 239 */       int inval = input[input_ptr + k * stride];
/* 240 */       int out32_Q14 = Inlines.silk_LSHIFT(Inlines.silk_SMLAWB(S[0], B_Q28[0], inval), 2);
/*     */       
/* 242 */       S[0] = S[1] + Inlines.silk_RSHIFT_ROUND(Inlines.silk_SMULWB(out32_Q14, A0_L_Q28), 14);
/* 243 */       S[0] = Inlines.silk_SMLAWB(S[0], out32_Q14, A0_U_Q28);
/* 244 */       S[0] = Inlines.silk_SMLAWB(S[0], B_Q28[1], inval);
/*     */       
/* 246 */       S[1] = Inlines.silk_RSHIFT_ROUND(Inlines.silk_SMULWB(out32_Q14, A1_L_Q28), 14);
/* 247 */       S[1] = Inlines.silk_SMLAWB(S[1], out32_Q14, A1_U_Q28);
/* 248 */       S[1] = Inlines.silk_SMLAWB(S[1], B_Q28[2], inval);
/*     */ 
/*     */       
/* 251 */       output[output_ptr + k * stride] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT(out32_Q14 + 16384 - 1, 14));
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
/*     */   static void silk_biquad_alt(short[] input, int input_ptr, int[] B_Q28, int[] A_Q28, int[] S, int S_ptr, short[] output, int output_ptr, int len, int stride) {
/* 271 */     int A0_L_Q28 = -A_Q28[0] & 0x3FFF;
/*     */     
/* 273 */     int A0_U_Q28 = Inlines.silk_RSHIFT(-A_Q28[0], 14);
/*     */     
/* 275 */     int A1_L_Q28 = -A_Q28[1] & 0x3FFF;
/*     */     
/* 277 */     int A1_U_Q28 = Inlines.silk_RSHIFT(-A_Q28[1], 14);
/*     */ 
/*     */     
/* 280 */     for (int k = 0; k < len; k++) {
/* 281 */       int s1 = S_ptr + 1;
/*     */       
/* 283 */       int inval = input[input_ptr + k * stride];
/* 284 */       int out32_Q14 = Inlines.silk_LSHIFT(Inlines.silk_SMLAWB(S[S_ptr], B_Q28[0], inval), 2);
/*     */       
/* 286 */       S[S_ptr] = S[s1] + Inlines.silk_RSHIFT_ROUND(Inlines.silk_SMULWB(out32_Q14, A0_L_Q28), 14);
/* 287 */       S[S_ptr] = Inlines.silk_SMLAWB(S[S_ptr], out32_Q14, A0_U_Q28);
/* 288 */       S[S_ptr] = Inlines.silk_SMLAWB(S[S_ptr], B_Q28[1], inval);
/*     */       
/* 290 */       S[s1] = Inlines.silk_RSHIFT_ROUND(Inlines.silk_SMULWB(out32_Q14, A1_L_Q28), 14);
/* 291 */       S[s1] = Inlines.silk_SMLAWB(S[s1], out32_Q14, A1_U_Q28);
/* 292 */       S[s1] = Inlines.silk_SMLAWB(S[s1], B_Q28[2], inval);
/*     */ 
/*     */       
/* 295 */       output[output_ptr + k * stride] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT(out32_Q14 + 16384 - 1, 14));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_ana_filt_bank_1(short[] input, int input_ptr, int[] S, short[] outL, short[] outH, int outH_ptr, int N) {
/* 321 */     int N2 = Inlines.silk_RSHIFT(N, 1);
/*     */ 
/*     */ 
/*     */     
/* 325 */     for (int k = 0; k < N2; k++) {
/*     */       
/* 327 */       int in32 = Inlines.silk_LSHIFT(input[input_ptr + 2 * k], 10);
/*     */ 
/*     */       
/* 330 */       int Y = Inlines.silk_SUB32(in32, S[0]);
/* 331 */       int X = Inlines.silk_SMLAWB(Y, Y, -24290);
/* 332 */       int out_1 = Inlines.silk_ADD32(S[0], X);
/* 333 */       S[0] = Inlines.silk_ADD32(in32, X);
/*     */ 
/*     */       
/* 336 */       in32 = Inlines.silk_LSHIFT(input[input_ptr + 2 * k + 1], 10);
/*     */ 
/*     */       
/* 339 */       Y = Inlines.silk_SUB32(in32, S[1]);
/* 340 */       X = Inlines.silk_SMULWB(Y, 10788);
/* 341 */       int out_2 = Inlines.silk_ADD32(S[1], X);
/* 342 */       S[1] = Inlines.silk_ADD32(in32, X);
/*     */ 
/*     */       
/* 345 */       outL[k] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(Inlines.silk_ADD32(out_2, out_1), 11));
/* 346 */       outH[outH_ptr + k] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(Inlines.silk_SUB32(out_2, out_1), 11));
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
/*     */   static void silk_bwexpander_32(int[] ar, int d, int chirp_Q16) {
/* 358 */     int chirp_minus_one_Q16 = chirp_Q16 - 65536;
/*     */     
/* 360 */     for (int i = 0; i < d - 1; i++) {
/* 361 */       ar[i] = Inlines.silk_SMULWW(chirp_Q16, ar[i]);
/* 362 */       chirp_Q16 += Inlines.silk_RSHIFT_ROUND(Inlines.silk_MUL(chirp_Q16, chirp_minus_one_Q16), 16);
/*     */     } 
/*     */     
/* 365 */     ar[d - 1] = Inlines.silk_SMULWW(chirp_Q16, ar[d - 1]);
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
/*     */   static void silk_LP_interpolate_filter_taps(int[] B_Q28, int[] A_Q28, int ind, int fac_Q16) {
/* 385 */     if (ind < 4) {
/* 386 */       if (fac_Q16 > 0) {
/* 387 */         if (fac_Q16 < 32768)
/*     */         {
/*     */           
/* 390 */           for (int nb = 0; nb < 3; nb++) {
/* 391 */             B_Q28[nb] = Inlines.silk_SMLAWB(SilkTables.silk_Transition_LP_B_Q28[ind][nb], SilkTables.silk_Transition_LP_B_Q28[ind + 1][nb] - SilkTables.silk_Transition_LP_B_Q28[ind][nb], fac_Q16);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 398 */           for (int na = 0; na < 2; na++) {
/* 399 */             A_Q28[na] = Inlines.silk_SMLAWB(SilkTables.silk_Transition_LP_A_Q28[ind][na], SilkTables.silk_Transition_LP_A_Q28[ind + 1][na] - SilkTables.silk_Transition_LP_A_Q28[ind][na], fac_Q16);
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */         else
/*     */         {
/*     */           
/* 407 */           Inlines.OpusAssert((fac_Q16 - 65536 == Inlines.silk_SAT16(fac_Q16 - 65536)));
/*     */ 
/*     */           
/* 410 */           for (int nb = 0; nb < 3; nb++) {
/* 411 */             B_Q28[nb] = Inlines.silk_SMLAWB(SilkTables.silk_Transition_LP_B_Q28[ind + 1][nb], SilkTables.silk_Transition_LP_B_Q28[ind + 1][nb] - SilkTables.silk_Transition_LP_B_Q28[ind][nb], fac_Q16 - 65536);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 418 */           for (int na = 0; na < 2; na++) {
/* 419 */             A_Q28[na] = Inlines.silk_SMLAWB(SilkTables.silk_Transition_LP_A_Q28[ind + 1][na], SilkTables.silk_Transition_LP_A_Q28[ind + 1][na] - SilkTables.silk_Transition_LP_A_Q28[ind][na], fac_Q16 - 65536);
/*     */           
/*     */           }
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 427 */         System.arraycopy(SilkTables.silk_Transition_LP_B_Q28[ind], 0, B_Q28, 0, 3);
/* 428 */         System.arraycopy(SilkTables.silk_Transition_LP_A_Q28[ind], 0, A_Q28, 0, 2);
/*     */       } 
/*     */     } else {
/* 431 */       System.arraycopy(SilkTables.silk_Transition_LP_B_Q28[4], 0, B_Q28, 0, 3);
/* 432 */       System.arraycopy(SilkTables.silk_Transition_LP_A_Q28[4], 0, A_Q28, 0, 2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void silk_LPC_analysis_filter(short[] output, int output_ptr, short[] input, int input_ptr, short[] B, int B_ptr, int len, int d) {
/* 459 */     short[] mem = new short[16];
/* 460 */     short[] num = new short[16];
/*     */     
/* 462 */     Inlines.OpusAssert((d >= 6));
/* 463 */     Inlines.OpusAssert(((d & 0x1) == 0));
/* 464 */     Inlines.OpusAssert((d <= len));
/*     */     
/* 466 */     Inlines.OpusAssert((d <= 16)); int j;
/* 467 */     for (j = 0; j < d; j++) {
/* 468 */       num[j] = (short)(0 - B[B_ptr + j]);
/*     */     }
/* 470 */     for (j = 0; j < d; j++) {
/* 471 */       mem[j] = input[input_ptr + d - j - 1];
/*     */     }
/* 473 */     Kernels.celt_fir(input, input_ptr + d, num, output, output_ptr + d, len - d, d, mem);
/* 474 */     for (j = output_ptr; j < output_ptr + d; j++) {
/* 475 */       output[j] = 0;
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
/*     */   static int LPC_inverse_pred_gain_QA(int[][] A_QA, int order) {
/* 496 */     int[] Anew_QA = A_QA[order & 0x1];
/*     */     
/* 498 */     int invGain_Q30 = 1073741824;
/* 499 */     for (int k = order - 1; k > 0; k--) {
/*     */       
/* 501 */       if (Anew_QA[k] > 16773022 || Anew_QA[k] < -16773022) {
/* 502 */         return 0;
/*     */       }
/*     */ 
/*     */       
/* 506 */       int i = 0 - Inlines.silk_LSHIFT(Anew_QA[k], 7);
/*     */ 
/*     */       
/* 509 */       int j = 1073741824 - Inlines.silk_SMMUL(i, i);
/* 510 */       Inlines.OpusAssert((j > 32768));
/*     */       
/* 512 */       Inlines.OpusAssert((j <= 1073741824));
/*     */ 
/*     */       
/* 515 */       int mult2Q = 32 - Inlines.silk_CLZ32(Inlines.silk_abs(j));
/* 516 */       int rc_mult2 = Inlines.silk_INVERSE32_varQ(j, mult2Q + 30);
/*     */ 
/*     */ 
/*     */       
/* 520 */       invGain_Q30 = Inlines.silk_LSHIFT(Inlines.silk_SMMUL(invGain_Q30, j), 2);
/* 521 */       Inlines.OpusAssert((invGain_Q30 >= 0));
/* 522 */       Inlines.OpusAssert((invGain_Q30 <= 1073741824));
/*     */ 
/*     */       
/* 525 */       int[] Aold_QA = Anew_QA;
/* 526 */       Anew_QA = A_QA[k & 0x1];
/*     */ 
/*     */       
/* 529 */       for (int n = 0; n < k; n++) {
/* 530 */         int tmp_QA = Aold_QA[n] - Inlines.MUL32_FRAC_Q(Aold_QA[k - n - 1], i, 31);
/* 531 */         Anew_QA[n] = Inlines.MUL32_FRAC_Q(tmp_QA, rc_mult2, mult2Q);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 536 */     if (Anew_QA[0] > 16773022 || Anew_QA[0] < -16773022) {
/* 537 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 541 */     int rc_Q31 = 0 - Inlines.silk_LSHIFT(Anew_QA[0], 7);
/*     */ 
/*     */     
/* 544 */     int rc_mult1_Q30 = 1073741824 - Inlines.silk_SMMUL(rc_Q31, rc_Q31);
/*     */ 
/*     */ 
/*     */     
/* 548 */     invGain_Q30 = Inlines.silk_LSHIFT(Inlines.silk_SMMUL(invGain_Q30, rc_mult1_Q30), 2);
/* 549 */     Inlines.OpusAssert((invGain_Q30 >= 0));
/* 550 */     Inlines.OpusAssert((invGain_Q30 <= 1073741824));
/*     */     
/* 552 */     return invGain_Q30;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int silk_LPC_inverse_pred_gain(short[] A_Q12, int order) {
/* 563 */     int[][] Atmp_QA = new int[2][];
/* 564 */     Atmp_QA[0] = new int[order];
/* 565 */     Atmp_QA[1] = new int[order];
/*     */     
/* 567 */     int DC_resp = 0;
/*     */     
/* 569 */     int[] Anew_QA = Atmp_QA[order & 0x1];
/*     */ 
/*     */     
/* 572 */     for (int k = 0; k < order; k++) {
/* 573 */       DC_resp += A_Q12[k];
/* 574 */       Anew_QA[k] = Inlines.silk_LSHIFT32(A_Q12[k], 12);
/*     */     } 
/*     */ 
/*     */     
/* 578 */     if (DC_resp >= 4096) {
/* 579 */       return 0;
/*     */     }
/*     */     
/* 582 */     return LPC_inverse_pred_gain_QA(Atmp_QA, order);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Filters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */