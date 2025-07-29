/*    */ package de.maxhenkel.voicechat.integration.freecam;
/*    */ 
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.voice.client.PositionalAudioUtils;
/*    */ import net.minecraft.class_243;
/*    */ import net.minecraft.class_310;
/*    */ 
/*    */ public class FreecamUtil
/*    */ {
/* 10 */   private static final class_310 mc = class_310.method_1551();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isFreecamEnabled() {
/* 16 */     if (mc.field_1724 == null) {
/* 17 */       return false;
/*    */     }
/* 19 */     return (((FreecamMode)VoicechatClient.CLIENT_CONFIG.freecamMode.get()).equals(FreecamMode.PLAYER) && !mc.field_1724.method_7325() && !mc.field_1724.equals(mc.method_1560()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class_243 getReferencePoint() {
/* 28 */     if (mc.field_1724 == null) {
/* 29 */       return class_243.field_1353;
/*    */     }
/* 31 */     return isFreecamEnabled() ? mc.field_1724.method_5836(1.0F) : mc.field_1773.method_19418().method_19326();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static double getDistanceTo(class_243 pos) {
/* 43 */     return getReferencePoint().method_1022(pos);
/*    */   }
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
/*    */   public static float getDistanceVolume(float maxDistance, class_243 pos) {
/* 56 */     return PositionalAudioUtils.getDistanceVolume(maxDistance, getReferencePoint(), pos);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\integration\freecam\FreecamUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */