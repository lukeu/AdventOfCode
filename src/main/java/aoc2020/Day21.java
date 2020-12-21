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
import framework.Base;
import util.SUtils;

public class Day21 extends Base {
    public static void main(String[] args) {
        Base.run(Day21::new, 1);
    }

    @Override
    public String testInput() {
        return "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)\n"
                + "trh fvjkl sbzzf mxmxvkd (contains dairy)\n"
                + "sqjhc fvjkl (contains soy)\n"
                + "sqjhc mxmxvkd sbzzf (contains fish)";
    }

    // ALLERGEN => ingredients
    Map<String, Set<String>> poss = new TreeMap<>();

    @Override
    public void parse(String in) {
        record Food(List<String> ingr, List<String> allergens) {}

        var foods = SUtils.lines(in).stream().map(line -> {
            int split = line.indexOf(" (");
            String allergenText = line.substring(split + ", (contains".length(), line.length() - 1);
            return new Food(
                    Splitter.on(' ').splitToList(line.substring(0, split)),
                    Splitter.on(", ").splitToList(allergenText));

        }).collect(toList());

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
        System.out.println("No allergens: " + none.size());

        long found = 0;
        for (Food food : foods) {
            for (String i : food.ingr) {
                if (none.contains(i)) {
                    found++;
                }
            }
        }
        System.out.println("Appear: " + found);
        var p2 = new ArrayList<>();
        for (Set<String> s : poss.values()) {
            p2.add(s.iterator().next());
        }
        System.out.println("P2: " + Joiner.on(",").join(p2));
    }
}
