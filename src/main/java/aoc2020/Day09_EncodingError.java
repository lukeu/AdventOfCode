package aoc2020;

import java.util.Arrays;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;
import framework.Base;
import framework.Input;
import util.Util;

public class Day09_EncodingError extends Base {
    public static void main(String[] args) {
        Base.run(Day09_EncodingError::new, 1);
    }

    @Override public Object expect1() { return 21806024L; }
    @Override public Object expect2() { return 2986195L; }

    static final int PRE = 25;
    private long[] in;

    @Override
    public void parse(Input input) {
        in = input.lineLongs();
    }

    @Override
    public Long part1() {
        for (int i = PRE; i < in.length; i++) {
            long val = in[i];
            if (!isSum_FAST(i)) {
                return val;
            }
        }
        return -1L;
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
    @SuppressWarnings("unused")
    private boolean isSum_ALTERNATIVE(int index) {

        // NB order matters - ImmutableSet and 'combinations' both retain this
        // Using a Set already filters-out identical numbers
        var previous = ImmutableSet.copyOf(Longs.asList(in).subList(index - PRE, index));
        return Sets.combinations(previous, 2)
            .stream()
            .anyMatch(set -> Util.sumBoxed(set) == in[index]);
    }

    @Override
    public Object part2() {
        long invalid = part1();
        for (int s = 2; s < in.length; s++) {
            for (int i = 0; i < in.length - s; i++) {

                long sum = Arrays.stream(in, i, i+s).sum();
                if (sum == invalid) {
                    long min = Util.min(in, i, i+s);
                    long max = Util.max(in, i, i+s);

                    return min + max;
                }
            }
        }
        return -1L;
    }
}
