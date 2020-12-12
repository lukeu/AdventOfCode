package aoc2020;

import java.util.Arrays;
import java.util.List;

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
    List<String> in;
    char[] grid;
    char[] prev;

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
        do {
            prev = Arrays.copyOf(grid, grid.length);
            iterate();
        } while (!Arrays.equals(prev, grid));

        int found = 0;
        for (char c : grid) {
            if (c == '#') {
                found++;
            }
        }
        return found;
    }

    private void iterate() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < len; y++) {
                char seat = rules(x,y);
                grid[y*width + x] = seat;
            }
        }
    }

    private char rules(int x, int y) {
        char c = at(x,y);
        if (c == '.') {
            return '.';
        }
        int occupied = 0;
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                if (dx != 0 || dy != 0) {
                    for (int m = 1; m < width; m++) {
                        char ch = at(x+dx*m, y+dy*m);
                        if (ch == '#') {
                            if (c == 'L') {
                                return 'L';
                            }
                            occupied ++;
                        }
                        if (ch != '.') {
                            break;
                        }
                    }
                }
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
