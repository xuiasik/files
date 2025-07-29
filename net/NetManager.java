/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2596;
/*    */ import net.minecraft.class_2658;
/*    */ import net.minecraft.class_3222;
/*    */ import net.minecraft.class_3244;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public abstract class NetManager {
/*    */   public Channel<UpdateStatePacket> updateStateChannel;
/*    */   public Channel<PlayerStatePacket> playerStateChannel;
/*    */   public Channel<PlayerStatesPacket> playerStatesChannel;
/*    */   public Channel<SecretPacket> secretChannel;
/*    */   public Channel<RequestSecretPacket> requestSecretChannel;
/*    */   public Channel<AddGroupPacket> addGroupChannel;
/*    */   public Channel<RemoveGroupPacket> removeGroupChannel;
/*    */   public Channel<JoinGroupPacket> joinGroupChannel;
/*    */   public Channel<CreateGroupPacket> createGroupChannel;
/*    */   public Channel<LeaveGroupPacket> leaveGroupChannel;
/*    */   public Channel<JoinedGroupPacket> joinedGroupChannel;
/*    */   public Channel<AddCategoryPacket> addCategoryChannel;
/*    */   public Channel<RemoveCategoryPacket> removeCategoryChannel;
/*    */   
/*    */   public void init() {
/* 28 */     this.updateStateChannel = registerReceiver(UpdateStatePacket.class, false, true);
/* 29 */     this.playerStateChannel = registerReceiver(PlayerStatePacket.class, true, false);
/* 30 */     this.playerStatesChannel = registerReceiver(PlayerStatesPacket.class, true, false);
/* 31 */     this.secretChannel = registerReceiver(SecretPacket.class, true, false);
/* 32 */     this.requestSecretChannel = registerReceiver(RequestSecretPacket.class, false, true);
/* 33 */     this.addGroupChannel = registerReceiver(AddGroupPacket.class, true, false);
/* 34 */     this.removeGroupChannel = registerReceiver(RemoveGroupPacket.class, true, false);
/* 35 */     this.joinGroupChannel = registerReceiver(JoinGroupPacket.class, false, true);
/* 36 */     this.createGroupChannel = registerReceiver(CreateGroupPacket.class, false, true);
/* 37 */     this.leaveGroupChannel = registerReceiver(LeaveGroupPacket.class, false, true);
/* 38 */     this.joinedGroupChannel = registerReceiver(JoinedGroupPacket.class, true, false);
/* 39 */     this.addCategoryChannel = registerReceiver(AddCategoryPacket.class, true, false);
/* 40 */     this.removeCategoryChannel = registerReceiver(RemoveCategoryPacket.class, true, false);
/*    */   }
/*    */   
/*    */   public abstract <T extends Packet<T>> Channel<T> registerReceiver(Class<T> paramClass, boolean paramBoolean1, boolean paramBoolean2);
/*    */   
/*    */   public static void sendToClient(class_3222 player, Packet<?> packet) {
/* 46 */     if (!Voicechat.SERVER.isCompatible(player)) {
/*    */       return;
/*    */     }
/* 49 */     class_2540 buffer = new class_2540(Unpooled.buffer());
/* 50 */     packet.toBytes(buffer);
/* 51 */     player.field_13987.method_14364((class_2596)new class_2658(packet.getIdentifier(), buffer));
/*    */   }
/*    */   
/*    */   public static interface ServerReceiver<T extends Packet<T>> {
/*    */     void onPacket(MinecraftServer param1MinecraftServer, class_3222 param1class_3222, class_3244 param1class_3244, T param1T);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\NetManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */