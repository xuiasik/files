package de.maxhenkel.voicechat.api.packets;

public interface MicrophonePacket extends Packet, ConvertablePacket {
  boolean isWhispering();
  
  byte[] getOpusEncodedData();
  
  void setOpusEncodedData(byte[] paramArrayOfbyte);
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\packets\MicrophonePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */