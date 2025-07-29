package de.maxhenkel.voicechat.api.mp3;

import java.io.IOException;
import javax.sound.sampled.AudioFormat;

public interface Mp3Decoder {
  short[] decode() throws IOException;
  
  AudioFormat getAudioFormat() throws IOException;
  
  int getBitrate() throws IOException;
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\mp3\Mp3Decoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */