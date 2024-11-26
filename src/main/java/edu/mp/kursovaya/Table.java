package edu.mp.kursovaya;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {

    Integer[] factoriesVolume;
    Integer[] consumersVolume;
    public List<Integer[]> factoriesCost;

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

    public Table setFactoriesCost(Integer[] A, Integer[] B){
        factoriesCost = new ArrayList<>(){{
            for (int i = 0; i < A.length; i++) {
                add(new Integer[]{A[i],B[i]});
            }
        }};
        return this;
    }
}
