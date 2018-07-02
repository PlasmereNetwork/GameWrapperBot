package info.addisoncrump.oklahoma.bot.minecraft.event;

import lombok.Value;
import org.slf4j.event.Level;

@Value
public class MinecraftCleanedConsoleOutputEvent {
    Level level;
    String message;
}
