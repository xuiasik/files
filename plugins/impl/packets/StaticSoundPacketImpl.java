/*    */ package de.maxhenkel.voicechat.plugins.impl.packets;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.packets.SoundPacket;
/*    */ import de.maxhenkel.voicechat.api.packets.StaticSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.GroupSoundPacket;
/*    */ import de.maxhenkel.voicechat.voice.common.SoundPacket;
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class StaticSoundPacketImpl extends SoundPacketImpl implements StaticSoundPacket {
/*    */   public StaticSoundPacketImpl(GroupSoundPacket packet) {
/* 12 */     super((SoundPacket<?>)packet);
/*    */   }
/*    */   
/*    */   public static class BuilderImpl
/*    */     extends SoundPacketImpl.BuilderImpl<BuilderImpl, StaticSoundPacket> implements StaticSoundPacket.Builder<BuilderImpl> {
/*    */     public BuilderImpl(SoundPacketImpl soundPacket) {
/* 18 */       super(soundPacket);
/*    */     }
/*    */     
/*    */     public BuilderImpl(UUID channelId, UUID sender, byte[] opusEncodedData, long sequenceNumber, @Nullable String category) {
/* 22 */       super(channelId, sender, opusEncodedData, sequenceNumber, category);
/*    */     }
/*    */ 
/*    */     
/*    */     public StaticSoundPacket build() {
/* 27 */       return new StaticSoundPacketImpl(new GroupSoundPacket(this.channelId, this.sender, this.opusEncodedData, this.sequenceNumber, this.category));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\packets\StaticSoundPacketImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */