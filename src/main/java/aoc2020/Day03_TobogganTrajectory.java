package aoc2020;

import java.util.List;

import framework.Base;
import framework.Input;

public class Day03_TobogganTrajectory extends Base {
    public static void main(String[] args) {
        Base.run(Day03_TobogganTrajectory::new, 1);
    }

    @Override public Object expect1() { return 240L; }
    @Override public Object expect2() { return 2832009600L; }

    List<String> in;
    int w;

    @Override
    public void parse(Input input) {
        in = input.lines();
        w = in.get(0).length();
    }

    @Override
    public Object part1() {
        return slopeHits(1, 3);
    }

    @Override
    public Object part2() {
        long total = 1;
        for (int dx : new int []{1,3,5,7}) {
            total *= slopeHits(1, dx);
        }
        return total * slopeHits(2, 1);
    }

    private long slopeHits(int dy, int dx) {
        int x = 0;
        int found = 0;
        for (int r = 0; r < in.size(); r += dy) {
            String s = in.get(r);
            if (s.charAt(x % w) == '#') {
                found++;
            }
            x += dx;
        }
        return found;
    }
}
