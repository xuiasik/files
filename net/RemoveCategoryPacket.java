/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ public class RemoveCategoryPacket
/*    */   implements Packet<RemoveCategoryPacket>
/*    */ {
/*  9 */   public static final class_2960 REMOVE_CATEGORY = new class_2960("voicechat", "remove_category");
/*    */   
/*    */   private String categoryId;
/*    */ 
/*    */   
/*    */   public RemoveCategoryPacket() {}
/*    */ 
/*    */   
/*    */   public RemoveCategoryPacket(String categoryId) {
/* 18 */     this.categoryId = categoryId;
/*    */   }
/*    */   
/*    */   public String getCategoryId() {
/* 22 */     return this.categoryId;
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2960 getIdentifier() {
/* 27 */     return REMOVE_CATEGORY;
/*    */   }
/*    */ 
/*    */   
/*    */   public RemoveCategoryPacket fromBytes(class_2540 buf) {
/* 32 */     this.categoryId = buf.method_10800(16);
/* 33 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(class_2540 buf) {
/* 38 */     buf.method_10788(this.categoryId, 16);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\RemoveCategoryPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */