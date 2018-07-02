package info.addisoncrump.oklahoma.bot.auth.event;

import lombok.Value;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

@Value
public class HandshakeCancelledEvent {
    User user;
    TextChannel channel;
}
