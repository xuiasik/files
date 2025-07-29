/*    */ package de.maxhenkel.voicechat.gui.audiodevice;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_124;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2588;
/*    */ import net.minecraft.class_2960;
/*    */ import net.minecraft.class_339;
/*    */ import net.minecraft.class_3532;
/*    */ import net.minecraft.class_364;
/*    */ import net.minecraft.class_4185;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ import net.minecraft.class_5348;
/*    */ 
/*    */ public abstract class SelectDeviceScreen extends ListScreenBase {
/* 19 */   protected static final class_2960 TEXTURE = new class_2960("voicechat", "textures/gui/gui_audio_devices.png");
/* 20 */   protected static final class_2561 BACK = (class_2561)new class_2588("message.voicechat.back");
/*    */   
/*    */   protected static final int HEADER_SIZE = 16;
/*    */   
/*    */   protected static final int FOOTER_SIZE = 32;
/*    */   
/*    */   protected static final int UNIT_SIZE = 18;
/*    */   @Nullable
/*    */   protected class_437 parent;
/*    */   protected AudioDeviceList deviceList;
/*    */   protected class_4185 back;
/*    */   protected int units;
/*    */   
/*    */   public SelectDeviceScreen(class_2561 title, @Nullable class_437 parent) {
/* 34 */     super(title, 236, 0);
/* 35 */     this.parent = parent;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void method_25426() {
/* 46 */     super.method_25426();
/* 47 */     this.guiLeft += 2;
/* 48 */     this.guiTop = 32;
/* 49 */     int minUnits = class_3532.method_15386(2.2222223F);
/* 50 */     this.units = Math.max(minUnits, (this.field_22790 - 16 - 32 - this.guiTop * 2) / 18);
/* 51 */     this.ySize = 16 + this.units * 18 + 32;
/*    */     
/* 53 */     if (this.deviceList != null) {
/* 54 */       this.deviceList.updateSize(this.field_22789, this.units * 18, this.guiTop + 16);
/*    */     } else {
/* 56 */       this.deviceList = createAudioDeviceList(this.field_22789, this.units * 18, this.guiTop + 16);
/*    */     } 
/* 58 */     method_25429((class_364)this.deviceList);
/*    */     
/* 60 */     this.back = new class_4185(this.guiLeft + 7, this.guiTop + this.ySize - 20 - 7, this.xSize - 14, 20, BACK, button -> this.field_22787.method_1507(this.parent));
/*    */ 
/*    */     
/* 63 */     method_25411((class_339)this.back);
/*    */     
/* 65 */     this.deviceList.setAudioDevices(getDevices());
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderBackground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/* 70 */     if (isIngame()) {
/* 71 */       this.field_22787.method_1531().method_22813(TEXTURE);
/* 72 */       method_25302(poseStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, 16);
/* 73 */       for (int i = 0; i < this.units; i++) {
/* 74 */         method_25302(poseStack, this.guiLeft, this.guiTop + 16 + 18 * i, 0, 16, this.xSize, 18);
/*    */       }
/* 76 */       method_25302(poseStack, this.guiLeft, this.guiTop + 16 + 18 * this.units, 0, 34, this.xSize, 32);
/* 77 */       method_25302(poseStack, this.guiLeft + 10, this.guiTop + 16 + 6 - 2, this.xSize, 0, 12, 12);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderForeground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/* 83 */     this.field_22793.method_30883(poseStack, this.field_22785, (this.field_22789 / 2 - this.field_22793.method_27525((class_5348)this.field_22785) / 2), (this.guiTop + 5), isIngame() ? 4210752 : class_124.field_1068.method_532().intValue());
/* 84 */     if (!this.deviceList.isEmpty()) {
/* 85 */       this.deviceList.method_25394(poseStack, mouseX, mouseY, delta);
/*    */     } else {
/* 87 */       this.field_22793.getClass(); method_27534(poseStack, this.field_22793, getEmptyListComponent(), this.field_22789 / 2, this.guiTop + 16 + this.units * 18 / 2 - 9 / 2, -1);
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract List<String> getDevices();
/*    */   
/*    */   public abstract class_2561 getEmptyListComponent();
/*    */   
/*    */   public abstract AudioDeviceList createAudioDeviceList(int paramInt1, int paramInt2, int paramInt3);
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\audiodevice\SelectDeviceScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */