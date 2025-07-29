/*    */ package de.maxhenkel.voicechat.mixin;
/*    */ 
/*    */ import de.maxhenkel.voicechat.events.InputEvents;
/*    */ import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
/*    */ import net.minecraft.class_309;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({class_309.class})
/*    */ public class KeyboardMixin {
/*    */   @Inject(at = {@At("HEAD")}, method = {"keyPress"})
/*    */   private void onKey(long window, int key, int scancode, int i, int j, CallbackInfo info) {
/* 15 */     ((ClientCompatibilityManager.KeyboardEvent)InputEvents.KEYBOARD_KEY.invoker()).onKeyboardEvent(window, key, scancode);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\mixin\KeyboardMixin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */