/*    */ package de.maxhenkel.voicechat.plugins.impl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.DatagramPacket;
/*    */ import java.net.DatagramSocket;
/*    */ 
/*    */ public class VoicechatSocketBase
/*    */ {
/*  9 */   private final byte[] BUFFER = new byte[4096];
/*    */   
/*    */   public RawUdpPacketImpl read(DatagramSocket socket) throws IOException {
/* 12 */     DatagramPacket packet = new DatagramPacket(this.BUFFER, this.BUFFER.length);
/* 13 */     socket.receive(packet);
/*    */     
/* 15 */     long timestamp = System.currentTimeMillis();
/* 16 */     byte[] data = new byte[packet.getLength()];
/* 17 */     System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
/* 18 */     return new RawUdpPacketImpl(data, packet.getSocketAddress(), timestamp);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\VoicechatSocketBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */