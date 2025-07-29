/*     */ package de.maxhenkel.voicechat.gui.widgets;
/*     */ import de.maxhenkel.voicechat.Voicechat;
/*     */ import de.maxhenkel.voicechat.VoicechatClient;
/*     */ import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
/*     */ import de.maxhenkel.voicechat.voice.client.MicActivator;
/*     */ import de.maxhenkel.voicechat.voice.client.MicThread;
/*     */ import de.maxhenkel.voicechat.voice.client.MicrophoneActivationType;
/*     */ import de.maxhenkel.voicechat.voice.client.MicrophoneException;
/*     */ import de.maxhenkel.voicechat.voice.client.SoundManager;
/*     */ import de.maxhenkel.voicechat.voice.client.speaker.Speaker;
/*     */ import de.maxhenkel.voicechat.voice.client.speaker.SpeakerException;
/*     */ import de.maxhenkel.voicechat.voice.common.Utils;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.class_124;
/*     */ import net.minecraft.class_2561;
/*     */ import net.minecraft.class_2588;
/*     */ import net.minecraft.class_2960;
/*     */ import net.minecraft.class_437;
/*     */ import net.minecraft.class_4587;
/*     */ 
/*     */ public class MicTestButton extends ToggleImageButton implements ImageButton.TooltipSupplier {
/*  22 */   private static final class_2960 MICROPHONE = new class_2960("voicechat", "textures/icons/microphone_button.png");
/*  23 */   private static final class_2561 TEST_DISABLED = (class_2561)new class_2588("message.voicechat.mic_test.disabled");
/*  24 */   private static final class_2561 TEST_ENABLED = (class_2561)new class_2588("message.voicechat.mic_test.enabled");
/*  25 */   private static final class_2561 TEST_UNAVAILABLE = (class_2561)(new class_2588("message.voicechat.mic_test_unavailable")).method_27692(class_124.field_1061);
/*     */   
/*     */   private boolean micActive;
/*     */   @Nullable
/*     */   private VoiceThread voiceThread;
/*     */   private final MicListener micListener;
/*     */   @Nullable
/*     */   private final ClientVoicechat client;
/*     */   
/*     */   public MicTestButton(int xIn, int yIn, MicListener micListener) {
/*  35 */     super(xIn, yIn, MICROPHONE, (Supplier<Boolean>)null, (ImageButton.PressAction)null, (ImageButton.TooltipSupplier)null);
/*  36 */     this.micListener = micListener;
/*  37 */     this.client = ClientManager.getClient();
/*  38 */     this.field_22763 = (this.client == null || this.client.getSoundManager() != null);
/*     */     
/*  40 */     this.stateSupplier = (() -> Boolean.valueOf(!this.micActive));
/*  41 */     this.tooltipSupplier = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25394(class_4587 matrixStack, int x, int y, float partialTicks) {
/*  46 */     super.method_25394(matrixStack, x, y, partialTicks);
/*  47 */     if (this.field_22764 && this.voiceThread != null) {
/*  48 */       this.voiceThread.updateLastRender();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldRenderTooltip() {
/*  54 */     return false;
/*     */   }
/*     */   
/*     */   public void setMicActive(boolean micActive) {
/*  58 */     this.micActive = micActive;
/*     */   }
/*     */   
/*     */   public boolean method_25367() {
/*  62 */     return this.field_22762;
/*     */   }
/*     */ 
/*     */   
/*     */   public void method_25306() {
/*  67 */     setMicActive(!this.micActive);
/*  68 */     if (this.micActive) {
/*  69 */       close();
/*     */       try {
/*  71 */         this.voiceThread = new VoiceThread();
/*  72 */         this.voiceThread.start();
/*  73 */       } catch (Exception e) {
/*  74 */         setMicActive(false);
/*  75 */         this.field_22763 = false;
/*  76 */         Voicechat.LOGGER.error("Microphone error", new Object[] { e });
/*     */       } 
/*     */     } else {
/*  79 */       close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void close() {
/*  84 */     if (this.voiceThread != null) {
/*  85 */       this.voiceThread.close();
/*  86 */       this.voiceThread = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/*  91 */     close();
/*  92 */     setMicActive(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTooltip(ImageButton button, class_4587 matrices, int mouseX, int mouseY) {
/*  97 */     class_437 screen = this.mc.field_1755;
/*  98 */     if (screen == null) {
/*     */       return;
/*     */     }
/* 101 */     if (!this.field_22763) {
/* 102 */       screen.method_25424(matrices, TEST_UNAVAILABLE, mouseX, mouseY);
/*     */       return;
/*     */     } 
/* 105 */     if (this.micActive) {
/* 106 */       screen.method_25424(matrices, TEST_ENABLED, mouseX, mouseY);
/*     */     } else {
/* 108 */       screen.method_25424(matrices, TEST_DISABLED, mouseX, mouseY);
/*     */     } 
/*     */   }
/*     */   public static interface MicListener {
/*     */     void onMicValue(double param1Double); }
/*     */   private class VoiceThread extends Thread { private final MicActivator micActivator;
/*     */     private final Speaker speaker;
/*     */     private boolean running;
/*     */     private long lastRender;
/*     */     private MicThread micThread;
/*     */     private boolean usesOwnMicThread;
/*     */     @Nullable
/*     */     private SoundManager ownSoundManager;
/*     */     
/*     */     public VoiceThread() throws SpeakerException, MicrophoneException {
/*     */       SoundManager soundManager;
/* 124 */       this.running = true;
/* 125 */       setDaemon(true);
/* 126 */       setName("VoiceTestingThread");
/* 127 */       setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new VoicechatUncaughtExceptionHandler());
/*     */       
/* 129 */       this.micActivator = new MicActivator();
/*     */       
/* 131 */       this.micThread = (MicTestButton.this.client != null) ? MicTestButton.this.client.getMicThread() : null;
/* 132 */       if (this.micThread == null) {
/* 133 */         this.micThread = new MicThread(MicTestButton.this.client, null);
/* 134 */         this.usesOwnMicThread = true;
/*     */       } 
/*     */ 
/*     */       
/* 138 */       if (MicTestButton.this.client == null) {
/* 139 */         soundManager = new SoundManager((String)VoicechatClient.CLIENT_CONFIG.speaker.get());
/* 140 */         this.ownSoundManager = soundManager;
/*     */       } else {
/* 142 */         soundManager = MicTestButton.this.client.getSoundManager();
/*     */       } 
/*     */       
/* 145 */       if (soundManager == null) {
/* 146 */         throw new SpeakerException("No sound manager");
/*     */       }
/*     */       
/* 149 */       this.speaker = SpeakerManager.createSpeaker(soundManager, null);
/*     */       
/* 151 */       updateLastRender();
/* 152 */       setMicLocked(true);
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 157 */       while (this.running && 
/* 158 */         System.currentTimeMillis() - this.lastRender <= 500L) {
/*     */ 
/*     */         
/* 161 */         short[] buff = this.micThread.pollMic();
/* 162 */         if (buff == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 166 */         MicTestButton.this.micListener.onMicValue(Utils.dbToPerc(Utils.getHighestAudioLevel(buff)));
/*     */         
/* 168 */         if (((MicrophoneActivationType)VoicechatClient.CLIENT_CONFIG.microphoneActivationType.get()).equals(MicrophoneActivationType.VOICE)) {
/* 169 */           if (this.micActivator.push(buff, a -> { 
/*     */               }))
/* 171 */             play(buff); 
/*     */           continue;
/*     */         } 
/* 174 */         this.micActivator.stopActivating();
/* 175 */         play(buff);
/*     */       } 
/*     */ 
/*     */       
/* 179 */       this.speaker.close();
/* 180 */       setMicLocked(false);
/* 181 */       MicTestButton.this.micListener.onMicValue(0.0D);
/* 182 */       if (this.usesOwnMicThread) {
/* 183 */         this.micThread.close();
/*     */       }
/* 185 */       if (this.ownSoundManager != null) {
/* 186 */         this.ownSoundManager.close();
/*     */       }
/* 188 */       MicTestButton.this.setMicActive(false);
/* 189 */       Voicechat.LOGGER.info("Mic test audio channel closed", new Object[0]);
/*     */     }
/*     */     
/*     */     private void play(short[] buff) {
/* 193 */       this.speaker.play(buff, ((Double)VoicechatClient.CLIENT_CONFIG.voiceChatVolume.get()).floatValue(), null);
/*     */     }
/*     */     
/*     */     public void updateLastRender() {
/* 197 */       this.lastRender = System.currentTimeMillis();
/*     */     }
/*     */     
/*     */     private void setMicLocked(boolean locked) {
/* 201 */       this.micThread.setMicrophoneLocked(locked);
/*     */     }
/*     */     
/*     */     public void close() {
/* 205 */       if (!this.running) {
/*     */         return;
/*     */       }
/* 208 */       Voicechat.LOGGER.info("Stopping mic test audio channel", new Object[0]);
/* 209 */       this.running = false;
/*     */       try {
/* 211 */         join();
/* 212 */       } catch (InterruptedException e) {
/* 213 */         Voicechat.LOGGER.warn("Failed to close microphone", new Object[] { e });
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\gui\widgets\MicTestButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */