/*    */ package de.maxhenkel.voicechat.command;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import de.maxhenkel.voicechat.Voicechat;
/*    */ import de.maxhenkel.voicechat.voice.server.Group;
/*    */ import de.maxhenkel.voicechat.voice.server.Server;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.class_2168;
/*    */ 
/*    */ public class GroupNameSuggestionProvider
/*    */   implements SuggestionProvider<class_2168>
/*    */ {
/* 16 */   public static final GroupNameSuggestionProvider INSTANCE = new GroupNameSuggestionProvider();
/*    */ 
/*    */   
/*    */   public CompletableFuture<Suggestions> getSuggestions(CommandContext<class_2168> context, SuggestionsBuilder builder) {
/* 20 */     Server server = Voicechat.SERVER.getServer();
/* 21 */     if (server == null) {
/* 22 */       return builder.buildFuture();
/*    */     }
/* 24 */     server.getGroupManager().getGroups().values().stream().map(Group::getName).distinct().map(s -> s.contains(" ") ? String.format("\"%s\"", new Object[] {
/*    */             
/*    */             s
/*    */ 
/*    */           
/* 29 */           }) : s).forEach(builder::suggest);
/* 30 */     return builder.buildFuture();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\command\GroupNameSuggestionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */