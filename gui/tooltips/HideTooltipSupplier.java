/*    */ package de.maxhenkel.voicechat.gui.tooltips;
/*    */ 
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.gui.widgets.ImageButton;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_5481;
/*    */ 
/*    */ public class HideTooltipSupplier
/*    */   implements ImageButton.TooltipSupplier
/*    */ {
/* 15 */   public static final class_2588 HIDE_ICONS_ENABLED = new class_2588("message.voicechat.hide_icons.enabled");
/* 16 */   public static final class_2588 HIDE_ICONS_DISABLED = new class_2588("message.voicechat.hide_icons.disabled");
/*    */   
/*    */   private final class_437 screen;
/*    */   
/*    */   public HideTooltipSupplier(class_437 screen) {
/* 21 */     this.screen = screen;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTooltip(ImageButton button, class_4587 matrices, int mouseX, int mouseY) {
/* 26 */     List<class_5481> tooltip = new ArrayList<>();
/*    */     
/* 28 */     if (((Boolean)VoicechatClient.CLIENT_CONFIG.hideIcons.get()).booleanValue()) {
/* 29 */       tooltip.add(HIDE_ICONS_ENABLED.method_30937());
/*    */     } else {
/* 31 */       tooltip.add(HIDE_ICONS_DISABLED.method_30937());
/*    */     } 
/*    */     
/* 34 */     this.screen.method_25417(matrices, tooltip, mouseX, mouseY);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\tooltips\HideTooltipSupplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */