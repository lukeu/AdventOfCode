package aoc2023;

import framework.Base;
import framework.Input;

public class Day_Template extends Base {
    public static void main(String[] args) {
        Base.run(Day_Template::new, 1);
    }

    @Override
    public String testInput() {
        return
"""
""";
    }
    @Override public Object testExpect1() { return 0L; }
    @Override public Object testExpect2() { return 0L; }

    /** Times parses separately for optimisation runs. Can ignore when speed-coding.  */
    @Override
    public void parse(Input in) {
        for (var line : in.lines()) {
            
        }
    }

    @Override
    public Long part1() {
        long found = 0;
        var in = input().lines();
        
        return found;
    }

    @Override
    public Long part2() {
        long found = 0;
        
        return found;
    }
}
