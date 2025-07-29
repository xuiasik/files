/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.events.MergeClientSoundEvent;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MergeClientSoundEventImpl
/*    */   extends ClientEventImpl
/*    */   implements MergeClientSoundEvent
/*    */ {
/*    */   @Nullable
/*    */   private List<short[]> audioToMerge;
/*    */   
/*    */   public boolean isCancellable() {
/* 20 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void mergeAudio(short[] audio) {
/* 25 */     if (this.audioToMerge == null) {
/* 26 */       this.audioToMerge = (List)new ArrayList<>();
/*    */     }
/* 28 */     this.audioToMerge.add(audio);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public List<short[]> getAudioToMerge() {
/* 33 */     return this.audioToMerge;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\MergeClientSoundEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */