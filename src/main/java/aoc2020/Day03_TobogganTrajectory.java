package aoc2020;

import com.google.common.primitives.Bytes;
import framework.Base;
import framework.Input;
import util.ByteBiter;

public class Day03_TobogganTrajectory extends Base {
    public static void main(String[] args) {
        Base.run(Day03_TobogganTrajectory::new, 1);
    }

    @Override public Object expect1() { return 240L; }
    @Override public Object expect2() { return 2832009600L; }

    long[] rows;
    int w;

    @Override
    public void parse(Input input) {
        var bb = new ByteBiter(input.bytes(this));
        w = Bytes.indexOf(bb.bytes, (byte) '\n');
        rows = new long[(bb.bytes.length + 1) / (w+1)]; // 1st +1 in case no \n @ EOF, 2nd for EOLs
        int r = 0;
        while (bb.hasRemaining()) {
            rows[r++] = bb.readAsBinary((byte) '.', (byte) '#', w);
            if (bb.hasRemaining()) {
                bb.get();
            }
        }
    }

    @Override
    public Object part1() {
        return slopeHits(1, 3);
    }

    @Override
    public Object part2() {
        long total = 1;
        for (int dx : new int []{1,3,5,7}) {
            total *= slopeHits(1, dx);
        }
        return total * slopeHits(2, 1);
    }

    private long slopeHits(int dy, int dx) {
        int x = w-1;
        int found = 0;
        for (int r = 0; r < rows.length; r += dy) {
            var bs = rows[r];
            if (isBitSet(bs, x)) {
                found++;
            }
            x -= dx;
            if (x < 0) { // branch quicker than %?
                x += w;
            }
        }
        return found;
    }

    private boolean isBitSet(long value, int bit) {
        return (value & (1L << bit)) != 0;
    }
}
