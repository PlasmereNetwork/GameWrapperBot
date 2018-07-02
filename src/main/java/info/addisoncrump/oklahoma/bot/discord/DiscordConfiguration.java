package info.addisoncrump.oklahoma.bot.discord;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import info.addisoncrump.oklahoma.bot.auth.collect.VerifiedUserRegistry;
import info.addisoncrump.oklahoma.bot.discord.listener.ChatListener;
import info.addisoncrump.oklahoma.bot.discord.listener.ConsoleListener;
import info.addisoncrump.oklahoma.bot.minecraft.streams.MinecraftProcessInputWriter;
import lombok.NonNull;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.entities.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;

import javax.annotation.PreDestroy;
import javax.security.auth.login.LoginException;

@Order(2)
@ComponentScan
@Configuration("discordConfiguration")
public class DiscordConfiguration {
    private ShardManager jda;

    @Autowired
    @Bean
    public CommandClient getCommandClient(final @NonNull @Value("${oklahoma.bot.discord.command.prefix}") String prefix,
                                          final @NonNull Command[] commandList,
                                          final @NonNull @Value("${oklahoma.bot.discord.command.owner}") String ownerID) {
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setPrefix(prefix);
        builder.setGame(Game.watching("you"));
        builder.addCommands(commandList);
        builder.setOwnerId(ownerID);
        return builder.build();
    }

    @Bean("shardManager")
    public ShardManager getShardManager(final @NonNull @Value("${oklahoma.bot.discord.token}") String token,
                                        final @Value("${oklahoma.bot.discord.shards}") int shards,
                                        final @NonNull CommandClient commandClient) throws
                                                                                    LoginException {
        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
        builder.setToken(token);
        builder.addEventListeners(commandClient);
        builder.setShardsTotal(shards);
        return this.jda = builder.build();
    }

    @Bean
    @DependsOn({
                       "shardManager",
                       "authRegistry"
               })
    public ChatListener getChatListener(final @NonNull @Value("${oklahoma.bot.discord.chat.relay}") String chatRelayChannelID,
                                        final @NonNull ShardManager shardManager,
                                        final @NonNull VerifiedUserRegistry userRegistry,
                                        final @NonNull @Qualifier("minecraftProcessInputWriter") MinecraftProcessInputWriter minecraftProcessInputWriter) {
        return new ChatListener(
                chatRelayChannelID,
                shardManager,
                userRegistry,
                minecraftProcessInputWriter
        );
    }

    @Bean
    public ConsoleListener getConsoleListener(final @NonNull @Value("${oklahoma.bot.discord.console.relay}") String consoleRelayChannelID,
                                              final @NonNull ShardManager shardManager,
                                              final @NonNull @Qualifier("minecraftProcessInputWriter") MinecraftProcessInputWriter writer) {
        return new ConsoleListener(
                consoleRelayChannelID,
                shardManager,
                writer
        );
    }

    @PreDestroy
    public void cleanup() {
        if (jda != null) {
            jda.shutdown();
        }
    }
}
