/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Position;
/*    */ import de.maxhenkel.voicechat.api.events.OpenALSoundEvent;
/*    */ import de.maxhenkel.voicechat.plugins.impl.PositionImpl;
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class OpenALSoundEventImpl
/*    */   extends ClientEventImpl
/*    */   implements OpenALSoundEvent, OpenALSoundEvent.Pre, OpenALSoundEvent.Post {
/*    */   @Nullable
/*    */   protected PositionImpl position;
/*    */   @Nullable
/*    */   protected UUID channelId;
/*    */   @Nullable
/*    */   protected String category;
/*    */   protected int source;
/*    */   
/*    */   public OpenALSoundEventImpl(@Nullable UUID channelId, @Nullable PositionImpl position, @Nullable String category, int source) {
/* 21 */     this.channelId = channelId;
/* 22 */     this.position = position;
/* 23 */     this.category = category;
/* 24 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Position getPosition() {
/* 30 */     return (Position)this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public UUID getChannelId() {
/* 36 */     return this.channelId;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSource() {
/* 41 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCategory() {
/* 46 */     return this.category;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancellable() {
/* 51 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\OpenALSoundEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */