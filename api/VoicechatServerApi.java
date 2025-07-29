/*     */ package de.maxhenkel.voicechat.api;
/*     */ 
/*     */ import de.maxhenkel.voicechat.api.audiochannel.AudioChannel;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.AudioPlayer;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.StaticAudioChannel;
/*     */ import de.maxhenkel.voicechat.api.audiolistener.AudioListener;
/*     */ import de.maxhenkel.voicechat.api.audiolistener.PlayerAudioListener;
/*     */ import de.maxhenkel.voicechat.api.audiosender.AudioSender;
/*     */ import de.maxhenkel.voicechat.api.config.ConfigAccessor;
/*     */ import de.maxhenkel.voicechat.api.opus.OpusEncoder;
/*     */ import de.maxhenkel.voicechat.api.packets.EntitySoundPacket;
/*     */ import de.maxhenkel.voicechat.api.packets.LocationalSoundPacket;
/*     */ import de.maxhenkel.voicechat.api.packets.StaticSoundPacket;
/*     */ import java.util.Collection;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface VoicechatServerApi
/*     */   extends VoicechatApi
/*     */ {
/*     */   void sendEntitySoundPacketTo(VoicechatConnection paramVoicechatConnection, EntitySoundPacket paramEntitySoundPacket);
/*     */   
/*     */   void sendLocationalSoundPacketTo(VoicechatConnection paramVoicechatConnection, LocationalSoundPacket paramLocationalSoundPacket);
/*     */   
/*     */   void sendStaticSoundPacketTo(VoicechatConnection paramVoicechatConnection, StaticSoundPacket paramStaticSoundPacket);
/*     */   
/*     */   @Nullable
/*     */   EntityAudioChannel createEntityAudioChannel(UUID paramUUID, Entity paramEntity);
/*     */   
/*     */   @Nullable
/*     */   LocationalAudioChannel createLocationalAudioChannel(UUID paramUUID, ServerLevel paramServerLevel, Position paramPosition);
/*     */   
/*     */   @Nullable
/*     */   StaticAudioChannel createStaticAudioChannel(UUID paramUUID, ServerLevel paramServerLevel, VoicechatConnection paramVoicechatConnection);
/*     */   
/*     */   AudioPlayer createAudioPlayer(AudioChannel paramAudioChannel, OpusEncoder paramOpusEncoder, Supplier<short[]> paramSupplier);
/*     */   
/*     */   AudioPlayer createAudioPlayer(AudioChannel paramAudioChannel, OpusEncoder paramOpusEncoder, short[] paramArrayOfshort);
/*     */   
/*     */   AudioSender createAudioSender(VoicechatConnection paramVoicechatConnection);
/*     */   
/*     */   boolean registerAudioSender(AudioSender paramAudioSender);
/*     */   
/*     */   boolean unregisterAudioSender(AudioSender paramAudioSender);
/*     */   
/*     */   PlayerAudioListener.Builder playerAudioListenerBuilder();
/*     */   
/*     */   boolean registerAudioListener(AudioListener paramAudioListener);
/*     */   
/*     */   boolean unregisterAudioListener(AudioListener paramAudioListener);
/*     */   
/*     */   boolean unregisterAudioListener(UUID paramUUID);
/*     */   
/*     */   @Nullable
/*     */   VoicechatConnection getConnectionOf(UUID paramUUID);
/*     */   
/*     */   @Nullable
/*     */   default VoicechatConnection getConnectionOf(ServerPlayer player) {
/* 184 */     return getConnectionOf(player.getUuid());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   Group createGroup(String paramString1, @Nullable String paramString2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   Group createGroup(String paramString1, @Nullable String paramString2, boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Group.Builder groupBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean removeGroup(UUID paramUUID);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Group getGroup(UUID paramUUID);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Collection<Group> getGroups();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   @Deprecated
/*     */   UUID getSecret(UUID paramUUID);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Collection<ServerPlayer> getPlayersInRange(ServerLevel paramServerLevel, Position paramPosition, double paramDouble, Predicate<ServerPlayer> paramPredicate);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   double getBroadcastRange();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Collection<ServerPlayer> getPlayersInRange(ServerLevel level, Position pos, double range) {
/* 281 */     return getPlayersInRange(level, pos, range, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void registerVolumeCategory(VolumeCategory paramVolumeCategory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void unregisterVolumeCategory(VolumeCategory category) {
/* 301 */     unregisterVolumeCategory(category.getId());
/*     */   }
/*     */   
/*     */   void unregisterVolumeCategory(String paramString);
/*     */   
/*     */   Collection<VolumeCategory> getVolumeCategories();
/*     */   
/*     */   ConfigAccessor getServerConfig();
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\VoicechatServerApi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */