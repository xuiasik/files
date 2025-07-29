package de.maxhenkel.voicechat.api.audio;

public interface AudioConverter {
  short[] bytesToShorts(byte[] paramArrayOfbyte);
  
  byte[] shortsToBytes(short[] paramArrayOfshort);
  
  short[] floatsToShorts(float[] paramArrayOffloat);
  
  float[] shortsToFloats(short[] paramArrayOfshort);
  
  byte[] floatsToBytes(float[] paramArrayOffloat);
  
  float[] bytesToFloats(byte[] paramArrayOfbyte);
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\audio\AudioConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */