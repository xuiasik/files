/*     */ package de.maxhenkel.voicechat.gui.widgets;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.class_124;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2585;
/*     */ import net.minecraft.class_304;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_3675;
/*     */ import net.minecraft.class_4264;
/*     */ import net.minecraft.class_5250;
/*     */ 
/*     */ 
/*     */ public class KeybindButton
/*     */   extends class_4264
/*     */ {
/*  17 */   private static final class_310 mc = class_310.method_1551();
/*     */   
/*     */   protected class_304 keyMapping;
/*     */   @Nullable
/*     */   protected class_2561 description;
/*     */   protected boolean listening;
/*     */   
/*     */   public KeybindButton(class_304 mapping, int x, int y, int width, int height, @Nullable class_2561 description) {
/*  25 */     super(x, y, width, height, class_2585.field_24366);
/*  26 */     this.keyMapping = mapping;
/*  27 */     this.description = description;
/*  28 */     updateText();
/*     */   }
/*     */   
/*     */   public KeybindButton(class_304 mapping, int x, int y, int width, int height) {
/*  32 */     this(mapping, x, y, width, height, (class_2561)null);
/*     */   }
/*     */   
/*     */   protected void updateText() {
/*     */     class_5250 text;
/*  37 */     if (this.listening) {
/*  38 */       text = (new class_2585("> ")).method_10852((class_2561)getText(this.keyMapping).method_27661().method_27695(new class_124[] { class_124.field_1068, class_124.field_1073 })).method_27693(" <").method_27692(class_124.field_1054);
/*     */     } else {
/*  40 */       text = getText(this.keyMapping).method_27661();
/*     */     } 
/*     */     
/*  43 */     if (this.description != null) {
/*  44 */       text = this.description.method_27661().method_27693(": ").method_10852((class_2561)text);
/*     */     }
/*     */     
/*  47 */     method_25355((class_2561)text);
/*     */   }
/*     */   
/*     */   private static class_2561 getText(class_304 keyMapping) {
/*  51 */     return keyMapping.method_16007();
/*     */   }
/*     */   
/*     */   public boolean method_25367() {
/*  55 */     return this.field_22762;
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25306() {
/*  60 */     this.listening = true;
/*  61 */     updateText();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25402(double x, double y, int button) {
/*  66 */     if (this.listening) {
/*  67 */       mc.field_1690.method_1641(this.keyMapping, class_3675.class_307.field_1672.method_1447(button));
/*  68 */       this.listening = false;
/*  69 */       updateText();
/*  70 */       return true;
/*     */     } 
/*  72 */     return super.method_25402(x, y, button);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean method_25404(int key, int scanCode, int modifiers) {
/*  78 */     if (this.listening) {
/*  79 */       if (key == 256) {
/*  80 */         mc.field_1690.method_1641(this.keyMapping, class_3675.field_16237);
/*     */       } else {
/*  82 */         mc.field_1690.method_1641(this.keyMapping, class_3675.method_15985(key, scanCode));
/*     */       } 
/*  84 */       this.listening = false;
/*  85 */       updateText();
/*  86 */       return true;
/*     */     } 
/*  88 */     return super.method_25404(key, scanCode, modifiers);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_16803(int key, int scanCode, int modifiers) {
/*  93 */     if (this.listening && key == 256) {
/*  94 */       return true;
/*     */     }
/*  96 */     return super.method_16803(key, scanCode, modifiers);
/*     */   }
/*     */   
/*     */   public boolean isListening() {
/* 100 */     return this.listening;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\KeybindButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */