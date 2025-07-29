/*     */ package de.maxhenkel.voicechat.gui;
/*     */ import de.maxhenkel.voicechat.VoicechatClient;
/*     */ import de.maxhenkel.voicechat.gui.group.GroupScreen;
/*     */ import de.maxhenkel.voicechat.gui.group.JoinGroupScreen;
/*     */ import de.maxhenkel.voicechat.gui.tooltips.DisableTooltipSupplier;
/*     */ import de.maxhenkel.voicechat.gui.tooltips.MuteTooltipSupplier;
/*     */ import de.maxhenkel.voicechat.gui.tooltips.RecordingTooltipSupplier;
/*     */ import de.maxhenkel.voicechat.gui.volume.AdjustVolumesScreen;
/*     */ import de.maxhenkel.voicechat.gui.widgets.ImageButton;
/*     */ import de.maxhenkel.voicechat.gui.widgets.ToggleImageButton;
/*     */ import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
/*     */ import de.maxhenkel.voicechat.voice.client.AudioRecorder;
/*     */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*     */ import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
/*     */ import de.maxhenkel.voicechat.voice.client.KeyEvents;
/*     */ import de.maxhenkel.voicechat.voice.common.ClientGroup;
/*     */ import net.minecraft.class_124;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2585;
/*     */ import net.minecraft.class_2588;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_339;
/*     */ import net.minecraft.class_4185;
/*     */ import net.minecraft.class_437;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_5348;
/*     */ 
/*     */ public class VoiceChatScreen extends VoiceChatScreenBase {
/*  29 */   private static final class_2960 TEXTURE = new class_2960("voicechat", "textures/gui/gui_voicechat.png");
/*  30 */   private static final class_2960 MICROPHONE = new class_2960("voicechat", "textures/icons/microphone_button.png");
/*  31 */   private static final class_2960 HIDE = new class_2960("voicechat", "textures/icons/hide_button.png");
/*  32 */   private static final class_2960 VOLUMES = new class_2960("voicechat", "textures/icons/adjust_volumes.png");
/*  33 */   private static final class_2960 SPEAKER = new class_2960("voicechat", "textures/icons/speaker_button.png");
/*  34 */   private static final class_2960 RECORD = new class_2960("voicechat", "textures/icons/record_button.png");
/*  35 */   private static final class_2561 TITLE = (class_2561)new class_2588("gui.voicechat.voice_chat.title");
/*  36 */   private static final class_2561 SETTINGS = (class_2561)new class_2588("message.voicechat.settings");
/*  37 */   private static final class_2561 GROUP = (class_2561)new class_2588("message.voicechat.group");
/*  38 */   private static final class_2561 ADJUST_PLAYER_VOLUMES = (class_2561)new class_2588("message.voicechat.adjust_volumes");
/*     */   
/*     */   private ToggleImageButton mute;
/*     */   
/*     */   private ToggleImageButton disable;
/*     */   private VoiceChatScreenBase.HoverArea recordingHoverArea;
/*     */   private ClientPlayerStateManager stateManager;
/*     */   
/*     */   public VoiceChatScreen() {
/*  47 */     super(TITLE, 195, 76);
/*  48 */     this.stateManager = ClientManager.getPlayerStateManager();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void method_25426() {
/*  53 */     super.method_25426();
/*  54 */     ClientVoicechat client = ClientManager.getClient();
/*     */     
/*  56 */     this.mute = new ToggleImageButton(this.guiLeft + 6, this.guiTop + this.ySize - 6 - 20, MICROPHONE, this.stateManager::isMuted, button -> this.stateManager.setMuted(!this.stateManager.isMuted()), (ImageButton.TooltipSupplier)new MuteTooltipSupplier(this, this.stateManager));
/*     */ 
/*     */     
/*  59 */     method_25411((class_339)this.mute);
/*     */     
/*  61 */     this.disable = new ToggleImageButton(this.guiLeft + 6 + 20 + 2, this.guiTop + this.ySize - 6 - 20, SPEAKER, this.stateManager::isDisabled, button -> this.stateManager.setDisabled(!this.stateManager.isDisabled()), (ImageButton.TooltipSupplier)new DisableTooltipSupplier(this, this.stateManager));
/*     */ 
/*     */     
/*  64 */     method_25411((class_339)this.disable);
/*     */     
/*  66 */     ImageButton volumes = new ImageButton(this.guiLeft + 6 + 20 + 2 + 20 + 2, this.guiTop + this.ySize - 6 - 20, VOLUMES, button -> this.field_22787.method_1507((class_437)new AdjustVolumesScreen()), (button, matrices, mouseX, mouseY) -> method_25424(matrices, ADJUST_PLAYER_VOLUMES, mouseX, mouseY));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     method_25411((class_339)volumes);
/*     */     
/*  73 */     if (client != null && ((Boolean)VoicechatClient.CLIENT_CONFIG.useNatives.get()).booleanValue() && (
/*  74 */       client.getRecorder() != null || (client.getConnection() != null && client.getConnection().getData().allowRecording()))) {
/*  75 */       ToggleImageButton record = new ToggleImageButton(this.guiLeft + this.xSize - 6 - 20 - 2 - 20, this.guiTop + this.ySize - 6 - 20, RECORD, () -> Boolean.valueOf((ClientManager.getClient() != null && ClientManager.getClient().getRecorder() != null)), button -> toggleRecording(), (ImageButton.TooltipSupplier)new RecordingTooltipSupplier(this));
/*  76 */       method_25411((class_339)record);
/*     */     } 
/*     */ 
/*     */     
/*  80 */     ToggleImageButton hide = new ToggleImageButton(this.guiLeft + this.xSize - 6 - 20, this.guiTop + this.ySize - 6 - 20, HIDE, VoicechatClient.CLIENT_CONFIG.hideIcons::get, button -> VoicechatClient.CLIENT_CONFIG.hideIcons.set(Boolean.valueOf(!((Boolean)VoicechatClient.CLIENT_CONFIG.hideIcons.get()).booleanValue())).save(), (ImageButton.TooltipSupplier)new HideTooltipSupplier(this));
/*     */ 
/*     */     
/*  83 */     method_25411((class_339)hide);
/*     */     
/*  85 */     class_4185 settings = new class_4185(this.guiLeft + 6, this.guiTop + 6 + 15, 75, 20, SETTINGS, button -> this.field_22787.method_1507(new VoiceChatSettingsScreen()));
/*     */ 
/*     */     
/*  88 */     method_25411((class_339)settings);
/*     */     
/*  90 */     class_4185 group = new class_4185(this.guiLeft + this.xSize - 6 - 75 + 1, this.guiTop + 6 + 15, 75, 20, GROUP, button -> {
/*     */           ClientGroup g = this.stateManager.getGroup();
/*     */           if (g != null) {
/*     */             this.field_22787.method_1507((class_437)new GroupScreen(g));
/*     */           } else {
/*     */             this.field_22787.method_1507((class_437)new JoinGroupScreen());
/*     */           } 
/*     */         });
/*  98 */     method_25411((class_339)group);
/*     */     
/* 100 */     group.field_22763 = (client != null && client.getConnection() != null && client.getConnection().getData().groupsEnabled());
/* 101 */     this.recordingHoverArea = new VoiceChatScreenBase.HoverArea(72, this.ySize - 6 - 20, this.xSize - 122, 20);
/*     */     
/* 103 */     checkButtons();
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25393() {
/* 108 */     super.method_25393();
/* 109 */     checkButtons();
/*     */   }
/*     */   
/*     */   private void checkButtons() {
/* 113 */     this.mute.field_22763 = MuteTooltipSupplier.canMuteMic();
/* 114 */     this.disable.field_22763 = this.stateManager.canEnable();
/*     */   }
/*     */   
/*     */   private void toggleRecording() {
/* 118 */     ClientVoicechat c = ClientManager.getClient();
/* 119 */     if (c == null) {
/*     */       return;
/*     */     }
/* 122 */     c.toggleRecording();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
/* 127 */     if (keyCode == ClientCompatibilityManager.INSTANCE.getBoundKeyOf(KeyEvents.KEY_VOICE_CHAT).method_1444()) {
/* 128 */       this.field_22787.method_1507(null);
/* 129 */       return true;
/*     */     } 
/* 131 */     return super.method_25404(keyCode, scanCode, modifiers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/* 136 */     this.field_22787.method_1531().method_22813(TEXTURE);
/* 137 */     method_25302(poseStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderForeground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/* 142 */     int titleWidth = this.field_22793.method_27525((class_5348)TITLE);
/* 143 */     this.field_22793.method_27528(poseStack, TITLE.method_30937(), (this.guiLeft + (this.xSize - titleWidth) / 2), (this.guiTop + 7), 4210752);
/*     */     
/* 145 */     ClientVoicechat client = ClientManager.getClient();
/* 146 */     if (client != null && client.getRecorder() != null) {
/* 147 */       AudioRecorder recorder = client.getRecorder();
/* 148 */       class_2585 time = new class_2585(recorder.getDuration());
/* 149 */       this.field_22793.getClass(); this.field_22793.method_30883(poseStack, (class_2561)time.method_27692(class_124.field_1079), (this.guiLeft + this.recordingHoverArea.getPosX()) + this.recordingHoverArea.getWidth() / 2.0F - this.field_22793.method_27525((class_5348)time) / 2.0F, (this.guiTop + this.recordingHoverArea.getPosY()) + this.recordingHoverArea.getHeight() / 2.0F - 9.0F / 2.0F, 0);
/*     */       
/* 151 */       if (this.recordingHoverArea.isHovered(this.guiLeft, this.guiTop, mouseX, mouseY))
/* 152 */         method_25424(poseStack, (class_2561)new class_2588("message.voicechat.storage_size", new Object[] { recorder.getStorage() }), mouseX, mouseY); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\VoiceChatScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */