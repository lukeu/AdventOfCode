package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;

public class FUtils {

    public static String readLine(int year, int day) {
        return readLines(year, day).get(0);
    }

    public static List<String> splitLines(int year, int day, String split) {
        return Splitter.on(split).splitToList(readString(year, day));
    }

    public static String readString(int year, int day) {
        var br = newReader(newStream(year, day));
        return br.lines().collect(Collectors.joining("\n"));
    }

    public static List<String> readLines(int year, int day) {
        var br = newReader(newStream(year, day));
        return br.lines().collect(Collectors.toList());
    }

    public static int[] readLineInts(int year, int day) {
        var br = newReader(newStream(year, day));
        return br.lines().mapToInt(Integer::parseInt).toArray();
    }

    public static int[] readCommaInts(int year, int day) {
        var br = newReader(newStream(year, day));
        return br.lines()
                .flatMap(s ->Splitter.on(",").splitToList(s).stream())
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static BufferedReader newReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    private static InputStream newStream(int year, int day) {
        return FUtils.class.getClassLoader().getResourceAsStream(
                String.format("%d/in_%02d.txt", year, day));
    }
}
