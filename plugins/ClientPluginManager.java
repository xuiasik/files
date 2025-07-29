/*     */ package de.maxhenkel.voicechat.plugins;
/*     */ import de.maxhenkel.voicechat.Voicechat;
/*     */ import de.maxhenkel.voicechat.api.ClientVoicechatSocket;
/*     */ import de.maxhenkel.voicechat.api.events.ClientReceiveSoundEvent;
/*     */ import de.maxhenkel.voicechat.api.events.OpenALSoundEvent;
/*     */ import de.maxhenkel.voicechat.plugins.impl.ClientVoicechatSocketImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.PositionImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.events.ClientReceiveSoundEventImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.events.ClientSoundEventImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.events.ClientVoicechatInitializationEventImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.events.MergeClientSoundEventImpl;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.class_243;
/*     */ 
/*     */ public class ClientPluginManager {
/*     */   private final PluginManager pluginManager;
/*     */   
/*     */   public ClientPluginManager(PluginManager pluginManager) {
/*  21 */     this.pluginManager = pluginManager;
/*     */   } private static ClientPluginManager instance;
/*     */   public ClientVoicechatSocket getClientSocketImplementation() {
/*     */     ClientVoicechatSocketImpl clientVoicechatSocketImpl;
/*  25 */     ClientVoicechatInitializationEventImpl event = new ClientVoicechatInitializationEventImpl();
/*  26 */     this.pluginManager.dispatchEvent(ClientVoicechatInitializationEvent.class, event);
/*  27 */     ClientVoicechatSocket socket = event.getSocketImplementation();
/*  28 */     if (socket == null) {
/*  29 */       clientVoicechatSocketImpl = new ClientVoicechatSocketImpl();
/*  30 */       Voicechat.LOGGER.debug("Using default voicechat client socket implementation", new Object[0]);
/*     */     } else {
/*  32 */       Voicechat.LOGGER.info("Using custom voicechat client socket implementation: {}", new Object[] { clientVoicechatSocketImpl.getClass().getName() });
/*     */     } 
/*  34 */     return (ClientVoicechatSocket)clientVoicechatSocketImpl;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public short[] onMergeClientSound(@Nullable short[] rawAudio) {
/*  39 */     MergeClientSoundEventImpl event = new MergeClientSoundEventImpl();
/*  40 */     this.pluginManager.dispatchEvent(MergeClientSoundEvent.class, event);
/*  41 */     List<short[]> audioToMerge = event.getAudioToMerge();
/*  42 */     if (audioToMerge == null) {
/*  43 */       return rawAudio;
/*     */     }
/*  45 */     if (rawAudio != null) {
/*  46 */       audioToMerge.add(0, rawAudio);
/*     */     }
/*  48 */     return Utils.combineAudio(audioToMerge);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public short[] onClientSound(short[] rawAudio, boolean whispering) {
/*  53 */     ClientSoundEventImpl clientSoundEvent = new ClientSoundEventImpl(rawAudio, whispering);
/*  54 */     boolean cancelled = this.pluginManager.dispatchEvent(ClientSoundEvent.class, clientSoundEvent);
/*  55 */     if (cancelled) {
/*  56 */       return null;
/*     */     }
/*  58 */     return clientSoundEvent.getRawAudio();
/*     */   }
/*     */   
/*     */   public short[] onReceiveEntityClientSound(UUID id, UUID entity, short[] rawAudio, boolean whispering, float distance) {
/*  62 */     ClientReceiveSoundEventImpl.EntitySoundImpl clientSoundEvent = new ClientReceiveSoundEventImpl.EntitySoundImpl(id, entity, rawAudio, whispering, distance);
/*  63 */     this.pluginManager.dispatchEvent(ClientReceiveSoundEvent.EntitySound.class, clientSoundEvent);
/*  64 */     return clientSoundEvent.getRawAudio();
/*     */   }
/*     */   
/*     */   public short[] onReceiveLocationalClientSound(UUID id, short[] rawAudio, class_243 pos, float distance) {
/*  68 */     ClientReceiveSoundEventImpl.LocationalSoundImpl clientSoundEvent = new ClientReceiveSoundEventImpl.LocationalSoundImpl(id, rawAudio, (Position)new PositionImpl(pos), distance);
/*  69 */     this.pluginManager.dispatchEvent(ClientReceiveSoundEvent.LocationalSound.class, clientSoundEvent);
/*  70 */     return clientSoundEvent.getRawAudio();
/*     */   }
/*     */   
/*     */   public short[] onReceiveStaticClientSound(UUID id, short[] rawAudio) {
/*  74 */     ClientReceiveSoundEventImpl.StaticSoundImpl clientSoundEvent = new ClientReceiveSoundEventImpl.StaticSoundImpl(id, rawAudio);
/*  75 */     this.pluginManager.dispatchEvent(ClientReceiveSoundEvent.StaticSound.class, clientSoundEvent);
/*  76 */     return clientSoundEvent.getRawAudio();
/*     */   }
/*     */   
/*     */   public void onALSound(int source, @Nullable UUID channelId, @Nullable class_243 pos, @Nullable String category, Class<? extends OpenALSoundEvent> eventClass) {
/*  80 */     this.pluginManager.dispatchEvent(eventClass, new OpenALSoundEventImpl(channelId, (pos == null) ? null : new PositionImpl(pos), category, source));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCreateALContext(long context, long device) {
/*  89 */     this.pluginManager.dispatchEvent(CreateOpenALContextEvent.class, new CreateOpenALContextEventImpl(context, device));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDestroyALContext(long context, long device) {
/*  96 */     this.pluginManager.dispatchEvent(DestroyOpenALContextEvent.class, new DestroyOpenALContextEventImpl(context, device));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClientPluginManager instance() {
/* 105 */     if (instance == null) {
/* 106 */       instance = new ClientPluginManager(PluginManager.instance());
/*     */     }
/* 108 */     return instance;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\ClientPluginManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */