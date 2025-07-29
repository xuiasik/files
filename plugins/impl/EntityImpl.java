/*    */ package de.maxhenkel.voicechat.plugins.impl;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Entity;
/*    */ import de.maxhenkel.voicechat.api.Position;
/*    */ import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.class_1297;
/*    */ 
/*    */ public class EntityImpl
/*    */   implements Entity {
/*    */   protected class_1297 entity;
/*    */   
/*    */   public EntityImpl(class_1297 entity) {
/* 15 */     this.entity = entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getUuid() {
/* 20 */     return this.entity.method_5667();
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getEntity() {
/* 25 */     return CommonCompatibilityManager.INSTANCE.createRawApiEntity(this.entity);
/*    */   }
/*    */ 
/*    */   
/*    */   public Position getPosition() {
/* 30 */     return new PositionImpl(this.entity.method_19538());
/*    */   }
/*    */   
/*    */   public class_1297 getRealEntity() {
/* 34 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 39 */     if (this == object) {
/* 40 */       return true;
/*    */     }
/* 42 */     if (object == null || getClass() != object.getClass()) {
/* 43 */       return false;
/*    */     }
/* 45 */     EntityImpl entity1 = (EntityImpl)object;
/* 46 */     return Objects.equals(this.entity, entity1.entity);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 51 */     return (this.entity != null) ? this.entity.hashCode() : 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\EntityImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */