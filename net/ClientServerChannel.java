/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_634;
/*    */ 
/*    */ public class ClientServerChannel<T extends Packet<T>>
/*    */   extends Channel<T>
/*    */ {
/*    */   @Nullable
/*    */   private ClientServerNetManager.ClientReceiver<T> clientListener;
/*    */   
/*    */   public void setClientListener(ClientServerNetManager.ClientReceiver<T> packetReceiver) {
/* 14 */     this.clientListener = packetReceiver;
/*    */   }
/*    */   
/*    */   public void onClientPacket(class_310 client, class_634 handler, T packet) {
/* 18 */     client.execute(() -> {
/*    */           if (this.clientListener != null)
/*    */             this.clientListener.onPacket(client, handler, (T)packet); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\ClientServerChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */