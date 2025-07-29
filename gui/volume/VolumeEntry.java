/*    */ package de.maxhenkel.voicechat.gui.volume;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.widgets.ListScreenEntryBase;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_332;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_5253;
/*    */ 
/*    */ public abstract class VolumeEntry
/*    */   extends ListScreenEntryBase<VolumeEntry>
/*    */ {
/* 15 */   protected static final class_2561 OTHER_VOLUME = (class_2561)new class_2588("message.voicechat.other_volume");
/* 16 */   protected static final class_2561 OTHER_VOLUME_DESCRIPTION = (class_2561)new class_2588("message.voicechat.other_volume.description");
/* 17 */   protected static final class_2960 OTHER_VOLUME_ICON = new class_2960("voicechat", "textures/icons/other_volume.png");
/*    */   
/*    */   protected static final int SKIN_SIZE = 24;
/*    */   protected static final int PADDING = 4;
/* 21 */   protected static final int BG_FILL = class_5253.class_5254.method_27764(255, 74, 74, 74);
/* 22 */   protected static final int PLAYER_NAME_COLOR = class_5253.class_5254.method_27764(255, 255, 255, 255);
/*    */   
/*    */   protected final class_310 minecraft;
/*    */   protected final AdjustVolumesScreen screen;
/*    */   protected final AdjustVolumeSlider volumeSlider;
/*    */   
/*    */   public VolumeEntry(AdjustVolumesScreen screen, AdjustVolumeSlider.VolumeConfigEntry entry) {
/* 29 */     this.minecraft = class_310.method_1551();
/* 30 */     this.screen = screen;
/* 31 */     this.volumeSlider = new AdjustVolumeSlider(0, 0, 100, 20, entry);
/* 32 */     this.children.add(this.volumeSlider);
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25343(class_4587 poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta) {
/* 37 */     int skinX = left + 4;
/* 38 */     int skinY = top + (height - 24) / 2;
/* 39 */     int textX = skinX + 24 + 4;
/* 40 */     this.minecraft.field_1772.getClass(); int textY = top + (height - 9) / 2;
/*    */     
/* 42 */     class_332.method_25294(poseStack, left, top, left + width, top + height, BG_FILL);
/*    */     
/* 44 */     renderElement(poseStack, index, top, left, width, height, mouseX, mouseY, hovered, delta, skinX, skinY, textX, textY);
/*    */     
/* 46 */     this.volumeSlider.field_22760 = left + width - this.volumeSlider.method_25368() - 4;
/* 47 */     this.volumeSlider.field_22761 = top + (height - this.volumeSlider.method_25364()) / 2;
/* 48 */     this.volumeSlider.method_25394(poseStack, mouseX, mouseY, delta);
/*    */   }
/*    */   
/*    */   public abstract void renderElement(class_4587 paramclass_4587, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean, float paramFloat, int paramInt8, int paramInt9, int paramInt10, int paramInt11);
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\volume\VolumeEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */