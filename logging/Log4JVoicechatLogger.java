/*    */ package de.maxhenkel.voicechat.logging;
/*    */ 
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.Appender;
/*    */ import org.apache.logging.log4j.core.Logger;
/*    */ import org.apache.logging.log4j.core.config.Configurator;
/*    */ 
/*    */ public class Log4JVoicechatLogger
/*    */   implements VoicechatLogger {
/*    */   private final boolean debugMode;
/*    */   private final Logger logger;
/*    */   
/*    */   public Log4JVoicechatLogger(Logger logger) {
/* 18 */     this.logger = logger;
/* 19 */     this.debugMode = Voicechat.debugMode();
/*    */     
/*    */     try {
/* 22 */       if (this.debugMode) {
/* 23 */         initDebugLogLevel();
/*    */       }
/* 25 */     } catch (Throwable t) {
/* 26 */       logger.error("Failed to set log level", t);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Log4JVoicechatLogger(String name) {
/* 31 */     this(LogManager.getLogger(name));
/*    */   }
/*    */   
/*    */   private void initDebugLogLevel() throws Exception {
/* 35 */     if (!(this.logger instanceof Logger)) {
/* 36 */       throw new IllegalStateException("Logger is not an instance of org.apache.logging.log4j.core.Logger");
/*    */     }
/* 38 */     Logger coreLogger = (Logger)this.logger;
/* 39 */     Map<String, Appender> appenders = coreLogger.getAppenders();
/* 40 */     coreLogger.setAdditive(false);
/* 41 */     Configurator.setLevel(this.logger.getName(), Level.DEBUG);
/* 42 */     for (Appender appender : appenders.values()) {
/* 43 */       coreLogger.addAppender(appender);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void log(LogLevel level, String message, Object... args) {
/* 49 */     if (!isEnabled(level)) {
/*    */       return;
/*    */     }
/* 52 */     this.logger.log(fromLogLevel(level), modifyMessage(message), args);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEnabled(LogLevel level) {
/* 57 */     return this.logger.isEnabled(fromLogLevel(level));
/*    */   }
/*    */   
/*    */   private Level fromLogLevel(LogLevel level) {
/* 61 */     switch (level) {
/*    */       case TRACE:
/* 63 */         return Level.TRACE;
/*    */       case DEBUG:
/* 65 */         return Level.DEBUG;
/*    */       case WARN:
/* 67 */         return Level.WARN;
/*    */       case ERROR:
/* 69 */         return Level.ERROR;
/*    */       case FATAL:
/* 71 */         return Level.FATAL;
/*    */     } 
/* 73 */     return Level.INFO;
/*    */   }
/*    */ 
/*    */   
/*    */   private String modifyMessage(String message) {
/* 78 */     return String.format("[%s] %s", new Object[] { this.logger.getName(), message });
/*    */   }
/*    */   
/*    */   public Logger getLogger() {
/* 82 */     return this.logger;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\logging\Log4JVoicechatLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */