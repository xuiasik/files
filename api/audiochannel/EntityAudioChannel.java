package de.maxhenkel.voicechat.api.audiochannel;

import de.maxhenkel.voicechat.api.Entity;

public interface EntityAudioChannel extends AudioChannel {
  boolean isWhispering();
  
  void setWhispering(boolean paramBoolean);
  
  void updateEntity(Entity paramEntity);
  
  Entity getEntity();
  
  float getDistance();
  
  void setDistance(float paramFloat);
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\audiochannel\EntityAudioChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */