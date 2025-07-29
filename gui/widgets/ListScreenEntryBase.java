/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.class_364;
/*    */ import net.minecraft.class_4265;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ListScreenEntryBase<T extends class_4265.class_4266<T>>
/*    */   extends class_4265.class_4266<T>
/*    */ {
/* 14 */   protected final List<class_364> children = Lists.newArrayList();
/*    */ 
/*    */ 
/*    */   
/*    */   public List<? extends class_364> method_25396() {
/* 19 */     return this.children;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\ListScreenEntryBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */