package info.addisoncrump.oklahoma.bot.minecraft.streams;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class PrintlnFriendlyBufferedWriter extends BufferedWriter {
    public PrintlnFriendlyBufferedWriter(final Writer out) {
        super(out);
    }

    public PrintlnFriendlyBufferedWriter(final Writer out,
                                         final int sz) {
        super(
                out,
                sz
        );
    }

    public synchronized void println(String line) throws
                                                  IOException {
        write(line);
        newLine();
        flush();
    }
}
