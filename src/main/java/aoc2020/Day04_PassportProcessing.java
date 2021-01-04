package aoc2020;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.primitives.Ints;
import framework.AocMeta;
import framework.Base;
import framework.Input;
import util.ByteBiter;

@AocMeta(notes = "input validation")
public class Day04_PassportProcessing extends Base {
    public static void main(String[] args) {
        Base.run(Day04_PassportProcessing::new, 1);
    }

    @Override public Object expect1() { return 254; }
    @Override public Object expect2() { return 184; }

    private List<Object[]> passports = new ArrayList<>();

    @Override
    public void parse(Input input) {
        boolean valid = true;
        var pp = new Object[8];
        var bb = new ByteBiter(input.bytes(this));
        while (bb.hasRemaining()) {
            int key = bb.getBinaryInt();
            int index = index(key);
            Object value = (index >= 0 && index <= 2)
                    ? bb.positiveInt() // NB: nasty assumption about input made here
                    : bb.extractToWhitespace();
            if (index >= 0) {
                pp[index] = value;
            } else {
                valid = false;
            }
            if (bb.hasRemaining() && bb.get() == '\n') {
                if (bb.hasRemaining() && bb.peek() == '\n') {
                    bb.get();
                    if (valid) {
                        addIfValid(pp);
                    }
                    valid = true;
                    pp = new Object[8];
                }
            }
        }
    }

    private void addIfValid(Object[] pp) {
        for (int i = 0; i < 7; i++) {
            if (pp[i] == null) {
                return;
            }
        }
        passports.add(pp);
    }

    int index(int hash) {
        return switch (hash) {
            case 1652126266 -> 0; // "byr"
            case 1769566778 -> 1; // "iyr"
            case 1702457914 -> 2; // "eyr"
            case 1751610426 -> 3; // "hgt"
            case 1751346234 -> 4; // "hcl"
            case 1701014586 -> 5; // "ecl"
            case 1885955130 -> 6; // "pid"
            case 1667851322 -> 7; // "cid" // no extra validation
            default -> -1;
        };
    }

    @Override
    public Object part1() {
        return passports.size();
    }

    @Override
    public Object part2() {
        int p2 = 0;
        for (var pp : passports) {
            if (checkFields(pp)) {
                p2 ++;
            }
        }
        return p2;
    }

    Set<String> colours = Set.of("amb","blu","brn","gry","grn","hzl","oth");

    private boolean checkFields(Object[] pp) {
        int b = (Integer) pp[0];
        int i = (Integer) pp[1];
        int e = (Integer) pp[2];
        return b >= 1920 && b <= 2002
                && i >= 2010 && i <= 2020
                && e >= 2020 && e <= 2030
                && checkHeight((String) pp[3]) // 14 us
                && checkHair((String) pp[4])
                && colours.contains(pp[5]) // 20 us
                && checkId((String) pp[6])
                ;
    }

    private boolean checkHeight(String hh) {
        if (hh.length() < 4) {
            return false;
        }
        if (hh.charAt(2) == 'i') {
            int hgt = Integer.parseInt(hh, 0, 2, 10);
            return hgt >= 59 && hgt <= 76 && hh .charAt(3) == 'n';
        }
        if (hh.charAt(3) == 'c') {
            int hgt = Integer.parseInt(hh, 0, 3, 10);
            return hgt >= 150 && hgt <= 193 && hh.charAt(4) == 'm';
        }
        return false;
    }

    private boolean checkHair(String s) {
        return s.length() == 7 && s.charAt(0) == '#' && Ints.tryParse(s.substring(1), 16) != null;
    }

    private boolean checkId(String s) {
        return s.length() == 9 && Ints.tryParse(s) != null;
    }
}
