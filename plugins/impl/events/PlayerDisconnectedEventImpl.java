/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.events.PlayerDisconnectedEvent;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class PlayerDisconnectedEventImpl
/*    */   extends ServerEventImpl
/*    */   implements PlayerDisconnectedEvent {
/*    */   protected UUID player;
/*    */   
/*    */   public PlayerDisconnectedEventImpl(UUID player) {
/* 12 */     this.player = player;
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getPlayerUuid() {
/* 17 */     return this.player;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\PlayerDisconnectedEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */