package de.maxhenkel.voicechat.api.packets;

import de.maxhenkel.voicechat.api.Position;
import java.util.UUID;

public interface ConvertablePacket {
  EntitySoundPacket.Builder<?> entitySoundPacketBuilder();
  
  LocationalSoundPacket.Builder<?> locationalSoundPacketBuilder();
  
  StaticSoundPacket.Builder<?> staticSoundPacketBuilder();
  
  @Deprecated
  EntitySoundPacket toEntitySoundPacket(UUID paramUUID, boolean paramBoolean);
  
  @Deprecated
  LocationalSoundPacket toLocationalSoundPacket(Position paramPosition);
  
  @Deprecated
  StaticSoundPacket toStaticSoundPacket();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\packets\ConvertablePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */