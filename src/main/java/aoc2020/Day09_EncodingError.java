package aoc2020;

import java.util.Arrays;

import util.FUtils;
import util.Util;

public class Day09_EncodingError {
    public static void main(String[] args) {
        new Day09_EncodingError().go();
    }

    int pre = 25;
    long[] in;
    void go() {
        in = FUtils.readLineLongs(2020, 9);

        long invalid = part1();
        System.out.println(invalid);

        for (int s = 2; s < in.length; s++) {
            for (int i = 0; i < in.length - s; i++) {

                long sum = Arrays.stream(in, i, i+s).sum();
                if (sum == invalid) {
                    long min = Util.min(in, i, i+s);
                    long max = Util.max(in, i, i+s);
                    System.out.println(min + max);
                    return;
                }
            }
        }
    }

    private long part1() {
        for (int i = pre; i < in.length; i++) {
            long val = in[i];
            if (!isSum(val, i-pre)) {
                return val;
            }
        }
        return -1;
    }

    private boolean isSum(long expect, int start) {
        for (int i = start; i < start+pre; i++) {
            for (int j = start; j < start+pre; j++) {
                if (in[i] != in[j] && in[i] + in[j] == expect) {
                    return true;
                }
            }
        }
        return false;
    }
}
