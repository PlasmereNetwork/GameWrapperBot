package info.addisoncrump.oklahoma.bot.auth;

import info.addisoncrump.oklahoma.bot.auth.collect.VerifiedUserRegistry;
import info.addisoncrump.oklahoma.bot.auth.entity.AuthenticationToken;
import info.addisoncrump.oklahoma.bot.auth.entity.DiscordMCLink;
import info.addisoncrump.oklahoma.bot.auth.event.*;
import info.addisoncrump.oklahoma.bot.discord.entity.SimplifiedDiscordUser;
import info.addisoncrump.oklahoma.bot.minecraft.entity.SimplifiedMCPlayer;
import javafx.util.Pair;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuthenticationService {
    private final SecureRandom secureRandom;
    private final Map<AuthenticationToken, Pair<User, SimplifiedMCPlayer>> handshakePendingMap;
    private final VerifiedUserRegistry verifiedUserRegistry;
    private final ScheduledExecutorService authenticationExecutorService;
    private final ApplicationEventPublisher publisher;

    public AuthenticationService(final @NonNull SecureRandom secureRandom,
                                 final @NonNull @Qualifier("handshakePendingMap") Map<AuthenticationToken, Pair<User, SimplifiedMCPlayer>> handshakePendingMap,
                                 final @NonNull @Qualifier("authRegistry") VerifiedUserRegistry verifiedUserRegistry,
                                 final @NonNull ScheduledExecutorService authenticationExecutorService,
                                 final @NonNull ApplicationEventPublisher publisher) {
        this.secureRandom = secureRandom;
        this.handshakePendingMap = handshakePendingMap;
        this.verifiedUserRegistry = verifiedUserRegistry;
        this.authenticationExecutorService = authenticationExecutorService;
        this.publisher = publisher;
    }

    @EventListener
    public void onHandshakeStarted(final @NonNull HandshakeStartedEvent event) {
        final AuthenticationToken token = new AuthenticationToken(secureRandom);
        handshakePendingMap.put(
                token,
                new Pair<>(
                        event.getUser(),
                        event.getMcPlayer()
                )
        );
        authenticationExecutorService.schedule(
                () -> cancelHandshake(
                        token,
                        event.getChannel()
                ),
                1,
                TimeUnit.MINUTES
        );
        this.publisher.publishEvent(new HandshakeInitializedEvent(
                event.getUser(),
                event.getMcPlayer(),
                event.getChannel(),
                token
        ));
    }

    private void cancelHandshake(final @NonNull AuthenticationToken token,
                                 final @NonNull TextChannel channel) {
        Pair<User, SimplifiedMCPlayer> pair = handshakePendingMap.remove(token);
        if (pair != null) {
            this.publisher.publishEvent(new HandshakeCancelledEvent(
                    pair.getKey(),
                    channel
            ));
        }
    }

    @EventListener
    public void onHandshakeCancelled(final @NonNull HandshakeCancelledEvent event) {
        event.getChannel().sendMessage(String.format(
                "%s: The handshake timed out before it was verified.",
                event.getUser().getAsMention()
        )).complete();
    }

    @EventListener
    public void onVerificationAttempt(final @NonNull VerificationAttemptEvent event) {
        Pair<User, SimplifiedMCPlayer> result = handshakePendingMap.remove(event.getSubmittedToken());
        if (result != null && event.getUser().getId().equals(result.getKey().getId())) {
            this.publisher.publishEvent(new VerificationSuccessEvent(
                    event.getUser(),
                    event.getChannel(),
                    new DiscordMCLink(
                            result.getValue(),
                            new SimplifiedDiscordUser(event.getUser())
                    )
            ));
        } else {
            this.publisher.publishEvent(new VerificationFailedEvent(
                    event.getUser(),
                    event.getChannel()
            ));
        }
    }

    @EventListener
    public void onVerificationSuccess(final @NonNull VerificationSuccessEvent event) {
        this.verifiedUserRegistry.add(event.getLink());
    }
}
