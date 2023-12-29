package aoc2020;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;
import framework.Base;
import framework.Input;
import util.SUtils;

public class Day16_TicketTranslation extends Base {
    public static void main(String[] args) {
        Base.run(Day16_TicketTranslation::new, -1);
    }

    record Range(String name, int a, int b, int c, int d) implements IntPredicate {
        @Override
        public boolean test(int value) {
            return (value >= a && value <= b) || (value >= c && value <= d);
        }
    }

    List<Range> ranges;
    int[] myTicket;
    List<int[]> nearby;
    List<Set<Range>> candidates;

    IntPredicate inAnyRange = v -> ranges.stream().anyMatch(r -> r.test(v));

    @Override public Object expect1() { return 22073; }
    @Override public Object expect2() { return 1346570764607L; }

    @Override
    public void parse(Input input) {

        var in = input.blocks();
        var splitter = Splitter.on('\n');
        ranges = readRanges(splitter.splitToList(in.get(0)));
        myTicket = readTickets(splitter.splitToList(in.get(1).trim())).get(0);
        nearby = readTickets(splitter.splitToList(in.get(2).trim()));

        candidates = ranges.stream()
                .map(unused -> new HashSet<>(ranges))
                .collect(Collectors.toList());
    }

    @Override
    public Integer part1() {
        return nearby.stream()
                .flatMapToInt(n -> Arrays.stream(n))
                .filter(inAnyRange.negate())
                .sum();
    }

    @Override
    public Long part2() {
        for (int[] testValues : nearby) {
            if (Arrays.stream(testValues).allMatch(inAnyRange)) { // Skip invalid tickets
                for (int i = 0; i < testValues.length; i++) {
                    int t = testValues[i];
                    candidates.get(i).removeIf(r -> !r.test(t)); // Remove possibilities
                }
            }
        }

        // Process of elimination
        // Insight to sort by size from: https://www.youtube.com/watch?v=0OJ5rX71HKk
        Set<Range> solved = new HashSet<>();
        candidates.stream()
            .sorted(Comparator.comparingInt(Set::size))
            .forEach(set -> {
                set.removeAll(solved);
                solved.addAll(set); // Always a single item left
            });

        // Find the 'departure' fields
        long product = 1;
        for (int i = 0; i < candidates.size(); i++) {
            Range r = candidates.get(i).iterator().next();
            if (r.name.startsWith("dep")) {
                product *= myTicket[i];
            }
        }
        return product;
    }

    @SuppressWarnings("unused") // Useful for debugging
    private void printCandidates() {
        int i = 0;
        for (var set : candidates) {
            System.out.println((i++) + ": " + set.size() + " " + set);
        }
    }

    List<Range> readRanges(List<String> lines) {
        Pattern p = Pattern.compile("(\\: |-| or )");
        return lines.stream()
            .map(s -> {
                try (var scan = new Scanner(s)) {
                    scan.useDelimiter(p);
                    return new Range(
                            scan.next(),
                            scan.nextInt(),
                            scan.nextInt(),
                            scan.nextInt(),
                            scan.nextInt());
                }
            }).collect(Collectors.toList());
    }

    List<int[]> readTickets(List<String> lines) {
        return lines.stream()
                .skip(1)
                .map(s -> SUtils.splitInts(s, ",", -1))
                .collect(Collectors.toList());
    }
}
