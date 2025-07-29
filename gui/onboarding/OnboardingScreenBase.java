/*    */ package de.maxhenkel.voicechat.gui.onboarding;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_339;
/*    */ import net.minecraft.class_4185;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_5348;
/*    */ import net.minecraft.class_5481;
/*    */ 
/*    */ public abstract class OnboardingScreenBase extends class_437 {
/* 15 */   public static final class_2561 NEXT = (class_2561)new class_2588("message.voicechat.onboarding.next");
/* 16 */   public static final class_2561 BACK = (class_2561)new class_2588("message.voicechat.onboarding.back");
/* 17 */   public static final class_2561 CANCEL = (class_2561)new class_2588("message.voicechat.onboarding.cancel");
/*    */   
/*    */   protected static final int TEXT_COLOR = -1;
/*    */   
/*    */   protected static final int PADDING = 8;
/*    */   
/*    */   protected static final int SMALL_PADDING = 2;
/*    */   protected static final int BUTTON_HEIGHT = 20;
/*    */   protected int contentWidth;
/*    */   protected int guiLeft;
/*    */   protected int guiTop;
/*    */   protected int contentHeight;
/*    */   @Nullable
/*    */   protected class_437 previous;
/*    */   
/*    */   public OnboardingScreenBase(class_2561 title, @Nullable class_437 previous) {
/* 33 */     super(title);
/* 34 */     this.previous = previous;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25426() {
/* 39 */     super.method_25426();
/*    */     
/* 41 */     this.contentWidth = this.field_22789 / 2;
/* 42 */     this.guiLeft = (this.field_22789 - this.contentWidth) / 2;
/* 43 */     this.guiTop = 20;
/* 44 */     this.contentHeight = this.field_22790 - this.guiTop * 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25394(class_4587 stack, int mouseX, int mouseY, float partialTicks) {
/* 49 */     method_25420(stack);
/* 50 */     super.method_25394(stack, mouseX, mouseY, partialTicks);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public class_437 getNextScreen() {
/* 55 */     return null;
/*    */   }
/*    */   
/*    */   protected void addPositiveButton(class_2561 text, class_4185.class_4241 onPress) {
/* 59 */     class_4185 nextButton = new class_4185(this.guiLeft + this.contentWidth / 2 + 4, this.guiTop + this.contentHeight - 20, this.contentWidth / 2 - 4, 20, text, onPress);
/* 60 */     method_25411((class_339)nextButton);
/*    */   }
/*    */   
/*    */   protected void addNextButton() {
/* 64 */     addPositiveButton(NEXT, button -> this.field_22787.method_1507(getNextScreen()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addBackOrCancelButton(boolean big) {
/* 70 */     class_2561 text = CANCEL;
/* 71 */     if (this.previous instanceof OnboardingScreenBase) {
/* 72 */       text = BACK;
/*    */     }
/* 74 */     class_4185 cancel = new class_4185(this.guiLeft, this.guiTop + this.contentHeight - 20, big ? this.contentWidth : (this.contentWidth / 2 - 4), 20, text, button -> this.field_22787.method_1507(this.previous));
/*    */ 
/*    */     
/* 77 */     method_25411((class_339)cancel);
/*    */   }
/*    */   
/*    */   protected void addBackOrCancelButton() {
/* 81 */     addBackOrCancelButton(false);
/*    */   }
/*    */   
/*    */   protected void renderTitle(class_4587 stack, class_2561 titleComponent) {
/* 85 */     int titleWidth = this.field_22793.method_27525((class_5348)titleComponent);
/* 86 */     this.field_22793.method_27517(stack, titleComponent.method_30937(), (this.field_22789 / 2 - titleWidth / 2), this.guiTop, -1);
/*    */   }
/*    */   
/*    */   protected void renderMultilineText(class_4587 stack, class_2561 textComponent) {
/* 90 */     List<class_5481> text = this.field_22793.method_1728((class_5348)textComponent, this.contentWidth);
/*    */     
/* 92 */     for (int i = 0; i < text.size(); i++) {
/* 93 */       class_5481 line = text.get(i);
/* 94 */       this.field_22793.getClass(); this.field_22793.getClass(); this.field_22793.method_27517(stack, line, (this.field_22789 / 2 - this.field_22793.method_30880(line) / 2), (this.guiTop + 9 + 20 + i * (9 + 1)), -1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\onboarding\OnboardingScreenBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */