package aoc2020;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;
import util.FUtils;
import util.Util;

public class Day09_EncodingError {
    public static void main(String[] args) {
        Util.profile(new Day09_EncodingError()::go, 1);
    }

    static final int PRE = 25;
    long[] in;
    List<Long> list;

    void go() {
        in = FUtils.readLineLongs(2020, 9);
        list = Longs.asList(in);

        long invalid = part1();

        part2(invalid);
    }

    long part1() {
        for (int i = PRE; i < in.length; i++) {
            long val = in[i];
            if (!isSum_FAST(i)) {
                return val;
            }
        }
        return -1;
    }

    // Original - part 1 takes about 0.02 ms
    private boolean isSum_FAST(int index) {
        for (int i = index - PRE; i < index; i++) {
            for (int j = index - PRE; j < index; j++) {
                if (in[i] != in[j] && in[i] + in[j] == in[index]) {
                    return true;
                }
            }
        }
        return false;
    }

    // Alternative - Good to know about in case I need combinations with > 2 elements!
    // But slow... part 1 takes 3-4 ms (warm)
    private boolean isSum_ALTERNATIVE(int index) {

        // NB order matters - ImmutableSet and 'combinations' both retain this
        // Using a Set already filters-out identical numbers
        ImmutableSet<Long> previous = ImmutableSet.copyOf(list.subList(index - PRE, index));
        return Sets.combinations(previous, 2)
            .stream()
            .anyMatch(set -> Util.sumBoxed(set) == in[index]);
    }

    private void part2(long invalid) {
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
}
