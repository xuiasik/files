/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import de.maxhenkel.voicechat.voice.common.ClientGroup;
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ public class AddGroupPacket
/*    */   implements Packet<AddGroupPacket>
/*    */ {
/* 10 */   public static final class_2960 ADD_ADD_GROUP = new class_2960("voicechat", "add_group");
/*    */   
/*    */   private ClientGroup group;
/*    */ 
/*    */   
/*    */   public AddGroupPacket() {}
/*    */ 
/*    */   
/*    */   public AddGroupPacket(ClientGroup group) {
/* 19 */     this.group = group;
/*    */   }
/*    */   
/*    */   public ClientGroup getGroup() {
/* 23 */     return this.group;
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2960 getIdentifier() {
/* 28 */     return ADD_ADD_GROUP;
/*    */   }
/*    */ 
/*    */   
/*    */   public AddGroupPacket fromBytes(class_2540 buf) {
/* 33 */     this.group = ClientGroup.fromBytes(buf);
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(class_2540 buf) {
/* 39 */     this.group.toBytes(buf);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\AddGroupPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */