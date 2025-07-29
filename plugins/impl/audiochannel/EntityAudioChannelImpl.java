/*    */ package de.maxhenkel.voicechat.plugins.impl.audiochannel;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Entity;
/*    */ import de.maxhenkel.voicechat.api.audiochannel.EntityAudioChannel;
/*    */ import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
/*    */ import de.maxhenkel.voicechat.plugins.impl.EntityImpl;
/*    */ import de.maxhenkel.voicechat.plugins.impl.ServerPlayerImpl;
/*    */ import de.maxhenkel.voicechat.voice.common.PlayerSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.Utils;
/*    */ import de.maxhenkel.voicechat.voice.server.Server;
/*    */ import de.maxhenkel.voicechat.voice.server.ServerWorldUtils;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.class_3218;
/*    */ import net.minecraft.class_3222;
/*    */ 
/*    */ public class EntityAudioChannelImpl
/*    */   extends AudioChannelImpl implements EntityAudioChannel {
/*    */   protected Entity entity;
/*    */   protected boolean whispering;
/*    */   protected float distance;
/*    */   
/*    */   public EntityAudioChannelImpl(UUID channelId, Server server, Entity entity) {
/* 24 */     super(channelId, server);
/* 25 */     this.entity = entity;
/* 26 */     this.whispering = false;
/* 27 */     this.distance = Utils.getDefaultDistanceServer();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setWhispering(boolean whispering) {
/* 32 */     this.whispering = whispering;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWhispering() {
/* 37 */     return this.whispering;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateEntity(Entity entity) {
/* 42 */     this.entity = entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public Entity getEntity() {
/* 47 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDistance() {
/* 52 */     return this.distance;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDistance(float distance) {
/* 57 */     this.distance = distance;
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(byte[] opusData) {
/* 62 */     broadcast(new PlayerSoundPacket(this.channelId, this.entity.getUuid(), opusData, this.sequenceNumber.getAndIncrement(), this.whispering, this.distance, this.category));
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(MicrophonePacket microphonePacket) {
/* 67 */     broadcast(new PlayerSoundPacket(this.channelId, this.entity.getUuid(), microphonePacket.getOpusEncodedData(), this.sequenceNumber.getAndIncrement(), this.whispering, this.distance, this.category));
/*    */   }
/*    */ 
/*    */   
/*    */   public void flush() {
/* 72 */     broadcast(new PlayerSoundPacket(this.channelId, this.entity.getUuid(), new byte[0], this.sequenceNumber.getAndIncrement(), this.whispering, this.distance, this.category));
/*    */   }
/*    */   
/*    */   private void broadcast(PlayerSoundPacket packet) {
/* 76 */     if (!(this.entity instanceof EntityImpl)) {
/* 77 */       throw new IllegalArgumentException("entity is not an instance of EntityImpl");
/*    */     }
/* 79 */     EntityImpl entityimpl = (EntityImpl)this.entity;
/* 80 */     this.server.broadcast(ServerWorldUtils.getPlayersInRange((class_3218)(entityimpl.getRealEntity()).field_6002, entityimpl.getRealEntity().method_5836(1.0F), this.server.getBroadcastRange(this.distance), (this.filter == null) ? (player -> true) : (player -> this.filter.test(new ServerPlayerImpl(player)))), (SoundPacket)packet, null, null, null, "plugin");
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audiochannel\EntityAudioChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */