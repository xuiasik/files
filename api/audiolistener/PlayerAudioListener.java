package de.maxhenkel.voicechat.api.audiolistener;

import de.maxhenkel.voicechat.api.ServerPlayer;
import de.maxhenkel.voicechat.api.packets.SoundPacket;
import java.util.UUID;
import java.util.function.Consumer;

public interface PlayerAudioListener extends AudioListener {
  UUID getPlayerUuid();
  
  public static interface Builder {
    Builder setPlayer(ServerPlayer param1ServerPlayer);
    
    Builder setPlayer(UUID param1UUID);
    
    Builder setPacketListener(Consumer<SoundPacket> param1Consumer);
    
    PlayerAudioListener build();
  }
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\audiolistener\PlayerAudioListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */