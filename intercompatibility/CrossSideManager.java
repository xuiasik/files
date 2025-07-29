/*    */ package de.maxhenkel.voicechat.intercompatibility;
/*    */ 
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public abstract class CrossSideManager
/*    */ {
/*    */   private static CrossSideManager instance;
/*    */   
/*    */   public static CrossSideManager get() {
/* 10 */     if (instance == null) {
/* 11 */       if (CommonCompatibilityManager.INSTANCE.isDedicatedServer()) {
/* 12 */         instance = new DedicatedServerCrossSideManager();
/*    */       } else {
/*    */         try {
/* 15 */           Class<?> crossSideManagerClass = Class.forName("de.maxhenkel.voicechat.intercompatibility.ClientCrossSideManager");
/* 16 */           instance = crossSideManagerClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 17 */         } catch (Exception e) {
/* 18 */           throw new RuntimeException(e);
/*    */         } 
/*    */       } 
/*    */     }
/* 22 */     return instance;
/*    */   }
/*    */   
/*    */   public abstract int getMtuSize();
/*    */   
/*    */   public abstract boolean useNatives();
/*    */   
/*    */   public abstract boolean shouldRunVoiceChatServer(MinecraftServer paramMinecraftServer);
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\intercompatibility\CrossSideManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */