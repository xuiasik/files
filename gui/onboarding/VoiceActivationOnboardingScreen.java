/*    */ package de.maxhenkel.voicechat.gui.onboarding;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.widgets.DenoiserButton;
/*    */ import de.maxhenkel.voicechat.gui.widgets.ImageButton;
/*    */ import de.maxhenkel.voicechat.gui.widgets.MicAmplificationSlider;
/*    */ import de.maxhenkel.voicechat.gui.widgets.MicTestButton;
/*    */ import de.maxhenkel.voicechat.gui.widgets.VoiceActivationSlider;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_339;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ 
/*    */ public class VoiceActivationOnboardingScreen extends OnboardingScreenBase {
/* 17 */   private static final class_2561 TITLE = (class_2561)(new class_2588("message.voicechat.onboarding.voice.title")).method_27692(class_124.field_1067);
/* 18 */   private static final class_2561 DESCRIPTION = (class_2561)new class_2588("message.voicechat.onboarding.voice.description");
/*    */   
/*    */   protected VoiceActivationSlider slider;
/*    */   protected MicTestButton micTestButton;
/*    */   
/*    */   public VoiceActivationOnboardingScreen(@Nullable class_437 previous) {
/* 24 */     super(TITLE, previous);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25426() {
/* 29 */     super.method_25426();
/*    */     
/* 31 */     int bottom = this.guiTop + this.contentHeight - 24 - 40;
/* 32 */     int space = 22;
/*    */     
/* 34 */     method_25411((class_339)new MicAmplificationSlider(this.guiLeft, bottom - space * 2, this.contentWidth, 20));
/* 35 */     method_25411((class_339)new DenoiserButton(this.guiLeft, bottom - space, this.contentWidth, 20));
/*    */     
/* 37 */     this.slider = new VoiceActivationSlider(this.guiLeft + 20 + 2, bottom, this.contentWidth - 20 - 2, 20);
/* 38 */     this.micTestButton = new MicTestButton(this.guiLeft, bottom, (MicTestButton.MicListener)this.slider);
/* 39 */     method_25411((class_339)this.micTestButton);
/* 40 */     method_25411((class_339)this.slider);
/*    */     
/* 42 */     addBackOrCancelButton();
/* 43 */     addNextButton();
/*    */   }
/*    */ 
/*    */   
/*    */   public class_437 getNextScreen() {
/* 48 */     return new FinalOnboardingScreen(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25394(class_4587 stack, int mouseX, int mouseY, float partialTicks) {
/* 53 */     super.method_25394(stack, mouseX, mouseY, partialTicks);
/*    */     
/* 55 */     renderTitle(stack, TITLE);
/* 56 */     renderMultilineText(stack, DESCRIPTION);
/*    */     
/* 58 */     class_2561 sliderTooltip = this.slider.getHoverText();
/* 59 */     if (this.slider.method_25367() && sliderTooltip != null) {
/* 60 */       method_25424(stack, sliderTooltip, mouseX, mouseY);
/* 61 */     } else if (this.micTestButton.method_25367()) {
/* 62 */       this.micTestButton.onTooltip((ImageButton)this.micTestButton, stack, mouseX, mouseY);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\onboarding\VoiceActivationOnboardingScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */