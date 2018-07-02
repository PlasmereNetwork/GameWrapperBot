package info.addisoncrump.oklahoma.bot.auth.event;

import info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer;
import lombok.Value;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * Event which is fired after the handshake is started, as fired and handled by the {@link
 * info.addisoncrump.oklahoma.bot.minecraft.listener.MinecraftAuthenticationEventListener} instance. This event is not
 * fired if name lookup fails.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see info.addisoncrump.oklahoma.bot.auth.AuthenticationService
 * @see HandshakeCancelledEvent
 * @see HandshakeInitializedEvent
 * @see NoUserFoundWithNameEvent
 * @since 1.0.0
 */
@Value
public class HandshakeStartedEvent {
    /**
     * The discord user associated with this event. This is used mostly for {@link info.addisoncrump.oklahoma.bot.auth.entity.DiscordMCLink}
     * preconfiguration and mentioning.
     */
    User user;
    /**
     * The player that was found in association with this authentication event.
     */
    SimplifiedMCPlayer mcPlayer;
    /**
     * The channel in which the handshake began. This is used for replying.
     */
    TextChannel channel;
}
