package aoc2020;

import java.util.List;

import util.FUtils;
import util.Util;

public class Day18 {
    public static void main(String[] args) {
        Util.profile(() -> new Day18().go(), 1);
    }

    void go() {
        var in = FUtils.readLines(2020, 18);

        in = List.of(
            "2 * 3 + (4 * 5)", // 26 > 46
            "5 + (8 * 3 + 9 + 3 * 4 * 3)", // 437 > 1445
            "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", // 12240 > 669060
            "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2" // 13632 > 23340
        );

        long sum = 0;
        for (String expr : in) {
            char[] ch = expr.replaceAll(" ", "").toCharArray();
            sum += compute(ch, 0, ch.length);
        }
        System.out.println("SUM: " + sum);
    }

    private long compute(char[] ch, int start, int end) {
        long acc = 0;
        char op = '+';
        long val = -1;
        for (int i = start; i < end; i++) {
            switch (ch[i]) {
            case '(': {
                int skipTo = findEnd(ch, i+1);
                val = compute(ch, i+1, skipTo);
                i = skipTo;
                break;
            }
            case '*':
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
