/*    */ package de.maxhenkel.voicechat.mixin;
/*    */ 
/*    */ import de.maxhenkel.voicechat.events.PublishServerEvents;
/*    */ import java.util.function.Consumer;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_1132;
/*    */ import net.minecraft.class_1934;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({class_1132.class})
/*    */ public abstract class IntegratedServerMixin
/*    */ {
/*    */   @Inject(method = {"publishServer"}, at = {@At("RETURN")})
/*    */   public void publishServer(@Nullable class_1934 gameType, boolean cheats, int port, CallbackInfoReturnable<Boolean> callbackInfo) {
/* 18 */     if (!((Boolean)callbackInfo.getReturnValue()).booleanValue()) {
/*    */       return;
/*    */     }
/* 21 */     ((Consumer<Integer>)PublishServerEvents.SERVER_PUBLISHED.invoker()).accept(Integer.valueOf(port));
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\mixin\IntegratedServerMixin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */