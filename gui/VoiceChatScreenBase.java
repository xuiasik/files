/*     */ package de.maxhenkel.voicechat.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.class_124;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_437;
/*     */ import net.minecraft.class_4587;
/*     */ import net.minecraft.class_5481;
/*     */ 
/*     */ 
/*     */ public abstract class VoiceChatScreenBase
/*     */   extends class_437
/*     */ {
/*     */   public static final int FONT_COLOR = 4210752;
/*     */   protected List<HoverArea> hoverAreas;
/*     */   protected int guiLeft;
/*     */   protected int guiTop;
/*     */   protected int xSize;
/*     */   protected int ySize;
/*     */   
/*     */   protected VoiceChatScreenBase(class_2561 title, int xSize, int ySize) {
/*  25 */     super(title);
/*  26 */     this.xSize = xSize;
/*  27 */     this.ySize = ySize;
/*  28 */     this.hoverAreas = new ArrayList<>();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void method_25426() {
/*  33 */     this.field_22791.clear();
/*  34 */     this.field_22786.clear();
/*  35 */     super.method_25426();
/*     */     
/*  37 */     this.guiLeft = (this.field_22789 - this.xSize) / 2;
/*  38 */     this.guiTop = (this.field_22790 - this.ySize) / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25394(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/*  43 */     method_25420(poseStack);
/*  44 */     renderBackground(poseStack, mouseX, mouseY, delta);
/*  45 */     super.method_25394(poseStack, mouseX, mouseY, delta);
/*  46 */     renderForeground(poseStack, mouseX, mouseY, delta);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderBackground(class_4587 poseStack, int mouseX, int mouseY, float delta) {}
/*     */ 
/*     */   
/*     */   public void renderForeground(class_4587 poseStack, int mouseX, int mouseY, float delta) {}
/*     */ 
/*     */   
/*     */   public int getGuiLeft() {
/*  58 */     return this.guiLeft;
/*     */   }
/*     */   
/*     */   public int getGuiTop() {
/*  62 */     return this.guiTop;
/*     */   }
/*     */   
/*     */   protected boolean isIngame() {
/*  66 */     return (this.field_22787.field_1687 != null);
/*     */   }
/*     */   
/*     */   protected int getFontColor() {
/*  70 */     return isIngame() ? 4210752 : class_124.field_1068.method_532().intValue();
/*     */   }
/*     */   
/*     */   public void drawHoverAreas(class_4587 matrixStack, int mouseX, int mouseY) {
/*  74 */     for (HoverArea hoverArea : this.hoverAreas) {
/*  75 */       if (hoverArea.tooltip != null && hoverArea.isHovered(this.guiLeft, this.guiTop, mouseX, mouseY)) {
/*  76 */         method_25417(matrixStack, hoverArea.tooltip.get(), mouseX - this.guiLeft, mouseY - this.guiTop);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static class HoverArea
/*     */   {
/*     */     private final int posX;
/*     */     private final int posY;
/*     */     
/*     */     public HoverArea(int posX, int posY, int width, int height) {
/*  88 */       this(posX, posY, width, height, null);
/*     */     } private final int width; private final int height; @Nullable
/*     */     private final Supplier<List<class_5481>> tooltip;
/*     */     public HoverArea(int posX, int posY, int width, int height, Supplier<List<class_5481>> tooltip) {
/*  92 */       this.posX = posX;
/*  93 */       this.posY = posY;
/*  94 */       this.width = width;
/*  95 */       this.height = height;
/*  96 */       this.tooltip = tooltip;
/*     */     }
/*     */     
/*     */     public int getPosX() {
/* 100 */       return this.posX;
/*     */     }
/*     */     
/*     */     public int getPosY() {
/* 104 */       return this.posY;
/*     */     }
/*     */     
/*     */     public int getWidth() {
/* 108 */       return this.width;
/*     */     }
/*     */     
/*     */     public int getHeight() {
/* 112 */       return this.height;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Supplier<List<class_5481>> getTooltip() {
/* 117 */       return this.tooltip;
/*     */     }
/*     */     
/*     */     public boolean isHovered(int guiLeft, int guiTop, int mouseX, int mouseY) {
/* 121 */       if (mouseX >= guiLeft + this.posX && mouseX < guiLeft + this.posX + this.width && 
/* 122 */         mouseY >= guiTop + this.posY && mouseY < guiTop + this.posY + this.height) {
/* 123 */         return true;
/*     */       }
/*     */       
/* 126 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\VoiceChatScreenBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */