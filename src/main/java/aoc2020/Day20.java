package aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import util.FUtils;
import util.Util;

public class Day20 {
    public static void main(String[] args) {
        Util.profile(() -> new Day20().go(), 1);
    }

    record Tile(int id, int rot, boolean flip, char[][] grid) {

        @Override
        public String toString() {
            return String.format("tile %s [rot %s, %s] \n%s\n", id, rot, flip,
                Arrays.stream(grid).map(c -> new String(c)).collect(Collectors.joining("\n")));
        }
    }

    private static final int WIDTH = 12;

    List<List<Tile>> tiles = new ArrayList<>();
    ArrayList<Tile> table = new ArrayList<>();
    HashSet<Integer> placed = new HashSet<>();

    void go() {
        long found = 0;
        var in = FUtils.splitLines(2020, 20, "\n\n");
        for (String str : in) {
            int num = Integer.parseInt(str.substring(5,9));
            char[][] grid = Arrays.stream(str.trim().split("\n"))
                    .skip(1)
                    .map(s -> s.toCharArray())
                    .toArray(char[][]::new);
            List<Tile> orientations = new ArrayList<>();
            boolean flip = false;
            for (int f = 0; f < 2; f++) {
                for (int r = 0; r < 4; r++) {
                    Tile tile = new Tile(num, r, flip, grid);
                    orientations.add(tile);
                    grid = rotate(grid);
                }
                grid = flip(grid);
                flip = true;
            }
            tiles.add(orientations);
        }
        System.out.println("Tiles: " + tiles.size());
        System.out.println("Found: " + found);

        part1();
        part2();
    }

    void part1() {

        for (int c = 0; c < WIDTH*WIDTH; c++) {
            for (int o = 0; o < 8; o++) {
                if (place(c, o)) {
                    System.out.println("Part1 : " + ((long) table.get(0).id
                         * table.get(WIDTH-1).id
                         * table.get(table.size()-1).id
                         * table.get(table.size()-WIDTH).id));
                    return;
                }
                placed.clear();
            }
        }
        System.out.println("P1 failed");
    }

    private boolean place(int card, int orient) {
        Tile next = tiles.get(card).get(orient);
        int row = table.size() / WIDTH;
        int col = table.size() % WIDTH;
        if (row > 0) {
            Tile above = table.get(table.size() - WIDTH);
            if (!checkAbove(above, next)) {
                return false;
            }
        }
        if (col > 0) {
            Tile left = table.get(table.size() - 1);
            if (!checkLeft(left, next)) {
                return false;
            }
        }

        table.add(next);
        placed.add(card);
        if (table.size() == WIDTH*WIDTH) {
            return true;
        }
        for (int c = 0; c < WIDTH*WIDTH; c++) {
            if (placed.contains(c)) {
                continue;
            }
            for (int o = 0; o < 8; o++) {
                if (place(c, o)) {
                    return true;
                }
            }
        }
        table.remove(table.size() - 1);
        return false;
    }

    private boolean checkAbove(Tile above, Tile next) {
        for (int i = 0; i < 10; i++) {
            if (above.grid[9][i] != next.grid[0][i]) {
                return false;
            }
        }
        return true;
    }

    private boolean checkLeft(Tile left, Tile next) {
        for (int i = 0; i < 10; i++) {
            if (left.grid[i][9] != next.grid[i][0]) {
                return false;
            }
        }
        return true;
    }

    private static final char[][] MONSTER = Arrays.stream(new String[]{
            "                  # ",
            "#    ##    ##    ###",
            " #  #  #  #  #  #   "
    }).map(s -> s.toCharArray()).toArray(char[][]::new);

    void part2() {
        var image = createImage();

        for (int f = 0; f < 2; f++) {
            for (int r = 0; r < 4; r++) {

                boolean matched = false;
                for (int y = 0; y <= (8*WIDTH)-3; y++) {
                    for (int x = 0; x < (8*WIDTH) - MONSTER[0].length + 1; x++) {
                        if (match(image, y, x)) {
                            matched = true;
                            remove(image, y, x);
                        }
                    }
                }

                if (matched) {
                    System.out.println(
                            Arrays.stream(image)
                            .map(c -> new String(c))
                            .collect(Collectors.joining("\n")));
                    System.out.print("Roughness: ");
                    System.out.println(
                            Arrays.stream(image)
                            .flatMapToInt(c -> new String(c).chars())
                            .filter(c -> (c == '#'))
                            .count());
                    return;
                }
                image = rotate(image);
            }
            image = flip(image);
        }
    }

    private boolean match(char[][] image, int y, int x) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < MONSTER[0].length; j++) {
                if (MONSTER[i][j] == '#') {
                    if (image[y+i][x+j] != '#') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean remove(char[][] image, int y, int x) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < MONSTER[0].length; j++) {
                if (MONSTER[i][j] == '#') {
                    image[y+i][x+j] = 'O';
                }
            }
        }
        return true;
    }

    private char[][] createImage() {
        char[][] image = new char[8*WIDTH][];
        int line = 0;
        for (int y = 0; y < WIDTH; y++) {
            for (int i = 1; i <= 8; i++) {
                image[line] = new char[8*WIDTH];
                for (int x = 0; x < WIDTH; x++) {
                    Tile tile = table.get(x*WIDTH + y); // SEEMS WRONG? Needed to match example
                    for (int j = 1; j <= 8; j++) {
                        image[line][x*8+j-1] = tile.grid[j][i];
                    }
                }
                line++;
            }
        }
        return image;
    }

    private char[][] rotate(char[][] grid) {
        int size = grid.length;
        char[][] copy = new char[size][];
        for (int y = 0; y < size; y++) {
            copy[y] = new char[size];
            for (int x = 0; x < size; x++) {
                copy[y][x] = grid[x][size-y-1];
            }
        }
        return copy;
    }

    private char[][] flip(char[][] grid) {
        int size = grid.length;
        char[][] copy = new char[size][];
        for (int y = 0; y < size; y++) {
            copy[y] = new char[size];
            for (int x = 0; x < size; x++) {
                copy[y][x] = grid[y][size-x-1];
            }
        }
        return copy;
    }
}
