package aoc2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.primitives.Chars;
import framework.AocMeta;
import framework.Base;
import framework.Input;

@AocMeta(notes = "flood fill")
public class Day10_PipeMaze extends Base {
    public static void main(String[] args) {
        Base.run(Day10_PipeMaze::new, 1);
    }

    ArrayList<String> lines;
    ArrayList<char[]> canvas;
    Pos start;

    @Override
    public String testInput() {
        return
"""
.F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...
""";
    }

    @Override public Object testExpect1() { return 70; }
    @Override public Object testExpect2() { return 8; }
    @Override public Object expect1() { return 6812; }
    @Override public Object expect2() { return 527; }

    @Override
    public void parse(Input in) {

        // Add a border of '.' around all sides, to avoid bounds-checking
        lines = in.lines().stream()
                .map(s -> "." + s + ".")
                .collect(Collectors.toCollection(ArrayList::new));
        String pad = ".".repeat(lines.get(0).length());
        lines.add(0, pad);
        lines.add(pad);

        canvas = IntStream.range(0, lines.size())
                .mapToObj(i -> pad.toCharArray())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    enum Dir {
        N,E,S,W;
        Dir clock()    { return shift(1); }
        Dir opposite() { return shift(2); }
        Dir anti()     { return shift(3); }

        private Dir shift(int offset) {
            return values()[(ordinal() + offset) % 4];
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
        start = findStart();

        Pos prev = new Pos(-1, -1); // Set off in any direction.
        Pos current = start;
        long steps = 1;
        do {
            copyToCanvas(current);
            Pos next = current.step(getDir(prev, current));
            prev = current;
            current = next;
            ++ steps;
        } while (!current.equals(start));
        return steps / 2;
    }

    void copyToCanvas(Pos p) {
        canvasSet(p, at(p));
    }

    @Override
    public Integer part2() {

        Pos prev = new Pos(-1, -1);
        // Set off in an arbitrary direction. The code will figure out which of I and O
        // is outside by whether the flood-fill hits a bound. (Lazy solution: throws exception!)
        Pos current = start;
        boolean iOut = false;
        boolean oOut = false;
        do {
            var dir = getDir(prev, current);
            Pos next = current.step(dir);
            if (!iOut) iOut |= floodFill(current, dir.anti(), next, 'I');
            if (!oOut) oOut |= floodFill(current, dir.clock(), next, 'O');
            prev = current;
            current = next;
        } while (!current.equals(start));

        printLines(toString());
        printLines();
        printLines("Count I: " + count('I') + " outside? " + iOut);
        printLines("Count O: " + count('O') + " outside? " + oOut);
        printLines("Count .: " + count('.'));
        printLines();
        return iOut ? count('O') : count('I');
    }

    /** @return whether the fill hit the 'picture' bounds */
    private boolean floodFill(Pos current, Dir sideDirection, Pos next, char fill) {
        try {
            fillRecursive(current.step(sideDirection), fill);
            fillRecursive(next.step(sideDirection), fill);
        } catch (IndexOutOfBoundsException ex) {
            return true;
        }
        return false;
    }

    private void fillRecursive(Pos p, char fill) {
        if (canvasAt(p) != '.') {
            return;
        }
        canvasSet(p, fill);
        for (Dir d : Dir.values()) {
            fillRecursive(p.step(d), fill);
        }
    }

    private int count(char fill) {
        return Collections.frequency(
                canvas.stream()
                        .flatMap(cc -> Chars.asList(cc).stream())
                        .toList(),
                fill);
    }

    @Override
    public final String toString() {
        return canvas.stream().map(String::new).collect(Collectors.joining("\n"));
    }

    Dir getDir(Pos prev, Pos cur) {
        for (Dir d : Dir.values()) {
            if (joins(at(cur)).contains(d)) { // Can step out of 'cur' in direction 'd'?
                Pos next = cur.step(d);
                if (!next.equals(prev)) {
                    if (joins(at(next)).contains(d.opposite())) { // May step into 'next'?
                        return d;
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

    char canvasAt(Pos p) {
        return canvas.get(p.r)[p.c];
    }

    void canvasSet(Pos p, char ch) {
        canvas.get(p.r)[p.c] = ch;
    }
}
