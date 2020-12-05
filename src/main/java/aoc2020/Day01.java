package aoc2020;

import util.FUtils;

public class Day01 {
    public static void main(String[] args) {
        new Day01().go();
    }

    void go() {
        int[] in = FUtils.readLineInts(2020, 1);
        for (int a : in) {
            for (int b : in) {
                for (int c : in) {
                    if (a + b + c == 2020) {
                        System.out.println(a*b*c);
                    }
                }
            }
        }
    }
}
