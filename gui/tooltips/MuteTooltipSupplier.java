/*    */ package de.maxhenkel.voicechat.gui.tooltips;
/*    */ 
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.gui.widgets.ImageButton;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
/*    */ import de.maxhenkel.voicechat.voice.client.MicrophoneActivationType;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_5481;
/*    */ 
/*    */ public class MuteTooltipSupplier
/*    */   implements ImageButton.TooltipSupplier
/*    */ {
/* 17 */   public static final class_2588 MUTE_UNMUTED = new class_2588("message.voicechat.mute.disabled");
/* 18 */   public static final class_2588 MUTE_MUTED = new class_2588("message.voicechat.mute.enabled");
/* 19 */   public static final class_2588 MUTE_DISABLED_PTT = new class_2588("message.voicechat.mute.disabled_ptt");
/*    */   
/*    */   private class_437 screen;
/*    */   private ClientPlayerStateManager stateManager;
/*    */   
/*    */   public MuteTooltipSupplier(class_437 screen, ClientPlayerStateManager stateManager) {
/* 25 */     this.screen = screen;
/* 26 */     this.stateManager = stateManager;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTooltip(ImageButton button, class_4587 matrices, int mouseX, int mouseY) {
/* 31 */     List<class_5481> tooltip = new ArrayList<>();
/*    */     
/* 33 */     if (!canMuteMic()) {
/* 34 */       tooltip.add(MUTE_DISABLED_PTT.method_30937());
/* 35 */     } else if (this.stateManager.isMuted()) {
/* 36 */       tooltip.add(MUTE_MUTED.method_30937());
/*    */     } else {
/* 38 */       tooltip.add(MUTE_UNMUTED.method_30937());
/*    */     } 
/*    */     
/* 41 */     this.screen.method_25417(matrices, tooltip, mouseX, mouseY);
/*    */   }
/*    */   
/*    */   public static boolean canMuteMic() {
/* 45 */     return ((MicrophoneActivationType)VoicechatClient.CLIENT_CONFIG.microphoneActivationType.get()).equals(MicrophoneActivationType.VOICE);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\tooltips\MuteTooltipSupplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */