package aoc2020;

import util.FUtils;

public class Day08_HandheldHalting {
    public static void main(String[] args) {
        new Day08_HandheldHalting().go();
    }

    void go() {
        var in = FUtils.readLines(2020, 8);

        for (int swap = -1; swap < in.size(); swap++) {
            boolean[] exe = new boolean[in.size()];
            long acc = 0;
            int pc = 0;
            while (!exe[pc]) {
                exe[pc] = true;
                var split = in.get(pc).split(" ");
                String cmd = split[0];
                int arg = Integer.parseInt(split[1]);

                if (swap != pc) {
                    switch (cmd) {
                    case "acc" -> acc += arg;
                    case "jmp" -> pc += (arg-1);
                    }
                } else {
                    switch (cmd) {
                    case "acc" -> acc += arg;
                    case "nop" -> pc += (arg-1);
                    }
                }
                pc++;
                if (pc == exe.length) {
                    System.out.println("Halted: acc = " + acc);
                    System.exit(0);
                }
            }
            if (swap == -1) {
                System.out.println("Part 1: acc = " + acc);
            }
        }
        System.err.println("failed");
    }
}
