/*     */ package de.maxhenkel.voicechat.gui.group;
/*     */ import de.maxhenkel.voicechat.VoicechatClient;
/*     */ import de.maxhenkel.voicechat.api.Group;
/*     */ import de.maxhenkel.voicechat.gui.GroupType;
/*     */ import de.maxhenkel.voicechat.gui.tooltips.DisableTooltipSupplier;
/*     */ import de.maxhenkel.voicechat.gui.tooltips.HideGroupHudTooltipSupplier;
/*     */ import de.maxhenkel.voicechat.gui.tooltips.MuteTooltipSupplier;
/*     */ import de.maxhenkel.voicechat.gui.widgets.ImageButton;
/*     */ import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
/*     */ import de.maxhenkel.voicechat.gui.widgets.ToggleImageButton;
/*     */ import de.maxhenkel.voicechat.net.ClientServerNetManager;
/*     */ import de.maxhenkel.voicechat.net.LeaveGroupPacket;
/*     */ import de.maxhenkel.voicechat.net.Packet;
/*     */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*     */ import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
/*     */ import de.maxhenkel.voicechat.voice.client.MicrophoneActivationType;
/*     */ import de.maxhenkel.voicechat.voice.common.ClientGroup;
/*     */ import java.util.Collections;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2585;
/*     */ import net.minecraft.class_2588;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_339;
/*     */ import net.minecraft.class_364;
/*     */ import net.minecraft.class_437;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_5348;
/*     */ 
/*     */ public class GroupScreen extends ListScreenBase {
/*  30 */   protected static final class_2960 TEXTURE = new class_2960("voicechat", "textures/gui/gui_group.png");
/*  31 */   protected static final class_2960 LEAVE = new class_2960("voicechat", "textures/icons/leave.png");
/*  32 */   protected static final class_2960 MICROPHONE = new class_2960("voicechat", "textures/icons/microphone_button.png");
/*  33 */   protected static final class_2960 SPEAKER = new class_2960("voicechat", "textures/icons/speaker_button.png");
/*  34 */   protected static final class_2960 GROUP_HUD = new class_2960("voicechat", "textures/icons/group_hud_button.png");
/*  35 */   protected static final class_2561 TITLE = (class_2561)new class_2588("gui.voicechat.group.title");
/*  36 */   protected static final class_2561 LEAVE_GROUP = (class_2561)new class_2588("message.voicechat.leave_group");
/*     */   
/*     */   protected static final int HEADER_SIZE = 16;
/*     */   
/*     */   protected static final int FOOTER_SIZE = 32;
/*     */   
/*     */   protected static final int UNIT_SIZE = 18;
/*     */   protected static final int CELL_HEIGHT = 36;
/*     */   protected GroupList groupList;
/*     */   protected int units;
/*     */   protected final ClientGroup group;
/*     */   protected ToggleImageButton mute;
/*     */   protected ToggleImageButton disable;
/*     */   protected ToggleImageButton showHUD;
/*     */   protected ImageButton leave;
/*     */   
/*     */   public GroupScreen(ClientGroup group) {
/*  53 */     super(TITLE, 236, 0);
/*  54 */     this.group = group;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void method_25426() {
/*  59 */     super.method_25426();
/*  60 */     this.guiLeft += 2;
/*  61 */     this.guiTop = 32;
/*  62 */     int minUnits = class_3532.method_15386(2.2222223F);
/*  63 */     this.units = Math.max(minUnits, (this.field_22790 - 16 - 32 - this.guiTop * 2) / 18);
/*  64 */     this.ySize = 16 + this.units * 18 + 32;
/*     */     
/*  66 */     ClientPlayerStateManager stateManager = ClientManager.getPlayerStateManager();
/*     */     
/*  68 */     if (this.groupList != null) {
/*  69 */       this.groupList.updateSize(this.field_22789, this.units * 18, this.guiTop + 16);
/*     */     } else {
/*  71 */       this.groupList = new GroupList(this, this.field_22789, this.units * 18, this.guiTop + 16, 36);
/*     */     } 
/*  73 */     method_25429((class_364)this.groupList);
/*     */     
/*  75 */     int buttonY = this.guiTop + this.ySize - 20 - 7;
/*  76 */     int buttonSize = 20;
/*     */     
/*  78 */     this.mute = new ToggleImageButton(this.guiLeft + 7, buttonY, MICROPHONE, stateManager::isMuted, button -> stateManager.setMuted(!stateManager.isMuted()), (ImageButton.TooltipSupplier)new MuteTooltipSupplier((class_437)this, stateManager));
/*     */ 
/*     */     
/*  81 */     method_25411((class_339)this.mute);
/*     */     
/*  83 */     this.disable = new ToggleImageButton(this.guiLeft + 7 + buttonSize + 3, buttonY, SPEAKER, stateManager::isDisabled, button -> stateManager.setDisabled(!stateManager.isDisabled()), (ImageButton.TooltipSupplier)new DisableTooltipSupplier((class_437)this, stateManager));
/*     */ 
/*     */     
/*  86 */     method_25411((class_339)this.disable);
/*     */     
/*  88 */     this.showHUD = new ToggleImageButton(this.guiLeft + 7 + (buttonSize + 3) * 2, buttonY, GROUP_HUD, VoicechatClient.CLIENT_CONFIG.showGroupHUD::get, button -> VoicechatClient.CLIENT_CONFIG.showGroupHUD.set(Boolean.valueOf(!((Boolean)VoicechatClient.CLIENT_CONFIG.showGroupHUD.get()).booleanValue())).save(), (ImageButton.TooltipSupplier)new HideGroupHudTooltipSupplier((class_437)this));
/*     */ 
/*     */     
/*  91 */     method_25411((class_339)this.showHUD);
/*     */     
/*  93 */     this.leave = new ImageButton(this.guiLeft + this.xSize - buttonSize - 7, buttonY, LEAVE, button -> {
/*     */           ClientServerNetManager.sendToServer((Packet)new LeaveGroupPacket());
/*     */           
/*     */           this.field_22787.method_1507((class_437)new JoinGroupScreen());
/*     */         }(button, matrices, mouseX, mouseY) -> method_25417(matrices, Collections.singletonList(LEAVE_GROUP.method_30937()), mouseX, mouseY));
/*     */     
/*  99 */     method_25411((class_339)this.leave);
/*     */     
/* 101 */     checkButtons();
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25393() {
/* 106 */     super.method_25393();
/* 107 */     checkButtons();
/*     */   }
/*     */   
/*     */   private void checkButtons() {
/* 111 */     this.mute.field_22763 = ((MicrophoneActivationType)VoicechatClient.CLIENT_CONFIG.microphoneActivationType.get()).equals(MicrophoneActivationType.VOICE);
/* 112 */     this.showHUD.field_22763 = !((Boolean)VoicechatClient.CLIENT_CONFIG.hideIcons.get()).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/* 117 */     this.field_22787.method_1531().method_22813(TEXTURE);
/* 118 */     method_25302(poseStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, 16);
/* 119 */     for (int i = 0; i < this.units; i++) {
/* 120 */       method_25302(poseStack, this.guiLeft, this.guiTop + 16 + 18 * i, 0, 16, this.xSize, 18);
/*     */     }
/* 122 */     method_25302(poseStack, this.guiLeft, this.guiTop + 16 + 18 * this.units, 0, 34, this.xSize, 32);
/* 123 */     method_25302(poseStack, this.guiLeft + 10, this.guiTop + 16 + 6 - 2, this.xSize, 0, 12, 12);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderForeground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/*     */     class_2588 class_2588;
/* 129 */     if (this.group.getType().equals(Group.Type.NORMAL)) {
/* 130 */       class_2588 = new class_2588("message.voicechat.group_title", new Object[] { new class_2585(this.group.getName()) });
/*     */     } else {
/* 132 */       class_2588 = new class_2588("message.voicechat.group_type_title", new Object[] { new class_2585(this.group.getName()), GroupType.fromType(this.group.getType()).getTranslation() });
/*     */     } 
/*     */     
/* 135 */     this.field_22793.method_30883(poseStack, (class_2561)class_2588, (this.guiLeft + this.xSize / 2 - this.field_22793.method_27525((class_5348)class_2588) / 2), (this.guiTop + 5), 4210752);
/*     */     
/* 137 */     this.groupList.method_25394(poseStack, mouseX, mouseY, delta);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\group\GroupScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */