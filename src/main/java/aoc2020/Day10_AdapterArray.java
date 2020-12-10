package aoc2020;

import java.util.Arrays;

import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Ints;
import framework.Base;
import framework.Input;
import util.Util;

public class Day10_AdapterArray extends Base {
    public static void main(String[] args) {
        Base.run(Day10_AdapterArray::new, 1);
    }

    @Override public Object expect1() { return 1917L; }
    @Override public Object expect2() { return 113387824750592L; }

    int[] in;
    int highest;
    ImmutableSet<Integer> set;

    @Override
    public void parse(Input input) {
        in = input.lineInts();
        highest = (int) Util.max(in) + 3;
        in = Ints.concat(new int[] {0}, in, new int[] {highest});
        Arrays.sort(in);
        set = ImmutableSet.copyOf(Ints.asList(in));
    }

    @Override
    public Long part1() {
        var diffs = new int[in.length - 1];
        for (int i = 0; i < diffs.length; i++) {
            diffs[i] = in[i+1] - in[i];
        }

        var one = Arrays.stream(diffs).filter(i -> i == 1).count();
        var three = Arrays.stream(diffs).filter(i -> i == 3).count();
        return one * three;
    }

    @Override
    public Long part2() {
        long[] comb = new long[highest + 1];
        return recurse(0L, comb, 0);
    }

    private long recurse(long acc, long[] comb, int i) {
        if (i == highest) {
            return 1;
        }
        long choices = 0;
        for (int j = i+1; j <= i+3; j++) {
            if (set.contains(j)) {
                if (comb[j] == 0) {
                    comb[j] = recurse(acc, comb, j);
                }
                choices += comb[j];
            }
        }
        return choices;
    }
}
