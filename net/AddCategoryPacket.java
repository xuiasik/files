/*    */ package de.maxhenkel.voicechat.net;
/*    */ 
/*    */ import de.maxhenkel.voicechat.plugins.impl.VolumeCategoryImpl;
/*    */ import net.minecraft.class_2540;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ public class AddCategoryPacket
/*    */   implements Packet<AddCategoryPacket>
/*    */ {
/* 10 */   public static final class_2960 ADD_CATEGORY = new class_2960("voicechat", "add_category");
/*    */   
/*    */   private VolumeCategoryImpl category;
/*    */ 
/*    */   
/*    */   public AddCategoryPacket() {}
/*    */ 
/*    */   
/*    */   public AddCategoryPacket(VolumeCategoryImpl category) {
/* 19 */     this.category = category;
/*    */   }
/*    */   
/*    */   public VolumeCategoryImpl getCategory() {
/* 23 */     return this.category;
/*    */   }
/*    */ 
/*    */   
/*    */   public class_2960 getIdentifier() {
/* 28 */     return ADD_CATEGORY;
/*    */   }
/*    */ 
/*    */   
/*    */   public AddCategoryPacket fromBytes(class_2540 buf) {
/* 33 */     this.category = VolumeCategoryImpl.fromBytes(buf);
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(class_2540 buf) {
/* 39 */     this.category.toBytes(buf);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\AddCategoryPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */