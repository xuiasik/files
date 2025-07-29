/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ 
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.voice.client.MicrophoneActivationType;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ 
/*    */ public class MicActivationButton
/*    */   extends EnumButton<MicrophoneActivationType>
/*    */ {
/*    */   protected Consumer<MicrophoneActivationType> onChange;
/*    */   
/*    */   public MicActivationButton(int xIn, int yIn, int widthIn, int heightIn, Consumer<MicrophoneActivationType> onChange) {
/* 15 */     super(xIn, yIn, widthIn, heightIn, VoicechatClient.CLIENT_CONFIG.microphoneActivationType);
/* 16 */     this.onChange = onChange;
/* 17 */     updateText();
/* 18 */     onChange.accept(this.entry.get());
/*    */   }
/*    */ 
/*    */   
/*    */   protected class_2561 getText(MicrophoneActivationType type) {
/* 23 */     return (class_2561)new class_2588("message.voicechat.activation_type", new Object[] { type.getText() });
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onUpdate(MicrophoneActivationType type) {
/* 28 */     this.onChange.accept(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\MicActivationButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */