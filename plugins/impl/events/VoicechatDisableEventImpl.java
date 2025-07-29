/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.events.VoicechatDisableEvent;
/*    */ 
/*    */ public class VoicechatDisableEventImpl
/*    */   extends ClientEventImpl implements VoicechatDisableEvent {
/*    */   private final boolean disabled;
/*    */   
/*    */   public VoicechatDisableEventImpl(boolean disabled) {
/* 10 */     this.disabled = disabled;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisabled() {
/* 15 */     return this.disabled;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\VoicechatDisableEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */