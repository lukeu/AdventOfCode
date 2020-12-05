package aoc15;

import java.util.List;

import util.FUtils;

public class Old2 {
    public static void main(String[] args) {
        new Old2().go();
    }

    // Version written with my daughter when she asked about programming
    void go() {
        List<String> lines = FUtils.readLines(2015, 2);
        int total = 0;
        for (String dimension : lines) {
            System.out.println("Read: " + dimension);
            String[] dims = dimension.split("x");
            int l = Integer.parseInt(dims[0]);
            int w = Integer.parseInt(dims[1]);
            int h = Integer.parseInt(dims[2]);
            int s1 = w*l;
            int s2 = l*h;
            int s3 = w*h;
            int surface = 2*s2 + 2*s1 + 2*s3;
            int small = Math.min (s1, s2);
            int smallest = Math.min(small, s3);
            int paper = surface + smallest;
            total = total + paper;
        }
        System.out.println(total);
    }
}
