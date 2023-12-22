package aoc15;

import java.util.Arrays;
import java.util.List;

import framework.Input;

public class Old2 {
    public static void main(String[] args) {
        new Old2().go();
    }

    // Version written with my daughter when she asked about programming
    void go() {
        List<String> lines = new Input(2015, 2).lines();
        int part1 = 0;
        int part2 = 0;
        for (String dimension : lines) {
            System.out.println("Read: " + dimension);
            String[] dims = dimension.split("x");
            int l = Integer.parseInt(dims[0]);
            int w = Integer.parseInt(dims[1]);
            int h = Integer.parseInt(dims[2]);
            part1 += paperForBox(l, w, h)  ;

            int[] perim = new int[] {
                (w+w+h+h),
                (l+l+h+h),
                (w+w+l+l)
            };
            Arrays.sort(perim);
            int vol = (w*h*l);
            part2 += perim[0];
            part2 += vol;
        }
        System.out.println("Part1: " + part1);
        System.out.println("Part2: " + part2);
        
    }

    private int paperForBox(int l, int w, int h) {
        var side1 = l*w;
        var side2 = w*h;
        var side3 = h*l;
        int area = (side1 + side2 + side3) * 2;
        int smallest = Math.min(Math.min(side1, side2), side3);
        System.out.println(area + " > " + smallest);
        var paper = area+smallest;
        return paper;
    }
}
