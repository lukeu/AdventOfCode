package aoc2020;

import framework.Base;
import util.SUtils;

public class Day01_ReportRepair extends Base {

    public static void main(String[] args) {
        Base.run(Day01_ReportRepair::new, 1);
    }

    @Override public Object expect2() { return 193598720; }

    int[] in;

    @Override
    public void parse(String text) {
        in = SUtils.lineInts(text);
    }

    @Override
    public Object part2() {
        for (int a : in) {
            for (int b : in) {
                for (int c : in) {
                    if (a + b + c == 2020) {
                        return a*b*c;
                    }
                }
            }
        }
        return null;
    }
}