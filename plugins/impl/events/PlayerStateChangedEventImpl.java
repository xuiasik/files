/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*    */ import de.maxhenkel.voicechat.api.events.PlayerStateChangedEvent;
/*    */ import de.maxhenkel.voicechat.plugins.impl.VoicechatConnectionImpl;
/*    */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*    */ import de.maxhenkel.voicechat.voice.server.Server;
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_3222;
/*    */ 
/*    */ public class PlayerStateChangedEventImpl
/*    */   extends ServerEventImpl
/*    */   implements PlayerStateChangedEvent {
/*    */   protected final PlayerState state;
/*    */   @Nullable
/*    */   protected VoicechatConnectionImpl connection;
/*    */   
/*    */   public PlayerStateChangedEventImpl(PlayerState state) {
/* 21 */     this.state = state;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisabled() {
/* 26 */     return this.state.isDisabled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisconnected() {
/* 31 */     return this.state.isDisconnected();
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getPlayerUuid() {
/* 36 */     return this.state.getUuid();
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public VoicechatConnection getConnection() {
/* 42 */     if (this.connection == null) {
/* 43 */       Server server = Voicechat.SERVER.getServer();
/* 44 */       if (server == null) {
/* 45 */         return null;
/*    */       }
/* 47 */       class_3222 player = server.getServer().method_3760().method_14602(this.state.getUuid());
/* 48 */       if (player == null) {
/* 49 */         return null;
/*    */       }
/* 51 */       this.connection = VoicechatConnectionImpl.fromPlayer(player);
/*    */     } 
/* 53 */     return (VoicechatConnection)this.connection;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\PlayerStateChangedEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */