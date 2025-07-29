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
/*     */ class TonalityAnalysisState
/*     */ {
/*     */   boolean enabled = false;
/*  40 */   final float[] angle = new float[240];
/*  41 */   final float[] d_angle = new float[240];
/*  42 */   final float[] d2_angle = new float[240];
/*  43 */   final int[] inmem = new int[720];
/*     */   
/*     */   int mem_fill;
/*  46 */   final float[] prev_band_tonality = new float[18];
/*     */   float prev_tonality;
/*  48 */   final float[][] E = Arrays.InitTwoDimensionalArrayFloat(8, 18);
/*  49 */   final float[] lowE = new float[18];
/*  50 */   final float[] highE = new float[18];
/*  51 */   final float[] meanE = new float[21];
/*  52 */   final float[] mem = new float[32];
/*  53 */   final float[] cmean = new float[8];
/*  54 */   final float[] std = new float[9];
/*     */   float music_prob;
/*     */   float Etracker;
/*     */   float lowECount;
/*     */   int E_count;
/*     */   int last_music;
/*     */   int last_transition;
/*     */   int count;
/*  62 */   final float[] subframe_mem = new float[3];
/*     */ 
/*     */ 
/*     */   
/*     */   int analysis_offset;
/*     */ 
/*     */   
/*  69 */   final float[] pspeech = new float[200];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   final float[] pmusic = new float[200];
/*     */   float speech_confidence;
/*     */   float music_confidence;
/*     */   int speech_confidence_count;
/*     */   int music_confidence_count;
/*     */   int write_pos;
/*     */   int read_pos;
/*     */   int read_subframe;
/*  83 */   final AnalysisInfo[] info = new AnalysisInfo[200];
/*     */   
/*     */   TonalityAnalysisState() {
/*  86 */     for (int c = 0; c < 200; c++) {
/*  87 */       this.info[c] = new AnalysisInfo();
/*     */     }
/*     */   }
/*     */   
/*     */   void Reset() {
/*  92 */     Arrays.MemSet(this.angle, 0.0F, 240);
/*  93 */     Arrays.MemSet(this.d_angle, 0.0F, 240);
/*  94 */     Arrays.MemSet(this.d2_angle, 0.0F, 240);
/*  95 */     Arrays.MemSet(this.inmem, 0, 720);
/*  96 */     this.mem_fill = 0;
/*  97 */     Arrays.MemSet(this.prev_band_tonality, 0.0F, 18);
/*  98 */     this.prev_tonality = 0.0F; int c;
/*  99 */     for (c = 0; c < 8; c++) {
/* 100 */       Arrays.MemSet(this.E[c], 0.0F, 18);
/*     */     }
/* 102 */     Arrays.MemSet(this.lowE, 0.0F, 18);
/* 103 */     Arrays.MemSet(this.highE, 0.0F, 18);
/* 104 */     Arrays.MemSet(this.meanE, 0.0F, 21);
/* 105 */     Arrays.MemSet(this.mem, 0.0F, 32);
/* 106 */     Arrays.MemSet(this.cmean, 0.0F, 8);
/* 107 */     Arrays.MemSet(this.std, 0.0F, 9);
/* 108 */     this.music_prob = 0.0F;
/* 109 */     this.Etracker = 0.0F;
/* 110 */     this.lowECount = 0.0F;
/* 111 */     this.E_count = 0;
/* 112 */     this.last_music = 0;
/* 113 */     this.last_transition = 0;
/* 114 */     this.count = 0;
/* 115 */     Arrays.MemSet(this.subframe_mem, 0.0F, 3);
/* 116 */     this.analysis_offset = 0;
/* 117 */     Arrays.MemSet(this.pspeech, 0.0F, 200);
/* 118 */     Arrays.MemSet(this.pmusic, 0.0F, 200);
/* 119 */     this.speech_confidence = 0.0F;
/* 120 */     this.music_confidence = 0.0F;
/* 121 */     this.speech_confidence_count = 0;
/* 122 */     this.music_confidence_count = 0;
/* 123 */     this.write_pos = 0;
/* 124 */     this.read_pos = 0;
/* 125 */     this.read_subframe = 0;
/* 126 */     for (c = 0; c < 200; c++)
/* 127 */       this.info[c].Reset(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\TonalityAnalysisState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */