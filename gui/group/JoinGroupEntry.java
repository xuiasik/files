/*     */ package de.maxhenkel.voicechat.gui.group;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import de.maxhenkel.voicechat.gui.GameProfileUtils;
/*     */ import de.maxhenkel.voicechat.gui.GroupType;
/*     */ import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
/*     */ import de.maxhenkel.voicechat.gui.widgets.ListScreenEntryBase;
/*     */ import de.maxhenkel.voicechat.voice.common.ClientGroup;
/*     */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.class_124;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2585;
/*     */ import net.minecraft.class_2588;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_332;
/*     */ import net.minecraft.class_437;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_5253;
/*     */ import net.minecraft.class_5348;
/*     */ import net.minecraft.class_5481;
/*     */ 
/*     */ public class JoinGroupEntry
/*     */   extends ListScreenEntryBase<JoinGroupEntry>
/*     */ {
/*  29 */   protected static final class_2960 LOCK = new class_2960("voicechat", "textures/icons/lock.png");
/*  30 */   protected static final class_2561 GROUP_MEMBERS = (class_2561)(new class_2588("message.voicechat.group_members")).method_27692(class_124.field_1080);
/*  31 */   protected static final class_2561 NO_GROUP_MEMBERS = (class_2561)(new class_2588("message.voicechat.no_group_members")).method_27692(class_124.field_1080);
/*     */   
/*     */   protected static final int SKIN_SIZE = 12;
/*     */   protected static final int PADDING = 4;
/*  35 */   protected static final int BG_FILL = class_5253.class_5254.method_27764(255, 74, 74, 74);
/*  36 */   protected static final int BG_FILL_SELECTED = class_5253.class_5254.method_27764(255, 90, 90, 90);
/*  37 */   protected static final int PLAYER_NAME_COLOR = class_5253.class_5254.method_27764(255, 255, 255, 255);
/*     */   
/*     */   protected final ListScreenBase parent;
/*     */   protected final class_310 minecraft;
/*     */   protected final Group group;
/*     */   
/*     */   public JoinGroupEntry(ListScreenBase parent, Group group) {
/*  44 */     this.parent = parent;
/*  45 */     this.minecraft = class_310.method_1551();
/*  46 */     this.group = group;
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25343(class_4587 poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta) {
/*  51 */     if (hovered) {
/*  52 */       class_332.method_25294(poseStack, left, top, left + width, top + height, BG_FILL_SELECTED);
/*     */     } else {
/*  54 */       class_332.method_25294(poseStack, left, top, left + width, top + height, BG_FILL);
/*     */     } 
/*     */     
/*  57 */     boolean hasPassword = this.group.group.hasPassword();
/*     */     
/*  59 */     if (hasPassword) {
/*  60 */       poseStack.method_22903();
/*  61 */       poseStack.method_22904((left + 4), (top + height / 2.0F - 8.0F), 0.0D);
/*  62 */       poseStack.method_22905(1.3333334F, 1.3333334F, 1.0F);
/*  63 */       this.minecraft.method_1531().method_22813(LOCK);
/*  64 */       class_437.method_25290(poseStack, 0, 0, 0.0F, 0.0F, 12, 12, 16, 16);
/*  65 */       poseStack.method_22909();
/*     */     } 
/*     */     
/*  68 */     class_2585 groupName = new class_2585(this.group.group.getName());
/*  69 */     this.minecraft.field_1772.getClass(); this.minecraft.field_1772.method_30883(poseStack, (class_2561)groupName, (left + 4 + (hasPassword ? 20 : 0)), (top + height / 2 - 9 / 2), PLAYER_NAME_COLOR);
/*     */     
/*  71 */     int textWidth = this.minecraft.field_1772.method_27525((class_5348)groupName) + (hasPassword ? 20 : 0);
/*     */     
/*  73 */     int headsPerRow = (width - 4 + textWidth + 4 + 4) / 13;
/*  74 */     int rows = 2;
/*     */     
/*  76 */     for (int i = 0; i < this.group.members.size(); i++) {
/*  77 */       PlayerState state = this.group.members.get(i);
/*     */       
/*  79 */       int headXIndex = i / rows;
/*  80 */       int headYIndex = i % rows;
/*     */       
/*  82 */       if (i >= headsPerRow * rows) {
/*     */         break;
/*     */       }
/*     */       
/*  86 */       int headPosX = left + width - 12 - 4 - headXIndex * 13;
/*  87 */       int headPosY = top + height / 2 - 13 + 13 * headYIndex;
/*     */       
/*  89 */       poseStack.method_22903();
/*  90 */       this.minecraft.method_1531().method_22813(GameProfileUtils.getSkin(state.getUuid()));
/*  91 */       poseStack.method_22904(headPosX, headPosY, 0.0D);
/*  92 */       float scale = 1.5F;
/*  93 */       poseStack.method_22905(scale, scale, scale);
/*  94 */       class_437.method_25290(poseStack, 0, 0, 8.0F, 8.0F, 8, 8, 64, 64);
/*  95 */       RenderSystem.enableBlend();
/*  96 */       class_437.method_25290(poseStack, 0, 0, 40.0F, 8.0F, 8, 8, 64, 64);
/*  97 */       RenderSystem.disableBlend();
/*  98 */       poseStack.method_22909();
/*     */     } 
/*     */     
/* 101 */     if (!hovered) {
/*     */       return;
/*     */     }
/* 104 */     List<class_5481> tooltip = Lists.newArrayList();
/*     */     
/* 106 */     if (this.group.getGroup().getType().equals(de.maxhenkel.voicechat.api.Group.Type.NORMAL)) {
/* 107 */       tooltip.add((new class_2588("message.voicechat.group_title", new Object[] { new class_2585(this.group.getGroup().getName()) })).method_30937());
/*     */     } else {
/* 109 */       tooltip.add((new class_2588("message.voicechat.group_type_title", new Object[] { new class_2585(this.group.getGroup().getName()), GroupType.fromType(this.group.getGroup().getType()).getTranslation() })).method_30937());
/*     */     } 
/* 111 */     if (this.group.getMembers().isEmpty()) {
/* 112 */       tooltip.add(NO_GROUP_MEMBERS.method_30937());
/*     */     } else {
/* 114 */       tooltip.add(GROUP_MEMBERS.method_30937());
/* 115 */       int maxMembers = 10;
/* 116 */       for (int j = 0; j < this.group.getMembers().size(); j++) {
/* 117 */         if (j >= maxMembers) {
/* 118 */           tooltip.add((new class_2588("message.voicechat.more_members", new Object[] { Integer.valueOf(this.group.getMembers().size() - maxMembers) })).method_27692(class_124.field_1080).method_30937());
/*     */           break;
/*     */         } 
/* 121 */         PlayerState state = this.group.getMembers().get(j);
/* 122 */         tooltip.add((new class_2585("  " + state.getName())).method_27692(class_124.field_1080).method_30937());
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     this.parent.postRender(() -> this.parent.method_25417(poseStack, tooltip, mouseX, mouseY));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Group getGroup() {
/* 132 */     return this.group;
/*     */   }
/*     */   
/*     */   public static class Group {
/*     */     private final ClientGroup group;
/*     */     private final List<PlayerState> members;
/*     */     
/*     */     public Group(ClientGroup group) {
/* 140 */       this.group = group;
/* 141 */       this.members = new ArrayList<>();
/*     */     }
/*     */     
/*     */     public ClientGroup getGroup() {
/* 145 */       return this.group;
/*     */     }
/*     */     
/*     */     public List<PlayerState> getMembers() {
/* 149 */       return this.members;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\group\JoinGroupEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */