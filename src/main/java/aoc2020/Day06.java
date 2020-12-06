package aoc2020;

import java.util.Arrays;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset.Entry;
import util.FUtils;

public class Day06 {
    public static void main(String[] args) {
        new Day06().go();
    }

    void go() {
        long found = 0;
        var in = FUtils.splitLines(2020, 6, "\n\n");
        for (String group : in) {
            var lines = group.split("\n");
            System.out.println(Arrays.toString(lines));

            var set = HashMultiset.<Character>create();
            for (String line : lines) {
                for (int i = 0; i < line.length(); i++) {
                    set.add(line.charAt(i));
                }
            }
            for (Entry<Character> e : set.entrySet()) {
                if (e.getCount() == lines.length) {
                    found ++;
                }
            }
        }
        System.out.println("Found: " + found);
    }
}
