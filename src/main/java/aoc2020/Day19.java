package aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;
import util.FUtils;
import util.Util;

public class Day19 {
    public static void main(String[] args) {
        Util.profile(() -> new Day19().go(), 1);
    }

    List<Object> rules;
    List<char[]> messages = new ArrayList<>();

    void go() {
        long found = 0;
        var in = FUtils.readLines(2020, 19);
        var tree = new TreeMap<Integer, Object>();
        boolean first = true;
        for (String s : in) {
            if (s.isEmpty()) {
                first = false;
                continue;
            }
            if (first) {
                var split = s.split(": ");
                int i = Ints.tryParse(split[0]);
                Object o = (split[1].startsWith("\"")) ?
                        split[1].charAt(1) : getInts(split[1]);
                tree.put(i, o);
            } else {
                messages.add(s.toCharArray());
            }
        }
        rules = new ArrayList<>(tree.values());

        System.out.println("Input lines: " + rules.size());
        System.out.println("Input lines: " + messages.size());

        for (char[] m : messages) {
            List<int[]> rule = (List<int[]>) rules.get(0);
            if (match(m, rule, 0) == m.length) {
                found++;
            }
        }
        System.out.println("Found: " + found);
    }

    private int match(char[] m, Object rule, int oc) {
        if (rule instanceof Character) {
            if (m[oc] == (char) rule) {
                return oc+1;
            }
            return -1;
        }
        var ii = (List<int[]>) rule;
        for (int[] is : ii) {
            int c = oc;
            boolean good = true;
            for (int j : is) {
                int next = match(m, rules.get(j), c);
                if (next < 0) {
                    good = false;
                    break;
                }
                c = next;
            }
            if (good) {
                return c;
            }
        }
        return -1;
    }

    private List<int[]> getInts(String s) {
        List<int[]> result = new ArrayList<>();

        for (String ss : Splitter.on("|").trimResults().splitToList(s)) {
            result.add(Arrays.stream(ss.split(" ")).mapToInt(Ints::tryParse).toArray());
        }
        return result;
    }
}
