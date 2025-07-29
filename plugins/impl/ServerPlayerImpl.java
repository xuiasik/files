/*    */ package de.maxhenkel.voicechat.plugins.impl;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.ServerLevel;
/*    */ import de.maxhenkel.voicechat.api.ServerPlayer;
/*    */ import net.minecraft.class_1657;
/*    */ import net.minecraft.class_3218;
/*    */ import net.minecraft.class_3222;
/*    */ 
/*    */ public class ServerPlayerImpl extends PlayerImpl implements ServerPlayer {
/*    */   public ServerPlayerImpl(class_3222 entity) {
/* 11 */     super((class_1657)entity);
/*    */   }
/*    */   
/*    */   public class_3222 getRealServerPlayer() {
/* 15 */     return (class_3222)this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerLevel getServerLevel() {
/* 20 */     return new ServerLevelImpl((class_3218)this.entity.field_6002);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\ServerPlayerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */