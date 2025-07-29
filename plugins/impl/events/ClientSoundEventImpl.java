/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.events.ClientSoundEvent;
/*    */ 
/*    */ public class ClientSoundEventImpl
/*    */   extends ClientEventImpl implements ClientSoundEvent {
/*    */   private short[] rawAudio;
/*    */   private boolean whispering;
/*    */   
/*    */   public ClientSoundEventImpl(short[] rawAudio, boolean whispering) {
/* 11 */     this.rawAudio = rawAudio;
/* 12 */     this.whispering = whispering;
/*    */   }
/*    */ 
/*    */   
/*    */   public short[] getRawAudio() {
/* 17 */     return this.rawAudio;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRawAudio(short[] rawAudio) {
/* 22 */     this.rawAudio = rawAudio;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWhispering() {
/* 27 */     return this.whispering;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\ClientSoundEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */