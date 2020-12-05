package aoc2020;

import util.FUtils;

public class Day05 {
    public static void main(String[] args) {
        new Day05().faster();
    }

    // New and improved, learning for next time...
    void faster() {
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

    // As written under pressure...
    void go() {
        int max = 0;
        int min = Integer.MAX_VALUE;
        boolean[] found = new boolean[1024];
        var in = FUtils.readLines(2020, 5);

        for (var a : in) {
            int row = 0;
            int two = 64;
            for (int i = 0; i < 7; i++) {
                char ch = a.charAt(i);
                if (ch == 'B') {
                    row += two;
                }
                two /= 2;
            }

            int seat = 0;
            two = 4;
            for (int i = 7; i < 10; i++) {
                char ch = a.charAt(i);
                if (ch == 'R') {
                    seat += two;
                }
                two /= 2;
            }
            int id = row * 8 + seat;
            System.out.println(id + "\t" + row + ":" + seat);
            found[id] = true;
            if (row > max) {
                max = row;
            }
            if (row < min) {
                min = row;
            }
        }
        System.out.println("min/max " + min + ":" + max);
        for (int r = min; r < max; r++) {
            for (int c = 0; c < 8; c++) {
                int id = r*8+c;
                if (!found[id]) {
                    System.out.println("id: " + id);
                }
            }
        }
    }
}
