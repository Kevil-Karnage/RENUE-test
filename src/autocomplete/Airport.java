package autocomplete;

import java.util.Arrays;
import java.util.Locale;

/**
 * Класс хранения строки (аэропорта)
 */
public class Airport implements Comparable<Airport>{
    String name;
    String[] data;

    public Airport(String name, String[] data) {
        this.name = name;
        this.data = data;
    }

    /**
     * сравнение объектов класса Airport по названию аэропорта
     * @param a the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Airport a) {
        return compareTo(a.name);
    }

    /**
     * сравнение названия аэропорта со строкой
     * @param s
     * @return
     */
    public int compareTo(String s) {
        if (this.name.length() < s.length())
            return -1;

        String s1 = this.name.toLowerCase(Locale.ROOT).replaceAll("\"", "");
        String s2 = s.toLowerCase(Locale.ROOT).replaceAll("\"", "");

        for (int i = 0; i < s2.length(); i++) {
            char char1 = s1.charAt(i);
            char char2 = s2.charAt(i);

            if (char1 != char2)
                return (int) char1 - (int) char2;
        }

        return 0;

    }


    @Override
    public String toString() {
        return name + Arrays.toString(data);
    }
}
