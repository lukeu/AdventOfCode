package aoc2020;

import util.FUtils;
import util.Util;

public class Day05_BinaryBoarding {
    public static void main(String[] args) {
        Util.profile(() -> new Day05_BinaryBoarding().go(), 1);
    }

    // New and improved, learning for next time. (See git history for the 'under pressure' version)
    void go() {
        int max = 0;
        boolean[] found = new boolean[1024];
        var in = FUtils.readLines(2020, 5);

        for (var a : in) {
            a = a.replace('F', '0').replace('B', '1');
            a = a.replace('L', '0').replace('R', '1');
            int row = Integer.parseInt(a.substring(0, 7), 2);
            int seat = Integer.parseInt(a.substring(7, 10), 2);
            int id = row * 8 + seat; // or just parse 'a' itself

            //System.out.println(id + "\t" + row + ":" + seat);
            found[id] = true;
            if (row > max) {
                max = row;
            }
        }
        System.out.println("Part 1: " + max);

        // For part 2 just scan the output visually - skip the last bit of code
        for (int i = 8; i < found.length - 1; i++) {
            if (!found[i]) {
                System.out.println(i);
            }
        }
    }
}
