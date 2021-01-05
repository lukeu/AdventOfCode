package aoc2020;

import java.util.BitSet;

import framework.Base;
import framework.Input;
import util.ByteBiter;

public class Day10_AdapterArray extends Base {
    public static void main(String[] args) {
        Base.run(Day10_AdapterArray::new, 1);
    }

    @Override
    public String testInput() {
        return "16 10 15 5 1 11 7 19 6 12 4";
    }

    @Override public Object testExpect1() { return 7*5; }
    @Override public Object testExpect2() { return 8L; }
    @Override public Object expect1() { return 1917; }
    @Override public Object expect2() { return 113387824750592L; }

    int highest;
    BitSet bs;

    @Override
    public void parse(Input input) {
        byte[] bytes = input.bytes(this);
        bs = new BitSet(bytes.length * 2 / 3); // crude approximation
        new ByteBiter(bytes).readPositiveInts(bs::set);
        highest = bs.length() - 1 + 3;
        bs.set(0);
        bs.set(highest);
    }

    @Override
    public Integer part1() {
        // Adapters are always 1 or 3 "jolts" apart, so threes appear as a pair of 0-bits.
        int three = (highest - bs.cardinality() + 1) / 2;
        int one = highest - three*3;
        return one * three;
    }

    @Override
    public Long part2() {
        long[] comb = new long[highest + 1];
        return recurse(comb, 0);
    }

    private long recurse(long[] comb, int i) {
        if (i == highest) {
            return 1;
        }
        long choices = 0;
        for (int j = i+1; j <= i+3; j++) {
            if (bs.get(j)) {
                if (comb[j] == 0) {
                    comb[j] = recurse(comb, j);
                }
                choices += comb[j];
            }
        }
        return choices;
    }
}
