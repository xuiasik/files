package de.maxhenkel.voicechat.api.audiochannel;

public interface AudioPlayer {
  void startPlaying();
  
  void stopPlaying();
  
  boolean isStarted();
  
  boolean isPlaying();
  
  boolean isStopped();
  
  void setOnStopped(Runnable paramRunnable);
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\audiochannel\AudioPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */