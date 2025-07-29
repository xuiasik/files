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
/*     */ class GainQuantization
/*     */ {
/*     */   private static final int OFFSET = 2090;
/*     */   private static final int SCALE_Q16 = 2251;
/*     */   private static final int INV_SCALE_Q16 = 1907825;
/*     */   
/*     */   static void silk_gains_quant(byte[] ind, int[] gain_Q16, BoxedValueByte prev_ind, int conditional, int nb_subfr) {
/*  56 */     for (int k = 0; k < nb_subfr; k++) {
/*     */ 
/*     */       
/*  59 */       ind[k] = (byte)Inlines.silk_SMULWB(2251, Inlines.silk_lin2log(gain_Q16[k]) - 2090);
/*     */ 
/*     */       
/*  62 */       if (ind[k] < prev_ind.Val) {
/*  63 */         ind[k] = (byte)(ind[k] + 1);
/*     */       }
/*     */       
/*  66 */       ind[k] = (byte)Inlines.silk_LIMIT_int(ind[k], 0, 63);
/*     */ 
/*     */       
/*  69 */       if (k == 0 && conditional == 0) {
/*     */         
/*  71 */         ind[k] = (byte)Inlines.silk_LIMIT_int(ind[k], prev_ind.Val + -4, 63);
/*  72 */         prev_ind.Val = ind[k];
/*     */       } else {
/*     */         
/*  75 */         ind[k] = (byte)(ind[k] - prev_ind.Val);
/*     */ 
/*     */         
/*  78 */         int double_step_size_threshold = 8 + prev_ind.Val;
/*  79 */         if (ind[k] > double_step_size_threshold) {
/*  80 */           ind[k] = (byte)(double_step_size_threshold + Inlines.silk_RSHIFT(ind[k] - double_step_size_threshold + 1, 1));
/*     */         }
/*     */         
/*  83 */         ind[k] = (byte)Inlines.silk_LIMIT_int(ind[k], -4, 36);
/*     */ 
/*     */         
/*  86 */         if (ind[k] > double_step_size_threshold) {
/*  87 */           prev_ind.Val = (byte)(prev_ind.Val + (byte)(Inlines.silk_LSHIFT(ind[k], 1) - double_step_size_threshold));
/*     */         } else {
/*  89 */           prev_ind.Val = (byte)(prev_ind.Val + ind[k]);
/*     */         } 
/*     */ 
/*     */         
/*  93 */         ind[k] = (byte)(ind[k] - -4);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  98 */       gain_Q16[k] = Inlines.silk_log2lin(Inlines.silk_min_32(Inlines.silk_SMULWB(1907825, prev_ind.Val) + 2090, 3967));
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
/*     */   static void silk_gains_dequant(int[] gain_Q16, byte[] ind, BoxedValueByte prev_ind, int conditional, int nb_subfr) {
/* 119 */     for (int k = 0; k < nb_subfr; k++) {
/* 120 */       if (k == 0 && conditional == 0) {
/*     */         
/* 122 */         prev_ind.Val = (byte)Inlines.silk_max_int(ind[k], prev_ind.Val - 16);
/*     */       } else {
/*     */         
/* 125 */         int ind_tmp = ind[k] + -4;
/*     */ 
/*     */         
/* 128 */         int double_step_size_threshold = 8 + prev_ind.Val;
/* 129 */         if (ind_tmp > double_step_size_threshold) {
/* 130 */           prev_ind.Val = (byte)(prev_ind.Val + (byte)(Inlines.silk_LSHIFT(ind_tmp, 1) - double_step_size_threshold));
/*     */         } else {
/* 132 */           prev_ind.Val = (byte)(prev_ind.Val + (byte)ind_tmp);
/*     */         } 
/*     */       } 
/*     */       
/* 136 */       prev_ind.Val = (byte)Inlines.silk_LIMIT_int(prev_ind.Val, 0, 63);
/*     */ 
/*     */       
/* 139 */       gain_Q16[k] = Inlines.silk_log2lin(Inlines.silk_min_32(Inlines.silk_SMULWB(1907825, prev_ind.Val) + 2090, 3967));
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
/*     */   static int silk_gains_ID(byte[] ind, int nb_subfr) {
/* 154 */     int gainsID = 0;
/* 155 */     for (int k = 0; k < nb_subfr; k++) {
/* 156 */       gainsID = Inlines.silk_ADD_LSHIFT32(ind[k], gainsID, 8);
/*     */     }
/*     */     
/* 159 */     return gainsID;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\GainQuantization.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */