/*    */ package de.maxhenkel.voicechat.plugins.impl.opus;
/*    */ 
/*    */ import de.maxhenkel.opus4j.OpusDecoder;
/*    */ import de.maxhenkel.opus4j.UnknownPlatformException;
/*    */ import de.maxhenkel.voicechat.api.opus.OpusDecoder;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class NativeOpusDecoderImpl
/*    */   extends OpusDecoder implements OpusDecoder {
/*    */   public NativeOpusDecoderImpl(int sampleRate, int channels) throws IOException, UnknownPlatformException {
/* 11 */     super(sampleRate, channels);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\opus\NativeOpusDecoderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */