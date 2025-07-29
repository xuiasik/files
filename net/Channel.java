/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_3222;
/*    */ import net.minecraft.class_3244;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Channel<T extends Packet<T>>
/*    */ {
/*    */   @Nullable
/*    */   private NetManager.ServerReceiver<T> serverListener;
/*    */   
/*    */   public void setServerListener(NetManager.ServerReceiver<T> packetReceiver) {
/* 19 */     this.serverListener = packetReceiver;
/*    */   }
/*    */   
/*    */   public void onServerPacket(MinecraftServer server, class_3222 player, class_3244 handler, T packet) {
/* 23 */     server.execute(() -> {
/*    */           if (this.serverListener != null)
/*    */             this.serverListener.onPacket(server, player, handler, (T)packet); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\Channel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */