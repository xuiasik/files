/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.VoicechatClientApi;
/*    */ import de.maxhenkel.voicechat.api.events.ClientEvent;
/*    */ import de.maxhenkel.voicechat.plugins.impl.VoicechatClientApiImpl;
/*    */ 
/*    */ public class ClientEventImpl
/*    */   extends EventImpl
/*    */   implements ClientEvent {
/*    */   public VoicechatClientApi getVoicechat() {
/* 11 */     return VoicechatClientApiImpl.instance();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\ClientEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */