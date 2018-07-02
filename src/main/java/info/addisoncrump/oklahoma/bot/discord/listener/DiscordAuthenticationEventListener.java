package info.addisoncrump.oklahoma.bot.discord.listener;

import info.addisoncrump.oklahoma.bot.auth.event.HandshakeInitializedEvent;
import info.addisoncrump.oklahoma.bot.auth.event.NoUserFoundWithNameEvent;
import info.addisoncrump.oklahoma.bot.auth.event.VerificationFailedEvent;
import info.addisoncrump.oklahoma.bot.auth.event.VerificationSuccessEvent;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DiscordAuthenticationEventListener {
    private final String prefix;

    public DiscordAuthenticationEventListener(final @NonNull @Value("${oklahoma.bot.discord.command.prefix}") String prefix) {
        this.prefix = prefix;
    }

    @EventListener
    public void onHandshakeInitialized(final @NonNull HandshakeInitializedEvent event) {
        event.getChannel().sendMessage(String.format(
                "%s: Sent you your token in Minecraft. Once you see this, use '%sverify <token>' (without the <>) to verify.",
                event.getUser().getAsMention(),
                prefix
        )).complete();
    }

    @EventListener
    public void onNoUserFoundWithName(final @NonNull NoUserFoundWithNameEvent event) {
        event.getChannel().sendMessage(String.format(
                "%s: Couldn't find a user with the specified name. Make sure you're online before trying to verify.",
                event.getUser().getAsMention()
        )).complete();
    }

    @EventListener
    public void onVerificationFailed(final @NonNull VerificationFailedEvent event) {
        event.getChannel().sendMessage(String.format(
                "%s: Your token was invalid! You have not been authenticated.",
                event.getUser().getAsMention()
        )).complete();
    }

    @EventListener
    public void onVerificationSuccess(final @NonNull VerificationSuccessEvent event) {
        event.getChannel().sendMessage(String.format(
                "%s: Your token was valid! You have been authenticated and now may use the chat relay.",
                event.getUser().getAsMention()
        )).complete();
    }
}
