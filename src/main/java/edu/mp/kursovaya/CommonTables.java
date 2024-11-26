package edu.mp.kursovaya;

public class CommonTables {

    public static Table table1;

    static {
        Integer[][] costs = {
                {2, 4, 1, 3},
                {4, 8, 2, 4},
                {2, 2, 6, 5}
        };
        Integer[] consumers = {3, 6, 5, 7};
        Integer[] factories = {4, 6, 8};

        table1 = new Table(
                costs,
                factories,
                consumers
                );
    }

    public static Table table2;

    static {
        Integer[][] costs = {
                {2, 5, 1},
                {7, 9, 2},
                {3, 2, 8}
        };
        Integer[] consumers = {30, 15, 25};
        Integer[] factories = {3, 0, 15};
        table2 = new Table(
                costs,
                factories,
                consumers
        );
    }
}
