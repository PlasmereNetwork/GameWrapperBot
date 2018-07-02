package info.addisoncrump.oklahoma.bot.minecraft.streams.filter;

import info.addisoncrump.oklahoma.bot.aspect.ExcludeFromLogging;
import info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer;
import info.addisoncrump.oklahoma.bot.minecraft.event.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,
               makeFinal = true)
public class MinecraftConsoleOutputParserService {
    ApplicationEventPublisher publisher;
    Pattern serverStartPattern = Pattern.compile("Done \\([0-9]+\\.[0-9]{3}s\\)! For help, type \"help\"");
    Pattern playerJoinPattern = Pattern.compile("UUID of player [A-z0-9]{3,15} is [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    Pattern playerLeavePattern = Pattern.compile("[A-z0-9]{3,15} left the game");
    Pattern playerChatPattern = Pattern.compile("<.+> .+");

    public MinecraftConsoleOutputParserService(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @ExcludeFromLogging
    @EventListener
    public void onCleanedConsoleOutput(MinecraftCleanedConsoleOutputEvent event) {
        if (playerChatPattern.matcher(event.getMessage()).matches()) {
            this.publisher.publishEvent(parsePlayerChatEvent(event.getMessage()));
        } else if (playerJoinPattern.matcher(event.getMessage()).matches()) {
            this.publisher.publishEvent(parsePlayerConnectEvent(event.getMessage()));
        } else if (playerLeavePattern.matcher(event.getMessage()).matches()) {
            this.publisher.publishEvent(parsePlayerLeaveEvent(event.getMessage()));
        } else if (serverStartPattern.matcher(event.getMessage()).matches()) {
            this.publisher.publishEvent(new MinecraftServerStartupEvent());
        }
    }

    private PlayerChatEvent parsePlayerChatEvent(String message) {
        return new PlayerChatEvent(message);
    }

    private PlayerConnectEvent parsePlayerConnectEvent(String joinMessage) {
        String[] components = joinMessage.split(" ");
        return new PlayerConnectEvent(new SimplifiedMCPlayer(
                components[5],
                components[3]
        ));
    }

    private PlayerDisconnectEvent parsePlayerLeaveEvent(String leaveMessage) {
        return new PlayerDisconnectEvent(leaveMessage.substring(
                0,
                leaveMessage.indexOf(' ')
        ));
    }
}
