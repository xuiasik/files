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
/*     */ class CWRS
/*     */ {
/* 158 */   static final int[] CELT_PVQ_U_ROW = new int[] { 0, 176, 351, 525, 698, 870, 1041, 1131, 1178, 1207, 1226, 1240, 1248, 1254, 1257 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long CELT_PVQ_U(int _n, int _k) {
/* 165 */     return CeltTables.CELT_PVQ_U_DATA[CELT_PVQ_U_ROW[Inlines.IMIN(_n, _k)] + Inlines.IMAX(_n, _k)];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long CELT_PVQ_V(int _n, int _k) {
/* 172 */     return CELT_PVQ_U(_n, _k) + CELT_PVQ_U(_n, _k + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long icwrs(int _n, int[] _y) {
/* 179 */     Inlines.OpusAssert((_n >= 2));
/* 180 */     int j = _n - 1;
/* 181 */     long i = (_y[j] < 0) ? 1L : 0L;
/* 182 */     int k = Inlines.abs(_y[j]);
/*     */     while (true) {
/* 184 */       j--;
/* 185 */       i += CELT_PVQ_U(_n - j, k);
/* 186 */       k += Inlines.abs(_y[j]);
/* 187 */       if (_y[j] < 0) {
/* 188 */         i += CELT_PVQ_U(_n - j, k + 1);
/*     */       }
/* 190 */       if (j <= 0)
/* 191 */         return i; 
/*     */     } 
/*     */   }
/*     */   static void encode_pulses(int[] _y, int _n, int _k, EntropyCoder _enc) {
/* 195 */     Inlines.OpusAssert((_k > 0));
/* 196 */     _enc.enc_uint(icwrs(_n, _y), CELT_PVQ_V(_n, _k));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int cwrsi(int _n, int _k, long _i, int[] _y) {
/* 204 */     int yy = 0;
/* 205 */     int y_ptr = 0;
/* 206 */     Inlines.OpusAssert((_k > 0));
/* 207 */     Inlines.OpusAssert((_n > 1));
/*     */     
/* 209 */     while (_n > 2) {
/*     */ 
/*     */       
/* 212 */       if (_k >= _n) {
/*     */         
/* 214 */         int row = CELT_PVQ_U_ROW[_n];
/*     */         
/* 216 */         long l1 = CeltTables.CELT_PVQ_U_DATA[row + _k + 1];
/* 217 */         int i = 0 - ((_i >= l1) ? 1 : 0);
/* 218 */         _i -= Inlines.CapToUInt32(l1 & i);
/*     */         
/* 220 */         int j = _k;
/* 221 */         long q = CeltTables.CELT_PVQ_U_DATA[row + _n];
/*     */         
/* 223 */         if (q > _i) {
/* 224 */           Inlines.OpusAssert((l1 > q));
/* 225 */           _k = _n;
/*     */           
/*     */           do {
/* 228 */             l1 = CeltTables.CELT_PVQ_U_DATA[CELT_PVQ_U_ROW[--_k] + _n];
/* 229 */           } while (l1 > _i);
/*     */         } else {
/* 231 */           for (l1 = CeltTables.CELT_PVQ_U_DATA[row + _k]; l1 > _i; l1 = CeltTables.CELT_PVQ_U_DATA[row + _k]) {
/* 232 */             _k--;
/*     */           }
/*     */         } 
/*     */         
/* 236 */         _i -= l1;
/* 237 */         short s1 = (short)(j - _k + i ^ i);
/* 238 */         _y[y_ptr++] = s1;
/* 239 */         yy = Inlines.MAC16_16(yy, s1, s1);
/*     */       } else {
/*     */         
/* 242 */         long l1 = CeltTables.CELT_PVQ_U_DATA[CELT_PVQ_U_ROW[_k] + _n];
/* 243 */         long q = CeltTables.CELT_PVQ_U_DATA[CELT_PVQ_U_ROW[_k + 1] + _n];
/* 244 */         if (l1 <= _i && _i < q) {
/* 245 */           _i -= l1;
/* 246 */           _y[y_ptr++] = 0;
/*     */         } else {
/*     */           
/* 249 */           int i = 0 - ((_i >= q) ? 1 : 0);
/* 250 */           _i -= Inlines.CapToUInt32(q & i);
/*     */           
/* 252 */           int j = _k;
/*     */           do {
/* 254 */             l1 = CeltTables.CELT_PVQ_U_DATA[CELT_PVQ_U_ROW[--_k] + _n];
/* 255 */           } while (l1 > _i);
/*     */           
/* 257 */           _i -= l1;
/* 258 */           short s1 = (short)(j - _k + i ^ i);
/* 259 */           _y[y_ptr++] = s1;
/* 260 */           yy = Inlines.MAC16_16(yy, s1, s1);
/*     */         } 
/*     */       } 
/* 263 */       _n--;
/*     */     } 
/*     */ 
/*     */     
/* 267 */     long p = 2L * _k + 1L;
/* 268 */     int s = 0 - ((_i >= p) ? 1 : 0);
/* 269 */     _i -= Inlines.CapToUInt32(p & s);
/* 270 */     int k0 = _k;
/* 271 */     _k = (int)(_i + 1L >> 1L);
/* 272 */     if (_k != 0) {
/* 273 */       _i -= 2L * _k - 1L;
/*     */     }
/*     */     
/* 276 */     short val = (short)(k0 - _k + s ^ s);
/* 277 */     _y[y_ptr++] = val;
/* 278 */     yy = Inlines.MAC16_16(yy, val, val);
/*     */     
/* 280 */     s = -((int)_i);
/* 281 */     val = (short)(_k + s ^ s);
/* 282 */     _y[y_ptr] = val;
/* 283 */     yy = Inlines.MAC16_16(yy, val, val);
/* 284 */     return yy;
/*     */   }
/*     */   
/*     */   static int decode_pulses(int[] _y, int _n, int _k, EntropyCoder _dec) {
/* 288 */     return cwrsi(_n, _k, _dec.dec_uint(CELT_PVQ_V(_n, _k)), _y);
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\CWRS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */