package aoc19;

import java.util.Arrays;

import util.FUtils;

public class Old2 {
    public static void main(String[] args) {
        new Old2().go();
    }

    private final int[] in;

    Old2() {
        in = FUtils.readCommaInts(2019, 2);
    }

    void go() {
        System.out.println(Arrays.toString(in));
    }
}
