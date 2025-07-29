package de.maxhenkel.voicechat.api.events;

import de.maxhenkel.voicechat.api.Position;
import java.util.UUID;
import javax.annotation.Nullable;

public interface ClientReceiveSoundEvent extends ClientEvent {
  UUID getId();
  
  short[] getRawAudio();
  
  void setRawAudio(@Nullable short[] paramArrayOfshort);
  
  public static interface StaticSound extends ClientReceiveSoundEvent {}
  
  public static interface LocationalSound extends ClientReceiveSoundEvent {
    Position getPosition();
    
    float getDistance();
  }
  
  public static interface EntitySound extends ClientReceiveSoundEvent {
    UUID getEntityId();
    
    boolean isWhispering();
    
    float getDistance();
  }
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\events\ClientReceiveSoundEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */