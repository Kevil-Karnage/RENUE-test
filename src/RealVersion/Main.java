package RealVersion;

import java.util.*;

public class Main {
    static String fileName = "airports.csv";

    public static void main(String[] args) throws Exception {
        Scanner scn = new Scanner(System.in);
        System.out.println("Введите фильтр: ");
//        String filter = scn.nextLine();
//        String filter = "column[1]>10&column[5]='IFJ'";
        String filter = "column[1]>10";
//        String filter = "column[1]>10&(column[2]=’GKA’&column[3]=’Three’||column[4]<200)&column[5]<250";

        // фильтруем и сортируем список
        int count = Filtration.filterAndSort(filter, fileName);

        List<Airport> airports = CSVReader.airports;

        System.out.println("Введите начало названия аэропорта:");
        String beginAirportName = scn.nextLine();

        search(airports, beginAirportName);
    }

    private static void search(List<Airport> airports, String beginAirportName) {

    }
}
