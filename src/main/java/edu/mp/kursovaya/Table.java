package edu.mp.kursovaya;

import java.util.Arrays;

public class Table {

    Integer[] factoriesVolume;
    Integer[] consumersVolume;

    public int row, column;
    public Integer[][] mainField;
    Integer[][] transportField;

    public Table(Integer[][] costs, Integer[] factories, Integer[] consumers) {
        mainField = costs;
        row = mainField.length;
        column = mainField[0].length;
        Arrays.stream(transportField = new Integer[row][column]).forEach(str-> Arrays.fill(str, 0));
        factoriesVolume = factories;
        consumersVolume = consumers;
    }
}
