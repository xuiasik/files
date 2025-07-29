/*    */ package de.maxhenkel.voicechat.gui.group;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
/*    */ import de.maxhenkel.voicechat.gui.widgets.ListScreenListBase;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*    */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*    */ import java.util.Comparator;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_350;
/*    */ import net.minecraft.class_437;
/*    */ 
/*    */ public class GroupList
/*    */   extends ListScreenListBase<GroupEntry> {
/*    */   protected final ListScreenBase parent;
/*    */   
/*    */   public GroupList(ListScreenBase parent, int width, int height, int top, int size) {
/* 20 */     super(width, height, top, size);
/* 21 */     this.parent = parent;
/* 22 */     method_31322(false);
/* 23 */     method_31323(false);
/* 24 */     updateMembers();
/*    */   }
/*    */   
/*    */   public void updateMembers() {
/* 28 */     List<PlayerState> playerStates = ClientManager.getPlayerStateManager().getPlayerStates(true);
/* 29 */     UUID group = ClientManager.getPlayerStateManager().getGroupID();
/* 30 */     if (group == null) {
/* 31 */       method_25339();
/* 32 */       this.field_22740.method_1507(null);
/*    */       return;
/*    */     } 
/* 35 */     boolean changed = false;
/* 36 */     List<GroupEntry> toRemove = new LinkedList<>();
/* 37 */     for (GroupEntry entry : method_25396()) {
/* 38 */       PlayerState state = ClientManager.getPlayerStateManager().getState(entry.getState().getUuid());
/* 39 */       if (state == null) {
/* 40 */         toRemove.add(entry);
/* 41 */         changed = true;
/*    */         continue;
/*    */       } 
/* 44 */       entry.setState(state);
/* 45 */       if (!isInGroup(state, group)) {
/* 46 */         toRemove.add(entry);
/* 47 */         changed = true;
/*    */       } 
/*    */     } 
/* 50 */     for (GroupEntry entry : toRemove) {
/* 51 */       method_25330((class_350.class_351)entry);
/*    */     }
/* 53 */     for (PlayerState state : playerStates) {
/* 54 */       if (isInGroup(state, group) && 
/* 55 */         method_25396().stream().noneMatch(groupEntry -> groupEntry.getState().getUuid().equals(state.getUuid()))) {
/* 56 */         method_25321((class_350.class_351)new GroupEntry(this.parent, state));
/* 57 */         changed = true;
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 62 */     if (changed) {
/* 63 */       method_25396().sort(Comparator.comparing(o -> o.getState().getName()));
/*    */     }
/*    */   }
/*    */   
/*    */   public static void update() {
/* 68 */     class_437 screen = (class_310.method_1551()).field_1755;
/* 69 */     if (screen instanceof GroupScreen) {
/* 70 */       GroupScreen groupScreen = (GroupScreen)screen;
/* 71 */       groupScreen.groupList.updateMembers();
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean isInGroup(PlayerState state, UUID group) {
/* 76 */     return (state.hasGroup() && state.getGroup().equals(group));
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\group\GroupList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */