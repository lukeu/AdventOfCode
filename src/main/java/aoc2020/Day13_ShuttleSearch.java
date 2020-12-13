package aoc2020;

import java.util.ArrayList;
import java.util.Comparator;

import framework.Base;
import util.SUtils;

public class Day13_ShuttleSearch extends Base {
    public static void main(String[] args) {
        Base.run(Day13_ShuttleSearch::new, 1);
    }

    @Override
    public String testInput() {
        return "939\n"
                + "7,13,x,x,59,x,31,19";
    }
    @Override public Object testExpect1() { return 295; }
    @Override public Object testExpect2() { return 1068781L; }
    @Override public Object expect1() { return 6568; }
    @Override public Object expect2() { return 554865447501099L; }

    int earliest;
    int[] buses;

    @Override
    public void parse(String text) {
        var in = SUtils.lines(text);

        earliest = Integer.parseInt(in.get(0));
        buses = SUtils.splitInts(in.get(1), ",", -1);
    }

    @Override
    public Integer part1() {
        int min = Integer.MAX_VALUE;
        int id = -1;
        for (int b : buses) {
            if (b > -1) {
                int wait = b - earliest % b;
                if (wait < min) {
                    min = wait;
                    id = b;
                }
            }
        }
        return min * id;
    }

    // OK, nailed it. Runs in 5 us - slightly better than 89 min ;-)
    @Override
    public Long part2() {
        long inc = 1;
        long t = 0;
        for (int offset = 0; offset < buses.length; offset++) {
            int bus = buses[offset];
            if (bus != -1) {
                do {
                    t += inc;
                } while ((t + offset) % bus != 0);

                // This works because the bus numbers are (kindly?) prime numbers.
                // Otherwise we'd be having to work out the LCM here.
                inc *= bus;
            }
        }
        return t;
    }

    void part2_slooooow() {

        record Slot(int bus, int offset) {}

        int off = 0;
        var slots = new ArrayList<Slot>();
        for (int b : buses) {
            if (b != -1) {
                slots.add(new Slot(b, off));
            }
            off++;
        }

        // Heehee! This dumb little optimisation (43x faster upon brute force) did the trick.
        // It may've taken 1.5 hours - but it got it!! ;-) My kids needed me, but I left this
        // running & what do you know - it completed.
        slots.sort(Comparator.comparing(s -> -s.bus));

        System.out.println(slots);
        long mult = 1;
        next:
        while (true) {
            Slot first = slots.get(0);
            long att = first.bus * (++mult) - first.offset;
            // long att = 1068781 + 4;
            for (int i = 1; i < slots.size(); i++) {
                Slot slot = slots.get(i);
                if ((att + slot.offset) % slot.bus != 0) {
                    continue next;
                }
            }
            System.out.println("Part 2 (slow): " + att);
            return;
        }
    }
}
