package de.maxhenkel.voicechat.api;

import de.maxhenkel.voicechat.api.events.EventRegistration;

public interface VoicechatPlugin {
  String getPluginId();
  
  default void initialize(VoicechatApi api) {}
  
  default void registerEvents(EventRegistration registration) {}
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\VoicechatPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */