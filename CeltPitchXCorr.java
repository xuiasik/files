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
/*     */ 
/*     */ class CeltPitchXCorr
/*     */ {
/*     */   static int pitch_xcorr(int[] _x, int[] _y, int[] xcorr, int len, int max_pitch) {
/*  46 */     int maxcorr = 1;
/*  47 */     Inlines.OpusAssert((max_pitch > 0));
/*  48 */     BoxedValueInt sum0 = new BoxedValueInt(0);
/*  49 */     BoxedValueInt sum1 = new BoxedValueInt(0);
/*  50 */     BoxedValueInt sum2 = new BoxedValueInt(0);
/*  51 */     BoxedValueInt sum3 = new BoxedValueInt(0); int i;
/*  52 */     for (i = 0; i < max_pitch - 3; i += 4) {
/*  53 */       sum0.Val = 0;
/*  54 */       sum1.Val = 0;
/*  55 */       sum2.Val = 0;
/*  56 */       sum3.Val = 0;
/*  57 */       Kernels.xcorr_kernel(_x, _y, i, sum0, sum1, sum2, sum3, len);
/*  58 */       xcorr[i] = sum0.Val;
/*  59 */       xcorr[i + 1] = sum1.Val;
/*  60 */       xcorr[i + 2] = sum2.Val;
/*  61 */       xcorr[i + 3] = sum3.Val;
/*  62 */       sum0.Val = Inlines.MAX32(sum0.Val, sum1.Val);
/*  63 */       sum2.Val = Inlines.MAX32(sum2.Val, sum3.Val);
/*  64 */       sum0.Val = Inlines.MAX32(sum0.Val, sum2.Val);
/*  65 */       maxcorr = Inlines.MAX32(maxcorr, sum0.Val);
/*     */     } 
/*     */     
/*  68 */     for (; i < max_pitch; i++) {
/*  69 */       int inner_sum = Kernels.celt_inner_prod(_x, 0, _y, i, len);
/*  70 */       xcorr[i] = inner_sum;
/*  71 */       maxcorr = Inlines.MAX32(maxcorr, inner_sum);
/*     */     } 
/*  73 */     return maxcorr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int pitch_xcorr(short[] _x, int _x_ptr, short[] _y, int _y_ptr, int[] xcorr, int len, int max_pitch) {
/*  85 */     int maxcorr = 1;
/*  86 */     Inlines.OpusAssert((max_pitch > 0));
/*  87 */     BoxedValueInt sum0 = new BoxedValueInt(0);
/*  88 */     BoxedValueInt sum1 = new BoxedValueInt(0);
/*  89 */     BoxedValueInt sum2 = new BoxedValueInt(0);
/*  90 */     BoxedValueInt sum3 = new BoxedValueInt(0); int i;
/*  91 */     for (i = 0; i < max_pitch - 3; i += 4) {
/*  92 */       sum0.Val = 0;
/*  93 */       sum1.Val = 0;
/*  94 */       sum2.Val = 0;
/*  95 */       sum3.Val = 0;
/*  96 */       Kernels.xcorr_kernel(_x, _x_ptr, _y, _y_ptr + i, sum0, sum1, sum2, sum3, len);
/*     */       
/*  98 */       xcorr[i] = sum0.Val;
/*  99 */       xcorr[i + 1] = sum1.Val;
/* 100 */       xcorr[i + 2] = sum2.Val;
/* 101 */       xcorr[i + 3] = sum3.Val;
/* 102 */       sum0.Val = Inlines.MAX32(sum0.Val, sum1.Val);
/* 103 */       sum2.Val = Inlines.MAX32(sum2.Val, sum3.Val);
/* 104 */       sum0.Val = Inlines.MAX32(sum0.Val, sum2.Val);
/* 105 */       maxcorr = Inlines.MAX32(maxcorr, sum0.Val);
/*     */     } 
/*     */     
/* 108 */     for (; i < max_pitch; i++) {
/* 109 */       int inner_sum = Kernels.celt_inner_prod(_x, _x_ptr, _y, _y_ptr + i, len);
/* 110 */       xcorr[i] = inner_sum;
/* 111 */       maxcorr = Inlines.MAX32(maxcorr, inner_sum);
/*     */     } 
/* 113 */     return maxcorr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int pitch_xcorr(short[] _x, short[] _y, int[] xcorr, int len, int max_pitch) {
/* 123 */     int maxcorr = 1;
/* 124 */     Inlines.OpusAssert((max_pitch > 0));
/* 125 */     BoxedValueInt sum0 = new BoxedValueInt(0);
/* 126 */     BoxedValueInt sum1 = new BoxedValueInt(0);
/* 127 */     BoxedValueInt sum2 = new BoxedValueInt(0);
/* 128 */     BoxedValueInt sum3 = new BoxedValueInt(0); int i;
/* 129 */     for (i = 0; i < max_pitch - 3; i += 4) {
/* 130 */       sum0.Val = 0;
/* 131 */       sum1.Val = 0;
/* 132 */       sum2.Val = 0;
/* 133 */       sum3.Val = 0;
/* 134 */       Kernels.xcorr_kernel(_x, 0, _y, i, sum0, sum1, sum2, sum3, len);
/*     */       
/* 136 */       xcorr[i] = sum0.Val;
/* 137 */       xcorr[i + 1] = sum1.Val;
/* 138 */       xcorr[i + 2] = sum2.Val;
/* 139 */       xcorr[i + 3] = sum3.Val;
/* 140 */       sum0.Val = Inlines.MAX32(sum0.Val, sum1.Val);
/* 141 */       sum2.Val = Inlines.MAX32(sum2.Val, sum3.Val);
/* 142 */       sum0.Val = Inlines.MAX32(sum0.Val, sum2.Val);
/* 143 */       maxcorr = Inlines.MAX32(maxcorr, sum0.Val);
/*     */     } 
/*     */     
/* 146 */     for (; i < max_pitch; i++) {
/* 147 */       int inner_sum = Kernels.celt_inner_prod(_x, _y, i, len);
/* 148 */       xcorr[i] = inner_sum;
/* 149 */       maxcorr = Inlines.MAX32(maxcorr, inner_sum);
/*     */     } 
/* 151 */     return maxcorr;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\CeltPitchXCorr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */