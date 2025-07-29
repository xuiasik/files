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
/*    */ 
/*    */ 
/*    */ class DecodePitch
/*    */ {
/*    */   static void silk_decode_pitch(short lagIndex, byte contourIndex, int[] pitch_lags, int Fs_kHz, int nb_subfr) {
/*    */     byte[][] Lag_CB_ptr;
/* 46 */     if (Fs_kHz == 8) {
/* 47 */       if (nb_subfr == 4) {
/* 48 */         Lag_CB_ptr = SilkTables.silk_CB_lags_stage2;
/*    */       } else {
/* 50 */         Inlines.OpusAssert((nb_subfr == 2));
/* 51 */         Lag_CB_ptr = SilkTables.silk_CB_lags_stage2_10_ms;
/*    */       } 
/* 53 */     } else if (nb_subfr == 4) {
/* 54 */       Lag_CB_ptr = SilkTables.silk_CB_lags_stage3;
/*    */     } else {
/* 56 */       Inlines.OpusAssert((nb_subfr == 2));
/* 57 */       Lag_CB_ptr = SilkTables.silk_CB_lags_stage3_10_ms;
/*    */     } 
/*    */     
/* 60 */     int min_lag = Inlines.silk_SMULBB(2, Fs_kHz);
/* 61 */     int max_lag = Inlines.silk_SMULBB(18, Fs_kHz);
/* 62 */     int lag = min_lag + lagIndex;
/*    */     
/* 64 */     for (int k = 0; k < nb_subfr; k++) {
/* 65 */       pitch_lags[k] = lag + Lag_CB_ptr[k][contourIndex];
/* 66 */       pitch_lags[k] = Inlines.silk_LIMIT(pitch_lags[k], min_lag, max_lag);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\DecodePitch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */