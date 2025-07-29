/*    */ package de.maxhenkel.voicechat.concentus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OpusException
/*    */   extends Exception
/*    */ {
/*    */   private String _message;
/*    */   private int _opus_error_code;
/*    */   
/*    */   public OpusException() {
/* 39 */     this("", 0);
/*    */   }
/*    */   
/*    */   public OpusException(String message) {
/* 43 */     this(message, 1);
/*    */   }
/*    */   
/*    */   public OpusException(String message, int opus_error_code) {
/* 47 */     this._message = message + ": " + CodecHelpers.opus_strerror(opus_error_code);
/* 48 */     this._opus_error_code = opus_error_code;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 53 */     return this._message;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\OpusException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */