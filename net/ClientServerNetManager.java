/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import io.netty.buffer.Unpooled;
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_2817;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_634;
/*    */ 
/*    */ public abstract class ClientServerNetManager extends NetManager {
/*    */   public static void sendToServer(Packet<?> packet) {
/* 12 */     class_2540 buffer = new class_2540(Unpooled.buffer());
/* 13 */     packet.toBytes(buffer);
/* 14 */     class_634 connection = class_310.method_1551().method_1562();
/* 15 */     if (connection != null && connection.method_2890() != null) {
/* 16 */       connection.method_2883((class_2596)new class_2817(packet.getIdentifier(), buffer));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends Packet<T>> void setClientListener(Channel<T> channel, ClientReceiver<T> packetReceiver) {
/* 25 */     if (channel instanceof ClientServerChannel) {
/* 26 */       ClientServerChannel<T> c = (ClientServerChannel<T>)channel;
/* 27 */       c.setClientListener(packetReceiver);
/*    */     } else {
/* 29 */       throw new IllegalStateException("Channel is not a ClientServerChannel");
/*    */     } 
/*    */   }
/*    */   
/*    */   public static interface ClientReceiver<T extends Packet<T>> {
/*    */     void onPacket(class_310 param1class_310, class_634 param1class_634, T param1T);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\ClientServerNetManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */