/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ 
/*    */ public class JoinedGroupPacket
/*    */   implements Packet<JoinedGroupPacket>
/*    */ {
/* 12 */   public static final class_2960 JOINED_GROUP = new class_2960("voicechat", "joined_group");
/*    */   
/*    */   @Nullable
/*    */   private UUID group;
/*    */   
/*    */   private boolean wrongPassword;
/*    */ 
/*    */   
/*    */   public JoinedGroupPacket() {}
/*    */   
/*    */   public JoinedGroupPacket(@Nullable UUID group, boolean wrongPassword) {
/* 23 */     this.group = group;
/* 24 */     this.wrongPassword = wrongPassword;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public UUID getGroup() {
/* 29 */     return this.group;
/*    */   }
/*    */   
/*    */   public boolean isWrongPassword() {
/* 33 */     return this.wrongPassword;
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2960 getIdentifier() {
/* 38 */     return JOINED_GROUP;
/*    */   }
/*    */ 
/*    */   
/*    */   public JoinedGroupPacket fromBytes(class_2540 buf) {
/* 43 */     if (buf.readBoolean()) {
/* 44 */       this.group = buf.method_10790();
/*    */     }
/* 46 */     this.wrongPassword = buf.readBoolean();
/* 47 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(class_2540 buf) {
/* 52 */     buf.writeBoolean((this.group != null));
/* 53 */     if (this.group != null) {
/* 54 */       buf.method_10797(this.group);
/*    */     }
/* 56 */     buf.writeBoolean(this.wrongPassword);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\JoinedGroupPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */