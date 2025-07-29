/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2585;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_4264;
/*    */ import net.minecraft.class_4587;
/*    */ 
/*    */ public class ImageButton
/*    */   extends class_4264 {
/*    */   protected class_310 mc;
/*    */   protected class_2960 texture;
/*    */   @Nullable
/*    */   protected PressAction onPress;
/*    */   protected TooltipSupplier tooltipSupplier;
/*    */   
/*    */   public ImageButton(int x, int y, class_2960 texture, @Nullable PressAction onPress, TooltipSupplier tooltipSupplier) {
/* 20 */     super(x, y, 20, 20, (class_2561)new class_2585(""));
/* 21 */     this.mc = class_310.method_1551();
/* 22 */     this.texture = texture;
/* 23 */     this.onPress = onPress;
/* 24 */     this.tooltipSupplier = tooltipSupplier;
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25306() {
/* 29 */     if (this.onPress != null) {
/* 30 */       this.onPress.onPress(this);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void renderImage(class_4587 matrices, int mouseX, int mouseY, float delta) {
/* 35 */     this.mc.method_1531().method_22813(this.texture);
/* 36 */     method_25290(matrices, this.field_22760 + 2, this.field_22761 + 2, 0.0F, 0.0F, 16, 16, 16, 16);
/*    */   }
/*    */   
/*    */   protected boolean shouldRenderTooltip() {
/* 40 */     return this.field_22762;
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25359(class_4587 matrices, int mouseX, int mouseY, float delta) {
/* 45 */     super.method_25359(matrices, mouseX, mouseY, delta);
/* 46 */     renderImage(matrices, mouseX, mouseY, delta);
/*    */     
/* 48 */     if (shouldRenderTooltip()) {
/* 49 */       method_25352(matrices, mouseX, mouseY);
/*    */     }
/*    */   }
/*    */   
/*    */   public void method_25352(class_4587 matrices, int mouseX, int mouseY) {
/* 54 */     this.tooltipSupplier.onTooltip(this, matrices, mouseX, mouseY);
/*    */   }
/*    */   
/*    */   public static interface PressAction {
/*    */     void onPress(ImageButton param1ImageButton);
/*    */   }
/*    */   
/*    */   public static interface TooltipSupplier {
/*    */     void onTooltip(ImageButton param1ImageButton, class_4587 param1class_4587, int param1Int1, int param1Int2);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\ImageButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */