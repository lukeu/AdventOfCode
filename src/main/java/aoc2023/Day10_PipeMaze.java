package aoc2023;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.stream.Collectors;

import framework.Base;
import framework.Input;

public class Day10_PipeMaze extends Base {
    public static void main(String[] args) {
        Base.run(Day10_PipeMaze::new, 1);
    }

    ArrayList<String> lines;

    @Override
    public String testInput() {
        return
"""
7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ
""";
    }

    @Override public Object testExpect1() { return 8; }
    @Override public Object testExpect2() { return 0; }
    @Override public Object expect1() { return 6812; }

    @Override
    public void parse(Input in) {

        // Add a border of '.' around all sides, to avoid bounds-checking
        lines = in.lines().stream()
                .map(s -> "." + s + ".")
                .collect(Collectors.toCollection(ArrayList::new));
        String pad = ".".repeat(lines.get(0).length());
        lines.add(0, pad);
        lines.add(pad);
    }

    enum Dir {
        N,E,S,W;
        Dir opposite() {
            return values()[(ordinal() + 2) % 4];
        }
    }

    EnumSet<Dir> joins(char ch) {
        return switch(ch) {
        case 'S' -> EnumSet.allOf(Dir.class);
        case '|' -> EnumSet.of(Dir.N, Dir.S);
        case '-' -> EnumSet.of(Dir.W, Dir.E);
        case 'L' -> EnumSet.of(Dir.N, Dir.E);
        case 'J' -> EnumSet.of(Dir.N, Dir.W);
        case '7' -> EnumSet.of(Dir.S, Dir.W);
        case 'F' -> EnumSet.of(Dir.S, Dir.E);
        case '.' -> EnumSet.noneOf(Dir.class);
        default -> throw new AssertionError("failed");
        };
    }

    record Pos(int r, int c) {
        Pos step(Dir d) {
            return switch(d) {
            case N -> new Pos(r-1, c);
            case E -> new Pos(r, c+1);
            case S -> new Pos(r+1, c);
            case W -> new Pos(r, c-1);
            };
        }
    }

    @Override
    public Long part1() {
        Pos start = findStart();
        Pos prev = new Pos(-1, -1); // Set off in any direction.
        Pos current = start;
        long steps = 1;
        do {
            Pos next = move(prev, current);
            prev = current;
            current = next;
            ++ steps;
        } while (!current.equals(start));
        return steps / 2;
    }

    Pos move(Pos prev, Pos cur) {
        for (Dir d : Dir.values()) {
            if (joins(at(cur)).contains(d)) { // Can step out of 'cur' in direction 'd'?
                Pos next = cur.step(d);
                if (!next.equals(prev)) {
                    if (joins(at(next)).contains(d.opposite())) { // May step into 'next'?
                        return next;
                    }
                }
            }
        }
        throw new AssertionError();
    }

    Pos findStart() {
        int r = 0;
        for (String line : lines) {
            for (int c = 0; c < line.length(); c++) {
                char ch = line.charAt(c);
                if (ch == 'S') {
                    return new Pos(r, c);
                }
            }
            r++;
        }
        throw new AssertionError();
    }

    char at(Pos p) {
        return lines.get(p.r).charAt(p.c);
    }

    @Override
    public Long part2() {
        long found = 0;
        
        return found;
    }
}
