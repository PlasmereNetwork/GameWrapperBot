package info.addisoncrump.oklahoma.bot.auth.collect;

import info.addisoncrump.oklahoma.bot.auth.entity.AuthenticationToken;
import info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer;
import javafx.util.Pair;
import net.dv8tion.jda.core.entities.User;

import java.util.TreeMap;

public final class HandshakePendingMap extends TreeMap<AuthenticationToken, Pair<User, SimplifiedMCPlayer>> {
}
