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
/*    */ class SilkEncoder
/*    */ {
/* 39 */   final SilkChannelEncoder[] state_Fxx = new SilkChannelEncoder[2];
/* 40 */   final StereoEncodeState sStereo = new StereoEncodeState();
/* 41 */   int nBitsUsedLBRR = 0;
/* 42 */   int nBitsExceeded = 0;
/* 43 */   int nChannelsAPI = 0;
/* 44 */   int nChannelsInternal = 0;
/* 45 */   int nPrevChannelsInternal = 0;
/* 46 */   int timeSinceSwitchAllowed_ms = 0;
/* 47 */   int allowBandwidthSwitch = 0;
/* 48 */   int prev_decode_only_middle = 0;
/*    */   
/*    */   SilkEncoder() {
/* 51 */     for (int c = 0; c < 2; c++) {
/* 52 */       this.state_Fxx[c] = new SilkChannelEncoder();
/*    */     }
/*    */   }
/*    */   
/*    */   void Reset() {
/* 57 */     for (int c = 0; c < 2; c++) {
/* 58 */       this.state_Fxx[c].Reset();
/*    */     }
/*    */     
/* 61 */     this.sStereo.Reset();
/* 62 */     this.nBitsUsedLBRR = 0;
/* 63 */     this.nBitsExceeded = 0;
/* 64 */     this.nChannelsAPI = 0;
/* 65 */     this.nChannelsInternal = 0;
/* 66 */     this.nPrevChannelsInternal = 0;
/* 67 */     this.timeSinceSwitchAllowed_ms = 0;
/* 68 */     this.allowBandwidthSwitch = 0;
/* 69 */     this.prev_decode_only_middle = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static int silk_init_encoder(SilkChannelEncoder psEnc) {
/* 79 */     int ret = 0;
/*    */ 
/*    */     
/* 82 */     psEnc.Reset();
/*    */     
/* 84 */     psEnc.variable_HP_smth1_Q15 = Inlines.silk_LSHIFT(Inlines.silk_lin2log(3932160) - 2048, 8);
/* 85 */     psEnc.variable_HP_smth2_Q15 = psEnc.variable_HP_smth1_Q15;
/*    */ 
/*    */     
/* 88 */     psEnc.first_frame_after_reset = 1;
/*    */ 
/*    */     
/* 91 */     ret += VoiceActivityDetection.silk_VAD_Init(psEnc.sVAD);
/*    */     
/* 93 */     return ret;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SilkEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */