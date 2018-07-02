package info.addisoncrump.oklahoma.bot.auth.event;

import info.addisoncrump.oklahoma.bot.auth.entity.AuthenticationToken;
import lombok.NonNull;
import lombok.Value;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

@Value
public class VerificationAttemptEvent {
    User user;
    TextChannel channel;
    AuthenticationToken submittedToken;

    public VerificationAttemptEvent(final @NonNull User user,
                                    final @NonNull TextChannel channel,
                                    final @NonNull String stringToken) {
        this.user = user;
        this.channel = channel;
        this.submittedToken = new AuthenticationToken(stringToken);
    }
}
