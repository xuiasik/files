/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ 
/*    */ public class JoinGroupPacket
/*    */   implements Packet<JoinGroupPacket>
/*    */ {
/* 12 */   public static final class_2960 SET_GROUP = new class_2960("voicechat", "set_group");
/*    */   
/*    */   private UUID group;
/*    */   
/*    */   @Nullable
/*    */   private String password;
/*    */ 
/*    */   
/*    */   public JoinGroupPacket() {}
/*    */   
/*    */   public JoinGroupPacket(UUID group, @Nullable String password) {
/* 23 */     this.group = group;
/* 24 */     this.password = password;
/*    */   }
/*    */   
/*    */   public UUID getGroup() {
/* 28 */     return this.group;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public String getPassword() {
/* 33 */     return this.password;
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2960 getIdentifier() {
/* 38 */     return SET_GROUP;
/*    */   }
/*    */ 
/*    */   
/*    */   public JoinGroupPacket fromBytes(class_2540 buf) {
/* 43 */     this.group = buf.method_10790();
/* 44 */     if (buf.readBoolean()) {
/* 45 */       this.password = buf.method_10800(512);
/*    */     }
/* 47 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(class_2540 buf) {
/* 52 */     buf.method_10797(this.group);
/* 53 */     buf.writeBoolean((this.password != null));
/* 54 */     if (this.password != null)
/* 55 */       buf.method_10788(this.password, 512); 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\JoinGroupPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */