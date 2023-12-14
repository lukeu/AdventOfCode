package framework;

import java.util.function.Supplier;

import util.SUtils;
import util.Util;

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
        Util.profile(() -> ctor.get().go(), 1);

        if (Math.abs(repetitions) > 1) {
            System.out.println("Profiling... ");
            Util.profile(() -> ctor.get().profile(), Math.abs(repetitions) - 1);
        }
    }

    private final int day = Integer.parseInt(getClass().getSimpleName().substring(3,5));

    /** Used by the AoC bulk-runner. */
    private boolean m_quiet = false;

    void setQuiet() {
        m_quiet = true;
    }

    public void test() {
        String testInput = testInput();
        if (testInput.isEmpty()) {
            return;
        }
        parse(new Input(testInput));
        printAndCheck("Test 1: ", testExpect1(), part1());
        printAndCheck("Test 2: ", testExpect2(), part2());
    }

    private boolean profiling = false;

    void profile() {
        profiling = true;
        go();
    }

    /** Just less quiet. */
    public void setProfiling() {
        profiling = true;
    }

    public void go() {
        parse(input());
        printAndCheck("PART 1: ", expect1(), part1());
        printAndCheck("PART 2: ", expect2(), part2());
    }

    Object printAndCheck(String heading, Object expected, Object got) {
        if (!m_quiet) {
            if (!profiling) {
                System.out.println(heading + got + (expected == null ? " (not checked)" : ""));
            }
            // Still check during daily profiling - this could catch stale state, or failing to re-parse
            if (expected != null && !expected.equals(got)) {
                if (profiling) {
                    System.out.format(
                            "Day %s %sgot %s -- EXPECTED %s\n", day(), heading, got, expected);
                } else {
                    System.out.println(" - EXPECTED " + expected);
                }
            }
        }
        return got;
    }

    public int year() {
        return SUtils.extractInts(getClass().getName())[0];
    }

    public int day() {
        return day;
    }

    public void parse(Input input) {
        if (input != null) {
            parse(input.text());
        }
    }

    public void parse(String text) {
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
        if (!profiling && !m_quiet) {
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
