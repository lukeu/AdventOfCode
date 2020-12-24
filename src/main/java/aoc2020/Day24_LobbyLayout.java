package aoc2020;

import java.util.Arrays;

import framework.AocMeta;
import framework.Base;
import util.SUtils;

@AocMeta(notes = "cellular automa 3")
public class Day24_LobbyLayout extends Base {
    public static void main(String[] args) {
        Base.run(Day24_LobbyLayout::new, 1);
    }

    @Override
    public String testInput() {
        return "sesenwnenenewseeswwswswwnenewsewsw\n"
                + "neeenesenwnwwswnenewnwwsewnenwseswesw\n"
                + "seswneswswsenwwnwse\n"
                + "nwnwneseeswswnenewneswwnewseswneseene\n"
                + "swweswneswnenwsewnwneneseenw\n"
                + "eesenwseswswnenwswnwnwsewwnwsene\n"
                + "sewnenenenesenwsewnenwwwse\n"
                + "wenwwweseeeweswwwnwwe\n"
                + "wsweesenenewnwwnwsenewsenwwsesesenwne\n"
                + "neeswseenwwswnwswswnw\n"
                + "nenwswwsewswnenenewsenwsenwnesesenew\n"
                + "enewnwewneswsewnwswenweswnenwsenwsw\n"
                + "sweneswneswneneenwnewenewwneswswnese\n"
                + "swwesenesewenwneswnwwneseswwne\n"
                + "enesenwswwswneneswsenwnewswseenwsese\n"
                + "wnwnesenesenenwwnenwsewesewsesesew\n"
                + "nenewswnwewswnenesenwnesewesw\n"
                + "eneswnwswnwsenenwnwnwwseeswneewsenese\n"
                + "neswnwewnwnwseenwseesewsenwsweewe\n"
                + "wseweeenwnesenwwwswnew";
    }
    @Override public Object testExpect1() { return 10L; }
    @Override public Object testExpect2() { return 2208L; }
    @Override public Object expect1() { return 289L; }
    @Override public Object expect2() { return 3551L; }

    enum Dir {
        SE(1,1),
        SW(0,1),
        NE(0,-1),
        NW(-1,-1),
        E(1,0),
        W(-1,0);
        private Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
        final int dx;
        final int dy;
    }

    private static final int SIZE = 140;
    private static final int REF = SIZE / 2;
    boolean[] tiles = new boolean[SIZE*SIZE];
    int minx, miny, maxx, maxy;

    @Override
    public void parse(String in) {
        minx = maxx = miny = maxy = REF;
        for (String line : SUtils.lines(in)) {
            int x = REF;
            int y = REF;
            line = line.toUpperCase();
            while (!line.isEmpty()) {
                for (Dir d : Dir.values()) {
                    if (line.startsWith(d.name())) {
                        x += d.dx;
                        y += d.dy;
                        line = line.substring(d.name().length());
                    }
                }
            }
            updateRange(x, y);
            tiles[at(x, y)] ^= true;
        }
    }

    private void updateRange(int x, int y) {
        if (x < minx) minx = x;
        if (x > maxx) maxx = x;
        if (y < miny) miny = y;
        if (y > maxy) maxy = y;
        if (minx < 2 || maxx >= SIZE-2 || miny < 2 || maxy >= SIZE-2) {
            throw new IllegalStateException("OoB");
        }
    }

    @Override
    public Long part1() {
        long found = 0;
        for (boolean b : tiles) {
            if (b) found++;
        }
        return found;
    }

    @Override
    public Long part2() {
        for (int n = 0; n < 100; n++) {
            tiles = iterate();
        }
        return part1();
    }

    private boolean[] iterate() {
        boolean[] next = Arrays.copyOf(tiles, tiles.length);
        int x1 = minx - 1;
        int x2 = maxx + 1;
        int y1 = miny - 1;
        int y2 = maxy + 1;
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                int c = countAround(x, y);
                if (tiles[at(x, y)]) {
                    if (c == 0 || c > 2) {
                        next[at(x, y)] = false;
                    }
                } else {
                    if (c == 2) {
                        updateRange(x, y);
                        next[at(x, y)] = true;
                    }
                }
            }
        }
        return next;
    }

    int countAround(int x, int y) {
        int c = 0;
        for (Dir d : Dir.values()) {
            if (tiles[at(x + d.dx, y + d.dy)]) {
                c++;
            }
        }
        return c;
    }

    private int at(int x, int y) {
        return y*SIZE + x;
    }
}
