/*      */ package de.maxhenkel.voicechat.concentus;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class NLSF
/*      */ {
/*      */   private static final int MAX_STABILIZE_LOOPS = 20;
/*      */   private static final int QA = 16;
/*      */   private static final int BIN_DIV_STEPS_A2NLSF = 3;
/*      */   private static final int MAX_ITERATIONS_A2NLSF = 30;
/*      */   
/*      */   static void silk_NLSF_VQ(int[] err_Q26, short[] in_Q15, short[] pCB_Q8, int K, int LPC_order) {
/*   61 */     int pCB_idx = 0;
/*      */     
/*   63 */     Inlines.OpusAssert((err_Q26 != null));
/*   64 */     Inlines.OpusAssert((LPC_order <= 16));
/*   65 */     Inlines.OpusAssert(((LPC_order & 0x1) == 0));
/*      */ 
/*      */     
/*   68 */     for (int i = 0; i < K; i++) {
/*   69 */       int sum_error_Q26 = 0;
/*      */       
/*   71 */       for (int m = 0; m < LPC_order; m += 2) {
/*      */         
/*   73 */         int diff_Q15 = Inlines.silk_SUB_LSHIFT32(in_Q15[m], pCB_Q8[pCB_idx++], 7);
/*   74 */         int sum_error_Q30 = Inlines.silk_SMULBB(diff_Q15, diff_Q15);
/*      */ 
/*      */         
/*   77 */         diff_Q15 = Inlines.silk_SUB_LSHIFT32(in_Q15[m + 1], pCB_Q8[pCB_idx++], 7);
/*   78 */         sum_error_Q30 = Inlines.silk_SMLABB(sum_error_Q30, diff_Q15, diff_Q15);
/*      */         
/*   80 */         sum_error_Q26 = Inlines.silk_ADD_RSHIFT32(sum_error_Q26, sum_error_Q30, 4);
/*      */         
/*   82 */         Inlines.OpusAssert((sum_error_Q26 >= 0));
/*   83 */         Inlines.OpusAssert((sum_error_Q30 >= 0));
/*      */       } 
/*      */       
/*   86 */       err_Q26[i] = sum_error_Q26;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_NLSF_VQ_weights_laroia(short[] pNLSFW_Q_OUT, short[] pNLSF_Q15, int D) {
/*  100 */     Inlines.OpusAssert((pNLSFW_Q_OUT != null));
/*  101 */     Inlines.OpusAssert((D > 0));
/*  102 */     Inlines.OpusAssert(((D & 0x1) == 0));
/*      */ 
/*      */     
/*  105 */     int tmp1_int = Inlines.silk_max_int(pNLSF_Q15[0], 1);
/*  106 */     tmp1_int = Inlines.silk_DIV32(131072, tmp1_int);
/*  107 */     int tmp2_int = Inlines.silk_max_int(pNLSF_Q15[1] - pNLSF_Q15[0], 1);
/*  108 */     tmp2_int = Inlines.silk_DIV32(131072, tmp2_int);
/*  109 */     pNLSFW_Q_OUT[0] = (short)Inlines.silk_min_int(tmp1_int + tmp2_int, 32767);
/*      */     
/*  111 */     Inlines.OpusAssert((pNLSFW_Q_OUT[0] > 0));
/*      */ 
/*      */     
/*  114 */     for (int k = 1; k < D - 1; k += 2) {
/*  115 */       tmp1_int = Inlines.silk_max_int(pNLSF_Q15[k + 1] - pNLSF_Q15[k], 1);
/*  116 */       tmp1_int = Inlines.silk_DIV32(131072, tmp1_int);
/*  117 */       pNLSFW_Q_OUT[k] = (short)Inlines.silk_min_int(tmp1_int + tmp2_int, 32767);
/*  118 */       Inlines.OpusAssert((pNLSFW_Q_OUT[k] > 0));
/*      */       
/*  120 */       tmp2_int = Inlines.silk_max_int(pNLSF_Q15[k + 2] - pNLSF_Q15[k + 1], 1);
/*  121 */       tmp2_int = Inlines.silk_DIV32(131072, tmp2_int);
/*  122 */       pNLSFW_Q_OUT[k + 1] = (short)Inlines.silk_min_int(tmp1_int + tmp2_int, 32767);
/*  123 */       Inlines.OpusAssert((pNLSFW_Q_OUT[k + 1] > 0));
/*      */     } 
/*      */ 
/*      */     
/*  127 */     tmp1_int = Inlines.silk_max_int(32768 - pNLSF_Q15[D - 1], 1);
/*  128 */     tmp1_int = Inlines.silk_DIV32(131072, tmp1_int);
/*  129 */     pNLSFW_Q_OUT[D - 1] = (short)Inlines.silk_min_int(tmp1_int + tmp2_int, 32767);
/*      */     
/*  131 */     Inlines.OpusAssert((pNLSFW_Q_OUT[D - 1] > 0));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_NLSF_residual_dequant(short[] x_Q10, byte[] indices, int indices_ptr, short[] pred_coef_Q8, int quant_step_size_Q16, short order) {
/*  152 */     short out_Q10 = 0;
/*  153 */     for (int i = order - 1; i >= 0; i--) {
/*  154 */       int pred_Q10 = Inlines.silk_RSHIFT(Inlines.silk_SMULBB(out_Q10, pred_coef_Q8[i]), 8);
/*  155 */       out_Q10 = Inlines.silk_LSHIFT16((short)indices[indices_ptr + i], 10);
/*  156 */       if (out_Q10 > 0) {
/*  157 */         out_Q10 = Inlines.silk_SUB16(out_Q10, (short)102);
/*  158 */       } else if (out_Q10 < 0) {
/*  159 */         out_Q10 = Inlines.silk_ADD16(out_Q10, (short)102);
/*      */       } 
/*  161 */       out_Q10 = (short)Inlines.silk_SMLAWB(pred_Q10, out_Q10, quant_step_size_Q16);
/*  162 */       x_Q10[i] = out_Q10;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_NLSF_unpack(short[] ec_ix, short[] pred_Q8, NLSFCodebook psNLSF_CB, int CB1_index) {
/*  176 */     short[] ec_sel = psNLSF_CB.ec_sel;
/*  177 */     int ec_sel_ptr = CB1_index * psNLSF_CB.order / 2;
/*      */     
/*  179 */     for (int i = 0; i < psNLSF_CB.order; i += 2) {
/*  180 */       short entry = ec_sel[ec_sel_ptr];
/*  181 */       ec_sel_ptr++;
/*  182 */       ec_ix[i] = (short)Inlines.silk_SMULBB(Inlines.silk_RSHIFT(entry, 1) & 0x7, 9);
/*  183 */       pred_Q8[i] = psNLSF_CB.pred_Q8[i + (entry & 0x1) * (psNLSF_CB.order - 1)];
/*  184 */       ec_ix[i + 1] = (short)Inlines.silk_SMULBB(Inlines.silk_RSHIFT(entry, 5) & 0x7, 9);
/*  185 */       pred_Q8[i + 1] = psNLSF_CB.pred_Q8[i + (Inlines.silk_RSHIFT(entry, 4) & 0x1) * (psNLSF_CB.order - 1) + 1];
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_NLSF_stabilize(short[] NLSF_Q15, short[] NDeltaMin_Q15, int L) {
/*  196 */     int I = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  201 */     Inlines.OpusAssert((NDeltaMin_Q15[L] >= 1));
/*      */     int loops;
/*  203 */     for (loops = 0; loops < 20; loops++) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  212 */       int min_diff_Q15 = NLSF_Q15[0] - NDeltaMin_Q15[0];
/*  213 */       I = 0;
/*      */ 
/*      */       
/*  216 */       for (int i = 1; i <= L - 1; i++) {
/*  217 */         int j = NLSF_Q15[i] - NLSF_Q15[i - 1] + NDeltaMin_Q15[i];
/*  218 */         if (j < min_diff_Q15) {
/*  219 */           min_diff_Q15 = j;
/*  220 */           I = i;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  225 */       int diff_Q15 = 32768 - NLSF_Q15[L - 1] + NDeltaMin_Q15[L];
/*  226 */       if (diff_Q15 < min_diff_Q15) {
/*  227 */         min_diff_Q15 = diff_Q15;
/*  228 */         I = L;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  238 */       if (min_diff_Q15 >= 0) {
/*      */         return;
/*      */       }
/*      */       
/*  242 */       if (I == 0) {
/*      */         
/*  244 */         NLSF_Q15[0] = NDeltaMin_Q15[0];
/*  245 */       } else if (I == L) {
/*      */         
/*  247 */         NLSF_Q15[L - 1] = (short)(32768 - NDeltaMin_Q15[L]);
/*      */       } else {
/*      */         
/*  250 */         int min_center_Q15 = 0; int k;
/*  251 */         for (k = 0; k < I; k++) {
/*  252 */           min_center_Q15 += NDeltaMin_Q15[k];
/*      */         }
/*      */         
/*  255 */         min_center_Q15 += Inlines.silk_RSHIFT(NDeltaMin_Q15[I], 1);
/*      */ 
/*      */         
/*  258 */         int max_center_Q15 = 32768;
/*  259 */         for (k = L; k > I; k--) {
/*  260 */           max_center_Q15 -= NDeltaMin_Q15[k];
/*      */         }
/*      */         
/*  263 */         max_center_Q15 -= Inlines.silk_RSHIFT(NDeltaMin_Q15[I], 1);
/*      */ 
/*      */         
/*  266 */         short center_freq_Q15 = (short)Inlines.silk_LIMIT_32(Inlines.silk_RSHIFT_ROUND(NLSF_Q15[I - 1] + NLSF_Q15[I], 1), min_center_Q15, max_center_Q15);
/*      */         
/*  268 */         NLSF_Q15[I - 1] = (short)(center_freq_Q15 - Inlines.silk_RSHIFT(NDeltaMin_Q15[I], 1));
/*  269 */         NLSF_Q15[I] = (short)(NLSF_Q15[I - 1] + NDeltaMin_Q15[I]);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  274 */     if (loops == 20) {
/*  275 */       Sort.silk_insertion_sort_increasing_all_values_int16(NLSF_Q15, L);
/*      */ 
/*      */       
/*  278 */       NLSF_Q15[0] = (short)Inlines.silk_max_int(NLSF_Q15[0], NDeltaMin_Q15[0]);
/*      */       
/*      */       int i;
/*  281 */       for (i = 1; i < L; i++) {
/*  282 */         NLSF_Q15[i] = (short)Inlines.silk_max_int(NLSF_Q15[i], NLSF_Q15[i - 1] + NDeltaMin_Q15[i]);
/*      */       }
/*      */ 
/*      */       
/*  286 */       NLSF_Q15[L - 1] = (short)Inlines.silk_min_int(NLSF_Q15[L - 1], 32768 - NDeltaMin_Q15[L]);
/*      */ 
/*      */       
/*  289 */       for (i = L - 2; i >= 0; i--) {
/*  290 */         NLSF_Q15[i] = (short)Inlines.silk_min_int(NLSF_Q15[i], NLSF_Q15[i + 1] - NDeltaMin_Q15[i + 1]);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_NLSF_decode(short[] pNLSF_Q15, byte[] NLSFIndices, NLSFCodebook psNLSF_CB) {
/*  303 */     short[] pred_Q8 = new short[psNLSF_CB.order];
/*  304 */     short[] ec_ix = new short[psNLSF_CB.order];
/*  305 */     short[] res_Q10 = new short[psNLSF_CB.order];
/*  306 */     short[] W_tmp_QW = new short[psNLSF_CB.order];
/*      */ 
/*      */ 
/*      */     
/*  310 */     short[] pCB = psNLSF_CB.CB1_NLSF_Q8;
/*  311 */     int pCB_element = NLSFIndices[0] * psNLSF_CB.order;
/*      */     int i;
/*  313 */     for (i = 0; i < psNLSF_CB.order; i++) {
/*  314 */       pNLSF_Q15[i] = Inlines.silk_LSHIFT16(pCB[pCB_element + i], 7);
/*      */     }
/*      */ 
/*      */     
/*  318 */     silk_NLSF_unpack(ec_ix, pred_Q8, psNLSF_CB, NLSFIndices[0]);
/*      */ 
/*      */     
/*  321 */     silk_NLSF_residual_dequant(res_Q10, NLSFIndices, 1, pred_Q8, psNLSF_CB.quantStepSize_Q16, psNLSF_CB.order);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  329 */     silk_NLSF_VQ_weights_laroia(W_tmp_QW, pNLSF_Q15, psNLSF_CB.order);
/*      */ 
/*      */     
/*  332 */     for (i = 0; i < psNLSF_CB.order; i++) {
/*  333 */       int W_tmp_Q9 = Inlines.silk_SQRT_APPROX(Inlines.silk_LSHIFT(W_tmp_QW[i], 16));
/*  334 */       int NLSF_Q15_tmp = Inlines.silk_ADD32(pNLSF_Q15[i], Inlines.silk_DIV32_16(Inlines.silk_LSHIFT(res_Q10[i], 14), (short)W_tmp_Q9));
/*  335 */       pNLSF_Q15[i] = (short)Inlines.silk_LIMIT(NLSF_Q15_tmp, 0, 32767);
/*      */     } 
/*      */ 
/*      */     
/*  339 */     silk_NLSF_stabilize(pNLSF_Q15, psNLSF_CB.deltaMin_Q15, psNLSF_CB.order);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_NLSF_del_dec_quant(byte[] indices, short[] x_Q10, short[] w_Q5, short[] pred_coef_Q8, short[] ec_ix, short[] ec_rates_Q5, int quant_step_size_Q16, short inv_quant_step_size_Q6, int mu_Q20, short order) {
/*  371 */     int[] ind_sort = new int[4];
/*  372 */     byte[][] ind = new byte[4][]; int i;
/*  373 */     for (i = 0; i < 4; i++) {
/*  374 */       ind[i] = new byte[16];
/*      */     }
/*      */     
/*  377 */     short[] prev_out_Q10 = new short[8];
/*  378 */     int[] RD_Q25 = new int[8];
/*  379 */     int[] RD_min_Q25 = new int[4];
/*  380 */     int[] RD_max_Q25 = new int[4];
/*      */ 
/*      */     
/*  383 */     int[] out0_Q10_table = new int[20];
/*  384 */     int[] out1_Q10_table = new int[20];
/*      */     
/*  386 */     for (i = -10; i <= 9; i++) {
/*  387 */       int out0_Q10 = Inlines.silk_LSHIFT(i, 10);
/*  388 */       int out1_Q10 = Inlines.silk_ADD16((short)out0_Q10, (short)1024);
/*      */       
/*  390 */       if (i > 0) {
/*  391 */         out0_Q10 = Inlines.silk_SUB16((short)out0_Q10, (short)102);
/*  392 */         out1_Q10 = Inlines.silk_SUB16((short)out1_Q10, (short)102);
/*  393 */       } else if (i == 0) {
/*  394 */         out1_Q10 = Inlines.silk_SUB16((short)out1_Q10, (short)102);
/*  395 */       } else if (i == -1) {
/*  396 */         out0_Q10 = Inlines.silk_ADD16((short)out0_Q10, (short)102);
/*      */       } else {
/*  398 */         out0_Q10 = Inlines.silk_ADD16((short)out0_Q10, (short)102);
/*  399 */         out1_Q10 = Inlines.silk_ADD16((short)out1_Q10, (short)102);
/*      */       } 
/*      */       
/*  402 */       out0_Q10_table[i + 10] = Inlines.silk_SMULWB(out0_Q10, quant_step_size_Q16);
/*  403 */       out1_Q10_table[i + 10] = Inlines.silk_SMULWB(out1_Q10, quant_step_size_Q16);
/*      */     } 
/*      */     
/*  406 */     Inlines.OpusAssert(true);
/*      */     
/*  408 */     int nStates = 1;
/*  409 */     RD_Q25[0] = 0;
/*  410 */     prev_out_Q10[0] = 0;
/*      */     
/*  412 */     for (i = order - 1;; i--) {
/*  413 */       int pred_coef_Q16 = Inlines.silk_LSHIFT(pred_coef_Q8[i], 8);
/*  414 */       int in_Q10 = x_Q10[i];
/*      */       int j;
/*  416 */       for (j = 0; j < nStates; j++) {
/*  417 */         int rate0_Q5, rate1_Q5, pred_Q10 = Inlines.silk_SMULWB(pred_coef_Q16, prev_out_Q10[j]);
/*  418 */         int res_Q10 = Inlines.silk_SUB16((short)in_Q10, (short)pred_Q10);
/*  419 */         int k = Inlines.silk_SMULWB(inv_quant_step_size_Q6, res_Q10);
/*  420 */         k = Inlines.silk_LIMIT(k, -10, 9);
/*  421 */         ind[j][i] = (byte)k;
/*  422 */         int rates_Q5 = ec_ix[i] + k;
/*      */ 
/*      */         
/*  425 */         int out0_Q10 = out0_Q10_table[k + 10];
/*  426 */         int out1_Q10 = out1_Q10_table[k + 10];
/*      */         
/*  428 */         out0_Q10 = Inlines.silk_ADD16((short)out0_Q10, (short)pred_Q10);
/*  429 */         out1_Q10 = Inlines.silk_ADD16((short)out1_Q10, (short)pred_Q10);
/*  430 */         prev_out_Q10[j] = (short)out0_Q10;
/*  431 */         prev_out_Q10[j + nStates] = (short)out1_Q10;
/*      */ 
/*      */         
/*  434 */         if (k + 1 >= 4) {
/*  435 */           if (k + 1 == 4) {
/*  436 */             rate0_Q5 = ec_rates_Q5[rates_Q5 + 4];
/*  437 */             rate1_Q5 = 280;
/*      */           } else {
/*  439 */             rate0_Q5 = Inlines.silk_SMLABB(108, 43, k);
/*  440 */             rate1_Q5 = Inlines.silk_ADD16((short)rate0_Q5, (short)43);
/*      */           } 
/*  442 */         } else if (k <= -4) {
/*  443 */           if (k == -4) {
/*  444 */             rate0_Q5 = 280;
/*  445 */             rate1_Q5 = ec_rates_Q5[rates_Q5 + 1 + 4];
/*      */           } else {
/*  447 */             rate0_Q5 = Inlines.silk_SMLABB(108, -43, k);
/*  448 */             rate1_Q5 = Inlines.silk_SUB16((short)rate0_Q5, (short)43);
/*      */           } 
/*      */         } else {
/*  451 */           rate0_Q5 = ec_rates_Q5[rates_Q5 + 4];
/*  452 */           rate1_Q5 = ec_rates_Q5[rates_Q5 + 1 + 4];
/*      */         } 
/*      */         
/*  455 */         int RD_tmp_Q25 = RD_Q25[j];
/*  456 */         int diff_Q10 = Inlines.silk_SUB16((short)in_Q10, (short)out0_Q10);
/*  457 */         RD_Q25[j] = Inlines.silk_SMLABB(Inlines.silk_MLA(RD_tmp_Q25, Inlines.silk_SMULBB(diff_Q10, diff_Q10), w_Q5[i]), mu_Q20, rate0_Q5);
/*  458 */         diff_Q10 = Inlines.silk_SUB16((short)in_Q10, (short)out1_Q10);
/*  459 */         RD_Q25[j + nStates] = Inlines.silk_SMLABB(Inlines.silk_MLA(RD_tmp_Q25, Inlines.silk_SMULBB(diff_Q10, diff_Q10), w_Q5[i]), mu_Q20, rate1_Q5);
/*      */       } 
/*      */       
/*  462 */       if (nStates <= 2) {
/*      */         
/*  464 */         for (j = 0; j < nStates; j++) {
/*  465 */           ind[j + nStates][i] = (byte)(ind[j][i] + 1);
/*      */         }
/*  467 */         nStates = Inlines.silk_LSHIFT(nStates, 1);
/*      */         
/*  469 */         for (j = nStates; j < 4; j++) {
/*  470 */           ind[j][i] = ind[j - nStates][i];
/*      */         }
/*  472 */       } else if (i > 0) {
/*      */         
/*  474 */         for (j = 0; j < 4; j++) {
/*  475 */           if (RD_Q25[j] > RD_Q25[j + 4]) {
/*  476 */             RD_max_Q25[j] = RD_Q25[j];
/*  477 */             RD_min_Q25[j] = RD_Q25[j + 4];
/*  478 */             RD_Q25[j] = RD_min_Q25[j];
/*  479 */             RD_Q25[j + 4] = RD_max_Q25[j];
/*      */ 
/*      */             
/*  482 */             int out0_Q10 = prev_out_Q10[j];
/*  483 */             prev_out_Q10[j] = prev_out_Q10[j + 4];
/*  484 */             prev_out_Q10[j + 4] = (short)out0_Q10;
/*  485 */             ind_sort[j] = j + 4;
/*      */           } else {
/*  487 */             RD_min_Q25[j] = RD_Q25[j];
/*  488 */             RD_max_Q25[j] = RD_Q25[j + 4];
/*  489 */             ind_sort[j] = j;
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*      */         while (true) {
/*  496 */           int min_max_Q25 = Integer.MAX_VALUE;
/*  497 */           int max_min_Q25 = 0;
/*  498 */           int ind_min_max = 0;
/*  499 */           int ind_max_min = 0;
/*      */           
/*  501 */           for (j = 0; j < 4; j++) {
/*  502 */             if (min_max_Q25 > RD_max_Q25[j]) {
/*  503 */               min_max_Q25 = RD_max_Q25[j];
/*  504 */               ind_min_max = j;
/*      */             } 
/*  506 */             if (max_min_Q25 < RD_min_Q25[j]) {
/*  507 */               max_min_Q25 = RD_min_Q25[j];
/*  508 */               ind_max_min = j;
/*      */             } 
/*      */           } 
/*      */           
/*  512 */           if (min_max_Q25 >= max_min_Q25) {
/*      */             break;
/*      */           }
/*      */ 
/*      */           
/*  517 */           ind_sort[ind_max_min] = ind_sort[ind_min_max] ^ 0x4;
/*  518 */           RD_Q25[ind_max_min] = RD_Q25[ind_min_max + 4];
/*  519 */           prev_out_Q10[ind_max_min] = prev_out_Q10[ind_min_max + 4];
/*  520 */           RD_min_Q25[ind_max_min] = 0;
/*  521 */           RD_max_Q25[ind_min_max] = Integer.MAX_VALUE;
/*  522 */           System.arraycopy(ind[ind_min_max], 0, ind[ind_max_min], 0, order);
/*      */         } 
/*      */ 
/*      */         
/*  526 */         for (j = 0; j < 4; j++) {
/*  527 */           byte x = (byte)Inlines.silk_RSHIFT(ind_sort[j], 2);
/*  528 */           ind[j][i] = (byte)(ind[j][i] + x);
/*      */         } 
/*      */       } else {
/*      */         break;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  537 */     int ind_tmp = 0;
/*  538 */     int min_Q25 = Integer.MAX_VALUE; byte b;
/*  539 */     for (b = 0; b < 8; b++) {
/*  540 */       if (min_Q25 > RD_Q25[b]) {
/*  541 */         min_Q25 = RD_Q25[b];
/*  542 */         ind_tmp = b;
/*      */       } 
/*      */     } 
/*      */     
/*  546 */     for (b = 0; b < order; b++) {
/*  547 */       indices[b] = ind[ind_tmp & 0x3][b];
/*  548 */       Inlines.OpusAssert((indices[b] >= -10));
/*  549 */       Inlines.OpusAssert((indices[b] <= 10));
/*      */     } 
/*      */     
/*  552 */     indices[0] = (byte)(indices[0] + Inlines.silk_RSHIFT(ind_tmp, 2));
/*  553 */     Inlines.OpusAssert((indices[0] <= 10));
/*  554 */     Inlines.OpusAssert((min_Q25 >= 0));
/*  555 */     return min_Q25;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_NLSF_encode(byte[] NLSFIndices, short[] pNLSF_Q15, NLSFCodebook psNLSF_CB, short[] pW_QW, int NLSF_mu_Q20, int nSurvivors, int signalType) {
/*  583 */     short[] res_Q15 = new short[psNLSF_CB.order];
/*  584 */     short[] res_Q10 = new short[psNLSF_CB.order];
/*  585 */     short[] NLSF_tmp_Q15 = new short[psNLSF_CB.order];
/*  586 */     short[] W_tmp_QW = new short[psNLSF_CB.order];
/*  587 */     short[] W_adj_Q5 = new short[psNLSF_CB.order];
/*  588 */     short[] pred_Q8 = new short[psNLSF_CB.order];
/*  589 */     short[] ec_ix = new short[psNLSF_CB.order];
/*  590 */     short[] pCB = psNLSF_CB.CB1_NLSF_Q8;
/*      */ 
/*      */ 
/*      */     
/*  594 */     Inlines.OpusAssert((nSurvivors <= 32));
/*  595 */     Inlines.OpusAssert((signalType >= 0 && signalType <= 2));
/*  596 */     Inlines.OpusAssert((NLSF_mu_Q20 <= 32767 && NLSF_mu_Q20 >= 0));
/*      */ 
/*      */     
/*  599 */     silk_NLSF_stabilize(pNLSF_Q15, psNLSF_CB.deltaMin_Q15, psNLSF_CB.order);
/*      */ 
/*      */     
/*  602 */     int[] err_Q26 = new int[psNLSF_CB.nVectors];
/*  603 */     silk_NLSF_VQ(err_Q26, pNLSF_Q15, psNLSF_CB.CB1_NLSF_Q8, psNLSF_CB.nVectors, psNLSF_CB.order);
/*      */ 
/*      */     
/*  606 */     int[] tempIndices1 = new int[nSurvivors];
/*  607 */     Sort.silk_insertion_sort_increasing(err_Q26, tempIndices1, psNLSF_CB.nVectors, nSurvivors);
/*      */     
/*  609 */     int[] RD_Q25 = new int[nSurvivors];
/*  610 */     byte[][] tempIndices2 = Arrays.InitTwoDimensionalArrayByte(nSurvivors, 16);
/*      */ 
/*      */     
/*  613 */     for (int s = 0; s < nSurvivors; s++) {
/*  614 */       int prob_Q8, ind1 = tempIndices1[s];
/*      */ 
/*      */       
/*  617 */       int pCB_element = ind1 * psNLSF_CB.order; int i;
/*  618 */       for (i = 0; i < psNLSF_CB.order; i++) {
/*  619 */         NLSF_tmp_Q15[i] = Inlines.silk_LSHIFT16(pCB[pCB_element + i], 7);
/*  620 */         res_Q15[i] = (short)(pNLSF_Q15[i] - NLSF_tmp_Q15[i]);
/*      */       } 
/*      */ 
/*      */       
/*  624 */       silk_NLSF_VQ_weights_laroia(W_tmp_QW, NLSF_tmp_Q15, psNLSF_CB.order);
/*      */ 
/*      */       
/*  627 */       for (i = 0; i < psNLSF_CB.order; i++) {
/*  628 */         int W_tmp_Q9 = Inlines.silk_SQRT_APPROX(Inlines.silk_LSHIFT(W_tmp_QW[i], 16));
/*  629 */         res_Q10[i] = (short)Inlines.silk_RSHIFT(Inlines.silk_SMULBB(res_Q15[i], W_tmp_Q9), 14);
/*      */       } 
/*      */ 
/*      */       
/*  633 */       for (i = 0; i < psNLSF_CB.order; i++) {
/*  634 */         W_adj_Q5[i] = (short)Inlines.silk_DIV32_16(Inlines.silk_LSHIFT(pW_QW[i], 5), W_tmp_QW[i]);
/*      */       }
/*      */ 
/*      */       
/*  638 */       silk_NLSF_unpack(ec_ix, pred_Q8, psNLSF_CB, ind1);
/*      */ 
/*      */       
/*  641 */       RD_Q25[s] = silk_NLSF_del_dec_quant(tempIndices2[s], res_Q10, W_adj_Q5, pred_Q8, ec_ix, psNLSF_CB.ec_Rates_Q5, psNLSF_CB.quantStepSize_Q16, psNLSF_CB.invQuantStepSize_Q6, NLSF_mu_Q20, psNLSF_CB.order);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  654 */       int iCDF_ptr = (signalType >> 1) * psNLSF_CB.nVectors;
/*      */       
/*  656 */       if (ind1 == 0) {
/*  657 */         prob_Q8 = 256 - psNLSF_CB.CB1_iCDF[iCDF_ptr + ind1];
/*      */       } else {
/*  659 */         prob_Q8 = psNLSF_CB.CB1_iCDF[iCDF_ptr + ind1 - 1] - psNLSF_CB.CB1_iCDF[iCDF_ptr + ind1];
/*      */       } 
/*      */       
/*  662 */       int bits_q7 = 1024 - Inlines.silk_lin2log(prob_Q8);
/*  663 */       RD_Q25[s] = Inlines.silk_SMLABB(RD_Q25[s], bits_q7, Inlines.silk_RSHIFT(NLSF_mu_Q20, 2));
/*      */     } 
/*      */ 
/*      */     
/*  667 */     int[] bestIndex = new int[1];
/*  668 */     Sort.silk_insertion_sort_increasing(RD_Q25, bestIndex, nSurvivors, 1);
/*      */     
/*  670 */     NLSFIndices[0] = (byte)tempIndices1[bestIndex[0]];
/*  671 */     System.arraycopy(tempIndices2[bestIndex[0]], 0, NLSFIndices, 1, psNLSF_CB.order);
/*      */ 
/*      */     
/*  674 */     silk_NLSF_decode(pNLSF_Q15, NLSFIndices, psNLSF_CB);
/*      */     
/*  676 */     return RD_Q25[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_NLSF2A_find_poly(int[] o, int[] cLSF, int cLSF_ptr, int dd) {
/*  692 */     o[0] = Inlines.silk_LSHIFT(1, 16);
/*  693 */     o[1] = 0 - cLSF[cLSF_ptr];
/*  694 */     for (int k = 1; k < dd; k++) {
/*  695 */       int ftmp = cLSF[cLSF_ptr + 2 * k];
/*      */       
/*  697 */       o[k + 1] = Inlines.silk_LSHIFT(o[k - 1], 1) - (int)Inlines.silk_RSHIFT_ROUND64(Inlines.silk_SMULL(ftmp, o[k]), 16);
/*  698 */       for (int n = k; n > 1; n--) {
/*  699 */         o[n] = o[n] + o[n - 2] - (int)Inlines.silk_RSHIFT_ROUND64(Inlines.silk_SMULL(ftmp, o[n - 1]), 16);
/*      */       }
/*  701 */       o[1] = o[1] - ftmp;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  707 */   private static final byte[] ordering16 = new byte[] { 0, 15, 8, 7, 4, 11, 12, 3, 2, 13, 10, 5, 6, 9, 14, 1 };
/*  708 */   private static final byte[] ordering10 = new byte[] { 0, 9, 6, 3, 4, 5, 8, 1, 2, 7 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_NLSF2A(short[] a_Q12, short[] arrayOfShort1, int d) {
/*  723 */     int[] cos_LSF_QA = new int[d];
/*  724 */     int[] P = new int[d / 2 + 1];
/*  725 */     int[] Q = new int[d / 2 + 1];
/*  726 */     int[] a32_QA1 = new int[d];
/*      */ 
/*      */     
/*  729 */     int idx = 0;
/*      */     
/*  731 */     Inlines.OpusAssert(true);
/*  732 */     Inlines.OpusAssert((d == 10 || d == 16));
/*      */ 
/*      */     
/*  735 */     byte[] ordering = (d == 16) ? ordering16 : ordering10;
/*      */     int k;
/*  737 */     for (k = 0; k < d; k++) {
/*  738 */       Inlines.OpusAssert((arrayOfShort1[k] >= 0));
/*      */ 
/*      */       
/*  741 */       int f_int = Inlines.silk_RSHIFT(arrayOfShort1[k], 8);
/*      */ 
/*      */       
/*  744 */       int f_frac = arrayOfShort1[k] - Inlines.silk_LSHIFT(f_int, 8);
/*      */       
/*  746 */       Inlines.OpusAssert((f_int >= 0));
/*  747 */       Inlines.OpusAssert((f_int < 128));
/*      */ 
/*      */       
/*  750 */       int cos_val = SilkTables.silk_LSFCosTab_Q12[f_int];
/*      */       
/*  752 */       int delta = SilkTables.silk_LSFCosTab_Q12[f_int + 1] - cos_val;
/*      */ 
/*      */ 
/*      */       
/*  756 */       cos_LSF_QA[ordering[k]] = Inlines.silk_RSHIFT_ROUND(Inlines.silk_LSHIFT(cos_val, 8) + Inlines.silk_MUL(delta, f_frac), 4);
/*      */     } 
/*      */ 
/*      */     
/*  760 */     int dd = Inlines.silk_RSHIFT(d, 1);
/*      */ 
/*      */     
/*  763 */     silk_NLSF2A_find_poly(P, cos_LSF_QA, 0, dd);
/*  764 */     silk_NLSF2A_find_poly(Q, cos_LSF_QA, 1, dd);
/*      */ 
/*      */     
/*  767 */     for (k = 0; k < dd; k++) {
/*  768 */       int Ptmp = P[k + 1] + P[k];
/*  769 */       int Qtmp = Q[k + 1] - Q[k];
/*      */ 
/*      */       
/*  772 */       a32_QA1[k] = -Qtmp - Ptmp;
/*      */       
/*  774 */       a32_QA1[d - k - 1] = Qtmp - Ptmp;
/*      */     } 
/*      */     
/*      */     int i;
/*      */     
/*  779 */     for (i = 0; i < 10; ) {
/*      */       
/*  781 */       int maxabs = 0;
/*  782 */       for (k = 0; k < d; k++) {
/*  783 */         int absval = Inlines.silk_abs(a32_QA1[k]);
/*  784 */         if (absval > maxabs) {
/*  785 */           maxabs = absval;
/*  786 */           idx = k;
/*      */         } 
/*      */       } 
/*      */       
/*  790 */       maxabs = Inlines.silk_RSHIFT_ROUND(maxabs, 5);
/*      */ 
/*      */       
/*  793 */       if (maxabs > 32767) {
/*      */         
/*  795 */         maxabs = Inlines.silk_min(maxabs, 163838);
/*      */         
/*  797 */         int sc_Q16 = 65470 - Inlines.silk_DIV32(Inlines.silk_LSHIFT(maxabs - 32767, 14), 
/*  798 */             Inlines.silk_RSHIFT32(Inlines.silk_MUL(maxabs, idx + 1), 2));
/*  799 */         Filters.silk_bwexpander_32(a32_QA1, d, sc_Q16);
/*      */         
/*      */         i++;
/*      */       } 
/*      */     } 
/*      */     
/*  805 */     if (i == 10) {
/*      */       
/*  807 */       for (k = 0; k < d; k++) {
/*  808 */         a_Q12[k] = (short)Inlines.silk_SAT16(Inlines.silk_RSHIFT_ROUND(a32_QA1[k], 5));
/*      */         
/*  810 */         a32_QA1[k] = Inlines.silk_LSHIFT(a_Q12[k], 5);
/*      */       } 
/*      */     } else {
/*  813 */       for (k = 0; k < d; k++) {
/*  814 */         a_Q12[k] = (short)Inlines.silk_RSHIFT_ROUND(a32_QA1[k], 5);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  819 */     for (i = 0; i < 16 && 
/*  820 */       Filters.silk_LPC_inverse_pred_gain(a_Q12, d) < 107374; i++) {
/*      */ 
/*      */       
/*  823 */       Filters.silk_bwexpander_32(a32_QA1, d, 65536 - Inlines.silk_LSHIFT(2, i));
/*      */       
/*  825 */       for (k = 0; k < d; k++) {
/*  826 */         a_Q12[k] = (short)Inlines.silk_RSHIFT_ROUND(a32_QA1[k], 5);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_A2NLSF_trans_poly(int[] p, int dd) {
/*  843 */     for (int k = 2; k <= dd; k++) {
/*  844 */       for (int n = dd; n > k; n--) {
/*  845 */         p[n - 2] = p[n - 2] - p[n];
/*      */       }
/*  847 */       p[k - 2] = p[k - 2] - Inlines.silk_LSHIFT(p[k], 1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_A2NLSF_eval_poly(int[] p, int x, int dd) {
/*  862 */     int y32 = p[dd];
/*      */     
/*  864 */     int x_Q16 = Inlines.silk_LSHIFT(x, 4);
/*      */     
/*  866 */     if (8 == dd) {
/*  867 */       y32 = Inlines.silk_SMLAWW(p[7], y32, x_Q16);
/*  868 */       y32 = Inlines.silk_SMLAWW(p[6], y32, x_Q16);
/*  869 */       y32 = Inlines.silk_SMLAWW(p[5], y32, x_Q16);
/*  870 */       y32 = Inlines.silk_SMLAWW(p[4], y32, x_Q16);
/*  871 */       y32 = Inlines.silk_SMLAWW(p[3], y32, x_Q16);
/*  872 */       y32 = Inlines.silk_SMLAWW(p[2], y32, x_Q16);
/*  873 */       y32 = Inlines.silk_SMLAWW(p[1], y32, x_Q16);
/*  874 */       y32 = Inlines.silk_SMLAWW(p[0], y32, x_Q16);
/*      */     } else {
/*  876 */       for (int n = dd - 1; n >= 0; n--) {
/*  877 */         y32 = Inlines.silk_SMLAWW(p[n], y32, x_Q16);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  882 */     return y32;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_A2NLSF_init(int[] a_Q16, int[] P, int[] Q, int dd) {
/*  893 */     P[dd] = Inlines.silk_LSHIFT(1, 16);
/*  894 */     Q[dd] = Inlines.silk_LSHIFT(1, 16); int k;
/*  895 */     for (k = 0; k < dd; k++) {
/*  896 */       P[k] = -a_Q16[dd - k - 1] - a_Q16[dd + k];
/*      */       
/*  898 */       Q[k] = -a_Q16[dd - k - 1] + a_Q16[dd + k];
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  905 */     for (k = dd; k > 0; k--) {
/*  906 */       P[k - 1] = P[k - 1] - P[k];
/*  907 */       Q[k - 1] = Q[k - 1] + Q[k];
/*      */     } 
/*      */ 
/*      */     
/*  911 */     silk_A2NLSF_trans_poly(P, dd);
/*  912 */     silk_A2NLSF_trans_poly(Q, dd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_A2NLSF(short[] arrayOfShort, int[] a_Q16, int d) {
/*  927 */     int root_ix, P[] = new int[9];
/*  928 */     int[] Q = new int[9];
/*  929 */     int[][] PQ = new int[2][];
/*      */ 
/*      */ 
/*      */     
/*  933 */     PQ[0] = P;
/*  934 */     PQ[1] = Q;
/*      */     
/*  936 */     int dd = Inlines.silk_RSHIFT(d, 1);
/*      */     
/*  938 */     silk_A2NLSF_init(a_Q16, P, Q, dd);
/*      */ 
/*      */     
/*  941 */     int[] p = P;
/*      */ 
/*      */     
/*  944 */     int xlo = SilkTables.silk_LSFCosTab_Q12[0];
/*      */     
/*  946 */     int ylo = silk_A2NLSF_eval_poly(p, xlo, dd);
/*      */     
/*  948 */     if (ylo < 0) {
/*      */       
/*  950 */       arrayOfShort[0] = 0;
/*  951 */       p = Q;
/*      */       
/*  953 */       ylo = silk_A2NLSF_eval_poly(p, xlo, dd);
/*  954 */       root_ix = 1;
/*      */     } else {
/*      */       
/*  957 */       root_ix = 0;
/*      */     } 
/*      */     
/*  960 */     int k = 1;
/*      */     
/*  962 */     int i = 0;
/*      */     
/*  964 */     int thr = 0;
/*      */     
/*      */     while (true) {
/*  967 */       int xhi = SilkTables.silk_LSFCosTab_Q12[k];
/*      */       
/*  969 */       int yhi = silk_A2NLSF_eval_poly(p, xhi, dd);
/*      */ 
/*      */       
/*  972 */       if ((ylo <= 0 && yhi >= thr) || (ylo >= 0 && yhi <= -thr)) {
/*  973 */         if (yhi == 0) {
/*      */ 
/*      */           
/*  976 */           thr = 1;
/*      */         } else {
/*  978 */           thr = 0;
/*      */         } 
/*      */         
/*  981 */         int ffrac = -256;
/*  982 */         for (int m = 0; m < 3; m++) {
/*      */           
/*  984 */           int xmid = Inlines.silk_RSHIFT_ROUND(xlo + xhi, 1);
/*  985 */           int ymid = silk_A2NLSF_eval_poly(p, xmid, dd);
/*      */ 
/*      */           
/*  988 */           if ((ylo <= 0 && ymid >= 0) || (ylo >= 0 && ymid <= 0)) {
/*      */             
/*  990 */             xhi = xmid;
/*  991 */             yhi = ymid;
/*      */           } else {
/*      */             
/*  994 */             xlo = xmid;
/*  995 */             ylo = ymid;
/*  996 */             ffrac = Inlines.silk_ADD_RSHIFT(ffrac, 128, m);
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 1001 */         if (Inlines.silk_abs(ylo) < 65536) {
/*      */           
/* 1003 */           int den = ylo - yhi;
/* 1004 */           int nom = Inlines.silk_LSHIFT(ylo, 5) + Inlines.silk_RSHIFT(den, 1);
/* 1005 */           if (den != 0) {
/* 1006 */             ffrac += Inlines.silk_DIV32(nom, den);
/*      */           }
/*      */         } else {
/*      */           
/* 1010 */           ffrac += Inlines.silk_DIV32(ylo, Inlines.silk_RSHIFT(ylo - yhi, 5));
/*      */         } 
/* 1012 */         arrayOfShort[root_ix] = (short)Inlines.silk_min_32(Inlines.silk_LSHIFT(k, 8) + ffrac, 32767);
/*      */         
/* 1014 */         Inlines.OpusAssert((arrayOfShort[root_ix] >= 0));
/*      */         
/* 1016 */         root_ix++;
/*      */         
/* 1018 */         if (root_ix >= d) {
/*      */           break;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1024 */         p = PQ[root_ix & 0x1];
/*      */ 
/*      */         
/* 1027 */         xlo = SilkTables.silk_LSFCosTab_Q12[k - 1];
/*      */         
/* 1029 */         ylo = Inlines.silk_LSHIFT(1 - (root_ix & 0x2), 12);
/*      */         continue;
/*      */       } 
/* 1032 */       k++;
/* 1033 */       xlo = xhi;
/* 1034 */       ylo = yhi;
/* 1035 */       thr = 0;
/*      */       
/* 1037 */       if (k > 128) {
/* 1038 */         i++;
/* 1039 */         if (i > 30) {
/*      */           
/* 1041 */           arrayOfShort[0] = (short)Inlines.silk_DIV32_16(32768, (short)(d + 1));
/* 1042 */           for (k = 1; k < d; k++) {
/* 1043 */             arrayOfShort[k] = (short)Inlines.silk_SMULBB(k + 1, arrayOfShort[0]);
/*      */           }
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 1049 */         Filters.silk_bwexpander_32(a_Q16, d, 65536 - Inlines.silk_SMULBB(10 + i, i));
/*      */ 
/*      */         
/* 1052 */         silk_A2NLSF_init(a_Q16, P, Q, dd);
/* 1053 */         p = P;
/*      */         
/* 1055 */         xlo = SilkTables.silk_LSFCosTab_Q12[0];
/*      */         
/* 1057 */         ylo = silk_A2NLSF_eval_poly(p, xlo, dd);
/* 1058 */         if (ylo < 0) {
/*      */           
/* 1060 */           arrayOfShort[0] = 0;
/* 1061 */           p = Q;
/*      */           
/* 1063 */           ylo = silk_A2NLSF_eval_poly(p, xlo, dd);
/* 1064 */           root_ix = 1;
/*      */         } else {
/*      */           
/* 1067 */           root_ix = 0;
/*      */         } 
/*      */         
/* 1070 */         k = 1;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_process_NLSFs(SilkChannelEncoder psEncC, short[][] PredCoef_Q12, short[] pNLSF_Q15, short[] prev_NLSFq_Q15) {
/* 1093 */     short[] pNLSF0_temp_Q15 = new short[16];
/* 1094 */     short[] pNLSFW_QW = new short[16];
/* 1095 */     short[] pNLSFW0_temp_QW = new short[16];
/*      */     
/* 1097 */     Inlines.OpusAssert((psEncC.speech_activity_Q8 >= 0));
/* 1098 */     Inlines.OpusAssert((psEncC.speech_activity_Q8 <= 256));
/* 1099 */     Inlines.OpusAssert((psEncC.useInterpolatedNLSFs == 1 || psEncC.indices.NLSFInterpCoef_Q2 == 4));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1109 */     int NLSF_mu_Q20 = Inlines.silk_SMLAWB(3146, -268434, psEncC.speech_activity_Q8);
/* 1110 */     if (psEncC.nb_subfr == 2)
/*      */     {
/* 1112 */       NLSF_mu_Q20 = Inlines.silk_ADD_RSHIFT(NLSF_mu_Q20, NLSF_mu_Q20, 1);
/*      */     }
/*      */     
/* 1115 */     Inlines.OpusAssert((NLSF_mu_Q20 > 0));
/* 1116 */     Inlines.OpusAssert((NLSF_mu_Q20 <= 5243));
/*      */ 
/*      */     
/* 1119 */     silk_NLSF_VQ_weights_laroia(pNLSFW_QW, pNLSF_Q15, psEncC.predictLPCOrder);
/*      */ 
/*      */     
/* 1122 */     boolean doInterpolate = (psEncC.useInterpolatedNLSFs == 1 && psEncC.indices.NLSFInterpCoef_Q2 < 4);
/* 1123 */     if (doInterpolate) {
/*      */       
/* 1125 */       Inlines.silk_interpolate(pNLSF0_temp_Q15, prev_NLSFq_Q15, pNLSF_Q15, psEncC.indices.NLSFInterpCoef_Q2, psEncC.predictLPCOrder);
/*      */ 
/*      */ 
/*      */       
/* 1129 */       silk_NLSF_VQ_weights_laroia(pNLSFW0_temp_QW, pNLSF0_temp_Q15, psEncC.predictLPCOrder);
/*      */ 
/*      */       
/* 1132 */       int i_sqr_Q15 = Inlines.silk_LSHIFT(Inlines.silk_SMULBB(psEncC.indices.NLSFInterpCoef_Q2, psEncC.indices.NLSFInterpCoef_Q2), 11);
/*      */       
/* 1134 */       for (int i = 0; i < psEncC.predictLPCOrder; i++) {
/* 1135 */         pNLSFW_QW[i] = (short)Inlines.silk_SMLAWB(Inlines.silk_RSHIFT(pNLSFW_QW[i], 1), pNLSFW0_temp_QW[i], i_sqr_Q15);
/* 1136 */         Inlines.OpusAssert((pNLSFW_QW[i] >= 1));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1141 */     silk_NLSF_encode(psEncC.indices.NLSFIndices, pNLSF_Q15, psEncC.psNLSF_CB, pNLSFW_QW, NLSF_mu_Q20, psEncC.NLSF_MSVQ_Survivors, psEncC.indices.signalType);
/*      */ 
/*      */ 
/*      */     
/* 1145 */     silk_NLSF2A(PredCoef_Q12[1], pNLSF_Q15, psEncC.predictLPCOrder);
/*      */     
/* 1147 */     if (doInterpolate) {
/*      */       
/* 1149 */       Inlines.silk_interpolate(pNLSF0_temp_Q15, prev_NLSFq_Q15, pNLSF_Q15, psEncC.indices.NLSFInterpCoef_Q2, psEncC.predictLPCOrder);
/*      */ 
/*      */ 
/*      */       
/* 1153 */       silk_NLSF2A(PredCoef_Q12[0], pNLSF0_temp_Q15, psEncC.predictLPCOrder);
/*      */     }
/*      */     else {
/*      */       
/* 1157 */       System.arraycopy(PredCoef_Q12[1], 0, PredCoef_Q12[0], 0, psEncC.predictLPCOrder);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\NLSF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */