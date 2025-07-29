/*     */ package de.maxhenkel.voicechat.gui.volume;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import de.maxhenkel.voicechat.VoicechatClient;
/*     */ import de.maxhenkel.voicechat.gui.widgets.ListScreenListBase;
/*     */ import de.maxhenkel.voicechat.plugins.impl.VolumeCategoryImpl;
/*     */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*     */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.class_156;
/*     */ import net.minecraft.class_310;
/*     */ 
/*     */ public class AdjustVolumeList extends ListScreenListBase<VolumeEntry> {
/*     */   protected AdjustVolumesScreen screen;
/*     */   
/*     */   public AdjustVolumeList(int width, int height, int top, int size, AdjustVolumesScreen screen) {
/*  21 */     super(width, height, top, size);
/*  22 */     this.screen = screen;
/*  23 */     this.entries = Lists.newArrayList();
/*  24 */     this.filter = "";
/*  25 */     method_31322(false);
/*  26 */     method_31323(false);
/*  27 */     updateEntryList();
/*     */   }
/*     */   protected final List<VolumeEntry> entries; protected String filter;
/*     */   public static void update() {
/*  31 */     if ((class_310.method_1551()).field_1755 instanceof AdjustVolumesScreen) {
/*  32 */       ((AdjustVolumesScreen)(class_310.method_1551()).field_1755).volumeList.updateEntryList();
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateEntryList() {
/*  37 */     Collection<PlayerState> onlinePlayers = ClientManager.getPlayerStateManager().getPlayerStates(false);
/*  38 */     this.entries.clear();
/*     */     
/*  40 */     for (VolumeCategoryImpl category : ClientManager.getCategoryManager().getCategories()) {
/*  41 */       this.entries.add(new CategoryVolumeEntry(category, this.screen));
/*     */     }
/*     */     
/*  44 */     for (PlayerState state : onlinePlayers) {
/*  45 */       this.entries.add(new PlayerVolumeEntry(state, this.screen));
/*     */     }
/*     */     
/*  48 */     if (((Boolean)VoicechatClient.CLIENT_CONFIG.offlinePlayerVolumeAdjustment.get()).booleanValue()) {
/*  49 */       addOfflinePlayers(onlinePlayers);
/*     */     }
/*     */     
/*  52 */     updateFilter();
/*     */   }
/*     */   
/*     */   private void addOfflinePlayers(Collection<PlayerState> onlinePlayers) {
/*  56 */     for (UUID uuid : VoicechatClient.VOLUME_CONFIG.getPlayerVolumes().keySet()) {
/*  57 */       if (uuid.equals(class_156.field_25140)) {
/*     */         continue;
/*     */       }
/*  60 */       if (onlinePlayers.stream().anyMatch(state -> uuid.equals(state.getUuid()))) {
/*     */         continue;
/*     */       }
/*     */       
/*  64 */       String name = VoicechatClient.USERNAME_CACHE.getUsername(uuid);
/*     */       
/*  66 */       if (name == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  70 */       this.entries.add(new PlayerVolumeEntry(new PlayerState(uuid, name, false, true), this.screen));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateFilter() {
/*  75 */     method_25339();
/*  76 */     List<VolumeEntry> filteredEntries = new ArrayList<>(this.entries);
/*  77 */     if (!this.filter.isEmpty())
/*  78 */       filteredEntries.removeIf(volumeEntry -> {
/*     */             if (volumeEntry instanceof PlayerVolumeEntry) {
/*     */               PlayerVolumeEntry playerVolumeEntry = (PlayerVolumeEntry)volumeEntry;
/*  81 */               return (playerVolumeEntry.getState() == null || !playerVolumeEntry.getState().getName().toLowerCase(Locale.ROOT).contains(this.filter));
/*     */             } 
/*     */             if (volumeEntry instanceof CategoryVolumeEntry) {
/*     */               CategoryVolumeEntry categoryVolumeEntry = (CategoryVolumeEntry)volumeEntry;
/*     */               return !categoryVolumeEntry.getCategory().getName().toLowerCase(Locale.ROOT).contains(this.filter);
/*     */             } 
/*     */             return true;
/*     */           }); 
/*  89 */     filteredEntries.sort((e1, e2) -> !e1.getClass().equals(e2.getClass()) ? ((e1 instanceof PlayerVolumeEntry) ? 1 : -1) : volumeEntryToString(e1).compareToIgnoreCase(volumeEntryToString(e2)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     if (this.filter.isEmpty()) {
/* 101 */       filteredEntries.add(0, new PlayerVolumeEntry(null, this.screen));
/*     */     }
/* 103 */     method_25314(filteredEntries);
/*     */   }
/*     */   
/*     */   private String volumeEntryToString(VolumeEntry entry) {
/* 107 */     if (entry instanceof PlayerVolumeEntry) {
/* 108 */       PlayerVolumeEntry playerVolumeEntry = (PlayerVolumeEntry)entry;
/* 109 */       return (playerVolumeEntry.getState() == null) ? "" : playerVolumeEntry.getState().getName();
/* 110 */     }  if (entry instanceof CategoryVolumeEntry) {
/* 111 */       CategoryVolumeEntry categoryVolumeEntry = (CategoryVolumeEntry)entry;
/* 112 */       return categoryVolumeEntry.getCategory().getName();
/*     */     } 
/* 114 */     return "";
/*     */   }
/*     */   
/*     */   public void setFilter(String filter) {
/* 118 */     this.filter = filter;
/* 119 */     updateFilter();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 123 */     return method_25396().isEmpty();
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\volume\AdjustVolumeList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */