package aoc2020;

import java.util.stream.IntStream;

import framework.AocMeta;
import framework.Base;
import framework.Input;

@AocMeta(notes="custom linked list")
public class Day23_CrabCups extends Base {
    public static void main(String[] args) {
        Base.run(Day23_CrabCups::new, 1);
    }

    @Override
    public String testInput() {
        return "389125467";
    }
    @Override public Object testExpect1() { return "67384529"; }
    @Override public Object testExpect2() { return 149245887792L; }
    @Override public Object expect1() { return "47598263"; }
    @Override public Object expect2() { return 248009574232L; }

    char[] original;

    /**
     * Emulate a singly linked list.
     * Index 0 contains the starting cup, this is not part of the cycle.
     * Indexes 1..N represent a cup-ID, the value at this index is the next (clockwise) cup's ID;
     */
    int [] cups;

    @Override
    public void parse(Input in) {
        original = in.text().toCharArray();
    }

    @Override
    public String part1() {
        build(original.length);
        play(100);

        return print(1, 8);
    }

    @Override
    public Long part2() {
        build(1_000_000);
        play(10_000_000);
        return (long) cups[1] * cups[cups[1]];
    }

    private void build(int size) {
        cups = IntStream.rangeClosed(0, size).map(i -> i + 1).toArray();
        cups[size] = original[0] - '0';
        int cur = 0;
        for (char ch : original) {
            int next = ch - '0';
            cups[cur] = next;
            cur = next;
        }
        cups[cur] = size == original.length ? original[0] - '0' : original.length + 1;
    }

    private void play(int moves) {
        int current = 0;
        for (int n = 0; n < moves; n++) {
            current = cups[current];

            // Remove 3 items
            int h0 = cups[current];
            int h1 = cups[h0];
            int h2 = cups[h1];

            // Find the dest cup - one that isn't in the removed chain
            int dest = current;
            do {
                if (--dest == 0) {
                    dest = cups.length - 1;
                }
            } while (h0 == dest || h1 == dest || h2 == dest);

            // Insert the chain of 3 @ dest
            int temp = cups[dest];
            cups[dest] = h0;
            cups[current] = cups[h2];
            cups[h2] = temp;
        }
    }

    private String print(int c, int len) {
        var sb = new StringBuilder();
        while ((len--) > 0) {
            c = cups[c];
            sb.append(c);
        }
        return sb.toString();
    }
}
