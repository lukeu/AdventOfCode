package aoc2020;

import java.util.List;

import framework.Base;
import util.SUtils;

public class Day06_CustomCustoms extends Base {

    public static void main(String[] args) {
        Base.run(Day06_CustomCustoms::new, 1);
    }

    @Override
    public Object expect2() { return 2971; }

    private List<String> in;

    @Override
    public void parse(String text) {
        in = SUtils.blocks(text);
    }

    @Override
    public Object part2() {
        int found = 0;
        for (String group : in) {
            var lines = group.split("\n");
            var counts = new int[26];
            group.chars().forEach(c -> {if (c != '\n') counts[c-'a']++;});
            for (int i : counts) {
                if (i == lines.length) { // for Part1: (i != 0)
                    found++;
                }
            }
        }
        return found;
    }
}
