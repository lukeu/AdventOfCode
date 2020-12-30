package framework;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class AoC {

    public static void main(String[] args) {
        new AoC().run();
    }

    private static final int REPETITIONS = 5;
    private static final long MIN_WARMUP_TIME_MS = 300;

    private static final Function<List<Long>, String> PRINT_LAST = longs -> {
            long v = longs.get(longs.size() - 1);
            return v == 0 ? "-" : String.format("%8.2f", v / 1000000f);
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

    private final Map<String, DayStats> measurements = new TreeMap<>();

    private void run() {
        List<Class<? extends Base>> classes = findClasses();
        measure(classes);
        System.out.println(formatTable(PRINT_LAST));
        for (int i = 0; i < REPETITIONS; i++) {
            warmUp(classes);
            System.out.println("\n=== Run " + (i+1) + " ===");
            System.gc();
            long t0 = System.nanoTime();
            measure(classes);
            long t1 = System.nanoTime();

            // TODO: Rather than printing out each repetition, print a summary with variance/range
            System.out.println(formatTable(PRINT_LAST));
            System.out.format("TOTAL: %.2f ms\n", (t1 - t0) / 1000000f);
        }
    }

    private void warmUp(List<Class<? extends Base>> classes) {
        long t0 = System.nanoTime();
        classes.parallelStream().forEach(this::warmup);
        System.out.format("Time in warmUp: %.2f ms\n", (System.nanoTime() - t0) / 1_000_000f);
    }

    private void warmup(Class<? extends Base> c) {
        long t0 = System.nanoTime();
        do {
            invokeDefaultConstructor(c).go();
        } while (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t0) < MIN_WARMUP_TIME_MS);
    }

    private void measure(List<Class<? extends Base>> classes) {
        for (Class<? extends Base> c : classes) {
            DayStats ds = measurements.computeIfAbsent(prettyNameFor(c), n -> new DayStats());
            long t0 = System.nanoTime();
            Base b = invokeDefaultConstructor(c);
            if (b != null) {
                b.parse(b.input());
                long t1 = System.nanoTime();
                Object p1 = b.part1();
                long t2 = System.nanoTime();
                Object p2 = b.part2();
                long t3 = System.nanoTime();
                ds.record(t1-t0, p1 == null ? 0L : t2-t1, p2 == null ? 0L : t3-t2);
            }
        }
    }

    private String prettyNameFor(Class<?> c) {
        String name = c.getSimpleName();
        int under = name.indexOf('_');
        if (under < 0) {
            return "Day " + name.substring(3);
        }
        var caps = Pattern.compile("[A-Z]");
        var sb = new StringBuilder();
        sb.append("Day ");
        sb.append(name.substring(3, under));
        sb.append(caps.matcher(name.substring(under + 1)).replaceAll(" $0"));
        AocMeta meta = c.getAnnotation(AocMeta.class);
        if (meta != null && meta.notes() != null) {
            sb.append(" (").append(meta.notes()).append(')');
        }
        return sb.toString();
    }

    private String formatTable(Function<List<Long>, String> formatter) {
        List<List<String>> rows = new ArrayList<>();
        for (Entry<String, DayStats> entry : measurements.entrySet()) {
            DayStats ds = entry.getValue();
            rows.add(List.of(
                    entry.getKey(),
                    formatter.apply(ds.total),
                    formatter.apply(ds.parse),
                    formatter.apply(ds.part1),
                    formatter.apply(ds.part2)));
        }
        return TableFormatter.format(
                List.of("Name   ", " Total", " Parsing", " Part 1", " Part 2"),
                rows);
    }

    private Base invokeDefaultConstructor(Class<? extends Base> c) {
        try {
            Base b = c.getConstructor().newInstance();
            b.setQuiet();
            return b;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Class<? extends Base>> findClasses() {
        String pkg = "aoc2020"; // TODO: generalise!
        Pattern pattern = Pattern.compile(pkg + "\\.Day\\d.+");

        List<Class<? extends Base>> result = new ArrayList<>();
        for (var info : getClassesInPackage(pkg)) {
            String name = info.getName();
            if (pattern.matcher(name).matches()) {
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

    private ImmutableSet<ClassInfo> getClassesInPackage(String pkg) throws AssertionError {
        ClassPath cp;
        try {
            cp = ClassPath.from(AoC.class.getClassLoader());
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
        var classes = cp.getTopLevelClasses(pkg);
        return classes;
    }
}
