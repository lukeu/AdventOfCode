package aoc19;

import java.util.Arrays;

import util.FUtils;

public class Old1 {
    public static void main(String[] args) {
        new Old1().go();
    }

    void go() {
        int[] in = FUtils.readLineInts(2019, 1);

        System.out.println(Arrays.stream(in).map(i -> i / 3 - 2).sum());

        int total = 0;
        for (int i : in) {
            int f = 0;
            int r = i;
            while (true) {
                int more = r / 3 - 2;
                if (more > 0) {
                    f += more;
                    r = more;
                } else {
                    break;
                }
            }
            total += f;
        }
        System.out.println(total);
    }
}
