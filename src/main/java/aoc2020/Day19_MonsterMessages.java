package aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;
import framework.AocMeta;
import framework.Base;
import util.SUtils;

@AocMeta(notes = "lexical token recognition")
@SuppressWarnings("unchecked")
public class Day19_MonsterMessages extends Base {
    public static void main(String[] args) {
        Base.run(Day19_MonsterMessages::new, 1);
    }

    @Override
    public String testInput() {
        return "42: 9 14 | 10 1\n"
                + "9: 14 27 | 1 26\n"
                + "10: 23 14 | 28 1\n"
                + "1: \"a\"\n"
                + "11: 42 31\n"
                + "5: 1 14 | 15 1\n"
                + "19: 14 1 | 14 14\n"
                + "12: 24 14 | 19 1\n"
                + "16: 15 1 | 14 14\n"
                + "31: 14 17 | 1 13\n"
                + "6: 14 14 | 1 14\n"
                + "2: 1 24 | 14 4\n"
                + "0: 8 11\n"
                + "13: 14 3 | 1 12\n"
                + "15: 1 | 14\n"
                + "17: 14 2 | 1 7\n"
                + "23: 25 1 | 22 14\n"
                + "28: 16 1\n"
                + "4: 1 1\n"
                + "20: 14 14 | 1 15\n"
                + "3: 5 14 | 16 1\n"
                + "27: 1 6 | 14 18\n"
                + "14: \"b\"\n"
                + "21: 14 1 | 1 14\n"
                + "25: 1 1 | 1 14\n"
                + "22: 14 14\n"
                + "8: 42\n"
                + "26: 14 22 | 1 20\n"
                + "18: 15 15\n"
                + "7: 14 5 | 1 21\n"
                + "24: 14 1\n"
                + "\n"
                + "abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa\n"
                + "bbabbbbaabaabba\n"
                + "babbbbaabbbbbabbbbbbaabaaabaaa\n"
                + "aaabbbbbbaaaabaababaabababbabaaabbababababaaa\n"
                + "bbbbbbbaaaabbbbaaabbabaaa\n"
                + "bbbababbbbaaaaaaaabbababaaababaabab\n"
                + "ababaaaaaabaaab\n"
                + "ababaaaaabbbaba\n"
                + "baabbaaaabbaaaababbaababb\n"
                + "abbbbabbbbaaaababbbbbbaaaababb\n"
                + "aaaaabbaabaaaaababaa\n"
                + "aaaabbaaaabbaaa\n"
                + "aaaabbaabbaaaaaaabbbabbbaaabbaabaaa\n"
                + "babaaabbbaaabaababbaabababaaab\n"
                + "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba";
    }
    @Override public Object testExpect1() { return 3L; }
    @Override public Object testExpect2() { return 12L; }
    @Override public Object expect1() { return 184L; }
    @Override public Object expect2() { return 389L; }

    Map<Integer, Object> rules = new TreeMap<>();
    List<char[]> messages = new ArrayList<>();

    @Override
    public void parse(String text) {
        var in = SUtils.lines(text);
        for (String s : in) {
            if (!s.isEmpty()) {
                var split = s.split(": ");
                if (split.length == 2) {
                    int i = Ints.tryParse(split[0]);
                    Object o = (split[1].startsWith("\"")) ? split[1].charAt(1) : getInts(split[1]);
                    rules.put(i, o);
                } else {
                    messages.add(s.toCharArray());
                }
            }
        }
    }

    @Override
    public Long part1() {
        return countMatches();
    }

    @Override
    public Long part2() {
        rules.put(8, getInts("42 | 42 8"));
        rules.put(11, getInts("42 31 | 42 11 31"));
        return countMatches();
    }

    private long countMatches() {
        long found = 0;
        for (char[] m : messages) {
            var remaining = ((List<int[]>) rules.get(0)).get(0); // big assumption here
            if (match(m, 0, remaining)) {
                found++;
            }
        }
        return found;
    }

    boolean match(char[] m, int im, int[] remaining) {
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
        for (int[] sequence : (List<int[]>) rule) {
            if (match(m, im, Ints.concat(sequence, remaining))) {
                return true;
            }
        }
        return false;
    }

    List<int[]> getInts(String text) {
        return Splitter.on(" | ").splitToList(text).stream()
            .map(s -> Arrays.stream(s.split(" ")).mapToInt(Ints::tryParse).toArray())
            .collect(Collectors.toList());
    }
}
