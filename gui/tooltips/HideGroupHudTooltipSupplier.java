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
/*    */ public class HideGroupHudTooltipSupplier
/*    */   implements ImageButton.TooltipSupplier
/*    */ {
/* 15 */   public static final class_2588 SHOW_GROUP_HUD_ENABLED = new class_2588("message.voicechat.show_group_hud.enabled");
/* 16 */   public static final class_2588 SHOW_GROUP_HUD_DISABLED = new class_2588("message.voicechat.show_group_hud.disabled");
/*    */   
/*    */   private final class_437 screen;
/*    */ 
/*    */   
/*    */   public HideGroupHudTooltipSupplier(class_437 screen) {
/* 22 */     this.screen = screen;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTooltip(ImageButton button, class_4587 matrices, int mouseX, int mouseY) {
/* 27 */     List<class_5481> tooltip = new ArrayList<>();
/*    */     
/* 29 */     if (((Boolean)VoicechatClient.CLIENT_CONFIG.showGroupHUD.get()).booleanValue()) {
/* 30 */       tooltip.add(SHOW_GROUP_HUD_ENABLED.method_30937());
/*    */     } else {
/* 32 */       tooltip.add(SHOW_GROUP_HUD_DISABLED.method_30937());
/*    */     } 
/*    */     
/* 35 */     this.screen.method_25417(matrices, tooltip, mouseX, mouseY);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\tooltips\HideGroupHudTooltipSupplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */