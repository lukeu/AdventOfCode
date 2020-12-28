package aoc2020;

import java.util.regex.Pattern;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import framework.AocMeta;
import framework.Base;
import util.SUtils;

@AocMeta(notes = "recursive bags")
public class Day07_HandyHaversacks extends Base {
    public static void main(String[] args) {
        Base.run(Day07_HandyHaversacks::new, 1);
    }

    @Override public Object expect1() { return 131; }
    @Override public Object expect2() { return 11261; }

    record Pair(int count, String colour) {}

    SetMultimap<String, Pair> rules = HashMultimap.create();

    private static final Pattern PAT = Pattern.compile("(\\d)+ (\\w+ \\w+) bag");

    @Override
    public void parse(String text) {
        for (String rule : SUtils.lines(text)) {
            var parts = rule.split(" bags contain ");
            var m = PAT.matcher(parts[1]);
            while (m.find()) {
                rules.put(parts[0], new Pair(Integer.parseInt(m.group(1)), m.group(2)));
            }
        }
    }

    @Override
    public Object part1() {
        int found = 0;
        for (String start : rules.keySet()) {
            if (hasGold(start)) {
                ++ found;
            }
        }
        return found - 1; // exclude the 'shiny gold' rule itself
    }

    boolean hasGold(String bag) {
        return bag.equals("shiny gold") ||
                rules.get(bag).stream().anyMatch(r -> hasGold(r.colour));
    }

    @Override
    public Object part2() {
        return countRecursive(0, "shiny gold");
    }

    int countRecursive(int num, String bag) {
        var cols = rules.get(bag);
        int sum = 0;
        for (Pair c : cols) {
            sum += c.count + c.count * countRecursive(c.count, c.colour);
        }
        return sum;
    }
}
