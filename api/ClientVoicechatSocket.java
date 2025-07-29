package de.maxhenkel.voicechat.api;

import java.net.SocketAddress;

public interface ClientVoicechatSocket {
  void open() throws Exception;
  
  RawUdpPacket read() throws Exception;
  
  void send(byte[] paramArrayOfbyte, SocketAddress paramSocketAddress) throws Exception;
  
  void close();
  
  boolean isClosed();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\ClientVoicechatSocket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */