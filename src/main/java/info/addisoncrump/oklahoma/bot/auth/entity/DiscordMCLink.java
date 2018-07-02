package info.addisoncrump.oklahoma.bot.auth.entity;

import info.addisoncrump.oklahoma.bot.discord.entity.SimplifiedDiscordUser;
import info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer;
import lombok.Value;

@Value
public class DiscordMCLink {
    SimplifiedMCPlayer mcPlayer;
    SimplifiedDiscordUser discordUser;

    public boolean equals(Object anObject) {
        if (anObject instanceof SimplifiedMCPlayer) {
            return mcPlayer.equals(anObject);
        } else if (anObject instanceof SimplifiedDiscordUser) {
            return discordUser.equals(anObject);
        } else if (anObject instanceof DiscordMCLink) {
            return this.mcPlayer.equals(((DiscordMCLink) anObject).mcPlayer) && this.discordUser.equals(((DiscordMCLink) anObject).discordUser);
        } else {
            return false;
        }
    }
}
