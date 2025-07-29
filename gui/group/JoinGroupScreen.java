/*     */ package de.maxhenkel.voicechat.gui.group;
/*     */ import de.maxhenkel.voicechat.gui.CreateGroupScreen;
/*     */ import de.maxhenkel.voicechat.gui.EnterPasswordScreen;
/*     */ import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
/*     */ import de.maxhenkel.voicechat.net.JoinGroupPacket;
/*     */ import de.maxhenkel.voicechat.net.Packet;
/*     */ import de.maxhenkel.voicechat.voice.common.ClientGroup;
/*     */ import net.minecraft.class_1113;
/*     */ import net.minecraft.class_124;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2588;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_339;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_364;
/*     */ import net.minecraft.class_4185;
/*     */ import net.minecraft.class_437;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_5348;
/*     */ 
/*     */ public class JoinGroupScreen extends ListScreenBase {
/*  22 */   protected static final class_2960 TEXTURE = new class_2960("voicechat", "textures/gui/gui_join_group.png");
/*  23 */   protected static final class_2561 TITLE = (class_2561)new class_2588("gui.voicechat.join_create_group.title");
/*  24 */   protected static final class_2561 CREATE_GROUP = (class_2561)new class_2588("message.voicechat.create_group_button");
/*  25 */   protected static final class_2561 JOIN_CREATE_GROUP = (class_2561)new class_2588("message.voicechat.join_create_group");
/*  26 */   protected static final class_2561 NO_GROUPS = (class_2561)(new class_2588("message.voicechat.no_groups")).method_27692(class_124.field_1080);
/*     */   
/*     */   protected static final int HEADER_SIZE = 16;
/*     */   
/*     */   protected static final int FOOTER_SIZE = 32;
/*     */   protected static final int UNIT_SIZE = 18;
/*     */   protected static final int CELL_HEIGHT = 36;
/*     */   protected JoinGroupList groupList;
/*     */   protected class_4185 createGroup;
/*     */   protected int units;
/*     */   
/*     */   public JoinGroupScreen() {
/*  38 */     super(TITLE, 236, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void method_25426() {
/*  43 */     super.method_25426();
/*  44 */     this.guiLeft += 2;
/*  45 */     this.guiTop = 32;
/*  46 */     int minUnits = class_3532.method_15386(2.2222223F);
/*  47 */     this.units = Math.max(minUnits, (this.field_22790 - 16 - 32 - this.guiTop * 2) / 18);
/*  48 */     this.ySize = 16 + this.units * 18 + 32;
/*     */     
/*  50 */     if (this.groupList != null) {
/*  51 */       this.groupList.updateSize(this.field_22789, this.units * 18, this.guiTop + 16);
/*     */     } else {
/*  53 */       this.groupList = new JoinGroupList(this, this.field_22789, this.units * 18, this.guiTop + 16, 36);
/*     */     } 
/*  55 */     method_25429((class_364)this.groupList);
/*     */     
/*  57 */     this.createGroup = new class_4185(this.guiLeft + 7, this.guiTop + this.ySize - 20 - 7, this.xSize - 14, 20, CREATE_GROUP, button -> this.field_22787.method_1507((class_437)new CreateGroupScreen()));
/*     */ 
/*     */     
/*  60 */     method_25411((class_339)this.createGroup);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/*  65 */     this.field_22787.method_1531().method_22813(TEXTURE);
/*  66 */     method_25302(poseStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, 16);
/*  67 */     for (int i = 0; i < this.units; i++) {
/*  68 */       method_25302(poseStack, this.guiLeft, this.guiTop + 16 + 18 * i, 0, 16, this.xSize, 18);
/*     */     }
/*  70 */     method_25302(poseStack, this.guiLeft, this.guiTop + 16 + 18 * this.units, 0, 34, this.xSize, 32);
/*  71 */     method_25302(poseStack, this.guiLeft + 10, this.guiTop + 16 + 6 - 2, this.xSize, 0, 12, 12);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderForeground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/*  76 */     this.field_22793.method_30883(poseStack, JOIN_CREATE_GROUP, (this.guiLeft + this.xSize / 2 - this.field_22793.method_27525((class_5348)JOIN_CREATE_GROUP) / 2), (this.guiTop + 5), 4210752);
/*     */     
/*  78 */     if (!this.groupList.isEmpty()) {
/*  79 */       this.groupList.method_25394(poseStack, mouseX, mouseY, delta);
/*     */     } else {
/*  81 */       this.field_22793.getClass(); method_27534(poseStack, this.field_22793, NO_GROUPS, this.field_22789 / 2, this.guiTop + 16 + this.units * 18 / 2 - 9 / 2, -1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25402(double mouseX, double mouseY, int button) {
/*  87 */     if (super.method_25402(mouseX, mouseY, button)) {
/*  88 */       return true;
/*     */     }
/*  90 */     for (JoinGroupEntry entry : this.groupList.method_25396()) {
/*  91 */       if (entry.method_25405(mouseX, mouseY)) {
/*  92 */         ClientGroup group = entry.getGroup().getGroup();
/*  93 */         this.field_22787.method_1483().method_4873((class_1113)class_1109.method_4758(class_3417.field_15015, 1.0F));
/*  94 */         if (group.hasPassword()) {
/*  95 */           this.field_22787.method_1507((class_437)new EnterPasswordScreen(group));
/*     */         } else {
/*  97 */           ClientServerNetManager.sendToServer((Packet)new JoinGroupPacket(group.getId(), null));
/*     */         } 
/*  99 */         return true;
/*     */       } 
/*     */     } 
/* 102 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\group\JoinGroupScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */