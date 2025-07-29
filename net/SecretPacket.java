/*     */ package de.maxhenkel.voicechat.net;
/*     */ 
/*     */ import de.maxhenkel.voicechat.config.ServerConfig;
/*     */ import de.maxhenkel.voicechat.plugins.PluginManager;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.class_2540;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_3222;
/*     */ 
/*     */ 
/*     */ public class SecretPacket
/*     */   implements Packet<SecretPacket>
/*     */ {
/*  14 */   public static final class_2960 SECRET = new class_2960("voicechat", "secret");
/*     */   
/*     */   private UUID secret;
/*     */   
/*     */   private int serverPort;
/*     */   
/*     */   private UUID playerUUID;
/*     */   private ServerConfig.Codec codec;
/*     */   private int mtuSize;
/*     */   private double voiceChatDistance;
/*     */   private int keepAlive;
/*     */   private boolean groupsEnabled;
/*     */   private String voiceHost;
/*     */   private boolean allowRecording;
/*     */   
/*     */   public SecretPacket() {}
/*     */   
/*     */   public SecretPacket(class_3222 player, UUID secret, int port, ServerConfig serverConfig) {
/*  32 */     this.secret = secret;
/*  33 */     this.serverPort = port;
/*  34 */     this.playerUUID = player.method_5667();
/*  35 */     this.codec = (ServerConfig.Codec)serverConfig.voiceChatCodec.get();
/*  36 */     this.mtuSize = ((Integer)serverConfig.voiceChatMtuSize.get()).intValue();
/*  37 */     this.voiceChatDistance = ((Double)serverConfig.voiceChatDistance.get()).doubleValue();
/*  38 */     this.keepAlive = ((Integer)serverConfig.keepAlive.get()).intValue();
/*  39 */     this.groupsEnabled = ((Boolean)serverConfig.groupsEnabled.get()).booleanValue();
/*  40 */     this.voiceHost = PluginManager.instance().getVoiceHost((String)serverConfig.voiceHost.get());
/*  41 */     this.allowRecording = ((Boolean)serverConfig.allowRecording.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public UUID getSecret() {
/*  45 */     return this.secret;
/*     */   }
/*     */   
/*     */   public int getServerPort() {
/*  49 */     return this.serverPort;
/*     */   }
/*     */   
/*     */   public UUID getPlayerUUID() {
/*  53 */     return this.playerUUID;
/*     */   }
/*     */   
/*     */   public ServerConfig.Codec getCodec() {
/*  57 */     return this.codec;
/*     */   }
/*     */   
/*     */   public int getMtuSize() {
/*  61 */     return this.mtuSize;
/*     */   }
/*     */   
/*     */   public double getVoiceChatDistance() {
/*  65 */     return this.voiceChatDistance;
/*     */   }
/*     */   
/*     */   public int getKeepAlive() {
/*  69 */     return this.keepAlive;
/*     */   }
/*     */   
/*     */   public boolean groupsEnabled() {
/*  73 */     return this.groupsEnabled;
/*     */   }
/*     */   
/*     */   public String getVoiceHost() {
/*  77 */     return this.voiceHost;
/*     */   }
/*     */ 
/*     */   
/*     */   public class_2960 getIdentifier() {
/*  82 */     return SECRET;
/*     */   }
/*     */   
/*     */   public boolean allowRecording() {
/*  86 */     return this.allowRecording;
/*     */   }
/*     */ 
/*     */   
/*     */   public SecretPacket fromBytes(class_2540 buf) {
/*  91 */     this.secret = buf.method_10790();
/*  92 */     this.serverPort = buf.readInt();
/*  93 */     this.playerUUID = buf.method_10790();
/*  94 */     this.codec = ServerConfig.Codec.values()[buf.readByte()];
/*  95 */     this.mtuSize = buf.readInt();
/*  96 */     this.voiceChatDistance = buf.readDouble();
/*  97 */     this.keepAlive = buf.readInt();
/*  98 */     this.groupsEnabled = buf.readBoolean();
/*  99 */     this.voiceHost = buf.method_10800(32767);
/* 100 */     this.allowRecording = buf.readBoolean();
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void toBytes(class_2540 buf) {
/* 106 */     buf.method_10797(this.secret);
/* 107 */     buf.writeInt(this.serverPort);
/* 108 */     buf.method_10797(this.playerUUID);
/* 109 */     buf.writeByte(this.codec.ordinal());
/* 110 */     buf.writeInt(this.mtuSize);
/* 111 */     buf.writeDouble(this.voiceChatDistance);
/* 112 */     buf.writeInt(this.keepAlive);
/* 113 */     buf.writeBoolean(this.groupsEnabled);
/* 114 */     buf.method_10814(this.voiceHost);
/* 115 */     buf.writeBoolean(this.allowRecording);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\net\SecretPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */