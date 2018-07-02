package info.addisoncrump.oklahoma.bot.auth;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import info.addisoncrump.oklahoma.bot.auth.collect.HandshakePendingMap;
import info.addisoncrump.oklahoma.bot.auth.collect.VerifiedUserRegistry;
import info.addisoncrump.oklahoma.bot.auth.entity.AuthenticationToken;
import info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer;
import javafx.util.Pair;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PreDestroy;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@ComponentScan
@Configuration("crossAuthenticationConfiguration")
public class CrossAuthenticationConfiguration {
    private final Gson gson;
    private Path registryPath;
    private VerifiedUserRegistry registry;
    private Logger authLogger;

    public CrossAuthenticationConfiguration(final @NonNull Gson gson) {
        this.gson = gson;
    }

    @Bean("authLogger")
    public Logger getLogger() {
        return this.authLogger = LoggerFactory.getLogger("CrossAuth");
    }

    @Bean("registryPath")
    public Path getRegistryPath(@NonNull @Value("${oklahoma.bot.auth.registry}") String registryFile) {
        return this.registryPath = Paths.get(registryFile);
    }

    @Bean
    public SecureRandom getRandomGenerator() {
        return new SecureRandom();
    }

    @Bean("handshakePendingMap")
    public Map<AuthenticationToken, Pair<User, SimplifiedMCPlayer>> getHandshakePendingMap() {
        return Collections.synchronizedMap(new HandshakePendingMap());
    }

    @Bean("authenticationExecutorService")
    public ScheduledExecutorService getAuthenticationExecutorService() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Order(1)
    @Bean("authRegistry")
    public VerifiedUserRegistry verifiedUserRegistry(@NonNull @Qualifier("registryPath") Path registryPath,
                                                     @NonNull Gson gson,
                                                     @NonNull @Qualifier("authLogger") Logger authLogger) {
        try {
            return this.registry = gson.fromJson(
                    new FileReader(registryPath.toFile()),
                    VerifiedUserRegistry.class
            );
        } catch (FileNotFoundException e) {
            authLogger.warn(
                    "Couldn't find Oklahoma user registry. Creating...",
                    e
            );
            return this.registry = new VerifiedUserRegistry();
        } catch (JsonIOException | JsonSyntaxException e) {
            authLogger.warn(
                    "Tried to load Oklahoma user registry, but it wasn't valid. Backing up and instantiating...",
                    e
            );
            Path oldPath = Paths.get(registryPath.toAbsolutePath().toString());
            Path newPath = Paths.get(registryPath.toAbsolutePath().toString() + ".bak");
            if (Files.exists(newPath)) {
                int extension = 0;
                while (Files.exists(Paths.get(String.format(
                        "%s.%d",
                        newPath.toString(),
                        extension
                )))) {
                    extension++;
                }
                newPath = Paths.get(String.format(
                        "%s.%d",
                        newPath.toString(),
                        extension
                ));
            }
            try {
                Files.move(
                        oldPath,
                        newPath
                );
            } catch (IOException e1) {
                authLogger.warn(
                        "Couldn't backup existing (invalid) registry.",
                        e1
                );
            }
            return this.registry = new VerifiedUserRegistry();
        }
    }

    @Bean
    public AuthenticationService getAuthenticationService(final SecureRandom secureRandom,
                                                          final @Qualifier("handshakePendingMap") Map<AuthenticationToken, Pair<User, SimplifiedMCPlayer>> handshakePendingMap,
                                                          final @Qualifier("authRegistry") VerifiedUserRegistry verifiedUserRegistry,
                                                          final ScheduledExecutorService authenticationExecutorService,
                                                          final ApplicationEventPublisher publisher) {
        return new AuthenticationService(
                secureRandom,
                handshakePendingMap,
                verifiedUserRegistry,
                authenticationExecutorService,
                publisher
        );
    }

    @PreDestroy
    public void cleanup() {
        try {
            Writer registryWriter = Files.newBufferedWriter(registryPath);
            gson.toJson(
                    registry,
                    registryWriter
            );
            registryWriter.flush();
            registryWriter.close();
        } catch (IOException e) {
            authLogger.warn(
                    "Couldn't save registry to disk. Printing to console...",
                    e
            );
            System.out.println(gson.toJson(registry));
        }
    }
}
