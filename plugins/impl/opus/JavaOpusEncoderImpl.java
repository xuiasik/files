/*    */ package de.maxhenkel.voicechat.plugins.impl.opus;
/*    */ 
/*    */ import de.maxhenkel.opus4j.OpusEncoder;
/*    */ import de.maxhenkel.voicechat.api.opus.OpusEncoder;
/*    */ import de.maxhenkel.voicechat.concentus.OpusApplication;
/*    */ import de.maxhenkel.voicechat.concentus.OpusEncoder;
/*    */ 
/*    */ public class JavaOpusEncoderImpl implements OpusEncoder {
/*    */   protected OpusEncoder opusEncoder;
/*    */   protected byte[] buffer;
/*    */   protected int sampleRate;
/*    */   protected int frameSize;
/*    */   protected OpusEncoder.Application application;
/*    */   
/*    */   public JavaOpusEncoderImpl(int sampleRate, int frameSize, int maxPayloadSize, OpusEncoder.Application application) {
/* 16 */     this.sampleRate = sampleRate;
/* 17 */     this.frameSize = frameSize;
/* 18 */     this.application = application;
/* 19 */     this.buffer = new byte[maxPayloadSize];
/* 20 */     open();
/*    */   }
/*    */   
/*    */   private void open() {
/* 24 */     if (this.opusEncoder != null) {
/*    */       return;
/*    */     }
/*    */     try {
/* 28 */       this.opusEncoder = new OpusEncoder(this.sampleRate, 1, getApplication(this.application));
/* 29 */     } catch (Exception e) {
/* 30 */       throw new IllegalStateException("Failed to create Opus encoder", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public byte[] encode(short[] rawAudio) {
/*    */     int result;
/* 36 */     if (isClosed()) {
/* 37 */       throw new IllegalStateException("Encoder is closed");
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 42 */       result = this.opusEncoder.encode(rawAudio, 0, this.frameSize, this.buffer, 0, this.buffer.length);
/* 43 */     } catch (Exception e) {
/* 44 */       throw new RuntimeException("Failed to encode audio", e);
/*    */     } 
/*    */     
/* 47 */     if (result < 0) {
/* 48 */       throw new RuntimeException("Failed to encode audio data");
/*    */     }
/*    */     
/* 51 */     byte[] audio = new byte[result];
/* 52 */     System.arraycopy(this.buffer, 0, audio, 0, result);
/* 53 */     return audio;
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetState() {
/* 58 */     if (isClosed()) {
/* 59 */       throw new IllegalStateException("Encoder is closed");
/*    */     }
/* 61 */     this.opusEncoder.resetState();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 66 */     return (this.opusEncoder == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 71 */     if (isClosed()) {
/*    */       return;
/*    */     }
/* 74 */     this.opusEncoder = null;
/*    */   }
/*    */   
/*    */   public static OpusApplication getApplication(OpusEncoder.Application application) {
/* 78 */     switch (application)
/*    */     
/*    */     { default:
/* 81 */         return OpusApplication.OPUS_APPLICATION_VOIP;
/*    */       case AUDIO:
/* 83 */         return OpusApplication.OPUS_APPLICATION_AUDIO;
/*    */       case LOW_DELAY:
/* 85 */         break; }  return OpusApplication.OPUS_APPLICATION_RESTRICTED_LOWDELAY;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\opus\JavaOpusEncoderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */