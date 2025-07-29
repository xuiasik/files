/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*    */ import de.maxhenkel.voicechat.api.events.PlayerConnectedEvent;
/*    */ import de.maxhenkel.voicechat.plugins.impl.VoicechatConnectionImpl;
/*    */ 
/*    */ public class PlayerConnectedEventImpl
/*    */   extends ServerEventImpl implements PlayerConnectedEvent {
/*    */   protected VoicechatConnectionImpl connection;
/*    */   
/*    */   public PlayerConnectedEventImpl(VoicechatConnectionImpl connection) {
/* 12 */     this.connection = connection;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoicechatConnection getConnection() {
/* 17 */     return (VoicechatConnection)this.connection;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\PlayerConnectedEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */