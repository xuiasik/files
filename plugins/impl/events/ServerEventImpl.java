/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.VoicechatServerApi;
/*    */ import de.maxhenkel.voicechat.api.events.ServerEvent;
/*    */ import de.maxhenkel.voicechat.plugins.impl.VoicechatServerApiImpl;
/*    */ 
/*    */ public class ServerEventImpl
/*    */   extends EventImpl
/*    */   implements ServerEvent {
/*    */   public VoicechatServerApi getVoicechat() {
/* 11 */     return VoicechatServerApiImpl.instance();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\ServerEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */