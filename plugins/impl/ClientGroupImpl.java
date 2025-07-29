/*    */ package de.maxhenkel.voicechat.plugins.impl;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Group;
/*    */ import de.maxhenkel.voicechat.voice.common.ClientGroup;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class ClientGroupImpl
/*    */   implements Group
/*    */ {
/*    */   private final ClientGroup group;
/*    */   
/*    */   public ClientGroupImpl(ClientGroup group) {
/* 14 */     this.group = group;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 19 */     return this.group.getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasPassword() {
/* 24 */     return this.group.hasPassword();
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID getId() {
/* 29 */     return this.group.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPersistent() {
/* 34 */     return this.group.isPersistent();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isHidden() {
/* 39 */     return this.group.isHidden();
/*    */   }
/*    */ 
/*    */   
/*    */   public Group.Type getType() {
/* 44 */     return this.group.getType();
/*    */   }
/*    */   
/*    */   public ClientGroup getGroup() {
/* 48 */     return this.group;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 53 */     if (this == object) {
/* 54 */       return true;
/*    */     }
/* 56 */     if (object == null || getClass() != object.getClass()) {
/* 57 */       return false;
/*    */     }
/* 59 */     ClientGroupImpl that = (ClientGroupImpl)object;
/* 60 */     return Objects.equals(this.group.getId(), that.group.getId());
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 65 */     return (this.group != null) ? this.group.getId().hashCode() : 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\ClientGroupImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */