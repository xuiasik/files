package de.maxhenkel.voicechat.mixin;

import io.netty.channel.Channel;
import net.minecraft.class_2535;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2535.class})
public interface ConnectionAccessor {
  @Accessor("channel")
  Channel getChannel();
}


/* Location:              C:\Users\bakla\Downloads\voicechat-fabric-1.16.5-2.5.35(1).jar!\de\maxhenkel\voicechat\mixin\ConnectionAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */