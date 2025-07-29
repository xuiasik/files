/*    */ package de.maxhenkel.voicechat.logging;
/*    */ 
/*    */ public interface VoicechatLogger
/*    */ {
/*    */   void log(LogLevel paramLogLevel, String paramString, Object... paramVarArgs);
/*    */   
/*    */   boolean isEnabled(LogLevel paramLogLevel);
/*    */   
/*    */   void trace(String message, Object... args) {
/* 10 */     log(LogLevel.TRACE, message, args);
/*    */   }
/*    */   
/*    */   void debug(String message, Object... args) {
/* 14 */     log(LogLevel.DEBUG, message, args);
/*    */   }
/*    */   
/*    */   void info(String message, Object... args) {
/* 18 */     log(LogLevel.INFO, message, args);
/*    */   }
/*    */   
/*    */   void warn(String message, Object... args) {
/* 22 */     log(LogLevel.WARN, message, args);
/*    */   }
/*    */   
/*    */   void error(String message, Object... args) {
/* 26 */     log(LogLevel.ERROR, message, args);
/*    */   }
/*    */   
/*    */   void fatal(String message, Object... args) {
/* 30 */     log(LogLevel.FATAL, message, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\logging\VoicechatLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */