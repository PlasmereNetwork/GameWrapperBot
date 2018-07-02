package info.addisoncrump.oklahoma.bot.minecraft.event;

import info.addisoncrump.oklahoma.bot.minecraft.MinecraftConfiguration;
import lombok.Value;

/**
 * Event which is fired upon server shutdown. This should not be used to terminate the OklahomaBot application; that
 * should only be done manually.
 * <p>
 * This event is fired by the {@link info.addisoncrump.oklahoma.bot.minecraft.streams.MinecraftOutputPumpService}
 * instance itself, which manages the process output stream and watches for closing. This should not be fired when
 * reading a "Stopping server" line, as this can be spoofed.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see MinecraftConfiguration#onMinecraftServerShutdown()
 * @since 1.0.0
 */
@Value
public class MinecraftServerShutdownEvent {
}
