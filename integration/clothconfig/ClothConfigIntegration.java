/*     */ package de.maxhenkel.voicechat.integration.clothconfig;
/*     */ import de.maxhenkel.voicechat.VoicechatClient;
/*     */ import de.maxhenkel.voicechat.configbuilder.entry.BooleanConfigEntry;
/*     */ import de.maxhenkel.voicechat.configbuilder.entry.ConfigEntry;
/*     */ import de.maxhenkel.voicechat.configbuilder.entry.DoubleConfigEntry;
/*     */ import de.maxhenkel.voicechat.configbuilder.entry.IntegerConfigEntry;
/*     */ import de.maxhenkel.voicechat.configbuilder.entry.StringConfigEntry;
/*     */ import de.maxhenkel.voicechat.voice.client.GroupPlayerIconOrientation;
/*     */ import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
/*     */ import me.shedaniel.clothconfig2.api.ConfigBuilder;
/*     */ import me.shedaniel.clothconfig2.api.ConfigCategory;
/*     */ import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2588;
/*     */ 
/*     */ public class ClothConfigIntegration {
/*  17 */   public static final class_2588 SETTINGS = new class_2588("cloth_config.voicechat.settings");
/*  18 */   public static final class_2588 OTHER_SETTINGS = new class_2588("cloth_config.voicechat.category.ingame_menu");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class_437 createConfigScreen(class_437 parent) {
/*  24 */     ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle((class_2561)SETTINGS);
/*     */     
/*  26 */     ConfigEntryBuilder entryBuilder = builder.entryBuilder();
/*     */     
/*  28 */     ConfigCategory general = builder.getOrCreateCategory((class_2561)new class_2588("cloth_config.voicechat.category.general"));
/*     */     
/*  30 */     general.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.recordingDestination));
/*  31 */     general.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.runLocalServer));
/*  32 */     general.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.offlinePlayerVolumeAdjustment));
/*  33 */     general.addEntry((AbstractConfigListEntry)entryBuilder
/*  34 */         .startEnumSelector((class_2561)new class_2588("cloth_config.voicechat.config.freecam_mode"), FreecamMode.class, (Enum)VoicechatClient.CLIENT_CONFIG.freecamMode.get())
/*  35 */         .setEnumNameProvider(e -> new class_2588(String.format("cloth_config.voicechat.config.freecam_mode.%s", new Object[] { e.name().toLowerCase()
/*  36 */               }))).setTooltip(new class_2561[] { (class_2561)new class_2588("cloth_config.voicechat.config.freecam_mode.description")
/*  37 */           }).setDefaultValue(VoicechatClient.CLIENT_CONFIG.freecamMode::getDefault)
/*  38 */         .setSaveConsumer(e -> VoicechatClient.CLIENT_CONFIG.freecamMode.set(e).save())
/*  39 */         .build());
/*     */     
/*  41 */     general.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.muteOnJoin));
/*     */     
/*  43 */     ConfigCategory audio = builder.getOrCreateCategory((class_2561)new class_2588("cloth_config.voicechat.category.audio"));
/*  44 */     audio.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.audioPacketThreshold));
/*  45 */     audio.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.deactivationDelay));
/*  46 */     audio.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.outputBufferSize));
/*     */     
/*  48 */     ConfigCategory hudIcons = builder.getOrCreateCategory((class_2561)new class_2588("cloth_config.voicechat.category.hud_icons"));
/*  49 */     hudIcons.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.hudIconScale));
/*  50 */     hudIcons.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.hudIconPosX));
/*  51 */     hudIcons.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.hudIconPosY));
/*     */     
/*  53 */     ConfigCategory groupIcons = builder.getOrCreateCategory((class_2561)new class_2588("cloth_config.voicechat.category.group_chat_icons"));
/*  54 */     groupIcons.addEntry((AbstractConfigListEntry)entryBuilder
/*  55 */         .startEnumSelector((class_2561)new class_2588("cloth_config.voicechat.config.group_player_icon_orientation"), GroupPlayerIconOrientation.class, (Enum)VoicechatClient.CLIENT_CONFIG.groupPlayerIconOrientation.get())
/*  56 */         .setEnumNameProvider(e -> new class_2588(String.format("cloth_config.voicechat.config.group_player_icon_orientation.%s", new Object[] { e.name().toLowerCase()
/*  57 */               }))).setTooltip(new class_2561[] { (class_2561)new class_2588("cloth_config.voicechat.config.group_player_icon_orientation.description")
/*  58 */           }).setDefaultValue(VoicechatClient.CLIENT_CONFIG.groupPlayerIconOrientation::getDefault)
/*  59 */         .setSaveConsumer(e -> VoicechatClient.CLIENT_CONFIG.groupPlayerIconOrientation.set(e).save())
/*  60 */         .build());
/*     */     
/*  62 */     groupIcons.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.groupHudIconScale));
/*  63 */     groupIcons.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.groupPlayerIconPosX));
/*  64 */     groupIcons.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.groupPlayerIconPosY));
/*  65 */     groupIcons.addEntry(fromConfigEntry(entryBuilder, VoicechatClient.CLIENT_CONFIG.showOwnGroupIcon));
/*     */     
/*  67 */     builder.getOrCreateCategory((class_2561)OTHER_SETTINGS);
/*  68 */     return builder.build();
/*     */   }
/*     */   
/*     */   protected static <T> AbstractConfigListEntry<T> fromConfigEntry(ConfigEntryBuilder entryBuilder, ConfigEntry<T> entry) {
/*  72 */     class_2588 class_25881 = new class_2588(String.format("cloth_config.voicechat.config.%s", new Object[] { entry.getKey() }));
/*  73 */     class_2588 class_25882 = new class_2588(String.format("cloth_config.voicechat.config.%s.description", new Object[] { entry.getKey() }));
/*     */     
/*  75 */     if (entry instanceof DoubleConfigEntry) {
/*  76 */       DoubleConfigEntry e = (DoubleConfigEntry)entry;
/*  77 */       return (AbstractConfigListEntry<T>)entryBuilder
/*  78 */         .startDoubleField((class_2561)class_25881, ((Double)e.get()).doubleValue())
/*  79 */         .setTooltip(new class_2561[] { (class_2561)class_25882
/*  80 */           }).setMin(((Double)e.getMin()).doubleValue())
/*  81 */         .setMax(((Double)e.getMax()).doubleValue())
/*  82 */         .setDefaultValue(e::getDefault)
/*  83 */         .setSaveConsumer(d -> {
/*     */             e.set(d);
/*     */             
/*     */             e.save();
/*  87 */           }).build();
/*  88 */     }  if (entry instanceof IntegerConfigEntry) {
/*  89 */       IntegerConfigEntry e = (IntegerConfigEntry)entry;
/*  90 */       return (AbstractConfigListEntry<T>)entryBuilder
/*  91 */         .startIntField((class_2561)class_25881, ((Integer)e.get()).intValue())
/*  92 */         .setTooltip(new class_2561[] { (class_2561)class_25882
/*  93 */           }).setMin(((Integer)e.getMin()).intValue())
/*  94 */         .setMax(((Integer)e.getMax()).intValue())
/*  95 */         .setDefaultValue(e::getDefault)
/*  96 */         .setSaveConsumer(d -> e.set(d).save())
/*  97 */         .build();
/*  98 */     }  if (entry instanceof BooleanConfigEntry) {
/*  99 */       BooleanConfigEntry e = (BooleanConfigEntry)entry;
/* 100 */       return (AbstractConfigListEntry<T>)entryBuilder
/* 101 */         .startBooleanToggle((class_2561)class_25881, ((Boolean)e.get()).booleanValue())
/* 102 */         .setTooltip(new class_2561[] { (class_2561)class_25882
/* 103 */           }).setDefaultValue(e::getDefault)
/* 104 */         .setSaveConsumer(d -> e.set(d).save())
/* 105 */         .build();
/* 106 */     }  if (entry instanceof StringConfigEntry) {
/* 107 */       StringConfigEntry e = (StringConfigEntry)entry;
/* 108 */       return (AbstractConfigListEntry<T>)entryBuilder
/* 109 */         .startStrField((class_2561)class_25881, (String)e.get())
/* 110 */         .setTooltip(new class_2561[] { (class_2561)class_25882
/* 111 */           }).setDefaultValue(e::getDefault)
/* 112 */         .setSaveConsumer(d -> e.set(d).save())
/* 113 */         .build();
/*     */     } 
/*     */     
/* 116 */     throw new IllegalArgumentException(String.format("Unknown config entry type %s", new Object[] { entry.getClass().getName() }));
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\integration\clothconfig\ClothConfigIntegration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */