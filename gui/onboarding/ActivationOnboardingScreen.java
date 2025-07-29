/*    */ package de.maxhenkel.voicechat.gui.onboarding;
/*    */ 
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.voice.client.MicrophoneActivationType;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_339;
/*    */ import net.minecraft.class_4185;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ 
/*    */ public class ActivationOnboardingScreen
/*    */   extends OnboardingScreenBase {
/* 16 */   private static final class_2561 TITLE = (class_2561)(new class_2588("message.voicechat.onboarding.activation.title")).method_27692(class_124.field_1067);
/* 17 */   private static final class_2561 DESCRIPTION = (class_2561)(new class_2588("message.voicechat.onboarding.activation"))
/* 18 */     .method_27693("\n\n")
/* 19 */     .method_10852((class_2561)new class_2588("message.voicechat.onboarding.activation.ptt", new Object[] { (new class_2588("message.voicechat.onboarding.activation.ptt.name")).method_27695(new class_124[] { class_124.field_1067, class_124.field_1073
/* 20 */             }) })).method_27693("\n\n")
/* 21 */     .method_10852((class_2561)new class_2588("message.voicechat.onboarding.activation.voice", new Object[] { (new class_2588("message.voicechat.onboarding.activation.voice.name")).method_27695(new class_124[] { class_124.field_1067, class_124.field_1073 }) }));
/*    */   
/*    */   public ActivationOnboardingScreen(@Nullable class_437 previous) {
/* 24 */     super(TITLE, previous);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25426() {
/* 29 */     super.method_25426();
/*    */     
/* 31 */     class_4185 ptt = new class_4185(this.guiLeft, this.guiTop + this.contentHeight - 40 - 8, this.contentWidth / 2 - 4, 20, (class_2561)new class_2588("message.voicechat.onboarding.activation.ptt.name"), button -> {
/*    */           VoicechatClient.CLIENT_CONFIG.microphoneActivationType.set(MicrophoneActivationType.PTT).save();
/*    */           this.field_22787.method_1507(new PttOnboardingScreen(this));
/*    */         });
/* 35 */     method_25411((class_339)ptt);
/*    */     
/* 37 */     class_4185 voice = new class_4185(this.guiLeft + this.contentWidth / 2 + 4, this.guiTop + this.contentHeight - 40 - 8, this.contentWidth / 2 - 4, 20, (class_2561)new class_2588("message.voicechat.onboarding.activation.voice.name"), button -> {
/*    */           VoicechatClient.CLIENT_CONFIG.microphoneActivationType.set(MicrophoneActivationType.VOICE).save();
/*    */           this.field_22787.method_1507(new VoiceActivationOnboardingScreen(this));
/*    */         });
/* 41 */     method_25411((class_339)voice);
/*    */     
/* 43 */     addBackOrCancelButton(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25394(class_4587 stack, int mouseX, int mouseY, float partialTicks) {
/* 48 */     super.method_25394(stack, mouseX, mouseY, partialTicks);
/* 49 */     renderTitle(stack, TITLE);
/* 50 */     renderMultilineText(stack, DESCRIPTION);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\onboarding\ActivationOnboardingScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */