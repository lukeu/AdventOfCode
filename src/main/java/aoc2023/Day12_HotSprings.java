package aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.primitives.Ints;
import framework.Base;
import framework.Input;
import util.SUtils;

public class Day12_HotSprings extends Base {

    public static void main(String[] args) {
        Base.run(Day12_HotSprings::new, 1);
    }

    @Override
    public String testInput() {
        return "??##???#? 4,1\n" + // An extra test-case to cover the recursion termination clause
"""
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
""";
    }
    @Override public Object testExpect1() { return 24; }     // 21 without extra test
    @Override public Object testExpect2() { return 525395; } // 525152 without extra test
    @Override public Object expect1() { return 7716; }
    @Override public Object expect2() { return 18716325559999L; }

    record Line(String springs, int[] blocks) {}
    List<Line> lines = new ArrayList<>();

    @Override
    public void parse(Input in) {
        for (String s : in.lines()) {
            var split = SUtils.split(s, " ");
            var line = new Line(
                    split.get(0),
                    SUtils.extractInts(split.get(1)));
            lines.add(line);
        }
    }

    @Override
    public Long part1() {
        int maxSprings = 20;
        int maxBlocks = 20;
        var cache = ThreadLocal.withInitial(() -> new long[maxSprings * maxBlocks]);
        return lines.parallelStream()
                .mapToLong(l -> findArrangements(l, cache.get(), maxSprings))
                .sum();
    }

    @Override
    public Long part2() {
        int maxSprings = 110;
        int maxBlocks = 32;
        var cache = ThreadLocal.withInitial(() -> new long[maxSprings * maxBlocks]);
        return lines.parallelStream()
                .map(this::unfold)
                .mapToLong(l -> findArrangements(l, cache.get(), maxSprings))
                .sum();
    }

    Line unfold(Line line) {
        var b = line.blocks;
        return new Line(
                (line.springs + "?").repeat(4) + line.springs,
                Ints.concat(b, b, b, b, b));
    }

    long findArrangements(Line line, long[] cache, int maxSprings) {
        Arrays.fill(cache, -1);
        char[] cs = line.springs().toCharArray();
        return findArrangements(cs, line.blocks, 0, 0, cache, maxSprings);
    }

    long findArrangements(char[] cs, int[] blocks, int b, int pos, long[] cache, int maxSprings) {
        int cacheIndex = b * maxSprings + pos;
        long count = cache[cacheIndex];
        if (cache[cacheIndex] != -1) {
            return count;
        }
        if (b == blocks.length) {
            return noneMissed(cs, pos, cs.length) ? 1 : 0;
        }
        int bLen = blocks[b];
        count = 0;
        for (int i = pos; i < cs.length; ++i) {
            if (noneMissed(cs, pos, i) && canFit(cs, i, bLen)) {
                count += findArrangements(cs, blocks, b+1, i + bLen + 1, cache, maxSprings);
            }
        }
        cache[cacheIndex] = count;
        return count;
    }

    boolean noneMissed(char[] cs, int start, int current) {
        for (int i = start; i < current; ++i) {
            if (cs[i] == '#') {
                return false;
            }
        }
        return true;
    }

    boolean canFit(char[] cs, int pos, int len) {
        var end = pos + len;
        if (end > cs.length) {
            return false;
        }
        for (; pos < end; ++pos) {
            if (cs[pos] == '.') {
                return false;
            }
        }
        return pos == cs.length || cs[pos] != '#';
    }
}
