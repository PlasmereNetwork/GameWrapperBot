package info.addisoncrump.oklahoma.bot.minecraft.streams;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.OutputStreamWriter;

@FieldDefaults(level = AccessLevel.PRIVATE,
               makeFinal = true)
public class MinecraftProcessInputWriter {
    Process minecraftProcess;
    PrintlnFriendlyBufferedWriter writer;

    public MinecraftProcessInputWriter(final @NonNull @Qualifier("minecraftProcess") Process minecraftProcess) {
        this.minecraftProcess = minecraftProcess;
        writer = new PrintlnFriendlyBufferedWriter(new OutputStreamWriter(minecraftProcess.getOutputStream()));
    }

    @PreDestroy
    public void cleanup() throws
                          IOException {
        writer.close();
    }

    public synchronized void println(String line) throws
                                                  IOException {
        writer.println(line);
        minecraftProcess.getOutputStream().flush();
    }
}
