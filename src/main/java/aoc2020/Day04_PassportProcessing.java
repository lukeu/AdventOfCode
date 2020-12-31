package aoc2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.primitives.Ints;
import framework.AocMeta;
import framework.Base;
import framework.Input;

@AocMeta(notes = "input validation")
public class Day04_PassportProcessing extends Base {
    public static void main(String[] args) {
        Base.run(Day04_PassportProcessing::new, 1);
    }

    @Override public Object expect1() { return 254L; }
    @Override public Object expect2() { return 184L; }

    Set<String> keys = Set.of(
            "byr",//" (Birth Year)     
            "iyr",//" (Issue Year)
            "eyr",//" (Expiration Year)
            "hgt",//" (Height)
            "hcl",//" (Hair Color)
            "ecl",//" (Eye Color)
            "pid"//" (Passport ID)
    //        "cid"//" (Country ID)
    );

    List<Map<String, String>> passports = new ArrayList<>();

    @Override
    public void parse(Input input) {
        var m = new HashMap<String, String>();
        for (String line : input.lines()) {
            if (line.isEmpty()) {
                passports.add(m);
                m = new HashMap<>();
            } else {
                for (String field : line.split(" ")) {
                    int c = field.indexOf(':');
                    m.put(field.substring(0, c), field.substring(c + 1));
                }
            }
        }
        if (!m.isEmpty()) {
            passports.add(m);
        }
    }

    @Override
    public Object part1() {
        return passports.stream()
                .filter(m -> m.keySet().containsAll(keys))
                .count();
    }

    @Override
    public Object part2() {
        return passports.stream()
                .filter(this::checkFields)
                .count();
    }

    Set<String> colours = Set.of("amb","blu","brn","gry","grn","hzl","oth");
    Pattern hair = Pattern.compile("\\#[0-9a-f]{6}");
    Pattern id = Pattern.compile("[0-9]{9}");

    // Not aiming for clean code, but things I should have thought of for speed
    private boolean checkFields(Map<String, String> m) {
        SetView<String> inter = Sets.intersection(keys, m.keySet());
        if (inter.size() < 7) {
            return false;
        }

        for (Entry<String, String> e : m.entrySet()) {
            String v = e.getValue();
            Integer i = Ints.tryParse(v);
            boolean ok = switch (e.getKey()) {
                case "byr" -> i != null && i >= 1920 && i <= 2002;
                case "iyr" -> i != null && i >= 2010 && i <= 2020;
                case "eyr" -> i != null && i >= 2020 && i <= 2030;
                case "hgt" -> checkHeight(v);
                case "hcl" -> hair.matcher(v).matches();
                case "ecl" -> colours.contains(v);
                case "pid" -> id.matcher(v).matches();
                default -> true;
            };
            if (!ok) return false;
        }
        return true;
    }

    private boolean checkHeight(String hh) {
        if (hh.indexOf("cm") == 3) {
            int hgt = Ints.tryParse(hh.substring(0,3));
            return hgt >= 150 && hgt <= 193;
        } else if (hh.indexOf("in") == 2) {
            int hgt = Ints.tryParse(hh.substring(0,2));
            return hgt >= 59 && hgt <= 76;
        } else {
            return false;
        }
    }
}
