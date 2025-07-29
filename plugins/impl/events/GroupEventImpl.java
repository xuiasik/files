/*    */ package de.maxhenkel.voicechat.plugins.impl.events;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Group;
/*    */ import de.maxhenkel.voicechat.api.VoicechatConnection;
/*    */ import de.maxhenkel.voicechat.api.events.GroupEvent;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class GroupEventImpl
/*    */   extends ServerEventImpl
/*    */   implements GroupEvent {
/*    */   @Nullable
/*    */   protected Group group;
/*    */   protected VoicechatConnection connection;
/*    */   
/*    */   public GroupEventImpl(@Nullable Group group, VoicechatConnection connection) {
/* 16 */     this.group = group;
/* 17 */     this.connection = connection;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Group getGroup() {
/* 23 */     return this.group;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoicechatConnection getConnection() {
/* 28 */     return this.connection;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\events\GroupEventImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */