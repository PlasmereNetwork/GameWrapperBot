package info.addisoncrump.oklahoma.bot.minecraft.event;

import info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer;
import lombok.Value;

/**
 * Event which is fired when a player connects to the server and successfully logs in as logged by the Minecraft process
 * and parsed by the {@link info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputParserService}
 * instance. These events are not explicitly forwarded to the Discord server in this version.
 * <p>
 * This particular event is used for tracing logins as provided by the {@link info.addisoncrump.oklahoma.bot.minecraft.listener.MinecraftPlayerLinkService}
 * instance.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputParserService
 * @since 1.0.0
 */
@Value
public class PlayerConnectEvent {
    /**
     * The player entity which has joined the server.
     */
    SimplifiedMCPlayer player;
}
