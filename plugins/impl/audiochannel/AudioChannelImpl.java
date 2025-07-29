/*    */ package de.maxhenkel.voicechat.plugins.impl.audiochannel;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.ServerPlayer;
/*    */ import de.maxhenkel.voicechat.api.audiochannel.AudioChannel;
/*    */ import de.maxhenkel.voicechat.voice.server.Server;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public abstract class AudioChannelImpl
/*    */   implements AudioChannel
/*    */ {
/*    */   protected UUID channelId;
/*    */   protected Server server;
/*    */   protected AtomicLong sequenceNumber;
/*    */   @Nullable
/*    */   protected Predicate<ServerPlayer> filter;
/*    */   @Nullable
/*    */   protected String category;
/*    */   
/*    */   public AudioChannelImpl(UUID channelId, Server server) {
/* 23 */     this.channelId = channelId;
/* 24 */     this.server = server;
/* 25 */     this.sequenceNumber = new AtomicLong();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFilter(Predicate<ServerPlayer> filter) {
/* 30 */     this.filter = filter;
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getId() {
/* 35 */     return this.channelId;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 40 */     return this.server.isClosed();
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getCategory() {
/* 46 */     return this.category;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCategory(@Nullable String category) {
/* 51 */     this.category = category;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audiochannel\AudioChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */