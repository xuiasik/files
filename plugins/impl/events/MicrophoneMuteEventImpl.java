/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.events.MicrophoneMuteEvent;
/*    */ 
/*    */ public class MicrophoneMuteEventImpl
/*    */   extends ClientEventImpl
/*    */   implements MicrophoneMuteEvent {
/*    */   private final boolean muted;
/*    */   
/*    */   public MicrophoneMuteEventImpl(boolean muted) {
/* 11 */     this.muted = muted;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisabled() {
/* 16 */     return this.muted;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\MicrophoneMuteEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */