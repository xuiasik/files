/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.events.VoiceHostEvent;
/*    */ 
/*    */ public class VoiceHostEventImpl
/*    */   extends ServerEventImpl implements VoiceHostEvent {
/*    */   private String voiceHost;
/*    */   
/*    */   public VoiceHostEventImpl(String voiceHost) {
/* 10 */     this.voiceHost = voiceHost;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getVoiceHost() {
/* 15 */     return this.voiceHost;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setVoiceHost(String voiceHost) {
/* 20 */     this.voiceHost = voiceHost;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\VoiceHostEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */