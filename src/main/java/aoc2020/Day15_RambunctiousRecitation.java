package aoc2020;

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
        Map<Integer, Integer> history = new HashMap<>();

        int turn = 1;
        for (int i : in) {
            history.put(i, turn++);
        }
        int speek = in[in.length - 1];
        history.remove(speek);

        for ( ; turn <= numTurns; turn++) {
            int spoken = speek;
            Integer prev = history.get(spoken);
            speek = (prev == null) ? 0 : (turn - prev - 1);
            history.put(spoken, turn - 1);
        }

        return speek;
    }
}
