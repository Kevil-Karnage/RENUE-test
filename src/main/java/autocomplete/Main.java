package autocomplete;

import java.util.*;

import static autocomplete.Searching.searching;

public class Main {
    static String fileName = "airports.csv";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите фильтр: ");
        String filter = scanner.nextLine();

        // фильтруем и сортируем список
        List<CSVModel> airports = Filtration.getAndSortAirportsFromFile(filter, fileName);
        // запускаем поиск
        searching(airports, scanner);
    }


}
