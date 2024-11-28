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

    public static Table table3;

    static {
        Integer[][] costs = {
                {3, 4, 1, 2},
                {2, 2, 4, 3},
                {1, 1, 2, 1},
                {1, 1, 1, 1},
        };
        Integer[] consumers = {3, 8, 4, 3};
        Integer[] factories = {4, 5, 4, 5};
        table3 = new Table(
                costs,
                factories,
                consumers
        );
    }

    public static Table table4;

    static {
        Integer[][] costs = {
                {7,8,5,3},
                {2,4,5,9},
                {6,3,1,2},
        };
        Integer[] consumers = {8,9,9,7};
        Integer[] factories = {11,11,8};
        table4 = new Table(
                costs,
                factories,
                consumers
        );
    }    public static Table table5;

    static {
        Integer[][] costs = {
                {2,3,6,8,1,3},
                {1,7,2,6,5,2},
                {3,6,1,2,4,5},
                {7,4,2,5,3,1},
        };
        Integer[] consumers = {1,4,4,5,1,3};
        Integer[] factories = {7,6,2,3};
        table5 = new Table(
                costs,
                factories,
                consumers
        );
    }
}
