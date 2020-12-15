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
        int spoken = 0;
        for (int i : in) {
            spoken = i;
            history.put(spoken, turn);
            turn++;
        }
        history.remove(spoken);

        int speek = spoken;
        while (turn <= END) {
            spoken = speek;
            Integer prev = history.get(spoken);
            speek = (prev == null) ? 0 : (turn - prev - 1);
            history.put(spoken, turn - 1);
            turn++;
        }

        System.out.println("Found: " + speek);
    }
}
