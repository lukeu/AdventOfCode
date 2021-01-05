package util;

import java.nio.charset.StandardCharsets;
import java.util.function.IntConsumer;

/**
 * Similar to a ByteBuffer, but being less abstract and simpler yields a small perf benefit (~10%)
 * and allows things like moving the position arbitrarily.
 *
 * <p>This is not intended for use with initial puzzle solving, but rather for post-optimisation of
 * very quick-running puzzles.
 */
public class ByteBiter {

    public final byte[] bytes;
    public int pos;

    public ByteBiter(byte[] bytes) {
        this.bytes = bytes;
    }

    public boolean hasRemaining() {
        return pos < bytes.length;
    }

    public byte get() {
        return bytes[pos++];
    }


    public byte peek() {
        return bytes[pos];
    }

    /** Reads ASCII text, leaving the position at the next non-digit. */
    public int positiveInt() {
        int acc = 0;
        do {
            int i = bytes[pos] - '0';
            if (i >= 0 && i <= 9) {
                acc = acc * 10 + i;
            } else {
                break;
            }
        } while (++pos < bytes.length);
        return acc;
    }

    /**
     * Reads a sequence of positive (well, non-negative) integers separated by a single non-digit
     * byte - like '\n', ' '  or ',' - through to the end of the input
     */
    public void readPositiveInts(IntConsumer consumer) {
        while (hasRemaining()) {
            consumer.accept(positiveInt());
            if (hasRemaining()) {
                skip();
            }
        }
    }

    /**
     * Reads ASCII text, leaving the position at the next ' ' or '\n' (NB: limited ws support!)
     */
    public String extractToWhitespace() {
        int start = pos;
        do {
            int b = bytes[pos];
            if (b == ' ' || b == '\n') {
                break;
            }
        } while (++pos < bytes.length);
        return new String(bytes, start, pos - start, StandardCharsets.US_ASCII);
    }

    /** interprets 4 bytes as a BINARY integer from the file, big endian */
    public int getBinaryInt() {
        return (get() << 24)
                + (get() << 16)
                + (get() << 8)
                + get();
    }

    public void skip() {
        pos++;
    }

    public long readAsBinary(byte zero, byte one, int num) {
        return zero > one
                ? readAsBinary0(zero, num)
                : readAsBinary1(one, num);
    }

    private long readAsBinary0(byte zero, int num) {
        long acc = 0;
        for (int i = 0; i < num; i++) {
            byte b = get();
            int v = (b - zero) >>> 31; // Read the negative bit. A ~tiny~ bit faster than a ternary
            acc = (acc << 1) + v;
        }
        return acc;
    }

    private long readAsBinary1(byte one, int num) {
        long acc = 0;
        for (int i = 0; i < num; i++) {
            byte b = get();
            int v = 1 - ((b - one) >>> 31); // Read the negative bit.
            acc = (acc << 1) + v;
        }
        return acc;
    }

    public void consumeUntilWs() {
        while (hasRemaining() && peek() != '\n' && peek() != ' ') {
            skip();
        }
    }
}
