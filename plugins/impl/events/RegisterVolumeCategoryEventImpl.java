/*   */ package de.maxhenkel.voicechat.plugins.impl.events;
/*   */ 
/*   */ import de.maxhenkel.voicechat.api.VolumeCategory;
/*   */ import de.maxhenkel.voicechat.api.events.RegisterVolumeCategoryEvent;
/*   */ 
/*   */ public class RegisterVolumeCategoryEventImpl
/*   */   extends VolumeCategoryEventImpl implements RegisterVolumeCategoryEvent {
/*   */   public RegisterVolumeCategoryEventImpl(VolumeCategory category) {
/* 9 */     super(category);
/*   */   }
/*   */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\RegisterVolumeCategoryEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */