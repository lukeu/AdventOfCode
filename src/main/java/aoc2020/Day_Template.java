package aoc2020;

import util.FUtils;

public class Day_Template {
    public static void main(String[] args) {
        new Day_Template().go();
    }

    void go() {
        long found = 0;
        var in = FUtils.readLines(2020, 99999);

        

        System.out.println("Input lines: " + in.size());

        System.out.println("Found: " + found);
    }
}
