package edu.mp.kursovaya;

import java.util.*;

public class CalculatePotential {
    public static void setPseudoCost(Table table) {
        initPotentialFields(table);
        Integer[][] mainField = table.mainField;
        Integer[][] transportField = table.transportField;
        Integer[][] pseudoField = table.pseudoField;
        Integer[] factoriesPotential = table.factoriesPotential;
        Integer[] consumersPotential = table.consumersPotential;

        fillEmptyPotential(transportField, factoriesPotential, consumersPotential, mainField);

        for (int i = 0; i < pseudoField.length; i++) {
            for (int j = 0; j < pseudoField[i].length; j++) {
                if (transportField[i][j].equals(0)) {
                    pseudoField[i][j] = factoriesPotential[i] + consumersPotential[j];
                }
            }
        }
    }

    public static void recalculateLoop(Table table, Integer[] startCeil) {
        Integer[][] transportField = table.transportField;
        Integer[][] mainField = table.mainField;
        Integer[][] pseudoField = table.pseudoField;
        List<Integer[]> loop = new ArrayList<>();
        buildCycle(transportField, new boolean[mainField.length][mainField[0].length], loop, startCeil, new Integer[]{-1, -1});
        table.loop = loop.toArray(Integer[][]::new);
    }

    private static boolean buildCycle(Integer[][] mainTable, boolean[][] visitedCeil, List<Integer[]> loop, Integer[] currentCeil, Integer[] previousCeil) {
        int curI = currentCeil[0], curJ = currentCeil[1];
        int prevI = previousCeil[0], prevJ = previousCeil[1];
        loop.add(currentCeil);
        visitedCeil[curI][curJ] = true;
        for (int j = 0; j < mainTable[curI].length; j++) {
            if (j != curJ && (!mainTable[curI][j].equals(0) || (j == loop.getFirst()[1] && curI == loop.getFirst()[0])) && !(curI == prevI && j == prevJ)) {
                if (loop.size() > 3 && j == loop.getFirst()[1] && curI == loop.getFirst()[0]) {
                    return true;
                }
                if (!visitedCeil[curI][j]) {
                    if (buildCycle(mainTable, visitedCeil, loop, new Integer[]{curI, j}, new Integer[]{curI, curJ})) {
                        return true;
                    }
                }
            }
        }

        for (int i = 0; i < mainTable.length; i++) {
            if (i != curI && (!mainTable[i][curJ].equals(0) || (i == loop.getFirst()[0] && curJ == loop.getFirst()[1])) && !(i == prevI && curJ == prevJ)) {
                if (loop.size() > 3 && i == loop.getFirst()[0] && curJ == loop.getFirst()[1]) {
                    return true;
                }
                if (!visitedCeil[i][curJ]) {
                    if (buildCycle(mainTable, visitedCeil, loop, new Integer[]{i, curJ}, new Integer[]{curI, curJ})) {
                        return true;
                    }
                }
            }
        }

        loop.remove(currentCeil);
        visitedCeil[curI][curJ] = false;
        return false;
    }

    public static Integer findConversionValue(Table table) {
        Integer[][] loop = table.loop;
        Integer[][] transportField = table.transportField;
        int min = Integer.MAX_VALUE;

        for (int i = 1; i < loop.length; i += 2) {
            int curI = loop[i][0], curJ = loop[i][1];
            if (min > transportField[curI][curJ]) {
                min = transportField[curI][curJ];
            }
        }
        return min;

    }

    public static void calcConversion(Table table, Integer min) {
        Integer[][] loop = table.loop;
        Integer[][] transportField = table.transportField;
        for (int i = 0; i < loop.length; i++) {
            int curI = loop[i][0], curJ = loop[i][1];
            transportField[curI][curJ] = i % 2 == 0 ? transportField[curI][curJ] + min : transportField[curI][curJ] - min;
        }
    }


    private static void initPotentialFields(Table table) {
        int row = table.transportField.length;
        int column = table.transportField[0].length;
        Arrays.stream(table.pseudoField = new Integer[row][column]).forEach(str -> Arrays.fill(str, 0));
        table.factoriesPotential = new Integer[row];
        table.factoriesPotential[0] = 0;
        table.consumersPotential = new Integer[column];
    }

    public static Map<String, ?> isOptimalPlan(Table table) {
        Integer[][] mainField = table.mainField;
        Integer[][] pseudoField = table.pseudoField;

        for (int i = 0; i < mainField.length; i++) {
            for (int j = 0; j < mainField[i].length; j++) {
                if (mainField[i][j] < pseudoField[i][j]) {
                    int dif = pseudoField[i][j] - mainField[i][j];
                    return Map.of(
                            "check", false,
                            "ceil", i + "," + j,
                            "dif", dif
                    );
                }
            }
        }
        return Map.of("check", true);
    }

    private static void fillEmptyPotential(Integer[][] transportField, Integer[] factoriesPotential, Integer[] consumersPotential, Integer[][] mainField) {
        do {
            for (int i = 0; i < transportField.length; i++) {
                for (int j = 0; j < transportField[i].length; j++) {
                    if (!transportField[i][j].equals(0)) {
                        if (Objects.isNull(factoriesPotential[i]) && Objects.isNull(consumersPotential[j])) {
                        } else if (Objects.isNull(consumersPotential[j])) {
                            consumersPotential[j] = mainField[i][j] - factoriesPotential[i];
                        } else if (Objects.isNull(factoriesPotential[i])) {
                            factoriesPotential[i] = mainField[i][j] - consumersPotential[j];
                        }
                    }
                }
            }

        } while (isEmptyPotentials(factoriesPotential, consumersPotential));
    }

    private static boolean isEmptyPotentials(Integer[] factoriesPotential, Integer[] consumersPotential) {
        return Arrays.stream(consumersPotential).anyMatch(Objects::isNull) &&
                Arrays.stream(factoriesPotential).anyMatch(Objects::isNull);
    }


}
