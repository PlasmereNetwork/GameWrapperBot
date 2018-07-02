package info.addisoncrump.oklahoma.bot.discord.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import info.addisoncrump.oklahoma.bot.auth.event.HandshakeStartedEvent;
import info.addisoncrump.oklahoma.bot.discord.event.NoUserFoundWithNameEvent;
import info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer;
import info.addisoncrump.oklahoma.bot.minecraft.listener.MinecraftPlayerLinkService;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public final class CrossAuthenticateInitializeCommand extends Command {
    private final MinecraftPlayerLinkService lookupService;
    private final ApplicationEventPublisher publisher;

    public CrossAuthenticateInitializeCommand(final @NonNull MinecraftPlayerLinkService lookupService,
                                              final @NonNull ApplicationEventPublisher publisher) {
        this.lookupService = lookupService;
        this.publisher = publisher;
    }

    @PostConstruct
    public void establish() {
        this.name = "crossauth";
        this.aliases = new String[]{"register"};
        this.help = "Crossauthenticates users between Minecraft and Discord (initialization phase)";
    }

    @Override
    protected void execute(final CommandEvent event) {
        SimplifiedMCPlayer mcPlayer = lookupService.findPlayerByName(event.getArgs());
        if (mcPlayer == null) {
            publisher.publishEvent(new NoUserFoundWithNameEvent(
                    event.getAuthor(),
                    event.getTextChannel(),
                    event.getArgs()
            ));
        } else {
            publisher.publishEvent(new HandshakeStartedEvent(
                    event.getAuthor(),
                    mcPlayer,
                    event.getTextChannel()
            ));
        }
    }
}
