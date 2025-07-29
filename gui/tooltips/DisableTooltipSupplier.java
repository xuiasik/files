/*    */ package de.maxhenkel.voicechat.gui.tooltips;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.widgets.ImageButton;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_5481;
/*    */ 
/*    */ public class DisableTooltipSupplier
/*    */   implements ImageButton.TooltipSupplier
/*    */ {
/* 15 */   public static final class_2588 DISABLE_ENABLED = new class_2588("message.voicechat.disable.enabled");
/* 16 */   public static final class_2588 DISABLE_DISABLED = new class_2588("message.voicechat.disable.disabled");
/* 17 */   public static final class_2588 DISABLE_NO_SPEAKER = new class_2588("message.voicechat.disable.no_speaker");
/*    */   
/*    */   private final class_437 screen;
/*    */   private final ClientPlayerStateManager stateManager;
/*    */   
/*    */   public DisableTooltipSupplier(class_437 screen, ClientPlayerStateManager stateManager) {
/* 23 */     this.screen = screen;
/* 24 */     this.stateManager = stateManager;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTooltip(ImageButton button, class_4587 matrices, int mouseX, int mouseY) {
/* 29 */     List<class_5481> tooltip = new ArrayList<>();
/*    */     
/* 31 */     if (!this.stateManager.canEnable()) {
/* 32 */       tooltip.add(DISABLE_NO_SPEAKER.method_30937());
/* 33 */     } else if (this.stateManager.isDisabled()) {
/* 34 */       tooltip.add(DISABLE_ENABLED.method_30937());
/*    */     } else {
/* 36 */       tooltip.add(DISABLE_DISABLED.method_30937());
/*    */     } 
/*    */     
/* 39 */     this.screen.method_25417(matrices, tooltip, mouseX, mouseY);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\tooltips\DisableTooltipSupplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */