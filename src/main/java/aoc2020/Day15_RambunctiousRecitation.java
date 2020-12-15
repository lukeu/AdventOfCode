package aoc2020;

import java.util.HashMap;
import java.util.Map;

import util.Util;

public class Day15_RambunctiousRecitation {
    private static final int END = 30000000;
    public static void main(String[] args) {
        Util.profile(new Day15_RambunctiousRecitation()::go, 1);
    }

    void go() {
        Map<Integer, Integer> history = new HashMap<>();
        var in = new int[] {8,0,17,4,1,12};

        int turn = 1;
        for (int i : in) {
            history.put(i, turn++);
        }
        int speek = in[in.length - 1];
        history.remove(speek);

        for ( ; turn <= END; turn++) {
            int spoken = speek;
            Integer prev = history.get(spoken);
            speek = (prev == null) ? 0 : (turn - prev - 1);
            history.put(spoken, turn - 1);
        }

        System.out.println("Found: " + speek);
    }
}
