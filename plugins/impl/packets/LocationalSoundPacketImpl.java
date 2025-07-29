/*    */ package de.maxhenkel.voicechat.plugins.impl.packets;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Position;
/*    */ import de.maxhenkel.voicechat.api.packets.LocationalSoundPacket;
/*    */ import de.maxhenkel.voicechat.api.packets.SoundPacket;
/*    */ import de.maxhenkel.voicechat.plugins.impl.PositionImpl;
/*    */ import de.maxhenkel.voicechat.voice.common.LocationSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.Utils;
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class LocationalSoundPacketImpl extends SoundPacketImpl implements LocationalSoundPacket {
/*    */   private final LocationSoundPacket packet;
/*    */   private final PositionImpl position;
/*    */   
/*    */   public LocationalSoundPacketImpl(LocationSoundPacket packet) {
/* 18 */     super((SoundPacket<?>)packet);
/* 19 */     this.packet = packet;
/* 20 */     this.position = new PositionImpl(packet.getLocation());
/*    */   }
/*    */   
/*    */   public Position getPosition() {
/* 24 */     return (Position)this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDistance() {
/* 29 */     return this.packet.getDistance();
/*    */   }
/*    */ 
/*    */   
/*    */   public LocationSoundPacket getPacket() {
/* 34 */     return this.packet;
/*    */   }
/*    */   
/*    */   public static class BuilderImpl
/*    */     extends SoundPacketImpl.BuilderImpl<BuilderImpl, LocationalSoundPacket> implements LocationalSoundPacket.Builder<BuilderImpl> {
/*    */     protected PositionImpl position;
/*    */     protected float distance;
/*    */     
/*    */     public BuilderImpl(SoundPacketImpl soundPacket) {
/* 43 */       super(soundPacket);
/* 44 */       if (soundPacket instanceof LocationalSoundPacketImpl) {
/* 45 */         LocationalSoundPacketImpl p = (LocationalSoundPacketImpl)soundPacket;
/* 46 */         this.position = p.position;
/* 47 */         this.distance = p.getDistance();
/* 48 */       } else if (soundPacket instanceof EntitySoundPacketImpl) {
/* 49 */         EntitySoundPacketImpl p = (EntitySoundPacketImpl)soundPacket;
/* 50 */         this.distance = p.getDistance();
/*    */       } else {
/* 52 */         this.distance = Utils.getDefaultDistanceServer();
/*    */       } 
/*    */     }
/*    */     
/*    */     public BuilderImpl(UUID channelId, UUID sender, byte[] opusEncodedData, long sequenceNumber, @Nullable String category) {
/* 57 */       super(channelId, sender, opusEncodedData, sequenceNumber, category);
/* 58 */       this.distance = Utils.getDefaultDistanceServer();
/*    */     }
/*    */ 
/*    */     
/*    */     public BuilderImpl position(Position position) {
/* 63 */       this.position = (PositionImpl)position;
/* 64 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public BuilderImpl distance(float distance) {
/* 69 */       this.distance = distance;
/* 70 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LocationalSoundPacket build() {
/* 75 */       if (this.position == null) {
/* 76 */         throw new IllegalStateException("position missing");
/*    */       }
/* 78 */       return new LocationalSoundPacketImpl(new LocationSoundPacket(this.channelId, this.sender, this.position.getPosition(), this.opusEncodedData, this.sequenceNumber, this.distance, this.category));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\packets\LocationalSoundPacketImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */