/*    */ package de.maxhenkel.voicechat.gui.widgets;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.VoiceChatScreenBase;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_4587;
/*    */ 
/*    */ public abstract class ListScreenBase
/*    */   extends VoiceChatScreenBase {
/*    */   private Runnable postRender;
/*    */   
/*    */   public ListScreenBase(class_2561 title, int xSize, int ySize) {
/* 12 */     super(title, xSize, ySize);
/*    */   }
/*    */ 
/*    */   
/*    */   public void method_25394(class_4587 poseStack, int mouseX, int mouseY, float delta) {
/* 17 */     super.method_25394(poseStack, mouseX, mouseY, delta);
/* 18 */     if (this.postRender != null) {
/* 19 */       this.postRender.run();
/* 20 */       this.postRender = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void postRender(Runnable postRender) {
/* 25 */     this.postRender = postRender;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\ListScreenBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */