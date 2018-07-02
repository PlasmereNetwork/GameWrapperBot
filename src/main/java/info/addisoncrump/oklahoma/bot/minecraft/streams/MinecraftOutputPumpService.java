package info.addisoncrump.oklahoma.bot.minecraft.streams;

import info.addisoncrump.oklahoma.bot.minecraft.event.MinecraftConsoleOutputEvent;
import info.addisoncrump.oklahoma.bot.minecraft.event.MinecraftServerShutdownEvent;
import lombok.NonNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MinecraftOutputPumpService {
    private final BufferedReader reader;
    private final ExecutorService pumpService;
    private final ApplicationEventPublisher publisher;
    private final Logger logger;
    private Process minecraftProcess;

    public MinecraftOutputPumpService(final @NonNull @Qualifier("minecraftProcess") Process minecraftProcess,
                                      final @NonNull @Qualifier("minecraftProcessOutputReader") BufferedReader reader,
                                      final @NonNull ApplicationEventPublisher publisher,
                                      final @NonNull @Qualifier("minecraftLogger") Logger logger) {
        this.minecraftProcess = minecraftProcess;
        this.reader = reader;
        this.publisher = publisher;
        this.logger = logger;
        this.pumpService = Executors.newSingleThreadExecutor();
    }

    @PostConstruct
    public void initialize() {
        pumpService.submit(() -> {
            try {
                for (String line; (line = reader.readLine()) != null; ) {
                    this.publisher.publishEvent(new MinecraftConsoleOutputEvent(line));
                }
            } catch (IOException e) {
                logger.warn(
                        "IOException occurred while reading from Minecraft process output.",
                        e
                );
            }
            this.publisher.publishEvent(new MinecraftServerShutdownEvent());
        });
    }

    @PreDestroy
    public void close() {
        try {
            minecraftProcess.waitFor(
                    30,
                    TimeUnit.SECONDS
            );
        } catch (InterruptedException e) {
            logger.warn("Interrupted while awaiting pump service closure; assuming process has stopped.");
        }
        pumpService.shutdownNow();
        try {
            if (!pumpService.awaitTermination(
                    30,
                    TimeUnit.SECONDS
            )) {
                logger.warn("Couldn't shutdown pump service; throwing the nuke (System#exit).");
                System.exit(137);
            }
        } catch (InterruptedException e) {
            logger.warn("Interrupted while awaiting pump service closure; skipping nuke.");
        }
    }
}
