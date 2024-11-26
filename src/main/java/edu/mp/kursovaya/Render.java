package edu.mp.kursovaya;

import java.util.ArrayList;
import java.util.List;

public class Render {
    public static String renderMainTable(Table table) {
        List<StringBuilder> rows = new ArrayList<>(){{
            add(new StringBuilder("a/b"));
        }};

        for (Integer i : table.consumersVolume) {
            rows.getFirst().append("\t").append(i);
        }
        rows.getFirst().append("\n");

        for (int i = 0; i < table.mainField.length; i++) {
            rows.add(new StringBuilder(table.factoriesVolume[i] + "\t"));
            for (Integer ceil : table.mainField[i]) {
                rows.getLast().append(ceil).append("\t");
            }
            rows.getLast().append("\n");
        }

        return String.join("\n", rows);
    }

    public static String renderTransportTable(Table table){
        List<StringBuilder> rows = new ArrayList<>(){{
            add(new StringBuilder("a/b"));
        }};

        for (Integer i : table.consumersVolume) {
            rows.getFirst().append("\t").append(i);
        }
        rows.getFirst().append("\n");

        for (int i = 0; i < table.mainField.length; i++) {
            rows.add(new StringBuilder(table.factoriesVolume[i] + "\t"));
            for (Integer ceil : table.transportField[i]) {
                rows.getLast().append(ceil.equals(0) ? "X" : ceil).append("\t");
            }
            rows.getLast().append("\n");
        }

        return String.join("\n", rows);
    }
}
