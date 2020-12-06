package util;

public class Util {

    public static void profile(Runnable run, int iterations) {
        for (int i = 0; i < iterations; i++) {
            long t0 = System.nanoTime();
            run.run();
            long t1 = System.nanoTime();
            System.out.format("Time to run: %.2f ms\n", (t1 - t0) / 1000000f);
        }
    }
}
