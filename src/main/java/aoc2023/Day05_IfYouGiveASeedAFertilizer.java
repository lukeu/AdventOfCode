package aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import framework.Base;
import framework.Input;
import util.SUtils;

public class Day05_IfYouGiveASeedAFertilizer extends Base {
    @Override
    public String testInput() {
        return
"""
seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4
""";
    }

    public static void main(String[] args) {
        testRemapRanges();
        Base.run(Day05_IfYouGiveASeedAFertilizer::new, 1);
    }

    /** A quick sanity test. */
    private static void testRemapRanges() {
        var test = new Day05_IfYouGiveASeedAFertilizer();
        var m = new Mapping("Test mapping", List.of(
                new Conversion(20, 10, 10),
                new Conversion(10, 20, 10)));

        var seeds = new long[] { 19, 2, 28, 2 };
        for (int i = 0; i < seeds.length; i += 2) {
            test.ranges.add(new Range(seeds[i], seeds[i + 1] + seeds[i]));
        }
        test.remapRanges(m);
        System.out.println(test.ranges);
        test.ranges.clear();
    }

    @Override public Object testExpect1() { return 35; }
    @Override public Object testExpect2() { return 46; }

    record Conversion(long dest, long source, long len) {
        Conversion (String str) {
            this(SUtils.extractLongs(str));
        }
        private Conversion(long[] values) {
            this(values[0], values[1], values[2]);
            assert values.length == 3;
        }

        long adjust(long v) {
            return v + dest - source;
        }

        boolean contains(long v) {
            return v >= source && v < end();
        }

        long end() {
            return source + len;
        }
    }

    record Mapping(String name, List<Conversion> conversions) {
        long apply(long v) {
            for (var c : conversions) {
                if (c.contains(v)) {
                    return c.adjust(v);
                }
            }
            return v;
        }
    }

    record Range(long first, long end) {
        Range {
            assert len() > 0; // Our algorithm should drop (not construct) empty ranges
        }
        long len() { return end - first; }
        long last() { return end - 1; }
    }

    long seeds[] = null;
    List<Range> ranges = new ArrayList<>();
    List<Mapping> mappings = new ArrayList<>();

    @Override
    public void parse(Input in) {
        for (var block : in.blocks()) {
            var lines = new ArrayList<>(SUtils.lines(block));
            if (seeds == null) {
                assert lines.size() == 1;
                String line = lines.get(0).substring(7);
                seeds = SUtils.extractLongs(line);
                for (int i = 0; i < seeds.length; i += 2) {
                    ranges.add(new Range(seeds[i], seeds[i] + seeds[i + 1]));
                }
            } else {
                String name = lines.remove(0);
                mappings.add(new Mapping(
                        name,
                        lines.stream().filter(s -> !s.isBlank()).map(Conversion::new).toList()));
            }
        }
    }

    @Override
    public Long part1() {
        return Arrays.stream(seeds).map(this::applyAll).min().getAsLong();
    }

    long applyAll(long v) {
        for (var m : mappings) {
            v = m.apply(v);
        }
        return v;
    }

    @Override
    public Long part2() {
        for (var m : mappings) {
            remapRanges(m);
        }
        return ranges.stream().mapToLong(Range::first).min().getAsLong();
    }

    void remapRanges(Mapping m) {
        var todo = new ArrayList<>(ranges);
        var unmapped = newList();
        var mapped = newList();
        for (var c : m.conversions)  {
            for (var r : todo) {
                if (r.last() < c.source() || r.first() >= c.end()) {
                    unmapped.add(r);
                    continue;
                }

                boolean sliceLeft = r.first() < c.source();
                boolean sliceRight = r.end() > c.end();

                if (sliceLeft) {
                    unmapped.add(new Range(r.first(), c.source()));
                }
                if (sliceRight) {
                    unmapped.add(new Range(c.end(), r.end()));
                }
                long interFirst = sliceLeft ? c.source() : r.first();
                long interEnd = sliceRight ? c.end() : r.end();
                mapped.add(new Range(c.adjust(interFirst), c.adjust(interEnd)));
            }
            todo = unmapped;
            unmapped = newList();
        }
        ranges = new ArrayList<>(todo);
        ranges.addAll(mapped);
    }

    static ArrayList<Range> newList() {
        return new ArrayList<>();
    }
}
