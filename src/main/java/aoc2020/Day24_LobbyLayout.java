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
        W(-1,0)
        ;

        private Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
        int dx;
        int dy;
    }

    int SIZE = 140;
    int REF = SIZE / 2;
    boolean [] tiles = new boolean[SIZE*SIZE];

    @Override
    public void parse(String in) {
        for (String line : SUtils.lines(in)) {
            int x = REF;
            int y = REF;
            while (!line.isEmpty()) {
                for (Dir d : Dir.values()) {
                    if (line.startsWith(d.name().toLowerCase())) {
                        x += d.dx;
                        y += d.dy;
                        line = line.substring(d.name().length());
                    }
                }

            }
            tiles[y*SIZE + x] ^= true;
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
            boolean[] next = Arrays.copyOf(tiles, tiles.length);
            for (int y = 1; y < SIZE - 1; y++) {
                for (int x = 1; x < SIZE - 1; x++) {
                    int c = countAround(x, y);
                    if (tiles[y*SIZE + x]) {
                        if (c == 0 || c > 2) {
                            next[y*SIZE + x] = false;
                        }
                    } else {
                        if (c == 2) {
                            next[y*SIZE + x] = true;
                        }
                    }
                }
            }
            tiles = next;
        }


        return part1();
    }

    int countAround(int x, int y) {
        int c = 0;
        for (Dir d : Dir.values()) {
            if (tiles[(y+d.dy) * SIZE + (x + d.dx)]) {
                c++;
            }
        }
        return c;
    }
}
