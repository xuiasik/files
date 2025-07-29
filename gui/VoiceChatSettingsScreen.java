/*     */ package de.maxhenkel.voicechat.gui;
/*     */ import de.maxhenkel.voicechat.configbuilder.entry.ConfigEntry;
/*     */ import de.maxhenkel.voicechat.gui.audiodevice.SelectMicrophoneScreen;
/*     */ import de.maxhenkel.voicechat.gui.audiodevice.SelectSpeakerScreen;
/*     */ import de.maxhenkel.voicechat.gui.volume.AdjustVolumesScreen;
/*     */ import de.maxhenkel.voicechat.gui.widgets.EnumButton;
/*     */ import de.maxhenkel.voicechat.gui.widgets.KeybindButton;
/*     */ import de.maxhenkel.voicechat.gui.widgets.MicAmplificationSlider;
/*     */ import de.maxhenkel.voicechat.gui.widgets.MicTestButton;
/*     */ import de.maxhenkel.voicechat.gui.widgets.VoiceActivationSlider;
/*     */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*     */ import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
/*     */ import de.maxhenkel.voicechat.voice.client.MicrophoneActivationType;
/*     */ import de.maxhenkel.voicechat.voice.client.speaker.AudioType;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2588;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_339;
/*     */ import net.minecraft.class_4185;
/*     */ import net.minecraft.class_437;
/*     */ import net.minecraft.class_4587;
/*     */ 
/*     */ public class VoiceChatSettingsScreen extends VoiceChatScreenBase {
/*  25 */   private static final class_2960 TEXTURE = new class_2960("voicechat", "textures/gui/gui_voicechat_settings.png");
/*  26 */   private static final class_2561 TITLE = (class_2561)new class_2588("gui.voicechat.voice_chat_settings.title");
/*     */   
/*  28 */   private static final class_2561 ASSIGN_TOOLTIP = (class_2561)new class_2588("message.voicechat.press_to_reassign_key");
/*  29 */   private static final class_2561 PUSH_TO_TALK = (class_2561)new class_2588("message.voicechat.activation_type.ptt");
/*  30 */   private static final class_2561 ADJUST_VOLUMES = (class_2561)new class_2588("message.voicechat.adjust_volumes");
/*  31 */   private static final class_2561 SELECT_MICROPHONE = (class_2561)new class_2588("message.voicechat.select_microphone");
/*  32 */   private static final class_2561 SELECT_SPEAKER = (class_2561)new class_2588("message.voicechat.select_speaker");
/*  33 */   private static final class_2561 BACK = (class_2561)new class_2588("message.voicechat.back");
/*     */   
/*     */   @Nullable
/*     */   private final class_437 parent;
/*     */   private VoiceActivationSlider voiceActivationSlider;
/*     */   private MicTestButton micTestButton;
/*     */   private KeybindButton keybindButton;
/*     */   
/*     */   public VoiceChatSettingsScreen(@Nullable class_437 parent) {
/*  42 */     super(TITLE, 248, 219);
/*  43 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public VoiceChatSettingsScreen() {
/*  47 */     this((class_437)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void method_25426() {
/*  52 */     super.method_25426();
/*     */     
/*  54 */     int y = this.guiTop + 20;
/*     */     
/*  56 */     method_25411((class_339)new VoiceSoundSlider(this.guiLeft + 10, y, this.xSize - 20, 20));
/*  57 */     y += 21;
/*  58 */     method_25411((class_339)new MicAmplificationSlider(this.guiLeft + 10, y, this.xSize - 20, 20));
/*  59 */     y += 21;
/*  60 */     method_25411((class_339)new DenoiserButton(this.guiLeft + 10, y, this.xSize - 20, 20));
/*  61 */     y += 21;
/*     */     
/*  63 */     this.voiceActivationSlider = new VoiceActivationSlider(this.guiLeft + 10 + 20 + 1, y + 21, this.xSize - 20 - 20 - 1, 20);
/*  64 */     this.micTestButton = new MicTestButton(this.guiLeft + 10, y + 21, (MicTestButton.MicListener)this.voiceActivationSlider);
/*  65 */     this.keybindButton = new KeybindButton(KeyEvents.KEY_PTT, this.guiLeft + 10 + 20 + 1, y + 21, this.xSize - 20 - 20 - 1, 20, PUSH_TO_TALK);
/*  66 */     method_25411((class_339)new MicActivationButton(this.guiLeft + 10, y, this.xSize - 20, 20, type -> {
/*     */             this.voiceActivationSlider.field_22764 = MicrophoneActivationType.VOICE.equals(type);
/*     */             
/*     */             this.keybindButton.field_22764 = MicrophoneActivationType.PTT.equals(type);
/*     */           }));
/*  71 */     method_25411((class_339)this.micTestButton);
/*  72 */     method_25411((class_339)this.voiceActivationSlider);
/*  73 */     method_25411((class_339)this.keybindButton);
/*  74 */     y += 42;
/*     */     
/*  76 */     method_25411((class_339)new EnumButton<AudioType>(this.guiLeft + 10, y, this.xSize - 20, 20, VoicechatClient.CLIENT_CONFIG.audioType)
/*     */         {
/*     */           protected class_2561 getText(AudioType type)
/*     */           {
/*  80 */             return (class_2561)new class_2588("message.voicechat.audio_type", new Object[] { type.getText() });
/*     */           }
/*     */ 
/*     */           
/*     */           protected void onUpdate(AudioType type) {
/*  85 */             ClientVoicechat client = ClientManager.getClient();
/*  86 */             if (client != null) {
/*  87 */               VoiceChatSettingsScreen.this.micTestButton.stop();
/*  88 */               client.reloadAudio();
/*     */             } 
/*     */           }
/*     */         });
/*  92 */     y += 21;
/*  93 */     if (isIngame()) {
/*  94 */       method_25411((class_339)new class_4185(this.guiLeft + 10, y, this.xSize - 20, 20, ADJUST_VOLUMES, button -> this.field_22787.method_1507((class_437)new AdjustVolumesScreen())));
/*     */ 
/*     */       
/*  97 */       y += 21;
/*     */     } 
/*  99 */     method_25411((class_339)new class_4185(this.guiLeft + 10, y, this.xSize / 2 - 15, 20, SELECT_MICROPHONE, button -> this.field_22787.method_1507((class_437)new SelectMicrophoneScreen(this))));
/*     */ 
/*     */     
/* 102 */     method_25411((class_339)new class_4185(this.guiLeft + this.xSize / 2 + 1, y, (this.xSize - 20) / 2 - 1, 20, SELECT_SPEAKER, button -> this.field_22787.method_1507((class_437)new SelectSpeakerScreen(this))));
/*     */ 
/*     */     
/* 105 */     y += 21;
/* 106 */     if (!isIngame() && this.parent != null) {
/* 107 */       method_25411((class_339)new class_4185(this.guiLeft + 10, y, this.xSize - 20, 20, BACK, button -> this.field_22787.method_1507(this.parent)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderBackground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/* 115 */     this.field_22787.method_1531().method_22813(TEXTURE);
/* 116 */     if (isIngame()) {
/* 117 */       method_25302(poseStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderForeground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/* 123 */     int titleWidth = this.field_22793.method_27525((class_5348)TITLE);
/* 124 */     this.field_22793.method_27528(poseStack, TITLE.method_30937(), (this.guiLeft + (this.xSize - titleWidth) / 2), (this.guiTop + 7), getFontColor());
/*     */     
/* 126 */     class_2561 sliderTooltip = this.voiceActivationSlider.getHoverText();
/* 127 */     if (this.voiceActivationSlider.method_25367() && sliderTooltip != null) {
/* 128 */       method_25424(poseStack, sliderTooltip, mouseX, mouseY);
/* 129 */     } else if (this.micTestButton.method_25367()) {
/* 130 */       this.micTestButton.method_25352(poseStack, mouseX, mouseY);
/* 131 */     } else if (this.keybindButton.method_25367()) {
/* 132 */       method_25424(poseStack, ASSIGN_TOOLTIP, mouseX, mouseY);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25422() {
/* 138 */     if (this.keybindButton.isListening()) {
/* 139 */       return false;
/*     */     }
/* 141 */     return super.method_25422();
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\VoiceChatSettingsScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */