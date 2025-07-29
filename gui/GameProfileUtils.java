/*    */ package de.maxhenkel.voicechat.gui;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.class_1068;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_634;
/*    */ import net.minecraft.class_640;
/*    */ 
/*    */ 
/*    */ public class GameProfileUtils
/*    */ {
/* 13 */   private static final class_310 mc = class_310.method_1551();
/*    */   
/*    */   public static class_2960 getSkin(UUID uuid) {
/* 16 */     class_634 connection = mc.method_1562();
/* 17 */     if (connection == null) {
/* 18 */       return class_1068.method_4648(uuid);
/*    */     }
/* 20 */     class_640 playerInfo = connection.method_2871(uuid);
/* 21 */     if (playerInfo == null) {
/* 22 */       return class_1068.method_4648(uuid);
/*    */     }
/* 24 */     return playerInfo.method_2968();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\GameProfileUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */