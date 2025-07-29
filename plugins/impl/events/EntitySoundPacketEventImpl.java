/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*    */ import de.maxhenkel.voicechat.api.events.EntitySoundPacketEvent;
/*    */ import de.maxhenkel.voicechat.api.packets.EntitySoundPacket;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class EntitySoundPacketEventImpl
/*    */   extends SoundPacketEventImpl<EntitySoundPacket>
/*    */   implements EntitySoundPacketEvent {
/*    */   public EntitySoundPacketEventImpl(EntitySoundPacket packet, @Nullable VoicechatConnection senderConnection, VoicechatConnection receiverConnection, String source) {
/* 12 */     super(packet, senderConnection, receiverConnection, source);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\EntitySoundPacketEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */