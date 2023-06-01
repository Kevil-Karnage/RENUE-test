package RealVersion;

import java.util.*;

public class Main {
    static String fileName = "airports.csv";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите фильтр: ");
        String filter = scanner.nextLine();

        // фильтруем и сортируем список
        List<Airport> airports = Filtration.filterAndSort(filter, fileName);

        System.out.println("Введите начало названия аэропорта:");
        String beginAirportName = scn.nextLine();

        search(airports, beginAirportName);
    }

    private static void search(List<Airport> airports, String beginAirportName) {

    }
}
