/*    */ package de.maxhenkel.voicechat.gui.onboarding;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_4185;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ 
/*    */ public class SkipOnboardingScreen
/*    */   extends OnboardingScreenBase {
/* 13 */   private static final class_2561 TITLE = (class_2561)(new class_2588("message.voicechat.onboarding.skip.title")).method_27692(class_124.field_1067);
/* 14 */   private static final class_2561 DESCRIPTION = (class_2561)new class_2588("message.voicechat.onboarding.skip.description");
/* 15 */   private static final class_2561 CONFIRM = (class_2561)new class_2588("message.voicechat.onboarding.confirm");
/*    */   
/*    */   public SkipOnboardingScreen(@Nullable class_437 previous) {
/* 18 */     super(TITLE, previous);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25426() {
/* 23 */     super.method_25426();
/*    */     
/* 25 */     addBackOrCancelButton();
/* 26 */     addPositiveButton(CONFIRM, button -> OnboardingManager.finishOnboarding());
/*    */   }
/*    */ 
/*    */   
/*    */   public class_437 getNextScreen() {
/* 31 */     return this.previous;
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25394(class_4587 stack, int mouseX, int mouseY, float partialTicks) {
/* 36 */     super.method_25394(stack, mouseX, mouseY, partialTicks);
/* 37 */     renderTitle(stack, TITLE);
/* 38 */     renderMultilineText(stack, DESCRIPTION);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\onboarding\SkipOnboardingScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */