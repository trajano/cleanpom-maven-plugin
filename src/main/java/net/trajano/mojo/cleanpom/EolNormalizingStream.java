package net.trajano.mojo.cleanpom;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This ensures that the EOL characters are consistent when writing out the
 * stream. It will use the platform's EOL by default or a specified EOL
 * sequence. It simply strips out CR characters. This won't work correctly with
 * CR line endings, only CRLF or LF line endings will work correctly.
 *
 * @author Archimedes
 */
public class EolNormalizingStream extends FilterOutputStream {

    public static final String UNIX_LF = "\012";

    private final String lineSeparator;

    public EolNormalizingStream(final OutputStream os) {
        this(os, System.getProperty("line.separator"));
    }

    public EolNormalizingStream(final OutputStream os, final String lineSeparator) {
        super(os);
        this.lineSeparator = lineSeparator;
    }

    @Override
    public void write(final int b) throws IOException {

        if (b == 10) {
            for (final byte lb : lineSeparator.getBytes()) {
                super.write(lb);
            }
        } else if (b != 13) {
            super.write(b);
        }
    }
}
