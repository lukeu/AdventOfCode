package aoc2020;

import java.nio.ByteBuffer;
import java.util.List;

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
        byte[] bytes = input.bytes(this);
        var bb = ByteBuffer.wrap(bytes);
        while (bb.hasRemaining()) {
            byte b;
            int i;

            i = bb.get();
            while (i == '\n') {
                if (bb.hasRemaining()) {
                    i = bb.get();
                } else {
                    return;
                }
            }
            i -= '0';
            b = bb.get();
            int min = (b == '-') ? i : i * 10 + b - '0' + bb.get() - '-';

            i = bb.get() - '0';
            b = bb.get();
            int max = (b == ' ') ? i : i * 10 + b - '0' + bb.get() - ' ';

            byte ch = bb.get();
            int j = bb.position();
            bb.get(); bb.get();

            int count = 0;
            for (int k = j+2; k < bytes.length; k++) {
                byte bk = bb.get();
                if (bk == ch) {
                    count++;
                } else if (bk == '\n') { // will safely skip over '\r'
                    break;
                }
            }
            if (count >= min && count <= max) {
                part1 ++;
            }
            if (bytes[min + j + 1] == ch ^ bytes[max + j + 1] == ch) {
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
