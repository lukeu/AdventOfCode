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
            int min = Integer.parseInt(s, 0, i, 10);
            int max = Integer.parseInt(s, i+1, j-2, 10);
            char ch = s.charAt(j-1);

            int count = 0;
            for (int k = j+2; k < s.length(); k++) {
                if (s.charAt(k) == ch) {
                    count++;
                }
            }
            if (count >= min && count <= max) {
                part1 ++;
            }
            if (s.charAt(min + j + 1) == ch ^ s.charAt(max + j + 1) == ch) {
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
