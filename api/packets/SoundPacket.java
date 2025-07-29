package de.maxhenkel.voicechat.api.packets;

import java.util.UUID;
import javax.annotation.Nullable;

public interface SoundPacket extends Packet, ConvertablePacket {
  UUID getChannelId();
  
  UUID getSender();
  
  byte[] getOpusEncodedData();
  
  long getSequenceNumber();
  
  @Nullable
  String getCategory();
  
  public static interface Builder<T extends Builder<T, P>, P extends SoundPacket> {
    T channelId(UUID param1UUID);
    
    T opusEncodedData(byte[] param1ArrayOfbyte);
    
    T category(@Nullable String param1String);
    
    P build();
  }
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\packets\SoundPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */