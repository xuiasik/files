/*    */ package de.maxhenkel.voicechat.gui.onboarding;
/*    */ 
/*    */ import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_339;
/*    */ import net.minecraft.class_4185;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ 
/*    */ public class IntroductionOnboardingScreen
/*    */   extends OnboardingScreenBase {
/* 15 */   private static final class_2561 TITLE = (class_2561)(new class_2588("message.voicechat.onboarding.introduction.title", new Object[] { CommonCompatibilityManager.INSTANCE.getModName() })).method_27692(class_124.field_1067);
/* 16 */   private static final class_2561 DESCRIPTION = (class_2561)new class_2588("message.voicechat.onboarding.introduction.description");
/* 17 */   private static final class_2561 SKIP = (class_2561)new class_2588("message.voicechat.onboarding.introduction.skip");
/*    */   
/*    */   public IntroductionOnboardingScreen(@Nullable class_437 previous) {
/* 20 */     super(TITLE, previous);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25426() {
/* 25 */     super.method_25426();
/*    */     
/* 27 */     class_4185 skipButton = new class_4185(this.guiLeft, this.guiTop + this.contentHeight - 40 - 8, this.contentWidth, 20, SKIP, button -> this.field_22787.method_1507(new SkipOnboardingScreen(this)));
/*    */ 
/*    */     
/* 30 */     method_25411((class_339)skipButton);
/*    */     
/* 32 */     addBackOrCancelButton();
/* 33 */     addNextButton();
/*    */   }
/*    */ 
/*    */   
/*    */   public class_437 getNextScreen() {
/* 38 */     return new MicOnboardingScreen(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25394(class_4587 stack, int mouseX, int mouseY, float partialTicks) {
/* 43 */     super.method_25394(stack, mouseX, mouseY, partialTicks);
/* 44 */     renderTitle(stack, TITLE);
/* 45 */     renderMultilineText(stack, DESCRIPTION);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\onboarding\IntroductionOnboardingScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */