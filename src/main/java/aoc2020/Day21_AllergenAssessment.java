package aoc2020;

import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;
import framework.AocMeta;
import framework.Base;
import framework.Input;

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
    public void parse(Input in) {
        foods = in.lines().stream().map(line -> {
            int split = line.indexOf(" (");
            String allergenText = line.substring(split + ", (contains".length(), line.length() - 1);
            return new Food(
                    Splitter.on(' ').splitToList(line.substring(0, split)),
                    Splitter.on(", ").splitToList(allergenText));

        }).toList();
    }

    /** ALLERGEN => possible ingredients */
    Map<String, Set<String>> poss = new TreeMap<>();

    @Override
    public Long part1() {

        // Start with all ingredients as possibilities
        Set<String> ingr = foods.stream().flatMap(f -> f.ingr.stream()).collect(toSet());
        for (Food f : foods) {
            for (String a : f.allergens) {
                poss.put(a, new HashSet<>(ingr));
            }
        }

        for (Food food : foods) {
            for (var a : food.allergens) {
                poss.get(a).retainAll(food.ingr);
            }
        }

        // Eliminate any known allergens from all other possibilities
        boolean change;
        do {
            change = false;
            for (Set<String> values : poss.values()) {
                if (values.size() == 1) {
                    for (var other : poss.values()) {
                        if (other != values) {
                            change |= other.removeAll(values); // Only removing a single item
                        }
                    }
                }
            }
        } while (change);

        // Remove ingredients with allergens, leaving just the 'inert' ingredients in "ingr"
        poss.values().forEach(ingr::removeAll);
        return foods.stream()
                .flatMap(f -> f.ingr.stream())
                .filter(ingr::contains)
                .count();
    }

    @Override
    public String part2() {
        return poss.values().stream()
                .map(s -> s.iterator().next())
                .collect(Collectors.joining(","));
    }
}
