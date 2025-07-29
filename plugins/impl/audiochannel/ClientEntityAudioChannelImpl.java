/*    */ package de.maxhenkel.voicechat.plugins.impl.audiochannel;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.audiochannel.ClientEntityAudioChannel;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientUtils;
/*    */ import de.maxhenkel.voicechat.voice.common.PlayerSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class ClientEntityAudioChannelImpl
/*    */   extends ClientAudioChannelImpl
/*    */   implements ClientEntityAudioChannel {
/*    */   private UUID entityId;
/*    */   private boolean whispering;
/*    */   private float distance;
/*    */   
/*    */   public ClientEntityAudioChannelImpl(UUID id, UUID entityId) {
/* 17 */     super(id);
/* 18 */     this.entityId = entityId;
/* 19 */     this.whispering = false;
/* 20 */     this.distance = ClientUtils.getDefaultDistanceClient();
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundPacket<?> createSoundPacket(short[] rawAudio) {
/* 25 */     return (SoundPacket<?>)new PlayerSoundPacket(this.id, this.id, rawAudio, this.whispering, this.distance, this.category);
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getEntityId() {
/* 30 */     return this.entityId;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setWhispering(boolean whispering) {
/* 35 */     this.whispering = whispering;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWhispering() {
/* 40 */     return this.whispering;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDistance() {
/* 45 */     return this.distance;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDistance(float distance) {
/* 50 */     this.distance = distance;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audiochannel\ClientEntityAudioChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */