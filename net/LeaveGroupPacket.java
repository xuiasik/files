/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ public class LeaveGroupPacket
/*    */   implements Packet<LeaveGroupPacket>
/*    */ {
/*  9 */   public static final class_2960 LEAVE_GROUP = new class_2960("voicechat", "leave_group");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public class_2960 getIdentifier() {
/* 17 */     return LEAVE_GROUP;
/*    */   }
/*    */ 
/*    */   
/*    */   public LeaveGroupPacket fromBytes(class_2540 buf) {
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   public void toBytes(class_2540 buf) {}
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\LeaveGroupPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */