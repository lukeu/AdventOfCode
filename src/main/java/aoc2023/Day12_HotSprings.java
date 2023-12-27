package aoc2023;

import java.util.ArrayList;
import java.util.List;

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
    @Override public Object testExpect1() { return 24; }
    @Override public Object testExpect2() { return 0L; }
    @Override public Object expect1() { return 7716; }

    record Line(char[] springs, int[] blocks) {}
    List<Line> lines = new ArrayList<>();

    @Override
    public void parse(Input in) {
        for (String s : in.lines()) {
            var split = SUtils.split(s, " ");
            var line = new Line(
                    split.get(0).toCharArray(),
                    SUtils.extractInts(split.get(1)));
            lines.add(line);
        }
    }

    @Override
    public Long part1() {
        long found = 0;
        for (var line : lines) {
            int arrangements = recurse(line, 0, 0);
            found += arrangements;
        }
        return found;
    }

    int recurse(Line line, int b, int pos) {
        char[] cs = line.springs();
        if (b == line.blocks().length) {
            return noneMissed(cs, pos, cs.length) ? 1 : 0;
        }
        int bLen = line.blocks()[b];
        int count = 0;
        for (int i = pos; i < cs.length; ++i) {
            if (noneMissed(cs, pos, i) && canFit(cs, i, bLen)) {
                count += recurse(line, b+1, i + bLen + 1);
            }
        }
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

    @Override
    public Long part2() {
        long found = 0;
        
        return found;
    }
}
