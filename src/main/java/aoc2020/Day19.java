package aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;
import util.FUtils;
import util.Util;

public class Day19 {
    public static void main(String[] args) {
        Util.profile(() -> new Day19().go(), 1);
    }

    Map<Integer, Object> rules = new TreeMap<>();
    List<char[]> messages = new ArrayList<>();

    void go() {
        long found = 0;
        var in = FUtils.readLines(2020, 19);
        boolean first = true;
        for (String s : in) {
            if (s.isEmpty()) {
                first = false;
                continue;
            }
            if (first) {
                var split = s.split(": ");
                int i = Ints.tryParse(split[0]);
                if (i == 8) {
                    split[1] = "42 | 42 8";
                }
                if (i == 11) {
                    split[1] = "42 31 | 42 11 31";
                }
                Object o = (split[1].startsWith("\"")) ?
                        split[1].charAt(1) : getInts(split[1]);
                rules.put(i, o);
            } else {
                messages.add(s.toCharArray());
            }
        }

        System.out.println("Rules: " + rules.size());
        System.out.println("Messages: " + messages.size());

//        String notMatch =    "aaaabbaaaabbaaa";
//        String shouldMatch = "babbbbaabbbbbabbbbbbaabaaabaaa";
//        messages = List.of(shouldMatch.toCharArray());

        for (char[] m : messages) {
            var remaining = ((List<int[]>) rules.get(0)).get(0); // big assumption here
            if (match(m, 0, remaining)) {
                found++;
            }
        }
        System.out.println("Found: " + found);
    }

    private boolean match(char[] m, int im, int[] remaining) {
        if (remaining.length == 0) {
            return im == m.length;
        }
        int head = remaining[0];
        remaining = Arrays.copyOfRange(remaining, 1, remaining.length);
        Object rule = rules.get(head);
        if (rule instanceof Character) {
            if (im >= m.length || m[im] != (char) rule) {
                return false;
            }
            return match(m, im + 1, remaining);
        }
        List<int[]> sequences = (List<int[]>) rule;
        for (int[] sequence : sequences) {
            if (match(m, im, Ints.concat(sequence, remaining))) {
                return true;
            }
        }
        return false;
    }

    private List<int[]> getInts(String s) {
        List<int[]> result = new ArrayList<>();
        for (String ss : Splitter.on("|").trimResults().splitToList(s)) {
            result.add(Arrays.stream(ss.split(" ")).mapToInt(Ints::tryParse).toArray());
        }
        return result;
    }
}
