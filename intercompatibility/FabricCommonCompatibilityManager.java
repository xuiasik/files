/*     */ package de.maxhenkel.voicechat.intercompatibility;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import de.maxhenkel.voicechat.api.VoicechatPlugin;
/*     */ import de.maxhenkel.voicechat.events.PlayerEvents;
/*     */ import de.maxhenkel.voicechat.events.ServerVoiceChatEvents;
/*     */ import de.maxhenkel.voicechat.net.FabricNetManager;
/*     */ import de.maxhenkel.voicechat.net.NetManager;
/*     */ import de.maxhenkel.voicechat.permission.FabricPermissionManager;
/*     */ import de.maxhenkel.voicechat.permission.PermissionManager;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.fabricmc.api.EnvType;
/*     */ import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
/*     */ import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
/*     */ import net.fabricmc.loader.api.FabricLoader;
/*     */ import net.fabricmc.loader.api.ModContainer;
/*     */ import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
/*     */ import net.minecraft.class_2168;
/*     */ import net.minecraft.class_3222;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ 
/*     */ public class FabricCommonCompatibilityManager
/*     */   extends CommonCompatibilityManager
/*     */ {
/*     */   private FabricNetManager netManager;
/*     */   
/*     */   public String getModVersion() {
/*  32 */     ModContainer modContainer = FabricLoader.getInstance().getModContainer("voicechat").orElse(null);
/*  33 */     if (modContainer == null) {
/*  34 */       return "N/A";
/*     */     }
/*  36 */     return modContainer.getMetadata().getVersion().getFriendlyString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getModName() {
/*  41 */     ModContainer modContainer = FabricLoader.getInstance().getModContainer("voicechat").orElse(null);
/*  42 */     if (modContainer == null) {
/*  43 */       return "voicechat";
/*     */     }
/*  45 */     return modContainer.getMetadata().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getGameDirectory() {
/*  50 */     return FabricLoader.getInstance().getGameDir();
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitServerVoiceChatConnectedEvent(class_3222 player) {
/*  55 */     ((Consumer<class_3222>)ServerVoiceChatEvents.VOICECHAT_CONNECTED.invoker()).accept(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitServerVoiceChatDisconnectedEvent(UUID clientID) {
/*  60 */     ((Consumer<UUID>)ServerVoiceChatEvents.VOICECHAT_DISCONNECTED.invoker()).accept(clientID);
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitPlayerCompatibilityCheckSucceeded(class_3222 player) {
/*  65 */     ((Consumer<class_3222>)ServerVoiceChatEvents.VOICECHAT_COMPATIBILITY_CHECK_SUCCEEDED.invoker()).accept(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onServerVoiceChatConnected(Consumer<class_3222> onVoiceChatConnected) {
/*  70 */     ServerVoiceChatEvents.VOICECHAT_CONNECTED.register(onVoiceChatConnected);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onServerVoiceChatDisconnected(Consumer<UUID> onVoiceChatDisconnected) {
/*  75 */     ServerVoiceChatEvents.VOICECHAT_DISCONNECTED.register(onVoiceChatDisconnected);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onServerStarting(Consumer<MinecraftServer> onServerStarting) {
/*  80 */     ServerLifecycleEvents.SERVER_STARTED.register(onServerStarting::accept);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onServerStopping(Consumer<MinecraftServer> onServerStopping) {
/*  85 */     ServerLifecycleEvents.SERVER_STOPPING.register(onServerStopping::accept);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerLoggedIn(Consumer<class_3222> onPlayerLoggedIn) {
/*  90 */     PlayerEvents.PLAYER_LOGGED_IN.register(onPlayerLoggedIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerLoggedOut(Consumer<class_3222> onPlayerLoggedOut) {
/*  95 */     PlayerEvents.PLAYER_LOGGED_OUT.register(onPlayerLoggedOut);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerCompatibilityCheckSucceeded(Consumer<class_3222> onPlayerCompatibilityCheckSucceeded) {
/* 100 */     ServerVoiceChatEvents.VOICECHAT_COMPATIBILITY_CHECK_SUCCEEDED.register(onPlayerCompatibilityCheckSucceeded);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRegisterServerCommands(Consumer<CommandDispatcher<class_2168>> onRegisterServerCommands) {
/* 105 */     CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> onRegisterServerCommands.accept(dispatcher));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetManager getNetManager() {
/* 112 */     if (this.netManager == null) {
/* 113 */       this.netManager = new FabricNetManager();
/*     */     }
/* 115 */     return (NetManager)this.netManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDevEnvironment() {
/* 120 */     return FabricLoader.getInstance().isDevelopmentEnvironment();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDedicatedServer() {
/* 125 */     return FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isModLoaded(String modId) {
/* 130 */     return FabricLoader.getInstance().isModLoaded(modId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<VoicechatPlugin> loadPlugins() {
/* 135 */     return (List<VoicechatPlugin>)FabricLoader.getInstance().getEntrypointContainers("voicechat", VoicechatPlugin.class).stream().map(EntrypointContainer::getEntrypoint).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   public PermissionManager createPermissionManager() {
/* 140 */     return (PermissionManager)new FabricPermissionManager();
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\intercompatibility\FabricCommonCompatibilityManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */