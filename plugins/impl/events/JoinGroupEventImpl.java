/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Group;
/*    */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*    */ import de.maxhenkel.voicechat.api.events.JoinGroupEvent;
/*    */ 
/*    */ public class JoinGroupEventImpl
/*    */   extends GroupEventImpl implements JoinGroupEvent {
/*    */   public JoinGroupEventImpl(Group group, VoicechatConnection connection) {
/* 10 */     super(group, connection);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\JoinGroupEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */