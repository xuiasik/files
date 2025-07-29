/*    */ package de.maxhenkel.voicechat.plugins.impl.packets;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.packets.EntitySoundPacket;
/*    */ import de.maxhenkel.voicechat.api.packets.SoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.PlayerSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.Utils;
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class EntitySoundPacketImpl extends SoundPacketImpl implements EntitySoundPacket {
/*    */   private final PlayerSoundPacket packet;
/*    */   
/*    */   public EntitySoundPacketImpl(PlayerSoundPacket packet) {
/* 15 */     super((SoundPacket<?>)packet);
/* 16 */     this.packet = packet;
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getEntityUuid() {
/* 21 */     return this.packet.getSender();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWhispering() {
/* 26 */     return this.packet.isWhispering();
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDistance() {
/* 31 */     return this.packet.getDistance();
/*    */   }
/*    */ 
/*    */   
/*    */   public PlayerSoundPacket getPacket() {
/* 36 */     return this.packet;
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getChannelId() {
/* 41 */     return this.packet.getChannelId();
/*    */   }
/*    */   
/*    */   public static class BuilderImpl
/*    */     extends SoundPacketImpl.BuilderImpl<BuilderImpl, EntitySoundPacket> implements EntitySoundPacket.Builder<BuilderImpl> {
/*    */     protected UUID entityUuid;
/*    */     protected boolean whispering;
/*    */     protected float distance;
/*    */     
/*    */     public BuilderImpl(SoundPacketImpl soundPacket) {
/* 51 */       super(soundPacket);
/* 52 */       if (soundPacket instanceof EntitySoundPacketImpl) {
/* 53 */         EntitySoundPacketImpl p = (EntitySoundPacketImpl)soundPacket;
/* 54 */         this.entityUuid = p.getEntityUuid();
/* 55 */         this.whispering = p.isWhispering();
/* 56 */         this.distance = p.getDistance();
/* 57 */       } else if (soundPacket instanceof LocationalSoundPacketImpl) {
/* 58 */         LocationalSoundPacketImpl p = (LocationalSoundPacketImpl)soundPacket;
/* 59 */         this.distance = p.getDistance();
/*    */       } else {
/* 61 */         this.distance = Utils.getDefaultDistanceServer();
/*    */       } 
/*    */     }
/*    */     
/*    */     public BuilderImpl(UUID channelId, UUID sender, byte[] opusEncodedData, long sequenceNumber, @Nullable String category) {
/* 66 */       super(channelId, sender, opusEncodedData, sequenceNumber, category);
/* 67 */       this.distance = Utils.getDefaultDistanceServer();
/*    */     }
/*    */ 
/*    */     
/*    */     public BuilderImpl entityUuid(UUID entityUuid) {
/* 72 */       this.entityUuid = entityUuid;
/* 73 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public BuilderImpl whispering(boolean whispering) {
/* 78 */       this.whispering = whispering;
/* 79 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public BuilderImpl distance(float distance) {
/* 84 */       this.distance = distance;
/* 85 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public EntitySoundPacket build() {
/* 90 */       if (this.entityUuid == null) {
/* 91 */         throw new IllegalStateException("entityUuid missing");
/*    */       }
/* 93 */       return new EntitySoundPacketImpl(new PlayerSoundPacket(this.channelId, this.sender, this.opusEncodedData, this.sequenceNumber, this.whispering, this.distance, this.category));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\packets\EntitySoundPacketImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */