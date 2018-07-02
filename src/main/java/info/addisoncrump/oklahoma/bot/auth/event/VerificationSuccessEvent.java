package info.addisoncrump.oklahoma.bot.auth.event;

import info.addisoncrump.oklahoma.bot.auth.entity.DiscordMCLink;
import lombok.Value;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

@Value
public class VerificationSuccessEvent {
    User user;
    TextChannel channel;
    DiscordMCLink link;
}
