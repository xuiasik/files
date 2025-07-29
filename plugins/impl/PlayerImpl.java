/*    */ package de.maxhenkel.voicechat.plugins.impl;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.Player;
/*    */ import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_1657;
/*    */ 
/*    */ public class PlayerImpl extends EntityImpl implements Player {
/*    */   public PlayerImpl(class_1657 entity) {
/* 10 */     super((class_1297)entity);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getPlayer() {
/* 15 */     return CommonCompatibilityManager.INSTANCE.createRawApiPlayer(getRealPlayer());
/*    */   }
/*    */   
/*    */   public class_1657 getRealPlayer() {
/* 19 */     return (class_1657)this.entity;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\PlayerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */