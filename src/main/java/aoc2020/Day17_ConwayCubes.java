package aoc2020;

import java.util.Arrays;

import framework.AocMeta;
import framework.Base;
import framework.Input;

@AocMeta(notes = "part 2 only")
public class Day17_ConwayCubes extends Base {
    public static void main(String[] args) {
        Base.run(Day17_ConwayCubes::new, 1);
    }

    private static final int SIZE = 23;
    boolean[] vol = new boolean[SIZE * SIZE * SIZE * SIZE];
    int[] min = new int[4], max = new int[4];

    @Override
    public String testInput() {
        return ".#.\n"
             + "..#\n"
             + "###\n";
    }
    @Override public Object testExpect2() { return 848L; }
    @Override public Object expect2() { return 1868L; }

    @Override
    public void parse(Input input) {
        Arrays.fill(min, SIZE);
        Arrays.fill(max, -1);
        var in = input.lines();
        int y = 0;
        for (String line : in) {
            int off = SIZE / 2 - line.length() / 2 + 1;
            for (int x = 0; x < line.length(); x++) {
                if ('#' == line.charAt(x)) {
                    set(x + off, y + off, SIZE/2, SIZE/2, true);
                    updateRanges(x + off, y + off, SIZE/2, SIZE/2);
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

        int[] dmin = Arrays.copyOf(min, min.length);
        int[] dmax = Arrays.copyOf(max, max.length);

        for (int w = dmin[3]-1; w <= dmax[3]+1; w++) {
            for (int z = dmin[2]-1; z <= dmax[2]+1; z++) {
                for (int y = dmin[1]-1; y <= dmax[1]+1; y++) {
                    for (int x = dmin[0]-1; x <= dmax[0]+1; x++) {
                        int count = countAround(old, x, y, z, w);
                        if (at(x, y, z, w)) {
                            if (count < 2 || count > 3) {
                                set(x, y, z, w, false);
                            }
                        } else {
                            if (count == 3) {
                                updateRanges(x, y, z, w);
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
                        if (old[index(px + x, py + y, pz + z, pw + w)]) {
                            count++;
                        }
                    }
                }
            }
        }
        return old[index(px, py, pz, pw)] ? count - 1 : count;
    }

    private void updateRanges(int... vv) {
        assert vv.length == 4;
        for (int i = 0; i < vv.length; i++) {
            if (vv[i] < min[i]) min[i] = vv[i];
            if (vv[i] > max[i]) max[i] = vv[i];
            if (min[i] < 2 || max[i] >= SIZE-2) {
                throw new IllegalStateException("OoB");
            }
        }
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
        for (int w = min[3]; w <= max[3]; w++) {
            for (int z = min[2]; z <= max[2]; z++) {
                System.out.println("\nZ=" + z + ", W=" + w);
                for (int y = min[1]; y <= max[1]; y++) {
                    for (int x = min[0]; x <= max[0]; x++) {
                        System.out.print(at(x, y, z, w) ? '#' : '.');
                    }
                    System.out.println();
                }
            }
        }
    }

}
