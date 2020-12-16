package util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

public class SUtils {

    public static List<String> split(String in, String split) {
        return Splitter.on(split).splitToList(in);
    }

    public static List<String> lines(String in) {
        return Splitter.on("\n").splitToList(in);
    }

    public static List<String> blocks(String in) {
        return Splitter.on("\n\n").splitToList(in);
    }

    public static int[] lineInts(String in) {
        return lines(in).stream().mapToInt(Integer::parseInt).toArray();
    }

    public static long[] lineLongs(String in) {
        return lines(in).stream().mapToLong(Long::parseLong).toArray();
    }

    public static List<int[]> readCommaInts(String in) {
        return lines(in).stream()
                .map(s ->Splitter.on(",").splitToList(s).stream().mapToInt(Ints::tryParse).toArray())
                .collect(Collectors.toList());
    }

    public static int[] splitInts(String text, String regex, int nonIntValue) {
        return Arrays.stream(text.split(regex))
                .map(Ints::tryParse)
                .mapToInt(i -> i == null ? nonIntValue : i)
                .toArray();
    }

    public static long[] splitLongs(String text, String regex, int nonIntValue) {
        return Arrays.stream(text.split(regex))
                .map(Longs::tryParse)
                .mapToLong(i -> i == null ? nonIntValue : i)
                .toArray();
    }
}
