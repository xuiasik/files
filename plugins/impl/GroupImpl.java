/*     */ package de.maxhenkel.voicechat.plugins.impl;
/*     */ 
/*     */ import de.maxhenkel.voicechat.Voicechat;
/*     */ import de.maxhenkel.voicechat.api.Group;
/*     */ import de.maxhenkel.voicechat.voice.common.PlayerState;
/*     */ import de.maxhenkel.voicechat.voice.server.Group;
/*     */ import de.maxhenkel.voicechat.voice.server.Server;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public class GroupImpl
/*     */   implements Group {
/*     */   private final Group group;
/*     */   
/*     */   public GroupImpl(Group group) {
/*  17 */     this.group = group;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  22 */     return this.group.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPassword() {
/*  27 */     return (this.group.getPassword() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public UUID getId() {
/*  32 */     return this.group.getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPersistent() {
/*  37 */     return this.group.isPersistent();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHidden() {
/*  42 */     return this.group.isHidden();
/*     */   }
/*     */ 
/*     */   
/*     */   public Group.Type getType() {
/*  47 */     return this.group.getType();
/*     */   }
/*     */   
/*     */   public Group getGroup() {
/*  51 */     return this.group;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static GroupImpl create(PlayerState state) {
/*  56 */     UUID groupId = state.getGroup();
/*  57 */     Server server = Voicechat.SERVER.getServer();
/*  58 */     if (server != null && groupId != null) {
/*  59 */       Group g = server.getGroupManager().getGroup(groupId);
/*  60 */       if (g != null) {
/*  61 */         return new GroupImpl(g);
/*     */       }
/*     */     } 
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  69 */     if (this == object) {
/*  70 */       return true;
/*     */     }
/*  72 */     if (object == null || getClass() != object.getClass()) {
/*  73 */       return false;
/*     */     }
/*  75 */     GroupImpl group1 = (GroupImpl)object;
/*  76 */     return Objects.equals(this.group.getId(), group1.group.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  81 */     return (this.group != null) ? this.group.getId().hashCode() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class BuilderImpl
/*     */     implements Group.Builder
/*     */   {
/*     */     @Nullable
/*     */     private UUID id;
/*     */     
/*     */     private String name;
/*     */     @Nullable
/*     */     private String password;
/*     */     private boolean persistent;
/*     */     private boolean hidden;
/*  96 */     private Group.Type type = Group.Type.NORMAL;
/*     */ 
/*     */ 
/*     */     
/*     */     public Group.Builder setId(@Nullable UUID id) {
/* 101 */       this.id = id;
/* 102 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Group.Builder setName(String name) {
/* 107 */       this.name = convertGroupName(name);
/* 108 */       return this;
/*     */     }
/*     */     
/*     */     private static String convertGroupName(String name) {
/* 112 */       name = name.replaceAll("[\\n\\r\\t]", "");
/* 113 */       if (name.matches("^\\s.*")) {
/* 114 */         name = name.replaceFirst("^\\s+", "");
/*     */       }
/* 116 */       if (name.length() > 16) {
/* 117 */         return name.substring(0, 16);
/*     */       }
/* 119 */       return name;
/*     */     }
/*     */ 
/*     */     
/*     */     public Group.Builder setPassword(String password) {
/* 124 */       this.password = password;
/* 125 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Group.Builder setPersistent(boolean persistent) {
/* 130 */       this.persistent = persistent;
/* 131 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Group.Builder setHidden(boolean hidden) {
/* 136 */       this.hidden = hidden;
/* 137 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Group.Builder setType(Group.Type type) {
/* 142 */       this.type = type;
/* 143 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Group build() {
/* 148 */       if (this.name == null) {
/* 149 */         throw new IllegalStateException("Group is missing a name");
/*     */       }
/* 151 */       if (!Voicechat.GROUP_REGEX.matcher(this.name).matches()) {
/* 152 */         throw new IllegalStateException(String.format("Invalid group name: %s", new Object[] { this.name }));
/*     */       }
/* 154 */       GroupImpl group = new GroupImpl(new Group((this.id == null) ? UUID.randomUUID() : this.id, this.name, this.password, this.persistent, this.hidden, this.type));
/* 155 */       Server server = Voicechat.SERVER.getServer();
/* 156 */       if (server != null && this.persistent) {
/* 157 */         server.getGroupManager().addGroup(group.getGroup(), null);
/*     */       }
/* 159 */       return group;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TypeImpl
/*     */     implements Group.Type {
/*     */     public static short toInt(Group.Type type) {
/* 166 */       if (type == OPEN)
/* 167 */         return 1; 
/* 168 */       if (type == ISOLATED) {
/* 169 */         return 2;
/*     */       }
/* 171 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public static Group.Type fromInt(short i) {
/* 176 */       if (i == 1)
/* 177 */         return OPEN; 
/* 178 */       if (i == 2) {
/* 179 */         return ISOLATED;
/*     */       }
/* 181 */       return NORMAL;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\GroupImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */