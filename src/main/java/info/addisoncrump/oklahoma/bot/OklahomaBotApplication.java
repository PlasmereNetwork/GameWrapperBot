package info.addisoncrump.oklahoma.bot;

import info.addisoncrump.oklahoma.bot.auth.CrossAuthenticationConfiguration;
import info.addisoncrump.oklahoma.bot.discord.DiscordConfiguration;
import info.addisoncrump.oklahoma.bot.minecraft.MinecraftConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Launcher class for the OklahomaBot Application. Performs resource autoconfiguration at startup.
 *
 * @author vtcakavsmoace
 * @version 1.0.0
 * @see DiscordConfiguration
 * @see CrossAuthenticationConfiguration
 * @see MinecraftConfiguration
 * @since 1.0.0
 */
@Component
@EnableAutoConfiguration
@PropertySource(value = "file:oklahomabot.properties")
@Import({
                DiscordConfiguration.class,
                CrossAuthenticationConfiguration.class,
                MinecraftConfiguration.class
        })
public class OklahomaBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(
                OklahomaBotApplication.class,
                args
        );
    }
}
