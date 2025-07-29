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
/*     */ class Laplace
/*     */ {
/*     */   private static final int LAPLACE_LOG_MINP = 0;
/*     */   private static final long LAPLACE_MINP = 1L;
/*     */   private static final int LAPLACE_NMIN = 16;
/*     */   
/*     */   static long ec_laplace_get_freq1(long fs0, int decay) {
/*  48 */     long ft = Inlines.CapToUInt32(32736L - fs0);
/*  49 */     return Inlines.CapToUInt32(ft * (16384 - decay)) >> 15L;
/*     */   }
/*     */ 
/*     */   
/*     */   static void ec_laplace_encode(EntropyCoder enc, BoxedValueInt value, long fs, int decay) {
/*  54 */     int val = value.Val;
/*  55 */     long fl = 0L;
/*  56 */     if (val != 0) {
/*     */ 
/*     */       
/*  59 */       int s = 0 - ((val < 0) ? 1 : 0);
/*  60 */       val = val + s ^ s;
/*  61 */       fl = fs;
/*  62 */       fs = ec_laplace_get_freq1(fs, decay);
/*     */       
/*     */       int i;
/*  65 */       for (i = 1; fs > 0L && i < val; i++) {
/*  66 */         fs *= 2L;
/*  67 */         fl = Inlines.CapToUInt32(fl + fs + 2L);
/*  68 */         fs = Inlines.CapToUInt32(fs * decay >> 15L);
/*     */       } 
/*     */ 
/*     */       
/*  72 */       if (fs == 0L) {
/*     */ 
/*     */         
/*  75 */         int ndi_max = (int)(32768L - fl + 1L - 1L) >> 0;
/*  76 */         ndi_max = ndi_max - s >> 1;
/*  77 */         int di = Inlines.IMIN(val - i, ndi_max - 1);
/*  78 */         fl = Inlines.CapToUInt32(fl + (2 * di + 1 + s) * 1L);
/*  79 */         fs = Inlines.IMIN(1L, 32768L - fl);
/*  80 */         value.Val = i + di + s ^ s;
/*     */       } else {
/*  82 */         fs++;
/*  83 */         fl += Inlines.CapToUInt32(fs & (s ^ 0xFFFFFFFF));
/*     */       } 
/*  85 */       Inlines.OpusAssert((fl + fs <= 32768L));
/*  86 */       Inlines.OpusAssert((fs > 0L));
/*     */     } 
/*     */     
/*  89 */     enc.encode_bin(fl, fl + fs, 15);
/*     */   }
/*     */   
/*     */   static int ec_laplace_decode(EntropyCoder dec, long fs, int decay) {
/*  93 */     int val = 0;
/*     */ 
/*     */     
/*  96 */     long fm = dec.decode_bin(15);
/*  97 */     long fl = 0L;
/*     */     
/*  99 */     if (fm >= fs) {
/* 100 */       val++;
/* 101 */       fl = fs;
/* 102 */       fs = ec_laplace_get_freq1(fs, decay) + 1L;
/*     */       
/* 104 */       while (fs > 1L && fm >= fl + 2L * fs) {
/* 105 */         fs *= 2L;
/* 106 */         fl = Inlines.CapToUInt32(fl + fs);
/* 107 */         fs = Inlines.CapToUInt32((fs - 2L) * decay >> 15L);
/* 108 */         fs++;
/* 109 */         val++;
/*     */       } 
/*     */       
/* 112 */       if (fs <= 1L) {
/*     */         
/* 114 */         int di = (int)(fm - fl) >> 1;
/* 115 */         val += di;
/* 116 */         fl = Inlines.CapToUInt32(fl + Inlines.CapToUInt32((2 * di) * 1L));
/*     */       } 
/* 118 */       if (fm < fl + fs) {
/* 119 */         val = -val;
/*     */       } else {
/* 121 */         fl = Inlines.CapToUInt32(fl + fs);
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     Inlines.OpusAssert((fl < 32768L));
/* 126 */     Inlines.OpusAssert((fs > 0L));
/* 127 */     Inlines.OpusAssert((fl <= fm));
/* 128 */     Inlines.OpusAssert((fm < Inlines.IMIN(fl + fs, 32768L)));
/*     */     
/* 130 */     dec.dec_update(fl, Inlines.IMIN(fl + fs, 32768L), 32768L);
/* 131 */     return val;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Laplace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */