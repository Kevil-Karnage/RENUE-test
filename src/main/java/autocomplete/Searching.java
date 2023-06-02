package autocomplete;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Searching {

    /**
     * Поиск в списке аэропортов по началу названия
     * @param airports
     * @param scn
     */
    public static void searching(List<CSVModel> airports, Scanner scn) {
        while (true) {
            System.out.println("Введите начало названия аэропорта:");
            String beginAirportName = scn.nextLine();

            if (beginAirportName.equals("!quit"))
                break;

            search(airports, beginAirportName);
        }
    }

    /**
     * метод поиска подходящих аэропортов (строк)
     * @param airports список аэропортов (строк)
     * @param name начало названия аэропорта (второй колонки строки)
     */
    private static void search(List<CSVModel> airports, String name) {
        Date begin = new Date();
        int firstIndex = searchFirst(airports, name);
        int lastIndex = searchLast(airports, name);
        Date end = new Date();

        int count = firstIndex == -1 || lastIndex == -1 ? 0: lastIndex - firstIndex + 1;

        for (int i = firstIndex; i <= lastIndex; i++) {
            System.out.println(airports.get(i).toString());
        }

        System.out.printf("Количество найденных строк: %d; Время, затраченное на поиск: %d мс\n",
                count, (end.getTime() - begin.getTime()));
    }

    /**
     * поиск первого подходящего элемента (для читабельности кода)
     * @param airports
     * @param name
     * @return
     */
    private static int searchFirst(List<CSVModel> airports, String name) {
        return binarySearch(airports, name, true);
    }

    /**
     * поиск последнего подходящего элемента (для читабельности кода)
     * @param airports
     * @param name
     * @return
     */
    private static int searchLast(List<CSVModel> airports, String name) {
        return binarySearch(airports, name, false);
    }

    /**
     * Модифицированный бинарный поиск
     * @param list список аэропортов
     * @param valueToFind искомое значение
     * @param isFirst поиск первого/последнего подходящего (first = true, second = false)
     * @return индекс найденного значения
     */
    private static int binarySearch(List<CSVModel> list, String valueToFind, boolean isFirst) {
        int index = -1;

        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int compare = list.get(mid).compareTo(valueToFind);
            if (compare > 0) {
                high = mid;

            } else if (compare < 0) {
                low = mid;

            } else {
                // для поиска первого ищем такой элемент, чтобы ПЕРЕД НИМ шёл неподходящий
                if (isFirst) {
                    if (list.get(mid - 1).compareTo(valueToFind) < 0) {
                        index = mid;
                        break;

                    } else
                        high = mid;

                } else {
                    // для поиска последнего - такой, чтобы ПОСЛЕ НЕГО шел неподходящий
                    if (list.get(mid + 1).compareTo(valueToFind) > 0) {
                        index = mid;
                        break;

                    } else
                        low = mid;

                }
            }
        }
        return index;
    }
}
