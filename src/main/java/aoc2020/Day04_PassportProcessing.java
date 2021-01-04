package aoc2020;

import java.util.Set;

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

    int p1 = 0;
    int p2 = 0;

    @Override
    public void parse(Input input) {
        int found = 0;
        var bb = new ByteBiter(input.bytes(this));
        boolean valid = true;
        while (bb.hasRemaining()) {
            int key = bb.getBinaryInt();
            int index = index(key);
            found |= index;
            valid = valid && consumeAndCheckField(bb, index);
            bb.consumeUntilWs();
            if (bb.hasRemaining() && bb.get() == '\n') {
                if (bb.hasRemaining() && bb.peek() == '\n') {
                    bb.get();
                    if (found == 127) {
                        p1++;
                        if (valid) {
                            p2++;
                        }
                    }
                    found = 0;
                    valid = true;
                }
            }
        }
    }

    int index(int hash) {
        return switch (hash) {
            case 1652126266 -> 1; // "byr"
            case 1769566778 -> 2; // "iyr"
            case 1702457914 -> 4; // "eyr"
            case 1751610426 -> 8; // "hgt"
            case 1751346234 -> 16; // "hcl"
            case 1701014586 -> 32; // "ecl"
            case 1885955130 -> 64; // "pid"
            default -> 0; // "cid" // no extra validation
        };
    }

    boolean consumeAndCheckField(ByteBiter bb, int index) {
        return switch (index) {
            case 1 -> checkRange(bb, 1920, 2002); // "byr"
            case 2 -> checkRange(bb, 2010, 2020); // "iyr"
            case 4 -> checkRange(bb, 2020, 2030); // "eyr"
            case 8 -> checkHeight(bb); // "hgt"
            case 16 -> checkHair(bb); // "hcl"
            case 32 -> colours.contains(bb.extractToWhitespace()); // "ecl"
            case 64 -> checkDigits(bb, 9); // "pid"
            default -> true; // "cid" // no extra validation
        };
    }

    boolean checkRange(ByteBiter bb, int min, int max) {
        int i = bb.positiveInt();
        return i >= min && i <= max;
    }

    boolean checkDigits(ByteBiter bb, int nDigits) {
        int mark = bb.pos;
        return bb.positiveInt() > 0 && (bb.pos - mark) == nDigits;
    }

    @Override
    public Object part1() {
        return p1;
    }

    @Override
    public Object part2() {
        return p2;
    }

    Set<String> colours = Set.of("amb","blu","brn","gry","grn","hzl","oth");

    boolean checkHeight(ByteBiter bb) {
        int n = bb.positiveInt();
        return switch (bb.peek()) {
            case 'i' -> n >= 59 && n <= 76;
            case 'c' -> n >= 150 && n <= 193;
            default -> false;
        };
    }

    boolean checkHair(ByteBiter bb) {
        return bb.get() == '#' && consumeHex(bb) == 6;
    }

    int consumeHex(ByteBiter bb) {
        int mark = bb.pos;
        while (bb.hasRemaining()) {
            byte b = bb.peek();
            if ((b < '0' || b > '9') && (b < 'a' || b > 'f')) {
                break;
            }
            bb.skip();
        }
        return bb.pos - mark;
    }
}
