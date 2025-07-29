package de.maxhenkel.voicechat.api.events;

import java.util.UUID;

public interface PlayerDisconnectedEvent extends ServerEvent {
  UUID getPlayerUuid();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\events\PlayerDisconnectedEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */