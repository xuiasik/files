package de.maxhenkel.voicechat.api.audiochannel;

import de.maxhenkel.voicechat.api.Position;

public interface ClientLocationalAudioChannel extends ClientAudioChannel {
  Position getLocation();
  
  void setLocation(Position paramPosition);
  
  float getDistance();
  
  void setDistance(float paramFloat);
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\audiochannel\ClientLocationalAudioChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */