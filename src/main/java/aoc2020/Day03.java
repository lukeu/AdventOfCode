package aoc2020;

import util.FUtils;
import util.Util;

public class Day03 {
    public static void main(String[] args) {
        Util.profile(() -> new Day03().go(), 1);
    }

    void go() {

        long total = 1;
        var in = FUtils.readLines(2020, 3);
        for (int i : new int []{1,3,5,7}) {
            int x = 0;
            int found = 0;
            for (String s : in) {
                int w = s.length();
                if (s.charAt(x % w) == '#') {
                    found++;
                }
                x += i;
            }
            System.out.println(found);
            total *= found;
        }

        int x = 0;
        int found = 0;
        for (int i = 0; i < in.size(); i += 2) {
            String s = in.get(i);
            int w = s.length();
            if (s.charAt(x % w) == '#') {
                found++;
            }
            x += 1;
        }
        System.out.println(found);
        System.out.println(total * found);
    }
}
