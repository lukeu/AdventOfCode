package aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import framework.AocMeta;
import framework.Base;
import util.FUtils;
import util.SUtils;

@AocMeta(notes = "monochrome image processing")
public class Day20_JurassicJigsaw extends Base {
    public static void main(String[] args) {
        Base.run(Day20_JurassicJigsaw::new, 1);
    }

    record Tile(int id, int rot, boolean flip, char[][] grid) {

        @Override
        public String toString() {
            return String.format("tile %s [rot %s, %s] \n%s\n", id, rot, flip,
                Arrays.stream(grid).map(c -> new String(c)).collect(Collectors.joining("\n")));
        }
    }

    @Override
    public String testInput() {
        return FUtils.readIfExists("" + year() + "/test_" + day() + ".txt");
    }
    @Override public Object testExpect1() { return 20899048083289L; }
    @Override public Object testExpect2() { return 273L; }
    @Override public Object expect1() { return 32287787075651L; }
    @Override public Object expect2() { return 1939L; }

    int width;
    List<List<Tile>> tiles = new ArrayList<>();
    ArrayList<Tile> table = new ArrayList<>();
    HashSet<Integer> placed = new HashSet<>();

    @Override
    public void parse(String text) {
        List<String> blocks = SUtils.blocks(text);
        width = (int) Math.pow(blocks.size(), 0.5);
        for (String str : blocks) {
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
    }

    @Override
    public Object part1() {
        for (int c = 0; c < width*width; c++) {
            for (int o = 0; o < 8; o++) {
                if (place(c, o)) {
                    return (long) table.get(0).id
                         * table.get(width-1).id
                         * table.get(table.size()-1).id
                         * table.get(table.size()-width).id;
                }
                placed.clear();
            }
        }
        return "P1 failed";
    }

    private boolean place(int card, int orient) {
        Tile next = tiles.get(card).get(orient);
        int row = table.size() / width;
        int col = table.size() % width;
        if (row > 0) {
            Tile above = table.get(table.size() - width);
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
        if (table.size() == width*width) {
            return true;
        }
        for (int c = 0; c < width*width; c++) {
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

    @Override
    public Object part2() {
        var image = createImage();

        for (int f = 0; f < 2; f++) {
            for (int r = 0; r < 4; r++) {

                boolean matched = false;
                for (int y = 0; y <= (8*width)-3; y++) {
                    for (int x = 0; x < (8*width) - MONSTER[0].length + 1; x++) {
                        if (match(image, y, x)) {
                            matched = true;
                            remove(image, y, x);
                        }
                    }
                }

                if (matched) {
//                    System.out.println(
//                            Arrays.stream(image)
//                            .map(c -> new String(c))
//                            .collect(Collectors.joining("\n")));
                    return Arrays.stream(image)
                            .flatMapToInt(c -> new String(c).chars())
                            .filter(c -> (c == '#'))
                            .count();
                }
                image = rotate(image);
            }
            image = flip(image);
        }
        return "P2 Failed";
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
        char[][] image = new char[8*width][];
        int line = 0;
        for (int y = 0; y < width; y++) {
            for (int i = 1; i <= 8; i++) {
                image[line] = new char[8*width];
                for (int x = 0; x < width; x++) {
                    Tile tile = table.get(x*width + y); // SEEMS WRONG? Needed to match example
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
