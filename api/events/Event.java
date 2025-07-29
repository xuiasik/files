package de.maxhenkel.voicechat.api.events;

public interface Event {
  boolean isCancellable();
  
  boolean cancel();
  
  boolean isCancelled();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\events\Event.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */