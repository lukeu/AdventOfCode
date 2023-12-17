package aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import framework.Base;
import framework.Input;
import util.SUtils;

public class Day05_IfYouGiveASeedAFertilizer extends Base {
    public static void main(String[] args) {
        Base.run(Day05_IfYouGiveASeedAFertilizer::new, 1);
    }

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
    @Override public Object testExpect1() { return 35; }
    @Override public Object testExpect2() { return 46; }

    record Conversion(long dest, long source, long len) {
        Conversion (String str) {
            this(
                    SUtils.extractLongs(str)[0],
                    SUtils.extractLongs(str)[1], 
                    SUtils.extractLongs(str)[2]);
        }
    }

    record Mapping(String name, List<Conversion> conversions) {
        long apply(long v) {
            for (var c : conversions) {
                if (v > c.source && v <= c.source + c.len) {
                    return c.dest + v - c.source;
                }
            }
            return v;
        }
    }

    record Range(long start, long len) {}

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
                    ranges.add(new Range(seeds[i], seeds[i + 1]));
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
        long found = 0;
        
        return found;
    }
}
