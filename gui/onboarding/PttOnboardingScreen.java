/*    */ package de.maxhenkel.voicechat.gui.onboarding;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.widgets.KeybindButton;
/*    */ import de.maxhenkel.voicechat.voice.client.KeyEvents;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_339;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_5348;
/*    */ 
/*    */ public class PttOnboardingScreen extends OnboardingScreenBase {
/* 15 */   private static final class_2561 TITLE = (class_2561)(new class_2588("message.voicechat.onboarding.ptt.title")).method_27692(class_124.field_1067);
/* 16 */   private static final class_2561 DESCRIPTION = (class_2561)new class_2588("message.voicechat.onboarding.ptt.description");
/* 17 */   private static final class_2561 BUTTON_DESCRIPTION = (class_2561)new class_2588("message.voicechat.onboarding.ptt.button_description");
/*    */   
/*    */   protected KeybindButton keybindButton;
/*    */   
/*    */   protected int keybindButtonPos;
/*    */   
/*    */   public PttOnboardingScreen(@Nullable class_437 previous) {
/* 24 */     super(TITLE, previous);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25426() {
/* 29 */     super.method_25426();
/*    */     
/* 31 */     this.keybindButtonPos = this.guiTop + this.contentHeight - 60 - 16 - 40;
/* 32 */     this.keybindButton = new KeybindButton(KeyEvents.KEY_PTT, this.guiLeft + 40, this.keybindButtonPos, this.contentWidth - 80, 20);
/* 33 */     method_25411((class_339)this.keybindButton);
/*    */     
/* 35 */     addBackOrCancelButton();
/* 36 */     addNextButton();
/*    */   }
/*    */ 
/*    */   
/*    */   public class_437 getNextScreen() {
/* 41 */     return new FinalOnboardingScreen(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25394(class_4587 stack, int mouseX, int mouseY, float partialTicks) {
/* 46 */     super.method_25394(stack, mouseX, mouseY, partialTicks);
/* 47 */     renderTitle(stack, TITLE);
/* 48 */     renderMultilineText(stack, DESCRIPTION);
/* 49 */     this.field_22793.getClass(); this.field_22793.method_27517(stack, BUTTON_DESCRIPTION.method_30937(), (this.field_22789 / 2 - this.field_22793.method_27525((class_5348)BUTTON_DESCRIPTION) / 2), (this.keybindButtonPos - 9 - 8), -1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean method_25422() {
/* 54 */     if (this.keybindButton.isListening()) {
/* 55 */       return false;
/*    */     }
/* 57 */     return super.method_25422();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\onboarding\PttOnboardingScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */