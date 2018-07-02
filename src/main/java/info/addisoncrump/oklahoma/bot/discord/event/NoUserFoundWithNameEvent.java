package info.addisoncrump.oklahoma.bot.discord.event;

import lombok.Value;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

@Value
public class NoUserFoundWithNameEvent {
    User user;
    TextChannel channel;
    String name;
}
