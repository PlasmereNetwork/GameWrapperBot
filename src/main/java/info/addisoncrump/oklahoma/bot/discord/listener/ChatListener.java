package info.addisoncrump.oklahoma.bot.discord.listener;

import info.addisoncrump.oklahoma.bot.auth.collect.VerifiedUserRegistry;
import info.addisoncrump.oklahoma.bot.auth.entity.DiscordMCLink;
import info.addisoncrump.oklahoma.bot.minecraft.event.PlayerChatEvent;
import info.addisoncrump.oklahoma.bot.minecraft.streams.MinecraftProcessInputWriter;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import java.io.IOException;

@FieldDefaults(level = AccessLevel.PRIVATE,
               makeFinal = true)
public class ChatListener extends ListenerAdapter {
    String chatRelayChannelID;
    ShardManager shardManager;
    VerifiedUserRegistry userRegistry;
    MinecraftProcessInputWriter minecraftProcessInputWriter;
    Logger logger;

    public ChatListener(final @NonNull @Value("${oklahoma.bot.discord.chat.relay}") String chatRelayChannelID,
                        final @NonNull ShardManager shardManager,
                        final @NonNull VerifiedUserRegistry userRegistry,
                        final @NonNull @Qualifier("minecraftProcessInputWriter") MinecraftProcessInputWriter minecraftProcessInputWriter) {
        this.chatRelayChannelID = chatRelayChannelID;
        this.shardManager = shardManager;
        this.userRegistry = userRegistry;
        this.minecraftProcessInputWriter = minecraftProcessInputWriter;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @PostConstruct
    public void initialize() {
        shardManager.addEventListener(this);
    }

    @Override
    public synchronized void onMessageReceived(final @NonNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (event.getChannel().getId().equals(chatRelayChannelID) && !event.getAuthor().isBot()) {
            for (DiscordMCLink link : userRegistry) {
                if (link.getDiscordUser().getUuid().equals(event.getAuthor().getId())) {
                    try {
                        minecraftProcessInputWriter.println(String.format(
                                "/tellraw @a {\"text\":\"<%s> %s\"}",
                                link.getMcPlayer().getName(),
                                JSONValue.escape(event.getMessage().getContentRaw())
                        ));
                    } catch (IOException e) {
                        logger.warn("IOException occurred while writing ");
                    }
                    break;
                }
            }
        }
    }

    @EventListener
    public synchronized void onPlayerChat(final @NonNull PlayerChatEvent event) {
        shardManager.getTextChannelById(chatRelayChannelID).sendMessage(event.getMessage()).complete();
    }
}
