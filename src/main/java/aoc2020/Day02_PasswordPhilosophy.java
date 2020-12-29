package aoc2020;

import framework.Base;
import framework.Input;

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
    public void parse(Input input) {
        for (String s : input.lines()) {
            int i = s.indexOf('-');
            int j = s.indexOf(':');
            int min = Integer.parseInt(s.substring(0, i));
            int max = Integer.parseInt(s.substring(i+1, j-2));
            char ch = s.charAt(j-1);
            String str = s.substring(j+2);

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
