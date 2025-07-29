/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.events.ClientVoicechatConnectionEvent;
/*    */ 
/*    */ public class ClientVoicechatConnectionEventImpl
/*    */   extends ClientEventImpl implements ClientVoicechatConnectionEvent {
/*    */   private final boolean connected;
/*    */   
/*    */   public ClientVoicechatConnectionEventImpl(boolean connected) {
/* 10 */     this.connected = connected;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isConnected() {
/* 15 */     return this.connected;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\ClientVoicechatConnectionEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */