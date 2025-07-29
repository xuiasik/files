/*   */ package de.maxhenkel.voicechat.plugins.impl.events;
/*   */ 
/*   */ import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
/*   */ 
/*   */ public class VoicechatServerStartedEventImpl
/*   */   extends ServerEventImpl
/*   */   implements VoicechatServerStartedEvent {
/*   */   public boolean isCancellable() {
/* 9 */     return false;
/*   */   }
/*   */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\VoicechatServerStartedEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */