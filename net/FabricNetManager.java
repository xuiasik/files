/*    */ package de.maxhenkel.voicechat.net;
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import java.util.Set;
/*    */ import net.fabricmc.api.EnvType;
/*    */ import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
/*    */ import net.fabricmc.fabric.api.networking.v1.PacketSender;
/*    */ import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
/*    */ import net.fabricmc.loader.api.FabricLoader;
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_3222;
/*    */ import net.minecraft.class_3244;
/*    */ import net.minecraft.class_634;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class FabricNetManager extends NetManager {
/* 18 */   private final Set<class_2960> packets = new HashSet<>();
/*    */ 
/*    */   
/*    */   public Set<class_2960> getPackets() {
/* 22 */     return this.packets;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends Packet<T>> Channel<T> registerReceiver(Class<T> packetType, boolean toClient, boolean toServer) {
/* 27 */     ClientServerChannel<T> c = new ClientServerChannel<>();
/*    */     try {
/* 29 */       Packet packet = packetType.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 30 */       class_2960 identifier = packet.getIdentifier();
/* 31 */       this.packets.add(identifier);
/* 32 */       if (toServer) {
/* 33 */         ServerPlayNetworking.registerGlobalReceiver(identifier, (server, player, handler, buf, responseSender) -> {
/*    */               try {
/*    */                 if (!Voicechat.SERVER.isCompatible(player) && !packetType.equals(RequestSecretPacket.class)) {
/*    */                   return;
/*    */                 }
/*    */                 Packet packet = packetType.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/*    */                 packet.fromBytes(buf);
/*    */                 c.onServerPacket(server, player, handler, packet);
/* 41 */               } catch (Exception e) {
/*    */                 Voicechat.LOGGER.error("Failed to process packet", new Object[] { e });
/*    */               } 
/*    */             });
/*    */       }
/* 46 */       if (toClient && FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
/* 47 */         ClientPlayNetworking.registerGlobalReceiver(identifier, (client, handler, buf, responseSender) -> {
/*    */               try {
/*    */                 Packet packet = packetType.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/*    */                 packet.fromBytes(buf);
/*    */                 client.execute(());
/* 52 */               } catch (Exception e) {
/*    */                 Voicechat.LOGGER.error("Failed to register packet receiver", new Object[] { e });
/*    */               } 
/*    */             });
/*    */       }
/* 57 */     } catch (Exception e) {
/* 58 */       throw new IllegalArgumentException(e);
/*    */     } 
/* 60 */     return c;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\FabricNetManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */