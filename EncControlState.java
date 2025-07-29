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
/*     */ class EncControlState
/*     */ {
/*  40 */   int nChannelsAPI = 0;
/*     */ 
/*     */   
/*  43 */   int nChannelsInternal = 0;
/*     */ 
/*     */   
/*  46 */   int API_sampleRate = 0;
/*     */ 
/*     */   
/*  49 */   int maxInternalSampleRate = 0;
/*     */ 
/*     */   
/*  52 */   int minInternalSampleRate = 0;
/*     */ 
/*     */   
/*  55 */   int desiredInternalSampleRate = 0;
/*     */ 
/*     */   
/*  58 */   int payloadSize_ms = 0;
/*     */ 
/*     */   
/*  61 */   int bitRate = 0;
/*     */ 
/*     */   
/*  64 */   int packetLossPercentage = 0;
/*     */ 
/*     */   
/*  67 */   int complexity = 0;
/*     */ 
/*     */   
/*  70 */   int useInBandFEC = 0;
/*     */ 
/*     */   
/*  73 */   int useDTX = 0;
/*     */ 
/*     */   
/*  76 */   int useCBR = 0;
/*     */ 
/*     */   
/*  79 */   int maxBits = 0;
/*     */ 
/*     */   
/*  82 */   int toMono = 0;
/*     */ 
/*     */   
/*  85 */   int opusCanSwitch = 0;
/*     */ 
/*     */   
/*  88 */   int reducedDependency = 0;
/*     */ 
/*     */   
/*  91 */   int internalSampleRate = 0;
/*     */ 
/*     */   
/*  94 */   int allowBandwidthSwitch = 0;
/*     */ 
/*     */   
/*  97 */   int inWBmodeWithoutVariableLP = 0;
/*     */ 
/*     */   
/* 100 */   int stereoWidth_Q14 = 0;
/*     */ 
/*     */   
/* 103 */   int switchReady = 0;
/*     */   
/*     */   void Reset() {
/* 106 */     this.nChannelsAPI = 0;
/* 107 */     this.nChannelsInternal = 0;
/* 108 */     this.API_sampleRate = 0;
/* 109 */     this.maxInternalSampleRate = 0;
/* 110 */     this.minInternalSampleRate = 0;
/* 111 */     this.desiredInternalSampleRate = 0;
/* 112 */     this.payloadSize_ms = 0;
/* 113 */     this.bitRate = 0;
/* 114 */     this.packetLossPercentage = 0;
/* 115 */     this.complexity = 0;
/* 116 */     this.useInBandFEC = 0;
/* 117 */     this.useDTX = 0;
/* 118 */     this.useCBR = 0;
/* 119 */     this.maxBits = 0;
/* 120 */     this.toMono = 0;
/* 121 */     this.opusCanSwitch = 0;
/* 122 */     this.reducedDependency = 0;
/* 123 */     this.internalSampleRate = 0;
/* 124 */     this.allowBandwidthSwitch = 0;
/* 125 */     this.inWBmodeWithoutVariableLP = 0;
/* 126 */     this.stereoWidth_Q14 = 0;
/* 127 */     this.switchReady = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int check_control_input() {
/* 135 */     if ((this.API_sampleRate != 8000 && this.API_sampleRate != 12000 && this.API_sampleRate != 16000 && this.API_sampleRate != 24000 && this.API_sampleRate != 32000 && this.API_sampleRate != 44100 && this.API_sampleRate != 48000) || (this.desiredInternalSampleRate != 8000 && this.desiredInternalSampleRate != 12000 && this.desiredInternalSampleRate != 16000) || (this.maxInternalSampleRate != 8000 && this.maxInternalSampleRate != 12000 && this.maxInternalSampleRate != 16000) || (this.minInternalSampleRate != 8000 && this.minInternalSampleRate != 12000 && this.minInternalSampleRate != 16000) || this.minInternalSampleRate > this.desiredInternalSampleRate || this.maxInternalSampleRate < this.desiredInternalSampleRate || this.minInternalSampleRate > this.maxInternalSampleRate) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 154 */       Inlines.OpusAssert(false);
/* 155 */       return SilkError.SILK_ENC_FS_NOT_SUPPORTED;
/*     */     } 
/* 157 */     if (this.payloadSize_ms != 10 && this.payloadSize_ms != 20 && this.payloadSize_ms != 40 && this.payloadSize_ms != 60) {
/*     */ 
/*     */ 
/*     */       
/* 161 */       Inlines.OpusAssert(false);
/* 162 */       return SilkError.SILK_ENC_PACKET_SIZE_NOT_SUPPORTED;
/*     */     } 
/* 164 */     if (this.packetLossPercentage < 0 || this.packetLossPercentage > 100) {
/* 165 */       Inlines.OpusAssert(false);
/* 166 */       return SilkError.SILK_ENC_INVALID_LOSS_RATE;
/*     */     } 
/* 168 */     if (this.useDTX < 0 || this.useDTX > 1) {
/* 169 */       Inlines.OpusAssert(false);
/* 170 */       return SilkError.SILK_ENC_INVALID_DTX_SETTING;
/*     */     } 
/* 172 */     if (this.useCBR < 0 || this.useCBR > 1) {
/* 173 */       Inlines.OpusAssert(false);
/* 174 */       return SilkError.SILK_ENC_INVALID_CBR_SETTING;
/*     */     } 
/* 176 */     if (this.useInBandFEC < 0 || this.useInBandFEC > 1) {
/* 177 */       Inlines.OpusAssert(false);
/* 178 */       return SilkError.SILK_ENC_INVALID_INBAND_FEC_SETTING;
/*     */     } 
/* 180 */     if (this.nChannelsAPI < 1 || this.nChannelsAPI > 2) {
/* 181 */       Inlines.OpusAssert(false);
/* 182 */       return SilkError.SILK_ENC_INVALID_NUMBER_OF_CHANNELS_ERROR;
/*     */     } 
/* 184 */     if (this.nChannelsInternal < 1 || this.nChannelsInternal > 2) {
/* 185 */       Inlines.OpusAssert(false);
/* 186 */       return SilkError.SILK_ENC_INVALID_NUMBER_OF_CHANNELS_ERROR;
/*     */     } 
/* 188 */     if (this.nChannelsInternal > this.nChannelsAPI) {
/* 189 */       Inlines.OpusAssert(false);
/* 190 */       return SilkError.SILK_ENC_INVALID_NUMBER_OF_CHANNELS_ERROR;
/*     */     } 
/* 192 */     if (this.complexity < 0 || this.complexity > 10) {
/* 193 */       Inlines.OpusAssert(false);
/* 194 */       return SilkError.SILK_ENC_INVALID_COMPLEXITY_SETTING;
/*     */     } 
/*     */     
/* 197 */     return SilkError.SILK_NO_ERROR;
/*     */   }
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\EncControlState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */