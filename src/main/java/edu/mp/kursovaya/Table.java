package edu.mp.kursovaya;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Table {

    // Аргументы A и B функции расчёта цены производства
    public List<Integer[]> factoriesCost;

    // Производители и потребители
    Integer[] factoriesVolume;
    Integer[] consumersVolume;

    // Поле с ценами за перевозку и количеством перевозок
    public int row, column;
    public Integer[][] mainField;
    Integer[][] transportField;

    //------------------------------------------------------

    // Потенциалы производителей и потребителей
    Integer[] factoriesPotential;
    Integer[] consumersPotential;

    // Поле с псевдостоимостью
    Integer[][] pseudoField;


    // Цикл для пересчёта
    Integer[][] loop;

    // Клетки которые мы обозначили за эпсилон
    boolean[][] epsilonsCeil;

    public Table(Integer[][] costs, Integer[] factories, Integer[] consumers) {
        mainField = costs;
        row = mainField.length;
        column = mainField[0].length;
        Arrays.stream(transportField = new Integer[row][column]).forEach(str -> Arrays.fill(str, 0));
        factoriesVolume = factories;
        consumersVolume = consumers;
    }

    public Table setFactoriesCost(Integer[] A, Integer[] B) {
        factoriesCost = new ArrayList<>() {{
            for (int i = 0; i < A.length; i++) {
                add(new Integer[]{A[i], B[i]});
            }
        }};
        return this;
    }

    public int sizeEpsilons(){
        if (Objects.isNull(epsilonsCeil)) return 0;
        int count = 0;
        for (boolean[] booleans : epsilonsCeil) {
            for (boolean aBoolean : booleans) {
                if(aBoolean) count++;
            }
        }
        return count;
    }

    public boolean isEpsilon(int i, int j) {
        if (Objects.nonNull(epsilonsCeil)) {
            if (epsilonsCeil[i][j]) return true;
        }
        return false;
    }


}
