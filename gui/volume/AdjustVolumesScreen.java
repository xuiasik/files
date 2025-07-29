/*     */ package de.maxhenkel.voicechat.gui.volume;
/*     */ 
/*     */ import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.class_124;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2588;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_342;
/*     */ import net.minecraft.class_3532;
/*     */ import net.minecraft.class_364;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_5348;
/*     */ 
/*     */ public class AdjustVolumesScreen
/*     */   extends ListScreenBase
/*     */ {
/*  18 */   protected static final class_2960 TEXTURE = new class_2960("voicechat", "textures/gui/gui_volumes.png");
/*  19 */   protected static final class_2561 TITLE = (class_2561)new class_2588("gui.voicechat.adjust_volume.title");
/*  20 */   protected static final class_2561 SEARCH_HINT = (class_2561)(new class_2588("message.voicechat.search_hint")).method_27692(class_124.field_1056).method_27692(class_124.field_1080);
/*  21 */   protected static final class_2561 EMPTY_SEARCH = (class_2561)(new class_2588("message.voicechat.search_empty")).method_27692(class_124.field_1080);
/*     */   
/*     */   protected static final int HEADER_SIZE = 16;
/*     */   
/*     */   protected static final int FOOTER_SIZE = 8;
/*     */   protected static final int SEARCH_HEIGHT = 16;
/*     */   protected static final int UNIT_SIZE = 18;
/*     */   protected static final int CELL_HEIGHT = 36;
/*     */   protected AdjustVolumeList volumeList;
/*     */   protected class_342 searchBox;
/*     */   protected String lastSearch;
/*     */   protected int units;
/*     */   
/*     */   public AdjustVolumesScreen() {
/*  35 */     super(TITLE, 236, 0);
/*  36 */     this.lastSearch = "";
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25393() {
/*  41 */     super.method_25393();
/*  42 */     this.searchBox.method_1865();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void method_25426() {
/*  47 */     super.method_25426();
/*  48 */     this.guiLeft += 2;
/*  49 */     this.guiTop = 32;
/*  50 */     int minUnits = class_3532.method_15386(3.1111112F);
/*  51 */     this.units = Math.max(minUnits, (this.field_22790 - 16 - 8 - this.guiTop * 2 - 16) / 18);
/*  52 */     this.ySize = 16 + this.units * 18 + 8;
/*     */     
/*  54 */     this.field_22787.field_1774.method_1462(true);
/*  55 */     if (this.volumeList != null) {
/*  56 */       this.volumeList.updateSize(this.field_22789, this.units * 18 - 16, this.guiTop + 16 + 16);
/*     */     } else {
/*  58 */       this.volumeList = new AdjustVolumeList(this.field_22789, this.units * 18 - 16, this.guiTop + 16 + 16, 36, this);
/*     */     } 
/*  60 */     String string = (this.searchBox != null) ? this.searchBox.method_1882() : "";
/*  61 */     this.searchBox = new class_342(this.field_22793, this.guiLeft + 28, this.guiTop + 16 + 6, 196, 16, SEARCH_HINT);
/*  62 */     this.searchBox.method_1880(16);
/*  63 */     this.searchBox.method_1858(false);
/*  64 */     this.searchBox.method_1862(true);
/*  65 */     this.searchBox.method_1868(16777215);
/*  66 */     this.searchBox.method_1852(string);
/*  67 */     this.searchBox.method_1863(this::checkSearchStringUpdate);
/*  68 */     method_25429((class_364)this.searchBox);
/*  69 */     method_25429((class_364)this.volumeList);
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25419() {
/*  74 */     super.method_25419();
/*  75 */     this.field_22787.field_1774.method_1462(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/*  80 */     this.field_22787.method_1531().method_22813(TEXTURE);
/*  81 */     method_25302(poseStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, 16);
/*  82 */     for (int i = 0; i < this.units; i++) {
/*  83 */       method_25302(poseStack, this.guiLeft, this.guiTop + 16 + 18 * i, 0, 16, this.xSize, 18);
/*     */     }
/*  85 */     method_25302(poseStack, this.guiLeft, this.guiTop + 16 + 18 * this.units, 0, 34, this.xSize, 8);
/*  86 */     method_25302(poseStack, this.guiLeft + 10, this.guiTop + 16 + 6 - 2, this.xSize, 0, 12, 12);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderForeground(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/*  91 */     this.field_22793.method_30883(poseStack, TITLE, (this.field_22789 / 2 - this.field_22793.method_27525((class_5348)TITLE) / 2), (this.guiTop + 5), 4210752);
/*  92 */     if (!this.volumeList.isEmpty()) {
/*  93 */       this.volumeList.method_25394(poseStack, mouseX, mouseY, delta);
/*  94 */     } else if (!this.searchBox.method_1882().isEmpty()) {
/*  95 */       this.field_22793.getClass(); method_27534(poseStack, this.field_22793, EMPTY_SEARCH, this.field_22789 / 2, this.guiTop + 16 + this.units * 18 / 2 - 9 / 2, -1);
/*     */     } 
/*  97 */     if (!this.searchBox.method_25370() && this.searchBox.method_1882().isEmpty()) {
/*  98 */       method_27535(poseStack, this.field_22793, SEARCH_HINT, this.searchBox.field_22760, this.searchBox.field_22761, -1);
/*     */     } else {
/* 100 */       this.searchBox.method_25394(poseStack, mouseX, mouseY, delta);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean method_25402(double mouseX, double mouseY, int button) {
/* 106 */     if (this.searchBox.method_25370()) {
/* 107 */       this.searchBox.method_25402(mouseX, mouseY, button);
/*     */     }
/* 109 */     return (super.method_25402(mouseX, mouseY, button) || this.volumeList.method_25402(mouseX, mouseY, button));
/*     */   }
/*     */   
/*     */   private void checkSearchStringUpdate(String string) {
/* 113 */     if (!(string = string.toLowerCase(Locale.ROOT)).equals(this.lastSearch)) {
/* 114 */       this.volumeList.setFilter(string);
/* 115 */       this.lastSearch = string;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\volume\AdjustVolumesScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */