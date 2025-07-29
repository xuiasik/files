/*    */ package de.maxhenkel.voicechat.gui.group;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import de.maxhenkel.voicechat.gui.GameProfileUtils;
/*    */ import de.maxhenkel.voicechat.gui.volume.AdjustVolumeSlider;
/*    */ import de.maxhenkel.voicechat.gui.volume.PlayerVolumeEntry;
/*    */ import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
/*    */ import de.maxhenkel.voicechat.gui.widgets.ListScreenEntryBase;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
/*    */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2585;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_332;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_5253;
/*    */ import net.minecraft.class_5348;
/*    */ 
/*    */ public class GroupEntry
/*    */   extends ListScreenEntryBase<GroupEntry> {
/* 24 */   protected static final class_2960 TALK_OUTLINE = new class_2960("voicechat", "textures/icons/talk_outline.png");
/* 25 */   protected static final class_2960 SPEAKER_OFF = new class_2960("voicechat", "textures/icons/speaker_small_off.png");
/*    */   
/*    */   protected static final int PADDING = 4;
/* 28 */   protected static final int BG_FILL = class_5253.class_5254.method_27764(255, 74, 74, 74);
/* 29 */   protected static final int PLAYER_NAME_COLOR = class_5253.class_5254.method_27764(255, 255, 255, 255);
/*    */   
/*    */   protected final ListScreenBase parent;
/*    */   protected final class_310 minecraft;
/*    */   protected PlayerState state;
/*    */   protected final AdjustVolumeSlider volumeSlider;
/*    */   
/*    */   public GroupEntry(ListScreenBase parent, PlayerState state) {
/* 37 */     this.parent = parent;
/* 38 */     this.minecraft = class_310.method_1551();
/* 39 */     this.state = state;
/* 40 */     this.volumeSlider = new AdjustVolumeSlider(0, 0, 100, 20, (AdjustVolumeSlider.VolumeConfigEntry)new PlayerVolumeEntry.PlayerVolumeConfigEntry(state.getUuid()));
/* 41 */     this.children.add(this.volumeSlider);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void method_25343(class_4587 poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta) {
/* 47 */     class_332.method_25294(poseStack, left, top, left + width, top + height, BG_FILL);
/*    */     
/* 49 */     poseStack.method_22903();
/* 50 */     int outlineSize = height - 8;
/*    */     
/* 52 */     poseStack.method_22904((left + 4), (top + 4), 0.0D);
/* 53 */     float scale = outlineSize / 10.0F;
/* 54 */     poseStack.method_22905(scale, scale, scale);
/*    */     
/* 56 */     if (!this.state.isDisabled()) {
/* 57 */       ClientVoicechat client = ClientManager.getClient();
/* 58 */       if (client != null && client.getTalkCache().isTalking(this.state.getUuid())) {
/* 59 */         this.minecraft.method_1531().method_22813(TALK_OUTLINE);
/* 60 */         class_437.method_25290(poseStack, 0, 0, 0.0F, 0.0F, 10, 10, 16, 16);
/*    */       } 
/*    */     } 
/*    */     
/* 64 */     this.minecraft.method_1531().method_22813(GameProfileUtils.getSkin(this.state.getUuid()));
/* 65 */     class_332.method_25293(poseStack, 1, 1, 8, 8, 8.0F, 8.0F, 8, 8, 64, 64);
/* 66 */     RenderSystem.enableBlend();
/* 67 */     class_332.method_25293(poseStack, 1, 1, 8, 8, 40.0F, 8.0F, 8, 8, 64, 64);
/* 68 */     RenderSystem.disableBlend();
/*    */     
/* 70 */     if (this.state.isDisabled()) {
/* 71 */       poseStack.method_22903();
/* 72 */       poseStack.method_22904(1.0D, 1.0D, 0.0D);
/* 73 */       poseStack.method_22905(0.5F, 0.5F, 1.0F);
/* 74 */       this.minecraft.method_1531().method_22813(SPEAKER_OFF);
/* 75 */       class_437.method_25290(poseStack, 0, 0, 0.0F, 0.0F, 16, 16, 16, 16);
/* 76 */       poseStack.method_22909();
/*    */     } 
/* 78 */     poseStack.method_22909();
/*    */     
/* 80 */     class_2585 name = new class_2585(this.state.getName());
/* 81 */     this.minecraft.field_1772.getClass(); this.minecraft.field_1772.method_30883(poseStack, (class_2561)name, (left + 4 + outlineSize + 4), (top + height / 2 - 9 / 2), PLAYER_NAME_COLOR);
/*    */     
/* 83 */     if (hovered && !ClientManager.getPlayerStateManager().getOwnID().equals(this.state.getUuid())) {
/* 84 */       this.volumeSlider.method_25358(Math.min(width - 4 + outlineSize + 4 + this.minecraft.field_1772.method_27525((class_5348)name) + 4 + 4, 100));
/* 85 */       this.volumeSlider.field_22760 = left + width - this.volumeSlider.method_25368() - 4;
/* 86 */       this.volumeSlider.field_22761 = top + (height - this.volumeSlider.method_25364()) / 2;
/* 87 */       this.volumeSlider.method_25394(poseStack, mouseX, mouseY, delta);
/*    */     } 
/*    */   }
/*    */   
/*    */   public PlayerState getState() {
/* 92 */     return this.state;
/*    */   }
/*    */   
/*    */   public void setState(PlayerState state) {
/* 96 */     this.state = state;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\group\GroupEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */