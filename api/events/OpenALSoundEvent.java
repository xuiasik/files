package de.maxhenkel.voicechat.api.events;

import de.maxhenkel.voicechat.api.Position;
import java.util.UUID;
import javax.annotation.Nullable;

public interface OpenALSoundEvent extends ClientEvent {
  @Nullable
  Position getPosition();
  
  @Nullable
  UUID getChannelId();
  
  int getSource();
  
  @Nullable
  String getCategory();
  
  public static interface Post extends OpenALSoundEvent {}
  
  public static interface Pre extends OpenALSoundEvent {}
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\events\OpenALSoundEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */