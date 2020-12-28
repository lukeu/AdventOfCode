package aoc2020;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Sets;
import util.FUtils;

public class Day06_CustomCustoms {
    public static void main(String[] args) {
        new Day06_CustomCustoms().best();
    }

    /**
     * This would have been the way to go - a simple array of counts
     */
    void best() {
        long found = 0;
        var in = FUtils.splitLines(2020, 6, "\n\n");
        for (String group : in) {
            var lines = group.split("\n");
            var counts = new int[26];
            group.chars().forEach(c -> {if (c != '\n') counts[c-'a']++;});
            for (int i : counts) {
                if (i == lines.length) { // for Part1: (i != 0)
                    found++;
                }
            }
        }
        System.out.println("Found: " + found);
    }

    /**
     * Code used - which evolved from using a HashSet in part 1 to a Multiset
     * I'm happy enough with the resulting code, but it took me too long...
     *
     * Time went on trying to remember things, like the specific class to construct & how to use it
     * properly (iterate EntrySet to get the counts). I was getting muddled-up with Multimap's API.
     */
    void original() {
        long found = 0;
        var in = FUtils.splitLines(2020, 6, "\n\n");
        for (String group : in) {
            var lines = group.split("\n");

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

    /**
     * Alternative - I could have extended from EXACTLY my solution for Part-1 (without changing)
     * with some quick duplication here. This was one idea, but couldn't quickly see how to apply
     * Sets.intersection, so I went in the Multiset direction.
     *
     * (It looks long to type, but the code-templates I made for AoC would write this quickly.)
     */
    void alternative() {
        long found = 0;
        var in = FUtils.splitLines(2020, 6, "\n\n");

        for (String group : in) {
            Set<Character> set = new HashSet<>();
            for (int i = 0; i < group.length(); i++) {
                char ch = group.charAt(i);
                if (ch != '\n') {
                    set.add(ch);
                }
            }

            // Part 2 (Delete this block for Part 1)
            for (String line : group.split("\n")) {
                var lineSet = new HashSet<Character>();
                for (int i = 0; i < line.length(); i++) {
                    char ch = line.charAt(i);
                    lineSet.add(ch);
                }
                set = Sets.intersection(set, lineSet);
            }

            found += set.size();
        }
        System.out.println("Found: " + found);
    }
}
