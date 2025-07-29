/*    */ package de.maxhenkel.voicechat.plugins.impl.audiochannel;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Position;
/*    */ import de.maxhenkel.voicechat.api.audiochannel.ClientLocationalAudioChannel;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientUtils;
/*    */ import de.maxhenkel.voicechat.voice.common.LocationSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.class_243;
/*    */ 
/*    */ public class ClientLocationalAudioChannelImpl
/*    */   extends ClientAudioChannelImpl
/*    */   implements ClientLocationalAudioChannel {
/*    */   private Position position;
/*    */   private float distance;
/*    */   
/*    */   public ClientLocationalAudioChannelImpl(UUID id, Position position) {
/* 18 */     super(id);
/* 19 */     this.position = position;
/* 20 */     this.distance = ClientUtils.getDefaultDistanceClient();
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundPacket<?> createSoundPacket(short[] rawAudio) {
/* 25 */     return (SoundPacket<?>)new LocationSoundPacket(this.id, this.id, rawAudio, new class_243(this.position.getX(), this.position.getY(), this.position.getZ()), this.distance, this.category);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLocation(Position position) {
/* 30 */     this.position = position;
/*    */   }
/*    */ 
/*    */   
/*    */   public Position getLocation() {
/* 35 */     return this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDistance() {
/* 40 */     return this.distance;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDistance(float distance) {
/* 45 */     this.distance = distance;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audiochannel\ClientLocationalAudioChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */