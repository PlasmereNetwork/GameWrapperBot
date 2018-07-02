package info.addisoncrump.oklahoma.bot.minecraft.event;

import info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer;
import lombok.Value;

@Value
public class PlayerConnectEvent {
    SimplifiedMCPlayer player;
}
