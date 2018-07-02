package info.addisoncrump.oklahoma.bot.minecraft.event;

import lombok.Value;

/**
 * Event which is fired when the Minecraft process outputs a line of logging. You should avoid listening for this event;
 * instead, listen for {@link MinecraftCleanedConsoleOutputEvent}, which has been pre-parsed.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see MinecraftCleanedConsoleOutputEvent
 * @see info.addisoncrump.oklahoma.bot.minecraft.streams.MinecraftOutputPumpService
 * @see info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputCleanerService
 * @since 1.0.0
 */
@Value
public class MinecraftConsoleOutputEvent {
    /**
     * The raw line of log fetched from the Minecraft process. This is what is later cleaned by {@link
     * info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputCleanerService} and turned into a
     * {@link MinecraftCleanedConsoleOutputEvent}
     *
     * @see info.addisoncrump.oklahoma.bot.minecraft.streams.filter.MinecraftConsoleOutputCleanerService
     * @see MinecraftCleanedConsoleOutputEvent
     */
    String line;
}
