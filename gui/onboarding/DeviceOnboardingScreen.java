/*    */ package de.maxhenkel.voicechat.gui.onboarding;
/*    */ 
/*    */ import de.maxhenkel.voicechat.gui.audiodevice.AudioDeviceList;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.class_2561;
/*    */ import net.minecraft.class_310;
/*    */ import net.minecraft.class_364;
/*    */ import net.minecraft.class_437;
/*    */ import net.minecraft.class_4587;
/*    */ 
/*    */ public abstract class DeviceOnboardingScreen
/*    */   extends OnboardingScreenBase {
/*    */   protected AudioDeviceList deviceList;
/*    */   
/*    */   public DeviceOnboardingScreen(class_2561 title, @Nullable class_437 previous) {
/* 16 */     super(title, previous);
/* 17 */     this.field_22787 = class_310.method_1551();
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract AudioDeviceList createAudioDeviceList(int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   protected void method_25426() {
/* 24 */     super.method_25426();
/*    */     
/* 26 */     if (this.deviceList != null) {
/* 27 */       this.field_22793.getClass(); this.field_22793.getClass(); this.deviceList.updateSize(this.field_22789, this.contentHeight - 9 - 20 - 16, this.guiTop + 9 + 8);
/*    */     } else {
/* 29 */       this.field_22793.getClass(); this.field_22793.getClass(); this.deviceList = createAudioDeviceList(this.field_22789, this.contentHeight - 9 - 20 - 16, this.guiTop + 9 + 8);
/*    */     } 
/* 31 */     method_25429((class_364)this.deviceList);
/*    */     
/* 33 */     addBackOrCancelButton();
/* 34 */     addNextButton();
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract class_437 getNextScreen();
/*    */ 
/*    */   
/*    */   public void method_25394(class_4587 stack, int mouseX, int mouseY, float partialTicks) {
/* 42 */     super.method_25394(stack, mouseX, mouseY, partialTicks);
/* 43 */     this.deviceList.method_25394(stack, mouseX, mouseY, partialTicks);
/* 44 */     renderTitle(stack, this.field_22785);
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\onboarding\DeviceOnboardingScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */