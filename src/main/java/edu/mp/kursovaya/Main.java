package edu.mp.kursovaya;


import static edu.mp.kursovaya.Calculate.*;
import static edu.mp.kursovaya.CommonTables.*;
import static edu.mp.kursovaya.Render.*;
import static edu.mp.kursovaya.CalculateOptimalFactories.*;


public class Main {
    private static final Table table = table1;
    public static void main(String[] args) {

//        Table table = initTable();

        // ИНИЦИАЛИЗАЦИЯ ТАБЛИЦЫ
        System.out.println("Input table:\n");
        System.out.println(renderMainTable(table));

        // ПРОВЕРКА УСЛОВИЯ БАЛАНСА ЗАДАЧИ
        System.out.println("Check balance");
        if (isBalance(table) > 0){
            System.out.println("FALSE: sumA > sumB");
            System.out.println("Balanced table:\n");
            normalize(table);
        }else if (isBalance(table) < 0){
            System.out.println("FALSE: sumA < sumB");
            System.out.println("Balanced table:\n");
            normalize(table);
        }else System.out.println("TRUE");

        System.out.println(renderMainTable(table));

        System.out.println("Find First Plan:\n");
        findFirstPlan(table);
        System.out.println(renderTransportTable(table));
        System.out.println(calcTargetFunction(table));
    }
}