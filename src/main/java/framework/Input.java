package framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;
import com.google.common.primitives.Ints;

public class Input {

    private final Supplier<BufferedReader> supplier;

    Input(int year, int day) {
        this(() -> newReader(newStream(year, day)));
    }

    public Input(String text) {
        this(() -> new BufferedReader(new StringReader(text)));
    }

    Input(Supplier<BufferedReader> supplier) {
        this.supplier = supplier;
    }

    public String text() {
        try (BufferedReader reader = supplier.get()) {
            return CharStreams.toString(reader).trim();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<String> lines() {
        return withReader(r -> r.lines().collect(Collectors.toList()));
    }

    public int[] lineInts() {
        return withReader(r -> r.lines()
                .filter(s -> !s.isEmpty())
                .mapToInt(Integer::parseInt)
                .toArray());
    }

    public long[] lineLongs() {
        return withReader(r -> r.lines()
                .filter(s -> !s.isEmpty())
                .mapToLong(Long::parseLong)
                .toArray());
    }

    private static final Pattern INT_SEP = Pattern.compile("[^0-9\\-]+");

    public int[] ints() {
        return withReader(r -> r.lines()
                .flatMap(s -> Splitter.on(INT_SEP).omitEmptyStrings().splitToList(s).stream())
                .mapToInt(Ints::tryParse)
                .filter(Objects::nonNull) // Skip '-' when it does not prefix a digit
                .toArray());
    }

    /** A bit verbose, but avoids splitting or processing the input multiple times. */
    public List<String> blocks() {
        return withReader(r -> {
            var result = new ArrayList<String>();
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                while ((line = r.readLine()) != null) {
                    if (line.isEmpty()) {
                        result.add(sb.toString());
                        if (!sb.isEmpty()) {
                            sb = new StringBuilder();
                        }
                    } else {
                        sb.append(line).append('\n');
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!sb.isEmpty()) {
                result.add(sb.toString());
            }
            return result;
        });
    }

    private <T> T withReader(Function<BufferedReader, T> fn) {
        try (var br = supplier.get()) {
            return fn.apply(br);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static BufferedReader newReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    private static InputStream newStream(int year, int day) {
        return Input.class.getClassLoader().getResourceAsStream(resourceName(year, day));
    }

    public static String resourceName(int year, int day) {
        return String.format("%d/in_%02d.txt", year, day);
    }
}
