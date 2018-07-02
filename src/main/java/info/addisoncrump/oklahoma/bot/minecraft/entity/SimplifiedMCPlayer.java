package info.addisoncrump.oklahoma.bot.minecraft.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SimplifiedMCPlayer {
    final String uuid;
    String name;

    public boolean equals(Object anObject) {
        return anObject instanceof SimplifiedMCPlayer && uuid.equals(((SimplifiedMCPlayer) anObject).uuid);
    }
}
