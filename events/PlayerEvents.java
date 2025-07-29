/*    */ package de.maxhenkel.voicechat.events;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.fabricmc.fabric.api.event.Event;
/*    */ import net.fabricmc.fabric.api.event.EventFactory;
/*    */ import net.minecraft.class_3222;
/*    */ 
/*    */ 
/*    */ public class PlayerEvents
/*    */ {
/* 11 */   public static final Event<Consumer<class_3222>> PLAYER_LOGGED_IN = EventFactory.createArrayBacked(Consumer.class, listeners -> ());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final Event<Consumer<class_3222>> PLAYER_LOGGED_OUT = EventFactory.createArrayBacked(Consumer.class, listeners -> ());
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\events\PlayerEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */