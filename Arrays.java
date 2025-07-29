/*     */ package de.maxhenkel.voicechat.concentus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Arrays
/*     */ {
/*     */   static int[][] InitTwoDimensionalArrayInt(int x, int y) {
/*  36 */     return new int[x][y];
/*     */   }
/*     */   
/*     */   static float[][] InitTwoDimensionalArrayFloat(int x, int y) {
/*  40 */     return new float[x][y];
/*     */   }
/*     */   
/*     */   static short[][] InitTwoDimensionalArrayShort(int x, int y) {
/*  44 */     return new short[x][y];
/*     */   }
/*     */   
/*     */   static byte[][] InitTwoDimensionalArrayByte(int x, int y) {
/*  48 */     return new byte[x][y];
/*     */   }
/*     */   
/*     */   static byte[][][] InitThreeDimensionalArrayByte(int x, int y, int z) {
/*  52 */     return new byte[x][y][z];
/*     */   }
/*     */   
/*     */   static void MemSet(byte[] array, byte value) {
/*  56 */     java.util.Arrays.fill(array, value);
/*     */   }
/*     */   
/*     */   static void MemSet(short[] array, short value) {
/*  60 */     java.util.Arrays.fill(array, value);
/*     */   }
/*     */   
/*     */   static void MemSet(int[] array, int value) {
/*  64 */     java.util.Arrays.fill(array, value);
/*     */   }
/*     */   
/*     */   static void MemSet(float[] array, float value) {
/*  68 */     java.util.Arrays.fill(array, value);
/*     */   }
/*     */   
/*     */   static void MemSet(byte[] array, byte value, int length) {
/*  72 */     java.util.Arrays.fill(array, 0, length, value);
/*     */   }
/*     */   
/*     */   static void MemSet(short[] array, short value, int length) {
/*  76 */     java.util.Arrays.fill(array, 0, length, value);
/*     */   }
/*     */   
/*     */   static void MemSet(int[] array, int value, int length) {
/*  80 */     java.util.Arrays.fill(array, 0, length, value);
/*     */   }
/*     */   
/*     */   static void MemSet(float[] array, float value, int length) {
/*  84 */     java.util.Arrays.fill(array, 0, length, value);
/*     */   }
/*     */   
/*     */   static void MemSetWithOffset(byte[] array, byte value, int offset, int length) {
/*  88 */     java.util.Arrays.fill(array, offset, offset + length, value);
/*     */   }
/*     */   
/*     */   static void MemSetWithOffset(short[] array, short value, int offset, int length) {
/*  92 */     java.util.Arrays.fill(array, offset, offset + length, value);
/*     */   }
/*     */   
/*     */   static void MemSetWithOffset(int[] array, int value, int offset, int length) {
/*  96 */     java.util.Arrays.fill(array, offset, offset + length, value);
/*     */   }
/*     */   
/*     */   static void MemMove(byte[] array, int src_idx, int dst_idx, int length) {
/* 100 */     System.arraycopy(array, src_idx, array, dst_idx, length);
/*     */   }
/*     */   
/*     */   static void MemMove(short[] array, int src_idx, int dst_idx, int length) {
/* 104 */     System.arraycopy(array, src_idx, array, dst_idx, length);
/*     */   }
/*     */   
/*     */   static void MemMove(int[] array, int src_idx, int dst_idx, int length) {
/* 108 */     System.arraycopy(array, src_idx, array, dst_idx, length);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Arrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */