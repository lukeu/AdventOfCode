package aoc2020;

import java.util.BitSet;

import framework.Base;
import framework.Input;

public class Day01_ReportRepair extends Base {

    public static void main(String[] args) {
        Base.run(Day01_ReportRepair::new, 1);
    }

    @Override public Object expect1() { return 32064; }
    @Override public Object expect2() { return 193598720; }

    int[] in;
    BitSet bs;

    @Override
    public void parse(Input input) {

        // Occurrence bitset is used both for O(N) sorting and O(1) lookup
        // credit to https://github.com/Voltara/advent2020-fast
        bs = new BitSet(2020);
        input.withReaderDo(r -> r.lines()
            .mapToInt(Integer::parseInt)
            .forEach(bs::set));

        in = new int[bs.cardinality()];
        int i = 0;
        for (int b = bs.nextSetBit(0); b >= 0; b = bs.nextSetBit(b+1)) {
            in[i++] = b;
            if (b == Integer.MAX_VALUE) {
                break;
            }
        }
    }

    @Override
    public Object part1() {
        for (int a = 0; a < in.length; ++a) {
            int diff = 2020 - in[a];
            if (bs.get(diff)) {
                return in[a] * diff;
            }
        }
        return -1;
    }

    @Override
    public Object part2() {
        for (int a : in) {
            for (int ib = 0; ib < in.length; ++ib) {
                int diff = 2020 - a - in[ib];
                if (diff < 0) {
                    break;
                }
                if (bs.get(diff)) {
                    return a * in[ib] * diff;
                }
            }
        }
        return null;
    }
}
