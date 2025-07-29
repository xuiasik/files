/*     */ package de.maxhenkel.voicechat.plugins.impl;
/*     */ 
/*     */ import de.maxhenkel.voicechat.Voicechat;
/*     */ import de.maxhenkel.voicechat.api.Group;
/*     */ import de.maxhenkel.voicechat.api.ServerPlayer;
/*     */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*     */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*     */ import de.maxhenkel.voicechat.voice.server.Group;
/*     */ import de.maxhenkel.voicechat.voice.server.PlayerStateManager;
/*     */ import de.maxhenkel.voicechat.voice.server.Server;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.class_3222;
/*     */ 
/*     */ public class VoicechatConnectionImpl
/*     */   implements VoicechatConnection {
/*     */   private final ServerPlayer player;
/*     */   private final class_3222 serverPlayer;
/*     */   private final PlayerState state;
/*     */   @Nullable
/*     */   private final Group group;
/*     */   
/*     */   public VoicechatConnectionImpl(class_3222 player, PlayerState state) {
/*  23 */     this.serverPlayer = player;
/*  24 */     this.player = new ServerPlayerImpl(player);
/*  25 */     this.state = state;
/*  26 */     this.group = GroupImpl.create(state);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static VoicechatConnectionImpl fromPlayer(@Nullable class_3222 player) {
/*  31 */     if (player == null) {
/*  32 */       return null;
/*     */     }
/*  34 */     Server server = Voicechat.SERVER.getServer();
/*  35 */     if (server == null) {
/*  36 */       return null;
/*     */     }
/*  38 */     PlayerState state = server.getPlayerStateManager().getState(player.method_5667());
/*  39 */     if (state == null) {
/*  40 */       return null;
/*     */     }
/*  42 */     return new VoicechatConnectionImpl(player, state);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Group getGroup() {
/*  48 */     return this.group;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInGroup() {
/*  53 */     return (this.group != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGroup(@Nullable Group group) {
/*  58 */     Server server = Voicechat.SERVER.getServer();
/*  59 */     if (server == null) {
/*     */       return;
/*     */     }
/*  62 */     if (group == null) {
/*  63 */       server.getGroupManager().leaveGroup(this.serverPlayer);
/*     */       return;
/*     */     } 
/*  66 */     if (group instanceof GroupImpl) {
/*  67 */       GroupImpl g = (GroupImpl)group;
/*  68 */       Group actualGroup = server.getGroupManager().getGroup(g.getGroup().getId());
/*  69 */       if (actualGroup == null) {
/*  70 */         server.getGroupManager().addGroup(g.getGroup(), this.serverPlayer);
/*  71 */         actualGroup = g.getGroup();
/*     */       } 
/*  73 */       server.getGroupManager().joinGroup(actualGroup, this.serverPlayer, g.getGroup().getPassword());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConnected() {
/*  79 */     return !this.state.isDisconnected();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConnected(boolean connected) {
/*  84 */     if (isInstalled()) {
/*     */       return;
/*     */     }
/*  87 */     Server server = Voicechat.SERVER.getServer();
/*  88 */     if (server == null) {
/*     */       return;
/*     */     }
/*  91 */     PlayerStateManager manager = server.getPlayerStateManager();
/*  92 */     PlayerState actualState = manager.getState(this.state.getUuid());
/*  93 */     if (actualState == null) {
/*     */       return;
/*     */     }
/*  96 */     if (actualState.isDisconnected() != connected) {
/*     */       return;
/*     */     }
/*  99 */     actualState.setDisconnected(!connected);
/* 100 */     manager.broadcastState(actualState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisabled() {
/* 105 */     return this.state.isDisabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDisabled(boolean disabled) {
/* 110 */     if (isInstalled()) {
/*     */       return;
/*     */     }
/* 113 */     Server server = Voicechat.SERVER.getServer();
/* 114 */     if (server == null) {
/*     */       return;
/*     */     }
/* 117 */     PlayerStateManager manager = server.getPlayerStateManager();
/* 118 */     PlayerState actualState = manager.getState(this.state.getUuid());
/* 119 */     if (actualState == null) {
/*     */       return;
/*     */     }
/* 122 */     if (actualState.isDisabled() == disabled) {
/*     */       return;
/*     */     }
/* 125 */     actualState.setDisabled(disabled);
/* 126 */     manager.broadcastState(actualState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInstalled() {
/* 131 */     return Voicechat.SERVER.isCompatible(this.serverPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerPlayer getPlayer() {
/* 136 */     return this.player;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\VoicechatConnectionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */