/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ 
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2585;
/*    */ import net.minecraft.class_2588;
/*    */ 
/*    */ public class VoiceSoundSlider
/*    */   extends DebouncedSlider {
/*    */   public VoiceSoundSlider(int x, int y, int width, int height) {
/* 11 */     super(x, y, width, height, (class_2561)new class_2585(""), (((Double)VoicechatClient.CLIENT_CONFIG.voiceChatVolume.get()).floatValue() / 2.0F));
/* 12 */     method_25346();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25346() {
/* 17 */     method_25355(getMsg());
/*    */   }
/*    */   
/*    */   public class_2561 getMsg() {
/* 21 */     return (class_2561)new class_2588("message.voicechat.voice_chat_volume", new Object[] { Math.round(this.field_22753 * 200.0D) + "%" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyDebounced() {
/* 26 */     VoicechatClient.CLIENT_CONFIG.voiceChatVolume.set(Double.valueOf(this.field_22753 * 2.0D)).save();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\VoiceSoundSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */