/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Position;
/*    */ import de.maxhenkel.voicechat.api.events.ClientReceiveSoundEvent;
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class ClientReceiveSoundEventImpl
/*    */   extends ClientEventImpl
/*    */   implements ClientReceiveSoundEvent {
/*    */   private UUID id;
/*    */   private short[] rawAudio;
/*    */   
/*    */   public ClientReceiveSoundEventImpl(UUID id, short[] rawAudio) {
/* 15 */     this.id = id;
/* 16 */     this.rawAudio = rawAudio;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isCancellable() {
/* 22 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getId() {
/* 27 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public short[] getRawAudio() {
/* 33 */     return this.rawAudio;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRawAudio(@Nullable short[] rawAudio) {
/* 38 */     this.rawAudio = rawAudio;
/*    */   }
/*    */   
/*    */   public static class EntitySoundImpl extends ClientReceiveSoundEventImpl implements ClientReceiveSoundEvent.EntitySound {
/*    */     private UUID entity;
/*    */     private boolean whispering;
/*    */     private float distance;
/*    */     
/*    */     public EntitySoundImpl(UUID id, UUID entity, short[] rawAudio, boolean whispering, float distance) {
/* 47 */       super(id, rawAudio);
/* 48 */       this.entity = entity;
/* 49 */       this.whispering = whispering;
/* 50 */       this.distance = distance;
/*    */     }
/*    */ 
/*    */     
/*    */     public UUID getEntityId() {
/* 55 */       return this.entity;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isWhispering() {
/* 60 */       return this.whispering;
/*    */     }
/*    */ 
/*    */     
/*    */     public float getDistance() {
/* 65 */       return this.distance;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class LocationalSoundImpl extends ClientReceiveSoundEventImpl implements ClientReceiveSoundEvent.LocationalSound {
/*    */     private Position position;
/*    */     private float distance;
/*    */     
/*    */     public LocationalSoundImpl(UUID id, short[] rawAudio, Position position, float distance) {
/* 74 */       super(id, rawAudio);
/* 75 */       this.position = position;
/* 76 */       this.distance = distance;
/*    */     }
/*    */ 
/*    */     
/*    */     public Position getPosition() {
/* 81 */       return this.position;
/*    */     }
/*    */ 
/*    */     
/*    */     public float getDistance() {
/* 86 */       return this.distance;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class StaticSoundImpl
/*    */     extends ClientReceiveSoundEventImpl implements ClientReceiveSoundEvent.StaticSound {
/*    */     public StaticSoundImpl(UUID id, short[] rawAudio) {
/* 93 */       super(id, rawAudio);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\ClientReceiveSoundEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */