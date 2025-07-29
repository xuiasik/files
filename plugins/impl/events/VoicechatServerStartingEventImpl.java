/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.VoicechatSocket;
/*    */ import de.maxhenkel.voicechat.api.events.VoicechatServerStartingEvent;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class VoicechatServerStartingEventImpl
/*    */   extends ServerEventImpl
/*    */   implements VoicechatServerStartingEvent
/*    */ {
/*    */   @Nullable
/*    */   private VoicechatSocket socketImplementation;
/*    */   
/*    */   public void setSocketImplementation(VoicechatSocket socket) {
/* 15 */     this.socketImplementation = socket;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public VoicechatSocket getSocketImplementation() {
/* 21 */     return this.socketImplementation;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancellable() {
/* 26 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\VoicechatServerStartingEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */