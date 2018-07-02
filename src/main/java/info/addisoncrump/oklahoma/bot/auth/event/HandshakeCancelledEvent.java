package info.addisoncrump.oklahoma.bot.auth.event;

import lombok.Value;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * Event which is fired if the handshake attempted timed out, as fired and handled by the {@link
 * info.addisoncrump.oklahoma.bot.auth.AuthenticationService} instance. This is automatically queued for firing as soon
 * as the {@link info.addisoncrump.oklahoma.bot.auth.AuthenticationService} finalizes the {@link HandshakeStartedEvent},
 * but prevented if the verification finishes before the timeout.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see info.addisoncrump.oklahoma.bot.auth.AuthenticationService
 * @see HandshakeStartedEvent
 * @since 1.0.0
 */
@Value
public class HandshakeCancelledEvent {
    /**
     * The discord user associated with this event. This is used mostly for {@link info.addisoncrump.oklahoma.bot.auth.entity.DiscordMCLink}
     * preconfiguration and mentioning.
     */
    User user;
    /**
     * The channel in which the handshake began. This is used for replying.
     */
    TextChannel channel;
}
