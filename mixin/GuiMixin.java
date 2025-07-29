/*    */ package de.maxhenkel.voicechat.mixin;
/*    */ 
/*    */ import de.maxhenkel.voicechat.events.RenderEvents;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.class_329;
/*    */ import net.minecraft.class_4587;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin(value = {class_329.class}, priority = 999)
/*    */ public class GuiMixin {
/*    */   @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/IngameGui;renderEffects(Lcom/mojang/blaze3d/matrix/MatrixStack;)V")})
/*    */   private void onHUDRender(class_4587 poseStack, float f, CallbackInfo ci) {
/* 16 */     ((Consumer<class_4587>)RenderEvents.RENDER_HUD.invoker()).accept(poseStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\mixin\GuiMixin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */