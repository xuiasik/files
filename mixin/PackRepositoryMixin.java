/*    */ package de.maxhenkel.voicechat.mixin;
/*    */ 
/*    */ import de.maxhenkel.voicechat.resourcepacks.IPackRepository;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.class_3283;
/*    */ import net.minecraft.class_3285;
/*    */ import org.spongepowered.asm.mixin.Final;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Mutable;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ 
/*    */ 
/*    */ @Mixin(value = {class_3283.class}, priority = 0)
/*    */ public class PackRepositoryMixin
/*    */   implements IPackRepository
/*    */ {
/*    */   @Shadow
/*    */   @Final
/*    */   @Mutable
/*    */   private Set<class_3285> field_14227;
/*    */   
/*    */   public void addSource(class_3285 source) {
/* 24 */     Set<class_3285> set = new HashSet<>(this.field_14227);
/* 25 */     set.add(source);
/* 26 */     this.field_14227 = set;
/*    */   }
/*    */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\mixin\PackRepositoryMixin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */