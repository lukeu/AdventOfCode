package framework;

import java.util.function.Supplier;

import util.FUtils;
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
        parse(testInput);
        printAndCheck("Test 1: ", testExpect1(), part1());
        printAndCheck("Test 2: ", testExpect2(), part2());
    }

    private boolean profiling = false;
    void profile() {
        profiling = true;
        go();
    }

    public void go() {
        parse(input());
        printAndCheck("PART 1: ", expect1(), part1());
        printAndCheck("PART 2: ", expect2(), part2());
    }

    private void printAndCheck(String heading, Object expected, Object got) {
        if (m_quiet) {
            return;
        }
        if (!profiling) {
            System.out.println(heading + got + (expected == null ? " (not checked)" : ""));
        }
        // Still check during daily profiling - this could catch stale state, or failing to re-parse
        if (expected != null && !expected.equals(got)) {
            System.out.println(" - EXPECTED: " + expected);
        }
    }

    public int year() {
        return 2020;
    }

    public int day() {
        return Integer.parseInt(getClass().getSimpleName().substring(3,5));
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


    public String input() {
        if (!profiling && !m_quiet) {
            System.out.println("Reading: " + FUtils.resourceName(year(), day()));
        }
        return FUtils.readString(year(), day());
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
