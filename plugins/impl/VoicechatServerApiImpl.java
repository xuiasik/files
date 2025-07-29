/*     */ package de.maxhenkel.voicechat.plugins.impl;
/*     */ import de.maxhenkel.voicechat.Voicechat;
/*     */ import de.maxhenkel.voicechat.api.Group;
/*     */ import de.maxhenkel.voicechat.api.Position;
/*     */ import de.maxhenkel.voicechat.api.ServerLevel;
/*     */ import de.maxhenkel.voicechat.api.ServerPlayer;
/*     */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*     */ import de.maxhenkel.voicechat.api.VoicechatServerApi;
/*     */ import de.maxhenkel.voicechat.api.VolumeCategory;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.AudioChannel;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.AudioPlayer;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
/*     */ import de.maxhenkel.voicechat.api.audiolistener.AudioListener;
/*     */ import de.maxhenkel.voicechat.api.audiolistener.PlayerAudioListener;
/*     */ import de.maxhenkel.voicechat.api.audiosender.AudioSender;
/*     */ import de.maxhenkel.voicechat.api.opus.OpusEncoder;
/*     */ import de.maxhenkel.voicechat.api.packets.EntitySoundPacket;
/*     */ import de.maxhenkel.voicechat.api.packets.LocationalSoundPacket;
/*     */ import de.maxhenkel.voicechat.api.packets.StaticSoundPacket;
/*     */ import de.maxhenkel.voicechat.plugins.PluginManager;
/*     */ import de.maxhenkel.voicechat.plugins.impl.audiochannel.AudioPlayerImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.audiosender.AudioSenderImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.packets.EntitySoundPacketImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.packets.LocationalSoundPacketImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.packets.StaticSoundPacketImpl;
/*     */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*     */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*     */ import de.maxhenkel.voicechat.voice.server.ClientConnection;
/*     */ import de.maxhenkel.voicechat.voice.server.Group;
/*     */ import de.maxhenkel.voicechat.voice.server.Server;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.class_3222;
/*     */ 
/*     */ public class VoicechatServerApiImpl extends VoicechatApiImpl implements VoicechatServerApi {
/*     */   @Deprecated
/*  43 */   public static final VoicechatServerApiImpl INSTANCE = new VoicechatServerApiImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VoicechatServerApi instance() {
/*  50 */     return CommonCompatibilityManager.INSTANCE.getServerApi();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendEntitySoundPacketTo(VoicechatConnection connection, EntitySoundPacket p) {
/*  55 */     if (p instanceof EntitySoundPacketImpl) {
/*  56 */       EntitySoundPacketImpl packet = (EntitySoundPacketImpl)p;
/*  57 */       sendPacket(connection, (SoundPacket<?>)packet.getPacket());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendLocationalSoundPacketTo(VoicechatConnection connection, LocationalSoundPacket p) {
/*  63 */     if (p instanceof LocationalSoundPacketImpl) {
/*  64 */       LocationalSoundPacketImpl packet = (LocationalSoundPacketImpl)p;
/*  65 */       sendPacket(connection, (SoundPacket<?>)packet.getPacket());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendStaticSoundPacketTo(VoicechatConnection connection, StaticSoundPacket p) {
/*  71 */     if (p instanceof StaticSoundPacketImpl) {
/*  72 */       StaticSoundPacketImpl packet = (StaticSoundPacketImpl)p;
/*  73 */       sendPacket(connection, packet.getPacket());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public EntityAudioChannel createEntityAudioChannel(UUID channelId, Entity entity) {
/*  80 */     Server server = Voicechat.SERVER.getServer();
/*  81 */     if (server == null) {
/*  82 */       return null;
/*     */     }
/*  84 */     return (EntityAudioChannel)new EntityAudioChannelImpl(channelId, server, entity);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public LocationalAudioChannel createLocationalAudioChannel(UUID channelId, ServerLevel level, Position initialPosition) {
/*  90 */     Server server = Voicechat.SERVER.getServer();
/*  91 */     if (server == null) {
/*  92 */       return null;
/*     */     }
/*  94 */     if (initialPosition instanceof PositionImpl) {
/*  95 */       PositionImpl p = (PositionImpl)initialPosition;
/*  96 */       return (LocationalAudioChannel)new LocationalAudioChannelImpl(channelId, server, level, p);
/*     */     } 
/*  98 */     throw new IllegalArgumentException("initialPosition is not an instance of PositionImpl");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public StaticAudioChannel createStaticAudioChannel(UUID channelId, ServerLevel level, VoicechatConnection connection) {
/* 105 */     Server server = Voicechat.SERVER.getServer();
/* 106 */     if (server == null) {
/* 107 */       return null;
/*     */     }
/* 109 */     if (connection instanceof VoicechatConnectionImpl) {
/* 110 */       VoicechatConnectionImpl conn = (VoicechatConnectionImpl)connection;
/* 111 */       return (StaticAudioChannel)new StaticAudioChannelImpl(channelId, server, conn);
/*     */     } 
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public AudioPlayer createAudioPlayer(AudioChannel audioChannel, OpusEncoder encoder, Supplier<short[]> audioSupplier) {
/* 118 */     return (AudioPlayer)new AudioPlayerImpl(audioChannel, encoder, audioSupplier);
/*     */   }
/*     */ 
/*     */   
/*     */   public AudioPlayer createAudioPlayer(AudioChannel audioChannel, OpusEncoder encoder, short[] audio) {
/* 123 */     return (AudioPlayer)new AudioPlayerImpl(audioChannel, encoder, (Supplier)new AudioSupplier(audio));
/*     */   }
/*     */ 
/*     */   
/*     */   public AudioSender createAudioSender(VoicechatConnection connection) {
/* 128 */     return (AudioSender)new AudioSenderImpl(connection.getPlayer().getUuid());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean registerAudioSender(AudioSender sender) {
/* 133 */     if (!(sender instanceof AudioSenderImpl)) {
/* 134 */       return false;
/*     */     }
/* 136 */     return AudioSenderImpl.registerAudioSender((AudioSenderImpl)sender);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean unregisterAudioSender(AudioSender sender) {
/* 141 */     if (!(sender instanceof AudioSenderImpl)) {
/* 142 */       return false;
/*     */     }
/* 144 */     return AudioSenderImpl.unregisterAudioSender((AudioSenderImpl)sender);
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerAudioListener.Builder playerAudioListenerBuilder() {
/* 149 */     return (PlayerAudioListener.Builder)new PlayerAudioListenerImpl.BuilderImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean registerAudioListener(AudioListener listener) {
/* 154 */     return PluginManager.instance().registerAudioListener(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean unregisterAudioListener(AudioListener listener) {
/* 159 */     return unregisterAudioListener(listener.getListenerId());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean unregisterAudioListener(UUID listenerId) {
/* 164 */     return PluginManager.instance().unregisterAudioListener(listenerId);
/*     */   }
/*     */   
/*     */   public static void sendPacket(VoicechatConnection receiver, SoundPacket<?> soundPacket) {
/* 168 */     Server server = Voicechat.SERVER.getServer();
/* 169 */     if (server == null) {
/*     */       return;
/*     */     }
/*     */     
/* 173 */     PlayerState state = server.getPlayerStateManager().getState(receiver.getPlayer().getUuid());
/* 174 */     if (state == null) {
/*     */       return;
/*     */     }
/*     */     
/* 178 */     if (!(receiver.getPlayer() instanceof ServerPlayerImpl)) {
/* 179 */       throw new IllegalArgumentException("ServerPlayer is not an instance of ServerPlayerImpl");
/*     */     }
/* 181 */     ServerPlayerImpl serverPlayerImpl = (ServerPlayerImpl)receiver.getPlayer();
/*     */     
/* 183 */     ClientConnection c = (ClientConnection)server.getConnections().get(receiver.getPlayer().getUuid());
/* 184 */     server.sendSoundPacket(null, null, serverPlayerImpl.getRealServerPlayer(), state, c, soundPacket, "plugin");
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public VoicechatConnection getConnectionOf(UUID playerUuid) {
/* 190 */     Server server = Voicechat.SERVER.getServer();
/* 191 */     if (server == null) {
/* 192 */       return null;
/*     */     }
/* 194 */     class_3222 player = server.getServer().method_3760().method_14602(playerUuid);
/* 195 */     if (player == null) {
/* 196 */       return null;
/*     */     }
/* 198 */     return VoicechatConnectionImpl.fromPlayer(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public Group createGroup(String name, @Nullable String password) {
/* 203 */     return createGroup(name, password, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Group createGroup(String name, @Nullable String password, boolean persistent) {
/* 208 */     return groupBuilder().setName(name).setPassword(password).setPersistent(persistent).build();
/*     */   }
/*     */ 
/*     */   
/*     */   public Group.Builder groupBuilder() {
/* 213 */     return new GroupImpl.BuilderImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeGroup(UUID groupId) {
/* 218 */     Server server = Voicechat.SERVER.getServer();
/* 219 */     if (server == null) {
/* 220 */       return false;
/*     */     }
/* 222 */     return server.getGroupManager().removeGroup(groupId);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Group getGroup(UUID groupId) {
/* 228 */     Server server = Voicechat.SERVER.getServer();
/* 229 */     if (server == null) {
/* 230 */       return null;
/*     */     }
/* 232 */     return new GroupImpl(server.getGroupManager().getGroup(groupId));
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<Group> getGroups() {
/* 237 */     Server server = Voicechat.SERVER.getServer();
/* 238 */     if (server == null) {
/* 239 */       return Collections.emptyList();
/*     */     }
/* 241 */     return (Collection<Group>)server.getGroupManager().getGroups().values().stream().map(group -> new GroupImpl(group)).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public UUID getSecret(UUID userId) {
/* 247 */     Server server = Voicechat.SERVER.getServer();
/* 248 */     if (server == null) {
/* 249 */       return null;
/*     */     }
/* 251 */     return server.getSecret(userId);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ServerPlayer> getPlayersInRange(ServerLevel level, Position pos, double range, @Nullable Predicate<ServerPlayer> filter) {
/* 256 */     if (!(pos instanceof PositionImpl)) {
/* 257 */       throw new IllegalArgumentException("Position is not an instance of PositionImpl");
/*     */     }
/* 259 */     PositionImpl p = (PositionImpl)pos;
/* 260 */     if (!(level instanceof ServerLevelImpl)) {
/* 261 */       throw new IllegalArgumentException("ServerLevel is not an instance of ServerLevelImpl");
/*     */     }
/* 263 */     ServerLevelImpl serverLevel = (ServerLevelImpl)level;
/* 264 */     return (Collection<ServerPlayer>)ServerWorldUtils.getPlayersInRange(serverLevel.getRawServerLevel(), p.getPosition(), range, (filter == null) ? null : (player -> filter.test(new ServerPlayerImpl(player)))).stream().map(ServerPlayerImpl::new).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   public double getBroadcastRange() {
/* 269 */     return Math.max(((Double)Voicechat.SERVER_CONFIG.voiceChatDistance.get()).doubleValue(), ((Double)Voicechat.SERVER_CONFIG.broadcastRange.get()).doubleValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerVolumeCategory(VolumeCategory category) {
/* 274 */     if (!(category instanceof VolumeCategoryImpl)) {
/* 275 */       throw new IllegalArgumentException("VolumeCategory is not an instance of VolumeCategoryImpl");
/*     */     }
/* 277 */     VolumeCategoryImpl c = (VolumeCategoryImpl)category;
/* 278 */     Server server = Voicechat.SERVER.getServer();
/* 279 */     if (server == null) {
/*     */       return;
/*     */     }
/* 282 */     server.getCategoryManager().addCategory(c);
/* 283 */     PluginManager.instance().onRegisterVolumeCategory(category);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unregisterVolumeCategory(String categoryId) {
/* 288 */     Server server = Voicechat.SERVER.getServer();
/* 289 */     if (server == null) {
/*     */       return;
/*     */     }
/* 292 */     VolumeCategoryImpl category = server.getCategoryManager().removeCategory(categoryId);
/* 293 */     if (category != null) {
/* 294 */       PluginManager.instance().onUnregisterVolumeCategory(category);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<VolumeCategory> getVolumeCategories() {
/* 300 */     Server server = Voicechat.SERVER.getServer();
/* 301 */     if (server == null) {
/* 302 */       return Collections.emptyList();
/*     */     }
/* 304 */     return (Collection<VolumeCategory>)server.getCategoryManager().getCategories().stream().map(VolumeCategory.class::cast).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigAccessor getServerConfig() {
/* 309 */     return (ConfigAccessor)new ConfigAccessorImpl(Voicechat.SERVER_CONFIG.voiceChatDistance.getConfig());
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\VoicechatServerApiImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */