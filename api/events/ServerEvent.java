package de.maxhenkel.voicechat.api.events;

import de.maxhenkel.voicechat.api.VoicechatServerApi;

public interface ServerEvent extends Event {
  VoicechatServerApi getVoicechat();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\events\ServerEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */