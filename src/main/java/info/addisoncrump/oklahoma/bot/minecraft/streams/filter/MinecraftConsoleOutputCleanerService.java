package info.addisoncrump.oklahoma.bot.minecraft.streams.filter;

import info.addisoncrump.oklahoma.bot.aspect.ExcludeFromLogging;
import info.addisoncrump.oklahoma.bot.minecraft.event.MinecraftCleanedConsoleOutputEvent;
import info.addisoncrump.oklahoma.bot.minecraft.event.MinecraftConsoleOutputEvent;
import org.slf4j.event.Level;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class MinecraftConsoleOutputCleanerService {
    private final ApplicationEventPublisher publisher;

    public MinecraftConsoleOutputCleanerService(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    // This method assumes standard formatting for Minecraft output. If that isn't the case, raise an issue and I
    // might consider actually fixing this.
    @ExcludeFromLogging
    @EventListener
    public void onConsoleOutput(MinecraftConsoleOutputEvent event) {
        Level level = Level.INFO;
        int cutpoint = event.getLine().indexOf(": ") + 2;
        int levelpoint;
        for (Level possible : Level.values()) {
            levelpoint = event.getLine().indexOf(possible.toString());
            if (levelpoint != -1 && levelpoint < cutpoint) {
                level = possible;
                break;
            }
        }
        String message = event.getLine().substring(cutpoint);
        this.publisher.publishEvent(new MinecraftCleanedConsoleOutputEvent(
                level,
                message
        ));
    }
}
