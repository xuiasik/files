/*    */ package de.maxhenkel.voicechat.gui.volume;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.widgets.DebouncedSlider;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_2585;
/*    */ import net.minecraft.class_2588;
/*    */ 
/*    */ public class AdjustVolumeSlider
/*    */   extends DebouncedSlider {
/* 10 */   protected static final class_2561 MUTED = (class_2561)new class_2588("message.voicechat.muted");
/*    */   
/*    */   protected static final float MAXIMUM = 4.0F;
/*    */   
/*    */   protected final VolumeConfigEntry volumeConfigEntry;
/*    */   
/*    */   public AdjustVolumeSlider(int xIn, int yIn, int widthIn, int heightIn, VolumeConfigEntry volumeConfigEntry) {
/* 17 */     super(xIn, yIn, widthIn, heightIn, (class_2561)new class_2585(""), volumeConfigEntry.get() / 4.0D);
/* 18 */     this.volumeConfigEntry = volumeConfigEntry;
/* 19 */     method_25346();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void method_25346() {
/* 24 */     if (this.field_22753 <= 0.0D) {
/* 25 */       method_25355(MUTED);
/*    */       return;
/*    */     } 
/* 28 */     long amp = Math.round(this.field_22753 * 4.0D * 100.0D - 100.0D);
/* 29 */     method_25355((class_2561)new class_2588("message.voicechat.volume_amplification", new Object[] { (((float)amp > 0.0F) ? "+" : "") + amp + "%" }));
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyDebounced() {
/* 34 */     this.volumeConfigEntry.save(this.field_22753 * 4.0D);
/*    */   }
/*    */   
/*    */   public static interface VolumeConfigEntry {
/*    */     void save(double param1Double);
/*    */     
/*    */     double get();
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\volume\AdjustVolumeSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */