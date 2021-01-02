package aoc2020;

import framework.Base;
import framework.Input;
import util.ByteBiter;

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
        var bb = new ByteBiter(input.bytes(this));
        while (bb.hasRemaining()) {
            int min = bb.positiveInt();
            bb.skip();
            int max = bb.positiveInt();
            bb.skip();
            byte ch = bb.get();
            int startChars = bb.pos + 1;

            if (scanLinePart1(bb, min, max, ch)) {
                part1 ++;
            }
            if (bb.bytes[min + startChars] == ch ^ bb.bytes[max + startChars] == ch) {
                part2 ++;
            }
        }
    }

    /** Performs much faster than the 1-line Stream incantation. */
    private boolean scanLinePart1(ByteBiter bb, int min, int max, int ch) {
        int count = 0;
        int k;
        for (k = bb.pos + 2; k < bb.bytes.length; k++) {
            byte bk = bb.bytes[k];
            if (bk == ch) {
                count++;
            } else if (bk == '\n') { // will safely skip over '\r'
                break;
            }
        }
        bb.pos = k + 1;
        return count >= min && count <= max;
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
