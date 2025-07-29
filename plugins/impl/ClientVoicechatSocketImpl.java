/*    */ package de.maxhenkel.voicechat.plugins.impl;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.ClientVoicechatSocket;
/*    */ import de.maxhenkel.voicechat.api.RawUdpPacket;
/*    */ import java.net.DatagramPacket;
/*    */ import java.net.DatagramSocket;
/*    */ import java.net.SocketAddress;
/*    */ 
/*    */ public class ClientVoicechatSocketImpl
/*    */   extends VoicechatSocketBase
/*    */   implements ClientVoicechatSocket
/*    */ {
/*    */   private DatagramSocket socket;
/*    */   
/*    */   public void open() throws Exception {
/* 16 */     this.socket = new DatagramSocket();
/*    */   }
/*    */ 
/*    */   
/*    */   public RawUdpPacket read() throws Exception {
/* 21 */     if (this.socket == null) {
/* 22 */       throw new IllegalStateException("Socket not opened yet");
/*    */     }
/* 24 */     return read(this.socket);
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(byte[] data, SocketAddress address) throws Exception {
/* 29 */     if (this.socket == null) {
/*    */       return;
/*    */     }
/* 32 */     this.socket.send(new DatagramPacket(data, data.length, address));
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 37 */     if (this.socket != null) {
/* 38 */       this.socket.close();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 44 */     return (this.socket == null);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\ClientVoicechatSocketImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */