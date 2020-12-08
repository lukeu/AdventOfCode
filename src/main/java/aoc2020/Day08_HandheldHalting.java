package aoc2020;

import util.FUtils;

public class Day08_HandheldHalting {
    public static void main(String[] args) {
        new Day08_HandheldHalting().go();
    }

    void go() {
        var in = FUtils.readLines(2020, 8);

        for (int swap = 0; swap < in.size(); swap++) {
            boolean[] exe = new boolean[in.size()];
            long acc = 0;
            int pc = 0;
            while (!exe[pc]) {
                exe[pc] = true;
                String[] split = in.get(pc).split(" ");
                String cmd = split[0];
                int arg = Integer.parseInt(split[1]);
                if (swap != pc) {
                    switch (cmd) {
                    case "acc": acc += arg; pc++; break;
                    case "jmp": pc += arg; break;
                    default: pc ++; break;
                    }
                } else {
                    switch (cmd) {
                    case "acc": acc += arg; pc++; break;
                    case "nop": pc += arg; break;
                    default: pc++; break;
                    }
                }
                if (pc == exe.length) {
                    System.out.println("Halted: acc = " + acc);
                    System.exit(0);
                }
            }
        }
        System.err.println("failed");
    }
}
