/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ 
/*    */ import de.maxhenkel.voicechat.configbuilder.entry.ConfigEntry;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2585;
/*    */ import net.minecraft.class_4264;
/*    */ 
/*    */ public class BooleanConfigButton
/*    */   extends class_4264
/*    */ {
/*    */   protected ConfigEntry<Boolean> entry;
/*    */   protected Function<Boolean, class_2561> component;
/*    */   
/*    */   public BooleanConfigButton(int x, int y, int width, int height, ConfigEntry<Boolean> entry, Function<Boolean, class_2561> component) {
/* 16 */     super(x, y, width, height, (class_2561)new class_2585(""));
/* 17 */     this.entry = entry;
/* 18 */     this.component = component;
/* 19 */     updateText();
/*    */   }
/*    */   
/*    */   private void updateText() {
/* 23 */     method_25355(this.component.apply(this.entry.get()));
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25306() {
/* 28 */     this.entry.set(Boolean.valueOf(!((Boolean)this.entry.get()).booleanValue())).save();
/* 29 */     updateText();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\BooleanConfigButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */