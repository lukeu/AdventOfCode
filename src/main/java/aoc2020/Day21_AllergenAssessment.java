package aoc2020;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import framework.AocMeta;
import framework.Base;
import util.SUtils;

@AocMeta(notes = "logic")
public class Day21_AllergenAssessment extends Base {
    public static void main(String[] args) {
        Base.run(Day21_AllergenAssessment::new, 1);
    }

    @Override
    public String testInput() {
        return "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)\n"
                + "trh fvjkl sbzzf mxmxvkd (contains dairy)\n"
                + "sqjhc fvjkl (contains soy)\n"
                + "sqjhc mxmxvkd sbzzf (contains fish)";
    }

    @Override public Object testExpect1() { return 5L; }
    @Override public Object testExpect2() { return "mxmxvkd,sqjhc,fvjkl"; }
    @Override public Object expect1() { return 1913L; }
    @Override public Object expect2() { return "gpgrb,tjlz,gtjmd,spbxz,pfdkkzp,xcfpc,txzv,znqbr"; }

    record Food(List<String> ingr, List<String> allergens) {}
    List<Food> foods;

    @Override
    public void parse(String in) {
        foods = SUtils.lines(in).stream().map(line -> {
            int split = line.indexOf(" (");
            String allergenText = line.substring(split + ", (contains".length(), line.length() - 1);
            return new Food(
                    Splitter.on(' ').splitToList(line.substring(0, split)),
                    Splitter.on(", ").splitToList(allergenText));

        }).collect(toList());
    }

    /** ALLERGEN => ingredients */
    Map<String, Set<String>> poss = new TreeMap<>();

    @Override
    public Long part1() {
        Set<String> allIngr = foods.stream().flatMap(f -> f.ingr.stream()).collect(toSet());
        Set<String> allAll = foods.stream().flatMap(f -> f.allergens.stream()).collect(toSet());
        for (String string : allAll) {
            poss.put(string, new TreeSet<>(allIngr));
        }

        for (Food food : foods) {
            for (var a : food.allergens) {
                poss.get(a).retainAll(food.ingr);
            }
        }

        // Eliminate
        boolean change = false;
        do {
            change = false;
            for (Set<String> allergens : poss.values()) {
                if (allergens.size() == 1) {
                    var all = allergens.iterator().next();
                    for (var e : poss.entrySet()) {
                        if (e.getValue().size() > 1 && e.getValue().contains(all)) {
                            change = true;
                            e.getValue().removeAll(allergens);
                        }
                    }
                }
            }
        } while (change);

        var none = new TreeSet<>(allIngr);
        for (Set<String> set : poss.values()) {
            none.removeAll(set);
        }
        long found = 0;
        for (Food food : foods) {
            for (String i : food.ingr) {
                if (none.contains(i)) {
                    found++;
                }
            }
        }
        return found;
    }

    @Override
    public String part2() {
        var p2 = new ArrayList<>();
        for (Set<String> s : poss.values()) {
            p2.add(s.iterator().next());
        }
        return Joiner.on(",").join(p2);
    }
}
