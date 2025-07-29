/*    */ package de.maxhenkel.voicechat.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.voice.client.ClientVoicechatConnection;
/*    */ import java.util.function.Consumer;
/*    */ import net.fabricmc.fabric.api.event.Event;
/*    */ import net.fabricmc.fabric.api.event.EventFactory;
/*    */ 
/*    */ 
/*    */ public class ClientVoiceChatEvents
/*    */ {
/* 11 */   public static final Event<Consumer<ClientVoicechatConnection>> VOICECHAT_CONNECTED = EventFactory.createArrayBacked(Consumer.class, listeners -> ());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final Event<Runnable> VOICECHAT_DISCONNECTED = EventFactory.createArrayBacked(Runnable.class, listeners -> ());
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\events\ClientVoiceChatEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */