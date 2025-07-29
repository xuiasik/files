/*    */ package de.maxhenkel.voicechat.mixin;
/*    */ 
/*    */ import de.maxhenkel.voicechat.events.ClientWorldEvents;
/*    */ import de.maxhenkel.voicechat.events.InputEvents;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_638;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({class_310.class})
/*    */ public class MinecraftMixin
/*    */ {
/*    */   @Shadow
/*    */   public class_638 field_1687;
/*    */   
/*    */   @Inject(at = {@At("HEAD")}, method = {"clearLevel(Lnet/minecraft/client/gui/screen/Screen;)V"})
/*    */   private void disconnect(class_437 screen, CallbackInfo info) {
/* 22 */     if (this.field_1687 != null) {
/* 23 */       ((Runnable)ClientWorldEvents.DISCONNECT.invoker()).run();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(at = {@At("HEAD")}, method = {"handleKeybinds"})
/*    */   private void handleKeybinds(CallbackInfo info) {
/* 29 */     ((Runnable)InputEvents.HANDLE_KEYBINDS.invoker()).run();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\mixin\MinecraftMixin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */