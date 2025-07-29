/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*    */ import de.maxhenkel.voicechat.api.events.SoundPacketEvent;
/*    */ import de.maxhenkel.voicechat.api.packets.Packet;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class SoundPacketEventImpl<T extends Packet>
/*    */   extends PacketEventImpl<T>
/*    */   implements SoundPacketEvent<T> {
/*    */   private final String source;
/*    */   
/*    */   public SoundPacketEventImpl(T packet, @Nullable VoicechatConnection senderConnection, @Nullable VoicechatConnection receiverConnection, String source) {
/* 14 */     super(packet, senderConnection, receiverConnection);
/* 15 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSource() {
/* 20 */     return this.source;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\SoundPacketEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */