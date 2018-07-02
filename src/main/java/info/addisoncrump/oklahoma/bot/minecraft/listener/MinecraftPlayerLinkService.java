package info.addisoncrump.oklahoma.bot.minecraft.listener;

import info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer;
import info.addisoncrump.oklahoma.bot.minecraft.event.PlayerConnectEvent;
import info.addisoncrump.oklahoma.bot.minecraft.event.PlayerDisconnectEvent;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class MinecraftPlayerLinkService {
    private final LinkedList<SimplifiedMCPlayer> mcPlayers;

    public MinecraftPlayerLinkService() {
        mcPlayers = new LinkedList<>();
    }

    public synchronized SimplifiedMCPlayer findPlayerByName(@NonNull String username) {
        for (final SimplifiedMCPlayer mcPlayer : mcPlayers) {
            if (mcPlayer.getName().equalsIgnoreCase(username)) {
                return mcPlayer;
            }
        }
        return null;
    }

    @EventListener
    public synchronized void onPlayerConnect(@NonNull PlayerConnectEvent event) {
        mcPlayers.add(event.getPlayer());
    }

    @EventListener
    public synchronized void onPlayerDisconnect(@NonNull PlayerDisconnectEvent event) {
        mcPlayers.removeIf(simplifiedMCPlayer -> event.getUsername().equals(simplifiedMCPlayer.getName()));
    }
}
