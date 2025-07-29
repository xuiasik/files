/*    */ package de.maxhenkel.voicechat.gui;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Group;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ 
/*    */ public enum GroupType {
/*  8 */   NORMAL((class_2561)new class_2588("message.voicechat.group_type.normal"), (class_2561)new class_2588("message.voicechat.group_type.normal.description"), Group.Type.NORMAL),
/*  9 */   OPEN((class_2561)new class_2588("message.voicechat.group_type.open"), (class_2561)new class_2588("message.voicechat.group_type.open.description"), Group.Type.OPEN),
/* 10 */   ISOLATED((class_2561)new class_2588("message.voicechat.group_type.isolated"), (class_2561)new class_2588("message.voicechat.group_type.isolated.description"), Group.Type.ISOLATED);
/*    */   
/*    */   private final class_2561 translation;
/*    */   private final class_2561 description;
/*    */   private final Group.Type type;
/*    */   
/*    */   GroupType(class_2561 translation, class_2561 description, Group.Type type) {
/* 17 */     this.translation = translation;
/* 18 */     this.description = description;
/* 19 */     this.type = type;
/*    */   }
/*    */   
/*    */   public class_2561 getTranslation() {
/* 23 */     return this.translation;
/*    */   }
/*    */   
/*    */   public class_2561 getDescription() {
/* 27 */     return this.description;
/*    */   }
/*    */   
/*    */   public Group.Type getType() {
/* 31 */     return this.type;
/*    */   }
/*    */   
/*    */   public static GroupType fromType(Group.Type type) {
/* 35 */     for (GroupType groupType : values()) {
/* 36 */       if (groupType.getType() == type) {
/* 37 */         return groupType;
/*    */       }
/*    */     } 
/* 40 */     return NORMAL;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\GroupType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */