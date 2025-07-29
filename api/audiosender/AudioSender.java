package de.maxhenkel.voicechat.api.audiosender;

public interface AudioSender {
  AudioSender whispering(boolean paramBoolean);
  
  boolean isWhispering();
  
  AudioSender sequenceNumber(long paramLong);
  
  boolean canSend();
  
  boolean send(byte[] paramArrayOfbyte);
  
  boolean reset();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\audiosender\AudioSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */