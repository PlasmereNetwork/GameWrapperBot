package info.addisoncrump.oklahoma.bot;

import info.addisoncrump.oklahoma.bot.auth.CrossAuthenticationConfiguration;
import info.addisoncrump.oklahoma.bot.discord.DiscordConfiguration;
import info.addisoncrump.oklahoma.bot.minecraft.MinecraftConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@EnableAutoConfiguration
@PropertySource(value = "file:oklahomabot.properties",
                ignoreResourceNotFound = true)
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
