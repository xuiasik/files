/*    */ package de.maxhenkel.voicechat.mixin;
/*    */ 
/*    */ import de.maxhenkel.voicechat.events.InputEvents;
/*    */ import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
/*    */ import net.minecraft.class_312;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({class_312.class})
/*    */ public class MouseMixin {
/*    */   @Inject(at = {@At("HEAD")}, method = {"onPress"})
/*    */   private void onMouseButton(long window, int button, int action, int mods, CallbackInfo info) {
/* 15 */     ((ClientCompatibilityManager.MouseEvent)InputEvents.MOUSE_KEY.invoker()).onMouseEvent(window, button, action, mods);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\mixin\MouseMixin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */