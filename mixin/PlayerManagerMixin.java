/*    */ package de.maxhenkel.voicechat.mixin;
/*    */ 
/*    */ import de.maxhenkel.voicechat.events.PlayerEvents;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.class_2535;
/*    */ import net.minecraft.class_3222;
/*    */ import net.minecraft.class_3324;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({class_3324.class})
/*    */ public class PlayerManagerMixin
/*    */ {
/*    */   @Inject(at = {@At("RETURN")}, method = {"placeNewPlayer"})
/*    */   private void onPlayerConnect(class_2535 connection, class_3222 player, CallbackInfo info) {
/* 18 */     ((Consumer<class_3222>)PlayerEvents.PLAYER_LOGGED_IN.invoker()).accept(player);
/*    */   }
/*    */   
/*    */   @Inject(at = {@At("HEAD")}, method = {"remove"})
/*    */   private void onPlayerConnect(class_3222 player, CallbackInfo info) {
/* 23 */     ((Consumer<class_3222>)PlayerEvents.PLAYER_LOGGED_OUT.invoker()).accept(player);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\mixin\PlayerManagerMixin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */