package aoc2020;

import java.util.regex.Pattern;

import util.FUtils;
import util.Util;

public class Day02 {
    public static void main(String[] args) {
        Util.profile(() -> new Day02().go(), 1);
    }

    // Example input
    // 7-8 l: lllllzllbfl

    // Alternatives...
    //
    // * Too fragile & slow to code
    //     var p = Pattern.compile("(\\d+)\\-(\\d+) (.)\\: (.*)");
    //
    // * Best for speed-coding: (poor perf)
    //
    //    var scan = new Scanner(s);
    //    scan.useDelimiter("[-: ]+");
    //    int min = scan.nextInt();

    void go() {
        int part1 = 0;
        int part2 = 0;

        var p = Pattern.compile("[-: ]+");
        var in = FUtils.readLines(2020, 2);
        for (String s : in) {

            String[] sp = p.split(s);
            int min = Integer.parseInt(sp[0]);
            int max = Integer.parseInt(sp[1]);
            char ch = sp[2].charAt(0);
            String str = sp[3];

            int count = (int) str.chars().filter(c -> c == ch).count();
            if (count >= min && count <= max) {
                part1 ++;
            }
            if (str.charAt(min-1) == ch ^ str.charAt(max-1) == ch) {
                part2 ++;
            }
        }
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }
}
