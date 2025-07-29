/*    */ package de.maxhenkel.voicechat.intercompatibility;
/*    */ 
/*    */ import com.sun.jna.Platform;
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import de.maxhenkel.voicechat.macos.VersionCheck;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class DedicatedServerCrossSideManager
/*    */   extends CrossSideManager
/*    */ {
/*    */   public int getMtuSize() {
/* 12 */     return ((Integer)Voicechat.SERVER_CONFIG.voiceChatMtuSize.get()).intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean useNatives() {
/* 17 */     if (Platform.isMac() && 
/* 18 */       !VersionCheck.isMacOSNativeCompatible()) {
/* 19 */       return false;
/*    */     }
/*    */     
/* 22 */     return ((Boolean)Voicechat.SERVER_CONFIG.useNatives.get()).booleanValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRunVoiceChatServer(MinecraftServer server) {
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\intercompatibility\DedicatedServerCrossSideManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */