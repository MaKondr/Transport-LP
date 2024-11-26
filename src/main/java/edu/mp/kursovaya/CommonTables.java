package edu.mp.kursovaya;

public class CommonTables {

    public static Table table;

    static {
        Integer[][] costs = {
                {2, 4, 1, 3},
                {4, 8, 2, 4},
                {2, 2, 6, 5}
        };
        Integer[] consumers = {3, 6, 5, 7};
        Integer[] factories = {7, 6, 8};

        table = new Table(
                costs,
                factories,
                consumers
                );
    }
}
