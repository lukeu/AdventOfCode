package aoc2020;

import java.util.ArrayList;
import util.FUtils;

public class Day08_HandheldHalting {
    public static void main(String[] args) {
        long acc = new Day08_HandheldHalting().go();
        System.out.println("Halted: acc = " + acc);
    }

    record Ins (String cmd, int arg) {}

    long go() {
        var in = FUtils.readLines(2020, 8);

        // Pre-split for performance...
        var instructions = new ArrayList<Ins>(in.size());
        for (String s : in) {
            var split = s.split(" ");
            instructions.add(new Ins(split[0].intern(), Integer.parseInt(split[1])));
        }

        // ...after which even brute force isn't too bad. (Whole method is sub 1 ms, warm)
        for (int swap = -1; swap < in.size(); swap++) {
            boolean[] exe = new boolean[in.size()];
            long acc = 0;
            int pc = 0;
            while (!exe[pc]) {
                exe[pc] = true;
                var i = instructions.get(pc);
                if (swap != pc) {
                    switch (i.cmd) {
                    case "acc" -> acc += i.arg;
                    case "jmp" -> pc += (i.arg-1);
                    }
                } else {
                    switch (i.cmd) {
                    case "acc" -> acc += i.arg;
                    case "nop" -> pc += (i.arg-1);
                    }
                }
                if (++pc == exe.length) {
                    return acc;
                }
            }
            if (swap == -1) {
                System.out.println("Part 1: acc = " + acc);
            }
        }
        return -1;
    }
}
