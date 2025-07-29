package de.maxhenkel.voicechat.concentus;

class TuningParameters {
  static final int BITRESERVOIR_DECAY_TIME_MS = 500;
  
  static final float FIND_PITCH_WHITE_NOISE_FRACTION = 0.001F;
  
  static final float FIND_PITCH_BANDWIDTH_EXPANSION = 0.99F;
  
  static final float FIND_LPC_COND_FAC = 1.0E-5F;
  
  static final float FIND_LTP_COND_FAC = 1.0E-5F;
  
  static final float LTP_DAMPING = 0.05F;
  
  static final float LTP_SMOOTHING = 0.1F;
  
  static final float MU_LTP_QUANT_NB = 0.03F;
  
  static final float MU_LTP_QUANT_MB = 0.025F;
  
  static final float MU_LTP_QUANT_WB = 0.02F;
  
  static final float MAX_SUM_LOG_GAIN_DB = 250.0F;
  
  static final float VARIABLE_HP_SMTH_COEF1 = 0.1F;
  
  static final float VARIABLE_HP_SMTH_COEF2 = 0.015F;
  
  static final float VARIABLE_HP_MAX_DELTA_FREQ = 0.4F;
  
  static final int VARIABLE_HP_MIN_CUTOFF_HZ = 60;
  
  static final int VARIABLE_HP_MAX_CUTOFF_HZ = 100;
  
  static final float SPEECH_ACTIVITY_DTX_THRES = 0.05F;
  
  static final float LBRR_SPEECH_ACTIVITY_THRES = 0.3F;
  
  static final float BG_SNR_DECR_dB = 2.0F;
  
  static final float HARM_SNR_INCR_dB = 2.0F;
  
  static final float SPARSE_SNR_INCR_dB = 2.0F;
  
  static final float SPARSENESS_THRESHOLD_QNT_OFFSET = 0.75F;
  
  static final float WARPING_MULTIPLIER = 0.015F;
  
  static final float SHAPE_WHITE_NOISE_FRACTION = 5.0E-5F;
  
  static final float BANDWIDTH_EXPANSION = 0.95F;
  
  static final float LOW_RATE_BANDWIDTH_EXPANSION_DELTA = 0.01F;
  
  static final float LOW_RATE_HARMONIC_BOOST = 0.1F;
  
  static final float LOW_INPUT_QUALITY_HARMONIC_BOOST = 0.1F;
  
  static final float HARMONIC_SHAPING = 0.3F;
  
  static final float HIGH_RATE_OR_LOW_QUALITY_HARMONIC_SHAPING = 0.2F;
  
  static final float HP_NOISE_COEF = 0.25F;
  
  static final float HARM_HP_NOISE_COEF = 0.35F;
  
  static final float INPUT_TILT = 0.05F;
  
  static final float HIGH_RATE_INPUT_TILT = 0.1F;
  
  static final float LOW_FREQ_SHAPING = 4.0F;
  
  static final float LOW_QUALITY_LOW_FREQ_SHAPING_DECR = 0.5F;
  
  static final float SUBFR_SMTH_COEF = 0.4F;
  
  static final float LAMBDA_OFFSET = 1.2F;
  
  static final float LAMBDA_SPEECH_ACT = -0.2F;
  
  static final float LAMBDA_DELAYED_DECISIONS = -0.05F;
  
  static final float LAMBDA_INPUT_QUALITY = -0.1F;
  
  static final float LAMBDA_CODING_QUALITY = -0.2F;
  
  static final float LAMBDA_QUANT_OFFSET = 0.8F;
  
  static final int REDUCE_BITRATE_10_MS_BPS = 2200;
  
  static final int MAX_BANDWIDTH_SWITCH_DELAY_MS = 5000;
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\concentus\TuningParameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */