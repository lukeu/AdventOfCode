package aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;
import util.FUtils;
import util.SUtils;
import util.Util;

public class Day16 {
    public static void main(String[] args) {
        Util.profile(() -> new Day16().go(), 1);
    }

    record Range(String name, int a, int b, int c, int d) implements IntPredicate {
        @Override
        public boolean test(int value) {
            return (value >= a && value <= b) || (value >= c && value <= d);
        }
    }

    List<Range> ranges = new ArrayList<>();
    List<int[]> nearby;

    IntPredicate inAnyRange = val -> {
        for (Range r : ranges) {
            if (r.test(val)) {
                return true;
            }
        }
        return false;
    };

    List<Set<Range>> cand = new ArrayList<>();

    void go() {
        var in = FUtils.splitLines(2020, 16, "\n\n");
        readRanges(Splitter.on('\n').splitToList(in.get(0)));
        List<String> yourLines = Splitter.on('\n').splitToList(in.get(1).trim());
        List<String> nearbyLines = Splitter.on('\n').splitToList(in.get(2).trim());

        int[] ticket = readTickets(yourLines).get(0);
        nearby = readTickets(nearbyLines);

        System.out.println("Range count: " + ranges.size());
        System.out.println("Nearby count: " + nearby.size());
        System.out.println("Part1: " + part1(nearbyLines));

        for (int i = 0; i < ranges.size(); i++) {
            cand.add(new HashSet<>(ranges));
        }

        for (int[] testValues : nearby) {
            if (Arrays.stream(testValues).allMatch(inAnyRange)) {
                for (int i = 0; i < testValues.length; i++) {
                    int t = testValues[i];
                    cand.get(i).removeIf(r -> !r.test(t));
                }
            }
        }

        // printCandidates();

        var change = true;
        while (change) {
            change = false;
            for (Set<Range> j : cand) {
                if (j.size() == 1) {
                    for (Set<Range> k : cand) {
                        if (k.size() > 1 && k.remove(j.iterator().next())) {
                            change = true;
                        }
                    }
                }
            }
        }

        long product = 1;
        for (int i = 0; i < cand.size(); i++) {
            Range r = cand.get(i).iterator().next();
            if (r.name.startsWith("dep")) {
                product *= ticket[i];
            }
        }
        System.out.println("Part 2: " + product);
    }

    void printCandidates() {
        int i = 0;
        for (var set : cand) {
            System.out.println((i++) + ": " + set.size() + " " + set);
        }
    }

    long part1(List<String> lines) {
        return lines.stream()
                .flatMapToInt(s -> Arrays.stream(SUtils.splitInts(s, ",", -1)))
                .filter(i -> i >= 0)
                .filter(inAnyRange.negate())
                .sum();
    }

    List<int[]> readTickets(List<String> lines) {
        return lines.stream()
                .skip(1)
                .map(s -> SUtils.splitInts(s, ",", -1))
                .collect(Collectors.toList());
    }

    void readRanges(List<String> splitToList) {
        Pattern p = Pattern.compile("(\\: |-| or )");
        for (String s : splitToList) {
            var scan = new Scanner(s);
            scan.useDelimiter(p);
            ranges.add(new Range(
                    scan.next(),
                    scan.nextInt(),
                    scan.nextInt(),
                    scan.nextInt(),
                    scan.nextInt()
                    ));
        }
    }
}
