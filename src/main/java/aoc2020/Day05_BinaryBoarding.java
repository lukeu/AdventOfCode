package aoc2020;

import framework.Base;
import framework.Input;
import util.ByteBiter;

public class Day05_BinaryBoarding extends Base {
    public static void main(String[] args) {
        Base.run(Day05_BinaryBoarding::new, 1);
    }

    @Override public Object expect1() { return 980; }
    @Override public Object expect2() { return 607; }

    ByteBiter bb;
    boolean[] found = new boolean[1024];

    @Override
    public void parse(Input input) {
        bb = new ByteBiter(input.bytes(this));
    }

    // New and improved, learning for next time. (See git history for the 'under pressure' version)
    @Override
    public Object part1() {
        int max = 0;

        while (bb.hasRemaining()) {
            long row = bb.readAsBinary('F', 'B', 7);
            long seat = bb.readAsBinary('L', 'R', 3);
            int id = (int) (row * 8 + seat); // or just parse 'a' itself

            found[id] = true;
            if (id > max) {
                max = id;
            }
            bb.skip();
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
