package edu.mp.kursovaya;

import java.util.*;

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

    public static String renderTransportTable(Table table, boolean showEpsilon) {
        List<StringBuilder> rows = new ArrayList<>() {{
            add(new StringBuilder("Transport Table\n"));
        }};
//        rows.getFirst().append("a/b");
//        for (Integer i : table.consumersVolume) {
//            rows.getFirst().append("\t").append(i);
//        }
//        rows.getFirst().append("\n");

        for (int i = 0; i < table.transportField.length; i++) {
//            rows.add(new StringBuilder(table.factoriesVolume[i] + "\t"));
            for (int j = 0; j < table.transportField[i].length; j++) {
                if (!table.transportField[i][j].equals(0)) {
                    rows.getLast().append(table.transportField[i][j]).append("\t");
                } else if (Objects.nonNull(table.epsilonsCeil) && table.epsilonsCeil[i][j] && showEpsilon) {
                    rows.getLast().append("ξ").append("\t");
                } else {
                    rows.getLast().append("X").append("\t");
                }
            }
            rows.getLast().append("\n\n");
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
        List<Integer[]> ceils = (List<Integer[]>) result.get("ceil");
        StringBuilder ceilBuilder = new StringBuilder();
        ceils.stream().forEach(ceil -> ceilBuilder.append(Arrays.toString(ceil)));
        String ceil = "Ceil: " + ceilBuilder + "\t";
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
            column:
            for (int j = 0; j < transportField[i].length; j++) {
                for (int pos = 0; pos < loop.length; pos++) {
                    if (loop[pos][0].equals(i) && loop[pos][1].equals(j)) {
                        String sign = pos % 2 == 0 ? "+" : "-";
                        if (table.isEpsilon(i, j)) {
                            rows.getLast().append(sign).append("ξ").append("\t");
                        } else {
                            rows.getLast().append(sign).append(transportField[i][j]).append("\t");
                        }
                        continue column;
                    }
                }
                if (table.isEpsilon(i, j)) {
                    rows.getLast().append("ξ").append("\t");
                } else if (!transportField[i][j].equals(0)) {
                    rows.getLast().append(transportField[i][j]).append("\t");
                } else {
                    rows.getLast().append("X").append("\t");
                }

            }
            rows.getLast().append("\n");
        }
        return String.join("\n", rows);
    }
}