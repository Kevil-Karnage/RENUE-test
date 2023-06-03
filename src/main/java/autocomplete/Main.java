package autocomplete;

import autocomplete.csv.CSVModel;
import autocomplete.filterTree.Filtration;
import autocomplete.filterTree.exceptions.FiltrationException;

import java.util.*;

import static autocomplete.search.Searching.searching;

public class Main {
    static String fileName = "airports.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<CSVModel> airports = new ArrayList<>();
        while (true) {
            System.out.println("Введите фильтр: ");
            String filter = scanner.nextLine();

            try {
                // фильтруем и сортируем список
                airports = Filtration.getAndSortAirportsFromFile(filter, fileName);
                break;
            } catch (FiltrationException e) {
                System.out.println(e);
            }
        }
        // запускаем поиск
        searching(airports, scanner);
    }


}
