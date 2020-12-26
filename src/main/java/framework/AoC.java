package framework;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class AoC {

    public static void main(String[] args) {
        new AoC().run();
    }

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

    private void run() {
        List<Class<? extends Base>> classes = findClasses();
        Map<String, DayStats> measurements = new TreeMap<>();
        measure(measurements, classes);
        System.out.println(formatTable(measurements, PRINT_LAST));
        for (int i = 0; i < 5; i++) {
            warmUp(classes, 10);
            System.out.println("\n=== Run " + (i+1) + " ===");
            System.gc();
            measure(measurements, classes);
            System.out.println(formatTable(measurements, PRINT_LAST));
        }
    }

    private void measure(Map<String, DayStats> measurements, List<Class<? extends Base>> classes) {
        for (Class<? extends Base> c : classes) {
            DayStats ds = measurements.computeIfAbsent(prettyNameFor(c), n -> new DayStats());
            long t0 = System.nanoTime();
            Base b = invokeDefaultConstructor(c);
            if (b == null) continue;
            b.parse(b.input());
            long t1 = System.nanoTime();
            Object p1 = b.part1();
            long t2 = System.nanoTime();
            Object p2 = b.part2();
            long t3 = System.nanoTime();
            ds.record(t1-t0, p1 == null ? 0L : t2-t1, p2 == null ? 0L : t3-t2);
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

    private static final Function<List<Long>, String> PRINT_LAST = longs -> {
            long v = longs.get(longs.size() - 1);
            return v == 0 ? "-" : String.format("%8.2f", v / 1000000f);
    };


    private String formatTable(
            Map<String, DayStats> measurements,
            Function<List<Long>, String> formatter) {
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

    private void warmUp(List<Class<? extends Base>> classes, int count) {
        long t0 = System.nanoTime();
        createAll(classes, count)
            .parallelStream()
            .forEach(Base::go);
        long t1 = System.nanoTime();
        System.out.format("Time in warmUp: %.2f ms\n", (t1 - t0) / 1000000f);
    }

    private List<? extends Base> createAll(List<Class<? extends Base>> classes, int repititions) {
        return classes.stream()
                .flatMap(c -> Stream.generate(() -> invokeDefaultConstructor(c)).limit(repititions))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
