package info.addisoncrump.oklahoma.bot.auth.event;

import lombok.Value;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * Event which, when fired, precludes the firing of the {@link HandshakeStartedEvent} if the Minecraft player referenced
 * by the {@link info.addisoncrump.oklahoma.bot.discord.command.CrossAuthenticateInitializeCommand} is not currently
 * logged in to the Minecraft server.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see info.addisoncrump.oklahoma.bot.discord.command.CrossAuthenticateInitializeCommand
 * @since 1.0.0
 */
@Value
public class NoUserFoundWithNameEvent {
    /**
     * The discord user associated with this event. This is used mostly for {@link info.addisoncrump.oklahoma.bot.auth.entity.DiscordMCLink}
     * preconfiguration and mentioning.
     */
    User user;
    /**
     * The channel in which the handshake began. This is used for replying.
     */
    TextChannel channel;
    /**
     * The name associated with the lookup attempt, but was not found.
     */
    String name;
}
