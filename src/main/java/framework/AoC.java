package framework;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import framework.TableFormatter.Row;

public class AoC {

    public static void main(String[] args) {
        new AoC().run();
    }

    private static final int REPETITIONS = 5;
    private static final long MIN_WARMUP_TIME_MS = 300;

    private static final Pattern CLASS_PATTERN = Pattern.compile("aoc(202.?)\\.Day(\\d+).+");

    private static final Function<List<Long>, String> PRINT_LAST = longs -> {
            long v = longs.get(longs.size() - 1);
            return v == 0 ? "-" : String.format("%,8.0f", v / 1000f);
    };

    record DayStats(
            List<Long> total,
            List<Long> parse,
            List<Long> part1,
            List<Long> part2) {
        public DayStats() {
            this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        public void record(long p, long p1, long p2) {
            total.add(p + p1 + p2);
            parse.add(p);
            part1.add(p1);
            part2.add(p2);
        }
    }

    record Key(int year, String title) {
        static Key from(Class<?> c) {
            Matcher m = CLASS_PATTERN.matcher(c.getName());
            m.matches();
            int year = Integer.parseInt(m.group(1));
            return new Key(year, prettyNameFor(c));
        }
    }

    private final SortedMap<Key, DayStats> measurements = new TreeMap<>(
            Comparator.comparing(Key::year).thenComparing(Key::title));

    private void run() {
        List<Class<? extends Base>> classes = findClasses();

        preload();
        measure(classes, false);
        System.out.println(formatTable(PRINT_LAST));
        for (int i = 0; i < REPETITIONS; i++) {
            warmUp(classes);
            System.out.println("\n=== Run " + (i+1) + " ===");
            System.gc();
            long total = measure(classes, true);

            // TODO: Rather than printing out each repetition, print a summary with variance/range
            System.out.println(formatTable(PRINT_LAST));
            System.out.format("TOTAL: %,.0f μs\n", total / 1000f);
        }
    }

    private static void preload() {
        long t0 = System.nanoTime();
        int count = 0;
        for (var info : getClasses()) {
            Matcher m = CLASS_PATTERN.matcher(info.getName());
            if (m.matches()) {
                int year = Integer.parseInt(m.group(1));
                int day = Integer.parseInt(m.group(2));
                if (Input.preload(year, day)) {
                    count ++;
                }
            }
        }

        long t1 = System.nanoTime();
        System.out.format("Preloaded %d inputs in: %.0f ms\n", count, (t1 - t0) / 1000000f);
    }

    private void warmUp(List<Class<? extends Base>> classes) {
        long t0 = System.nanoTime();
        classes.parallelStream().forEach(this::warmup);
        System.out.format("\nWarm-up wall time: %.2f ms\n", (System.nanoTime() - t0) / 1_000_000f);
    }

    private void warmup(Class<? extends Base> c) {
        long t0 = System.nanoTime();
        do {
            Base b = invokeDefaultConstructor(c);
            b.setQuiet();
            b.go();
        } while (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t0) < MIN_WARMUP_TIME_MS);
    }

    private long measure(List<Class<? extends Base>> classes, boolean quiet) {
        long total = 0;
        for (Class<? extends Base> c : classes) {
            DayStats ds = measurements.computeIfAbsent(Key.from(c), n -> new DayStats());
            long t0 = System.nanoTime();
            Base b = invokeDefaultConstructor(c);
            if (b != null) {
                if (quiet) {
                    b.setQuiet();
                } else {
                    b.setProfiling();
                }
                b.parse(b.input());
                long t1 = System.nanoTime();
                Object p1 = b.printAndCheck("PART 1: ", b.expect1(), b.part1());
                long t2 = System.nanoTime();
                Object p2 = b.printAndCheck("PART 1: ", b.expect2(), b.part2());
                long t3 = System.nanoTime();
                ds.record(t1-t0, p1 == null ? 0L : t2-t1, p2 == null ? 0L : t3-t2);
                total += (t3 - t0);
            }
        }
        return total;
    }

    private static String prettyNameFor(Class<?> c) {
        String name = c.getSimpleName();
        int under = name.indexOf('_');
        if (under < 0) {
            return "Day " + name.substring(3);
        }
        return "Day "
                + name.substring(3, under)
                + name.substring(under + 1).replaceAll("[A-Z]", " $0")
                + getNotes(c);
    }

    private static String getNotes(Class<?> c) {
        AocMeta meta = c.getAnnotation(AocMeta.class);
        return (meta == null || meta.notes() == null) ? "" : " (" + meta.notes() + ')';
    }

    private String formatTable(Function<List<Long>, String> formatter) {
        int lastYear = -1;
        List<Row> rows = new ArrayList<>();
        for (var entry : measurements.entrySet()) {
            if (lastYear != entry.getKey().year()) {
                lastYear = entry.getKey().year();
                rows.add(Row.heading(
                        "YEAR " + lastYear, " Total μs", " Parsing", " Part 1", " Part 2"));
            }
            DayStats ds = entry.getValue();
            rows.add(Row.ofData(
                    entry.getKey().title(),
                    formatter.apply(ds.total),
                    formatter.apply(ds.parse),
                    formatter.apply(ds.part1),
                    formatter.apply(ds.part2)));
        }
        return TableFormatter.format(rows);
    }

    private static Base invokeDefaultConstructor(Class<? extends Base> c) {
        try {
            return c.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Class<? extends Base>> findClasses() {
        List<Class<? extends Base>> result = new ArrayList<>();
        for (var info : getClasses()) {
            String name = info.getName();
            if (CLASS_PATTERN.matcher(name).matches()) {
                try {
                    Class<?> clazz = AoC.class.getClassLoader().loadClass(name);
                    if (clazz.getSuperclass() == Base.class) {
                        @SuppressWarnings("unchecked")
                        var base = (Class<? extends Base>) clazz;
                        result.add(base);
                    } else {
                        System.err.println(name + " does not extend from Base");
                    }
                } catch (ClassNotFoundException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static ImmutableSet<ClassInfo> getClasses() throws AssertionError {
        ClassPath cp;
        try {
            cp = ClassPath.from(AoC.class.getClassLoader());
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
        return cp.getTopLevelClasses();
    }
}
