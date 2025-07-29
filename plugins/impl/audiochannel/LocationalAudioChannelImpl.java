/*    */ package de.maxhenkel.voicechat.plugins.impl.audiochannel;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Position;
/*    */ import de.maxhenkel.voicechat.api.ServerLevel;
/*    */ import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
/*    */ import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
/*    */ import de.maxhenkel.voicechat.plugins.impl.PositionImpl;
/*    */ import de.maxhenkel.voicechat.plugins.impl.ServerLevelImpl;
/*    */ import de.maxhenkel.voicechat.plugins.impl.ServerPlayerImpl;
/*    */ import de.maxhenkel.voicechat.voice.common.LocationSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.Utils;
/*    */ import de.maxhenkel.voicechat.voice.server.Server;
/*    */ import de.maxhenkel.voicechat.voice.server.ServerWorldUtils;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.class_3222;
/*    */ 
/*    */ public class LocationalAudioChannelImpl
/*    */   extends AudioChannelImpl
/*    */   implements LocationalAudioChannel {
/*    */   protected ServerLevel level;
/*    */   protected PositionImpl position;
/*    */   protected float distance;
/*    */   
/*    */   public LocationalAudioChannelImpl(UUID channelId, Server server, ServerLevel level, PositionImpl position) {
/* 26 */     super(channelId, server);
/* 27 */     this.level = level;
/* 28 */     this.position = position;
/* 29 */     this.distance = Utils.getDefaultDistanceServer();
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocation(Position position) {
/* 34 */     if (position instanceof PositionImpl) {
/* 35 */       this.position = (PositionImpl)position;
/*    */     } else {
/* 37 */       throw new IllegalArgumentException("position is not an instance of PositionImpl");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Position getLocation() {
/* 43 */     return (Position)this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDistance() {
/* 48 */     return this.distance;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDistance(float distance) {
/* 53 */     this.distance = distance;
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(byte[] opusData) {
/* 58 */     broadcast(new LocationSoundPacket(this.channelId, this.channelId, this.position.getPosition(), opusData, this.sequenceNumber.getAndIncrement(), this.distance, this.category));
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(MicrophonePacket packet) {
/* 63 */     send(packet.getOpusEncodedData());
/*    */   }
/*    */ 
/*    */   
/*    */   public void flush() {
/* 68 */     broadcast(new LocationSoundPacket(this.channelId, this.channelId, this.position.getPosition(), new byte[0], this.sequenceNumber.getAndIncrement(), this.distance, this.category));
/*    */   }
/*    */   
/*    */   private void broadcast(LocationSoundPacket packet) {
/* 72 */     if (!(this.level instanceof ServerLevelImpl)) {
/* 73 */       throw new IllegalArgumentException("level is not an instance of ServerLevelImpl");
/*    */     }
/* 75 */     ServerLevelImpl serverLevel = (ServerLevelImpl)this.level;
/* 76 */     this.server.broadcast(ServerWorldUtils.getPlayersInRange(serverLevel.getRawServerLevel(), this.position.getPosition(), this.server.getBroadcastRange(this.distance), (this.filter == null) ? (player -> true) : (player -> this.filter.test(new ServerPlayerImpl(player)))), (SoundPacket)packet, null, null, null, "plugin");
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audiochannel\LocationalAudioChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */