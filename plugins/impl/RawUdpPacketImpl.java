/*    */ package de.maxhenkel.voicechat.plugins.impl;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.RawUdpPacket;
/*    */ import java.net.SocketAddress;
/*    */ 
/*    */ public class RawUdpPacketImpl
/*    */   implements RawUdpPacket
/*    */ {
/*    */   private final byte[] data;
/*    */   private final SocketAddress socketAddress;
/*    */   private final long timestamp;
/*    */   
/*    */   public RawUdpPacketImpl(byte[] data, SocketAddress socketAddress, long timestamp) {
/* 14 */     this.data = data;
/* 15 */     this.socketAddress = socketAddress;
/* 16 */     this.timestamp = timestamp;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getData() {
/* 21 */     return this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTimestamp() {
/* 26 */     return this.timestamp;
/*    */   }
/*    */ 
/*    */   
/*    */   public SocketAddress getSocketAddress() {
/* 31 */     return this.socketAddress;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\RawUdpPacketImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */