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
/*    */ class LTPScaleControl
/*    */ {
/*    */   static void silk_LTP_scale_ctrl(SilkChannelEncoder psEnc, SilkEncoderControl psEncCtrl, int condCoding) {
/* 44 */     if (condCoding == 0) {
/*    */       
/* 46 */       int round_loss = psEnc.PacketLoss_perc + psEnc.nFramesPerPacket;
/* 47 */       psEnc.indices.LTP_scaleIndex = (byte)Inlines.silk_LIMIT(
/* 48 */           Inlines.silk_SMULWB(Inlines.silk_SMULBB(round_loss, psEncCtrl.LTPredCodGain_Q7), 51), 0, 2);
/*    */     } else {
/*    */       
/* 51 */       psEnc.indices.LTP_scaleIndex = 0;
/*    */     } 
/* 53 */     psEncCtrl.LTP_scale_Q14 = SilkTables.silk_LTPScales_table_Q14[psEnc.indices.LTP_scaleIndex];
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\LTPScaleControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */