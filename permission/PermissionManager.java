/*    */ package de.maxhenkel.voicechat.permission;
/*    */ 
/*    */ import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PermissionManager
/*    */ {
/* 11 */   public static PermissionManager INSTANCE = CommonCompatibilityManager.INSTANCE.createPermissionManager();
/*    */   
/*    */   public final Permission LISTEN_PERMISSION;
/*    */   
/*    */   public final Permission SPEAK_PERMISSION;
/*    */   public final Permission GROUPS_PERMISSION;
/*    */   public final Permission ADMIN_PERMISSION;
/* 18 */   protected List<Permission> permissions = new ArrayList<>();
/*    */   
/*    */   public PermissionManager() {
/* 21 */     this.LISTEN_PERMISSION = createPermission("voicechat", "listen", PermissionType.EVERYONE);
/* 22 */     this.SPEAK_PERMISSION = createPermission("voicechat", "speak", PermissionType.EVERYONE);
/* 23 */     this.GROUPS_PERMISSION = createPermission("voicechat", "groups", PermissionType.EVERYONE);
/* 24 */     this.ADMIN_PERMISSION = createPermission("voicechat", "admin", PermissionType.OPS);
/*    */   }
/*    */   
/*    */   public abstract Permission createPermissionInternal(String paramString1, String paramString2, PermissionType paramPermissionType);
/*    */   
/*    */   public Permission createPermission(String modId, String node, PermissionType type) {
/* 30 */     Permission p = createPermissionInternal(modId, node, type);
/* 31 */     this.permissions.add(p);
/* 32 */     return p;
/*    */   }
/*    */   
/*    */   public List<Permission> getPermissions() {
/* 36 */     return this.permissions;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\permission\PermissionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */