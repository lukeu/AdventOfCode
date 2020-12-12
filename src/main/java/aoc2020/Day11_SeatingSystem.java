package aoc2020;

import java.util.Arrays;

import framework.Base;
import framework.Input;

public class Day11_SeatingSystem extends Base {
    public static void main(String[] args) {
        Base.run(Day11_SeatingSystem::new, 1);
    }

    @Override public Object expect2() { return 2131; }

    int width, len;
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

        prev = Arrays.copyOf(grid, grid.length);

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

    public enum Dir {
        E(1,0),
        S(0,1),
        W(-1,0),
        N(0,-1),
        NW(-1,-1),
        NE(1, -1),
        SW(-1,1),
        SE(1,1);
        private Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
        public int dx;
        public int dy;
    }

    private char rules(int x, int y) {
        int occupied = 0;
        for (Dir d : Dir.values()) {
            for (int i = 1; i < width; i++) {
                int nx = x + i * d.dx;
                int ny = y + i * d.dy;
                if (at(nx,ny) != '.') {
                    if (at(nx, ny) == '#') {
                        occupied ++;
                    }
                    break;
                }
            }
        }
        /*
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i != 0 || j != 0) {
                    if (at(x+i, y+j) == '#') {
                        occupied ++;
                    }
                }
            }
        }
        */
        char ch = at(x, y);
        if (ch == 'L' && occupied == 0) {
            return '#';
        }
        if (ch == '#' && occupied >= 5) {
            return 'L';
        }
        return ch;
    }

    private char at(int x, int y) {
        int index = y*width + x;
        if (x < 0 || x >= width || y < 0 || index >= prev.length) {
            return '.';
        }
        return prev[index];
    }
}
