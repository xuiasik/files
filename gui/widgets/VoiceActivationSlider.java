/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.voice.common.Utils;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2585;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_4587;
/*    */ 
/*    */ public class VoiceActivationSlider
/*    */   extends DebouncedSlider
/*    */   implements MicTestButton.MicListener
/*    */ {
/* 19 */   private static final class_2960 SLIDER = new class_2960("voicechat", "textures/gui/voice_activation_slider.png");
/* 20 */   private static final class_2561 NO_ACTIVATION = (class_2561)(new class_2588("message.voicechat.voice_activation.disabled")).method_27692(class_124.field_1061);
/*    */   
/*    */   private double micValue;
/*    */   
/*    */   public VoiceActivationSlider(int x, int y, int width, int height) {
/* 25 */     super(x, y, width, height, (class_2561)new class_2585(""), Utils.dbToPerc(((Double)VoicechatClient.CLIENT_CONFIG.voiceActivationThreshold.get()).floatValue()));
/* 26 */     method_25346();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25353(class_4587 poseStack, class_310 minecraft, int i, int j) {
/* 31 */     minecraft.method_1531().method_22813(SLIDER);
/* 32 */     RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 33 */     int width = (int)((method_25368() - 2) * this.micValue);
/* 34 */     method_25302(poseStack, this.field_22760 + 1, this.field_22761 + 1, 0, 0, width, 18);
/* 35 */     super.method_25353(poseStack, minecraft, i, j);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25346() {
/* 40 */     long db = Math.round(Utils.percToDb(this.field_22753));
/* 41 */     class_2588 component = new class_2588("message.voicechat.voice_activation", new Object[] { Long.valueOf(db) });
/*    */     
/* 43 */     if (db >= -10L) {
/* 44 */       component.method_27692(class_124.field_1061);
/*    */     }
/*    */     
/* 47 */     method_25355((class_2561)component);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public class_2561 getHoverText() {
/* 52 */     if (this.field_22753 >= 1.0D) {
/* 53 */       return NO_ACTIVATION;
/*    */     }
/* 55 */     return null;
/*    */   }
/*    */   
/*    */   public boolean method_25367() {
/* 59 */     return this.field_22762;
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyDebounced() {
/* 64 */     VoicechatClient.CLIENT_CONFIG.voiceActivationThreshold.set(Double.valueOf(Utils.percToDb(this.field_22753))).save();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMicValue(double percentage) {
/* 69 */     this.micValue = percentage;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\VoiceActivationSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */