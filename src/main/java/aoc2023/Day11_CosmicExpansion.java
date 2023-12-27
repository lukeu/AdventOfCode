package aoc2023;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import framework.Base;
import framework.Input;

public class Day11_CosmicExpansion extends Base {
    public static void main(String[] args) {
        Base.run(Day11_CosmicExpansion::new, 1);
    }

    @Override
    public String testInput() {
        return
"""
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
""";
    }
    @Override public Object testExpect1() { return 374; }
    @Override public Object testExpect2() { return 82000210; }

    record G(int r, int c) {}

    BitSet hitCol = new BitSet(150);
    BitSet hitRow = new BitSet(150);
    List<G> init = new ArrayList<>();

    /** Times parses separately for optimisation runs. Can ignore when speed-coding.  */
    @Override
    public void parse(Input in) {
        int r = 0, c = 0;
        for (var line : in.lines()) {
            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);
                if (ch != '.') {
                    hitRow.set(r);
                    hitCol.set(c);
                    init.add(new G(r, c));
                }
                ++ c;
            }
            ++ r; c = 0;
        }
    }

    @Override
    public Long part1() {
        return accumulate(expand(1));
    }

    @Override
    public Long part2() {
        return accumulate(expand(999_999));
    }

    List<G> expand(int factor) {
        List<G> galaxies = new ArrayList<>();
        for (G g : init) {
            galaxies.add(new G(
                    g.r + factor * getExpansion(g.r, hitRow),
                    g.c + factor * getExpansion(g.c, hitCol)));
        }
        return galaxies;
    }

    static long accumulate(List<G> galaxies) {
        long found = 0;
        for (G a : galaxies) {
            for (G b : galaxies) {
                found += (Math.abs(a.r - b.r) + Math.abs(a.c - b.c));
            }
        }
        return found / 2;
    }

    static int getExpansion(int pos, BitSet hits) {
        int result = 0;
        for (int i = 0; i < pos; i++) {
            if (!hits.get(i)) {
                ++ result;
            }
        }
        return result;
    }
}
