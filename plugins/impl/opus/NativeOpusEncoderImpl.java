/*    */ package de.maxhenkel.voicechat.plugins.impl.opus;
/*    */ 
/*    */ import de.maxhenkel.opus4j.OpusEncoder;
/*    */ import de.maxhenkel.opus4j.UnknownPlatformException;
/*    */ import de.maxhenkel.voicechat.api.opus.OpusEncoder;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class NativeOpusEncoderImpl
/*    */   extends OpusEncoder implements OpusEncoder {
/*    */   public NativeOpusEncoderImpl(int sampleRate, int channels, OpusEncoder.Application application) throws IOException, UnknownPlatformException {
/* 11 */     super(sampleRate, channels, application);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\opus\NativeOpusEncoderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */