package aoc2020;

import framework.Base;
import util.SUtils;

public class Day25_ComboBreaker extends Base {
    public static void main(String[] args) {
        Base.run(Day25_ComboBreaker::new, 1);
    }

    @Override
    public String testInput() {
        return "5764801\n" +
               "17807724";
    }
    @Override public Object testExpect1() { return 14897079L; }
    @Override public Object expect1() { return 296776L; }

    long cpk;
    long dpk;

    @Override
    public void parse(String in) {
        long[] longs = SUtils.lineLongs(in);
        cpk = longs[0];
        dpk = longs[1];
    }

    @Override
    public Long part1() {
        int cls = findLoopSize(cpk);
//        int dls = findLoopSize(dpk);
        long ek = transform(dpk, cls);
        //ek = transform(cpk, dls);
        return ek;
    }

    private int findLoopSize(long target) {
        int trial = 0;
        long v = 1;
        while (v != target) {
            v = v * 7;
            v = v % 20201227;
            trial ++;
        }
        return trial;
    }

    long transform(long subject, long loop) {
        long v = 1;
        for (int i = 0; i < loop; i++) {
            v = v * subject;
            v = v % 20201227;
        }
        return v;
    }
}
