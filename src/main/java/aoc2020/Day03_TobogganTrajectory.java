package aoc2020;

import java.util.List;

import framework.Base;
import util.SUtils;

public class Day03_TobogganTrajectory extends Base {
    public static void main(String[] args) {
        Base.run(Day03_TobogganTrajectory::new, 1);
    }

    @Override public Object expect2() { return 2832009600L; }

    List<String> in;

    @Override
    public void parse(String text) {
        in = SUtils.lines(text);
    }

    @Override
    public Object part2() {
        long total = 1;
        int w = in.get(0).length();
        for (int i : new int []{1,3,5,7}) {
            int x = 0;
            int found = 0;
            for (String s : in) {
                if (s.charAt(x % w) == '#') {
                    found++;
                }
                x += i;
            }
            total *= found;
        }

        int x = 0;
        int found = 0;
        for (int i = 0; i < in.size(); i += 2) {
            String s = in.get(i);
            if (s.charAt(x % w) == '#') {
                found++;
            }
            x += 1;
        }
        return total * found;
    }
}
