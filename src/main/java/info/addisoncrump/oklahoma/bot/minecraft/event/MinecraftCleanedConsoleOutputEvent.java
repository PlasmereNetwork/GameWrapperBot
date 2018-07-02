package info.addisoncrump.oklahoma.bot.minecraft.event;

import info.addisoncrump.oklahoma.bot.minecraft.listener.MinecraftConsoleForwardingService;
import lombok.Value;
import org.slf4j.event.Level;

/**
 * Event which is fired after console cleaning. These events are much more useful for parsing than {@link
 * MinecraftConsoleOutputEvent} instances due to their message filtering.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see MinecraftConsoleOutputEvent
 * @see info.addisoncrump.oklahoma.bot.discord.listener.ConsoleListener
 * @see MinecraftConsoleForwardingService
 * @see info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputCleanerService
 * @see info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputParserService
 * @since 1.0.0
 */
@Value
public class MinecraftCleanedConsoleOutputEvent {
    /**
     * The log level associated with this message. This is generally unused, but is good for console output.
     */
    Level level;
    /**
     * The raw message of this console output event. This will generally only be used by raw listeners (such as {@link
     * info.addisoncrump.oklahoma.bot.discord.listener.ConsoleListener}) and parsers ({@link
     * info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputParserService}).
     *
     * @see info.addisoncrump.oklahoma.bot.discord.listener.ConsoleListener
     * @see info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputParserService
     */
    String message;
}
