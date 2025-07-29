/*    */ package de.maxhenkel.voicechat.gui.audiodevice;
/*    */ 
/*    */ import de.maxhenkel.voicechat.configbuilder.entry.ConfigEntry;
/*    */ import de.maxhenkel.voicechat.gui.widgets.ListScreenListBase;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientManager;
/*    */ import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
/*    */ import de.maxhenkel.voicechat.voice.client.SoundManager;
/*    */ import java.util.Collection;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_1109;
/*    */ import net.minecraft.class_1113;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2585;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_3417;
/*    */ 
/*    */ 
/*    */ public class AudioDeviceList
/*    */   extends ListScreenListBase<AudioDeviceEntry>
/*    */ {
/*    */   public static final int CELL_HEIGHT = 36;
/*    */   @Nullable
/*    */   protected class_2960 icon;
/*    */   @Nullable
/*    */   protected class_2561 defaultDeviceText;
/*    */   @Nullable
/*    */   protected ConfigEntry<String> configEntry;
/*    */   
/*    */   public AudioDeviceList(int width, int height, int top) {
/* 32 */     super(width, height, top, 36);
/* 33 */     method_31322(false);
/* 34 */     method_31323(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean method_25402(double mouseX, double mouseY, int button) {
/* 39 */     if (super.method_25402(mouseX, mouseY, button)) {
/* 40 */       return true;
/*    */     }
/* 42 */     AudioDeviceEntry entry = (AudioDeviceEntry)method_25308(mouseX, mouseY);
/* 43 */     if (entry == null) {
/* 44 */       return false;
/*    */     }
/* 46 */     if (!method_25405(mouseX, mouseY)) {
/* 47 */       return false;
/*    */     }
/* 49 */     if (!isSelected(entry.getDevice())) {
/* 50 */       this.field_22740.method_1483().method_4873((class_1113)class_1109.method_4758(class_3417.field_15015, 1.0F));
/* 51 */       onSelect(entry);
/* 52 */       return true;
/*    */     } 
/* 54 */     return false;
/*    */   }
/*    */   
/*    */   protected void onSelect(AudioDeviceEntry entry) {
/* 58 */     if (this.configEntry != null) {
/* 59 */       this.configEntry.set(entry.device).save();
/*    */     }
/* 61 */     ClientVoicechat client = ClientManager.getClient();
/* 62 */     if (client != null) {
/* 63 */       client.reloadAudio();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25314(Collection<AudioDeviceEntry> entries) {
/* 69 */     super.method_25314(entries);
/*    */   }
/*    */   
/*    */   public void setAudioDevices(Collection<String> entries) {
/* 73 */     method_25314(
/* 74 */         (Collection<AudioDeviceEntry>)Stream.concat(Stream.of(""), entries.stream())
/* 75 */         .map(s -> new AudioDeviceEntry(s, getVisibleName(s), this.icon, ()))
/* 76 */         .collect(Collectors.toList()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSelected(String name) {
/* 81 */     if (this.configEntry == null) {
/* 82 */       return false;
/*    */     }
/* 84 */     return ((String)this.configEntry.get()).equals(name);
/*    */   }
/*    */   
/*    */   public class_2561 getVisibleName(String device) {
/* 88 */     if (device.isEmpty() && this.defaultDeviceText != null) {
/* 89 */       return this.defaultDeviceText;
/*    */     }
/* 91 */     return (class_2561)new class_2585(SoundManager.cleanDeviceName(device));
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 95 */     return method_25396().isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\audiodevice\AudioDeviceList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */