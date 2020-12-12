package aoc2020;

import java.util.List;

import framework.AocMeta;
import framework.Base;
import framework.Input;
import util.Bot;

@AocMeta(notes = "grid navigation")
public class Day12_RainRisk extends Base {
    public static void main(String[] args) {
        Base.run(Day12_RainRisk::new, 1);
    }

    @Override public Object expect2() { return 42495; }

    List<String> in;

    @Override
    public void parse(Input input) {
        in = input.lines();
    }

    @Override
    public Integer part2() {
        Bot way = new Bot(); // Kept from part 1, made into the waypoint
        way.x = 10;
        way.y = -1;

        int x = 0,y = 0;
        for (String str : in) {
            char ch = str.charAt(0);
            int val = Integer.parseInt(str.substring(1));
            if (ch == 'L') {
                switch (val) {
                    case 270: way.leftOrigin();
                    case 180: way.leftOrigin();
                    case 90: way.leftOrigin();
                    break;
                    default: System.out.println("left problem");
                }
                continue;
            }
            if (ch == 'R') {
                switch (val) {
                    case 270: way.rightOrigin();
                    case 180: way.rightOrigin();
                    case 90: way.rightOrigin();
                    break;
                    default: System.out.println("right problem");
                }
                continue;
            }
            switch (ch) {
                case 'F' -> { x += way.x*val; y += way.y*val; }
                case 'N' -> way.y = way.y - val;
                case 'S' -> way.y = way.y + val;
                case 'W' -> way.x = way.x - val;
                case 'E' -> way.x = way.x + val;
                default -> System.out.println("move problem");
            }
        }

        return Math.abs(x) + Math.abs(y);
    }
}
