/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Group;
/*    */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*    */ import de.maxhenkel.voicechat.api.events.RemoveGroupEvent;
/*    */ 
/*    */ public class RemoveGroupEventImpl extends GroupEventImpl implements RemoveGroupEvent {
/*    */   public RemoveGroupEventImpl(Group group) {
/*  9 */     super(group, (VoicechatConnection)null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancellable() {
/* 14 */     return (super.isCancellable() && this.group.isPersistent());
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\RemoveGroupEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */