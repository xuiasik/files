/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerStatesPacket
/*    */   implements Packet<PlayerStatesPacket>
/*    */ {
/*    */   private Map<UUID, PlayerState> playerStates;
/* 16 */   public static final class_2960 PLAYER_STATES = new class_2960("voicechat", "player_states");
/*    */ 
/*    */   
/*    */   public PlayerStatesPacket() {}
/*    */ 
/*    */   
/*    */   public PlayerStatesPacket(Map<UUID, PlayerState> playerStates) {
/* 23 */     this.playerStates = playerStates;
/*    */   }
/*    */   
/*    */   public Map<UUID, PlayerState> getPlayerStates() {
/* 27 */     return this.playerStates;
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2960 getIdentifier() {
/* 32 */     return PLAYER_STATES;
/*    */   }
/*    */ 
/*    */   
/*    */   public PlayerStatesPacket fromBytes(class_2540 buf) {
/* 37 */     this.playerStates = new HashMap<>();
/* 38 */     int count = buf.readInt();
/* 39 */     for (int i = 0; i < count; i++) {
/* 40 */       PlayerState playerState = PlayerState.fromBytes(buf);
/* 41 */       this.playerStates.put(playerState.getUuid(), playerState);
/*    */     } 
/*    */     
/* 44 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(class_2540 buf) {
/* 49 */     buf.writeInt(this.playerStates.size());
/* 50 */     for (Map.Entry<UUID, PlayerState> entry : this.playerStates.entrySet())
/* 51 */       ((PlayerState)entry.getValue()).toBytes(buf); 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\PlayerStatesPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */