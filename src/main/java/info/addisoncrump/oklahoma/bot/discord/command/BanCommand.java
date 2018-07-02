package info.addisoncrump.oklahoma.bot.discord.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import info.addisoncrump.oklahoma.bot.discord.event.BanOrderEvent;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public final class BanCommand extends Command {
    private final ApplicationEventPublisher publisher;
    private String moderationChannelID;

    public BanCommand(final @NonNull @Value("${oklahoma.bot.discord.moderation.chat}") String moderationChannelID,
                      final @NonNull ApplicationEventPublisher publisher) {
        this.moderationChannelID = moderationChannelID;
        this.publisher = publisher;
    }

    @PostConstruct
    public void establish() {
        this.name = "ban";
        this.help = "Orders a ban on the Minecraft server";
    }

    @Override
    protected void execute(final CommandEvent event) {
        if (moderationChannelID.equals(event.getChannel().getId())) {
            this.publisher.publishEvent(new BanOrderEvent(event.getArgs()));
        } else {
            event.reply(String.format(
                    "%s: Hmm, this doesn't seem to be the moderation chat. Try again there.",
                    event.getAuthor().getAsMention()
            ));
        }
    }
}
