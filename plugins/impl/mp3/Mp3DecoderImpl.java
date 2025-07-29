/*    */ package de.maxhenkel.voicechat.plugins.impl.mp3;
/*    */ 
/*    */ import de.maxhenkel.lame4j.Mp3Decoder;
/*    */ import de.maxhenkel.lame4j.ShortArrayBuffer;
/*    */ import de.maxhenkel.lame4j.UnknownPlatformException;
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import de.maxhenkel.voicechat.api.mp3.Mp3Decoder;
/*    */ import de.maxhenkel.voicechat.intercompatibility.CrossSideManager;
/*    */ import de.maxhenkel.voicechat.voice.common.Utils;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import javax.annotation.Nullable;
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ 
/*    */ public class Mp3DecoderImpl
/*    */   implements Mp3Decoder {
/*    */   private final Mp3Decoder decoder;
/*    */   private IOException decodeError;
/*    */   @Nullable
/*    */   private short[] samples;
/*    */   @Nullable
/*    */   private AudioFormat audioFormat;
/*    */   
/*    */   public Mp3DecoderImpl(InputStream inputStream) throws IOException, UnknownPlatformException {
/* 25 */     this.decoder = new Mp3Decoder(inputStream);
/*    */   }
/*    */   
/*    */   private void decodeIfNecessary() throws IOException {
/* 29 */     if (this.decodeError != null) {
/* 30 */       throw this.decodeError;
/*    */     }
/*    */     try {
/* 33 */       if (this.samples == null) {
/* 34 */         ShortArrayBuffer sampleBuffer = new ShortArrayBuffer(2048);
/*    */         while (true) {
/* 36 */           short[] samples = this.decoder.decodeNextFrame();
/* 37 */           if (samples == null) {
/*    */             break;
/*    */           }
/* 40 */           sampleBuffer.writeShorts(samples);
/*    */         } 
/* 42 */         this.samples = sampleBuffer.toShortArray();
/* 43 */         this.audioFormat = this.decoder.createAudioFormat();
/*    */       } 
/* 45 */     } catch (IOException e) {
/* 46 */       this.decodeError = e;
/* 47 */       throw e;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public short[] decode() throws IOException {
/* 53 */     decodeIfNecessary();
/* 54 */     return this.samples;
/*    */   }
/*    */ 
/*    */   
/*    */   public AudioFormat getAudioFormat() throws IOException {
/* 59 */     decodeIfNecessary();
/* 60 */     return this.audioFormat;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBitrate() throws IOException {
/* 65 */     decodeIfNecessary();
/* 66 */     return this.decoder.getBitRate();
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public static Mp3Decoder createDecoder(InputStream inputStream) {
/* 71 */     if (!CrossSideManager.get().useNatives()) {
/* 72 */       return null;
/*    */     }
/* 74 */     return (Mp3Decoder)Utils.createSafe(() -> new Mp3DecoderImpl(inputStream), e -> Voicechat.LOGGER.error("Failed to load mp3 decoder", new Object[] { e }));
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\mp3\Mp3DecoderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */