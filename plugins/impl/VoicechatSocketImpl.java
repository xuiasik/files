/*     */ package de.maxhenkel.voicechat.plugins.impl;
/*     */ 
/*     */ import de.maxhenkel.voicechat.Voicechat;
/*     */ import de.maxhenkel.voicechat.api.RawUdpPacket;
/*     */ import de.maxhenkel.voicechat.api.VoicechatSocket;
/*     */ import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
/*     */ import java.net.BindException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public class VoicechatSocketImpl extends VoicechatSocketBase implements VoicechatSocket {
/*     */   public void open(int port, String bindAddress) throws Exception {
/*  18 */     if (this.socket != null) {
/*  19 */       throw new IllegalStateException("Socket already opened");
/*     */     }
/*  21 */     checkCorrectHost();
/*  22 */     InetAddress address = null;
/*     */     try {
/*  24 */       if (!bindAddress.isEmpty()) {
/*  25 */         address = InetAddress.getByName(bindAddress);
/*     */       }
/*  27 */     } catch (Exception e) {
/*  28 */       bindAddress = "";
/*  29 */       Voicechat.LOGGER.error("Failed to parse bind IP address '{}'", new Object[] { bindAddress, e });
/*     */     } 
/*     */     
/*     */     try {
/*     */       try {
/*  34 */         this.socket = new DatagramSocket(port, address);
/*  35 */       } catch (BindException e) {
/*  36 */         if (address == null || bindAddress.equals("0.0.0.0")) {
/*  37 */           throw e;
/*     */         }
/*  39 */         Voicechat.LOGGER.error("Failed to bind to address '{}', binding to wildcard IP instead", new Object[] { bindAddress });
/*  40 */         this.socket = new DatagramSocket(port);
/*     */       } 
/*  42 */     } catch (BindException e) {
/*  43 */       Voicechat.LOGGER.error("Failed to run voice chat at UDP port {}, make sure no other application is running at that port", new Object[] { Integer.valueOf(port) });
/*  44 */       Voicechat.LOGGER.error("Voice chat server error", new Object[] { e });
/*  45 */       if (CommonCompatibilityManager.INSTANCE.isDedicatedServer()) {
/*  46 */         Voicechat.LOGGER.error("Shutting down server", new Object[0]);
/*  47 */         System.exit(1);
/*     */       } 
/*  49 */       throw e;
/*     */     } 
/*     */   } @Nullable
/*     */   private DatagramSocket socket;
/*     */   private void checkCorrectHost() throws Exception {
/*  54 */     String host = (String)Voicechat.SERVER_CONFIG.voiceHost.get();
/*  55 */     if (!host.isEmpty()) {
/*     */       try {
/*  57 */         new URI("voicechat://" + host);
/*  58 */         Voicechat.LOGGER.info("Voice host is '{}'", new Object[] { host });
/*  59 */       } catch (URISyntaxException e) {
/*  60 */         Voicechat.LOGGER.warn("Failed to parse voice host", new Object[] { e });
/*  61 */         System.exit(1);
/*  62 */         throw e;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public RawUdpPacket read() throws Exception {
/*  69 */     if (this.socket == null) {
/*  70 */       throw new IllegalStateException("Socket not opened yet");
/*     */     }
/*  72 */     return read(this.socket);
/*     */   }
/*     */ 
/*     */   
/*     */   public void send(byte[] data, SocketAddress address) throws Exception {
/*  77 */     if (this.socket == null || this.socket.isClosed()) {
/*     */       return;
/*     */     }
/*  80 */     this.socket.send(new DatagramPacket(data, data.length, address));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/*  85 */     if (this.socket == null) {
/*  86 */       return -1;
/*     */     }
/*  88 */     return this.socket.getLocalPort();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  93 */     if (this.socket != null) {
/*  94 */       this.socket.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 100 */     if (this.socket == null) {
/* 101 */       return true;
/*     */     }
/* 103 */     return this.socket.isClosed();
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\VoicechatSocketImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */