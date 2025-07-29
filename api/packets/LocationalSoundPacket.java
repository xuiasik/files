package de.maxhenkel.voicechat.api.packets;

import de.maxhenkel.voicechat.api.Position;

public interface LocationalSoundPacket extends SoundPacket {
  Position getPosition();
  
  float getDistance();
  
  public static interface Builder<T extends Builder<T>> extends SoundPacket.Builder<T, LocationalSoundPacket> {
    T position(Position param1Position);
    
    T distance(float param1Float);
  }
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\packets\LocationalSoundPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */