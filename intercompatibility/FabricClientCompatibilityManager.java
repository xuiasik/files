/*     */ package de.maxhenkel.voicechat.intercompatibility;
/*     */ import de.maxhenkel.voicechat.events.ClientVoiceChatEvents;
/*     */ import de.maxhenkel.voicechat.events.InputEvents;
/*     */ import de.maxhenkel.voicechat.events.PublishServerEvents;
/*     */ import de.maxhenkel.voicechat.events.RenderEvents;
/*     */ import de.maxhenkel.voicechat.mixin.ConnectionAccessor;
/*     */ import de.maxhenkel.voicechat.resourcepacks.IPackRepository;
/*     */ import de.maxhenkel.voicechat.voice.client.ClientVoicechatConnection;
/*     */ import java.util.function.Consumer;
/*     */ import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
/*     */ import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
/*     */ import net.minecraft.class_2535;
/*     */ import net.minecraft.class_304;
/*     */ import net.minecraft.class_310;
/*     */ import net.minecraft.class_3283;
/*     */ import net.minecraft.class_3285;
/*     */ import net.minecraft.class_3675;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_634;
/*     */ 
/*     */ public class FabricClientCompatibilityManager extends ClientCompatibilityManager {
/*  22 */   private static final class_310 mc = class_310.method_1551();
/*     */ 
/*     */   
/*     */   public void onRenderNamePlate(ClientCompatibilityManager.RenderNameplateEvent onRenderNamePlate) {
/*  26 */     RenderEvents.RENDER_NAMEPLATE.register(onRenderNamePlate);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRenderHUD(ClientCompatibilityManager.RenderHUDEvent onRenderHUD) {
/*  31 */     RenderEvents.RENDER_HUD.register(poseStack -> onRenderHUD.render(poseStack, mc.method_1488()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onKeyboardEvent(ClientCompatibilityManager.KeyboardEvent onKeyboardEvent) {
/*  36 */     InputEvents.KEYBOARD_KEY.register(onKeyboardEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseEvent(ClientCompatibilityManager.MouseEvent onMouseEvent) {
/*  41 */     InputEvents.MOUSE_KEY.register(onMouseEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClientTick(Runnable onClientTick) {
/*  46 */     ClientTickEvents.START_CLIENT_TICK.register(client -> onClientTick.run());
/*     */   }
/*     */ 
/*     */   
/*     */   public class_3675.class_306 getBoundKeyOf(class_304 keyBinding) {
/*  51 */     return KeyBindingHelper.getBoundKeyOf(keyBinding);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onHandleKeyBinds(Runnable onHandleKeyBinds) {
/*  56 */     InputEvents.HANDLE_KEYBINDS.register(onHandleKeyBinds);
/*     */   }
/*     */ 
/*     */   
/*     */   public class_304 registerKeyBinding(class_304 keyBinding) {
/*  61 */     return KeyBindingHelper.registerKeyBinding(keyBinding);
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitVoiceChatConnectedEvent(ClientVoicechatConnection client) {
/*  66 */     ((Consumer<ClientVoicechatConnection>)ClientVoiceChatEvents.VOICECHAT_CONNECTED.invoker()).accept(client);
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitVoiceChatDisconnectedEvent() {
/*  71 */     ((Runnable)ClientVoiceChatEvents.VOICECHAT_DISCONNECTED.invoker()).run();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVoiceChatConnected(Consumer<ClientVoicechatConnection> onVoiceChatConnected) {
/*  76 */     ClientVoiceChatEvents.VOICECHAT_CONNECTED.register(onVoiceChatConnected);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVoiceChatDisconnected(Runnable onVoiceChatDisconnected) {
/*  81 */     ClientVoiceChatEvents.VOICECHAT_DISCONNECTED.register(onVoiceChatDisconnected);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisconnect(Runnable onDisconnect) {
/*  86 */     ClientWorldEvents.DISCONNECT.register(onDisconnect);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onJoinWorld(Runnable onJoinWorld) {
/*  91 */     ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> onJoinWorld.run());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPublishServer(Consumer<Integer> onPublishServer) {
/*  96 */     PublishServerEvents.SERVER_PUBLISHED.register(onPublishServer);
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getSocketAddress(class_2535 connection) {
/* 101 */     return ((ConnectionAccessor)connection).getChannel().remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addResourcePackSource(class_3283 packRepository, class_3285 repositorySource) {
/* 106 */     IPackRepository repository = (IPackRepository)packRepository;
/* 107 */     repository.addSource(repositorySource);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\intercompatibility\FabricClientCompatibilityManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */