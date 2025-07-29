/*    */ package de.maxhenkel.voicechat.plugins.impl.opus;
/*    */ 
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import de.maxhenkel.voicechat.api.opus.OpusDecoder;
/*    */ import de.maxhenkel.voicechat.concentus.OpusDecoder;
/*    */ import de.maxhenkel.voicechat.concentus.OpusException;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class JavaOpusDecoderImpl
/*    */   implements OpusDecoder {
/*    */   protected OpusDecoder opusDecoder;
/*    */   protected short[] buffer;
/*    */   protected int sampleRate;
/*    */   
/*    */   public JavaOpusDecoderImpl(int sampleRate, int frameSize) {
/* 16 */     this.sampleRate = sampleRate;
/* 17 */     this.buffer = new short[frameSize];
/* 18 */     open();
/*    */   }
/*    */   
/*    */   private void open() {
/* 22 */     if (this.opusDecoder != null) {
/*    */       return;
/*    */     }
/*    */     try {
/* 26 */       this.opusDecoder = new OpusDecoder(this.sampleRate, 1);
/* 27 */     } catch (OpusException e) {
/* 28 */       throw new IllegalStateException("Failed to create Opus decoder", e);
/*    */     } 
/* 30 */     Voicechat.LOGGER.debug("Initializing Java Opus decoder with sample rate {} Hz, frame size {} bytes", new Object[] { Integer.valueOf(this.sampleRate), Integer.valueOf(this.buffer.length) });
/*    */   }
/*    */   
/*    */   public short[] decode(@Nullable byte[] data) {
/*    */     int result;
/* 35 */     if (isClosed()) {
/* 36 */       throw new IllegalStateException("Decoder is closed");
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 41 */       if (data == null || data.length == 0) {
/* 42 */         result = this.opusDecoder.decode(null, 0, 0, this.buffer, 0, this.buffer.length, true);
/*    */       } else {
/* 44 */         result = this.opusDecoder.decode(data, 0, data.length, this.buffer, 0, this.buffer.length, false);
/*    */       } 
/* 46 */     } catch (Exception e) {
/* 47 */       throw new RuntimeException("Failed to decode audio", e);
/*    */     } 
/*    */     
/* 50 */     short[] audio = new short[result];
/* 51 */     System.arraycopy(this.buffer, 0, audio, 0, result);
/* 52 */     return audio;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 57 */     return (this.opusDecoder == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 62 */     if (isClosed()) {
/*    */       return;
/*    */     }
/* 65 */     this.opusDecoder = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetState() {
/* 70 */     if (isClosed()) {
/* 71 */       throw new IllegalStateException("Decoder is closed");
/*    */     }
/* 73 */     this.opusDecoder.resetState();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\opus\JavaOpusDecoderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */