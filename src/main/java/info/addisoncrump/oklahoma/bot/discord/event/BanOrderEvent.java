package info.addisoncrump.oklahoma.bot.discord.event;

import lombok.Value;

/**
 * Event which is fired upon a ban order being issued from a Discord moderation channel.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see info.addisoncrump.oklahoma.bot.discord.command.BanCommand
 * @see info.addisoncrump.oklahoma.bot.minecraft.listener.MinecraftModerationEventListener
 * @since 1.0.0
 */
@Value
public class BanOrderEvent {
    /**
     * The arguments of the ban command.
     *
     * @implNote It is not worth digesting these arguments into comment and {@link info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer}
     * instances, as we can maintain functionality without this.
     */
    String args;
}
