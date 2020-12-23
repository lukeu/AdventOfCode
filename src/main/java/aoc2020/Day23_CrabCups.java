package aoc2020;

import java.util.ArrayList;
import java.util.Comparator;

import framework.AocMeta;
import framework.Base;

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

    /** A doubly-linked list where each node retains a fixed pointer to the cup 'one lower'. */
    class Cup {
        Cup(int i) {
            id = i;
        }
        int id;
        Cup next;
        Cup prev;
        Cup lower;

        @Override
        public String toString() {
            return String.format(
                    " %s<-%s->%s ", prev == null ? "." : prev.id, id, next == null ? "." : next.id);
        }
    }

    char[] original;

    @Override
    public void parse(String in) {
        original = in.toCharArray();
    }

    @Override
    public String part1() {
        Cup current = build(original.length);

        play(current, 100);

        Cup one = findOne(current);
        return print(one.next, 8).replaceAll(" ", "");
    }

    @Override
    public Long part2() {
        Cup current = build(1_000_000);

        play(current, 10_000_000);

        Cup one = findOne(current);
        return (long) one.next.id * one.next.next.id;
    }

    private Cup build(int size) {
        var cups = new ArrayList<Cup>();

        // Populate linking backwards
        Cup prev = null;
        for (int j = 0; j < size; j++) {
            int id = j < original.length ? original[j] - '0' : j+1;
            Cup c = new Cup(id);
            c.prev = prev;
            cups.add(c);
            prev = c;
        }
        Cup current = cups.get(0);
        current.prev = prev;

        // Link forwards
        for (Cup c : cups) {
            c.prev.next = c;
        }

        // Link each cup to the one numerically lower
        cups.sort(Comparator.comparing(c -> c.id));
        Cup lower = cups.get(cups.size() - 1);
        for (Cup c : cups) {
            c.lower = lower;
            lower = c;
        }

        return current;
    }

    private void play(Cup current, int moves) {
        for (int n = 0; n < moves; n++) {

            Cup take = current.next;

            // Remove 3 items
            Cup link = take.next.next.next;
            join(current, link);

            // Find the dest that isn't in the removed chain
            Cup dest = current.lower;
            while (dest == take || dest == take.next || dest == take.next.next) {
                dest = dest.lower;
            }

            // Insert chain of 3 @ d
            Cup after = dest.next;
            join(dest, take);
            join(take.next.next, after);

            current = current.next;
        }
    }

    private void join(Cup a, Cup b) {
        a.next = b;
        b.prev = a;
    }

    private Cup findOne(Cup c) {
        while (c.id != 1) {
            c = c.next;
        }
        return c;
    }

    /** Useful for debugging, so print with spaces even though part 1 discards them. */
    private String print(Cup c, int len) {
        String result = "";
        while ((len--) > 0) {
            result += c.id;
            result += " ";
            c = c.next;
        }
        return result;
    }
}
