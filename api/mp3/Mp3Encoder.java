package de.maxhenkel.voicechat.api.mp3;

import java.io.IOException;

public interface Mp3Encoder {
  void encode(short[] paramArrayOfshort) throws IOException;
  
  void close() throws IOException;
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\mp3\Mp3Encoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */