package de.maxhenkel.voicechat.api.audiochannel;

import de.maxhenkel.voicechat.api.ServerPlayer;
import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public interface AudioChannel {
  void send(byte[] paramArrayOfbyte);
  
  void send(MicrophonePacket paramMicrophonePacket);
  
  void setFilter(Predicate<ServerPlayer> paramPredicate);
  
  void flush();
  
  boolean isClosed();
  
  UUID getId();
  
  @Nullable
  String getCategory();
  
  void setCategory(@Nullable String paramString);
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\audiochannel\AudioChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */