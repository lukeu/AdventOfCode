package aoc2020;

import java.util.Arrays;

import framework.AocMeta;
import framework.Base;
import util.SUtils;

@AocMeta(notes = "part 2 only")
public class Day17_ConwayCubes extends Base {
    public static void main(String[] args) {
        Base.run(Day17_ConwayCubes::new, 1);
    }

    private static final int SIZE = 22;
    boolean[] vol = new boolean[SIZE * SIZE * SIZE * SIZE];

    @Override
    public String testInput() {
        return ".#.\n"
             + "..#\n"
             + "###\n";
    }
    @Override public Object testExpect2() { return 848L; }
    @Override public Object expect2() { return 1868L; }

    @Override
    public void parse(String text) {
        var in = SUtils.lines(text);
        int y = 0;
        for (String line : in) {
            int off = SIZE / 2 - line.length() / 2;
            for (int x = 0; x < line.length(); x++) {
                if ('#' == line.charAt(x)) {
                    set(x + off, y + off, off, off, true);
                }
            }
            y++;
        }
    }

    @Override
    public Long part2() {
        for (int i = 0; i < 6; i++) {
            iterate();
        }
        return (long) countFinal();
    }

    private void iterate() {
        boolean[] old = Arrays.copyOf(vol, vol.length);

        for (int w = 1; w < SIZE - 1; w++) {
            for (int z = 1; z < SIZE - 1; z++) {
                for (int y = 1; y < SIZE - 1; y++) {
                    for (int x = 1; x < SIZE - 1; x++) {
                        int count = countAround(old, x, y, z, w);
                        if (at(x, y, z, w)) {
                            if (count < 2 || count > 3) {
                                set(x, y, z, w, false);
                            }
                        } else {
                            if (count == 3) {
                                set(x, y, z, w, true);
                            }
                        }
                    }
                }
            }
        }
    }

    private int countAround(boolean[] old, int px, int py, int pz, int pw) {
        int count = 0;
        for (int w = -1; w <= 1; w++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 1; y++) {
                    for (int x = -1; x <= 1; x++) {
                        if (x == 0 && y == 0 && z == 0 && w == 0) {
                            continue;
                        }
                        if (old[index(px + x, py + y, pz + z, pw + w)]) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private boolean at(int x, int y, int z, int w) {
        return vol[index(x, y, z, w)];
    }

    private void set(int x, int y, int z, int w, boolean b) {
        vol[index(x, y, z, w)] = b;
    }

    private int index(int x, int y, int z, int w) {
        return w * SIZE * SIZE * SIZE + z * SIZE * SIZE + y * SIZE + x;
    }

    private int countFinal() {
        int count = 0;
        for (boolean b : vol) {
            if (b)
                count++;
        }
        return count;
    }

    @SuppressWarnings("unused")
    private void printCube() {
        for (int w = 0; w < SIZE; w++) {
            for (int z = 0; z < SIZE; z++) {
                if (hasPoint(z, w)) {
                    System.out.println("\nZ=" + z + ", W=" + w);
                    for (int y = 1; y < SIZE-1; y++) {
                        for (int x = 1; x < SIZE-1; x++) {
                            System.out.print(at(x, y, z, w) ? '#' : '.');
                        }
                        System.out.println();
                    }
                }
            }
        }
    }

    private boolean hasPoint(int z, int w) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (at(x, y, z, w)) {
                    return true;
                }
            }
        }
        return false;
    }
}
