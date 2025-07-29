/*    */ package de.maxhenkel.voicechat.gui.group;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
/*    */ import de.maxhenkel.voicechat.gui.widgets.ListScreenListBase;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*    */ import de.maxhenkel.voicechat.voice.common.ClientGroup;
/*    */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_437;
/*    */ 
/*    */ public class JoinGroupList extends ListScreenListBase<JoinGroupEntry> {
/*    */   protected final ListScreenBase parent;
/*    */   
/*    */   public JoinGroupList(ListScreenBase parent, int width, int height, int top, int size) {
/* 21 */     super(width, height, top, size);
/* 22 */     this.parent = parent;
/* 23 */     method_31322(false);
/* 24 */     method_31323(false);
/* 25 */     updateGroups();
/*    */   }
/*    */   
/*    */   private void updateGroups() {
/* 29 */     Map<UUID, JoinGroupEntry.Group> groups = (Map<UUID, JoinGroupEntry.Group>)ClientManager.getGroupManager().getGroups().stream().filter(clientGroup -> !clientGroup.isHidden()).collect(Collectors.toMap(ClientGroup::getId, Group::new));
/* 30 */     Collection<PlayerState> playerStates = ClientManager.getPlayerStateManager().getPlayerStates(true);
/*    */     
/* 32 */     for (PlayerState state : playerStates) {
/* 33 */       if (!state.hasGroup()) {
/*    */         continue;
/*    */       }
/* 36 */       JoinGroupEntry.Group group = groups.get(state.getGroup());
/* 37 */       if (group == null) {
/*    */         continue;
/*    */       }
/* 40 */       group.getMembers().add(state);
/*    */     } 
/*    */     
/* 43 */     groups.values().forEach(group -> group.getMembers().sort(Comparator.comparing(PlayerState::getName)));
/*    */     
/* 45 */     method_25314((Collection)groups.values().stream().map(group -> new JoinGroupEntry(this.parent, group)).sorted(Comparator.comparing(o -> o.getGroup().getGroup().getName())).collect(Collectors.toList()));
/*    */   }
/*    */   
/*    */   public static void update() {
/* 49 */     class_437 screen = (class_310.method_1551()).field_1755;
/* 50 */     if (screen instanceof JoinGroupScreen) {
/* 51 */       JoinGroupScreen joinGroupScreen = (JoinGroupScreen)screen;
/* 52 */       joinGroupScreen.groupList.updateGroups();
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 57 */     return method_25396().isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\group\JoinGroupList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */