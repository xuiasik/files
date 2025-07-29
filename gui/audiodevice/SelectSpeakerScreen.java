/*    */ package de.maxhenkel.voicechat.gui.audiodevice;
/*    */ 
/*    */ import de.maxhenkel.voicechat.voice.client.SoundManager;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_437;
/*    */ 
/*    */ public class SelectSpeakerScreen
/*    */   extends SelectDeviceScreen
/*    */ {
/* 14 */   public static final class_2561 TITLE = (class_2561)new class_2588("gui.voicechat.select_speaker.title");
/* 15 */   public static final class_2561 NO_SPEAKER = (class_2561)(new class_2588("message.voicechat.no_speaker")).method_27692(class_124.field_1080);
/*    */   
/*    */   public SelectSpeakerScreen(@Nullable class_437 parent) {
/* 18 */     super(TITLE, parent);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> getDevices() {
/* 23 */     return SoundManager.getAllSpeakers();
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2561 getEmptyListComponent() {
/* 28 */     return NO_SPEAKER;
/*    */   }
/*    */ 
/*    */   
/*    */   public AudioDeviceList createAudioDeviceList(int width, int height, int top) {
/* 33 */     return new SpeakerAudioDeviceList(width, height, top);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\audiodevice\SelectSpeakerScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */