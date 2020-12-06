package aoc2020;

import java.util.Scanner;

import util.FUtils;
import util.Util;

public class Day02 {
    public static void main(String[] args) {
        Util.profile(() -> new Day02().go(), 1);
    }

    // Example:
    // 7-8 l: lllllzllbfl
    // Too fragile & slow to code to - use Scanner next time.
    // var p = Pattern.compile("(\\d+)\\-(\\d+) (.)\\: (.*)");

    void go() {
        int found = 0;

        var in = FUtils.readLines(2020, 2);
        for (String s : in) {
            var scan = new Scanner(s);
            scan.useDelimiter("[-: ]+");

            int min = scan.nextInt();
            int max = scan.nextInt();
            char ch = scan.next().charAt(0);
            String str = scan.next();

            // TRUE = part 1
            if (Boolean.FALSE) {
                int count = (int) str.chars().filter(c -> c == ch).count();
                if (count >= min && count <= max) {
                    found ++;
                }
            } else {
                if (str.charAt(min-1) == ch ^ str.charAt(max-1) == ch) {
                    found ++;
                }
            }

            scan.close();
        }
        System.out.println(found);
    }
}
