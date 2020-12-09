package util;

import java.util.Arrays;

public class Util {

    public static void profile(Runnable run, int iterations) {
        for (int i = 0; i < iterations; i++) {
            long t0 = System.nanoTime();
            run.run();
            long t1 = System.nanoTime();
            System.out.format("Time to run: %.2f ms\n", (t1 - t0) / 1000000f);
        }
    }

    public static long max(int[] arr) {
        return Arrays.stream(arr).max().getAsInt();
    }
    public static long max(long[] arr) {
        return Arrays.stream(arr).max().getAsLong();
    }
    public static long max(long[] arr, int start, int end) {
        return Arrays.stream(arr, start, end).max().getAsLong();
    }
    public static int max(int[] arr, int start, int end) {
        return Arrays.stream(arr, start, end).max().getAsInt();
    }

    public static long min(int[] arr) {
        return Arrays.stream(arr).min().getAsInt();
    }
    public static long min(long[] arr) {
        return Arrays.stream(arr).min().getAsLong();
    }
    public static long min(long[] arr, int start, int end) {
        return Arrays.stream(arr, start, end).min().getAsLong();
    }
    public static int min(int[] arr, int start, int end) {
        return Arrays.stream(arr, start, end).min().getAsInt();
    }
}
