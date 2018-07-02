package info.addisoncrump.oklahoma.bot.minecraft.process;

import info.addisoncrump.oklahoma.bot.aspect.ExcludeFromLogging;
import info.addisoncrump.oklahoma.bot.minecraft.event.MinecraftCleanedConsoleOutputEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class MinecraftConsoleForwardingService {
    private final Logger logger;

    public MinecraftConsoleForwardingService() {
        logger = LoggerFactory.getLogger("Minecraft Console Relay");
    }

    @ExcludeFromLogging
    @EventListener
    public void onCleanedConsoleOutput(MinecraftCleanedConsoleOutputEvent event) {
        switch (event.getLevel()) {
            case ERROR:
                logger.error(event.getMessage());
                break;
            case WARN:
                logger.warn(event.getMessage());
                break;
            case INFO:
                logger.info(event.getMessage());
                break;
            case DEBUG:
                logger.debug(event.getMessage());
                break;
            case TRACE:
                logger.trace(event.getMessage());
                break;
        }
    }
}
