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

        fillEmptyPotential(transportField, factoriesPotential, consumersPotential, mainField, table.epsilonsCeil);

        for (int i = 0; i < pseudoField.length; i++) {
            for (int j = 0; j < pseudoField[i].length; j++) {
                if (transportField[i][j].equals(0) && !isEpsilon(table.epsilonsCeil, i, j)) {
                    pseudoField[i][j] = factoriesPotential[i] + consumersPotential[j];
                }
            }
        }
    }

    public static void recalculateLoop(Table table, Integer[] startCeil) {
        Integer[][] transportField = table.transportField;
        Integer[][] mainField = table.mainField;
        List<Integer[]> loop = new ArrayList<>();
        buildCycle(table, new boolean[mainField.length][mainField[0].length], loop, startCeil, new Integer[]{-1, -1});
        table.loop = loop.toArray(Integer[][]::new);
    }

    private static boolean buildCycle(Table table, boolean[][] visitedCeil, List<Integer[]> loop, Integer[] currentCeil, Integer[] previousCeil) {
        Integer[][] transportField = table.transportField;
        List<Integer[]> epsilonsCeil = table.epsilonsCeil;
        int curI = currentCeil[0], curJ = currentCeil[1];
        int prevI = previousCeil[0], prevJ = previousCeil[1];
        loop.add(currentCeil);
        visitedCeil[curI][curJ] = true;

        for (int j = 0; j < transportField[curI].length; j++) {
            // Проверка на наличие перевозок и добавленного пункта, а также исключение бесконечного цикла
            if (j != curJ && (!transportField[curI][j].equals(0) || (j == loop.getFirst()[1] && curI == loop.getFirst()[0])) && !(curI == prevI && j == prevJ)) {

                // Если мы пришли в начальную точку - цикл замкнут
                if (loop.size() > 3 && j == loop.getFirst()[1] && curI == loop.getFirst()[0]) {
                    return true;
                }
                // Если в строке есть начальная точка - цикл замкнут
                if (loop.size() > 3 && curI == loop.getFirst()[0]) {
                    return true;
                }
                // Добавление первых двух точек
                if (!visitedCeil[curI][j] && loop.size() < 2) {
                    if (buildCycle(table, visitedCeil, loop, new Integer[]{curI, j}, new Integer[]{curI, curJ})) {
                        return true;
                    }
                }
                // Исключение линий из 3-х точек
                if (!visitedCeil[curI][j] && loop.size() >= 2 && loop.get(loop.size() - 2)[0] != curI) {
                    if (buildCycle(table, visitedCeil, loop, new Integer[]{curI, j}, new Integer[]{curI, curJ})) {
                        return true;
                    }
                }
            }
        }

        for (int i = 0; i < transportField.length; i++) {
            // Проверка на наличие перевозок и добавленного пункта, а также исключение бесконечного цикла
            if (i != curI && (!transportField[i][curJ].equals(0) || (i == loop.getFirst()[0] && curJ == loop.getFirst()[1])) && !(i == prevI && curJ == prevJ)) {
                // Если мы пришли в начальную точку - цикл замкнут
                if (loop.size() > 3 && i == loop.getFirst()[0] && curJ == loop.getFirst()[1]) {
                    return true;
                }
                // Если в столбце есть начальная точка - цикл замкнут
                if (loop.size() > 3 && curJ == loop.getFirst()[1]) {
                    return true;
                }
                // Добавление первых двух точек
                if (!visitedCeil[i][curJ] && loop.size() < 2) {
                    if (buildCycle(table, visitedCeil, loop, new Integer[]{i, curJ}, new Integer[]{curI, curJ})) {
                        return true;
                    }
                }
                // Исключение линий из 3-х точек
                if (!visitedCeil[i][curJ] && loop.size() >= 2 && loop.get(loop.size()-2)[1] != curJ) {
                    if (buildCycle(table, visitedCeil, loop, new Integer[]{i, curJ}, new Integer[]{curI, curJ})) {
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

            if (i % 2 == 0) {
                transportField[curI][curJ] = transportField[curI][curJ] + min;
            } else {
//                if (isSameValue(table)) {
//                    continue;
//                }
                transportField[curI][curJ] = transportField[curI][curJ] - min;
            }

        }
    }

    private static boolean isSameValue(Table table) {
        Integer[][] loop = table.loop;
        Integer[][] transportField = table.transportField;
        List<Integer[]> epsilonsCeil = table.epsilonsCeil;
        int valI = loop[1][0];
        int valJ = loop[1][1];
        for (int i = 3; i < loop.length; i += 2) {
            int curI = loop[i][0];
            int curJ = loop[i][1];
            if (transportField[curI][curJ].equals(transportField[valI][valJ])) {
                return true;
            }
        }
        return false;
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
        List<Integer> dif = new ArrayList<>();
        List<Integer[]> ceil = new ArrayList<>();
        for (int i = 0; i < mainField.length; i++) {
            for (int j = 0; j < mainField[i].length; j++) {
                if (mainField[i][j] < pseudoField[i][j]) {
                    dif.add(pseudoField[i][j] - mainField[i][j]);
                    ceil.add(new Integer[]{i, j});
                }
            }
        }
        return dif.isEmpty() ? Map.of("check", true) : Map.of(
                "check", false,
                "ceil", ceil,
                "dif", dif
        );
    }

    public static Integer[] findOptimalCeil(Map<String, ?> resultCheckOptimal) {
        List<Integer> dif = (List<Integer>) resultCheckOptimal.get("dif");
        int max = dif.stream().max(Integer::compareTo).get();
        int maxIndex = dif.indexOf(max);
        List<Integer[]> ceil = (List<Integer[]>) resultCheckOptimal.get("ceil");
        return ceil.get(maxIndex);
    }

    private static void fillEmptyPotential(Integer[][] transportField, Integer[] factoriesPotential, Integer[]
            consumersPotential, Integer[][] mainField, List<Integer[]> epsilonsCeil) {
        do {
            for (int i = 0; i < transportField.length; i++) {
                for (int j = 0; j < transportField[i].length; j++) {
                    boolean isEpsilon = isEpsilon(epsilonsCeil, i, j);
                    if (!transportField[i][j].equals(0) || isEpsilon) {
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

    private static boolean isEpsilon(List<Integer[]> epsilonsCeil, int i, int j) {
        boolean isEpsilon = false;
        if (Objects.nonNull(epsilonsCeil)) {
            for (Integer[] position : epsilonsCeil) {
                if (position[0].equals(i) && position[1].equals(j)) {
                    isEpsilon = true;
                    break;
                }
            }
        }
        return isEpsilon;
    }

    private static boolean isEmptyPotentials(Integer[] factoriesPotential, Integer[] consumersPotential) {
        return !(Arrays.stream(consumersPotential).allMatch(Objects::nonNull) &&
                Arrays.stream(factoriesPotential).allMatch(Objects::nonNull));
    }


}
