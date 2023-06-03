package autocomplete.csv;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

/**
    Базовый класс хранения csv-модели
 */
@AllArgsConstructor
@Getter
public class CSVModel {
    private final int column; // номер столбца с важной информацией
    private final String[] data; // список информации об объекте

    @Override
    public String toString() {
        return data[column] + Arrays.toString(data);
    }

    public String getName() {
        return data[column];
    }
    /**
     * сравнение названия модели со строкой
     * @param string входная строка
     * @return compare
     */
    public int compareTo(String string) {
        if (this.data[column].length() < string.length())
            return -1;

        String s1 = this.data[column].toLowerCase(Locale.ROOT).replaceAll("\"", "");
        String s2 = string.toLowerCase(Locale.ROOT).replaceAll("\"", "");

        for (int i = 0; i < s2.length(); i++) {
            char char1 = s1.charAt(i);
            char char2 = s2.charAt(i);

            if (char1 != char2)
                return (int) char1 - (int) char2;
        }

        return 0;

    }
}
