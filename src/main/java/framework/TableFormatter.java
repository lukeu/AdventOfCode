package framework;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.primitives.Ints;

/**
 * A trivial utility to format the provided data into a table with aligned columns.
 * Some control of the formatting can be provided by some magical heading strings:
 *
 *  - To set a minimum column width, add an appropriate amount of trailing whitespace to the heading
 *  - To tell the whole column to right-align, insert a leading space in the heading
 */
public class TableFormatter {

    private final List<String> headings;
    private final List<List<String>> rows;
    private final int[] columnWidths;

    public static String format(List<String> headings, List<List<String>> rows) {
        return new TableFormatter(headings, rows).formatTable();
    }

    private TableFormatter(List<String> headings, List<List<String>> rows) {
        this.headings = headings.stream().map(s -> s.stripLeading()).collect(Collectors.toList());
        this.rows = rows;
        this.columnWidths = findColumnWidths(headings, rows);
    }

    private String formatTable() {
        var sb = new StringBuilder();
        int c = 0;
        for (String h : headings) {
            if (c != 0) sb.append(" | ");
            appendCell(sb, h, columnWidths[c++]);
        }
        sb.append('\n');

        c = 0;
        for (@SuppressWarnings("unused") String unused : headings) {
            if (c != 0) sb.append("-+-");
            sb.append(Strings.repeat("-", Math.abs(columnWidths[c++])));
        }
        sb.append('\n');

        for (var row : rows) {
            c = 0;
            for (String cell : row) {
                if (c != 0) sb.append(" | ");
                appendCell(sb, cell, columnWidths[c++]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private void appendCell(StringBuilder sb, String s, int width) {
        if (width < 0) {
            sb.append(Strings.repeat(" ", (-width) - s.length()));
            sb.append(s);
        } else {
            sb.append(s);
            sb.append(Strings.repeat(" ", width - s.length()));
        }
    }

    private int[] findColumnWidths(List<String> headings, List<List<String>> rows) {
        var widths = new ArrayList<Integer>();
        int c = 0;
        for (String heading : headings) {
            widths.add(getColWidth(heading, rows, c++));
        }
        return Ints.toArray(widths);
    }

    private int getColWidth(String heading, List<List<String>> rows, int col) {
        int w = rows.stream()
                .mapToInt(r -> r.get(col).length())
                .reduce(heading.stripLeading().length(), Math::max);
        return heading.startsWith(" ") ? -w : w;
    }
}
