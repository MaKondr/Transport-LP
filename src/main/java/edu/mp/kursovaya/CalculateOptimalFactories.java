package edu.mp.kursovaya;

import java.lang.reflect.Array;
import java.util.*;

import static edu.mp.kursovaya.InputValue.*;

public class CalculateOptimalFactories {
    public static List<List<Integer>> combinationFactories;

    private CalculateOptimalFactories() {
    }

    public static Table initTable() {
        return new Table(costs, optimalFactories(), consumers).setFactoriesCost(A,B);
    }

    public static Table[] initAllTable(){
        combinationFactories = new ArrayList<>();
        generateCombinations(factories, 0, new ArrayList<>(), combinationFactories);
        Table[] tables = new Table[combinationFactories.size()];
        for (int i = 0; i < combinationFactories.size(); i++) {
            tables[i] = new Table(costs, combinationFactories.get(i).toArray(Integer[]::new), Arrays.copyOf(consumers,consumers.length)).setFactoriesCost(A,B);
        }
        return tables;
    }


    private static Integer[] optimalFactories() {
        List<List<Integer>> combinationFactories = new ArrayList<>();
        generateCombinations(factories, 0, new ArrayList<>(), combinationFactories);
        filterFactories(combinationFactories);

        List<List<Integer>> transportVariance = calcTransportVariance(combinationFactories);
        List<List<Integer>> factoriesVariance = calcFactoriesVariance(combinationFactories);

        List<Integer> optimalFactories = calcOptimalFactories(transportVariance,factoriesVariance);

        int min = optimalFactories.stream().min(Integer::compareTo).orElse(-1);
        int indexOptimal = optimalFactories.indexOf(min);

        return combinationFactories.get(indexOptimal).toArray(Integer[]::new);
    }

    private static List<Integer> calcOptimalFactories(List<List<Integer>> transportVariance, List<List<Integer>> factoriesVariance) {
        List<Integer> sumVariance = new ArrayList<>();

        for (List<Integer> variances : transportVariance) {
            int curVariances = transportVariance.indexOf(variances);
            int sum = 0;
            for (Integer variance : variances) {
                int curIndex = variances.indexOf(variance);
                sum += variance + factoriesVariance.get(curVariances).get(curIndex);
            }
            sumVariance.add(sum);
        }
//        sumVariance.sort(Integer::compare);
        Optional<Integer> min = sumVariance.stream().min(Integer::compare);
        return sumVariance;


    }

    private static List<List<Integer>> calcFactoriesVariance(List<List<Integer>> combinationFactories) {
        List<List<Integer>> factoriesVariance = new ArrayList<>();

        for (List<Integer> combinationFactory : combinationFactories) {
            List<Integer> factoriesVarianceForConcreteVariant = new ArrayList<>();
            for (int i = 0; i < combinationFactory.size(); i++) {
                int sum = A[i] * combinationFactory.get(i) + B[i];
                factoriesVarianceForConcreteVariant.add(sum);
            }
            factoriesVariance.add(factoriesVarianceForConcreteVariant);
        }
        return factoriesVariance;
    }

//    private static Integer calcTransportMean() {
//        int length = consumers.length;
//        int sum = Arrays.stream(consumers).reduce(0, Integer::sum);
//        return sum / length;
//    }

    private static List<List<Integer>> calcTransportVariance(List<List<Integer>> combinationFactories) {
        List<List<Integer>> transportVariances = new ArrayList<>();

        List<Integer> factoriesMean = new ArrayList<>();

        for (int i = 0; i < A.length; i++) {
            int factoryMean = A[i] + Arrays.stream(costs[i]).reduce(0,Integer::sum) / costs.length;
            factoriesMean.add(factoryMean);
        }

        int sumAmountConsumed = Arrays.stream(consumers).reduce(0, Integer::sum);
        int indexOptimalFactory = factoriesMean.indexOf(factoriesMean.stream().min(Integer::compareTo).orElse(-1));

        for (List<Integer> combination : combinationFactories) {
            int sumAmountProduced = combination.stream().reduce(0,Integer::sum);
            List<Integer> transportVariancesForConcreteVariant = new ArrayList<>();
            for (int i = 0; i < combination.size(); i++) {
                double sum = 0;
                for (int j = 0; j < consumers.length; j++) {
                    double d = (double) consumers[j] / Arrays.stream(consumers).reduce(0, Integer::sum);
                    sum += costs[i][j] * d;
                }
                if (i == indexOptimalFactory) {
                    transportVariancesForConcreteVariant.add((int) sum * (combination.get(i) + (sumAmountConsumed - sumAmountProduced)));
                }else {
                    transportVariancesForConcreteVariant.add((int) sum * (combination.get(i)));
                }
            }
            transportVariances.add(transportVariancesForConcreteVariant);
        }
        return transportVariances;
    }

    private static void generateCombinations(Integer[][] array, int rowIndex, List<Integer> currentCombination, List<List<Integer>> combinations) {
        if (rowIndex == array.length) {
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }
        for (Integer value : array[rowIndex]) {
            currentCombination.add(value);
            generateCombinations(array, rowIndex + 1, currentCombination, combinations);
            currentCombination.removeLast();
        }
    }

    private static void filterFactories(List<List<Integer>> combinations) {
        int sumConsumers = Arrays.stream(consumers).reduce(0, Integer::sum);
        List<List<Integer>> deleteCombinations = new ArrayList<>();
        for (List<Integer> combination : combinations) {
            if (combination.stream().reduce(0, Integer::sum) > sumConsumers) {
                deleteCombinations.add(combination);
            }
        }
        for (List<Integer> deleteCombination : deleteCombinations) {
            combinations.remove(deleteCombination);
        }
    }
}
