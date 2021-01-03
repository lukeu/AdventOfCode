package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class ByteBiterTest {

    @Test
    void testReadAsBinary_GivenDigits() {
        byte[] bytes = "1101".getBytes(StandardCharsets.US_ASCII);
        long actual = new ByteBiter(bytes).readAsBinary((byte) '0', (byte) '1', bytes.length);
        assertEquals(13L, actual);
    }

    @Test
    void testReadAsBinary_GivenChars() {
        byte[] bytes = "#..#.".getBytes(StandardCharsets.US_ASCII);
        long actual = new ByteBiter(bytes).readAsBinary((byte) '.', (byte) '#', bytes.length);
        assertEquals(18L, actual);
    }
}
