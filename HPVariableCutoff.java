/*    */ package de.maxhenkel.voicechat.concentus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class HPVariableCutoff
/*    */ {
/*    */   static void silk_HP_variable_cutoff(SilkChannelEncoder[] state_Fxx) {
/* 43 */     SilkChannelEncoder psEncC1 = state_Fxx[0];
/*    */ 
/*    */     
/* 46 */     if (psEncC1.prevSignalType == 2) {
/*    */       
/* 48 */       int pitch_freq_Hz_Q16 = Inlines.silk_DIV32_16(Inlines.silk_LSHIFT(Inlines.silk_MUL(psEncC1.fs_kHz, 1000), 16), psEncC1.prevLag);
/* 49 */       int pitch_freq_log_Q7 = Inlines.silk_lin2log(pitch_freq_Hz_Q16) - 2048;
/*    */ 
/*    */       
/* 52 */       int quality_Q15 = psEncC1.input_quality_bands_Q15[0];
/* 53 */       pitch_freq_log_Q7 = Inlines.silk_SMLAWB(pitch_freq_log_Q7, Inlines.silk_SMULWB(Inlines.silk_LSHIFT(-quality_Q15, 2), quality_Q15), pitch_freq_log_Q7 - 
/* 54 */           Inlines.silk_lin2log(3932160) - 2048);
/*    */ 
/*    */       
/* 57 */       int delta_freq_Q7 = pitch_freq_log_Q7 - Inlines.silk_RSHIFT(psEncC1.variable_HP_smth1_Q15, 8);
/* 58 */       if (delta_freq_Q7 < 0)
/*    */       {
/* 60 */         delta_freq_Q7 = Inlines.silk_MUL(delta_freq_Q7, 3);
/*    */       }
/*    */ 
/*    */       
/* 64 */       delta_freq_Q7 = Inlines.silk_LIMIT_32(delta_freq_Q7, -51, 51);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 70 */       psEncC1.variable_HP_smth1_Q15 = Inlines.silk_SMLAWB(psEncC1.variable_HP_smth1_Q15, 
/* 71 */           Inlines.silk_SMULBB(psEncC1.speech_activity_Q8, delta_freq_Q7), 6554);
/*    */ 
/*    */       
/* 74 */       psEncC1.variable_HP_smth1_Q15 = Inlines.silk_LIMIT_32(psEncC1.variable_HP_smth1_Q15, 
/* 75 */           Inlines.silk_LSHIFT(Inlines.silk_lin2log(60), 8), 
/* 76 */           Inlines.silk_LSHIFT(Inlines.silk_lin2log(100), 8));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\HPVariableCutoff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */