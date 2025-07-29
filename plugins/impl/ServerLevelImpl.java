/*    */ package de.maxhenkel.voicechat.plugins.impl;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.ServerLevel;
/*    */ import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.class_3218;
/*    */ 
/*    */ public class ServerLevelImpl
/*    */   implements ServerLevel
/*    */ {
/*    */   private final class_3218 serverLevel;
/*    */   
/*    */   public ServerLevelImpl(class_3218 serverLevel) {
/* 14 */     this.serverLevel = serverLevel;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getServerLevel() {
/* 19 */     return CommonCompatibilityManager.INSTANCE.createRawApiLevel(this.serverLevel);
/*    */   }
/*    */   
/*    */   public class_3218 getRawServerLevel() {
/* 23 */     return this.serverLevel;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 28 */     if (this == object) {
/* 29 */       return true;
/*    */     }
/* 31 */     if (object == null || getClass() != object.getClass()) {
/* 32 */       return false;
/*    */     }
/* 34 */     ServerLevelImpl that = (ServerLevelImpl)object;
/* 35 */     return Objects.equals(this.serverLevel, that.serverLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 40 */     return (this.serverLevel != null) ? this.serverLevel.hashCode() : 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\ServerLevelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */