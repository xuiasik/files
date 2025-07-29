/*     */ package de.maxhenkel.voicechat.command;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import de.maxhenkel.voicechat.Voicechat;
/*     */ import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
/*     */ import de.maxhenkel.voicechat.permission.Permission;
/*     */ import de.maxhenkel.voicechat.permission.PermissionManager;
/*     */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*     */ import de.maxhenkel.voicechat.voice.server.ClientConnection;
/*     */ import de.maxhenkel.voicechat.voice.server.Group;
/*     */ import de.maxhenkel.voicechat.voice.server.PingManager;
/*     */ import de.maxhenkel.voicechat.voice.server.Server;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.class_124;
/*     */ import net.minecraft.class_156;
/*     */ import net.minecraft.class_2168;
/*     */ import net.minecraft.class_2170;
/*     */ import net.minecraft.class_2186;
/*     */ import net.minecraft.class_2558;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2564;
/*     */ import net.minecraft.class_2568;
/*     */ import net.minecraft.class_2583;
/*     */ import net.minecraft.class_2585;
/*     */ import net.minecraft.class_2588;
/*     */ import net.minecraft.class_3222;
/*     */ import net.minecraft.class_5242;
/*     */ 
/*     */ public class VoicechatCommands {
/*     */   public static void register(CommandDispatcher<class_2168> dispatcher) {
/*  39 */     LiteralArgumentBuilder<class_2168> literalBuilder = class_2170.method_9247("voicechat");
/*     */     
/*  41 */     literalBuilder.executes(commandSource -> help(dispatcher, commandSource));
/*  42 */     literalBuilder.then(class_2170.method_9247("help").executes(commandSource -> help(dispatcher, commandSource)));
/*     */     
/*  44 */     literalBuilder.then(((LiteralArgumentBuilder)class_2170.method_9247("test").requires(commandSource -> checkPermission(commandSource, PermissionManager.INSTANCE.ADMIN_PERMISSION))).then(class_2170.method_9244("target", (ArgumentType)class_2186.method_9305()).executes(commandSource -> {
/*     */               if (checkNoVoicechat(commandSource)) {
/*     */                 return 0;
/*     */               }
/*     */               
/*     */               class_3222 player = class_2186.method_9315(commandSource, "target");
/*     */               
/*     */               Server server = Voicechat.SERVER.getServer();
/*     */               
/*     */               if (server == null) {
/*     */                 ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.voice_chat_unavailable"), false);
/*     */                 return 1;
/*     */               } 
/*     */               if (!Voicechat.SERVER.isCompatible(player)) {
/*     */                 ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.player_no_voicechat", new Object[] { player.method_5476(), CommonCompatibilityManager.INSTANCE.getModName() }), false);
/*     */                 return 1;
/*     */               } 
/*     */               ClientConnection clientConnection = (ClientConnection)server.getConnections().get(player.method_5667());
/*     */               if (clientConnection == null) {
/*     */                 ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.client_not_connected"), false);
/*     */                 return 1;
/*     */               } 
/*     */               try {
/*     */                 ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.sending_ping"), false);
/*     */                 server.getPingManager().sendPing(clientConnection, 500L, 10, new PingManager.PingListener()
/*     */                     {
/*     */                       public void onPong(int attempts, long pingMilliseconds)
/*     */                       {
/*  72 */                         if (attempts <= 1) {
/*  73 */                           ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.ping_received", new Object[] { Long.valueOf(pingMilliseconds) }), false);
/*     */                         } else {
/*  75 */                           ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.ping_received_attempt", new Object[] { Long.valueOf(pingMilliseconds), Integer.valueOf(attempts) }), false);
/*     */                         } 
/*     */                       }
/*     */ 
/*     */                       
/*     */                       public void onFailedAttempt(int attempts) {
/*  81 */                         ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.ping_retry"), false);
/*     */                       }
/*     */ 
/*     */                       
/*     */                       public void onTimeout(int attempts) {
/*  86 */                         ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.ping_timed_out", new Object[] { Integer.valueOf(attempts) }), false);
/*     */                       }
/*     */                     });
/*     */                 ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.ping_sent_waiting"), false);
/*  90 */               } catch (Exception e) {
/*     */                 ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.failed_to_send_ping", new Object[] { e.getMessage() }), false);
/*     */                 
/*     */                 Voicechat.LOGGER.warn("Failed to send ping", new Object[] { e });
/*     */                 return 1;
/*     */               } 
/*     */               return 1;
/*     */             })));
/*  98 */     literalBuilder.then(class_2170.method_9247("invite").then(class_2170.method_9244("target", (ArgumentType)class_2186.method_9305()).executes(commandSource -> {
/*     */               if (checkNoVoicechat(commandSource)) {
/*     */                 return 0;
/*     */               }
/*     */               
/*     */               class_3222 source = ((class_2168)commandSource.getSource()).method_9207();
/*     */               
/*     */               Server server = Voicechat.SERVER.getServer();
/*     */               
/*     */               if (server == null) {
/*     */                 ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.voice_chat_unavailable"), false);
/*     */                 
/*     */                 return 1;
/*     */               } 
/*     */               
/*     */               PlayerState state = server.getPlayerStateManager().getState(source.method_5667());
/*     */               
/*     */               if (state == null || !state.hasGroup()) {
/*     */                 ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.not_in_group"), false);
/*     */                 
/*     */                 return 1;
/*     */               } 
/*     */               
/*     */               class_3222 player = class_2186.method_9315(commandSource, "target");
/*     */               
/*     */               Group group = server.getGroupManager().getGroup(state.getGroup());
/*     */               
/*     */               if (group == null) {
/*     */                 return 1;
/*     */               }
/*     */               if (!Voicechat.SERVER.isCompatible(player)) {
/*     */                 ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.player_no_voicechat", new Object[] { player.method_5476(), CommonCompatibilityManager.INSTANCE.getModName() }), false);
/*     */                 return 1;
/*     */               } 
/*     */               String passwordSuffix = (group.getPassword() == null) ? "" : (" \"" + group.getPassword() + "\"");
/*     */               player.method_9203((class_2561)new class_2588("message.voicechat.invite", new Object[] { source.method_5476(), (new class_2585(group.getName())).method_27692(class_124.field_1080), class_2564.method_10885((class_2561)(new class_2588("message.voicechat.accept_invite")).method_27694(())).method_27692(class_124.field_1060) }), class_156.field_25140);
/*     */               ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.invite_successful", new Object[] { player.method_5476() }), false);
/*     */               return 1;
/*     */             })));
/* 137 */     literalBuilder.then(class_2170.method_9247("join").then(class_2170.method_9244("group_id", (ArgumentType)class_5242.method_27643()).executes(commandSource -> {
/*     */               if (checkNoVoicechat(commandSource)) {
/*     */                 return 0;
/*     */               }
/*     */               
/*     */               UUID groupID = class_5242.method_27645(commandSource, "group_id");
/*     */               return joinGroupById((class_2168)commandSource.getSource(), groupID, null);
/*     */             })));
/* 145 */     literalBuilder.then(class_2170.method_9247("join").then(class_2170.method_9244("group_id", (ArgumentType)class_5242.method_27643()).then(class_2170.method_9244("password", (ArgumentType)StringArgumentType.string()).executes(commandSource -> {
/*     */                 if (checkNoVoicechat(commandSource)) {
/*     */                   return 0;
/*     */                 }
/*     */                 
/*     */                 UUID groupID = class_5242.method_27645(commandSource, "group_id");
/*     */                 String password = StringArgumentType.getString(commandSource, "password");
/*     */                 return joinGroupById((class_2168)commandSource.getSource(), groupID, password.isEmpty() ? null : password);
/*     */               }))));
/* 154 */     literalBuilder.then(class_2170.method_9247("join").then(class_2170.method_9244("group_name", (ArgumentType)StringArgumentType.string()).suggests(GroupNameSuggestionProvider.INSTANCE).executes(commandSource -> {
/*     */               if (checkNoVoicechat(commandSource)) {
/*     */                 return 0;
/*     */               }
/*     */               
/*     */               String groupName = StringArgumentType.getString(commandSource, "group_name");
/*     */               return joinGroupByName((class_2168)commandSource.getSource(), groupName, null);
/*     */             })));
/* 162 */     literalBuilder.then(class_2170.method_9247("join").then(class_2170.method_9244("group_name", (ArgumentType)StringArgumentType.string()).suggests(GroupNameSuggestionProvider.INSTANCE).then(class_2170.method_9244("password", (ArgumentType)StringArgumentType.string()).executes(commandSource -> {
/*     */                 if (checkNoVoicechat(commandSource)) {
/*     */                   return 0;
/*     */                 }
/*     */                 
/*     */                 String groupName = StringArgumentType.getString(commandSource, "group_name");
/*     */                 String password = StringArgumentType.getString(commandSource, "password");
/*     */                 return joinGroupByName((class_2168)commandSource.getSource(), groupName, password.isEmpty() ? null : password);
/*     */               }))));
/* 171 */     literalBuilder.then(class_2170.method_9247("leave").executes(commandSource -> {
/*     */             if (checkNoVoicechat(commandSource)) {
/*     */               return 0;
/*     */             }
/*     */             
/*     */             if (!((Boolean)Voicechat.SERVER_CONFIG.groupsEnabled.get()).booleanValue()) {
/*     */               ((class_2168)commandSource.getSource()).method_9213((class_2561)new class_2588("message.voicechat.groups_disabled"));
/*     */               
/*     */               return 1;
/*     */             } 
/*     */             
/*     */             Server server = Voicechat.SERVER.getServer();
/*     */             
/*     */             if (server == null) {
/*     */               ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.voice_chat_unavailable"), false);
/*     */               return 1;
/*     */             } 
/*     */             class_3222 source = ((class_2168)commandSource.getSource()).method_9207();
/*     */             PlayerState state = server.getPlayerStateManager().getState(source.method_5667());
/*     */             if (state == null || !state.hasGroup()) {
/*     */               ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.not_in_group"), false);
/*     */               return 1;
/*     */             } 
/*     */             server.getGroupManager().leaveGroup(source);
/*     */             ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2588("message.voicechat.leave_successful"), false);
/*     */             return 1;
/*     */           }));
/* 198 */     dispatcher.register(literalBuilder);
/*     */   }
/*     */   public static final String VOICECHAT_COMMAND = "voicechat";
/*     */   private static Server joinGroup(class_2168 source) throws CommandSyntaxException {
/* 202 */     if (!((Boolean)Voicechat.SERVER_CONFIG.groupsEnabled.get()).booleanValue()) {
/* 203 */       source.method_9213((class_2561)new class_2588("message.voicechat.groups_disabled"));
/* 204 */       return null;
/*     */     } 
/*     */     
/* 207 */     Server server = Voicechat.SERVER.getServer();
/* 208 */     if (server == null) {
/* 209 */       source.method_9226((class_2561)new class_2588("message.voicechat.voice_chat_unavailable"), false);
/* 210 */       return null;
/*     */     } 
/* 212 */     class_3222 player = source.method_9207();
/*     */     
/* 214 */     if (!PermissionManager.INSTANCE.GROUPS_PERMISSION.hasPermission(player)) {
/* 215 */       source.method_9226((class_2561)new class_2588("message.voicechat.no_group_permission"), false);
/* 216 */       return null;
/*     */     } 
/*     */     
/* 219 */     return server;
/*     */   }
/*     */   
/*     */   private static int joinGroupByName(class_2168 source, String groupName, @Nullable String password) throws CommandSyntaxException {
/* 223 */     Server server = joinGroup(source);
/* 224 */     if (server == null) {
/* 225 */       return 1;
/*     */     }
/*     */     
/* 228 */     List<Group> groups = (List<Group>)server.getGroupManager().getGroups().values().stream().filter(group -> group.getName().equals(groupName)).collect(Collectors.toList());
/*     */     
/* 230 */     if (groups.isEmpty()) {
/* 231 */       source.method_9213((class_2561)new class_2588("message.voicechat.group_does_not_exist"));
/* 232 */       return 1;
/*     */     } 
/*     */     
/* 235 */     if (groups.size() > 1) {
/* 236 */       source.method_9213((class_2561)new class_2588("message.voicechat.group_name_not_unique"));
/* 237 */       return 1;
/*     */     } 
/*     */     
/* 240 */     return joinGroup(source, server, ((Group)groups.get(0)).getId(), password);
/*     */   }
/*     */   
/*     */   private static int joinGroupById(class_2168 source, UUID groupID, @Nullable String password) throws CommandSyntaxException {
/* 244 */     Server server = joinGroup(source);
/* 245 */     if (server == null) {
/* 246 */       return 1;
/*     */     }
/* 248 */     return joinGroup(source, server, groupID, password);
/*     */   }
/*     */   
/*     */   private static int joinGroup(class_2168 source, Server server, UUID groupID, @Nullable String password) throws CommandSyntaxException {
/* 252 */     Group group = server.getGroupManager().getGroup(groupID);
/*     */     
/* 254 */     if (group == null) {
/* 255 */       source.method_9213((class_2561)new class_2588("message.voicechat.group_does_not_exist"));
/* 256 */       return 1;
/*     */     } 
/*     */     
/* 259 */     server.getGroupManager().joinGroup(group, source.method_9207(), password);
/* 260 */     source.method_9226((class_2561)new class_2588("message.voicechat.join_successful", new Object[] { (new class_2585(group.getName())).method_27692(class_124.field_1080) }), false);
/* 261 */     return 1;
/*     */   }
/*     */   
/*     */   private static int help(CommandDispatcher<class_2168> dispatcher, CommandContext<class_2168> commandSource) {
/* 265 */     if (checkNoVoicechat(commandSource)) {
/* 266 */       return 0;
/*     */     }
/* 268 */     CommandNode<class_2168> voicechatCommand = dispatcher.getRoot().getChild("voicechat");
/* 269 */     Map<CommandNode<class_2168>, String> map = dispatcher.getSmartUsage(voicechatCommand, commandSource.getSource());
/* 270 */     for (Map.Entry<CommandNode<class_2168>, String> entry : map.entrySet()) {
/* 271 */       ((class_2168)commandSource.getSource()).method_9226((class_2561)new class_2585("/voicechat " + (String)entry.getValue()), false);
/*     */     }
/* 273 */     return map.size();
/*     */   }
/*     */   
/*     */   private static boolean checkNoVoicechat(CommandContext<class_2168> commandSource) {
/*     */     try {
/* 278 */       class_3222 player = ((class_2168)commandSource.getSource()).method_9207();
/* 279 */       if (Voicechat.SERVER.isCompatible(player)) {
/* 280 */         return false;
/*     */       }
/* 282 */       ((class_2168)commandSource.getSource()).method_9213((class_2561)new class_2585(String.format((String)Voicechat.TRANSLATIONS.voicechatNeededForCommandMessage.get(), new Object[] { CommonCompatibilityManager.INSTANCE.getModName() })));
/* 283 */       return true;
/* 284 */     } catch (Exception e) {
/* 285 */       ((class_2168)commandSource.getSource()).method_9213((class_2561)new class_2585((String)Voicechat.TRANSLATIONS.playerCommandMessage.get()));
/* 286 */       return true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean checkPermission(class_2168 stack, Permission permission) {
/*     */     try {
/* 292 */       return permission.hasPermission(stack.method_9207());
/* 293 */     } catch (CommandSyntaxException e) {
/* 294 */       return stack.method_9259(stack.method_9211().method_3798());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\command\VoicechatCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */