/*    */ package de.maxhenkel.voicechat.plugins.impl.audiochannel;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*    */ import de.maxhenkel.voicechat.api.audiochannel.StaticAudioChannel;
/*    */ import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
/*    */ import de.maxhenkel.voicechat.plugins.impl.VoicechatConnectionImpl;
/*    */ import de.maxhenkel.voicechat.plugins.impl.VoicechatServerApiImpl;
/*    */ import de.maxhenkel.voicechat.voice.common.GroupSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.server.Server;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class StaticAudioChannelImpl extends AudioChannelImpl implements StaticAudioChannel {
/*    */   protected VoicechatConnectionImpl connection;
/*    */   
/*    */   public StaticAudioChannelImpl(UUID channelId, Server server, VoicechatConnectionImpl connection) {
/* 17 */     super(channelId, server);
/* 18 */     this.connection = connection;
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(byte[] opusData) {
/* 23 */     broadcast(new GroupSoundPacket(this.channelId, this.channelId, opusData, this.sequenceNumber.getAndIncrement(), this.category));
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(MicrophonePacket packet) {
/* 28 */     send(packet.getOpusEncodedData());
/*    */   }
/*    */ 
/*    */   
/*    */   public void flush() {
/* 33 */     GroupSoundPacket packet = new GroupSoundPacket(this.channelId, this.channelId, new byte[0], this.sequenceNumber.getAndIncrement(), this.category);
/* 34 */     broadcast(packet);
/*    */   }
/*    */   
/*    */   private void broadcast(GroupSoundPacket packet) {
/* 38 */     VoicechatServerApiImpl.sendPacket((VoicechatConnection)this.connection, (SoundPacket)packet);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audiochannel\StaticAudioChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */