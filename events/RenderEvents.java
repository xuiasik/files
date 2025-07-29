/*    */ package de.maxhenkel.voicechat.events;
/*    */ import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
/*    */ import java.util.function.Consumer;
/*    */ import net.fabricmc.fabric.api.event.Event;
/*    */ import net.fabricmc.fabric.api.event.EventFactory;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_4597;
/*    */ 
/*    */ public class RenderEvents {
/* 12 */   public static final Event<ClientCompatibilityManager.RenderNameplateEvent> RENDER_NAMEPLATE = EventFactory.createArrayBacked(ClientCompatibilityManager.RenderNameplateEvent.class, listeners -> ());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final Event<Consumer<class_4587>> RENDER_HUD = EventFactory.createArrayBacked(Consumer.class, listeners -> ());
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\events\RenderEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */