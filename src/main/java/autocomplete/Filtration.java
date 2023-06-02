package autocomplete;

import java.util.*;

public class Filtration {

    /**
     * получение отфильтрованных и отсортированных строк файла
     * @param fString фильтр в строковом формате
     * @param fileName название файла для чтения моделей
     * @return Список прочитанных и отсортированных моделей из файла
     * @throws FiltrationException при некорректном фильтре
     */
    public static List<CSVModel> getAndSortAirportsFromFile(String fString, String fileName) throws FiltrationException {
        FilterTree filters = new FilterTree().buildFromString(fString);

        List<CSVModel> airports = CSVReader.readFromFileAndFilter(fileName, filters);
        airports.sort(new Comparator<CSVModel>() {
            @Override
            public int compare(CSVModel o1, CSVModel o2) {
                String s1 = o1.getName().toLowerCase(Locale.ROOT).replaceAll("\"", "");
                String s2 = o2.getName().toLowerCase(Locale.ROOT).replaceAll("\"", "");

                for (int i = 0; i < Integer.min(s1.length(), s2.length()); i++) {
                    char char1 = s1.charAt(i);
                    char char2 = s2.charAt(i);

                    if (char1 != char2)
                        return (int) char1 - (int) char2;
                }

                return 0;
            }
        });
        return airports;
    }


    /**
     * перевод фильтра из String в Filter
     * @param fString фильтр в строковом формате
     * @return объект класса Filter
     * @throws FiltrationException
     */
    public static Filter parseFilter(String fString) throws FiltrationException {
        if (fString.equals(""))
            return new Filter();

        fString = fString.replaceAll(" ", "");
        if (fString.length() < 11 && fString.length() != 0) {
            throw new FiltrationException("Некорректный фильтр");
        }

        int column;
        try {
            String columnString = fString.split("[\\[\\]]")[1];
            column = Integer.parseInt("" + columnString) - 1;
        } catch (NumberFormatException e) {
            throw new FiltrationException("Некорректный фильтр");
        }
        FilterAction action;
        int actionIndex;
        if (fString.contains("<>")) {
            action = FilterAction.NOT_EQUALS;
            actionIndex = fString.indexOf(">");

        } else if (fString.contains("<")) {
            action = FilterAction.LESS;
            actionIndex = fString.indexOf("<");

        } else if (fString.contains(">")) {
            action = FilterAction.OVER;
            actionIndex = fString.indexOf(">");

        } else if (fString.contains("=")) {
            action = FilterAction.EQUALS;
            actionIndex = fString.indexOf("=");

        } else {
            throw new FiltrationException("Некорректный фильтр: не найдено действие");
        }

        String value = String.copyValueOf(fString.toCharArray(),
                actionIndex + 1, fString.length() - actionIndex - 1);
        value = value.replaceAll("['’]", "\"");
        return new Filter(column, action, value);
    }
}

class FiltrationException extends Exception {
    public FiltrationException(String message) {
        super(message);
    }
}