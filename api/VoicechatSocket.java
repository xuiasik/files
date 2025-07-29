package de.maxhenkel.voicechat.api;

import java.net.SocketAddress;

public interface VoicechatSocket {
  void open(int paramInt, String paramString) throws Exception;
  
  RawUdpPacket read() throws Exception;
  
  void send(byte[] paramArrayOfbyte, SocketAddress paramSocketAddress) throws Exception;
  
  int getLocalPort();
  
  void close();
  
  boolean isClosed();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\VoicechatSocket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */