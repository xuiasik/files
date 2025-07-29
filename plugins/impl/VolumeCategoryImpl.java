/*     */ package de.maxhenkel.voicechat.plugins.impl;
/*     */ 
/*     */ import de.maxhenkel.voicechat.api.VolumeCategory;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.class_2540;
/*     */ 
/*     */ public class VolumeCategoryImpl
/*     */   implements VolumeCategory
/*     */ {
/*  11 */   public static final Pattern ID_REGEX = Pattern.compile("^[a-z_]{1,16}$");
/*     */   
/*     */   private final String id;
/*     */   private final String name;
/*     */   @Nullable
/*     */   private final String description;
/*     */   @Nullable
/*     */   private final int[][] icon;
/*     */   
/*     */   public VolumeCategoryImpl(String id, String name, @Nullable String description, @Nullable int[][] icon) {
/*  21 */     if (!ID_REGEX.matcher(id).matches()) {
/*  22 */       throw new IllegalArgumentException("Volume category ID can only contain a-z and _ with a maximum amount of 16 characters");
/*     */     }
/*  24 */     this.id = id;
/*  25 */     this.name = name;
/*  26 */     this.description = description;
/*  27 */     this.icon = icon;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  32 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  37 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDescription() {
/*  43 */     return this.description;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public int[][] getIcon() {
/*  49 */     return this.icon;
/*     */   }
/*     */   
/*     */   public static VolumeCategoryImpl fromBytes(class_2540 buf) {
/*  53 */     String id = buf.method_10800(16);
/*  54 */     String name = buf.method_10800(16);
/*  55 */     String description = null;
/*  56 */     if (buf.readBoolean()) {
/*  57 */       description = buf.method_10800(32767);
/*     */     }
/*  59 */     int[][] icon = (int[][])null;
/*  60 */     if (buf.readBoolean()) {
/*  61 */       icon = new int[16][16];
/*  62 */       for (int x = 0; x < icon.length; x++) {
/*  63 */         for (int y = 0; y < icon.length; y++) {
/*  64 */           icon[x][y] = buf.readInt();
/*     */         }
/*     */       } 
/*     */     } 
/*  68 */     return new VolumeCategoryImpl(id, name, description, icon);
/*     */   }
/*     */   
/*     */   public void toBytes(class_2540 buf) {
/*  72 */     buf.method_10788(this.id, 16);
/*  73 */     buf.method_10788(this.name, 16);
/*  74 */     buf.writeBoolean((this.description != null));
/*  75 */     if (this.description != null) {
/*  76 */       buf.method_10788(this.description, 32767);
/*     */     }
/*  78 */     buf.writeBoolean((this.icon != null));
/*  79 */     if (this.icon != null) {
/*  80 */       if (this.icon.length != 16) {
/*  81 */         throw new IllegalStateException("Icon is not 16x16");
/*     */       }
/*  83 */       for (int x = 0; x < this.icon.length; x++) {
/*  84 */         if ((this.icon[x]).length != 16) {
/*  85 */           throw new IllegalStateException("Icon is not 16x16");
/*     */         }
/*  87 */         for (int y = 0; y < this.icon.length; y++) {
/*  88 */           buf.writeInt(this.icon[x][y]);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  96 */     if (this == object) {
/*  97 */       return true;
/*     */     }
/*  99 */     if (object == null || getClass() != object.getClass()) {
/* 100 */       return false;
/*     */     }
/* 102 */     VolumeCategoryImpl that = (VolumeCategoryImpl)object;
/* 103 */     return this.id.equals(that.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 108 */     return this.id.hashCode();
/*     */   }
/*     */   
/*     */   public static class BuilderImpl
/*     */     implements VolumeCategory.Builder
/*     */   {
/*     */     private String id;
/*     */     private String name;
/*     */     @Nullable
/*     */     private String description;
/*     */     @Nullable
/*     */     private int[][] icon;
/*     */     
/*     */     public VolumeCategory.Builder setId(String id) {
/* 122 */       this.id = id;
/* 123 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VolumeCategory.Builder setName(String name) {
/* 128 */       this.name = name;
/* 129 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VolumeCategory.Builder setDescription(@Nullable String description) {
/* 134 */       this.description = description;
/* 135 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VolumeCategory.Builder setIcon(@Nullable int[][] icon) {
/* 140 */       this.icon = icon;
/* 141 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VolumeCategory build() {
/* 146 */       if (this.id == null) {
/* 147 */         throw new IllegalStateException("id missing");
/*     */       }
/* 149 */       if (this.name == null) {
/* 150 */         throw new IllegalStateException("name missing");
/*     */       }
/* 152 */       return new VolumeCategoryImpl(this.id, this.name, this.description, this.icon);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\VolumeCategoryImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */