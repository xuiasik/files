package de.maxhenkel.voicechat.api.events;

import de.maxhenkel.voicechat.api.ClientVoicechatSocket;
import javax.annotation.Nullable;

public interface ClientVoicechatInitializationEvent extends ClientEvent {
  void setSocketImplementation(ClientVoicechatSocket paramClientVoicechatSocket);
  
  @Nullable
  ClientVoicechatSocket getSocketImplementation();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\events\ClientVoicechatInitializationEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */