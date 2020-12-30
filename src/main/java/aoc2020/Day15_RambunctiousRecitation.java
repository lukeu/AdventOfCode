package aoc2020;

import java.util.BitSet;

import framework.AocMeta;
import framework.Base;
import framework.Input;

@AocMeta(notes = "elf memory game")
public class Day15_RambunctiousRecitation extends Base {
    public static void main(String[] args) {
        Base.run(Day15_RambunctiousRecitation::new, 1);
    }

    int[] in = {8,0,17,4,1,12};
    @Override public Object expect1() { return 981L; }
    @Override public Object expect2() { return 164878L; }

    @Override
    public Input input() {
        return null; // No input file used
    }

    @Override
    public Long part1() {
        return play(2020);
    }

    @Override
    public Long part2() {
        return play(30000000);
    }

    long play(int numTurns) {
        int[] history = new int[numTurns]; // dictionary [ number-spoken => turn-spoken ]
        var bs = new BitSet(numTurns);

        int turn = 0;
        for (int i : in) {
            history[i] = ++turn;
            bs.set(i);
        }
        int speek = in[in.length - 1];
        history[speek] = 0;
        bs.set(speek, false);

        for ( ; turn < numTurns; turn++) {
            int spoken = speek;
            speek = bs.get(spoken) ? turn - history[spoken] : 0;
            history[spoken] = turn;
            bs.set(spoken);
        }

        return speek;
    }
}
