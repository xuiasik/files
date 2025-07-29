package de.maxhenkel.voicechat.api;

import javax.annotation.Nullable;

public interface VoicechatConnection {
  @Nullable
  Group getGroup();
  
  boolean isInGroup();
  
  void setGroup(@Nullable Group paramGroup);
  
  boolean isConnected();
  
  void setConnected(boolean paramBoolean);
  
  boolean isDisabled();
  
  void setDisabled(boolean paramBoolean);
  
  boolean isInstalled();
  
  ServerPlayer getPlayer();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\VoicechatConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */