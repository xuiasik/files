/*     */ package de.maxhenkel.voicechat.plugins.impl;
/*     */ import de.maxhenkel.voicechat.api.Entity;
/*     */ import de.maxhenkel.voicechat.api.Position;
/*     */ import de.maxhenkel.voicechat.api.ServerPlayer;
/*     */ import de.maxhenkel.voicechat.api.VolumeCategory;
/*     */ import de.maxhenkel.voicechat.api.audio.AudioConverter;
/*     */ import de.maxhenkel.voicechat.api.mp3.Mp3Decoder;
/*     */ import de.maxhenkel.voicechat.api.mp3.Mp3Encoder;
/*     */ import de.maxhenkel.voicechat.api.opus.OpusDecoder;
/*     */ import de.maxhenkel.voicechat.api.opus.OpusEncoder;
/*     */ import de.maxhenkel.voicechat.api.opus.OpusEncoderMode;
/*     */ import de.maxhenkel.voicechat.plugins.impl.audio.AudioConverterImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.mp3.Mp3DecoderImpl;
/*     */ import de.maxhenkel.voicechat.plugins.impl.opus.OpusManager;
/*     */ import de.maxhenkel.voicechat.voice.common.Utils;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import net.minecraft.class_1297;
/*     */ import net.minecraft.class_3218;
/*     */ import net.minecraft.class_3222;
/*     */ 
/*     */ public abstract class VoicechatApiImpl implements VoicechatApi {
/*  25 */   private static final AudioConverter AUDIO_CONVERTER = (AudioConverter)new AudioConverterImpl();
/*     */ 
/*     */   
/*     */   public OpusEncoder createEncoder() {
/*  29 */     return OpusManager.createEncoder(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public OpusEncoder createEncoder(OpusEncoderMode mode) {
/*  34 */     return OpusManager.createEncoder(mode);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Mp3Encoder createMp3Encoder(AudioFormat audioFormat, int bitrate, int quality, OutputStream outputStream) {
/*  40 */     return Mp3EncoderImpl.createEncoder(audioFormat, bitrate, quality, outputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Mp3Decoder createMp3Decoder(InputStream inputStream) {
/*  46 */     return Mp3DecoderImpl.createDecoder(inputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public OpusDecoder createDecoder() {
/*  51 */     return OpusManager.createDecoder();
/*     */   }
/*     */   
/*     */   public AudioConverter getAudioConverter() {
/*  55 */     return AUDIO_CONVERTER;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity fromEntity(Object entity) {
/*  60 */     if (entity instanceof class_1297) {
/*  61 */       class_1297 e = (class_1297)entity;
/*  62 */       return new EntityImpl(e);
/*     */     } 
/*  64 */     throw new IllegalArgumentException("entity is not an instance of Entity");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerLevel fromServerLevel(Object serverLevel) {
/*  70 */     if (serverLevel instanceof class_3218) {
/*  71 */       class_3218 l = (class_3218)serverLevel;
/*  72 */       return new ServerLevelImpl(l);
/*     */     } 
/*  74 */     throw new IllegalArgumentException("serverLevel is not an instance of ServerLevel");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerPlayer fromServerPlayer(Object serverPlayer) {
/*  80 */     if (serverPlayer instanceof class_3222) {
/*  81 */       class_3222 p = (class_3222)serverPlayer;
/*  82 */       return new ServerPlayerImpl(p);
/*     */     } 
/*  84 */     throw new IllegalArgumentException("serverPlayer is not an instance of ServerPlayer");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Position createPosition(double x, double y, double z) {
/*  90 */     return new PositionImpl(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public VolumeCategory.Builder volumeCategoryBuilder() {
/*  95 */     return new VolumeCategoryImpl.BuilderImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public double getVoiceChatDistance() {
/* 100 */     return Utils.getDefaultDistanceServer();
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\VoicechatApiImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */