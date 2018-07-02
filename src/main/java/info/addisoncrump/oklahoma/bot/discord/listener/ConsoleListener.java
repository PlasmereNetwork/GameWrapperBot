package info.addisoncrump.oklahoma.bot.discord.listener;

import info.addisoncrump.oklahoma.bot.minecraft.event.MinecraftCleanedConsoleOutputEvent;
import info.addisoncrump.oklahoma.bot.minecraft.streams.MinecraftProcessInputWriter;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@FieldDefaults(level = AccessLevel.PRIVATE,
               makeFinal = true)
public class ConsoleListener extends ListenerAdapter {
    String consoleRelayChannelID;
    ShardManager shardManager;
    MinecraftProcessInputWriter writer;
    ScheduledExecutorService deliveryService;
    LinkedList<String> messageQueue;
    Logger logger;

    public ConsoleListener(final @NonNull @Value("${oklahoma.bot.discord.console.relay}") String consoleRelayChannelID,
                           final @NonNull ShardManager shardManager,
                           final @NonNull @Qualifier("minecraftProcessInputWriter") MinecraftProcessInputWriter minecraftProcessInputWriter) {
        this.consoleRelayChannelID = consoleRelayChannelID;
        this.shardManager = shardManager;
        this.writer = minecraftProcessInputWriter;
        this.deliveryService = Executors.newSingleThreadScheduledExecutor();
        this.messageQueue = new LinkedList<>();
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @PostConstruct
    public void initialize() {
        shardManager.addEventListener(this);
        LinkedList<String> messages = new LinkedList<>();
        for (JDA jda : shardManager.getShards()) {
            while (jda.getStatus().ordinal() < JDA.Status.CONNECTED.ordinal()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    logger.warn("Interrupted while awaiting shard initialization.");
                }
            }
        }
        TextChannel channel = shardManager.getTextChannelById(consoleRelayChannelID);
        if (channel == null) {
            logger.error("Channel is not accessible or does not exist.");
        } else {
            StringBuilder builder = new StringBuilder();
            deliveryService.scheduleAtFixedRate(
                    () -> {
                        if (!messageQueue.isEmpty()) {
                            try {
                                messages.addAll(messageQueue);
                                messageQueue.removeAll(messages);
                                for (; !messages.isEmpty() && builder.length() + messages.getFirst().length() < 2000; ) {
                                    builder.append(messages.removeFirst()).append('\n');
                                }
                                channel.sendMessage(builder.toString()).complete();
                                builder.setLength(0);
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                    },
                    0,
                    200,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    @Override
    public synchronized void onMessageReceived(final @NonNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (event.getChannel().getId().equals(consoleRelayChannelID) && !event.getAuthor().isBot()) {
            try {
                writer.println(event.getMessage().getContentRaw());
            } catch (IOException e) {
                logger.warn(
                        "IOException encountered while attempting to fire command.",
                        e
                );
            }
            logger.info(String.format(
                    "Wrote to console: %s",
                    event.getMessage().getContentRaw()
            ));
        }
    }

    @EventListener
    public void onMinecraftCleanedConsoleOutput(final @NonNull MinecraftCleanedConsoleOutputEvent event) {
        if (event.getLevel().ordinal() <= Level.INFO.ordinal()) {
            messageQueue.add(event.getMessage());
        }
    }

    @PreDestroy
    public void cleanup() {
        deliveryService.shutdown();
        try {
            if (!deliveryService.awaitTermination(
                    10,
                    TimeUnit.SECONDS
            )) {
                deliveryService.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.warn("Interrupted while shutting down delivery service, terminating forcefully.");
            deliveryService.shutdownNow();
        }
    }
}
