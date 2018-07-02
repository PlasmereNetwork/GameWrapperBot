package info.addisoncrump.oklahoma.bot.auth.event;

import info.addisoncrump.oklahoma.bot.auth.entity.AuthenticationToken;
import lombok.NonNull;
import lombok.Value;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * Event which is fired after a Discord user attempts verification, as fired by the {@link
 * info.addisoncrump.oklahoma.bot.discord.command.CrossAuthenticateVerifyCommand} instance. This event will lead to the
 * firing of either a {@link VerificationFailedEvent} or {@link VerificationSuccessEvent} instance, depending on the
 * result.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see info.addisoncrump.oklahoma.bot.auth.AuthenticationService
 * @see VerificationFailedEvent
 * @see VerificationSuccessEvent
 * @since 1.0.0
 */
@Value
public class VerificationAttemptEvent {
    /**
     * The discord user associated with this event. This is used mostly for {@link info.addisoncrump.oklahoma.bot.auth.entity.DiscordMCLink}
     * preconfiguration and mentioning.
     */
    User user;
    /**
     * The channel in which the verification began. This is used for replying.
     */
    TextChannel channel;
    /**
     * The token which was submitted for authentication. This token is NOT generated, but instead only used for
     * comparison.
     */
    AuthenticationToken submittedToken;

    public VerificationAttemptEvent(final @NonNull User user,
                                    final @NonNull TextChannel channel,
                                    final @NonNull String stringToken) {
        this.user = user;
        this.channel = channel;
        this.submittedToken = new AuthenticationToken(stringToken);
    }
}
