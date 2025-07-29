/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.events.Event;
/*    */ 
/*    */ public class EventImpl
/*    */   implements Event
/*    */ {
/*    */   private boolean cancelled;
/*    */   
/*    */   public boolean isCancellable() {
/* 11 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel() {
/* 16 */     if (!isCancellable()) {
/* 17 */       return false;
/*    */     }
/* 19 */     this.cancelled = true;
/* 20 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 25 */     return this.cancelled;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\EventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */