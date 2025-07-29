/*    */ package de.maxhenkel.voicechat.api;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.audiochannel.ClientEntityAudioChannel;
/*    */ import de.maxhenkel.voicechat.api.audiochannel.ClientLocationalAudioChannel;
/*    */ import de.maxhenkel.voicechat.api.audiochannel.ClientStaticAudioChannel;
/*    */ import de.maxhenkel.voicechat.api.config.ConfigAccessor;
/*    */ import java.util.UUID;
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
/*    */ public interface VoicechatClientApi
/*    */   extends VoicechatApi
/*    */ {
/*    */   boolean isMuted();
/*    */   
/*    */   boolean isDisabled();
/*    */   
/*    */   boolean isDisconnected();
/*    */   
/*    */   @Nullable
/*    */   Group getGroup();
/*    */   
/*    */   @Deprecated
/*    */   ClientEntityAudioChannel createEntityAudioChannel(UUID paramUUID);
/*    */   
/*    */   ClientEntityAudioChannel createEntityAudioChannel(UUID paramUUID, Entity paramEntity);
/*    */   
/*    */   ClientLocationalAudioChannel createLocationalAudioChannel(UUID paramUUID, Position paramPosition);
/*    */   
/*    */   ClientStaticAudioChannel createStaticAudioChannel(UUID paramUUID);
/*    */   
/*    */   void registerClientVolumeCategory(VolumeCategory paramVolumeCategory);
/*    */   
/*    */   default void unregisterClientVolumeCategory(VolumeCategory category) {
/* 82 */     unregisterClientVolumeCategory(category.getId());
/*    */   }
/*    */   
/*    */   void unregisterClientVolumeCategory(String paramString);
/*    */   
/*    */   ConfigAccessor getClientConfig();
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\VoicechatClientApi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */