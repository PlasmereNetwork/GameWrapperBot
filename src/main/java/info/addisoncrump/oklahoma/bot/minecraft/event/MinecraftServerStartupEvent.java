package info.addisoncrump.oklahoma.bot.minecraft.event;

import lombok.Value;
import org.slf4j.Logger;

/**
 * Event which is fired as soon as the Minecraft process starts up. This should only be fired by the bean instantiation
 * ({@link info.addisoncrump.oklahoma.bot.minecraft.MinecraftConfiguration#getMinecraftProcess(String, Logger)}) method
 * and not at any other point.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see info.addisoncrump.oklahoma.bot.minecraft.MinecraftConfiguration
 * @since 1.0.0
 */
@Value
public class MinecraftServerStartupEvent {
}
