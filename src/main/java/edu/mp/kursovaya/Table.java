package edu.mp.kursovaya;

public class Table {

    Integer[] factoriesVolume;
    Integer[] consumersVolume;
    public  Ceil[][] mainField;
    public static class Ceil{
        Integer costCeil = 0 ;

        public Ceil(Integer costCeil){
            this.costCeil = costCeil;
        }
    }

    public Table(int[][] costs, Integer[] factories, Integer[] consumers){
        mainField = new Ceil[costs.length][costs[0].length];
        factoriesVolume = factories;
        consumersVolume = consumers;
        initCeils(costs);
    }

    private void initCeils(int[][] costs){
        for (int i = 0; i < costs.length; i++) {
            for (int i1 = 0; i1 < costs[i].length; i1++) {
                mainField[i][i1] = new Ceil(costs[i][i1]);
            }
        }
    }


}
