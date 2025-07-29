package de.maxhenkel.voicechat.api.audiochannel;

import java.util.UUID;

public interface ClientEntityAudioChannel extends ClientAudioChannel {
  UUID getEntityId();
  
  void setWhispering(boolean paramBoolean);
  
  boolean isWhispering();
  
  float getDistance();
  
  void setDistance(float paramFloat);
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\audiochannel\ClientEntityAudioChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */