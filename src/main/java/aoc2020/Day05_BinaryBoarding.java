package aoc2020;

import java.util.List;

import framework.Base;
import util.SUtils;

public class Day05_BinaryBoarding extends Base {
    public static void main(String[] args) {
        Base.run(Day05_BinaryBoarding::new, 1);
    }

    @Override public Object expect1() { return 980; }
    @Override public Object expect2() { return 607; }

    List<String> in;
    boolean[] found = new boolean[1024];

    @Override
    public void parse(String text) {
        in = SUtils.lines(text);
    }

    // New and improved, learning for next time. (See git history for the 'under pressure' version)
    @Override
    public Object part1() {
        int max = 0;

        for (var a : in) {
            a = a.replace('F', '0').replace('B', '1');
            a = a.replace('L', '0').replace('R', '1');
            int row = Integer.parseInt(a.substring(0, 7), 2);
            int seat = Integer.parseInt(a.substring(7, 10), 2);
            int id = row * 8 + seat; // or just parse 'a' itself

            found[id] = true;
            if (id > max) {
                max = id;
            }
        }
        return max;
    }

    @Override
    public Object part2() {
        for (int i = 9; i < found.length - 2; i++) {
            if (found[i-1] && !found[i] && found[i+1]) {
                return i;
            }
        }
        return null;
    }
}
