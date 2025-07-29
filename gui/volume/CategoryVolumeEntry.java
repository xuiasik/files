/*    */ package de.maxhenkel.voicechat.gui.volume;
/*    */ 
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.plugins.impl.VolumeCategoryImpl;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2585;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_332;
/*    */ import net.minecraft.class_4587;
/*    */ 
/*    */ public class CategoryVolumeEntry extends VolumeEntry {
/*    */   protected final VolumeCategoryImpl category;
/*    */   protected final class_2960 texture;
/*    */   
/*    */   public CategoryVolumeEntry(VolumeCategoryImpl category, AdjustVolumesScreen screen) {
/* 17 */     super(screen, new CategoryVolumeConfigEntry(category.getId()));
/* 18 */     this.category = category;
/* 19 */     this.texture = ClientManager.getCategoryManager().getTexture(category.getId(), OTHER_VOLUME_ICON);
/*    */   }
/*    */   
/*    */   public VolumeCategoryImpl getCategory() {
/* 23 */     return this.category;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderElement(class_4587 poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta, int skinX, int skinY, int textX, int textY) {
/* 28 */     this.minecraft.method_1531().method_22813(this.texture);
/* 29 */     class_332.method_25293(poseStack, skinX, skinY, 24, 24, 16.0F, 16.0F, 16, 16, 16, 16);
/* 30 */     this.minecraft.field_1772.method_30883(poseStack, (class_2561)new class_2585(this.category.getName()), textX, textY, PLAYER_NAME_COLOR);
/* 31 */     if (hovered && this.category.getDescription() != null) {
/* 32 */       this.screen.postRender(() -> this.screen.method_25424(poseStack, (class_2561)new class_2585(this.category.getDescription()), mouseX, mouseY));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private static class CategoryVolumeConfigEntry
/*    */     implements AdjustVolumeSlider.VolumeConfigEntry
/*    */   {
/*    */     private final String category;
/*    */     
/*    */     public CategoryVolumeConfigEntry(String category) {
/* 43 */       this.category = category;
/*    */     }
/*    */ 
/*    */     
/*    */     public void save(double value) {
/* 48 */       VoicechatClient.VOLUME_CONFIG.setCategoryVolume(this.category, value);
/* 49 */       VoicechatClient.VOLUME_CONFIG.save();
/*    */     }
/*    */ 
/*    */     
/*    */     public double get() {
/* 54 */       return VoicechatClient.VOLUME_CONFIG.getCategoryVolume(this.category);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\volume\CategoryVolumeEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */