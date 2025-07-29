/*    */ package de.maxhenkel.voicechat.events;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.fabricmc.fabric.api.event.Event;
/*    */ import net.fabricmc.fabric.api.event.EventFactory;
/*    */ 
/*    */ 
/*    */ public class PublishServerEvents
/*    */ {
/* 10 */   public static final Event<Consumer<Integer>> SERVER_PUBLISHED = EventFactory.createArrayBacked(Consumer.class, listeners -> ());
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\events\PublishServerEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */