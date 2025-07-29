/*     */ package de.maxhenkel.voicechat.plugins.impl.packets;
/*     */ 
/*     */ import de.maxhenkel.voicechat.api.Position;
/*     */ import de.maxhenkel.voicechat.api.packets.EntitySoundPacket;
/*     */ import de.maxhenkel.voicechat.api.packets.LocationalSoundPacket;
/*     */ import de.maxhenkel.voicechat.api.packets.SoundPacket;
/*     */ import de.maxhenkel.voicechat.api.packets.StaticSoundPacket;
/*     */ import de.maxhenkel.voicechat.plugins.impl.PositionImpl;
/*     */ import de.maxhenkel.voicechat.voice.common.GroupSoundPacket;
/*     */ import de.maxhenkel.voicechat.voice.common.LocationSoundPacket;
/*     */ import de.maxhenkel.voicechat.voice.common.PlayerSoundPacket;
/*     */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*     */ import de.maxhenkel.voicechat.voice.common.Utils;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public class SoundPacketImpl
/*     */   implements SoundPacket {
/*     */   private final SoundPacket<?> packet;
/*     */   
/*     */   public SoundPacketImpl(SoundPacket<?> packet) {
/*  22 */     this.packet = packet;
/*     */   }
/*     */ 
/*     */   
/*     */   public UUID getChannelId() {
/*  27 */     return this.packet.getChannelId();
/*     */   }
/*     */ 
/*     */   
/*     */   public UUID getSender() {
/*  32 */     return this.packet.getSender();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getOpusEncodedData() {
/*  37 */     return this.packet.getData();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSequenceNumber() {
/*  42 */     return this.packet.getSequenceNumber();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getCategory() {
/*  48 */     return this.packet.getCategory();
/*     */   }
/*     */   
/*     */   public SoundPacket<?> getPacket() {
/*  52 */     return this.packet;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntitySoundPacket.Builder<?> entitySoundPacketBuilder() {
/*  57 */     return new EntitySoundPacketImpl.BuilderImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public LocationalSoundPacket.Builder<?> locationalSoundPacketBuilder() {
/*  62 */     return new LocationalSoundPacketImpl.BuilderImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public StaticSoundPacket.Builder<?> staticSoundPacketBuilder() {
/*  67 */     return new StaticSoundPacketImpl.BuilderImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntitySoundPacket toEntitySoundPacket(UUID entityUuid, boolean whispering) {
/*  72 */     return new EntitySoundPacketImpl(new PlayerSoundPacket(this.packet.getChannelId(), this.packet.getSender(), this.packet.getData(), this.packet.getSequenceNumber(), whispering, getDistance(), null));
/*     */   }
/*     */ 
/*     */   
/*     */   public LocationalSoundPacket toLocationalSoundPacket(Position position) {
/*  77 */     if (position instanceof PositionImpl) {
/*  78 */       PositionImpl p = (PositionImpl)position;
/*  79 */       return new LocationalSoundPacketImpl(new LocationSoundPacket(this.packet.getChannelId(), this.packet.getSender(), p.getPosition(), this.packet.getData(), this.packet.getSequenceNumber(), getDistance(), null));
/*     */     } 
/*  81 */     throw new IllegalArgumentException("position is not an instance of PositionImpl");
/*     */   }
/*     */ 
/*     */   
/*     */   private float getDistance() {
/*  86 */     if (this instanceof EntitySoundPacket) {
/*  87 */       EntitySoundPacket p = (EntitySoundPacket)this;
/*  88 */       return p.getDistance();
/*  89 */     }  if (this instanceof LocationalSoundPacket) {
/*  90 */       LocationalSoundPacket p = (LocationalSoundPacket)this;
/*  91 */       return p.getDistance();
/*     */     } 
/*  93 */     return Utils.getDefaultDistanceServer();
/*     */   }
/*     */ 
/*     */   
/*     */   public StaticSoundPacket toStaticSoundPacket() {
/*  98 */     return new StaticSoundPacketImpl(new GroupSoundPacket(this.packet.getChannelId(), this.packet.getSender(), this.packet.getData(), this.packet.getSequenceNumber(), null));
/*     */   }
/*     */   
/*     */   public static abstract class BuilderImpl<T extends BuilderImpl<T, P>, P extends SoundPacket>
/*     */     implements SoundPacket.Builder<T, P> {
/*     */     protected UUID channelId;
/*     */     protected UUID sender;
/*     */     protected byte[] opusEncodedData;
/*     */     protected long sequenceNumber;
/*     */     @Nullable
/*     */     protected String category;
/*     */     
/*     */     public BuilderImpl(SoundPacketImpl soundPacket) {
/* 111 */       this.channelId = soundPacket.getChannelId();
/* 112 */       this.sender = soundPacket.getSender();
/* 113 */       this.opusEncodedData = soundPacket.getOpusEncodedData();
/* 114 */       this.sequenceNumber = soundPacket.getSequenceNumber();
/* 115 */       this.category = soundPacket.getCategory();
/*     */     }
/*     */     
/*     */     public BuilderImpl(UUID channelId, UUID sender, byte[] opusEncodedData, long sequenceNumber, @Nullable String category) {
/* 119 */       this.channelId = channelId;
/* 120 */       this.sender = sender;
/* 121 */       this.opusEncodedData = opusEncodedData;
/* 122 */       this.sequenceNumber = sequenceNumber;
/* 123 */       this.category = category;
/*     */     }
/*     */ 
/*     */     
/*     */     public T channelId(UUID channelId) {
/* 128 */       if (channelId == null) {
/* 129 */         throw new IllegalArgumentException("channelId can't be null");
/*     */       }
/* 131 */       this.channelId = channelId;
/* 132 */       return (T)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public T opusEncodedData(byte[] data) {
/* 137 */       this.opusEncodedData = data;
/* 138 */       return (T)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public T category(@Nullable String category) {
/* 143 */       this.category = category;
/* 144 */       return (T)this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\packets\SoundPacketImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */