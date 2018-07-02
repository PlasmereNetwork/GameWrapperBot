package info.addisoncrump.oklahoma.bot.auth.event;

import info.addisoncrump.oklahoma.bot.auth.entity.DiscordMCLink;
import lombok.Value;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * Event which is fired after a verification attempt succeeds, as fired by the {@link
 * info.addisoncrump.oklahoma.bot.auth.AuthenticationService} instance.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see info.addisoncrump.oklahoma.bot.auth.AuthenticationService
 * @see VerificationAttemptEvent
 * @since 1.0.0
 */
@Value
public class VerificationSuccessEvent {
    /**
     * The discord user associated with this event. This is used for mentioning.
     */
    User user;
    /**
     * The channel in which the verification began. This is used for replying.
     */
    TextChannel channel;
    /**
     * The Discord user and Minecraft player pairing that has been authenticated by this handshake verification.
     */
    DiscordMCLink link;
}
