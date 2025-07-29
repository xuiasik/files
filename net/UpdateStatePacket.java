/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ public class UpdateStatePacket
/*    */   implements Packet<UpdateStatePacket>
/*    */ {
/*  9 */   public static final class_2960 PLAYER_STATE = new class_2960("voicechat", "update_state");
/*    */   
/*    */   private boolean disabled;
/*    */ 
/*    */   
/*    */   public UpdateStatePacket() {}
/*    */ 
/*    */   
/*    */   public UpdateStatePacket(boolean disabled) {
/* 18 */     this.disabled = disabled;
/*    */   }
/*    */   
/*    */   public boolean isDisabled() {
/* 22 */     return this.disabled;
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2960 getIdentifier() {
/* 27 */     return PLAYER_STATE;
/*    */   }
/*    */ 
/*    */   
/*    */   public UpdateStatePacket fromBytes(class_2540 buf) {
/* 32 */     this.disabled = buf.readBoolean();
/* 33 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(class_2540 buf) {
/* 38 */     buf.writeBoolean(this.disabled);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\UpdateStatePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */