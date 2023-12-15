package aoc19;

import java.util.Arrays;

import framework.Input;

public class Old1 {
    public static void main(String[] args) {
        new Old1().go();
    }

    void go() {
        int[] in = new Input(2019, 1).ints();

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
