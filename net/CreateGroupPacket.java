/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Group;
/*    */ import de.maxhenkel.voicechat.plugins.impl.GroupImpl;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ 
/*    */ public class CreateGroupPacket
/*    */   implements Packet<CreateGroupPacket>
/*    */ {
/* 13 */   public static final class_2960 CREATE_GROUP = new class_2960("voicechat", "create_group");
/*    */   
/*    */   private String name;
/*    */   
/*    */   @Nullable
/*    */   private String password;
/*    */   
/*    */   private Group.Type type;
/*    */   
/*    */   public CreateGroupPacket() {}
/*    */   
/*    */   public CreateGroupPacket(String name, @Nullable String password, Group.Type type) {
/* 25 */     this.name = name;
/* 26 */     this.password = password;
/* 27 */     this.type = type;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 31 */     return this.name;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public String getPassword() {
/* 36 */     return this.password;
/*    */   }
/*    */   
/*    */   public Group.Type getType() {
/* 40 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2960 getIdentifier() {
/* 45 */     return CREATE_GROUP;
/*    */   }
/*    */ 
/*    */   
/*    */   public CreateGroupPacket fromBytes(class_2540 buf) {
/* 50 */     this.name = buf.method_10800(512);
/* 51 */     this.password = null;
/* 52 */     if (buf.readBoolean()) {
/* 53 */       this.password = buf.method_10800(512);
/*    */     }
/* 55 */     this.type = GroupImpl.TypeImpl.fromInt(buf.readShort());
/* 56 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(class_2540 buf) {
/* 61 */     buf.method_10788(this.name, 512);
/* 62 */     buf.writeBoolean((this.password != null));
/* 63 */     if (this.password != null) {
/* 64 */       buf.method_10788(this.password, 512);
/*    */     }
/* 66 */     buf.writeShort(GroupImpl.TypeImpl.toInt(this.type));
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\CreateGroupPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */