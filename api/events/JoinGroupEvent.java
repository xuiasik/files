package de.maxhenkel.voicechat.api.events;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;

public interface JoinGroupEvent extends GroupEvent {
  Group getGroup();
  
  VoicechatConnection getConnection();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\events\JoinGroupEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */