/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ 
/*    */ import de.maxhenkel.voicechat.configbuilder.entry.ConfigEntry;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2585;
/*    */ import net.minecraft.class_4264;
/*    */ 
/*    */ public abstract class EnumButton<T extends Enum<T>>
/*    */   extends class_4264 {
/*    */   protected ConfigEntry<T> entry;
/*    */   
/*    */   public EnumButton(int xIn, int yIn, int widthIn, int heightIn, ConfigEntry<T> entry) {
/* 13 */     super(xIn, yIn, widthIn, heightIn, (class_2561)new class_2585(""));
/* 14 */     this.entry = entry;
/* 15 */     updateText();
/*    */   }
/*    */   
/*    */   protected void updateText() {
/* 19 */     method_25355(getText((T)this.entry.get()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract class_2561 getText(T paramT);
/*    */ 
/*    */   
/*    */   protected void onUpdate(T type) {}
/*    */ 
/*    */   
/*    */   public void method_25306() {
/* 30 */     Enum enum_1 = (Enum)this.entry.get();
/* 31 */     Enum[] arrayOfEnum = (Enum[])enum_1.getClass().getEnumConstants();
/* 32 */     Enum enum_2 = arrayOfEnum[(enum_1.ordinal() + 1) % arrayOfEnum.length];
/* 33 */     this.entry.set(enum_2).save();
/* 34 */     updateText();
/* 35 */     onUpdate((T)enum_2);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\EnumButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */