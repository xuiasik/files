/*     */ package de.maxhenkel.voicechat.plugins.impl.opus;
/*     */ 
/*     */ import de.maxhenkel.opus4j.OpusEncoder;
/*     */ import de.maxhenkel.voicechat.Voicechat;
/*     */ import de.maxhenkel.voicechat.api.opus.OpusDecoder;
/*     */ import de.maxhenkel.voicechat.api.opus.OpusEncoder;
/*     */ import de.maxhenkel.voicechat.api.opus.OpusEncoderMode;
/*     */ import de.maxhenkel.voicechat.intercompatibility.CrossSideManager;
/*     */ import de.maxhenkel.voicechat.voice.common.Utils;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ public class OpusManager
/*     */ {
/*     */   private static boolean nativeOpusCompatible = true;
/*     */   
/*     */   public static boolean opusNativeCheck() {
/*  18 */     Voicechat.LOGGER.info("Loading Opus", new Object[0]);
/*  19 */     if (!nativeOpusCompatible || !CrossSideManager.get().useNatives()) {
/*  20 */       return false;
/*     */     }
/*     */     
/*  23 */     Boolean success = (Boolean)Utils.createSafe(() -> {
/*     */           NativeOpusEncoderImpl encoder = new NativeOpusEncoderImpl(48000, 1, OpusEncoder.Application.VOIP);
/*     */           
/*     */           encoder.setMaxPayloadSize(1024);
/*     */           
/*     */           byte[] encoded = encoder.encode(new short[960]);
/*     */           
/*     */           encoder.resetState();
/*     */           encoder.close();
/*     */           NativeOpusDecoderImpl decoder = new NativeOpusDecoderImpl(48000, 1);
/*     */           decoder.setFrameSize(960);
/*     */           decoder.decode(encoded);
/*     */           decoder.decodeFec();
/*     */           decoder.resetState();
/*     */           decoder.close();
/*     */           return Boolean.valueOf(true);
/*     */         }e -> Voicechat.LOGGER.warn("Failed to load native Opus implementation", new Object[] { e }));
/*  40 */     if (success == null || !success.booleanValue()) {
/*  41 */       Voicechat.LOGGER.warn("Failed to load native Opus encoder - Falling back to Java Opus implementation", new Object[0]);
/*  42 */       nativeOpusCompatible = false;
/*  43 */       return false;
/*     */     } 
/*  45 */     return true;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static OpusEncoder createNativeEncoder(int mtuSize, OpusEncoder.Application application) {
/*  50 */     if (!nativeOpusCompatible) {
/*  51 */       return null;
/*     */     }
/*     */     
/*     */     try {
/*  55 */       NativeOpusEncoderImpl encoder = new NativeOpusEncoderImpl(48000, 1, application);
/*  56 */       encoder.setMaxPayloadSize(mtuSize);
/*  57 */       return encoder;
/*  58 */     } catch (Throwable e) {
/*  59 */       nativeOpusCompatible = false;
/*  60 */       Voicechat.LOGGER.warn("Failed to load native Opus encoder - Falling back to Java Opus implementation", new Object[0]);
/*  61 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static OpusEncoder createEncoder(OpusEncoderMode mode) {
/*  66 */     int mtuSize = CrossSideManager.get().getMtuSize();
/*     */     
/*  68 */     OpusEncoder.Application application = OpusEncoder.Application.VOIP;
/*  69 */     if (mode != null) {
/*  70 */       switch (mode) {
/*     */         case VOIP:
/*  72 */           application = OpusEncoder.Application.VOIP;
/*     */           break;
/*     */         case AUDIO:
/*  75 */           application = OpusEncoder.Application.AUDIO;
/*     */           break;
/*     */         case RESTRICTED_LOWDELAY:
/*  78 */           application = OpusEncoder.Application.LOW_DELAY;
/*     */           break;
/*     */       } 
/*     */     
/*     */     }
/*  83 */     if (CrossSideManager.get().useNatives()) {
/*  84 */       OpusEncoder encoder = createNativeEncoder(mtuSize, application);
/*  85 */       if (encoder != null) {
/*  86 */         return encoder;
/*     */       }
/*     */     } 
/*     */     
/*  90 */     return new JavaOpusEncoderImpl(48000, 960, mtuSize, application);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static OpusDecoder createNativeDecoder() {
/*  95 */     if (!nativeOpusCompatible) {
/*  96 */       return null;
/*     */     }
/*     */     try {
/*  99 */       NativeOpusDecoderImpl decoder = new NativeOpusDecoderImpl(48000, 1);
/* 100 */       decoder.setFrameSize(960);
/* 101 */       return decoder;
/* 102 */     } catch (Throwable e) {
/* 103 */       nativeOpusCompatible = false;
/* 104 */       Voicechat.LOGGER.warn("Failed to load native Opus decoder - Falling back to Java Opus implementation", new Object[0]);
/* 105 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static OpusDecoder createDecoder() {
/* 110 */     if (CrossSideManager.get().useNatives()) {
/* 111 */       OpusDecoder decoder = createNativeDecoder();
/* 112 */       if (decoder != null) {
/* 113 */         return decoder;
/*     */       }
/*     */     } 
/* 116 */     return new JavaOpusDecoderImpl(48000, 960);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\opus\OpusManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */