/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Group;
/*    */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*    */ import de.maxhenkel.voicechat.api.events.LeaveGroupEvent;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class LeaveGroupEventImpl
/*    */   extends GroupEventImpl
/*    */   implements LeaveGroupEvent {
/*    */   public LeaveGroupEventImpl(@Nullable Group group, VoicechatConnection connection) {
/* 12 */     super(group, connection);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\LeaveGroupEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */