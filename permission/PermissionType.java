/*    */ package de.maxhenkel.voicechat.permission;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_3222;
/*    */ 
/*    */ 
/*    */ public enum PermissionType
/*    */ {
/*  9 */   EVERYONE, NOONE, OPS;
/*    */   
/*    */   boolean hasPermission(@Nullable class_3222 player) {
/* 12 */     switch (this)
/*    */     { case EVERYONE:
/* 14 */         return true;
/*    */       
/*    */       default:
/* 17 */         return false;
/*    */       case OPS:
/* 19 */         break; }  return (player != null && player.method_5687(player.field_13995.method_3798()));
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\permission\PermissionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */