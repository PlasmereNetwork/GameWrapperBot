package info.addisoncrump.oklahoma.bot.minecraft.listener;

import info.addisoncrump.oklahoma.bot.auth.event.HandshakeInitializedEvent;
import info.addisoncrump.oklahoma.bot.minecraft.streams.MinecraftProcessInputWriter;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MinecraftAuthenticationEventListener {
    private final MinecraftProcessInputWriter writer;

    public MinecraftAuthenticationEventListener(final @NonNull MinecraftProcessInputWriter writer) {
        this.writer = writer;
    }

    @EventListener
    public void onHandshakeInitialized(HandshakeInitializedEvent event) throws
                                                                        IOException {
        writer.println(String.format(
                "/tell %s Your authentication token is: %s",
                event.getMcPlayer().getName(),
                event.getToken().toString()
        ));
    }
}
