/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ 
/*    */ public class RemoveGroupPacket
/*    */   implements Packet<RemoveGroupPacket>
/*    */ {
/* 11 */   public static final class_2960 REMOVE_GROUP = new class_2960("voicechat", "remove_group");
/*    */   
/*    */   private UUID groupId;
/*    */ 
/*    */   
/*    */   public RemoveGroupPacket() {}
/*    */ 
/*    */   
/*    */   public RemoveGroupPacket(UUID groupId) {
/* 20 */     this.groupId = groupId;
/*    */   }
/*    */   
/*    */   public UUID getGroupId() {
/* 24 */     return this.groupId;
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2960 getIdentifier() {
/* 29 */     return REMOVE_GROUP;
/*    */   }
/*    */ 
/*    */   
/*    */   public RemoveGroupPacket fromBytes(class_2540 buf) {
/* 34 */     this.groupId = buf.method_10790();
/* 35 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(class_2540 buf) {
/* 40 */     buf.method_10797(this.groupId);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\RemoveGroupPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */