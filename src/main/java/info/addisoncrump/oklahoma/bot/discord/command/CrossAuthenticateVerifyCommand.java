package info.addisoncrump.oklahoma.bot.discord.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import info.addisoncrump.oklahoma.bot.auth.event.VerificationAttemptEvent;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public final class CrossAuthenticateVerifyCommand extends Command {
    private final ApplicationEventPublisher publisher;

    public CrossAuthenticateVerifyCommand(final @NonNull ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostConstruct
    public void establish() {
        this.name = "crossverify";
        this.aliases = new String[]{"verify"};
        this.help = "Crossauthenticates users between Minecraft and Discord (verification stage)";
    }

    @Override
    protected void execute(final CommandEvent event) {
        this.publisher.publishEvent(new VerificationAttemptEvent(
                event.getAuthor(),
                event.getTextChannel(),
                event.getArgs()
        ));
    }
}
