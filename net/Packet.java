package de.maxhenkel.voicechat.net;

import net.minecraft.class_2540;
import net.minecraft.class_2960;

public interface Packet<T extends Packet<T>> {
  class_2960 getIdentifier();
  
  T fromBytes(class_2540 paramclass_2540);
  
  void toBytes(class_2540 paramclass_2540);
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\Packet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */