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

    int width, len;
    char[] grid;
    char[] prev;
    int[][] visible;

    @Override
    public void parse(Input input) {
        var in = input.lines();

        len = in.size();
        width = in.get(0).length();
        grid = new char[in.size() * width];
        int n = 0;
        for (String string : in) {
            for (int i = 0; i < string.length(); i++) {
                grid[n++] = string.charAt(i);
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

    private static final int[] nyarp = new int[0];

    int[] findVisibleSeatIndexes(int x, int y) {
        char c = at(x,y);
        if (c == '.') {
            return nyarp;
        }
        int[] results = new int[8];
        int i = 0;
        for (int dy = -1; dy < 2; dy++) {
            for (int dx = -1; dx < 2; dx++) {
                if (dx != 0 || dy != 0) {
                    for (int m = 1; m < width -1 ; m++) {
                        char ch = at(x+dx*m, y+dy*m);
                        if (ch != '.') {
                            if (ch != '+') {
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
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < len; y++) {
                char seat = rules(x,y);
                grid[y*width + x] = seat;
                if (seat == '#') {
                    count++;
                }
            }
        }
        return count;
    }

    char rules(int x, int y) {
        char c = prev[y*width + x];
        int occupied = 0;
        for (int seat : visible[y*width + x]) {
            char ch = prev[seat];
            if (ch == '#') {
                if (c == 'L') {
                    return 'L';
                }
                occupied ++;
            }
        }
        if (c == 'L' && occupied == 0) {
            return '#';
        }
        if (c == '#' && occupied >= 5) {
            return 'L';
        }
        return c;
    }

    private char at(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= len) {
            return '+';
        }
        return prev[y*width + x];
    }
}
