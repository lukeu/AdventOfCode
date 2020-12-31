package aoc2020;

import java.util.Arrays;

import framework.AocMeta;
import framework.Base;
import framework.Input;

@AocMeta(notes = "cellular automa 1")
public class Day11_SeatingSystem extends Base {
    public static void main(String[] args) {
        Base.run(Day11_SeatingSystem::new, 1);
    }

    @Override
    public String testInput() {
        return    "L.LL.LL.LL\n"
                + "LLLLLLL.LL\n"
                + "L.L.L..L..\n"
                + "LLLL.LL.LL\n"
                + "L.LL.LL.LL\n"
                + "L.LLLLL.LL\n"
                + "..L.L.....\n"
                + "LLLLLLLLLL\n"
                + "L.LLLLLL.L\n"
                + "L.LLLLL.LL";
    }
    @Override public Object testExpect2() { return 26; }
    @Override public Object expect2() { return 2131; }

    private static final int EMPTY = 0, FLOOR = 1, WALL = 2, TAKEN = 100_000;

    int width, len;
    int[] grid;
    int[] prev;
    int[] visible;

    @Override
    public void parse(Input input) {
        var in = input.lines();

        len = in.size();
        width = in.get(0).length();
        grid = new int[len * width + 1];
        int n = 0;
        for (String string : in) {
            for (int i = 0; i < string.length(); i++) {
                grid[n++] = string.charAt(i) == '.' ? FLOOR : EMPTY;
            }
        }
    }

    @Override
    public Integer part2() {
        prev = Arrays.copyOf(grid, grid.length);

        visible = new int[len * width * 8];
        for (int i = 0; i < len * width; i++) {
            findVisibleSeatIndexes(visible, i % width, i / width);
        }

        do {
            prev = Arrays.copyOf(grid, grid.length);
            iterate();
        } while (!Arrays.equals(prev, grid));
        return Arrays.stream(grid).sum() / TAKEN;
    }

    void findVisibleSeatIndexes(int[] vis, int x, int y) {
        int c = at(x,y);
        if (c == FLOOR) {
            return; // Irrelevant - the corresponding range in vis will never be read
        }
        int start = (y * width + x) * 8;
        int i = 0;
        for (int dy = -1; dy < 2; dy++) {
            for (int dx = -1; dx < 2; dx++) {
                if (dx != 0 || dy != 0) {
                    for (int m = 1; m < width -1 ; m++) {
                        int ch = at(x+dx*m, y+dy*m);
                        if (ch != FLOOR) {
                            if (ch != WALL) {
                                vis[start + (i++)] = (y+dy*m) * width + x+dx*m;
                            }
                            break;
                        }
                    }
                }
            }
        }
        while (i < 8) {
            vis[start + (i++)] = width * len;
        }
    }

    void iterate() {
        for (int i = 0; i < prev.length - 1; i++) {
            switch (prev[i]) {
                case TAKEN -> { checkLeave(i); }
                case EMPTY -> checkTake(i);
            }
        }
    }

    /** prev[i] is 'L'. See if grid[i] should be set to '#' */
    void checkTake(int i) {
        if (sumVisible(i) < TAKEN) {
            grid[i] = TAKEN;
        }
    }

    /** prev[i] is '#'. See if grid[i] should be set to 'L' */
    void checkLeave(int i) {
        if (sumVisible(i) >= (5*TAKEN)) {
            grid[i] = EMPTY;
        }
    }

    private int sumVisible(int from) {
        int sum = 0;
        for (int i = 0; i < 8; ++i) {
            int seat = visible[from * 8 + i];
            sum += prev[seat];
        }
        return sum;
    }

    private int at(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= len) {
            return WALL;
        }
        return prev[y*width + x];
    }

    String print() {
        var sb = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            if (i > 0 && i % width == 0) {
                sb.append('\n');
            }
            sb.append(switch (grid[i]) {
                case EMPTY -> 'L';
                case TAKEN -> '#';
                default -> '.';
            });
        }
        return sb.toString();
    }
}
