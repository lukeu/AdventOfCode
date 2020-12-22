package aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import framework.Base;
import util.SUtils;

public class Day22 extends Base {
    public static void main(String[] args) {
        Base.run(Day22::new, 1);
    }

    @Override
    public String testInput() {
        return "Player 1:\n"
                + "9\n"
                + "2\n"
                + "6\n"
                + "3\n"
                + "1\n"
                + "\n"
                + "Player 2:\n"
                + "5\n"
                + "8\n"
                + "4\n"
                + "7\n"
                + "10";
    }
    @Override public Object testExpect1() { return 306L; }
    @Override public Object testExpect2() { return 291L; }
    @Override public Object expect1() { return 33694L; }
    @Override public Object expect2() { return 31835L; }

    List<Integer> p1;
    List<Integer> p2;

    @Override
    public void parse(String in) {
        var blocks = SUtils.blocks(in);
        p1 = Arrays.stream(blocks.get(0).split("\n")).skip(1).map(Integer::parseInt).collect(Collectors.toList());
        p2 = Arrays.stream(blocks.get(1).split("\n")).skip(1).map(Integer::parseInt).collect(Collectors.toList());
    }

    @Override
    public Long part1() {
        var old1 = new ArrayList<>(p1);
        var old2 = new ArrayList<>(p2);
        while (!p1.isEmpty() && !p2.isEmpty()) {
            int c1 = p1.remove(0);
            int c2 = p2.remove(0);
            if (c1 > c2) {
                p1.add(c1);
                p1.add(c2);
            } else {
                p2.add(c2);
                p2.add(c1);
            }
        }
        long found = score();
        p1 = old1;
        p2 = old2;
        return found;
    }

    long score() {
        long found = 0;
        var winner = p1.isEmpty() ? p2 : p1;
        int i = winner.size();
        for (int c : winner) {
            found += (i--) * c;
        }
        return found;
    }

    HashSet<Long> history = new HashSet<>();

    @Override
    public Long part2() {
        play();

        return score();
    }

    boolean play() {
        while (!p1.isEmpty() && !p2.isEmpty()) {
            if (!history.add(hash())) {
                return true;
            }
            int c1 = p1.remove(0);
            int c2 = p2.remove(0);
            boolean win1 = c1 > c2;
            if (p1.size() >= c1 && p2.size() >= c2) {
                var old1 = new ArrayList<>(p1);
                var old2 = new ArrayList<>(p2);
                var oldH = new HashSet<>(history);
                while (p1.size() > c1) p1.remove(p1.size() - 1);
                while (p2.size() > c2) p2.remove(p2.size() - 1);

                win1 = play();
                // System.out.println("score "+ score() + "" + p1 + "" + p2);

                p1 = old1;
                p2 = old2;
                history = oldH;
            }
            if (win1) {
                p1.add(c1);
                p1.add(c2);
            } else {
                p2.add(c2);
                p2.add(c1);
            }
        }
        return p2.isEmpty();
    }

    long hash() {
        Hasher hasher = Hashing.goodFastHash(64).newHasher();
        p1.stream().forEach(hasher::putInt);
        hasher.putLong(-1L);
        p2.stream().forEach(hasher::putInt);
        return hasher.hash().asLong();
    }
}
