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
/*     */ class SilkChannelDecoder
/*     */ {
/*  39 */   int prev_gain_Q16 = 0;
/*  40 */   final int[] exc_Q14 = new int[320];
/*  41 */   final int[] sLPC_Q14_buf = new int[16];
/*  42 */   final short[] outBuf = new short[480];
/*     */   
/*  44 */   int lagPrev = 0;
/*     */   
/*  46 */   byte LastGainIndex = 0;
/*     */   
/*  48 */   int fs_kHz = 0;
/*     */   
/*  50 */   int fs_API_hz = 0;
/*     */   
/*  52 */   int nb_subfr = 0;
/*     */   
/*  54 */   int frame_length = 0;
/*     */   
/*  56 */   int subfr_length = 0;
/*     */   
/*  58 */   int ltp_mem_length = 0;
/*     */   
/*  60 */   int LPC_order = 0;
/*     */   
/*  62 */   final short[] prevNLSF_Q15 = new short[16];
/*     */   
/*  64 */   int first_frame_after_reset = 0;
/*     */ 
/*     */   
/*     */   short[] pitch_lag_low_bits_iCDF;
/*     */ 
/*     */   
/*     */   short[] pitch_contour_iCDF;
/*     */   
/*  72 */   int nFramesDecoded = 0;
/*  73 */   int nFramesPerPacket = 0;
/*     */ 
/*     */   
/*  76 */   int ec_prevSignalType = 0;
/*  77 */   short ec_prevLagIndex = 0;
/*     */   
/*  79 */   final int[] VAD_flags = new int[3];
/*  80 */   int LBRR_flag = 0;
/*  81 */   final int[] LBRR_flags = new int[3];
/*     */   
/*  83 */   final SilkResamplerState resampler_state = new SilkResamplerState();
/*     */   
/*  85 */   NLSFCodebook psNLSF_CB = null;
/*     */ 
/*     */ 
/*     */   
/*  89 */   final SideInfoIndices indices = new SideInfoIndices();
/*     */ 
/*     */   
/*  92 */   final CNGState sCNG = new CNGState();
/*     */ 
/*     */   
/*  95 */   int lossCnt = 0;
/*  96 */   int prevSignalType = 0;
/*     */   
/*  98 */   final PLCStruct sPLC = new PLCStruct();
/*     */   
/*     */   void Reset() {
/* 101 */     this.prev_gain_Q16 = 0;
/* 102 */     Arrays.MemSet(this.exc_Q14, 0, 320);
/* 103 */     Arrays.MemSet(this.sLPC_Q14_buf, 0, 16);
/* 104 */     Arrays.MemSet(this.outBuf, (short)0, 480);
/* 105 */     this.lagPrev = 0;
/* 106 */     this.LastGainIndex = 0;
/* 107 */     this.fs_kHz = 0;
/* 108 */     this.fs_API_hz = 0;
/* 109 */     this.nb_subfr = 0;
/* 110 */     this.frame_length = 0;
/* 111 */     this.subfr_length = 0;
/* 112 */     this.ltp_mem_length = 0;
/* 113 */     this.LPC_order = 0;
/* 114 */     Arrays.MemSet(this.prevNLSF_Q15, (short)0, 16);
/* 115 */     this.first_frame_after_reset = 0;
/* 116 */     this.pitch_lag_low_bits_iCDF = null;
/* 117 */     this.pitch_contour_iCDF = null;
/* 118 */     this.nFramesDecoded = 0;
/* 119 */     this.nFramesPerPacket = 0;
/* 120 */     this.ec_prevSignalType = 0;
/* 121 */     this.ec_prevLagIndex = 0;
/* 122 */     Arrays.MemSet(this.VAD_flags, 0, 3);
/* 123 */     this.LBRR_flag = 0;
/* 124 */     Arrays.MemSet(this.LBRR_flags, 0, 3);
/* 125 */     this.resampler_state.Reset();
/* 126 */     this.psNLSF_CB = null;
/* 127 */     this.indices.Reset();
/* 128 */     this.sCNG.Reset();
/* 129 */     this.lossCnt = 0;
/* 130 */     this.prevSignalType = 0;
/* 131 */     this.sPLC.Reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int silk_init_decoder() {
/* 141 */     Reset();
/*     */ 
/*     */     
/* 144 */     this.first_frame_after_reset = 1;
/* 145 */     this.prev_gain_Q16 = 65536;
/*     */ 
/*     */     
/* 148 */     silk_CNG_Reset();
/*     */ 
/*     */     
/* 151 */     silk_PLC_Reset();
/*     */     
/* 153 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void silk_CNG_Reset() {
/* 163 */     int NLSF_step_Q15 = Inlines.silk_DIV32_16(32767, this.LPC_order + 1);
/* 164 */     int NLSF_acc_Q15 = 0;
/* 165 */     for (int i = 0; i < this.LPC_order; i++) {
/* 166 */       NLSF_acc_Q15 += NLSF_step_Q15;
/* 167 */       this.sCNG.CNG_smth_NLSF_Q15[i] = (short)NLSF_acc_Q15;
/*     */     } 
/* 169 */     this.sCNG.CNG_smth_Gain_Q16 = 0;
/* 170 */     this.sCNG.rand_seed = 3176576;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void silk_PLC_Reset() {
/* 178 */     this.sPLC.pitchL_Q8 = Inlines.silk_LSHIFT(this.frame_length, 7);
/* 179 */     this.sPLC.prevGain_Q16[0] = 65536;
/* 180 */     this.sPLC.prevGain_Q16[1] = 65536;
/* 181 */     this.sPLC.subfr_length = 20;
/* 182 */     this.sPLC.nb_subfr = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int silk_decoder_set_fs(int fs_kHz, int fs_API_Hz) {
/* 190 */     int ret = 0;
/*     */     
/* 192 */     Inlines.OpusAssert((fs_kHz == 8 || fs_kHz == 12 || fs_kHz == 16));
/* 193 */     Inlines.OpusAssert((this.nb_subfr == 4 || this.nb_subfr == 2));
/*     */ 
/*     */     
/* 196 */     this.subfr_length = Inlines.silk_SMULBB(5, fs_kHz);
/* 197 */     int frame_length = Inlines.silk_SMULBB(this.nb_subfr, this.subfr_length);
/*     */ 
/*     */     
/* 200 */     if (this.fs_kHz != fs_kHz || this.fs_API_hz != fs_API_Hz) {
/*     */       
/* 202 */       ret += Resampler.silk_resampler_init(this.resampler_state, Inlines.silk_SMULBB(fs_kHz, 1000), fs_API_Hz, 0);
/*     */       
/* 204 */       this.fs_API_hz = fs_API_Hz;
/*     */     } 
/*     */     
/* 207 */     if (this.fs_kHz != fs_kHz || frame_length != this.frame_length) {
/* 208 */       if (fs_kHz == 8) {
/* 209 */         if (this.nb_subfr == 4) {
/* 210 */           this.pitch_contour_iCDF = SilkTables.silk_pitch_contour_NB_iCDF;
/*     */         } else {
/* 212 */           this.pitch_contour_iCDF = SilkTables.silk_pitch_contour_10_ms_NB_iCDF;
/*     */         } 
/* 214 */       } else if (this.nb_subfr == 4) {
/* 215 */         this.pitch_contour_iCDF = SilkTables.silk_pitch_contour_iCDF;
/*     */       } else {
/* 217 */         this.pitch_contour_iCDF = SilkTables.silk_pitch_contour_10_ms_iCDF;
/*     */       } 
/* 219 */       if (this.fs_kHz != fs_kHz) {
/* 220 */         this.ltp_mem_length = Inlines.silk_SMULBB(20, fs_kHz);
/* 221 */         if (fs_kHz == 8 || fs_kHz == 12) {
/* 222 */           this.LPC_order = 10;
/* 223 */           this.psNLSF_CB = SilkTables.silk_NLSF_CB_NB_MB;
/*     */         } else {
/* 225 */           this.LPC_order = 16;
/* 226 */           this.psNLSF_CB = SilkTables.silk_NLSF_CB_WB;
/*     */         } 
/* 228 */         if (fs_kHz == 16) {
/* 229 */           this.pitch_lag_low_bits_iCDF = SilkTables.silk_uniform8_iCDF;
/* 230 */         } else if (fs_kHz == 12) {
/* 231 */           this.pitch_lag_low_bits_iCDF = SilkTables.silk_uniform6_iCDF;
/* 232 */         } else if (fs_kHz == 8) {
/* 233 */           this.pitch_lag_low_bits_iCDF = SilkTables.silk_uniform4_iCDF;
/*     */         } else {
/*     */           
/* 236 */           Inlines.OpusAssert(false);
/*     */         } 
/* 238 */         this.first_frame_after_reset = 1;
/* 239 */         this.lagPrev = 100;
/* 240 */         this.LastGainIndex = 10;
/* 241 */         this.prevSignalType = 0;
/* 242 */         Arrays.MemSet(this.outBuf, (short)0, 480);
/* 243 */         Arrays.MemSet(this.sLPC_Q14_buf, 0, 16);
/*     */       } 
/*     */       
/* 246 */       this.fs_kHz = fs_kHz;
/* 247 */       this.frame_length = frame_length;
/*     */     } 
/*     */ 
/*     */     
/* 251 */     Inlines.OpusAssert((this.frame_length > 0 && this.frame_length <= 320));
/*     */     
/* 253 */     return ret;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int silk_decode_frame(EntropyCoder psRangeDec, short[] pOut, int pOut_ptr, BoxedValueInt pN, int lostFlag, int condCoding) {
/* 271 */     SilkDecoderControl thisCtrl = new SilkDecoderControl();
/* 272 */     int ret = 0;
/*     */     
/* 274 */     int L = this.frame_length;
/* 275 */     thisCtrl.LTP_scale_Q14 = 0;
/*     */ 
/*     */     
/* 278 */     Inlines.OpusAssert((L > 0 && L <= 320));
/*     */     
/* 280 */     if (lostFlag == 0 || (lostFlag == 2 && this.LBRR_flags[this.nFramesDecoded] == 1)) {
/*     */       
/* 282 */       short[] pulses = new short[L + 16 - 1 & 0xFFFFFFF0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 290 */       DecodeIndices.silk_decode_indices(this, psRangeDec, this.nFramesDecoded, lostFlag, condCoding);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 299 */       DecodePulses.silk_decode_pulses(psRangeDec, pulses, this.indices.signalType, this.indices.quantOffsetType, this.frame_length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 309 */       DecodeParameters.silk_decode_parameters(this, thisCtrl, condCoding);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 318 */       DecodeCore.silk_decode_core(this, thisCtrl, pOut, pOut_ptr, pulses);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 327 */       PLC.silk_PLC(this, thisCtrl, pOut, pOut_ptr, 0);
/*     */       
/* 329 */       this.lossCnt = 0;
/* 330 */       this.prevSignalType = this.indices.signalType;
/* 331 */       Inlines.OpusAssert((this.prevSignalType >= 0 && this.prevSignalType <= 2));
/*     */ 
/*     */       
/* 334 */       this.first_frame_after_reset = 0;
/*     */     } else {
/*     */       
/* 337 */       PLC.silk_PLC(this, thisCtrl, pOut, pOut_ptr, 1);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 347 */     Inlines.OpusAssert((this.ltp_mem_length >= this.frame_length));
/* 348 */     int mv_len = this.ltp_mem_length - this.frame_length;
/* 349 */     Arrays.MemMove(this.outBuf, this.frame_length, 0, mv_len);
/* 350 */     System.arraycopy(pOut, pOut_ptr, this.outBuf, mv_len, this.frame_length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     CNG.silk_CNG(this, thisCtrl, pOut, pOut_ptr, L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 368 */     PLC.silk_PLC_glue_frames(this, pOut, pOut_ptr, L);
/*     */ 
/*     */     
/* 371 */     this.lagPrev = thisCtrl.pitchL[this.nb_subfr - 1];
/*     */ 
/*     */     
/* 374 */     pN.Val = L;
/*     */     
/* 376 */     return ret;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SilkChannelDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */