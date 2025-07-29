/*    */ package de.maxhenkel.voicechat.gui.audiodevice;
/*    */ 
/*    */ import de.maxhenkel.voicechat.voice.client.microphone.MicrophoneManager;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_437;
/*    */ 
/*    */ public class SelectMicrophoneScreen
/*    */   extends SelectDeviceScreen
/*    */ {
/* 14 */   public static final class_2561 TITLE = (class_2561)new class_2588("gui.voicechat.select_microphone.title");
/* 15 */   public static final class_2561 NO_MICROPHONE = (class_2561)(new class_2588("message.voicechat.no_microphone")).method_27692(class_124.field_1080);
/*    */   
/*    */   public SelectMicrophoneScreen(@Nullable class_437 parent) {
/* 18 */     super(TITLE, parent);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> getDevices() {
/* 23 */     return MicrophoneManager.deviceNames();
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2561 getEmptyListComponent() {
/* 28 */     return NO_MICROPHONE;
/*    */   }
/*    */ 
/*    */   
/*    */   public AudioDeviceList createAudioDeviceList(int width, int height, int top) {
/* 33 */     return new MicrophoneAudioDeviceList(width, height, top);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\audiodevice\SelectMicrophoneScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */