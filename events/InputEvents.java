/*    */ package de.maxhenkel.voicechat.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
/*    */ import net.fabricmc.fabric.api.event.Event;
/*    */ import net.fabricmc.fabric.api.event.EventFactory;
/*    */ 
/*    */ public class InputEvents
/*    */ {
/*  9 */   public static final Event<ClientCompatibilityManager.KeyboardEvent> KEYBOARD_KEY = EventFactory.createArrayBacked(ClientCompatibilityManager.KeyboardEvent.class, listeners -> ());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static final Event<ClientCompatibilityManager.MouseEvent> MOUSE_KEY = EventFactory.createArrayBacked(ClientCompatibilityManager.MouseEvent.class, listeners -> ());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static final Event<Runnable> HANDLE_KEYBINDS = EventFactory.createArrayBacked(Runnable.class, listeners -> ());
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\events\InputEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */