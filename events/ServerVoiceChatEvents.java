/*    */ package de.maxhenkel.voicechat.events;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import java.util.function.Consumer;
/*    */ import net.fabricmc.fabric.api.event.Event;
/*    */ import net.fabricmc.fabric.api.event.EventFactory;
/*    */ import net.minecraft.class_3222;
/*    */ 
/*    */ 
/*    */ public class ServerVoiceChatEvents
/*    */ {
/* 12 */   public static final Event<Consumer<class_3222>> VOICECHAT_CONNECTED = EventFactory.createArrayBacked(Consumer.class, listeners -> ());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final Event<Consumer<UUID>> VOICECHAT_DISCONNECTED = EventFactory.createArrayBacked(Consumer.class, listeners -> ());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final Event<Consumer<class_3222>> VOICECHAT_COMPATIBILITY_CHECK_SUCCEEDED = EventFactory.createArrayBacked(Consumer.class, listeners -> ());
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\events\ServerVoiceChatEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */