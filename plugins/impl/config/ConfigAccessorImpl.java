/*    */ package de.maxhenkel.voicechat.plugins.impl.config;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.config.ConfigAccessor;
/*    */ import de.maxhenkel.voicechat.configbuilder.Config;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class ConfigAccessorImpl
/*    */   implements ConfigAccessor
/*    */ {
/*    */   private Config config;
/*    */   
/*    */   public ConfigAccessorImpl(Config config) {
/* 13 */     this.config = config;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasKey(String key) {
/* 18 */     return this.config.getEntries().containsKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getValue(String key) {
/* 24 */     return (String)this.config.getEntries().get(key);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\config\ConfigAccessorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */