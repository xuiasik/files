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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SilkEncoderControl
/*     */ {
/*  44 */   final int[] Gains_Q16 = new int[4];
/*  45 */   final short[][] PredCoef_Q12 = Arrays.InitTwoDimensionalArrayShort(2, 16);
/*     */   
/*  47 */   final short[] LTPCoef_Q14 = new short[20];
/*  48 */   int LTP_scale_Q14 = 0;
/*  49 */   final int[] pitchL = new int[4];
/*     */ 
/*     */   
/*  52 */   final short[] AR1_Q13 = new short[64];
/*  53 */   final short[] AR2_Q13 = new short[64];
/*  54 */   final int[] LF_shp_Q14 = new int[4];
/*     */   
/*  56 */   final int[] GainsPre_Q14 = new int[4];
/*  57 */   final int[] HarmBoost_Q14 = new int[4];
/*  58 */   final int[] Tilt_Q14 = new int[4];
/*  59 */   final int[] HarmShapeGain_Q14 = new int[4];
/*  60 */   int Lambda_Q10 = 0;
/*  61 */   int input_quality_Q14 = 0;
/*  62 */   int coding_quality_Q14 = 0;
/*     */ 
/*     */   
/*  65 */   int sparseness_Q8 = 0;
/*  66 */   int predGain_Q16 = 0;
/*  67 */   int LTPredCodGain_Q7 = 0;
/*     */ 
/*     */   
/*  70 */   final int[] ResNrg = new int[4];
/*     */ 
/*     */   
/*  73 */   final int[] ResNrgQ = new int[4];
/*     */ 
/*     */   
/*  76 */   final int[] GainsUnq_Q16 = new int[4];
/*  77 */   byte lastGainIndexPrev = 0;
/*     */   
/*     */   void Reset() {
/*  80 */     Arrays.MemSet(this.Gains_Q16, 0, 4);
/*  81 */     Arrays.MemSet(this.PredCoef_Q12[0], (short)0, 16);
/*  82 */     Arrays.MemSet(this.PredCoef_Q12[1], (short)0, 16);
/*  83 */     Arrays.MemSet(this.LTPCoef_Q14, (short)0, 20);
/*  84 */     this.LTP_scale_Q14 = 0;
/*  85 */     Arrays.MemSet(this.pitchL, 0, 4);
/*  86 */     Arrays.MemSet(this.AR1_Q13, (short)0, 64);
/*  87 */     Arrays.MemSet(this.AR2_Q13, (short)0, 64);
/*  88 */     Arrays.MemSet(this.LF_shp_Q14, 0, 4);
/*  89 */     Arrays.MemSet(this.GainsPre_Q14, 0, 4);
/*  90 */     Arrays.MemSet(this.HarmBoost_Q14, 0, 4);
/*  91 */     Arrays.MemSet(this.Tilt_Q14, 0, 4);
/*  92 */     Arrays.MemSet(this.HarmShapeGain_Q14, 0, 4);
/*  93 */     this.Lambda_Q10 = 0;
/*  94 */     this.input_quality_Q14 = 0;
/*  95 */     this.coding_quality_Q14 = 0;
/*  96 */     this.sparseness_Q8 = 0;
/*  97 */     this.predGain_Q16 = 0;
/*  98 */     this.LTPredCodGain_Q7 = 0;
/*  99 */     Arrays.MemSet(this.ResNrg, 0, 4);
/* 100 */     Arrays.MemSet(this.ResNrgQ, 0, 4);
/* 101 */     Arrays.MemSet(this.GainsUnq_Q16, 0, 4);
/* 102 */     this.lastGainIndexPrev = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SilkEncoderControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */