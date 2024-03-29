package util;

import java.util.Arrays;
import java.util.Collection;

public class Util {

    /** For primitives, do instead like: Arrays.stream(arr, start, end).sum()  */
    public static long sumBoxed(Collection<? extends Number> coll) {
        long sum = 0;
        for (Number n : coll) {
            sum += n.longValue();
        }
        return sum;
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
