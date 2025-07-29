/*    */ package de.maxhenkel.voicechat.plugins.impl.audiochannel;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.audiochannel.ClientAudioChannel;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
/*    */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public abstract class ClientAudioChannelImpl
/*    */   implements ClientAudioChannel
/*    */ {
/*    */   protected UUID id;
/*    */   @Nullable
/*    */   protected String category;
/*    */   
/*    */   public ClientAudioChannelImpl(UUID id) {
/* 18 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getId() {
/* 23 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract SoundPacket<?> createSoundPacket(short[] paramArrayOfshort);
/*    */   
/*    */   public void play(short[] rawAudio) {
/* 30 */     ClientVoicechat client = ClientManager.getClient();
/* 31 */     if (client != null) {
/* 32 */       client.processSoundPacket(createSoundPacket(rawAudio));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getCategory() {
/* 39 */     return this.category;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCategory(@Nullable String category) {
/* 44 */     this.category = category;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audiochannel\ClientAudioChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */