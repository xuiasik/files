/*     */ package de.maxhenkel.voicechat.gui;
/*     */ import de.maxhenkel.voicechat.Voicechat;
/*     */ import de.maxhenkel.voicechat.net.ClientServerNetManager;
/*     */ import de.maxhenkel.voicechat.net.JoinGroupPacket;
/*     */ import de.maxhenkel.voicechat.net.Packet;
/*     */ import de.maxhenkel.voicechat.voice.common.ClientGroup;
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
/*     */ public class EnterPasswordScreen extends VoiceChatScreenBase {
/*  19 */   private static final class_2960 TEXTURE = new class_2960("voicechat", "textures/gui/gui_enter_password.png");
/*  20 */   private static final class_2561 TITLE = (class_2561)new class_2588("gui.voicechat.enter_password.title");
/*  21 */   private static final class_2561 JOIN_GROUP = (class_2561)new class_2588("message.voicechat.join_group");
/*  22 */   private static final class_2561 ENTER_GROUP_PASSWORD = (class_2561)new class_2588("message.voicechat.enter_group_password");
/*  23 */   private static final class_2561 PASSWORD = (class_2561)new class_2588("message.voicechat.password");
/*     */   
/*     */   private class_342 password;
/*     */   private class_4185 joinGroup;
/*     */   private ClientGroup group;
/*     */   
/*     */   public EnterPasswordScreen(ClientGroup group) {
/*  30 */     super(TITLE, 195, 74);
/*  31 */     this.group = group;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void method_25426() {
/*  36 */     super.method_25426();
/*  37 */     this.hoverAreas.clear();
/*  38 */     this.field_22786.clear();
/*  39 */     this.field_22791.clear();
/*     */     
/*  41 */     this.field_22787.field_1774.method_1462(true);
/*     */     
/*  43 */     this.field_22793.getClass(); this.password = new class_342(this.field_22793, this.guiLeft + 7, this.guiTop + 7 + (9 + 5) * 2 - 5 + 1, this.xSize - 14, 12, (class_2561)new class_2585(""));
/*  44 */     this.password.method_1880(32);
/*  45 */     this.password.method_1890(s -> (s.isEmpty() || Voicechat.GROUP_REGEX.matcher(s).matches()));
/*  46 */     method_25411((class_339)this.password);
/*     */     
/*  48 */     this.joinGroup = new class_4185(this.guiLeft + 7, this.guiTop + this.ySize - 20 - 7, this.xSize - 14, 20, JOIN_GROUP, button -> joinGroup());
/*     */ 
/*     */     
/*  51 */     method_25411((class_339)this.joinGroup);
/*     */   }
/*     */   
/*     */   private void joinGroup() {
/*  55 */     if (!this.password.method_1882().isEmpty()) {
/*  56 */       ClientServerNetManager.sendToServer((Packet)new JoinGroupPacket(this.group.getId(), this.password.method_1882()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25393() {
/*  62 */     super.method_25393();
/*  63 */     this.password.method_1865();
/*  64 */     this.joinGroup.field_22763 = !this.password.method_1882().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25419() {
/*  69 */     super.method_25419();
/*  70 */     this.field_22787.field_1774.method_1462(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/*  75 */     this.field_22787.method_1531().method_22813(TEXTURE);
/*  76 */     method_25302(poseStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderForeground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/*  81 */     this.field_22793.method_30883(poseStack, ENTER_GROUP_PASSWORD, (this.guiLeft + this.xSize / 2 - this.field_22793.method_27525((class_5348)ENTER_GROUP_PASSWORD) / 2), (this.guiTop + 7), 4210752);
/*  82 */     this.field_22793.getClass(); this.field_22793.method_30883(poseStack, PASSWORD, (this.guiLeft + 8), (this.guiTop + 7 + 9 + 5), 4210752);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
/*  87 */     if (keyCode == 256) {
/*  88 */       this.field_22787.method_1507(null);
/*  89 */       return true;
/*     */     } 
/*  91 */     if (super.method_25404(keyCode, scanCode, modifiers)) {
/*  92 */       return true;
/*     */     }
/*  94 */     if (keyCode == 257) {
/*  95 */       joinGroup();
/*  96 */       return true;
/*     */     } 
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25410(class_310 client, int width, int height) {
/* 103 */     String passwordText = this.password.method_1882();
/* 104 */     method_25423(client, width, height);
/* 105 */     this.password.method_1852(passwordText);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\EnterPasswordScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */