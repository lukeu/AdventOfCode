package aoc2020;

import java.util.ArrayList;

import framework.AocMeta;
import framework.Base;
import framework.Input;

@AocMeta(notes = "trivial asm")
public class Day08_HandheldHalting extends Base {
    public static void main(String[] args) {
        Base.run(Day08_HandheldHalting::new, 1);
    }

    @Override
    public String testInput() {
        return "nop +0\n"
                + "acc +1\n"
                + "jmp +4\n"
                + "acc +3\n"
                + "jmp -3\n"
                + "acc -99\n"
                + "acc +1\n"
                + "jmp -4\n"
                + "acc +6";
    }
    @Override public Object testExpect1() { return 5L; }
    @Override public Object testExpect2() { return 8L; }
    @Override public Object expect1() { return 2058L; }
    @Override public Object expect2() { return 1000L; }

    record Ins (String cmd, int arg) {}
    ArrayList<Ins> instructions;

    @Override
    public void parse(Input input) {
        var in = input.lines();
        instructions = new ArrayList<Ins>(in.size());
        for (String s : in) {
            var split = s.split(" ");
            instructions.add(new Ins(split[0].intern(), Integer.parseInt(split[1])));
        }
    }

    @Override
    public Object part1() {
        return solve(false);
    }

    @Override
    public Object part2() {
        return solve(true);
    }

    public long solve(boolean p2) {

        // Even brute force doesn't seem too bad. (Whole method is sub 1 ms, warm)
        for (int swap = -1; swap < instructions.size(); swap++) {
            boolean[] exe = new boolean[instructions.size()];
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
                ++pc;
                if (p2 && pc == exe.length) {
                    return acc;
                }
            }
            if (!p2 && swap == -1) {
                return acc;
            }
        }
        return -1;
    }
}
