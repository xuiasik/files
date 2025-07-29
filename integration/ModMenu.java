/*    */ package de.maxhenkel.voicechat.integration;
/*    */ 
/*    */ import com.terraformersmc.modmenu.api.ConfigScreenFactory;
/*    */ import com.terraformersmc.modmenu.api.ModMenuApi;
/*    */ import de.maxhenkel.voicechat.gui.VoiceChatSettingsScreen;
/*    */ import de.maxhenkel.voicechat.gui.onboarding.OnboardingManager;
/*    */ import de.maxhenkel.voicechat.integration.clothconfig.ClothConfig;
/*    */ import de.maxhenkel.voicechat.integration.clothconfig.ClothConfigIntegration;
/*    */ import net.minecraft.class_437;
/*    */ 
/*    */ public class ModMenu
/*    */   implements ModMenuApi {
/*    */   public ConfigScreenFactory<?> getModConfigScreenFactory() {
/* 14 */     return parent -> OnboardingManager.isOnboarding() ? OnboardingManager.getOnboardingScreen(parent) : (ClothConfig.isLoaded() ? ClothConfigIntegration.createConfigScreen(parent) : new VoiceChatSettingsScreen(parent));
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\integration\ModMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */