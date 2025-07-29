/*     */ package de.maxhenkel.voicechat.concentus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CeltMode
/*     */ {
/*  39 */   int Fs = 0;
/*  40 */   int overlap = 0;
/*     */   
/*  42 */   int nbEBands = 0;
/*  43 */   int effEBands = 0;
/*  44 */   int[] preemph = new int[] { 0, 0, 0, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   short[] eBands = null;
/*     */   
/*  51 */   int maxLM = 0;
/*  52 */   int nbShortMdcts = 0;
/*  53 */   int shortMdctSize = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   int nbAllocVectors = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   short[] allocVectors = null;
/*  64 */   short[] logN = null;
/*     */   
/*  66 */   int[] window = null;
/*  67 */   MDCTLookup mdct = new MDCTLookup();
/*  68 */   PulseCache cache = new PulseCache();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   static final CeltMode mode48000_960_120 = new CeltMode();
/*     */   
/*     */   static {
/*  76 */     mode48000_960_120.Fs = 48000;
/*  77 */     mode48000_960_120.overlap = 120;
/*  78 */     mode48000_960_120.nbEBands = 21;
/*  79 */     mode48000_960_120.effEBands = 21;
/*  80 */     mode48000_960_120.preemph = new int[] { 27853, 0, 4096, 8192 };
/*  81 */     mode48000_960_120.eBands = CeltTables.eband5ms;
/*  82 */     mode48000_960_120.maxLM = 3;
/*  83 */     mode48000_960_120.nbShortMdcts = 8;
/*  84 */     mode48000_960_120.shortMdctSize = 120;
/*  85 */     mode48000_960_120.nbAllocVectors = 11;
/*  86 */     mode48000_960_120.allocVectors = CeltTables.band_allocation;
/*  87 */     mode48000_960_120.logN = CeltTables.logN400;
/*  88 */     mode48000_960_120.window = CeltTables.window120;
/*  89 */     mode48000_960_120.mdct = new MDCTLookup();
/*     */     
/*  91 */     mode48000_960_120.mdct.n = 1920;
/*  92 */     mode48000_960_120.mdct.maxshift = 3;
/*  93 */     mode48000_960_120.mdct.kfft = new FFTState[] { CeltTables.fft_state48000_960_0, CeltTables.fft_state48000_960_1, CeltTables.fft_state48000_960_2, CeltTables.fft_state48000_960_3 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     mode48000_960_120.mdct.trig = CeltTables.mdct_twiddles960;
/* 100 */     mode48000_960_120.cache = new PulseCache();
/* 101 */     mode48000_960_120.cache.size = 392;
/* 102 */     mode48000_960_120.cache.index = CeltTables.cache_index50;
/* 103 */     mode48000_960_120.cache.bits = CeltTables.cache_bits50;
/* 104 */     mode48000_960_120.cache.caps = CeltTables.cache_caps50;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\CeltMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */