/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_364;
/*    */ import net.minecraft.class_4265;
/*    */ import net.minecraft.class_4587;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ public abstract class ListScreenListBase<T extends ListScreenEntryBase<T>> extends class_4265<T> {
/*    */   public ListScreenListBase(int width, int height, int top, int size) {
/* 11 */     super(class_310.method_1551(), width, height, top, top + height, size);
/*    */   }
/*    */   
/*    */   public void updateSize(int width, int height, int top) {
/* 15 */     method_25323(width, height, top, top + height);
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25394(class_4587 poseStack, int x, int y, float partialTicks) {
/* 20 */     double scale = this.field_22740.method_22683().method_4495();
/* 21 */     int scaledHeight = this.field_22740.method_22683().method_4502();
/* 22 */     RenderSystem.enableScissor(0, (int)((scaledHeight - this.field_19086) * scale), 1073741823, (int)(this.field_22743 * scale));
/* 23 */     super.method_25394(poseStack, x, y, partialTicks);
/* 24 */     RenderSystem.disableScissor();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\ListScreenListBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */