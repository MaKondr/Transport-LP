package edu.mp.kursovaya;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Render {
    public static String renderMainTable(Table table) {
        List<StringBuilder> rows = new ArrayList<>() {{
            add(new StringBuilder("Main Costs Table\n"));
        }};

        rows.getFirst().append("a/b");
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

    public static String renderTransportTable(Table table) {
        List<StringBuilder> rows = new ArrayList<>() {{
            add(new StringBuilder("Transport Table\n"));
        }};
        rows.getFirst().append("a/b");
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

    public static String renderPseudoTable(Table table) {
        List<StringBuilder> rows = new ArrayList<>() {{
            add(new StringBuilder("Pseudo Costs Table\n"));
        }};
        rows.getFirst().append("A/B");
        for (Integer i : table.consumersPotential) {
            rows.getFirst().append("\t").append(i);
        }
        rows.getFirst().append("\n");

        for (int i = 0; i < table.mainField.length; i++) {
            rows.add(new StringBuilder(table.factoriesPotential[i] + "\t"));
            for (Integer ceil : table.pseudoField[i]) {
                rows.getLast().append(ceil).append("\t");
            }
            rows.getLast().append("\n");
        }

        return String.join("\n", rows);
    }

    public static String renderCheckOptimalPlan(Map<String, ?> result) {
        StringBuilder sb = new StringBuilder();
        if ((Boolean) result.get("check")) {
            return sb.append("true").toString();
        }
        String ceil = "Ceil: " + result.get("ceil") + "\t";
        String dif = "Dif: " + result.get("dif").toString() + "\t";
        return sb.append("false\t").append(ceil).append(dif).toString();
    }

    public static String renderRecalculationLoop(Table table) {
        StringBuilder sb = new StringBuilder();
        Integer[][] loop = table.loop;

        for (Integer[] integers : loop) {
            sb.append(Arrays.toString(integers)).append("->");
        }
        sb.append(Arrays.toString(loop[0]));
        return sb.toString();

    }

    public static String renderConversionValue(Table table) {
        List<StringBuilder> rows = new ArrayList<>() {{
            add(new StringBuilder("Conversion Table"));
        }};
        Integer[][] loop = table.loop;
        Integer[][] transportField = table.transportField;

        for (int i = 0; i < transportField.length; i++) {
            rows.add(new StringBuilder());
           column : for (int j = 0; j < transportField.length; j++) {
                for (int pos = 0; pos < loop.length; pos++) {
                    if (loop[pos][0].equals(i) && loop[pos][1].equals(j)) {
                        String sign = pos % 2 == 0 ? "+" : "-";
                        rows.getLast().append(sign).append(transportField[i][j]).append("\t");
                        continue column;
                    }
                }
                rows.getLast().append(transportField[i][j].equals(0) ? "X" : transportField[i][j]).append("\t");
            }
            rows.getLast().append("\n");
        }
        return String.join("\n", rows);
    }
}