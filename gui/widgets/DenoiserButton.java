/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ 
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.voice.client.Denoiser;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ 
/*    */ public class DenoiserButton
/*    */   extends BooleanConfigButton {
/* 10 */   private static final class_2561 ENABLED = (class_2561)new class_2588("message.voicechat.denoiser.on");
/* 11 */   private static final class_2561 DISABLED = (class_2561)new class_2588("message.voicechat.denoiser.off");
/*    */   
/*    */   public DenoiserButton(int x, int y, int width, int height) {
/* 14 */     super(x, y, width, height, VoicechatClient.CLIENT_CONFIG.denoiser, enabled -> new class_2588("message.voicechat.denoiser", new Object[] { enabled.booleanValue() ? ENABLED : DISABLED }));
/*    */ 
/*    */     
/* 17 */     if (Denoiser.createDenoiser() == null)
/* 18 */       this.field_22763 = false; 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\DenoiserButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */