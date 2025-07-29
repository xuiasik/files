/*     */ package de.maxhenkel.voicechat.plugins.impl;
/*     */ import de.maxhenkel.voicechat.VoicechatClient;
/*     */ import de.maxhenkel.voicechat.api.Entity;
/*     */ import de.maxhenkel.voicechat.api.Group;
/*     */ import de.maxhenkel.voicechat.api.Position;
/*     */ import de.maxhenkel.voicechat.api.VoicechatClientApi;
/*     */ import de.maxhenkel.voicechat.api.VolumeCategory;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.ClientEntityAudioChannel;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.ClientLocationalAudioChannel;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.ClientStaticAudioChannel;
/*     */ import de.maxhenkel.voicechat.api.config.ConfigAccessor;
/*     */ import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
/*     */ import de.maxhenkel.voicechat.plugins.impl.audiochannel.ClientEntityAudioChannelImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.audiochannel.ClientLocationalAudioChannelImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.audiochannel.ClientStaticAudioChannelImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.config.ConfigAccessorImpl;
/*     */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*     */ import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
/*     */ import de.maxhenkel.voicechat.voice.client.ClientUtils;
/*     */ import de.maxhenkel.voicechat.voice.common.ClientGroup;
/*     */ import java.util.UUID;
/*     */ 
/*     */ public class VoicechatClientApiImpl extends VoicechatApiImpl implements VoicechatClientApi {
/*     */   @Deprecated
/*  25 */   public static final VoicechatClientApiImpl INSTANCE = new VoicechatClientApiImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VoicechatClientApi instance() {
/*  32 */     return ClientCompatibilityManager.INSTANCE.getClientApi();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMuted() {
/*  37 */     return ClientManager.getPlayerStateManager().isMuted();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisabled() {
/*  42 */     return ClientManager.getPlayerStateManager().isDisabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisconnected() {
/*  47 */     return ClientManager.getPlayerStateManager().isDisconnected();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Group getGroup() {
/*  53 */     ClientPlayerStateManager playerStateManager = ClientManager.getPlayerStateManager();
/*  54 */     if (playerStateManager.getGroupID() == null) {
/*  55 */       return null;
/*     */     }
/*  57 */     ClientGroup group = playerStateManager.getGroup();
/*  58 */     if (group == null) {
/*  59 */       return null;
/*     */     }
/*  61 */     return new ClientGroupImpl(group);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientEntityAudioChannel createEntityAudioChannel(UUID uuid) {
/*  66 */     return (ClientEntityAudioChannel)new ClientEntityAudioChannelImpl(uuid, uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientEntityAudioChannel createEntityAudioChannel(UUID uuid, Entity entity) {
/*  71 */     return (ClientEntityAudioChannel)new ClientEntityAudioChannelImpl(uuid, entity.getUuid());
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientLocationalAudioChannel createLocationalAudioChannel(UUID uuid, Position position) {
/*  76 */     return (ClientLocationalAudioChannel)new ClientLocationalAudioChannelImpl(uuid, position);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientStaticAudioChannel createStaticAudioChannel(UUID uuid) {
/*  81 */     return (ClientStaticAudioChannel)new ClientStaticAudioChannelImpl(uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unregisterClientVolumeCategory(String categoryId) {
/*  86 */     ClientManager.getCategoryManager().removeCategory(categoryId);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigAccessor getClientConfig() {
/*  91 */     return (ConfigAccessor)new ConfigAccessorImpl(VoicechatClient.CLIENT_CONFIG.disabled.getConfig());
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerClientVolumeCategory(VolumeCategory category) {
/*  96 */     if (!(category instanceof VolumeCategoryImpl)) {
/*  97 */       throw new IllegalArgumentException("VolumeCategory is not an instance of VolumeCategoryImpl");
/*     */     }
/*  99 */     VolumeCategoryImpl c = (VolumeCategoryImpl)category;
/* 100 */     ClientManager.getCategoryManager().addCategory(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getVoiceChatDistance() {
/* 105 */     return ClientUtils.getDefaultDistanceClient();
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\VoicechatClientApiImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */