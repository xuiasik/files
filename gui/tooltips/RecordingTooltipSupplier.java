/*    */ package de.maxhenkel.voicechat.gui.tooltips;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.widgets.ImageButton;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_5481;
/*    */ 
/*    */ public class RecordingTooltipSupplier
/*    */   implements ImageButton.TooltipSupplier
/*    */ {
/* 16 */   public static final class_2588 RECORDING_ENABLED = new class_2588("message.voicechat.recording.enabled");
/* 17 */   public static final class_2588 RECORDING_DISABLED = new class_2588("message.voicechat.recording.disabled");
/*    */   
/*    */   private final class_437 screen;
/*    */   
/*    */   public RecordingTooltipSupplier(class_437 screen) {
/* 22 */     this.screen = screen;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTooltip(ImageButton button, class_4587 matrices, int mouseX, int mouseY) {
/* 27 */     ClientVoicechat client = ClientManager.getClient();
/* 28 */     if (client == null) {
/*    */       return;
/*    */     }
/*    */     
/* 32 */     List<class_5481> tooltip = new ArrayList<>();
/*    */     
/* 34 */     if (client.getRecorder() == null) {
/* 35 */       tooltip.add(RECORDING_DISABLED.method_30937());
/*    */     } else {
/* 37 */       tooltip.add(RECORDING_ENABLED.method_30937());
/*    */     } 
/*    */     
/* 40 */     this.screen.method_25417(matrices, tooltip, mouseX, mouseY);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\tooltips\RecordingTooltipSupplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */