package edu.mp.kursovaya;


import java.util.*;

import static edu.mp.kursovaya.Calculate.*;
import static edu.mp.kursovaya.CalculatePotential.*;
import static edu.mp.kursovaya.CommonTables.*;
import static edu.mp.kursovaya.Render.*;
import static edu.mp.kursovaya.CalculateOptimalFactories.*;


public class Main {
    private static final Table table = tableNikita;
    static final Table[] tables = initAllTable();

    public static void main(String[] args) {
        while (true) {
            System.out.println("Input operating mode: ");
            System.out.println("\t 1. main mode");
            System.out.println("\t IN PROGRESS 2. manual mode");
            System.out.println("\t 3. all mode");
            System.out.println("\t 4. debugging mode");
            System.out.println("\t 0. exit");
            System.out.print("> ");
            int mode = new Scanner(System.in).nextInt();
            if (mode == 0) break;

            if (mode == 3) {
                List<Integer> targets = new ArrayList<>();
                for (int n = 0; n < tables.length; n++) {
                    System.out.println("***** NUMBER OF TABLE " + n + " *****");
                    targets.add(calculate(tables[n], false));
                }
                int min = targets.stream().min(Integer::compareTo).orElse(-1);
                int indexMin = targets.indexOf(min);
                for (int i = 0; i < targets.size(); i++) {
                    System.out.print(i + ".\t" + targets.get(i));
                    if (indexMin == i) System.out.print("\t->\tmin");
                    System.out.println();
                }
                combinationFactories.forEach(System.out::println);
                System.out.println();
                System.out.println(combinationFactories.get(indexMin));
            } else if (mode == 4) {
                calculate(table, true);
            }else if (mode == 1){
                calculate(initTable(), true);
            }else if (mode == 2){
                calculate(initManualTable(), true);
            }
        }

    }

    private static int calculate(Table table, boolean showInfo) {
        // ИНИЦИАЛИЗАЦИЯ ТАБЛИЦЫ
        System.out.println("Input table:");
        System.out.println(renderMainTable(table));

        // ПРОВЕРКА УСЛОВИЯ БАЛАНСА ЗАДАЧИ
        if (showInfo) System.out.println("0. Check balance\n");
        if (isBalance(table) > 0) {
            if (showInfo) System.out.println("FALSE: sumA > sumB");
            if (showInfo) System.out.println("Balanced table:");
            normalize(table);
            System.out.println(renderMainTable(table));
        } else if (isBalance(table) < 0) {
            if (showInfo) System.out.println("FALSE: sumA < sumB");
            if (showInfo) System.out.println("Balanced table:");
            normalize(table);
            if (showInfo) System.out.println(renderMainTable(table));
        } else if (showInfo) System.out.println("TRUE: Table is Balanced\n");

        // Нахождение Начального Опорного плана
        if (showInfo) System.out.println("1. Find First Plan:\n");
        findFirstPlan(table);
        if (showInfo) System.out.println(renderTransportTable(table, false));

        // Значение Целевой Функции
        if (showInfo) System.out.println("L=" + calcTargetFunction(table) + "\n");


        // Оптимизация Методом Потенциалов
        if (showInfo) System.out.println("**** Method Potentials ****\n");

        while (true) {

            Map<String, ?> isNoneDegenerate = isNoneDegenerate(table);
            if (showInfo) System.out.println("Plan is None Degenerate: " + isNoneDegenerate.get("check") + "\n");
            if (!(Boolean) isNoneDegenerate.get("check")) {
                setEpsilon(table, isNoneDegenerate);
            }

            // Определяем потенциалы и вычисляем псевдостоимости
            setPseudoCost(table);
            if (showInfo) System.out.println("2 and 3. Find Pseudo Costs\n");
            if (showInfo) System.out.println(renderPseudoTable(table));
            if (showInfo) System.out.println(renderMainTable(table));

            // Проверка на оптимальность полученного плана
            Map<String, ?> isOptimalPlan = isOptimalPlan(table);
            if (showInfo) System.out.println("Plan is Optimal? : " + renderCheckOptimalPlan(isOptimalPlan) + "\n");

            if ((Boolean) isOptimalPlan.get("check")) {

                // План оптимален. Вывод конечной таблицы и целевой функции
                System.out.println("Plan is OPTIMAL!\n");
                System.out.println(renderTransportTable(table, true));
                int L = calcTargetFunction(table);
                System.out.println("L=" + L + "\n");
                return L;
            }else {
                System.out.println(renderTransportTable(table, true));
                int L = calcTargetFunction(table);
                System.out.println("L=" + L + "\n");

            }

            // Составляем цикл пересчёта
            if (showInfo) System.out.println("4. Find Recalculation Loop\n");
            recalculateLoop(table, findOptimalCeil(isOptimalPlan));
            if (showInfo) System.out.println(renderRecalculationLoop(table) + "\n");

            // Находим значение пересчёта
            if (showInfo) System.out.println("5. Find Conversion Value\n");
            int minConvValue = findConversionValue(table);
            if (showInfo) System.out.println(renderConversionValue(table));
            if (showInfo) System.out.println("Conversion Value (Θ): " + minConvValue + "\n");

            // Выполняем пересчёт
            if (showInfo) System.out.println("6. Calculate Conversion\n");
            calcConversion(table, minConvValue);
            if (showInfo) System.out.println(renderTransportTable(table, true));

        }
    }
}