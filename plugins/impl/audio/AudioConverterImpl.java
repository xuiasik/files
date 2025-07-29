/*    */ package de.maxhenkel.voicechat.plugins.impl.audio;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.audio.AudioConverter;
/*    */ import de.maxhenkel.voicechat.voice.common.Utils;
/*    */ 
/*    */ public class AudioConverterImpl
/*    */   implements AudioConverter
/*    */ {
/*    */   public short[] bytesToShorts(byte[] bytes) {
/* 10 */     return Utils.bytesToShorts(bytes);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] shortsToBytes(short[] shorts) {
/* 15 */     return Utils.shortsToBytes(shorts);
/*    */   }
/*    */ 
/*    */   
/*    */   public short[] floatsToShorts(float[] floats) {
/* 20 */     return Utils.floatsToShorts(floats);
/*    */   }
/*    */ 
/*    */   
/*    */   public float[] shortsToFloats(short[] shorts) {
/* 25 */     return Utils.shortsToFloats(shorts);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] floatsToBytes(float[] floats) {
/* 30 */     return Utils.floatsToBytes(floats);
/*    */   }
/*    */ 
/*    */   
/*    */   public float[] bytesToFloats(byte[] bytes) {
/* 35 */     return Utils.bytesToFloats(bytes);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audio\AudioConverterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */