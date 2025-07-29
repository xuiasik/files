/*    */ package de.maxhenkel.voicechat.plugins.impl.audiochannel;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.audiochannel.ClientStaticAudioChannel;
/*    */ import de.maxhenkel.voicechat.voice.common.GroupSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class ClientStaticAudioChannelImpl
/*    */   extends ClientAudioChannelImpl
/*    */   implements ClientStaticAudioChannel {
/*    */   public ClientStaticAudioChannelImpl(UUID id) {
/* 12 */     super(id);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundPacket<?> createSoundPacket(short[] rawAudio) {
/* 17 */     return (SoundPacket<?>)new GroupSoundPacket(this.id, this.id, rawAudio, this.category);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audiochannel\ClientStaticAudioChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */