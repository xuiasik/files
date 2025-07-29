/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ public class PlayerStatePacket
/*    */   implements Packet<PlayerStatePacket>
/*    */ {
/* 10 */   public static final class_2960 PLAYER_STATE = new class_2960("voicechat", "player_state");
/*    */   
/*    */   private PlayerState playerState;
/*    */ 
/*    */   
/*    */   public PlayerStatePacket() {}
/*    */ 
/*    */   
/*    */   public PlayerStatePacket(PlayerState playerState) {
/* 19 */     this.playerState = playerState;
/*    */   }
/*    */   
/*    */   public PlayerState getPlayerState() {
/* 23 */     return this.playerState;
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2960 getIdentifier() {
/* 28 */     return PLAYER_STATE;
/*    */   }
/*    */ 
/*    */   
/*    */   public PlayerStatePacket fromBytes(class_2540 buf) {
/* 33 */     this.playerState = PlayerState.fromBytes(buf);
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(class_2540 buf) {
/* 39 */     this.playerState.toBytes(buf);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\PlayerStatePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */