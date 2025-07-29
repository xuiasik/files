package de.maxhenkel.voicechat.api.audiochannel;

import java.util.UUID;
import javax.annotation.Nullable;

public interface ClientAudioChannel {
  UUID getId();
  
  void play(short[] paramArrayOfshort);
  
  @Nullable
  String getCategory();
  
  void setCategory(@Nullable String paramString);
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\audiochannel\ClientAudioChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */