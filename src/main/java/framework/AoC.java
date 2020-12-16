package framework;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        public String reportLatest(String name) {
            int last = total.size() - 1;
            return String.format("%s Took %7.2f ms (parse: %7.2f, Part1: %7.2f, Part2: %7.2f)",
                    name,
                    total.get(last) / 1000000f,
                    parse.get(last) / 1000000f,
                    part1.get(last) / 1000000f,
                    part2.get(last) / 1000000f);
        }
    }

    private void run() {
        List<Class<? extends Base>> classes = findClasses();
        Map<String, DayStats> measurements = new TreeMap<>();
        measure(measurements, classes);
        for (int i = 0; i < 5; i++) {
            warmUp(classes, 10);
            System.out.println("\n=== Run " + (i+1) + " ===");
            System.gc();
            measure(measurements, classes);
        }
    }

    private void measure(Map<String, DayStats> measurements, List<Class<? extends Base>> classes) {
        for (Class<? extends Base> c : classes) {
            DayStats ds = measurements.computeIfAbsent(c.getSimpleName(), n -> new DayStats());
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
