package aoc2020;

import java.util.regex.Pattern;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import util.FUtils;

public class Day07_HandyHaversacks {
    public static void main(String[] args) {
        new Day07_HandyHaversacks().go();
    }

    record Pair(int count, String colour) {}

    SetMultimap<String, Pair> rules = HashMultimap.create();

    void go() {
        for (String rule : FUtils.readLines(2020, 7)) {
            parse(rule);
        }
        //System.out.println(Joiner.on("\n").join(rules.asMap().entrySet()));
        System.out.println("Rules: " + rules.keySet().size());
        System.out.println("Rules: " + rules.keys().size());

        long found = 0;
        for (String start : rules.keySet()) {
            if (!start.equals("shiny gold") && hasGold(start)) {
                ++ found;
            }
        }
        System.out.println("Has Gold: " + found);
        System.out.println("Contents: " + countRecursive(0, "shiny gold"));
    }

    boolean hasGold(String bag) {
        if (bag.equals("shiny gold")) {
            return true;
        }
        for (Pair c : rules.get(bag)) {
            if (hasGold(c.colour)) {
                return true;
            }
        }
        return false;
    }

    int countRecursive(int num, String bag) {
        var cols = rules.get(bag);
        int sum = 0;
        for (Pair c : cols) {
            sum += c.count + c.count * countRecursive(c.count, c.colour);
        }
        return sum;
    }

    Pattern PAT = Pattern.compile("(\\d)+ (\\w+ \\w+) bag");

    void parse(String text) {
        var parts = text.split(" bags contain ");
        var m = PAT.matcher(parts[1]);
        while (m.find()) {
            rules.put(parts[0], new Pair(Integer.parseInt(m.group(1)), m.group(2)));
        }
    }
}
