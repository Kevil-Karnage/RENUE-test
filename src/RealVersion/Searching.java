package RealVersion;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Searching {

    public static void searching(List<Airport> airports, Scanner scn) {
        while (true) {
            System.out.println("Введите начало названия аэропорта:");
            String beginAirportName = scn.nextLine();

            if (beginAirportName.equals("!quit"))
                break;

            search(airports, beginAirportName);
        }
    }

    private static void search(List<Airport> airports, String name) {
        Date begin = new Date();
        int firstIndex = searchFirst(airports, name);
        int lastIndex = searchLast(airports, name);
        Date end = new Date();
        for (int i = firstIndex; i <= lastIndex; i++) {
            System.out.println(airports.get(i).toString());
        }

        System.out.printf("Количество найденных строк: %d; Время, затраченное на поиск: %d мс\n",
                lastIndex - firstIndex + 1, (end.getTime() - begin.getTime()));
    }

    private static int searchFirst(List<Airport> airports, String name) {
        return binarySearch(airports, name, true);
    }

    private static int searchLast(List<Airport> airports, String name) {
        return binarySearch(airports, name, false);
    }

    private static int binarySearch(List<Airport> list, String valueToFind, boolean isFirst) {
        int index = -1;

        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int compare = stringCompareTo(list.get(mid).name, valueToFind);
            if (compare > 0) {
                high = mid - 1;
            } else if (compare < 0) {
                low = mid + 1;
            } else {
                if (isFirst) {
                    if (stringCompareTo(list.get(mid - 1).name, valueToFind) < 0) {
                        index = mid;
                        break;
                    } else {
                        high = mid - 1;
                    }
                } else {
                    if (stringCompareTo(list.get(mid + 1).name, valueToFind) > 0) {
                        index = mid;
                        break;
                    } else {
                        low = mid - 1;
                    }
                }
            }
        }
        return index;
    }

    private static int stringCompareTo(String string1, String string2) {
        if (string1.length() < string2.length())
            return -1;

        String s1 = string1.toLowerCase(Locale.ROOT).replaceAll("\"", "");
        String s2 = string2.toLowerCase(Locale.ROOT).replaceAll("\"", "");

        for (int i = 0; i < s2.length(); i++) {
            char char1 = s1.charAt(i);
            char char2 = s2.charAt(i);

            if (char1 != char2)
                return (int) char1 - (int) char2;
        }

        return 0;
    }
}
