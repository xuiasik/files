/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_4587;
/*    */ 
/*    */ public class ToggleImageButton
/*    */   extends ImageButton
/*    */ {
/*    */   @Nullable
/*    */   protected Supplier<Boolean> stateSupplier;
/*    */   
/*    */   public ToggleImageButton(int x, int y, class_2960 texture, @Nullable Supplier<Boolean> stateSupplier, ImageButton.PressAction onPress, ImageButton.TooltipSupplier tooltipSupplier) {
/* 15 */     super(x, y, texture, onPress, tooltipSupplier);
/* 16 */     this.stateSupplier = stateSupplier;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderImage(class_4587 matrices, int mouseX, int mouseY, float delta) {
/* 21 */     if (this.stateSupplier == null) {
/*    */       return;
/*    */     }
/* 24 */     this.mc.method_1531().method_22813(this.texture);
/*    */     
/* 26 */     if (((Boolean)this.stateSupplier.get()).booleanValue()) {
/* 27 */       method_25290(matrices, this.field_22760 + 2, this.field_22761 + 2, 16.0F, 0.0F, 16, 16, 32, 32);
/*    */     } else {
/* 29 */       method_25290(matrices, this.field_22760 + 2, this.field_22761 + 2, 0.0F, 0.0F, 16, 16, 32, 32);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\ToggleImageButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */