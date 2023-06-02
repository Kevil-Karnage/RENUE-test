package autocomplete;

import java.util.*;

import static autocomplete.Searching.searching;

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
            } catch (FiltrationException ignored) {
                System.out.println(ignored);
            }
        }
        // запускаем поиск
        searching(airports, scanner);
    }


}
