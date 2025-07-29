/*    */ package de.maxhenkel.voicechat.gui.onboarding;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.audiodevice.AudioDeviceList;
/*    */ import de.maxhenkel.voicechat.gui.audiodevice.MicrophoneAudioDeviceList;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_437;
/*    */ 
/*    */ public class MicOnboardingScreen
/*    */   extends DeviceOnboardingScreen
/*    */ {
/* 14 */   private static final class_2561 TITLE = (class_2561)(new class_2588("message.voicechat.onboarding.microphone")).method_27692(class_124.field_1067);
/*    */   
/*    */   public MicOnboardingScreen(@Nullable class_437 previous) {
/* 17 */     super(TITLE, previous);
/*    */   }
/*    */ 
/*    */   
/*    */   public AudioDeviceList createAudioDeviceList(int width, int height, int top) {
/* 22 */     return (AudioDeviceList)new MicrophoneAudioDeviceList(width, height, top);
/*    */   }
/*    */ 
/*    */   
/*    */   public class_437 getNextScreen() {
/* 27 */     return new SpeakerOnboardingScreen(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\onboarding\MicOnboardingScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */