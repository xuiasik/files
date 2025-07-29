/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*    */ import de.maxhenkel.voicechat.api.events.PacketEvent;
/*    */ import de.maxhenkel.voicechat.api.packets.Packet;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class PacketEventImpl<T extends Packet>
/*    */   extends ServerEventImpl
/*    */   implements PacketEvent<T> {
/*    */   private final T packet;
/*    */   @Nullable
/*    */   private final VoicechatConnection receiverConnection;
/*    */   @Nullable
/*    */   private final VoicechatConnection senderConnection;
/*    */   
/*    */   public PacketEventImpl(T packet, @Nullable VoicechatConnection senderConnection, @Nullable VoicechatConnection receiverConnection) {
/* 18 */     this.packet = packet;
/* 19 */     this.senderConnection = senderConnection;
/* 20 */     this.receiverConnection = receiverConnection;
/*    */   }
/*    */ 
/*    */   
/*    */   public T getPacket() {
/* 25 */     return this.packet;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public VoicechatConnection getReceiverConnection() {
/* 31 */     return this.receiverConnection;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public VoicechatConnection getSenderConnection() {
/* 37 */     return this.senderConnection;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\PacketEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */