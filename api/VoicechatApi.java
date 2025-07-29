package de.maxhenkel.voicechat.api;

import de.maxhenkel.voicechat.api.audio.AudioConverter;
import de.maxhenkel.voicechat.api.mp3.Mp3Decoder;
import de.maxhenkel.voicechat.api.mp3.Mp3Encoder;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;
import de.maxhenkel.voicechat.api.opus.OpusEncoderMode;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nullable;
import javax.sound.sampled.AudioFormat;

public interface VoicechatApi {
  OpusEncoder createEncoder();
  
  OpusEncoder createEncoder(OpusEncoderMode paramOpusEncoderMode);
  
  OpusDecoder createDecoder();
  
  @Nullable
  Mp3Encoder createMp3Encoder(AudioFormat paramAudioFormat, int paramInt1, int paramInt2, OutputStream paramOutputStream);
  
  @Nullable
  Mp3Decoder createMp3Decoder(InputStream paramInputStream);
  
  AudioConverter getAudioConverter();
  
  Entity fromEntity(Object paramObject);
  
  ServerLevel fromServerLevel(Object paramObject);
  
  ServerPlayer fromServerPlayer(Object paramObject);
  
  Position createPosition(double paramDouble1, double paramDouble2, double paramDouble3);
  
  VolumeCategory.Builder volumeCategoryBuilder();
  
  double getVoiceChatDistance();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\VoicechatApi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */