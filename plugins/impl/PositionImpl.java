/*    */ package de.maxhenkel.voicechat.plugins.impl;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Position;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.class_243;
/*    */ 
/*    */ public class PositionImpl
/*    */   implements Position
/*    */ {
/*    */   private final class_243 position;
/*    */   
/*    */   public PositionImpl(class_243 position) {
/* 13 */     this.position = position;
/*    */   }
/*    */   
/*    */   public PositionImpl(double x, double y, double z) {
/* 17 */     this.position = new class_243(x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   public double getX() {
/* 22 */     return this.position.field_1352;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getY() {
/* 27 */     return this.position.field_1351;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getZ() {
/* 32 */     return this.position.field_1350;
/*    */   }
/*    */   
/*    */   public class_243 getPosition() {
/* 36 */     return this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 41 */     if (this == object) {
/* 42 */       return true;
/*    */     }
/* 44 */     if (object == null || getClass() != object.getClass()) {
/* 45 */       return false;
/*    */     }
/* 47 */     PositionImpl position1 = (PositionImpl)object;
/* 48 */     return Objects.equals(this.position, position1.position);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 53 */     return (this.position != null) ? this.position.hashCode() : 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\PositionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */