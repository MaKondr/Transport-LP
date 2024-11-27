package edu.mp.kursovaya;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static edu.mp.kursovaya.Calculate.*;
import static edu.mp.kursovaya.CalculatePotential.*;
import static edu.mp.kursovaya.CommonTables.*;
import static edu.mp.kursovaya.Render.*;
import static edu.mp.kursovaya.CalculateOptimalFactories.*;


public class Main {
    private static final Table table = table4;

    public static void main(String[] args) {

//        Table table = initTable();

        // ИНИЦИАЛИЗАЦИЯ ТАБЛИЦЫ
        System.out.println("Input table:");
        System.out.println(renderMainTable(table));

        // ПРОВЕРКА УСЛОВИЯ БАЛАНСА ЗАДАЧИ
        System.out.println("0. Check balance\n");
        if (isBalance(table) > 0) {
            System.out.println("FALSE: sumA > sumB");
            System.out.println("Balanced table:");
            normalize(table);
            System.out.println(renderMainTable(table));
        } else if (isBalance(table) < 0) {
            System.out.println("FALSE: sumA < sumB");
            System.out.println("Balanced table:");
            normalize(table);
            System.out.println(renderMainTable(table));
        } else System.out.println("TRUE\n");

        // Нахождение Начального Опорного плана
        System.out.println("1. Find First Plan:\n");
        findFirstPlan(table);
        System.out.println(renderTransportTable(table));

        // Значение Целевой Функции
        System.out.println("L=" + calcTargetFunction(table) + "\n");


        // Оптимизация Методом Потенциалов
        System.out.println("**** Method Potentials ****\n");

        while (true) {

            boolean isNoneDegenerate = isNoneDegenerate(table);
            if (!isNoneDegenerate) {
                System.out.println("Plan is None Degenerate: " + isNoneDegenerate + "\n");
            }
            System.out.println("Plan is None Degenerate: " + isNoneDegenerate + "\n");

            // Определяем потенциалы и вычисляем псевдостоимости
            setPseudoCost(table);
            System.out.println("2 and 3. Find Pseudo Costs\n");
            System.out.println(renderPseudoTable(table));
            System.out.println(renderMainTable(table));

            // Проверка на оптимальность полученного плана
            Map<String, ?> isOptimalPlan = isOptimalPlan(table);
            System.out.println("Plan is Optimal? : " + renderCheckOptimalPlan(isOptimalPlan) + "\n");

            if ((Boolean) isOptimalPlan.get("check")) {

                // План оптимален. Вывод конечной таблицы и целевой функции
                System.out.println("Plan is OPTIMAL!\n");
                System.out.println(renderTransportTable(table));
                System.out.println("L=" + calcTargetFunction(table) + "\n");
                break;
            }

            // Составляем цикл пересчёта
            System.out.println("4. Find Recalculation Loop\n");

            Integer[] startCeil = findOptimalCeil(isOptimalPlan);
//            String ceil = isOptimalPlan.get("ceil").toString();
//            Integer[] startCeil = Arrays.stream(ceil.split(",")).map(Integer::parseInt).toArray(Integer[]::new);


            recalculateLoop(table, startCeil);
            System.out.println(renderRecalculationLoop(table) + "\n");

            // Находим значение пересчёта
            System.out.println("5. Find Conversion Value\n");
            int minConvValue = findConversionValue(table);
            System.out.println(renderConversionValue(table));
            System.out.println("Conversion Value (Θ): " + minConvValue + "\n");

            // Выполняем пересчёт
            System.out.println("6. Calculate Conversion\n");
            calcConversion(table, minConvValue);
            System.out.println(renderTransportTable(table));

        }
    }
}