/*    */ package de.maxhenkel.voicechat.gui.audiodevice;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.widgets.ListScreenEntryBase;
/*    */ import java.util.function.Supplier;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_332;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_5253;
/*    */ import net.minecraft.class_5348;
/*    */ 
/*    */ public class AudioDeviceEntry
/*    */   extends ListScreenEntryBase<AudioDeviceEntry>
/*    */ {
/* 17 */   protected static final class_2960 SELECTED = new class_2960("voicechat", "textures/icons/device_selected.png");
/*    */   
/*    */   protected static final int PADDING = 4;
/* 20 */   protected static final int BG_FILL = class_5253.class_5254.method_27764(255, 74, 74, 74);
/* 21 */   protected static final int BG_FILL_HOVERED = class_5253.class_5254.method_27764(255, 90, 90, 90);
/* 22 */   protected static final int BG_FILL_SELECTED = class_5253.class_5254.method_27764(255, 40, 40, 40);
/* 23 */   protected static final int DEVICE_NAME_COLOR = class_5253.class_5254.method_27764(255, 255, 255, 255);
/*    */   
/*    */   protected final class_310 minecraft;
/*    */   protected final String device;
/*    */   protected final class_2561 name;
/*    */   @Nullable
/*    */   protected final class_2960 icon;
/*    */   protected final Supplier<Boolean> isSelected;
/*    */   
/*    */   public AudioDeviceEntry(String device, class_2561 name, @Nullable class_2960 icon, Supplier<Boolean> isSelected) {
/* 33 */     this.device = device;
/* 34 */     this.icon = icon;
/* 35 */     this.isSelected = isSelected;
/* 36 */     this.name = name;
/* 37 */     this.minecraft = class_310.method_1551();
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25343(class_4587 poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta) {
/* 42 */     boolean selected = ((Boolean)this.isSelected.get()).booleanValue();
/* 43 */     if (selected) {
/* 44 */       class_332.method_25294(poseStack, left, top, left + width, top + height, BG_FILL_SELECTED);
/* 45 */     } else if (hovered) {
/* 46 */       class_332.method_25294(poseStack, left, top, left + width, top + height, BG_FILL_HOVERED);
/*    */     } else {
/* 48 */       class_332.method_25294(poseStack, left, top, left + width, top + height, BG_FILL);
/*    */     } 
/*    */     
/* 51 */     if (this.icon != null) {
/* 52 */       this.minecraft.method_1531().method_22813(this.icon);
/* 53 */       class_332.method_25290(poseStack, left + 4, top + height / 2 - 8, 16.0F, 16.0F, 16, 16, 16, 16);
/*    */     } 
/* 55 */     if (selected) {
/* 56 */       this.minecraft.method_1531().method_22813(SELECTED);
/* 57 */       class_332.method_25290(poseStack, left + 4, top + height / 2 - 8, 16.0F, 16.0F, 16, 16, 16, 16);
/*    */     } 
/*    */     
/* 60 */     float deviceWidth = this.minecraft.field_1772.method_27525((class_5348)this.name);
/* 61 */     float space = (width - 4 - 16 - 4 - 4);
/* 62 */     float scale = Math.min(space / deviceWidth, 1.0F);
/*    */     
/* 64 */     poseStack.method_22903();
/* 65 */     this.minecraft.field_1772.getClass(); poseStack.method_22904((left + 4 + 16 + 4), ((top + height / 2) - 9.0F * scale / 2.0F), 0.0D);
/* 66 */     poseStack.method_22905(scale, scale, 1.0F);
/*    */     
/* 68 */     this.minecraft.field_1772.method_30883(poseStack, this.name, 0.0F, 0.0F, DEVICE_NAME_COLOR);
/* 69 */     poseStack.method_22909();
/*    */   }
/*    */   
/*    */   public String getDevice() {
/* 73 */     return this.device;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\audiodevice\AudioDeviceEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */