package framework;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.base.Strings;

/**
 * A trivial utility to format the provided data into a table with aligned columns.
 * Some control of the formatting can be provided by some magical heading strings:
 *
 *  - To set a minimum column width, add an appropriate amount of trailing whitespace to the heading
 *  - To tell the whole column to right-align, insert a leading space in the heading
 */
public class TableFormatter {

    record Row(List<String> cells, boolean isHeading) {
        static Row heading(String... titles) {
            return new Row(List.of(titles), true);
        }
        static Row ofData(String... values) {
            return new Row(List.of(values), false);
        }
    }

    private final List<Row> rows;
    private final int[] columnWidths;

    public static String format(List<Row> rows) {
        return new TableFormatter(rows).formatTable();
    }

    private TableFormatter(List<Row> rows) {
        this.rows = rows;
        this.columnWidths = findColumnWidths(rows);
    }

    private String formatTable() {
        var sb = new StringBuilder();
        for (var row : rows) {
            if (row.isHeading) {
                appendLine(sb);
            }
            appendCells(sb, row);
            if (row.isHeading) {
                appendLine(sb);
            }
        }
        return sb.toString();
    }

    private void appendCells(StringBuilder sb, Row row) {
        var cells = row.cells();
        int c = 0;
        for (String cell : cells) {
            int w = columnWidths[c];
            if (c++ == 0) {
                sb.append(alignLeft(cell, w));
            } else {
                sb.append(" | ").append(alignRight(cell, w));
            }
        }
        sb.append('\n');
    }

    private void appendLine(StringBuilder sb) {
        var line = Arrays.stream(columnWidths)
                .mapToObj(c -> "-".repeat(Math.abs(c)))
                .collect(Collectors.joining("-+-"));
        sb.append(line).append('\n');
    }

    private static String alignLeft(String s, int width) {
        return s + Strings.repeat(" ", width - s.length());
    }

    private static String alignRight(String s, int width) {
        return Strings.repeat(" ", width - s.length()) + s;
    }

    private static int[] findColumnWidths(List<Row> rows) {
        return IntStream.range(0, rows.get(0).cells.size())
                .map(c -> getColWidth(rows, c))
                .toArray();
    }

    private static int getColWidth(List<Row> rows, int col) {
        return rows.stream()
                .mapToInt(r -> r.cells().get(col).length())
                .reduce(0, Math::max);
    }
}
