package info.addisoncrump.oklahoma.bot.auth.event;

import lombok.Value;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * Event which is fired after a verification attempt fails, as fired by the {@link
 * info.addisoncrump.oklahoma.bot.auth.AuthenticationService} instance.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see info.addisoncrump.oklahoma.bot.auth.AuthenticationService
 * @see VerificationAttemptEvent
 * @since 1.0.0
 */
@Value
public class VerificationFailedEvent {
    /**
     * The discord user associated with this event. This is used mostly for {@link info.addisoncrump.oklahoma.bot.auth.entity.DiscordMCLink}
     * preconfiguration and mentioning.
     */
    User user;
    /**
     * The channel in which the verification began. This is used for replying.
     */
    TextChannel channel;
}
