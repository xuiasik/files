/*    */ package de.maxhenkel.voicechat.gui.audiodevice;
/*    */ 
/*    */ import de.maxhenkel.voicechat.VoicechatClient;
/*    */ import de.maxhenkel.voicechat.voice.client.SoundManager;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ public class SpeakerAudioDeviceList
/*    */   extends AudioDeviceList
/*    */ {
/* 12 */   public static final class_2960 SPEAKER_ICON = new class_2960("voicechat", "textures/icons/speaker.png");
/* 13 */   public static final class_2561 DEFAULT_SPEAKER = (class_2561)new class_2588("message.voicechat.default_speaker");
/*    */   
/*    */   public SpeakerAudioDeviceList(int width, int height, int top) {
/* 16 */     super(width, height, top);
/* 17 */     this.defaultDeviceText = DEFAULT_SPEAKER;
/* 18 */     this.icon = SPEAKER_ICON;
/* 19 */     this.configEntry = VoicechatClient.CLIENT_CONFIG.speaker;
/* 20 */     setAudioDevices(SoundManager.getAllSpeakers());
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\audiodevice\SpeakerAudioDeviceList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */