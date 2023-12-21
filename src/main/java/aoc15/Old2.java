package aoc15;

import java.util.List;

import framework.Input;

public class Old2 {
    public static void main(String[] args) {
        new Old2().go();
    }

    // Version written with my daughter when she asked about programming
    void go() {
        List<String> lines = new Input(2015, 2).lines();
        int sum = 0;
        for (String dimension : lines) {
            System.out.println("Read: " + dimension);
            String[] dims = dimension.split("x");
            int l = Integer.parseInt(dims[0]);
            int w = Integer.parseInt(dims[1]);
            int h = Integer.parseInt(dims[2]);
            var side1 = l*w;
            var side2 = w*h;
            var side3 = h*l;
            int area = (side1 + side2 + side3) * 2;
            int smallest = Math.min(Math.min(side1, side2), side3);
            System.out.println(area + " > " + smallest);
            sum +=area+smallest  ;
        }
        System.out.println("The grand glorious summation of all box paper, in sqaure feet before his majesty Luke Dukem is... " + sum );
    }
}
