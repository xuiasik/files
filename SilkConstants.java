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
/*     */ class SilkConstants
/*     */ {
/*     */   public static final int ENCODER_NUM_CHANNELS = 2;
/*     */   public static final int DECODER_NUM_CHANNELS = 2;
/*     */   public static final int MAX_FRAMES_PER_PACKET = 3;
/*     */   public static final int MIN_TARGET_RATE_BPS = 5000;
/*     */   public static final int MAX_TARGET_RATE_BPS = 80000;
/*     */   public static final int TARGET_RATE_TAB_SZ = 8;
/*     */   public static final int LBRR_NB_MIN_RATE_BPS = 12000;
/*     */   public static final int LBRR_MB_MIN_RATE_BPS = 14000;
/*     */   public static final int LBRR_WB_MIN_RATE_BPS = 16000;
/*     */   public static final int NB_SPEECH_FRAMES_BEFORE_DTX = 10;
/*     */   public static final int MAX_CONSECUTIVE_DTX = 20;
/*     */   public static final int MAX_FS_KHZ = 16;
/*     */   public static final int MAX_API_FS_KHZ = 48;
/*     */   public static final int TYPE_NO_VOICE_ACTIVITY = 0;
/*     */   public static final int TYPE_UNVOICED = 1;
/*     */   public static final int TYPE_VOICED = 2;
/*     */   public static final int CODE_INDEPENDENTLY = 0;
/*     */   public static final int CODE_INDEPENDENTLY_NO_LTP_SCALING = 1;
/*     */   public static final int CODE_CONDITIONALLY = 2;
/*     */   public static final int STEREO_QUANT_TAB_SIZE = 16;
/*     */   public static final int STEREO_QUANT_SUB_STEPS = 5;
/*     */   public static final int STEREO_INTERP_LEN_MS = 8;
/*     */   public static final float STEREO_RATIO_SMOOTH_COEF = 0.01F;
/*     */   public static final int PITCH_EST_MIN_LAG_MS = 2;
/*     */   public static final int PITCH_EST_MAX_LAG_MS = 18;
/*     */   public static final int MAX_NB_SUBFR = 4;
/*     */   public static final int LTP_MEM_LENGTH_MS = 20;
/*     */   public static final int SUB_FRAME_LENGTH_MS = 5;
/*     */   public static final int MAX_SUB_FRAME_LENGTH = 80;
/*     */   public static final int MAX_FRAME_LENGTH_MS = 20;
/*     */   public static final int MAX_FRAME_LENGTH = 320;
/*     */   public static final int LA_PITCH_MS = 2;
/*     */   public static final int LA_PITCH_MAX = 32;
/*     */   public static final int MAX_FIND_PITCH_LPC_ORDER = 16;
/*     */   public static final int FIND_PITCH_LPC_WIN_MS = 24;
/*     */   public static final int FIND_PITCH_LPC_WIN_MS_2_SF = 14;
/*     */   public static final int FIND_PITCH_LPC_WIN_MAX = 384;
/*     */   public static final int LA_SHAPE_MS = 5;
/*     */   public static final int LA_SHAPE_MAX = 80;
/*     */   public static final int SHAPE_LPC_WIN_MAX = 240;
/*     */   public static final int MIN_QGAIN_DB = 2;
/*     */   public static final int MAX_QGAIN_DB = 88;
/*     */   public static final int N_LEVELS_QGAIN = 64;
/*     */   public static final int MAX_DELTA_GAIN_QUANT = 36;
/*     */   public static final int MIN_DELTA_GAIN_QUANT = -4;
/*     */   public static final short OFFSET_VL_Q10 = 32;
/*     */   public static final short OFFSET_VH_Q10 = 100;
/*     */   public static final short OFFSET_UVL_Q10 = 100;
/*     */   public static final short OFFSET_UVH_Q10 = 240;
/*     */   public static final int QUANT_LEVEL_ADJUST_Q10 = 80;
/*     */   public static final int MAX_LPC_STABILIZE_ITERATIONS = 16;
/*     */   public static final float MAX_PREDICTION_POWER_GAIN = 10000.0F;
/*     */   public static final float MAX_PREDICTION_POWER_GAIN_AFTER_RESET = 100.0F;
/*     */   public static final int SILK_MAX_ORDER_LPC = 16;
/*     */   public static final int MAX_LPC_ORDER = 16;
/*     */   public static final int MIN_LPC_ORDER = 10;
/*     */   public static final int LTP_ORDER = 5;
/*     */   public static final int NB_LTP_CBKS = 3;
/*     */   public static final int USE_HARM_SHAPING = 1;
/*     */   public static final int MAX_SHAPE_LPC_ORDER = 16;
/*     */   public static final int HARM_SHAPE_FIR_TAPS = 3;
/*     */   public static final int MAX_DEL_DEC_STATES = 4;
/*     */   public static final int LTP_BUF_LENGTH = 512;
/*     */   public static final int LTP_MASK = 511;
/*     */   public static final int DECISION_DELAY = 32;
/*     */   public static final int DECISION_DELAY_MASK = 31;
/*     */   public static final int SHELL_CODEC_FRAME_LENGTH = 16;
/*     */   public static final int LOG2_SHELL_CODEC_FRAME_LENGTH = 4;
/*     */   public static final int MAX_NB_SHELL_BLOCKS = 20;
/*     */   public static final int N_RATE_LEVELS = 10;
/*     */   public static final int SILK_MAX_PULSES = 16;
/*     */   public static final int MAX_MATRIX_SIZE = 16;
/* 182 */   static final int NSQ_LPC_BUF_LENGTH = Math.max(16, 32);
/*     */   public static final int VAD_N_BANDS = 4;
/*     */   public static final int VAD_INTERNAL_SUBFRAMES_LOG2 = 2;
/*     */   public static final int VAD_INTERNAL_SUBFRAMES = 4;
/*     */   public static final int VAD_NOISE_LEVEL_SMOOTH_COEF_Q16 = 1024;
/*     */   public static final int VAD_NOISE_LEVELS_BIAS = 50;
/*     */   public static final int VAD_NEGATIVE_OFFSET_Q5 = 128;
/*     */   public static final int VAD_SNR_FACTOR_Q16 = 45000;
/*     */   public static final int VAD_SNR_SMOOTH_COEF_Q18 = 4096;
/*     */   public static final int LSF_COS_TAB_SZ = 128;
/*     */   public static final int NLSF_W_Q = 2;
/*     */   public static final int NLSF_VQ_MAX_VECTORS = 32;
/*     */   public static final int NLSF_VQ_MAX_SURVIVORS = 32;
/*     */   public static final int NLSF_QUANT_MAX_AMPLITUDE = 4;
/*     */   public static final int NLSF_QUANT_MAX_AMPLITUDE_EXT = 10;
/*     */   public static final float NLSF_QUANT_LEVEL_ADJ = 0.1F;
/*     */   public static final int NLSF_QUANT_DEL_DEC_STATES_LOG2 = 2;
/*     */   public static final int NLSF_QUANT_DEL_DEC_STATES = 4;
/*     */   public static final int TRANSITION_TIME_MS = 5120;
/*     */   public static final int TRANSITION_NB = 3;
/*     */   public static final int TRANSITION_NA = 2;
/*     */   public static final int TRANSITION_INT_NUM = 5;
/*     */   public static final int TRANSITION_FRAMES = 256;
/*     */   public static final int TRANSITION_INT_STEPS = 64;
/*     */   public static final int BWE_AFTER_LOSS_Q16 = 63570;
/*     */   public static final int CNG_BUF_MASK_MAX = 255;
/*     */   public static final int CNG_GAIN_SMTH_Q16 = 4634;
/*     */   public static final int CNG_NLSF_SMTH_Q16 = 16348;
/*     */   public static final int PE_MAX_FS_KHZ = 16;
/*     */   public static final int PE_MAX_NB_SUBFR = 4;
/*     */   public static final int PE_SUBFR_LENGTH_MS = 5;
/*     */   public static final int PE_LTP_MEM_LENGTH_MS = 20;
/*     */   public static final int PE_MAX_FRAME_LENGTH_MS = 40;
/*     */   public static final int PE_MAX_FRAME_LENGTH = 640;
/*     */   public static final int PE_MAX_FRAME_LENGTH_ST_1 = 160;
/*     */   public static final int PE_MAX_FRAME_LENGTH_ST_2 = 320;
/*     */   public static final int PE_MAX_LAG_MS = 18;
/*     */   public static final int PE_MIN_LAG_MS = 2;
/*     */   public static final int PE_MAX_LAG = 288;
/*     */   public static final int PE_MIN_LAG = 32;
/*     */   public static final int PE_D_SRCH_LENGTH = 24;
/*     */   public static final int PE_NB_STAGE3_LAGS = 5;
/*     */   public static final int PE_NB_CBKS_STAGE2 = 3;
/*     */   public static final int PE_NB_CBKS_STAGE2_EXT = 11;
/*     */   public static final int PE_NB_CBKS_STAGE3_MAX = 34;
/*     */   public static final int PE_NB_CBKS_STAGE3_MID = 24;
/*     */   public static final int PE_NB_CBKS_STAGE3_MIN = 16;
/*     */   public static final int PE_NB_CBKS_STAGE3_10MS = 12;
/*     */   public static final int PE_NB_CBKS_STAGE2_10MS = 3;
/*     */   public static final float PE_SHORTLAG_BIAS = 0.2F;
/*     */   public static final float PE_PREVLAG_BIAS = 0.2F;
/*     */   public static final float PE_FLATCONTOUR_BIAS = 0.05F;
/*     */   public static final int SILK_PE_MIN_COMPLEX = 0;
/*     */   public static final int SILK_PE_MID_COMPLEX = 1;
/*     */   public static final int SILK_PE_MAX_COMPLEX = 2;
/*     */   public static final float BWE_COEF = 0.99F;
/*     */   public static final int V_PITCH_GAIN_START_MIN_Q14 = 11469;
/*     */   public static final int V_PITCH_GAIN_START_MAX_Q14 = 15565;
/*     */   public static final int MAX_PITCH_LAG_MS = 18;
/*     */   public static final int RAND_BUF_SIZE = 128;
/*     */   public static final int RAND_BUF_MASK = 127;
/*     */   public static final int LOG2_INV_LPC_GAIN_HIGH_THRES = 3;
/*     */   public static final int LOG2_INV_LPC_GAIN_LOW_THRES = 8;
/*     */   public static final int PITCH_DRIFT_FAC_Q16 = 655;
/*     */   public static final int SILK_RESAMPLER_MAX_FIR_ORDER = 36;
/*     */   public static final int SILK_RESAMPLER_MAX_IIR_ORDER = 6;
/*     */   public static final int RESAMPLER_DOWN_ORDER_FIR0 = 18;
/*     */   public static final int RESAMPLER_DOWN_ORDER_FIR1 = 24;
/*     */   public static final int RESAMPLER_DOWN_ORDER_FIR2 = 36;
/*     */   public static final int RESAMPLER_ORDER_FIR_12 = 8;
/*     */   public static final int RESAMPLER_MAX_BATCH_SIZE_MS = 10;
/*     */   public static final int RESAMPLER_MAX_FS_KHZ = 48;
/*     */   public static final int RESAMPLER_MAX_BATCH_SIZE_IN = 480;
/*     */   public static final int SILK_MAX_FRAMES_PER_PACKET = 3;
/*     */ }


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\SilkConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */