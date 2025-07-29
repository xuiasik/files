/*     */ package de.maxhenkel.voicechat.gui;
/*     */ 
/*     */ import de.maxhenkel.voicechat.Voicechat;
/*     */ import de.maxhenkel.voicechat.net.ClientServerNetManager;
/*     */ import de.maxhenkel.voicechat.net.CreateGroupPacket;
/*     */ import de.maxhenkel.voicechat.net.Packet;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2585;
/*     */ import net.minecraft.class_2588;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_339;
/*     */ import net.minecraft.class_342;
/*     */ import net.minecraft.class_4185;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_5348;
/*     */ 
/*     */ public class CreateGroupScreen extends VoiceChatScreenBase {
/*  19 */   private static final class_2960 TEXTURE = new class_2960("voicechat", "textures/gui/gui_create_group.png");
/*  20 */   private static final class_2561 TITLE = (class_2561)new class_2588("gui.voicechat.create_group.title");
/*  21 */   private static final class_2561 CREATE = (class_2561)new class_2588("message.voicechat.create");
/*  22 */   private static final class_2561 CREATE_GROUP = (class_2561)new class_2588("message.voicechat.create_group");
/*  23 */   private static final class_2561 GROUP_NAME = (class_2561)new class_2588("message.voicechat.group_name");
/*  24 */   private static final class_2561 OPTIONAL_PASSWORD = (class_2561)new class_2588("message.voicechat.optional_password");
/*  25 */   private static final class_2561 GROUP_TYPE = (class_2561)new class_2588("message.voicechat.group_type");
/*     */   
/*     */   private class_342 groupName;
/*     */   private class_342 password;
/*     */   private GroupType groupType;
/*     */   private class_4185 groupTypeButton;
/*     */   private class_4185 createGroup;
/*     */   
/*     */   public CreateGroupScreen() {
/*  34 */     super(TITLE, 195, 124);
/*  35 */     this.groupType = GroupType.NORMAL;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void method_25426() {
/*  40 */     super.method_25426();
/*  41 */     this.hoverAreas.clear();
/*  42 */     this.field_22786.clear();
/*  43 */     this.field_22791.clear();
/*     */     
/*  45 */     this.field_22787.field_1774.method_1462(true);
/*     */     
/*  47 */     this.groupName = new class_342(this.field_22793, this.guiLeft + 7, this.guiTop + 31, this.xSize - 14, 12, (class_2561)new class_2585(""));
/*  48 */     this.groupName.method_1880(24);
/*  49 */     this.groupName.method_1890(s -> (s.isEmpty() || Voicechat.GROUP_REGEX.matcher(s).matches()));
/*  50 */     method_25411((class_339)this.groupName);
/*     */     
/*  52 */     this.password = new class_342(this.field_22793, this.guiLeft + 7, this.guiTop + 57, this.xSize - 14, 12, (class_2561)new class_2585(""));
/*  53 */     this.password.method_1880(32);
/*  54 */     this.password.method_1890(s -> (s.isEmpty() || Voicechat.GROUP_REGEX.matcher(s).matches()));
/*  55 */     method_25411((class_339)this.password);
/*     */     
/*  57 */     this.groupTypeButton = new class_4185(this.guiLeft + 6, this.guiTop + 74, this.xSize - 12, 20, GROUP_TYPE, button -> this.groupType = GroupType.values()[(this.groupType.ordinal() + 1) % (GroupType.values()).length])
/*     */       {
/*     */         public class_2561 method_25369()
/*     */         {
/*  61 */           return (class_2561)(new class_2588("message.voicechat.group_type")).method_27693(": ").method_10852(CreateGroupScreen.this.groupType.getTranslation());
/*     */         }
/*     */       };
/*  64 */     method_25411((class_339)this.groupTypeButton);
/*     */     
/*  66 */     this.createGroup = new class_4185(this.guiLeft + 6, this.guiTop + this.ySize - 27, this.xSize - 12, 20, CREATE, button -> createGroup());
/*     */ 
/*     */     
/*  69 */     method_25411((class_339)this.createGroup);
/*     */   }
/*     */   
/*     */   private void createGroup() {
/*  73 */     if (!this.groupName.method_1882().isEmpty()) {
/*  74 */       ClientServerNetManager.sendToServer((Packet)new CreateGroupPacket(this.groupName.method_1882(), this.password.method_1882().isEmpty() ? null : this.password.method_1882(), this.groupType.getType()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25393() {
/*  80 */     super.method_25393();
/*  81 */     this.groupName.method_1865();
/*  82 */     this.password.method_1865();
/*  83 */     this.createGroup.field_22763 = !this.groupName.method_1882().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25419() {
/*  88 */     super.method_25419();
/*  89 */     this.field_22787.field_1774.method_1462(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/*  94 */     this.field_22787.method_1531().method_22813(TEXTURE);
/*  95 */     method_25302(poseStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderForeground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/* 100 */     this.field_22793.method_30883(poseStack, CREATE_GROUP, (this.guiLeft + this.xSize / 2 - this.field_22793.method_27525((class_5348)CREATE_GROUP) / 2), (this.guiTop + 7), 4210752);
/* 101 */     this.field_22793.getClass(); this.field_22793.method_30883(poseStack, GROUP_NAME, (this.guiLeft + 8), (this.guiTop + 7 + 9 + 5), 4210752);
/* 102 */     this.field_22793.getClass(); this.field_22793.method_30883(poseStack, OPTIONAL_PASSWORD, (this.guiLeft + 8), (this.guiTop + 7 + (9 + 5) * 2 + 10 + 2), 4210752);
/*     */     
/* 104 */     if (mouseX >= this.groupTypeButton.field_22760 && mouseY >= this.groupTypeButton.field_22761 && mouseX < this.groupTypeButton.field_22760 + this.groupTypeButton.method_25368() && mouseY < this.groupTypeButton.field_22761 + this.groupTypeButton.method_25364()) {
/* 105 */       method_25417(poseStack, this.field_22787.field_1772.method_1728((class_5348)this.groupType.getDescription(), 200), mouseX, mouseY);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
/* 111 */     if (keyCode == 256) {
/* 112 */       this.field_22787.method_1507(null);
/* 113 */       return true;
/*     */     } 
/* 115 */     if (super.method_25404(keyCode, scanCode, modifiers)) {
/* 116 */       return true;
/*     */     }
/* 118 */     if (keyCode == 257) {
/* 119 */       createGroup();
/* 120 */       return true;
/*     */     } 
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25410(class_310 client, int width, int height) {
/* 127 */     String groupNameText = this.groupName.method_1882();
/* 128 */     String passwordText = this.password.method_1882();
/* 129 */     method_25423(client, width, height);
/* 130 */     this.groupName.method_1852(groupNameText);
/* 131 */     this.password.method_1852(passwordText);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\CreateGroupScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */