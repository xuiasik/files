/*      */ package de.maxhenkel.voicechat.concentus;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class Inlines
/*      */ {
/*      */   static void OpusAssert(boolean condition) {
/*   39 */     if (!condition) {
/*   40 */       throw new AssertionError();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static void OpusAssert(boolean condition, String message) {
/*   46 */     if (!condition) {
/*   47 */       throw new AssertionError(message);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static long CapToUInt32(long val) {
/*   53 */     return 0xFFFFFFFFL & (int)val;
/*      */   }
/*      */   
/*      */   static long CapToUInt32(int val) {
/*   57 */     return val;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int MULT16_16SU(int a, int b) {
/*   64 */     return (short)a * (b & 0xFFFF);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int MULT16_32_Q16(short a, int b) {
/*   70 */     return ADD32(MULT16_16(a, SHR(b, 16)), SHR(MULT16_16SU(a, b & 0xFFFF), 16));
/*      */   }
/*      */   
/*      */   static int MULT16_32_Q16(int a, int b) {
/*   74 */     return ADD32(MULT16_16(a, SHR(b, 16)), SHR(MULT16_16SU(a, b & 0xFFFF), 16));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int MULT16_32_P16(short a, int b) {
/*   80 */     return ADD32(MULT16_16(a, SHR(b, 16)), PSHR(MULT16_16SU(a, b & 0xFFFF), 16));
/*      */   }
/*      */   
/*      */   static int MULT16_32_P16(int a, int b) {
/*   84 */     return ADD32(MULT16_16(a, SHR(b, 16)), PSHR(MULT16_16SU(a, b & 0xFFFF), 16));
/*      */   }
/*      */ 
/*      */   
/*      */   static int MULT16_32_Q15(short a, int b) {
/*   89 */     return (a * (b >> 16) << 1) + (a * (b & 0xFFFF) >> 15);
/*      */   }
/*      */ 
/*      */   
/*      */   static int MULT16_32_Q15(int a, int b) {
/*   94 */     return (a * (b >> 16) << 1) + (a * (b & 0xFFFF) >> 15);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int MULT32_32_Q31(int a, int b) {
/*  101 */     return ADD32(ADD32(SHL(MULT16_16(SHR(a, 16), SHR(b, 16)), 1), SHR(MULT16_16SU(SHR(a, 16), b & 0xFFFF), 15)), SHR(MULT16_16SU(SHR(b, 16), a & 0xFFFF), 15));
/*      */   }
/*      */ 
/*      */   
/*      */   static short QCONST16(float x, int bits) {
/*  106 */     return (short)(int)(0.5D + (x * (1 << bits)));
/*      */   }
/*      */ 
/*      */   
/*      */   static int QCONST32(float x, int bits) {
/*  111 */     return (int)(0.5D + (x * (1 << bits)));
/*      */   }
/*      */ 
/*      */   
/*      */   static short NEG16(short x) {
/*  116 */     return (short)(0 - x);
/*      */   }
/*      */   
/*      */   static int NEG16(int x) {
/*  120 */     return 0 - x;
/*      */   }
/*      */ 
/*      */   
/*      */   static int NEG32(int x) {
/*  125 */     return 0 - x;
/*      */   }
/*      */ 
/*      */   
/*      */   static short EXTRACT16(int x) {
/*  130 */     return (short)x;
/*      */   }
/*      */ 
/*      */   
/*      */   static int EXTEND32(short x) {
/*  135 */     return x;
/*      */   }
/*      */   
/*      */   static int EXTEND32(int x) {
/*  139 */     return x;
/*      */   }
/*      */ 
/*      */   
/*      */   static short SHR16(short a, int shift) {
/*  144 */     return (short)(a >> shift);
/*      */   }
/*      */   
/*      */   static int SHR16(int a, int shift) {
/*  148 */     return a >> shift;
/*      */   }
/*      */ 
/*      */   
/*      */   static short SHL16(short a, int shift) {
/*  153 */     return (short)((a & 0xFFFF) << shift);
/*      */   }
/*      */   
/*      */   static int SHL16(int a, int shift) {
/*  157 */     return (int)(0xFFFFFFFFFFFFFFFFL & a << shift);
/*      */   }
/*      */ 
/*      */   
/*      */   static int SHR32(int a, int shift) {
/*  162 */     return a >> shift;
/*      */   }
/*      */ 
/*      */   
/*      */   static int SHL32(int a, int shift) {
/*  167 */     return (int)(0xFFFFFFFFFFFFFFFFL & a << shift);
/*      */   }
/*      */ 
/*      */   
/*      */   static int PSHR32(int a, int shift) {
/*  172 */     return SHR32(a + (EXTEND32(1) << shift >> 1), shift);
/*      */   }
/*      */   
/*      */   static short PSHR16(short a, int shift) {
/*  176 */     return SHR16((short)(a + (1 << shift >> 1)), shift);
/*      */   }
/*      */   
/*      */   static int PSHR16(int a, int shift) {
/*  180 */     return SHR32(a + (1 << shift >> 1), shift);
/*      */   }
/*      */ 
/*      */   
/*      */   static int VSHR32(int a, int shift) {
/*  185 */     return (shift > 0) ? SHR32(a, shift) : SHL32(a, -shift);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int SHR(int a, int shift) {
/*  190 */     return a >> shift;
/*      */   }
/*      */   
/*      */   private static int SHL(int a, int shift) {
/*  194 */     return SHL32(a, shift);
/*      */   }
/*      */   
/*      */   private static int SHR(short a, int shift) {
/*  198 */     return a >> shift;
/*      */   }
/*      */   
/*      */   private static int SHL(short a, int shift) {
/*  202 */     return SHL32(a, shift);
/*      */   }
/*      */   
/*      */   private static int PSHR(int a, int shift) {
/*  206 */     return SHR(a + (EXTEND32(1) << shift >> 1), shift);
/*      */   }
/*      */   
/*      */   static int SATURATE(int x, int a) {
/*  210 */     return (x > a) ? a : ((x < -a) ? -a : x);
/*      */   }
/*      */   
/*      */   static short SATURATE16(int x) {
/*  214 */     return EXTRACT16((x > 32767) ? 32767 : ((x < -32768) ? -32768 : x));
/*      */   }
/*      */ 
/*      */   
/*      */   static short ROUND16(short x, short a) {
/*  219 */     return EXTRACT16(PSHR32(x, a));
/*      */   }
/*      */   
/*      */   static int ROUND16(int x, int a) {
/*  223 */     return PSHR32(x, a);
/*      */   }
/*      */   
/*      */   static int PDIV32(int a, int b) {
/*  227 */     return a / b;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static short HALF16(short x) {
/*  233 */     return SHR16(x, 1);
/*      */   }
/*      */   
/*      */   static int HALF16(int x) {
/*  237 */     return SHR32(x, 1);
/*      */   }
/*      */   
/*      */   static int HALF32(int x) {
/*  241 */     return SHR32(x, 1);
/*      */   }
/*      */ 
/*      */   
/*      */   static short ADD16(short a, short b) {
/*  246 */     return (short)(a + b);
/*      */   }
/*      */   
/*      */   static int ADD16(int a, int b) {
/*  250 */     return a + b;
/*      */   }
/*      */ 
/*      */   
/*      */   static short SUB16(short a, short b) {
/*  255 */     return (short)(a - b);
/*      */   }
/*      */   
/*      */   static int SUB16(int a, int b) {
/*  259 */     return a - b;
/*      */   }
/*      */ 
/*      */   
/*      */   static int ADD32(int a, int b) {
/*  264 */     return a + b;
/*      */   }
/*      */ 
/*      */   
/*      */   static int SUB32(int a, int b) {
/*  269 */     return a - b;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static short MULT16_16_16(short a, short b) {
/*  275 */     return (short)(a * b);
/*      */   }
/*      */   
/*      */   static int MULT16_16_16(int a, int b) {
/*  279 */     return a * b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int MULT16_16(int a, int b) {
/*  286 */     return a * b;
/*      */   }
/*      */   
/*      */   static int MULT16_16(short a, short b) {
/*  290 */     return a * b;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int MAC16_16(short c, short a, short b) {
/*  296 */     return c + a * b;
/*      */   }
/*      */   
/*      */   static int MAC16_16(int c, short a, short b) {
/*  300 */     return c + a * b;
/*      */   }
/*      */   
/*      */   static int MAC16_16(int c, int a, int b) {
/*  304 */     return c + a * b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int MAC16_32_Q15(int c, short a, short b) {
/*  312 */     return ADD32(c, ADD32(MULT16_16(a, SHR(b, 15)), SHR(MULT16_16(a, b & Short.MAX_VALUE), 15)));
/*      */   }
/*      */   
/*      */   static int MAC16_32_Q15(int c, int a, int b) {
/*  316 */     return ADD32(c, ADD32(MULT16_16(a, SHR(b, 15)), SHR(MULT16_16(a, b & 0x7FFF), 15)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int MAC16_32_Q16(int c, short a, short b) {
/*  323 */     return ADD32(c, ADD32(MULT16_16(a, SHR(b, 16)), SHR(MULT16_16SU(a, b & 0xFFFF), 16)));
/*      */   }
/*      */   
/*      */   static int MAC16_32_Q16(int c, int a, int b) {
/*  327 */     return ADD32(c, ADD32(MULT16_16(a, SHR(b, 16)), SHR(MULT16_16SU(a, b & 0xFFFF), 16)));
/*      */   }
/*      */ 
/*      */   
/*      */   static int MULT16_16_Q11_32(short a, short b) {
/*  332 */     return SHR(MULT16_16(a, b), 11);
/*      */   }
/*      */   
/*      */   static int MULT16_16_Q11_32(int a, int b) {
/*  336 */     return SHR(MULT16_16(a, b), 11);
/*      */   }
/*      */ 
/*      */   
/*      */   static short MULT16_16_Q11(short a, short b) {
/*  341 */     return (short)SHR(MULT16_16(a, b), 11);
/*      */   }
/*      */   
/*      */   static int MULT16_16_Q11(int a, int b) {
/*  345 */     return SHR(MULT16_16(a, b), 11);
/*      */   }
/*      */ 
/*      */   
/*      */   static short MULT16_16_Q13(short a, short b) {
/*  350 */     return (short)SHR(MULT16_16(a, b), 13);
/*      */   }
/*      */   
/*      */   static int MULT16_16_Q13(int a, int b) {
/*  354 */     return SHR(MULT16_16(a, b), 13);
/*      */   }
/*      */ 
/*      */   
/*      */   static short MULT16_16_Q14(short a, short b) {
/*  359 */     return (short)SHR(MULT16_16(a, b), 14);
/*      */   }
/*      */   
/*      */   static int MULT16_16_Q14(int a, int b) {
/*  363 */     return SHR(MULT16_16(a, b), 14);
/*      */   }
/*      */ 
/*      */   
/*      */   static short MULT16_16_Q15(short a, short b) {
/*  368 */     return (short)SHR(MULT16_16(a, b), 15);
/*      */   }
/*      */   
/*      */   static int MULT16_16_Q15(int a, int b) {
/*  372 */     return SHR(MULT16_16(a, b), 15);
/*      */   }
/*      */ 
/*      */   
/*      */   static short MULT16_16_P13(short a, short b) {
/*  377 */     return (short)SHR(ADD32(4096, MULT16_16(a, b)), 13);
/*      */   }
/*      */   
/*      */   static int MULT16_16_P13(int a, int b) {
/*  381 */     return SHR(ADD32(4096, MULT16_16(a, b)), 13);
/*      */   }
/*      */ 
/*      */   
/*      */   static short MULT16_16_P14(short a, short b) {
/*  386 */     return (short)SHR(ADD32(8192, MULT16_16(a, b)), 14);
/*      */   }
/*      */   
/*      */   static int MULT16_16_P14(int a, int b) {
/*  390 */     return SHR(ADD32(8192, MULT16_16(a, b)), 14);
/*      */   }
/*      */ 
/*      */   
/*      */   static short MULT16_16_P15(short a, short b) {
/*  395 */     return (short)SHR(ADD32(16384, MULT16_16(a, b)), 15);
/*      */   }
/*      */   
/*      */   static int MULT16_16_P15(int a, int b) {
/*  399 */     return SHR(ADD32(16384, MULT16_16(a, b)), 15);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static short DIV32_16(int a, short b) {
/*  405 */     return (short)(a / b);
/*      */   }
/*      */   
/*      */   static int DIV32_16(int a, int b) {
/*  409 */     return a / b;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int DIV32(int a, int b) {
/*  415 */     return a / b;
/*      */   }
/*      */ 
/*      */   
/*      */   static short SAT16(int x) {
/*  420 */     return (x > 32767) ? Short.MAX_VALUE : ((x < -32768) ? Short.MIN_VALUE : (short)x);
/*      */   }
/*      */   
/*      */   static short SIG2WORD16(int x) {
/*  424 */     x = PSHR32(x, 12);
/*  425 */     x = MAX32(x, -32768);
/*  426 */     x = MIN32(x, 32767);
/*  427 */     return EXTRACT16(x);
/*      */   }
/*      */   
/*      */   static short MIN(short a, short b) {
/*  431 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static short MAX(short a, short b) {
/*  435 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static short MIN16(short a, short b) {
/*  439 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static short MAX16(short a, short b) {
/*  443 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static int MIN16(int a, int b) {
/*  447 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static int MAX16(int a, int b) {
/*  451 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static float MIN16(float a, float b) {
/*  455 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static float MAX16(float a, float b) {
/*  459 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static int MIN(int a, int b) {
/*  463 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static int MAX(int a, int b) {
/*  467 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static int IMIN(int a, int b) {
/*  471 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static long IMIN(long a, long b) {
/*  475 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static int IMAX(int a, int b) {
/*  479 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static int MIN32(int a, int b) {
/*  483 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static int MAX32(int a, int b) {
/*  487 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static float MIN32(float a, float b) {
/*  491 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static float MAX32(float a, float b) {
/*  495 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static int ABS16(int x) {
/*  499 */     return (x < 0) ? -x : x;
/*      */   }
/*      */   
/*      */   static float ABS16(float x) {
/*  503 */     return (x < 0.0F) ? -x : x;
/*      */   }
/*      */   
/*      */   static short ABS16(short x) {
/*  507 */     return (short)((x < 0) ? -x : x);
/*      */   }
/*      */   
/*      */   static int ABS32(int x) {
/*  511 */     return (x < 0) ? -x : x;
/*      */   }
/*      */   
/*      */   static int celt_udiv(int n, int d) {
/*  515 */     OpusAssert((d > 0));
/*  516 */     return n / d;
/*      */   }
/*      */   
/*      */   static int celt_sudiv(int n, int d) {
/*  520 */     OpusAssert((d > 0));
/*  521 */     return n / d;
/*      */   }
/*      */ 
/*      */   
/*      */   static int celt_div(int a, int b) {
/*  526 */     return MULT32_32_Q31(a, celt_rcp(b));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int celt_ilog2(int x) {
/*  533 */     OpusAssert((x > 0), "celt_ilog2() only defined for strictly positive numbers");
/*  534 */     return EC_ILOG(x) - 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int celt_zlog2(int x) {
/*  541 */     return (x <= 0) ? 0 : celt_ilog2(x);
/*      */   }
/*      */ 
/*      */   
/*      */   static int celt_maxabs16(int[] x, int x_ptr, int len) {
/*  546 */     int maxval = 0;
/*  547 */     int minval = 0;
/*  548 */     for (int i = x_ptr; i < len + x_ptr; i++) {
/*  549 */       maxval = MAX32(maxval, x[i]);
/*  550 */       minval = MIN32(minval, x[i]);
/*      */     } 
/*  552 */     return MAX32(EXTEND32(maxval), -EXTEND32(minval));
/*      */   }
/*      */ 
/*      */   
/*      */   static int celt_maxabs32(int[] x, int x_ptr, int len) {
/*  557 */     int maxval = 0;
/*  558 */     int minval = 0;
/*  559 */     for (int i = x_ptr; i < x_ptr + len; i++) {
/*  560 */       maxval = MAX32(maxval, x[i]);
/*  561 */       minval = MIN32(minval, x[i]);
/*      */     } 
/*  563 */     return MAX32(maxval, 0 - minval);
/*      */   }
/*      */ 
/*      */   
/*      */   static short celt_maxabs32(short[] x, int x_ptr, int len) {
/*  568 */     short maxval = 0;
/*  569 */     short minval = 0;
/*  570 */     for (int i = x_ptr; i < x_ptr + len; i++) {
/*  571 */       maxval = MAX16(maxval, x[i]);
/*  572 */       minval = MIN16(minval, x[i]);
/*      */     } 
/*  574 */     return MAX(maxval, (short)(0 - minval));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int FRAC_MUL16(int a, int b) {
/*  584 */     return 16384 + (short)a * (short)b >> 15;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int isqrt32(long _val) {
/*  601 */     int g = 0;
/*  602 */     int bshift = EC_ILOG(_val) - 1 >> 1;
/*  603 */     int b = 1 << bshift;
/*      */     
/*      */     while (true) {
/*  606 */       long t = ((g << 1) + b << bshift);
/*  607 */       if (t <= _val) {
/*  608 */         g += b;
/*  609 */         _val -= t;
/*      */       } 
/*  611 */       b >>= 1;
/*  612 */       bshift--;
/*  613 */       if (bshift < 0)
/*  614 */         return g; 
/*      */     } 
/*      */   }
/*  617 */   private static short[] sqrt_C = new short[] { 23175, 11561, -3011, 1699, -664 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int celt_sqrt(int x) {
/*  627 */     if (x == 0)
/*  628 */       return 0; 
/*  629 */     if (x >= 1073741824) {
/*  630 */       return 32767;
/*      */     }
/*  632 */     int k = (celt_ilog2(x) >> 1) - 7;
/*  633 */     x = VSHR32(x, 2 * k);
/*  634 */     short n = (short)(x - 32768);
/*  635 */     int rt = ADD16(sqrt_C[0], MULT16_16_Q15(n, ADD16(sqrt_C[1], MULT16_16_Q15(n, ADD16(sqrt_C[2], 
/*  636 */                 MULT16_16_Q15(n, ADD16(sqrt_C[3], MULT16_16_Q15(n, sqrt_C[4]))))))));
/*  637 */     rt = VSHR32(rt, 7 - k);
/*  638 */     return rt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int celt_rcp(int x) {
/*  648 */     OpusAssert((x > 0), "celt_rcp() only defined for positive values");
/*  649 */     int i = celt_ilog2(x);
/*      */     
/*  651 */     int n = VSHR32(x, i - 15) - 32768;
/*      */ 
/*      */ 
/*      */     
/*  655 */     int r = ADD16(30840, MULT16_16_Q15(-15420, n));
/*      */ 
/*      */ 
/*      */     
/*  659 */     r = SUB16(r, MULT16_16_Q15(r, 
/*  660 */           ADD16(MULT16_16_Q15(r, n), ADD16(r, -32768))));
/*      */ 
/*      */     
/*  663 */     r = SUB16(r, ADD16(1, MULT16_16_Q15(r, 
/*  664 */             ADD16(MULT16_16_Q15(r, n), ADD16(r, -32768)))));
/*      */ 
/*      */ 
/*      */     
/*  668 */     return VSHR32(EXTEND32(r), i - 16);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int celt_rsqrt_norm(int x) {
/*  680 */     int n = x - 32768;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  685 */     int r = ADD16(23557, MULT16_16_Q15(n, ADD16(-13490, MULT16_16_Q15(n, 6713))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  690 */     int r2 = MULT16_16_Q15(r, r);
/*  691 */     int y = SHL16(SUB16(ADD16(MULT16_16_Q15(r2, n), r2), 16384), 1);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  696 */     return ADD16(r, MULT16_16_Q15(r, MULT16_16_Q15(y, 
/*  697 */             SUB16(MULT16_16_Q15(y, 12288), 16384))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int frac_div32(int a, int b) {
/*  703 */     int shift = celt_ilog2(b) - 29;
/*  704 */     a = VSHR32(a, shift);
/*  705 */     b = VSHR32(b, shift);
/*      */     
/*  707 */     int rcp = ROUND16(celt_rcp(ROUND16(b, 16)), 3);
/*  708 */     int result = MULT16_32_Q15(rcp, a);
/*  709 */     int rem = PSHR32(a, 2) - MULT32_32_Q31(result, b);
/*  710 */     result = ADD32(result, SHL32(MULT16_32_Q15(rcp, rem), 2));
/*  711 */     if (result >= 536870912)
/*  712 */       return Integer.MAX_VALUE; 
/*  713 */     if (result <= -536870912) {
/*  714 */       return -2147483647;
/*      */     }
/*  716 */     return SHL32(result, 2);
/*      */   }
/*      */ 
/*      */   
/*  720 */   private static short log2_C0 = -6793;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int celt_log2(int x) {
/*  730 */     if (x == 0) {
/*  731 */       return -32767;
/*      */     }
/*  733 */     int i = celt_ilog2(x);
/*  734 */     int n = VSHR32(x, i - 15) - 32768 - 16384;
/*  735 */     int frac = ADD16(log2_C0, MULT16_16_Q15(n, ADD16(15746, MULT16_16_Q15(n, ADD16(-5217, MULT16_16_Q15(n, ADD16(2545, MULT16_16_Q15(n, -1401))))))));
/*  736 */     return SHL16((short)(i - 13), 10) + SHR16(frac, 4);
/*      */   }
/*      */ 
/*      */   
/*      */   static int celt_exp2_frac(int x) {
/*  741 */     int frac = SHL16(x, 4);
/*  742 */     return ADD16(16383, MULT16_16_Q15(frac, ADD16(22804, MULT16_16_Q15(frac, ADD16(14819, MULT16_16_Q15(10204, frac))))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int celt_exp2(int x) {
/*  751 */     int integer = SHR16(x, 10);
/*  752 */     if (integer > 14)
/*  753 */       return 2130706432; 
/*  754 */     if (integer < -15) {
/*  755 */       return 0;
/*      */     }
/*  757 */     int frac = (short)celt_exp2_frac((short)(x - SHL16((short)integer, 10)));
/*  758 */     return VSHR32(EXTEND32(frac), -integer - 2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int celt_atan01(int x) {
/*  764 */     return MULT16_16_P15(x, ADD32(32767, MULT16_16_P15(x, ADD32(-21, MULT16_16_P15(x, ADD32(-11943, MULT16_16_P15(4936, x)))))));
/*      */   }
/*      */ 
/*      */   
/*      */   static int celt_atan2p(int y, int x) {
/*  769 */     if (y < x) {
/*      */       
/*  771 */       int i = celt_div(SHL32(EXTEND32(y), 15), x);
/*  772 */       if (i >= 32767) {
/*  773 */         i = 32767;
/*      */       }
/*  775 */       return SHR32(celt_atan01(EXTRACT16(i)), 1);
/*      */     } 
/*      */     
/*  778 */     int arg = celt_div(SHL32(EXTEND32(x), 15), y);
/*  779 */     if (arg >= 32767) {
/*  780 */       arg = 32767;
/*      */     }
/*  782 */     return 25736 - SHR16(celt_atan01(EXTRACT16(arg)), 1);
/*      */   }
/*      */ 
/*      */   
/*      */   static int celt_cos_norm(int x) {
/*  787 */     x &= 0x1FFFF;
/*  788 */     if (x > SHL32(EXTEND32(1), 16)) {
/*  789 */       x = SUB32(SHL32(EXTEND32(1), 17), x);
/*      */     }
/*  791 */     if ((x & 0x7FFF) != 0) {
/*  792 */       if (x < SHL32(EXTEND32(1), 15)) {
/*  793 */         return _celt_cos_pi_2(EXTRACT16(x));
/*      */       }
/*  795 */       return NEG32(_celt_cos_pi_2(EXTRACT16(65536 - x)));
/*      */     } 
/*  797 */     if ((x & 0xFFFF) != 0)
/*  798 */       return 0; 
/*  799 */     if ((x & 0x1FFFF) != 0) {
/*  800 */       return -32767;
/*      */     }
/*  802 */     return 32767;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int _celt_cos_pi_2(int x) {
/*  809 */     int x2 = MULT16_16_P15(x, x);
/*  810 */     return ADD32(1, MIN32(32766, ADD32(SUB16(32767, x2), MULT16_16_P15(x2, ADD32(-7651, MULT16_16_P15(x2, ADD32(8277, MULT16_16_P15(-626, x2))))))));
/*      */   }
/*      */   
/*      */   static short FLOAT2INT16(float x) {
/*  814 */     x *= 32768.0F;
/*  815 */     if (x < -32768.0F) {
/*  816 */       x = -32768.0F;
/*      */     }
/*  818 */     if (x > 32767.0F) {
/*  819 */       x = 32767.0F;
/*      */     }
/*  821 */     return (short)(int)x;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_ROR32(int a32, int rot) {
/*  833 */     int m = 0 - rot;
/*  834 */     if (rot == 0)
/*  835 */       return a32; 
/*  836 */     if (rot < 0) {
/*  837 */       return a32 << m | a32 >> 32 - m;
/*      */     }
/*  839 */     return a32 << 32 - rot | a32 >> rot;
/*      */   }
/*      */ 
/*      */   
/*      */   static int silk_MUL(int a32, int b32) {
/*  844 */     int ret = a32 * b32;
/*  845 */     return ret;
/*      */   }
/*      */   
/*      */   static int silk_MLA(int a32, int b32, int c32) {
/*  849 */     int ret = silk_ADD32(a32, b32 * c32);
/*  850 */     OpusAssert((ret == a32 + b32 * c32));
/*  851 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_SMULTT(int a32, int b32) {
/*  861 */     return (a32 >> 16) * (b32 >> 16);
/*      */   }
/*      */   
/*      */   static int silk_SMLATT(int a32, int b32, int c32) {
/*  865 */     return silk_ADD32(a32, (b32 >> 16) * (c32 >> 16));
/*      */   }
/*      */   
/*      */   static long silk_SMLALBB(long a64, short b16, short c16) {
/*  869 */     return silk_ADD64(a64, (b16 * c16));
/*      */   }
/*      */   
/*      */   static long silk_SMULL(int a32, int b32) {
/*  873 */     return a32 * b32;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_ADD32_ovflw(int a, int b) {
/*  884 */     return (int)(a + b);
/*      */   }
/*      */   
/*      */   static int silk_ADD32_ovflw(long a, long b) {
/*  888 */     return (int)(a + b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_SUB32_ovflw(int a, int b) {
/*  899 */     return (int)(a - b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_MLA_ovflw(int a32, int b32, int c32) {
/*  910 */     return silk_ADD32_ovflw(a32, b32 * c32);
/*      */   }
/*      */   
/*      */   static int silk_SMLABB_ovflw(int a32, int b32, int c32) {
/*  914 */     return silk_ADD32_ovflw(a32, (short)b32 * (short)c32);
/*      */   }
/*      */   
/*      */   static int silk_SMULBB(int a32, int b32) {
/*  918 */     return (short)a32 * (short)b32;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_SMULWB(int a32, int b32) {
/*  928 */     return (int)(a32 * (short)b32 >> 16L);
/*      */   }
/*      */   
/*      */   static int silk_SMLABB(int a32, int b32, int c32) {
/*  932 */     return a32 + (short)b32 * (short)c32;
/*      */   }
/*      */   
/*      */   static int silk_DIV32_16(int a32, int b32) {
/*  936 */     return a32 / b32;
/*      */   }
/*      */   
/*      */   static int silk_DIV32(int a32, int b32) {
/*  940 */     return a32 / b32;
/*      */   }
/*      */   
/*      */   static short silk_ADD16(short a, short b) {
/*  944 */     short ret = (short)(a + b);
/*  945 */     return ret;
/*      */   }
/*      */   
/*      */   static int silk_ADD32(int a, int b) {
/*  949 */     int ret = a + b;
/*  950 */     return ret;
/*      */   }
/*      */   
/*      */   static long silk_ADD64(long a, long b) {
/*  954 */     long ret = a + b;
/*  955 */     OpusAssert((ret == silk_ADD_SAT64(a, b)));
/*  956 */     return ret;
/*      */   }
/*      */   
/*      */   static short silk_SUB16(short a, short b) {
/*  960 */     short ret = (short)(a - b);
/*  961 */     OpusAssert((ret == silk_SUB_SAT16(a, b)));
/*  962 */     return ret;
/*      */   }
/*      */   
/*      */   static int silk_SUB32(int a, int b) {
/*  966 */     int ret = a - b;
/*  967 */     OpusAssert((ret == silk_SUB_SAT32(a, b)));
/*  968 */     return ret;
/*      */   }
/*      */   
/*      */   static long silk_SUB64(long a, long b) {
/*  972 */     long ret = a - b;
/*  973 */     OpusAssert((ret == silk_SUB_SAT64(a, b)));
/*  974 */     return ret;
/*      */   }
/*      */   
/*      */   static int silk_SAT8(int a) {
/*  978 */     return (a > 127) ? 127 : ((a < -128) ? -128 : a);
/*      */   }
/*      */   
/*      */   static int silk_SAT16(int a) {
/*  982 */     return (a > 32767) ? 32767 : ((a < -32768) ? -32768 : a);
/*      */   }
/*      */   
/*      */   static int silk_SAT32(long a) {
/*  986 */     return (a > 2147483647L) ? Integer.MAX_VALUE : ((a < -2147483648L) ? Integer.MIN_VALUE : (int)a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static short silk_ADD_SAT16(short a16, short b16) {
/*  996 */     short res = (short)silk_SAT16(silk_ADD32(a16, b16));
/*  997 */     OpusAssert((res == silk_SAT16(a16 + b16)));
/*  998 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_ADD_SAT32(int a32, int b32) {
/* 1004 */     int res = ((a32 + b32 & 0xFFFFFFFF80000000L) == 0L) ? (((a32 & b32 & Integer.MIN_VALUE) != 0) ? Integer.MIN_VALUE : (a32 + b32)) : ((((a32 | b32) & Integer.MIN_VALUE) == 0) ? Integer.MAX_VALUE : (a32 + b32));
/* 1005 */     OpusAssert((res == silk_SAT32(a32 + b32)));
/* 1006 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static long silk_ADD_SAT64(long a64, long b64) {
/* 1013 */     long res = ((a64 + b64 & Long.MIN_VALUE) == 0L) ? (((a64 & b64 & Long.MIN_VALUE) != 0L) ? Long.MIN_VALUE : (a64 + b64)) : ((((a64 | b64) & Long.MIN_VALUE) == 0L) ? Long.MAX_VALUE : (a64 + b64));
/* 1014 */     return res;
/*      */   }
/*      */   
/*      */   static short silk_SUB_SAT16(short a16, short b16) {
/* 1018 */     short res = (short)silk_SAT16(silk_SUB32(a16, b16));
/* 1019 */     OpusAssert((res == silk_SAT16(a16 - b16)));
/* 1020 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_SUB_SAT32(int a32, int b32) {
/* 1026 */     int res = ((a32 - b32 & 0xFFFFFFFF80000000L) == 0L) ? (((a32 & (b32 ^ Integer.MIN_VALUE) & Integer.MIN_VALUE) != 0) ? Integer.MIN_VALUE : (a32 - b32)) : ((((a32 ^ Integer.MIN_VALUE) & b32 & Integer.MIN_VALUE) != 0) ? Integer.MAX_VALUE : (a32 - b32));
/* 1027 */     OpusAssert((res == silk_SAT32(a32 - b32)));
/* 1028 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static long silk_SUB_SAT64(long a64, long b64) {
/* 1035 */     long res = ((a64 - b64 & Long.MIN_VALUE) == 0L) ? (((a64 & (b64 ^ Long.MIN_VALUE) & Long.MIN_VALUE) != 0L) ? Long.MIN_VALUE : (a64 - b64)) : ((((a64 ^ Long.MIN_VALUE) & b64 & Long.MIN_VALUE) != 0L) ? Long.MAX_VALUE : (a64 - b64));
/* 1036 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte silk_ADD_POS_SAT8(byte a, byte b) {
/* 1048 */     return (byte)(((a + b & 0x80) != 0) ? Byte.MAX_VALUE : (a + b));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static short silk_ADD_POS_SAT16(short a, short b) {
/* 1058 */     return (short)(((a + b & 0x8000) != 0) ? Short.MAX_VALUE : (a + b));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_ADD_POS_SAT32(int a, int b) {
/* 1068 */     return ((a + b & Integer.MIN_VALUE) != 0) ? Integer.MAX_VALUE : (a + b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static long silk_ADD_POS_SAT64(long a, long b) {
/* 1078 */     return ((a + b & Long.MIN_VALUE) != 0L) ? Long.MAX_VALUE : (a + b);
/*      */   }
/*      */   
/*      */   static byte silk_LSHIFT8(byte a, int shift) {
/* 1082 */     byte ret = (byte)(a << shift);
/* 1083 */     return ret;
/*      */   }
/*      */   
/*      */   static short silk_LSHIFT16(short a, int shift) {
/* 1087 */     short ret = (short)(a << shift);
/* 1088 */     return ret;
/*      */   }
/*      */   
/*      */   static int silk_LSHIFT32(int a, int shift) {
/* 1092 */     int ret = a << shift;
/* 1093 */     return ret;
/*      */   }
/*      */   
/*      */   static long silk_LSHIFT64(long a, int shift) {
/* 1097 */     long ret = a << shift;
/* 1098 */     return ret;
/*      */   }
/*      */   
/*      */   static int silk_LSHIFT(int a, int shift) {
/* 1102 */     int ret = a << shift;
/* 1103 */     return ret;
/*      */   }
/*      */   
/*      */   static int silk_LSHIFT_ovflw(int a, int shift) {
/* 1107 */     return a << shift;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_LSHIFT_SAT32(int a, int shift) {
/* 1117 */     return silk_LSHIFT32(silk_LIMIT(a, silk_RSHIFT32(-2147483648, shift), silk_RSHIFT32(2147483647, shift)), shift);
/*      */   }
/*      */   
/*      */   static byte silk_RSHIFT8(byte a, int shift) {
/* 1121 */     return (byte)(a >> shift);
/*      */   }
/*      */   
/*      */   static short silk_RSHIFT16(short a, int shift) {
/* 1125 */     return (short)(a >> shift);
/*      */   }
/*      */   
/*      */   static int silk_RSHIFT32(int a, int shift) {
/* 1129 */     return a >> shift;
/*      */   }
/*      */   
/*      */   static int silk_RSHIFT(int a, int shift) {
/* 1133 */     return a >> shift;
/*      */   }
/*      */   
/*      */   static long silk_RSHIFT64(long a, int shift) {
/* 1137 */     return a >> shift;
/*      */   }
/*      */   
/*      */   static long silk_RSHIFT_uint(long a, int shift) {
/* 1141 */     return CapToUInt32(a) >> shift;
/*      */   }
/*      */   
/*      */   static int silk_ADD_LSHIFT(int a, int b, int shift) {
/* 1145 */     int ret = a + (b << shift);
/* 1146 */     return ret;
/*      */   }
/*      */ 
/*      */   
/*      */   static int silk_ADD_LSHIFT32(int a, int b, int shift) {
/* 1151 */     int ret = a + (b << shift);
/* 1152 */     return ret;
/*      */   }
/*      */ 
/*      */   
/*      */   static int silk_ADD_RSHIFT(int a, int b, int shift) {
/* 1157 */     int ret = a + (b >> shift);
/* 1158 */     return ret;
/*      */   }
/*      */ 
/*      */   
/*      */   static int silk_ADD_RSHIFT32(int a, int b, int shift) {
/* 1163 */     int ret = a + (b >> shift);
/* 1164 */     return ret;
/*      */   }
/*      */ 
/*      */   
/*      */   static long silk_ADD_RSHIFT_uint(long a, long b, int shift) {
/* 1169 */     long ret = CapToUInt32(a + (CapToUInt32(b) >> shift));
/* 1170 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_SUB_LSHIFT32(int a, int b, int shift) {
/* 1176 */     int ret = a - (b << shift);
/* 1177 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_SUB_RSHIFT32(int a, int b, int shift) {
/* 1183 */     int ret = a - (b >> shift);
/* 1184 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_RSHIFT_ROUND(int a, int shift) {
/* 1190 */     int ret = (shift == 1) ? ((a >> 1) + (a & 0x1)) : ((a >> shift - 1) + 1 >> 1);
/* 1191 */     return ret;
/*      */   }
/*      */ 
/*      */   
/*      */   static long silk_RSHIFT_ROUND64(long a, int shift) {
/* 1196 */     long ret = (shift == 1) ? ((a >> 1L) + (a & 0x1L)) : ((a >> shift - 1) + 1L >> 1L);
/* 1197 */     return ret;
/*      */   }
/*      */   
/*      */   static int silk_min(int a, int b) {
/* 1201 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static int silk_max(int a, int b) {
/* 1205 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static float silk_min(float a, float b) {
/* 1209 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static float silk_max(float a, float b) {
/* 1213 */     return (a > b) ? a : b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int SILK_CONST(float number, int scale) {
/* 1221 */     return (int)((number * (float)(1L << scale)) + 0.5D);
/*      */   }
/*      */ 
/*      */   
/*      */   static int silk_min_int(int a, int b) {
/* 1226 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static short silk_min_16(short a, short b) {
/* 1230 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static int silk_min_32(int a, int b) {
/* 1234 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   static long silk_min_64(long a, long b) {
/* 1238 */     return (a < b) ? a : b;
/*      */   }
/*      */ 
/*      */   
/*      */   static int silk_max_int(int a, int b) {
/* 1243 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static short silk_max_16(short a, short b) {
/* 1247 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static int silk_max_32(int a, int b) {
/* 1251 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static long silk_max_64(long a, long b) {
/* 1255 */     return (a > b) ? a : b;
/*      */   }
/*      */   
/*      */   static float silk_LIMIT(float a, float limit1, float limit2) {
/* 1259 */     return (limit1 > limit2) ? ((a > limit1) ? limit1 : ((a < limit2) ? limit2 : a)) : ((a > limit2) ? limit2 : ((a < limit1) ? limit1 : a));
/*      */   }
/*      */   
/*      */   static int silk_LIMIT(int a, int limit1, int limit2) {
/* 1263 */     return silk_LIMIT_32(a, limit1, limit2);
/*      */   }
/*      */   
/*      */   static int silk_LIMIT_int(int a, int limit1, int limit2) {
/* 1267 */     return silk_LIMIT_32(a, limit1, limit2);
/*      */   }
/*      */   
/*      */   static short silk_LIMIT_16(short a, short limit1, short limit2) {
/* 1271 */     return (limit1 > limit2) ? ((a > limit1) ? limit1 : ((a < limit2) ? limit2 : a)) : ((a > limit2) ? limit2 : ((a < limit1) ? limit1 : a));
/*      */   }
/*      */   
/*      */   static int silk_LIMIT_32(int a, int limit1, int limit2) {
/* 1275 */     return (limit1 > limit2) ? ((a > limit1) ? limit1 : ((a < limit2) ? limit2 : a)) : ((a > limit2) ? limit2 : ((a < limit1) ? limit1 : a));
/*      */   }
/*      */ 
/*      */   
/*      */   static int silk_abs(int a) {
/* 1280 */     return (a > 0) ? a : -a;
/*      */   }
/*      */   
/*      */   static int silk_abs_int16(int a) {
/* 1284 */     return (a ^ a >> 15) - (a >> 15);
/*      */   }
/*      */   
/*      */   static int silk_abs_int32(int a) {
/* 1288 */     return (a ^ a >> 31) - (a >> 31);
/*      */   }
/*      */   
/*      */   static long silk_abs_int64(long a) {
/* 1292 */     return (a > 0L) ? a : -a;
/*      */   }
/*      */   
/*      */   static long silk_sign(int a) {
/* 1296 */     return (a > 0) ? 1L : ((a < 0) ? -1L : 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_RAND(int seed) {
/* 1306 */     return silk_MLA_ovflw(907633515, seed, 196314165);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_SMMUL(int a32, int b32) {
/* 1316 */     return (int)silk_RSHIFT64(silk_SMULL(a32, b32), 32);
/*      */   }
/*      */ 
/*      */   
/*      */   static int silk_SMLAWT(int a32, int b32, int c32) {
/* 1321 */     int ret = a32 + (b32 >> 16) * (c32 >> 16) + ((b32 & 0xFFFF) * (c32 >> 16) >> 16);
/* 1322 */     return ret;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_DIV32_varQ(int a32, int b32, int Qres) {
/* 1336 */     OpusAssert((b32 != 0));
/* 1337 */     OpusAssert((Qres >= 0));
/*      */ 
/*      */     
/* 1340 */     int a_headrm = silk_CLZ32(silk_abs(a32)) - 1;
/* 1341 */     int a32_nrm = silk_LSHIFT(a32, a_headrm);
/*      */     
/* 1343 */     int b_headrm = silk_CLZ32(silk_abs(b32)) - 1;
/* 1344 */     int b32_nrm = silk_LSHIFT(b32, b_headrm);
/*      */ 
/*      */ 
/*      */     
/* 1348 */     int b32_inv = silk_DIV32_16(536870911, silk_RSHIFT(b32_nrm, 16));
/*      */ 
/*      */ 
/*      */     
/* 1352 */     int result = silk_SMULWB(a32_nrm, b32_inv);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1357 */     a32_nrm = silk_SUB32_ovflw(a32_nrm, silk_LSHIFT_ovflw(silk_SMMUL(b32_nrm, result), 3));
/*      */ 
/*      */ 
/*      */     
/* 1361 */     result = silk_SMLAWB(result, a32_nrm, b32_inv);
/*      */ 
/*      */ 
/*      */     
/* 1365 */     int lshift = 29 + a_headrm - b_headrm - Qres;
/* 1366 */     if (lshift < 0)
/* 1367 */       return silk_LSHIFT_SAT32(result, -lshift); 
/* 1368 */     if (lshift < 32) {
/* 1369 */       return silk_RSHIFT(result, lshift);
/*      */     }
/*      */     
/* 1372 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_INVERSE32_varQ(int b32, int Qres) {
/* 1386 */     OpusAssert((b32 != 0));
/* 1387 */     OpusAssert((Qres > 0));
/*      */ 
/*      */     
/* 1390 */     int b_headrm = silk_CLZ32(silk_abs(b32)) - 1;
/* 1391 */     int b32_nrm = silk_LSHIFT(b32, b_headrm);
/*      */ 
/*      */ 
/*      */     
/* 1395 */     int b32_inv = silk_DIV32_16(536870911, (short)silk_RSHIFT(b32_nrm, 16));
/*      */ 
/*      */ 
/*      */     
/* 1399 */     int result = silk_LSHIFT(b32_inv, 16);
/*      */ 
/*      */ 
/*      */     
/* 1403 */     int err_Q32 = silk_LSHIFT(536870912 - silk_SMULWB(b32_nrm, b32_inv), 3);
/*      */ 
/*      */ 
/*      */     
/* 1407 */     result = silk_SMLAWW(result, err_Q32, b32_inv);
/*      */ 
/*      */ 
/*      */     
/* 1411 */     int lshift = 61 - b_headrm - Qres;
/* 1412 */     if (lshift <= 0)
/* 1413 */       return silk_LSHIFT_SAT32(result, -lshift); 
/* 1414 */     if (lshift < 32) {
/* 1415 */       return silk_RSHIFT(result, lshift);
/*      */     }
/*      */     
/* 1418 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_SMLAWB(int a32, int b32, int c32) {
/* 1430 */     int ret = a32 + silk_SMULWB(b32, c32);
/* 1431 */     return ret;
/*      */   }
/*      */ 
/*      */   
/*      */   static int silk_SMULWT(int a32, int b32) {
/* 1436 */     return (a32 >> 16) * (b32 >> 16) + ((a32 & 0xFFFF) * (b32 >> 16) >> 16);
/*      */   }
/*      */ 
/*      */   
/*      */   static int silk_SMULBT(int a32, int b32) {
/* 1441 */     return (short)a32 * (b32 >> 16);
/*      */   }
/*      */ 
/*      */   
/*      */   static int silk_SMLABT(int a32, int b32, int c32) {
/* 1446 */     return a32 + (short)b32 * (c32 >> 16);
/*      */   }
/*      */ 
/*      */   
/*      */   static long silk_SMLAL(long a64, int b32, int c32) {
/* 1451 */     return silk_ADD64(a64, b32 * c32);
/*      */   }
/*      */   
/*      */   static int MatrixGetPointer(int row, int column, int N) {
/* 1455 */     return row * N + column;
/*      */   }
/*      */   
/*      */   static int MatrixGet(int[] Matrix_base_adr, int row, int column, int N) {
/* 1459 */     return Matrix_base_adr[row * N + column];
/*      */   }
/*      */   
/*      */   static short MatrixGet(short[] Matrix_base_adr, int row, int column, int N) {
/* 1463 */     return Matrix_base_adr[row * N + column];
/*      */   }
/*      */   
/*      */   static PitchAnalysisCore.silk_pe_stage3_vals MatrixGet(PitchAnalysisCore.silk_pe_stage3_vals[] Matrix_base_adr, int row, int column, int N) {
/* 1467 */     return Matrix_base_adr[row * N + column];
/*      */   }
/*      */   
/*      */   static int MatrixGet(int[] Matrix_base_adr, int matrix_ptr, int row, int column, int N) {
/* 1471 */     return Matrix_base_adr[matrix_ptr + row * N + column];
/*      */   }
/*      */   
/*      */   static short MatrixGet(short[] Matrix_base_adr, int matrix_ptr, int row, int column, int N) {
/* 1475 */     return Matrix_base_adr[matrix_ptr + row * N + column];
/*      */   }
/*      */   
/*      */   static void MatrixSet(int[] Matrix_base_adr, int matrix_ptr, int row, int column, int N, int value) {
/* 1479 */     Matrix_base_adr[matrix_ptr + row * N + column] = value;
/*      */   }
/*      */   
/*      */   static void MatrixSet(short[] Matrix_base_adr, int matrix_ptr, int row, int column, int N, short value) {
/* 1483 */     Matrix_base_adr[matrix_ptr + row * N + column] = value;
/*      */   }
/*      */   
/*      */   static void MatrixSet(int[] Matrix_base_adr, int row, int column, int N, int value) {
/* 1487 */     Matrix_base_adr[row * N + column] = value;
/*      */   }
/*      */   
/*      */   static void MatrixSet(short[] Matrix_base_adr, int row, int column, int N, short value) {
/* 1491 */     Matrix_base_adr[row * N + column] = value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_SMULWW(int a32, int b32) {
/* 1499 */     return silk_MLA(silk_SMULWB(a32, b32), a32, silk_RSHIFT_ROUND(b32, 16));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_SMLAWW(int a32, int b32, int c32) {
/* 1507 */     return silk_MLA(silk_SMLAWB(a32, b32, c32), b32, silk_RSHIFT_ROUND(c32, 16));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_CLZ64(long input) {
/* 1514 */     int in_upper = (int)silk_RSHIFT64(input, 32);
/* 1515 */     if (in_upper == 0)
/*      */     {
/* 1517 */       return 32 + silk_CLZ32((int)input);
/*      */     }
/*      */     
/* 1520 */     return silk_CLZ32(in_upper);
/*      */   }
/*      */ 
/*      */   
/*      */   static int silk_CLZ32(int in32) {
/* 1525 */     return (in32 == 0) ? 32 : (32 - EC_ILOG(in32));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_CLZ_FRAC(int input, BoxedValueInt lz, BoxedValueInt frac_Q7) {
/* 1535 */     int lzeros = silk_CLZ32(input);
/*      */     
/* 1537 */     lz.Val = lzeros;
/* 1538 */     frac_Q7.Val = silk_ROR32(input, 24 - lzeros) & 0x7F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_SQRT_APPROX(int x) {
/* 1553 */     if (x <= 0) {
/* 1554 */       return 0;
/*      */     }
/*      */     
/* 1557 */     BoxedValueInt boxed_lz = new BoxedValueInt(0);
/* 1558 */     BoxedValueInt boxed_frac_Q7 = new BoxedValueInt(0);
/* 1559 */     silk_CLZ_FRAC(x, boxed_lz, boxed_frac_Q7);
/* 1560 */     int lz = boxed_lz.Val;
/* 1561 */     int frac_Q7 = boxed_frac_Q7.Val;
/*      */     
/* 1563 */     if ((lz & 0x1) != 0) {
/* 1564 */       y = 32768;
/*      */     } else {
/* 1566 */       y = 46214;
/*      */     } 
/*      */ 
/*      */     
/* 1570 */     y >>= silk_RSHIFT(lz, 1);
/*      */ 
/*      */     
/* 1573 */     int y = silk_SMLAWB(y, y, silk_SMULBB(213, frac_Q7));
/*      */     
/* 1575 */     return y;
/*      */   }
/*      */   
/*      */   static int MUL32_FRAC_Q(int a32, int b32, int Q) {
/* 1579 */     return (int)silk_RSHIFT_ROUND64(silk_SMULL(a32, b32), Q);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_lin2log(int inLin) {
/* 1589 */     BoxedValueInt lz = new BoxedValueInt(0);
/* 1590 */     BoxedValueInt frac_Q7 = new BoxedValueInt(0);
/*      */     
/* 1592 */     silk_CLZ_FRAC(inLin, lz, frac_Q7);
/*      */ 
/*      */     
/* 1595 */     return silk_LSHIFT(31 - lz.Val, 7) + silk_SMLAWB(frac_Q7.Val, silk_MUL(frac_Q7.Val, 128 - frac_Q7.Val), 179);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_log2lin(int inLog_Q7) {
/* 1607 */     if (inLog_Q7 < 0)
/* 1608 */       return 0; 
/* 1609 */     if (inLog_Q7 >= 3967) {
/* 1610 */       return Integer.MAX_VALUE;
/*      */     }
/*      */     
/* 1613 */     int output = silk_LSHIFT(1, silk_RSHIFT(inLog_Q7, 7));
/* 1614 */     int frac_Q7 = inLog_Q7 & 0x7F;
/*      */     
/* 1616 */     if (inLog_Q7 < 2048) {
/*      */       
/* 1618 */       output = silk_ADD_RSHIFT32(output, silk_MUL(output, silk_SMLAWB(frac_Q7, silk_SMULBB(frac_Q7, 128 - frac_Q7), -174)), 7);
/*      */     } else {
/*      */       
/* 1621 */       output = silk_MLA(output, silk_RSHIFT(output, 7), silk_SMLAWB(frac_Q7, silk_SMULBB(frac_Q7, 128 - frac_Q7), -174));
/*      */     } 
/*      */     
/* 1624 */     return output;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_interpolate(short[] xi, short[] x0, short[] x1, int ifact_Q2, int d) {
/* 1643 */     OpusAssert((ifact_Q2 >= 0));
/* 1644 */     OpusAssert((ifact_Q2 <= 4));
/*      */     
/* 1646 */     for (int i = 0; i < d; i++) {
/* 1647 */       xi[i] = (short)silk_ADD_RSHIFT(x0[i], silk_SMULBB(x1[i] - x0[i], ifact_Q2), 2);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_inner_prod_aligned_scale(short[] inVec1, short[] inVec2, int scale, int len) {
/* 1664 */     int sum = 0;
/* 1665 */     for (int i = 0; i < len; i++) {
/* 1666 */       sum = silk_ADD_RSHIFT32(sum, silk_SMULBB(inVec1[i], inVec2[i]), scale);
/*      */     }
/*      */     
/* 1669 */     return sum;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_scale_copy_vector16(short[] data_out, int data_out_ptr, short[] data_in, int data_in_ptr, int gain_Q16, int dataSize) {
/* 1681 */     for (int i = 0; i < dataSize; i++) {
/* 1682 */       data_out[data_out_ptr + i] = (short)silk_SMULWB(gain_Q16, data_in[data_in_ptr + i]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void silk_scale_vector32_Q26_lshift_18(int[] data1, int data1_ptr, int gain_Q26, int dataSize) {
/* 1693 */     for (int i = data1_ptr; i < data1_ptr + dataSize; i++) {
/* 1694 */       data1[i] = (int)silk_RSHIFT64(silk_SMULL(data1[i], gain_Q26), 8);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_inner_prod(short[] inVec1, int inVec1_ptr, short[] inVec2, int inVec2_ptr, int len) {
/* 1708 */     int xy = 0;
/* 1709 */     for (int i = 0; i < len; i++) {
/* 1710 */       xy = MAC16_16(xy, inVec1[inVec1_ptr + i], inVec2[inVec2_ptr + i]);
/*      */     }
/* 1712 */     return xy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int silk_inner_prod_self(short[] inVec, int inVec_ptr, int len) {
/* 1721 */     int xy = 0;
/* 1722 */     for (int i = inVec_ptr; i < inVec_ptr + len; i++) {
/* 1723 */       xy = MAC16_16(xy, inVec[i], inVec[i]);
/*      */     }
/* 1725 */     return xy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static long silk_inner_prod16_aligned_64(short[] inVec1, int inVec1_ptr, short[] inVec2, int inVec2_ptr, int len) {
/* 1736 */     long sum = 0L;
/* 1737 */     for (int i = 0; i < len; i++) {
/* 1738 */       sum = silk_SMLALBB(sum, inVec1[inVec1_ptr + i], inVec2[inVec2_ptr + i]);
/*      */     }
/* 1740 */     return sum;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static long EC_MINI(long a, long b) {
/* 1750 */     return a + (b - a & ((b < a) ? -1L : 0L));
/*      */   }
/*      */   
/*      */   static int EC_ILOG(long x) {
/* 1754 */     if (x == 0L) {
/* 1755 */       return 1;
/*      */     }
/* 1757 */     x |= x >> 1L;
/* 1758 */     x |= x >> 2L;
/* 1759 */     x |= x >> 4L;
/* 1760 */     x |= x >> 8L;
/* 1761 */     x |= x >> 16L;
/* 1762 */     long y = x - (x >> 1L & 0x55555555L);
/* 1763 */     y = (y >> 2L & 0x33333333L) + (y & 0x33333333L);
/* 1764 */     y = (y >> 4L) + y & 0xF0F0F0FL;
/* 1765 */     y += y >> 8L;
/* 1766 */     y += y >> 16L;
/* 1767 */     y &= 0x3FL;
/* 1768 */     return (int)y;
/*      */   }
/*      */   
/*      */   static int abs(int a) {
/* 1772 */     if (a < 0) {
/* 1773 */       return 0 - a;
/*      */     }
/* 1775 */     return a;
/*      */   }
/*      */   
/*      */   static int SignedByteToUnsignedInt(byte b) {
/* 1779 */     return b & 0xFF;
/*      */   }
/*      */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\Inlines.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */