package de.maxhenkel.voicechat.api.events;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import javax.annotation.Nullable;

public interface CreateGroupEvent extends GroupEvent {
  Group getGroup();
  
  @Nullable
  VoicechatConnection getConnection();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\events\CreateGroupEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */