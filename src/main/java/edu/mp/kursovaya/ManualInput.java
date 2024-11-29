package edu.mp.kursovaya;

import org.ietf.jgss.GSSContext;
import org.w3c.dom.css.CSSImportRule;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManualInput {
    static Integer[][] mainTable;
    static Integer[][] factories;
    static Integer[] consumers;
    static Integer[] A;
    static Integer[] B;

    static void initInput() {
        System.out.println("It manual mode. A mode which input table costs, consumers and producers.");
        initTable(inputSize());
        fillVariancesFactory();
        fillConst();
    }

    static int[] inputSize() {
        System.out.println("Input size costs table:");
        System.out.print("\trows: ");
        int rows = new Scanner(System.in).nextInt();
        System.out.print("\tcolumns: ");
        int columns = new Scanner(System.in).nextInt();
        return new int[]{rows, columns};
    }

    static void initTable(int[] size) {
        int rows = size[0], columns = size[1];
        mainTable = new Integer[rows][columns];
        consumers = new Integer[columns];
        A = new Integer[columns];
        B = new Integer[columns];
    }

    static void fillMainTable() {
        System.out.println("Input main table");
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < mainTable.length; i++) {
            System.out.println("row-" + i);
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < mainTable[i].length; j++) {
                row.add(scanner.nextInt());
            }
//            mainTable[i]
        }
    }

    static void fillVariancesFactory() {
        System.out.print("Input num variance: ");
        int numVariance = new Scanner(System.in).nextInt();
        factories = new Integer[numVariance][];
        System.out.println("Input values or 'q' for quit");
        for (int i = 0; i < numVariance; i++) {
            List<Integer> values = new ArrayList<>();
            Scanner scanner = new Scanner(System.in);
            int counter = 0;
            while (counter < mainTable.length) {
                counter++;
                System.out.print(counter + ".\t");
                String s = scanner.nextLine();
                if (s.equals("q")) break;
                values.add(Integer.parseInt(s));
            }
            factories[i] = values.toArray(Integer[]::new);
        }
    }

    static void fillConst() {
        System.out.println("Filling A: ");
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < A.length; i++) {
            System.out.print(i + ".\t");
            A[i] = scanner.nextInt();
        }
        System.out.println("Filling B: ");
        for (int i = 0; i < B.length; i++) {
            System.out.print(i + ".\t");
            B[i] = scanner.nextInt();
        }
    }


}
