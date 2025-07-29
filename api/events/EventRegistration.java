/*    */ package de.maxhenkel.voicechat.api.events;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface EventRegistration
/*    */ {
/*    */   <T extends Event> void registerEvent(Class<T> paramClass, Consumer<T> paramConsumer, int paramInt);
/*    */   
/*    */   default <T extends Event> void registerEvent(Class<T> eventClass, Consumer<T> onPacket) {
/* 25 */     registerEvent(eventClass, onPacket, 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\events\EventRegistration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */