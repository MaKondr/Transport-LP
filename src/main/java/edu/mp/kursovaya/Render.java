package edu.mp.kursovaya;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Render {
    public static String renderTable(Table table) {
        List<StringBuilder> rows = new ArrayList<>(){{
            add(new StringBuilder("a/b"));
        }};

        for (Integer i : table.consumersVolume) {
            rows.getFirst().append("\t").append(i);
        }
        rows.getFirst().append("\n");




        for (int i = 0; i < table.mainField.length; i++) {
            rows.add(new StringBuilder(table.factoriesVolume[i] + "\t"));
            for (Table.Ceil ceil : table.mainField[i]) {
                rows.getLast().append(ceil.costCeil).append("\t");
            }
            rows.getLast().append("\n");
        }

        return String.join("\n", rows);
    }
}
