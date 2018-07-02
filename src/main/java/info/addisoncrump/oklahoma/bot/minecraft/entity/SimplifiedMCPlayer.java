package info.addisoncrump.oklahoma.bot.minecraft.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Simplified player entity, containing all useful information about the player that is extractable from login
 * information.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see info.addisoncrump.oklahoma.bot.minecraft.listener.MinecraftAuthenticationEventListener
 * @see info.addisoncrump.oklahoma.bot.minecraft.listener.MinecraftPlayerLinkService
 * @since 1.0.0
 */
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SimplifiedMCPlayer {
    /**
     * The uuid associated with this player entity.
     */
    final String uuid;
    /**
     * The name associated with this player entity. This value is prone to change, so it will not be used for equality
     * checking.
     */
    String name;

    /*
     * (non-Javadoc) see Object#equals for information.
     */
    public boolean equals(Object anObject) {
        return anObject instanceof SimplifiedMCPlayer && uuid.equals(((SimplifiedMCPlayer) anObject).uuid);
    }
}
