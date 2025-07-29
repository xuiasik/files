/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ 
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2585;
/*    */ import net.minecraft.class_2588;
/*    */ 
/*    */ public class MicAmplificationSlider extends DebouncedSlider {
/*    */   private static final float MAXIMUM = 4.0F;
/*    */   
/*    */   public MicAmplificationSlider(int xIn, int yIn, int widthIn, int heightIn) {
/* 12 */     super(xIn, yIn, widthIn, heightIn, (class_2561)new class_2585(""), (((Double)VoicechatClient.CLIENT_CONFIG.microphoneAmplification.get()).floatValue() / 4.0F));
/* 13 */     method_25346();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25346() {
/* 18 */     long amp = Math.round(this.field_22753 * 4.0D * 100.0D - 100.0D);
/* 19 */     method_25355((class_2561)new class_2588("message.voicechat.microphone_amplification", new Object[] { (((float)amp > 0.0F) ? "+" : "") + amp + "%" }));
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyDebounced() {
/* 24 */     VoicechatClient.CLIENT_CONFIG.microphoneAmplification.set(Double.valueOf(this.field_22753 * 4.0D)).save();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\MicAmplificationSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */