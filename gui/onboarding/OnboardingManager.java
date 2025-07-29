/*    */ package de.maxhenkel.voicechat.gui.onboarding;
/*    */ 
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.voice.client.ChatUtils;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*    */ import de.maxhenkel.voicechat.voice.client.KeyEvents;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_437;
/*    */ 
/*    */ public class OnboardingManager
/*    */ {
/* 16 */   private static final class_310 MC = class_310.method_1551();
/*    */   
/*    */   public static boolean isOnboarding() {
/* 19 */     return !((Boolean)VoicechatClient.CLIENT_CONFIG.onboardingFinished.get()).booleanValue();
/*    */   }
/*    */   
/*    */   public static void startOnboarding(@Nullable class_437 parent) {
/* 23 */     MC.method_1507(getOnboardingScreen(parent));
/*    */   }
/*    */   
/*    */   public static class_437 getOnboardingScreen(@Nullable class_437 parent) {
/* 27 */     return new IntroductionOnboardingScreen(parent);
/*    */   }
/*    */   
/*    */   public static void finishOnboarding() {
/* 31 */     VoicechatClient.CLIENT_CONFIG.muted.set(Boolean.valueOf(true)).save();
/* 32 */     VoicechatClient.CLIENT_CONFIG.disabled.set(Boolean.valueOf(false)).save();
/* 33 */     VoicechatClient.CLIENT_CONFIG.onboardingFinished.set(Boolean.valueOf(true)).save();
/* 34 */     ClientManager.getPlayerStateManager().onFinishOnboarding();
/* 35 */     MC.method_1507(null);
/*    */   }
/*    */   
/*    */   public static void onConnecting() {
/* 39 */     if (!isOnboarding()) {
/*    */       return;
/*    */     }
/* 42 */     ChatUtils.sendModMessage((class_2561)new class_2588("message.voicechat.set_up", new Object[] { KeyEvents.KEY_VOICE_CHAT
/* 43 */             .method_16007().method_27661().method_27695(new class_124[] { class_124.field_1067, class_124.field_1073 }) }));
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\onboarding\OnboardingManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */