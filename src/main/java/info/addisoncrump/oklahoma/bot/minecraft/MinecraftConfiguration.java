package info.addisoncrump.oklahoma.bot.minecraft;

import edu.rice.cs.util.ArgumentTokenizer;
import info.addisoncrump.oklahoma.bot.minecraft.event.MinecraftServerShutdownEvent;
import info.addisoncrump.oklahoma.bot.minecraft.streams.MinecraftOutputPumpService;
import info.addisoncrump.oklahoma.bot.minecraft.streams.MinecraftProcessInputWriter;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ComponentScan
@Configuration("minecraftConfiguration")
public class MinecraftConfiguration {
    private boolean expectingShutdown = false, failed = false;
    private Process minecraftProcess;
    private Logger minecraftLogger;
    private MinecraftOutputPumpService minecraftOutputPumpService;

    @Bean(value = "minecraftProcess",
          destroyMethod = "")
    public Process getMinecraftProcess(final @NonNull @Value("${oklahoma.bot.minecraft.exec}") String command,
                                       final @NonNull @Qualifier("minecraftLogger") Logger logger) throws
                                                                                                   IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(ArgumentTokenizer.tokenize(command));
        processBuilder.redirectInput(ProcessBuilder.Redirect.PIPE);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        processBuilder.redirectErrorStream(true);
        Process minecraftProcess = processBuilder.start();
        logger.info("Minecraft process started.");
        return minecraftProcess;
    }

    @Bean("minecraftProcessOutputReader")
    public BufferedReader getMinecraftProcessOutputReader(final @NonNull @Qualifier("minecraftProcess") Process minecraftProcess) {
        return new BufferedReader(new InputStreamReader(minecraftProcess.getInputStream()));
    }

    @Bean("minecraftProcessTracer")
    public ExecutorService getMinecraftProcessExecutorTracer() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean("minecraftLogger")
    public Logger getMinecraftLogger() {
        return this.minecraftLogger = LoggerFactory.getLogger("Minecraft Wrapper");
    }

    @EventListener(MinecraftServerShutdownEvent.class)
    public void onMinecraftServerShutdown() {
        if (!expectingShutdown) {
            failed = true;
            this.minecraftLogger.error("Minecraft process failed suddenly.");
        }
    }

    @Bean("minecraftProcessInputWriter")
    public MinecraftProcessInputWriter getMinecraftProcessWriter(final @NonNull @Qualifier("minecraftProcess") Process minecraftProcess) {
        return new MinecraftProcessInputWriter(minecraftProcess);
    }

    @Bean
    public MinecraftOutputPumpService getMinecraftOutputPumpService(final @NonNull @Qualifier("minecraftProcess") Process minecraftProcess,
                                                                    final @NonNull @Qualifier("minecraftProcessOutputReader") BufferedReader reader,
                                                                    final @NonNull ApplicationEventPublisher publisher,
                                                                    final @NonNull @Qualifier("minecraftLogger") Logger logger) {
        return this.minecraftOutputPumpService = new MinecraftOutputPumpService(
                minecraftProcess,
                reader,
                publisher,
                logger
        );
    }

    @PreDestroy
    public void cleanup() {
        this.expectingShutdown = true;
        if (minecraftProcess != null) {
            if (!failed) {
                try {
                    if (!minecraftProcess.waitFor(
                            120,
                            TimeUnit.SECONDS
                    )) {
                        minecraftLogger.warn("Couldn't stop Minecraft process naturally in 2 minutes, killing with SIGINT...");
                        minecraftProcess.destroy();
                        if (minecraftProcess.waitFor(
                                30,
                                TimeUnit.SECONDS
                        )) {
                            minecraftLogger.error("Couldn't stop Minecraft process with SIGINT after 30 seconds, SIGKILLing...");
                            minecraftProcess.destroyForcibly();
                        }
                    }
                } catch (InterruptedException e) {
                    minecraftLogger.warn("Interrupted while awaiting process death. Forcibly destroying Minecraft process...");
                    minecraftProcess.destroyForcibly();
                }
                if (minecraftOutputPumpService != null) {
                    minecraftOutputPumpService.close();
                }
            }
        }
    }

    @Autowired
    public void setMinecraftOutputPumpService(final MinecraftOutputPumpService minecraftOutputPumpService) {
        this.minecraftOutputPumpService = minecraftOutputPumpService;
    }

    @Autowired
    public void setMinecraftProcess(final @Qualifier("minecraftProcess") Process minecraftProcess) {
        this.minecraftProcess = minecraftProcess;
    }
}
