package aoc2020;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.primitives.Ints;
import util.FUtils;
import util.Util;

public class Day04 {
    public static void main(String[] args) {
        Util.profile(() -> new Day04().go(), 1);
    }

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

    void go() {

        long found = 0;
        var in = FUtils.splitLines(2020, 4, "\n\n");
        for (String s : in) {
            Map<String,String> m = new HashMap<>();
            String[] fields = s.split("\\s+");
            for (String field : fields) {
                var f = field.split(":");
                m.put(f[0], f[1]);
            }
            if (revised(m)) {
                ++ found;
            }
        }
        System.out.println(found);
    }

    Set<String> colours = Set.of("amb","blu","brn","gry","grn","hzl","oth");

    // Not aiming for clean code, but things I should have thought of for speed
    private boolean revised(Map<String, String> m) {
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
                case "hcl" -> v.matches("\\#[0-9a-f]{6}");
                case "ecl" -> colours.contains(v);
                case "pid" -> v.matches("[0-9]{9}");
                default -> true;
            };
            if (!ok) return false;
        }
        return true;
    }

    @SuppressWarnings("unused")
    // As written under pressure...
    private boolean original(Map<String, String> m) {
        System.out.println("" + m.size() + m);
        SetView<String> inter = Sets.intersection(keys, m.keySet());
        System.out.println("" + inter.size() + inter);
        if (inter.size() < 7) {
            return false;
        }
        int byr = Ints.tryParse(m.get("byr"));
        int iyr = Ints.tryParse(m.get("iyr"));
        int eyr = Ints.tryParse(m.get("eyr"));
        if (byr < 1920 || byr > 2002
                || iyr < 2010 || iyr > 2020
                || eyr < 2020 || eyr > 2030) {
            return false;
        }
        String hh = m.get("hgt");
        if (!checkHeight(hh)) {
            return false;
        }
        String hc = m.get("hcl");
        if (!hc.startsWith("#")) {
            return false;
        }
        for (int i = 1; i < hc.length(); i++) {
            char ch = hc.charAt(i);
            boolean ok =  (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f');
            if (!ok) return false;
        }
        if (!colours.contains(m.get("ecl"))) {
            return false;
        }
        String pid = m.get("pid");
        if (pid.length() != 9) return false;
        for (int i = 0; i < pid.length(); i++) {
            char ch = pid.charAt(i);
            if (!(ch >= '0' && ch <= '9')) {
                return false;
            }
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
