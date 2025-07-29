package de.maxhenkel.voicechat.api.packets;

import java.util.UUID;

public interface EntitySoundPacket extends SoundPacket {
  UUID getEntityUuid();
  
  boolean isWhispering();
  
  float getDistance();
  
  public static interface Builder<T extends Builder<T>> extends SoundPacket.Builder<T, EntitySoundPacket> {
    T entityUuid(UUID param1UUID);
    
    T whispering(boolean param1Boolean);
    
    T distance(float param1Float);
  }
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\packets\EntitySoundPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */