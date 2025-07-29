package de.maxhenkel.voicechat.api.packets;

public interface StaticSoundPacket extends SoundPacket {
  public static interface Builder<T extends Builder<T>> extends SoundPacket.Builder<T, StaticSoundPacket> {}
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\packets\StaticSoundPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */