/*    */ package de.maxhenkel.voicechat.plugins.impl.audiolistener;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.ServerPlayer;
/*    */ import de.maxhenkel.voicechat.api.audiolistener.PlayerAudioListener;
/*    */ import de.maxhenkel.voicechat.api.packets.SoundPacket;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Consumer;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class PlayerAudioListenerImpl
/*    */   implements PlayerAudioListener
/*    */ {
/*    */   private final UUID playerUuid;
/*    */   private final Consumer<SoundPacket> listener;
/*    */   private final UUID listenerId;
/*    */   
/*    */   public PlayerAudioListenerImpl(UUID playerUuid, Consumer<SoundPacket> listener) {
/* 18 */     this.playerUuid = playerUuid;
/* 19 */     this.listener = listener;
/* 20 */     this.listenerId = UUID.randomUUID();
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getListenerId() {
/* 25 */     return this.listenerId;
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getPlayerUuid() {
/* 30 */     return this.playerUuid;
/*    */   }
/*    */   
/*    */   public Consumer<SoundPacket> getListener() {
/* 34 */     return this.listener;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static class BuilderImpl
/*    */     implements PlayerAudioListener.Builder
/*    */   {
/*    */     @Nullable
/*    */     private UUID playerUuid;
/*    */     
/*    */     @Nullable
/*    */     private Consumer<SoundPacket> listener;
/*    */ 
/*    */     
/*    */     public PlayerAudioListener.Builder setPlayer(ServerPlayer player) {
/* 50 */       this.playerUuid = player.getUuid();
/* 51 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public PlayerAudioListener.Builder setPlayer(UUID playerUuid) {
/* 56 */       this.playerUuid = playerUuid;
/* 57 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public PlayerAudioListener.Builder setPacketListener(Consumer<SoundPacket> listener) {
/* 62 */       this.listener = listener;
/* 63 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public PlayerAudioListener build() {
/* 68 */       if (this.playerUuid == null) {
/* 69 */         throw new IllegalStateException("No player provided");
/*    */       }
/* 71 */       if (this.listener == null) {
/* 72 */         throw new IllegalStateException("No listener provided");
/*    */       }
/* 74 */       return new PlayerAudioListenerImpl(this.playerUuid, this.listener);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audiolistener\PlayerAudioListenerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */