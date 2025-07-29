/*    */ package de.maxhenkel.voicechat.intercompatibility;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import de.maxhenkel.voicechat.api.VoicechatPlugin;
/*    */ import de.maxhenkel.voicechat.api.VoicechatServerApi;
/*    */ import de.maxhenkel.voicechat.net.NetManager;
/*    */ import de.maxhenkel.voicechat.permission.PermissionManager;
/*    */ import de.maxhenkel.voicechat.plugins.impl.VoicechatServerApiImpl;
/*    */ import de.maxhenkel.voicechat.service.Service;
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_1657;
/*    */ import net.minecraft.class_2168;
/*    */ import net.minecraft.class_3218;
/*    */ import net.minecraft.class_3222;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ 
/*    */ public abstract class CommonCompatibilityManager
/*    */ {
/* 24 */   public static CommonCompatibilityManager INSTANCE = (CommonCompatibilityManager)Service.get(CommonCompatibilityManager.class);
/*    */   
/*    */   public abstract String getModVersion();
/*    */   
/*    */   public abstract String getModName();
/*    */   
/*    */   public abstract Path getGameDirectory();
/*    */   
/*    */   public abstract void emitServerVoiceChatConnectedEvent(class_3222 paramclass_3222);
/*    */   
/*    */   public abstract void emitServerVoiceChatDisconnectedEvent(UUID paramUUID);
/*    */   
/*    */   public abstract void emitPlayerCompatibilityCheckSucceeded(class_3222 paramclass_3222);
/*    */   
/*    */   public abstract void onServerVoiceChatConnected(Consumer<class_3222> paramConsumer);
/*    */   
/*    */   public abstract void onServerVoiceChatDisconnected(Consumer<UUID> paramConsumer);
/*    */   
/*    */   public abstract void onServerStarting(Consumer<MinecraftServer> paramConsumer);
/*    */   
/*    */   public abstract void onServerStopping(Consumer<MinecraftServer> paramConsumer);
/*    */   
/*    */   public abstract void onPlayerLoggedIn(Consumer<class_3222> paramConsumer);
/*    */   
/*    */   public abstract void onPlayerLoggedOut(Consumer<class_3222> paramConsumer);
/*    */   
/*    */   public abstract void onPlayerCompatibilityCheckSucceeded(Consumer<class_3222> paramConsumer);
/*    */   
/*    */   public abstract void onRegisterServerCommands(Consumer<CommandDispatcher<class_2168>> paramConsumer);
/*    */   
/*    */   public abstract NetManager getNetManager();
/*    */   
/*    */   public abstract boolean isDevEnvironment();
/*    */   
/*    */   public abstract boolean isDedicatedServer();
/*    */   
/*    */   public abstract boolean isModLoaded(String paramString);
/*    */   
/*    */   public abstract List<VoicechatPlugin> loadPlugins();
/*    */   
/*    */   public abstract PermissionManager createPermissionManager();
/*    */   
/*    */   public VoicechatServerApi getServerApi() {
/* 67 */     return (VoicechatServerApi)VoicechatServerApiImpl.INSTANCE;
/*    */   }
/*    */   
/*    */   public Object createRawApiEntity(class_1297 entity) {
/* 71 */     return entity;
/*    */   }
/*    */   
/*    */   public Object createRawApiPlayer(class_1657 player) {
/* 75 */     return player;
/*    */   }
/*    */   
/*    */   public Object createRawApiLevel(class_3218 level) {
/* 79 */     return level;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\intercompatibility\CommonCompatibilityManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */