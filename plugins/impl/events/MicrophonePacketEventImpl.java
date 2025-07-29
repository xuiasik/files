/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*    */ import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
/*    */ import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
/*    */ 
/*    */ public class MicrophonePacketEventImpl
/*    */   extends PacketEventImpl<MicrophonePacket> implements MicrophonePacketEvent {
/*    */   public MicrophonePacketEventImpl(MicrophonePacket packet, VoicechatConnection connection) {
/* 10 */     super(packet, connection, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\MicrophonePacketEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */