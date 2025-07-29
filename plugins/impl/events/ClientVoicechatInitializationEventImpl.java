/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.ClientVoicechatSocket;
/*    */ import de.maxhenkel.voicechat.api.events.ClientVoicechatInitializationEvent;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class ClientVoicechatInitializationEventImpl
/*    */   extends ClientEventImpl
/*    */   implements ClientVoicechatInitializationEvent
/*    */ {
/*    */   @Nullable
/*    */   private ClientVoicechatSocket socketImplementation;
/*    */   
/*    */   public void setSocketImplementation(ClientVoicechatSocket socket) {
/* 15 */     this.socketImplementation = socket;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ClientVoicechatSocket getSocketImplementation() {
/* 21 */     return this.socketImplementation;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancellable() {
/* 26 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\ClientVoicechatInitializationEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */