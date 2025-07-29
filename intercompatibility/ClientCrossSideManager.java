/*    */ package de.maxhenkel.voicechat.intercompatibility;
/*    */ 
/*    */ import com.sun.jna.Platform;
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.macos.VersionCheck;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientVoicechatConnection;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientCrossSideManager
/*    */   extends CrossSideManager
/*    */ {
/*    */   public int getMtuSize() {
/* 21 */     ClientVoicechat client = ClientManager.getClient();
/* 22 */     if (client != null) {
/* 23 */       ClientVoicechatConnection connection = client.getConnection();
/* 24 */       if (connection != null) {
/* 25 */         return connection.getData().getMtuSize();
/*    */       }
/*    */     } 
/* 28 */     return ((Integer)Voicechat.SERVER_CONFIG.voiceChatMtuSize.get()).intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean useNatives() {
/* 33 */     if (Platform.isMac() && 
/* 34 */       !VersionCheck.isMacOSNativeCompatible()) {
/* 35 */       return false;
/*    */     }
/*    */     
/* 38 */     if (VoicechatClient.CLIENT_CONFIG == null) {
/* 39 */       return ((Boolean)Voicechat.SERVER_CONFIG.useNatives.get()).booleanValue();
/*    */     }
/* 41 */     return ((Boolean)VoicechatClient.CLIENT_CONFIG.useNatives.get()).booleanValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRunVoiceChatServer(MinecraftServer server) {
/* 46 */     return (server instanceof net.minecraft.class_3176 || VoicechatClient.CLIENT_CONFIG == null || ((Boolean)VoicechatClient.CLIENT_CONFIG.runLocalServer.get()).booleanValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\intercompatibility\ClientCrossSideManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */