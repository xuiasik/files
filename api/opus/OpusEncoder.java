package de.maxhenkel.voicechat.api.opus;

public interface OpusEncoder {
  byte[] encode(short[] paramArrayOfshort);
  
  void resetState();
  
  boolean isClosed();
  
  void close();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\opus\OpusEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */