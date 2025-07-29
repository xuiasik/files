/*    */ package de.maxhenkel.voicechat.plugins.impl.packets;
/*    */ import de.maxhenkel.voicechat.api.Position;
/*    */ import de.maxhenkel.voicechat.api.packets.EntitySoundPacket;
/*    */ import de.maxhenkel.voicechat.api.packets.LocationalSoundPacket;
/*    */ import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
/*    */ import de.maxhenkel.voicechat.api.packets.StaticSoundPacket;
/*    */ import de.maxhenkel.voicechat.plugins.impl.PositionImpl;
/*    */ import de.maxhenkel.voicechat.voice.common.GroupSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.LocationSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.MicPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.PlayerSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.Utils;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class MicrophonePacketImpl implements MicrophonePacket {
/*    */   private final MicPacket packet;
/*    */   
/*    */   public MicrophonePacketImpl(MicPacket packet, UUID sender) {
/* 20 */     this.packet = packet;
/* 21 */     this.sender = sender;
/*    */   }
/*    */   private final UUID sender;
/*    */   
/*    */   public boolean isWhispering() {
/* 26 */     return this.packet.isWhispering();
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getOpusEncodedData() {
/* 31 */     return this.packet.getData();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setOpusEncodedData(byte[] data) {
/* 36 */     this.packet.setData(Objects.<byte[]>requireNonNull(data));
/*    */   }
/*    */ 
/*    */   
/*    */   public EntitySoundPacket.Builder<?> entitySoundPacketBuilder() {
/* 41 */     return new EntitySoundPacketImpl.BuilderImpl(this.sender, this.sender, this.packet.getData(), this.packet.getSequenceNumber(), null);
/*    */   }
/*    */ 
/*    */   
/*    */   public LocationalSoundPacket.Builder<?> locationalSoundPacketBuilder() {
/* 46 */     return new LocationalSoundPacketImpl.BuilderImpl(this.sender, this.sender, this.packet.getData(), this.packet.getSequenceNumber(), null);
/*    */   }
/*    */ 
/*    */   
/*    */   public StaticSoundPacket.Builder<?> staticSoundPacketBuilder() {
/* 51 */     return new StaticSoundPacketImpl.BuilderImpl(this.sender, this.sender, this.packet.getData(), this.packet.getSequenceNumber(), null);
/*    */   }
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public EntitySoundPacket toEntitySoundPacket(UUID entityUuid, boolean whispering) {
/* 57 */     return new EntitySoundPacketImpl(new PlayerSoundPacket(this.sender, this.sender, this.packet.getData(), this.packet.getSequenceNumber(), whispering, Utils.getDefaultDistanceServer(), null));
/*    */   }
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public LocationalSoundPacket toLocationalSoundPacket(Position position) {
/* 63 */     if (position instanceof PositionImpl) {
/* 64 */       PositionImpl p = (PositionImpl)position;
/* 65 */       return new LocationalSoundPacketImpl(new LocationSoundPacket(this.sender, this.sender, p.getPosition(), this.packet.getData(), this.packet.getSequenceNumber(), Utils.getDefaultDistanceServer(), null));
/*    */     } 
/* 67 */     throw new IllegalArgumentException("position is not an instance of PositionImpl");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public StaticSoundPacket toStaticSoundPacket() {
/* 74 */     return new StaticSoundPacketImpl(new GroupSoundPacket(this.sender, this.sender, this.packet.getData(), this.packet.getSequenceNumber(), null));
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\packets\MicrophonePacketImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */