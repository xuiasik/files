package de.maxhenkel.voicechat.api.events;

import de.maxhenkel.voicechat.api.VoicechatConnection;
import java.util.UUID;
import javax.annotation.Nullable;

public interface PlayerStateChangedEvent extends ServerEvent {
  boolean isDisabled();
  
  boolean isDisconnected();
  
  UUID getPlayerUuid();
  
  @Nullable
  VoicechatConnection getConnection();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\events\PlayerStateChangedEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */