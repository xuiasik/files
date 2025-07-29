/*    */ package de.maxhenkel.voicechat.mixin;
/*    */ 
/*    */ import de.maxhenkel.voicechat.events.RenderEvents;
/*    */ import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_4597;
/*    */ import net.minecraft.class_897;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin(value = {class_897.class}, priority = 10000)
/*    */ public abstract class EntityRendererMixin {
/*    */   @Inject(at = {@At("HEAD")}, method = {"renderNameTag"})
/*    */   private void renderNameTag(class_1297 entity, class_2561 component, class_4587 poseStack, class_4597 multiBufferSource, int light, CallbackInfo info) {
/* 20 */     if (info.isCancelled()) {
/*    */       return;
/*    */     }
/* 23 */     if (!method_3921(entity)) {
/*    */       return;
/*    */     }
/* 26 */     if (!entity.method_5476().equals(component)) {
/*    */       return;
/*    */     }
/* 29 */     ((ClientCompatibilityManager.RenderNameplateEvent)RenderEvents.RENDER_NAMEPLATE.invoker()).render(entity, component, poseStack, multiBufferSource, light);
/*    */   }
/*    */   
/*    */   @Shadow
/*    */   protected abstract boolean method_3921(class_1297 paramclass_1297);
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\mixin\EntityRendererMixin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */