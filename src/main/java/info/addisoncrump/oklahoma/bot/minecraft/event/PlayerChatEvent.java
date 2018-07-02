package info.addisoncrump.oklahoma.bot.minecraft.event;

import lombok.Value;

/**
 * Event which is fired when a player chat is logged by the Minecraft process (as parsed by {@link
 * info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputParserService}). These events are
 * forwarded to the Discord server by the {@link info.addisoncrump.oklahoma.bot.discord.listener.ChatListener}
 * instance.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see info.addisoncrump.oklahoma.bot.discord.listener.ChatListener
 * @see info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputParserService
 * @since 1.0.0
 */
@Value
public class PlayerChatEvent {
    /**
     * The message of the player chat event.
     *
     * @implNote It is not worth digesting these arguments into message and {@link info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer}
     * instances, as we can maintain functionality without this.
     */
    String message;
}
