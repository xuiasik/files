/*    */ package de.maxhenkel.voicechat.plugins.impl.mp3;
/*    */ 
/*    */ import de.maxhenkel.lame4j.Mp3Encoder;
/*    */ import de.maxhenkel.lame4j.UnknownPlatformException;
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import de.maxhenkel.voicechat.api.mp3.Mp3Encoder;
/*    */ import de.maxhenkel.voicechat.intercompatibility.CrossSideManager;
/*    */ import de.maxhenkel.voicechat.voice.common.Utils;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import javax.annotation.Nullable;
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ 
/*    */ public class Mp3EncoderImpl
/*    */   implements Mp3Encoder, AutoCloseable {
/*    */   private final Mp3Encoder encoder;
/*    */   
/*    */   public Mp3EncoderImpl(AudioFormat audioFormat, int bitrate, int quality, OutputStream outputStream) throws IOException, UnknownPlatformException {
/* 19 */     this.encoder = new Mp3Encoder(audioFormat.getChannels(), (int)audioFormat.getSampleRate(), bitrate, quality, outputStream);
/*    */   }
/*    */ 
/*    */   
/*    */   public void encode(short[] samples) throws IOException {
/* 24 */     this.encoder.write(samples);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 29 */     this.encoder.close();
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public static Mp3Encoder createEncoder(AudioFormat audioFormat, int bitrate, int quality, OutputStream outputStream) {
/* 34 */     if (!CrossSideManager.get().useNatives()) {
/* 35 */       return null;
/*    */     }
/* 37 */     return (Mp3Encoder)Utils.createSafe(() -> new Mp3EncoderImpl(audioFormat, bitrate, quality, outputStream), e -> Voicechat.LOGGER.error("Failed to load mp3 encoder", new Object[] { e }));
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\mp3\Mp3EncoderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */