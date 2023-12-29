package framework;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;
import com.google.common.primitives.Ints;

public class Input {

    private final Supplier<BufferedReader> supplier;
    private final boolean isText;

    public Input(int year, int day) {
        this.supplier = () -> newReader(newStream(year, day));
        isText = false;
    }

    public Input(String text) {
        this.supplier = () -> new BufferedReader(new StringReader(text));
        isText = true;
    }

    /** All input as text, trimmed. */
    public String text() {
        try (BufferedReader reader = supplier.get()) {
            return CharStreams.toString(reader).trim();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Each lines in input (including blanks) */
    public List<String> lines() {
        return withReader(r -> r.lines().toList());
    }

    /** One number per line. (blanks ignored) */
    public long[] lineLongs() {
        return withReader(r -> r.lines()
                .filter(s -> !s.isBlank())
                .mapToLong(Long::parseLong)
                .toArray());
    }

    private static final Pattern INT_SEP = Pattern.compile("[^0-9\\-]+");

    /** Extract ints from all line(s) with any separators. Includes negatives. */
    public int[] ints() {
        return withReader(r -> r.lines()
                .flatMap(s -> Splitter.on(INT_SEP).omitEmptyStrings().splitToList(s).stream())
                .mapToInt(Ints::tryParse)
                .filter(Objects::nonNull) // Skip '-' when it does not prefix a digit
                .toArray());
    }

    /**
     * For input with multiple sections, separated by blank lines.
     * @return Each multi-line block, '\n' separated.
     */
    public List<String> blocks() {

        // A bit verbose, but avoids splitting or processing the input multiple times.
        return withReader(r -> {
            var result = new ArrayList<String>();
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                while ((line = r.readLine()) != null) {
                    if (line.isEmpty()) {
                        result.add(sb.toString().stripTrailing());
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
                result.add(sb.toString().stripTrailing());
            }
            return result;
        });
    }

    public void withReaderDo(Consumer<BufferedReader> doer) {
        try (var br = supplier.get()) {
            doer.accept(br);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public <T> T withReader(Function<BufferedReader, T> fn) {
        try (var br = supplier.get()) {
            return fn.apply(br);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public byte[] bytes(Base base) {
        if (!isText) {
            byte[] cached = preloadCache.get(cacheKey(base.year(), base.day()));
            if (cached != null) {
                return cached;
            }
        }
        try {
            return CharStreams.toString(supplier.get()).getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    private static BufferedReader newReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    private static InputStream newStream(int year, int day) {
        byte[] cached = preloadCache.get(cacheKey(year, day));
        if (cached != null) {
            return new ByteArrayInputStream(cached);
        }

        // TODO: generalise (e.g. multiple directories of test data)
        File f = Path.of("src/main/resources").resolve(resourceName(year, day)).toFile();
        try {
            if (f.exists()) {
                return new FileInputStream(f);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return Input.class.getClassLoader().getResourceAsStream(resourceName(year, day));
    }

    public static String resourceName(int year, int day) {
        return String.format("%d/in_%02d.txt", year, day);
    }

    private static Map<Integer, byte[]> preloadCache = new HashMap<>();

    static boolean preload(int year, int day) {
        try {
            var bytes = new BufferedInputStream(newStream(year, day)).readAllBytes();
            preloadCache.put(cacheKey(year, day), bytes);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static int cacheKey(int year, int day) {
        return year * 100 + day;
    }

}
