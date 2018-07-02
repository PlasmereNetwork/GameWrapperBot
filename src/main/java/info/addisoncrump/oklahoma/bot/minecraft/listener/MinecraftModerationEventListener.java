package info.addisoncrump.oklahoma.bot.minecraft.listener;

import info.addisoncrump.oklahoma.bot.minecraft.event.BanOrderEvent;
import info.addisoncrump.oklahoma.bot.minecraft.event.KickOrderEvent;
import info.addisoncrump.oklahoma.bot.minecraft.event.PardonOrderEvent;
import info.addisoncrump.oklahoma.bot.minecraft.streams.MinecraftProcessInputWriter;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MinecraftModerationEventListener {
    private final MinecraftProcessInputWriter writer;

    public MinecraftModerationEventListener(final @NonNull MinecraftProcessInputWriter writer) {
        this.writer = writer;
    }

    @EventListener
    public void onKickOrdered(KickOrderEvent event) throws
                                                    IOException {
        writer.println(String.format(
                "/kick %s",
                event.getArgs()
        ));
    }

    @EventListener
    public void onBanOrdered(BanOrderEvent event) throws
                                                  IOException {
        writer.println(String.format(
                "/ban %s",
                event.getArgs()
        ));
    }

    @EventListener
    public void onBanOrdered(PardonOrderEvent event) throws
                                                  IOException {
        writer.println(String.format(
                "/pardon %s",
                event.getArgs()
        ));
    }
}
