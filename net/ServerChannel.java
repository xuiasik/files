/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.class_3222;
/*    */ import net.minecraft.class_3244;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerChannel<T extends Packet<T>>
/*    */ {
/* 15 */   private final List<NetManager.ServerReceiver<T>> listeners = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public void registerServerListener(NetManager.ServerReceiver<T> packetReceiver) {
/* 19 */     this.listeners.add(packetReceiver);
/*    */   }
/*    */   
/*    */   public void onPacket(MinecraftServer server, class_3222 player, class_3244 handler, T packet) {
/* 23 */     this.listeners.forEach(receiver -> receiver.onPacket(server, player, handler, packet));
/*    */   }
/*    */   
/*    */   public List<NetManager.ServerReceiver<T>> getListeners() {
/* 27 */     return this.listeners;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\ServerChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */