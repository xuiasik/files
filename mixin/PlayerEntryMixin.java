/*    */ package de.maxhenkel.voicechat.mixin;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import de.maxhenkel.voicechat.gui.widgets.ImageButton;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*    */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Supplier;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_4185;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_5519;
/*    */ import net.minecraft.class_5522;
/*    */ import org.spongepowered.asm.mixin.Final;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ 
/*    */ @Mixin({class_5519.class})
/*    */ public class PlayerEntryMixin
/*    */ {
/* 30 */   private static final class_2960 GROUP_ICON = new class_2960("voicechat", "textures/icons/invite_button.png");
/*    */   
/*    */   @Shadow
/*    */   @Nullable
/*    */   private class_4185 field_26860;
/*    */   
/*    */   @Shadow
/*    */   @Final
/*    */   private String field_26857;
/*    */   @Shadow
/*    */   float field_26864;
/*    */   @Shadow
/*    */   @Final
/*    */   private class_310 field_26854;
/*    */   @Shadow
/*    */   @Final
/*    */   private UUID field_26856;
/*    */   private class_5522 screen;
/*    */   private ImageButton inviteButton;
/*    */   private boolean invited;
/*    */   
/*    */   @Inject(method = {"<init>"}, at = {@At("RETURN")})
/*    */   private void init(class_310 minecraft, class_5522 socialInteractionsScreen, UUID uUID, String string, Supplier<class_2960> supplier, CallbackInfo ci) {
/* 53 */     this.screen = socialInteractionsScreen;
/*    */   }
/*    */   
/*    */   @Redirect(method = {"<init>"}, at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;of(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;"))
/*    */   private ImmutableList<?> children(Object o1, Object o2) {
/* 58 */     this.inviteButton = new ImageButton(0, 0, GROUP_ICON, button -> {
/*    */           this.field_26854.field_1724.method_3142(String.format("/voicechat invite %s", new Object[] { this.field_26857 }));
/*    */           
/*    */           this.invited = true;
/*    */         }(button, matrices, mouseX, mouseY) -> {
/*    */           if (this.screen == null || this.invited) {
/*    */             return;
/*    */           }
/*    */           
/*    */           this.field_26864 += this.field_26854.method_1534();
/*    */           
/*    */           if (this.field_26864 < 10.0F) {
/*    */             return;
/*    */           }
/*    */           this.screen.method_31354(());
/*    */         });
/* 74 */     return ImmutableList.of(o1, o2, this.inviteButton);
/*    */   }
/*    */   
/*    */   @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/button/Button;render(Lcom/mojang/blaze3d/matrix/MatrixStack;IIF)V", ordinal = 1)})
/*    */   private void render(class_4587 poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta, CallbackInfo ci) {
/* 79 */     if (this.inviteButton != null && this.field_26860 != null) {
/* 80 */       if (ClientManager.getPlayerStateManager().getGroupID() == null || !canInvite()) {
/* 81 */         this.inviteButton.field_22764 = false;
/*    */         return;
/*    */       } 
/* 84 */       this.inviteButton.field_22764 = true;
/* 85 */       this.inviteButton.field_22763 = !this.invited;
/* 86 */       this.inviteButton.field_22760 = left + width - this.field_26860.method_25368() - 4 - this.inviteButton.method_25368() - 4;
/* 87 */       this.inviteButton.field_22761 = top + (height - this.inviteButton.method_25364()) / 2;
/* 88 */       this.inviteButton.method_25394(poseStack, mouseX, mouseY, delta);
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean canInvite() {
/* 93 */     PlayerState state = ClientManager.getPlayerStateManager().getState(this.field_26856);
/* 94 */     if (state == null) {
/* 95 */       return false;
/*    */     }
/* 97 */     return !state.hasGroup();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\mixin\PlayerEntryMixin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */