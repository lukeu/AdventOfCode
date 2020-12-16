package aoc2020;

import framework.Base;
import util.FUtils;
import util.SUtils;

public class Day_Template extends Base {
    public static void main(String[] args) {
        Base.run(Day_Template::new, 1);
    }

    @Override
    public String testInput() {
        return FUtils.readIfExists("" + year() + "/Test_" + day() + ".txt");
    }
    @Override public Object testExpect1() { return 0L; }
    @Override public Object testExpect2() { return 0L; }

    @Override
    public void parse(String in) {
        for (String line : SUtils.lines(in)) {

            

        }
    }

    @Override
    public Long part1() {
        long found = 0;

        

        return found;
    }

    @Override
    public Long part2() {
        long found = 0;

        

        return found;
    }
}
