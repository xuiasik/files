/*     */ package de.maxhenkel.voicechat.plugins.impl.audiochannel;
/*     */ 
/*     */ import de.maxhenkel.voicechat.Voicechat;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.AudioChannel;
/*     */ import de.maxhenkel.voicechat.api.audiochannel.AudioPlayer;
/*     */ import de.maxhenkel.voicechat.api.opus.OpusEncoder;
/*     */ import de.maxhenkel.voicechat.debug.VoicechatUncaughtExceptionHandler;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ public class AudioPlayerImpl
/*     */   extends Thread
/*     */   implements AudioPlayer
/*     */ {
/*     */   private static final long FRAME_SIZE_NS = 20000000L;
/*     */   private final AudioChannel audioChannel;
/*     */   private final OpusEncoder encoder;
/*     */   private final Supplier<short[]> audioSupplier;
/*     */   private boolean started;
/*     */   @Nullable
/*     */   private Runnable onStopped;
/*     */   
/*     */   public AudioPlayerImpl(AudioChannel audioChannel, @Nonnull OpusEncoder encoder, Supplier<short[]> audioSupplier) {
/*  26 */     this.audioChannel = audioChannel;
/*  27 */     this.encoder = encoder;
/*  28 */     this.audioSupplier = audioSupplier;
/*  29 */     setDaemon(true);
/*  30 */     setName("AudioPlayer-" + audioChannel.getId());
/*  31 */     setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new VoicechatUncaughtExceptionHandler());
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPlaying() {
/*  36 */     if (this.started) {
/*     */       return;
/*     */     }
/*  39 */     start();
/*  40 */     this.started = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopPlaying() {
/*  45 */     interrupt();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/*  50 */     return this.started;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPlaying() {
/*  55 */     return isAlive();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStopped() {
/*  60 */     return (this.started && !isAlive());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOnStopped(Runnable onStopped) {
/*  65 */     this.onStopped = onStopped;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  70 */     int framePosition = 0;
/*     */     
/*  72 */     long startTime = System.nanoTime();
/*     */     
/*     */     short[] frame;
/*     */     
/*  76 */     while ((frame = this.audioSupplier.get()) != null) {
/*  77 */       if (frame.length != 960) {
/*  78 */         Voicechat.LOGGER.error("Got invalid audio frame size {}!={}", new Object[] { Integer.valueOf(frame.length), Integer.valueOf(960) });
/*     */         break;
/*     */       } 
/*  81 */       this.audioChannel.send(this.encoder.encode(frame));
/*  82 */       framePosition++;
/*  83 */       long waitTimestamp = startTime + framePosition * 20000000L;
/*     */       
/*  85 */       long waitNanos = waitTimestamp - System.nanoTime();
/*     */       
/*     */       try {
/*  88 */         if (waitNanos > 0L) {
/*  89 */           Thread.sleep(waitNanos / 1000000L, (int)(waitNanos % 1000000L));
/*     */         }
/*  91 */       } catch (InterruptedException e) {
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     this.encoder.close();
/*  97 */     this.audioChannel.flush();
/*     */     
/*  99 */     if (this.onStopped != null)
/* 100 */       this.onStopped.run(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\plugins\impl\audiochannel\AudioPlayerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */