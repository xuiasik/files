/*    */ package de.maxhenkel.voicechat.intercompatibility;
/*    */ 
/*    */ import de.maxhenkel.voicechat.api.VoicechatClientApi;
/*    */ import de.maxhenkel.voicechat.plugins.impl.VoicechatClientApiImpl;
/*    */ import de.maxhenkel.voicechat.service.Service;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientVoicechatConnection;
/*    */ import java.net.SocketAddress;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.class_1297;
/*    */ import net.minecraft.class_2535;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_304;
/*    */ import net.minecraft.class_3283;
/*    */ import net.minecraft.class_3285;
/*    */ import net.minecraft.class_3675;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_4597;
/*    */ 
/*    */ 
/*    */ public abstract class ClientCompatibilityManager
/*    */ {
/* 22 */   public static ClientCompatibilityManager INSTANCE = (ClientCompatibilityManager)Service.get(ClientCompatibilityManager.class);
/*    */   
/*    */   public abstract void onRenderNamePlate(RenderNameplateEvent paramRenderNameplateEvent);
/*    */   
/*    */   public abstract void onRenderHUD(RenderHUDEvent paramRenderHUDEvent);
/*    */   
/*    */   public abstract void onKeyboardEvent(KeyboardEvent paramKeyboardEvent);
/*    */   
/*    */   public abstract void onMouseEvent(MouseEvent paramMouseEvent);
/*    */   
/*    */   public abstract void onClientTick(Runnable paramRunnable);
/*    */   
/*    */   public abstract class_3675.class_306 getBoundKeyOf(class_304 paramclass_304);
/*    */   
/*    */   public abstract void onHandleKeyBinds(Runnable paramRunnable);
/*    */   
/*    */   public abstract class_304 registerKeyBinding(class_304 paramclass_304);
/*    */   
/*    */   public abstract void emitVoiceChatConnectedEvent(ClientVoicechatConnection paramClientVoicechatConnection);
/*    */   
/*    */   public abstract void emitVoiceChatDisconnectedEvent();
/*    */   
/*    */   public abstract void onVoiceChatConnected(Consumer<ClientVoicechatConnection> paramConsumer);
/*    */   
/*    */   public abstract void onVoiceChatDisconnected(Runnable paramRunnable);
/*    */   
/*    */   public abstract void onDisconnect(Runnable paramRunnable);
/*    */   
/*    */   public abstract void onJoinWorld(Runnable paramRunnable);
/*    */   
/*    */   public abstract void onPublishServer(Consumer<Integer> paramConsumer);
/*    */   
/*    */   public abstract SocketAddress getSocketAddress(class_2535 paramclass_2535);
/*    */   
/*    */   public abstract void addResourcePackSource(class_3283 paramclass_3283, class_3285 paramclass_3285);
/*    */   
/*    */   public VoicechatClientApi getClientApi() {
/* 59 */     return (VoicechatClientApi)VoicechatClientApiImpl.INSTANCE;
/*    */   }
/*    */   
/*    */   public static interface MouseEvent {
/*    */     void onMouseEvent(long param1Long, int param1Int1, int param1Int2, int param1Int3);
/*    */   }
/*    */   
/*    */   public static interface KeyboardEvent {
/*    */     void onKeyboardEvent(long param1Long, int param1Int1, int param1Int2);
/*    */   }
/*    */   
/*    */   public static interface RenderHUDEvent {
/*    */     void render(class_4587 param1class_4587, float param1Float);
/*    */   }
/*    */   
/*    */   public static interface RenderNameplateEvent {
/*    */     void render(class_1297 param1class_1297, class_2561 param1class_2561, class_4587 param1class_4587, class_4597 param1class_4597, int param1Int);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\intercompatibility\ClientCompatibilityManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */