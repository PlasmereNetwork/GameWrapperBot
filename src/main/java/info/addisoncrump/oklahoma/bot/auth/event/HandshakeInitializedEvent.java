package info.addisoncrump.oklahoma.bot.auth.event;

import info.addisoncrump.oklahoma.bot.auth.entity.AuthenticationToken;
import info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer;
import lombok.Value;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

@Value
public class HandshakeInitializedEvent {
    User user;
    SimplifiedMCPlayer mcPlayer;
    TextChannel channel;
    AuthenticationToken token;
}
