package framework;

import java.util.function.Supplier;

import util.SUtils;

public class Base {

    /**
     * @param repetitions number of runs (after testing); negative = no testing
     */
    public static void run(Supplier<Base> ctor, int repetitions) {
        if (repetitions >= 0) {
            ctor.get().test();
        }
        if (repetitions == 0) {
            return;
        }
        profile(() -> ctor.get().go(), 1);

        if (Math.abs(repetitions) > 1) {
            System.out.println("Profiling... ");
            profile(() -> ctor.get().profile(), Math.abs(repetitions) - 1);
        }
    }

    private final int day = Integer.parseInt(getClass().getSimpleName().substring(3,5));

    private boolean quiet = false;
    private boolean profiling = false;
    private boolean testing = false;

    public void test() {
        if (testInput().isBlank()) {
            return;
        }
        testing = true;
        try {
            parse(input());
            printAndCheck("Test 1: ", testExpect1(), part1());
            printAndCheck("Test 2: ", testExpect2(), part2());
        } finally {
            testing = false;
        }
    }


    void profile() {
        profiling = true;
        go();
    }

    /** Just less quiet. */
    public void setProfiling() {
        profiling = true;
    }

    /** Used by the AoC bulk-runner. */
    void setQuiet() {
        quiet = true;
    }

    public void go() {
        parse(input());
        printAndCheck("PART 1: ", expect1(), part1());
        printAndCheck("PART 2: ", expect2(), part2());
    }

    Object printAndCheck(String heading, Object expected, Object got) {
        if (!quiet) {
            boolean ok = expected == null || compare(expected, got);
            String warning = (expected == null) ? " (not checked)"
                    : ok ? "" : (" - EXPECTED " + expected);

            // Still check during daily profiling - could catch stale state, or failing to re-parse
            if (!profiling || !ok) {
                System.out.format("%s Day %s %sgot %s%s\n", year(), day(), heading, got, warning);
            }
        }
        return got;
    }


    private  boolean compare(Object expected, Object got) {
        return got instanceof Number n && ((Number) expected).doubleValue() == n.doubleValue()
                || expected.equals(got);
    }

    static void profile(Runnable run, int iterations) {
        for (int i = 0; i < iterations; i++) {
            long t0 = System.nanoTime();
            run.run();
            long t1 = System.nanoTime();
            System.out.format("Time to run: %.2f ms\n", (t1 - t0) / 1000000f);
        }
    }

    public int year() {
        return SUtils.extractInts(getClass().getName())[0];
    }

    public int day() {
        return day;
    }

    public void parse(Input input) {
    }

    public String testInput() {
        return "";
    }
    public Object testExpect1() {
        return null;
    }
    public Object testExpect2() {
        return null;
    }

    public Input input() {
        if (testing) {
            return new Input(testInput());
        }
        if (!profiling && !quiet) {
            System.out.println("Reading: " + Input.resourceName(year(), day()));
        }
        return new Input(year(), day());
    }

    public Object part1() {
        return null;
    }
    public Object part2() {
        return null;
    }
    public Object expect1() {
        return null;
    }
    public Object expect2() {
        return null;
    }
}
