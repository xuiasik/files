/*   */ package de.maxhenkel.voicechat.plugins.impl.events;
/*   */ 
/*   */ import de.maxhenkel.voicechat.api.events.VoicechatServerStoppedEvent;
/*   */ 
/*   */ public class VoicechatServerStoppedEventImpl
/*   */   extends ServerEventImpl
/*   */   implements VoicechatServerStoppedEvent {
/*   */   public boolean isCancellable() {
/* 9 */     return false;
/*   */   }
/*   */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\VoicechatServerStoppedEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */