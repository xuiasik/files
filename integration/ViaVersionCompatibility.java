/*    */ package de.maxhenkel.voicechat.integration;
/*    */ 
/*    */ import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
/*    */ import de.maxhenkel.voicechat.net.FabricNetManager;
/*    */ import java.util.Set;
/*    */ import net.fabricmc.loader.api.FabricLoader;
/*    */ import net.minecraft.class_2960;
/*    */ 
/*    */ 
/*    */ public class ViaVersionCompatibility
/*    */ {
/*    */   private static final String OLD_VOICECHAT_PREFIX = "vc";
/*    */   
/*    */   public static void register() {
/*    */     try {
/* 18 */       if (FabricLoader.getInstance().isModLoaded("viaversion")) {
/* 19 */         registerMappings();
/* 20 */         Voicechat.LOGGER.info("Successfully registered ViaVersion mappings", new Object[0]);
/*    */       } 
/* 22 */     } catch (Throwable t) {
/* 23 */       Voicechat.LOGGER.error("Failed to register ViaVersion mappings", new Object[] { t });
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void registerMappings() {
/* 28 */     Set<class_2960> packets = ((FabricNetManager)CommonCompatibilityManager.INSTANCE.getNetManager()).getPackets();
/* 29 */     for (class_2960 id : packets) {
/* 30 */       Protocol1_13To1_12_2.MAPPINGS.getChannelMappings().put(String.format("%s:%s", new Object[] { "vc", id.method_12832() }), id.toString());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\integration\ViaVersionCompatibility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */