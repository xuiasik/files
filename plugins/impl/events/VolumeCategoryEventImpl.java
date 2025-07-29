/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.VolumeCategory;
/*    */ import de.maxhenkel.voicechat.api.events.VolumeCategoryEvent;
/*    */ 
/*    */ public class VolumeCategoryEventImpl
/*    */   extends ServerEventImpl implements VolumeCategoryEvent {
/*    */   private final VolumeCategory category;
/*    */   
/*    */   public VolumeCategoryEventImpl(VolumeCategory category) {
/* 11 */     this.category = category;
/*    */   }
/*    */ 
/*    */   
/*    */   public VolumeCategory getVolumeCategory() {
/* 16 */     return this.category;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancellable() {
/* 21 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\VolumeCategoryEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */