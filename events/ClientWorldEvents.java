/*   */ package de.maxhenkel.voicechat.events;
/*   */ 
/*   */ import net.fabricmc.fabric.api.event.Event;
/*   */ import net.fabricmc.fabric.api.event.EventFactory;
/*   */ 
/*   */ public class ClientWorldEvents
/*   */ {
/* 8 */   public static final Event<Runnable> DISCONNECT = EventFactory.createArrayBacked(Runnable.class, listeners -> ());
/*   */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\events\ClientWorldEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */