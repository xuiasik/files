package de.maxhenkel.voicechat.api.events;

public interface SoundPacketEvent<T extends de.maxhenkel.voicechat.api.packets.Packet> extends PacketEvent<T> {
  public static final String SOURCE_GROUP = "group";
  
  public static final String SOURCE_PROXIMITY = "proximity";
  
  public static final String SOURCE_SPECTATOR = "spectator";
  
  public static final String SOURCE_PLUGIN = "plugin";
  
  String getSource();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\events\SoundPacketEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */