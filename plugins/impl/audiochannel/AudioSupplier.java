/*    */ package de.maxhenkel.voicechat.plugins.impl.audiochannel;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ public class AudioSupplier
/*    */   implements Supplier<short[]>
/*    */ {
/*    */   private final short[] audioData;
/*    */   private final short[] frame;
/*    */   private int framePosition;
/*    */   
/*    */   public AudioSupplier(short[] audioData) {
/* 15 */     this.audioData = audioData;
/* 16 */     this.frame = new short[960];
/*    */   }
/*    */ 
/*    */   
/*    */   public short[] get() {
/* 21 */     if (this.framePosition >= this.audioData.length) {
/* 22 */       return null;
/*    */     }
/*    */     
/* 25 */     Arrays.fill(this.frame, (short)0);
/* 26 */     System.arraycopy(this.audioData, this.framePosition, this.frame, 0, Math.min(this.frame.length, this.audioData.length - this.framePosition));
/* 27 */     this.framePosition += this.frame.length;
/* 28 */     return this.frame;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audiochannel\AudioSupplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */