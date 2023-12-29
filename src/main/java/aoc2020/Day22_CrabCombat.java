package aoc2020;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import framework.AocMeta;
import framework.Base;
import framework.Input;

@AocMeta(notes = "recursive card game")
public class Day22_CrabCombat extends Base {
    public static void main(String[] args) {
        Base.run(Day22_CrabCombat::new, 1);
    }

    @Override
    public String testInput() {
        return "Player 1:\n9\n2\n6\n3\n1\n\nPlayer 2:\n5\n8\n4\n7\n10";
    }
    @Override public Object testExpect1() { return 306L; }
    @Override public Object testExpect2() { return 291L; }
    @Override public Object expect1() { return 33694L; }
    @Override public Object expect2() { return 31835L; }

    List<Integer> hand1;
    List<Integer> hand2;

    @Override
    public void parse(Input in) {
        var blocks = in.blocks();
        hand1 = Arrays.stream(blocks.get(0).split("\n")).skip(1).map(Integer::parseInt).toList();
        hand2 = Arrays.stream(blocks.get(1).split("\n")).skip(1).map(Integer::parseInt).toList();
    }

    @Override
    public Long part1() {
        return playAndScore(false);
    }

    @Override
    public Long part2() {
        return playAndScore(true);
    }

    private Long playAndScore(boolean recurse) {
        var p1 = new ArrayList<>(hand1);
        var p2 = new ArrayList<>(hand2);
        boolean win1 = play(p1, p2, recurse);
        return score(win1 ? p1 : p2);
    }

    boolean play(List<Integer> p1, List<Integer> p2, boolean recurse) {
        var history = new HashSet<Long>();
        while (!p1.isEmpty() && !p2.isEmpty()) {
            if (!history.add(hash(p1, p2))) {
                return true;
            }
            int c1 = p1.remove(0);
            int c2 = p2.remove(0);
            boolean win1 = c1 > c2;
            if (recurse && p1.size() >= c1 && p2.size() >= c2) {
                var next1 = p1.stream().limit(c1).collect(toList());
                var next2 = p2.stream().limit(c2).collect(toList());
                win1 = play(next1, next2, recurse);
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

    long hash(List<Integer> a, List<Integer> b) {
        return ((long) a.hashCode()) << 32 | b.hashCode();
    }

    long score(Collection<Integer> winner) {
        long found = 0;
        int i = winner.size();
        for (int c : winner) {
            found += (i--) * c;
        }
        return found;
    }
}
