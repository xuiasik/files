package de.maxhenkel.voicechat.api;

import java.net.SocketAddress;

public interface RawUdpPacket {
  byte[] getData();
  
  long getTimestamp();
  
  SocketAddress getSocketAddress();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\RawUdpPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */