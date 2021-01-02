package util;

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

    public int positiveInt() {
        int acc = 0;
        do {
            int i = bytes[pos];
            if (i >= '0' && i <= '9') {
                acc = acc * 10 + (i - '0');
            } else {
                break;
            }
        } while (++pos < bytes.length);
        return acc;
    }

    public void skip() {
        pos++;
    }
}
