/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Group;
/*    */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*    */ import de.maxhenkel.voicechat.api.events.CreateGroupEvent;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class CreateGroupEventImpl
/*    */   extends GroupEventImpl
/*    */   implements CreateGroupEvent {
/*    */   public CreateGroupEventImpl(Group group, @Nullable VoicechatConnection connection) {
/* 12 */     super(group, connection);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\CreateGroupEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */