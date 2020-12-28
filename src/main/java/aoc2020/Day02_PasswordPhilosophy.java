package aoc2020;

import java.util.regex.Pattern;

import framework.Base;
import util.SUtils;

public class Day02_PasswordPhilosophy extends Base {

    public static void main(String[] args) {
        Base.run(Day02_PasswordPhilosophy::new, 1);
    }

    @Override
    public String testInput() {
        return "1-3 a: abcde\n"
                + "1-3 b: cdefg\n"
                + "2-9 c: ccccccccc";
    }

    @Override public Object testExpect1() { return 2; }
    @Override public Object testExpect2() { return 1; }
    @Override public Object expect1() { return 645; }
    @Override public Object expect2() { return 737; }

    // Best for speed-coding: (but performs poorly)
    //
    //    var scan = new Scanner(s);
    //    scan.useDelimiter("[-: ]+");
    //    int min = scan.nextInt();

    int part1 = 0;
    int part2 = 0;

    @Override
    public void parse(String text) {
        var pattern = Pattern.compile("[-: ]+");
        for (String s : SUtils.lines(text)) {

            String[] sp = pattern.split(s);
            int min = Integer.parseInt(sp[0]);
            int max = Integer.parseInt(sp[1]);
            char ch = sp[2].charAt(0);
            String str = sp[3];

            int count = (int) str.chars().filter(c -> c == ch).count();
            if (count >= min && count <= max) {
                part1 ++;
            }
            if (str.charAt(min-1) == ch ^ str.charAt(max-1) == ch) {
                part2 ++;
            }
        }
    }

    @Override
    public Object part1() {
        return part1;
    }

    @Override
    public Object part2() {
        return part2;
    }
}
