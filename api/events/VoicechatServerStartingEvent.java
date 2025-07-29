package de.maxhenkel.voicechat.api.events;

import de.maxhenkel.voicechat.api.VoicechatSocket;
import javax.annotation.Nullable;

public interface VoicechatServerStartingEvent extends ServerEvent {
  void setSocketImplementation(VoicechatSocket paramVoicechatSocket);
  
  @Nullable
  VoicechatSocket getSocketImplementation();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\events\VoicechatServerStartingEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */