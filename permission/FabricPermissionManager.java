/*    */ package de.maxhenkel.voicechat.permission;
/*    */ 
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import me.lucko.fabric.api.permissions.v0.Permissions;
/*    */ import net.fabricmc.loader.api.FabricLoader;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_3222;
/*    */ 
/*    */ public class FabricPermissionManager
/*    */   extends PermissionManager {
/*    */   public Permission createPermissionInternal(final String modId, final String node, final PermissionType type) {
/* 12 */     return new Permission()
/*    */       {
/*    */         public boolean hasPermission(class_3222 player) {
/*    */           try {
/* 16 */             if (FabricPermissionManager.isFabricPermissionsAPILoaded()) {
/* 17 */               return Permissions.check((class_1297)player, modId + "." + node, type.hasPermission(player));
/*    */             }
/* 19 */           } catch (Throwable t) {
/* 20 */             FabricPermissionManager.loaded = Boolean.valueOf(false);
/* 21 */             Voicechat.LOGGER.warn("Failed to use fabric-permissions-api-v0", new Object[] { t });
/* 22 */             Voicechat.LOGGER.info("Disabling fabric-permissions-api-v0 integration", new Object[0]);
/*    */           } 
/* 24 */           return type.hasPermission(player);
/*    */         }
/*    */ 
/*    */         
/*    */         public PermissionType getPermissionType() {
/* 29 */           return type;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   private static Boolean loaded;
/*    */   
/*    */   private static boolean isFabricPermissionsAPILoaded() {
/* 37 */     if (loaded == null) {
/* 38 */       loaded = Boolean.valueOf(FabricLoader.getInstance().isModLoaded("fabric-permissions-api-v0"));
/* 39 */       if (loaded.booleanValue()) {
/* 40 */         Voicechat.LOGGER.info("Using Fabric Permissions API", new Object[0]);
/*    */       }
/*    */     } 
/* 43 */     return loaded.booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\permission\FabricPermissionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */