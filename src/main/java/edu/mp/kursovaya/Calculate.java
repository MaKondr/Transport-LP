package edu.mp.kursovaya;

import java.util.*;

public class Calculate {
    public static int isBalance(Table table) {
        Integer aSum = Arrays.stream(table.factoriesVolume).reduce(0, Integer::sum);
        Integer bSum = Arrays.stream(table.consumersVolume).reduce(0, Integer::sum);
        return aSum.compareTo(bSum);
    }

    public static void normalize(Table table) {
        int aSum = Arrays.stream(table.factoriesVolume).reduce(0, Integer::sum);
        int bSum = Arrays.stream(table.consumersVolume).reduce(0, Integer::sum);
        int column = table.mainField[0].length;
        int row = table.mainField.length;
        Integer[][] newArray;
        if (aSum > bSum) {
            newArray = new Integer[row][column + 1];
            Arrays.stream(newArray).forEach(str -> Arrays.fill(str, 0));
            for (int i = 0; i < row; i++) {
                System.arraycopy(table.mainField[i], 0, newArray[i], 0, column);
            }

            table.consumersVolume = Arrays.copyOf(table.consumersVolume, table.consumersVolume.length + 1);
            table.consumersVolume[table.consumersVolume.length - 1] = aSum - bSum;
            table.mainField = newArray;
            Arrays.stream(table.transportField = new Integer[newArray.length][newArray[0].length]).forEach(str -> Arrays.fill(str, 0));

        } else if (bSum > aSum) {
            newArray = new Integer[row + 1][column];
            Arrays.stream(newArray).forEach(str -> Arrays.fill(str, 0));
            for (int i = 0; i < row; i++) {
                System.arraycopy(table.mainField[i], 0, newArray[i], 0, column);
            }
            table.factoriesVolume = Arrays.copyOf(table.factoriesVolume, table.factoriesVolume.length + 1);
            table.factoriesVolume[table.factoriesVolume.length - 1] = bSum - aSum;
            table.mainField = newArray;
            Arrays.stream(table.transportField = new Integer[newArray.length][newArray[0].length]).forEach(str -> Arrays.fill(str, 0));
        }
    }

    public static void findFirstPlan(Table table) {
        Integer[][] mainField = table.mainField;
        Integer[][] transportField = table.transportField;
        Integer[] consumersVolume = table.consumersVolume;
        Integer[] factoriesVolume = table.factoriesVolume;
        Set<String> verified = new HashSet<>();

        while (Arrays.stream(consumersVolume).reduce(0, Integer::sum) != 0 &&
                Arrays.stream(factoriesVolume).reduce(0, Integer::sum) != 0) {
            Integer min = Integer.MAX_VALUE;
            int i_min = 0, j_min = 0;
            for (int i = 0; i < mainField.length; i++) {

                // Проверка на вычеркнутую строку
                if (factoriesVolume[i] == 0) continue;

                for (int j = 0; j < mainField[i].length; j++) {

                    // Проверка на вычеркнутый столбец
                    if (consumersVolume[j] == 0) continue;

                    Integer current = mainField[i][j];

                    // Проверка на минимальный элемент C_ij
                    if (min.compareTo(current) >= 0 && !verified.contains(i + "," + j)) {
                        if (current.equals(0) && isZeroLast(factoriesVolume, consumersVolume)) {
                            min = current;
                            i_min = i;
                            j_min = j;
                        } else if (!current.equals(0)) {
                            min = current;
                            i_min = i;
                            j_min = j;
                        }
                    }
                }
            }
            verified.add(i_min + "," + j_min);
            // Нахождение X_ij
            if (consumersVolume[j_min].compareTo(factoriesVolume[i_min]) > 0) {
                transportField[i_min][j_min] = factoriesVolume[i_min];
                consumersVolume[j_min] -= factoriesVolume[i_min];
                factoriesVolume[i_min] = 0;
            } else {
                transportField[i_min][j_min] = consumersVolume[j_min];
                factoriesVolume[i_min] -= consumersVolume[j_min];
                consumersVolume[j_min] = 0;
            }
        }
    }

    private static boolean isZeroLast(Integer[] factories, Integer[] consumers) {
        int counterF = 0;
        int counterC = 0;
        for (Integer factory : factories) {
            if (!factory.equals(0)) counterF++;
        }
        for (Integer consumer : consumers) {
            if (!consumer.equals(0)) counterC++;
        }
        return counterC == 1 || counterF == 1;
    }

    private static Integer[][] copyTable(Table table) {
        Integer[][] copyTable = new Integer[table.row][table.column];
        for (int i = 0; i < table.mainField.length; i++) {
            System.arraycopy(table.mainField[i], 0, copyTable[i], 0, table.column);
        }
        return copyTable;
    }
}