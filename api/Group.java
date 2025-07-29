/*    */ package de.maxhenkel.voicechat.api;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Group
/*    */ {
/*    */   String getName();
/*    */   
/*    */   boolean hasPassword();
/*    */   
/*    */   UUID getId();
/*    */   
/*    */   boolean isPersistent();
/*    */   
/*    */   boolean isHidden();
/*    */   
/*    */   Type getType();
/*    */   
/*    */   public static interface Builder
/*    */   {
/*    */     Builder setId(@Nullable UUID param1UUID);
/*    */     
/*    */     Builder setName(String param1String);
/*    */     
/*    */     Builder setPassword(@Nullable String param1String);
/*    */     
/*    */     Builder setPersistent(boolean param1Boolean);
/*    */     
/*    */     Builder setHidden(boolean param1Boolean);
/*    */     
/*    */     Builder setType(Group.Type param1Type);
/*    */     
/*    */     Group build();
/*    */   }
/*    */   
/*    */   public static interface Type
/*    */   {
/* 46 */     public static final Type NORMAL = new Type()
/*    */       {
/*    */       
/*    */       };
/*    */ 
/*    */     
/* 52 */     public static final Type OPEN = new Type()
/*    */       {
/*    */       
/*    */       };
/*    */ 
/*    */     
/* 58 */     public static final Type ISOLATED = new Type() {
/*    */       
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\Group.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */