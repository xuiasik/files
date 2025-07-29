/*    */ package de.maxhenkel.voicechat.plugins;
/*    */ 
/*    */ import de.maxhenkel.voicechat.plugins.impl.VolumeCategoryImpl;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CategoryManager
/*    */ {
/* 15 */   protected final Map<String, VolumeCategoryImpl> categories = new ConcurrentHashMap<>();
/*    */ 
/*    */   
/*    */   public void addCategory(VolumeCategoryImpl category) {
/* 19 */     this.categories.put(category.getId(), category);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public VolumeCategoryImpl removeCategory(String categoryId) {
/* 24 */     return this.categories.remove(categoryId);
/*    */   }
/*    */   
/*    */   public Collection<VolumeCategoryImpl> getCategories() {
/* 28 */     return this.categories.values();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\CategoryManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */