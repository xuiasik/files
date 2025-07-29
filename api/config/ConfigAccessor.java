/*    */ package de.maxhenkel.voicechat.api.config;
/*    */ 
/*    */ import javax.annotation.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ConfigAccessor
/*    */ {
/*    */   boolean hasKey(String paramString);
/*    */   
/*    */   @Nullable
/*    */   String getValue(String paramString);
/*    */   
/*    */   default String getString(String key, String def) {
/* 46 */     String value = getValue(key);
/* 47 */     if (value == null) {
/* 48 */       return def;
/*    */     }
/* 50 */     return value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean getBoolean(String key, boolean def) {
/* 61 */     String value = getValue(key);
/* 62 */     if (value == null) {
/* 63 */       return def;
/*    */     }
/* 65 */     return Boolean.parseBoolean(value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default int getInt(String key, int def) {
/* 77 */     String value = getValue(key);
/* 78 */     if (value == null) {
/* 79 */       return def;
/*    */     }
/* 81 */     return Integer.parseInt(value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default double getDouble(String key, double def) {
/* 93 */     String value = getValue(key);
/* 94 */     if (value == null) {
/* 95 */       return def;
/*    */     }
/* 97 */     return Double.parseDouble(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\config\ConfigAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */