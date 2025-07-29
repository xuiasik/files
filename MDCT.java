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
/*     */ class MDCT
/*     */ {
/*     */   static void clt_mdct_forward(MDCTLookup l, int[] input, int input_ptr, int[] output, int output_ptr, int[] window, int overlap, int shift, int stride) {
/*  46 */     FFTState st = l.kfft[shift];
/*     */     
/*  48 */     int trig_ptr = 0;
/*     */ 
/*     */     
/*  51 */     int scale_shift = st.scale_shift - 1;
/*  52 */     int scale = st.scale;
/*     */     
/*  54 */     int N = l.n;
/*  55 */     short[] trig = l.trig; int i;
/*  56 */     for (i = 0; i < shift; i++) {
/*  57 */       N >>= 1;
/*  58 */       trig_ptr += N;
/*     */     } 
/*  60 */     int N2 = N >> 1;
/*  61 */     int N4 = N >> 2;
/*     */     
/*  63 */     int[] f = new int[N2];
/*  64 */     int[] f2 = new int[N4 * 2];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     int xp1 = input_ptr + (overlap >> 1);
/*  71 */     int xp2 = input_ptr + N2 - 1 + (overlap >> 1);
/*  72 */     int j = 0;
/*  73 */     int wp1 = overlap >> 1;
/*  74 */     int wp2 = (overlap >> 1) - 1;
/*  75 */     for (i = 0; i < overlap + 3 >> 2; i++) {
/*     */       
/*  77 */       f[j++] = Inlines.MULT16_32_Q15(window[wp2], input[xp1 + N2]) + Inlines.MULT16_32_Q15(window[wp1], input[xp2]);
/*  78 */       f[j++] = Inlines.MULT16_32_Q15(window[wp1], input[xp1]) - Inlines.MULT16_32_Q15(window[wp2], input[xp2 - N2]);
/*  79 */       xp1 += 2;
/*  80 */       xp2 -= 2;
/*  81 */       wp1 += 2;
/*  82 */       wp2 -= 2;
/*     */     } 
/*  84 */     wp1 = 0;
/*  85 */     wp2 = overlap - 1;
/*  86 */     for (; i < N4 - (overlap + 3 >> 2); i++) {
/*     */       
/*  88 */       f[j++] = input[xp2];
/*  89 */       f[j++] = input[xp1];
/*  90 */       xp1 += 2;
/*  91 */       xp2 -= 2;
/*     */     } 
/*  93 */     for (; i < N4; i++) {
/*     */       
/*  95 */       f[j++] = Inlines.MULT16_32_Q15(window[wp2], input[xp2]) - Inlines.MULT16_32_Q15(window[wp1], input[xp1 - N2]);
/*  96 */       f[j++] = Inlines.MULT16_32_Q15(window[wp2], input[xp1]) + Inlines.MULT16_32_Q15(window[wp1], input[xp2 + N2]);
/*  97 */       xp1 += 2;
/*  98 */       xp2 -= 2;
/*  99 */       wp1 += 2;
/* 100 */       wp2 -= 2;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 105 */     int yp = 0;
/* 106 */     int t = trig_ptr;
/* 107 */     for (i = 0; i < N4; i++) {
/*     */ 
/*     */       
/* 110 */       short t0 = trig[t + i];
/* 111 */       short t1 = trig[t + N4 + i];
/* 112 */       int re = f[yp++];
/* 113 */       int im = f[yp++];
/* 114 */       int yr = KissFFT.S_MUL(re, t0) - KissFFT.S_MUL(im, t1);
/* 115 */       int yi = KissFFT.S_MUL(im, t0) + KissFFT.S_MUL(re, t1);
/* 116 */       f2[2 * st.bitrev[i]] = Inlines.PSHR32(Inlines.MULT16_32_Q16(scale, yr), scale_shift);
/* 117 */       f2[2 * st.bitrev[i] + 1] = Inlines.PSHR32(Inlines.MULT16_32_Q16(scale, yi), scale_shift);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 122 */     KissFFT.opus_fft_impl(st, f2, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     int fp = 0;
/* 128 */     int yp1 = output_ptr;
/* 129 */     int yp2 = output_ptr + stride * (N2 - 1);
/* 130 */     int k = trig_ptr;
/* 131 */     for (i = 0; i < N4; i++) {
/*     */       
/* 133 */       int yr = KissFFT.S_MUL(f2[fp + 1], trig[k + N4 + i]) - KissFFT.S_MUL(f2[fp], trig[k + i]);
/* 134 */       int yi = KissFFT.S_MUL(f2[fp], trig[k + N4 + i]) + KissFFT.S_MUL(f2[fp + 1], trig[k + i]);
/* 135 */       output[yp1] = yr;
/* 136 */       output[yp2] = yi;
/* 137 */       fp += 2;
/* 138 */       yp1 += 2 * stride;
/* 139 */       yp2 -= 2 * stride;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void clt_mdct_backward(MDCTLookup l, int[] input, int input_ptr, int[] output, int output_ptr, int[] window, int overlap, int shift, int stride) {
/* 148 */     int trig = 0;
/*     */ 
/*     */     
/* 151 */     int N = l.n; int i;
/* 152 */     for (i = 0; i < shift; i++) {
/* 153 */       N >>= 1;
/* 154 */       trig += N;
/*     */     } 
/* 156 */     int N2 = N >> 1;
/* 157 */     int N4 = N >> 2;
/*     */ 
/*     */ 
/*     */     
/* 161 */     int xp2 = input_ptr + stride * (N2 - 1);
/* 162 */     int yp = output_ptr + (overlap >> 1);
/* 163 */     short[] bitrev = (l.kfft[shift]).bitrev;
/* 164 */     int bitrav_ptr = 0;
/* 165 */     for (i = 0; i < N4; i++) {
/* 166 */       int rev = bitrev[bitrav_ptr++];
/* 167 */       int ypr = yp + 2 * rev;
/*     */       
/* 169 */       output[ypr + 1] = KissFFT.S_MUL(input[xp2], l.trig[trig + i]) + KissFFT.S_MUL(input[input_ptr], l.trig[trig + N4 + i]);
/* 170 */       output[ypr] = KissFFT.S_MUL(input[input_ptr], l.trig[trig + i]) - KissFFT.S_MUL(input[xp2], l.trig[trig + N4 + i]);
/*     */       
/* 172 */       input_ptr += 2 * stride;
/* 173 */       xp2 -= 2 * stride;
/*     */     } 
/*     */     
/* 176 */     KissFFT.opus_fft_impl(l.kfft[shift], output, output_ptr + (overlap >> 1));
/*     */ 
/*     */ 
/*     */     
/* 180 */     int yp0 = output_ptr + (overlap >> 1);
/* 181 */     int yp1 = output_ptr + (overlap >> 1) + N2 - 2;
/* 182 */     int t = trig;
/*     */ 
/*     */ 
/*     */     
/* 186 */     int tN4m1 = t + N4 - 1;
/* 187 */     int tN2m1 = t + N2 - 1;
/* 188 */     for (i = 0; i < N4 + 1 >> 1; i++) {
/*     */ 
/*     */ 
/*     */       
/* 192 */       int re = output[yp0 + 1];
/* 193 */       int im = output[yp0];
/* 194 */       short t0 = l.trig[t + i];
/* 195 */       short t1 = l.trig[t + N4 + i];
/*     */       
/* 197 */       int yr = KissFFT.S_MUL(re, t0) + KissFFT.S_MUL(im, t1);
/* 198 */       int yi = KissFFT.S_MUL(re, t1) - KissFFT.S_MUL(im, t0);
/*     */       
/* 200 */       re = output[yp1 + 1];
/* 201 */       im = output[yp1];
/* 202 */       output[yp0] = yr;
/* 203 */       output[yp1 + 1] = yi;
/* 204 */       t0 = l.trig[tN4m1 - i];
/* 205 */       t1 = l.trig[tN2m1 - i];
/*     */       
/* 207 */       yr = KissFFT.S_MUL(re, t0) + KissFFT.S_MUL(im, t1);
/* 208 */       yi = KissFFT.S_MUL(re, t1) - KissFFT.S_MUL(im, t0);
/* 209 */       output[yp1] = yr;
/* 210 */       output[yp0 + 1] = yi;
/* 211 */       yp0 += 2;
/* 212 */       yp1 -= 2;
/*     */     } 
/*     */ 
/*     */     
/* 216 */     int xp1 = output_ptr + overlap - 1;
/* 217 */     yp1 = output_ptr;
/* 218 */     int wp1 = 0;
/* 219 */     int wp2 = overlap - 1;
/*     */     
/* 221 */     for (i = 0; i < overlap / 2; i++) {
/* 222 */       int x1 = output[xp1];
/* 223 */       int x2 = output[yp1];
/* 224 */       output[yp1++] = Inlines.MULT16_32_Q15(window[wp2], x2) - Inlines.MULT16_32_Q15(window[wp1], x1);
/* 225 */       output[xp1--] = Inlines.MULT16_32_Q15(window[wp1], x2) + Inlines.MULT16_32_Q15(window[wp2], x1);
/* 226 */       wp1++;
/* 227 */       wp2--;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\MDCT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */