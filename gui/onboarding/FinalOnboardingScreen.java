/*    */ package de.maxhenkel.voicechat.gui.onboarding;
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
/*    */ import de.maxhenkel.voicechat.voice.client.KeyEvents;
/*    */ import de.maxhenkel.voicechat.voice.client.MicrophoneActivationType;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2585;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_4185;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_5250;
/*    */ 
/*    */ public class FinalOnboardingScreen extends OnboardingScreenBase {
/* 17 */   private static final class_2561 TITLE = (class_2561)(new class_2588("message.voicechat.onboarding.final")).method_27692(class_124.field_1067);
/* 18 */   private static final class_2561 FINISH_SETUP = (class_2561)new class_2588("message.voicechat.onboarding.final.finish_setup");
/*    */   
/*    */   protected class_2561 description;
/*    */   
/*    */   public FinalOnboardingScreen(@Nullable class_437 previous) {
/* 23 */     super(TITLE, previous);
/* 24 */     this.description = (class_2561)new class_2585("");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25426() {
/* 29 */     super.method_25426();
/*    */ 
/*    */ 
/*    */     
/* 33 */     class_5250 text = (new class_2588("message.voicechat.onboarding.final.description.success", new Object[] { KeyEvents.KEY_VOICE_CHAT.method_16007().method_27661().method_27695(new class_124[] { class_124.field_1067, class_124.field_1073 }) })).method_27693("\n\n");
/*    */     
/* 35 */     if (((MicrophoneActivationType)VoicechatClient.CLIENT_CONFIG.microphoneActivationType.get()).equals(MicrophoneActivationType.PTT)) {
/*    */ 
/*    */       
/* 38 */       text = text.method_10852((class_2561)(new class_2588("message.voicechat.onboarding.final.description.ptt", new Object[] { KeyEvents.KEY_PTT.method_16007().method_27661().method_27695(new class_124[] { class_124.field_1067, class_124.field_1073 }) })).method_27692(class_124.field_1067)).method_27693("\n\n");
/*    */     }
/*    */     else {
/*    */       
/* 42 */       text = text.method_10852((class_2561)(new class_2588("message.voicechat.onboarding.final.description.voice", new Object[] { KeyEvents.KEY_MUTE.method_16007().method_27661().method_27695(new class_124[] { class_124.field_1067, class_124.field_1073 }) })).method_27692(class_124.field_1067)).method_27693("\n\n");
/*    */     } 
/*    */     
/* 45 */     this.description = (class_2561)text.method_10852((class_2561)new class_2588("message.voicechat.onboarding.final.description.configuration"));
/*    */     
/* 47 */     addBackOrCancelButton();
/* 48 */     addPositiveButton(FINISH_SETUP, button -> OnboardingManager.finishOnboarding());
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25394(class_4587 stack, int mouseX, int mouseY, float partialTicks) {
/* 53 */     super.method_25394(stack, mouseX, mouseY, partialTicks);
/* 54 */     renderTitle(stack, TITLE);
/* 55 */     renderMultilineText(stack, this.description);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
/* 60 */     if (keyCode == 256) {
/* 61 */       OnboardingManager.finishOnboarding();
/* 62 */       return true;
/*    */     } 
/* 64 */     if (keyCode == ClientCompatibilityManager.INSTANCE.getBoundKeyOf(KeyEvents.KEY_VOICE_CHAT).method_1444()) {
/* 65 */       OnboardingManager.finishOnboarding();
/* 66 */       this.field_22787.method_1507((class_437)new VoiceChatScreen());
/* 67 */       return true;
/*    */     } 
/*    */     
/* 70 */     return super.method_25404(keyCode, scanCode, modifiers);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean method_25422() {
/* 75 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\onboarding\FinalOnboardingScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */