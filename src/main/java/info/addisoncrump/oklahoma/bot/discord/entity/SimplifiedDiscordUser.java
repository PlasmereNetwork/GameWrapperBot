package info.addisoncrump.oklahoma.bot.discord.entity;

import lombok.NonNull;
import lombok.Value;
import net.dv8tion.jda.core.entities.User;

@Value
public class SimplifiedDiscordUser {
    String uuid;

    public SimplifiedDiscordUser(final @NonNull User user) {
        this.uuid = user.getId();
    }

    public boolean equals(Object anObject) {
        return anObject instanceof SimplifiedDiscordUser && uuid.equals(((SimplifiedDiscordUser) anObject).uuid);
    }
}
