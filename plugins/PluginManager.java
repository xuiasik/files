/*     */ package de.maxhenkel.voicechat.plugins;
/*     */ import de.maxhenkel.voicechat.Voicechat;
/*     */ import de.maxhenkel.voicechat.api.Group;
/*     */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*     */ import de.maxhenkel.voicechat.api.VoicechatPlugin;
/*     */ import de.maxhenkel.voicechat.api.VoicechatSocket;
/*     */ import de.maxhenkel.voicechat.api.audiolistener.PlayerAudioListener;
/*     */ import de.maxhenkel.voicechat.api.events.Event;
/*     */ import de.maxhenkel.voicechat.plugins.impl.GroupImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.VoicechatConnectionImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.VoicechatSocketImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.events.VoicechatServerStartingEventImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.packets.SoundPacketImpl;
/*     */ import de.maxhenkel.voicechat.voice.common.GroupSoundPacket;
/*     */ import de.maxhenkel.voicechat.voice.common.PlayerSoundPacket;
/*     */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*     */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*     */ import de.maxhenkel.voicechat.voice.server.Group;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.class_3222;
/*     */ 
/*     */ public class PluginManager {
/*     */   private List<VoicechatPlugin> plugins;
/*     */   private Map<Class<? extends Event>, List<Consumer<? extends Event>>> events;
/*     */   
/*     */   public void init() {
/*  30 */     if (this.plugins != null) {
/*     */       return;
/*     */     }
/*  33 */     Voicechat.LOGGER.info("Loading plugins", new Object[0]);
/*  34 */     this.plugins = CommonCompatibilityManager.INSTANCE.loadPlugins();
/*  35 */     Voicechat.LOGGER.info("Loaded {} plugin(s)", new Object[] { Integer.valueOf(this.plugins.size()) });
/*  36 */     Voicechat.LOGGER.info("Initializing plugins", new Object[0]);
/*  37 */     for (VoicechatPlugin plugin : this.plugins) {
/*     */       try {
/*  39 */         plugin.initialize((VoicechatApi)VoicechatServerApiImpl.instance());
/*  40 */       } catch (Throwable e) {
/*  41 */         Voicechat.LOGGER.warn("Failed to initialize plugin '{}'", new Object[] { plugin.getPluginId(), e });
/*     */       } 
/*     */     } 
/*  44 */     Voicechat.LOGGER.info("Initialized {} plugin(s)", new Object[] { Integer.valueOf(this.plugins.size()) });
/*  45 */     gatherEvents();
/*  46 */     this.playerAudioListeners = new HashMap<>();
/*     */   }
/*     */   private Map<UUID, List<PlayerAudioListener>> playerAudioListeners; private static PluginManager instance;
/*     */   private void gatherEvents() {
/*  50 */     EventBuilder eventBuilder = EventBuilder.create();
/*  51 */     EventRegistration registration = eventBuilder::addEvent;
/*  52 */     for (VoicechatPlugin plugin : this.plugins) {
/*  53 */       Voicechat.LOGGER.info("Registering events for '{}'", new Object[] { plugin.getPluginId() });
/*     */       try {
/*  55 */         plugin.registerEvents(registration);
/*  56 */       } catch (Throwable e) {
/*  57 */         Voicechat.LOGGER.warn("Failed to register events for plugin '{}'", new Object[] { plugin.getPluginId(), e });
/*     */       } 
/*     */     } 
/*  60 */     this.events = eventBuilder.build();
/*     */   }
/*     */   
/*     */   public boolean registerAudioListener(AudioListener l) {
/*  64 */     if (!(l instanceof PlayerAudioListener)) {
/*  65 */       return false;
/*     */     }
/*  67 */     PlayerAudioListener listener = (PlayerAudioListener)l;
/*     */ 
/*     */ 
/*     */     
/*  71 */     boolean exists = this.playerAudioListeners.values().stream().anyMatch(listeners -> listeners.stream().anyMatch(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     if (exists) {
/*  78 */       return false;
/*     */     }
/*     */     
/*  81 */     ((List<PlayerAudioListener>)this.playerAudioListeners.computeIfAbsent(listener.getPlayerUuid(), k -> new ArrayList())).add(listener);
/*  82 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean unregisterAudioListener(UUID listenerId) {
/*  89 */     boolean removed = this.playerAudioListeners.values().stream().anyMatch(listeners -> listeners.removeIf(()));
/*     */ 
/*     */     
/*  92 */     if (!removed) {
/*  93 */       return false;
/*     */     }
/*  95 */     this.playerAudioListeners.values().removeIf(List::isEmpty);
/*  96 */     return true;
/*     */   }
/*     */   
/*     */   public List<PlayerAudioListener> getPlayerAudioListeners(UUID playerUuid) {
/* 100 */     return this.playerAudioListeners.getOrDefault(playerUuid, Collections.emptyList());
/*     */   }
/*     */   public void onListenerAudio(UUID playerUuid, SoundPacket<?> packet) {
/*     */     SoundPacketImpl soundPacket;
/* 104 */     if (playerUuid.equals(packet.getSender())) {
/*     */       return;
/*     */     }
/* 107 */     List<PlayerAudioListener> listeners = getPlayerAudioListeners(playerUuid);
/* 108 */     if (listeners.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 113 */     if (packet instanceof GroupSoundPacket) {
/* 114 */       StaticSoundPacketImpl staticSoundPacketImpl = new StaticSoundPacketImpl((GroupSoundPacket)packet);
/* 115 */     } else if (packet instanceof PlayerSoundPacket) {
/* 116 */       EntitySoundPacketImpl entitySoundPacketImpl = new EntitySoundPacketImpl((PlayerSoundPacket)packet);
/* 117 */     } else if (packet instanceof LocationSoundPacket) {
/* 118 */       LocationalSoundPacketImpl locationalSoundPacketImpl = new LocationalSoundPacketImpl((LocationSoundPacket)packet);
/*     */     } else {
/* 120 */       soundPacket = new SoundPacketImpl(packet);
/*     */     } 
/*     */     
/* 123 */     for (PlayerAudioListener l : listeners) {
/* 124 */       if (l instanceof PlayerAudioListenerImpl) {
/* 125 */         ((PlayerAudioListenerImpl)l).getListener().accept(soundPacket);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public <T extends Event> boolean dispatchEvent(Class<? extends T> eventClass, T event) {
/* 131 */     List<Consumer<? extends Event>> events = this.events.get(eventClass);
/* 132 */     if (events == null) {
/* 133 */       return false;
/*     */     }
/* 135 */     for (Consumer<? extends Event> evt : events) {
/*     */       try {
/* 137 */         Consumer<? extends Event> consumer = evt;
/* 138 */         consumer.accept((Event)event);
/* 139 */         if (event.isCancelled()) {
/*     */           break;
/*     */         }
/* 142 */       } catch (Exception e) {
/* 143 */         Voicechat.LOGGER.error("Failed to dispatch event '{}'", new Object[] { event.getClass().getSimpleName(), e });
/*     */       } 
/*     */     } 
/* 146 */     return event.isCancelled();
/*     */   }
/*     */   public VoicechatSocket getSocketImplementation(MinecraftServer server) {
/*     */     VoicechatSocketImpl voicechatSocketImpl;
/* 150 */     VoicechatServerStartingEventImpl event = new VoicechatServerStartingEventImpl();
/* 151 */     dispatchEvent(VoicechatServerStartingEvent.class, event);
/* 152 */     VoicechatSocket socket = event.getSocketImplementation();
/* 153 */     if (socket == null) {
/* 154 */       voicechatSocketImpl = new VoicechatSocketImpl();
/* 155 */       Voicechat.LOGGER.debug("Using default voicechat socket implementation", new Object[0]);
/*     */     } else {
/* 157 */       Voicechat.LOGGER.info("Using custom voicechat socket implementation: {}", new Object[] { voicechatSocketImpl.getClass().getName() });
/*     */     } 
/* 159 */     return (VoicechatSocket)voicechatSocketImpl;
/*     */   }
/*     */   
/*     */   public String getVoiceHost(String voiceHost) {
/* 163 */     VoiceHostEventImpl event = new VoiceHostEventImpl(voiceHost);
/* 164 */     dispatchEvent(VoiceHostEvent.class, event);
/* 165 */     return event.getVoiceHost();
/*     */   }
/*     */   
/*     */   public void onRegisterVolumeCategory(VolumeCategory category) {
/* 169 */     dispatchEvent(RegisterVolumeCategoryEvent.class, new RegisterVolumeCategoryEventImpl(category));
/*     */   }
/*     */   
/*     */   public void onUnregisterVolumeCategory(VolumeCategory category) {
/* 173 */     dispatchEvent(UnregisterVolumeCategoryEvent.class, new UnregisterVolumeCategoryEventImpl(category));
/*     */   }
/*     */   
/*     */   public void onServerStarted() {
/* 177 */     dispatchEvent(VoicechatServerStartedEvent.class, new VoicechatServerStartedEventImpl());
/*     */   }
/*     */   
/*     */   public void onServerStopped() {
/* 181 */     dispatchEvent(VoicechatServerStoppedEvent.class, new VoicechatServerStoppedEventImpl());
/*     */   }
/*     */   
/*     */   public void onPlayerConnected(class_3222 player) {
/* 185 */     dispatchEvent(PlayerConnectedEvent.class, new PlayerConnectedEventImpl(VoicechatConnectionImpl.fromPlayer(player)));
/*     */   }
/*     */   
/*     */   public void onPlayerDisconnected(UUID player) {
/* 189 */     dispatchEvent(PlayerDisconnectedEvent.class, new PlayerDisconnectedEventImpl(player));
/*     */   }
/*     */   
/*     */   public void onPlayerStateChanged(PlayerState state) {
/* 193 */     dispatchEvent(PlayerStateChangedEvent.class, new PlayerStateChangedEventImpl(state));
/*     */   }
/*     */   
/*     */   public boolean onJoinGroup(class_3222 player, @Nullable Group group) {
/* 197 */     if (group == null) {
/* 198 */       return onLeaveGroup(player);
/*     */     }
/* 200 */     return dispatchEvent(JoinGroupEvent.class, new JoinGroupEventImpl((Group)new GroupImpl(group), (VoicechatConnection)VoicechatConnectionImpl.fromPlayer(player)));
/*     */   }
/*     */   
/*     */   public boolean onCreateGroup(@Nullable class_3222 player, @Nullable Group group) {
/* 204 */     if (group == null) {
/* 205 */       if (player == null) {
/* 206 */         return false;
/*     */       }
/* 208 */       return onLeaveGroup(player);
/*     */     } 
/* 210 */     return dispatchEvent(CreateGroupEvent.class, new CreateGroupEventImpl((Group)new GroupImpl(group), (VoicechatConnection)VoicechatConnectionImpl.fromPlayer(player)));
/*     */   }
/*     */   
/*     */   public boolean onLeaveGroup(class_3222 player) {
/* 214 */     Server server = Voicechat.SERVER.getServer();
/* 215 */     if (server == null) {
/* 216 */       return false;
/*     */     }
/* 218 */     GroupImpl group = null;
/* 219 */     PlayerState state = server.getPlayerStateManager().getState(player.method_5667());
/* 220 */     if (state != null) {
/* 221 */       UUID groupUUID = state.getGroup();
/* 222 */       if (groupUUID != null) {
/* 223 */         Group g = server.getGroupManager().getGroup(groupUUID);
/* 224 */         if (g != null) {
/* 225 */           group = new GroupImpl(g);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 230 */     return dispatchEvent(LeaveGroupEvent.class, new LeaveGroupEventImpl((Group)group, (VoicechatConnection)VoicechatConnectionImpl.fromPlayer(player)));
/*     */   }
/*     */   
/*     */   public boolean onRemoveGroup(Group group) {
/* 234 */     return dispatchEvent(RemoveGroupEvent.class, new RemoveGroupEventImpl((Group)new GroupImpl(group)));
/*     */   }
/*     */   
/*     */   public boolean onMicPacket(class_3222 player, PlayerState state, MicPacket packet) {
/* 238 */     return dispatchEvent(MicrophonePacketEvent.class, new MicrophonePacketEventImpl((MicrophonePacket)new MicrophonePacketImpl(packet, player
/* 239 */             .method_5667()), (VoicechatConnection)new VoicechatConnectionImpl(player, state)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onSoundPacket(@Nullable class_3222 sender, @Nullable PlayerState senderState, class_3222 receiver, PlayerState receiverState, SoundPacket<?> p, String source) {
/*     */     VoicechatConnectionImpl voicechatConnectionImpl1;
/* 245 */     VoicechatConnection senderConnection = null;
/* 246 */     if (sender != null && senderState != null) {
/* 247 */       voicechatConnectionImpl1 = new VoicechatConnectionImpl(sender, senderState);
/*     */     }
/*     */     
/* 250 */     VoicechatConnectionImpl voicechatConnectionImpl2 = new VoicechatConnectionImpl(receiver, receiverState);
/* 251 */     if (p instanceof LocationSoundPacket) {
/* 252 */       LocationSoundPacket packet = (LocationSoundPacket)p;
/* 253 */       return dispatchEvent(LocationalSoundPacketEvent.class, new LocationalSoundPacketEventImpl((LocationalSoundPacket)new LocationalSoundPacketImpl(packet), (VoicechatConnection)voicechatConnectionImpl1, (VoicechatConnection)voicechatConnectionImpl2, source));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     if (p instanceof PlayerSoundPacket) {
/* 260 */       PlayerSoundPacket packet = (PlayerSoundPacket)p;
/* 261 */       return dispatchEvent(EntitySoundPacketEvent.class, new EntitySoundPacketEventImpl((EntitySoundPacket)new EntitySoundPacketImpl(packet), (VoicechatConnection)voicechatConnectionImpl1, (VoicechatConnection)voicechatConnectionImpl2, source));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 267 */     if (p instanceof GroupSoundPacket) {
/* 268 */       GroupSoundPacket packet = (GroupSoundPacket)p;
/* 269 */       return dispatchEvent(StaticSoundPacketEvent.class, new StaticSoundPacketEventImpl((StaticSoundPacket)new StaticSoundPacketImpl(packet), (VoicechatConnection)voicechatConnectionImpl1, (VoicechatConnection)voicechatConnectionImpl2, source));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 276 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static PluginManager instance() {
/* 282 */     if (instance == null) {
/* 283 */       instance = new PluginManager();
/* 284 */       instance.init();
/*     */     } 
/* 286 */     return instance;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\PluginManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */