/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ 
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_357;
/*    */ 
/*    */ public abstract class DebouncedSlider
/*    */   extends class_357
/*    */ {
/*    */   private boolean dragged;
/*    */   private double lastValue;
/*    */   
/*    */   public DebouncedSlider(int i, int j, int k, int l, class_2561 component, double d) {
/* 13 */     super(i, j, k, l, component, d);
/* 14 */     this.lastValue = d;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean method_25404(int keyCode, int j, int k) {
/* 19 */     boolean result = super.method_25404(keyCode, j, k);
/* 20 */     if (keyCode == 263 || keyCode == 262) {
/* 21 */       applyDebouncedInternal();
/*    */     }
/* 23 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25348(double d, double e) {
/* 28 */     super.method_25348(d, e);
/* 29 */     applyDebouncedInternal();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25349(double d, double e, double f, double g) {
/* 34 */     super.method_25349(d, e, f, g);
/* 35 */     this.dragged = true;
/* 36 */     if (this.field_22753 >= 1.0D || this.field_22753 <= 0.0D) {
/* 37 */       applyDebouncedInternal();
/* 38 */       this.dragged = false;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25357(double d, double e) {
/* 44 */     super.method_25357(d, e);
/* 45 */     if (this.dragged) {
/* 46 */       applyDebouncedInternal();
/* 47 */       this.dragged = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void applyDebouncedInternal() {
/* 52 */     if (this.field_22753 == this.lastValue) {
/*    */       return;
/*    */     }
/* 55 */     this.lastValue = this.field_22753;
/* 56 */     applyDebounced();
/*    */   }
/*    */   
/*    */   public abstract void applyDebounced();
/*    */   
/*    */   protected void method_25344() {}
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\DebouncedSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */