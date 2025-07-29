/*    */ package de.maxhenkel.voicechat.plugins.impl.audiosender;
/*    */ 
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import de.maxhenkel.voicechat.api.audiosender.AudioSender;
/*    */ import de.maxhenkel.voicechat.voice.common.MicPacket;
/*    */ import de.maxhenkel.voicechat.voice.server.Server;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class AudioSenderImpl
/*    */   implements AudioSender
/*    */ {
/* 14 */   private static final Map<UUID, AudioSenderImpl> AUDIO_SENDERS = new HashMap<>();
/*    */   
/*    */   private final UUID uuid;
/*    */   private boolean whispering;
/*    */   private long nextSequenceNumber;
/*    */   
/*    */   public AudioSenderImpl(UUID uuid) {
/* 21 */     this.uuid = uuid;
/*    */   }
/*    */   
/*    */   public static boolean registerAudioSender(AudioSenderImpl audioSender) {
/* 25 */     if (Voicechat.SERVER.isCompatible(audioSender.uuid)) {
/* 26 */       return false;
/*    */     }
/* 28 */     if (AUDIO_SENDERS.containsKey(audioSender.uuid)) {
/* 29 */       return false;
/*    */     }
/* 31 */     AUDIO_SENDERS.put(audioSender.uuid, audioSender);
/* 32 */     return true;
/*    */   }
/*    */   
/*    */   public static boolean unregisterAudioSender(AudioSenderImpl audioSender) {
/* 36 */     return (AUDIO_SENDERS.remove(audioSender.uuid) != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public AudioSender whispering(boolean whispering) {
/* 41 */     this.whispering = whispering;
/* 42 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWhispering() {
/* 47 */     return this.whispering;
/*    */   }
/*    */ 
/*    */   
/*    */   public AudioSender sequenceNumber(long sequenceNumber) {
/* 52 */     if (sequenceNumber < 0L) {
/* 53 */       throw new IllegalArgumentException("Sequence number must be positive");
/*    */     }
/* 55 */     this.nextSequenceNumber = sequenceNumber;
/* 56 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSend() {
/* 61 */     return (!Voicechat.SERVER.isCompatible(this.uuid) && AUDIO_SENDERS.get(this.uuid) == this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean send(byte[] opusEncodedAudioData) {
/* 66 */     return sendMicrophonePacket(opusEncodedAudioData);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean reset() {
/* 71 */     return sendMicrophonePacket(new byte[0]);
/*    */   }
/*    */   
/*    */   public boolean sendMicrophonePacket(byte[] data) {
/* 75 */     if (data == null) {
/* 76 */       throw new IllegalStateException("opusEncodedData is not set");
/*    */     }
/* 78 */     if (!canSend()) {
/* 79 */       return false;
/*    */     }
/* 81 */     Server server = Voicechat.SERVER.getServer();
/* 82 */     if (server == null) {
/* 83 */       return true;
/*    */     }
/*    */     try {
/* 86 */       MicPacket packet = new MicPacket(data, (data.length > 0 && this.whispering), this.nextSequenceNumber++);
/* 87 */       if (data.length <= 0) {
/* 88 */         this.nextSequenceNumber = 0L;
/*    */       }
/* 90 */       server.onMicPacket(this.uuid, packet);
/* 91 */     } catch (Exception e) {
/* 92 */       Voicechat.LOGGER.error("Failed to send audio", new Object[] { e });
/*    */     } 
/* 94 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audiosender\AudioSenderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */