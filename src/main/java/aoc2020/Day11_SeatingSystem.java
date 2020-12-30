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

    @Override public Object expect2() { return 2131; }

    private static final int EMPTY = 0, FLOOR = 1, WALL = 2, TAKEN = 100;

    int width, len;
    int[] grid;
    int[] prev;
    int[][] visible;

    @Override
    public void parse(Input input) {
        var in = input.lines();

        len = in.size();
        width = in.get(0).length();
        grid = new int[in.size() * width];
        int n = 0;
        for (String string : in) {
            for (int i = 0; i < string.length(); i++) {
                grid[n++] = string.charAt(i) == '.' ? FLOOR : EMPTY;
            }
        }
    }

    @Override
    public Integer part2() {
        int found = 0;
        prev = Arrays.copyOf(grid, grid.length);

        visible = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            visible[i] = findVisibleSeatIndexes(i % width, i / width);
        }

        do {
            prev = Arrays.copyOf(grid, grid.length);
            found = iterate();
        } while (!Arrays.equals(prev, grid));
        return found;
    }

    int[] findVisibleSeatIndexes(int x, int y) {
        int c = at(x,y);
        if (c == FLOOR) {
            return null;
        }
        int[] results = new int[8];
        int i = 0;
        for (int dy = -1; dy < 2; dy++) {
            for (int dx = -1; dx < 2; dx++) {
                if (dx != 0 || dy != 0) {
                    for (int m = 1; m < width -1 ; m++) {
                        int ch = at(x+dx*m, y+dy*m);
                        if (ch != FLOOR) {
                            if (ch != WALL) {
                                results[i++] = (y+dy*m) * width + x+dx*m;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return i == 8 ? results : Arrays.copyOf(results, i);
    }

    int iterate() {
        int count = 0;
        for (int i = 0; i < prev.length; i++) {
            switch (prev[i]) {
                case TAKEN -> { checkLeave(i); count++; }
                case EMPTY -> checkTake(i);
            }
        }
        return count;
    }

    /** prev[i] is 'L'. See if grid[i] should be set to '#' */
    void checkTake(int i) {
        for (int seat : visible[i]) {
            if (prev[seat] == TAKEN) {
                return;
            }
        }
        grid[i] = TAKEN;
    }

    /** prev[i] is '#'. See if grid[i] should be set to 'L' */
    void checkLeave(int i) {

        // NB: this could be a 1-liner using Streams, but this compiles to faster code
        int occupied = 0;
        for (int seat : visible[i]) {
            occupied += prev[seat];
        }
        if (occupied >= 500) {
            grid[i] = EMPTY;
        }
    }

    private int at(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= len) {
            return WALL;
        }
        return prev[y*width + x];
    }
}
