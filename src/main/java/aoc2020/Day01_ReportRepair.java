package aoc2020;

import util.FUtils;
import util.Util;

public class Day01_ReportRepair {
    public static void main(String[] args) {
        Util.profile(() -> new Day01_ReportRepair().go(), 1);
    }

    void go() {
        int[] in = FUtils.readLineInts(2020, 1);
        for (int a : in) {
            for (int b : in) {
                for (int c : in) {
                    if (a + b + c == 2020) {
                        System.out.println(a*b*c);
                        return;
                    }
                }
            }
        }
    }
}
