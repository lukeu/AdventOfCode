package aoc2020;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.AocMeta;
import framework.Base;
import util.SUtils;

@AocMeta(notes = "Floating bit-masks")
public class Day14_DockingData extends Base {
    public static void main(String[] args) {
        Base.run(Day14_DockingData::new, 1);
    }

    @Override
    public String testInput() { // Part 2 only
        return "mask = 000000000000000000000000000000X1001X\n"
                + "mem[42] = 100\n"
                + "mask = 00000000000000000000000000000000X0XX\n"
                + "mem[26] = 1";
    }
    @Override public Object testExpect2() { return 208L; }
    @Override public Object expect1() { return 4886706177792L; }
    @Override public Object expect2() { return 3348493585827L; }

    private static final int NBITS = 36;

    List<String> in;
    Map<Long, Long> mem1 = new HashMap<>();
    Map<Long, Long> mem2 = new HashMap<>();
    long andMask;
    long orMask;
    List<Integer> xx;

    @Override
    public void parse(String text) {
        andMask = orMask = 0;
        in = SUtils.lines(text);
    }

    @Override
    public Long part1() {
        for (String s : in) {
            if (s.startsWith("mask = ")) {
                String mask = s.substring(7);
                andMask = Long.parseLong(mask.replace('X', '1'), 2);
                orMask = Long.parseLong(mask.replace('X', '0'), 2);

                xx = new ArrayList<>();
                for (int i = 0; i < mask.length(); i++) {
                    if (mask.charAt(i) == 'X') {
                        xx.add(mask.length() - i - 1);
                    }
                }
            }
            if (s.startsWith("mem")) {
                long[] pair = SUtils.splitLongs(s.substring(4), "[\\] \\=]+", -1);
                long addr = pair[0];
                long val = pair[1];
                mem1.put(addr, ((val & andMask) | orMask));

                // An addition hacked-in for Part 2
                for (int j = 0; j < (1 << xx.size()); j++) {
                    long[] aom = makeAndOrMask(j);
                    long addr2 = (addr & aom[0]) | aom[1] | orMask;
                    mem2.put(addr2, val);
                }
            }
        }

        return mem1.values().stream().mapToLong(i -> i).sum();
    }

    @Override
    public Object part2() {
        return mem2.values().stream().mapToLong(i -> i).sum();
    }

    private long[] makeAndOrMask(int n) {
        String bunched = Integer.toString(n, 2);
        BitSet orBits = new BitSet(NBITS);
        BitSet andBits = new BitSet(NBITS);
        andBits.set(0, NBITS);
        int nBits = xx.size();
        for (int b = 0; b < nBits; b++) {
            int i = bunched.length() - b - 1;
            boolean bit = '1' == (i >= 0 ? bunched.charAt(i) : '0');
            int index = xx.get(xx.size() - b - 1);
            if (bit) {
                orBits.set(index);
            } else {
                andBits.clear(index);
            }
        }
        return new long[] {
                toLong(andBits),
                toLong(orBits),
        };
    }

    private long toLong(BitSet bs) {
        return bs.length() == 0 ? 0L : bs.toLongArray()[0];
    }
}
