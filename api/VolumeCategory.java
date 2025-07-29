package de.maxhenkel.voicechat.api;

import javax.annotation.Nullable;

public interface VolumeCategory {
  String getId();
  
  String getName();
  
  @Nullable
  String getDescription();
  
  @Nullable
  int[][] getIcon();
  
  public static interface Builder {
    Builder setId(String param1String);
    
    Builder setName(String param1String);
    
    Builder setDescription(@Nullable String param1String);
    
    Builder setIcon(@Nullable int[][] param1ArrayOfint);
    
    VolumeCategory build();
  }
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\api\VolumeCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */