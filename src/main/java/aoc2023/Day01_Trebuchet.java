package aoc2023;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableMap;
import framework.Base;
import framework.Input;

public class Day01_Trebuchet extends Base {

    public static void main(String[] args) {
        Base.run(Day01_Trebuchet::new, 1);
    }

    @Override
    public String testInput() {
        return
// part 2 test input
"""
two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen
""";
    }

    // Part 1 input expected 142. This is for part 2 input
    @Override public Object testExpect1() { return 209; }
    @Override public Object testExpect2() { return 281; }
    @Override public Object expect1() { return 57346; }
    @Override public Object expect2() { return 57345; }

    private static final ImmutableMap<String, Integer> WORD_TO_DIGITS = ImmutableMap.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9
    );

    private List<String> lines;

    @Override
    public void parse(Input in) {
        lines = in.lines();
    }

    @Override
    public Long part1() {
        return sumNumbersInAllLines(ImmutableMap.of());
    }

    @Override
    public Long part2() {
        return sumNumbersInAllLines(WORD_TO_DIGITS);
    }

    long sumNumbersInAllLines(Map<String, Integer> words) {
        long sum = 0;
        for (String line : lines) {
            int[] digits = findAllNumbers(line, words);
            if (digits.length > 0) {
                int first = digits[0];
                int last = digits[digits.length - 1];
                sum += first * 10 + last;
            }
        }
        return sum;
    }

    static int[] findAllNumbers(String str, Map<String, Integer> words) {
        return IntStream.range(0, str.length())
                .map(pos -> tryDigitAt(str, pos, words))
                .filter(d -> d != -1)
                .toArray();
    }

    /**
     * Looks into 'str' starting at position 'pos' and:
     *  - if there is a digit, this is returned as a int
     *  - if it starts with one of 'words', the matching int is returned
     * otherwise returns -1.
     */
    static int tryDigitAt(String str, int pos, Map<String, Integer> words) {

        char ch = str.charAt(pos);
        if (ch >= '0' && ch <= '9') {
            return ch - '0'; // Convert to int
        }

        for (var entry : words.entrySet()) {
            if (str.startsWith(entry.getKey(), pos)) {
                return entry.getValue();
            }
        }
        return -1;
    }
}
