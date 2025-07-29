/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.events.CreateOpenALContextEvent;
/*    */ 
/*    */ public class CreateOpenALContextEventImpl
/*    */   extends ClientEventImpl implements CreateOpenALContextEvent {
/*    */   protected long context;
/*    */   protected long device;
/*    */   
/*    */   public CreateOpenALContextEventImpl(long context, long device) {
/* 11 */     this.context = context;
/* 12 */     this.device = device;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getContext() {
/* 17 */     return this.context;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getDevice() {
/* 22 */     return this.device;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancellable() {
/* 27 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\CreateOpenALContextEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */