package aoc2020;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.AocMeta;
import framework.Base;
import util.FUtils;
import util.SUtils;

@AocMeta(notes = "Bit masks")
public class Day14_DockingData extends Base {
    public static void main(String[] args) {
        Base.run(Day14_DockingData::new, 1);
    }

    private static final int NBITS = 36;
    Map<Long, Long> mem1 = new HashMap<>();
    Map<Long, Long> mem2 = new HashMap<>();
    long andMask;
    long orMask;
    List<Integer> xx;

    @Override
    public void go() {
        andMask = orMask = 0;
        var in = FUtils.readLines(2020, 14);
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

                for (int j = 0; j < (1 << xx.size()); j++) {
                    long[] aom = makeAndOrMask(j);
                    long addr2 = (addr & aom[0]) | aom[1] | orMask;
                    mem2.put(addr2, val);
                }
            }
        }

        System.out.println("mem entries: " + mem2.size());
        System.out.println("Part 1: " + mem1.values().stream().mapToLong(i -> i).sum());
        System.out.println("Part 2: " + mem2.values().stream().mapToLong(i -> i).sum());
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
