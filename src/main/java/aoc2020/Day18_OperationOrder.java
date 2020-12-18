package aoc2020;

import java.util.List;

import framework.AocMeta;
import framework.Base;
import util.SUtils;

@AocMeta(notes = "expression evaluation")
public class Day18_OperationOrder extends Base {
    public static void main(String[] args) {
        Base.run(Day18_OperationOrder::new, 1);
    }

    @Override
    public String testInput() {
        return "1 + 2 * 3 + 4 * 5 + 6\n"
                + "1 + (2 * 3) + (4 * (5 + 6))\n"
                + "2 * 3 + (4 * 5)\n"
                + "5 + (8 * 3 + 9 + 3 * 4 * 3)\n"
                + "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))\n"
                + "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 ";
    }
    @Override public Object testExpect1() { return 71L + 51 + 26 + 437 + 12240 + 13632; }
    @Override public Object testExpect2() { return 231L + 51 + 46 + 1445 + 669060 + 23340; }
    @Override public Object expect1() { return 6923486965641L; }
    @Override public Object expect2() { return 70722650566361L; }

    List<String> in;
    boolean isPart2 = false;

    @Override
    public void parse(String text) {
        in = SUtils.lines(text);
    }

    @Override
    public Long part1() {
        isPart2 = false;
        return sumEach();
    }

    @Override
    public Long part2() {
        isPart2 = true;
        return sumEach();
    }

    private Long sumEach() {
        long sum = 0;
        for (String expr : in) {
            char[] ch = expr.replaceAll(" ", "").toCharArray();
            sum += compute(ch, 0, ch.length);
        }
        return sum;
    }

    private long compute(char[] ch, int start, int end) {
        long acc = 0;
        long val = -1;
        char op = '+';
        for (int i = start; i < end; i++) {
            switch (ch[i]) {
            case '(': {
                int skipTo = findEnd(ch, i+1);
                val = compute(ch, i+1, skipTo);
                i = skipTo;
                break;
            }
            case '*':
                if (isPart2)
                    return acc * compute(ch, i+1, end);
            case '+':
                op = ch[i];
                continue;
            default:
                val = ch[i] - '0';
            }
            switch (op) {
                case '+' -> acc += val;
                case '*' -> acc *= val;
                default -> acc = val;
            }
        }
        return acc;
    }

    private int findEnd(char[] ch, int i) {
        int nest = 1;
        for (; nest > 0; ++i) {
            switch (ch[i]) {
                case '(' -> nest ++;
                case ')' -> nest --;
                default -> {}
            }
        }
        return i - 1;
    }
}
