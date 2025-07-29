/*    */ package de.maxhenkel.voicechat.integration.clothconfig;
/*    */ 
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import de.maxhenkel.voicechat.gui.VoiceChatSettingsScreen;
/*    */ import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
/*    */ import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
/*    */ import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_437;
/*    */ 
/*    */ public class ClothConfig {
/* 12 */   private static final class_310 MC = class_310.method_1551();
/*    */   private static Boolean loaded;
/*    */   
/*    */   public static boolean isLoaded() {
/* 16 */     if (loaded == null) {
/* 17 */       loaded = Boolean.valueOf(checkLoaded());
/*    */     }
/* 19 */     return loaded.booleanValue();
/*    */   }
/*    */   
/*    */   private static boolean checkLoaded() {
/* 23 */     if (CommonCompatibilityManager.INSTANCE.isModLoaded("cloth-config2") || CommonCompatibilityManager.INSTANCE.isModLoaded("cloth-config") || CommonCompatibilityManager.INSTANCE.isModLoaded("cloth-config2") || CommonCompatibilityManager.INSTANCE.isModLoaded("cloth_config")) {
/*    */       try {
/* 25 */         Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder");
/* 26 */         Voicechat.LOGGER.info("Using Cloth Config GUI", new Object[0]);
/* 27 */         return true;
/* 28 */       } catch (Exception e) {
/* 29 */         Voicechat.LOGGER.warn("Failed to load Cloth Config", new Object[] { e });
/*    */       } 
/*    */     }
/* 32 */     return false;
/*    */   }
/*    */   
/*    */   public static void init() {
/* 36 */     if (isLoaded()) {
/* 37 */       ClientCompatibilityManager.INSTANCE.onClientTick(ClothConfig::onTick);
/*    */     }
/*    */   }
/*    */   
/*    */   private static void onTick() {
/* 42 */     if (isLoaded() && 
/* 43 */       MC.field_1755 instanceof ClothConfigScreen) {
/* 44 */       ClothConfigScreen screen = (ClothConfigScreen)MC.field_1755;
/* 45 */       if (screen.getSelectedCategory().equals(ClothConfigIntegration.OTHER_SETTINGS)) {
/* 46 */         screen.selectedCategoryIndex = 0;
/* 47 */         MC.method_1507((class_437)new VoiceChatSettingsScreen(MC.field_1755));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\integration\clothconfig\ClothConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */