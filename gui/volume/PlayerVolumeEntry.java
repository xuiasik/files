/*    */ package de.maxhenkel.voicechat.gui.volume;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.gui.GameProfileUtils;
/*    */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_156;
/*    */ import net.minecraft.class_332;
/*    */ import net.minecraft.class_4587;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerVolumeEntry
/*    */   extends VolumeEntry
/*    */ {
/*    */   @Nullable
/*    */   protected final PlayerState state;
/*    */   
/*    */   public PlayerVolumeEntry(@Nullable PlayerState state, AdjustVolumesScreen screen) {
/* 24 */     super(screen, new PlayerVolumeConfigEntry((state != null) ? state.getUuid() : class_156.field_25140));
/* 25 */     this.state = state;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public PlayerState getState() {
/* 30 */     return this.state;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderElement(class_4587 poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta, int skinX, int skinY, int textX, int textY) {
/* 35 */     if (this.state != null) {
/* 36 */       this.minecraft.method_1531().method_22813(GameProfileUtils.getSkin(this.state.getUuid()));
/* 37 */       class_332.method_25293(poseStack, skinX, skinY, 24, 24, 8.0F, 8.0F, 8, 8, 64, 64);
/* 38 */       RenderSystem.enableBlend();
/* 39 */       class_332.method_25293(poseStack, skinX, skinY, 24, 24, 40.0F, 8.0F, 8, 8, 64, 64);
/* 40 */       RenderSystem.disableBlend();
/* 41 */       this.minecraft.field_1772.method_1729(poseStack, this.state.getName(), textX, textY, PLAYER_NAME_COLOR);
/*    */     } else {
/* 43 */       this.minecraft.method_1531().method_22813(OTHER_VOLUME_ICON);
/* 44 */       class_332.method_25293(poseStack, skinX, skinY, 24, 24, 16.0F, 16.0F, 16, 16, 16, 16);
/* 45 */       this.minecraft.field_1772.method_30883(poseStack, OTHER_VOLUME, textX, textY, PLAYER_NAME_COLOR);
/* 46 */       if (hovered) {
/* 47 */         this.screen.postRender(() -> this.screen.method_25424(poseStack, OTHER_VOLUME_DESCRIPTION, mouseX, mouseY));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static class PlayerVolumeConfigEntry
/*    */     implements AdjustVolumeSlider.VolumeConfigEntry
/*    */   {
/*    */     private final UUID playerUUID;
/*    */     
/*    */     public PlayerVolumeConfigEntry(UUID playerUUID) {
/* 59 */       this.playerUUID = playerUUID;
/*    */     }
/*    */ 
/*    */     
/*    */     public void save(double value) {
/* 64 */       VoicechatClient.VOLUME_CONFIG.setPlayerVolume(this.playerUUID, value);
/* 65 */       VoicechatClient.VOLUME_CONFIG.save();
/*    */     }
/*    */ 
/*    */     
/*    */     public double get() {
/* 70 */       return VoicechatClient.VOLUME_CONFIG.getPlayerVolume(this.playerUUID);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\volume\PlayerVolumeEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */